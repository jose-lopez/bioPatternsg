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
package estructura;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import configuracion.utilidades;
import java.util.ArrayList;
import pipeline.escribirBC;
import pipeline.objetosMinados;

/**
 *
 * @author yacson
 */
public class objetos_Experto {

    private String ID;
    private ArrayList<HGNC> HGNC;

    public objetos_Experto() {
        this.HGNC = new ArrayList<>();
    }

    public ArrayList<String> listaNombres() {
        ArrayList<String> lista = new ArrayList<>();
        lista.add(ID);
        HGNC.forEach(hgnc -> lista.addAll(hgnc.ListaNombres()));

        return lista;
    }

    public void vaciar_pl(String ruta) {
        boolean encontrado = false;
        objetosMinados objMin = new objetosMinados();
        for (HGNC hgnc : HGNC) {
            String cadena_txt = "";
            String cadena = "[";
            cadena += "\'" + hgnc.getSimbolo().replace("\'", "") + "\',";
            cadena_txt += objMin.procesarNombre(hgnc.getSimbolo()) + ";";
            cadena += "\'" + hgnc.getNombre().replace("\'", "") + "\'";
            cadena_txt += objMin.procesarNombre(hgnc.getNombre());
            for (String sinonimo : hgnc.getSinonimos()) {
                cadena += ",\'" + sinonimo.replace("\'", "") + "\'";
                cadena_txt += ";" + objMin.procesarNombre(sinonimo);
            }

            cadena += "]";
            //System.out.println("Experto: "+cadena);
            new escribirBC("sinonimos(\'" + hgnc.getSimbolo().replace("\'", "") + "\'," + cadena + ").", ruta + "/minedObjects.pl");
            new escribirBC(cadena_txt, ruta + "/minedObjects.txt");
            new utilidades().carga();
            if (hgnc.getTejidos().size() > 0) {
                String cadTej ="[\'"+hgnc.getTejidos().get(0)+"\'";
                for (int i = 1; i < hgnc.getTejidos().size(); i++) {
                    cadTej+=",\'"+hgnc.getTejidos().get(0)+"\'";
                }
                    cadTej+="]";
                new escribirBC("tejidos(\'" + hgnc.getSimbolo().replace("\'", "") + "\'," + cadTej + ").", ruta + "/minedObjects.pl");
                new utilidades().carga();
            }
            
            

            ArrayList<String> lista = hgnc.ListaNombres();
            if (lista.contains(ID)) {
                encontrado = true;
            }
        }

        if (!encontrado) {
            new escribirBC("sinonimos(\'" + ID + "\',[\'" + ID + "\']).", ruta + "/minedObjects.pl");
            String cadena_txt = ID + ";" + objMin.procesarNombre(ID);
            new escribirBC(cadena_txt, ruta + "/minedObjects.txt");
            new utilidades().carga();
        }

    }

    public boolean buscar(objetos_Experto objeto, String ruta) {

        boolean encontrado = false;
        try {
            ObjectContainer db = Db4o.openFile(ruta + "/homologousObjects.db");
            try {

                ObjectSet result = db.queryByExample(objeto);
                if (result.hasNext()) {
                    encontrado = true;
                }
            } catch (Exception e) {
                System.out.println("Error al acceder a minedObjectsOntology.db");
            } finally {
                db.close();
            }
        } catch (Exception e) {

        }
        return encontrado;

    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public ArrayList<HGNC> getHGNC() {
        return HGNC;
    }

    public void setHGNC(ArrayList<HGNC> HGNC) {
        this.HGNC = HGNC;
    }
}
