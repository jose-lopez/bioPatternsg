/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author yacson-ramirez
 */
public class ontologiaMESH {

    private String MESH;
    private String Nombre;
    private ArrayList<String> parent;

    public ontologiaMESH() {
        parent = new ArrayList<>();
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getMESH() {
        return MESH;
    }

    public void setMESH(String MESH) {
        this.MESH = MESH;
    }

    public ArrayList<String> getParent() {
        return parent;
    }

    public void setParent(ArrayList<String> parent) {
        this.parent = parent;
    }

    public String buscarNombre(String MESH) {
        ontologiaMESH objeto = new ontologiaMESH();
        objeto.setMESH(MESH);
        objeto = consultarBD(objeto);
        return objeto.Nombre;
    }

    public void vaciar_pl(String MESH, String obj, ArrayList<String> listObj, String archivo) {

        ontologiaMESH objeto = new ontologiaMESH();
        objeto.setMESH(MESH);
        objeto = consultarBD(objeto);
        String ruta_wnr = "well_know_rules.pl";

        if (obj != null && objeto.getNombre() != null) {
            String cadena = "is_a(\'" + obj.replace("\'", "") + "\',\'" + objeto.getNombre().replace("\'", "") + "\').";
            new escribirBC(cadena, archivo);
            String[] separa = obj.split(",");
            crear_rama_artificial(obj, ruta_wnr, archivo);
            if(separa[0].equals("Receptors")){
               cadena = "is_a(\'" + obj.replace("\'", "") + "\',\'Receptors\').";
               new escribirBC(cadena, archivo);
               String obj1 = procesarTexto("Receptors");
               String obj2 = procesarTexto(obj);
               String rule = obj1 + "(X):-" + obj2 + "(X).";
               new escribirBC(rule, ruta_wnr);
               
            }
            //procesando texto para crear las reglas en formato prolog
            String obj1 = procesarTexto(objeto.getNombre());
            String obj2 = procesarTexto(obj);

            String rule = obj1 + "(X):-" + obj2 + "(X).";
            new escribirBC(rule, ruta_wnr);
            
        }

        if (!listObj.contains(MESH)) {
            listObj.add(MESH);

            for (int i = 0; i < objeto.getParent().size(); i++) {
                vaciar_pl(objeto.getParent().get(i), objeto.getNombre(), listObj, archivo);
            }
        }

    }
    
    private void crear_rama_artificial(String obj, String ruta_wnr, String ruta_mesh) {
        String[] separa = obj.split(",");
        String cadena = null;
        if (separa[0].equals("Receptors")) {
            cadena = "is_a(\'" + obj.replace("\'", "") + "\',\'Receptors\').";
            new escribirBC(cadena, ruta_mesh);
            String obj1 = procesarTexto("Receptors");
            String obj2 = procesarTexto(obj);
            String rule = obj1 + "(X):-" + obj2 + "(X).";
            new escribirBC(rule, ruta_wnr);
        } else if (separa[0].equals("Adaptor Proteins")) {
            cadena = "is_a(\'" + obj.replace("\'", "") + "\',\'Adaptor Proteins\').";
            new escribirBC(cadena, ruta_mesh);
            String obj1 = procesarTexto("Adaptor Proteins");
            String obj2 = procesarTexto(obj);
            String rule = obj1 + "(X):-" + obj2 + "(X).";
            new escribirBC(rule, ruta_wnr);
        
        } else if (obj.equals("Intercellular Signaling Peptides and Proteins")) {
            String obj1 = procesarTexto("ligand");
            String obj2 = procesarTexto(obj);
            String rule = obj1 + "(X):-" + obj2 + "(X).";
            new escribirBC(rule, ruta_wnr);
        } 
        /*else if (obj.equals("Circadian Rhythm Signaling Peptides and Proteins")) {
            String obj1 = procesarTexto("ligand");
            String obj2 = procesarTexto(obj);
            String rule = obj1 + "(X):-" + obj2 + "(X).";
            new escribirBC(rule, ruta_wnr);
        }  else if (obj.equals("Intracellular Signaling Peptides and Proteins")) {
            String obj1 = procesarTexto("ligand");
            String obj2 = procesarTexto(obj);
            String rule = obj1 + "(X):-" + obj2 + "(X).";
            new escribirBC(rule, ruta_wnr);
        }*/
        //-----------------------------------------------------------
        String rule = procesarTexto("ligand");
        new escribirBC(rule+"(\'\').", ruta_wnr);
        rule = procesarTexto("proteins");
        new escribirBC(rule+"(\'\').", ruta_wnr);
        rule = procesarTexto("transcription factors");
        new escribirBC(rule+"(\'\').", ruta_wnr);
        rule =procesarTexto("adaptor proteins");
        new escribirBC(rule+"(\'\').", ruta_wnr);
        rule = procesarTexto("receptors");
        new escribirBC(rule+"(\'\').", ruta_wnr);
        rule = procesarTexto("enzymes");
        new escribirBC(rule+"(\'\').", ruta_wnr);
        rule = procesarTexto("transcription factors");
        new escribirBC(rule+"(\'\').", ruta_wnr);
        
    }


    private ontologiaMESH consultarBD(ontologiaMESH obj) {
        ontologiaMESH objeto = new ontologiaMESH();
        ObjectContainer db = Db4o.openFile("mineria/OntologiaMESH.db");
        try {

            ObjectSet result = db.queryByExample(obj);
            while (result.hasNext()) {
                objeto = (ontologiaMESH) result.next();
            }
        } catch (Exception e) {
            System.out.println("Error al acceder a OntologiaMESH.db");
        } finally {
            db.close();
        }
        return objeto;
    }

    public void buscar(String MESH) {

        buscarObjeto(MESH, 0, "");

    }

    private void buscarObjeto(String MESH, int nivel, String relacion) {
        ontologiaMESH objeto = new ontologiaMESH();
        objeto.setMESH(MESH);
        objeto = consultarBD(objeto);

        for (int i = 0; i < nivel; i++) {
            System.out.print("      ");
        }

        System.out.println(relacion + objeto.getNombre());
        nivel++;

        for (String obj : objeto.getParent()) {
            if (!obj.equals("1000048")) {
                buscarObjeto(obj, nivel, "is a->  ");
            }

        }
    }

    public String procesarTexto(String texto) {
        String aux = texto.toLowerCase();
        aux = aux.replace(" ", "_");
        aux = aux.replace("-", "_");
        aux = aux.replace(",", "_");
        aux = aux.replace("(", "_");
        aux = aux.replace(")", "_");
        aux = aux.replace("'", "");
        aux = aux.replace("+", "");
        aux = aux.replace("__", "_");
        aux = "wkr_"+aux;
        return aux;
    }
}
