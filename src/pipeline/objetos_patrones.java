/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import configuracion.configuracion;
import configuracion.utilidades;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jpl7.Query;

/**
 *
 * @author yacson
 */
public class objetos_patrones {

    public void generar_archivo(configuracion config, String ruta) {
        String v = "style_check(-discontiguous).";
        Query q0 = new Query(v);
        q0.hasSolution();

        String MESH = "['" + ruta + "/ontologyMESH'].";
        String objMin = "['" + ruta + "/minedObjects'].";
        String wkr = "['" + ruta + "/wellKnownRules'].";
        String bc = "['" + ruta + "/kBase'].";

        boolean error = false;
        try {
            Query q1 = new Query(MESH);
            q1.hasSolution();
        } catch (Exception e) {
            System.out.println("Error al leer MESH");
            error = true;
        }

        try {
            Query q2 = new Query(objMin);
            q2.hasSolution();
        } catch (Exception e) {
            System.out.println("Error al lerr objetos minados");
            error = true;
        }

        try {
            Query q3 = new Query(wkr);
            q3.hasSolution();
        } catch (Exception e) {
            System.out.println("Error al leer WKR");
            error = true;
        }

        try {
            Query q4 = new Query(bc);
            q4.hasSolution();
        } catch (Exception e) {
            error = true;
        }

        if (!error) {
            String archivo = "[scripts/generatorPathwaysObjects].";
            Query q = new Query(archivo);
            q.hasSolution();

            ArrayList<String> lista = listaObjetos();
            //System.out.println(lista);

            clasificar_objetos(lista, ruta);

            q.close();

            config.setObjetosPatrones(true);
            config.guardar(ruta);
        }else{
           
           System.exit(0);
        }
    }

    private ArrayList<String> listaObjetos() {
        ArrayList<String> lista = new ArrayList<>();

        String consulta = "listar_eventos(A,B).";

        Query q2 = new Query(consulta);

        for (int i = 0; i < q2.allSolutions().length; i++) {
            String separa[] = q2.allSolutions()[i].toString().split(",");

            String separa1[] = separa[0].split("=");

            String objeto = separa1[1].replace("'", "");

            if (!lista.contains(objeto)) {
                lista.add(objeto);
            }

            String separa2[] = separa[1].split("=");

            objeto = separa2[1].replace("'", "");
            objeto = objeto.replace("}", "");

            if (!lista.contains(objeto)) {
                lista.add(objeto);
            }

        }

        q2.close();

        return lista;
    }

    private void clasificar_objetos(ArrayList<String> lista, String ruta) {

        crear_archivo(ruta);
        String ruta2 = ruta + "/pathwaysObjects.pl";
        new escribirBC("%//" + lista.toString(), ruta2);
        new utilidades().carga();
        for (String obj : lista) {

            String consulta = "p_ligand('" + obj + "').";
            Query q1 = new Query(consulta);

            if (q1.hasSolution()) {
                new escribirBC("ligand('" + obj + "').", ruta2);
                new utilidades().carga();
            }

            consulta = "p_receptor('" + obj + "').";
            Query q2 = new Query(consulta);

            if (q2.hasSolution()) {
                new escribirBC("receptor('" + obj + "').", ruta2);
                new utilidades().carga();
            }

            consulta = "p_transcription_factor('" + obj + "').";
            Query q3 = new Query(consulta);

            if (q3.hasSolution()) {
                new escribirBC("transcription_factor('" + obj + "').", ruta2);
            }

            consulta = "p_protein('" + obj + "').";
            Query q4 = new Query(consulta);

            if (q4.hasSolution()) {
                new escribirBC("protein('" + obj + "').", ruta2);
                new utilidades().carga();
            }

            consulta = "p_enzyme('" + obj + "').";
            Query q5 = new Query(consulta);

            if (q5.hasSolution()) {
                new escribirBC("enzyme('" + obj + "').", ruta2);
                new utilidades().carga();
            }

            q1.close();
            q2.close();
            q3.close();
            q4.close();

        }
        new escribirBC("\n%las siguientes lineas son para evitar errores en el proceso no deben ser modificadas", ruta2);
        new escribirBC("enzyme('').", ruta2);
        new escribirBC("protein('').", ruta2);
        new escribirBC("transcription_factor('').", ruta2);
        new escribirBC("receptor('').", ruta2);
        new escribirBC("ligand('').", ruta2);

    }

    private void crear_archivo(String ruta) {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter(ruta + "/pathwaysObjects.pl");
        } catch (IOException ex) {
            Logger.getLogger(minado_FT.class.getName()).log(Level.SEVERE, null, ex);
        }
        pw = new PrintWriter(fichero);
    }

}
