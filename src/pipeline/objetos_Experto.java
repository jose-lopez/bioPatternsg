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
    
    public void vaciar_pl(String archivo) {
        boolean encontrado = false;
       
        for (int i = 0; i < HGNC.size(); i++) {
            String cadena = "[";
            cadena += "\'" + HGNC.get(i).getSimbolo().replace("\'", "") + "\',";
            cadena += "\'" + HGNC.get(i).getNombre().replace("\'", "") + "\'";
            for (int j = 0; j < HGNC.get(i).getSinonimos().size(); j++) {
                cadena += ",\'" + HGNC.get(i).getSinonimos().get(j).replace("\'", "") + "\'";
            }
            
            cadena += "]";
            //System.out.println("Experto: "+cadena);
            new escribirBC("sinonimos(\'" + HGNC.get(i).getSimbolo().replace("\'", "") + "\'," + cadena + ").", archivo);
       
            ArrayList<String> lista = HGNC.get(i).ListaNombres();
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



