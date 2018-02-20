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
        HGNC.forEach(hgnc ->  lista.addAll(hgnc.ListaNombres()));
         
        return lista;
    }
    
    public void vaciar_pl(String archivo) {
        boolean encontrado = false;
        
        for (HGNC hgnc : HGNC) {
            String cadena = "[";
            cadena += "\'" + hgnc.getSimbolo().replace("\'", "") + "\',";
            cadena += "\'" + hgnc.getNombre().replace("\'", "") + "\'";
            for (String sinonimo : hgnc.getSinonimos()) {
                cadena += ",\'" + sinonimo.replace("\'", "") + "\'";
            }
            
            cadena += "]";
            //System.out.println("Experto: "+cadena);
            new escribirBC("sinonimos(\'" + hgnc.getSimbolo().replace("\'", "") + "\'," + cadena + ").", archivo);
       
            ArrayList<String> lista = hgnc.ListaNombres();
            if (lista.contains(ID)) {
                encontrado = true;
            }
        }
        
        if(!encontrado){
            new escribirBC("sinonimos(\'" +ID+"\',[\'" +ID+ "\']).", archivo);
        }

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



