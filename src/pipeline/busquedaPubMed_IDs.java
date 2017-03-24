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
 * @author yacson-ramirez
 */
public class busquedaPubMed_IDs {

    public ArrayList<String> busqueda_IDs(boolean criterioCombinacion, int cantIDs, boolean mostrarComb, configuracion config) {
        System.out.println("\nGenerando combinaciones de objetos...");
        Objs_homologos_Expertos(mostrarComb);
        borrar_archivo("mineria/combinaciones.db");
        ObjectContainer db = Db4o.openFile("mineria/FT.db");
        factorTranscripcion FT = new factorTranscripcion();
        try {

            ObjectSet result = db.queryByExample(FT);
            while (result.hasNext()) {
                try {
                    factorTranscripcion ft = (factorTranscripcion) result.next();
                    factor_ligando(ft,mostrarComb);
                    factor_nuevoObjeto(ft,mostrarComb);
                    //Si criterioCombinacion es 'true' los objetos del experto se combinaran en todas las Iteraciones
                    //Si criterioCombinacion es 'false' los objetos del experto solo se combinaran en la primera Iteracion
                    if (ft.getN_Iteracion() == 0 || criterioCombinacion) {
                        //System.out.println("FT + Objetos experto y homologos");
                        factor_objetos_H_E(ft,mostrarComb);
                    }
                } catch (Exception e) {

                }

            }
        } catch (Exception e) {

        } finally {
            db.close();
        }
        config.setCombinaciones(true);
        config.guardar(config);
        System.out.println("Listo...");
        
        return consulta_PudMed(cantIDs);
    }

    public ArrayList<String> consulta_PudMed(int cantIDs) {
        System.out.println("Consulta Pudmed IDs");
        ArrayList<String> listaPM = new ArrayList<>();
        ObjectContainer db = Db4o.openFile("mineria/combinaciones.db");
        combinacion com = new combinacion();
        try {
            ObjectSet result = db.queryByExample(com);
            System.out.println("Combinaciones: " + result.size());
            while (result.hasNext()) {
                try {
                    combinacion c = (combinacion) result.next();
                    ArrayList<String> lista = new ArrayList<>();
                    //System.out.print(c.getPalabra1() + "+" + c.getPalabra2());
                    lista = new lecturas_PM().busquedaPM_ID(c.getPalabra1() + "+" + c.getPalabra2(), cantIDs);
                    //System.out.println("  ids: "+lista.size());
                    for (int i = 0; i < lista.size(); i++) {
                        if (!listaPM.contains(lista.get(i))) {
                            listaPM.add(lista.get(i));
                        }
                    }
                } catch (Exception e) {

                }
            }
        } catch (Exception e) {

        } finally {
            db.close();
        }
        System.out.println(listaPM.size() + " PudMed IDs Encontrados");
        return listaPM;
    }

    private void factor_nuevoObjeto(factorTranscripcion FT,boolean mostrarComb) {

        for (int i = 0; i < FT.listaNombres().size(); i++) {
            for (int j = 0; j < FT.getComplejoProteinico().size(); j++) {
                ArrayList<String> listNO = FT.getComplejoProteinico().get(j).listaNombres();
                for (int k = 0; k < listNO.size(); k++) {
                    guardarCombinacion(FT.listaNombres().get(i), listNO.get(k), true,mostrarComb);
                }
            }
        }
    }

    private void factor_ligando(factorTranscripcion FT,boolean mostrarComb) {

        for (int i = 0; i < FT.listaNombres().size(); i++) {
            for (int j = 0; j < FT.getComplejoProteinico().size(); j++) {
                for (int k = 0; k < FT.getComplejoProteinico().get(j).getLigandos().size(); k++) {
                    guardarCombinacion(FT.listaNombres().get(i), FT.getComplejoProteinico().get(j).getLigandos().get(k).getId(), true,mostrarComb);
                    guardarCombinacion(FT.listaNombres().get(i), FT.getComplejoProteinico().get(j).getLigandos().get(k).getNombre(), true,mostrarComb);
                }
            }
        }
    }

    private void factor_objetos_H_E(factorTranscripcion FT,boolean mostrarComb) {
        ObjectContainer db = Db4o.openFile("mineria/ObjH_E.db");
        objetos_Experto Obj = new objetos_Experto();
        try {

            ObjectSet result = db.queryByExample(Obj);
            while (result.hasNext()) {
                try {
                    objetos_Experto obj = (objetos_Experto) result.next();

                    for (int i = 0; i < FT.listaNombres().size(); i++) {
                        for (int j = 0; j < obj.getHGNC().size(); j++) {
                            guardarCombinacion(FT.listaNombres().get(i), obj.listaNombres().get(i), true,mostrarComb);
                        }
                    }
                } catch (Exception e) {
                }

            }
        } catch (Exception e) {

        } finally {
            db.close();
        }
    }

    private void Objs_homologos_Expertos(boolean mostrarComb) {
        ObjectContainer db = Db4o.openFile("mineria/ObjH_E.db");
        objetos_Experto Obj = new objetos_Experto();
        ArrayList<String> nombres = new ActivatableArrayList<>();
        try {
            ObjectSet result = db.queryByExample(Obj);
            while (result.hasNext()) {
                try {
                    objetos_Experto obj = (objetos_Experto) result.next();
                    nombres.add(obj.getID());
                    //System.out.println(obj.getID());
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {

        } finally {
            db.close();
        }
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
                    for (int i = 0; i < obj1.listaNombres().size(); i++) {
                        for (int j = 0; j < obj2.listaNombres().size(); j++) {
                            //System.out.println(obj1.listaNombres().get(i)+" "+obj2.listaNombres().get(j));
                            guardarCombinacion(obj1.listaNombres().get(i), obj2.listaNombres().get(j), true,mostrarComb);

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
            //System.out.println(objs.listaNombres());
            return objs;

        } catch (Exception e) {

        } finally {
            db.close();
        }

        return objExp;
    }

    private void guardarCombinacion(String pal1, String pal2, boolean buscar,boolean mostarComb) {

        combinacion comb = new combinacion(pal1, pal2);
        combinacion comb2 = new combinacion(pal2, pal1);
        if (buscar && (!consultar(comb) && !consultar(comb2))) {
            //System.out.println(pal1 + "+" + pal2);
            agregar(comb,mostarComb);
        }

    }

    private Boolean consultar(combinacion com) {
        boolean existe = false;

        ObjectContainer db = Db4o.openFile("mineria/combinaciones.db");
        try {
            //System.out.println(com.getPalabra1()+"+"+com.getPalabra2());
            ObjectSet result = db.queryByExample(com);
            if (result.size() > 0) {
                existe = true;
            }
        } catch (Exception e) {

        } finally {
            db.close();
        }

        return existe;
    }

    private void agregar(combinacion com, boolean mostrar) {
        ObjectContainer db = Db4o.openFile("mineria/combinaciones.db");

        try {
            db.store(com);
            if (mostrar) {
                System.out.println(com.getPalabra1() + "+" + com.getPalabra2());
            }
        } catch (Exception e) {
            System.out.println("Error al guardar en la base de datos combinaciones.db");
        } finally {

            db.close();
        }

    }

    private void borrar_archivo(String nombre) {
        try {
            File ficherod = new File(nombre);
            ficherod.delete();
        } catch (Exception e) {

        }
    }

}

class combinacion {

    private String palabra1;
    private String palabra2;

    public combinacion() {
    }

    public combinacion(String palabra1, String palabra2) {
        this.palabra1 = palabra1;
        this.palabra2 = palabra2;
    }

    public String getPalabra1() {
        return palabra1;
    }

    public void setPalabra1(String palabra1) {
        this.palabra1 = palabra1;
    }

    public String getPalabra2() {
        return palabra2;
    }

    public void setPalabra2(String palabra2) {
        this.palabra2 = palabra2;
    }

}
