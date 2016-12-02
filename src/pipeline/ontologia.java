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
    private ArrayList <String> is_a;
    private ArrayList<String> sinonimos;

    public ontologia(){
        is_a = new ArrayList<>();
        sinonimos = new ArrayList<>();
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
    
    
    
    
    
}


