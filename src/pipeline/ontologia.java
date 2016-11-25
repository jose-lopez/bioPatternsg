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
    private String FuncionMolecular;
    private ArrayList <String> is_a;
    private ArrayList<String> sinonimos;

    public ontologia(){
        is_a = new ArrayList<>();
        sinonimos = new ArrayList<>();
    }
    
    
    public String getGO() {
        return GO;
    }

    public void setGO(String GO) {
        this.GO = GO;
    }

    public String getFuncionMolecular() {
        return FuncionMolecular;
    }

    public void setFuncionMolecular(String FuncionMolecular) {
        this.FuncionMolecular = FuncionMolecular;
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


