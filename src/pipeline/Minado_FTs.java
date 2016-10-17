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

public class Minado_FTs {

    public void minado(String ruta, float confiabilidad, int Iteraciones, int numeroObjetos, boolean criterio) {

        objetos_mineria2 objetosMineria = new objetos_mineria2();
        //Se crea un nuevo archivo de Objectos minados
        borrar_archivo("objetosMinados.txt");
        crear_archivo("objetosMinados.txt");
        //Nuevo archivo de objetos Homologos y Objetos de Experto
        borrar_archivo("ObjH_E.db");
        borrar_archivo("FT.db");

        leer_archivo_homologos(objetosMineria);
        leer_archivo_ObjetosExperto(objetosMineria);

        //Primera Iteracion
        System.out.println("====Iteracion 1====");
        ArrayList<Lecturas_TFBIND> lecturasTFB = lecturasTFBID(ruta, confiabilidad);
        for (int i = 0; i < lecturasTFB.size(); i++) {
            Factor_Transcripcion2 FT = new Factor_Transcripcion2(lecturasTFB.get(i), criterio, numeroObjetos, objetosMineria);
            objetosMineria.getObjetos_minados().add(FT.getID());
            objetosMineria.agregar_objeto(FT.getComplejoProteinico());
            generar_objetosMinados_txt(FT.getLecturas_HGNC());

            guardar_Factor_transcripcion(FT);
        }
        objetosMineria.imprimir();

        //Iteracion 2 en adelante
        for (int i = 1; i < Iteraciones; i++) {
            System.out.println("====Iteracion " + (i + 1) + "====");
            ArrayList<String> Lista = objetosMineria.getNuevos_objetos();
            objetosMineria.setNuevos_objetos(new ArrayList<>());

            for (int j = 0; j < Lista.size(); j++) {
                Factor_Transcripcion2 FT = new Factor_Transcripcion2(Lista.get(j), true, i, numeroObjetos);
                objetosMineria.getObjetos_minados().add(FT.getID());
                objetosMineria.agregar_objeto(FT.getComplejoProteinico());
                generar_objetosMinados_txt(FT.getLecturas_HGNC());

                guardar_Factor_transcripcion(FT);
            }
            objetosMineria.imprimir();
        }

    }

//  se obtinen lecturas de TFBIND recibe la ruta del archivo bloquesconsenso 
//  y el porsentaje de confiabnilidad, debuelve un listado con los factores de transcripcion 
//  encontrados y algunas caracteristicas que ofrece TFBIND
    private ArrayList<Lecturas_TFBIND> lecturasTFBID(String ruta, float confiabilidad) {

        Lecturas_TFBIND lecturasTFBIND = new Lecturas_TFBIND();
        return lecturasTFBIND.leer_de_archivo(ruta, confiabilidad);

    }

    public void crear_archivo(String nombre) {

        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter(nombre);
        } catch (IOException ex) {
            Logger.getLogger(Minado_FTs.class.getName()).log(Level.SEVERE, null, ex);
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

    public void generar_objetosMinados_txt(lecturas_HGNC HGNC) {

        ArrayList<String> Nombres = new ArrayList<>();
        for (int i = 0; i < HGNC.getSinonimosExperto().size(); i++) {
            if (!Nombres.contains(HGNC.getSinonimosExperto().get(i))) {
                Nombres.add(HGNC.getSinonimosExperto().get(i));
            }
        }

        if (!Nombres.contains(HGNC.getID())) {
            Nombres.add(HGNC.getID());
        }
        for (int i = 0; i < HGNC.getHGNC().size(); i++) {
            HGNC hgnc = new HGNC();

            if (!Nombres.contains(hgnc.getNombre())) {
                Nombres.add(hgnc.getNombre());
            }
            if (!Nombres.contains(hgnc.getSimbolo())) {
                Nombres.add(hgnc.getSimbolo());
            }

            for (int j = 0; j < hgnc.getSinonimos().size(); j++) {
                if (!Nombres.contains(hgnc.getSinonimos().get(j))) {
                    Nombres.add(hgnc.getSinonimos().get(j));
                }
            }
        }

        String cadena = "";
        for (int i = 0; i < Nombres.size(); i++) {
            cadena = cadena + Nombres.get(i) + ";";
        }

        escribe_txt("objetosMinados.txt", cadena);

    }

    public void escribe_txt(String archivo, String texto) {

        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter(archivo, true);
            pw = new PrintWriter(fichero);

            pw.println(texto);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Nuevamente aprovechamos el finally para 
                // asegurarnos que se cierra el fichero.
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

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

        } finally {
            db.close();
        }

    }

    public void leer_archivo_homologos(objetos_mineria2 objetosMineria) {

        System.out.println("Leyendo archivo de Homologos...");

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
                HGNC.busqueda_genenames(lectura, false);
                generar_objetosMinados_txt(HGNC);
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

    public void leer_archivo_ObjetosExperto(objetos_mineria2 objetosMineria) {

        System.out.println("Leyendo archivo de Objetos Experto...");

        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            archivo = new File("objetos_Experto2.txt");
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String lectura;

            while ((lectura = br.readLine()) != null) {

                String[] separa = lectura.split(";");

                for (int i = 0; i < separa.length; i++) {
                    System.out.println("busqueda.." + lectura);
                    lecturas_HGNC HGNC = new lecturas_HGNC();
                    HGNC.busqueda_genenames(lectura, false);

                    for (int j = 0; j < separa.length; j++) {
                        if (HGNC.getSinonimosExperto().contains(separa[j])) {
                            HGNC.getSinonimosExperto().add(separa[j]);
                        }

                    }
                    generar_objetosMinados_txt(HGNC);
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
        } finally {

            db.close();
        }

    }
}

//-------------------------------------------------------------------------------------------------//
class objetos_mineria2 {

    private ArrayList<String> objetos_minados;
    private ArrayList<String> nuevos_objetos;

    public objetos_mineria2() {

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

    public ArrayList<String> getObjetos_minados() {
        return objetos_minados;
    }

    public void setObjetos_minados(ArrayList<String> objetos_minados) {
        this.objetos_minados = objetos_minados;
    }

    public ArrayList<String> getNuevos_objetos() {
        return nuevos_objetos;
    }

    public void setNuevos_objetos(ArrayList<String> nuevos_objetos) {
        this.nuevos_objetos = nuevos_objetos;
    }

    public void agregar_objeto(lecturas_HGNC HGNC) {
        for (int i = 0; i < HGNC.getHGNC().size(); i++) {

            if (!objetos_minados.contains(HGNC.getHGNC().get(i).getSimbolo())) {

                if (!nuevos_objetos.contains(HGNC.getHGNC().get(i).getSimbolo())) {
                    nuevos_objetos.add(HGNC.getHGNC().get(i).getSimbolo());
                }

            }
        }
    }

    public void agregar_objeto(ArrayList<complejoProteinico2> objetos) {

        for (int i = 0; i < objetos.size(); i++) {

            for (int j = 0; j < objetos.get(i).getHGNC().getHGNC().size(); j++) {

                HGNC hgnc = objetos.get(i).getHGNC().getHGNC().get(j);
                if (!objetos_minados.contains(hgnc.getSimbolo()) && objetos != null) {

                    if (!nuevos_objetos.contains(hgnc.getSimbolo())) {
                        nuevos_objetos.add(hgnc.getSimbolo());
                    }

                }

            }

        }

    }

}
