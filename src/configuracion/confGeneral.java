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
import estructura.factorTranscripcion;
import estructura.objetos_Experto;
import estructura.ontologiaGO;
import estructura.ontologiaMESH;
import estructura.ontologiaObjMin;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import pipeline.GeneradorBC;
import pipeline.PubMed_IDs;
import pipeline.Resumidor;
import pipeline.combinaciones;
import pipeline.consultasJPL;
import pipeline.escribirBC;
import pipeline.minado_FT;
import pipeline.objetos_patrones;
import pipeline.pathway;
import pipeline.patrones;
import servicios.lecturas_PM;
import servicios.lecturas_TFBIND;

/**
 *
 * @author yacson ramirez
 */
public class confGeneral {

    private String red;
    private String proceso;

    /**
     * Este método muestra el listado de las redes que se han minado
     */
    public void listarRedes() {

        final File carpeta = new File("mineria/redes");
        final File carpeta2 = new File("mineria/integracion");
        Scanner lectura = new Scanner(System.in);
        boolean r = true;
        
        listar_datos();
        
        while (r) {
            ArrayList<String> redes = new ArrayList<>();
            redes = listarCarpetas(carpeta);
            limpiarPantalla();

            System.out.println("Seleccione una opcion");
            if (redes.size() > 0) {
                System.out.println("==== Redes existentes ====");
                for (int i = 0; i < redes.size(); i++) {
                    System.out.println((i + 1) + ".- " + redes.get(i));
                }
            }

            ArrayList<String> redesInte = new ArrayList<>();
            redesInte = listarCarpetas(carpeta2);

            if (redesInte.size() > 0) {
                System.out.println("\nI.-Ir a redes integradas");
            }

            //System.out.println("N.-Crear una nueva Red");
            System.out.println("0.-Salir");
            String resp = lectura.nextLine();

            for (int i = 0; i < redes.size(); i++) {
                String r2 = (i + 1) + "";
                if (resp.equals(r2)) {
                    red = redes.get(i);
                    listarProcesos(redes.get(i));
                }
            }

//            if (resp.equalsIgnoreCase("N")) {
//                red = nuevaRed();
//                listarProcesos(red);
//            }

            if (resp.equalsIgnoreCase("I") && redesInte.size() > 0) {
                listarRedesInt(redesInte);
            }

            if (resp.equals("0")) {
                r = false;
            }
        }

    }

    public void listar_datos() {
        
        final File carpeta = new File("datos");

        ArrayList<String> redes = new ArrayList<>();

        redes = listarCarpetas(carpeta);

        for (String red : redes) {
            File dir_red = new File("mineria/redes/" + red);

            if (!dir_red.exists()) {
                dir_red.mkdir();
            }

            ArrayList<String> proteinas = new ArrayList<>();
            final File carpeta2 = new File("datos/"+red);
            proteinas = listarCarpetas(carpeta2);
            
            for (String proteina : proteinas) {
                File dir_prot = new File("mineria/redes/"+red+"/"+proteina);
                if(!dir_prot.exists()){
                     dir_prot.mkdir();
                }
            }
        }
    }

    private void listarRedesInt(ArrayList<String> redesInt) {

        Scanner lectura = new Scanner(System.in);
        boolean r = true;

        while (r) {
            limpiarPantalla();
            System.out.println("Seleccione una opcion");
            System.out.println("==== Redes existentes ====");
            for (int i = 0; i < redesInt.size(); i++) {
                System.out.println((i + 1) + ".- " + redesInt.get(i));
            }

            System.out.println("\n0.-Volver");
            String resp = lectura.nextLine();

            for (int i = 0; i < redesInt.size(); i++) {
                String r2 = (i + 1) + "";
                if (resp.equals(r2)) {
                    red = redesInt.get(i);
                    redIntegrada(red);
                }
            }

            if (resp.equals("0")) {
                r = false;
            }

        }

    }

    private void redIntegrada(String red) {
        String ruta = "mineria/integracion/" + red;
        Scanner lectura = new Scanner(System.in);
        boolean r = true;
        String resp;
        consultasJPL RRG = new consultasJPL();

        while (r) {
            ArrayList<pathway> patrones = RRG.cargarPatrones(ruta);
            limpiarPantalla();
            System.out.println("Red " + red + " integrada");
            System.out.println("Patrones encontrados:       " + patrones.size() + "\n");
            System.out.println("Seleccione una opcion");
            System.out.println("1.- Inferir patrones");
            System.out.println("2.- Analisis de la red");
            System.out.println("\n0.- Volver");

            resp = lectura.nextLine();
            switch (resp) {
                case "1":
                    new patrones().inferir_patrones(new configuracion(), ruta);
                    break;
                case "2":
                    RRG.menu(ruta);
                    break;

                case "0":
                    r = false;
                    break;
            }

        }

    }

    private String nuevaRed() {
        String nombreRed = "";
        Scanner lectura = new Scanner(System.in);

        while (true) {
            System.out.print("*Ingrese nombre de la nueva Red: ");
            nombreRed = lectura.nextLine();
            if (!nombreRed.equals("")) {
                break;
            } else {
                System.out.println("Debe ingresar un nombre valido");
            }
        }
        File file = new File("mineria/redes/" + nombreRed);
        file.mkdir();
        return nombreRed;
    }

    private void listarProcesos(String red) {

        final File carpeta = new File("mineria/redes/" + red);
        Scanner lectura = new Scanner(System.in);
        boolean r = true;

        while (r) {
            ArrayList<String> procesos = new ArrayList<>();
            procesos = listarCarpetas(carpeta);
            limpiarPantalla();
            System.out.println("Red : " + red);
            System.out.println("Seleccione una opcion");
            if (procesos.size() > 0) {
                System.out.println("==== Procesos existentes ====");

                for (int i = 0; i < procesos.size(); i++) {
                    System.out.println((i + 1) + ".- " + procesos.get(i));
                }
            }
            if (procesos.size() > 1) {
                System.out.println("\nI.-Hacer integracion de la red");
            }
           // System.out.println("\nN.-Crear un nuevo proceso");
            System.out.println("0.-Volver");
            String resp = lectura.nextLine();

            for (int i = 0; i < procesos.size(); i++) {
                String r2 = (i + 1) + "";
                if (resp.equals(r2)) {
                    proceso = procesos.get(i);
                   // System.out.println("seleccion = " + i + " " + procesos.get(i));
                    String ruta = "mineria/redes/" + red + "/" + proceso;
                    String rutaD ="datos/"+ red+"/"+proceso;
                    pipeline(ruta,rutaD);

                }
            }

//            if (resp.equalsIgnoreCase("N")) {
//                proceso = nuevoProceso();
//                String ruta = "mineria/redes/" + red + "/" + proceso;
//                pipeline(ruta);
//            }

            if (resp.equalsIgnoreCase("I")) {
                integrarRed(red, procesos);
            }

            if (resp.equals("0")) {
                r = false;
            }
        }

    }

    private void integrarRed(String red, ArrayList<String> procesos) {

        File file = new File("mineria/integracion/" + red);
        file.mkdir();

        String rutaDest = "mineria/integracion/" + red;

        for (String directorio : procesos) {
            System.out.print("Integrando: " + directorio);
            String rutaOri = "mineria/redes/" + red + "/" + directorio;
            integrarArchivos(rutaOri + "/objetosMinados.pl", rutaDest + "/objetosMinados.pl");
            integrarArchivos(rutaOri + "/objetos_patrones.pl", rutaDest + "/objetos_patrones.pl");
            integrarArchivos(rutaOri + "/ontologiaGO.pl", rutaDest + "/ontologiaGO.pl");
            integrarArchivos(rutaOri + "/ontologiaMESH.pl", rutaDest + "/ontologiaMESH.pl");
            integrarArchivos(rutaOri + "/well_know_rules.pl", rutaDest + "/well_know_rules.pl");

            integrarOntologias(rutaOri, rutaDest);

            integrarObjExp(rutaOri, rutaDest);
            integrarFT(rutaOri, rutaDest);

            System.out.println(" ...ok");
        }
        try {
            System.out.print("integrando BaseC.pl");
            new GeneradorBC().generadorBCIntg(red, true);
            System.out.println(" ...ok");
        } catch (StringIndexOutOfBoundsException ex) {
            Logger.getLogger(confGeneral.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(confGeneral.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void integrarObjExp(String ori, String dest) {
        objetos_Experto objExt = new objetos_Experto();
        ObjectContainer db = Db4o.openFile(ori + "/ObjH_E.db");
        ArrayList<objetos_Experto> listObjs = new ActivatableArrayList<>();
        try {
            ObjectSet result = db.queryByExample(objExt);
            listObjs.addAll(result);
        } catch (Exception e) {

        } finally {
            db.close();
        }

        for (objetos_Experto obj : listObjs) {

            if (!obj.buscar(obj, dest)) {
                ObjectContainer db2 = Db4o.openFile(dest + "/ObjH_E.db");
                try {
                    db2.store(obj);
                } catch (Exception e) {

                } finally {
                    db2.close();
                }
            }

        }

    }

    private void integrarFT(String ori, String dest) {
        factorTranscripcion ft = new factorTranscripcion();
        ObjectContainer db = Db4o.openFile(ori + "/FT.db");
        ArrayList<factorTranscripcion> listObjs = new ActivatableArrayList<>();
        try {
            ObjectSet result = db.queryByExample(ft);
            listObjs.addAll(result);
        } catch (Exception e) {

        } finally {
            db.close();
        }

        for (factorTranscripcion obj : listObjs) {
            if (!obj.buscar(obj, dest)) {
                ObjectContainer db2 = Db4o.openFile(dest + "/FT.db");
                try {
                    db2.store(obj);
                } catch (Exception e) {

                } finally {
                    db2.close();
                }
            }

        }

    }

    private void integrarOntologias(String ori, String dest) {
        //System.out.println(ori + "  -- " + dest);
        ontologiaObjMin objeto = new ontologiaObjMin();
        ObjectContainer db = Db4o.openFile(ori + "/ontologiaObjMin.db");
        ArrayList<ontologiaObjMin> listObjs = new ActivatableArrayList<>();
        try {
            ObjectSet result = db.queryByExample(objeto);
            listObjs.addAll(result);
        } catch (Exception e) {

        } finally {
            db.close();
        }

        for (ontologiaObjMin obj : listObjs) {
            if (!obj.buscarObjeto(obj, dest)) {

                obj.getFuncionMolecular().forEach(f -> integrarGO(f, ori, dest));
                obj.getProcesoBiologico().forEach(p -> integrarGO(p, ori, dest));
                obj.getComponenteCelular().forEach(c -> integrarGO(c, ori, dest));

                obj.getParent().forEach(p -> integrarMESH(p, ori, dest));

                ObjectContainer db2 = Db4o.openFile(dest + "/ontologiaObjMin.db");

                try {
                    db2.store(obj);
                } catch (Exception e) {

                } finally {
                    db2.close();
                }
            }

        }

    }

    private void integrarMESH(String MESH, String ori, String dest) {
        ontologiaMESH ont = new ontologiaMESH();
        ontologiaObjMin oom = new ontologiaObjMin();
        ont = ont.buscarO(MESH, ori);

        if (!oom.buscarObjeto(ont, dest)) {
            ont.getParent().forEach(o -> integrarMESH(o, ori, dest));
            ObjectContainer db = Db4o.openFile(dest + "/OntologiaMESH.db");
            try {
                db.store(ont);
                //System.out.println("Guardando: " + ont.getNombre() + " " + ont.getMESH());
            } catch (Exception e) {
                //System.out.println("Error al guardar en OntologiaGO.db...");
            } finally {
                db.close();
            }
        }
    }

    private void integrarGO(String GO, String ori, String dest) {
        ontologiaGO ont = new ontologiaGO();
        ontologiaObjMin oom = new ontologiaObjMin();
        ont = ont.buscarO(GO, ori);

        if (!oom.buscarObjeto(ont, dest)) {
            ont.getIs_a().forEach(o -> integrarGO(o, ori, dest));
            ont.getPart_of().forEach(o -> integrarGO(o, ori, dest));
            ont.getRegulates().forEach(o -> integrarGO(o, ori, dest));
            ont.getPositively_regulates().forEach(o -> integrarGO(o, ori, dest));
            ont.getNegatively_regulates().forEach(o -> integrarGO(o, ori, dest));
            ont.getCapable_of().forEach(o -> integrarGO(o, ori, dest));
            ont.getCapable_of_part_of().forEach(o -> integrarGO(o, ori, dest));
            ont.getOccurs_in().forEach(o -> integrarGO(o, ori, dest));

            ObjectContainer db = Db4o.openFile(dest + "/OntologiaGO.db");
            try {
                db.store(ont);
                //System.out.println("Guardando: " + ont.getNombre() + " " + ont.getGO());
            } catch (Exception e) {
                //System.out.println("Error al guardar en OntologiaGO.db...");
            } finally {
                db.close();
            }

        }

    }

    private void integrarArchivos(String ori, String dest) {

        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {

            archivo = new File(ori);

            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String linea;
            while ((linea = br.readLine()) != null) {
                //System.out.println(linea);
                new escribirBC(linea, dest);
            }

        } catch (Exception e) {

            // e.printStackTrace();
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

    private String nuevoProceso() {
        String proc = "";

        Scanner lectura = new Scanner(System.in);

        while (true) {
            System.out.print("*Ingrese nombre del nuevo proceso:  ");
            proc = lectura.nextLine();
            if (!proc.equals("")) {
                break;
            } else {
                System.out.println("Debe ingresar un nombre valido");
            }
        }
        File file = new File("mineria/redes/" + red + "/" + proc);
        file.mkdir();

        return proc;
    }

    public void pipeline(String ruta,String rutaD) throws StringIndexOutOfBoundsException {

        minado_FT mfts = new minado_FT(); // clase que contiene los metodos donde se buscara la informacion de los objetos minados
        //String ruta = "mineria/redes/" + r + "/" + p;
        configuracion config = new configuracion(); // clase donde se guarda la informacion de configuracion inicial del proceso de minado y los diferentes checklist que indican desde donde continuar la ejecucion

        try {
            config.recuperarConfiguracion(ruta); // recupera la configuracion actual y el checklist que indica desde que punto puede continuar la ejecucion
        } catch (Exception e) {
        }

        if (config.getRegionPromotora() == null) {

            try {
                //* Las siguientes lineas muestran un menu donde el usuario puede ingresar los datos de configuracion para ejecutar el proceso de mineria
                System.out.println("\n-------------------------\nNUEVO PROCESO DE MINERIA\n-------------------------");
                System.out.println("\nIngrese los datos de configuracion\n");

                String regProm = config.IngresarRegionPromotora();
                float conf = config.IngresarConfiabilidad();
                int cantObjs = config.ingresarCantComplejos();
                int iteraciones = config.ingresar_numIteraciones();
                //boolean GO = config.buscarGO();
                //boolean MESH = config.buscarMESH();
                boolean MESH = true;
                boolean GO = true;
                String rutaPMidExp = rutaD+"/"+config.PMidExperto();
                int cantPMID = config.ingresar_cantPubMedId(); //numero de pubmed IDs
                //fin de menu
                //-------------------------------------------------------------------------------------------------------------

                // crea una carpeta nueva 'mineria' donde se guardaran diferentes archivos generados durante el proceso .. si ya existe esta carpeta se eliminara con todos su contenido y se creara de nuevo vacia
                //mfts.crearCarpeta("mineria");
                //se guarda los datos de configuracion que se ingresaron el el menu anterior en mineria/config.db
                config.guardarConfiguracion(regProm, iteraciones, cantObjs, conf, GO, MESH, cantPMID, rutaPMidExp, ruta);

                //este metodo ejecuta el proceso de busqueda de informacio desde objetos del experto, homologos y los objetos encontrados en los diferentes niveles de busqueda
                mfts.minado(regProm, conf, iteraciones, cantObjs, GO, MESH, config, ruta, rutaD);

                //este metodo genera todas las combinaciones de objetos encontrados en el proceso anterior y guarda las ombinaciones en 'mineria/combinaciones.db'
                new combinaciones().generar_combinaciones(false, config, ruta);

                //este metodo toma el archivo de combinaciones anterior y procede a buscar PubMed IDs que resulten de cada combinacion guarda los IDs en 'mineria/PubMedId.db'
                new PubMed_IDs().buscar(cantPMID, config, ruta);

                //este metodo toma la el archivo de PubMed Ids y procede a hacer la busqueda abstracts
                //y crear una coleccion de archivos con extencion html en el directorio 'abctracts'
                new lecturas_PM().BusquedaPM_Abstracts("abstracts", 500, config, ruta); // Número máximo de abstracts por archivo

                //este metodo toma la imformacion minada tanto de los objetos minados como de las ontologias y la vacia en formato prolog
                //crea los archivos 'objetosMinados.pl' , ontologiaGO.pl, ontologiaMESH.pl , well_know_rules.pl
                mfts.vaciar_bc_pl(GO, MESH, config, ruta);

                //este metodo llama al resumidor_bioinformante hace uso de la coleccion de abstracts
                new Resumidor().resumidor(config, ruta);

                // crea la bace de conocimiento con el listado de eventos encontrados por el resumidor
                String kb = new GeneradorBC().generadorBC("baseC.pl", config, ruta);

                // se crea el archivo 'mineria/objetos_patrones.pl' haciendo uso de los objetos que se encontran en la base de conocimiento y la informacion en las ontologias
                new objetos_patrones().generar_archivo(config, ruta);

                //String kb = "baseC.pl";
                //new Razonador().inferir_patrones(kb, config);
                new patrones().inferir_patrones(config, ruta);
            } catch (Exception ex) {
                Logger.getLogger(confGeneral.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (config.reiniciar(ruta)) {
            //reinia el proceso de mineria 
            mfts.crearCarpeta(ruta);
            config = new configuracion();
            pipeline(ruta,rutaD);
        } else {
            //se reanuda desde el punto donde se marco el ultimo checklist
            config.reanudar_proceso(ruta,rutaD);
        }

    }

    private ArrayList<String> listarCarpetas(final File carpeta) {
        ArrayList<String> directorios = new ArrayList<>();
        for (final File ficheroEntrada : carpeta.listFiles()) {
            if (ficheroEntrada.isDirectory()) {
                directorios.add(ficheroEntrada.getName());
                //listarCarpetas(ficheroEntrada, directorios);
            }
        }
        return directorios;
    }

    private void limpiarPantalla() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public String getRed() {
        return red;
    }

    public String getProceso() {
        return proceso;
    }
}
