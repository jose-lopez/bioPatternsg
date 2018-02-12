/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.sun.javafx.geom.Vec2d;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jpl7.Query;
import org.jpl7.Term;
import org.jpl7.Variable;

/**
 *
 * @author yacson
 */
public class consultasJPL {

    public void consultas() {
        String archivo = "[consultas].";
        Query q = new Query(archivo);
        q.hasSolution();

        //buscar_receptores();
        
        buscar_complejos();

    }

    public void buscar_receptores() {
        String consulta = "buscar_receptores('EGF').";

        Query q2 = new Query(consulta);

        for (int i = 0; i < q2.allSolutions().length; i++) {
            System.out.println(q2.allSolutions()[i]);
        }
    }
    
    
    public void buscar_complejos(){
        ArrayList<pathway> pathway = new ArrayList<>();
        pathway = cargarPatrones();
        
        for (int i = 0; i < pathway.size(); i++) {
           ArrayList<String> objetos = pathway.get(i).getObjetos();
            ArrayList<Integer> pos_enzymas = new ArrayList<>();
            for (int j = 0; j < objetos.size(); j++) {
                String consulta = "enzyme("+objetos.get(j)+").";
                Query q2 = new Query(consulta);
                if (q2.hasSolution()) {
                    System.out.println("enzyne: "+objetos.get(j));
                    pos_enzymas.add(j);
                }
            }
            
            System.out.println("Pathway:");
            System.out.println(pathway.get(i).getPatron());
            //complejos por enzimas
            separar_complejo(pos_enzymas, objetos, pathway.get(i).getPatron());
                        
        }
        
    }
    
    public void separar_complejo(ArrayList<Integer> pos,ArrayList<String> ObjPat, String Patron){
        
        int posin = pos.get(0);
        int posfin = pos.get(pos.size()-1)+1;
        
        ArrayList<String> primerComplejo = new ArrayList<>();
        
        for (int i = 0; i <= posin; i++) {
            primerComplejo.add(ObjPat.get(i));
        }
        
        ArrayList<String> segundoComplejo = new ArrayList<>();
        for (int i = posfin-1; i < ObjPat.size()-1; i++) {
            segundoComplejo.add(ObjPat.get(i));
        }
                
        System.out.println("complejo: "+primerComplejo);
        System.out.println("complejo: "+segundoComplejo);
        System.out.println("\n\n");
        
    }

    public ArrayList<pathway> cargarPatrones() {
        ArrayList<pathway> pathways = new ArrayList<>();

        ObjectContainer db = Db4o.openFile("mineria/patrones.db");
        pathway pathway = new pathway();
        try {
            ObjectSet result = db.queryByExample(pathway);

            while (result.hasNext()) {
                pathway res = (pathway) result.next();
                pathways.add(res);
            }
        } catch (Exception e) {

        } finally{
            db.close();
        }
              
        return pathways;
    }

}
