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

    public void guardarObjeto(ontologiaObjMin objeto) {

        ObjectContainer db = Db4o.openFile("mineria/ontologiaObjMin.db");
        try {

            ObjectSet result = db.queryByExample(this);
            if (!result.hasNext()) {
                //System.out.println(nombre);
                db.store(this);
                for (int i = 0; i < funcionMolecular.size(); i++) {
                    //System.out.println("funcion mlecular:  "+ funcionMolecular.get(i));
                    buscarOntologiaGO(funcionMolecular.get(i));
                }
                for (int i = 0; i < procesoBiologico.size(); i++) {
                    //System.out.println("proceso biologico:  "+ procesoBiologico.get(i));
                    buscarOntologiaGO(procesoBiologico.get(i));
                }
                for (int i = 0; i < componenteCelular.size(); i++) {
                     //System.out.println("componente celular:  "+ componenteCelular.get(i));
                    buscarOntologiaGO(componenteCelular.get(i));
                }
                for (int i = 0; i < Parent.size(); i++) {
                    buscarOntologiaMESH(Parent.get(i));
                }
            }

        } catch (Exception e) {
            System.out.println("Error al guardar en ontologiaObjMin.db");
        } finally {
            db.close();
        }

    }

    public void buscarOntologiaMESH(String MESH){
        ontologiaMESH ontologia = new ontologiaMESH();
        lecturas_MESH letMESH = new lecturas_MESH();
        ontologia = letMESH.obtenerOntologia(MESH);
        
        if (!buscarObjeto(ontologia)) {
            for (int i = 0; i < ontologia.getParent().size(); i++) {
                buscarOntologiaMESH(ontologia.getParent().get(i));
            }
            guardar_Ontologia(ontologia);
        }
        
    }
    
    public void buscarOntologiaGO(String GO) {
        ontologiaGO ontologia = new ontologiaGO();
        lecturas_QuickGO letQGO = new lecturas_QuickGO();
        ontologia = letQGO.obtenerOntologia(GO);

        if (!buscarObjeto(ontologia)) {
            for (int i = 0; i < ontologia.getIs_a().size(); i++) {
                buscarOntologiaGO(ontologia.getIs_a().get(i));
            }
            for (int i = 0; i < ontologia.getPart_of().size(); i++) {
                buscarOntologiaGO(ontologia.getPart_of().get(i));
            }
            for (int i = 0; i < ontologia.getRegulates().size(); i++) {
                buscarOntologiaGO(ontologia.getRegulates().get(i));
            }
            for (int i = 0; i < ontologia.getNegatively_regulates().size(); i++) {
                buscarOntologiaGO(ontologia.getNegatively_regulates().get(i));
            }
            for (int i = 0; i < ontologia.getPositively_regulates().size(); i++) {
                buscarOntologiaGO(ontologia.getPositively_regulates().get(i));
            }
            for (int i = 0; i < ontologia.getCapable_of().size(); i++) {
                buscarOntologiaGO(ontologia.getCapable_of().get(i));
            }
            for (int i = 0; i < ontologia.getCapable_of_part_of().size(); i++) {
                buscarOntologiaGO(ontologia.getCapable_of_part_of().get(i));
            }
            for (int i = 0; i < ontologia.getOccurs_in().size(); i++) {
                buscarOntologiaGO(ontologia.getOccurs_in().get(i));
            }
            guardar_Ontologia(ontologia);
        }
    }

    private void guardar_Ontologia(ontologiaGO ontologia) {

        ObjectContainer db = Db4o.openFile("mineria/OntologiaGO.db");
        try {
            db.store(ontologia);
            //System.out.println("Guardando: "+ontologia.getNombre());
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
            //System.out.println("Guardando: "+ontologia.getNombre());
        } catch (Exception e) {
            System.out.println("Error al guardar en OntologiaMESH.db...");
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

    public void buscarGO(String nombre,String restriccion) {
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
        imprimirTodo(objetoGO,restriccion);
    }

    
    
     public void imprimirTodo(){
        ontologiaObjMin objeto = new ontologiaObjMin();
        ObjectContainer db = Db4o.openFile("mineria/ObjetosMinadosGO.db");
        try {

            ObjectSet result = db.queryByExample(objeto);
            while (result.hasNext()) {
                ontologiaObjMin obj = (ontologiaObjMin) result.next();
                System.out.println(obj.getNombre());
                imprimirTodo(obj, null);
            }
        }catch(Exception e){}
        finally{
           db.close(); 
        }
     }
    
    public void imprimirTodo(String tipo,String restriccion) {

        ontologiaObjMin objeto = new ontologiaObjMin();
        ObjectContainer db = Db4o.openFile("mineria/ObjetosMinadosGO.db");
        try {

            ObjectSet result = db.queryByExample(objeto);
            while (result.hasNext()) {
                ontologiaObjMin obj = (ontologiaObjMin) result.next();
                if (tipo == null) {
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
            System.out.println("Error al acceder a mineria/ObjetosMinados.db");
        } finally {
            db.close();
        }
    }
    
    public void vaciarOntologia_pl(){
        ontologiaObjMin objeto = new ontologiaObjMin();
        ObjectContainer db = Db4o.openFile("mineria/ObjetosMinadosGO.db");
        ontologiaGO ontologia = new ontologiaGO();
        try {

            ObjectSet result = db.queryByExample(objeto);
            while (result.hasNext()) {
                ontologiaObjMin obj = (ontologiaObjMin) result.next();
                ArrayList<String>ListaObj = new ArrayList<>();
                for (int i = 0; i < obj.funcionMolecular.size(); i++) {
                    ontologia.vaciar_pl(obj.funcionMolecular.get(i),null,null,ListaObj);
                }
                for (int i = 0; i < obj.procesoBiologico.size(); i++) {
                    ontologia.vaciar_pl(obj.procesoBiologico.get(i),null,null,ListaObj);
                }
                for (int i = 0; i < obj.componenteCelular.size(); i++) {
                    ontologia.vaciar_pl(obj.componenteCelular.get(i),null,null,ListaObj);
                }
            }
        }catch(Exception e){
            
        }finally{
            db.close();
        }
    }

    private void imprimirTodo(ontologiaObjMin obj, String restriccion) {
        System.out.println("\n________________________________________________________________");
        System.out.println(obj.getNombre());
        System.out.println("Funcion Molecular:");
        for (int i = 0; i < obj.funcionMolecular.size(); i++) {
            ontologiaGO objeto = new ontologiaGO();
            objeto.buscar(obj.funcionMolecular.get(i),restriccion);

        }
        System.out.println("\n_______________________________________________________________");
        System.out.println("Proceso biologico:");
        for (int i = 0; i < obj.procesoBiologico.size(); i++) {
            ontologiaGO objeto = new ontologiaGO();
            objeto.buscar(obj.procesoBiologico.get(i),restriccion);
        }
        System.out.println("\n________________________________________________________________");
        System.out.println("Componente celular:");
        for (int i = 0; i < obj.componenteCelular.size(); i++) {
            ontologiaGO objeto = new ontologiaGO();
            objeto.buscar(obj.componenteCelular.get(i),restriccion);
        }

    }

    private void imprimirFuncionMolecular(ontologiaObjMin obj,String restriccion) {
        System.out.println("\n________________________________________________________________");
        System.out.println(obj.getNombre());
        System.out.println("Funcion Molecular:");
        for (int i = 0; i < obj.funcionMolecular.size(); i++) {
            ontologiaGO objeto = new ontologiaGO();
            objeto.buscar(obj.funcionMolecular.get(i),restriccion);
        }
    }

    private void imprimirProcesobiologico(ontologiaObjMin obj, String restriccion) {
        System.out.println("\n_______________________________________________________________");
        System.out.println("Proceso biologico:");
        for (int i = 0; i < obj.procesoBiologico.size(); i++) {
            ontologiaGO objeto = new ontologiaGO();
            objeto.buscar(obj.procesoBiologico.get(i),restriccion);
        }
    }

    private void imprimirComponenteCelular(ontologiaObjMin obj, String restriccion) {
        System.out.println("\n________________________________________________________________");
        System.out.println("Componente celular:");
        for (int i = 0; i < obj.componenteCelular.size(); i++) {
            ontologiaGO objeto = new ontologiaGO();
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

    public ArrayList<String> getParent() {
        return Parent;
    }

    public void setParent(ArrayList<String> Parent) {
        this.Parent = Parent;
    }

    
    
    
}
