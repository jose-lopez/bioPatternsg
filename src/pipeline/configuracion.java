/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 *
 * @author yacson-ramirez
 */
public class configuracion {

    private int numIteraciones;
    private int cantComplejos;
    private float confiabilidad_tfbind;
    private String RegionPromotora;
    private ArrayList<lecturas_TFBIND> tfbind;
    
    public configuracion(){
        tfbind = new ArrayList<lecturas_TFBIND>();
    }
    
    public void guardarConfiguracion(String regionProm, int numIter, int cantCompl, float conf) {
        this.RegionPromotora = regionProm;
        this.cantComplejos = cantCompl;
        this.numIteraciones = numIter;
        this.confiabilidad_tfbind = conf;
        ObjectContainer db = Db4o.openFile("mineria/config.db");
        try {
            db.store(this);
            System.out.println("Configuracion guardada...");
        } catch (Exception e) {
            System.out.println("No fue posible guardar la configuracion");
        } finally {
            db.close();
        }
    }
    public void guardar_lecturasTFBIND(ArrayList<lecturas_TFBIND> lecturas){
        ObjectContainer db = Db4o.openFile("mineria/config.db");
        configuracion conf = new configuracion();
         try {
            ObjectSet result = db.queryByExample(conf);

            while (result.hasNext()) {
                configuracion config = (configuracion) result.next();
                config.tfbind = lecturas;
                db.store(config);
                System.out.println("guardado");
            }
        } catch (Exception e) {
            System.out.println("No fue posible guardar la configuracion");
        } finally {
            db.close();
        }
    }
    
    public configuracion verconfiguracion() {
        ObjectContainer db = Db4o.openFile("mineria/config.db");
        configuracion conf = new configuracion();
        try {
            ObjectSet result = db.queryByExample(conf);

            while (result.hasNext()) {
                configuracion config = (configuracion) result.next();
                this.RegionPromotora = config.RegionPromotora;
                this.cantComplejos = config.cantComplejos;
                this.numIteraciones = config.numIteraciones;
                this.confiabilidad_tfbind = config.confiabilidad_tfbind;
                this.tfbind = config.tfbind;

            }
        } catch (Exception e) {
            System.out.println("No fue posible guardar la configuracion");
        } finally {
            db.close();
        }

        System.out.println("\n**Configuracion de minado**");
        System.out.println("Region promotora: " + this.RegionPromotora);
        System.out.println("Cantidad de complejos: " + this.cantComplejos);
        System.out.println("Numero de iteraciones: " + this.numIteraciones);
        System.out.println("confiabilidad TFBind: " + this.confiabilidad_tfbind);
        for (int i = 0; i < tfbind.size(); i++) {
            System.out.println(tfbind.get(i).getFactor());
        }
        return this;
    }

    public void reanudar_mineria() {
        verconfiguracion();
        objetosMineria objMin = new objetosMineria();
        objMin = recuperarObjetosMin();

        if (revisarObjH_E("homologos").size() > 0) {
            System.out.println("Reanudar desde busqueda de homologos ...");
            reanudar(1, objMin);
        }else if (revisarObjH_E("objetos_Experto.txt").size() > 0) {
            System.out.println("Reanudar desde busqueda de objetos Experto ...");
            reanudar(2, objMin);
        }else if(objMin.getIteracion() == 0){
            System.out.println("Reanudando Iteracion: " + objMin.getIteracion());
            reanudar(3, objMin);
        }

        
    }
    
    private void reanudar(int punto,objetosMineria objetosMineria){
        minado_FT mfts = new minado_FT();
        switch(punto){
            case 1:
                mfts.buscarHomologos(revisarObjH_E("homologos"),objetosMineria);
                mfts.buscarObjetosExperto(listaObjetos_homologosExperto("objetos_Experto.txt"), objetosMineria);
                mfts.primeraIteracion(RegionPromotora, confiabilidad_tfbind,cantComplejos, objetosMineria);
                mfts.Iteraciones(false, new ArrayList<String>(), cantComplejos, numIteraciones, objetosMineria);
                break;
            case 2:
                mfts.buscarObjetosExperto(revisarObjH_E("objetos_Experto.txt"), objetosMineria);
                mfts.primeraIteracion(RegionPromotora, confiabilidad_tfbind,cantComplejos, objetosMineria);
                mfts.Iteraciones(false, new ArrayList<String>(), cantComplejos, numIteraciones, objetosMineria);
                break;
            case 3:
                mfts.primeraIteracion(RegionPromotora, confiabilidad_tfbind,cantComplejos, objetosMineria);
                mfts.Iteraciones(false, new ArrayList<String>(), cantComplejos, numIteraciones, objetosMineria);
        }
        
    }
    
    private void buscarobj(objetosMineria objMin){
        ArrayList<String> Lista = new ArrayList<>();
        ArrayList<String> NuevosObj = new ArrayList<>();
        factorTranscripcion ft = new factorTranscripcion();

        for (int i = 0; i < objMin.getNuevos_objetos().size(); i++) {
            if (buscarObjeto(objMin.getNuevos_objetos().get(i), ft)) {
                objMin.getObjetos_minados().add(objMin.getNuevos_objetos().get(i));
                ft.NuevosObjetos(NuevosObj);
            } else {
                Lista.add(objMin.getNuevos_objetos().get(i));
            }
        }
        System.out.println("\n Nuevos Objetos");
        for (int i = 0; i < Lista.size(); i++) {
            System.out.println(Lista.get(i));
        }

        objMin.setNuevos_objetos(NuevosObj);
    }
    
    private boolean buscarObjeto(String objeto, factorTranscripcion FT) {
        objetosMineria obj = new objetosMineria();
        ObjectContainer db = Db4o.openFile("mineria/FT.db");
        factorTranscripcion ft = new factorTranscripcion();
        ft.setID(objeto);
        try {
            ObjectSet result = db.queryByExample(ft);
            while (result.hasNext()) {
                FT = (factorTranscripcion) result.next();
                return true;
            }
        } catch (Exception e) {

        } finally {
            db.close();
        }

        return false;
    }

    private ArrayList<String> revisarObjH_E(String archivo) {
        ObjectContainer db = Db4o.openFile("mineria/ObjH_E.db");
        objetos_Experto Obj = new objetos_Experto();
        ArrayList<String> listaObjetos = listaObjetos_homologosExperto(archivo);
        ArrayList<String> listaFaltantes = new ArrayList<>();
        try {
            ObjectSet result = db.queryByExample(Obj);
            while (result.hasNext()) {
                objetos_Experto objExp = (objetos_Experto) result.next();
                for (int i = 0; i < listaObjetos.size(); i++) {
                    if (objExp.getID().equals(listaObjetos.get(i))) {
                        listaObjetos.remove(i);
                    }
                }
            }

        } catch (Exception e) {

        } finally {
            db.close();
        }

        return listaObjetos;
    }

    private ArrayList<String> listaObjetos_homologosExperto(String ruta) {
        ArrayList<String> lista = new ArrayList<>();
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            archivo = new File(ruta);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String lectura;
            while ((lectura = br.readLine()) != null) {
                lista.add(lectura);
            }
        } catch (Exception e) {
        }
        return lista;
    }

    private objetosMineria recuperarObjetosMin() {
        objetosMineria obj = new objetosMineria();
        ObjectContainer db = Db4o.openFile("mineria/objetosMineria.db");

        try {
            ObjectSet result = db.queryByExample(obj);
            while (result.hasNext()) {
                obj = (objetosMineria) result.next();
            }
        } catch (Exception e) {
        } finally {
            db.close();
        }

        return obj;
    }

    public int getNumIteraciones() {
        return numIteraciones;
    }

    public void setNumIteraciones(int numIteraciones) {
        this.numIteraciones = numIteraciones;
    }

    public int getCantComplejos() {
        return cantComplejos;
    }

    public void setCantComplejos(int cantComplejos) {
        this.cantComplejos = cantComplejos;
    }

    public float getConfiabilidad_tfbind() {
        return confiabilidad_tfbind;
    }

    public void setConfiabilidad_tfbind(float confiabilidad_tfbind) {
        this.confiabilidad_tfbind = confiabilidad_tfbind;
    }

}
