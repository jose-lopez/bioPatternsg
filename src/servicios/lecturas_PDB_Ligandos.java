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

import estructura.ligando;
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
public class lecturas_PDB_Ligandos extends conexionServ{

    public ArrayList<ligando> Buscar_ligandos(String CP) {
        ArrayList<ligando> ligandos = new ArrayList<>();
        try {

            String Url = "http://www.rcsb.org/pdb/rest/ligandInfo?structureId=" + CP;

            revisa_xml_ligandos(conecta(Url), ligandos);

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
