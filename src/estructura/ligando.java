/* 
 * bioPatternsg
 * BioPatternsg is a system that allows the integration and analysis of information related to the modeling of Gene Regulatory Networks (GRN).
 * Copyright (C) 2020
 * Jose Lopez (josesmooth@gmail.com), Jacinto Dávila (jacinto.davila@gmail.com), Yacson Ramirez (yacson.ramirez@gmail.com).
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
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
