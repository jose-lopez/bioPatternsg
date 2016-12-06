/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import java.util.ArrayList;

/**
 *
 * @author yacson-ramirez
 */

public class ontologia {
    
    private String GO;
    private String nombre;
    private ArrayList<String> sinonimos;
    private ArrayList <String> is_a;
    private ArrayList <String> part_of;
    private ArrayList <String> regulates;
    private ArrayList <String> positively_regulates;
    private ArrayList <String> negatively_regulates;
    private ArrayList <String> occurs_in;
    private ArrayList <String> capable_of;
    private ArrayList <String> capable_of_part_of;

    public ontologia(){
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

    public void imprimir(){
        System.out.println("GO= "+GO);
        System.out.println("FM= "+nombre);
        System.out.println("Sinonimos:");
        for (int i = 0; i < sinonimos.size(); i++) {
            System.out.println("    "+sinonimos.get(i));
        }
        System.out.println("is_a:");
        for (int i = 0; i < is_a.size(); i++) {
            System.out.println("    "+is_a.get(i));
        }
        
    }
    
    public void Obtener_Ontologia(){
        
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


