/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package estructura;

import pipeline.escribirBC;
import pipeline.objetosMinados;

/**
 *
 * @author yacson
 */
public class ligando {
    private String id;
    private String nombre;

    public ligando() {
    }
    
    public void imprimir(){
        
        System.out.println("    -Ligando ID: "+id+ "  Nombre: "+nombre);
                
    }
    
    public void vaciar_pl(String ruta){
        String cadena = "ligando(\'"+id.replace("\'", "")+"\').";
        new escribirBC(cadena,ruta+"/minedObjects.pl");
        cadena = "sinonimos(\'"+id.replace("\'", "")+"\',[\'"+id.replace("\'", "")+"\',\'"+nombre.replace("\'", "")+"\']).";
        new escribirBC(cadena, ruta+"/minedObjects.pl");
        String cadena_txt = id+";"+new objetosMinados().procesarNombre(nombre);
        new escribirBC(cadena_txt, ruta+"/minedObjects.txt");
    }
    
    private String procesarNombre(String nombre) {
        String cadena = nombre;
        cadena = cadena.replace(",", " ','");
        cadena = cadena.replace(" ", ", ");
        return cadena;
    }
       
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
