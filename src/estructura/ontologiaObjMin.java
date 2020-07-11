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
import servicios.lecturas_QuickGO;
import servicios.lecturas_MESH;

/**
 *
 * @author yacson
 */
public class ontologiaObjMin {

    private String nombre;
    private ArrayList<String> funcionMolecular;
    private ArrayList<String> procesoBiologico;
    private ArrayList<String> componenteCelular;
    private ArrayList<String> Parent;

    public ontologiaObjMin() {
        funcionMolecular = new ArrayList<>();
        procesoBiologico = new ArrayList<>();
        componenteCelular = new ArrayList<>();
        Parent = new ArrayList<>();
    }

    public void guardarObjeto(ontologiaObjMin objeto, boolean GO, boolean mesh, String ruta) {

        ObjectContainer db = Db4o.openFile(ruta + "/minedObjectsOntology.db");
        try {

            ObjectSet result = db.queryByExample(this);
            if (!result.hasNext()) {

                if (GO) {

                    funcionMolecular.forEach(fm -> buscarOntologiaGO(fm, ruta));

                    procesoBiologico.forEach(pb -> buscarOntologiaGO(pb, ruta));

                    componenteCelular.forEach(cc -> buscarOntologiaGO(cc, ruta));

                }
                if (mesh) {
                    Parent.forEach(parent -> buscarOntologiaMESH(parent, ruta));
                }

                db.store(this);
            }

        } catch (Exception e) {
            // System.out.println("Error al guardar en minedObjectsOntology.db");
        } finally {
            db.close();
        }

    }

    public void buscarOntologiaMESH(String MESH, String ruta) {
        new utilidades().carga();
        ontologiaMESH ontologia = new ontologiaMESH();
        lecturas_MESH letMESH = new lecturas_MESH();
        ontologia.setMESH(MESH);

        if (!buscarObjeto(ontologia, ruta) && !ontologia.getMESH().equals("1000048")) {

            ontologia = letMESH.obtenerOntologia(MESH);

            ontologia.getParent().forEach(ont -> buscarOntologiaMESH(ont, ruta));

            guardar_Ontologia(ontologia, ruta);

        }

    }

    public void buscarOntologiaGO(String GO, String ruta) {
        new utilidades().carga();
        // System.out.println(GO);
        ontologiaGO ontologia = new ontologiaGO();
        lecturas_QuickGO letQGO = new lecturas_QuickGO();
        ontologia.setGO(GO);
        if (!buscarObjeto(ontologia, ruta)) {
            ontologia = letQGO.obtenerOntologia(GO);

            ontologia.getIs_a().forEach(ont -> buscarOntologiaGO(ont, ruta));

            ontologia.getPart_of().forEach(ont -> buscarOntologiaGO(ont, ruta));

            ontologia.getRegulates().forEach(ont -> buscarOntologiaGO(ont, ruta));

            ontologia.getNegatively_regulates().forEach(ont -> buscarOntologiaGO(ont, ruta));

            ontologia.getPositively_regulates().forEach(ont -> buscarOntologiaGO(ont, ruta));

            ontologia.getCapable_of().forEach(ont -> buscarOntologiaGO(ont, ruta));

            ontologia.getCapable_of_part_of().forEach(ont -> buscarOntologiaGO(ont, ruta));

            ontologia.getOccurs_in().forEach(ont -> buscarOntologiaGO(ont, ruta));

            guardar_Ontologia(ontologia, ruta);
        }

    }

    private void guardar_Ontologia(ontologiaGO ontologia, String ruta) {

        ObjectContainer db = Db4o.openFile(ruta + "/ontologyGO.db");
        try {
            db.store(ontologia);
            //System.out.println("Guardando: " + ontologia.getNombre() + " " + ontologia.getGO());
        } catch (Exception e) {
            //System.out.println("Error al guardar en ontologyGO.db...");
        } finally {
            db.close();
        }

    }

    private void guardar_Ontologia(ontologiaMESH ontologia, String ruta) {

        ObjectContainer db = Db4o.openFile(ruta + "/ontologyMESH.db");
        try {
            db.store(ontologia);
            //System.out.println("Guardando: " + ontologia.getNombre() + " " + ontologia.getMESH());
        } catch (Exception e) {
            // System.out.println("Error al guardar en ontologyMESH.db...");
        } finally {
            db.close();
        }

    }

    public boolean buscarObjeto(ontologiaObjMin objeto, String ruta) {
        boolean encontrado = false;
        try {
            ObjectContainer db = Db4o.openFile(ruta + "/minedObjectsOntology.db");
            try {

                ObjectSet result = db.queryByExample(objeto);
                if (result.hasNext()) {
                    encontrado = true;
                }
            } catch (Exception e) {
                System.out.println("Error al acceder a minedObjectsOntology.db");
            } finally {
                db.close();
            }
        } catch (Exception e) {

        }
        return encontrado;
    }

    public boolean buscarObjeto(ontologiaGO objeto, String ruta) {
        boolean encontrado = false;

        ObjectContainer db = Db4o.openFile(ruta + "/ontologyGO.db");
        try {

            ObjectSet result = db.queryByExample(objeto);
            if (result.hasNext()) {
                encontrado = true;
            }
        } catch (Exception e) {
            System.out.println("Error al acceder a ontologyGO.db");
        } finally {
            db.close();
        }

        return encontrado;
    }

    public boolean buscarObjeto(ontologiaMESH objeto, String ruta) {
        boolean encontrado = false;
        ObjectContainer db = Db4o.openFile(ruta + "/ontologyMESH.db");
        try {

            ObjectSet result = db.queryByExample(objeto);
            if (result.hasNext()) {
                encontrado = true;
            }
        } catch (Exception e) {
            System.out.println("Error al acceder a ontologyMESH.db");
        } finally {
            db.close();
        }

        return encontrado;
    }

    public void buscarGO(String nombre, String restriccion, String ruta) {
        ontologiaObjMin objetoGO = new ontologiaObjMin();
        objetoGO.setNombre(nombre);
        ObjectContainer db = Db4o.openFile(ruta + "/minedObjectsOntology.db");
        try {

            ObjectSet result = db.queryByExample(objetoGO);
            while (result.hasNext()) {
                objetoGO = (ontologiaObjMin) result.next();
                break;
            }
        } catch (Exception e) {
            System.out.println("Error al acceder a mineria/Ontologia.db");
        } finally {
            db.close();
        }
        imprimirTodo(objetoGO, restriccion, true, true, ruta);
    }

    public void imprimirTodo(String ruta) {
        ontologiaObjMin objeto = new ontologiaObjMin();
        ObjectContainer db = Db4o.openFile(ruta + "/minedObjectsOntology.db");
        try {

            ObjectSet result = db.queryByExample(objeto);
            while (result.hasNext()) {
                ontologiaObjMin obj = (ontologiaObjMin) result.next();
                System.out.println(obj.getNombre());
                imprimirTodo(obj, null, true, true, ruta);
            }
        } catch (Exception e) {
        } finally {
            db.close();
        }
    }

    public void imprimirTodo(String tipo, String restriccion, String ruta) {

        ontologiaObjMin objeto = new ontologiaObjMin();
        ObjectContainer db = Db4o.openFile(ruta + "/minedObjectsOntology.db");
        try {

            ObjectSet result = db.queryByExample(objeto);
            while (result.hasNext()) {
                ontologiaObjMin obj = (ontologiaObjMin) result.next();
                if (tipo == null) {
                    imprimirTodo(obj, restriccion, true, true, ruta);
                } else if (tipo.equals("F")) {
                    imprimirFuncionMolecular(obj, restriccion);
                } else if (tipo.equals("P")) {
                    imprimirProcesobiologico(obj, restriccion);
                } else if (tipo.equals("C")) {
                    imprimirComponenteCelular(obj, restriccion);
                } else if (tipo.equals("M")) {
                    imprimirMESH(obj, ruta);
                }

            }
        } catch (Exception e) {
            System.out.println("Error al acceder a " + ruta + "/minedObjectsOntology.db");
        } finally {
            db.close();
        }
    }

    public void vaciarOntologia_pl(boolean GO, boolean MESH, String ruta) {
        ontologiaObjMin objeto = new ontologiaObjMin();
        ObjectContainer db = Db4o.openFile(ruta + "/minedObjectsOntology.db");
        ontologiaGO ontologiaGO = new ontologiaGO();
        ontologiaMESH ontologiaMESH = new ontologiaMESH();

        try {

            ObjectSet result = db.queryByExample(objeto);
            while (result.hasNext()) {
                ontologiaObjMin obj = (ontologiaObjMin) result.next();
                ArrayList<String> ListaObj = new ArrayList<>();
                vaciarpl(obj, ruta);
                if (GO) {

                    final ArrayList<ontologiaGO> ontGO = ontologiaGO.getOntGO(ruta);
                    try {
                        obj.funcionMolecular.forEach(fm -> ontologiaGO.vaciar_pl(ontGO, fm, null, null, ListaObj, ruta + "/ontologyGO.pl"));
                    } catch (Exception e) {
                    }

                    try {
                        obj.procesoBiologico.forEach(pb -> ontologiaGO.vaciar_pl(ontGO, pb, null, null, ListaObj, ruta + "/ontologyGO.pl"));
                    } catch (Exception e) {
                    }

                    try {
                        obj.componenteCelular.forEach(cc -> ontologiaGO.vaciar_pl(ontGO, cc, null, null, ListaObj, ruta + "/ontologyGO.pl"));
                    } catch (Exception e) {
                    }

                }

                if (MESH) {
                    try {
                        obj.Parent.forEach(p -> ontologiaMESH.vaciar_pl(p, null, ListaObj, ruta));
                    } catch (Exception e) {
                    }

                }

            }
        } catch (Exception e) {

        } finally {
            db.close();
        }
    }

    private void vaciarpl(ontologiaObjMin obj, String ruta) {
        String MESH = "";
        ontologiaGO GO = new ontologiaGO();
        ontologiaMESH mesh = new ontologiaMESH();
        new utilidades().carga();
        new escribirBC("objeto(\'" + obj.nombre.replace("\'", "") + "\').", ruta + "/ontologyMESH.pl");

        try {
            for (String fm : obj.funcionMolecular) {
                new escribirBC("leaf(\'" + obj.nombre.replace("\'", "") + "\',\'" + GO.buscar(fm, ruta).replace("\'", "") + "\').", ruta + "/ontologyGO.pl");
                new escribirBC("fm(\'" + GO.buscar(fm, ruta).replace("\'", "") + "\').", ruta + "/ontologyGO.pl");
            }
        } catch (Exception e) {

        }
        try {
            for (String pb : obj.procesoBiologico) {
                new escribirBC("leaf(\'" + obj.nombre.replace("\'", "") + "\',\'" + GO.buscar(pb, ruta).replace("\'", "") + "\').", ruta + "/ontologyGO.pl");
                new escribirBC("bp(\'" + GO.buscar(pb, ruta).replace("\'", "") + "\').", ruta + "/ontologyGO.pl");
            }
        } catch (Exception e) {

        }
        try {
            for (String cc : obj.componenteCelular) {
                new escribirBC("leaf(\'" + obj.nombre.replace("\'", "") + "\',\'" + GO.buscar(cc, ruta).replace("\'", "") + "\').", ruta + "/ontologyGO.pl");
                new escribirBC("cc(\'" + GO.buscar(cc, ruta).replace("\'", "") + "\').", ruta + "/ontologyGO.pl");
            }
        } catch (Exception e) {

        }
        try {
            for (String m : obj.Parent) {
                MESH = mesh.buscarNombre(m, ruta).replace("\'", "");
            }
        } catch (Exception e) {

        }

        if (!MESH.equals("")) {
            new escribirBC("is_a(\'" + obj.nombre.replace("\'", "") + "\',\'" + MESH + "\').", ruta + "/ontologyMESH.pl");
            String aux = mesh.procesarTexto(MESH);
            new escribirBC(aux + "(\'" + obj.nombre.replace("\'", "") + "\').", ruta + "/minedObjects.pl");
        }

    }

    private void imprimirTodo(ontologiaObjMin obj, String restriccion, boolean GO, boolean MESH, String ruta) {
        if (GO) {
            System.out.println("\n________________________________________________________________");
            System.out.println("Funcion Molecular:");
            for (int i = 0; i < obj.funcionMolecular.size(); i++) {
                ontologiaGO objeto = new ontologiaGO();
                objeto.buscar(obj.funcionMolecular.get(i), restriccion, ruta);

            }
            System.out.println("\n_______________________________________________________________");
            System.out.println("Proceso biologico:");
            for (int i = 0; i < obj.procesoBiologico.size(); i++) {
                ontologiaGO objeto = new ontologiaGO();
                objeto.buscar(obj.procesoBiologico.get(i), restriccion, ruta);
            }
            System.out.println("\n________________________________________________________________");
            System.out.println("Componente celular:");
            for (int i = 0; i < obj.componenteCelular.size(); i++) {
                ontologiaGO objeto = new ontologiaGO();
                objeto.buscar(obj.componenteCelular.get(i), restriccion, ruta);
            }
        }

        if (MESH) {
            // System.out.println("\n________________________________________________________________");
            imprimirMESH(obj, ruta);
        }
    }

    public void buscarObjeto(String nombre, boolean GO, boolean MESH, String ruta) {

        ontologiaObjMin objeto = new ontologiaObjMin();
        objeto.setNombre(nombre);
        ObjectContainer db = Db4o.openFile(ruta + "/minedObjectsOntology.db");
        try {

            ObjectSet result = db.queryByExample(objeto);
            while (result.hasNext()) {
                objeto = (ontologiaObjMin) result.next();

                // break;
            }
            imprimirTodo(objeto, null, GO, MESH, ruta);
        } catch (Exception e) {
        } finally {
            db.close();
        }

    }

    private void imprimirFuncionMolecular(ontologiaObjMin obj, String restriccion) {
        System.out.println("\n________________________________________________________________");
        System.out.println(obj.getNombre());
        System.out.println("Funcion Molecular:");
        for (int i = 0; i < obj.funcionMolecular.size(); i++) {
            ontologiaGO objeto = new ontologiaGO();
            objeto.buscar(obj.funcionMolecular.get(i), restriccion);
        }
    }

    private void imprimirProcesobiologico(ontologiaObjMin obj, String restriccion) {
        System.out.println("\n_______________________________________________________________");
        System.out.println("Proceso biologico:");
        for (int i = 0; i < obj.procesoBiologico.size(); i++) {
            ontologiaGO objeto = new ontologiaGO();
            objeto.buscar(obj.procesoBiologico.get(i), restriccion);
        }
    }

    private void imprimirComponenteCelular(ontologiaObjMin obj, String restriccion) {
        System.out.println("\n________________________________________________________________");
        System.out.println("Componente celular:");
        for (int i = 0; i < obj.componenteCelular.size(); i++) {
            ontologiaGO objeto = new ontologiaGO();
            objeto.buscar(obj.componenteCelular.get(i), restriccion);
        }
    }

    private void imprimirMESH(ontologiaObjMin obj, String ruta) {
        System.out.println("\n________________________________________________________________");
        System.out.println("Arbol de Identidad:");
        for (int i = 0; i < obj.Parent.size(); i++) {
            ontologiaMESH objeto = new ontologiaMESH();
            objeto.buscar(obj.Parent.get(i), ruta);
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<String> getFuncionMolecular() {
        return funcionMolecular;
    }

    public void setFuncionMolecular(ArrayList<String> funcionMolecular) {
        this.funcionMolecular = funcionMolecular;
    }

    public ArrayList<String> getProcesoBiologico() {
        return procesoBiologico;
    }

    public void setProcesoBiologico(ArrayList<String> procesoBiologico) {
        this.procesoBiologico = procesoBiologico;
    }

    public ArrayList<String> getComponenteCelular() {
        return componenteCelular;
    }

    public void setComponenteCelular(ArrayList<String> componenteCelular) {
        this.componenteCelular = componenteCelular;
    }

    public ArrayList<String> getParent() {
        return Parent;
    }

    public void setParent(ArrayList<String> Parent) {
        this.Parent = Parent;
    }
}
