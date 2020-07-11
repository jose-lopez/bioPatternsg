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

import configuracion.configuracion;
import configuracion.utilidades;
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

    public void resumidor(configuracion config,String ruta) {

        String resumidorCodigo = "resumidorcompleto";// Comando para cargar el consultResumidor        
        
        try {

            init(resumidorCodigo); // se carga codigo del consultResumidor

        } catch (Throwable t) {

            t.printStackTrace();
        }

        int n = config.getResumenes();
        String salida = "", entrada = "";

        while (buscarAbstractHtml(n,ruta)) {
            //metodo que llama al consultResumidor
            salida = "salida_" + n + ".html";
            entrada = "abstracts_" + n + ".html";
            utilidades.texto_carga="";
            utilidades.texto_etapa=utilidades.idioma.get(145);
            utilidades.momento=utilidades.idioma.get(149)+": " + entrada;
            new utilidades().carga();

            resumir(entrada, n, salida,ruta);
            //recibe la ruta del resumen y genera un archivo txt
            //en la carpeta abstracts llamado resumen_(n).txt 
            salida = ruta+"/abstracts/salida_" + n + ".html";
            try {
                generarResumenTXT(n, salida,ruta);
            } catch (Exception e) {
            }
           
            n++;
                           
        }
        config.setGenerarResumenes(true);
        config.guardar(ruta);
    }

    private boolean buscarAbstractHtml(int n,String ruta) {
        boolean encontrado = true;
        File archivo = null;

        String nombre_archivo = ruta+"/abstracts/abstracts_" + n + ".html";
        try {
            archivo = new File(nombre_archivo);

            encontrado = archivo.exists();

        } catch (Exception e) {
            encontrado = false;
        }
        return encontrado;
    }

    public void generarResumenTXT(int n, String fuente,String ruta) throws Exception {
        String nombre_archivo = ruta+"/abstracts/resumen_" + n + ".txt";

        File archivo_fuente = new File(fuente);
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

           
        } else {

            System.out.println(utilidades.idioma.get(146));

        }

        //archivo_fuente.delete();
    }

    public void resumir(String abstracts, int n, String salida,String ruta) { // El archivo de abstracts debe venir en formato HTML
        String v = "style_check(-discontiguous).";
        Query q0 = new Query(v);
        q0.hasSolution();
        // Comandon para realizar el resumen
        String resumirComando = "tell('" + salida + "')" + ", resume('" + abstracts + "'), told.";
        //String resumirComando = "tell('salida_1_p.html'), resume('abstracts_1.html'), told.";
        //System.out.println("cd(\""+ruta+"/abstracts\").");
        Query q = new Query("cd(\""+ruta+"/abstracts\").");
        q.hasSolution();
        //System.out.println("cambio a directorio abstracts:" + " " + (q.hasSolution() ? "succeeded" : "failed"));

        try {
            File ficherod = new File(salida);
            ficherod.delete();
        } catch (Exception e) {
        }

        try {

            //q = new Query("open('salida.html', write, Stream), flush_output(Stream), close(Stream).");
            //System.out.println("cambio a directorio resumidor_bioinformante:" + " " + (q.hasMoreElements() ? "succeeded" : "failed"));
          //  System.out.println(resumirComando);
            generarResumen(resumirComando); // se realiza resumen del archivo de abstracts

            q = new Query("cd(../../../../..).");
            q.hasSolution();
            //System.out.println("regresando a directorio raiz:" + " " + (q.hasSolution() ? "succeeded" : "failed"));

        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    public void consultResumidor(String codigoResumidorPNL) {

        String consultResumidor = "[" + codigoResumidorPNL + "].";
        Query query = new Query(consultResumidor);
        query.hasSolution();
        //System.out.println(consultResumidor + " " + (query.hasSolution() ? "succeeded" : "failed"));
    }

//---------------------------------------
    public boolean generarResumen(String resumirComando) {

        boolean hasSolution = false;

        Query q = new Query(resumirComando);
        hasSolution = q.hasSolution();
        //hasSolution;
       // System.out.println("Resumir: " + (hasSolution ? "succeeded" : "failed"));

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
        q.hasSolution();
        //System.out.println("cambio de directorio:" + " " + (q.hasSolution() ? "succeeded" : "failed"));

        String consultResumidor = "[" + codigoResumidorPNL + "].";
        Query query = new Query(consultResumidor);
        query.hasSolution();
        //System.out.println(consultResumidor + " " + (query.hasSolution() ? "succeeded" : "failed"));

        q = new Query("cd(..).");
        q.hasSolution();
        //System.out.println("cambio de directorio:" + " " + (q.hasSolution() ? "succeeded" : "failed"));

    }
}
