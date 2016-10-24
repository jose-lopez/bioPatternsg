/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

            if (FT.getLecturas_HGNC().getHGNC().size() > 0 && FT.getLecturas_HGNC().getHGNC().get(0).getSimbolo().equals(FT.getID())) {
                cadena = FT.getID() + ";" + FT.getLecturas_HGNC().getHGNC().get(0).getNombre();
                for (int i = 0; i < FT.getLecturas_HGNC().getHGNC().get(0).getSinonimos().size(); i++) {
                    cadena = cadena + ";" + FT.getLecturas_HGNC().getHGNC().get(0).getSinonimos().get(i);
                }
                escribir_en_archivo(cadena);

            } else {
                escribir_en_archivo(FT.getID());
                for (int i = 0; i < FT.getLecturas_HGNC().getHGNC().size(); i++) {

                    if (!revisar_en_archivo(FT.getLecturas_HGNC().getHGNC().get(i).getSimbolo())) {

                        cadena = FT.getLecturas_HGNC().getHGNC().get(i).getSimbolo() + ";";
                        cadena = FT.getLecturas_HGNC().getHGNC().get(i).getNombre();

                        for (int j = 0; j < FT.getLecturas_HGNC().getHGNC().get(i).getSinonimos().size(); j++) {
                            cadena = ";" + cadena + FT.getLecturas_HGNC().getHGNC().get(i).getSinonimos().get(j);
                        }
                        escribir_en_archivo(cadena);
                    }
                    //cadena = 

                }
            }
        }

        for (int i = 0; i < FT.getComplejoProteinico().size(); i++) {
            for (int j = 0; j < FT.getComplejoProteinico().get(i).getHGNC().size(); j++) {
                agregar_objetos(FT.getComplejoProteinico().get(i).getHGNC().get(j));
            }
        }
    }
   
    public void agregar_objetos(lecturas_HGNC HGNC) {
        String cadena = "";

        if (!revisar_en_archivo(HGNC.getID())) {

            if (HGNC.getID().equals(HGNC.getHGNC().get(0).getSimbolo())) {
                cadena = HGNC.getID();
                cadena = cadena + ";" + HGNC.getHGNC().get(0).getNombre();
                for (int i = 0; i < HGNC.getHGNC().get(0).getSinonimos().size(); i++) {
                    cadena = cadena + ";" + HGNC.getHGNC().get(0).getSinonimos().get(i);
                }
                escribir_en_archivo(cadena);
            } else {
                escribir_en_archivo(HGNC.getID());
                for (int i = 0; i < HGNC.getHGNC().size(); i++) {
                    if (!revisar_en_archivo(HGNC.getHGNC().get(i).getSimbolo())) {
                        cadena = HGNC.getHGNC().get(i).getSimbolo();
                        cadena = cadena + ";" + HGNC.getHGNC().get(i).getSimbolo();
                        for (int j = 0; j < HGNC.getHGNC().get(i).getSinonimos().size(); j++) {
                            cadena = cadena + ";" + HGNC.getHGNC().get(i).getSinonimos().get(j);
                        }
                        escribir_en_archivo(cadena);
                    }

                }
            }

        }

    }

    private boolean revisar_en_archivo(String nombre) {

        return false;
    }

    private void escribir_en_archivo(String cadena) {

    }

}
