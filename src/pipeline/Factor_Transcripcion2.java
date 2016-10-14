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
public class Factor_Transcripcion2 {
    
    private String ID;
    private int N_Iteracion;
    private lecturas_HGNC lecturas_HGNC;
    private Lecturas_TFBIND lecturasTFBIND;
    private ArrayList<complejoProteinico2> complejoProteinico;
    
    public Factor_Transcripcion2(){
        complejoProteinico = new ArrayList<>();
        lecturas_HGNC = new lecturas_HGNC();
        lecturasTFBIND = new Lecturas_TFBIND();
    }
    //constructor para la primera Iteracion con lecturas obtenidas desde TFBIND
    public Factor_Transcripcion2(Lecturas_TFBIND lecturasTFBIND, boolean criterio, int NumeroObjetos, objetos_mineria2 objetosMineria){
        
        this.lecturasTFBIND = lecturasTFBIND;
        this.ID = lecturasTFBIND.getFactor();
        this.lecturas_HGNC = lecturasHGNC(ID,criterio);
        this.N_Iteracion = 0;
        this.complejoProteinico = new ArrayList<>();
        
        ArrayList<String> IDCP = Buscar_ID_complejosProteinicos(ID, NumeroObjetos);
        
        for (int i = 0; i < IDCP.size(); i++) {
            complejoProteinico2 cp = new complejoProteinico2();
            cp = new lecturas_PDB().Busqueda_PDB( IDCP.get(i) , criterio );
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
    public Factor_Transcripcion2(String ID, boolean  criterio, int N_Iteracion, int NumeroObjetos){
        System.out.println("Factor "+ID);
        this.ID = ID;
        this.N_Iteracion = N_Iteracion;
        this.lecturas_HGNC = lecturasHGNC(ID, criterio);
        this.complejoProteinico = new ArrayList<>();
        
        ArrayList<String> IDCP = Buscar_ID_complejosProteinicos(ID, NumeroObjetos);
        
        for (int i = 0; i < IDCP.size(); i++) {
            complejoProteinico2 cp = new complejoProteinico2();
            cp = new lecturas_PDB().Busqueda_PDB( IDCP.get(i) , criterio );
            cp.buscar_ligandos();
            complejoProteinico.add(cp);
        }
    }
    
    public void imprimir(){
        System.out.println("Factor de Trancripcion: "+ ID);
        lecturas_HGNC.imprimir();
        System.out.println("=====complejos proteinicos=====");
        for (int i = 0; i < complejoProteinico.size(); i++) {
            complejoProteinico.get(i).imprimir();
        }
        
    }
            
    private lecturas_HGNC lecturasHGNC(String ID, boolean criterio){
        lecturas_HGNC HGNC = new lecturas_HGNC();
        HGNC.busqueda_genenames(ID, criterio);
        return HGNC;
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

    public lecturas_HGNC getLecturas_HGNC() {
        return lecturas_HGNC;
    }

    public void setLecturas_HGNC(lecturas_HGNC lecturas_HGNC) {
        this.lecturas_HGNC = lecturas_HGNC;
    }

    public Lecturas_TFBIND getLecturasTFBIND() {
        return lecturasTFBIND;
    }

    public void setLecturasTFBIND(Lecturas_TFBIND lecturasTFBIND) {
        this.lecturasTFBIND = lecturasTFBIND;
    }

    public ArrayList<complejoProteinico2> getComplejoProteinico() {
        return complejoProteinico;
    }

    public void setComplejoProteinico(ArrayList<complejoProteinico2> complejoProteinico) {
        this.complejoProteinico = complejoProteinico;
    }
      
    
}



