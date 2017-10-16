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
    
    public void vaciar_pl(String archivo){
         String cadena = "[";
        for (int i = 0; i < HGNC.size(); i++) {
            
            //if(HGNC.get(i).getSimbolo().equals(ID)){
                cadena +="\'"+HGNC.get(i).getSimbolo().replace("\'", "")+"\',";
                cadena +="\'"+HGNC.get(i).getNombre().replace("\'", "")+"\'";
                for (int j = 0; j < HGNC.get(i).getSinonimos().size(); j++) {
                    cadena+=",\'"+HGNC.get(i).getSinonimos().get(j).replace("\'", "")+"\'";
                }
              //  break;
            //}
            cadena+="]";
        new escribirBC("sinonimos(\'"+HGNC.get(i).getSimbolo().replace("\'", "")+"\',"+cadena+").", archivo);
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



