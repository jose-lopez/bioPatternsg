/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import EDU.purdue.cs.bloat.decorate.Main;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author yacson
 */
public class BioPattern {

    public static void main(String[] args) throws Exception {
        BioPattern bioPattern = new BioPattern();
        bioPattern.pruebas();
        
        
    }

    private void autenticarProxy(String proxy_IP, String proxy_Port) {

        System.setProperty("http.proxyHost", proxy_IP);
        System.setProperty("http.proxyPort", proxy_Port);
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {

                JTextField jtf = new JTextField();
                JPasswordField jpf = new JPasswordField();
               // if (JOptionPane.showConfirmDialog(null, new Object[]{jtf, jpf}, "Clave:", JOptionPane.OK_CANCEL_OPTION) == 0) {

                    String usuario = "yacson.ramirez"; 
                            //jtf.getText();
                    char[] clave = {'Y','a','c','s','o','N','3','2','8','7'};
                    //jpf.getPassword;
                    return new PasswordAuthentication(usuario, clave);
              //  } else {
                //    System.exit(0);
                  //  return null;
               // }
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
