 /*
 BioPattern.java


 Copyright (C) 2016.
 Jose Lopez (jlopez@unet.edu.ve), Yackson Ramirez (yackson.ramirez), Didier Rubio (didier.rubio@gmail.com).

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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author jose-lopez
 */
public class BioPattern {

    private String regionPromotora = "";
    private String secuenciaProblema = "";

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

    public BioPattern() {
    }

    public static void main(String[] args) throws Exception {

        BioPattern biopattern = new BioPattern(args[0], args[1]);

        biopattern.pipelineBioPattern(args[0], args[1], args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]), "abstracts", true);
        //biopattern.pipelineBioPatternRP(args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), true);
        //  biopattern.pruebas();

    }

    public Region pipelineBioPattern(String rutaSecProb, String rutaRegPromSecProb, String confiabilidad, int cantPromotores, int cant_compl_p, int num_iteraciones, String fileAbstID, boolean criterio) throws IOException, Exception {

        //Autenticación de proxy        
        autenticarProxy("150.187.65.3", "3128");
        // Búsqueda de regiones promotoras de los mejores homologos para la secuencia problema.
        String rutaRegsPromotorasHomolgs = new HomologosBlast().regionesPromotsHomolgs(rutaSecProb, cantPromotores);

        // Se reciben la region promotora de la secuencia problema y la de sus homólogas y se obtienen los bloques consenso que ellas comparten.
        //String rutabloquesConsenso = new Alineador(rutaRegPromSecProb, rutaRegsPromotorasHomolgs).alineadorClustalw();

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
        //*
        float conf = Float.parseFloat(confiabilidad);  //confiabilidad de las busquedas en tfbind

        
        Minado_FT MFT = new Minado_FT();
        MFT.minado(rutaRegPromSecProb, cant_compl_p, criterio, conf, num_iteraciones);
        //*
        Busqueda_PubMed bpm = new Busqueda_PubMed();
        bpm.busqueda_IDs(MFT.getListaFT(), MFT.getLista_homologos(), true);
        //genero abstracts
        //nuevo archivo.html
        //nombre del archivo local donde va, es la entrada que debe pasar html
        String abstracts = new lecturas_PM().BusquedaPM_Abstracts(bpm.getListaIDs(), fileAbstID);
        bpm.limpiar_men();
        //*/

        //*
         // Se reciben los abstracts descargados y se devuelve el archivo de oraciones SVC necesario para
         // construir la BC con la que se haran inferencias para deducir patrones de regulacion.
        
         //String abstracts = "abstracts_salida.txt";
         //String abstracts = "abstracts_CYP7A1_salida.txt";
         String oracionesSVC = new Resumidor().resumidor(abstracts);

         String base_conocimiento = new GeneradorBC().generador(oracionesSVC);

         // Se infieren los distintos patrones de regulacion para la secuencia problema.
         Razonador razonador = new Razonador();
         ArrayList<String> patrones = razonador.inferir_patrones(base_conocimiento);

        //*/
        Region region_promotora = new Region(this.regionPromotora);

        region_promotora.constructPromotor(MFT.getListaFT());
        region_promotora.imprimirRegRegulacion("listadoFTs.txt");
        return region_promotora;

    }

    public Region pipelineBioPatternRP(String rutabloquesConsenso, String confiabilidad, int cantPromotores, int cant_compl_p, int num_iteraciones, boolean criterio) throws IOException {

        //Autenticación de proxy        
        autenticarProxy("150.187.65.3", "3128");
        // Búsqueda de regiones promotoras de los mejores homologos para la secuencia problema.
        //String rutaRegsPromotorasHomolgs = new HomologosBlast().regionesPromotsHomolgs(rutaSecProb, cantPromotores);

        // Se reciben la region promotora de la secuencia problema y la de sus homólogas y se obtienen los bloques consenso que ellas comparten.
        //String rutabloquesConsenso = new Alineador(rutaRegPromSecProb, rutaRegsPromotorasHomolgs).alineadorClustalw();
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
        Minado_FT MFT = new Minado_FT();
        MFT.minado(rutabloquesConsenso, cant_compl_p, criterio, conf, num_iteraciones);

        //Busqueda_PubMed bpm = new Busqueda_PubMed();
        //bpm.busqueda_IDs(MFT.getListaFT(),MFT.getLista_homologos(),true);
        //Se pasa el listado de PubMed Ids y se devuelve el archivo que contiene todos los abstracts descargados desde PubMed. 
        //String abstracts = new lecturas_PM().BusquedaPM_Abstracts(bpm.getListaIDs(), fileAbstID);
        //bpm.limpiar_men();
        //*
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
         String oracionesSVC = new Resumidor().resumidor(abstracts);

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
        region_promotora.constructPromotor(MFT.getListaFT());
        return region_promotora;

    }

    private void autenticarProxy(String proxy_IP, String proxy_Port) {

        System.setProperty("http.proxyHost", proxy_IP);
        System.setProperty("http.proxyPort", proxy_Port);
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                /*JTextField jtf = new JTextField();
                 JPasswordField jpf = new JPasswordField();
                 if (JOptionPane.showConfirmDialog(null, new Object[]{jtf, jpf}, "Clave:", JOptionPane.OK_CANCEL_OPTION) == 0) {

                 //String usuario = jtf.getText();
                 //char[] clave = jpf.getPassword();*/
                String usuario = "jlopez";
                char[] clave = {'K', 'i', 't', 'e', 's', '0', '8', '.'};

                return new PasswordAuthentication(usuario, clave);
                /*} else {
                 System.exit(0);
                 return null;
                 }*/
            }
        });

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

    public void pruebas() throws IOException {

        //Autenticación de proxy        
        //autenticarProxy("150.187.65.3", "3128");
        Minado_FT mft = new Minado_FT();
        System.out.println(mft.busquedaEnsemblGenID("SAMD11"));
//        //rutaarchivo, cantcomplejos , criterio busqueda , confiabilidad tfbind , num iteraciones       
//        mft.minado("bloquesConsenso", 1, true, 0.99f, 1);
//        Busqueda_PubMed BPM = new Busqueda_PubMed();
//        BPM.busqueda_IDs(mft.getListaFT(),mft.getLista_homologos());
//        String abstracts = new lecturas_PM().BusquedaPM_Abstracts(BPM.getListaIDs());
    }
}
