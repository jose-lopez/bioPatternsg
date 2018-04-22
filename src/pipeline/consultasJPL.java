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
import java.util.Map;
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
        buscar_tejido();

    }

    public void buscar_tejido() {
        ArrayList<pathway> pathway = new ArrayList<>();
        pathway = cargarPatrones();

        String receptor = "'EGFR'";

        String consulta = "receptor(" + receptor + ").";

        Query q2 = new Query(consulta);

        ArrayList<objetos_Experto> minados = buscarOBJ();

        if (q2.hasSolution()) {

            pathway.forEach((pathway p) -> {

                if (p.getObjetos().get(1).equals(receptor)) {
                    ArrayList<String> consultaEsp = new ArrayList<>();
                    for (int i = 0; i < p.getObjetos().size(); i++) {
                        consultaEsp.add(construirConsulta(minados, p.getObjetos().get(i).toString().replace("'", "")));
                    }

                    System.out.println("Patron:  " + p.getPatron());
                    System.out.println(p.getObjetos());
                    System.out.println("\nTejitos:\n");

                    ArrayList<ArrayList<String>> tejidos = new ArrayList<>();
                    consultaEsp.forEach((t) -> {
                        Query q3 = new Query(t);
                        ArrayList<String> listaT = new ArrayList<>();

                        for (int i = 0; i < q3.allSolutions().length; i++) {
                            String Tejido = q3.allSolutions()[i].toString().replace("{T=", "").replace("}", "").replace("'", "");
                            if (!listaT.contains(Tejido) && !Tejido.equals("cellular_component")) {
                                listaT.add(Tejido);
                            }
                        }
                        tejidos.add(listaT);

                    });
                    cruzarTejidos(tejidos).forEach((cc) -> {
                        System.out.println(cc);
                    });
                    System.out.println();

                }
            });

        }
    }

    private ArrayList<String> cruzarTejidos(ArrayList<ArrayList<String>> tejidos) {
        ArrayList<String> tejido = new ArrayList<>();

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

        return tejido;
    }

    private String construirConsulta(ArrayList<objetos_Experto> minados, String obj) {
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
    }

    public void buscar_tipo_ligando() {
        ArrayList<pathway> pathway = new ArrayList<>();
        pathway = cargarPatrones();
        ArrayList<String[]> Lista = new ArrayList<>();

        String receptor = "'EGFR'";

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

    public void buscar_otros_ligandos() {
        String receptor = "'EGFR'";
        String consulta = "buscar_ligando_rec(" + receptor + ",L).";

        Query q2 = new Query(consulta);
        System.out.println("Receptor: " + receptor);
        for (int i = 0; i < q2.allSolutions().length; i++) {
            String result = q2.allSolutions()[i].toString().replace("{", "").replace("}", "").replace("L=", "");
            System.out.println("ligando: " + result);
        }

    }

    public void buscar_motivos() {

        ArrayList<pathway> pathway = new ArrayList<>();
        pathway = cargarPatrones();

        String receptor = "'EGFR'";

        String consulta = "receptor(" + receptor + ").";

        Query q2 = new Query(consulta);

        if (q2.hasSolution()) {

            pathway.forEach((p) -> {

                if (p.getObjetos().get(1).equals(receptor)) {
                    System.out.println("receptor: " + receptor);
                    System.out.println("motivo: " + p.getObjetos().get(p.getObjetos().size() - 1));

                    System.out.println("pathway: " + p.getPatron() + "\n");
                }

            });

        }

    }

    public void buscar_receptores() {
        String consulta = "buscar_receptores('EGF').";

        Query q2 = new Query(consulta);

        for (int i = 0; i < q2.allSolutions().length; i++) {
            System.out.println(q2.allSolutions()[i]);
        }
    }

    public void buscar_proteinas_adicionales() {

        String R = "'EGFR'";

        String consulta = "buscar_prot_adi(" + R + ",L,A).";

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

            cadena = L + "-->" + R + "-->" + A;

            if (!lista.contains(cadena) && !A.equals(L)) {
                System.out.println(cadena);
                lista.add(cadena);
            }
        }

        // System.out.println(lista);
    }

    public void interaccion_proteina_proteina() {

        ArrayList<pathway> pathways = new ArrayList<>();
        pathways = cargarPatrones();
        String ligando = "'EGF'";
        String receptor = "'EGFR'";
        //String gen = "'SST'";

        pathways.forEach((p) -> {
            try {
                if (ligando.equals(p.getObjetos().get(0)) && receptor.equals(p.getObjetos().get(1))) {
                    System.out.println("\n\n" + p.getPatron());
                    System.out.println("Tipo: " + tipo_Complejo(p.getPatron()));
                }
            } catch (Exception e) {
            }

        });

    }

    public void buscar_complejos() {
        ArrayList<pathway> pathway = new ArrayList<>();
        pathway = cargarPatrones();

        pathway.forEach((p) -> {
            ArrayList<String> objetos = p.getObjetos();
            ArrayList<Integer> pos_enzymas = new ArrayList<>();

            for (int j = 0; j < objetos.size(); j++) {
                String consulta = "enzyme(" + objetos.get(j) + ").";
                Query q2 = new Query(consulta);
                if (q2.hasSolution()) {
                    System.out.println("enzyne: " + objetos.get(j));
                    pos_enzymas.add(j);
                }
            }

            System.out.println("Pathway:");
            System.out.println(p.getPatron());
            //complejos por enzimas
            separar_complejo(pos_enzymas, objetos, p.getPatron());

        });

    }

    private void separar_complejo(ArrayList<Integer> pos, ArrayList<String> ObjPat, String Patron) {

        int posin = pos.get(0);
        int posfin = pos.get(pos.size() - 1) + 1;

        ArrayList<String> primerComplejo = new ArrayList<>();

        for (int i = 0; i <= posin; i++) {
            primerComplejo.add(ObjPat.get(i));
        }

        ArrayList<String> segundoComplejo = new ArrayList<>();
        for (int i = posfin - 1; i < ObjPat.size() - 1; i++) {
            segundoComplejo.add(ObjPat.get(i));
        }

        String tipo = tipo_Complejo(Patron);

        System.out.println("complejo: " + primerComplejo + "  " + tipo);
        System.out.println("complejo: " + segundoComplejo + "  " + tipo);
        System.out.println("\n\n");

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

        ArrayList<String> eventosDOWN = new ArrayList<>();
        eventosDOWN.add("inhibit");
        eventosDOWN.add("down-regulate");
        eventosDOWN.add("repress");
        eventosDOWN.add("prevent");
        eventosDOWN.add("suppress");
        eventosDOWN.add("retain");

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

    private ArrayList<pathway> cargarPatrones() {
        ArrayList<pathway> pathways = new ArrayList<>();

        ObjectContainer db = Db4o.openFile("mineria/patrones.db");
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

    public ArrayList<objetos_Experto> buscarOBJ() {

        ArrayList<objetos_Experto> minados = new ArrayList<>();
        objetos_Experto objExp = new objetos_Experto();
        ObjectContainer db = Db4o.openFile("mineria/ObjH_E.db");

        try {
            ObjectSet result = db.queryByExample(objExp);
            minados.addAll(result);
        } catch (Exception e) {

        } finally {
            db.close();
        }

        factorTranscripcion FT = new factorTranscripcion();
        ArrayList<factorTranscripcion> FTs = new ArrayList<>();
        ObjectContainer db2 = Db4o.openFile("mineria/FT.db");
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

}
