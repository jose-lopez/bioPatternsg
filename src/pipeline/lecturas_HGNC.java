/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author yacson-ramirez
 */

public class lecturas_HGNC {

    private String ID;
    private ArrayList<String> sinonimosExperto;
    private ArrayList<HGNC> HGNC;

    public lecturas_HGNC() {
        HGNC = new ArrayList<>();
        sinonimosExperto = new ArrayList<>();
    }

    public void busquedaInfGen(String contenido, boolean criterio, int opcion) {

        String lectura = contenido.replace(" ", "+");
        lecturas_pathwaycommons lpat = new lecturas_pathwaycommons();
        String cUP = lpat.obtenercodigoUP(lectura);
        //System.out.println("cod_UP: " + cUP);

        if (!cUP.equals("")) {
            lecturas_Uniprot UP = new lecturas_Uniprot(cUP);
            UP.obtener_Nombre();
            lectura = UP.getSimbolo();
            criterio = false;

            if (!busqueda_genenames(lectura, criterio, 0)) {

                lectura = UP.getNombre();
                if (!busqueda_genenames(lectura, criterio, 0)) {
                    busqueda_genenames(contenido, true, 0);
                }
            }

        }else{
            busqueda_genenames(contenido, true, 0);
        }

    }

    public boolean busqueda_genenames(String contenido, boolean criterio, int opcion) {

        ArrayList<String> factor = new ArrayList<>();
        if (criterio) {
            String cri = obtener_factor(contenido);
            try {
                cri = cri.replace(" ", "+");
                setID(cri);

                String Url = "http://rest.genenames.org/search/" + cri;
                Document doc = new conexionServ().conecta(Url);
                factor = busqueda_lista_xml(doc, opcion, cri);
            } catch (Exception e) {
                try {
                    contenido = contenido.replace(" ", "+");
                    setID(contenido);
                    String Url = "http://rest.genenames.org/search/" + contenido;
                    Document doc = new conexionServ().conecta(Url);
                    factor = busqueda_lista_xml(doc, opcion, cri);

                } catch (Exception ee) {
                }

            }
        } else {

            try {
                setID(contenido);
                contenido = contenido.replace(" ", "+");
                String Url = "http://rest.genenames.org/search/" + contenido;
                Document doc = new conexionServ().conecta(Url);
                factor = busqueda_lista_xml(doc, opcion, contenido);
            } catch (Exception ee) {
                HGNC hgnc = new HGNC();
                hgnc.setSimbolo(contenido);
                hgnc.setNombre(contenido);
                HGNC.add(hgnc);
            }
        }

        //System.out.println("etiqueta: "+ID);
        //System.out.println("Factor: "+factor);
        //System.out.println("cantidad Objetos HUGO: " + factor.size());
        for (int i = 0; i < factor.size(); i++) {

            try {
                // String nombre = busque String  Url = "http://rest.genenames.org/search/" + contenido;

                //System.out.println("Simbolo HUGO: " + factor.get(i));
                String Url = "http://rest.genenames.org/fetch/symbol/" + factor.get(i);
                Document doc = new conexionServ().conecta(Url);
                busqueda_datos_xml(doc);

            } catch (Exception e) {
                // System.out.println("no se encuentra "+contenido+" en HUGO");

            }
        }

        if (factor.size() > 0) {
            return true;
        } else {
            return false;
        }

    }

    private ArrayList<String> busqueda_lista_xml(Document doc, int opcion, String palabra) {
        ArrayList<String> nombres = new ArrayList<>();
        NodeList nList = doc.getElementsByTagName("result");
        Node nNode = nList.item(0);
        Element Element = (Element) nNode;
        float score = Float.parseFloat(Element.getAttribute("maxScore"));
        nList = doc.getElementsByTagName("doc");

        int cont = 0;
        for (int i = 0; i < nList.getLength(); i++) {
            nNode = nList.item(i);
            Element elemento = (Element) nNode;
            if (opcion == 0) {
                if (score == Float.parseFloat(elemento.getElementsByTagName("float").item(0).getTextContent())) {
                    //System.out.println(" simbolo "+elemento.getElementsByTagName("str").item(1).getTextContent());
                    nombres.add(elemento.getElementsByTagName("str").item(1).getTextContent());
                }
            } else if (opcion == -1) {

                if (elemento.getElementsByTagName("str").item(1).getTextContent().equals(palabra)) {
                    nombres.add(elemento.getElementsByTagName("str").item(1).getTextContent());
                    break;
                }

            } else if (opcion >= 1) {
                if (cont < opcion) {
                    cont++;
                    nombres.add(elemento.getElementsByTagName("str").item(1).getTextContent());

                }

            }
        }

        return nombres;
    }

    private void busqueda_datos_xml(Document doc) {

        HGNC hgnc = new HGNC();
        NodeList nList = doc.getElementsByTagName("doc");
        Node nNode = nList.item(0);

        if (nNode.getNodeType() == Node.ELEMENT_NODE) {

            Element elemento = (Element) nNode;
            hgnc.setSimbolo(elemento.getElementsByTagName("str").item(1).getTextContent());//Simbolo
            hgnc.setNombre(elemento.getElementsByTagName("str").item(2).getTextContent());//Nombre
            hgnc.setLocus_type(elemento.getElementsByTagName("str").item(4).getTextContent());//Locus type
            //-------------------------------------------------------
            //ensemble gene id
            NodeList list = elemento.getElementsByTagName("str");
            for (int i = 0; i < list.getLength(); i++) {
                Node nodo = list.item(i);
                if (nodo.getNodeType() == nodo.ELEMENT_NODE) {
                    Element elm = (Element) nodo;
                    if (elm.getAttribute("name").equals("ensembl_gene_id")) {
                        hgnc.setEnsembl_gene_id(elemento.getElementsByTagName("str").item(i).getTextContent());
                    }
                }
            }
            //-------------------------------------
            list = elemento.getElementsByTagName("arr");
            for (int i = 0; i < list.getLength(); i++) {
                Node nodo = list.item(i);
                if (nodo.getNodeType() == nodo.ELEMENT_NODE) {

                    //--------------------------------------------------------
                    //Busqueda de sinonimos
                    Element elm = (Element) nodo;
                    if (elm.getAttribute("name").equals("alias_symbol")) {

                        int ls = elm.getElementsByTagName("str").getLength();
                        for (int j = 0; j < ls; j++) {
                            hgnc.getSinonimos().add(elm.getElementsByTagName("str").item(j).getTextContent());
                        }

                    }

                    if (elm.getAttribute("name").equals("prev_name")) {

                        int ls = elm.getElementsByTagName("str").getLength();
                        for (int j = 0; j < ls; j++) {
                            hgnc.getSinonimos().add(elm.getElementsByTagName("str").item(j).getTextContent());
                        }

                    }

                    if (elm.getAttribute("name").equals("prev_symbol")) {

                        int ls = elm.getElementsByTagName("str").getLength();
                        for (int j = 0; j < ls; j++) {
                            hgnc.getSinonimos().add(elm.getElementsByTagName("str").item(j).getTextContent());
                        }

                    }

                    //----------------------------------------------------------
                    //gene_family
                    if (elm.getAttribute("name").equals("gene_family")) {

                        int ls = elm.getElementsByTagName("str").getLength();
                        for (int j = 0; j < ls; j++) {
                            hgnc.getGene_family().add(elm.getElementsByTagName("str").item(j).getTextContent());
                        }

                    }
                    //---------------------------------------------------
                    //Ontologia
                    if (elm.getAttribute("name").equals("uniprot_ids")) {

                        int ls = elm.getElementsByTagName("str").getLength();
                        for (int j = 0; j < ls; j++) {
                            String codUP = elm.getElementsByTagName("str").item(j).getTextContent();
                            lecturas_Uniprot letUP = new lecturas_Uniprot(codUP);
                            ObjetosMinadosGO objGO = new ObjetosMinadosGO();
                            objGO.setNombre(hgnc.getSimbolo());
                            letUP.Codigos_GO();
                            objGO.setFuncionMolecular(letUP.getFuncionMolecular());
                            objGO.setComponenteCelular(letUP.getComponenteCelular());
                            objGO.setProcesoBiologico(letUP.getProcesoBiologico());
                            objGO.guardarObjeto(objGO);
                                                                                    
                        }

                    }
                    
                }

            }

        }
        HGNC.add(hgnc);
       

    }
    
    
    public String obtener_factor(String desc) {
        String pal = "";
        String[] palabras = desc.split(" ");

        if (palabras.length == 1) {
            pal = desc;

        } else if (palabras.length == 2) {
            pal = desc;

        } else if (palabras.length > 2) {

            for (int i = 0; i < palabras.length; i++) {

                for (int j = 0; j < lista().length; j++) {
                    String[] pal_list = lista()[j].split(" ");
                    try {
                        if (palabras[i].equalsIgnoreCase(pal_list[0]) && palabras[i + 1].equalsIgnoreCase(pal_list[1])) {
                            if ((i + 2) < palabras.length) {
                                if (esAlfaNumerica(palabras[i + 2])) {
                                    pal = palabras[i + 2];
                                    return pal;
                                } else if (i > 0) {
                                    pal = palabras[i - 1];
                                    return pal;
                                }
                            } else if (i > 0) {
                                pal = palabras[i - 1];
                                return pal;
                            }
                        }
                    } catch (Exception e) {
                        pal = desc;
                    }

                }

            }

        }

        if (pal.equals("")) {
            pal = desc;
        }

        return pal;
    }

    private String[] lista() {
        String lista[] = {
            "initiation factor",
            "binding protein",
            "cyclase-associated protein",
            "transcription factor",
            "finger protein",
            "domain-binding protein",
            "domain-binding protein",
            "Sensor protein",
            "receptor coactivator",
            "transcription coactivator",
            "domain-binding protein",
            "kinase protein",
            "enzime protein",
            "adaptor protein",
            "translocator protein",
            "transporter protein"
        };

        return lista;
    }

    public boolean esAlfaNumerica(final String cadena) {

        for (int i = 0; i < cadena.length(); ++i) {
            char caracter = cadena.charAt(i);

            if (Character.isLetter(caracter)) {
                return true;
            }
        }
        return false;
    }

    public void imprimir() {
        System.out.println("Lecturas HGNC");
        System.out.println("    ID: " + ID);
        System.out.println("-------------------------------");
        for (int i = 0; i < HGNC.size(); i++) {
            HGNC.get(i).imprimir();
        }
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public ArrayList<HGNC> getHGNC() {
        return HGNC;
    }

    public void setHGNC(ArrayList<HGNC> HGNC) {
        this.HGNC = HGNC;
    }

    public ArrayList<String> getSinonimosExperto() {
        return sinonimosExperto;
    }

    public void setSinonimosExperto(ArrayList<String> sinonimosExperto) {
        this.sinonimosExperto = sinonimosExperto;
    }

    public ArrayList<String> listaNombres() {
        ArrayList<String> lista = new ArrayList<>();
        lista.add(ID);
        for (int i = 0; i < HGNC.size(); i++) {

            lista.addAll(HGNC.get(i).ListaNombres());

        }

        return lista;
    }

}

class HGNC {

    private String Simbolo;
    private String Nombre;
    private String locus_type;
    private String ensembl_gene_id;
    private ArrayList<String> sinonimos;
    private ArrayList<String> gene_family;
   
    public HGNC() {
        sinonimos = new ArrayList<>();
        gene_family = new ArrayList<>();
    }

    public void imprimir() {
        System.out.println("    Nombre: " + getNombre());
        System.out.println("    Simbolo: " + getSimbolo());
        System.out.println("    Ensembl gene id: " + getEnsembl_gene_id());
        System.out.println("    SINONIMOS:");
        for (int i = 0; i < getSinonimos().size(); i++) {
            System.out.println("       -" + getSinonimos().get(i));
        }
        System.out.println("_____________________________________");
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

    public String getLocus_type() {
        return locus_type;
    }

    public void setLocus_type(String locus_type) {
        this.locus_type = locus_type;
    }

    public String getEnsembl_gene_id() {
        return ensembl_gene_id;
    }

    public void setEnsembl_gene_id(String ensembl_gene_id) {
        this.ensembl_gene_id = ensembl_gene_id;
    }

    public ArrayList<String> getSinonimos() {
        return sinonimos;
    }

    public void setSinonimos(ArrayList<String> sinonimos) {
        this.sinonimos = sinonimos;
    }

    public ArrayList<String> getGene_family() {
        return gene_family;
    }

    public void setGene_family(ArrayList<String> gene_family) {
        this.gene_family = gene_family;
    }

    public ArrayList<String> ListaNombres() {
        ArrayList<String> Lista = new ArrayList<>();

        Lista.add(Simbolo);
        Lista.add(Nombre);

        for (int i = 0; i < sinonimos.size(); i++) {
            Lista.add(sinonimos.get(i));
        }

        return Lista;
    }

}
