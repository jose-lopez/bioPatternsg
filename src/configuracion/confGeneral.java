/* 
 * bioPatternsg
 * BioPatternsg is a system that allows the integration and analysis of information related to the modeling of Gene Regulatory Networks (GRN).
 * Copyright (C) 2020
 * Jose Lopez (josesmooth@gmail.com), Jacinto Dávila (jacinto.davila@gmail.com), Yacson Ramirez (yacson.ramirez@gmail.com).
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
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
        new utilidades();
        seleccionarIdioma();
        new utilidades().limpiarPantalla();
        final File carpeta = new File("minery/networks");
        final File carpeta2 = new File("minery/integration");
        Scanner lectura = new Scanner(System.in);
        boolean r = true;

        listar_datos();

        while (r) {
            ArrayList<String> redes = new ArrayList<>();
            redes = listarCarpetas(carpeta);
            new utilidades().limpiarPantalla();
            System.out.println(utilidades.colorTexto1+utilidades.titulo);
            System.out.println(utilidades.colorReset);
            System.out.println(utilidades.idioma.get(0));
            if (redes.size() > 0) {
                System.out.println(utilidades.idioma.get(1));
                for (int i = 0; i < redes.size(); i++) {
                    System.out.println((i + 1) + ".- " + redes.get(i));
                }
            }

            ArrayList<String> redesInte = new ArrayList<>();
            redesInte = listarCarpetas(carpeta2);

            if (redesInte.size() > 0) {
                System.out.println("\n" + utilidades.idioma.get(2));
            }

            //System.out.println("N.-Crear una nueva Red");
            System.out.println(utilidades.idioma.get(3));
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

        final File carpeta = new File("data");

        ArrayList<String> redes = new ArrayList<>();

        redes = listarCarpetas(carpeta);

        for (String red : redes) {
            File dir_red = new File("minery/networks/" + red);

            if (!dir_red.exists()) {
                dir_red.mkdir();
            }

            ArrayList<String> proteinas = new ArrayList<>();
            final File carpeta2 = new File("data/" + red);
            proteinas = listarCarpetas(carpeta2);

            for (String proteina : proteinas) {
                File dir_prot = new File("minery/networks/" + red + "/" + proteina);
                if (!dir_prot.exists()) {
                    dir_prot.mkdir();
                }
            }
        }
    }

    private void listarRedesInt(ArrayList<String> redesInt) {

        Scanner lectura = new Scanner(System.in);
        boolean r = true;

        while (r) {
            new utilidades().limpiarPantalla();
            System.out.println(utilidades.colorTexto1+utilidades.titulo);
            System.out.println(utilidades.colorReset);
            
            System.out.println(utilidades.idioma.get(0));
            System.out.println(utilidades.idioma.get(1));
            for (int i = 0; i < redesInt.size(); i++) {
                System.out.println((i + 1) + ".- " + redesInt.get(i));
            }

            System.out.println("\n" + utilidades.idioma.get(4));
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
        String ruta = "minery/integration/" + red;
        Scanner lectura = new Scanner(System.in);
        boolean r = true;
        String resp;
        consultasJPL RRG = new consultasJPL();

        while (r) {
            ArrayList<pathway> patrones = RRG.cargarPatrones(ruta);
           new utilidades().limpiarPantalla();
            System.out.println(utilidades.colorTexto1+utilidades.titulo);
            System.out.println(utilidades.colorReset);
            
            System.out.println(utilidades.idioma.get(5) + " " + red);
            System.out.println(utilidades.idioma.get(6) + "       " + patrones.size() + "\n");
            System.out.println(utilidades.idioma.get(0));
            System.out.println(utilidades.idioma.get(7));
            System.out.println(utilidades.idioma.get(8));
            System.out.println("\n" + utilidades.idioma.get(4));

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
        File file = new File("minery/networks/" + nombreRed);
        file.mkdir();
        return nombreRed;
    }

    private void listarProcesos(String red) {

        final File carpeta = new File("minery/networks/" + red);
        Scanner lectura = new Scanner(System.in);
        boolean r = true;

        while (r) {
            ArrayList<String> procesos = new ArrayList<>();
            procesos = listarCarpetas(carpeta);
            new utilidades().limpiarPantalla();
            System.out.println(utilidades.colorTexto1+utilidades.titulo);
            System.out.println(utilidades.colorReset);
            
            System.out.println(utilidades.idioma.get(9) + " " + red);
            System.out.println(utilidades.idioma.get(0));
            if (procesos.size() > 0) {
                System.out.println(utilidades.idioma.get(10));

                for (int i = 0; i < procesos.size(); i++) {
                    System.out.println((i + 1) + ".- " + procesos.get(i));
                }
            }
            if (procesos.size() > 1) {
                System.out.println("\n" + utilidades.idioma.get(11));
            }
            // System.out.println("\nN.-Crear un nuevo proceso");
            System.out.println(utilidades.idioma.get(4));
            String resp = lectura.nextLine();

            for (int i = 0; i < procesos.size(); i++) {
                String r2 = (i + 1) + "";
                if (resp.equals(r2)) {
                    proceso = procesos.get(i);
                    // System.out.println("seleccion = " + i + " " + procesos.get(i));
                    String ruta = "minery/networks/" + red + "/" + proceso;
                    String rutaD = "data/" + red + "/" + proceso;
                    pipeline(ruta, rutaD);

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

        File file = new File("minery/integration/" + red);
        file.mkdir();

        String rutaDest = "minery/integration/" + red;

        for (String directorio : procesos) {
            System.out.print(utilidades.idioma.get(12) + " " + directorio);
            String rutaOri = "minery/networks/" + red + "/" + directorio;
            integrarArchivos(rutaOri + "/minedObjects.pl", rutaDest + "/minedObjects.pl");
            integrarArchivos(rutaOri + "/pathwaysObjects.pl", rutaDest + "/pathwaysObjects.pl");
            integrarArchivos(rutaOri + "/ontologyGO.pl", rutaDest + "/ontologyGO.pl");
            integrarArchivos(rutaOri + "/ontologyMESH.pl", rutaDest + "/ontologyMESH.pl");
            integrarArchivos(rutaOri + "/wellKnownRules.pl", rutaDest + "/wellKnownRules.pl");

            integrarOntologias(rutaOri, rutaDest);

            integrarObjExp(rutaOri, rutaDest);
            integrarFT(rutaOri, rutaDest);

            System.out.println(" ...ok");
        }
        try {
            System.out.print(utilidades.idioma.get(13));
            new GeneradorBC().generadorBCIntg(red, true);
            new GeneradorBC().generadorBCIntg(red, false);
            System.out.println(" ...ok");
        } catch (StringIndexOutOfBoundsException ex) {
            Logger.getLogger(confGeneral.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(confGeneral.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void integrarObjExp(String ori, String dest) {
        objetos_Experto objExt = new objetos_Experto();
        ObjectContainer db = Db4o.openFile(ori + "/homologousObjects.db");
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
                ObjectContainer db2 = Db4o.openFile(dest + "/homologousObjects.db");
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
        ObjectContainer db = Db4o.openFile(ori + "/TF.db");
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
                ObjectContainer db2 = Db4o.openFile(dest + "/TF.db");
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
        ObjectContainer db = Db4o.openFile(ori + "/minedObjectsOntology.db");
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

                ObjectContainer db2 = Db4o.openFile(dest + "/minedObjectsOntology.db");

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
            ObjectContainer db = Db4o.openFile(dest + "/ontologyMESH.db");
            try {
                db.store(ont);
                //System.out.println("Guardando: " + ont.getNombre() + " " + ont.getMESH());
            } catch (Exception e) {
                //System.out.println("Error al guardar en ontologyGO.db...");
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

            ObjectContainer db = Db4o.openFile(dest + "/ontologyGO.db");
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

    public void pipeline(String ruta, String rutaD) throws StringIndexOutOfBoundsException {
        new utilidades().limpiarPantalla();
        System.out.println(utilidades.colorTexto1+utilidades.titulo);
        System.out.println(utilidades.colorReset);
        
        utilidades.proceso=ruta;
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
                System.out.println("\n-------------------------\n" + utilidades.idioma.get(14) + "\n-------------------------");
                System.out.println("\n" + utilidades.idioma.get(15) + "\n");

                String regProm = config.IngresarRegionPromotora();
                float conf = config.IngresarConfiabilidad();
                int cantObjs = config.ingresarCantComplejos();
                int iteraciones = config.ingresar_numIteraciones();
                //boolean GO = config.buscarGO();
                //boolean MESH = config.buscarMESH();
                boolean MESH = true;
                boolean GO = true;
                String rutaPMidExp = rutaD + "/" + config.PMidExperto();
                int cantPMID = config.ingresar_cantPubMedId(); //numero de pubmed IDs
                boolean nombreCorto = config.nombresCortos();
                //fin de menu
                //-------------------------------------------------------------------------------------------------------------

                // crea una carpeta nueva 'mineria' donde se guardaran diferentes archivos generados durante el proceso .. si ya existe esta carpeta se eliminara con todos su contenido y se creara de nuevo vacia
                //mfts.crearCarpeta("mineria");
                //se guarda los datos de configuracion que se ingresaron el el menu anterior en mineria/config.db
                config.guardarConfiguracion(regProm, iteraciones, cantObjs, conf, GO, MESH, nombreCorto, cantPMID, rutaPMidExp, ruta);

                //este metodo ejecuta el proceso de busqueda de informacio desde objetos del experto, homologos y los objetos encontrados en los diferentes niveles de busqueda
                mfts.minado(regProm, conf, iteraciones, cantObjs, GO, MESH, config, ruta, rutaD);

                //este metodo genera todas las combinaciones de objetos encontrados en el proceso anterior y guarda las ombinaciones en 'mineria/combinations.db'
                new combinaciones().generar_combinaciones(false, config, ruta, nombreCorto);

                //este metodo toma el archivo de combinaciones anterior y procede a buscar PubMed IDs que resulten de cada combinacion guarda los IDs en 'mineria/PubMedId.db'
                new PubMed_IDs().buscar(cantPMID, config, ruta);

                //este metodo toma la el archivo de PubMed Ids y procede a hacer la busqueda abstracts
                //y crear una coleccion de archivos con extencion html en el directorio 'abctracts'
                new lecturas_PM().BusquedaPM_Abstracts("abstracts", 500, config, ruta); // Número máximo de abstracts por archivo

                //este metodo toma la imformacion minada tanto de los objetos minados como de las ontologias y la vacia en formato prolog
                //crea los archivos 'objetosMinados.pl' , ontologyGO.pl, ontologyMESH.pl , wellKnownRules.pl
                mfts.vaciar_bc_pl(GO, MESH, config, ruta);

                //este metodo llama al resumidor_bioinformante hace uso de la coleccion de abstracts
                new Resumidor().resumidor(config, ruta);

                // crea la bace de conocimiento con el listado de eventos encontrados por el resumidor
                String kb = new GeneradorBC().generadorBC("kBase.pl", config, ruta);

                // se crea el archivo 'mineria/pathwaysObjects.pl' haciendo uso de los objetos que se encontran en la base de conocimiento y la informacion en las ontologias
                new objetos_patrones().generar_archivo(config, ruta);

                //String kb = "kBase.pl";
                //new Razonador().inferir_patrones(kb, config);
                new patrones().inferir_patrones(config, ruta);
                
            } catch (Exception ex) {
                Logger.getLogger(confGeneral.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (config.reiniciar(ruta)) {
            //reinia el proceso de mineria 
            mfts.crearCarpeta(ruta);
            config = new configuracion();
            pipeline(ruta, rutaD);
        } else {
            //se reanuda desde el punto donde se marco el ultimo checklist
            config.reanudar_proceso(ruta, rutaD);
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

    public void seleccionarIdioma() {
        
        Scanner lectura = new Scanner(System.in);
        boolean r = true;
        File carpeta = new File("language");
        
        while (r) {
            new utilidades().limpiarPantalla();
            System.out.println(utilidades.colorTexto1+""+utilidades.titulo);
            ArrayList<String> idiomas = new ArrayList<>();
            idiomas = listarCarpetas(carpeta);
            System.out.println(utilidades.colorReset);
            if (idiomas.size() > 0) {
                
                for (int i = 0; i < idiomas.size(); i++) {
                    System.out.println((i + 1) + ".- " + idiomas.get(i).toUpperCase());
                }
            }
            
            String resp = lectura.nextLine();

            for (int i = 0; i < idiomas.size(); i++) {
                String r2 = (i + 1) + "";
                if (resp.equals(r2)) {
                    String rlanguage="language/"+idiomas.get(i)+"/language.xml";
                    new utilidades().lenguaje(rlanguage);
                    r=false;
                }
            }
            
            
        }
        
    }

    

    public String getRed() {
        return red;
    }

    public String getProceso() {
        return proceso;
    }
}
