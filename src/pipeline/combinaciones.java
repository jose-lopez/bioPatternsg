/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.collections.ActivatableArrayList;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author yacson
 */
public class combinaciones {

    public void generar_combinaciones(boolean criterio, configuracion config) {
        System.out.print("\nGenerando conbinaciones de objetos .....");
        
        borrar_archivo();

        factorTranscripcion FT = new factorTranscripcion();
        ArrayList<String> combinacion = new ArrayList<>();

        objetos_Experto HE = new objetos_Experto();

        ObjectContainer dbhe = Db4o.openFile("mineria/ObjH_E.db");
        ObjectSet resulthe = dbhe.queryByExample(HE);
        ArrayList<objetos_Experto> homologos_experto = new ArrayList<>();
        homologos_experto.addAll(resulthe);

        dbhe.close();

        Objs_homologos_Expertos(homologos_experto, combinacion);

        ObjectContainer db = Db4o.openFile("mineria/FT.db");
        ObjectSet result = db.queryByExample(FT);

        result.parallelStream().forEach((ft) -> {
            factorTranscripcion factorT = (factorTranscripcion) ft;

            factor_ligando(combinacion, factorT);
            factor_nuevoObjeto(combinacion, factorT);

            if (factorT.getN_Iteracion() == 0 || criterio) {
                factor_objetos_H_E(combinacion, factorT, homologos_experto);
            }

        });

        db.close();

        guardar_combinaciones(combinacion);
        
        config.setCombinaciones(true);
        config.guardar();
        
        System.out.println("ok");

    }

    private void Objs_homologos_Expertos(ArrayList<objetos_Experto> objetos, ArrayList<String> combinacion) {

        ArrayList<String> nombres = new ActivatableArrayList<>();

        objetos.parallelStream().forEach((t) -> {
            objetos_Experto obj = (objetos_Experto) t;
            nombres.add(obj.getID());
        });

        if (nombres.size() > 1) {
            IteradorCombinacion it = new IteradorCombinacion(nombres, 2);
            Iterator s = it.iterator();

            while (s.hasNext()) {
                try {
                    List<String> listares = (List<String>) s.next();
                    objetos_Experto obj1 = new objetos_Experto();
                    obj1 = objExp(listares.get(0));
                    objetos_Experto obj2 = new objetos_Experto();
                    obj2 = objExp(listares.get(1));
                    //System.out.println(listares.get(0)+" "+listares.get(1));

                    for (String nombre1 : obj1.listaNombres()) {
                        for (String nombre2 : obj2.listaNombres()) {
                            insertar_combinacion(combinacion, nombre1, nombre2);
                        }
                    }
                } catch (Exception e) {
                }

            }
        }
    }

    private objetos_Experto objExp(String obj) {
        objetos_Experto objExp = new objetos_Experto();
        objExp.setID(obj);

        ObjectContainer db = Db4o.openFile("mineria/ObjH_E.db");
        try {
            ObjectSet result = db.queryByExample(objExp);
            objetos_Experto objs = (objetos_Experto) result.next();
            return objs;

        } catch (Exception e) {
        } finally {
            db.close();
        }

        return objExp;
    }

    private void factor_ligando(ArrayList<String> combinaciones, factorTranscripcion FT) {

        FT.listaNombres().parallelStream().forEach((nombre) -> {
            FT.getComplejoProteinico().parallelStream().forEach((comp) -> {
                comp.getLigandos().parallelStream().forEach((ligando) -> {
                    insertar_combinacion(combinaciones, nombre, ligando.getId());
                    insertar_combinacion(combinaciones, nombre, ligando.getNombre());
                });
            });
        });

    }

    private void factor_nuevoObjeto(ArrayList<String> combinaciones, factorTranscripcion FT) {

        FT.listaNombres().parallelStream().forEach((nombre) -> {
            FT.getComplejoProteinico().parallelStream().forEach((comp) -> {
                comp.listaNombres().parallelStream().forEach((nombre2) -> {
                    insertar_combinacion(combinaciones, nombre, nombre2);
                });
            });
        });

    }

    private void factor_objetos_H_E(ArrayList<String> combinaciones, factorTranscripcion FT, ArrayList<objetos_Experto> listaHE) {

        listaHE.parallelStream().forEach((t) -> {
            objetos_Experto obj = (objetos_Experto) t;
            FT.listaNombres().parallelStream().forEach((nombre) -> {
                obj.listaNombres().parallelStream().forEach((nombre2) -> {
                    insertar_combinacion(combinaciones, nombre, nombre2);
                });
            });
        });

    }

    public void insertar_combinacion(ArrayList<String> combinaciones, String palabra1, String palabra2) {

        if (!combinaciones.contains(palabra1 + "+" + palabra2) && !combinaciones.contains(palabra2 + "+" + palabra1) && !palabra1.equals(palabra2)) {
            combinaciones.add(palabra1 + "+" + palabra2);
            //System.out.println(palabra1 + "+" + palabra2);
        }

    }

    public void guardar_combinaciones(ArrayList<String> combinacion) {
        ObjectContainer db = Db4o.openFile("mineria/combinaciones.db");
        combinacion2 comb = new combinacion2();
        comb.combinaciones = combinacion;

        try {
            db.store(comb);

        } catch (Exception e) {
            System.out.println("Error al guardar combinaciones.db...");
        } finally {
            db.close();
        }

    }
    
     public void borrar_archivo() {
        try {
            File ficherod = new File("mineria/combinaciones.db");
            ficherod.delete();
        } catch (Exception e) {

        }
    }
    
    
}

class combinacion2 {
    public ArrayList<String> combinaciones = new ArrayList<>();
}
