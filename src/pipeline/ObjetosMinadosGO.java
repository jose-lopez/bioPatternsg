/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import java.util.ArrayList;

/**
 *
 * @author yacson
 */
public class ObjetosMinadosGO {

    private String nombre;
    private ArrayList<String> funcionMolecular;
    private ArrayList<String> procesoBiologico;
    private ArrayList<String> componenteCelular;

    public ObjetosMinadosGO() {
        funcionMolecular = new ArrayList<>();
        procesoBiologico = new ArrayList<>();
        componenteCelular = new ArrayList<>();
    }

    public void guardarObjeto(ObjetosMinadosGO objeto) {

        ObjectContainer db = Db4o.openFile("ObjetosMinadosGO.db");
        try {

            ObjectSet result = db.queryByExample(this);
            if (!result.hasNext()) {
                db.store(this);
                for (int i = 0; i < funcionMolecular.size(); i++) {
                    // System.out.println("funcion mlecular:  "+ funcionMolecular.get(i));
                    buscarOntologia(funcionMolecular.get(i));
                }
                for (int i = 0; i < procesoBiologico.size(); i++) {
                    //System.out.println("proceso biologico:  "+ procesoBiologico.get(i));
                    buscarOntologia(procesoBiologico.get(i));
                }
                for (int i = 0; i < componenteCelular.size(); i++) {
                    // System.out.println("componente celular:  "+ componenteCelular.get(i));
                    buscarOntologia(componenteCelular.get(i));
                }
            }

        } catch (Exception e) {
            System.out.println("Error al guardar en ObetosMinadosGO.db");
        } finally {
            db.close();
        }

    }

    public void buscarOntologia(String GO) {
        ontologia ontologia = new ontologia();
        lecturas_QuickGO letQGO = new lecturas_QuickGO();
        ontologia = letQGO.obtenerOntologia(GO);

        if (!buscarObjeto(ontologia)) {
            for (int i = 0; i < ontologia.getIs_a().size(); i++) {
                buscarOntologia(ontologia.getIs_a().get(i));
            }
            for (int i = 0; i < ontologia.getPart_of().size(); i++) {
                buscarOntologia(ontologia.getPart_of().get(i));
            }
            for (int i = 0; i < ontologia.getRegulates().size(); i++) {
                buscarOntologia(ontologia.getRegulates().get(i));
            }
            for (int i = 0; i < ontologia.getNegatively_regulates().size(); i++) {
                buscarOntologia(ontologia.getNegatively_regulates().get(i));
            }
            for (int i = 0; i < ontologia.getPositively_regulates().size(); i++) {
                buscarOntologia(ontologia.getPositively_regulates().get(i));
            }
            for (int i = 0; i < ontologia.getCapable_of().size(); i++) {
                buscarOntologia(ontologia.getCapable_of().get(i));
            }
            for (int i = 0; i < ontologia.getCapable_of_part_of().size(); i++) {
                buscarOntologia(ontologia.getCapable_of_part_of().get(i));
            }
            for (int i = 0; i < ontologia.getOccurs_in().size(); i++) {
                buscarOntologia(ontologia.getOccurs_in().get(i));
            }
            guardar_Ontologia(ontologia);
        }
    }

    private void guardar_Ontologia(ontologia ontologia) {

        ObjectContainer db = Db4o.openFile("Ontologia.db");
        try {
            db.store(ontologia);
            //System.out.println("Guardando: "+ontologia.getNombre());
        } catch (Exception e) {
            System.out.println("Error al guardar en Ontologia.db...");
        } finally {
            db.close();
        }

    }

    private boolean buscarObjeto(ontologia objeto) {
        boolean encontrado = false;
        ObjectContainer db = Db4o.openFile("Ontologia.db");
        try {

            ObjectSet result = db.queryByExample(objeto);
            if (result.hasNext()) {
                encontrado = true;
            }
        } catch (Exception e) {
            System.out.println("Error al acceder a Ontologia.db");
        } finally {
            db.close();
        }

        return encontrado;
    }

    public void buscar(String nombre,String restriccion) {
        ObjetosMinadosGO objetoGO = new ObjetosMinadosGO();
        objetoGO.setNombre(nombre);
        ObjectContainer db = Db4o.openFile("ObjetosMinadosGO.db");
        try {

            ObjectSet result = db.queryByExample(objetoGO);
            while (result.hasNext()) {
                objetoGO = (ObjetosMinadosGO) result.next();
                break;
            }
        } catch (Exception e) {
            System.out.println("Error al acceder a Ontologia.db");
        } finally {
            db.close();
        }
        imprimirTodo(objetoGO,restriccion);
    }

    public void imprimirTodo(String tipo,String restriccion) {

        ObjetosMinadosGO objeto = new ObjetosMinadosGO();
        ObjectContainer db = Db4o.openFile("ObjetosMinadosGO.db");
        try {

            ObjectSet result = db.queryByExample(objeto);
            while (result.hasNext()) {
                ObjetosMinadosGO obj = (ObjetosMinadosGO) result.next();
                if (tipo.equals(null)) {
                    imprimirTodo(obj,restriccion);
                } else if (tipo.equals("F")) {
                    imprimirFuncionMolecular(obj,restriccion);
                } else if (tipo.equals("P")) {
                    imprimirProcesobiologico(obj,restriccion);
                } else if (tipo.equals("C")) {
                    imprimirComponenteCelular(obj,restriccion);
                }

            }
        } catch (Exception e) {
            System.out.println("Error al acceder a Ontologia.db");
        } finally {
            db.close();
        }
    }

    private void imprimirTodo(ObjetosMinadosGO obj, String restriccion) {
        System.out.println("\n________________________________________________________________");
        System.out.println(obj.getNombre());
        System.out.println("Funcion Molecular:");
        for (int i = 0; i < obj.funcionMolecular.size(); i++) {
            ontologia objeto = new ontologia();
            objeto.buscar(obj.funcionMolecular.get(i),restriccion);

        }
        System.out.println("\n_______________________________________________________________");
        System.out.println("Proceso biologico:");
        for (int i = 0; i < obj.procesoBiologico.size(); i++) {
            ontologia objeto = new ontologia();
            objeto.buscar(obj.procesoBiologico.get(i),restriccion);
        }
        System.out.println("\n________________________________________________________________");
        System.out.println("Componente celular:");
        for (int i = 0; i < obj.componenteCelular.size(); i++) {
            ontologia objeto = new ontologia();
            objeto.buscar(obj.componenteCelular.get(i),restriccion);
        }

    }

    private void imprimirFuncionMolecular(ObjetosMinadosGO obj,String restriccion) {
        System.out.println("\n________________________________________________________________");
        System.out.println(obj.getNombre());
        System.out.println("Funcion Molecular:");
        for (int i = 0; i < obj.funcionMolecular.size(); i++) {
            ontologia objeto = new ontologia();
            objeto.buscar(obj.funcionMolecular.get(i),restriccion);
        }
    }

    private void imprimirProcesobiologico(ObjetosMinadosGO obj, String restriccion) {
        System.out.println("\n_______________________________________________________________");
        System.out.println("Proceso biologico:");
        for (int i = 0; i < obj.procesoBiologico.size(); i++) {
            ontologia objeto = new ontologia();
            objeto.buscar(obj.procesoBiologico.get(i),restriccion);
        }
    }

    private void imprimirComponenteCelular(ObjetosMinadosGO obj, String restriccion) {
        System.out.println("\n________________________________________________________________");
        System.out.println("Componente celular:");
        for (int i = 0; i < obj.componenteCelular.size(); i++) {
            ontologia objeto = new ontologia();
            objeto.buscar(obj.componenteCelular.get(i),restriccion);
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

}
