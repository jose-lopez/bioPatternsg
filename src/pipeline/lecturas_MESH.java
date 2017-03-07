/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author yacson-ramirez
 */
public class lecturas_MESH {

    public String busquedaTerm(String term) {

        String id = null;
        String url = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=mesh&term=" + term;

        try {
            Document doc = new conexionServ().conecta(url);
            NodeList nList = doc.getElementsByTagName("Id");
            Element element = (Element) nList.item(0);
            id = obtenerOntologia0(element.getTextContent());
        } catch (Exception e) {

        }
        return id;
    }

    public ontologiaMESH obtenerOntologia(String id) {
        String url = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?db=mesh&id=" + id;
        ontologiaMESH ontologia = new ontologiaMESH();
        try {
            Document doc = new conexionServ().conecta(url);
            ontologia = revisa_xml(doc,"Parent");
        } catch (Exception e) {

        }
        ontologia.setMESH(id);
        return ontologia;
    }
    
    private String obtenerOntologia0(String id) {
        String url = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?db=mesh&id=" + id;
        ontologiaMESH ontologia = new ontologiaMESH();
        try {
            Document doc = new conexionServ().conecta(url);
            ontologia = revisa_xml(doc,"int");
        } catch (Exception e) {

        }
        return ontologia.getParent().get(0);
    }
    
    private ontologiaMESH revisa_xml(Document doc, String lb) {
        ontologiaMESH ontologia = new ontologiaMESH();
        NodeList nList = doc.getElementsByTagName("Item");
        String nombre = null;
        
        for (int i = 0; i < nList.getLength(); i++) {
            Element element = (Element) nList.item(i);
            if (nombre == null && element.getAttribute("Name").toString().equals("string")){
                nombre = element.getTextContent().toString();
            }
            if(element.getAttribute("Name").toString().equals(lb)){
                String id = element.getTextContent().toString();
                if (!ontologia.getParent().contains(id)) {
                    ontologia.getParent().add(id);
                }
            }
        }
        ontologia.setNombre(nombre);
        return ontologia;
    }

}
