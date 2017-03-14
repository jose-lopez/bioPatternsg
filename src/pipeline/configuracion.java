/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

/**
 *
 * @author yacson-ramirez
 */
public class configuracion {

    private int numIteraciones;
    private int cantComplejos;
    private float confiabilidad_tfbind;
    private String RegionPromotora;

    public void guardarConfiguracion(String regionProm, int numIter, int cantCompl, float conf) {
        this.RegionPromotora = regionProm;
        this.cantComplejos = cantCompl;
        this.numIteraciones = numIter;
        this.confiabilidad_tfbind = conf;
        ObjectContainer db = Db4o.openFile("mineria/config.db");
        try {
            db.store(this);
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

            }
        } catch (Exception e) {
            System.out.println("No fue posible guardar la configuracion");
        } finally {
            db.close();
        }
        
        System.out.println("\n**Configuracion de minado**");
        System.out.println("Region promotora: "+this.RegionPromotora);
        System.out.println("Cantidad de complejos: "+this.cantComplejos);
        System.out.println("Numero de iteraciones: "+this.numIteraciones);
        System.out.println("confiabilidad TFBind: "+this.confiabilidad_tfbind);
        return this;
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
