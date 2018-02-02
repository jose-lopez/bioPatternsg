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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yacson-ramirez
 */
public class configuracion {

    private int numIteraciones;
    private int cantComplejos;
    private float confiabilidad_tfbind;
    private String RegionPromotora;
    private boolean crearOntologiaGO;
    private boolean crearOntologiaMESH;
    private int cantidadPMID;
    private String rutaPMID_experto;
//-------------------------------------//
    private boolean homologos; //Lectura de homologos
    private boolean objetosExperto; // Lectura objetos del experto
    private ArrayList<lecturas_TFBIND> tfbind; //lista de facores de transcripcion
    private boolean lecturas_tfbind; //primera iteracion
    private boolean procesoIteraciones; //proceso de Iteraciones 2 en adelante
    private boolean combinaciones; // generacion de comobinaciones de palabras clave
    private boolean abstracts; // buesqueda de abstracts en PUBMED
    private boolean vaciado_pl; // vaciado de objetos minados y ontologias a formato prolog
    private boolean generarResumenes; //generacion de resumenes a partir de los abstracts
    private int resumenes; //archivos resumidos
    private boolean GenerarBC;
    private boolean InferirPatrones;

    public configuracion() {
        resumenes = 1;
        RegionPromotora = null;
        tfbind = new ArrayList<lecturas_TFBIND>();
    }

    public void guardarConfiguracion(String regionProm, int numIter, int cantCompl, float conf, boolean GO, boolean MESH, int cantPMID, String PMidExp) {
        this.RegionPromotora = regionProm;
        this.cantComplejos = cantCompl;
        this.numIteraciones = numIter;
        this.confiabilidad_tfbind = conf;
        this.crearOntologiaGO = GO;
        this.crearOntologiaMESH = MESH;
        this.cantidadPMID = cantPMID;
        this.rutaPMID_experto = PMidExp;
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

    public void guardar_lecturasTFBIND(ArrayList<lecturas_TFBIND> lecturas) {
        ObjectContainer db = Db4o.openFile("mineria/config.db");
        configuracion conf = new configuracion();
        try {
            ObjectSet result = db.queryByExample(conf);

            while (result.hasNext()) {
                configuracion config = (configuracion) result.next();
                config.tfbind = lecturas;
                db.store(config);
                //System.out.println("guardado");
            }
        } catch (Exception e) {
            System.out.println("No fue posible guardar la configuracion");
        } finally {
            db.close();
        }
    }

    public void guardar() {
        ObjectContainer db = Db4o.openFile("mineria/config.db");
        configuracion conf = new configuracion();
        conf.RegionPromotora = this.RegionPromotora;
        try {
            ObjectSet result = db.queryByExample(conf);
            while (result.hasNext()) {
                configuracion config = (configuracion) result.next();
                config = this;
                db.store(config);
                //verConfiguracion();
                break;
            }
        } catch (Exception e) {
            System.out.println("No fue posible guardar la configuracion");
        } finally {
            db.close();
        }
    }

    public configuracion recuperarConfiguracion() {
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
                this.cantidadPMID = config.cantidadPMID;
                this.rutaPMID_experto = config.rutaPMID_experto;
                //------------------------------------------------------//
                this.tfbind = config.tfbind;
                this.homologos = config.homologos;
                this.objetosExperto = config.objetosExperto;
                this.lecturas_tfbind = config.lecturas_tfbind;
                this.procesoIteraciones = config.procesoIteraciones;
                this.combinaciones = config.combinaciones;
                this.abstracts = config.abstracts;
                this.vaciado_pl = config.vaciado_pl;
                this.crearOntologiaGO = config.crearOntologiaGO;
                this.crearOntologiaMESH = config.crearOntologiaMESH;
                this.generarResumenes = config.generarResumenes;
                this.resumenes = config.resumenes;
                this.GenerarBC = config.GenerarBC;
                this.InferirPatrones = config.InferirPatrones;
                
                
            }
        } catch (Exception e) {
            System.out.println("No fue posible guardar la configuracion");
        } finally {
            db.close();
        }

        return this;
    }

    public void verConfiguracion() {
        //System.out.println("\n**Configuracion de minado**");
        System.out.println("\n*Region promotora: " + this.RegionPromotora);
        System.out.println("*Cantidad de complejos: " + this.cantComplejos);
        System.out.println("*Numero de niveles: " + this.numIteraciones);
        System.out.println("*Confiabilidad TFBind: " + (int) (this.confiabilidad_tfbind * 100));
        System.out.print("*Creacion de ontologias GeneOntology: ");

        if (crearOntologiaGO) {
            System.out.println("Si");
        } else {
            System.out.println("No");
        }
        System.out.print("*Creacion de ontologias MESH: ");
        if (crearOntologiaMESH) {
            System.out.println("Si");
        } else {
            System.out.println("No");
        }

        estadoactual();
    }

    private void estadoactual() {
        System.out.print("\nEstado actual: ");
        if (!homologos) {
            System.out.println("Busqueda de homologos");
        } else if (!objetosExperto) {
            System.out.println("Busqueda objetos del Experto");
        } else if (!lecturas_tfbind) {
            System.out.println("Lecturas desde Tfbind");
        } else if (!procesoIteraciones) {
            objetosMineria objMin = new objetosMineria();
            objMin = recuperarObjetosMin();
            System.out.println("Busqueda de objetos PDB nivel " + (objMin.getIteracion() + 1));
        } else if (!combinaciones) {
            System.out.println("Generando combinaciones de palabras clave");
        } else if (!abstracts) {
            System.out.println("Busquedas en PubMed ");
        } else if (!vaciado_pl) {
            System.out.println("Vaciado de Objetos minados a formato prolog (.pl)");
        } else if (!generarResumenes) {
            System.out.println("Generando resumenes actual: " + resumenes);
        } else if (!GenerarBC) {
            System.out.println("Generar base de conocimiento");
        } else if (!InferirPatrones) {
            System.out.println("Inferir patrones");
        }
    }

    public void reanudar_proceso() {
        System.out.print("Preparando");
        recuperarConfiguracion();
        //verConfiguracion();
        objetosMineria objMin = new objetosMineria();
        objMin = recuperarObjetosMin();
        //System.out.println(objMin.getNuevos_objetos().size());
        System.out.println();
        if (!homologos) {
            //System.out.println("\nReanudar desde busqueda de homologos ...");
            reanudar(1, objMin);
        } else if (!objetosExperto) {
            //System.out.println("\nReanudar desde busqueda de objetos Experto ...");
            reanudar(2, objMin);
        } else if (!lecturas_tfbind) {
            //System.out.println("\nReanudando Iteracion: " + objMin.getIteracion());
            reanudar(3, objMin);
        } else if (!procesoIteraciones) {
            reanudar(4, objMin);
        } else if (!combinaciones) {
            reanudar(5, objMin);
        } else if (!abstracts) {
            reanudar(6, objMin);
        } else if (!vaciado_pl) {
            reanudar(7, objMin);
        } else if (!generarResumenes) {
            reanudar(8, objMin);
        } else if (!GenerarBC) {
            reanudar(9, objMin);
        } else if (!InferirPatrones) {
            reanudar(10, objMin);
        } else {
            //proceso terminado
            ver_detalles();
        }
    }

    private void reanudar(int punto, objetosMineria objetosMineria) {
        minado_FT mfts = new minado_FT();
        busquedaPubMed_IDs BPM = new busquedaPubMed_IDs();
        lecturas_PM lpm = new lecturas_PM();
        ArrayList<String> listaPMid;
        switch (punto) {
            case 1:
                mfts.buscarHomologos(revisarObjH_E("homologos", objetosMineria), objetosMineria, this, crearOntologiaGO, crearOntologiaMESH);
                mfts.buscarObjetosExperto(listaObjetos_homologosExperto("objetos_Experto.txt"), objetosMineria, this, crearOntologiaGO, crearOntologiaMESH);
                mfts.primeraIteracion(RegionPromotora, confiabilidad_tfbind, cantComplejos, objetosMineria, this, new ActivatableArrayList<lecturas_TFBIND>(), crearOntologiaGO, crearOntologiaMESH);
                mfts.Iteraciones(false, new ArrayList<String>(), cantComplejos, numIteraciones, objetosMineria, this, 1, crearOntologiaGO, crearOntologiaMESH);
                listaPMid = BPM.busqueda_IDs(false, cantidadPMID, false, this);
                lpm.BusquedaPM_Abstracts(listaPMid, "abstracts", 500, this);
                mfts.vaciar_bc_pl(crearOntologiaGO, crearOntologiaMESH);
                new Resumidor().resumidor(this);
                try {
                    String base_conocimiento = new GeneradorBC().generadorBC("baseC.pl", this);
                } catch (Exception e) {
                }
                new Razonador().inferir_patrones("baseC.pl", this);
                break;
            case 2:
                revisarObjH_E("homologos", objetosMineria);
                mfts.buscarObjetosExperto(revisarObjH_E("objetos_Experto.txt", objetosMineria), objetosMineria, this, crearOntologiaGO, crearOntologiaMESH);
                mfts.primeraIteracion(RegionPromotora, confiabilidad_tfbind, cantComplejos, objetosMineria, this, new ArrayList<lecturas_TFBIND>(), crearOntologiaGO, crearOntologiaMESH);
                mfts.Iteraciones(false, new ArrayList<String>(), cantComplejos, numIteraciones, objetosMineria, this, 1, crearOntologiaGO, crearOntologiaMESH);
                listaPMid = BPM.busqueda_IDs(false, cantidadPMID, false, this);
                lpm.BusquedaPM_Abstracts(listaPMid, "abstracts", 500, this);
                mfts.vaciar_bc_pl(crearOntologiaGO, crearOntologiaMESH);
                new Resumidor().resumidor(this);
                try {
                    String base_conocimiento = new GeneradorBC().generadorBC("baseC.pl", this);
                } catch (Exception e) {
                }
                new Razonador().inferir_patrones("baseC.pl", this);
                break;
            case 3:
                revisarObjH_E("homologos", objetosMineria);
                revisarObjH_E("objetos_Experto.txt", objetosMineria);
                ArrayList<lecturas_TFBIND> lecturas = actualizarListaTFBind(objetosMineria);
                mfts.primeraIteracion(RegionPromotora, confiabilidad_tfbind, cantComplejos, objetosMineria, this, lecturas, crearOntologiaGO, crearOntologiaMESH);
                mfts.Iteraciones(false, new ArrayList<String>(), cantComplejos, numIteraciones, objetosMineria, this, 1, crearOntologiaGO, crearOntologiaMESH);
                listaPMid = BPM.busqueda_IDs(false, cantidadPMID, false, this);
                lpm.BusquedaPM_Abstracts(listaPMid, "abstracts", 500, this);
                mfts.vaciar_bc_pl(crearOntologiaGO, crearOntologiaMESH);
                new Resumidor().resumidor(this);
                try {
                    String base_conocimiento = new GeneradorBC().generadorBC("baseC.pl", this);
                } catch (Exception e) {
                }
                new Razonador().inferir_patrones("baseC.pl", this);
                break;
            case 4:
                ArrayList<String> ListaObj = reanudarIteracion(objetosMineria);
                mfts.Iteraciones(true, ListaObj, cantComplejos, numIteraciones, objetosMineria, this, objetosMineria.getIteracion() + 1, crearOntologiaGO, crearOntologiaMESH);
                listaPMid = BPM.busqueda_IDs(false, cantidadPMID, false, this);
                lpm.BusquedaPM_Abstracts(listaPMid, "abstracts", 500, this);
                mfts.vaciar_bc_pl(crearOntologiaGO, crearOntologiaMESH);
                new Resumidor().resumidor(this);
                try {
                    String base_conocimiento = new GeneradorBC().generadorBC("baseC.pl", this);
                } catch (Exception e) {
                }
                new Razonador().inferir_patrones("baseC.pl", this);
                break;

            case 5:
                listaPMid = BPM.busqueda_IDs(false, cantidadPMID, false, this);
                lpm.BusquedaPM_Abstracts(listaPMid, "abstracts", 500, this);
                mfts.vaciar_bc_pl(crearOntologiaGO, crearOntologiaMESH);
                new Resumidor().resumidor(this);
                try {
                    String base_conocimiento = new GeneradorBC().generadorBC("baseC.pl", this);
                } catch (Exception e) {
                }
                new Razonador().inferir_patrones("baseC.pl", this);
                break;
            case 6:
                listaPMid = BPM.consulta_PudMed(cantidadPMID, this);
                lpm.BusquedaPM_Abstracts(listaPMid, "abstracts", 500, this);
                mfts.vaciar_bc_pl(crearOntologiaGO, crearOntologiaMESH);
                new Resumidor().resumidor(this);
                try {
                    String base_conocimiento = new GeneradorBC().generadorBC("baseC.pl", this);
                } catch (Exception e) {
                }
                new Razonador().inferir_patrones("baseC.pl", this);
                break;
            case 7:
                mfts.vaciar_bc_pl(crearOntologiaGO, crearOntologiaMESH);
                new Resumidor().resumidor(this);
                try {
                    String base_conocimiento = new GeneradorBC().generadorBC("baseC.pl", this);
                } catch (Exception e) {
                }
                new Razonador().inferir_patrones("baseC.pl", this);
                break;
            case 8:
                new Resumidor().resumidor(this);
                try {
                    String base_conocimiento = new GeneradorBC().generadorBC("baseC.pl", this);
                } catch (Exception e) {
                }
                new Razonador().inferir_patrones("baseC.pl", this);
                break;
            case 9:
                try {
                    String base_conocimiento = new GeneradorBC().generadorBC("baseC.pl", this);
                } catch (Exception e) {
                }
                new Razonador().inferir_patrones("baseC.pl", this);
                break;
            case 10:
                new Razonador().inferir_patrones("baseC.pl", this);
                break;

        }

    }

    private ArrayList<lecturas_TFBIND> actualizarListaTFBind(objetosMineria objetosMineria) {
        ArrayList<lecturas_TFBIND> lista = new ArrayList<>();
        factorTranscripcion ft = new factorTranscripcion();
        for (int i = 0; i < tfbind.size(); i++) {
            if (!buscarObjeto(tfbind.get(i).getFactor(), ft)) {
                buscarObjetos(objetosMineria, ft);
                lista.add(tfbind.get(i));
            }
            System.out.print(".");
        }

        //System.out.println(lista.size());
        return lista;
    }

    private void buscarObjetos(objetosMineria objetosMineria, factorTranscripcion ft) {
        objetosMineria.getObjetos_minados().add(ft.getID());

        for (int i = 0; i < ft.getComplejoProteinico().size(); i++) {
            for (int j = 0; j < ft.getComplejoProteinico().get(i).getHGNC().size(); j++) {
                objetosMineria.agregarObjetosMinado(ft.getComplejoProteinico().get(i).getHGNC().get(j).getSimbolo());
            }
        }

    }

    private void buscarobj(objetosMineria objMin) {
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

        objMin.setNuevos_objetos(NuevosObj);
    }

    private ArrayList<String> reanudarIteracion(objetosMineria objMin) {
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
            System.out.print(".");
        }

        objMin.setNuevos_objetos(new ArrayList<String>());
        for (int i = 0; i < NuevosObj.size(); i++) {
            objMin.agregar_objeto(NuevosObj.get(i));
        }

        return Lista;
    }

    private boolean buscarObjeto(String objeto, factorTranscripcion FT) {
        ObjectContainer db = Db4o.openFile("mineria/FT.db");
        factorTranscripcion ft = new factorTranscripcion();
        ft.setID(objeto);
        boolean encontrado = false;
        try {
            ObjectSet result = db.queryByExample(ft);
            while (result.hasNext()) {
                ft = (factorTranscripcion) result.next();
                //ft.imprimir();
                FT.setID(ft.getID());
                FT.setComplejoProteinico(ft.getComplejoProteinico());
                encontrado = true;
            }
        } catch (Exception e) {

        } finally {
            db.close();
        }

        return encontrado;
    }

    private ArrayList<String> revisarObjH_E(String archivo, objetosMineria objetosMineria) {
        ObjectContainer db = Db4o.openFile("mineria/ObjH_E.db");
        objetos_Experto Obj = new objetos_Experto();
        ArrayList<String> listaObjetos = listaObjetos_homologosExperto(archivo);
        try {
            ObjectSet result = db.queryByExample(Obj);
            while (result.hasNext()) {
                objetos_Experto objExp = (objetos_Experto) result.next();
                for (int i = 0; i < listaObjetos.size(); i++) {
                    if (objExp.getID().equals(listaObjetos.get(i))) {
                        for (int j = 0; j < objExp.getHGNC().size(); j++) {
                            objetosMineria.agregar_objeto(objExp.getHGNC().get(j));
                        }
                        listaObjetos.remove(i);
                    }
                }
                System.out.print(".");
            }

        } catch (Exception e) {

        } finally {
            db.close();
        }
        //objetosMineria.imprimir();
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
                System.out.print(".");
//System.out.println("iteracion " + obj.getIteracion());
            }
        } catch (Exception e) {
        } finally {
            db.close();
        }

        return obj;
    }

    public String IngresarRegionPromotora() {
        Scanner lectura = new Scanner(System.in);
        String regionPromotora;
        while (true) {
            System.out.print("*Nombre de archivo region promotora: ");
            regionPromotora = lectura.nextLine();
            if (!regionPromotora.equals("")) {
                break;
            } else {
                System.out.println("Debe ingresar un nombre de archivo");
            }
        }
        return regionPromotora;
    }

    public float IngresarConfiabilidad() {
        Scanner lectura = new Scanner(System.in);
        float conf;
        while (true) {
            try {
                System.out.print("*Indice de confiabilidad en TFbind (0-100): ");
                String confi = lectura.nextLine();
                conf = Float.parseFloat(confi) / 100;

                if (conf > 1) {
                    System.out.println("El dato ingresado debe ser numerico entre 0 y 100");
                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.println("El dato ingresado debe ser numerico entre 0 y 100");
            }
        }
        return conf;
    }

    public int ingresarCantComplejos() {
        int can_objs;
        Scanner lectura = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("*Numero maximo de objetos PDB : ");
                can_objs = Integer.parseInt(lectura.nextLine());
                break;
            } catch (Exception e) {
                System.out.println("El dato ingresado debe ser numerico");
            }
        }
        return can_objs;
    }

    public int ingresar_numIteracioens() {
        int num_iter;
        Scanner lectura = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("*Numero de niveles de busqueda: ");
                num_iter = Integer.parseInt(lectura.nextLine());
                break;
            } catch (Exception e) {
                System.out.println("El dato ingresado debe ser numerico");
            }
        }
        return num_iter;
    }
    
     public int ingresar_cantPubMedId() {
        int cant_pm_id;
        Scanner lectura = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("*Cantidad maxima de PubMed Ids para cada busqueda: ");
                cant_pm_id = Integer.parseInt(lectura.nextLine());
                break;
            } catch (Exception e) {
                System.out.println("El dato ingresado debe ser numerico");
            }
        }
        return cant_pm_id;
    }

    public boolean reiniciar() {
        boolean reiniciar;
        Scanner lectura = new Scanner(System.in);

        System.out.println("Existe un proceso de mineria de configuracion: ");
        verConfiguracion();
        System.out.println();
        while (true) {
            System.out.print("*Desea continuar con el proceso  ..S/N: ");
            String resp = lectura.nextLine();
            if (resp.equalsIgnoreCase("S")) {
                reiniciar = false;
                break;
            } else if (resp.equalsIgnoreCase("N")) {
                reiniciar = true;
                break;
            } else {
                System.out.println("Debe presionar las teclas (S) o (N) para seleccionar una opcion..");
            }

        }
        return reiniciar;
    }

    public boolean buscarGO() {
        boolean GO = false;
        Scanner lectura = new Scanner(System.in);
        while (true) {
            System.out.print("*Buscar ontologia en GeneOntology  ..S/N: ");
            String resp = lectura.nextLine();
            if (resp.equalsIgnoreCase("s")) {
                GO = true;
                break;
            } else if (resp.equalsIgnoreCase("n")) {
                GO = false;
                break;
            } else {
                System.out.println("Debe presionar las teclas (S) o (N) para seleccionar una opcion..");
            }

        }
        return GO;
    }

    public boolean buscarMESH() {
        boolean MESH = false;
        Scanner lectura = new Scanner(System.in);
        while (true) {
            System.out.print("*Buscar ontologia en MESH  ..S/N: ");
            String resp = lectura.nextLine();
            if (resp.equalsIgnoreCase("s")) {
                MESH = true;
                break;
            } else if (resp.equalsIgnoreCase("n")) {
                MESH = false;
                break;
            } else {
                System.out.println("Debe presionar las teclas (S) o (N) para seleccionar una opcion..");
            }
        }
        return MESH;
    }

    public String PMidExperto() {
        String ruta = "";
        Scanner lectura = new Scanner(System.in);
        boolean r;

        while (true) {
            System.out.print("*Desea agregar PubMed ID de Experto?  ..S/N: ");
            String resp = lectura.nextLine();
            if (resp.equalsIgnoreCase("s")) {
                r = true;
                break;
            } else if (resp.equalsIgnoreCase("n")) {
                r = false;
                break;
            } else {
                System.out.println("Debe presionar las teclas (S) o (N) para seleccionar una opcion..");
            }

        }
        if (r) {
            while (true) {
                System.out.print("*Indique el nombre del archivo donde estan los PubMed Ids: ");
                ruta = lectura.nextLine();
                if (!ruta.equals("")) {
                    break;
                } else {
                    System.out.println("Debe ingresar un nombre de archivo");
                }
            }
        }

        return ruta;
    }

    public void ver_detalles() {
        Scanner lectura = new Scanner(System.in);
        boolean r = true;
        while (r) {
            System.out.println("***Ver Detalles de objetos Minados***");
            System.out.println("1.- Listar Objetos minados");
            System.out.println("2.- Ver Detalle de Objeto");
            System.out.println("3.-Listar Ligandos");
            System.out.println("4.- Ontologias");
            System.out.println("0.-Salir");

            String resp = lectura.nextLine();

            switch (resp) {

                case "1":
                    ArrayList<String> lista = listar_objetos_minados();
                    System.out.println("Lista de objetos minados:\n");
                    for (int i = 0; i < lista.size(); i++) {
                        System.out.println(lista.get(i));
                    }
                    System.out.println();
                    break;

                case "2":
                    detalle_objeto();
                    System.out.println();
                    break;

                case "3":
                    System.out.println("\n Lista de ligandos encontrados");
                    ArrayList<String> ligandos = listar_ligandos();
                    for (int i = 0; i < ligandos.size(); i++) {
                        System.out.println(ligandos.get(i));
                    }
                    System.out.println();
                    break;
                case "4":
                    ontologiaObjMin objont = new ontologiaObjMin();
                    objont.imprimirTodo();
                    break;
                case "0":
                    r = false;
                    break;
            }
        }

    }

    public ArrayList<String> listar_objetos_minados() {

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

        return obj.getObjetos_minados();
    }

    public void detalle_objeto() {
        Scanner lectura = new Scanner(System.in);
        System.out.print("Ingrese el simbolo del objeto que desea consultar: ");
        String simbolo = lectura.nextLine();

        ArrayList<String> listaOM = listar_objetos_minados();
        if (listaOM.contains(simbolo)) {
            factorTranscripcion FT = new factorTranscripcion();
            buscar_Objeto(simbolo, FT);
            ontologiaObjMin objont = new ontologiaObjMin();

            ontologiaObjMin ontologias = new ontologiaObjMin();
            ontologias.buscarObjeto(simbolo);

        } else {
            System.out.println("no encontrado");
        }
    }

    private boolean buscar_Objeto(String objeto, factorTranscripcion FT) {
        objetosMineria obj = new objetosMineria();
        ObjectContainer db = Db4o.openFile("mineria/FT.db");
        factorTranscripcion ft = new factorTranscripcion();
        ft.setID(objeto);
        try {
            ObjectSet result = db.queryByExample(ft);
            while (result.hasNext()) {
                FT = (factorTranscripcion) result.next();
                FT.imprimir();
                return true;
            }
        } catch (Exception e) {

        } finally {
            db.close();
        }

        return false;
    }

    private ArrayList<String> listar_ligandos() {
        ArrayList<String> listaLigandos = new ArrayList<>();
        factorTranscripcion obj = new factorTranscripcion();
        ObjectContainer db = Db4o.openFile("mineria/FT.db");
        try {
            ObjectSet result = db.queryByExample(obj);
            while (result.hasNext()) {
                obj = (factorTranscripcion) result.next();
                for (int i = 0; i < obj.getComplejoProteinico().size(); i++) {
                    complejoProteinico comp = obj.getComplejoProteinico().get(i);
                    for (int j = 0; j < comp.getLigandos().size(); j++) {
                        if (!listaLigandos.contains(comp.getLigandos().get(j).getId())) {
                            listaLigandos.add(comp.getLigandos().get(j).getId());
                        }
                    }
                }
            }
        } catch (Exception e) {
        } finally {
            db.close();
        }
        return listaLigandos;
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

    public String getRegionPromotora() {
        return RegionPromotora;
    }

    public void setRegionPromotora(String RegionPromotora) {
        this.RegionPromotora = RegionPromotora;
    }

    public ArrayList<lecturas_TFBIND> getTfbind() {
        return tfbind;
    }

    public void setTfbind(ArrayList<lecturas_TFBIND> tfbind) {
        this.tfbind = tfbind;
    }

    public boolean isHomologos() {
        return homologos;
    }

    public void setHomologos(boolean homologos) {
        this.homologos = homologos;
    }

    public boolean isObjetosExperto() {
        return objetosExperto;
    }

    public void setObjetosExperto(boolean objetosExperto) {
        this.objetosExperto = objetosExperto;
    }

    public boolean isLecturas_tfbind() {
        return lecturas_tfbind;
    }

    public void setLecturas_tfbind(boolean lecturas_tfbind) {
        this.lecturas_tfbind = lecturas_tfbind;
    }

    public boolean isProcesoIteraciones() {
        return procesoIteraciones;
    }

    public void setProcesoIteraciones(boolean procesoIteraciones) {
        this.procesoIteraciones = procesoIteraciones;
    }

    public boolean isCombinaciones() {
        return combinaciones;
    }

    public void setCombinaciones(boolean combinaciones) {
        this.combinaciones = combinaciones;
    }

    public boolean isAbstracts() {
        return abstracts;
    }

    public void setAbstracts(boolean abstracts) {
        this.abstracts = abstracts;
    }

    public boolean isVaciado_pl() {
        return vaciado_pl;
    }

    public void setVaciado_pl(boolean vaciado_pl) {
        this.vaciado_pl = vaciado_pl;
    }

    public boolean isCrearOntologiaGO() {
        return crearOntologiaGO;
    }

    public void setCrearOntologiaGO(boolean crearOntologiaGO) {
        this.crearOntologiaGO = crearOntologiaGO;
    }

    public boolean isCrearOntologiaMESH() {
        return crearOntologiaMESH;
    }

    public void setCrearOntologiaMESH(boolean crearOntologiaMESH) {
        this.crearOntologiaMESH = crearOntologiaMESH;
    }

    public boolean isGenerarResumenes() {
        return generarResumenes;
    }

    public void setGenerarResumenes(boolean generarResumenes) {
        this.generarResumenes = generarResumenes;
    }

    public int getResumenes() {
        return resumenes;
    }

    public void setResumenes(int resumenes) {
        this.resumenes = resumenes;
    }

    public boolean isGenerarBC() {
        return GenerarBC;
    }

    public void setGenerarBC(boolean GenerarBC) {
        this.GenerarBC = GenerarBC;
    }

    public int getCantidadPMID() {
        return cantidadPMID;
    }

    public void setCantidadPMID(int cantidadPMID) {
        this.cantidadPMID = cantidadPMID;
    }

    public boolean isInferirPatrones() {
        return InferirPatrones;
    }

    public void setInferirPatrones(boolean InferirPatrones) {
        this.InferirPatrones = InferirPatrones;
    }

    public String getRutaPMID_experto() {
        return rutaPMID_experto;
    }

    public void setRutaPMID_experto(String rutaPMID_experto) {
        this.rutaPMID_experto = rutaPMID_experto;
    }

}
