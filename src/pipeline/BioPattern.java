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
        //biopattern.pruebas(args[1], args[2], Integer.parseInt(args[4]), Integer.parseInt(args[5]));
        
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
        //ruta de archivo, confiabilidad, N Iteraciones, N de objetos
        mfts.minado(regionPromotora, conf, num_iteraciones, cant_compl_p); 
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
        float conf = Float.parseFloat(confiabilidad);  //confiabilidad de las busquedas en tfbind

        // Recibe una lista de Bloques Consenso y genera lista de factores de transcripcion con sus complejos proteinicos caracteristicas y ligandos correspondientes.
        minado_FT mfts = new minado_FT();
        //ruta de archivo, confiabilidad, N Iteraciones, N de objetos
        mfts.minado(regionPromotora, conf, num_iteraciones, cant_compl_p); 
        mfts.obtenerFT();
        busquedaPubMed_IDs BPM = new busquedaPubMed_IDs();
        ArrayList<String> listaPMid = BPM.busqueda_IDs(false, 10); // cantidad de abstracts a descargar para cada combinación
        //ArrayList<String> listaPMid =  BPM.consulta_PudMed(1000);
        try {
            new lecturas_PM().BusquedaPM_Abstracts(listaPMid, "abstracts", 500); // Número máximo de abstracts por archivo
        } catch (Exception ex) {
            Logger.getLogger(BioPattern.class.getName()).log(Level.SEVERE, null, ex);
        }  
        
        new Resumidor().resumidor();

        String base_conocimiento = new GeneradorBC().generadorBC("baseC.pl");

        //Se infieren los distintos patrones de regulacion para la secuencia problema.
        new Razonador().inferir_patrones("baseC.pl"); 
        
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

    private String usuario = "yacson.ramirez";
    private char[] clave={'Y','a','c','s','o','N','3','2','8','7'};

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

    public void pruebas(String regionPromotora, String confiabilidad, int cant_compl_p, int num_iteraciones) throws IOException, Exception {

        //Autenticación de proxy        
        autenticarProxy("150.187.65.3", "3128");
////        
        float conf = Float.parseFloat(confiabilidad);
        minado_FT mfts = new minado_FT();
        //Nueva mineria (true),ruta de archivo, confiabilidad, N Iteraciones, N de objetos, Criterio de busqueda, opcion para busqueda en HGNC (0: todos los mejores ramqueados, -1:solo el objeto con el mismo nombre, [1-n]: cantidad de espesifica de objetos HUGO)
        mfts.minado(regionPromotora, conf, num_iteraciones, cant_compl_p);
        mfts.obtenerFT();
        //busquedaPubMed_IDs BPM = new busquedaPubMed_IDs();
        //ArrayList<String> listaPMid = BPM.busqueda_IDs(false, 10);
        //ArrayList<String> listaPMid =  BPM.consulta_PudMed(1000);
        //new lecturas_PM().BusquedaPM_Abstracts(listaPMid, "abstracts", 500);
        
//        lecturas_QuickGO qgo = new lecturas_QuickGO();
//        qgo.obtenerOntologia("GO:0000800");
//        //ontologia.buscar("GO:0044237",null);

         ObjetosMinadosGO GO = new ObjetosMinadosGO();
         //GO.imprimirTodo(null,null);
         GO.vaciarOntologia_pl();
         //GO.buscar("CYP7A1", null);
          
        
        
        
    }

}
