/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import EDU.purdue.cs.bloat.decorate.Main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

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
        biopattern.pipelineBioPattern(args[0], args[1], args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]), "abstracts", true);
        //biopattern.pipelineBioPatternRP(args[0], args[1], args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]), true);
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

    public Region pipelineBioPatternRP(String rutaSecuenciaProb, String regionPromotora, String confiabilidad, int cantPromotores, int cant_compl_p, int num_iteraciones, boolean criterio) throws IOException {

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

        // Recibe una lista de Bloques Consenso y genera lista de factores de transcripcion con sus complejos proteinicos caracteristicas y ligandos correspondientes.
        minado_FT mfts = new minado_FT();
        //ruta de archivo, confiabilidad, N Iteraciones, N de objetos, Criterio de busqueda, opcion para busqueda en HGNC (0: todos los mejores ramqueados, -1:solo el objeto con el mismo nombre, [1-n]: cantidad de espesifica de objetos HUGO)
        mfts.minado(regionPromotora, conf, num_iteraciones, cantPromotores, criterio, 1);
        mfts.obtenerFT();
        busquedaPubMed_IDs BPM = new busquedaPubMed_IDs();
        ArrayList<String> listaPMid = BPM.busqueda_IDs(false, 10);
        //ArrayList<String> listaPMid =  BPM.consulta_PudMed(1000);
        try {
            new lecturas_PM().BusquedaPM_Abstracts(listaPMid, "abstracts", 500);
        } catch (Exception ex) {
            Logger.getLogger(BioPattern.class.getName()).log(Level.SEVERE, null, ex);
        }

        /* Busqueda_PubMed bm = new Busqueda_PubMed();
         bm.listas_PubMed_Ids(listaFT, num_iteraciones, MFT.getObjetos_mineria(), cant_compl_p, criterio); // Se itera y se definen listas_PubMed y listas_FTs.
         ArrayList<ArrayList> listas_PubMed = bm.get_listas_PubMed();
         ArrayList<ArrayList> listas_FTs = bm.get_listas_FTs();*/
        // String abstracts = new Resumidor().abstracts_PubMed(listas_PubMed);
        /*
         Busqueda_PubMed bm= new Busqueda_PubMed();
         bm.listas_PubMed_Ids(listaFT, num_iteraciones,MFT.getObjetos_mineria(),cant_compl_p, criterio); // Se itera y se definen listas_PubMed y listas_FTs.
         ArrayList<ArrayList> listas_PubMed = bm.get_listas_PubMed();
         ArrayList<ArrayList> listas_FTs = bm.get_listas_FTs();
                       
            
         // Se pasa el listado de PubMed Ids y se devuelve el archivo que contiene todos los abstracts descargados desde PubMed.
         String abstracts = new Resumidor().abstracts_PubMed(listas_PubMed);
        
         // Se reciben los abstracts descargados y se devuelve el archivo de oraciones SVC necesario para
         // construir la BC con la que se haran inferencias para deducir patrones de regulacion.
         String oracionesSVC = new Resumidor().consultResumidor(abstracts);

         String base_conocimiento = new GeneradorBC().generador(oracionesSVC);

         // Se infieren los distintos patrones de regulacion para la secuencia problema.
         Razonador razonador = new Razonador();
         ArrayList<String> patrones = razonador.inferir_patrones(base_conocimiento);

         /*
         * ***********************
         */

 /*ArrayList<ArrayList> listas_FTs = new ArrayList<>();
         listas_FTs.add(listaFT);*/
        //this.regionPromotora = "AGGTACCTTCTCCCCCATTGTAGAGAAAAGTGAAGTTCTTTTAGAGCCCCGTTACATCTTCAAGGCTTTTTATGAGATAATGGAGGAAATAAAGAGGGCTCAGTCCTTCTACTGTCCATATTTCATTCTCAAATCTGTTATTAGAGGAATGATTCTGATCTCCACCTACCATACACATGCCCTGTTGCTTGTTGGGCCTTCCTAAAATGTTAGAGTATGATGACAGATGGAGTTGTCTGGGTACATTTGTGTGCATTTAAGGGTGATAGTGTATTTGCTCTTTAAGAGCTGAGTGTTTGAGCCTCTGTTTGTGTGTAATTGAGTGTGCATGTGTGGGAGTGAAATTGTGGAATGTGTATGCTCATAGCACTGAGTGAAAATAAAAGATTGTATAAATCGTGGGGCATGTGGAATTGTGTGTGCCTGTGCGTGTGCAGTATTTTTTTTTTTTTAAGTAAGCCACTTTAGATCTTGTCACCTCCCCTGTCTTCTGTGATTGATTTTGCGAGGCTAATGGTGCGTAAAAGGGCTGGTGAGATCTGGGGGCGCCTCCTAGCCTGACGTCAGAGAGAGAGTTTAAAACAGAGGGAGACGGTTGAGAGCACACAAGCCGCTTTAGGAGCGAGGTTCGGAGCCATCGCTGCTGCCTGCTGATCCGCGCCTAGAGTTTGACCAGCCACTCTCCAGCTCGGCTTTCGCGGCGCCGAGATGCTGTCCTGCCGCCTCCAGTGCGCGCTGGCTGCGCTGTCCATCGTCCTGGCCCTGGGCTGTGTCACCGGCGCTCCCTCGGACCCCAGACT";
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
        //boolean criterio = true;//criterio de busqueda genenames true = aplica criterio , false busca por la etiqueta completa
        /*float conf = Float.parseFloat(confiabilidad);  //confiabilidad de las busquedas en tfbind
        // Recibe una lista de Bloques Consenso y genera lista de factores de transcripcion con sus complejos proteinicos caracteristicas y ligandos correspondientes.
        //
        Minado_FT MFT = new Minado_FT();
        MFT.minado(rutaRegPromSecProb, cant_compl_p, criterio, conf, num_iteraciones);

        Busqueda_PubMed bpm = new Busqueda_PubMed();
        bpm.busqueda_IDs(MFT.getListaFT(),MFT.getLista_homologos(),false);
       //genero abstracts
       //nuevo archivo.html
       //nombre del archivo local donde va, es la entrada que debe pasar html
        String abstracts = new lecturas_PM().BusquedaPM_Abstracts(bpm.getListaIDs(), fileAbstID);
        bpm.limpiar_men();

        
        // Se reciben los abstracts descargados y se devuelve el archivo de oraciones SVC necesario para
        // construir la BC con la que se haran inferencias para deducir patrones de regulacion.
        
        */
        //String abstracts = "abstracts-BAXS-ascenso.html";
        
        //String abstracts = "abstracts.html"; */
        
        //new Resumidor().resumidor();

        String base_conocimiento = new GeneradorBC().generadorBC("baseC.pl");

        // Se infieren los distintos patrones de regulacion para la secuencia problema.
        //new Razonador().inferir_patrones(base_conocimiento);        

       //*/
        
        Region region_promotora = new Region(this.regionPromotora);
        //region_promotora.constructPromotor(MFT.getListaFT());
        return region_promotora;


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

    public void pruebas() throws IOException, Exception {

        //Autenticación de proxy        
        autenticarProxy("150.187.65.3", "3128");
//      Minado_FT mft = new Minado_FT();
//      //System.out.println(mft.busquedaEnsemblGenID("SAMD11"));
//      //rutaarchivo, cantcomplejos , criterio busqueda , confiabilidad tfbind , num iteraciones       
//      mft.minado("bloquesConsenso", 1, true, 0.99f, 1);
//      Busqueda_PubMed BPM = new Busqueda_PubMed();
//      BPM.busqueda_IDs(mft.getListaFT(),mft.getLista_homologos(),false);
//      String abstracts = new lecturas_PM().BusquedaPM_Abstracts(BPM.getListaIDs(),"pruebaAbs.txt");
//    

        minado_FT mfts = new minado_FT();
        //Nueva mineria (true),ruta de archivo, confiabilidad, N Iteraciones, N de objetos, Criterio de busqueda, opcion para busqueda en HGNC (0: todos los mejores ramqueados, -1:solo el objeto con el mismo nombre, [1-n]: cantidad de espesifica de objetos HUGO)
        mfts.minado("bloquesConsenso", 0.97f, 2, 5, true, 0);
        mfts.obtenerFT();
        busquedaPubMed_IDs BPM = new busquedaPubMed_IDs();
        ArrayList<String> listaPMid = BPM.busqueda_IDs(false, 10);
        //ArrayList<String> listaPMid =  BPM.consulta_PudMed(1000);
        new lecturas_PM().BusquedaPM_Abstracts(listaPMid, "abstracts", 500);

    }

}
