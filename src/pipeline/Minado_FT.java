 /*
    Minado_FT.java


    Copyright (C) 2016.
    Yackson Ramirez (yackson.ramirez), Jose Lopez (jlopez@unet.edu.ve).

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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;

/**
 *
 * @author yacson
 */
public class Minado_FT {

    //private ArrayList<Factor_Transcripcion> lista_FT;
    private objetos_mineria objetos_mineria;
    private ArrayList<ArrayList> listaFT;
    private ArrayList<Description> lista_homologos;

    public Minado_FT() throws IOException {

        this.objetos_mineria = new objetos_mineria();
        listaFT = new ArrayList<>();
        lista_homologos = new ArrayList<>();

    }

    public void minado(String bloquesConsenso, int cant_compl_p, boolean criterio, float confiabilidad, int numiteraciones) throws IOException {
        crear_archivo("objetosMinados.txt");
        Runtime garbage = Runtime.getRuntime();
        System.out.println("buscando informacion homologos Blast");
        leer_archivo_homologos();
        System.out.println("Listo..");

        System.out.println("buscando informacion Objetos del experto");
        leer_archivo_ObjetosExperto();
        System.out.println("Listo..");

        System.out.println();
        System.out.println("===========Iteracion 1==========");
        ArrayList<Factor_Transcripcion> lista_FT = new ArrayList<>();
        busqueda_factoresTrancripcion(lista_FT, bloquesConsenso, confiabilidad);
        int n = busqueda_ID_complejos_proteinicos(lista_FT, cant_compl_p);// Recibe la cantidad máxima de CP que se desean para cada FT
        busqueda_inf_complejos_proteinicos(lista_FT, n, criterio);
        busqueda_HUGO(lista_FT, criterio);
        busqueda_ligandos(lista_FT, n);

        listaFT.add(lista_FT);
        for (int i = 0; i < lista_homologos.size(); i++) {
            objetos_mineria.agregar_objeto(lista_homologos.get(i).getEtiqueta());
        }

        //------------------------------------------------  
        imprimir_datos(lista_FT);
        lista_FT = null;
        //System.out.println("Memoria libre antes de limpieza:"+ garbage.freeMemory());
        garbage.gc();
        //System.out.println("Memoria libre despues de limpieza:"+ garbage.freeMemory());

        //Proceso de iteracion ---------------------------------------
        for (int i = 1; i < numiteraciones; i++) {
            System.out.println("===========Iteracion" + (i + 1) + "==========");
            lista_FT = new ArrayList<>();
            listaFT.add(Min_Iteracion(lista_FT, objetos_mineria, cant_compl_p, criterio));
            lista_FT = null;
            garbage.gc();

        }

        //-------------------------------------------------------------
    }

    public ArrayList<Factor_Transcripcion> Min_Iteracion(ArrayList<Factor_Transcripcion> lista_FT, objetos_mineria objetos, int cant_compl_p, boolean criterio) {

        objetos_mineria.setObjetos_minados(objetos.getObjetos_minados());
        lecturas_genenames lg = new lecturas_genenames();
        //----------------------------------------------------------------
        for (int i = 0; i < objetos.getNuevos_objetos().size(); i++) {
            Factor_Transcripcion FT = new Factor_Transcripcion();
            FT.setID(objetos.getNuevos_objetos().get(i));
            Description des = new Description();
            lg.genenames(des, FT.getID(), false);
            FT.setNombre(des.getNombre());
            FT.setSimbolo(des.getSimbolo());

            if (FT.getNombre() == null) {
                FT.setNombre(FT.getID());
                FT.setSimbolo(FT.getID());
            }

            lista_FT.add(FT);
            objetos_mineria.getObjetos_minados().add(FT.getSimbolo());

        }
        //----------------------------------------------------------------------------
        int n = busqueda_ID_complejos_proteinicos(lista_FT, cant_compl_p);// Recibe la cantidad máxima de CP que se desean para cada FT
        busqueda_inf_complejos_proteinicos(lista_FT, n, criterio);
        busqueda_HUGO(lista_FT, criterio);
        busqueda_ligandos(lista_FT, n);

        //------------------------------------------------
        imprimir_datos(lista_FT);
        return lista_FT;
    }

    public void busqueda_factoresTrancripcion(ArrayList<Factor_Transcripcion> lista_FT, String blocques_consenso, float confiabilidad) throws IOException {
        lecturas_genenames lg = new lecturas_genenames();
        ArrayList<Lecturatfbind> listalb = new Lecturatfbind(blocques_consenso, confiabilidad).getLecturas();

        for (int i = 0; i < listalb.size(); i++) {

            Factor_Transcripcion FT = new Factor_Transcripcion();
            FT.setID(listalb.get(i).getFactor());
            Description des = new Description();
            if (lg.genenames(des, FT.getID(), false)) {
                lg.generar_objetosMinados_txt(des);
            } else {
                lg.escribe_txt("objetosMinados.txt", FT.getID());
            }

            FT.setNombre(des.getNombre());
            FT.setSimbolo(des.getSimbolo());
            FT.setLectura(listalb.get(i));
            if (FT.getNombre() == null) {
                FT.setNombre(FT.getID());
                FT.setSimbolo(FT.getID());
            }

            lista_FT.add(FT);
            objetos_mineria.getObjetos_minados().add(FT.getSimbolo());

        }
        System.out.println(lista_FT.size() + " Factores de transcripcion encontrados");
        System.out.println("");

    }

    public int busqueda_ID_complejos_proteinicos(ArrayList<Factor_Transcripcion> lista_FT, int limite) {
        System.out.println("buscando ID de complejos proteinicos");
        lecturas_rcsb rcsb = new lecturas_rcsb();
        int cont = 0;
        for (int i = 0; i < lista_FT.size(); i++) {

            rcsb.buscar_ID(lista_FT.get(i).getCom_prot(), lista_FT.get(i).getID(), limite);
            cont += lista_FT.get(i).getCom_prot().size();
            monitor_carga(i);
        }
        System.out.println(" Listo!");
        System.out.println(cont + " complejos proteinicos encontrados");
        System.out.println("");

        return cont;
    }

    public void busqueda_inf_complejos_proteinicos(ArrayList<Factor_Transcripcion> lista_FT, int n, boolean criterio) {
        System.out.println("Busquedas PDB...");
        lecturas_rcsb rcsb = new lecturas_rcsb();
        rcsb.setfin(n);

        for (int i = 0; i < lista_FT.size(); i++) {

            rcsb.Busqueda_PDB(lista_FT.get(i).getCom_prot(), criterio, objetos_mineria);

        }
        rcsb = null;
        System.out.println(" Listo!");
        System.out.println("");
    }

    public void busqueda_HUGO(ArrayList<Factor_Transcripcion> lista_FT, boolean criterio) {
        System.out.println("busquedas HUGO...");
        for (int i = 0; i < lista_FT.size(); i++) {

            for (int j = 0; j < lista_FT.get(i).getCom_prot().size(); j++) {
                ArrayList<Description> listaObj = lista_FT.get(i).getCom_prot().get(j).getDescripcion();
                for (int k = 0; k < listaObj.size(); k++) {

                    lecturas_genenames lg = new lecturas_genenames();
                    if (lg.genenames(listaObj.get(k), listaObj.get(k).getEtiqueta(), criterio)) {
                        lg.generar_objetosMinados_txt(listaObj.get(k));
                    } else {
                        lg.escribe_txt("objetosMinados.txt", listaObj.get(k).getEtiqueta());
                    }

                    objetos_mineria.agregar_objeto(listaObj.get(k).getSimbolo());

                }

            }

        }
        System.out.println(" Listo!");
        System.out.println("");

    }

    public void busqueda_ligandos(ArrayList<Factor_Transcripcion> lista_FT, int n) {
        System.out.println("Busquedas Ligandos...");
        lecturas_rcsb rcsb = new lecturas_rcsb();
        rcsb.setfin(n);
        for (int i = 0; i < lista_FT.size(); i++) {
            rcsb.Buscar_ligandos(lista_FT.get(i).getCom_prot());

            monitor_carga(i);
        }

        System.out.println(" Listo!");
        System.out.println("");

    }

    public objetos_mineria getObjetos_mineria() {
        return objetos_mineria;
    }

    public void setObjetos_mineria(objetos_mineria objetos_mineria) {
        this.objetos_mineria = objetos_mineria;
    }

    public void imprimir_datos(ArrayList<Factor_Transcripcion> lista_FT) {

        for (int i = 0; i < lista_FT.size(); i++) {
            lista_FT.get(i).imprimir();
        }

        objetos_mineria.imprimir();

    }

    private void monitor_carga(int mc) {
        System.out.print("#");
        if (mc % 100 == 0 && mc != 0) {
            System.out.println("");
        }
        mc++;
    }

    public void crear_archivo(String nombre) throws IOException {

        FileWriter fichero = null;
        PrintWriter pw = null;
        fichero = new FileWriter(nombre);
        pw = new PrintWriter(fichero);

    }

    public ArrayList<ArrayList> getListaFT() {
        return listaFT;
    }

    public ArrayList<Description> getLista_homologos() {
        return lista_homologos;
    }

    public void leer_archivo_homologos() {

        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            archivo = new File("homologos");
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String lectura;
            while ((lectura = br.readLine()) != null) {

                Description des = new Description();
                des.setEtiqueta(lectura);
                lecturas_genenames lg = new lecturas_genenames();

                if (lg.genenames(des, lectura, false)) {
                    lg.generar_objetosMinados_txt(des);
                } else {
                    lg.escribe_txt("objetosMinados.txt", lectura);
                }

                lista_homologos.add(des);

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

    public void leer_archivo_ObjetosExperto() {

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
                lecturas_genenames lg = new lecturas_genenames();
                boolean inf = false;
                for (int i = 0; i < separa.length; i++) {
                    Description des = new Description();
                    des.setEtiqueta(separa[i]);

                    if (lg.genenames(des, separa[i], false)) {

                        for (int j = 0; j < separa.length; j++) {
                            if (i != j && !des.getSinonimos().contains(separa[j])) {
                                des.getSinonimos().add(separa[j]);
                            }
                        }
                        lg.generar_objetosMinados_txt(des);
                        lista_homologos.add(des);
                        inf = true;
                        System.out.println("busqueda.." + lectura);
                        break;
                    }
                }
                if (!inf) {
                    lg.escribe_txt("objetosMinados.txt", separa[0]);
                    Description des = new Description();
                    des.setEtiqueta(separa[0]);
                    des.setNombre(separa[0]);
                    des.setSimbolo(separa[0]);
                    lista_homologos.add(des);
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

    //buscar Ensembl Gen ID
    //recive un String ID de un ogjeto  retorna el Ensembl Gen ID correspondiente
    public String busquedaEnsemblGenID(String ID) {
        String EnsemblGEnID = null;
        Description des = new Description();
        lecturas_genenames gn = new lecturas_genenames();
        gn.genenames(des, ID, false);
        EnsemblGEnID = des.getEnsembl_gene_id();
        return EnsemblGEnID;
    }

}
//-------------------------------------------------------------------------------------------------//

class objetos_mineria {

    private ArrayList<String> objetos_minados;
    private ArrayList<String> nuevos_objetos;

    public objetos_mineria() {

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

    public void agregar_objeto(String objeto) {

        if (!objetos_minados.contains(objeto) && objeto != null) {

            if (!nuevos_objetos.contains(objeto)) //System.out.println("obj: "+objeto);
            {
                nuevos_objetos.add(objeto);
            }

        }

    }

}
