/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 *
 * @author yacson-ramirez
 */
public class escribirBC {
    
    public escribirBC(String cadena, String archivo){
        if(!revisar_en_archivo(cadena, archivo)){
            escribirArchivo(cadena, archivo);
        }
    }
    
    private boolean revisar_en_archivo(String objeto, String ruta) {

        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
            archivo = new File(ruta);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String linea;

            while ((linea = br.readLine()) != null) {

                if (linea.equals(objeto)) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }
    
     private void escribirArchivo(String cadena, String archivo) {

        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter(archivo, true);
            pw = new PrintWriter(fichero);
            //System.out.println(cadena);
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
}
