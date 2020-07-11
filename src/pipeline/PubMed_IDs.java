/* 
 * bioPatternsg
 * BioPatternsg is a system that allows the integration and analysis of information related to the modeling of Gene Regulatory Networks (GRN).
 * Copyright (C) 2020
 * Jose Lopez (josesmooth@gmail.com), Jacinto Dávila (jacinto.davila@gmail.com), Yacson Ramirez (yacson.ramirez@gmail.com).
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package pipeline;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import configuracion.PMIDS;
import configuracion.combinacion;
import configuracion.configuracion;
import configuracion.utilidades;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import servicios.lecturas_PM;

/**
 *
 * @author yacson
 */
public class PubMed_IDs {

    private int combinaciones;
    private int probadas = 0;

    //busca los PubMed IDs de cada combinacion encontrada en el archivo mineria/combinations.db
    public void buscar(int cantIDs, configuracion config, String ruta) {
        new utilidades().limpiarPantalla();
        System.out.println(utilidades.colorTexto1+utilidades.titulo);
        System.out.println(utilidades.colorTexto1+utilidades.proceso);
        System.out.println("\n"+utilidades.colorTexto2+utilidades.idioma.get(82));

        ObjectContainer db = Db4o.openFile(ruta + "/combinations.db");
        combinacion com = new combinacion();
        ObjectSet result = db.queryByExample(com);
        combinacion combinacion = (combinacion) result.get(0);
        db.close();

        combinaciones = combinacion.combinaciones.size();

        ArrayList<String> pubmedIDs = new ArrayList<>();
        if (!config.getRutaPMID_experto().equals("")) {
            try {
                pubmedIDs.addAll(pubmedIDExperto(config.getRutaPMID_experto()));
            } catch (Exception e) {
            }
        }

        combinacion.combinaciones.parallelStream().forEach((comb) -> {
            //if (probadas < 500) {
            try {

                //consulta cada combinacion retorna una lista de pubmed IDs
                ArrayList<String> lista = new lecturas_PM().busquedaPM_ID(comb, cantIDs);

                //se inserta la lista encontrada en cada combiancion en la lista general de IDs
                insertar_en_lista(pubmedIDs, lista);
            } catch (Exception e) {
                //System.out.println("error en busqueda");
            }
            //}

        });

        //se guarda el listado de IDs en la base de datos
        guardar(pubmedIDs, ruta);

        //se giarda el checklist que indica que el proceso de busqueda de PubMed Ids a terminado
        config.setPubmedids(true);
        config.guardar(ruta);
        System.out.println("ok");
    }

    public ArrayList<String> pubmedIDExperto(String ruta) {
        ArrayList<String> lista = new ArrayList<>();

        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
            archivo = new File(ruta);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String linea;
            while ((linea = br.readLine()) != null) {
                lista.add(linea);
            }
        } catch (Exception e) {
        }

        return lista;
    }

    //agrega la lista de IDs encontrada en cada busqueda y la agrega a una lista general
    //cada ID es filtrado de manera que no existan IDs repetidos
    private void insertar_en_lista(ArrayList<String> pubmedIDS, ArrayList<String> lista) {

        lista.parallelStream().forEach((id) -> {
            if (!pubmedIDS.contains(id)) {
                System.out.println(id);
                pubmedIDS.add(id);
            }
        });
        probadas++;
        new utilidades().limpiarPantalla();
        System.out.println(utilidades.colorTexto1+utilidades.titulo);
        System.out.println(utilidades.colorTexto1+utilidades.proceso);
        System.out.println("\n"+utilidades.colorTexto2+utilidades.idioma.get(82));
        System.out.println(utilidades.colorReset+utilidades.idioma.get(83)+" " + probadas + " / " + combinaciones);
    }

    //se guarda la lista de IDs en 'mineria/pubmedIDs.db'
    private void guardar(ArrayList<String> pubmedIDS, String ruta) {

        ObjectContainer db = Db4o.openFile(ruta + "/pubmedIDs.db");
        PMIDS ids = new PMIDS();
        ids.pubmed_ids.addAll(pubmedIDS);

        try {
            db.store(ids);

        } catch (Exception e) {
            System.out.println(utilidades.idioma.get(84));
        } finally {
            db.close();
        }

    }

    public void borrar_archivo(String ruta) {
        try {
            File ficherod = new File(ruta + "/pubmedIDs.db");
            ficherod.delete();
        } catch (Exception e) {

        }
    }

   

}
