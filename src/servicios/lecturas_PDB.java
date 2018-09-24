/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios;


import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import estructura.complejoProteinico;
import estructura.HGNC;

/**
 *
 * @author yacson-ramirez
 */
public class lecturas_PDB extends conexionServ{

    public complejoProteinico Busqueda_PDB(String cp, boolean GO, boolean MESH,String ruta) {
        complejoProteinico CP = new complejoProteinico();
        CP.setID(cp);
        String url = "http://www.rcsb.org/pdb/rest/describeMol?structureId=" + cp;
        try {
            //System.out.print("leyendo: " + cp + "  ");
            revisa_xml_PDB(conecta(url), CP, GO, MESH,ruta);
            // System.out.println("   ....ok");
        } catch (Exception ex) {

        }
        return CP;
    }

    //busquedas PDB   
    private void revisa_xml_PDB(Document doc, complejoProteinico cp, boolean GO, boolean MESH,String ruta) {

        NodeList nList = doc.getElementsByTagName("polymer");

        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element Element = (Element) nNode;
                // System.out.print("tipo: "+Element.getAttribute("type")+" ");

                if (Element.getAttribute("type").equalsIgnoreCase("dna")) {
                    NodeList nList2 = Element.getElementsByTagName("polymerDescription");
                    Node nNode2 = nList2.item(0);
                    Element des = (Element) nNode2;
                    //System.out.println(des.getAttribute("description"));
                    String separa[] = des.getAttribute("description").split(" ");
                    if (separa.length > 1) {
                        try {
                            cp.setDNA(separa[1]);
                        } catch (Exception e) {
                            // System.out.println("Error AND " + etiqueta);
                        }
                    } else {
                        try {
                            cp.setDNA(separa[0]);
                        } catch (Exception e) {
                            // System.out.println("Error AND " + etiqueta);
                        }
                    }

                } else {

                    NodeList nList2 = Element.getElementsByTagName("macroMolecule");
                    Node nNode2 = nList2.item(0);
                    Element des = (Element) nNode2;
                    String nombre = des.getAttribute("name");
                    //System.out.println(nombre);

                    NodeList nList3 = des.getElementsByTagName("accession");
                    Node nNode3 = nList3.item(0);
                    Element des2 = (Element) nNode3;
                    String idUP = (des2.getAttribute("id"));
                    ArrayList<HGNC> L_HGNC = new ArrayList<>();
                    if (idUP.length() == 6) {
                        lecturas_Uniprot UP = new lecturas_Uniprot(idUP);
                        L_HGNC = new lecturas_HGNC().busquedaInfGen(UP.getSimbolo(), GO, MESH,ruta);
                    } else {
                        L_HGNC = new lecturas_HGNC().busquedaInfGen(nombre, GO, MESH,ruta);
                    }
                    //System.out.println(UP.getSimbolo());

                    for (HGNC hgnc : L_HGNC) {
                        boolean encontrado = false;
                        for (HGNC hgnc2 : cp.getHGNC()) {
                            if (hgnc2.getSimbolo().equals(hgnc.getSimbolo())) {
                                encontrado = true;
                                break;
                            }
                        }
                        if (!encontrado) {
                            cp.getHGNC().add(hgnc);
                        }
                    }
                }
            }
        }

    }

}
