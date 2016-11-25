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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class minado_FT {

    public void minado(String ruta, float confiabilidad, int Iteraciones, int numeroObjetos, boolean criterio, int opcion) {
        Runtime garbage = Runtime.getRuntime();
        objetosMineria objetosMineria = new objetosMineria();
        //Se crea un nuevo archivo de Objectos minados
        borrar_archivo("objetosMinados.txt");
        new objetosMinados().crear_archivo();
        //Nuevo archivo de objetos Homologos y Objetos de Experto
        borrar_archivo("ObjH_E.db");
        //Borrar archivo de base de datos de factores de trancripcion
        borrar_archivo("FT.db");
        borrar_archivo("objetosMineria.db");
               
        leer_archivo_homologos(objetosMineria, opcion);
        leer_archivo_ObjetosExperto(objetosMineria, opcion);

        //Primera Iteracion
        System.out.println("\n====Iteracion 0====\n");
        objetosMineria.setIteracion(0);
        ArrayList<lecturas_TFBIND> lecturasTFB = lecturasTFBID(ruta, confiabilidad);
        System.out.println(lecturasTFB.size() + " Factores de transcripcion encontrados");
        for (int i = 0; i < lecturasTFB.size(); i++) {
            factorTranscripcion FT = new factorTranscripcion(lecturasTFB.get(i), criterio, numeroObjetos, objetosMineria, opcion);
            objetosMineria.getObjetos_minados().add(FT.getID());
            objetosMineria.agregar_objeto(FT.getComplejoProteinico());
            new objetosMinados().agregar_objetos(FT);
            guardar_Factor_transcripcion(FT);
            System.out.println("Listo...");
            FT=null;
            garbage.gc();
        }
        guardar_objetosIteracion(objetosMineria);
        objetosMineria.imprimir();

        //Iteracion 2 en adelante
        for (int i = 1; i < Iteraciones; i++) {
            System.out.println("\n====Iteracion " + (i) + "====\n");
            ArrayList<String> Lista = objetosMineria.getNuevos_objetos();
            objetosMineria.setNuevos_objetos(new ArrayList<String>());
           
            for (int j = 0; j < Lista.size(); j++) {
                factorTranscripcion FT = new factorTranscripcion(Lista.get(j), true, i, numeroObjetos, opcion);
                objetosMineria.getObjetos_minados().add(FT.getID());
                objetosMineria.agregar_objeto(FT.getComplejoProteinico());
                new objetosMinados().agregar_objetos(FT);
                guardar_Factor_transcripcion(FT);
                System.out.println("Listo....");
                garbage.gc();
            }
            objetosMineria.setIteracion(i);
            guardar_objetosIteracion(objetosMineria);
            objetosMineria.imprimir();
        }

    }

//  se obtinen lecturas de TFBIND recibe la ruta del archivo bloquesconsenso 
//  y el porsentaje de confiabnilidad, debuelve un listado con los factores de transcripcion 
//  encontrados y algunas caracteristicas que ofrece TFBIND
    private ArrayList<lecturas_TFBIND> lecturasTFBID(String ruta, float confiabilidad) {

        lecturas_TFBIND lecturasTFBIND = new lecturas_TFBIND();
        return lecturasTFBIND.leer_de_archivo(ruta, confiabilidad);

    }

    public void crear_archivo(String nombre) {

        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter(nombre);
        } catch (IOException ex) {
            Logger.getLogger(minado_FT.class.getName()).log(Level.SEVERE, null, ex);
        }
        pw = new PrintWriter(fichero);

    }

    private void borrar_archivo(String nombre) {
        try {
            File ficherod = new File(nombre);
            ficherod.delete();
        } catch (Exception e) {

        }
    }

        
    private void guardar_Factor_transcripcion(factorTranscripcion FT) {

        ObjectContainer db = Db4o.openFile("FT.db");
        try {
            db.store(FT);
        }catch(Exception e){
            System.out.println("Error al guardar FT.db...");
        }finally {

            db.close();
        }
    }
    
    public void guardar_objetosIteracion(objetosMineria objetosMin){
        ObjectContainer db = Db4o.openFile("objetosMineria.db");
        try {
            db.store(objetosMin);
        }catch(Exception e){
            System.out.println("Error al guardar en objetosMineria.db...");
        }finally {
            db.close();
        }
    }

    public void obtenerFT() {

        ObjectContainer db = Db4o.openFile("FT.db");
        factorTranscripcion FT = new factorTranscripcion();
        try {

            ObjectSet result = db.queryByExample(FT);
            while (result.hasNext()) {

                factorTranscripcion ft = (factorTranscripcion) result.next();
                ft.imprimir();
                System.out.println("====================================================");
            }
        } catch (Exception e) {

        } finally {
            db.close();
        }

    }

    public void leer_archivo_homologos(objetosMineria objetosMineria, int opcion) {

        System.out.println("**Leyendo archivo de Homologos...");

        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            archivo = new File("homologos");
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String lectura;
            while ((lectura = br.readLine()) != null) {

                System.out.println("busqueda.." + lectura);
                lecturas_HGNC HGNC = new lecturas_HGNC();
                HGNC.busqueda_genenames(lectura, false, opcion);
                new objetosMinados().agregar_objetos(HGNC);
                objetosMineria.agregar_objeto(HGNC);
                guardarObjetos_Homologos_Experto(HGNC);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }

    public void leer_archivo_ObjetosExperto(objetosMineria objetosMineria, int opcion) {

        System.out.println("\n**Leyendo archivo de Objetos Experto...");

        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            archivo = new File("objetos_Experto.txt");
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String lectura;

            while ((lectura = br.readLine()) != null) {

                String[] separa = lectura.split(";");

                for (int i = 0; i < separa.length; i++) {
                    System.out.println("Busqueda.." + lectura);
                    lecturas_HGNC HGNC = new lecturas_HGNC();
                    HGNC.busqueda_genenames(lectura, false, opcion);

                    for (int j = 0; j < separa.length; j++) {
                        if (HGNC.getSinonimosExperto().contains(separa[j])) {
                            HGNC.getSinonimosExperto().add(separa[j]);
                        }

                    }
                    new objetosMinados().agregar_objetos(HGNC);
                    objetosMineria.agregar_objeto(HGNC);
                    guardarObjetos_Homologos_Experto(HGNC);

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }

    private void guardarObjetos_Homologos_Experto(lecturas_HGNC HGNC) {

        ObjectContainer db = Db4o.openFile("ObjH_E.db");
        try {
            db.store(HGNC);
        }catch(Exception e){
            System.out.println("Error al guardar en la base de datos ObjH_E.db");
        }finally {

            db.close();
        }

    }
}

//-------------------------------------------------------------------------------------------------//
class objetosMineria {

    private int Iteracion;
    private ArrayList<String> objetos_minados;
    private ArrayList<String> nuevos_objetos;
    
    public objetosMineria() {

        this.objetos_minados = new ArrayList<>();
        this.nuevos_objetos = new ArrayList<>();
    }

    public void imprimir() {

        //----------------------
        System.out.println();
        System.out.println("Objetos Minados: ");
        for (int i = 0; i < getObjetos_minados().size(); i++) {
            System.out.println(getObjetos_minados().get(i));
        }
        System.out.println();
        System.out.println("Nuevos Objetos: ");
        for (int i = 0; i < getNuevos_objetos().size(); i++) {
            System.out.println(getNuevos_objetos().get(i));
        }

    }

    public int getIteracion() {
        return Iteracion;
    }

    public void setIteracion(int Iteracion) {
        this.Iteracion = Iteracion;
    }

    public ArrayList<String> getObjetos_minados() {
        return objetos_minados;
    }

    public void setObjetos_minados(ArrayList<String> objetos_minados) {
        this.objetos_minados = objetos_minados;
    }
    
    public void agregarObjetosMinado(String objeto){
        this.objetos_minados.add(objeto);
       
    }

    public ArrayList<String> getNuevos_objetos() {
        return nuevos_objetos;
    }

    public void setNuevos_objetos(ArrayList<String> nuevos_objetos) {
        this.nuevos_objetos = nuevos_objetos;
    }

    public void agregar_objeto(lecturas_HGNC HGNC) {

        if (!objetos_minados.contains(HGNC.getID())) {

            if (!nuevos_objetos.contains(HGNC.getID())) {
                nuevos_objetos.add(HGNC.getID());
                
            }

        }

    }

    public void agregar_objeto(ArrayList<complejoProteinico> objetos) {

        for (int i = 0; i < objetos.size(); i++) {

            for (int j = 0; j < objetos.get(i).getHGNC().size(); j++) {

                lecturas_HGNC hgnc = objetos.get(i).getHGNC().get(j);
                if (!objetos_minados.contains(hgnc.getID()) && objetos != null) {

                    if (!nuevos_objetos.contains(hgnc.getID())) {
                        nuevos_objetos.add(hgnc.getID());
                        
                    }

                }

            }

        }

    }
    
}
