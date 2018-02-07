/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import java.io.File;
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

    public void prueba() {
        String archivo = "[consultas].";
        Query q = new Query(archivo);
        q.hasSolution();

        buscar_receptores();

    }

    public void buscar_receptores() {
        String consulta = "buscar_receptores('EGF').";

        Query q2 = new Query(consulta);

        for (int i = 0; i < q2.allSolutions().length; i++) {
            System.out.println(q2.allSolutions()[i]);
        }
    }

    public void buscar_patrones() {
        
        borrar_archivo("mineria/patrones.txt");

        String archivo = "[patronesJPL].";
        Query q = new Query(archivo);
        q.hasSolution();

        ArrayList<String> listaInicio = inicio();

        ArrayList<String> listaux1 = new ArrayList<>();
        for (int i = 0; i < listaInicio.size(); i++) {

            String sep1[] = listaInicio.get(i).split(",");
            String cadaux = sep1[0] + "," + sep1[2];

            if (!listaux1.contains(cadaux)) {
                listaux1.add(cadaux);
            }

            System.out.println("evento inicio:  " + listaInicio.get(i));
        }

        ArrayList<String> listaFin = fin("'SST'");

        ArrayList<String> FT = new ArrayList<>();
        for (int i = 0; i < listaFin.size(); i++) {
            String sep[] = listaFin.get(i).split(",");
            if (!FT.contains(sep[0])) {
                FT.add(sep[0]);
            }
            System.out.println("evento fin:  " + listaFin.get(i));
        }

        for (int i = 0; i < listaux1.size(); i++) {
            ArrayList<String> listain = new ArrayList<>();
            String sep[] = listaux1.get(i).split(",");
            listain.add(sep[0]);
            listain.add("'SST'");

            intermedios(listain, sep[1], FT, "", 0, listaInicio, listaFin);
        }

    }

    public ArrayList<String> inicio() {
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

    public ArrayList<String> fin(String Objf) {
        ArrayList<String> lista = new ArrayList<>();

        String consulta = "final(A,E," + Objf + ").";

        Query q2 = new Query(consulta);

        for (int i = 0; i < q2.allSolutions().length; i++) {

            String even = separa_cadena(q2.allSolutions()[i].toString());
            even = even + Objf;
            if (!lista.contains(even)) {
                lista.add(even);
            }

        }

        return lista;

    }

    public void intermedios(ArrayList<String> enlista, String objini, ArrayList<String> FT, String patron, int max, ArrayList<String> listain, ArrayList<String> listafin) {

        ArrayList<String> lista = new ArrayList<>();
        lista.addAll(enlista);
        lista.add(objini);

        String consulta = "intermedios(" + objini + ",E,B).";

        Query q2 = new Query(consulta);

        for (int i = 0; i < q2.allSolutions().length; i++) {
            int cont = 0;
            cont += max;
            String even = separa_cadena(q2.allSolutions()[i].toString());
            even = objini + even;

            String separa[] = even.split(",");
            boolean pat = false;
            for (int j = 0; j < FT.size(); j++) {
                if (separa[2].equals(FT.get(j)) && !lista.contains(separa[2])) {
                    encadenarPatron(patron + ";" + even, listain, listafin);
                    //pat = true;
                }

            }

            if (!lista.contains(separa[2]) && !pat) {
                //System.out.println(lista);
                //System.out.println(even);
                String patronaux = patron + ";" + even;
                if (cont < 10) {
                    cont++;
                    intermedios(lista, separa[2], FT, patronaux, cont, listain, listafin);
                }
            }

        }

    }

    //;'EGFR',regulate,'MAPK';'MAPK',regulate,'MEK';'MEK',phosphorylate,'Raf';'Raf',activate,'Ras';'Ras',activate,'CREB'
    public void encadenarPatron(String patron, ArrayList<String> listain, ArrayList<String> listafin) {

        String sep[] = patron.split(",");
        String primero = sep[0].replace(";", "");
        String ultimo = sep[sep.length - 1];

        ArrayList<String> patrones = new ArrayList<>();

        for (int i = 0; i < listain.size(); i++) {
            String sep1[] = listain.get(i).split(",");
            if (primero.equals(sep1[2])) {
                String pataux = listain.get(i) + patron;
                if (!patrones.contains(pataux)) {
                    patrones.add(pataux);
                    //System.out.println(pataux);
                }
            }
        }

        for (int i = 0; i < patrones.size(); i++) {
            for (int j = 0; j < listafin.size(); j++) {
                String sep2[] = listafin.get(j).split(",");
                if (ultimo.equals(sep2[0])) {
                    String patroF = patrones.get(i) + ";" + listafin.get(j);
                    System.out.println("\n\n" + patroF);
                    String list = listarObetosPtaron(patroF).toString();
                    System.out.println(listarObetosPtaron(patroF));
                    escribirArchivo(patroF,list,"patrones.txt");
                }

            }
        }

    }
    
    private void escribirArchivo(String cadena,String Lista, String archivo) {

        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter("mineria/"+archivo, true);
            pw = new PrintWriter(fichero);
            //System.out.println(cadena);
            pw.println("\n\n"+cadena);
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
    
     public void borrar_archivo(String nombre) {
        try {
            File ficherod = new File(nombre);
            ficherod.delete();
        } catch (Exception e) {

        }
    }


    public ArrayList<String> listarObetosPtaron(String patron) {
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

    public String separa_cadena(String cadena) {

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
