/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author yacson-ramirez
 */
public class lecturas_PDB {

    public complejoProteinico Busqueda_PDB(String cp, boolean criterio, int opcion) {
        complejoProteinico CP = new complejoProteinico();
        CP.setID(cp);
        //String url = "http://www.rcsb.org/pdb/files/" + cp + ".xml";
        String url = "http://www.rcsb.org/pdb/rest/describeMol?structureId=" + cp;
        try {
            System.out.print("leyendo: " + cp);
            revisa_xml_PDB(new conexionServ().conecta(url), CP, criterio, opcion);
            System.out.println("   Listo..");
        } catch (Exception ex) {

        }
        return CP;
    }

    //busquedas PDB   
    private void revisa_xml_PDB(Document doc, complejoProteinico cp, boolean criterio, int opcion) {

        NodeList nList = doc.getElementsByTagName("polymerDescription");

        for (int i = 0; i < nList.getLength(); i++) {

            Node nNode = nList.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                
                Element Element = (Element) nNode;
                String etiqueta = (Element.getAttribute("description"));
                //System.out.println("obj PDB: "+etiqueta);
                 String separa[] = etiqueta.split(" ");
                    //System.out.println("    Description: " + eElement.getElementsByTagName("PDBx:pdbx_description").item(0).getTextContent());

                    if (separa[0].equalsIgnoreCase("DNA") || separa[0].equalsIgnoreCase("mRNA")) {
                        try {
                            cp.setDNA(separa[1]);
                        } catch (Exception e) {
                            System.out.println("Error AND " + etiqueta);
                        }

                    } else {
//=====================================================================================
                        //BUSQUEDA DE INFORMACION HGNC DEL OBJETO

                        String partes_etiqueta[] = etiqueta.split("/");
                        for (int j = 0; j < partes_etiqueta.length; j++) {
                            lecturas_HGNC HGNC = new lecturas_HGNC();
                            HGNC.busqueda_genenames(partes_etiqueta[j], criterio, opcion);
                            cp.getHGNC().add(HGNC);
                        }

                    }
                
            }
        }
    }
}
