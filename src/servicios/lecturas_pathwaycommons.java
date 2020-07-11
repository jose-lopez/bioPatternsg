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

        //String url = "http://www.pathwaycommons.org/pc/webservice.do?version=2.0&q=+" + gen + "&format=xml&cmd=search";
        String url = "http://www.pathwaycommons.org/pc2/search.xml?q="+gen+"&datasource=ctd";
        try {
            Document doc = new conexionServ().conecta(url);
            codigoUP = revisa_xml2(doc);
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
    
     private String revisa_xml2(Document doc) {
        String simbolo = "";

        NodeList nList = doc.getElementsByTagName("name");
        simbolo = nList.item(0).getTextContent();
        return simbolo;
    }

}
