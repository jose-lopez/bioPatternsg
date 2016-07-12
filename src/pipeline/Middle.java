 /*
    Middle.java


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

package pipeline;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jpl7.Query;
import org.jpl7.Term;
import org.jpl7.Variable;

/**
 * Basicamente encargado de: Invocar a prolog Y usando p_genes, gen_prueba y
 * soportes_p_intrones obtener los resultados con el metodo
 * {Query}.oneSolution.get("{Nombre de la variable}")
 */
public class Middle {
    //---------------------------Static Constants-------------------------------
    // <editor-fold desc="Static Constants">

    public static final int G = 0;
    public static final int ATG = 1;
    public static final int GT = 2;
    public static final int AG = 3;
    public static final int STOPS = 4;
    //---------------------------------------
    private static final String[] textTypes = {"G", "Patgs", "Pgt", "Pag", "Ppar", "Pint", "Gen", "R"};
    //---------------------------------------
    private static final String prologConsult = "gen(G), p_gener(G, Patgs, Pgt, Pag, Ppar, Pint, Gen, R).";
    //private static final String tssConsult = "region_tss(RegionTSS), coords_tss(RegionTSS, CoordsTSS, R).";
    private static final String tssConsult = "region_tss(RegionTSS), p_atgs(RegionTSS, CoordsTSS, R, 0)";
    private static final String ttsConsult = "region_tts(RegionTTS), p_pars(RegionTTS, CoordsTTS, R, 0)";
    private static final String poliAsConsult = "region_polA(RegionPoliA), coords_poli_A(RegionPoliA, CoordsPoliA, R).";
    private static final String regexCleaner = "[^\\d\\w ]";
    //  </editor-fold>

    //---------------------------Public Methods--------------------------------- 
    // <editor-fold defaultstate="collapsed" desc="Public Methods">
    /**
     * Define el archivo matriz -> p_genes.pl Y busca
     */
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

    /**
     * Define el archivo matriz -> p_genes.pl Y busca
     */
    public String consult(String consulta) {
        //Obteniendo los valores...

        Query q2 = new Query(consulta);
        System.out.println("G = " + q2.oneSolution().get("G"));
        System.out.println("Patgs = " + q2.oneSolution().get("Patgs"));
        System.out.println("Pgts = " + q2.oneSolution().get("Pgt"));
        System.out.println("Pags = " + q2.oneSolution().get("Pag"));
        System.out.println("Pint = " + q2.oneSolution().get("Pint"));
        System.out.println("Ppar = " + q2.oneSolution().get("Ppar"));
        System.out.println("Gen = " + q2.oneSolution().get("Gen"));
        System.out.println("R = " + q2.oneSolution().get("R"));

        String solution = "";
        return solution;

    }

    /**
     * Define el archivo matriz -> p_genes.pl Y busca
     */
    public List<Integer> consultTSSs() {
        //Obteniendo los valores...
        //String consulta = "region_tss(RegionTSS), coords_tss(RegionTSS, CoordsTSS, R).";
        this.init("regionTSS.pl");
        String consulta = "region_tss(RegionTSS), p_atgs(RegionTSS, CoordsTSS, R, 0)";

        Query q = new Query(consulta);

        System.out.println("RegionTSS = " + q.oneSolution().get("RegionTSS"));
        System.out.println("CoordsTSS = " + q.oneSolution().get("CoordsTSS"));
        System.out.println("R = " + q.oneSolution().get("R"));

        System.out.println(consulta + " " + (q.hasSolution() ? "succeeded" : "failed"));

        List<Integer> coordsTSS = getTssPositions();
        //List<Integer> coordsTSS = this.getAtgPositions();

        return coordsTSS;



    }

    public List<Integer> consultTSSsInr(String region) {

        List<Integer> coordsTSS = new ArrayList<Integer>();
        Pattern p = Pattern.compile("[TC][TC]A[ACGT][TA][TC][TC]");
        Matcher m = p.matcher(region);

        while (m.find()) {
            int coordTSS = m.start();
            coordsTSS.add(coordTSS + 2);
        }

        //boolean hasConsense = Pattern.matches("[TC][TC]A[ACGT][TA][TC][TC]", region);// Consenso Inr
        //hasConsense = Pattern.matches("[AG]G[AT][CT][GAC]", "GGTCA");// Consenso DPE
        return coordsTSS;
    }

    public List<Integer> consultTSSsDPE(String region) {

        List<Integer> coordsTSS = new ArrayList<Integer>();
        Pattern p = Pattern.compile("[AG]G[AT][CT][GAC]");
        Matcher m = p.matcher(region);

        while (m.find()) {
            int coordTSS = m.start() - 28;
            if (coordTSS > 0) {
                coordsTSS.add(coordTSS);
            }
        }

        //boolean hasConsense = Pattern.matches("[TC][TC]A[ACGT][TA][TC][TC]", region);// Consenso Inr
        //hasConsense = Pattern.matches("[AG]G[AT][CT][GAC]", "GGTCA");// Consenso DPE
        return coordsTSS;
    }

    /**
     * Define el archivo matriz -> p_genes.pl Y busca
     */
    public List<Integer> consultTTSs() {
        //Obteniendo los valores...
        this.init("regionTTS.pl");
        //String consulta = "region_tts(RegionTSS), coords_tss(RegionTSS, CoordsTSS, R).";
        Query q = new Query(ttsConsult);

        System.out.println("RegionTTS = " + q.oneSolution().get("RegionTTS"));
        System.out.println("CoordsTTS = " + q.oneSolution().get("CoordsTTS"));
        System.out.println("R = " + q.oneSolution().get("R"));

        System.out.println(ttsConsult + " " + (q.hasSolution() ? "succeeded" : "failed"));

        List<Integer> coordsTTS = getTtsPositions();
        //List<Integer> coordsTTS = this.getParadasPositions();

        return coordsTTS;



    }

    public List<Integer> consultTTSsPolyA(String region) {

        List<Integer> coordsTTS = new ArrayList<Integer>();
        Pattern p = Pattern.compile("A[TA]TAAA");
        Matcher m = p.matcher(region);

        while (m.find()) {
            int coordTSS = m.start() + 30;
            if (coordTSS > 0) {
                coordsTTS.add(coordTSS);
            }
        }

        //boolean hasConsense = Pattern.matches("[TC][TC]A[ACGT][TA][TC][TC]", region);// Consenso Inr
        //hasConsense = Pattern.matches("[AG]G[AT][CT][GAC]", "GGTCA");// Consenso DPE
        return coordsTTS;
    }

    public List<Integer> consultTTSsDSE(String region) {

        List<Integer> coordsTTS = new ArrayList<Integer>();
        Pattern p = Pattern.compile("[CT]GTGTT[CT][CT]");
        Matcher m = p.matcher(region);

        while (m.find()) {
            int coordTSS = m.start() - 30;
            if (coordTSS > 0) {
                coordsTTS.add(coordTSS);
            }
        }

        //boolean hasConsense = Pattern.matches("[TC][TC]A[ACGT][TA][TC][TC]", region);// Consenso Inr
        //hasConsense = Pattern.matches("[AG]G[AT][CT][GAC]", "GGTCA");// Consenso DPE
        return coordsTTS;
    }

    /**
     * Define el archivo matriz -> p_genes.pl Y busca
     */
    public List<Integer> consultPoliAs() {
        //Obteniendo los valores...
        /*String consulta = "region_polA(RegionPoliA), coords_poli_A(RegionPoliA, CoordsPoliA, R).";
         Query q = new Query(consulta);
        
         System.out.println("RegionPoliA = " + q.oneSolution().get("RegionPoliA"));
         System.out.println("CoordsPoliA = " + q.oneSolution().get("CoordsPoliA"));
         System.out.println("R = " + q.oneSolution().get("R"));
        
         System.out.println(consulta + " " + (q.hasSolution() ? "succeeded" : "failed"));
        
         List<Integer> coordsPoliAs = getPoliAsPositions();*/

        List<Integer> coordsPoliAs = this.getParadasPositions();

        return coordsPoliAs;



    }

    /**
     * Define el archivo matriz -> p_genes.pl Y busca
     */
    public List<Integer> consultParadasT() {
        //Obteniendo los valores...
        String consulta = "region_parada_T(RegionParadaT), coords_poli_A(RegionParadaT, CoordsParadaT, R).";
        Query q = new Query(consulta);

        System.out.println("RegionPoliA = " + q.oneSolution().get("RegionParadaT"));
        System.out.println("CoordsPoliA = " + q.oneSolution().get("CoordsParadaT"));
        System.out.println("R = " + q.oneSolution().get("R"));

        System.out.println(consulta + " " + (q.hasSolution() ? "succeeded" : "failed"));

        List<Integer> coordsParadasT = getPoliAsPositions();

        return coordsParadasT;



    }

    //---------------------------------------
    public String getData(String Var) {
        Query query = new Query(prologConsult);
        System.out.println("G = " + query.oneSolution().get(Var));
        return query.oneSolution().get(Var).toString();
    }

    //---------------------------------------
    public void init(String URLdelPL) {
        //Abriendo el archivoCadena
        String testConsult = "consult('" + URLdelPL + "')";
        Query query = new Query(testConsult);
        System.out.println(testConsult + " " + (query.hasSolution() ? "succeeded" : "failed"));
    }

    //---------------------------------------
    /**
     * Metodo que REEMPLAZA los otros usados para obtener las listas de las
     * posiciones, recibe un entero que determina que lista se desea consultar
     * donde ese entero debe ser una de las CONSTANTES PUBLICAS de la clase
     * MiddleWare<br/>
     * <br/>
     * Ejemplo:<br/>
     * getPositions(MiddleWare.ATG);<br/>
     * El resultado a esta consulta serian todas las listas de ATG que devuelva
     * prolog
     */
    public Queue<List<Integer>> getPositions(int type) {

        Variable G = new Variable("G");
        Variable Patgs = new Variable("Patgs");
        Variable Pgt = new Variable("Pgt");
        Variable Pag = new Variable("Pag");
        Variable Ppar = new Variable("Ppar");
        Variable Pint = new Variable("Pint");
        Variable Gen = new Variable("Gen");
        Variable R = new Variable("R");

        Term arg[] = {G, Patgs, Pgt, Pag, Ppar, Pint, Gen, R};
        Query q = new Query(prologConsult, arg);
        String bound_to_x = "";

        Queue<List<Integer>> positions = new ArrayDeque<>();

        while (q.hasMoreElements()) {
            bound_to_x = ((Map) q.nextElement()).get(textTypes[type]).toString();
            //System.out.println(bound_to_x);

            List<Integer> pos = new ArrayList<>();

            String Cadena = bound_to_x;
            //Regex para dejar solo numeros 
            Cadena = Cadena.replaceAll(regexCleaner, "");
            if (Cadena.length() > 1) {
                String[] vector = Cadena.split(" ");
                for (int j = 0; j < vector.length; j++) {
                    pos.add(Integer.parseInt(vector[j]));
                }
            }

            positions.add(pos);

        }

        q.close();

        return positions;
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

    //---------------------------------------
    public List<Integer> getGtPositions() {

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
            bound_to_x = ((Map) q.oneSolution()).get("Pgt").toString();
            //System.out.println(bound_to_x);
        }

        List<Integer> GtPositions = new ArrayList<>();


        String Cadena = bound_to_x;
        //Regex para dejar solo numeros 
        Cadena = Cadena.replaceAll(regexCleaner, "");
        if (Cadena.length() > 1) {
            String[] vector = Cadena.split(" ");
            for (int j = 0; j < vector.length; j++) {
                GtPositions.add(Integer.parseInt(vector[j]));
            }
            return GtPositions;
        }

        return new ArrayList<>();
    }

    //---------------------------------------
    public List<Integer> getAgPositions() {

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
            bound_to_x = ((Map) q.oneSolution()).get("Pag").toString();
            //System.out.println(bound_to_x);
        }

        List<Integer> AgPositions = new ArrayList<>();


        String Cadena = bound_to_x;
        //Regex para dejar solo numeros 
        Cadena = Cadena.replaceAll(regexCleaner, "");
        if (Cadena.length() > 1) {
            String[] vector = Cadena.split(" ");
            for (int j = 0; j < vector.length; j++) {
                AgPositions.add(Integer.parseInt(vector[j]));
            }
            return AgPositions;
        }

        return new ArrayList<>();
    }

    //---------------------------------------
    public List<Integer> getParadasPositions() {

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
            bound_to_x = ((Map) q.oneSolution()).get("Ppar").toString();
            //System.out.println(bound_to_x);
        }

        List<Integer> ParadasPositions = new ArrayList<>();


        String Cadena = bound_to_x;
        //Regex para dejar solo numeros 
        Cadena = Cadena.replaceAll(regexCleaner, "");
        if (Cadena.length() > 1) {
            String[] vector = Cadena.split(" ");
            for (int j = 0; j < vector.length; j++) {
                ParadasPositions.add(Integer.parseInt(vector[j]));
            }
            return ParadasPositions;
        }

        return new ArrayList<>();
    }

    //---------------------------------------
    public List<String> getGenData() {

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
            bound_to_x = ((Map) q.oneSolution()).get("G").toString();
            //System.out.println(bound_to_x);
        }

        List<String> GenData = new ArrayList<>();

        String Cadena = bound_to_x;
        //Regex para dejar solo numeros 
        Cadena = Cadena.replaceAll(regexCleaner, "");
        if (Cadena.length() > 1) {
            String[] vector = Cadena.split(" ");
            for (String vector1 : vector) {
                GenData.add(vector1);
            }
            return GenData;
        }

        return new ArrayList<>();

    }

    //---------------------------------------
    public List<Integer> getTssPositions() {

        Variable RegionTSS = new Variable("RegionTSS");
        Variable CoordsTSS = new Variable("CoordsTSS");
        Variable R = new Variable("R");

        Term arg[] = {RegionTSS, CoordsTSS, R};
        Query q = new Query(tssConsult);
        String bound_to_x = "";

        if (q.hasSolution()) {
            bound_to_x = ((Map) q.oneSolution()).get("CoordsTSS").toString();
            //System.out.println(bound_to_x);
        }

        List<Integer> tssPositions = new ArrayList<>();


        String Cadena = bound_to_x;
        //Regex para dejar solo numeros 
        Cadena = Cadena.replaceAll(regexCleaner, "");
        if (Cadena.length() > 1) {
            String[] vector = Cadena.split(" ");
            for (int j = 0; j < vector.length; j++) {
                tssPositions.add(Integer.parseInt(vector[j]));
            }
            return tssPositions;
        }

        return new ArrayList<>();
    }

    //---------------------------------------
    public List<Integer> getTtsPositions() {

        Variable RegionTSS = new Variable("RegionTTS");
        Variable CoordsTSS = new Variable("CoordsTTS");
        Variable R = new Variable("R");

        Term arg[] = {RegionTSS, CoordsTSS, R};
        Query q = new Query(ttsConsult);
        String bound_to_x = "";

        if (q.hasSolution()) {
            bound_to_x = ((Map) q.oneSolution()).get("CoordsTSS").toString();
            //System.out.println(bound_to_x);
        }

        List<Integer> ttsPositions = new ArrayList<>();


        String Cadena = bound_to_x;
        //Regex para dejar solo numeros 
        Cadena = Cadena.replaceAll(regexCleaner, "");
        if (Cadena.length() > 1) {
            String[] vector = Cadena.split(" ");
            for (int j = 0; j < vector.length; j++) {
                ttsPositions.add(Integer.parseInt(vector[j]));
            }
            return ttsPositions;
        }

        return new ArrayList<>();
    }

    //---------------------------------------
    public List<Integer> getPoliAsPositions() {


        Variable RegionTSS = new Variable("RegionPoliA");
        Variable CoordsTSS = new Variable("CoordsPoliA");
        Variable R = new Variable("R");

        Term arg[] = {RegionTSS, CoordsTSS, R};
        Query q = new Query(poliAsConsult);
        String bound_to_x = "";

        if (q.hasSolution()) {
            bound_to_x = ((Map) q.oneSolution()).get("CoordsPoliA").toString();
            //System.out.println(bound_to_x);
        }

        List<Integer> poliAsPositions = new ArrayList<>();

        String Cadena = bound_to_x;
        //Regex para dejar solo numeros 
        Cadena = Cadena.replaceAll(regexCleaner, "");
        if (Cadena.length() > 1) {
            String[] vector = Cadena.split(" ");
            for (int j = 0; j < vector.length; j++) {
                poliAsPositions.add(Integer.parseInt(vector[j]));
            }
            return poliAsPositions;
        }

        return new ArrayList<>();
    }

    /**
     * Define el archivo matriz -> p_genes.pl Y busca
     */
    public String consultResumenPLN(String abstracts) {

        //Obteniendo los valores...
        String consulta = "tell(salida-resumidor-abstracts.html), resume('" + abstracts + "'), told.";
        Query q = new Query(consulta);

        System.out.println(consulta + " " + (q.hasSolution() ? "succeeded" : "failed"));

        return "salida-resumdor-abstracts.txt";
    }
    //---------------------------------------
    //  </editor-fold>
}
