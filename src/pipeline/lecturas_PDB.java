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

    public complejoProteinico Busqueda_PDB(String cp, boolean GO, boolean MESH) {
        complejoProteinico CP = new complejoProteinico();
        CP.setID(cp);
        String url = "http://www.rcsb.org/pdb/rest/describeMol?structureId=" + cp;
        try {
            System.out.print("leyendo: " + cp + "  ");
            revisa_xml_PDB2(new conexionServ().conecta(url), CP, GO, MESH);
            System.out.println("   ....ok");
        } catch (Exception ex) {

        }
        return CP;
    }

    //busquedas PDB   
    private void revisa_xml_PDB(Document doc, complejoProteinico cp, boolean GO, boolean MESH) {

        NodeList nList = doc.getElementsByTagName("polymerDescription");

        for (int i = 0; i < nList.getLength(); i++) {

            Node nNode = nList.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element Element = (Element) nNode;
                String etiqueta = (Element.getAttribute("description"));
                //System.out.println("obj PDB: "+etiqueta);
                String separa[] = etiqueta.split(" ");
                //System.out.println("    Description: " + Element.getElementsByTagName("PDBx:pdbx_description").item(0).getTextContent());

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
                        //lecturas_HGNC HGNC = new lecturas_HGNC();
                        //HGNC.busquedaInfGen(partes_etiqueta[j]);

                        ArrayList<HGNC> L_HGNC = new lecturas_HGNC().busquedaInfGen(partes_etiqueta[j], GO, MESH);

                        for (int l = 0; l < L_HGNC.size(); l++) {
                            boolean encontrado = false;
                            for (int k = 0; k < cp.getHGNC().size(); k++) {
                                if (cp.getHGNC().get(k).getSimbolo().equals(L_HGNC.get(l).getSimbolo())) {
                                    encontrado = true;
                                    break;
                                }
                            }
                            if (!encontrado) {
                                cp.getHGNC().add(L_HGNC.get(l));
                            }

                        }

                    }

                }

            }
        }
    }

    private void revisa_xml_PDB2(Document doc, complejoProteinico cp, boolean GO, boolean MESH) {

        NodeList nList = doc.getElementsByTagName("polymer");

        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element Element = (Element) nNode;
                //System.out.println(Element.getAttribute("type"));

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
                    NodeList nList2 = Element.getElementsByTagName("accession");
                    Node nNode2 = nList2.item(0);
                    Element des = (Element) nNode2;
                    //System.out.println(des.getAttribute("id"));
                    String etiqueta = (des.getAttribute("id"));

                    lecturas_Uniprot UP = new lecturas_Uniprot(etiqueta);
                    //System.out.println(UP.getSimbolo());
                    ArrayList<HGNC> L_HGNC = new lecturas_HGNC().busquedaInfGen(UP.getSimbolo(), GO, MESH);

                    for (int l = 0; l < L_HGNC.size(); l++) {
                        boolean encontrado = false;
                        for (int k = 0; k < cp.getHGNC().size(); k++) {
                            if (cp.getHGNC().get(k).getSimbolo().equals(L_HGNC.get(l).getSimbolo())) {
                                encontrado = true;
                                break;
                            }
                        }
                        if (!encontrado) {
                            cp.getHGNC().add(L_HGNC.get(l));
                        }
                    }
                }
            }
        }

    }

}
