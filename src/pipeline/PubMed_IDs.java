/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author yacson
 */
public class PubMed_IDs {

    //busca los PubMed IDs de cada combinacion encontrada en el archivo mineria/combinaciones.db
    public void buscar(int cantIDs, configuracion config) {
        
        System.out.print("\nBusqueda de PubMed Id.....");

        ObjectContainer db = Db4o.openFile("mineria/combinaciones.db");
        combinacion com = new combinacion();
        ObjectSet result = db.queryByExample(com);
        combinacion combinacion = (combinacion) result.get(0);
        db.close();

        ArrayList<String> pubmedIDs = new ArrayList<>();

        combinacion.combinaciones.forEach((comb) -> {
            
            //consulta cada combinacion retorna una lista de pubmed IDs
            ArrayList<String> lista = new lecturas_PM().busquedaPM_ID(comb, cantIDs);
            
            //se inserta la lista encontrada en cada combiancion en la lista general de IDs
            insertar_en_lista(pubmedIDs, lista);

        });
       
        //se guarda el listado de IDs en la base de datos
        guardar(pubmedIDs);
        
        //se giarda el checklist que indica que el proceso de busqueda de PubMed Ids a terminado
        config.setPubmedids(true);
        config.guardar();
        System.out.println("ok");
    }

    //agrega la lista de IDs encontrada en cada busqueda y la agrega a una lista general
    //cada ID es filtrado de manera que no existan IDs repetidos
    private void insertar_en_lista(ArrayList<String> pubmedIDS, ArrayList<String> lista) {
        lista.parallelStream().forEach((id) -> {
            if (!pubmedIDS.contains(id)) {
                pubmedIDS.add(id);
            }
        });

    }
    
    //se guarda la lista de IDs en 'mineria/pubmed_id.db'
    private void guardar(ArrayList<String> pubmedIDS){
        
        ObjectContainer db = Db4o.openFile("mineria/pubmed_id.db");
        PMIDS ids = new PMIDS();
        ids.pubmed_ids = pubmedIDS;

        try {
            db.store(ids);

        } catch (Exception e) {
            System.out.println("Error al guardar combinaciones.db...");
        } finally {
            db.close();
        }
        
    }
    
     public void borrar_archivo() {
        try {
            File ficherod = new File("mineria/pubmed_id.db");
            ficherod.delete();
        } catch (Exception e) {

        }
    }

}

class PMIDS {

    ArrayList<String> pubmed_ids = new ArrayList<>();
}
