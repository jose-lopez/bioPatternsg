/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import java.util.ArrayList;

/**
 *
 * @author yacson
 */
public class pathway {
    
   private String Patron;
   private ArrayList<String> objetos;

    public pathway() {
        this.objetos = new ArrayList<>();
    }

    public String getPatron() {
        return Patron;
    }

    public void setPatron(String Patron) {
        this.Patron = Patron;
    }

    public ArrayList<String> getObjetos() {
        return objetos;
    }

    public void setObjetos(ArrayList<String> objetos) {
        this.objetos = objetos;
    }
      
}
