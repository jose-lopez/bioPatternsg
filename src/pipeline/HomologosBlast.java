 /*
    HomologosBlast.java


    Copyright (C) 2016.
    Didier Rubio (didier.rubio@gmail.com), Jose Lopez (jlopez@unet.edu.ve).

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

import findmotifs.ADN;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;
import uk.ac.roslin.ensembl.config.DBConnection;
import uk.ac.roslin.ensembl.dao.database.DBRegistry;
import uk.ac.roslin.ensembl.exception.ConfigurationException;
import uk.ac.roslin.ensembl.exception.DAOException;
import uk.ac.roslin.ensembl.exception.NonUniqueException;

/**
 *
 * @author jose-lopez
 */
public class HomologosBlast {

    public String[] vectores, ident;
    public String adn = "";
    public PrintWriter es, hs, es1;
    public Vector<String> des;
    public boolean ban = false;
    public static int cantPromotores;

    public static String rutaSecProb = "";
    public static String rutaRegPromSecProb = "";
    public static String rutaRegsPromotorasHomolgs = "";
    public static String rutaRGsProms = "";

    public void buscarH(String secuencia) {

        try {

            String command;

            command = "blastx -query " + secuencia + " -out salidaBlast -db pdbaa -task blastx";
            final Process r = Runtime.getRuntime().exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(r.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            if (r.waitFor() != 0) {
                String Texto = new BufferedReader(new InputStreamReader(r.getErrorStream())).readLine();
                System.out.println(Texto);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public String comprobarHomologos(int cantHomologos) {

        BufferedReader le = null, le1 = null;
        DBRegistry eReg;
        vectores = new String[cantHomologos];
        ident = new String[cantHomologos];
        int regionesHalladas = 0;
        int homologosHallados = 0;
        String[] genDisplayNameRegionPromotor = new String[2];
        try {

            le = new BufferedReader(new FileReader(new File("salidaBlast_IDs")));
            le1 = new BufferedReader(new FileReader(new File("salidaBlast_des")));
            String linea = "", v[], pdbID = "", linea1 = "", regionPromotora, idFasta;
            eReg = DBRegistry.createRegistryForDataSourceCurrentRelease(DBConnection.DataSource.ENSEMBLDB);
            FileWriter s = new FileWriter(new File("regiones_promotoras_homolgs"));
            FileWriter h = new FileWriter(new File("homologos"));
            es = new PrintWriter(s);
            hs = new PrintWriter(h);
            while (le.ready()) {
                linea = le.readLine();
                linea1 = le1.readLine();
                if (!linea.equals("") && !linea1.equals("")) {
                    v = linea.split("-");
                    pdbID = v[1];
                    genDisplayNameRegionPromotor = jEnsemblRegionPromotora("human", pdbID, "72", eReg); // Halla region promotora para pdbID o vacio si no existe.
                    if (!genDisplayNameRegionPromotor[1].equals("")) {
                        //idFasta = "> " + pdbID + " " + linea1; // Esta línea permite reportar la salida en formato FASTA.
                        //es.println(idFasta);
                        es.println(genDisplayNameRegionPromotor[1]);
                        regionesHalladas++;

                    } else {
                        System.out.println("No existe region promotora para " + pdbID + "\n");
                    }
                    if (!genDisplayNameRegionPromotor[0].equals("")) {
                        //idFasta = "> " + pdbID + " " + linea1; // Esta línea permite reportar la salida en formato FASTA.
                        //es.println(idFasta);
                        hs.println(genDisplayNameRegionPromotor[0]);
                        homologosHallados++;

                    } else {
                        System.out.println("No existe Ensembl Gen Name para:  " + pdbID + "\n");
                    }
                }

            }

            es.close();
            hs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("regiones promotoras halladas: " + regionesHalladas);
        System.out.println("Homologos hallados: " + homologosHallados);
        return "regiones_promotoras_homolgs";
    }
    
    private String getBioDbNetEnsemblGenId(String pdbId) throws MalformedURLException, IOException {
 
        URL urlpagina;
        InputStreamReader isr;
        BufferedReader br;
        String linea, ensemblGenId= "";

        //boolen ensemblGenId = false;
        try {
            String url = "http://biodbnet.abcc.ncifcrf.gov/webServices/rest.php/biodbnetRestApi?method=db2db&input=pdbid&inputValues="+pdbId+"&outputs=ensemblgeneid,affyid&taxonId=9606&format=row";
            urlpagina = new URL(url);
            isr = new InputStreamReader(urlpagina.openStream());
            br = new BufferedReader(isr);
            while ((linea = br.readLine()) != null) {
                int indexIni=linea.indexOf("ENS");
                if (linea.indexOf("ENS")>0) {
                    //ensemblGenId = linea.replaceAll(" +", "").replace("<BR>", "").replace("/", "").replace("EnsemblGeneID", "");
                    int indexFin = linea.indexOf(",");
                    ensemblGenId=linea.substring(indexIni, indexFin-1);
                    break;
                }

            }
            br.close();
            isr.close();

        } catch (MalformedURLException e) {
            JOptionPane.showMessageDialog(null, "Error en la url bioDbNet");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al leer el archivo");
        }
        
        return ensemblGenId;
    }

    private String[] jEnsemblRegionPromotora(String especie, String pdbID, String dbVersion, DBRegistry eReg) throws ConfigurationException, DAOException, NonUniqueException, IOException {
        
        
        // Metodo manual para pruebas
        //String region = promotorManual(pdbID);
        
        // Metodo usando la API JEnsembl.
        String[] genNameRegionPromot= new String[2];
        
        // Se consulta BioDbNet para obtener el Ensembl Gene Id correspondiente al homologo pdbID.
        
        String ensemblGenId = getBioDbNetEnsemblGenId(pdbID);
        
        ADN objAdn = new ADN(especie, ensemblGenId, dbVersion, eReg);
        genNameRegionPromot[0] = objAdn.gen.getDisplayName();
        
        // Se empla la Api JEnsembl para obtener la region promotora relativa al gen ensemblGenId.
        genNameRegionPromot[1]=objAdn.getInformacionRegPromotor("600", "200");
        
        return genNameRegionPromot;

    }
    
    private String promotorManual(String id){
        
        String region = "";

        switch (id) {

            case "4I8V":
                region = "CGCGAACCTCAGCTAGTCGCCCGGGCTCTGGGGGACAGGTCCAGCCCCGCGGCGCCTCTGGCCTTCCGGCCCCCGTGACCTCAGGGCTGGGGTCGCAGCGCTTCTCACGCGAGCCGGGACTCAGTAACCCCGGGAAGGAGGTCACCACGGGGCAGCCCCGCCCCCGCCTGCCGAGTCCTGGTAGGCTGTAGCGCTGGGGAGGCATCTGCACGCCCAGCGTTCCAGTGGGTGCAAAAATGACGAAGAGGAGTCCCCGCGCCCCAGGATGGAGCTTCCCGTACCCTCTCTTCGGGCTGTCCTGGGACTTCTCCCTCAAGCCCCCTCCTCGGCTGGGTTCTGCACTGCCCTTGGGACGCCTTGGAATTGGGACTTCCAGGTGTTCCCAGCCCTCACCCCTCTATGTACAGGCACCGAGATGTGTCCCATAGTGGGTTCTTGCCCACCCGACCCCCCACCCCCGCCGCCCTCCGCCACCTTTCTCTCCAATCCCAGAGAGACCAGCCCGGTTCAGGCTGCTTCTCCCTCCATCTCAGCTCGCTCCAGGGAAGGAGGCGTGGCCACACGTACAAGCCCGCCTATAAAGGTGGCAGTGCCTTCACCCTCACCCTGAAGGTGACAGTTCCTTGGAACCTTCCCTGATCCTTGTGATCCCAGGCTCCAAGAGTCCACCCTTCCCAGCTCAGCTCAGTACCTCAGGTGAGTTGCTGGGGGACTTCTGGCTTGCCCTTTCTCTCCCAATAAAAGGAACATTTTGGTGCCTCCAGGACTTCTTAGGTAGCTACCTGTCTAGCACCTCCAAA";
                break;
            case "2HI4":
                region = "TTAGGGTGATGAACCAGTTGTTGCTTGGAGATCCAGAGGGGATGAAAGGCGCGGGGTGAAGACGCCCTGCGTTCTCCTGGATTGGTTGGGCAGGGCTTCCAAGGGCCGATGAAAGCTGGAGGAACTGGGCTCAAAGTGTCTTCTTGCCTTTCCCCGGAGGAAGCTGTGGACAGGTCGGGAAGGTAGTAGTGGGGGATCTTTCCCTCCGGAGTCCTGGGTCATCGAGCCCTCAACCCAACTCGACTCTCCTGAACCTTAATGGAGACTCTCTAGGATTGGAAGTTGCTATGGCAACGAAGCTCCCAGAATACTGAGGACAACTCAGGGCTAGAGAGGCGTCACCCTCTTGTACCCCCACTTCCGACCCACGGATAAAGTGGCGCCCCCTACGCACAGGAACCGTCCCTGGGCCCCCGCCCCGGCGCGGAGACCACCGTGGACCTCCGGGGGCGGGAGGCCGCTCTGGCCCCAAGCGGAGCCTCTCGTTTGTAGACGGGCCCGAGGGAGCCTCTCTGGGCGCAGCGCCGCTGAGCTGCTCGATGCTCCAGCCGGAAGTGCTCTTCACCGCCTGCCTCCCACCCGCCTCTCTCCGGGGTCAAGATGGCGGCTGCCGCGGAGTGCGATGTGGTAATGGCGGCGACCGAGCCAGAGCTGCACGACGACGGGGAAGCGAAGAGGTAGCGGGGTAGTGGGGGAGTGGAAGGGGTGAACTTCAGCTCCGTGACCCCGGGAAACCCGTCACTGCCATGATGCAGCCCCTGTTGAGGCGATGCCCCGCCCTCCGCGACCCCCACAACTGA";
                break;
            case "2PG5":
                region = "ACAAACATGCGTATGGTATGTCCTGATCCAGAGTTCTCCCGGCATTAAAGTGGTTCTCTCCTTCAACTTCTTCATTAAGAGATACAATGACAGAATGTCATTAAAATTAAAAACTTCTGCTCTGCAAAGGAATTGTCAATGGACTGACTCTGTATGTCCATTCATGTCTCGTTGTGTCCTAGACTGACTCTGATTGGGCCACAGTGGCCTTACCTGGTAGCGAGCTCAGATTACACCCTGGTGTTCAAAGCGTGCCGAGTAGGAAAGTGTGCAGAGAAGGCAGGACAGCCGCCCGCCAGGTCTCTGGAAAACCCCCCAGGACAGGGATGTTCAGGCCATGTGATGCTATACTAAGCTATTTTACGTGAGGAAGGGGTGCTTTTTAAGAATCTATCTAGAGGGCTATGGGCACAGATCTGTGTTCAAAGATTTTCTGGGCACTTACGTTTTGTGAGAAATCAGATAGAGACAACTTTCTGAGCCGCAGCTTTGTCACGTGTAAAATGGGCATGAACGCTTTTCCCCTTCGTAGAGGTTTGTGGCAATTAAGTGAGGTGAGCGTGCAACGCTTTTTGAACAAAGCTGACACGGAGGTACTTGTTCAGGAAATAAGAGCTGCTATTATTACTACTCTTTGTAGCATACTGTAAGGTTTCCCTTCCTCTCATCACAGCTCTGAAGGACATCATCAAGGTGAACTGAGCCCCTTCTGTTTCTTCTGTTCCCTCTAACCACCTCTCCCCTTCCTTCCCCCACTGCCCCCATTCTTAGCCCCGCCCCTCCCACAAGCCCCGCCCCTG";
                break;
            case "2PG7":
                region = "TCTGGGAGCACCTGCTGGGCTTGCTACACACTCCACCTCCCAGAAACTCCACACCCACAGCCCTGGGTCTTCCTAGCCCCAAGACTTTCAAGTCCATATGCCTGGAATCCCCCGTCCTGAGACCCTTAACCCTGCATCCTCCACAACAGAAGACCCCCAGATGCACAGCCACACTTCCATCTCACCCTAGTAAAACCCAGACCTTTGGATTCCTCTCCCGTGGAATGCCCAAATCCACAACTTTGGGGTGCATTCTCACTCCCAGATCCCAAATCCAAAGTCCAGGTGCTCCCCTGTGCAAATATTCCAAACTCCTCAGTTCCACAGTTTATCTGTTGCCCGCTCCTAAATCCACAGCCCTGCAGCAACCCTCCTGAAGTACCAGATTTAGTCTGGAGTTCCCCTCCCTGTTCAGCTTCCCTGGGGTCCCTCTCCTCCTCCCTTGCTGGCTGTGTCCTAAGCTGTGTGGGATTCAGGGTTGGGGTGTAGTTGGGAGGTGAAATGAGGTGATTATATAATCAACCAAAGTCCATCCCTCTTTTTCAGGCAGTATAAAGGCAAACCACCCCAGCCATCACCATCTATCATCCCACTGCCACCATGCTGGCCTCAGGGCTGCTTCTGGTGACCTTGCTGGCCTGCCTGACTGTGATGGTCTTGATGTCAGTCTGGCGGCAGAGGAAGAGCAGGGGGAAGCTGCCTCCGGGACCCACCCCATTGCCCTTCATTGGAAACTACCTGCAGCTGAACACAGAGCAGATGTACAACTCCCTCATGAAGGTGTCCTAAGGCAGGGAGAT";
                break;
            case "1Z10":
                region = "CTGGGAGCACCTGCTGGCTTGCTACACACTCCTCCTCCCAGAAACTCCACACCCACAGCCCTGGGTCTTCCTAGCCCCGAGACTTTCAAGTCCATATGCCTGGAGTCCCCCCTCCTGAGACCCTTAACCCTGCATCCTCCGCAACAGAAGACCTCCAGATGCACAGCCACACTTCCATCTCACCCTAATAAAACCCAGACCTTTGGATTCCTCTCCCTTGGAATGCCCAAATCCACAACTTTGGGGTGCATTCTCACTCTCAGACCCCAAATCCAAAGCCCAAGTGCTCCCCTATGCAAATATTCCAAACTCTTCAGTTCTACAGTTTATCGGTTGCCCCCTCCTAAATCCACAGCCCTGCGGCACCCCTCCTGAAGTACCACAGATTTAGTCTGGAGGCCCCCTCTCTGTTCAGCTGCCCTGGGGTCCCCTTATCCTCCCTTGCTGGCTGTGTCCCAAGCTAGGTGGCATTCATGGTGGGGCGTGTAGTTGGGAGGTGAAATAAGGTGATTATGTAATTAGCCAAAGTCCATCCCTCTTTTTCAGGCAGTATAAAGGCAAACCACCCCACCCATCACCATCTGTCATCTCACTACCACCATGCTGGCCTCAGGGCTGCTTCTGGTGGCCTTGCTGGCCTGCCTGACTGTGATGGTCTTGATGTCTGTCTGGCAGCAGAGGAAGAGCAGGGGGAAGCTGCCTCCGGGACCCACCCCACTGCCCTTCATTGGAAACTACCTCCAGCTGAACACAGAGCACATATGTGACTCCATCATGAAGGTGTCCCAAGGCAGGAAGAT";
                break;
            case "1DT6":
                region = "CTGTAAGAACATGCCTTTGTATAAGCATAGTCATTGGTGATCTAGTAACGTTTATCTTTCAGTGAGATTTTGATTCTCATCAACACAGTGTCACCTCCTGTTCAGGTCAACAAGTTGTGGTTCTTTGAGCTGTGGGTCCATTGAATGACTTCCCTTCCTCTCCTCCTCTATTGTATCTGCAAGAGCATTCTTTATTATTTCCCTGAATTGCTGCATTATTTCACTTTTCATTTCAGACCACGACAGACCCAAACTAACAGAATCATCCTTGGCTGGAGCTGAACATGAGGTGAGGGTAGTTTTAGTTTCCCAAGGGTTTCCCTCAAATTTTCTGAGATTGTCTCTTTCCTTAGTTACACTGACTAAATTTTACCTCTACAGTGAATCAACTGGAAGAACTCTGTGTTTTTTCTGTTCGTGTGAGCACATGTAGTTTTTTATCCTTGAAGTCAATTAAAGGAGTTCCTATTGGGTGTTACGTCTGTTTCATTATGAACATAGTGTGTTGTTGATTGAGAGTCACGAATAGTTTAGGTTTCGTTGTTGAAGTTGCTCTGGAGACTGCCTGTGTGTAGGACATAGTACACAAAATGGACATCAGATCTCATAAAAATGCCCCAGAATAGACACTGTGTCGTGGTAGTTCAGCTGCTGCTTTTGACACAGGCATTCAATGTCAGAAGCTTGAGTTTGAGCCAGTGTATGCTAATCCTATACGTGGGAGCAAGAGATGGTTTCTCAGCTCTTGGAACTCTTTCACCCATGTGGTAAGCTGGATTGACTTCTGAGTTTCTGATTTG";
                break;
            case "1SUO":
                region = "GATCAGAGACCCTGGGGACCCGACGGGGCACCCTGCCAGCTCTGTCTCCTGCCCCCGCCCCTTCTGTTCCTGCCATTATTGCCGCTGTGGCTGTCACACAAGGGATGCTGGGAGCCGAGACACAGGAGGGATCGCCCTGAGCTTCTGGAGCCCCTAAAGAGTCTGGGGCCTGGAGGTGCCCAACACACCAGCACCCACACAACCCGCACATGGGAACCCCCTTCCCACGTAGGAAACACACGGGCACACGCAGAGGCGCTGGCATGTGGGACGGACACCGGCAGGCCTGGAAAACCCATGGGCCAAACAGAGTGGAGACAGACAAGTGCTCACTGCCTCAGTGGAAATAAACAGGCACTCACGTGCACGCTACACCCTCTACATCTCCCCCACATTTCCCTGCCAACATCCAAGAAGGGGAGGGGGCAGAGTGACGGGCAGTGAGCTGTCACTGGGCTTCCTGGGCAGGAAGTGTGTGTCCTCTGGCTCCCAAGTTCACACTGTGGTAATGAGGAGAGGAGAGGCGGGGACTGGGTGAGGTTGCATAACTGGGTGGGATCCAGGCTCAGGATAAAAGGCCCAGCGCGAGGAGGCTGCAGCAGGTGGACAGCTGACGGCCGGGACCATGGAGTTCAGCCTGCTCCTCCTCCTGGCTTTCCTCGCAGGCCTCCTGCTGCTTCTGTTCAGGGGCCACCCCAAGGCCCACGGCCGCCTCCCCCCGGGACCCTCCCCTCTGCCCGTCCTGGGGAACCTTCTGCAGATGGACAGGAAGGGCCTGCTCCGCTCCTTCCTGCGGGTGA";
                break;
            case "3TK3":
                region = "CAACACCATCTGCTCTGGCATCTGCCTGCCTTAGGTGGTGCCTGCTAATTACCTGGGGGGGGGAGTCAGGAAGAGGCTTGGGAACCCCCCTCCCCTCTCCCACCCCATCACACTCCTGGAGCTAGGAGTGGGGCACATGGGGGCCCTAAGAGCAAATGGGCCACAGCCTTCTTAGTGGATCAAGCTGTACTGGGAGGGTCAGTAAGGCCAGGAAAAGCCATTTAGTCAATAAATCTTTACTGGGCACTTACTGTGTGCCAGGCCCTGTTCTAGGTGGTTATTAAATCAGGTGGCAGCTGGCCTGGAATTGGCATTCTGGTGATGCGAGATGGACAAAAAAGGTATAGAAGTAACTGGAAATACCTTCGGGCAGACTGACAAATGCAATGGACAGAGAAATAGCACCCAGGAGGACAAATGAGTTTGGTGGTCGCAGAATGGACACTTCCTTATGAGGGGCTGCGGGAGTCACGCCGCAGGGGAAGGAGACGCAGCCATGCAAACAGCCTCCCCCGCTGCGCTTGTTTCCGAAAGACGCGGTGGGCAGGGGTCTCGGACAGGGCTCCTGCGGGGCGGGTGGGGGAGAAATTGGTGTAACGGCCACTTCCGGGCCCCTCGCTCCCACCCCCACCCCGGCTGCAGGCATTCATTCCTCCCGCCCCCGCTCGTGTGAGGGGTTGGAAAGGCCCGTGGGAAGGGCCAGGGGAGAGAGCCGCCGTGTGCCGGGGAGGCGGGGCTGGCGCACAGCTGGGCCACATCTGGCCCGAGCGCAGAGGGACCAAACGGGACAGAAGGTGGCC";
                break;
            case "1R9O":
                region = "TGTTTATATCTGCTAAGGTAATTTACTTGATATATGTTTGGTTATTTAAGATATATGAGTTATGTTAGCTATTTCATGTTTAGGCTGCTGTATTTTTAGTAGGCTATATTAAATATTTGAAAGGATTTCATTATAAAGAACAAAGTCTCCTAATCTTTGATATAGCATTGACATACTTTTTAAATATACAAGGCATAGAATATGGCCATTTCTGTTAAATCATATATTCCCAACTGGTTATTAATCTAAGAATTCAGAATTTTGAGTAATTGCTTTTGCATCAGATTATTTACTTCAGTGCTCTCAATTATGATGGTGCATTAGAACCATCTGGGTTAACATTTGTTTTTTATTACCAATACCTAGGCTCCAACCAAGTACAGTGAAACTGGAATGTACAGAGTGGACAATGGAACGAAGGAGAACAAGACCAAAGGACATTTTATTTTTATCTGTATCAGTGGGTCAAAGTCCTTTCAGAAGGAGCATATAGTGGACCTAGGTGATTGGTCAATTTATCCATCAAAGAGGCACACACCGAATTAGCATGGAGTGTTATAAAAGGCTTGGAGTGCAAGCTCATGGTTGTCTTAACAAGAAGAGAAGGCTTCAATGGATTCTCTTGTGGTCCTTGTGCTCTGTCTCTCATGTTTGCTTCTCCTTTCACTCTGGAGACAGAGCTCTGGGAGAGGAAAACTCCCTCCTGGCCCCACTCCTCTCCCAGTGATTGGAAATATCCTACAGATAGGTATTAAGGACATCAGCAAATCCTTAACCAATGTAAGTATGCTCCTTCAGTG";
                break;
            case "3E4E":
                region = "TTCCTTTTATTTTTCTTCCATGGAATTTTCCAGTTAACTTGAGAAAGTGGAATCGAATTCCGATGTTGAATTTTCCTTCTGGCCCCATTCATGTGGCAGGTGGTGATTCAGGTACTACTGGGGGCTGCTCAGACAAACCTCCTCATCAGACATCAAGAGGCTGTTGCACCAGGAGGGCCGGTACCGTGTCTAGAGGTGGTCGGCATGGGGTTGGAGTTGTATTACATAAACCCTACTCCAAACAAATGCATGGGGATGTGGCTGGAGTTCCCCGTTGTCTAACCAGTGCCAAAGGGCAGGACGGTACCTCACCCCACGTTCTTAACTATGGGTTGGCAACATGTTCCTGGATGTGTTTGCTGGCACAGTGACAGGTGCTAGCAACCAGGGTGTTGACACAGTCCAACTCCATCCTCACCAGGTCACTGGCTGGAACCCCTGGGGGCCACCATTGCGGGAATCAGCCTTTGAAACGATGGCCAACAGCAGCTAATAATAAACCAGTAATTTGGGATAGACGAGTAGCAAGAGGGCATTGGTTGGTGGGTCACCCTCCTTCTCAGAACACATTATAAAAACCTTCCGTTTCCACAGGATTGTCTCCCGGGCTGGCAGCAGGGCCCCAGCGGCACCATGTCTGCCCTCGGAGTCACCGTGGCCCTGCTGGTGTGGGCGGCCTTCCTCCTGCTGGTGTCCATGTGGAGGCAGGTGCACAGCAGCTGGAATCTGCCCCCAGGCCCTTTCCCGCTTCCCATCATCGGGAACCTCTTCCAGTTGGAATTGAAGAATATTCCCAAGTC";
                break;
            case "2MI1":
                //region = "AGGTACCTTCTCCCCCATTGTAGAGAAAAGTGAAGTTCTTTTAGAGCCCCGTTACATCTTCAAGGCTTTTTATGAGATAATGGAGGAAATAAAGAGGGCTCAGTCCTTCTACTGTCCATATTTCATTCTCAAATCTGTTATTAGAGGAATGATTCTGATCTCCACCTACCATACACATGCCCTGTTGCTTGTTGGGCCTTCCTAAAATGTTAGAGTATGATGACAGATGGAGTTGTCTGGGTACATTTGTGTGCATTTAAGGGTGATAGTGTATTTGCTCTTTAAGAGCTGAGTGTTTGAGCCTCTGTTTGTGTGTAATTGAGTGTGCATGTGTGGGAGTGAAATTGTGGAATGTGTATGCTCATAGCACTGAGTGAAAATAAAAGATTGTATAAATCGTGGGGCATGTGGAATTGTGTGTGCCTGTGCGTGTGCAGTATTTTTTTTTTTTTAAGTAAGCCACTTTAGATCTTGTCACCTCCCCTGTCTTCTGTGATTGATTTTGCGAGGCTAATGGTGCGTAAAAGGGCTGGTGAGATCTGGGGGCGCCTCCTAGCCTGACGTCAGAGAGAGAGTTTAAAACAGAGGGAGACGGTTGAGAGCACACAAGCCGCTTTAGGAGCGAGGTTCGGAGCCATCGCTGCTGCCTGCTGATCCGCGCCTAGAGTTTGACCAGCCACTCTCCAGCTCGGCTTTCGCGGCGCCGAGATGCTGTCCTGCCGCCTCCAGTGCGCGCTGGCTGCGCTGTCCATCGTCCTGGCCCTGGGCTGTGTCACCGGCGCTCCCTCGGACCCCAGACT";               
                region = "TGGGGATTAAAAGGTACATCCTAGAGTGTTATTGTCCGTGTAAAATAGGGCTGTGAAACCCCCTACACCTATGCACAGATAAACTGATGGCCCTGCAAATGCCCAGAGAAGTAACCTTATAAAGATTTAGTGAATGTTTCCCACCATTATTGGAAAATCTGGCTAAGTCAGTAGCTTTGTTTTCATATACTTAGGGGGAAAATCAACAAAAACTTGATGCCCTGTTATTTCCTTCCAGTTTCTTCTGCAACTCTAGCTCCAAAGACTGCAGTTCCTGCAGTATTTTTTTTTTTAGAACACCAGGAAAAAAAGAAAAAAAAACCATGCAAATGTACTATTTCTTCAAACACAACTAGGAATGATTGAACAGCTGGGAGAATATGAAGAAAACCCCTCAGAGAACCAAGGGCAAGCAGTGAGTTCAGATCTAGGCACATGGCCACTAGGAATGGTTCTTCACTGCCACGTGGGCAGCCAACAGCCAGTAGCTAGAGACCAGCCTCGGTCTTCGGCCTGCGGGTTCTGCAAAGTCAGGCTAGCTGGCTCTCCGCCTGCTCCGCACCCCGGCGAGGTTCCGGTGGGGAGGGGTAGGGATGGTTCAGCCCCGCCCCGCTAGGGCGGGGCCTGCGCCTGCGCGCTCAGCGGCCGGGCGTGTAACCCACGGGTGCGCGCCCACGACCGCCAGACTCGAGCAGTCTCTGGAACACGCTGCGGGGCTCCCGGGCCTGAGCCAGGTCTGTTCTCCACGCAGGTGTTCCGCGCGCCCCGTTCAGCCATGTCGTCCGGCATCCATGTAGCGC";
              break;
        }

        return region;
        
    }
    
    private String promotorJEnsembl(String especie, String id, String dbVersion, DBRegistry eReg) throws ConfigurationException, DAOException, NonUniqueException, IOException{
        
        // Se consulta BioDbNet para obtener el Ensembl Gene Id correspondiente al homologo pdbID.
        
        String ensemblGenId = getBioDbNetEnsemblGenId(id);
        
        ADN objAdn = new ADN(especie, ensemblGenId, dbVersion, eReg);
        
        // Se empla la Api JEnsembl para obtener la region promotora relativa al gen ensemblGenId.
        
        String region = getJEnsemblRegionPromotora(especie, ensemblGenId, dbVersion, "600", "200");
        
        return region;
        
    }
    
       
    private String getJEnsemblRegionPromotora(String especie, String ensemblGenId, String dbVersion, String limInf, String limSup) throws ConfigurationException, DAOException, NonUniqueException, IOException{
        
        
        ADN objAdn = new ADN(especie, ensemblGenId, dbVersion);
        objAdn.getInformacionEspecie();
        objAdn.getInformacionGen();
        String regionPromotora=objAdn.getInformacionRegPromotor(limInf, limSup);
            
        return regionPromotora;
        
        
        
        
        
    }






    public void prepararIDsH() {

        FileReader buf = null;
        des = new Vector<String>();
        try {
            buf = new FileReader(new File("salidaBlast"));
            FileWriter s = new FileWriter(new File("salidaBlast_IDs"));
            es = new PrintWriter(s);
            FileWriter ss = new FileWriter(new File("salidaBlast_des"));
            es1 = new PrintWriter(ss);
            BufferedReader lec = new BufferedReader(buf);
            String linea = "";
            String[] x;
            String[] y;
            String l;
            String base = "";
            String ID = "";
            String f = "";
            String[] separador;
            boolean ban = true;
            boolean ban1 = false;
            char va = 0;
            int con = 0, compS, p;
            double expectation;
            while (lec.ready()) {

                linea = lec.readLine();

                compS = linea.compareToIgnoreCase("");

                if (compS != 0) {

                    va = linea.charAt(0);

                    if (va == '>') {
                        break;
                    }
                }

                if ((con > 19) && (compS != 0)) {

                    x = linea.split("   ");

                    expectation = Double.parseDouble(x[(x.length - 1)]);

                    if (expectation >= (double) (0.0)) {

                        if (f.equalsIgnoreCase("e-")) {
                            //va = '>';
                            break;
                        }
                        y = x[0].split(" ");
                        String ax = "";
                        for (int i = 1; i < y.length - 1; i++) {
                            ax += y[i] + " ";
                        }
                        l = y[0];
                        
                        //separador = l.split("|");
                        base = l.substring(0, 3);
                        ID = l.substring(4, 8);
                        /*for (int i = 0; i < l.length(); i++) {
                            if (l.charAt(i) == '|') {
                                ban = false;
                                ban1 = true;
                            } else {
                                if (ban) {
                                    base += l.charAt(i);
                                }
                                if (ban1) {
                                    ID += l.charAt(i);
                                }
                            }
                        }*/
                        es.println(base + "-" + ID);
                        es1.println(ax);
                        base = ID = "";
                        ban = true;
                        ban1 = false;
                        //if(con==20)
                        //    break;
                    } else {
                        va = '>';
                        System.out.println(va + "::::::::::::::::");
                        break;
                    }
                }
                con++;
            }
            lec.close();
            buf.close();
            es.close();
            es1.close();
            s.close();
            ss.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public String regionesPromotsHomolgs(String rutaSecProb, int cantPromo) {

        buscarH(rutaSecProb);
        prepararIDsH();
        return comprobarHomologos(cantPromo);

    }

    public HomologosBlast() {

    }

}
