/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuracion;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.collections.ActivatableArrayList;
import estructura.complejoProteinico;
import estructura.factorTranscripcion;
import estructura.objetos_Experto;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import pipeline.BioPattern;
import pipeline.GeneradorBC;
import pipeline.PubMed_IDs;
import pipeline.Resumidor;
import pipeline.combinaciones;
import servicios.lecturas_TFBIND;
import pipeline.consultasJPL;
import servicios.lecturas_PM;
import pipeline.minado_FT;
import pipeline.objetos_patrones;
import pipeline.pathway;
import pipeline.patrones;

/**
 *
 * @author yacson-ramirez
 */
public class configuracion {

    //conjunto de variables que indican la configuracion inicial del proceso
    private int numIteraciones;
    private int cantComplejos;
    private float confiabilidad_tfbind;
    private String RegionPromotora;
    private boolean crearOntologiaGO;
    private boolean crearOntologiaMESH;
    private int cantidadPMID;
    private String rutaPMID_experto;
//-------------------------------------//

    //conjunto de variables que indican los diferen checklist que debe complir el proceso
    private boolean homologos; //Lectura de homologos
    private boolean objetosExperto; // Lectura objetos del experto
    private ArrayList<lecturas_TFBIND> tfbind; //lista de facores de transcripcion
    private boolean lecturas_tfbind; //primera iteracion
    private boolean procesoIteraciones; //proceso de Iteraciones 2 en adelante
    private boolean combinaciones; // generacion de comobinaciones de palabras clave
    private boolean pubmedids; // busqeda de los pubmed ids
    private boolean abstracts; // buesqueda de abstracts en PUBMED
    private boolean vaciado_pl; // vaciado de objetos minados y ontologias a formato prolog
    private boolean generarResumenes; //generacion de resumenes a partir de los abstracts
    private int resumenes; //archivos resumidos
    private boolean GenerarBC;//Lista de eventos encontrados en los resumenes
    private boolean objetosPatrones; //genera archivo con clasificacion espesifica de los objetos en la BC a partir de las ontologias
    private boolean InferirPatrones; //crea los pathway usando los eventos, y la informacin de las ontologias
    private boolean nombreCorto;//nombre cortos para combinaciones de palabras clave
    //----------------------------------------------------------------------------

    public configuracion() {
        resumenes = 1;
        RegionPromotora = null;
        tfbind = new ArrayList<lecturas_TFBIND>();
    }

    //guarda la configuracion inicial del proceso
    public void guardarConfiguracion(String regionProm, int numIter, int cantCompl, float conf, boolean GO, boolean MESH, boolean nombreCorto, int cantPMID, String PMidExp, String ruta) {
        this.RegionPromotora = regionProm;
        this.cantComplejos = cantCompl;
        this.numIteraciones = numIter;
        this.confiabilidad_tfbind = conf;
        this.crearOntologiaGO = GO;
        this.crearOntologiaMESH = MESH;
        this.cantidadPMID = cantPMID;
        this.rutaPMID_experto = PMidExp;
        this.nombreCorto = nombreCorto;

        ObjectContainer db = Db4o.openFile(ruta + "/config.db");
        try {
            db.store(this);
            System.out.println(utilidades.idioma.get(16));
        } catch (Exception e) {
            System.out.println(utilidades.idioma.get(17));
        } finally {
            db.close();
        }
    }

    //guarda las lecturas obtenidas en tfbind. Necesario si el proceso se reanuda deste este punto
    public void guardar_lecturasTFBIND(ArrayList<lecturas_TFBIND> lecturas, String ruta) {
        ObjectContainer db = Db4o.openFile(ruta + "/config.db");
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
            System.out.println(utilidades.idioma.get(17));
        } finally {
            db.close();
        }
    }

    //este metodo sera llamado cada vez que culmine una tarea el proceso y se agregue un checklist
    public void guardar(String ruta) {
        ObjectContainer db = Db4o.openFile(ruta + "/config.db");
        configuracion conf = new configuracion();
        conf.RegionPromotora = this.RegionPromotora;
        try {
            ObjectSet result = db.queryByExample(conf);
            while (result.hasNext()) {
                configuracion config = (configuracion) result.next();
                config = this;
                db.store(config);

                break;
            }
        } catch (Exception e) {
            System.out.println(utilidades.idioma.get(17));
        } finally {
            db.close();
        }
    }

    //recupera la informacion guardada.. para poder continuar un proceso desde el ultimo checklist guardado
    public configuracion recuperarConfiguracion(String ruta) {
        ObjectContainer db = Db4o.openFile(ruta + "/config.db");
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
                this.nombreCorto = config.nombreCorto;
                //------------------------------------------------------//
                this.tfbind = config.tfbind;
                this.homologos = config.homologos;
                this.objetosExperto = config.objetosExperto;
                this.lecturas_tfbind = config.lecturas_tfbind;
                this.procesoIteraciones = config.procesoIteraciones;
                this.combinaciones = config.combinaciones;
                this.pubmedids = config.pubmedids;
                this.abstracts = config.abstracts;
                this.vaciado_pl = config.vaciado_pl;
                this.crearOntologiaGO = config.crearOntologiaGO;
                this.crearOntologiaMESH = config.crearOntologiaMESH;
                this.generarResumenes = config.generarResumenes;
                this.resumenes = config.resumenes;
                this.GenerarBC = config.GenerarBC;
                this.objetosPatrones = config.objetosPatrones;
                this.InferirPatrones = config.InferirPatrones;

            }
        } catch (Exception e) {
            System.out.println(utilidades.idioma.get(17));
        } finally {
            db.close();
        }

        return this;
    }

    //muestra la configuracion inicial del proceso
    public void verConfiguracion(String ruta) {
        //System.out.println("\n**Configuracion de minado**");
        System.out.println("\n" + utilidades.idioma.get(18) + " " + this.RegionPromotora);
        System.out.println(utilidades.idioma.get(19) + " " + this.cantComplejos);
        System.out.println(utilidades.idioma.get(20) + " " + this.numIteraciones);
        System.out.println(utilidades.idioma.get(21) + " " + (int) (this.confiabilidad_tfbind * 100));
        System.out.println(utilidades.idioma.get(22) + " " + this.cantidadPMID);

        estadoactual(ruta);
    }

    //muestra el estado actual del proceso .. dependiendo del los checklist que esten activos
    private void estadoactual(String ruta) {
        System.out.print("\n" + utilidades.idioma.get(23));
        if (!homologos) {
            System.out.println(utilidades.idioma.get(24));
        } else if (!objetosExperto) {
            System.out.println(utilidades.idioma.get(25));
        } else if (!lecturas_tfbind) {
            System.out.println(utilidades.idioma.get(26));
        } else if (!procesoIteraciones) {
            objetosMineria objMin = new objetosMineria();
            objMin = recuperarObjetosMin(ruta);
            System.out.println(utilidades.idioma.get(27) + " " + (objMin.getIteracion() + 1));
        } else if (!combinaciones) {
            System.out.println(utilidades.idioma.get(28));
        } else if (!pubmedids) {
            System.out.println(utilidades.idioma.get(29));
        } else if (!abstracts) {
            System.out.println(utilidades.idioma.get(30));
        } else if (!vaciado_pl) {
            System.out.println(utilidades.idioma.get(31));
        } else if (!generarResumenes) {
            System.out.println(utilidades.idioma.get(32) + " " + resumenes);
        } else if (!GenerarBC) {
            System.out.println(utilidades.idioma.get(33));
        } else if (!objetosPatrones) {
            System.out.println(utilidades.idioma.get(34));
        } else if (!InferirPatrones) {
            System.out.println(utilidades.idioma.get(35));
        }
    }

    //dependiendo de los checklist que esten activos el proceso se reanudara desde un punto espesifico
    public void reanudar_proceso(String ruta, String ruta2) {
        new utilidades().limpiarPantalla();
        System.out.println(utilidades.colorTexto1 + utilidades.titulo);
        System.out.println(utilidades.colorReset);
        System.out.print(utilidades.idioma.get(139));
        recuperarConfiguracion(ruta);
        //verConfiguracion();
        objetosMineria objMin = new objetosMineria();
        objMin = recuperarObjetosMin(ruta);
        //System.out.println(objMin.getNuevos_objetos().size());

        if (!homologos) {
            //System.out.println("\nReanudar desde busqueda de homologos ...");
            reanudar(1, objMin, ruta, ruta2);
        } else if (!objetosExperto) {
            //System.out.println("\nReanudar desde busqueda de objetos Experto ...");
            reanudar(2, objMin, ruta, ruta2);
        } else if (!lecturas_tfbind) {
            //System.out.println("\nReanudando Iteracion: " + objMin.getIteracion());
            reanudar(3, objMin, ruta, ruta2);
        } else if (!procesoIteraciones) {
            reanudar(4, objMin, ruta, ruta2);
        } else if (!combinaciones) {
            reanudar(5, objMin, ruta, ruta2);
        } else if (!pubmedids) {
            reanudar(6, objMin, ruta, ruta2);
        } else if (!abstracts) {
            reanudar(7, objMin, ruta, ruta2);
        } else if (!vaciado_pl) {
            reanudar(8, objMin, ruta, ruta2);
        } else if (!generarResumenes) {
            reanudar(9, objMin, ruta, ruta2);
        } else if (!GenerarBC) {
            reanudar(10, objMin, ruta, ruta2);
        } else if (!objetosPatrones) {
            reanudar(11, objMin, ruta, ruta2);
        } else if (!InferirPatrones) {
            reanudar(12, objMin, ruta, ruta2);
        }
        //proceso terminado
        menuFinal(ruta, ruta2);

    }

    private void menuFinal(String ruta, String rutaD) {
        utilidades.texto_carga = "";
        utilidades.texto_etapa = "";
        utilidades.momento = "";

        Scanner lectura = new Scanner(System.in);
        boolean r = true;
        consultasJPL RRG = new consultasJPL();
        int minados = listar_ligandos(ruta).size() + listar_nuevos_objetos(ruta).size() + listar_objetos_minados(ruta).size();

        while (r) {
            System.out.println();
            new utilidades().limpiarPantalla();
            System.out.println(utilidades.colorTexto1 + utilidades.titulo);
            System.out.println(utilidades.colorTexto1 + utilidades.proceso);
            System.out.println();
            ArrayList<pathway> patrones = RRG.cargarPatrones(ruta);
            //System.out.print(utilidades.colorTexto2);
            System.out.println(utilidades.colorTexto2 + utilidades.idioma.get(36) + "\n");

            System.out.println(utilidades.idioma.get(37) + "               " + this.RegionPromotora);
            System.out.println(utilidades.idioma.get(38) + "          " + this.cantComplejos);
            System.out.println(utilidades.idioma.get(39) + "              " + this.numIteraciones);
            System.out.println(utilidades.idioma.get(40) + "           " + (int) (this.confiabilidad_tfbind * 100));
            System.out.println(utilidades.idioma.get(41) + "  " + this.cantidadPMID);
            if (!rutaPMID_experto.equals("")) {
                System.out.println(utilidades.idioma.get(42) + " " + rutaPMID_experto);
            }
            //System.out.print(utilidades.colorTexto2);
            System.out.println("\n" + utilidades.colorTexto2 + utilidades.idioma.get(43) + "\n");

            System.out.println(utilidades.idioma.get(44) + "            " + minados);
            System.out.println(utilidades.idioma.get(45) + "   " + num_combinaciones(ruta));
            System.out.println(utilidades.idioma.get(46) + "      " + num_pubmedIds(ruta));
            System.out.println(utilidades.idioma.get(47) + "        " + num_eventos(ruta));
            System.out.println(utilidades.idioma.get(48) + "       " + patrones.size() + "\n");

            System.out.println(utilidades.colorReset);
            System.out.println(utilidades.idioma.get(0));
            System.out.println(utilidades.idioma.get(49));
            System.out.println(utilidades.idioma.get(50));
            System.out.println(utilidades.idioma.get(51));
            System.out.println(utilidades.idioma.get(154));
            System.out.println(utilidades.idioma.get(4));

            String resp = lectura.nextLine();

            switch (resp) {

                case "1":

                    while (true) {
                        System.out.print(utilidades.idioma.get(52));
                        String resp2 = lectura.nextLine();
                        if (resp2.equalsIgnoreCase("s") || resp2.equalsIgnoreCase("y")) {
                            System.out.print("\033[H\033[2J");
                            System.out.flush();
                            minado_FT mft = new minado_FT();
                            mft.crearCarpeta(ruta);
                            configuracion config = new configuracion();
                            confGeneral confG = new confGeneral();
                            {
                                try {
                                    confG.pipeline(ruta, rutaD);
                                } catch (Exception e) {

                                }
                            }

                            break;
                        } else if (resp.equalsIgnoreCase("n")) {

                            break;
                        } else {
                            System.out.println(utilidades.idioma.get(53));
                        }
                    }

                    break;
                case "2":
                    RRG.menu(ruta);
                    break;
                case "3":
                    new patrones().inferir_patrones(this, ruta);
                    break;

                case "4":

                    reanudarDesde(ruta, rutaD);
                    break;
                case "0":
                    r = false;
                    break;

            }

        }
    }

    private void reanudarDesde(String ruta, String rutaD) {
        Scanner lectura = new Scanner(System.in);
        boolean r = true;

        while (r) {
            System.out.println(utilidades.idioma.get(0));
            System.out.println("1.- " + utilidades.idioma.get(28));
            System.out.println("2.- " + utilidades.idioma.get(29));
            System.out.println("3.- " + utilidades.idioma.get(30));
            System.out.println("4.- " + utilidades.idioma.get(31));
            System.out.println("5.- " + utilidades.idioma.get(32));
            System.out.println("6.- " + utilidades.idioma.get(33));
            System.out.println("7.- " + utilidades.idioma.get(34));
            System.out.println("0.- " + utilidades.idioma.get(4));

            String resp = lectura.nextLine();

            switch (resp) {

                case "1":
                    combinaciones = false;
                    pubmedids = false;
                    abstracts = false;
                    vaciado_pl = false;
                    generarResumenes = false;
                    GenerarBC = false;
                    objetosPatrones = false;
                    InferirPatrones = false;
                    guardar(ruta);
                    reanudar(5, new objetosMineria(), ruta, rutaD);
                    break;

                case "2":
                    pubmedids = false;
                    abstracts = false;
                    vaciado_pl = false;
                    generarResumenes = false;
                    GenerarBC = false;
                    objetosPatrones = false;
                    InferirPatrones = false;
                    guardar(ruta);
                    reanudar(6, new objetosMineria(), ruta, rutaD);
                    break;

                case "3":
                    abstracts = false;
                    vaciado_pl = false;
                    generarResumenes = false;
                    GenerarBC = false;
                    objetosPatrones = false;
                    InferirPatrones = false;
                    guardar(ruta);
                    reanudar(7, new objetosMineria(), ruta, rutaD);
                    break;

                case "4":
                    vaciado_pl = false;
                    generarResumenes = false;
                    GenerarBC = false;
                    objetosPatrones = false;
                    InferirPatrones = false;
                    guardar(ruta);
                    reanudar(8, new objetosMineria(), ruta, rutaD);
                    break;

                case "5":
                    generarResumenes = false;
                    GenerarBC = false;
                    objetosPatrones = false;
                    InferirPatrones = false;
                    guardar(ruta);
                    reanudar(9, new objetosMineria(), ruta, rutaD);
                    break;

                case "6":
                    GenerarBC = false;
                    objetosPatrones = false;
                    InferirPatrones = false;
                    guardar(ruta);
                    reanudar(10, new objetosMineria(), ruta, rutaD);
                    break;

                case "7":
                    objetosPatrones = false;
                    InferirPatrones = false;
                    guardar(ruta);
                    reanudar(11, new objetosMineria(), ruta, rutaD);
                    break;

                case "0":
                    r = false;
                    break;

            }

        }

    }

    //dependiendo del punto de reanudacion del proceso se ejecutaran el juego instrucciones necesarias 
    //para que el proceso termine
    private void reanudar(int punto, objetosMineria objetosMineria, String ruta, String rutaD) {
        minado_FT mfts = new minado_FT();
        lecturas_PM lpm = new lecturas_PM();
        switch (punto) {
            case 1:
                mfts.buscarHomologos(revisarObjH_E(rutaD + "/homologous", objetosMineria, ruta), objetosMineria, this, crearOntologiaGO, crearOntologiaMESH, ruta);
                mfts.buscarObjetosExperto(listaObjetos_homologosExperto(rutaD + "/expert_objects.txt"), objetosMineria, this, crearOntologiaGO, crearOntologiaMESH, ruta);
                mfts.primeraIteracion(rutaD + "/" + RegionPromotora, confiabilidad_tfbind, cantComplejos, objetosMineria, this, new ActivatableArrayList<lecturas_TFBIND>(), crearOntologiaGO, crearOntologiaMESH, ruta);
                mfts.Iteraciones(false, new ArrayList<String>(), cantComplejos, numIteraciones, objetosMineria, this, 1, crearOntologiaGO, crearOntologiaMESH, ruta);
                new combinaciones().generar_combinaciones(false, this, ruta, nombreCorto);
                new PubMed_IDs().buscar(cantidadPMID, this, ruta);
                lpm.BusquedaPM_Abstracts("abstracts", 500, this, ruta);
                mfts.vaciar_bc_pl(crearOntologiaGO, crearOntologiaMESH, this, ruta);
                new Resumidor().resumidor(this, ruta);
                try {
                    String base_conocimiento = new GeneradorBC().generadorBC("kBase.pl", this, ruta);
                    objetos_patrones objetos_patrones = new objetos_patrones();
                    objetos_patrones.generar_archivo(this, ruta);
                } catch (Exception e) {
                }
                new patrones().inferir_patrones(this, ruta);
                break;
            case 2:
                revisarObjH_E(rutaD + "/homologous", objetosMineria, ruta);
                mfts.buscarObjetosExperto(revisarObjH_E(rutaD + "/expert_objects.txt", objetosMineria, ruta), objetosMineria, this, crearOntologiaGO, crearOntologiaMESH, ruta);
                mfts.primeraIteracion(rutaD + "/" + RegionPromotora, confiabilidad_tfbind, cantComplejos, objetosMineria, this, new ArrayList<lecturas_TFBIND>(), crearOntologiaGO, crearOntologiaMESH, ruta);
                mfts.Iteraciones(false, new ArrayList<String>(), cantComplejos, numIteraciones, objetosMineria, this, 1, crearOntologiaGO, crearOntologiaMESH, ruta);
                new combinaciones().generar_combinaciones(false, this, ruta, nombreCorto);
                new PubMed_IDs().buscar(cantidadPMID, this, ruta);
                lpm.BusquedaPM_Abstracts("abstracts", 500, this, ruta);
                mfts.vaciar_bc_pl(crearOntologiaGO, crearOntologiaMESH, this, ruta);
                new Resumidor().resumidor(this, ruta);
                try {
                    String base_conocimiento = new GeneradorBC().generadorBC("kBase.pl", this, ruta);
                    objetos_patrones objetos_patrones = new objetos_patrones();
                    objetos_patrones.generar_archivo(this, ruta);
                } catch (Exception e) {
                }
                new patrones().inferir_patrones(this, ruta);
                break;
            case 3:
                revisarObjH_E(rutaD + "/homologous", objetosMineria, ruta);
                revisarObjH_E(rutaD + "/expert_objects.txt", objetosMineria, ruta);
                ArrayList<lecturas_TFBIND> lecturas = actualizarListaTFBind(objetosMineria, ruta);
                mfts.primeraIteracion(rutaD + "/" + RegionPromotora, confiabilidad_tfbind, cantComplejos, objetosMineria, this, lecturas, crearOntologiaGO, crearOntologiaMESH, ruta);
                mfts.Iteraciones(false, new ArrayList<String>(), cantComplejos, numIteraciones, objetosMineria, this, 1, crearOntologiaGO, crearOntologiaMESH, ruta);
                new combinaciones().generar_combinaciones(false, this, ruta, nombreCorto);
                new PubMed_IDs().buscar(cantidadPMID, this, ruta);
                lpm.BusquedaPM_Abstracts("abstracts", 500, this, ruta);
                mfts.vaciar_bc_pl(crearOntologiaGO, crearOntologiaMESH, this, ruta);
                new Resumidor().resumidor(this, ruta);
                try {
                    String base_conocimiento = new GeneradorBC().generadorBC("kBase.pl", this, ruta);
                    objetos_patrones objetos_patrones = new objetos_patrones();
                    objetos_patrones.generar_archivo(this, ruta);
                } catch (Exception e) {
                }
                new patrones().inferir_patrones(this, ruta);
                break;
            case 4:
                ArrayList<String> ListaObj = reanudarIteracion(objetosMineria, ruta);
                mfts.Iteraciones(true, ListaObj, cantComplejos, numIteraciones, objetosMineria, this, objetosMineria.getIteracion() + 1, crearOntologiaGO, crearOntologiaMESH, ruta);
                new combinaciones().generar_combinaciones(false, this, ruta, nombreCorto);
                new PubMed_IDs().buscar(cantidadPMID, this, ruta);
                lpm.BusquedaPM_Abstracts("abstracts", 500, this, ruta);
                mfts.vaciar_bc_pl(crearOntologiaGO, crearOntologiaMESH, this, ruta);
                new Resumidor().resumidor(this, ruta);
                try {
                    String base_conocimiento = new GeneradorBC().generadorBC("kBase.pl", this, ruta);
                    objetos_patrones objetos_patrones = new objetos_patrones();
                    objetos_patrones.generar_archivo(this, ruta);
                } catch (Exception e) {
                }
                new patrones().inferir_patrones(this, ruta);
                break;

            case 5:
                new combinaciones().generar_combinaciones(false, this, ruta, nombreCorto);
                new PubMed_IDs().buscar(cantidadPMID, this, ruta);
                lpm.BusquedaPM_Abstracts("abstracts", 500, this, ruta);
                mfts.vaciar_bc_pl(crearOntologiaGO, crearOntologiaMESH, this, ruta);
                new Resumidor().resumidor(this, ruta);
                try {
                    String base_conocimiento = new GeneradorBC().generadorBC("kBase.pl", this, ruta);
                    objetos_patrones objetos_patrones = new objetos_patrones();
                    objetos_patrones.generar_archivo(this, ruta);
                } catch (Exception e) {
                }
                new patrones().inferir_patrones(this, ruta);
                break;
            case 6:
                new PubMed_IDs().buscar(cantidadPMID, this, ruta);
                lpm.BusquedaPM_Abstracts("abstracts", 500, this, ruta);
                mfts.vaciar_bc_pl(crearOntologiaGO, crearOntologiaMESH, this, ruta);
                new Resumidor().resumidor(this, ruta);
                try {
                    String base_conocimiento = new GeneradorBC().generadorBC("kBase.pl", this, ruta);
                    objetos_patrones objetos_patrones = new objetos_patrones();
                    objetos_patrones.generar_archivo(this, ruta);
                } catch (Exception e) {
                }
                new patrones().inferir_patrones(this, ruta);
                break;
            case 7:
                lpm.BusquedaPM_Abstracts("abstracts", 500, this, ruta);
                mfts.vaciar_bc_pl(crearOntologiaGO, crearOntologiaMESH, this, ruta);
                new Resumidor().resumidor(this, ruta);
                try {
                    String base_conocimiento = new GeneradorBC().generadorBC("kBase.pl", this, ruta);
                    objetos_patrones objetos_patrones = new objetos_patrones();
                    objetos_patrones.generar_archivo(this, ruta);
                } catch (Exception e) {
                }
                new patrones().inferir_patrones(this, ruta);
                break;
            case 8:
                mfts.vaciar_bc_pl(crearOntologiaGO, crearOntologiaMESH, this, ruta);
                new Resumidor().resumidor(this, ruta);
                try {
                    String base_conocimiento = new GeneradorBC().generadorBC("kBase.pl", this, ruta);
                    objetos_patrones objetos_patrones = new objetos_patrones();
                    objetos_patrones.generar_archivo(this, ruta);
                } catch (Exception e) {
                }
                new patrones().inferir_patrones(this, ruta);
                break;
            case 9:
                new Resumidor().resumidor(this, ruta);
                try {
                    String base_conocimiento = new GeneradorBC().generadorBC("kBase.pl", this, ruta);
                    objetos_patrones objetos_patrones = new objetos_patrones();
                    objetos_patrones.generar_archivo(this, ruta);
                } catch (Exception e) {
                }
                new patrones().inferir_patrones(this, ruta);
                break;
            case 10:
                try {
                    String base_conocimiento = new GeneradorBC().generadorBC("kBase.pl", this, ruta);
                    objetos_patrones objetos_patrones = new objetos_patrones();
                    objetos_patrones.generar_archivo(this, ruta);
                } catch (Exception e) {
                }
                new patrones().inferir_patrones(this, ruta);
            case 11:
                objetos_patrones objetos_patrones = new objetos_patrones();
                objetos_patrones.generar_archivo(this, ruta);
                new patrones().inferir_patrones(this, ruta);
                break;
            case 12:
                new patrones().inferir_patrones(this, ruta);
                break;
        }
    }

    /*busca de todos las lecturas tfbind cuales ya fueron procesadas 
     solo aquellas que no, se agregan a una lista y se continuara el proceso con estas 
     */
    private ArrayList<lecturas_TFBIND> actualizarListaTFBind(objetosMineria objetosMineria, String ruta) {
        ArrayList<lecturas_TFBIND> lista = new ArrayList<>();
        factorTranscripcion ft = new factorTranscripcion();

        tfbind.forEach((tfb) -> {
            if (!buscarObjeto(tfb.getFactor(), ft, ruta)) {
                buscarObjetos(objetosMineria, ft);
                lista.add(tfb);
            }
            System.out.print(".");
        });

        //System.out.println(lista.size());
        return lista;
    }

    private void buscarObjetos(objetosMineria objetosMineria, factorTranscripcion ft) {
        objetosMineria.getObjetos_minados().add(ft.getID());

        objetosMineria.getObjetos_minados().add(ft.getID());
        ft.getComplejoProteinico().forEach((comp) -> {
            comp.getHGNC().forEach(hgnc -> objetosMineria.agregarObjetosMinado(hgnc.getSimbolo()));
        });

    }

    private void buscarobj(objetosMineria objMin, String ruta) {
        ArrayList<String> Lista = new ArrayList<>();
        ArrayList<String> NuevosObj = new ArrayList<>();
        factorTranscripcion ft = new factorTranscripcion();

        for (int i = 0; i < objMin.getNuevos_objetos().size(); i++) {
            if (buscarObjeto(objMin.getNuevos_objetos().get(i), ft, ruta)) {
                objMin.getObjetos_minados().add(objMin.getNuevos_objetos().get(i));
                ft.NuevosObjetos(NuevosObj);

            } else {
                Lista.add(objMin.getNuevos_objetos().get(i));
            }
        }

        objMin.setNuevos_objetos(NuevosObj);
    }

    //Recupera los nuevos objetos y objetos faltantes por minar en una iteracion dada
    private ArrayList<String> reanudarIteracion(objetosMineria objMin, String ruta) {
        ArrayList<String> Lista = new ArrayList<>();
        ArrayList<String> NuevosObj = new ArrayList<>();
        factorTranscripcion ft = new factorTranscripcion();

        objMin.getNuevos_objetos().forEach((obj) -> {
            if (buscarObjeto(obj, ft, ruta)) {
                objMin.getObjetos_minados().add(obj);
                ft.NuevosObjetos(NuevosObj);
            } else {
                Lista.add(obj);
            }
            System.out.print(".");
        });

        objMin.setNuevos_objetos(new ArrayList<String>());
        NuevosObj.forEach(obj -> objMin.agregar_objeto(obj));

        return Lista;
    }

    /*se consulta un objeto espesifico provenientes del proceso de iteracion 
     si este se ecuentra retorna true de lo contrario retorna false
     */
    private boolean buscarObjeto(String objeto, factorTranscripcion FT, String ruta) {
        ObjectContainer db = Db4o.openFile(ruta + "/TF.db");
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

    /*Este metodo revida la base de datos de objetos del experto y homologos a los que 
     ya se hizo el proceso de busqueda y se compara con los archivos de homologos y objetos del experto
     el proceso continuara para los objetos que se procesaron aun*/
    private ArrayList<String> revisarObjH_E(String archivo, objetosMineria objetosMineria, String ruta) {
        ObjectContainer db = Db4o.openFile(ruta + "/homologousObjects.db");
        objetos_Experto Obj = new objetos_Experto();
        ArrayList<String> listaObjetos = listaObjetos_homologosExperto(archivo);
        try {
            ObjectSet result = db.queryByExample(Obj);

            result.parallelStream().forEach((res) -> {
                objetos_Experto objExp = (objetos_Experto) res;
                listaObjetos.forEach((objEH) -> {
                    if (objExp.getID().equals(objEH)) {
                        objExp.getHGNC().forEach(t -> objetosMineria.agregar_objeto(t));
                        listaObjetos.removeIf(x -> x.equals(objEH));
                    }
                });
                System.out.print(".");
            });

        } catch (Exception e) {

        } finally {
            db.close();
        }
        //objetosMineria.imprimir();
        return listaObjetos;
    }

    //Lee la informacion de un archivo ya sea homolos u objetos_Experto.txt
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

    private objetosMineria recuperarObjetosMin(String ruta) {
        objetosMineria obj = new objetosMineria();
        ObjectContainer db = Db4o.openFile(ruta + "/mineryObjects.db");

        try {
            ObjectSet result = db.queryByExample(obj);
            while (result.hasNext()) {
                obj = (objetosMineria) result.next();
                System.out.print(".");
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
            System.out.print(utilidades.idioma.get(54));
            regionPromotora = lectura.nextLine();
            if (!regionPromotora.equals("")) {
                break;
            } else {
                System.out.println(utilidades.idioma.get(55));
            }
        }
        return regionPromotora;
    }

    public float IngresarConfiabilidad() {
        Scanner lectura = new Scanner(System.in);
        float conf;
        while (true) {
            try {
                System.out.print(utilidades.idioma.get(56));
                String confi = lectura.nextLine();
                conf = Float.parseFloat(confi) / 100;

                if (conf > 1) {
                    System.out.println(utilidades.idioma.get(57));
                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.println(utilidades.idioma.get(57));
            }
        }
        return conf;
    }

    public int ingresarCantComplejos() {
        int can_objs;
        Scanner lectura = new Scanner(System.in);
        while (true) {
            try {
                System.out.print(utilidades.idioma.get(58));
                can_objs = Integer.parseInt(lectura.nextLine());
                break;
            } catch (Exception e) {
                System.out.println(utilidades.idioma.get(59));
            }
        }
        return can_objs;
    }

    public int ingresar_numIteraciones() {
        int num_iter;
        Scanner lectura = new Scanner(System.in);
        while (true) {
            try {
                System.out.print(utilidades.idioma.get(60));
                num_iter = Integer.parseInt(lectura.nextLine());
                break;
            } catch (Exception e) {
                System.out.println(utilidades.idioma.get(59));
            }
        }
        return num_iter;
    }

    public int ingresar_cantPubMedId() {
        int cant_pm_id;
        Scanner lectura = new Scanner(System.in);
        while (true) {
            try {
                System.out.print(utilidades.idioma.get(61));
                cant_pm_id = Integer.parseInt(lectura.nextLine());
                break;
            } catch (Exception e) {
                System.out.println(utilidades.idioma.get(59));
            }
        }
        return cant_pm_id;
    }

    public boolean reiniciar(String ruta) {
        new utilidades().limpiarPantalla();
        System.out.println(utilidades.colorTexto1 + utilidades.titulo);
        System.out.println(utilidades.colorReset);
        boolean reiniciar;
        Scanner lectura = new Scanner(System.in);

        if (isInferirPatrones()) {
            return false;
        } else {
            System.out.println(utilidades.idioma.get(62));
            verConfiguracion(ruta);
            System.out.println();
            while (true) {
                System.out.print(utilidades.idioma.get(63));
                String resp = lectura.nextLine();
                if (resp.equalsIgnoreCase("S") || resp.equalsIgnoreCase("Y")) {
                    reiniciar = false;
                    break;
                } else if (resp.equalsIgnoreCase("N")) {
                    reiniciar = true;
                    break;
                } else {
                    System.out.println(utilidades.idioma.get(53));
                }

            }
        }

        return reiniciar;
    }

    public boolean buscarGO() {
        boolean GO = false;
        Scanner lectura = new Scanner(System.in);
        while (true) {
            System.out.print(utilidades.idioma.get(64));
            String resp = lectura.nextLine();
            if (resp.equalsIgnoreCase("s") || resp.equalsIgnoreCase("y")) {
                GO = true;
                break;
            } else if (resp.equalsIgnoreCase("n")) {
                GO = false;
                break;
            } else {
                System.out.println(utilidades.idioma.get(53));
            }
        }
        return GO;
    }

    public boolean nombresCortos() {
        boolean nombreCorto = false;
        Scanner lectura = new Scanner(System.in);
        while (true) {
            System.out.print(utilidades.idioma.get(136));
            String resp = lectura.nextLine();
            if (resp.equalsIgnoreCase("s") || resp.equalsIgnoreCase("y")) {
                nombreCorto = true;
                break;
            } else if (resp.equalsIgnoreCase("n")) {
                nombreCorto = false;
                break;
            } else {
                System.out.println(utilidades.idioma.get(53));
            }
        }
        return nombreCorto;
    }

    public boolean buscarMESH() {
        boolean MESH = false;
        Scanner lectura = new Scanner(System.in);
        while (true) {
            System.out.print(utilidades.idioma.get(66));
            String resp = lectura.nextLine();
            if (resp.equalsIgnoreCase("s") || resp.equalsIgnoreCase("y")) {
                MESH = true;
                break;
            } else if (resp.equalsIgnoreCase("n")) {
                MESH = false;
                break;
            } else {
                System.out.println(utilidades.idioma.get(53));
            }
        }
        return MESH;
    }

    public String PMidExperto() {
        String ruta = "";
        Scanner lectura = new Scanner(System.in);
        boolean r;

        while (true) {
            System.out.print(utilidades.idioma.get(67));
            String resp = lectura.nextLine();
            if (resp.equalsIgnoreCase("s") || resp.equalsIgnoreCase("y")) {
                r = true;
                break;
            } else if (resp.equalsIgnoreCase("n")) {
                r = false;
                break;
            } else {
                System.out.println(utilidades.idioma.get(53));
            }

        }
        if (r) {
            while (true) {
                System.out.print(utilidades.idioma.get(68));
                ruta = lectura.nextLine();
                if (!ruta.equals("")) {
                    break;
                } else {
                    System.out.println(utilidades.idioma.get(69));
                }
            }
        }
        return ruta;
    }

    public ArrayList<String> listar_objetos_minados(String ruta) {

        objetosMineria obj = new objetosMineria();
        ObjectContainer db = Db4o.openFile(ruta + "/mineryObjects.db");

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

    public ArrayList<String> listar_nuevos_objetos(String ruta) {

        objetosMineria obj = new objetosMineria();
        ObjectContainer db = Db4o.openFile(ruta + "/mineryObjects.db");

        try {
            ObjectSet result = db.queryByExample(obj);

            while (result.hasNext()) {

                obj = (objetosMineria) result.next();

            }
        } catch (Exception e) {
        } finally {
            db.close();
        }

        return obj.getNuevos_objetos();
    }

    public ArrayList<String> listar_ligandos(String ruta) {
        ArrayList<String> listaLigandos = new ArrayList<>();
        factorTranscripcion obj = new factorTranscripcion();
        ObjectContainer db = Db4o.openFile(ruta + "/TF.db");
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

    private int num_combinaciones(String ruta) {
        int num = 0;
        ObjectContainer db = Db4o.openFile(ruta + "/combinations.db");
        combinacion com = new combinacion();
        ObjectSet result = db.queryByExample(com);
        combinacion combinacion = (combinacion) result.get(0);
        db.close();

        num = combinacion.combinaciones.size();

        return num;
    }

    private int num_pubmedIds(String ruta) {
        int num = 0;
        ObjectContainer db = Db4o.openFile(ruta + "/pubmedIDs.db");
        PMIDS ids = new PMIDS();
        ObjectSet result = db.queryByExample(ids);
        PMIDS pmids = (PMIDS) result.get(0);
        db.close();

        num = pmids.pubmed_ids.size();

        return num;
    }

    private int num_eventos(String ruta) {
        int num = 0;
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
            archivo = new File(ruta + "/kBase.pl");
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String linea;
            while ((linea = br.readLine()) != null) {

                num++;
            }
        } catch (Exception e) {
        }

        num = num - 2;
        return num;
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

    public boolean isPubmedids() {
        return pubmedids;
    }

    public void setPubmedids(boolean pubmedids) {
        this.pubmedids = pubmedids;
    }

    public boolean isObjetosPatrones() {
        return objetosPatrones;
    }

    public void setObjetosPatrones(boolean objetosPatrones) {
        this.objetosPatrones = objetosPatrones;
    }

}
