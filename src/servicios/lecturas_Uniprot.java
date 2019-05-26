/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios;

import pipeline.*;
import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author yacson-ramirez
 */
public class lecturas_Uniprot extends conexionServ{

    private Document doc;
    private String Simbolo;
    private String Nombre;
    private ArrayList<String> sinonimos;
    private ArrayList<String> tejidos;
    private ArrayList<String> funcionMolecular;
    private ArrayList<String> procesoBiologico;
    private ArrayList<String> componenteCelular;

    public lecturas_Uniprot(String codigo) {
        
        funcionMolecular = new ArrayList<>();
        procesoBiologico = new ArrayList<>();
        componenteCelular = new ArrayList<>();
        sinonimos = new ArrayList<>();
        tejidos = new ArrayList<>();
        
        String url = "https://www.uniprot.org/uniprot/" + codigo + ".xml";
        
        try {
            doc = conecta(url);
            obtener_Nombre();
        } catch (Exception e) {

        }

    }

    public ArrayList<String> Codigos_GO() {
        ArrayList<String> cod = new ArrayList<>();

        NodeList nList = doc.getElementsByTagName("dbReference");

        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element Element = (Element) nNode;

                String tipo = Element.getAttribute("type");
                String id = Element.getAttribute("id");

                if (tipo.equals("GO")) {
                    NodeList nList2 = Element.getElementsByTagName("property");
                    for (int j = 0; j < nList2.getLength(); j++) {
                        Node nNode2 = nList2.item(j);
                        if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                            Element Element2 = (Element) nNode2;
                            if (Element2.getAttribute("type").equals("term")) {
                                String sep[] = Element2.getAttribute("value").split(":");
                                if (sep[0].equals("F")) {
                                    funcionMolecular.add(id);
                                    //System.out.println(sep[1] + "  " + id);
                                }else if(sep[0].equals("C")){
                                    componenteCelular.add(id);
                                }else if(sep[0].equals("P")){
                                    procesoBiologico.add(id);
                                }
                            }
                        }
                    }
                }
            }
        }

        return cod;
    }

    public void obtener_Nombre() {
        
        NodeList lista = doc.getElementsByTagName("fullName");

        for (int i = 0; i < lista.getLength(); i++) {
            
            String nom = lista.item(i).getTextContent();
            sinonimos.add(nom);
        }
        
        try {
            Nombre = doc.getElementsByTagName("fullName").item(0).getTextContent();
        } catch (Exception e) {

        }

        Node node = doc.getElementsByTagName("gene").item(0);

        try {
            Element elemento = (Element) node;
            Simbolo = elemento.getElementsByTagName("name").item(0).getTextContent();

        } catch (Exception e) {
        }
         

    }
    
    public void buscar_tejido(){
        
        NodeList listat = doc.getElementsByTagName("tissue");
        for (int i = 0; i < listat.getLength(); i++) {
            
            String tejido = listat.item(i).getTextContent();
            if(!tejidos.contains(tejido)){
                tejidos.add(tejido);
            }
        }
       
    }

    public String getSimbolo() {
        return Simbolo;
    }

    public void setSimbolo(String Simbolo) {
        this.Simbolo = Simbolo;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public ArrayList<String> getFuncionMolecular() {
        return funcionMolecular;
    }

    public void setFuncionMolecular(ArrayList<String> funcionMolecular) {
        this.funcionMolecular = funcionMolecular;
    }

    public ArrayList<String> getProcesoBiologico() {
        return procesoBiologico;
    }

    public void setProcesoBiologico(ArrayList<String> procesoBiologico) {
        this.procesoBiologico = procesoBiologico;
    }

    public ArrayList<String> getComponenteCelular() {
        return componenteCelular;
    }

    public void setComponenteCelular(ArrayList<String> componenteCelular) {
        this.componenteCelular = componenteCelular;
    }

    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }

    public ArrayList<String> getSinonimos() {
        return sinonimos;
    }

    public void setSinonimos(ArrayList<String> sinonimos) {
        this.sinonimos = sinonimos;
    }

    public ArrayList<String> getTejidos() {
        return tejidos;
    }

    public void setTejidos(ArrayList<String> tejidos) {
        this.tejidos = tejidos;
    }
    
    

}
