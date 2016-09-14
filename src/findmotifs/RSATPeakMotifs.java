/*
    RSATPeakMotifs.java


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

import RSATWS.OligoAnalysisRequest;
import RSATWS.OligoAnalysisResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.BufferedReader;

import RSATWS.RSATWSPortType;
import RSATWS.RSATWebServicesLocator;
import RSATWS.PeakMotifsRequest;
import RSATWS.PeakMotifsResponse;

/**
 *
 * @author SauloDM
 */
public class RSATPeakMotifs {
    String fastaSeq;
    public RSATPeakMotifs(){
        /* Obtener la localización del Servicio */
         BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try{           
            // Leemos el archivo generado, para la variable fastaSeq
            FileReader fr = null;
            BufferedReader br = null;
            //Cadena de texto donde se guardara el contenido del archivo
            String fastaSeq = "";
            try
            {
                //ruta puede ser de tipo String o tipo File
                System.out.println("Obteniendo la Secuencia en formato FASTA\n");
                fr = new FileReader( "secufasta.stam" );
                br = new BufferedReader( fr );
 
                String linea;
                //Obtenemos el contenido del archivo linea por linea
                while( ( linea = br.readLine() ) != null ){ 
                    fastaSeq += linea + "\n";
                }
                System.out.println(fastaSeq);
                
            }catch( Exception e ){  }
            //finally se utiliza para que si todo ocurre correctamente o si ocurre 
            //algun error se cierre el archivo que anteriormente abrimos
            finally
            {
            try{
                br.close();
            }catch( Exception ex ){}
            }
            // Primer Try
            /* Obtener la localización del service */
            RSATWebServicesLocator service = new RSATWebServicesLocator();
            RSATWSPortType proxy = service.getRSATWSPortType();
            
            PeakMotifsRequest parameters = new PeakMotifsRequest();
                    	            
	    /* Preparar los parameters */
            //secuencia :
            parameters.setTest(fastaSeq);
	    String tmpControlinfile = "";
            parameters.setTmp_control_infile(tmpControlinfile);
            String Salida= "both";
            parameters.setOutput(Salida);
            parameters.setMax_length(10000);
            parameters.setMax_motif_number(10);
            parameters.setTop_peaks(5);
            parameters.setMin_length(6);
            parameters.setMax_length(8);
            parameters.setMarkov(2);
            parameters.setGraph_title("Bilateria");
            parameters.setImage_format("png");
            parameters.setDisco("oligos,dyads,positions");
            
            
            //
            System.out.println("*****------------------*****");
            System.out.println("Llamando al RSAT server...Peak_Motifs");	    
            PeakMotifsResponse res = proxy.peak_motifs(parameters);
            System.out.println("*****------------------*****");
            /* Process results  */  
	    //Report the remote command
	      System.out.println("Comando usado sobre el server Peak_Motifs:"+ res.getCommand());
	    //Report the result
	      System.out.println("Oligo(s) Descubiertos Peak_Motifs:\n"+ res.getClient());
	    //Report the server file location
	      System.out.println("Archivo de Resultado en el server Peak_Motifs:\n"+ res.getServer());
              System.out.println("*****------------------*****");
            // Primer Try
            }
        catch(Exception e) { System.out.println(e.toString()); 
	    }
    }
    
}
