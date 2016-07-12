 /*
    lecturas_rcsb.java


    Copyright (C) 2016.
    Yackson Ramirez (yackson.ramirez), Jose Lopez (jlopez@unet.edu.ve).

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>

*/

package pipeline;

import java.io.BufferedReader;
import java.io.FileWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class lecturas_rcsb {

    private int num;
    private int fin;

    public lecturas_rcsb() {
        num = 0;
    }

    public void setfin(int fin) {
        this.fin = fin;
    }

    public void buscar_ID(ArrayList<Complejo_Proteinico> CP, String FT, int limite) {
        String xml
                = "<orgPdbQuery>\n"
                + "\n"
                + "<queryType>org.pdb.query.simple.AdvancedKeywordQuery</queryType>\n"
                + "\n"
                + "<description>Text Search for: " + FT + "</description>\n"
                + "\n"
                + "<keywords>" + FT + "</keywords>\n"
                + "\n"
                + "</orgPdbQuery>";

        lecturas_rcsb aux = new lecturas_rcsb();
        int cont = 0;
        while (cont < 10) {
            cont++;
            try {
                //System.out.println("FT " + FT);
                List<String> pdbIds = aux.postQuery(xml);
                for (int i = pdbIds.size() - 1; i >= pdbIds.size() - limite && i > 0; i--) {

                    if (pdbIds.get(i) != "") {
                        //System.out.println("CP "+pdbIds.get(i));
                        Complejo_Proteinico cpaux = new Complejo_Proteinico();
                        cpaux.setId(pdbIds.get(i));
                        CP.add(cpaux);

                    }
                }
                break;
            } catch (Exception e) {
                try {
                    //System.out.println("error");
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    //Logger.getLogger(lecturas_rcsb.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

    }

    private List<String> postQuery(String xml) throws IOException {

        URL u = new URL("http://www.rcsb.org/pdb/rest/search/?sortfield=rank");
        String encodedXML = URLEncoder.encode(xml, "UTF-8");
        InputStream in = doPOST(u, encodedXML);
        List<String> pdbIds = new ArrayList<String>();
        BufferedReader rd = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = rd.readLine()) != null) {
            pdbIds.add(line);
        }

        rd.close();
        return pdbIds;
    }

    private static InputStream doPOST(URL url, String data) throws IOException {

        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();
        return conn.getInputStream();

    }

    public void Busqueda_PDB(ArrayList<Complejo_Proteinico> cp, boolean criterio, objetos_mineria obj_m) {

        for (int i = 0; i < cp.size(); i++) {
            System.out.println(num + "/" + fin);

            String url = "http://www.rcsb.org/pdb/files/" + cp.get(i).getId() + ".xml";
            
            try {
                revisa_xml_PDB(new conexionServ().conecta(url), cp.get(i), criterio, obj_m);
            } catch (Exception ex) {

            }

            num++;

        }
    }

    //busquedas PDB   
    private void revisa_xml_PDB(Document doc, Complejo_Proteinico cp, boolean criterio, objetos_mineria obj_m) {

        //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName("PDBx:entity");

        for (int temp = 0; temp < nList.getLength(); temp++) {

            Node nNode = nList.item(temp);
            //System.out.println("\nCurrent Element :" + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;
                if (eElement.getElementsByTagName("PDBx:type").item(0).getTextContent().equals("polymer")) {

                    String etiqueta = eElement.getElementsByTagName("PDBx:pdbx_description").item(0).getTextContent();
                    //guardar_en_archivo(texto);

                    String separa[] = etiqueta.split(" ");
                    //System.out.println("    Description: " + eElement.getElementsByTagName("PDBx:pdbx_description").item(0).getTextContent());

                    if (separa[0].equalsIgnoreCase("DNA") || separa[0].equalsIgnoreCase("mRNA")) {
                        try {
                            cp.setDNA(separa[1]);
                        } catch (Exception e) {
                            System.out.println("Error AND " + etiqueta);
                        }

                    } else {

                        Description desc = new Description();
                        String partes_etiqueta[] = etiqueta.split("/");
                        
                        for (int i = 0; i < partes_etiqueta.length; i++) {
                           desc.setEtiqueta(partes_etiqueta[i]);
                           cp.getDescripcion().add(desc);
                        }
                        
                    }

                }

            }

        }
        try {
            NodeList nList2 = doc.getElementsByTagName("PDBx:struct_keywords");
            for (int i = 0; i < nList2.getLength(); i++) {
                Node nNode = nList2.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element Element = (Element) nNode;
                    cp.getPdbx_keywords().add(Element.getElementsByTagName("PDBx:pdbx_keywords").item(0).getTextContent());
                    cp.getPdbx_keywords().add(Element.getElementsByTagName("PDBx:text").item(0).getTextContent());

                }
            }
        } catch (Exception e) {

        }

    }

    public void Buscar_ligandos(ArrayList<Complejo_Proteinico> CP) {

        for (int i = 0; i < CP.size(); i++) {
            try {

                String Url = "http://www.rcsb.org/pdb/rest/ligandInfo?structureId=" + CP.get(i).getId();
               
                revisa_xml_ligandos(new conexionServ().conecta(Url), CP.get(i).getLigandos());

            } catch (Exception e) {

            }

        }

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

        //System.out.println(num + "/" + fin);
        //num++;
    }

//    public void guardar_en_archivo(String texto) {
//        FileWriter fichero = null;
//        PrintWriter pw = null;
//        try {
//            fichero = new FileWriter("etiquetas_description.txt", true);
//            pw = new PrintWriter(fichero);
//
//            pw.println(texto);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                // Nuevamente aprovechamos el finally para 
//                // asegurarnos que se cierra el fichero.
//                if (null != fichero) {
//                    fichero.close();
//                }
//            } catch (Exception e2) {
//                e2.printStackTrace();
//            }
//        }
//
//    }
}
