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
            if (result.size() == 0) {
                db.store(this);
                for (int i = 0; i < funcionMolecular.size(); i++) {
                    buscarOntologia(funcionMolecular.get(i));
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
            guardar_Ontologia(ontologia);
        }
    }

    private void guardar_Ontologia(ontologia ontologia) {

        ObjectContainer db = Db4o.openFile("Ontologia.db");
        try {
            db.store(ontologia);
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
            if (result.size() > 0) {
                encontrado = true;
            }
        } catch (Exception e) {
            System.out.println("Error al acceder a Ontologia.db");
        } finally {
            db.close();
        }

        return encontrado;
    }
    
    public void imprimirTodo(){
        
        ObjetosMinadosGO objeto = new ObjetosMinadosGO();
        ObjectContainer db = Db4o.openFile("ObjetosMinadosGO.db");
        try {

            ObjectSet result = db.queryByExample(objeto);
            while (result.hasNext()) {
                ObjetosMinadosGO obj = (ObjetosMinadosGO)result.next();
                imprimir(obj);
            }
        } catch (Exception e) {
            System.out.println("Error al acceder a Ontologia.db");
        } finally {
            db.close();
        }
    }
    
    private void imprimir(ObjetosMinadosGO obj){
        System.out.println("\n+++++++++++++++++++++++++++++++++++++++");
        System.out.println(obj.getNombre());
        System.out.println("funcion Molecular:");
        for (int i = 0; i <obj.funcionMolecular.size(); i++) {
            ontologia objeto = new ontologia();
            //objeto.buscarObjeto(obj.funcionMolecular.get(i));
        }
        System.out.println("proceso biologico:");
        for (int i = 0; i <obj.procesoBiologico.size(); i++) {
            ontologia objeto = new ontologia();
            //objeto.buscarObjeto(obj.procesoBiologico.get(i));
        }
        System.out.println("componente celular:");
        for (int i = 0; i <obj.componenteCelular.size(); i++) {
            ontologia objeto = new ontologia();
            //objeto.buscarObjeto(obj.componenteCelular.get(i));
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
