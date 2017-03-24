/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author yacson-ramirez
 */
public class factorTranscripcion {

    private String ID;
    private int N_Iteracion;
    private lecturas_TFBIND lecturasTFBIND;
    private ArrayList<complejoProteinico> complejoProteinico;
    private ArrayList<HGNC> HGNC;

    public factorTranscripcion() {

        complejoProteinico = new ArrayList<>();
        // lecturas_HGNC = new lecturas_HGNC();
        lecturasTFBIND = new lecturas_TFBIND();
        HGNC = new ArrayList<>();
    }

    //constructor para la primera Iteracion con lecturas obtenidas desde TFBIND
    public factorTranscripcion(lecturas_TFBIND lecturasTFBIND, int NumeroObjetos, objetosMineria objetosMineria,boolean GO, boolean MESH) {
        System.out.println("Buscando información para : " + lecturasTFBIND.getFactor() + " ...");
        this.lecturasTFBIND = lecturasTFBIND;
        this.ID = lecturasTFBIND.getFactor();
        this.HGNC = new lecturas_HGNC().busquedaInfGen(ID,GO,MESH);
        this.N_Iteracion = 0;
        this.complejoProteinico = new ArrayList<>();

        ArrayList<String> IDCP = Buscar_ID_complejosProteinicos(ID, NumeroObjetos);

        for (int i = 0; i < IDCP.size(); i++) {
            complejoProteinico cp = new complejoProteinico();
            cp = new lecturas_PDB().Busqueda_PDB(IDCP.get(i),GO,MESH);
            cp.buscar_ligandos();
            complejoProteinico.add(cp);
        }
    }

    public ArrayList<String> Buscar_ID_complejosProteinicos(String FT, int Limite) {
        ArrayList<String> ID_CP = new ArrayList<>();

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

        int Intentos_conexion = 0;
        while (Intentos_conexion < 10) {
            Intentos_conexion++;
            try {
                //System.out.println("FT " + FT);
                List<String> pdbIds = postQuery(xml);
                for (int i = pdbIds.size() - 1; i >= pdbIds.size() - Limite && i > 0; i--) {

                    if (pdbIds.get(i) != "") {
                        //System.out.println("CP "+pdbIds.get(i));
                        ID_CP.add(pdbIds.get(i));
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

        return ID_CP;
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

    //constructor para la segunda Iteracion en adelante
    public factorTranscripcion(String ID, int N_Iteracion, int NumeroObjetos,boolean GO, boolean MESH) {

        System.out.println("Buscando información para: " + ID + " ...");
        this.lecturasTFBIND = new lecturas_TFBIND();
        this.ID = ID;
        this.N_Iteracion = N_Iteracion;
        this.HGNC = new lecturas_HGNC().busquedaInfGen(ID,GO,MESH);
        //  this.lecturas_HGNC = lecturasHGNC(ID);
        this.complejoProteinico = new ArrayList<>();

        ArrayList<String> IDCP = Buscar_ID_complejosProteinicos(ID, NumeroObjetos);

        for (int i = 0; i < IDCP.size(); i++) {
            complejoProteinico cp = new complejoProteinico();
            cp = new lecturas_PDB().Busqueda_PDB(IDCP.get(i),GO,MESH);
            cp.buscar_ligandos();
            complejoProteinico.add(cp);
        }
    }

    public void NuevosObjetos(ArrayList<String> Lista){
        for (int i = 0; i < complejoProteinico.size(); i++) {
            complejoProteinico.get(i).NuevosObjetos(Lista);
        }
        
        
        
    }
    
    public void imprimir() {
        System.out.println("**Iteracion: " + N_Iteracion);
        System.out.println("Factor de Trancripcion: " + ID);
        System.out.println("Lecturas HGNC");
        for (int i = 0; i < HGNC.size(); i++) {
            HGNC.get(i).imprimir();
        }
        System.out.println("\n=====COMPLEJOS PROTEINICOS=====\n");
        for (int i = 0; i < complejoProteinico.size(); i++) {
            complejoProteinico.get(i).imprimir();
            System.out.println("----------------------------------------------");
        }

    }

    public void vaciar_pl(String archivo) {

        String ligandos = "[";
        for (int i = 0; i < complejoProteinico.size(); i++) {
            complejoProteinico.get(i).vaciar_pl(archivo);
            for (int j = 0; j < complejoProteinico.get(i).getLigandos().size(); j++) {
                if (ligandos.equals("[")) {
                    ligandos += "\'" + complejoProteinico.get(i).getLigandos().get(j).getId() + "\'";
                } else {
                    ligandos += ",\'" + complejoProteinico.get(i).getLigandos().get(j).getId() + "\'";
                }
            }
        }
        ligandos+="]";
        if (!ligandos.equals("[]")) {
            new escribirBC("ligandos(\'"+ID+"\',"+ligandos+").",archivo);
        }
        String cadena = "[";
        for (int i = 0; i < HGNC.size(); i++) {
            
            if(HGNC.get(i).equals(ID)){
                cadena +="\'"+HGNC.get(i).getNombre()+"\'";
                for (int j = 0; j < HGNC.get(i).getSinonimos().size(); j++) {
                    cadena+=",\'"+HGNC.get(i).getSinonimos()+"\'";
                    
                }
                break;
            }
        }
        cadena+="]";
        new escribirBC("sinonimos(\'"+ID+"\',"+cadena+").", archivo);
    }

    private lecturas_HGNC lecturasHGNC(String ID,boolean GO,boolean MESH) {
        lecturas_HGNC HGNC = new lecturas_HGNC();
        this.HGNC = HGNC.busquedaInfGen(ID,GO,MESH);
        return HGNC;
    }

    public ArrayList<String> listaNombres() {
        ArrayList<String> lista = new ArrayList<>();
        lista.add(ID);
        for (int i = 0; i < HGNC.size(); i++) {
            lista.addAll(HGNC.get(i).ListaNombres());
        }
        return lista;
    }

    public int getN_Iteracion() {
        return N_Iteracion;
    }

    public void setN_Iteracion(int N_Iteracion) {
        this.N_Iteracion = N_Iteracion;
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

    /* public lecturas_HGNC getLecturas_HGNC() {
        return lecturas_HGNC;
    }

    public void setLecturas_HGNC(lecturas_HGNC lecturas_HGNC) {
        this.lecturas_HGNC = lecturas_HGNC;
    }
     */
    public lecturas_TFBIND getLecturasTFBIND() {
        return lecturasTFBIND;
    }

    public void setLecturasTFBIND(lecturas_TFBIND lecturasTFBIND) {
        this.lecturasTFBIND = lecturasTFBIND;
    }

    public ArrayList<complejoProteinico> getComplejoProteinico() {
        return complejoProteinico;
    }

    public void setComplejoProteinico(ArrayList<complejoProteinico> complejoProteinico) {
        this.complejoProteinico = complejoProteinico;
    }

}
