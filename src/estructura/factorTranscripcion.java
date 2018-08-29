/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package estructura;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import pipeline.escribirBC;
import servicios.lecturas_HGNC;
import servicios.lecturas_PDB;
import servicios.lecturas_TFBIND;
import servicios.lecturas_pathwaycommons;
import pipeline.objetosMinados;
import configuracion.objetosMineria;


/**
 *
 * @author yacson
 */
public class factorTranscripcion {
    private String ID;
    private int N_Iteracion;
    private lecturas_TFBIND lecturasTFBIND;
    private ArrayList<complejoProteinico> complejoProteinico;
    private ArrayList<HGNC> HGNC;

    public factorTranscripcion() {

        complejoProteinico = new ArrayList<>();
        lecturasTFBIND = new lecturas_TFBIND();
        HGNC = new ArrayList<>();
    }

    //constructor para la primera Iteracion con lecturas obtenidas desde TFBIND
    public factorTranscripcion(lecturas_TFBIND lecturasTFBIND, int NumeroObjetos, objetosMineria objetosMineria, boolean GO, boolean MESH) {
        System.out.println("Buscando información para : " + lecturasTFBIND.getFactor() + " ...");
        this.lecturasTFBIND = lecturasTFBIND;
        this.ID = lecturasTFBIND.getFactor();

        ArrayList<HGNC> infgen = new ArrayList<>();
        infgen = new lecturas_HGNC().busquedaInfGen(ID, GO, MESH);

        if (infgen.size() == 0) {
            lecturas_pathwaycommons pc = new lecturas_pathwaycommons();
            String simbolo = pc.obtenercodigoUP(ID);
            infgen = new lecturas_HGNC().busquedaInfGen(simbolo, GO, MESH);
        }

        this.HGNC = infgen;

        this.N_Iteracion = 0;
        this.complejoProteinico = new ArrayList<>();

        ArrayList<String> IDCP = Buscar_ID_complejosProteinicos(ID, NumeroObjetos);

        for (int i = 0; i < IDCP.size(); i++) {
            complejoProteinico cp = new complejoProteinico();
            cp = new lecturas_PDB().Busqueda_PDB(IDCP.get(i), GO, MESH);
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
    public factorTranscripcion(String ID, int N_Iteracion, int NumeroObjetos, boolean GO, boolean MESH) {

        System.out.println("Buscando información para: " + ID + " ...");
        this.lecturasTFBIND = new lecturas_TFBIND();
        this.ID = ID;
        this.N_Iteracion = N_Iteracion;
        this.HGNC = new lecturas_HGNC().busquedaInfGen(ID, GO, MESH);
        this.complejoProteinico = new ArrayList<>();

        ArrayList<String> IDCP = Buscar_ID_complejosProteinicos(ID, NumeroObjetos);

        IDCP.forEach((idcp) -> {
            complejoProteinico cp = new complejoProteinico();
            cp = new lecturas_PDB().Busqueda_PDB(idcp, GO, MESH);
            cp.buscar_ligandos();
            complejoProteinico.add(cp);
        });

    }

    public void NuevosObjetos(ArrayList<String> Lista) {
        for (int i = 0; i < complejoProteinico.size(); i++) {
            complejoProteinico.get(i).NuevosObjetos(Lista);
        }

    }

    public void imprimir() {
        //System.out.println("**Iteracion: " + N_Iteracion);
        System.out.println("Objeto: " + ID);
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
        ArrayList<String> AuxLig = new ArrayList<>();
        String ligandos = "[";

        for (complejoProteinico comp : complejoProteinico) {
            System.out.print(".");
            comp.vaciar_pl(archivo);
            for (ligando ligando : comp.getLigandos()) {
                if (!AuxLig.contains(ligando.getId())) {
                    if (ligandos.equals("[")) {
                        ligandos += "\'" + ligando.getId().replace("\'", "") + "\'";
                    } else {
                        ligandos += ",\'" + ligando.getId().replace("\'", "") + "\'";
                    }
                    AuxLig.add(ligando.getId());
                }
            }
        }
        ligandos += "]";
        if (!ligandos.equals("[]")) {
            new escribirBC("ligandos(\'" + ID.replace("\'", "") + "\'," + ligandos + ").", archivo);
        }
                
        boolean encontrado = false;
        objetosMinados objMIn = new objetosMinados();
        
        for (HGNC hgnc : HGNC) {
            System.out.print(".");
            String cadena_txt = "";
            String cadena = "[";
            cadena += "\'" + hgnc.getSimbolo().replace("\'", "") + "\',";
            cadena_txt += objMIn.procesarNombre(hgnc.getSimbolo()) + ";";
            cadena += "\'" + hgnc.getNombre().replace("\'", "") + "\'";
            cadena_txt += objMIn.procesarNombre(hgnc.getNombre());
            for (String sinonimo : hgnc.getSinonimos()) {
                cadena += ",\'" + sinonimo.replace("\'", "") + "\'";
                cadena_txt += ";" + objMIn.procesarNombre(sinonimo);
            }

            cadena += "]";
            //System.out.println("Experto: "+cadena);
            new escribirBC("sinonimos(\'" + hgnc.getSimbolo().replace("\'", "") + "\'," + cadena + ").", archivo);
            new escribirBC(cadena_txt, "objetosMinados.txt");
            ArrayList<String> lista = hgnc.ListaNombres();
            if (lista.contains(ID)) {
                encontrado = true;
            }
        }

        if (!encontrado) {
            new escribirBC("sinonimos(\'" + ID + "\',[\'" + ID + "\']).", archivo);
            String cadena_txt = ID + ";" + objMIn.procesarNombre(ID);
            new escribirBC(cadena_txt, "objetosMinados.txt");
        }

        if (N_Iteracion == 0) {
            new escribirBC("transcription_factors(\'" + ID.replace("\'", "") + "\').", archivo);
        }

    }

   
    private lecturas_HGNC lecturasHGNC(String ID, boolean GO, boolean MESH) {
        lecturas_HGNC HGNC = new lecturas_HGNC();
        this.HGNC = HGNC.busquedaInfGen(ID, GO, MESH);
        return HGNC;
    }

    public ArrayList<String> listaNombres() {
        ArrayList<String> lista = new ArrayList<>();
        lista.add(ID);
        HGNC.parallelStream().forEach(hgnc -> lista.addAll(hgnc.ListaNombres()));

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
