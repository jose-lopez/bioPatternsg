 /*
 Razonador.java


 Copyright (C) 2016.
 Jose Lopez (jlopez@unet.edu.ve).

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as
 published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>

 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import java.util.ArrayList;
import java.util.ArrayDeque;
//import Libreria.Utilities;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import org.jpl7.Query;
import org.jpl7.Term;
import org.jpl7.Variable;

/**
 *
 * @author jose
 */
public class Razonador {

    private static final String prologConsult = "";
    private static final String regexCleaner = "[^\\d\\w ]";

    public boolean inferir_patrones(String bc,configuracion config) {

        String consultPredictor = "[patron7].";
        Query query = new Query(consultPredictor);
        System.out.println(consultPredictor + " " + (query.hasSolution() ? "succeeded" : "failed"));
        
        //String inferirPatrones = "tell('patrones.txt'), patron(P, [bind, activate, regulate], ['SRIF'], 10,LOf), write(P),nl,write(LOf),nl,told.";
     //   patron(P, [_], [_], 10, LOf).
        String inferirPatrones = "tell('patrones.txt'), findnsols(50, P, (patron(P, [bind,activate,increase,regulate,enhance,induce,recognize,lead,trigger,target,express,reactivate,modulate,promote,mediate,synthesize], ['SST'], 10, LOf), write(P), nl, write(LOf), nl), ListP), told.";
        query = new Query(inferirPatrones);
        System.out.println(inferirPatrones + " " + (query.hasSolution() ? "succeeded" : "failed"));
        
        config.setInferirPatrones(true);
        config.guardar();
        
        return query.hasSolution();
        
    }

    public ArrayList<String> regiones(ArrayList<factorTranscripcion> factores) {

        for (factorTranscripcion ft : factores) {
        }

        return new ArrayList<String>();
    }

    public void consultEverything() {
        //Obteniendo los valores...
        String t2 = "gen(G), p_gener(G, Patgs, Pgt, Pag, Ppar, Pint, Gen, R).";
        Query q2 = new Query(t2);
        System.out.println("G = " + q2.oneSolution().get("G"));
        System.out.println("Patgs = " + q2.oneSolution().get("Patgs"));
        System.out.println("Pgts = " + q2.oneSolution().get("Pgt"));
        System.out.println("Pags = " + q2.oneSolution().get("Pag"));
        System.out.println("Pint = " + q2.oneSolution().get("Pint"));
        System.out.println("Ppar = " + q2.oneSolution().get("Ppar"));
        System.out.println("Gen = " + q2.oneSolution().get("Gen"));
        System.out.println("R = " + q2.oneSolution().get("R"));

    }

    //---------------------------------------
    public void init(String URLdelPL) {
        //Abriendo el archivo
        String testConsult = "consult('" + URLdelPL + "')";
        Query query = new Query(testConsult);
        System.out.println(testConsult + " " + (query.hasSolution() ? "succeeded" : "failed"));
    }

//---------------------------------------
    public List<Integer> getAtgPositions() {

        Variable G = new Variable("G");
        Variable Patgs = new Variable("Patgs");
        Variable Pgt = new Variable("Pgt");
        Variable Pag = new Variable("Pag");
        Variable Ppar = new Variable("Ppar");
        Variable Pint = new Variable("Pint");
        Variable Gen = new Variable("Gen");
        Variable R = new Variable("R");


        Term arg[] = {G, Patgs, Pgt, Pag, Ppar, Pint, Gen, R};
        Query q = new Query(prologConsult);
        String bound_to_x = "";

        if (q.hasSolution()) {
            bound_to_x = ((Map) q.oneSolution()).get("Patgs").toString();
            //System.out.println(bound_to_x);
        }

        List<Integer> atgPositions = new ArrayList<>();

        String cadena = bound_to_x;
        //Regex para dejar solo numeros 
        cadena = cadena.replaceAll(regexCleaner, "");
        if (cadena.length() > 1) {
            String[] vector = cadena.split(" ");
            for (int j = 0; j < vector.length; j++) {
                atgPositions.add(Integer.parseInt(vector[j]));
            }
            return atgPositions;
        }

        return new ArrayList<>();
    }
}
