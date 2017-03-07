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
public class ontologiaMESH {
    private String MESH;
    private String Nombre;
    private ArrayList<String> parent;

    public ontologiaMESH(){
        parent = new ArrayList<>();
    }
    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

   
    public String getMESH() {
        return MESH;
    }

    public void setMESH(String MESH) {
        this.MESH = MESH;
    }

    public ArrayList<String> getParent() {
        return parent;
    }

    public void setParent(ArrayList<String> parent) {
        this.parent = parent;
    }
    
    
    
    
}

