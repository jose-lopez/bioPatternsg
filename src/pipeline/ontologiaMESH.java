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
    
    public void vaciar_pl(String MESH, String obj, ArrayList<String> listObj) {

        ontologiaMESH objeto = new ontologiaMESH();
        objeto.setMESH(MESH);
        objeto = consultarBD(objeto);
        
        if (obj != null) {
            String cadena = "parent(\'" + obj + "\',\'" + objeto.getNombre() + "\').";
            if (!revisar_en_archivo(cadena)) {
                escribirArchivo(cadena);
            }
        }

        if (!listObj.contains(MESH)) {
            listObj.add(MESH);
         
            for (int i = 0; i < objeto.getParent().size(); i++) {
                vaciar_pl(objeto.getParent().get(i), objeto.getNombre(), listObj);
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

    private boolean revisar_en_archivo(String objeto) {

        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
            archivo = new File("ontologia.pl");
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String linea;

            while ((linea = br.readLine()) != null) {
                if (linea.equals(objeto)) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    private void escribirArchivo(String cadena) {

        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter("ontologia.pl", true);
            pw = new PrintWriter(fichero);
            System.out.println(cadena);
            pw.println(cadena);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }
    
    
}

