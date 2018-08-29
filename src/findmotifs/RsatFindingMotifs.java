///*
//    .java
//
//
//    Copyright (C) 2016.
//    Jose Lopez (jlopez@unet.edu.ve).
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU Affero General Public License as
//    published by the Free Software Foundation, either version 3 of the
//    License, or (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU Affero General Public License for more details.
//
//    You should have received a copy of the GNU Affero General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>
//
//*/
//
//
//package findmotifs;
//
//import findmotifs.ADN;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.Authenticator;
//import java.net.PasswordAuthentication;
//import java.util.List;
//import java.util.Set;
//import java.util.TreeMap;
//import javax.swing.JOptionPane;
//import javax.swing.JPasswordField;
//import javax.swing.JTextField;
//import uk.ac.roslin.ensembl.config.DBConnection;
//import uk.ac.roslin.ensembl.config.EnsemblCoordSystemType;
//import uk.ac.roslin.ensembl.config.FeatureType;
//import uk.ac.roslin.ensembl.dao.database.DBCollectionSpecies;
//import uk.ac.roslin.ensembl.dao.database.DBRegistry;
//import uk.ac.roslin.ensembl.dao.database.DBSpecies;
//import uk.ac.roslin.ensembl.datasourceaware.DAXRef;
//import uk.ac.roslin.ensembl.datasourceaware.compara.DAHomologyPairRelationship;
//import uk.ac.roslin.ensembl.datasourceaware.core.DAChromosome;
//import uk.ac.roslin.ensembl.datasourceaware.core.DACoordinateSystem;
//import uk.ac.roslin.ensembl.datasourceaware.core.DAExon;
//import uk.ac.roslin.ensembl.datasourceaware.core.DAGene;
//import uk.ac.roslin.ensembl.datasourceaware.core.DATranscript;
//import uk.ac.roslin.ensembl.exception.ConfigurationException;
//import uk.ac.roslin.ensembl.exception.DAOException;
//import uk.ac.roslin.ensembl.exception.NonUniqueException;
//import uk.ac.roslin.ensembl.model.Mapping;
//
//public class RsatFindingMotifs {
//    
//        String nombre_especie;
//        String gen_id;
//        String dBversion;
//        ADN objAdn;
//        
//        public RsatFindingMotifs(String nombreE, String genID, String dBv){
//            nombre_especie = nombreE;
//                    gen_id = genID;
//                    dBversion = dBv;
//        }
//
//    public static void main(String[] args) throws Exception {
//        
//        new RsatFindingMotifs("cow", "ENSBTAG00000021527", "68").buscarHomologos();
//        
//        /*
//        
//        System.out.println("-------------Genes-------------");
//        
//        System.out.print("Ingrese el nombre de la especie: ");
//        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
//        nombre_especie = bufferRead.readLine();
//        
//        System.out.print("Ingrese el ID del gen: ");
//        gen_id = bufferRead.readLine();
//        
//        System.out.print("Ingrese la version de la base de datos: ");
//        dBversion = bufferRead.readLine();
//
//        objAdn = new ADN(nombre_especie, gen_id, dBversion);
//        //objAdn = new ADN("cow", "ENSBTAG00000021527", "68");
//        objAdn.getInformacionEspecie();
//        
//        System.out.println("");
//        //System.out.println("---------------------------------------");
//        System.out.println("");
//        objAdn.getInformacionGen();
//        
//        System.out.println("");
//        //System.out.println("---------------------------------------");
//        System.out.println("");
//        objAdn.getInformacionHomologo();
//        
//                    
//        RSATOligoAnalysisCliente objOli = new RSATOligoAnalysisCliente();
//        String contenidoMotifs=objOli.getArchivo("motifs.stam");
//        if (contenidoMotifs.length()>0){
//             System.out.println(contenidoMotifs);
//             RSATPeakMotifs objPeak = new RSATPeakMotifs();
//        }
//        else
//            System.out.println("No genero archivo motifs.stam");
//            */
//    }
//  
//    
//    public void buscarHomologos() throws IOException, ConfigurationException, DAOException, NonUniqueException{
//        
//        //Autenticación de proxy        
//        autenticarProxy("150.187.65.3", "3128");
//        
//        System.out.println("-------------Genes-------------");
//        
//        /*System.out.print("Ingrese el nombre de la especie: ");
//        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
//        nombre_especie = bufferRead.readLine();
//        
//        System.out.print("Ingrese el ID del gen: ");
//        gen_id = bufferRead.readLine();
//        
//        System.out.print("Ingrese la version de la base de datos: ");
//        dBversion = bufferRead.readLine();*/
//
//        objAdn = new ADN(nombre_especie, gen_id, dBversion);
//        //objAdn = new ADN("cow", "ENSBTAG00000021527", "68");
//        objAdn.getInformacionEspecie();
//        
//        System.out.println("");
//        //System.out.println("---------------------------------------");
//        System.out.println("");
//        objAdn.getInformacionGen();
//        
//        System.out.println("");
//        //System.out.println("---------------------------------------");
//        System.out.println("");
//        objAdn.getInformacionHomologo();
//        
//                    
//        RSATOligoAnalysisCliente objOli = new RSATOligoAnalysisCliente();
//        String contenidoMotifs=objOli.getArchivo("motifs.stam");
//        if (contenidoMotifs.length()>0){
//             System.out.println(contenidoMotifs);
//             RSATPeakMotifs objPeak = new RSATPeakMotifs();
//        }
//        else
//            System.out.println("No genero archivo motifs.stam");
//    }
//    
//    private void autenticarProxy(String proxy_IP, String proxy_Port) {
//
//        System.setProperty("http.proxyHost", proxy_IP);
//        System.setProperty("http.proxyPort", proxy_Port);
//        Authenticator.setDefault(new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                JTextField jtf = new JTextField();
//                JPasswordField jpf = new JPasswordField();
//                if (JOptionPane.showConfirmDialog(null, new Object[]{jtf, jpf}, "Clave:", JOptionPane.OK_CANCEL_OPTION) == 0) {
//
//                    String usuario = jtf.getText();
//                    char[] clave = jpf.getPassword();
//
//                    return new PasswordAuthentication(usuario, clave);
//                } else {
//                    System.exit(0);
//                    return null;
//                }
//            }
//        });
//
//    }
//}
//// Para ejecutar ingresar
//// cow
//// ENSBTAG00000021527
//// 68