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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

/**
 *
 * @author jose-lopez
 */
public class Alineador {

    public String rutaRPSP = "", rutaRPH = "", rutaRPs = "", cadena = "", vec[], vecl = "", linea, aux = "";
    public Vector<String> secsPromtsHolomlgs;
    public static String path;
    
    static{
        ClassLoader classLoader;
        classLoader = ClustalWAlign.class.getClassLoader();
        path = classLoader.getResource("").getPath();
    }

    public Alineador(String rutaPromSecProb, String rutaRegsPromsHomlogs) throws FileNotFoundException, IOException {

        this.rutaRPSP = rutaPromSecProb; // Desde donde se leerá la región promotora de la secuencia problema.
        this.rutaRPH = rutaRegsPromsHomlogs; // Desde donde se leerań las regiones promotoras de las secuencias homologas.
        this.rutaRPs = "regionesPromotoras"; // Donde se guardará el archivo con todas las regiones promotoras (sec. problema más homologas)
        this.secsPromtsHolomlgs = new Vector<String>();
        

        FileReader bufSP = new FileReader(new File(rutaPromSecProb));
        BufferedReader lecSP = new BufferedReader(bufSP);

        FileReader bufRPH = new FileReader(new File(rutaRegsPromsHomlogs));
        BufferedReader lecRPH = new BufferedReader(bufRPH);

        FileWriter s = new FileWriter(new File(rutaRPs));
        PrintWriter esRP = new PrintWriter(s);
        try {

            while (lecSP.ready()) {

                cadena += lecSP.readLine();
                System.out.println(cadena);
            }

            esRP.println(cadena); // Se guarda region promotora de la secuencia problema en archivo general.

            secsPromtsHolomlgs.removeAllElements();

            while (lecRPH.ready()) {

                linea = lecRPH.readLine();
                System.out.println(linea);
                if (!linea.equals("")) {
                    aux += linea;
                } else {
                    secsPromtsHolomlgs.add(aux);
                    aux = "";
                }
            }

            for (int i = 0; i < secsPromtsHolomlgs.size(); i++) {

                System.out.println(secsPromtsHolomlgs.elementAt(i));
                esRP.println(secsPromtsHolomlgs.elementAt(i)); // Se guarda region promotora de la secuencia problema en archivo general.

            }

            lecRPH.close();
            esRP.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }

    /**
     * lee las secuencias para alinear y conseguir los motivos *
     */
    public void LeerCadena(String r) {
        FileReader buf;
        int con = 0;
        String linea = "", aux = "";
        try {
            buf = new FileReader(new File(r));
            BufferedReader lec = new BufferedReader(buf);

            while (lec.ready()) {
                linea = lec.readLine();
                secsPromtsHolomlgs.add(linea);

            }

            /*while (lec.ready()) {
             linea = lec.readLine();
             if (!linea.equals("")) {
             aux += linea;
             } else {
             secsPromtsHolomlgs.add(aux);
             aux = "";
             }
             }*/
            lec.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cadena = secsPromtsHolomlgs.elementAt(0);
        vec = new String[secsPromtsHolomlgs.size() - 1];
        System.out.println(cadena);
        for (int i = 1; i < secsPromtsHolomlgs.size(); i++) {
            vec[i - 1] = secsPromtsHolomlgs.elementAt(i);
            System.out.println(vec[i - 1]);
        }
    }

    /**
     * busca el archivo de alineacion y comprueba los motivos *
     */
    public void Lector() {
        try {
            FileReader l = new FileReader(new File(path + "archivodesecuencias.aln"));
            BufferedReader le = new BufferedReader(l);
            String linea = "", aux = "";
            boolean ban = false;
            while (le.ready()) {
                linea = le.readLine();
                aux = linea.substring(0, 1);
                if (aux.equals(">") && ban) {
                    break;
                }
                if (ban) {
                    vecl += linea;
                }
                if (linea.equalsIgnoreCase(">SeqProble")) {
                    ban = true;
                }
            }
            le.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String alineadorClustalw() throws IOException {

        FileWriter s = new FileWriter(new File("bloquesConsenso"));
        PrintWriter es = new PrintWriter(s);

        secsPromtsHolomlgs.removeAllElements();
        LeerCadena(rutaRPs);
        System.out.println(vec.length);
        for (int i = 0; i < vec.length; i++) {
            System.out.println(vec[i]);
        }
        new MultAlignTest(vec, cadena);
        Lector();
        String v[] = vecl.split("-");

        for (int i = 0; i < v.length; i++) {
            if (!v[i].equals("")) {
                // Guardar en el archivo de bloques consenso
                es.println(v[i]);
                System.out.println(v[i]);
            }
        }
        System.out.println("\n");
        es.close();
        
        System.out.println("Archivo con los bloques consenso es: bloquesConsenso" + "\n");
        return "bloquesConsenso";

    }

}
