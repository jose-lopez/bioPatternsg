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
 * @author yacson-ramirez
 */
public class ontologia {

    private String GO;
    private String nombre;
    private ArrayList<String> sinonimos;
    private ArrayList<String> is_a;
    private ArrayList<String> part_of;
    private ArrayList<String> regulates;
    private ArrayList<String> positively_regulates;
    private ArrayList<String> negatively_regulates;
    private ArrayList<String> occurs_in;
    private ArrayList<String> capable_of;
    private ArrayList<String> capable_of_part_of;

    public ontologia() {
        sinonimos = new ArrayList<>();
        is_a = new ArrayList<>();
        part_of = new ArrayList<>();
        regulates = new ArrayList<>();
        positively_regulates = new ArrayList<>();
        negatively_regulates = new ArrayList<>();
        occurs_in = new ArrayList<>();
        capable_of = new ArrayList<>();
        capable_of_part_of = new ArrayList<>();
    }

    public void imprimirTodo() {
        ontologia objeto = new ontologia();
        ObjectContainer db = Db4o.openFile("Ontologia.db");
        try {

            ObjectSet result = db.queryByExample(objeto);
            while (result.hasNext()) {
                ontologia obj = (ontologia) result.next();
                imprimir(obj);
            }
        } catch (Exception e) {
            System.out.println("Error al acceder a Ontologia.db");
        } finally {
            db.close();
        }

    }
    //GO:0008123
    private int max = 0;
    

    public void buscar(String GO) {
        
        buscarObjeto(GO, 0);


    }

    private void buscarObjeto(String GO, int nivel) {

        ontologia objeto = new ontologia();
        objeto.setGO(GO);
        objeto = consultarBD(objeto);
        
        for (int i = 0; i < nivel; i++) {
            System.out.print("      ");            
        }
        System.out.println(objeto.getNombre());
        
        nivel++;
        for (int i = 0; i < objeto.is_a.size(); i++) {
            buscarObjeto(objeto.is_a.get(i), nivel);
        }
                   
    }

    private ontologia consultarBD(ontologia obj) {
        ontologia objeto = new ontologia();
        ObjectContainer db = Db4o.openFile("Ontologia.db");
        try {

            ObjectSet result = db.queryByExample(obj);
            while (result.hasNext()) {
                objeto = (ontologia) result.next();

            }
        } catch (Exception e) {
            System.out.println("Error al acceder a Ontologia.db");
        } finally {
            db.close();
        }

        return objeto;
    }

    private void imprimir(ontologia objeto) {
        System.out.println("\n\n++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println(objeto.getGO());
        System.out.println("Nombre: " + objeto.getNombre());
        System.out.println("Sinonimos:");
        for (int i = 0; i < objeto.getSinonimos().size(); i++) {
            System.out.println("    " + objeto.getSinonimos().get(i));
        }
        if (objeto.getIs_a().size() > 0) {
            System.out.println("________________________________");
            for (int i = 0; i < objeto.getIs_a().size(); i++) {
                System.out.println("is a--> " + objeto.getIs_a().get(i));
            }
        }
        if (objeto.getRegulates().size() > 0) {
            System.out.println("________________________________");
            for (int i = 0; i < objeto.getIs_a().size(); i++) {
                System.out.println("regulates--> " + objeto.getRegulates().get(i));
            }
        }
        if (objeto.getCapable_of().size() > 0) {
            System.out.println("________________________________");
            for (int i = 0; i < objeto.getIs_a().size(); i++) {
                System.out.println("capable of--> " + objeto.getCapable_of().get(i));
            }
        }
        if (objeto.getCapable_of_part_of().size() > 0) {
            System.out.println("________________________________");
            for (int i = 0; i < objeto.getCapable_of_part_of().size(); i++) {
                System.out.println("capable of part--> " + objeto.getCapable_of_part_of().get(i));
            }
        }
        if (objeto.getNegatively_regulates().size() > 0) {
            System.out.println("________________________________");
            for (int i = 0; i < objeto.getNegatively_regulates().size(); i++) {
                System.out.println("negatively regulates--> " + objeto.getNegatively_regulates().get(i));
            }
        }
        if (objeto.getPositively_regulates().size() > 0) {
            System.out.println("________________________________");
            for (int i = 0; i < objeto.getPositively_regulates().size(); i++) {
                System.out.println("positively regulates--> " + objeto.getPositively_regulates().get(i));
            }
        }
        if (objeto.getOccurs_in().size() > 0) {
            System.out.println("________________________________");
            for (int i = 0; i < objeto.getOccurs_in().size(); i++) {
                System.out.println("is a--> " + objeto.getOccurs_in().get(i));
            }
        }

    }

    public String getGO() {
        return GO;
    }

    public void setGO(String GO) {
        this.GO = GO;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String FuncionMolecular) {
        this.nombre = FuncionMolecular;
    }

    public ArrayList<String> getIs_a() {
        return is_a;
    }

    public void setIs_a(ArrayList<String> is_a) {
        this.is_a = is_a;
    }

    public ArrayList<String> getSinonimos() {
        return sinonimos;
    }

    public void setSinonimos(ArrayList<String> sinonimos) {
        this.sinonimos = sinonimos;
    }

    public ArrayList<String> getPart_of() {
        return part_of;
    }

    public void setPart_of(ArrayList<String> part_of) {
        this.part_of = part_of;
    }

    public ArrayList<String> getRegulates() {
        return regulates;
    }

    public void setRegulates(ArrayList<String> regulates) {
        this.regulates = regulates;
    }

    public ArrayList<String> getPositively_regulates() {
        return positively_regulates;
    }

    public void setPositively_regulates(ArrayList<String> positively_regulates) {
        this.positively_regulates = positively_regulates;
    }

    public ArrayList<String> getNegatively_regulates() {
        return negatively_regulates;
    }

    public void setNegatively_regulates(ArrayList<String> negatively_regulates) {
        this.negatively_regulates = negatively_regulates;
    }

    public ArrayList<String> getOccurs_in() {
        return occurs_in;
    }

    public void setOccurs_in(ArrayList<String> occurs_in) {
        this.occurs_in = occurs_in;
    }

    public ArrayList<String> getCapable_of() {
        return capable_of;
    }

    public void setCapable_of(ArrayList<String> capable_of) {
        this.capable_of = capable_of;
    }

    public ArrayList<String> getCapable_of_part_of() {
        return capable_of_part_of;
    }

    public void setCapable_of_part_of(ArrayList<String> capable_of_part_of) {
        this.capable_of_part_of = capable_of_part_of;
    }

}

