/* 
 * bioPatternsg
 * BioPatternsg is a system that allows the integration and analysis of information related to the modeling of Gene Regulatory Networks (GRN).
 * Copyright (C) 2020
 * Jose Lopez (josesmooth@gmail.com), Jacinto Dávila (jacinto.davila@gmail.com), Yacson Ramirez (yacson.ramirez@gmail.com).
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package estructura;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import configuracion.utilidades;
import java.util.ArrayList;
import pipeline.escribirBC;

/**
 *
 * @author yacson
 */
public class ontologiaMESH {

    private String MESH;
    private String Nombre;
    private ArrayList<String> parent;

    public ontologiaMESH() {
        parent = new ArrayList<>();
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getMESH() {
        return MESH;
    }

    public void setMESH(String MESH) {
        this.MESH = MESH;
    }

    public ArrayList<String> getParent() {
        return parent;
    }

    public void setParent(ArrayList<String> parent) {
        this.parent = parent;
    }

    public String buscarNombre(String MESH, String ruta) {
        ontologiaMESH objeto = new ontologiaMESH();
        objeto.setMESH(MESH);
        objeto = consultarBD(objeto, ruta);
        return objeto.Nombre;
    }

    public void vaciar_pl(String MESH, String obj, ArrayList<String> listObj, String ruta) {
        ontologiaMESH objeto = new ontologiaMESH();
        objeto.setMESH(MESH);
        objeto = consultarBD(objeto, ruta);
        String ruta_wkr = ruta + "/wellKnownRules.pl";
        new utilidades().carga();
        if (obj != null && objeto.getNombre() != null) {
            String cadena = "is_a(\'" + obj.replace("\'", "") + "\',\'" + objeto.getNombre().replace("\'", "") + "\').";
            new escribirBC(cadena, ruta + "/ontologyMESH.pl");
            String[] separa = obj.split(",");
            crear_rama_artificial(obj, ruta_wkr, ruta + "/ontologyMESH.pl");
            if (separa[0].equals("Receptors")) {
                cadena = "is_a(\'" + obj.replace("\'", "") + "\',\'Receptors\').";
                new escribirBC(cadena, ruta + "/ontologyMESH.pl");
                String obj1 = procesarTexto("Receptors");
                String obj2 = procesarTexto(obj);
                String rule = obj1 + "(X):-" + obj2 + "(X).";
                new escribirBC(rule, ruta_wkr);

            }
            //procesando texto para crear las reglas en formato prolog
            String obj1 = procesarTexto(objeto.getNombre());
            String obj2 = procesarTexto(obj);

            String rule = obj1 + "(X):-" + obj2 + "(X).";
            new escribirBC(rule, ruta_wkr);

        }

        if (!listObj.contains(MESH)) {
            listObj.add(MESH);

            for (String mesh : objeto.getParent()) {
                try {
                    vaciar_pl(mesh, objeto.getNombre(), listObj, ruta);
                } catch (Exception e) {
                }

            }
        }

    }

    private void crear_rama_artificial(String obj, String ruta_wkr, String ruta_mesh) {
        String[] separa = obj.split(",");
        String cadena = null;
        if (separa[0].equals("Receptors")) {
            cadena = "is_a(\'" + obj.replace("\'", "") + "\',\'Receptors\').";
            new escribirBC(cadena, ruta_mesh);
            String obj1 = procesarTexto("Receptors");
            String obj2 = procesarTexto(obj);
            String rule = obj1 + "(X):-" + obj2 + "(X).";
            new escribirBC(rule, ruta_wkr);
        } else if (separa[0].equals("Adaptor Proteins")) {
            cadena = "is_a(\'" + obj.replace("\'", "") + "\',\'Adaptor Proteins\').";
            new escribirBC(cadena, ruta_mesh);
            String obj1 = procesarTexto("Adaptor Proteins");
            String obj2 = procesarTexto(obj);
            String rule = obj1 + "(X):-" + obj2 + "(X).";
            new escribirBC(rule, ruta_wkr);

        } else if (obj.equals("Intercellular Signaling Peptides and Proteins")) {
            String obj1 = procesarTexto("ligand");
            String obj2 = procesarTexto(obj);
            String rule = obj1 + "(X):-" + obj2 + "(X).";
            new escribirBC(rule, ruta_wkr);
        }
        /* else if (obj.equals("Circadian Rhythm Signaling Peptides and Proteins")) {
            String obj1 = procesarTexto("ligand");
            String obj2 = procesarTexto(obj);
            String rule = obj1 + "(X):-" + obj2 + "(X).";
            new escribirBC(rule, ruta_wnr);
        }  else if (obj.equals("Intracellular Signaling Peptides and Proteins")) {
            String obj1 = procesarTexto("ligand");
            String obj2 = procesarTexto(obj);
            String rule = obj1 + "(X):-" + obj2 + "(X).";
            new escribirBC(rule, ruta_wnr);
        }*/
        //-----------------------------------------------------------
        String rule = procesarTexto("ligand");
        new escribirBC(rule + "(\'\').", ruta_wkr);
        rule = procesarTexto("proteins");
        new escribirBC(rule + "(\'\').", ruta_wkr);
        rule = procesarTexto("transcription factors");
        new escribirBC(rule + "(\'\').", ruta_wkr);
        rule = procesarTexto("adaptor proteins");
        new escribirBC(rule + "(\'\').", ruta_wkr);
        rule = procesarTexto("receptors");
        new escribirBC(rule + "(\'\').", ruta_wkr);
        rule = procesarTexto("enzymes");
        new escribirBC(rule + "(\'\').", ruta_wkr);
        rule = procesarTexto("transcription factors");
        new escribirBC(rule + "(\'\').", ruta_wkr);

    }

    private ontologiaMESH consultarBD(ontologiaMESH obj, String ruta) {
        ontologiaMESH objeto = new ontologiaMESH();
        ObjectContainer db = Db4o.openFile(ruta + "/ontologyMESH.db");
        try {

            ObjectSet result = db.queryByExample(obj);
            while (result.hasNext()) {
                objeto = (ontologiaMESH) result.next();
            }
        } catch (Exception e) {
            System.out.println("Error al acceder a ontologyMESH.db");
        } finally {
            db.close();
        }
        return objeto;
    }

    public void buscar(String MESH, String ruta) {

        buscarObjeto(MESH, 0, "", ruta);

    }

    public ontologiaMESH buscarO(String MESH, String ruta) {
        ontologiaMESH ont = new ontologiaMESH();
        ont.setMESH(MESH);
        ont = consultarBD(ont, ruta);
        return ont;
    }

    private void buscarObjeto(String MESH, int nivel, String relacion, String ruta) {
        ontologiaMESH objeto = new ontologiaMESH();
        objeto.setMESH(MESH);
        objeto = consultarBD(objeto, ruta);

        for (int i = 0; i < nivel; i++) {
            System.out.print("      ");
        }

        System.out.println(relacion + objeto.getNombre());
        nivel++;

        for (String obj : objeto.getParent()) {
            if (!obj.equals("1000048")) {
                buscarObjeto(obj, nivel, "is a->  ", ruta);
            }

        }
    }

    public String procesarTexto(String texto) {
        String aux = texto.toLowerCase();
        aux = aux.replace(" ", "_");
        aux = aux.replace("-", "_");
        aux = aux.replace(",", "_");
        aux = aux.replace("(", "_");
        aux = aux.replace(")", "_");
        aux = aux.replace("'", "");
        aux = aux.replace("+", "");
        aux = aux.replace("__", "_");
        aux = aux.replace(".", "_");
        aux = "wkr_" + aux;
        return aux;
    }
}
