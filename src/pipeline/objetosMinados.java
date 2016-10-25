/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import com.db4o.collections.ActivatableArrayList;
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

    public void crear_archivo() {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter("objetosMinados.txt");
        } catch (IOException ex) {
            Logger.getLogger(Minado_FTs.class.getName()).log(Level.SEVERE, null, ex);
        }
        pw = new PrintWriter(fichero);
    }

    public void agregar_objetos(Factor_Transcripcion2 FT) {

        String cadena = "";
        if (!revisar_en_archivo(FT.getID())) {
            ArrayList<String> listaObj = new ArrayList<>();
            if (FT.getLecturas_HGNC().getHGNC().size() > 0 && FT.getLecturas_HGNC().getHGNC().get(0).getSimbolo().equals(FT.getID())) {
                agregar_a_lista(listaObj, FT.getID());
                agregar_a_lista(listaObj, FT.getLecturas_HGNC().getHGNC().get(0).getNombre());

                for (int i = 0; i < FT.getLecturas_HGNC().getHGNC().get(0).getSinonimos().size(); i++) {
                    agregar_a_lista(listaObj, FT.getLecturas_HGNC().getHGNC().get(0).getSinonimos().get(i));

                }
                escribir_en_archivo(construirCadena(listaObj));

            } else {
                escribir_en_archivo(FT.getID());
                for (int i = 0; i < FT.getLecturas_HGNC().getHGNC().size(); i++) {
                    listaObj = new ArrayList<>();
                    if (!revisar_en_archivo(FT.getLecturas_HGNC().getHGNC().get(i).getSimbolo())) {
                        agregar_a_lista(listaObj, FT.getLecturas_HGNC().getHGNC().get(i).getSimbolo());
                        agregar_a_lista(listaObj, FT.getLecturas_HGNC().getHGNC().get(i).getNombre());

                        for (int j = 0; j < FT.getLecturas_HGNC().getHGNC().get(i).getSinonimos().size(); j++) {
                            agregar_a_lista(listaObj, FT.getLecturas_HGNC().getHGNC().get(i).getSinonimos().get(j));
                        }
                        escribir_en_archivo(construirCadena(listaObj));
                    }
                }
            }
        }

        for (int i = 0; i < FT.getComplejoProteinico().size(); i++) {
            for (int j = 0; j < FT.getComplejoProteinico().get(i).getHGNC().size(); j++) {
                agregar_objetos(FT.getComplejoProteinico().get(i).getHGNC().get(j));
                for (int k = 0; k < FT.getComplejoProteinico().get(i).getLigandos().size(); k++) {
                    if (!revisar_en_archivo(FT.getComplejoProteinico().get(i).getLigandos().get(k).getId())) {
                        String Cadena = FT.getComplejoProteinico().get(i).getLigandos().get(k).getId()+";"+FT.getComplejoProteinico().get(i).getLigandos().get(k).getNombre();
                        escribir_en_archivo(Cadena);
                    }
                }
            }
        }
    }

    public void agregar_objetos(lecturas_HGNC HGNC) {

        if (!revisar_en_archivo(HGNC.getID())) {

            if (HGNC.getHGNC().size() > 0 && HGNC.getID().equals(HGNC.getHGNC().get(0).getSimbolo())) {
                ArrayList<String> listaObj = new ArrayList<>();
                agregar_a_lista(listaObj, HGNC.getID());
                agregar_a_lista(listaObj, HGNC.getHGNC().get(0).getNombre());

                for (int i = 0; i < HGNC.getHGNC().get(0).getSinonimos().size(); i++) {
                    agregar_a_lista(listaObj, HGNC.getHGNC().get(0).getSinonimos().get(i));
                }
                escribir_en_archivo(construirCadena(listaObj));
            } else {
                escribir_en_archivo(HGNC.getID());
                for (int i = 0; i < HGNC.getHGNC().size(); i++) {
                    if (!revisar_en_archivo(HGNC.getHGNC().get(i).getSimbolo())) {
                        ArrayList<String> listaObj = new ArrayList<>();
                        agregar_a_lista(listaObj, HGNC.getHGNC().get(i).getSimbolo());
                        agregar_a_lista(listaObj, HGNC.getHGNC().get(i).getNombre());
                        for (int j = 0; j < HGNC.getHGNC().get(i).getSinonimos().size(); j++) {
                            agregar_a_lista(listaObj,HGNC.getHGNC().get(i).getSinonimos().get(j) );
                        }
                        escribir_en_archivo(construirCadena(listaObj));
                    }

                }
            }

        }

    }

    private boolean revisar_en_archivo(String objeto) {

        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
            archivo = new File("objetosMinados.txt");
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String linea;

            while ((linea = br.readLine()) != null) {

                String[] separa = linea.split(";");
                if (separa[0].equals(objeto)) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    private void escribir_en_archivo(String cadena) {

        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter("objetosMinados.txt", true);
            pw = new PrintWriter(fichero);

            pw.println(cadena);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
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
