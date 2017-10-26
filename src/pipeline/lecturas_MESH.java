/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.collections.ActivatableArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author yacson-ramirez
 */
public class lecturas_MESH {

    public String busquedaTerm(String term, int tipo ) {
        //term = term.replace(" ", "+");
        String id = null;
        String url;
        if (tipo == 1) {
            url = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=mesh&term=" + term + "&retstart=6&retmax=6&tool=biomed3";
        } else {
            url = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=mesh&term=" + term + "&retmax=6&tool=biomed3";
        }
       
        try {
            Document doc = new conexionServ().conecta(url);
            NodeList nList = doc.getElementsByTagName("Id");
            Element element = (Element) nList.item(0);
            id = element.getTextContent();
        } catch (Exception e) {

        }
        return id;
    }

    public ontologiaMESH obtenerOntologia(String id) {
        String url = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?db=mesh&id=" + id;
        ontologiaMESH ontologia = new ontologiaMESH();
        try {
            Document doc = new conexionServ().conecta(url);
            ontologia = revisa_xml(doc);
        } catch (Exception e) {

        }
        ontologia.setMESH(id);
        return ontologia;
    }

    private ontologiaMESH revisa_xml(Document doc) {
        ontologiaMESH ontologia = new ontologiaMESH();
        NodeList nList = doc.getElementsByTagName("Item");
        String nombre = null;
        for (int i = 0; i < nList.getLength(); i++) {
            Element element = (Element) nList.item(i);
            if (nombre == null && element.getAttribute("Name").toString().equals("string")) {
                nombre = element.getTextContent().toString();
            }

            if (element.getAttribute("Name").toString().equals("LinksType")) {
                NodeList nod = element.getElementsByTagName("Item");
                verNodo(nod, "Parent",ontologia.getParent());
            }
            if (element.getAttribute("Name").toString().equals("DS_HeadingMappedToList")) {
                NodeList nod = element.getElementsByTagName("Item");
                verNodo(nod, "int",ontologia.getParent());
            }

        }
        ontologia.setNombre(nombre);
        return ontologia;
    }

    private void verNodo(NodeList nod, String lab,ArrayList<String> list) {
        for (int i = 0; i < nod.getLength(); i++) {
            Element element = (Element) nod.item(i);
            if (element.getAttribute("Name").equals(lab)) {
                if (!list.contains(element.getTextContent())) {
                    list.add(element.getTextContent());
                    //System.out.println(element.getTextContent());
                }

            }

        }
        
    }
}
