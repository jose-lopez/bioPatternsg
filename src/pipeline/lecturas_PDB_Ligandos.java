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
public class lecturas_PDB_Ligandos {

    public ArrayList<ligando> Buscar_ligandos(String CP) {
        ArrayList<ligando> ligandos = new ArrayList<>();
        try {

            String Url = "http://www.rcsb.org/pdb/rest/ligandInfo?structureId=" + CP;

            revisa_xml_ligandos(new conexionServ().conecta(Url), ligandos);

        } catch (Exception e) {

        }

       return ligandos;
    }

    private void revisa_xml_ligandos(Document doc, ArrayList<ligando> ligandos) {

        NodeList nList = doc.getElementsByTagName("ligand");

        for (int i = 0; i < nList.getLength(); i++) {

            Node nNode = nList.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                ligando ligando = new ligando();
                Element Element = (Element) nNode;
                ligando.setId(Element.getAttribute("chemicalID"));
                ligando.setNombre(Element.getElementsByTagName("chemicalName").item(0).getTextContent());
                ligandos.add(ligando);

            }
        }
        
    }

}
