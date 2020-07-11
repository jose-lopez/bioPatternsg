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
package configuracion;

import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author yacson
 */
public class utilidades {

    public static ArrayList<String> idioma = new ArrayList<>();
    public static String texto_etapa="";
    public static String texto_carga="";
    public static String momento="";
    public static String proceso="";
    
    public static String titulo="BioPatternsg";
    public static String colorTexto1 ="\u001B[34m";
    public static String colorTexto2 ="\u001B[36m";
    public static String colorReset ="\u001B[0m";
    
    public utilidades() {
        
    }
    
    public void lenguaje(String ruta) {
        File file = new File(ruta);
        Document doc = null;
        
        try {
        
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(file);
            doc.getDocumentElement().normalize();
            
            NodeList nList = doc.getElementsByTagName("m");
            
            for (int i = 0; i < nList.getLength(); i++) {
                idioma.add(nList.item(i).getTextContent());
            }
          
            

        } catch (Exception e) {

        }
    }

   
   
    public void carga(){
       
        if(texto_carga.length()<50 && texto_carga.length()>0){
            texto_carga+=".";
            System.out.print(".");
        }else{
            System.out.println("");
            limpiarPantalla();
            System.out.println(colorTexto1+titulo);
            System.out.println(colorTexto1+proceso);
            System.out.println("\n"+colorTexto2 + texto_etapa);
            System.out.print(colorReset+momento);
            texto_carga=".";
            
        }
        
    }
    
    public void limpiarPantalla() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}
