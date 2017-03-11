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

    public ontologiaMESH(){
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
    
    public String buscarNombre(String MESH){
        ontologiaMESH objeto = new ontologiaMESH();
        objeto.setMESH(MESH);
        objeto = consultarBD(objeto);
        return objeto.Nombre;
    }
    
    public void vaciar_pl(String MESH, String obj, ArrayList<String> listObj,String archivo) {

        ontologiaMESH objeto = new ontologiaMESH();
        objeto.setMESH(MESH);
        objeto = consultarBD(objeto);
        
        if (obj != null && objeto.getNombre() != null) {
            String cadena = "parent(\'" + obj + "\',\'" + objeto.getNombre() + "\').";
            new escribirBC(cadena,archivo);
            
        }

        if (!listObj.contains(MESH)) {
            listObj.add(MESH);
         
            for (int i = 0; i < objeto.getParent().size(); i++) {
                vaciar_pl(objeto.getParent().get(i), objeto.getNombre(), listObj, archivo);
            }
        }

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
        
    public void buscar(String MESH){
        
        buscarObjeto(MESH, 0, "");
        
    }
    
    private void buscarObjeto(String MESH, int nivel,String relacion){
        ontologiaMESH objeto = new ontologiaMESH();
        objeto.setMESH(MESH);
        objeto = consultarBD(objeto);
        
        for (int i = 0; i < nivel; i++) {
            System.out.print("      ");
        }
        
        System.out.println(relacion + objeto.getNombre());
        nivel++;
        
        for (int i = 0; i < objeto.getParent().size(); i++) {
            if (!objeto.getParent().get(i).equals("1000048")) {
                buscarObjeto(objeto.getParent().get(i), nivel, "parent->  ");
            }
            
        }
    }
    
}

