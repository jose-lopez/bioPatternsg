/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import configuracion.configuracion;
import configuracion.utilidades;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import org.jpl7.Query;
import org.jpl7.Term;


/**
 *
 * @author yacson
 */
public class patrones {

    ArrayList<pathway> patrones = new ArrayList<>();
    boolean detener = false;

    public void inferir_patrones(configuracion config, String ruta) {
        new utilidades().limpiarPantalla();
        utilidades.momento = "";
        utilidades.texto_carga = "";
        utilidades.texto_etapa = utilidades.idioma.get(153);
        System.out.println(utilidades.colorTexto1 + utilidades.titulo);
        System.out.println(utilidades.colorTexto1 + utilidades.proceso);
        System.out.println("\n" + utilidades.colorTexto2 + utilidades.texto_etapa);

        ArrayList<String> objRestricion = menuRestricionObjetos();

        ArrayList<String> objCierre = menuMotivos();

        resumirBaseC(ruta);

        // System.out.println(objRestricion);
        borrar_archivo(ruta + "/pathways.txt");
        borrar_archivo(ruta + "/pathways.db");

        String v = "style_check(-discontiguous).";
        Query q0 = new Query(v);
        q0.hasSolution();

        String objPatr = "['" + ruta + "/pathwaysObjects'].";
        Query q1 = new Query(objPatr);
        q1.hasSolution();

        cargarBaseC(ruta);

        String archivo = "[scripts/pathwaysJPL].";
        Query q = new Query(archivo);
        q.hasSolution();

        ArrayList<String> listaInicio = inicio(objRestricion);

        ArrayList<String> objEnlace = new ArrayList<>();

        listaInicio.parallelStream().forEach((obj) -> {
            String sep1[] = obj.split(",");
            if (!objEnlace.contains(sep1[2])) {
                objEnlace.add(sep1[2]);
            }
            //System.out.println("evento inicio:  " + obj);
        });

        ArrayList<String> listaFin = new ArrayList<>();

        objCierre.forEach(obj -> listaFin.addAll(fin(obj, objRestricion)));

        if (objCierre.size() == 0) {
            listaFin.addAll(fin("", objRestricion));
        }

        ArrayList<String> FT = new ArrayList<>();

        listaFin.forEach((String fin) -> {
            String sep[] = fin.split(",");
            if (!FT.contains(sep[0])) {
                FT.add(sep[0]);
            }

            if (!objCierre.contains(sep[2])) {
                objCierre.add(sep[2]);
            }

           // System.out.println("evento fin:  " + fin);
        });
        //patrones de 2 eventos
        patron_2_eventos(objCierre, listaInicio, objEnlace, ruta);

        objEnlace.forEach(enlace -> intermedios(new ArrayList<String>(), enlace, FT, "", 0, listaInicio, listaFin, objCierre, objRestricion, ruta));

        guardar_Patron(ruta);

        config.setInferirPatrones(true);
        config.guardar(ruta);

    }

    private void cargarBaseC(String ruta) {
        Scanner lectura = new Scanner(System.in);

        while (true) {
            System.out.print(utilidades.idioma.get(85));
            String resp = lectura.nextLine();
            if (resp.equalsIgnoreCase("s") || resp.equalsIgnoreCase("y")) {
                String bc = "['" + ruta + "/kBaseR'].";
                Query q2 = new Query(bc);
                q2.hasSolution();

                break;
            } else if (resp.equalsIgnoreCase("n")) {
                String bc = "['" + ruta + "/kBase'].";
                System.out.println(bc);
                Query q2 = new Query(bc);
                q2.hasSolution();

                break;
            } else {
                System.out.println(utilidades.idioma.get(53));
            }
        }
    }

    //Se ingresan los objetos que aparecen en los patrones
    private ArrayList<String> menuRestricionObjetos() {

        ArrayList<String> objetos = new ArrayList<>();
        Scanner lectura = new Scanner(System.in);
        boolean r = false;

        while (true) {

            System.out.print(utilidades.idioma.get(86));
            String resp = lectura.nextLine();
            if (resp.equalsIgnoreCase("s") || resp.equalsIgnoreCase("y")) {
                r = true;
                break;
            } else if (resp.equalsIgnoreCase("n")) {
                r = false;
                break;
            } else {
                System.out.println(utilidades.idioma.get(53));
            }
        }

        if (r) {
            while (true) {
                System.out.print(utilidades.idioma.get(87) + "\n");
                String objeto = lectura.nextLine();
                if (!objeto.equals("")) {
                    String sep[] = objeto.split(",");
                    for (int i = 0; i < sep.length; i++) {
                        String obj = sep[i].replace("'", "");
                        obj = "'" + obj + "'";
                        objetos.add(obj);
                    }
                    break;

                } else {
                    System.out.println(utilidades.idioma.get(88));
                }
            }
        }

        return objetos;
    }

    private ArrayList<String> menuMotivos() {
        ArrayList<String> motivos = new ArrayList<>();
        Scanner lectura = new Scanner(System.in);
        boolean r = false;

        while (true) {
            System.out.print(utilidades.idioma.get(89));
            String resp = lectura.nextLine();
            if (resp.equalsIgnoreCase("s") || resp.equalsIgnoreCase("y")) {
                r = true;
                break;
            } else if (resp.equalsIgnoreCase("n")) {
                r = false;
                break;
            } else {
                System.out.println(utilidades.idioma.get(53));
            }
        }

        if (r) {
            while (true) {
                System.out.print(utilidades.idioma.get(90));
                String motivo = lectura.nextLine();
                if (!motivo.equals("")) {
                    motivo = motivo.replace("'", "");
                    motivo = "'" + motivo + "'";
                    motivos.add(motivo);

                    boolean r2 = false;
                    while (true) {
                        System.out.print(utilidades.idioma.get(91));
                        String resp = lectura.nextLine();
                        if (resp.equalsIgnoreCase("s") || resp.equalsIgnoreCase("y")) {
                            r2 = true;
                            break;
                        } else if (resp.equalsIgnoreCase("n")) {
                            r2 = false;
                            break;
                        } else {
                            System.out.println(utilidades.idioma.get(53));
                        }
                    }

                    if (!r2) {
                        break;
                    }

                } else {
                    System.out.println(utilidades.idioma.get(92));
                }
            }
        }

        return motivos;
    }

    private ArrayList<String> inicio(ArrayList<String> objRest) {
        ArrayList<String> lista = new ArrayList<>();
        String consulta;
        if (objRest.size() > 0) {
            consulta = "inicio_rest(A,E,B," + objRest.toString() + ").";
        } else {
            consulta = "inicio(A,E,B).";
        }
        
        Query q2 = new Query(consulta);
        Map<String, Term>[] solutions = q2.allSolutions();
        
        for (int i = 0; i < solutions.length; i++) {

            try {
               // System.out.println(solutions[i].toString());
                String even = separa_cadena(solutions[i].toString());
                if (!lista.contains(even)) {
                    lista.add(even);
                }
            } catch (Exception e) {

            }

        }

        return lista;
    }

    private ArrayList<String> fin(String Objf, ArrayList<String> objRest) {
        ArrayList<String> lista = new ArrayList<>();
        String consulta;

        if (objRest.size() > 0) {
            if (!Objf.equals("")) {
                consulta = "final_rest(A,E," + Objf + "," + objRest + ").";
            } else {
                consulta = "final_rest(A,E,B," + objRest + ").";

            }
        } else {
            if (!Objf.equals("")) {
                consulta = "final(A,E," + Objf + ").";
            } else {
                consulta = "final(A,E,B).";

            }
        }

        Query q2 = new Query(consulta);
        Map<String, Term>[] solutions = q2.allSolutions();
        for (int i = 0; i < solutions.length; i++) {

            String even = separa_cadena(solutions[i].toString());

            if (!Objf.equals("")) {
                even = even + Objf;
            }
            if (!lista.contains(even)) {
                lista.add(even);
            }

        }

        return lista;

    }

    private void intermedios(ArrayList<String> enlista, String objini, ArrayList<String> FT, String patron, int max, ArrayList<String> listain, ArrayList<String> listafin, ArrayList<String> objCierre, ArrayList<String> objRest, String ruta) {

        ArrayList<String> lista = new ArrayList<>();
        ArrayList<String> cierre = new ArrayList<>();
        lista.addAll(enlista);
        lista.add(objini);
        String consulta;
        cierre.addAll(objCierre);
        if (objRest.size() > 0) {
            consulta = "intermedios_rest(" + objini + ",E,B," + objRest + ").";
        } else {
            consulta = "intermedios(" + objini + ",E,B).";
        }

        //System.out.println(consulta);
        Query q2 = new Query(consulta);
        ArrayList<String> resp = new ArrayList<>();
        Map<String, Term>[] solutions = q2.allSolutions();
        for (int i = 0; i < solutions.length; i++) {
            //   System.out.println(q2.allSolutions()[i].toString());
            resp.add(solutions[i].toString());
        }

        for (String sol : resp) {
            busqueda_nodo(max, sol, objini, cierre, FT, lista, listain, listafin, patron, objRest, ruta);
        }

    }

    private void busqueda_nodo(int max, String sol, String objini, ArrayList<String> cierre, ArrayList<String> FT, ArrayList<String> lista, ArrayList<String> listain, ArrayList<String> listafin, String patron, ArrayList<String> objRest, String ruta) {

        int cont = 0;
        cont += max;
        String even = separa_cadena(sol);
        even = objini + even;
        String separa[] = even.split(",");

        try {
            cierre.removeIf(x -> x.equals(separa[2]));
        } catch (Exception e) {

        }

        boolean pat = false;

        for (String factorT : FT) {
            //System.out.println(separa[2]+"  "+factorT);
            //
            if (separa[2].equals(factorT) && !lista.contains(separa[2]) && cierre.size() > 0) {

                ArrayList<String> listaAux = new ArrayList<>();
                listaAux.addAll(lista);
                listaAux.add(separa[2]);
                //listaAux.remove(0);
                try {

                    encadenarPatron(patron + ";" + even, listain, listafin, cierre, listaAux, ruta);
                } catch (Exception e) {
                }
                //pat = true;
            }

        }

        if (!lista.contains(separa[2]) && !pat && cierre.size() > 0) {
            // System.out.println(lista);
            // System.out.println(even);
            String patronaux = patron + ";" + even;
            if (cont < 10) {
                cont++;
                intermedios(lista, separa[2], FT, patronaux, cont, listain, listafin, cierre, objRest, ruta);

            }
        }

    }

    private void patron_2_eventos(ArrayList<String> finales, ArrayList<String> inicio, ArrayList<String> Objenlace, String ruta) {
        ArrayList<String> lista = new ArrayList<>();

        Objenlace.forEach((E) -> {
            finales.forEach((F) -> {
                String consulta = "eventoEspecial(" + E + ",E," + F + ").";

                Query q2 = new Query(consulta);
                 Map<String, Term>[] solutions = q2.allSolutions();
                for (int i = 0; i < solutions.length; i++) {
                    String evento = solutions[i].toString().replace("{", "").replace("}", "").replace("E", "").replace("=", "");
                    String fin = E + "," + evento + "," + F;
                    //System.out.println(fin);
                    encadenarPatron2eventos(inicio, fin, E,F, finales, ruta);
                }

            });

        });

    }

    private void encadenarPatron2eventos(ArrayList<String> inicio, String fin, String enlace,String objF, ArrayList<String> finales, String ruta) {

        ArrayList<String> patrones = new ArrayList<>();
        String sep[] = fin.split(",");
//&& (clasificarevento(fin)).equals("regulate") || clasificarevento(fin).endsWith("inhibit")

        if (sep[1].equals("bind") || clasificarevento(sep[1]).equals("regulate") || clasificarevento(sep[1]).equals("inhibit")) {
            //System.out.println("---" + fin);
            inicio.forEach((i) -> {
                String sep1[] = i.split(",");

                if (enlace.equals(sep1[2]) && !objF.equals(sep1[0])) {

                    String patron = i + ";" + fin;
                    ArrayList<String> list = listarObetosPatron(patron);
                    //System.out.println("\n\n" + patron);
                    //System.out.println(list);
                    agregar_a_lista(patron, list);
                    //guardar_Patron(patron, list);
                    escribirArchivo(patron, list.toString(), "pathways.txt", ruta);
                    new utilidades().carga();
                }
            });
        }

        if (!clasificarevento(fin).equals("bind") || !clasificarevento(fin).equals("associate")) {
            inicio.forEach((in) -> {
                String sep1[] = in.split(",");

                finales.forEach((f) -> {

                    String consulta = "finalEspecial(" + sep1[0] + ",E," + f + ").";
                    Query q2 = new Query(consulta);
                    Map<String, Term>[] solutions = q2.allSolutions();
                    for (int i = 0; i < solutions.length; i++) {
                        String evento = solutions[i].toString().replace("{", "").replace("}", "").replace("E", "").replace("=", "");
                        String Efin = sep1[0] + "," + evento + "," + f;
                        if (enlace.equals(sep1[2]) && sep[2].equals(f) && (clasificarevento(Efin).equals("regulate") || clasificarevento(Efin).equals("inhibit"))) {

                            String patron = in + ";" + fin;
                            ArrayList<String> list = listarObetosPatron(patron);
                            patron += ";" + Efin;
                            list.addAll(listarObetosPatron(Efin));
                            //System.out.println("\n\n" + patron);
                            //System.out.println(list);
                            agregar_a_lista(patron, list);
                            //guardar_Patron(patron, list);
                            escribirArchivo(patron, list.toString(), "pathways.txt", ruta);
                            new utilidades().carga();
                        }
                    }

                });

            });
        }

    }

    //;'EGFR',regulate,'MAPK';'MAPK',regulate,'MEK';'MEK',phosphorylate,'Raf';'Raf',activate,'Ras';'Ras',activate,'CREB'
    private void encadenarPatron(String patron, ArrayList<String> listain, ArrayList<String> listafin, ArrayList<String> Objcierre, ArrayList<String> enlista, String ruta) {

        String sep[] = patron.split(",");
        String primero = sep[0].replace(";", "");
        String ultimo = sep[sep.length - 1];

        ArrayList<String> patrones = new ArrayList<>();

        listain.forEach((objin) -> {
            String sep1[] = objin.split(",");
            if (primero.equals(sep1[2])) {
                if (!enlista.contains(sep1[0])) {
                    String pataux = objin + patron;
                    if (!patrones.contains(pataux)) {
                        patrones.add(pataux);

                    }
                }
            }
        });

        ArrayList<String> ObjC = new ArrayList<>();
        listafin.forEach((finales) -> {
            String sep2[] = finales.split(",");
            if (!ObjC.contains(sep2[2])) {
                ObjC.add(sep2[2]);
            }
        });

        patrones.forEach((Patron) -> {

            listafin.forEach((fin) -> {

                String sep2[] = fin.split(",");

                if (ultimo.equals(sep2[0]) && Patron != null) {

                    String patronF = Patron + ";" + fin;
                    ArrayList<String> list = listarObetosPatron(patronF);
                    ObjC.forEach((cierre) -> {

                        if (cierre.equals(list.get(list.size() - 1))) {
                            //System.out.println("\n\n" + patronF);
                            //System.out.println(list);
                            agregar_a_lista(patronF, list);
                            escribirArchivo(patronF, list.toString(), "pathways.txt", ruta);
                            new utilidades().carga();
                            //guardar_Patron(patronF, list);
                        }

                    });
                }
            });

        });

    }

    private void agregar_a_lista(String patron, ArrayList<String> objetos) {
        // System.out.println(patron);
        try {
            pathway pathway = new pathway();
            pathway.setPatron(patron);
            pathway.setObjetos(objetos);
            patrones.add(pathway);
        } catch (Exception e) {
            System.out.println("error");
        }

    }

    private void escribirArchivo(String cadena, String Lista, String archivo, String ruta) {

        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter(ruta + "/" + archivo, true);
            pw = new PrintWriter(fichero);
            //System.out.println(cadena);
            pw.println("\n\n" + cadena);
            pw.println(Lista);
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

    private void guardar_Patron(String ruta) {

        ObjectContainer db = Db4o.openFile(ruta + "/pathways.db");
        // pathway pathway = new pathway();
        //pathway.setPatron(patron);
        //pathway.setObjetos(objetos);

        try {
            db.store(patrones);
        } catch (Exception e) {
            System.out.println(utilidades.idioma.get(93));
        } finally {
            db.close();
        }

    }

    private void borrar_archivo(String nombre) {
        try {
            File ficherod = new File(nombre);
            ficherod.delete();
        } catch (Exception e) {

        }
    }

    private ArrayList<String> listarObetosPatron(String patron) {
        ArrayList<String> lista = new ArrayList<>();

        String sep1[] = patron.split(";");
        // System.out.println("patron: "+patron);             
        for (int i = 0; i < sep1.length; i++) {
            String sep2[] = sep1[i].split(",");
            //System.out.println(sep1[i]);
            if (!lista.contains(sep2[0])) {
                lista.add(sep2[0]);
            }
            if (!lista.contains(sep2[2])) {
                lista.add(sep2[2]);
            }

        }

        return lista;
    }

    private String separa_cadena(String cadena) {
        
        String even = "";

        cadena = cadena.replace("{", "").replace("}", "").replace(", ", ",");

        String separa[] = cadena.split(",");

        String obj1, eve, obj2;
        obj1 = eve = obj2 = "";

        for (int i = 0; i < separa.length; i++) {

            String sep[] = separa[i].split("=");

            if (sep[0].equals("A")) {
                obj1 = "'" + sep[1].replace("'", "") + "'";
            }
            if (sep[0].equals("B")) {
                obj2 = "'" + sep[1].replace("'", "") + "'";
            }
            if (sep[0].equals("E")) {
                eve = sep[1];
            }
        }

        even = obj1 + "," + eve + "," + obj2;

        return even;
    }

    public void resumirBaseC(String ruta) {
        borrar_archivo(ruta + "/kBaseR.pl");
        ArrayList<String> eventos = leerBaseC(ruta);
        ArrayList<String> resumenEven = new ArrayList<>();
        eventos.stream().forEach((e) -> {
            String sep[] = e.split(",");
            String obj1 = sep[0];
            String obj2 = sep[2];
            String eve = sep[1];
            eve = clasificarevento(eve);

            String eventoR = "event(" + obj1 + "," + eve + "," + obj2 + ")";

            if (!resumenEven.contains(eventoR)) {
                resumenEven.add(eventoR);
            }

        });

        new escribirBC("base([", ruta + "/kBaseR.pl");

        for (int i = 0; i < resumenEven.size() - 1; i++) {
            new escribirBC(resumenEven.get(i) + ",", ruta + "/kBaseR.pl");
        }
        new escribirBC(resumenEven.get(resumenEven.size() - 1), ruta + "/kBaseR.pl");

        new escribirBC("]).", ruta + "/kBaseR.pl");

    }

    public ArrayList<String> leerBaseC(String ruta) {
        ArrayList<String> eventos = new ArrayList<>();

        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {

            archivo = new File(ruta + "/kBase.pl");

            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String linea;
            while ((linea = br.readLine()) != null) {
                String texto = linea.replace("base([", "").replace("]).", "").replace("event(", "").replace(")", "");
                if (!texto.equals("")) {
                    eventos.add(texto);
                }
            }

        } catch (Exception e) {

            // e.printStackTrace();
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        return eventos;
    }

    public String clasificarevento(String rel) {
        String sinonimo = rel;

        ArrayList<String> eventosUP = new ArrayList<>();
        eventosUP.add("activate");
        eventosUP.add("increase");
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
        eventosUP.add("stimulate");

        ArrayList<String> eventosDOWN = new ArrayList<>();
        eventosDOWN.add("inhibit");
        eventosDOWN.add("down-regulate");
        eventosDOWN.add("repress");
        eventosDOWN.add("prevent");
        eventosDOWN.add("suppress");
        eventosDOWN.add("retain");
        eventosDOWN.add("decrease");
        eventosDOWN.add("inactivate");

        ArrayList<String> eventosMiddle = new ArrayList<>();
        eventosMiddle.add("require");
        eventosMiddle.add("interact");
        eventosMiddle.add("associate");
        eventosMiddle.add("phosphorylate");
        eventosMiddle.add("recruit");
        eventosMiddle.add("recognize");
        eventosMiddle.add("participate");

        if (eventosUP.contains(sinonimo)) {
            sinonimo = "regulate";
        } else if (eventosDOWN.contains(sinonimo)) {
            sinonimo = "inhibit";
        } else if (eventosMiddle.contains(sinonimo)) {
            sinonimo = "associate";
        }

        return sinonimo;
    }
}
