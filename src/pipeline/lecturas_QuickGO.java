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
     
    public ontologia obtenercodigoUP(String codigo) {
        ontologia ontologia = new ontologia();

        String url = "http://www.ebi.ac.uk/QuickGO/GTerm?id="+codigo+"&format=oboxml&term=ancchart";
        try {
            Document doc = new conexionServ().conecta(url);
            revisa_xml(doc);
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
                 ontologia.setFuncionMolecular(Element.getElementsByTagName("name").item(0).getTextContent());
                 NodeList nList2 = doc.getElementsByTagName("synonym_text");
                 System.out.println(nList2.getLength());
                
                 for (int j = 0; j < nList2.getLength(); j++) {
                     System.out.println("hola");
//                     Node nNode2 = nList.item(j);
//                      if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
//                          Element Element2 = (Element) nNode2;
//                          //ontologia.getSinonimos().add(Element2.getElementsByTagName("synonym_text").item(j).getTextContent());
//                          //System.out.println(Element2.getElementsByTagName("synonym_text").item(j).getTextContent());
//                      }
                     
                 }
                 
             }
            
        }
        
        
        return ontologia;
    }
    
    
    
}
