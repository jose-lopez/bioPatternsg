/*
    lecturas_PM.java


    Copyright (C) 2016.
    Yacson Ramirez (yacson.ramirez@gmail.com), Jose Lopez (jlopez@unet.edu.ve).

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
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author yacson
 */
public class lecturas_PM {

    public ArrayList<String> busquedaPM_ID(String palabras_clave, int cantIDs) {
        ArrayList<String> listID = new ArrayList<>();
        //System.out.println(palabras_clave);
      
        String Url = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&term=" + palabras_clave +"&retmax="+cantIDs;
        try {
            Document doc = new conexionServ().conecta(Url);
            listID = revisa_xml(doc, "Id");
        } catch (Exception e) {

        }
        return listID;
    }

    public String BusquedaPM_Abstracts(ArrayList<String> listaIDs, String fileAbstID) throws Exception {

        crearCarpeta(fileAbstID);
        
        System.out.println("generando archivo " + fileAbstID);
        int cont1 = 0, cont2 = 1;
        for (int i = 0; i < listaIDs.size(); i++) {
            String ruta = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&id=" + listaIDs.get(i) + "&retmode=xml&rettype=abstract";
            try {
                if (cont1 < 5000) {
                    ArrayList<String> lista = new ArrayList<>();
                    Document doc = new conexionServ().conecta(ruta);
                    lista = revisa_xml(doc, "AbstractText");
                    guardar_en_archivo(fileAbstID+"/"+fileAbstID+"_"+cont2, lista, listaIDs.get(i));
                    cont1++;
                } else {
                    cont1 = 0;
                    cont2++;
                }
            } catch (Exception e) {

            }
        }
        System.out.println("Listo..");

        //llamada de metodo para crear html
        //cambiar el return por abstracts.html (archivo html)
        System.out.println("IMPRIMO ARCHIVO HTML");
        String salida_html = generar_html(fileAbstID);

        // return abstracts;
        return salida_html;
    }

    private void crearCarpeta(String nombre) {
        File f = new File(nombre);
        try{
        borrarDirectorio(f);
        }catch(Exception e){
            
        }
        if (f.delete()) {
           // System.out.println("El directorio   ha sido borrado correctamente");
        } else {
            //System.out.println("El directorio  no se ha podido borrar");
        }
        
       File file =new File(nombre);
       file.mkdir();

    }

    private void borrarDirectorio(File directorio) {
        File[] ficheros = directorio.listFiles();
        for (int i = 0; i < ficheros.length; i++) {
            if (ficheros[i].isDirectory()) {
                borrarDirectorio(ficheros[i]);
            }
            ficheros[i].delete();
        }
    }

    private String generar_html(String fuente) throws Exception {

        String nombre_archivo = "abstracts_salida.html";
        File archivo_fuente = new File(fuente);
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
                + "<body>";

        String pie = "\n</body>\n"
                + "</html>";

        int contador = 0, contador2 = 0, longitud_linea;

        leer = new BufferedReader(new FileReader(archivo_fuente));

        escribir = new BufferedWriter(new FileWriter(archivo_destino));

        if (archivo_fuente.exists()) {

            System.out.println("archivo localizado");

            System.out.println("generacion de archivo html iniciada......");

            escribir.write(cabecera);

            while ((linea = leer.readLine()) != null) {

                escribir.write(linea);

            }

            escribir.write(pie);

            leer.close();

            escribir.close();

            System.out.println("generacion de archivo html culminada con exito");
        } else {

            System.out.println("archivo no localizado");

        }

        return nombre_archivo;
    }

    private ArrayList<String> revisa_xml(Document doc, String lb) {
        ArrayList<String> lista = new ArrayList<>();
        NodeList nList = doc.getElementsByTagName(lb);

        for (int i = 0; i < nList.getLength(); i++) {
            try {
                Element element = (Element) nList.item(i);
                lista.add(element.getTextContent());
            } catch (Exception e) {
            }

            //System.out.println("    "+element.getTextContent());
        };

        return lista;
    }

    private void guardar_en_archivo(String ruta, ArrayList<String> Abstract, String ID) {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter(ruta, true);
            pw = new PrintWriter(fichero);
            //pw.println("PMID:"+ ID);
            for (int i = 0; i < Abstract.size(); i++) {
                pw.println(Abstract.get(i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Nuevamente aprovechamos el finally para 
                // asegurarnos que se cierra el fichero.
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }

    public void gererar_html(String archivo) {
        String salida = "";
        System.out.println("generacion de archivo html iniciada");

        System.out.println("generacion de archivo html finalizada");

    }

}
