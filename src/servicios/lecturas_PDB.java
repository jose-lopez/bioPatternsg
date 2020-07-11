/* 
 * bioPatternsg
 * BioPatternsg is a system that allows the integration and analysis of information related to the modeling of Gene Regulatory Networks (GRN).
 * Copyright (C) 2020
 * Jose Lopez (josesmooth@gmail.com), Jacinto Dávila (jacinto.davila@gmail.com), Yacson Ramirez (yacson.ramirez@gmail.com).
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
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
