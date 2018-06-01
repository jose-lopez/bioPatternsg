/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

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

    public void generar_archivo(configuracion config) {

        String archivo = "[generaObjetosPatrones].";
        Query q = new Query(archivo);
        q.hasSolution();

        ArrayList<String> lista = listaObjetos();
        System.out.println(lista);

        clasificar_objetos(lista);

        q.close();

        config.setObjetosPatrones(true);
        config.guardar();
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

    private void clasificar_objetos(ArrayList<String> lista) {

        crear_archivo();
        String ruta = "objetos_patrones.pl";
        new escribirBC("%//" + lista.toString(), ruta);

        for (String obj : lista) {

            String consulta = "p_ligand('" + obj + "').";
            Query q1 = new Query(consulta);

            if (q1.hasSolution()) {
                new escribirBC("ligand('" + obj + "').", ruta);
            }

            consulta = "p_receptor('" + obj + "').";
            Query q2 = new Query(consulta);

            if (q2.hasSolution()) {
                new escribirBC("receptor('" + obj + "').", ruta);
            }

            consulta = "p_transcription_factor('" + obj + "').";
            Query q3 = new Query(consulta);

            if (q3.hasSolution()) {
                new escribirBC("transcription_factor('" + obj + "').", ruta);
            }

            consulta = "p_protein('" + obj + "').";
            Query q4 = new Query(consulta);

            if (q4.hasSolution()) {
                new escribirBC("protein('" + obj + "').", ruta);
            }

            consulta = "p_enzyme('" + obj + "').";
            Query q5 = new Query(consulta);

            if (q5.hasSolution()) {
                new escribirBC("enzyme('" + obj + "').", ruta);
            }

            q1.close();
            q2.close();
            q3.close();
            q4.close();

        }
        new escribirBC("\n%las siguientes lineas son para evitar errores en el proceso no deben ser modificadas", ruta);
        new escribirBC("enzyme('').", ruta);
        new escribirBC("protein('').", ruta);
        new escribirBC("transcription_factor('').", ruta);
        new escribirBC("receptor('').", ruta);
        new escribirBC("ligand('').", ruta);

    }

    private void crear_archivo() {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter("mineria/objetos_patrones.pl");
        } catch (IOException ex) {
            Logger.getLogger(minado_FT.class.getName()).log(Level.SEVERE, null, ex);
        }
        pw = new PrintWriter(fichero);
    }

}
