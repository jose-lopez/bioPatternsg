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
package estructura;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import configuracion.utilidades;
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
    public factorTranscripcion(lecturas_TFBIND lecturasTFBIND, int NumeroObjetos, objetosMineria objetosMineria, boolean GO, boolean MESH,String ruta) {
        //System.out.println(utilidades.idioma.get(142)+"" + lecturasTFBIND.getFactor() + " ...");
               
        this.lecturasTFBIND = lecturasTFBIND;
        this.ID = lecturasTFBIND.getFactor();

        ArrayList<HGNC> infgen = new ArrayList<>();
        infgen = new lecturas_HGNC().busquedaInfGen(ID, GO, MESH,ruta);

        if (infgen.size() == 0) {
            lecturas_pathwaycommons pc = new lecturas_pathwaycommons();
            String simbolo = pc.obtenercodigoUP(ID);
            infgen = new lecturas_HGNC().busquedaInfGen(simbolo, GO, MESH,ruta);
        }

        this.HGNC = infgen;

        this.N_Iteracion = 0;
        this.complejoProteinico = new ArrayList<>();

        ArrayList<String> IDCP = Buscar_ID_complejosProteinicos(ID, NumeroObjetos);

        for (int i = 0; i < IDCP.size(); i++) {
            complejoProteinico cp = new complejoProteinico();
            cp = new lecturas_PDB().Busqueda_PDB(IDCP.get(i), GO, MESH,ruta);
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
    public factorTranscripcion(String ID, int N_Iteracion, int NumeroObjetos, boolean GO, boolean MESH,String ruta) {
        
        this.lecturasTFBIND = new lecturas_TFBIND();
        this.ID = ID;
        this.N_Iteracion = N_Iteracion;
        this.HGNC = new lecturas_HGNC().busquedaInfGen(ID, GO, MESH,ruta);
        this.complejoProteinico = new ArrayList<>();

        ArrayList<String> IDCP = Buscar_ID_complejosProteinicos(ID, NumeroObjetos);

        IDCP.forEach((idcp) -> {
            complejoProteinico cp = new complejoProteinico();
            cp = new lecturas_PDB().Busqueda_PDB(idcp, GO, MESH,ruta);
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

    public void vaciar_pl(String ruta) {
        ArrayList<String> AuxLig = new ArrayList<>();
        String ligandos = "[";

        for (complejoProteinico comp : complejoProteinico) {
            comp.vaciar_pl(ruta);
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
            new utilidades().carga();
            new escribirBC("ligandos(\'" + ID.replace("\'", "") + "\'," + ligandos + ").", ruta+"/minedObjects.pl");
        }
                
        boolean encontrado = false;
        objetosMinados objMIn = new objetosMinados();
        
        for (HGNC hgnc : HGNC) {
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
            new utilidades().carga();
            new escribirBC("sinonimos(\'" + hgnc.getSimbolo().replace("\'", "") + "\'," + cadena + ").", ruta+"/minedObjects.pl");
            new escribirBC(cadena_txt, ruta+"/minedObjects.txt");
            ArrayList<String> lista = hgnc.ListaNombres();
            if (lista.contains(ID)) {
                encontrado = true;
            }
        }

        if (!encontrado) {
            new utilidades().carga();
            new escribirBC("sinonimos(\'" + ID + "\',[\'" + ID + "\']).", ruta+"/minedObjects.pl");
            String cadena_txt = ID + ";" + objMIn.procesarNombre(ID);
            new escribirBC(cadena_txt, "minedObjects.txt");
        }

        if (N_Iteracion == 0) {
            new utilidades().carga();
            new escribirBC("transcription_factors(\'" + ID.replace("\'", "") + "\').", ruta+"/minedObjects.pl");
        }

    }
    
    public boolean buscar(factorTranscripcion objeto, String ruta){
        boolean encontrado = false;
        try {
            ObjectContainer db = Db4o.openFile(ruta + "/TF.db");
            try {

                ObjectSet result = db.queryByExample(objeto);
                if (result.hasNext()) {
                    encontrado = true;
                }
            } catch (Exception e) {
                System.out.println("Error al acceder a minedObjectsOntology.db");
            } finally {
                db.close();
            }
        } catch (Exception e) {

        }
        return encontrado;
    }

   
    private lecturas_HGNC lecturasHGNC(String ID, boolean GO, boolean MESH,String ruta) {
        lecturas_HGNC HGNC = new lecturas_HGNC();
        this.HGNC = HGNC.busquedaInfGen(ID, GO, MESH,ruta);
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
