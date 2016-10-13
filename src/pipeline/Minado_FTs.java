/*
    Minado_FT.java


    Copyright (C) 2016.
    Yacson Ramirez (yacson.ramirez@gmail.com), Jose Lopez (jlopez@unet.edu.ve).

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>

 */
package pipeline;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import java.util.ArrayList;

public class Minado_FTs {

    public void minado(String ruta, float confiabilidad, int Iteraciones, int numeroObjetos, boolean criterio) {
        //primera Iteracion
        ArrayList<Lecturas_TFBIND> lecturasTFB = lecturasTFBID(ruta, confiabilidad);
        for (int i = 0; i < lecturasTFB.size(); i++) {
            Factor_Transcripcion2 FT = new Factor_Transcripcion2(lecturasTFB.get(i), criterio, numeroObjetos);
            guardar_Factor_transcripcion(FT);
        }
    }

//        se obtinen lecturas de TFBIND recibe la ruta del archivo bloquesconsenso 
//        y el porsentaje de confiabnilidad, debuelve un listado con los factores de transcripcion 
//        encontrados y algunas caracteristicas que ofrece TFBIND
    private ArrayList<Lecturas_TFBIND> lecturasTFBID(String ruta, float confiabilidad) {

        Lecturas_TFBIND lecturasTFBIND = new Lecturas_TFBIND();
        return lecturasTFBIND.leer_de_archivo(ruta, confiabilidad);

    }

    private void guardar_Factor_transcripcion(Factor_Transcripcion2 FT) {

        ObjectContainer db = Db4o.openFile("FT.db");
        try {
            db.store(FT);
        } finally {

            db.close();
        }
    }

    public void obtenerFT() {
        ObjectContainer db = Db4o.openFile("FT.db");
        Factor_Transcripcion2 FT = new Factor_Transcripcion2();
        try {

            ObjectSet result = db.queryByExample(FT);
            while (result.hasNext()) {

                Factor_Transcripcion2 ft = (Factor_Transcripcion2) result.next();
                ft.imprimir();
            }
        } catch (Exception e) {
        
        }finally{
            db.close();
        }
        
    }
}
