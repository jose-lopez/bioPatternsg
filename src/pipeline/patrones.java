/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
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

    public void inferir_patrones(configuracion config) {

        ArrayList<String> objRestricion = menuRestricionObjetos();

        ArrayList<String> objCierre = menuMotivos();

        borrar_archivo("mineria/patrones.txt");
        borrar_archivo("mineria/patrones.db");

        String archivo = "[patronesJPL].";
        Query q = new Query(archivo);
        q.hasSolution();

        ArrayList<String> listaInicio = inicio(objRestricion);

        ArrayList<String> objEnlace = new ArrayList<>();

        listaInicio.parallelStream().forEach((obj) -> {
            String sep1[] = obj.split(",");
            if (!objEnlace.contains(sep1[2])) {
                objEnlace.add(sep1[2]);
            }
            // System.out.println("evento inicio:  " + obj);
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
        patron_2_eventos(objCierre, listaInicio, objEnlace);

        objEnlace.forEach(enlace -> intermedios(new ArrayList<String>(), enlace, FT, "", 0, listaInicio, listaFin, objCierre, objRestricion));

        guardar_Patron();

        config.setInferirPatrones(true);
        config.guardar();

    }

    private ArrayList<String> menuRestricionObjetos() {
        ArrayList<String> objetos = new ArrayList<>();
        Scanner lectura = new Scanner(System.in);
        boolean r = false;

        while (true) {
            System.out.print("*Desea restringir los pathway a un listado de objetos específicos ..S/N: ");
            String resp = lectura.nextLine();
            if (resp.equalsIgnoreCase("s")) {
                r = true;
                break;
            } else if (resp.equalsIgnoreCase("n")) {
                r = false;
                break;
            } else {
                System.out.println("Debe presionar las teclas (S) o (N) para seleccionar una opcion..");
            }
        }

        if (r) {
            while (true) {
                System.out.print("*Ingrese en nombre del objeto: ");
                String objeto = lectura.nextLine();
                if (!objeto.equals("")) {
                    objeto = objeto.replace("'", "");
                    objeto = "'" + objeto + "'";
                    objetos.add(objeto);

                    boolean r2 = false;
                    while (true) {
                        System.out.print("*Desea agregar otro objeto?  ..S/N: ");
                        String resp = lectura.nextLine();
                        if (resp.equalsIgnoreCase("s")) {
                            r2 = true;
                            break;
                        } else if (resp.equalsIgnoreCase("n")) {
                            r2 = false;
                            break;
                        } else {
                            System.out.println("Debe presionar las teclas (S) o (N) para seleccionar una opcion..");
                        }
                    }

                    if (!r2) {
                        break;
                    }

                } else {
                    System.out.println("Debe ingresar un nombre valido");
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
            System.out.print("*Desea agregar objeto de cierre a los pathway  ..S/N: ");
            String resp = lectura.nextLine();
            if (resp.equalsIgnoreCase("s")) {
                r = true;
                break;
            } else if (resp.equalsIgnoreCase("n")) {
                r = false;
                break;
            } else {
                System.out.println("Debe presionar las teclas (S) o (N) para seleccionar una opcion..");
            }
        }

        if (r) {
            while (true) {
                System.out.print("*Ingrese en nombre del objeto: ");
                String motivo = lectura.nextLine();
                if (!motivo.equals("")) {
                    motivo = motivo.replace("'", "");
                    motivo = "'" + motivo + "'";
                    motivos.add(motivo);

                    boolean r2 = false;
                    while (true) {
                        System.out.print("*Desea agregar otro objeto de cierre?  ..S/N: ");
                        String resp = lectura.nextLine();
                        if (resp.equalsIgnoreCase("s")) {
                            r2 = true;
                            break;
                        } else if (resp.equalsIgnoreCase("n")) {
                            r2 = false;
                            break;
                        } else {
                            System.out.println("Debe presionar las teclas (S) o (N) para seleccionar una opcion..");
                        }
                    }

                    if (!r2) {
                        break;
                    }

                } else {
                    System.out.println("Debe ingresar un nombre valido");
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

        for (int i = 0; i < q2.allSolutions().length; i++) {
            String even = separa_cadena(q2.allSolutions()[i].toString());

            if (!lista.contains(even)) {
                lista.add(even);
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

        for (int i = 0; i < q2.allSolutions().length; i++) {

            String even = separa_cadena(q2.allSolutions()[i].toString());

            if (!Objf.equals("")) {
                even = even + Objf;
            }
            if (!lista.contains(even)) {
                lista.add(even);
            }

        }

        return lista;

    }

    private void intermedios(ArrayList<String> enlista, String objini, ArrayList<String> FT, String patron, int max, ArrayList<String> listain, ArrayList<String> listafin, ArrayList<String> objCierre, ArrayList<String> objRest) {

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

        Query q2 = new Query(consulta);
        ArrayList<String> resp = new ArrayList<>();

        for (int i = 0; i < q2.allSolutions().length; i++) {
            //    System.out.println(q2.allSolutions()[i].toString());
            resp.add(q2.allSolutions()[i].toString());
        }
        
        
        //busqueda simple
        if (objRest.size() > 0) {
            resp.forEach((sol) -> {
                busqueda_nodo(max, sol, objini, cierre, FT, lista, listain, listafin, patron, objRest);

            });

        //busquedas en con multiples hilos
        } else {
            resp.parallelStream().forEach((sol) -> {
                busqueda_nodo(max, sol, objini, cierre, FT, lista, listain, listafin, patron, objRest);

            });

        }
       
    }

    private void busqueda_nodo(int max, String sol, String objini, ArrayList<String> cierre, ArrayList<String> FT, ArrayList<String> lista, ArrayList<String> listain, ArrayList<String> listafin, String patron, ArrayList<String> objRest) {

        int cont = 0;
        cont += max;
        String even = separa_cadena(sol);
        even = objini + even;
        String separa[] = even.split(",");
        cierre.removeIf(x -> x.equals(separa[2]));
        boolean pat = false;

        for (String factorT : FT) {
            if (separa[2].equals(factorT) && !lista.contains(separa[2]) && cierre.size() > 0) {
                ArrayList<String> listaAux = new ArrayList<>();
                listaAux.addAll(lista);
                listaAux.add(separa[2]);
                //listaAux.remove(0);
                encadenarPatron(patron + ";" + even, listain, listafin, cierre, listaAux);
                //pat = true;
            }

        }

        if (!lista.contains(separa[2]) && !pat && cierre.size() > 0) {
            //System.out.println(lista);
            //System.out.println(even);
            String patronaux = patron + ";" + even;
            if (cont < 10) {
                cont++;
                intermedios(lista, separa[2], FT, patronaux, cont, listain, listafin, cierre, objRest);

            }
        }

    }

    private void patron_2_eventos(ArrayList<String> finales, ArrayList<String> inicio, ArrayList<String> Objenlace) {
        ArrayList<String> lista = new ArrayList<>();

        Objenlace.forEach((E) -> {
            finales.forEach((F) -> {
                String consulta = "eventoEspecial(" + E + ",E," + F + ").";
                Query q2 = new Query(consulta);
                for (int i = 0; i < q2.allSolutions().length; i++) {
                    String evento = q2.allSolutions()[i].toString().replace("{", "").replace("}", "").replace("E", "").replace("=", "");
                    System.out.println(E + " " + evento + " " + F);
                    String fin = E + "," + evento + "," + F;
                    encadenarPatron2eventos(inicio, fin, E);
                }

            });

        });

    }

    private void encadenarPatron2eventos(ArrayList<String> inicio, String fin, String enlace) {

        ArrayList<String> patrones = new ArrayList<>();
        String sep[] = fin.split(",");

        inicio.forEach((i) -> {
            String sep1[] = i.split(",");
            if (enlace.equals(sep1[2]) && !sep[2].equals(sep1[0])) {

                String patron = i + ";" + fin;
                ArrayList<String> list = listarObetosPatron(patron);
                System.out.println("\n\n" + patron);
                System.out.println(list);
                agregar_a_lista(patron, list);
                //guardar_Patron(patron, list);
                escribirArchivo(patron, list.toString(), "patrones.txt");
            }
        });

    }

    //;'EGFR',regulate,'MAPK';'MAPK',regulate,'MEK';'MEK',phosphorylate,'Raf';'Raf',activate,'Ras';'Ras',activate,'CREB'
    private void encadenarPatron(String patron, ArrayList<String> listain, ArrayList<String> listafin, ArrayList<String> Objcierre, ArrayList<String> enlista) {

        String sep[] = patron.split(",");
        String primero = sep[0].replace(";", "");
        String ultimo = sep[sep.length - 1];

        ArrayList<String> patrones = new ArrayList<>();

        listain.parallelStream().forEach((objin) -> {
            String sep1[] = objin.split(",");
            if (primero.equals(sep1[2])) {
                if (!enlista.contains(sep1[0])) {
                    String pataux = objin + patron;
                    if (!patrones.contains(pataux)) {
                        patrones.add(pataux);
                        //System.out.println(pataux);
                    }
                }
            }
        });

        patrones.stream().forEach((Patron) -> {

            listafin.stream().forEach((fin) -> {
                String sep2[] = fin.split(",");
                if (ultimo.equals(sep2[0]) && Patron != null) {
                    String patronF = Patron + ";" + fin;
                    ArrayList<String> list = listarObetosPatron(patronF);

                    Objcierre.stream().forEach((cierre) -> {
                        if (cierre.equals(list.get(list.size() - 1))) {
                            System.out.println("\n\n" + patronF);
                            System.out.println(list);
                            agregar_a_lista(patronF, list);
                            escribirArchivo(patronF, list.toString(), "patrones.txt");
                            //guardar_Patron(patronF, list);
                        }

                    });
                }
            });

        });

    }

    private void agregar_a_lista(String patron, ArrayList<String> objetos) {
        // System.out.println(patron);
        pathway pathway = new pathway();
        pathway.setPatron(patron);
        pathway.setObjetos(objetos);
        patrones.add(pathway);

    }

    private void escribirArchivo(String cadena, String Lista, String archivo) {

        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter("mineria/" + archivo, true);
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

    private void guardar_Patron() {

        ObjectContainer db = Db4o.openFile("mineria/patrones.db");
        // pathway pathway = new pathway();
        //pathway.setPatron(patron);
        //pathway.setObjetos(objetos);

        try {
            db.store(patrones);
        } catch (Exception e) {
            System.out.println("Error al guardar patrones.db...");
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

        cadena = cadena.replace("{", "").replace("}", "").replace(" ", "");

        String separa[] = cadena.split(",");

        String obj1, eve, obj2;
        obj1 = eve = obj2 = "";

        for (int i = 0; i < separa.length; i++) {

            String sep[] = separa[i].split("=");

            if (sep[0].equals("A")) {
                obj1 = sep[1];
            }
            if (sep[0].equals("B")) {
                obj2 = sep[1];
            }
            if (sep[0].equals("E")) {
                eve = sep[1];
            }
        }

        even = obj1 + "," + eve + "," + obj2;
        return even;
    }

}
