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
public class lecturas_Uniprot {

    private Document doc;

    public lecturas_Uniprot(String codigo) {

        String url = "http://www.uniprot.org/uniprot/" + codigo + ".xml";
        try {
            doc = new conexionServ().conecta(url);

        } catch (Exception e) {

        }

    }

    public ArrayList<String> Codigo_GO() {
        ArrayList<String> cod = new ArrayList<>();

        NodeList nList = doc.getElementsByTagName("dbReference");

        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element Element = (Element) nNode;

                String tipo = Element.getAttribute("type");
                String id = Element.getAttribute("id");

                if (tipo.equals("GO")) {
                    NodeList nList2 = Element.getElementsByTagName("property");
                    for (int j = 0; j < nList2.getLength(); j++) {
                        Node nNode2 = nList2.item(j);
                        if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                            Element Element2 = (Element) nNode2;
                            if (Element2.getAttribute("type").equals("term")) {
                                String sep[] = Element2.getAttribute("value").split(":");
                                if (sep[0].equals("F")) {
                                    cod.add(id);
                                    //System.out.println(sep[1] + "  " + id);
                                }
                            }
                        }
                    }

                }
            }
        }

        return cod;
    }

    public String obtener_Nombre() {
        String nombre = null;
        nombre = doc.getElementsByTagName("fullName").item(0).getTextContent();
        return nombre;

    }

}
