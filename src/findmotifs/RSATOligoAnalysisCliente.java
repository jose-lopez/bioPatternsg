/*
    RSATOligoAnalysisCliente.java


    Copyright (C) 2016.
    Jose Lopez (jlopez@unet.edu.ve).

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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package findmotifs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.BufferedReader;


import RSATWS.OligoAnalysisRequest;
import RSATWS.OligoAnalysisResponse;
import RSATWS.RSATWSPortType;
import RSATWS.RSATWebServicesLocator;

/**
 *
 * @author SauloDM
 */
public class RSATOligoAnalysisCliente {
     String motifsContenido;
     String fastaSeq;
     String nameOrganismo;
     String rutaServer;
     String rutaServerO;
     
    public RSATOligoAnalysisCliente(){
        /* Obtener la localización del Servicio */
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        
        try{
             fastaSeq="";
             fastaSeq=leerArchivo("secufasta.stam");
             System.out.println("*****--------------------*****");
             System.out.println("Archivo con Formato Fasta");
             System.out.println(fastaSeq);
             System.out.println("*****--------------------*****");
            nameOrganismo = "Saccharomyces_cerevisiae";
            System.out.print("Ingrese Nombre del Organismo: ");
            
            String nameOrganismo = bufferRead.readLine();
            // Fin de leer el archivo generado, para la variable fastaSeq
            
            RSATWebServicesLocator service = new RSATWebServicesLocator();
            RSATWSPortType proxy = service.getRSATWSPortType();
            /* preparar los parameters */
	    OligoAnalysisRequest parameters = new OligoAnalysisRequest();
            parameters.setSequence(fastaSeq);
            //Nombre del organismo a consultar
	    //parameters.setOrganism("Saccharomyces_cerevisiae");
            // Longitud de los parametros a hacer discubiertos
	    parameters.setLength(6);
	    //Nombre del Organismos a hacer consultado
	    parameters.setOrganism(nameOrganismo);
            //         
	    parameters.setStats("occ,proba,rank");
	    String[] lth_values = {"occ_sig 1"};
	    parameters.setLth(lth_values);
            
            /* LLamando al Servicio */
            System.out.println("*****------------------*****");
	    System.out.println("Llamando RSAT server...");
	    OligoAnalysisResponse res = proxy.oligo_analysis(parameters);
            /* Resultado del Proceso  */  
            //Reporte Comando remote
            System.out.println("*****------------------*****");
            System.out.println("Comando usado en el server:"+ res.getCommand());
            //Reporte de resultados
	    System.out.println("Oligo(s) Discubierto :\n"+ res.getClient());
            //Reporte del archivo localizado en el Server            
            System.out.println("Archivo de Resultados en el server::\n"+ res.getServer());
            //
            rutaServerO=res.getServer();
            System.out.println("*****------------------*****");
            rutaServer = "http://rsat.ulb.ac.be/"+rutaServerO.substring(18);
            System.out.println("Ruta en RSATWS:\n "+ rutaServer);
            System.out.println("*****------------------*****");
            //
            // Guardamos en un archivo local
            FileWriter fichero = null;
            PrintWriter pw = null;
            try
            {
               fichero = new FileWriter("motifs.stam");
               pw = new PrintWriter(fichero);
                        
               pw.println(res.getClient());
               System.out.println("Cerrando archivo motifs");
                        
            }    
            catch (Exception e) {
                   e.printStackTrace();
            }finally{
                try{
                    if (null != fichero)
                        fichero.close();
                }catch (Exception e2){
                }
            }             
            // Primer Try
       /*     }
        catch(Exception e) { System.out.println(e.toString()); 
	    } */
            }catch(Exception e3){
                e3.printStackTrace();
            }
    }
    
    public String getArchivo( String ruta ){
        String MotifsLinea;
        FileWriter fileMotifs = null;
        PrintWriter fM = null;
        //    
        FileReader fr = null;
        BufferedReader br = null;
        //Cadena de texto donde se guardara el contenido del archivo
        String contenido = "";
        System.out.println("Archivo a Procesar. \n"+ruta);
        try
        {
            //ruta puede ser de tipo String o tipo File
            fr = new FileReader( ruta );
            br = new BufferedReader( fr );
            //
            fileMotifs = new FileWriter("motifsFind.stam");
            fM = new PrintWriter(fileMotifs);
            //
            String linea;
            //Obtenemos el contenido del archivo linea por linea
            while( ( linea = br.readLine() ) != null ){ 
                contenido += linea.substring(0, linea.indexOf("\t")) + "\n";
                MotifsLinea = linea.substring(0, linea.indexOf("\t"));
            fM.println(MotifsLinea);    
            }
            
        }catch( Exception e ){  }
        //finally se utiliza para que si todo ocurre correctamente o si ocurre 
        //algun error se cierre el archivo que anteriormente abrimos
        finally
        {
            try{
                br.close();
                fM.close();
                System.out.println("Archivo con los Motivos Descubiertos en motifsFind.stam \n");
            }catch( Exception ex ){}
        }
        return contenido;
    }
    
    public String leerArchivo(String rutaFile){
       
        //BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
                   
            // Leemos el archivo generado, para la variable fastaSeq
            FileReader fr = null;
            BufferedReader br = null;
            //Cadena de texto donde se guardara el contenido del archivo
            String fastaSeq = "";
            
            try
            {
                //ruta puede ser de tipo String o tipo File "secufasta.stam"
                fr = new FileReader(rutaFile);
                br = new BufferedReader( fr );
                
                String linea;
                //Obtenemos el contenido del archivo linea por linea
                while( ( linea = br.readLine() ) != null ){ 
                    fastaSeq += linea + "\n";
                    
                }
                
            }catch( Exception e ){  }
            //finally se utiliza para que si todo ocurre correctamente o si ocurre 
            //algun error se cierre el archivo que anteriormente abrimos
            finally
            {
            try{
                br.close();
            }catch( Exception ex ){}
            }
        
        
           return fastaSeq;
    }
    
}
