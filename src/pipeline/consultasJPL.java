/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import org.jpl7.Query;
import org.jpl7.Term;
import org.jpl7.Variable;

/**
 *
 * @author yacson
 */
public class consultasJPL {
    
    
    public void prueba(){
        String archivo = "[consultas].";
        Query q = new Query(archivo);
        q.hasSolution();
        
        
        buscar_receptores();
        
             
    }
    
    public void buscar_receptores(){
        String consulta ="buscar_receptores('EGF').";
        
        Query q2 = new Query(consulta);
       
        for (int i = 0; i < q2.allSolutions().length; i++) {
             System.out.println(q2.allSolutions()[i]);
        }
    }
    
    
    
}
