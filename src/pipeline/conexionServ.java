 /*
    conexionServ.java


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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

/**
 *
 * @author yacson
 *
 */
public class conexionServ {

    public Document conecta(String Url) {
        
        Document doc = null;
        //System.out.println("url: " + Url);
        int cont = 0;
        while (cont < 100) {
            try {
                cont++;
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                URL url = new URL(Url);
                doc = db.parse(url.openStream());
                doc.getDocumentElement().normalize();
                break;
            } catch (Exception ex) {
                
                try {

                   // System.out.println("Error de conexion con: "+Url);
                    //System.out.println("reintentando..."+cont );
                    Thread.sleep(5000);
                } catch (InterruptedException ex1) {
                    //Logger.getLogger(lecturas_rcsb.class.getName()).log(Level.SEVERE, null, ex1);
                }

            }
        }
        if(cont>=10){
            System.out.println("Error de conexion imposible acceder a: "+Url);
        }

        return doc;
    }

}
