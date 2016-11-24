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

    public ArrayList<String> busqueda_IDs(boolean todas, int cantIDs) {
        System.out.println("\nGenerando combinaciones de objetos...");
        Objs_homologos_Expertos();
        borrar_archivo("combinaciones.db");
        ObjectContainer db = Db4o.openFile("FT.db");
        factorTranscripcion FT = new factorTranscripcion();
        try {

            ObjectSet result = db.queryByExample(FT);
            while (result.hasNext()) {

                factorTranscripcion ft = (factorTranscripcion) result.next();
                //System.out.println("FT + Ligando");
                factor_ligando(ft);
                //System.out.println("FT + Nuevos Objetos");
                factor_nuevoObjeto(ft);
                if (ft.getN_Iteracion() == 0 || todas) {
                    //System.out.println("FT + Objetos experto y homologos");
                    factor_objetos_H_E(ft);
                }

            }
        } catch (Exception e) {

        } finally {
            db.close();
        }
        System.out.println("Listo...");
        return consulta_PudMed(cantIDs);
    }

    public ArrayList<String> consulta_PudMed(int cantIDs) {
        System.out.println("Consulta Pudmed IDs");
        ArrayList<String> listaPM = new ArrayList<>();
        ObjectContainer db = Db4o.openFile("combinaciones.db");
        combinacion com = new combinacion();
        ObjectSet result = db.queryByExample(com);
        System.out.println("Combinaciones: "+result.size());
        while (result.hasNext()) {
            combinacion c = (combinacion) result.next();
            ArrayList<String> lista = new ArrayList<>();
            System.out.print(c.getPalabra1() + "+" + c.getPalabra2());
            lista = new lecturas_PM().busquedaPM_ID(c.getPalabra1() + "+" + c.getPalabra2(), cantIDs);
            System.out.println("  ids: "+lista.size());
            for (int i = 0; i < lista.size(); i++) {
                if (!listaPM.contains(lista.get(i))) {
                    listaPM.add(lista.get(i));
                }
            }
        }

        System.out.println(listaPM.size() + " PudMed IDs Encontrados");
        return listaPM;
    }

    private void factor_nuevoObjeto(factorTranscripcion FT) {

        for (int i = 0; i < FT.getLecturas_HGNC().listaNombres().size(); i++) {
            for (int j = 0; j < FT.getComplejoProteinico().size(); j++) {
                ArrayList<String> listNO = FT.getComplejoProteinico().get(j).listaNombres();
                for (int k = 0; k < listNO.size(); k++) {
                    guardarCombinacion(FT.getLecturas_HGNC().listaNombres().get(i), listNO.get(k), true);
                }
            }
        }
    }

    private void factor_ligando(factorTranscripcion FT) {

        for (int i = 0; i < FT.getLecturas_HGNC().listaNombres().size(); i++) {
            for (int j = 0; j < FT.getComplejoProteinico().size(); j++) {
                for (int k = 0; k < FT.getComplejoProteinico().get(j).getLigandos().size(); k++) {
                    guardarCombinacion(FT.getLecturas_HGNC().listaNombres().get(i), FT.getComplejoProteinico().get(j).getLigandos().get(k).getId(), true);
                    guardarCombinacion(FT.getLecturas_HGNC().listaNombres().get(i), FT.getComplejoProteinico().get(j).getLigandos().get(k).getNombre(), true);
                }
            }
        }
    }

    private void factor_objetos_H_E(factorTranscripcion FT) {
        ObjectContainer db = Db4o.openFile("ObjH_E.db");
        lecturas_HGNC Obj = new lecturas_HGNC();
        try {

            ObjectSet result = db.queryByExample(Obj);
            while (result.hasNext()) {

                lecturas_HGNC obj = (lecturas_HGNC) result.next();

                for (int i = 0; i < FT.getLecturas_HGNC().listaNombres().size(); i++) {
                    for (int j = 0; j < obj.getHGNC().size(); j++) {
                        guardarCombinacion(FT.getLecturas_HGNC().listaNombres().get(i), obj.listaNombres().get(i), true);
                    }
                }

            }
        } catch (Exception e) {

        } finally {
            db.close();
        }
    }

    private void Objs_homologos_Expertos() {
        ObjectContainer db = Db4o.openFile("ObjH_E.db");
        lecturas_HGNC Obj = new lecturas_HGNC();
        ArrayList<String> nombres = new ActivatableArrayList<>();
        try {
            ObjectSet result = db.queryByExample(Obj);
            while (result.hasNext()) {
                lecturas_HGNC obj = (lecturas_HGNC) result.next();
                nombres.add(obj.getID());
            }
        } catch (Exception e) {

        } finally {
            db.close();
        }

        IteradorCombinacion it = new IteradorCombinacion(nombres, 2);
        Iterator s = it.iterator();

        while (s.hasNext()) {

            List<String> listares = (List<String>) s.next();
            lecturas_HGNC obj1 = new lecturas_HGNC();
            obj1 = HGNC(listares.get(0));
            lecturas_HGNC obj2 = new lecturas_HGNC();
            obj2 = HGNC(listares.get(1));

            for (int i = 0; i < obj1.listaNombres().size(); i++) {
                for (int j = 0; j < obj2.listaNombres().size(); j++) {
                    guardarCombinacion(obj1.listaNombres().get(i), obj2.listaNombres().get(j), false);

                }
            }

        }
    }

    private lecturas_HGNC HGNC(String obj) {
        lecturas_HGNC HGNC = new lecturas_HGNC();
        HGNC.setID(obj);

        ObjectContainer db = Db4o.openFile("ObjH_E.db");
        try {
            ObjectSet result = db.queryByExample(HGNC);
            lecturas_HGNC objs = (lecturas_HGNC) result.next();
            return objs;

        } catch (Exception e) {

        } finally {
            db.close();
        }

        return HGNC;
    }

    private void guardarCombinacion(String pal1, String pal2, boolean buscar) {

        combinacion comb = new combinacion(pal1, pal2);
        combinacion comb2 = new combinacion(pal2, pal1);
        if (buscar && (!consultar(comb) && !consultar(comb2))) {
            //System.out.println(pal1 + "+" + pal2);
            agregar(comb);
        }

    }

    private Boolean consultar(combinacion com) {
        boolean existe = false;

        ObjectContainer db = Db4o.openFile("combinaciones.db");
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

    private void agregar(combinacion com) {
        ObjectContainer db = Db4o.openFile("combinaciones.db");

        try {
            db.store(com);
            System.out.println(com.getPalabra1() + "+" + com.getPalabra2());
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
