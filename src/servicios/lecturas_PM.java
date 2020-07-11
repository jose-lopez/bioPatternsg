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
package servicios;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import configuracion.PMIDS;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import configuracion.configuracion;
import configuracion.utilidades;
import configuracion.listPM;

/**
 *
 * @author yacson
 */
public class lecturas_PM extends conexionServ {

    public ArrayList<String> busquedaPM_ID(String palabras_clave, int cantIDs) {
        ArrayList<String> listID = new ArrayList<>();
        palabras_clave = palabras_clave.replace(" ", "+");
        //System.out.println(palabras_clave);
        String Url = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&term=" + palabras_clave + "&retmax=" + cantIDs + "&usehistory=y";

        try {
            Document doc = conecta(Url);
            listID = revisa_xml(doc, "Id");
        } catch (Exception e) {

        }
        return listID;
    }

//    public void BusquedaPM_AbstractsS(String fileAbstID, int cant_por_archivo, configuracion config) {
//
//        crearCarpeta(fileAbstID);
//        String cabecera = "<!DOCTYPE html>\r"
//                + "<html>\r"
//                + "<head>\r"
//                + "	<meta charset=\"utf-8\">\r"
//                + "	<title>generación de archivo html</title>\r"
//                + "</head>\r"
//                + "<body>\r";
//
//        String pie = "</body>\r"
//                + "</html>";
//
//        ArrayList<String> listaIDs = new ArrayList<>();
//        ObjectContainer db = Db4o.openFile("mineria/pubmedIDs.db");
//        PMIDS pm = new PMIDS();
//        int IDS = 0;
//        int descarga = 0;
//        try {
//
//            ObjectSet result = db.queryByExample(pm);
//            PMIDS aux = (PMIDS) result.get(0);
//            listaIDs.addAll(aux.pubmed_ids);
//            IDS = listaIDs.size();
//        } catch (Exception e) {
//        } finally {
//            db.close();
//        }
//
//        System.out.print("\n\n Generando coleccion de  abstracts .....");
//        int cont1 = 0, cont2 = 1;
//        ArrayList<String> lista = new ArrayList<>();
//        for (int i = 0; i < listaIDs.size(); i++) {
//            limpiarPantalla();
//            System.out.print("\n\n Generando coleccion de  abstracts .....");
//            System.out.println("Descargando " + i + " de " + IDS);
//
//            String ruta = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&id=" + listaIDs.get(i) + "&retmode=xml&rettype=abstract";
//            try {
//
//                String ruta_archivo = fileAbstID + "/" + fileAbstID + "_" + cont2 + ".html";
//
//                if (cont1 < cant_por_archivo) {
//                    if (cont1 == 0) {
//                        guardar_en_archivo(ruta_archivo, cabecera);
//                    }
//                    Document doc = conecta(ruta);
//                    lista = revisa_xml(doc, "AbstractText");
//                    guardar_en_archivo(ruta_archivo, lista, listaIDs.get(i));
//                    cont1++;
//
//                    if (i == (listaIDs.size() - 1)) {
//                        guardar_en_archivo(ruta_archivo, pie);
//                    }
//
//                } else {
//                    guardar_en_archivo(ruta_archivo, pie);
//                    cont1 = 0;
//                    cont2++;
//                }
//            } catch (Exception e) {
//
//            }
//        }
//        config.setAbstracts(true);
//        config.guardar();
//        System.out.println("ok");
//
//    }
    public void BusquedaPM_Abstracts(String fileAbstID, int cant_por_archivo, configuracion config, String ruta) {
        crearCarpeta(ruta + "/" + fileAbstID);
        String cabecera = "<!DOCTYPE html>\r"
                + "<html>\r"
                + "<head>\r"
                + "	<meta charset=\"utf-8\">\r"
                + "	<title>generación de archivo html</title>\r"
                + "</head>\r"
                + "<body>\r";

        String pie = "</body>\r"
                + "</html>";

        ArrayList<String> listaIDs = new ArrayList<>();
        ArrayList<listPM> listasPM = new ArrayList<>();

        ObjectContainer db = Db4o.openFile(ruta + "/pubmedIDs.db");
        PMIDS pm = new PMIDS();
        try {

            ObjectSet result = db.queryByExample(pm);
            PMIDS aux = (PMIDS) result.get(0);
            listaIDs.addAll(aux.pubmed_ids);
            //System.out.println(listaIDs);
        } catch (Exception e) {
        } finally {
            db.close();
        }
        final int IDS = listaIDs.size();
        int cont = 0, contuax = 1;
        ArrayList<String> aux = new ArrayList<>();
        for (String pmid : listaIDs) {
            //System.out.println(pmid);
            aux.add(pmid);
            cont++;
            if (cont > cant_por_archivo) {
                listPM laux = new listPM();
                laux.ID = aux;
                laux.num = contuax;
                listasPM.add(laux);
                contuax++;
                aux = new ArrayList<>();
                cont = 0;
            }
        }
        if (aux.size() > 0) {
            listPM laux = new listPM();
            laux.ID = aux;
            laux.num = contuax;
            listasPM.add(laux);
        }
        
        listasPM.parallelStream().forEach((lpm) -> {

            String ruta_archivo = ruta + "/" + fileAbstID + "/" + fileAbstID + "_" + lpm.num + ".html";
            descarga(lpm.ID, IDS, cabecera, pie, ruta_archivo);
        });

        config.setAbstracts(true);
        config.guardar(ruta);
        System.out.println("ok");

    }
    int cont1 = 0;

    private void descarga(ArrayList<String> listaPMID, int IDS, String cabecera, String pie, String ruta_archivo) {

        guardar_en_archivo(ruta_archivo, cabecera);
        listaPMID.forEach((pm) -> {

            cont1++;
            limpiarPantalla();
            System.out.println(utilidades.colorTexto1+utilidades.titulo);
            System.out.println(utilidades.colorTexto1+utilidades.proceso);
            System.out.println("\n"+utilidades.colorTexto2+utilidades.idioma.get(143));
            System.out.println(utilidades.colorReset+utilidades.idioma.get(144)+" " + cont1 + " / " + IDS);
            String ruta = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&id=" + pm + "&retmode=xml&rettype=abstract";

            try {
                Document doc = conecta(ruta);
                ArrayList<String> lista = revisa_xml(doc, "AbstractText");
                guardar_en_archivo(ruta_archivo, lista, pm);
            } catch (Exception e) {
                System.out.println("Error");
            }

        });
        guardar_en_archivo(ruta_archivo, pie);
    }

    private void limpiarPantalla() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void crearCarpeta(String nombre) {
        File f = new File(nombre);
        try {
            borrarDirectorio(f);
        } catch (Exception e) {

        }
        if (f.delete()) {
            // System.out.println("El directorio   ha sido borrado correctamente");
        } else {
            //System.out.println("El directorio  no se ha podido borrar");
        }

        File file = new File(nombre);
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

    private ArrayList<String> revisa_xml(Document doc, String lb) {
       
        ArrayList<String> lista = new ArrayList<>();
        NodeList nList = doc.getElementsByTagName(lb);

        for (int i = 0; i < nList.getLength(); i++) {
            try {
                Element element = (Element) nList.item(i);
                lista.add(element.getTextContent());
            } catch (Exception e) {
            }

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
            // String linea = "";

            for (String linea : Abstract) {
                linea = linea.replaceAll("&", "&amp;");
                linea = linea.replaceAll("<", "&lt;");
                linea = linea.replaceAll(">", "&gt;");
                pw.println(linea);
            }

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

    private void guardar_en_archivo(String ruta, String texto) {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter(ruta, true);
            pw = new PrintWriter(fichero);
            pw.print(texto);

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
