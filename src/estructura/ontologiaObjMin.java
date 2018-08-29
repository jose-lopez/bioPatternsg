/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package estructura;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
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

    public void guardarObjeto(ontologiaObjMin objeto, boolean GO, boolean mesh) {

        ObjectContainer db = Db4o.openFile("mineria/ontologiaObjMin.db");
        try {

            ObjectSet result = db.queryByExample(this);
            if (!result.hasNext()) {

                if (GO) {

                    funcionMolecular.forEach(fm -> buscarOntologiaGO(fm));

                    procesoBiologico.forEach(pb -> buscarOntologiaGO(pb));

                    componenteCelular.forEach(cc -> buscarOntologiaGO(cc));

                }
                if (mesh) {
                    Parent.forEach(parent -> buscarOntologiaMESH(parent));
                }

                db.store(this);
            }

        } catch (Exception e) {
            // System.out.println("Error al guardar en ontologiaObjMin.db");
        } finally {
            db.close();
        }

    }

    public void buscarOntologiaMESH(String MESH) {
        ontologiaMESH ontologia = new ontologiaMESH();
        lecturas_MESH letMESH = new lecturas_MESH();
        ontologia.setMESH(MESH);

        if (!buscarObjeto(ontologia) && !ontologia.getMESH().equals("1000048")) {

            ontologia = letMESH.obtenerOntologia(MESH);

            ontologia.getParent().forEach(ont -> buscarOntologiaMESH(ont));

            guardar_Ontologia(ontologia);

        }

    }

    public void buscarOntologiaGO(String GO) {
        ontologiaGO ontologia = new ontologiaGO();
        lecturas_QuickGO letQGO = new lecturas_QuickGO();
        ontologia.setGO(GO);
        if (!buscarObjeto(ontologia)) {
            ontologia = letQGO.obtenerOntologia(GO);

            ontologia.getIs_a().forEach(ont -> buscarOntologiaGO(ont));

            ontologia.getPart_of().forEach(ont -> buscarOntologiaGO(ont));

            ontologia.getRegulates().forEach(ont -> buscarOntologiaGO(ont));

            ontologia.getNegatively_regulates().forEach(ont -> buscarOntologiaGO(ont));

            ontologia.getPositively_regulates().forEach(ont -> buscarOntologiaGO(ont));

            ontologia.getCapable_of().forEach(ont -> buscarOntologiaGO(ont));

            ontologia.getCapable_of_part_of().forEach(ont -> buscarOntologiaGO(ont));

            ontologia.getOccurs_in().forEach(ont -> buscarOntologiaGO(ont));

            guardar_Ontologia(ontologia);
        }

    }

    private void guardar_Ontologia(ontologiaGO ontologia) {

        ObjectContainer db = Db4o.openFile("mineria/OntologiaGO.db");
        try {
            db.store(ontologia);
            System.out.println("Guardando: "+ontologia.getNombre()+" "+ontologia.getGO());
        } catch (Exception e) {
            System.out.println("Error al guardar en OntologiaGO.db...");
        } finally {
            db.close();
        }

    }

    private void guardar_Ontologia(ontologiaMESH ontologia) {
        ObjectContainer db = Db4o.openFile("mineria/OntologiaMESH.db");
        try {
            db.store(ontologia);
            //System.out.println("Guardando: "+ontologia.getNombre()+" "+ontologia.getMESH());
        } catch (Exception e) {
            // System.out.println("Error al guardar en OntologiaMESH.db...");
        } finally {
            db.close();
        }

    }

    private boolean buscarObjeto(ontologiaGO objeto) {
        boolean encontrado = false;

        ObjectContainer db = Db4o.openFile("mineria/OntologiaGO.db");
        try {

            ObjectSet result = db.queryByExample(objeto);
            if (result.hasNext()) {
                encontrado = true;
            }
        } catch (Exception e) {
            System.out.println("Error al acceder a OntologiaGO.db");
        } finally {
            db.close();
        }

        return encontrado;
    }

    private boolean buscarObjeto(ontologiaMESH objeto) {
        boolean encontrado = false;
        ObjectContainer db = Db4o.openFile("mineria/OntologiaMESH.db");
        try {

            ObjectSet result = db.queryByExample(objeto);
            if (result.hasNext()) {
                encontrado = true;
            }
        } catch (Exception e) {
            System.out.println("Error al acceder a OntologiaMESH.db");
        } finally {
            db.close();
        }

        return encontrado;
    }

    public void buscarGO(String nombre, String restriccion) {
        ontologiaObjMin objetoGO = new ontologiaObjMin();
        objetoGO.setNombre(nombre);
        ObjectContainer db = Db4o.openFile("mineria/ontologiaObjMin.db");
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
        imprimirTodo(objetoGO, restriccion,true,true);
    }

    public void imprimirTodo() {
        ontologiaObjMin objeto = new ontologiaObjMin();
        ObjectContainer db = Db4o.openFile("mineria/ontologiaObjMin.db");
        try {

            ObjectSet result = db.queryByExample(objeto);
            while (result.hasNext()) {
                ontologiaObjMin obj = (ontologiaObjMin) result.next();
                System.out.println(obj.getNombre());
                imprimirTodo(obj, null,true,true);
            }
        } catch (Exception e) {
        } finally {
            db.close();
        }
    }

    public void imprimirTodo(String tipo, String restriccion) {

        ontologiaObjMin objeto = new ontologiaObjMin();
        ObjectContainer db = Db4o.openFile("mineria/ontologiaObjMin.db");
        try {

            ObjectSet result = db.queryByExample(objeto);
            while (result.hasNext()) {
                ontologiaObjMin obj = (ontologiaObjMin) result.next();
                if (tipo == null) {
                    imprimirTodo(obj, restriccion,true,true);
                } else if (tipo.equals("F")) {
                    imprimirFuncionMolecular(obj, restriccion);
                } else if (tipo.equals("P")) {
                    imprimirProcesobiologico(obj, restriccion);
                } else if (tipo.equals("C")) {
                    imprimirComponenteCelular(obj, restriccion);
                } else if (tipo.equals("M")) {
                    imprimirMESH(obj);
                }

            }
        } catch (Exception e) {
            System.out.println("Error al acceder a mineria/ontologiaObjMin.db");
        } finally {
            db.close();
        }
    }

  
    public void vaciarOntologia_pl(boolean GO, boolean MESH) {
        ontologiaObjMin objeto = new ontologiaObjMin();
        ObjectContainer db = Db4o.openFile("mineria/ontologiaObjMin.db");
        ontologiaGO ontologiaGO = new ontologiaGO();
        ontologiaMESH ontologiaMESH = new ontologiaMESH();

        try {

            ObjectSet result = db.queryByExample(objeto);
            while (result.hasNext()) {
                ontologiaObjMin obj = (ontologiaObjMin) result.next();
                ArrayList<String> ListaObj = new ArrayList<>();
                vaciarpl(obj);
                if (GO) {

                    final ArrayList<ontologiaGO> ontGO = ontologiaGO.getOntGO();

                    obj.funcionMolecular.forEach(fm -> ontologiaGO.vaciar_pl(ontGO, fm, null, null, ListaObj, "ontologiaGO.pl"));

                    obj.procesoBiologico.forEach(pb -> ontologiaGO.vaciar_pl(ontGO, pb, null, null, ListaObj, "ontologiaGO.pl"));

                    obj.componenteCelular.forEach(cc -> ontologiaGO.vaciar_pl(ontGO, cc, null, null, ListaObj, "ontologiaGO.pl"));

                }

                if (MESH) {
                    obj.Parent.forEach(p -> ontologiaMESH.vaciar_pl(p, null, ListaObj, "ontologiaMESH.pl"));
                }

            }
        } catch (Exception e) {

        } finally {
            db.close();
        }
    }

    private void vaciarpl(ontologiaObjMin obj) {
        String FM = "[";
        String CC = "[";
        String PB = "[";
        String MESH = "";
        ontologiaGO GO = new ontologiaGO();
        ontologiaMESH mesh = new ontologiaMESH();

        new escribirBC("objeto(\'" + obj.nombre.replace("\'", "") + "\').", "ontologiaMESH.pl");

        for (String fm : obj.funcionMolecular) {
            if (FM.equals("[")) {
                FM += "\'" + GO.buscar(fm).replace("\'", "") + "\'";
            } else {
                FM += ",\'" + GO.buscar(fm).replace("\'", "") + "\'";
            }
        }
        FM += "]";

        for (String pb : obj.procesoBiologico) {
            if (PB.equals("[")) {
                PB += "\'" + GO.buscar(pb).replace("\'", "") + "\'";
            } else {
                PB += ",\'" + GO.buscar(pb).replace("\'", "") + "\'";
            }
        }
        PB += "]";

        for (String cc : obj.componenteCelular) {
            if (CC.equals("[")) {
                CC += "\'" + GO.buscar(cc).replace("\'", "") + "\'";
            } else {
                CC += ",\'" + GO.buscar(cc).replace("\'", "") + "\'";
            }
        }
        CC += "]";

        for (String m : obj.Parent) {
            MESH = mesh.buscarNombre(m).replace("\'", "");

        }

        if (!FM.equals("[]")) {
            new escribirBC("fm(\'" + obj.nombre.replace("\'", "") + "\'," + FM + ").", "ontologiaGO.pl");
        }
        if (!PB.equals("[]")) {
            new escribirBC("pb(\'" + obj.nombre.replace("\'", "") + "\'," + PB + ").", "ontologiaGO.pl");
        }
        if (!CC.equals("[]")) {
            new escribirBC("cc(\'" + obj.nombre.replace("\'", "") + "\'," + CC + ").", "ontologiaGO.pl");
        }
        if (!MESH.equals("")) {
            new escribirBC("is_a(\'" + obj.nombre.replace("\'", "") + "\',\'" + MESH + "\').", "ontologiaMESH.pl");
            String aux = mesh.procesarTexto(MESH);
            new escribirBC(aux + "(\'" + obj.nombre.replace("\'", "") + "\').", "objetosMinados.pl");
        }

    }

    private void imprimirTodo(ontologiaObjMin obj, String restriccion, boolean GO, boolean MESH) {
        if (GO) {
            System.out.println("\n________________________________________________________________");
            System.out.println("Funcion Molecular:");
            for (int i = 0; i < obj.funcionMolecular.size(); i++) {
                ontologiaGO objeto = new ontologiaGO();
                objeto.buscar(obj.funcionMolecular.get(i), restriccion);

            }
            System.out.println("\n_______________________________________________________________");
            System.out.println("Proceso biologico:");
            for (int i = 0; i < obj.procesoBiologico.size(); i++) {
                ontologiaGO objeto = new ontologiaGO();
                objeto.buscar(obj.procesoBiologico.get(i), restriccion);
            }
            System.out.println("\n________________________________________________________________");
            System.out.println("Componente celular:");
            for (int i = 0; i < obj.componenteCelular.size(); i++) {
                ontologiaGO objeto = new ontologiaGO();
                objeto.buscar(obj.componenteCelular.get(i), restriccion);
            }
        }

        if (MESH) {
            // System.out.println("\n________________________________________________________________");
            imprimirMESH(obj);
        }
    }

    public void buscarObjeto(String nombre,boolean GO, boolean MESH) {

        ontologiaObjMin objeto = new ontologiaObjMin();
        objeto.setNombre(nombre);
        ObjectContainer db = Db4o.openFile("mineria/ontologiaObjMin.db");
        try {

            ObjectSet result = db.queryByExample(objeto);
            while (result.hasNext()) {
                objeto = (ontologiaObjMin) result.next();

                // break;
            }
            imprimirTodo(objeto, null,GO,MESH);
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

    private void imprimirMESH(ontologiaObjMin obj) {
        System.out.println("\n________________________________________________________________");
        System.out.println("Arbol de Identidad:");
        for (int i = 0; i < obj.Parent.size(); i++) {
            ontologiaMESH objeto = new ontologiaMESH();
            objeto.buscar(obj.Parent.get(i));
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
