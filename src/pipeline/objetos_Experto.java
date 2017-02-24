/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import java.util.ArrayList;


public class objetos_Experto {
   
    private String ID; 
    private ArrayList<HGNC> HGNC;
    
    
    public objetos_Experto(){
        this.HGNC = new ArrayList<>();
    }
    
    
    public ArrayList<String> listaNombres() {
        ArrayList<String> lista = new ArrayList<>();
        lista.add(ID);
        for (int i = 0; i < HGNC.size(); i++) {
            lista.addAll(HGNC.get(i).ListaNombres());
        }
        return lista;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public ArrayList<HGNC> getHGNC() {
        return HGNC;
    }

    public void setHGNC(ArrayList<HGNC> HGNC) {
        this.HGNC = HGNC;
    }
    
}



