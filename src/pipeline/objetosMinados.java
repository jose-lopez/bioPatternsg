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

import com.db4o.collections.ActivatableArrayList;
import estructura.HGNC;
import estructura.complejoProteinico;
import estructura.factorTranscripcion;
import estructura.objetos_Experto;
import estructura.ligando;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yacson-ramirez
 */
public class objetosMinados {

    public void crear_archivo(String ruta) {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter(ruta+"/minedObjects.txt");
        } catch (IOException ex) {
            Logger.getLogger(minado_FT.class.getName()).log(Level.SEVERE, null, ex);
        }
        pw = new PrintWriter(fichero);
    }

    public void agregar_objetos(factorTranscripcion FT) {

        String cadena = "";
        if (!revisar_en_archivo(FT.getID())) {

            ArrayList<String> listaObj = new ArrayList<>();
            if (FT.getHGNC().size() > 0 && FT.getHGNC().get(0).getSimbolo().equals(FT.getID())) {

                agregar_a_lista(listaObj, FT.getID());
                agregar_a_lista(listaObj, FT.getHGNC().get(0).getNombre());

                for (String sinonimo : FT.getHGNC().get(0).getSinonimos()) {
                    agregar_a_lista(listaObj, sinonimo);
                }

//                escribir_en_archivo(construirCadena(listaObj));
            } else {
                escribir_en_archivo(FT.getID());

                for (HGNC hgnc : FT.getHGNC()) {
                    listaObj = new ArrayList<>();
                    if (!revisar_en_archivo(hgnc.getSimbolo())) {
                        agregar_a_lista(listaObj, hgnc.getSimbolo());
                        agregar_a_lista(listaObj, hgnc.getNombre());

                        for (String sinonimo : hgnc.getSinonimos()) {
                            agregar_a_lista(listaObj, sinonimo);
                        }
                        //        escribir_en_archivo(construirCadena(listaObj));
                    }
                }
            }
        }

        for (complejoProteinico complejo : FT.getComplejoProteinico()) {
            for (HGNC hgnc : complejo.getHGNC()) {
                agregar_objetos(hgnc);
                for (ligando ligando : complejo.getLigandos()) {
                    if (!revisar_en_archivo(ligando.getId())) {
                        String Cadena = ligando.getId() + ";" + ligando.getNombre();
                        //          escribir_en_archivo(Cadena);
                    }
                }
            }
        }
    }

    public void agregar_objetos(HGNC HGNC) {

        if (!revisar_en_archivo(HGNC.getSimbolo())) {
            ArrayList<String> listaObj = new ArrayList<>();
            agregar_a_lista(listaObj, HGNC.getSimbolo());
            agregar_a_lista(listaObj, HGNC.getNombre());

            HGNC.getSinonimos().forEach(obj -> agregar_a_lista(listaObj, obj));

            //escribir_en_archivo(construirCadena(listaObj));
        }
    }

    public void agregar_objetos(objetos_Experto objExp) {
        //revisa si el objetos ya extite en el archivo mineria/minedObjects.txt
        if (!revisar_en_archivo(objExp.getID())) {
            //si el primer objeto de HGNC es el mismo que el objeto de el experto se guarda como uno solo y se descartan los demas
            if (objExp.getHGNC().size() > 0 && objExp.getID().equals(objExp.getHGNC().get(0).getSimbolo())) {

                ArrayList<String> listaObj = new ArrayList<>();
                agregar_a_lista(listaObj, objExp.getID());
                agregar_a_lista(listaObj, objExp.getHGNC().get(0).getNombre());

                objExp.getHGNC().get(0).getSinonimos().forEach(sinonimo -> agregar_a_lista(listaObj, sinonimo));

                // escribir_en_archivo(construirCadena(listaObj));
            } else {
                //si el objeto del experto no es igual que los objetos de HGNC se guardan todos por separado
                //escribir_en_archivo(objExp.getID());

                objExp.getHGNC().forEach((obj) -> {

                    ArrayList<String> listaObj = new ArrayList<>();
                    agregar_a_lista(listaObj, obj.getSimbolo());
                    agregar_a_lista(listaObj, obj.getNombre());

                    obj.getSinonimos().forEach(obj2 -> agregar_a_lista(listaObj, obj2));

                    //      escribir_en_archivo(construirCadena(listaObj));
                });

            }

        }

    }
    
    public String procesarNombre(String nombre) {
        String cadena = nombre;
        cadena = cadena.replace(",", " ','");
        cadena = cadena.replace(" ", ", ");
        return cadena;
    }

    private boolean revisar_en_archivo(String objeto) {

//        File archivo = null;
//        FileReader fr = null;
//        BufferedReader br = null;
//
//        try {
//            archivo = new File("mineria/minedObjects.txt");
//            fr = new FileReader(archivo);
//            br = new BufferedReader(fr);
//            String linea;
//
//            while ((linea = br.readLine()) != null) {
//
//                String[] separa = linea.split(";");
//                if (separa[0].equals(objeto)) {
//                    return true;
//                }
//            }
//        } catch (Exception e) {
//        }
        return false;
    }

    private void escribir_en_archivo(String cadena) {
//        FileWriter fichero = null;
//        PrintWriter pw = null;
//        if (!cadena.equals("null")) {
//            try {
//                fichero = new FileWriter("mineria/minedObjects.txt", true);
//                pw = new PrintWriter(fichero);
//
//                pw.println(cadena);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    if (null != fichero) {
//                        fichero.close();
//                    }
//                } catch (Exception e2) {
//                    e2.printStackTrace();
//                }
//            }
//        }
    }

    private String construirCadena(ArrayList<String> listaObj) {
        String cadena = "";

        for (int i = 0; i < listaObj.size() - 1; i++) {
            cadena = cadena + listaObj.get(i) + ";";
        }
        cadena = cadena + listaObj.get(listaObj.size() - 1);
        return cadena;
    }

    private void agregar_a_lista(ArrayList<String> listaobj, String obj) {
        if (!listaobj.contains(obj)) {
            listaobj.add(obj);
        }
    }

}
