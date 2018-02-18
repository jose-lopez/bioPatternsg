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

    public void buscar(int cantIDs, configuracion config) {
        
        System.out.print("\nBusqueda de PubMed Id.....");

        ObjectContainer db = Db4o.openFile("mineria/combinaciones.db");
        combinacion2 com = new combinacion2();
        ObjectSet result = db.queryByExample(com);
        combinacion2 combinacion = (combinacion2) result.get(0);
        db.close();

        ArrayList<String> pubmedIDs = new ArrayList<>();

        combinacion.combinaciones.forEach((comb) -> {

            ArrayList<String> lista = new lecturas_PM().busquedaPM_ID(comb, cantIDs);
            //System.out.println(comb + "  "+ lista);
            insertar_en_lista(pubmedIDs, lista);

        });
        
        guardar(pubmedIDs);
        
        config.setPubmedids(true);
        config.guardar();
        System.out.println("ok");
    }

    private void insertar_en_lista(ArrayList<String> pubmedIDS, ArrayList<String> lista) {
        lista.parallelStream().forEach((id) -> {
            if (!pubmedIDS.contains(id)) {
                pubmedIDS.add(id);
                
            }
        });

    }
    
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
