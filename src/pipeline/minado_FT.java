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
import jdk.nashorn.internal.parser.TokenType;

public class minado_FT {

    public void minado(String ruta, float confiabilidad, int Iteraciones, int numeroObjetos) {
        Runtime garbage = Runtime.getRuntime();
        objetosMineria objetosMineria = new objetosMineria();

        crearCarpeta("mineria");
        //Se crea un nuevo archivo de Objectos minados
        new objetosMinados().crear_archivo();

        leer_archivo_homologos(objetosMineria);
        leer_archivo_ObjetosExperto(objetosMineria);
        //primera Iteracion partiendo de TFBind
        primeraIteracion(ruta, confiabilidad, numeroObjetos, objetosMineria);
        //Segunda Iteracion en adelante partiendo de nuevos objetos encontrados en PDB
        Iteraciones(false, new ArrayList<String>(), numeroObjetos, Iteraciones, objetosMineria);
    }

    private void primeraIteracion(String ruta, float confiabilidad, int numeroObjetos, objetosMineria objetosMineria) {
        //Primera Iteracion
        System.out.println("\n====Iteracion 0====\n");
        objetosMineria.setIteracion(0);
        ArrayList<lecturas_TFBIND> lecturasTFB = lecturasTFBID(ruta, confiabilidad);
        System.out.println(lecturasTFB.size() + " Factores de transcripcion encontrados");
        for (int i = 0; i < lecturasTFB.size(); i++) {
            factorTranscripcion FT = new factorTranscripcion(lecturasTFB.get(i), numeroObjetos, objetosMineria);
            objetosMineria.getObjetos_minados().add(FT.getID());
            objetosMineria.agregar_objeto(FT.getComplejoProteinico());
            new objetosMinados().agregar_objetos(FT);
            guardar_Factor_transcripcion(FT);
            System.out.println("Listo...");
            FT = null;
        }
        guardar_objetosIteracion(objetosMineria);
        objetosMineria.imprimir();
    }

    private void Iteraciones(boolean ReinicioMin, ArrayList<String> Lista, int numeroObjetos, int Iteraciones, objetosMineria objetosMineria) {
        //Iteracion 2 en adelante
        for (int i = 1; i < Iteraciones; i++) {
            System.out.println("\n====Iteracion " + (i) + "====\n");

            if (!ReinicioMin) {
                Lista = objetosMineria.getNuevos_objetos();
                objetosMineria.setNuevos_objetos(new ArrayList<String>());
            }
            ReinicioMin = false;

            for (int j = 0; j < Lista.size(); j++) {
                factorTranscripcion FT = new factorTranscripcion(Lista.get(j), i, numeroObjetos);
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
        conf.verconfiguracion();

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

    private void crearCarpeta(String nombre) {
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

    private void borrar_archivo(String nombre) {
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

    public void leer_archivo_homologos(objetosMineria objetosMineria) {

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
                objetos_Experto objExp = new objetos_Experto();
                objExp.setID(lectura);
                objExp.setHGNC(new lecturas_HGNC().busquedaInfGen(lectura));

                new objetosMinados().agregar_objetos(objExp);
                for (int i = 0; i < objExp.getHGNC().size(); i++) {
                    objetosMineria.agregar_objeto(objExp.getHGNC().get(i));
                }
                guardarObjetos_Homologos_Experto(objExp);

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

    public void leer_archivo_ObjetosExperto(objetosMineria objetosMineria) {

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

                    /* for (int j = 0; j < separa.length; j++) {
                        if (HGNC.getSinonimosExperto().contains(separa[j])) {
                            HGNC.getSinonimosExperto().add(separa[j]);
                        }

                    }*/
                    objetos_Experto objExp = new objetos_Experto();
                    objExp.setID(lectura);
                    objExp.setHGNC(new lecturas_HGNC().busquedaInfGen(lectura));

                    new objetosMinados().agregar_objetos(objExp);

                    for (int j = 0; j < objExp.getHGNC().size(); j++) {
                        objetosMineria.agregar_objeto(objExp.getHGNC().get(j));
                    }
                    guardarObjetos_Homologos_Experto(objExp);
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

    public void vaciar_bc_pl() {

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
        ontologias.vaciarOntologia_pl(true, true);

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

    public void agregar_objeto(ArrayList<complejoProteinico> objetos) {

        for (int i = 0; i < objetos.size(); i++) {

            for (int j = 0; j < objetos.get(i).getHGNC().size(); j++) {

                HGNC hgnc = objetos.get(i).getHGNC().get(j);
                if (!objetos_minados.contains(hgnc.getSimbolo()) && hgnc.getSimbolo()!= null) {

                    if (!nuevos_objetos.contains(hgnc.getSimbolo())) {
                        nuevos_objetos.add(hgnc.getSimbolo());
                    }

                }

            }

        }

    }

}
