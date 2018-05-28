/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import EDU.purdue.cs.bloat.decorate.Main;
import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.collections.ActivatableArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.DocFlavor;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import org.jpl7.Query;

/**
 *
 * @author yacson
 */
public class BioPattern {

    private String regionPromotora = "";
    private String secuenciaProblema = "";

    public BioPattern() {
    }

    public static void main(String[] args) throws Exception {
        BioPattern biopattern = new BioPattern();
        //biopattern.pipelineBioPattern(args[0], args[1], args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]), "abstracts", true);
        //biopattern.pipelineBioPatternRP(args[1], args[2], Integer.parseInt(args[4]), Integer.parseInt(args[5]));        //biopattern.pruebas();
        biopattern.pipelineBioPattern();
        //biopattern.pruebas();
    }

    public BioPattern(String secuenciaP, String regionP) throws FileNotFoundException, IOException {

        File secuenciaProb = new File(secuenciaP);
        File regionReguladora = new File(regionP);

        BufferedReader secProblema = new BufferedReader(new FileReader(secuenciaProb));
        BufferedReader regReguladora = new BufferedReader(new FileReader(regionReguladora));

        while (secProblema.ready()) {

            this.secuenciaProblema = this.secuenciaProblema + secProblema.readLine();

        }

        while (regReguladora.ready()) {

            this.regionPromotora = this.regionPromotora + regReguladora.readLine();

        }

    }

    public Region pipelineBioPatternRP(String regionPromotora, String confiabilidad, int cant_compl_p, int num_iteraciones) throws IOException {

        //Autenticación de proxy        
        autenticarProxy("150.187.65.3", "3128");
        // Búsqueda de regiones promotoras de los mejores homologos para la secuencia problema.
        //String rutaRegsPromotorasHomolgs = new HomologosBlast().regionesPromotsHomolgs(rutaSecProb, cantPromotores);

        // Se reciben la region promotora de la secuencia problema y la de sus homólogas y se obtienen los bloques consenso que ellas comparten.
        //String rutaSecuenciaProb = new Alineador(rutaRegPromSecProb, rutaRegsPromotorasHomolgs).alineadorClustalw();
        // Recibe una lista de Bloques Consenso y genera lista de factores de transcripcion con sus complejos proteinicos caracteristicas y ligandos correspondientes.
        //confiabilidad de las busquedas en tfbind
        // Recibe una lista de Bloques Consenso y genera lista de factores de transcripcion con sus complejos proteinicos caracteristicas y ligandos correspondientes.
        //

        /* En el siguiente juego de instruccions se itera por niveles para minar pathways segun un criterio definido por el usuario.
         * En este caso mediante un numero fijo de iteraciones. Varios niveles de busqueda se requieren para hallar pathways. En
         * esta etapa del pipeline se devuelve una "lista de listas" de PubMed Ids, cada lista en la lista 
         * contiene los PubMed IDs de cada iteracion ejecutada.
         * Esa lista de listas se pasa a la siguiente etapa del pipeline, donde se procede a descargar desde PubMed
         * Los abstracts correspondientes.
         * La clase Busqueda_PubMed posee dos listas criticas: listas_PubMed, ya mencionada, y listas_FTs.
         * listas_FTs contendra todos los objetos minados en todas las iteraciones; tambien una lista por iteracion.
         * Estos se emplearan mas adelante para organizar regiones promotoras.
         */
        float conf = Float.parseFloat(confiabilidad);  //confiabilidad de las busquedas en tfbind
        boolean buscarOntologiaGO = false;
        boolean buscarOntologiaMESH = false;
        int cantPMID = 20;
        // Recibe una lista de Bloques Consenso y genera lista de factores de transcripcion con sus complejos proteinicos caracteristicas y ligandos correspondientes.
        minado_FT mfts = new minado_FT();
        //ruta de archivo, confiabilidad, N Iteraciones, N de objetos
        mfts.minado(regionPromotora, conf, num_iteraciones, cant_compl_p, buscarOntologiaGO, buscarOntologiaMESH, new configuracion());
        mfts.obtenerFT();

        Region region_promotora = new Region(this.regionPromotora);
        region_promotora.constructPromotor();
        return region_promotora;

    }

    public Region pipelineBioPattern(String rutaSecProb, String rutaRegPromSecProb, String confiabilidad, int cantPromotores, int cant_compl_p, int num_iteraciones, String fileAbstID, boolean criterio) throws IOException, Exception {

        //Autenticación de proxy        
        autenticarProxy("150.187.65.3", "3128");
        // Búsqueda de regiones promotoras de los mejores homologos para la secuencia problema.
        //String rutaRegsPromotorasHomolgs = new HomologosBlast().regionesPromotsHomolgs(rutaSecProb, cantPromotores);

        // Se reciben la region promotora de la secuencia problema y la de sus homólogas y se obtienen los bloques consenso que ellas comparten.
        //String rutabloquesConsenso = new Alineador(rutaRegPromSecProb, rutaRegsPromotorasHomolgs).alineadorClustalw();
        // Recibe una lista de Bloques Consenso y genera lista de factores de transcripcion con sus complejos proteinicos caracteristicas y ligandos correspondientes.

        /* En el siguiente juego de instruccions se itera por niveles para minar pathways segun un criterio definido por el usuario.
         * En este caso mediante un numero fijo de iteraciones. Varios niveles de busqueda se requieren para hallar pathways. En
         * esta etapa del pipeline se devuelve una "lista de listas" de PubMed Ids, cada lista en la lista 
         * contiene los PubMed IDs de cada iteracion ejecutada.
         * Esa lista de listas se pasa a la siguiente etapa del pipeline, donde se procede a descargar desde PubMed
         * Los abstracts correspondientes.
         * La clase Busqueda_PubMed posee dos listas criticas: listas_PubMed, ya mencionada, y listas_FTs.
         * listas_FTs contendra todos los objetos minados en todas las iteraciones; tambien una lista por iteracion.
         * Estos se emplearan mas adelante para organizar regiones promotoras.
         */
        configuracion config = new configuracion();
        /*
         float conf = Float.parseFloat(confiabilidad);  //confiabilidad de las busquedas en tfbind
         boolean buscarOntologiaGO = true;
         boolean buscarOntologiaMESH = true;
         // Recibe una lista de Bloques Consenso y genera lista de factores de transcripcion con sus complejos proteinicos caracteristicas y ligandos correspondientes.
         minado_FT mfts = new minado_FT();
         //ruta de archivo, confiabilidad, N Iteraciones, N de objetos
         mfts.minado(regionPromotora, conf, num_iteraciones, cant_compl_p, buscarOntologiaGO, buscarOntologiaMESH);
         mfts.obtenerFT();
         busquedaPubMed_IDs BPM = new busquedaPubMed_IDs();
         ArrayList<String> listaPMid = BPM.busqueda_IDs(false, 10, false, config); // cantidad de abstracts a descargar para cada combinación
         //ArrayList<String> listaPMid =  BPM.consulta_PudMed(1000);
         try {
         new lecturas_PM().BusquedaPM_Abstracts(listaPMid, "abstracts", 500, config); // Número máximo de abstracts por archivo
         } catch (Exception ex) {
         Logger.getLogger(BioPattern.class.getName()).log(Level.SEVERE, null, ex);
         }
         /*/

        new Resumidor().resumidor(config);

        String base_conocimiento = new GeneradorBC().generadorBC("baseC.pl", config);

        //Se infieren los distintos patrones de regulacion para la secuencia problema.
        new Razonador().inferir_patrones("baseC.pl", config);

        Region region_promotora = new Region(this.regionPromotora);

        //region_promotora.constructPromotor(MFT.getListaFT());
        return region_promotora;

    }

    public void pipelineBioPattern() throws StringIndexOutOfBoundsException, Exception {
        //Autenticación de proxy        
        //autenticarProxy("150.187.65.3", "3128");

        minado_FT mfts = new minado_FT(); // clase que contiene los metodos donde se buscara la informacion de los objetos minados

        configuracion config = new configuracion(); // clase donde se guarda la informacion de configuracion inicial del proceso de minado y los diferentes checklist que indican desde donde continuar la ejecucion

        try {
            config.recuperarConfiguracion(); // recupera la configuracion actual y el checklist que indica desde que punto puede continuar la ejecucion
        } catch (Exception e) {
        }

        if (config.getRegionPromotora() == null) {

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
            String rutaPMidExp = config.PMidExperto();
            int cantPMID = config.ingresar_cantPubMedId(); //numero de pubmed IDs
            //fin de menu
            //-------------------------------------------------------------------------------------------------------------

            // crea una carpeta nueva 'mineria' donde se guardaran diferentes archivos generados durante el proceso .. si ya existe esta carpeta se eliminara con todos su contenido y se creara de nuevo vacia
            mfts.crearCarpeta("mineria");

            //se guarda los datos de configuracion que se ingresaron el el menu anterior en mineria/config.db
            config.guardarConfiguracion(regProm, iteraciones, cantObjs, conf, GO, MESH, cantPMID, rutaPMidExp);

            //este metodo ejecuta el proceso de busqueda de informacio desde objetos del experto, homologos y los objetos encontrados en los diferentes niveles de busqueda
            mfts.minado(regProm, conf, iteraciones, cantObjs, GO, MESH, config);

            //este metodo genera todas las combinaciones de objetos encontrados en el proceso anterior y guarda las ombinaciones en 'mineria/combinaciones.db'
            new combinaciones().generar_combinaciones(false, config);

            //este metodo toma el archivo de combinaciones anterior y procede a buscar PubMed IDs que resulten de cada combinacion guarda los IDs en 'mineria/PubMedId.db'
            new PubMed_IDs().buscar(cantPMID, config);

            //este metodo toma la el archivo de PubMed Ids y procede a hacer la busqueda abstracts 
            //y crear una coleccion de archivos con extencion html en el directorio 'abctracts'
            new lecturas_PM().BusquedaPM_Abstracts("abstracts", 500, config); // Número máximo de abstracts por archivo

            //este metodo toma la imformacion minada tanto de los objetos minados como de las ontologias y la vacia en formato prolog
            //crea los archivos 'objetosMinados.pl' , ontologiaGO.pl, ontologiaMESH.pl , well_know_rules.pl
            mfts.vaciar_bc_pl(GO, MESH, config);

            //este metodo llama al resumidor_bioinformante hace uso de la coleccion de abstracts 
            new Resumidor().resumidor(config);

            // crea la bace de conocimiento con el listado de eventos encontrados por el resumidor
            String kb = new GeneradorBC().generadorBC("baseC.pl", config);

            // se crea el archivo 'mineria/objetos_patrones.pl' haciendo uso de los objetos que se encontran en la base de conocimiento y la informacion en las ontologias
            new objetos_patrones().generar_archivo(config);

            //String kb = "baseC.pl";
            //new Razonador().inferir_patrones(kb, config);
            new patrones().inferir_patrones(config);

        } else if (config.reiniciar()) {
            //reinia el proceso de mineria 
            mfts.crearCarpeta("mineria");
            config = new configuracion();
            pipelineBioPattern();
        } else {
            //se reanuda desde el punto donde se marco el ultimo checklist
            config.reanudar_proceso();
        }

    }

    public void setRegionPromotora(String regionPromotora) {
        this.regionPromotora = regionPromotora;
    }

    public void setSecuenciaProblema(String secuenciaProblema) {
        this.secuenciaProblema = secuenciaProblema;
    }

    public String getRegionPromotora() {
        return regionPromotora;
    }

    public String getSecuenciaProblema() {
        return secuenciaProblema;
    }
    private String usuario = "";
    private char[] clave;
    // private char[] clave = {'', '', '', '', '', '', '', '', '', ''};

    private void autenticarProxy(String proxy_IP, String proxy_Port) {

        System.setProperty("http.proxyHost", proxy_IP);
        System.setProperty("http.proxyPort", proxy_Port);
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                if (usuario.equals("")) {
                    JTextField jtf = new JTextField();
                    JPasswordField jpf = new JPasswordField();
                    if (JOptionPane.showConfirmDialog(null, new Object[]{jtf, jpf}, "Clave:", JOptionPane.OK_CANCEL_OPTION) == 0) {
                        usuario = jtf.getText();
                        clave = jpf.getPassword();
                        return new PasswordAuthentication(usuario, clave);
                    } else {
                        System.exit(0);
                        return null;
                    }
                }
                return new PasswordAuthentication(usuario, clave);
            }
        });
    }

    public void pruebas() {

        minado_FT mfts = new minado_FT(); // clase que contiene los metodos donde se buscara la informacion de los objetos minados
        configuracion config = new configuracion();
        try {
            config.recuperarConfiguracion(); // recupera la configuracion actual y el checklist que indica desde que punto puede continuar la ejecucion
        } catch (Exception e) {
        }

        new PubMed_IDs().buscar(10, config);

    }

}
