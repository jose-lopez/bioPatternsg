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

    public void minado(String ruta, float confiabilidad, int Iteraciones, int numeroObjetos,boolean GO, boolean MESH) {

        objetosMineria objetosMineria = new objetosMineria();
        crearCarpeta("mineria");

        configuracion config = new configuracion();
        config.guardarConfiguracion(ruta, Iteraciones, numeroObjetos, confiabilidad,GO,MESH);

        new objetosMinados().crear_archivo();

        buscarHomologos(listaObjetos_homologosExperto("homologos"), objetosMineria, config,GO,MESH);

        buscarObjetosExperto(listaObjetos_homologosExperto("objetos_Experto.txt"), objetosMineria, config,GO,MESH);

        //primera Iteracion partiendo de TFBind
        primeraIteracion(ruta, confiabilidad, numeroObjetos, objetosMineria, config, new ArrayList<lecturas_TFBIND>(),GO,MESH);
        //Segunda Iteracion en adelante partiendo de nuevos objetos encontrados en PDB
        Iteraciones(false, new ArrayList<String>(), numeroObjetos, Iteraciones, objetosMineria, config,1,GO,MESH);
    }

    public void primeraIteracion(String ruta, float confiabilidad, int numeroObjetos, objetosMineria objetosMineria, configuracion config, ArrayList<lecturas_TFBIND> lecturas,boolean GO, boolean MESH) {
        //Primera Iteracion
        System.out.println("\n\n====Iteracion 0====\n");
        objetosMineria.setIteracion(0);
        ArrayList<lecturas_TFBIND> lecturasTFB;
        if (lecturas.size() == 0) {
            lecturasTFB = lecturasTFBID(ruta, confiabilidad);
            System.out.println("* " + lecturasTFB.size() + " Factores de transcripcion encontrados");
            config.setTfbind(lecturasTFB);
            config.guardar(config);
        } else {
            lecturasTFB = lecturas;
        }
        for (int i = 0; i < lecturasTFB.size(); i++) {
            factorTranscripcion FT = new factorTranscripcion(lecturasTFB.get(i), numeroObjetos, objetosMineria,GO,MESH);
            objetosMineria.getObjetos_minados().add(FT.getID());
            objetosMineria.agregar_objeto(FT.getComplejoProteinico());
            new objetosMinados().agregar_objetos(FT);
            guardar_Factor_transcripcion(FT);
            System.out.println("...ok");
            FT = null;
        }
        guardar_objetosIteracion(objetosMineria);
        config.setLecturas_tfbind(true);
        config.guardar(config);
        objetosMineria.imprimir();

    }

    public void Iteraciones(boolean ReinicioMin, ArrayList<String> Lista, int numeroObjetos, int Iteraciones, objetosMineria objetosMineria, configuracion config, int iter,boolean GO, boolean MESH) {
        //Iteracion 2 en adelante
        for (int i = iter; i < Iteraciones; i++) {
            System.out.println("\n\n====Iteracion " + (i) + "====\n");

            if (!ReinicioMin) {
                Lista = objetosMineria.getNuevos_objetos();
                objetosMineria.setNuevos_objetos(new ArrayList<String>());
            }
            ReinicioMin = false;

            for (int j = 0; j < Lista.size(); j++) {
                factorTranscripcion FT = new factorTranscripcion(Lista.get(j), i, numeroObjetos,GO,MESH);
                objetosMineria.getObjetos_minados().add(FT.getID());
                objetosMineria.agregar_objeto(FT.getComplejoProteinico());
                new objetosMinados().agregar_objetos(FT);
                guardar_Factor_transcripcion(FT);
                System.out.println("Listo....");
            }

            objetosMineria.setIteracion(i);
            guardar_objetosIteracion(objetosMineria);
            objetosMineria.imprimir();
        }
        config.setProcesoIteraciones(true);
        config.guardar(config);
    }
//  se obtinen lecturas de TFBIND recibe la ruta del archivo bloquesconsenso 
//  y el porsentaje de confiabnilidad, debuelve un listado con los factores de transcripcion 
//  encontrados y algunas caracteristicas que ofrece TFBIND

    private ArrayList<lecturas_TFBIND> lecturasTFBID(String ruta, float confiabilidad) {

        lecturas_TFBIND lecturasTFBIND = new lecturas_TFBIND();
        return lecturasTFBIND.leer_de_archivo(ruta, confiabilidad);

    }

    public void reanudarMinado() {

        configuracion conf = new configuracion();
        conf.obtener();

        objetosMineria objMin = new objetosMineria();
        objMin = recuperarObjetosMin();

        System.out.println("Reanudando Iteracion: " + objMin.getIteracion());

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

    public objetosMineria recuperarObjetosMin() {
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

    public void crearCarpeta(String nombre) {
        File f = new File(nombre);
        try {
            borrarDirectorio(f);
        } catch (Exception e) {

        }
        if (f.delete()) {
            // System.out.println("El directorio   ha sido borrado correctamente");
        } else {
            //System.out.println("El directorio  no se ha podido borrar");
        }

        File file = new File(nombre);
        file.mkdir();

    }

    private void borrarDirectorio(File directorio) {
        File[] ficheros = directorio.listFiles();
        for (int i = 0; i < ficheros.length; i++) {
            if (ficheros[i].isDirectory()) {
                borrarDirectorio(ficheros[i]);
            }
            ficheros[i].delete();
        }
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

    public void borrar_archivo(String nombre) {
        try {
            File ficherod = new File(nombre);
            ficherod.delete();
        } catch (Exception e) {

        }
    }

    private void guardar_Factor_transcripcion(factorTranscripcion FT) {

        ObjectContainer db = Db4o.openFile("mineria/FT.db");
        try {
            db.store(FT);
        } catch (Exception e) {
            System.out.println("Error al guardar FT.db...");
        } finally {

            db.close();
        }
    }

    public void guardar_objetosIteracion(objetosMineria objetosMin) {
        ObjectContainer db = Db4o.openFile("mineria/objetosMineria.db");
        try {
            db.store(objetosMin);
        } catch (Exception e) {
            System.out.println("Error al guardar en objetosMineria.db...");
        } finally {
            db.close();
        }
    }

    public void obtenerFT() {

        ObjectContainer db = Db4o.openFile("mineria/FT.db");
        factorTranscripcion FT = new factorTranscripcion();

        try {

            ObjectSet result = db.queryByExample(FT);
            while (result.hasNext()) {
                try {
                    factorTranscripcion ft = (factorTranscripcion) result.next();
                    ft.imprimir();
                    System.out.println("====================================================");
                } catch (Exception e) {

                }
            }
        } catch (Exception e) {
            System.out.println("error");
        } finally {
            db.close();
        }

    }

    public void buscarHomologos(ArrayList<String> lista, objetosMineria objetosMineria, configuracion config, boolean GO, boolean MESH) {
        System.out.println("\n\n**Leyendo archivo de Homologos...");
        for (int i = 0; i < lista.size(); i++) {
            System.out.println("busqueda.." + lista.get(i));
            objetos_Experto objExp = new objetos_Experto();
            objExp.setID(lista.get(i));
            objExp.setHGNC(new lecturas_HGNC().busquedaInfGen(lista.get(i),GO,MESH));
            new objetosMinados().agregar_objetos(objExp);

            for (int j = 0; j < objExp.getHGNC().size(); j++) {
                objetosMineria.agregar_objeto(objExp.getHGNC().get(j));
            }
            guardarObjetos_Homologos_Experto(objExp);

        }
        config.setHomologos(true);
        config.guardar(config);
    }

    public void buscarObjetosExperto(ArrayList<String> lista, objetosMineria objetosMineria, configuracion config,boolean GO, boolean MESH) {
        System.out.println("\n\n**Leyendo archivo de Objetos Experto...");
        for (int i = 0; i < lista.size(); i++) {
            System.out.println("busqueda.." + lista.get(i));
            objetos_Experto objExp = new objetos_Experto();
            objExp.setID(lista.get(i));
            objExp.setHGNC(new lecturas_HGNC().busquedaInfGen(lista.get(i),GO,MESH));
            new objetosMinados().agregar_objetos(objExp);

            for (int j = 0; j < objExp.getHGNC().size(); j++) {
                objetosMineria.agregar_objeto(objExp.getHGNC().get(j));
            }
            guardarObjetos_Homologos_Experto(objExp);
        }
        config.setObjetosExperto(true);
        config.guardar(config);
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

    private void guardarObjetos_Homologos_Experto(objetos_Experto objExp) {

        ObjectContainer db = Db4o.openFile("mineria/ObjH_E.db");
        try {
            db.store(objExp);
        } catch (Exception e) {
            System.out.println("Error al guardar en la base de datos ObjH_E.db");
        } finally {

            db.close();
        }

    }

    public void vaciar_bc_pl(boolean GO,boolean MESH) {

        ObjectContainer db = Db4o.openFile("mineria/FT.db");
        factorTranscripcion FT = new factorTranscripcion();
        try {

            ObjectSet result = db.queryByExample(FT);
            while (result.hasNext()) {
                try {
                    factorTranscripcion ft = (factorTranscripcion) result.next();
                    ft.vaciar_pl("objetosMinados.pl");
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {

        } finally {
            db.close();
        }

        ontologiaObjMin ontologias = new ontologiaObjMin();
        ontologias.vaciarOntologia_pl(GO, MESH);

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

    public void agregarObjetosMinado(String objeto) {
        if (objeto != null) {
            this.objetos_minados.add(objeto);
            
        }
    }

    public ArrayList<String> getNuevos_objetos() {
        return nuevos_objetos;
    }

    public void setNuevos_objetos(ArrayList<String> nuevos_objetos) {
        this.nuevos_objetos = nuevos_objetos;
    }

    public void agregar_objeto(HGNC HGNC) {

        if (!objetos_minados.contains(HGNC.getSimbolo())) {

            if (!nuevos_objetos.contains(HGNC.getSimbolo()) && HGNC.getSimbolo() != null) {
                nuevos_objetos.add(HGNC.getSimbolo());
            }

        }

    }
    
    public void agregar_objeto(String objeto){
        if(!objetos_minados.contains(objeto)){
            if(!nuevos_objetos.contains(objeto) && objeto != null){
                nuevos_objetos.add(objeto);
                //System.out.println("nuevo "+objeto);
            }
        }
    }

    public void agregar_objeto(ArrayList<complejoProteinico> objetos) {

        for (int i = 0; i < objetos.size(); i++) {

            for (int j = 0; j < objetos.get(i).getHGNC().size(); j++) {

                HGNC hgnc = objetos.get(i).getHGNC().get(j);
                if (!objetos_minados.contains(hgnc.getSimbolo()) && hgnc.getSimbolo() != null) {

                    if (!nuevos_objetos.contains(hgnc.getSimbolo())) {
                        nuevos_objetos.add(hgnc.getSimbolo());
                    }

                }

            }

        }

    }

}
