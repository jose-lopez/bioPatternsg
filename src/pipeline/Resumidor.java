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

    public String resumir(String abstracts) { // El archivo de abstracts debe venir en formato HTML

        String resumidorCodigo = "resumidorcompleto";// Comando para cargar el resumidor
        String[] base_name = abstracts.split("\\.");
        String resumen_salida = base_name[0] + "-salida.html";  // El archivo que contendra el resumen
        // Comandon para realizar el resumen
        String resumirComando = "tell('" + resumen_salida + "'), resume('" + abstracts + "'), told.";

        try {

            init(resumidorCodigo); // se carga codigo del resumidor

            generarResumen(resumirComando); // se realiza resumen del archivo de abstracts

        } catch (Throwable t) {
            t.printStackTrace();
        }

        return resumen_salida;

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
    }

//---------------------------------------
    public boolean generarResumen(String resumirComando) {

        
        Query q = new Query("cd(..).");
        System.out.println("cambio de directorio:" + " " + (q.hasSolution() ? "succeeded" : "failed"));
        
        q = new Query("cd('abstracts').");
        System.out.println("cambio de directorio:" + " " + (q.hasSolution() ? "succeeded" : "failed"));

        q = new Query(resumirComando);
        System.out.println("Resumir: " + (q.hasSolution() ? "succeeded" : "failed"));

        return q.hasSolution();

    }
}
