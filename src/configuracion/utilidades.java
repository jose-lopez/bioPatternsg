/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
