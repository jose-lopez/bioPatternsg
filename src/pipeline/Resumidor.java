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

    public void resumidor() throws Exception {

        String resumidorCodigo = "resumidorcompleto";// Comando para cargar el consultResumidor

        try {

            init(resumidorCodigo); // se carga codigo del consultResumidor

        } catch (Throwable t) {

            t.printStackTrace();
        }

        int n = 1;
        String salida = "", entrada = "";
        while (generar_html(n)) {
            /*Se genera un archivo html en la carpeta resumidor_bioinformante 
             llamado abstracts.html             * 
             */

            System.out.println("Resumiendo abstracts_" + n + "................");
            //metodo que llama al consultResumidor

            salida = "salida_" + n + ".html";
            entrada = "abstracts_" + n + ".html";

            resumir(entrada, n, salida);



            //recibe la ruta del resumen y genera un archivo txt
            //en la carpeta abstracts llamado resumen_(n).txt 
            salida = "resumidor_bioinformante/salida_" + n + ".html";
            generar_salidaTXT(n, salida);


            n++;
        }
        
        
        
    }

    public boolean generar_html(int n) throws Exception {
        boolean encontrado = true;
        String nombre_archivo = "resumidor_bioinformante/abstracts_" + n + ".html";

        try {
            File ficherod = new File(nombre_archivo);
            ficherod.delete();
        } catch (Exception e) {
        }

        String cabecera = "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "	<meta charset=\"utf-8\">\n"
                + "	<title>generación de archivo html</title>\n"
                + "</head>\n"
                + "<body>\n";

        String pie = "\n</body>\n"
                + "</html>";

        guardar_en_archivo(cabecera, nombre_archivo);
        encontrado = leerArchivo(n, nombre_archivo);
        guardar_en_archivo(pie, nombre_archivo);

        return encontrado;
    }

    public boolean leerArchivo(int n, String nombre_archivo) {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        boolean encontrado = true;
        try {
            archivo = new File("abstracts/abstracts_" + n);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            String linea = "";
            while ((linea = br.readLine()) != null) {

                linea = linea.replaceAll("&", "&amp;");
                linea = linea.replaceAll("<", "&lt;");
                linea = linea.replaceAll(">", "&gt;");
                guardar_en_archivo(linea, nombre_archivo);

            }
        } catch (Exception e) {
            encontrado = false;
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        return encontrado;
    }

    private void guardar_en_archivo(String texto, String nombre) {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter(nombre, true);
            pw = new PrintWriter(fichero);
            pw.println(texto);
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

    public void generar_salidaTXT(int n, String ruta) throws Exception {
        String nombre_archivo = "abstracts/resumen_" + n + ".txt";

        File archivo_fuente = new File(ruta);
        File archivo_destino = new File(nombre_archivo);

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

    }

    public void resumir(String abstracts, int n, String salida) { // El archivo de abstracts debe venir en formato HTML

        // Comandon para realizar el resumen
        String resumirComando = "tell('" + salida + "')" + ", resume('" + abstracts + "'), told.";
        //String resumirComando = "tell('salida_1_p.html'), resume('abstracts_1.html'), told.";

        Query q = new Query("cd(resumidor_bioinformante).");
        System.out.println("cambio a directorio resumidor_bioinformante:" + " " + (q.hasSolution() ? "succeeded" : "failed"));


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
