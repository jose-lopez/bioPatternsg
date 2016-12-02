/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import com.db4o.collections.ActivatableArrayList;
import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author yacson-ramirez
 */
public class lecturas_QuickGO {

    public ontologia obtenerOntologia(String codigo) {
        ontologia ontologia = new ontologia();

        String url = "http://www.ebi.ac.uk/QuickGO/GTerm?id=" + codigo + "&format=oboxml&term=ancchart";
        try {
            Document doc = new conexionServ().conecta(url);
            ontologia = revisa_xml(doc);
        } catch (Exception e) {

        }
        return ontologia;
    }

    private ontologia revisa_xml(Document doc) {
        ontologia ontologia = new ontologia();

        NodeList nList = doc.getElementsByTagName("term");

        for (int i = 0; i < nList.getLength(); i++) {

            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element Element = (Element) nNode;
                ontologia.setGO(Element.getElementsByTagName("id").item(0).getTextContent());
                ontologia.setNombre(Element.getElementsByTagName("name").item(0).getTextContent());

                NodeList nList2 = Element.getElementsByTagName("synonym_text");
                for (int j = 0; j < nList2.getLength(); j++) {
                    //System.out.println(nList2.item(j).getTextContent());
                    
                    ontologia.getSinonimos().add(nList2.item(j).getTextContent().trim());

                }
                
                NodeList nList3 = Element.getElementsByTagName("is_a");
                //System.out.println(nList3.getLength());
                for (int j = 0; j < nList3.getLength(); j++) {
                    String cadena = nList3.item(j).getTextContent().replaceAll("\\s*$","");
                    cadena = cadena.replaceAll("^\\s*","");
                    //System.out.println(cadena);
                    ontologia.getIs_a().add(cadena);
                }

            }

        }

        return ontologia;
    }

}
