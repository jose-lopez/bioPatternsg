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
import configuracion.combinacion;
import configuracion.configuracion;
import estructura.factorTranscripcion;
import estructura.objetos_Experto;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author yacson
 */
public class combinaciones {

    private int cont = 0;
    private String carga = "";
    boolean error = true;

    public void generar_combinaciones(boolean criterio, configuracion config) {
        //limpiarPantalla();
        //System.out.print("\nGenerando combinaciones de objetos .");

        while (error) {
            limpiarPantalla();
            System.out.print("\nGenerando combinaciones de objetos .");
            try {
                error = false;
                //Si ya existe un archivo mineria/combinaciones.db es eliminado y comienza el proceso de nuevo
                borrar_archivo();

                // Lista en la que se guardaran todas las combinaciones generadas
                ArrayList<String> combinacion = new ArrayList<>();

                objetos_Experto HE = new objetos_Experto();

                // se cargan la informacion de homologos y objetos del experto y se guardan temporalmente en una lista
                ObjectContainer dbhe = Db4o.openFile("mineria/ObjH_E.db");
                ObjectSet resulthe = dbhe.queryByExample(HE);
                ArrayList<objetos_Experto> homologos_experto = new ArrayList<>();
                homologos_experto.addAll(resulthe);
                dbhe.close();
                //---------------------------------------------------------------

                //se generan todas las combinaciones entre objetosdel experto y homologos entre si
                Objs_homologos_Expertos(homologos_experto, combinacion);

                //el siguiente juego de instrucciones genera combinaciones de los objetos encontrados en las diferentes Iteraciones
                factorTranscripcion FT = new factorTranscripcion();
                ObjectContainer db = Db4o.openFile("mineria/FT.db");
                ArrayList<factorTranscripcion> LFT = new ArrayList<>();
                try {
                    ObjectSet result = db.queryByExample(FT);
                    LFT.addAll(result);
                } catch (Exception e) {

                } finally {
                    db.close();
                }

                LFT.parallelStream().forEach((ft) -> {

                    factorTranscripcion factorT = (factorTranscripcion) ft;

                    // combina un objeto con los ligandos encontrados a partir de este
                    factor_ligando(combinacion, factorT);

                    //combina un objeto con los nuevos objetos encontrados a partir de este
                    factor_nuevoObjeto(combinacion, factorT);

                    //combina un objeto minado en las iteraciones con los objetos del experto y homologos
                    //si criterio = false; solo se ejecutara en la iteracion 0 objetos encontrados en tfbind
                    //si criterio = true; se ejecutara en todas las iteraciones
                    if (factorT.getN_Iteracion() == 0 || criterio) {
                        factor_objetos_H_E(combinacion, factorT, homologos_experto);
                    }

                });

                guardar_combinaciones(combinacion);
            } catch (Exception e) {
                error = true;
                System.out.println("Error al generar combinaciones");
            }
        }

        //generadas todas las combinaciones de guardan en mineria/combinaciones.db
        //se guarda el checklist que indica que el proceso de generar combinaciones a terminado
        config.setCombinaciones(true);
        config.guardar();

        System.out.println("ok");

    }

    //Busca todas las permutaciones en un listado de objetos del experto mas homologos
    private void Objs_homologos_Expertos(ArrayList<objetos_Experto> objetos, ArrayList<String> combinacion) {

        ArrayList<String> nombres = new ActivatableArrayList<>();

        //se carga una lista que contine los ID de cada objeto
        objetos.parallelStream().forEach((t) -> {
            objetos_Experto obj = (objetos_Experto) t;
            nombres.add(obj.getID());
        });

        //las siguientes instrucciones encuentra las permutaciones en combinaciones de pares en la lista de ID cargada antes
        if (nombres.size() > 1) {
            IteradorCombinacion it = new IteradorCombinacion(nombres, 2);
            Iterator s = it.iterator();

            while (s.hasNext()) {
                try {
                    List<String> listares = (List<String>) s.next();

                    //obtenida el par de IDs de una combinacion 
                    //se procede a generar las cominaciones entre simbolos y sinonimos de los mismos
                    final objetos_Experto obj1 = objExp(listares.get(0));
                    final objetos_Experto obj2 = objExp(listares.get(1));

                    obj1.listaNombres().parallelStream().forEach((nombre1) -> {
                        obj2.listaNombres().parallelStream().forEach((nombre2) -> {
                            insertar_combinacion(combinacion, nombre1, nombre2);
                        });
                    });

                } catch (Exception e) {
                }

            }
        }
    }

    //consulta un objeto por su ID en la base de datos y retona todo la imformacion de este
    //solo para homologos y objetos del experto
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

    //genera combinaciones entre un objeto minado y ligandos encontrados a partir de este
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

    //genera combinaciones entre un objeto minado y nuevos ojetos encontrados a partir de este
    private void factor_nuevoObjeto(ArrayList<String> combinaciones, factorTranscripcion FT) {

        FT.listaNombres().parallelStream().forEach((nombre1) -> {
            FT.getComplejoProteinico().parallelStream().forEach((comp) -> {
                comp.listaNombres().parallelStream().forEach((nombre2) -> {
                    insertar_combinacion(combinaciones, nombre1, nombre2);
                });
            });
        });

    }

    //genera combinaciones entre un objeto minado y los objetos del experto y homologos
    private void factor_objetos_H_E(ArrayList<String> combinaciones, factorTranscripcion FT, ArrayList<objetos_Experto> listaHE) {

        listaHE.parallelStream().forEach((objExp) -> {
            FT.listaNombres().parallelStream().forEach((nombre1) -> {
                objExp.listaNombres().parallelStream().forEach((nombre2) -> {
                    insertar_combinacion(combinaciones, nombre1, nombre2);
                });
            });
        });
    }

    //iserta una combinacion de palabras a la lista de combinaciones
    public void insertar_combinacion(ArrayList<String> combinaciones, String palabra1, String palabra2) {
        try {

            if (!combinaciones.contains(palabra1 + "+" + palabra2) && !combinaciones.contains(palabra2 + "+" + palabra1) && !palabra1.equals(palabra2)) {
                combinaciones.add(palabra1 + "+" + palabra2);
                cont++;
                if (cont % 1000 == 0) {
                    carga += ".";
                    System.out.print(carga);
                }

            }
        } catch (Exception e) {
            error = true;
            //System.out.println("Error aqui " + palabra1 + "+" + palabra2);
        }

    }

    public void guardar_combinaciones(ArrayList<String> combinacion) {
        ObjectContainer db = Db4o.openFile("mineria/combinaciones.db");
        combinacion comb = new combinacion();
        comb.combinaciones = combinacion;

        try {
            db.store(comb);

        } catch (Exception e) {
            //System.out.println("Error al guardar combinaciones.db...");
            error = true;
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

    private void limpiarPantalla() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}
