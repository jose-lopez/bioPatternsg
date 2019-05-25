/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuracion;

import java.io.File;
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
public class language {

    public static ArrayList<String> text = new ArrayList<>();

    public language(String ruta) {
        File file = new File(ruta);
        Document doc = null;
        
        try {
        
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(file);
            doc.getDocumentElement().normalize();
            
            NodeList nList = doc.getElementsByTagName("m");
            
            for (int i = 0; i < nList.getLength(); i++) {
                text.add(nList.item(i).getTextContent());
            }
          
            

        } catch (Exception e) {

        }
    }
   

}
