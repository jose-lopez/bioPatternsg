/*
 Resumidor.java


 Copyright (C) 2016.
 Jose Lopez (jlopez@unet.edu.ve).

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as
 published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>

 */

 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jpl7.Query;
import org.jpl7.Term;
import org.jpl7.Variable;

/**
 *
 * @author jose
 */
public class Resumidor {

    public void resumidor(configuracion config) {

        String resumidorCodigo = "resumidorcompleto";// Comando para cargar el consultResumidor        
        
        try {

            init(resumidorCodigo); // se carga codigo del consultResumidor

        } catch (Throwable t) {

            t.printStackTrace();
        }

        int n = config.getResumenes();
        String salida = "", entrada = "";

        while (buscarAbstractHtml(n)) {
            //metodo que llama al consultResumidor
            salida = "salida_" + n + ".html";
            entrada = "abstracts_" + n + ".html";
            System.out.println("Resumiendo abstracts_" + n + "................");

            resumir(entrada, n, salida);
            //recibe la ruta del resumen y genera un archivo txt
            //en la carpeta abstracts llamado resumen_(n).txt 
            salida = "abstracts/salida_" + n + ".html";
            try {
                generarResumenTXT(n, salida);
            } catch (Exception e) {
            }
            config.setResumenes(n);
            config.guardar();
            n++;
        }
        config.setGenerarResumenes(true);
        config.guardar();
    }

    private boolean buscarAbstractHtml(int n) {
        boolean encontrado = true;
        File archivo = null;

        String nombre_archivo = "abstracts/abstracts_" + n + ".html";

        try {
            archivo = new File(nombre_archivo);

            encontrado = archivo.exists();

        } catch (Exception e) {
            encontrado = false;
        }

        return encontrado;
    }

    public void generarResumenTXT(int n, String ruta) throws Exception {
        String nombre_archivo = "abstracts/resumen_" + n + ".txt";

        File archivo_fuente = new File(ruta);
        File archivo_destino = new File(nombre_archivo);

        //archivo_destino.delete();
        BufferedWriter escribir;
        BufferedReader leer;

        String linea = "";

        leer = new BufferedReader(new FileReader(archivo_fuente));

        escribir = new BufferedWriter(new FileWriter(archivo_destino));

        if (archivo_fuente.exists()) {

            while ((linea = leer.readLine()) != null) {

                try {
                    String pal = linea.substring(0, 8);

                    if (pal.equals("relacion")) {

                        linea = linea.replace("relacion(", "");
                        linea = linea.replace(").", ".");
                        escribir.write(linea);
                        escribir.newLine();
                    }
                } catch (Exception e) {
                }

            }

            leer.close();

            escribir.close();

            System.out.println("..ok");
        } else {

            System.out.println("archivo no localizado");

        }

        //archivo_fuente.delete();
    }

    public void resumir(String abstracts, int n, String salida) { // El archivo de abstracts debe venir en formato HTML

        // Comandon para realizar el resumen
        String resumirComando = "tell('" + salida + "')" + ", resume('" + abstracts + "'), told.";
        //String resumirComando = "tell('salida_1_p.html'), resume('abstracts_1.html'), told.";

        Query q = new Query("cd(abstracts).");
        System.out.println("cambio a directorio abstracts:" + " " + (q.hasSolution() ? "succeeded" : "failed"));

        try {
            File ficherod = new File(salida);
            ficherod.delete();
        } catch (Exception e) {
        }

        try {

            //q = new Query("open('salida.html', write, Stream), flush_output(Stream), close(Stream).");
            //System.out.println("cambio a directorio resumidor_bioinformante:" + " " + (q.hasMoreElements() ? "succeeded" : "failed"));
            generarResumen(resumirComando); // se realiza resumen del archivo de abstracts

            q = new Query("cd(..)");
            System.out.println("regresando a directorio raiz:" + " " + (q.hasSolution() ? "succeeded" : "failed"));

        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    public void consultResumidor(String codigoResumidorPNL) {

        String consultResumidor = "[" + codigoResumidorPNL + "].";
        Query query = new Query(consultResumidor);
        System.out.println(consultResumidor + " " + (query.hasSolution() ? "succeeded" : "failed"));
    }

//---------------------------------------
    public boolean generarResumen(String resumirComando) {

        boolean hasSolution = false;

        Query q = new Query(resumirComando);
        hasSolution = q.hasSolution();
        System.out.println("Resumir: " + (hasSolution ? "succeeded" : "failed"));

        //q = new Query("cd(..)");
        //System.out.println("cambio de directorio:" + " " + (q.hasSolution() ? "succeeded" : "failed"));
        return hasSolution;

    }

    //---------------------------------------
    /**
     * Se carga el codigo del resumidor PLN
     *
     * @param codigoResumidorPNL
     */
    public void init(String codigoResumidorPNL) {
        //Abriendo el archivo
        Query q = new Query("cd(resumidor_bioinformante).");
        System.out.println("cambio de directorio:" + " " + (q.hasSolution() ? "succeeded" : "failed"));

        String consultResumidor = "[" + codigoResumidorPNL + "].";
        Query query = new Query(consultResumidor);
        System.out.println(consultResumidor + " " + (query.hasSolution() ? "succeeded" : "failed"));

        q = new Query("cd(..).");
        System.out.println("cambio de directorio:" + " " + (q.hasSolution() ? "succeeded" : "failed"));

    }
}
