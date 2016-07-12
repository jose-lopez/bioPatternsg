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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    static {
        ClassLoader classLoader;
        classLoader = Resumidor.class.getClassLoader();
        currentPath = classLoader.getResource("").getPath();
    }

    public String resumidor(String abstracts) {

        int exitVal;

        try {

            System.out.println("Current Path " + currentPath);

            System.out.println("FileName " + abstracts);

            ruta_resumen = currentPath + abstracts;

            Runtime rt = Runtime.getRuntime();

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

            //System.out.println(""+dbSequences);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return ruta_resumen;
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
