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
import java.util.logging.Level;
import java.util.logging.Logger;

//import static pipeline.ClustalWAlign.clustalwPath;
/**
 *
 * @author jose
 */
public class Resumidor {

    public static String currentPath = null;

    String ruta_resumen;

    /*static {
        ClassLoader classLoader;
        classLoader = Resumidor.class.getClassLoader();
        currentPath = classLoader.getResource("").getPath();
    }*/
    public String resumidor(String abstracts) {

        int exitVal;

        try {

            //System.out.println("Current Path " + currentPath);
            System.out.println("FileName " + abstracts);

            ruta_resumen = "resumen_" + abstracts;

            /*Runtime rt = Runtime.getRuntime();

            String[] strComando = {ruta_resumen};

            for (int i = 0; i < strComando.length; i++) {
                System.out.println(strComando[i]);
            }

            Process proc = rt.exec(strComando);

            InputStream stdin = proc.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(stdin));

            while ((br.readLine()) != null) {
                // do nothing only read "stdout" from ClustalW
                // you can put a System.out.print here to prints
                // the output from ClustalW to console.
                System.out.print(br);
            }

            exitVal = proc.waitFor();

            if (exitVal == 0) {

                // Puedo abrir un filereader...
            }
             */
            //System.out.println(""+dbSequences);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return ruta_resumen;
    }

    public void resumidor() throws Exception {
        int n = 1;
        while (generar_html(n)) {
            /*Se genera un archivo html en la carpeta resumidor_bioinformante 
             llamado abstracts.html
            */
            
            //metodo que llama al resumidor
            
            
            //recibe la ruta del resumen y genera un archivo txt
            //en la carpeta abstracts llamado resumen_(n).txt 
            generar_salidaTXT(n,"resumidor_bioinformante/salida.html");
            n++;
        }

    }

    public boolean generar_html(int n) throws Exception {
        boolean encontrado = true;

        String nombre_archivo = "resumidor_bioinformante/abstracts.html";

        File archivo_fuente = new File("abstracts/abstracts_" + n);
        File archivo_destino = new File(nombre_archivo);

        BufferedWriter escribir;
        BufferedReader leer;

        String linea = "";
        String cabecera = "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "	<meta charset=\"utf-8\">\n"
                + "	<title>generación de archivo html</title>\n"
                + "</head>\n"
                + "<body>\n";

        String pie = "\n</body>\n"
                + "</html>";

        try {
            leer = new BufferedReader(new FileReader(archivo_fuente));

            escribir = new BufferedWriter(new FileWriter(archivo_destino));

            if (archivo_fuente.exists()) {

                escribir.write(cabecera);

                while ((linea = leer.readLine()) != null) {

                    linea = linea.replaceAll("&", "&amp;");
                    linea = linea.replaceAll("<", "&lt;");
                    linea = linea.replaceAll(">", "&gt;");

                    escribir.write(linea);

                }

                escribir.write(pie);

                leer.close();

                escribir.close();

                System.out.println("..ok");
            } else {

                System.out.println("archivo no localizado");
                encontrado = false;
            }
        } catch (Exception e) {
            encontrado = false;
        }

        return encontrado;
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

    
    /*
     public String resumidor(String abstracts) {


     // Se pasan los abstracts al resumidor, basado en prolog, y se deveulve un archivo
     // llamado oracionesSVC, que contendra las oracones SVC para ser usado en la generacion
     //  de la base de conocimiento para hacer inferencias.

     String resumen = "entradas.txt";

     return resumen;


     }*/
}
