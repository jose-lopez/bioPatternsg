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

    public ArrayList<String> obtenercodigoUP(String codigo) {
        ArrayList<String> codigoGO = new ActivatableArrayList<>();

        String url = "http://www.uniprot.org/uniprot/" + codigo + ".xml";
        try {
            Document doc = new conexionServ().conecta(url);
            revisa_xml(doc);
        } catch (Exception e) {

        }
        return codigoGO;
    }

    private ArrayList<String> revisa_xml(Document doc) {
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
                                    System.out.println(sep[1] + "  " + id);
                                }
                            }
                        }
                    }

                }
            }
        }
        return cod;
    }

}
