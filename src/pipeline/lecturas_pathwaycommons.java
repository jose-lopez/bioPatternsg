/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author yacson-ramirez
 */
public class lecturas_pathwaycommons {

    public String obtenercodigoUP(String gen) {
        String codigoUP = "";

        String url = "http://www.pathwaycommons.org/pc/webservice.do?version=2.0&q=+" + gen + "&format=xml&cmd=search";
        try {
            Document doc = new conexionServ().conecta(url);
            codigoUP = revisa_xml(doc);
        } catch (Exception e) {

        }

        return codigoUP;

    }

    private String revisa_xml(Document doc) {
        String cod = "";

        NodeList nList = doc.getElementsByTagName("xref");
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element Element = (Element) nNode;
                String etiqueta = Element.getElementsByTagName("db").item(0).getTextContent();
                if (etiqueta.equals("UniProt")) {
                    cod = Element.getElementsByTagName("id").item(0).getTextContent();
                    return cod;
                }
            }
        }
        return cod;
    }

}
