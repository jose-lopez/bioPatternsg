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
import configuracion.language;
import estructura.HGNC;
import estructura.factorTranscripcion;
import estructura.objetos_Experto;
import estructura.ontologiaObjMin;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
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
        //buscar_complejos();
        //buscar_proteinas_adicionales();
        //interaccion_proteina_proteina();
        //buscar_motivos();
        //buscar_otros_ligandos();
        //buscar_tipo_ligando();
        //buscar_tejido();
        //buscar_cadenas_pathways();
        ///home/yacson/swi-prolog/lib/swipl-7.6.3/lib/x86_64-linux
    }

    public void menu(String ruta) {
        String v = "style_check(-discontiguous).";
        Query q0 = new Query(v);
        q0.hasSolution();

        String objPatr = "['" + ruta + "/objetos_patrones'].";
        //System.out.println(objPatr);
        Query q1 = new Query(objPatr);
        q1.hasSolution();

        String bc = "['" + ruta + "/baseC'].";
        //System.out.println(bc);
        Query q2 = new Query(bc);
        q2.hasSolution();

        String objMin = "['" + ruta + "/objetosMinados'].";
        //System.out.println(objMin);
        Query q3 = new Query(objMin);
        q3.hasSolution();

        String objGO = "['" + ruta + "/ontologiaGO'].";
        //System.out.println(objGO);
        Query q4 = new Query(objGO);
        q4.hasSolution();

        String objMESH = "['" + ruta + "/ontologiaMESH'].";
        //System.out.println(objMESH);
        Query q5 = new Query(objMESH);
        q5.hasSolution();

        String archivo = "['scripts/consultas'].";
        Query q = new Query(archivo);
        q.hasSolution();

        limpiarPantalla();
        Scanner lectura = new Scanner(System.in);
        boolean r = true;
        while (r) {
            System.out.println(language.text.get(94) + "\n");
            System.out.println(language.text.get(95));
            System.out.println(language.text.get(96));
            System.out.println(language.text.get(97));
            System.out.println(language.text.get(98));
            System.out.println(language.text.get(99));
            System.out.println(language.text.get(100));
            System.out.println(language.text.get(101));
            System.out.println(language.text.get(102));
            System.out.println(language.text.get(103));
            //System.out.println("10.- Busqueda de tejidos");
            System.out.println(language.text.get(4));

            String resp = lectura.nextLine();

            switch (resp) {

                case "1":
                    buscar_receptores();
                    break;
                case "2":
                    buscar_complejos(ruta);
                    break;
                case "3":
                    buscar_proteinas_adicionales();
                    break;
                case "4":
                    interaccion_proteina_proteina(ruta);
                    break;
                case "5":
                    buscar_motivos(ruta);
                    break;
                case "6":
                    buscar_otros_ligandos(ruta);
                    break;
                case "7":
                    buscar_tipo_ligando(ruta);
                    break;
                case "8":
                    buscar_cadenas_pathways(ruta);
                    //buscar_cadenas_pathwaysRest(ruta);
                    break;
                case "9":
                    consultar_objeto(ruta);
                    break;
                /*case "10":
                    //buscar_tejido(ruta);
                    break;*/
                case "0":
                    r = false;
                    break;

            }

        }
    }

    public void consultar_objeto(String ruta) {
        Scanner lectura = new Scanner(System.in);
        boolean r = true;
        while (r) {
            limpiarPantalla();
            System.out.print(language.text.get(104));
            String text = lectura.nextLine();
            String objeto = "'" + text.replace("'", "") + "'";

            String simbolo = buscar_sinonimos(objeto);

            if (!simbolo.equals("")) {
                ver_ontologias(simbolo, ruta);
            }

            while (true) {
                System.out.println(language.text.get(105));
                String resp = lectura.nextLine();
                if (resp.equalsIgnoreCase("s") || resp.equalsIgnoreCase("y")) {
                    r = true;
                    break;
                } else if (resp.equalsIgnoreCase("n")) {
                    r = false;
                    break;
                } else {
                    System.out.println(language.text.get(53));
                }

            }

        }

    }

    public void ver_ontologias(String obj, String ruta) {
        Scanner lectura = new Scanner(System.in);
        boolean r = true;
        while (r) {

            System.out.println("\n " + language.text.get(0));
            System.out.println(language.text.get(106));
            System.out.println(language.text.get(107));
            System.out.println(language.text.get(4));
            String resp = lectura.nextLine();

            switch (resp) {
                case "1":
                    arbol_identidad(obj, 0);
                    //new ontologiaObjMin().buscarObjeto(obj.replace("'", ""), false, true, ruta);
                    break;

                case "2":
                    //new ontologiaObjMin().buscarObjeto(obj.replace("'", ""), true, false, ruta);
                    break;

                case "0":
                    r = false;
                    break;
            }

        }
    }

    public void arbol_identidad(String Obj, int tabulador) {
        String consulta = "arbol_identidad(" + Obj + ",Y).";
        Query q2 = new Query(consulta);
        for (int i = 0; i < q2.allSolutions().length; i++) {
            String aux = q2.allSolutions()[i].toString();
            aux = aux.replace("{", "").replace("}", "").replace("Y=", "");
            for (int j = 0; j < tabulador; j++) {
                System.out.print(" ");
            }
            System.out.println(aux.replace("'", ""));
            arbol_identidad(aux, tabulador + 8);
        }
    }

    public String buscar_sinonimos(String objeto) {
        String simbolo = "";
        String consulta = "buscar_objeto(" + objeto + ",B,S).";
        ArrayList<String> sinonimos = new ArrayList<>();

        Query q2 = new Query(consulta);
        for (int i = 0; i < q2.allSolutions().length; i++) {
            String aux = q2.allSolutions()[i].toString();
            aux = aux.replace("{", "").replace("}", "").replace("B=", "").replace("S=", "").replace(" ", "");
            String sep[] = aux.split(",");

            simbolo = sep[0];
            sinonimos.add(sep[1]);

        }

        System.out.println(language.text.get(108) + " " + simbolo);
        System.out.print(language.text.get(137));
        sinonimos.forEach(s -> System.out.print(s + ", "));
        System.out.println("\n");

        //---------------------------------------------------
        String consulta1 = "transcription_factors(" + simbolo + ").";
        Query q3 = new Query(consulta1);

        if (q3.hasSolution()) {
            System.out.println(language.text.get(109));
        }

        String consulta2 = "ligando(" + simbolo + ").";
        Query q4 = new Query(consulta2);

        if (q4.hasSolution()) {
            System.out.println(language.text.get(110));
        }

        String consulta3 = "buscar_tejidos(" + objeto + ",T).";
        Query q5 = new Query(consulta3);
        ArrayList<String> tejidos = new ArrayList<>();
        for (int i = 0; i < q5.allSolutions().length; i++) {
            String aux = q5.allSolutions()[i].toString();
            aux = aux.replace("{", "").replace("}", "").replace("T=", "").replace(" ", "");

            if (!tejidos.contains(aux)) {
                tejidos.add(aux);
            }
        }

        if (tejidos.size() > 0) {
            System.out.print("Tejidos: ");
            System.out.println(tejidos.toString().replace("[", "").replace("]", ""));
        }

        return simbolo;
    }

    public void buscar_cadenas_pathwaysRest(String ruta) {
        borrar_archivo(ruta + "/cadenas_Pathways.txt");
        String Objrest = "'NR0B2'";
        String Objresti = "'bile acid'";
        ArrayList<pathway> pathways = cargarPatrones(ruta);
        int max = 3;

        ArrayList<pathway> pathWaysin = new ArrayList<>();
        pathWaysin.addAll(pathways);

        pathWaysin.removeIf(p -> !p.getObjetos().get(p.getObjetos().size() - 1).equals(Objrest));
        pathWaysin.removeIf(p -> !p.getObjetos().get(0).equals(Objresti));
//        pathWaysin.forEach(t -> System.out.println(t.getPatron()));
//        System.out.println("\n\n");
        for (pathway pat : pathWaysin) {
            //System.out.println("***" + pat.getPatron() + "\n\n");
            int tp = pat.getPatron().split(";").length;
            if (tp <= max) {
                cadenaPat(pathways, pat, new ArrayList<cadenas_pathway>(), ruta, Objrest, max);
            }
        }

    }

    public void cadenaPatRest(ArrayList<pathway> pathways, pathway pat, ArrayList<cadenas_pathway> cadena, String ruta, String objRest) {

    }

    public void buscar_cadenas_pathways(String ruta) {

        limpiarPantalla();

        Scanner lectura = new Scanner(System.in);
        boolean r = true;
        while (r) {

            System.out.println(language.text.get(111));
            System.out.println("\n" + language.text.get(0));
            System.out.println(language.text.get(112));
            System.out.println(language.text.get(4));
            String resp = lectura.nextLine();

            switch (resp) {
                case "1":
                    limpiarPantalla();
                    borrar_archivo(ruta + "/cadenas_Pathways.txt");
                    final ArrayList<pathway> pathways = cargarPatrones(ruta);
                    String Objrest;
                    while (true) {
                        System.out.print(language.text.get(113));
                        String text = lectura.nextLine();
                        Objrest = "'" + text + "'";
                        if (!Objrest.equals("")) {
                            break;
                        } else {
                            System.out.println(language.text.get(114));
                        }
                    }
                    int max;

                    while (true) {
                        System.out.print(language.text.get(115));
                        String text2 = lectura.nextLine();
                        try {
                            max = Integer.parseInt(text2);
                            break;
                        } catch (Exception e) {
                            System.out.println(language.text.get(114));
                        }
                    }
                    ArrayList<pathway> pathWaysin = new ArrayList<>();
                    pathWaysin.addAll(pathways);

                    for (pathway pat : pathWaysin) {
                        //System.out.println("***" + pat.getPatron() + "\n\n");
                        int tp = pat.getPatron().split(";").length;
                        if (tp <= max) {
                            cadenaPat(pathways, pat, new ArrayList<cadenas_pathway>(), ruta, Objrest, max);
                        }
                    }

                    break;
                case "0":
                    r = false;
                    break;
            }

        }

    }

    private void cadenaPat(ArrayList<pathway> pathways, pathway pat, ArrayList<cadenas_pathway> cadena, String ruta, String objRest, int max) {
        ArrayList<pathway> listP2 = new ArrayList<>();
        listP2.addAll(pathways);
        listP2.removeIf(p -> p.getObjetos().toString().equals(pat.getObjetos().toString()));
        String objin = pat.getObjetos().get(pat.getObjetos().size() - 1);

        listP2.stream().forEach((p) -> {

            int pt = p.getPatron().split(";").length;
            if (pt <= max) {
                ArrayList<cadenas_pathway> cadenaAux = new ArrayList<>();
                cadenaAux.addAll(cadena);
                //System.out.println("--" + p.getPatron());
                if (!pat.getObjetos().get(0).equals(p.getObjetos().get(0)) && validar_pathway(cadenaAux, p)) {
                    String consulta = "buscar_evento(" + objin + ",E," + p.getObjetos().get(0) + ").";
                    Query q2 = new Query(consulta);
                    String resp = "";
                    if (!objin.equals(p.getObjetos().get(0))) {
                        for (int i = 0; i < q2.allSolutions().length; i++) {
                            String even = q2.allSolutions()[i].toString();
                            even = even.replace("E", "").replace("=", "").replace("{", "").replace("}", "");
                            resp += objin + "," + even + "," + p.getObjetos().get(0) + "; ";
                        }
                    }
                    cadenas_pathway cad = new cadenas_pathway();

                    if (objin.equals(p.getObjetos().get(0))) {

                        if (validar_cadena(pat, p)) {
                            String sep[] = p.getPatron().split(";");
                            cad.setPathway_inicial(pat.getPatron());
                            cad.setPathway_final(p.getPatron());
                            cad.setEventos(sep[0]);
                            cadenaAux.add(cad);
                            //if (!p.getObjetos().get(p.getObjetos().size() - 1).equals(objRest) && !p.getObjetos().get(p.getObjetos().size() - 1).equals(pat.getObjetos().get(pat.getObjetos().size() - 1))) {
                            if (validarCamino(cadenaAux)) {
                                cadenaPat(listP2, p, cadenaAux, ruta, objRest, max);
                            } else {
                                imprimirCadena(cadenaAux, ruta, objRest);
                            }
                        }
                    } else if (!resp.equals("")) {
                        if (validar_cadena(pat, p)) {
                            cad.setPathway_inicial(pat.getPatron());
                            cad.setPathway_final(p.getPatron());
                            cad.setEventos(resp);
                            cadenaAux.add(cad);

                            //if (!p.getObjetos().get(p.getObjetos().size() - 1).equals(objRest) && !p.getObjetos().get(p.getObjetos().size() - 1).equals(pat.getObjetos().get(pat.getObjetos().size() - 1))) {
                            if (validarCamino(cadenaAux)) {
                                cadenaPat(listP2, p, cadenaAux, ruta, objRest, max);
                            } else {
                                imprimirCadena(cadenaAux, ruta, objRest);
                            }
                        }
                    }

                }
            }
        });

        imprimirCadena(cadena, ruta, objRest);

    }

    private void imprimirCadena(ArrayList<cadenas_pathway> cadena, String ruta, String objRest) {
        if (cadena.size() > 0) {
            String pin = cadena.get(0).getPathway_inicial();
            String pfin = cadena.get(cadena.size() - 1).getPathway_final();
            if (validar_cadena(pin, pfin, objRest)) {
                String cad = "-----------------------------------------------------------\n";
                for (int i = 0; i < cadena.size(); i++) {
                    // System.out.println(cadena.get(i).getPathway_inicial() + "  " + cadena.get(i).getPathway_final());

                    if (i == 0) {
                        cad += "Pathway=> " + cadena.get(i).getPathway_inicial() + "\n";
                        cad += "\n" + language.text.get(116) + " " + cadena.get(i).getEventos() + "\n\n";
                        cad += "Pathway=> " + cadena.get(i).getPathway_final() + "\n";
                    } else {
                        cad += "\n" + language.text.get(116) + " " + cadena.get(i).getEventos() + "\n\n";
                        cad += "Pathway=> " + cadena.get(i).getPathway_final() + "\n";
                    }
                }
                //System.out.println("********************************");
                escribirArchivo(cad, ruta + "/cadenas_Pathways.txt");
                System.out.println(cad);
                cadena.clear();
            }
        }
    }

    private boolean validar_pathway(ArrayList<cadenas_pathway> cadena, pathway pat) {
        boolean val = true;

        String objin = pat.getObjetos().get(0);

        for (cadenas_pathway object : cadena) {
            String sep[] = object.getPathway_inicial().split(";");
            String sep2[] = sep[0].split(",");
            if (sep2[0].equals(objin)) {
                val = false;
                break;
            }
        }

        return val;
    }

    private boolean validar_cadena(pathway in, pathway fin) {

        String obj_in = in.getObjetos().get(in.getObjetos().size() - 1);
        String obj_fin = fin.getObjetos().get(fin.getObjetos().size() - 1);

        if (!obj_in.equals(obj_fin)) {

            return true;
        }

        String sep1[] = in.getPatron().split(";");
        String eve_in = sep1[sep1.length - 1];
        String tip1 = tipo_Complejo(eve_in);

        String sep2[] = fin.getPatron().split(";");
        String eve_fin = sep2[sep2.length - 1];
        String tip2 = tipo_Complejo(eve_fin);

        //System.out.println(sep1[sep1.length - 1] + " " + tip1 + " --> " + sep2[sep2.length - 1] + "  " + tip2);
        if (!tip1.equals(tip2)) {
            return true;
        }

        return false;
    }

    private boolean validar_cadena(String in, String fin, String objRest) {

        String sep[] = in.split(";");
        String eve_in = sep[sep.length - 1];
        sep = fin.split(";");
        String eve_fin = sep[sep.length - 1];

        sep = eve_in.split(",");
        String Obji = sep[2];
        sep = eve_fin.split(",");
        String Objf = sep[2];

        if (!objRest.equals("") && !objRest.equals(Objf)) {
            return false;
        }

        if (!Obji.equals(Objf)) {
            return true;
        }

        String tip1 = tipo_Complejo(eve_in);
        String tip2 = tipo_Complejo(eve_fin);
        //System.out.println(eve_in + "  " + tip1 + "   " + eve_fin + "  " + tip2);
        if (!tip1.equals(tip2)) {
            return true;
        }

        return false;
    }

    private boolean validarCamino(ArrayList<cadenas_pathway> cadena) {
        boolean seguir = true;

        ArrayList<String> lista = new ArrayList<>();
        for (int i = 0; i < cadena.size(); i++) {

            if (i == 0) {
                String sep[] = cadena.get(i).getPathway_inicial().split(";");
                String sep2[] = sep[sep.length - 1].split(",");
                lista.add(sep2[2]);

                String sep3[] = cadena.get(i).getPathway_final().split(";");
                String sep4[] = sep3[sep3.length - 1].split(",");
                if (lista.contains(sep4[2])) {
                    return false;
                } else {
                    lista.add(sep4[2]);
                }

            } else {
                String sep3[] = cadena.get(i).getPathway_final().split(";");
                String sep4[] = sep3[sep3.length - 1].split(",");
                if (lista.contains(sep4[2])) {
                    return false;
                } else {
                    lista.add(sep4[2]);
                }
            }

        }

        return seguir;

    }

    private void escribirArchivo(String cadena, String archivo) {

        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter(archivo, true);
            pw = new PrintWriter(fichero);

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

    /*public void buscar_tejido(String ruta) {

        limpiarPantalla();
        Scanner lectura = new Scanner(System.in);
        boolean r = true;
        while (r) {
            System.out.println("Dado un receptor y su(s) complejo(s) asociado(s), ¿en cuáles tejidos se podría esperar que este (estos) conlleven a una regulación transcripcional positiva ( o negativa)?.");
            System.out.println("Seleccione una opcion.");
            System.out.println("1.- Ingresar receptor.");
            System.out.println("0.- Volver al menu anterior.");

            String resp = lectura.nextLine();

            switch (resp) {

                case "1":
                    limpiarPantalla();
                    System.out.print("Ingrese el simbolo del receptor. ");
                    String text = lectura.nextLine();
                    String receptor = "'" + text + "'";

                    ArrayList<pathway> pathway = new ArrayList<>();
                    pathway = cargarPatrones(ruta);

                    String consulta = "receptor(" + receptor + ").";

                    Query q2 = new Query(consulta);

                    ArrayList<objetos_Experto> minados = buscarOBJ(ruta);

                    if (q2.hasSolution()) {

                        pathway.forEach((pathway p) -> {

                            if (p.getObjetos().get(1).equals(receptor)) {
                                ArrayList<String> consultaEsp = new ArrayList<>();
                                for (int i = 0; i < p.getObjetos().size(); i++) {
                                    consultaEsp.add(construirConsulta(minados, p.getObjetos().get(i).toString().replace("'", "")));
                                }

                                ArrayList<ArrayList<String>> tejidos = new ArrayList<>();
                                consultaEsp.forEach((t) -> {
                                    Query q3 = new Query(t);
                                    ArrayList<String> listaT = new ArrayList<>();
                                    try {
                                        for (int i = 0; i < q3.allSolutions().length; i++) {
                                            String Tejido = q3.allSolutions()[i].toString().replace("{T=", "").replace("}", "").replace("'", "");
                                            if (!listaT.contains(Tejido) && !Tejido.equals("cellular_component")) {
                                                listaT.add(Tejido);
                                            }
                                        }
                                        tejidos.add(listaT);
                                    } catch (Exception e) {

                                    }

                                });

                                System.out.println("Patron:  " + p.getPatron());
                                System.out.println(p.getObjetos());
                                System.out.println("\nTejidos:\n");

                                cruzarTejidos(tejidos).forEach((cc) -> {
                                    System.out.println(cc);
                                });
                                System.out.println();

                            }
                        });

                    }
                    System.out.println("\n");
                    break;

                case "0":
                    r = false;
                    break;
            }
        }

    }*/

 /*private ArrayList<String> cruzarTejidos(ArrayList<ArrayList<String>> tejidos) {
        ArrayList<String> tejido = new ArrayList<>();
        try {
            for (String cc : tejidos.get(0)) {
                boolean coincidencia = true;
                for (int i = 1; i < tejidos.size(); i++) {
                    if (!tejidos.get(i).contains(cc)) {
                        coincidencia = false;
                    }
                }

                if (coincidencia) {
                    tejido.add(cc);
                }
            }
        } catch (Exception e) {

        }

        return tejido;
    }*/

 /*private String construirConsulta(ArrayList<objetos_Experto> minados, String obj) {
        String consulta = "";

        for (objetos_Experto minado : minados) {
            if (minado.getID().equals(obj)) {
                consulta += "(componente_celular('" + minado.getID() + "',T)";

                for (HGNC hgnc : minado.getHGNC()) {
                    if (!hgnc.getSimbolo().equals(obj)) {
                        consulta += ";componente_celular('" + hgnc.getSimbolo() + "',T)";
                    }
                }
                consulta += ")";
            }
        }
       
        return consulta;
    }*/
    public void buscar_tipo_ligando(String ruta) {

        limpiarPantalla();
        Scanner lectura = new Scanner(System.in);
        boolean r = true;
        while (r) {
            System.out.println(language.text.get(117));
            System.out.println(language.text.get(0));
            System.out.println(language.text.get(118));
            System.out.println(language.text.get(4));

            String resp = lectura.nextLine();

            switch (resp) {

                case "1":
                    limpiarPantalla();
                    System.out.print(language.text.get(119));
                    String text = lectura.nextLine();
                    String receptor = "'" + text + "'";

                    ArrayList<pathway> pathway = new ArrayList<>();
                    pathway = cargarPatrones(ruta);
                    ArrayList<String[]> Lista = new ArrayList<>();

                    String consulta = "receptor(" + receptor + ").";

                    Query q2 = new Query(consulta);

                    if (q2.hasSolution()) {

                        pathway.forEach((p) -> {

                            if (p.getObjetos().get(1).equals(receptor)) {

                                String res = tipo_Complejo(p.getPatron());
                                if (res.equals("estimulatorio")) {
                                    agregar_ligando_list(p.getObjetos().get(0), "agonista", Lista);

                                }

                                if (res.equals("inhibitorio")) {
                                    agregar_ligando_list(p.getObjetos().get(0), "antagonista", Lista);

                                }
                            }
                        });

                    }

                    System.out.println("Receptor: " + receptor);
                    Lista.forEach(t -> System.out.println("ligando: " + t[0] + "  funcion " + t[1]));
                    System.out.println("\n");

                    break;

                case "0":
                    r = false;
                    break;
            }
        }

    }

    private void agregar_ligando_list(String lig, String tipo, ArrayList<String[]> lista) {
        boolean existe = false;

        for (int i = 0; i < lista.size(); i++) {

            if (lista.get(i)[0].equals(lig)) {

                if (!tipo.equals(lista.get(i)[1])) {
                    lista.get(i)[1] = "mixta";
                }

                existe = true;
            }

        }

        if (!existe) {
            String[] aux = new String[2];
            aux[0] = lig;
            aux[1] = tipo;
            lista.add(aux);
        }

    }

    public void buscar_otros_ligandos(String ruta) {

        limpiarPantalla();
        Scanner lectura = new Scanner(System.in);
        boolean r = true;
        while (r) {
            System.out.println(language.text.get(120));
            System.out.println(language.text.get(0));
            System.out.println(language.text.get(118));
            System.out.println(language.text.get(4));

            String resp = lectura.nextLine();

            switch (resp) {

                case "1":

                    ArrayList<pathway> pathway = new ArrayList<>();
                    pathway = cargarPatrones(ruta);

                    System.out.print(language.text.get(119));
                    String text = lectura.nextLine();
                    String receptor = "'" + text + "'";

                    String consulta = "buscar_ligando_rec(" + receptor + ",L).";

                    Query q2 = new Query(consulta);
                    System.out.println("Receptor: " + receptor);
                    ArrayList<String> lig = new ArrayList<>();
                    for (int i = 0; i < q2.allSolutions().length; i++) {
                        String result = q2.allSolutions()[i].toString().replace("{", "").replace("}", "").replace("L=", "");
                        if (!lig.contains(result)) {
                            lig.add(result);
                            System.out.println("ligando: " + result);
                        }

                    }
                    System.out.println("\n");

                    break;
                case "0":
                    r = false;
                    break;
            }
        }

    }

    public void buscar_motivos(String ruta) {

        limpiarPantalla();
        Scanner lectura = new Scanner(System.in);
        boolean r = true;
        while (r) {
            System.out.println(language.text.get(121));
            System.out.println(language.text.get(0));
            System.out.println(language.text.get(118));
            System.out.println(language.text.get(4));

            String resp = lectura.nextLine();

            switch (resp) {

                case "1":

                    ArrayList<pathway> pathway = new ArrayList<>();
                    pathway = cargarPatrones(ruta);

                    System.out.print(language.text.get(119));
                    String text = lectura.nextLine();
                    String receptor = "'" + text + "'";

                    String consulta = "receptor(" + receptor + ").";

                    Query q2 = new Query(consulta);
                    limpiarPantalla();

                    if (q2.hasSolution()) {
                        System.out.println();
                        pathway.forEach((p) -> {
                            String ft = p.getObjetos().get(p.getObjetos().size() - 2);
                            String consulta2 = "transcription_factor(" + ft + ").";
                            Query q3 = new Query(consulta2);

                            if (p.getObjetos().get(1).equals(receptor) && q3.hasSolution()) {
                                System.out.println(language.text.get(122) + " " + receptor);
                                System.out.println(language.text.get(123) + " " + "'" + ft.replace("'", "") + "RE'");
                                System.out.println("pathway: " + p.getPatron() + "\n");
                            }

                        });

                    }

                    break;

                case "0":
                    r = false;
                    break;
            }
        }
    }

    public void buscar_receptores() {
        limpiarPantalla();
        Scanner lectura = new Scanner(System.in);
        boolean r = true;
        while (r) {
            System.out.println(language.text.get(124) + "\n");
            System.out.println(language.text.get(0));
            System.out.println(language.text.get(125));
            System.out.println(language.text.get(4));

            String resp = lectura.nextLine();

            switch (resp) {

                case "1":
                    System.out.println(language.text.get(138));
                    String lig = lectura.nextLine();

                    String consulta = "buscar_receptores('" + lig + "',R,E).";

                    Query q2 = new Query(consulta);
                    ArrayList<String> eventos = new ArrayList<>();
                    ArrayList<String> receptor = new ArrayList<>();
                    //{R='EGFR', E=induce}
                    for (int i = 0; i < q2.allSolutions().length; i++) {
                        String sep1[] = q2.allSolutions()[i].toString().replace("{", "").replace("}", "").split(",");
                        String R = sep1[0].split("=")[1];
                        String E = sep1[1].split("=")[1];

                        eventos.add(lig + " " + E + " " + R);
                        if (!receptor.contains(R)) {
                            receptor.add(R);
                        }
                    }

                    limpiarPantalla();
                    System.out.println(language.text.get(126) + " " + lig);
                    receptor.forEach(t -> System.out.println("receptor:  " + t));

                    System.out.print("\n " + language.text.get(127));
                    String resp2 = lectura.nextLine();
                    System.out.println();
                    if (resp2.equalsIgnoreCase("S") || resp2.equalsIgnoreCase("Y")) {
                        eventos.forEach(even -> System.out.println(even));
                    }
                    System.out.println("\n");
                    break;

                case "0":
                    r = false;
                    break;
            }

        }
    }

    public void buscar_proteinas_adicionales() {
        limpiarPantalla();

        Scanner lectura = new Scanner(System.in);
        boolean r = true;
        while (r) {
            System.out.println(language.text.get(128));
            System.out.println(language.text.get(0));
            System.out.println(language.text.get(118));
            System.out.println(language.text.get(4));

            String resp = lectura.nextLine();

            switch (resp) {

                case "1":
                    System.out.println(language.text.get(119));
                    String receptor = lectura.nextLine();
                    String consulta = "buscar_prot_adi('" + receptor + "',L,A).";

                    Query q2 = new Query(consulta);
                    ArrayList<String> lista = new ArrayList<>();

                    for (int i = 0; i < q2.allSolutions().length; i++) {
                        //System.out.println(q2.allSolutions()[i]);
                        String sep1[] = q2.allSolutions()[i].toString().replace("{", "").replace("}", "").split(",");
                        String cadena = "";

                        String A = null, L = null;
                        for (int j = 0; j < sep1.length; j++) {

                            String sep2[] = sep1[j].replace(" ", "").split("=");

                            if (sep2[0].equals("A")) {
                                A = sep2[1];
                                // System.out.println("A " + A);
                            }

                            if (sep2[0].equals("L")) {
                                L = sep2[1];
                                //System.out.println("L " + L);
                            }

                        }

                        cadena = L + "-->" + receptor + "-->" + A;

                        if (!lista.contains(cadena) && !A.equals(L)) {
                            lista.add(cadena);
                        }
                    }
                    limpiarPantalla();
                    System.out.println(language.text.get(129) + " " + receptor);
                    System.out.println(language.text.get(130) + "'\n");

                    lista.forEach(t -> System.out.println(t));
                    System.out.println("\n");
                    break;
                case "0":
                    r = false;
                    break;

            }
        }

        // System.out.println(lista);
    }

    public void interaccion_proteina_proteina(String ruta) {

        limpiarPantalla();
        Scanner lectura = new Scanner(System.in);
        boolean r = true;
        while (r) {
            System.out.println(language.text.get(131));
            System.out.println(language.text.get(0));
            System.out.println(language.text.get(132));
            System.out.println(language.text.get(4));

            String resp = lectura.nextLine();

            switch (resp) {
                case "1":
                    System.out.print(language.text.get(133));
                    String text = lectura.nextLine();
                    String ligando = "'" + text + "'";
                    System.out.print(language.text.get(119));
                    text = lectura.nextLine();
                    String receptor = "'" + text + "'";

                    ArrayList<pathway> pathways = new ArrayList<>();
                    pathways = cargarPatrones(ruta);
                    pathways.forEach((p) -> {
                        try {
                            if (ligando.equals(p.getObjetos().get(0)) && receptor.equals(p.getObjetos().get(1))) {
                                System.out.println("\n\n" + p.getPatron());
                                System.out.println(language.text.get(134) + " " + tipo_Complejo(p.getPatron()));
                            }
                        } catch (Exception e) {
                        }

                    });
                    System.out.println("\n");

                    break;
                case "0":
                    r = false;
                    break;
            }

        }

    }

    public void buscar_complejos(String ruta) {
        limpiarPantalla();

        Scanner lectura = new Scanner(System.in);
        boolean r = true;
        while (r) {

            System.out.println(language.text.get(135));
            System.out.println("\n" + language.text.get(0));
            System.out.println(language.text.get(112));
            System.out.println(language.text.get(4));
            String resp = lectura.nextLine();

            switch (resp) {
                case "1":
                    limpiarPantalla();
                    ArrayList<pathway> pathway = new ArrayList<>();
                    pathway = cargarPatrones(ruta);

                    pathway.forEach((p) -> {
                        ArrayList<String> objetos = p.getObjetos();
                        ArrayList<Integer> pos_enzymas = new ArrayList<>();

                        for (int j = 0; j < objetos.size() - 1; j++) {
                            String consulta = "enzyme(" + objetos.get(j) + ").";
                            Query q2 = new Query(consulta);
                            if (q2.hasSolution()) {
                                System.out.println("enzyne: " + objetos.get(j));
                                pos_enzymas.add(j);
                            }
                        }

                        if (pos_enzymas.size() > 0) {
                            System.out.println("Pathway:");
                            System.out.println(p.getPatron());
                            try {

                                separar_complejo(pos_enzymas, objetos, p.getPatron());
                            } catch (Exception e) {

                            }
                            System.out.println("\n");
                        }
                    });

                    break;
                case "0":
                    r = false;
                    break;
            }

        }

    }

    private void separar_complejo(ArrayList<Integer> pos, ArrayList<String> ObjPat, String Patron) {

        int posin = pos.get(0);
        int posfin = pos.get(pos.size() - 1) + 1;

        ArrayList<String> Complejo = new ArrayList<>();
        String primero = "";
        for (int i = 0; i <= posin; i++) {
            primero += ObjPat.get(i);
            if (i < posin) {
                primero += "--";
            }
            // Complejo.add(ObjPat.get(i));
        }

        if (primero != "") {
            Complejo.add(primero);
        }
        String segundo = "";
        for (int i = posfin - 1; i < ObjPat.size() - 1; i++) {
            segundo += ObjPat.get(i);
            if (i < ObjPat.size() - 2) {
                segundo += "--";
            }
        }
        if (segundo != "") {
            Complejo.add(segundo);
        }

        String tipo = tipo_Complejo(Patron);
        Complejo.forEach(c -> System.out.println("complejo: " + c + "  rol: " + tipo));

    }

    private String tipo_Complejo(String Patron) {
        String tipo = "";

        ArrayList<String> eventosUP = new ArrayList<>();
        eventosUP.add("activate");
        eventosUP.add("phosphorylate");
        eventosUP.add("regulate");
        eventosUP.add("transcriptional-activate");
        eventosUP.add("up-regulate");
        eventosUP.add("enhance");
        eventosUP.add("induce");
        eventosUP.add("lead");
        eventosUP.add("trigger");
        eventosUP.add("translate");
        eventosUP.add("transcribe");
        eventosUP.add("reactivate");
        eventosUP.add("promote");
        eventosUP.add("synthesize");
        eventosUP.add("bind");
        eventosUP.add("stimulate");

        ArrayList<String> eventosDOWN = new ArrayList<>();
        eventosDOWN.add("inhibit");
        eventosDOWN.add("down-regulate");
        eventosDOWN.add("repress");
        eventosDOWN.add("prevent");
        eventosDOWN.add("suppress");
        eventosDOWN.add("retain");
        eventosDOWN.add("inactivate");

        String separa[] = Patron.split(";");

        for (int i = 0; i < separa.length; i++) {
            String sep[] = separa[i].split(",");

            if (eventosUP.contains(sep[1])) {
                tipo = "estimulatorio";
                // break;
            }

            if (eventosDOWN.contains(sep[1])) {
                tipo = "inhibitorio";
                break;
            }
        }

        return tipo;
    }

    public ArrayList<pathway> cargarPatrones(String ruta) {
        ArrayList<pathway> pathways = new ArrayList<>();

        ObjectContainer db = Db4o.openFile(ruta + "/patrones.db");
        pathway pathway = new pathway();
        try {
            ObjectSet result = db.queryByExample(pathway);
            pathways.addAll(result);
        } catch (Exception e) {

        } finally {
            db.close();
        }

        return pathways;
    }

    public ArrayList<objetos_Experto> buscarOBJ(String ruta) {

        ArrayList<objetos_Experto> minados = new ArrayList<>();
        objetos_Experto objExp = new objetos_Experto();
        ObjectContainer db = Db4o.openFile(ruta + "/ObjH_E.db");

        try {
            ObjectSet result = db.queryByExample(objExp);
            minados.addAll(result);

        } catch (Exception e) {

        } finally {
            db.close();
        }

        factorTranscripcion FT = new factorTranscripcion();
        ArrayList<factorTranscripcion> FTs = new ArrayList<>();
        ObjectContainer db2 = Db4o.openFile(ruta + "/FT.db");
        try {
            ObjectSet result = db2.queryByExample(FT);
            FTs.addAll(result);
        } catch (Exception e) {

        } finally {
            db2.close();
        }

        FTs.forEach((t) -> {
            objetos_Experto obj = new objetos_Experto();
            obj.setID(t.getID());
            obj.setHGNC(t.getHGNC());
            minados.add(obj);

        });

        return minados;

    }

    private void limpiarPantalla() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void borrar_archivo(String nombre) {
        try {
            File ficherod = new File(nombre);
            ficherod.delete();
        } catch (Exception e) {

        }
    }

}
