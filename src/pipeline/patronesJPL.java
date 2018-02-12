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
import org.jpl7.Query;

/**
 *
 * @author yacson
 */
public class patronesJPL {

    public void buscar_patrones(ArrayList<String> objCierre, configuracion config) {

        borrar_archivo("mineria/patrones.txt");
        borrar_archivo("mineria/patrones.db");

        String archivo = "[patronesJPL].";
        Query q = new Query(archivo);
        q.hasSolution();

        ArrayList<String> listaInicio = inicio();

        ArrayList<String> objEnlace = new ArrayList<>();
        for (int i = 0; i < listaInicio.size(); i++) {

            String sep1[] = listaInicio.get(i).split(",");
           
            if (!objEnlace.contains(sep1[2])) {
                objEnlace.add(sep1[2]);
            }

            System.out.println("evento inicio:  " + listaInicio.get(i));
        }

        ArrayList<String> listaFin = new ArrayList<>();

        for (int i = 0; i < objCierre.size(); i++) {
            listaFin.addAll(fin(objCierre.get(i)));
        }

        if (objCierre.size() == 0) {
            listaFin.addAll(fin(""));
        }

        ArrayList<String> FT = new ArrayList<>();
        for (int i = 0; i < listaFin.size(); i++) {
            String sep[] = listaFin.get(i).split(",");
            if (!FT.contains(sep[0])) {
                FT.add(sep[0]);
            }

            if (!objCierre.contains(sep[2])) {
                objCierre.add(sep[2]);
            }

            System.out.println("evento fin:  " + listaFin.get(i));
        }

        for (int i = 0; i < objEnlace.size(); i++) {

            intermedios(new ArrayList<String>(), objEnlace.get(i), FT, "", 0, listaInicio, listaFin, objCierre);
        }

        config.setInferirPatrones(true);
        config.guardar();

    }

    private ArrayList<String> inicio() {
        ArrayList<String> lista = new ArrayList<>();

        String consulta = "inicio(A,E,B).";

        Query q2 = new Query(consulta);

        for (int i = 0; i < q2.allSolutions().length; i++) {

            String even = separa_cadena(q2.allSolutions()[i].toString());

            if (!lista.contains(even)) {
                lista.add(even);
            }

        }

        return lista;
    }

    private ArrayList<String> fin(String Objf) {
        ArrayList<String> lista = new ArrayList<>();
        String consulta;
        if (!Objf.equals("")) {
            consulta = "final(A,E," + Objf + ").";
        } else {
            consulta = "final(A,E,B).";

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

    private void intermedios(ArrayList<String> enlista, String objini, ArrayList<String> FT, String patron, int max, ArrayList<String> listain, ArrayList<String> listafin, ArrayList<String> objCierre) {

        ArrayList<String> lista = new ArrayList<>();
        ArrayList<String> cierre = new ArrayList<>();
        lista.addAll(enlista);
        lista.add(objini);

        cierre.addAll(objCierre);

        String consulta = "intermedios(" + objini + ",E,B).";

        Query q2 = new Query(consulta);

        for (int i = 0; i < q2.allSolutions().length; i++) {
            int cont = 0;
            cont += max;
            String even = separa_cadena(q2.allSolutions()[i].toString());
            even = objini + even;
            String separa[] = even.split(",");

            for (int j = 0; j < cierre.size(); j++) {
                if (cierre.get(j).equals(separa[2])) {
                    cierre.remove(j);
                }
            }

            boolean pat = false;
            for (int j = 0; j < FT.size(); j++) {
                if (separa[2].equals(FT.get(j)) && !lista.contains(separa[2]) && cierre.size() > 0) {
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
                    intermedios(lista, separa[2], FT, patronaux, cont, listain, listafin, cierre);
                }
            }

        }

    }

    //;'EGFR',regulate,'MAPK';'MAPK',regulate,'MEK';'MEK',phosphorylate,'Raf';'Raf',activate,'Ras';'Ras',activate,'CREB'
    private void encadenarPatron(String patron, ArrayList<String> listain, ArrayList<String> listafin, ArrayList<String> Objcierre, ArrayList<String> enlista) {

        String sep[] = patron.split(",");
        String primero = sep[0].replace(";", "");
        String ultimo = sep[sep.length - 1];

        ArrayList<String> patrones = new ArrayList<>();

        for (int i = 0; i < listain.size(); i++) {
            String sep1[] = listain.get(i).split(",");
            if (primero.equals(sep1[2])) {
                if (!enlista.contains(sep1[0])) {
                    String pataux = listain.get(i) + patron;
                    if (!patrones.contains(pataux)) {
                        patrones.add(pataux);
                        //System.out.println(pataux);
                    }
                }
            }
        }

        for (int i = 0; i < patrones.size(); i++) {
            for (int j = 0; j < listafin.size(); j++) {
                String sep2[] = listafin.get(j).split(",");
                if (ultimo.equals(sep2[0])) {
                    String patronF = patrones.get(i) + ";" + listafin.get(j);

                    ArrayList<String> list = listarObetosPatron(patronF);
                    for (int k = 0; k < Objcierre.size(); k++) {
                        if (Objcierre.get(k).equals(list.get(list.size() - 1))) {
                            System.out.println("\n\n" + patronF);
                            System.out.println(list);
                            escribirArchivo(patronF, list.toString(), "patrones.txt");
                            guardar_Patron(patronF, list);
                        }
                    }

                }

            }
        }

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
    
    private void guardar_Patron(String patron , ArrayList<String> objetos){
        
        ObjectContainer db = Db4o.openFile("mineria/patrones.db");
        pathway pathway = new pathway();
        pathway.setPatron(patron);
        pathway.setObjetos(objetos);
        
        try {
            db.store(pathway);
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
        for (int i = 0; i < sep1.length; i++) {
            String sep2[] = sep1[i].split(",");
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

        cadena = cadena.replace("{", "");
        cadena = cadena.replace("}", "");
        cadena = cadena.replace(" ", "");

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
