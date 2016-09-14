 /*
    Lecturatfbind.java


    Copyright (C) 2016.
    Yackson Ramirez (yackson.ramirez), Jose Lopez (jlopez@unet.edu.ve).

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

package pipeline;

import pipeline.Busqueda_PubMed;
import pipeline.lista_palabras;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Lecturatfbind {

    private String id;
    private String factor;
    private float porcentaje;
    private int numero;
    private String signo;
    private String cadena;
    //--------------------------------------
    private ArrayList<Lecturatfbind> lecturas = new ArrayList<>(); // Que finalidad tiene??. No tiene sentido un array de lecturas aqui.
    //ArrayList factores = new ArrayList();
    //private ArrayList<lista_palabras> listapalabras = new ArrayList<>();
    //private String ruta;

    //public ArrayList<lista_palabras> getListapalabras() {
    //    return listapalabras;
    // }
    //public void setListapalabras(ArrayList<lista_palabras> listapalabras) {
    //    this.listapalabras = listapalabras;
    //}
    public Lecturatfbind() {
    }

    public Lecturatfbind(String id, String factor, String cadena) {
      this.id = id;
      this.factor = factor;
      this.cadena = cadena;
}
public Lecturatfbind(String ruta,float confiabilidad) throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        /*System.out.printf("\nIngrese Confiabilidad: ");
        confiabilidad = Float.parseFloat(in.readLine());*/
        leer_de_archivo(ruta, confiabilidad);
       
    }

    private int in = 0;

    private void obtener_lecturas(String metodo, float confiabilidad, ArrayList<String> control_factores) throws MalformedURLException, IOException {
        int cont = 0;
        URL urlpagina;
        InputStreamReader isr;
        BufferedReader br;
        String linea, segmento;
        StringBuffer buffer = new StringBuffer();
        String[] separar;
        String factor;
        try {
            urlpagina = new URL("http://tfbind.hgc.jp/cgi-bin/calculate.cgi?seq=%3E+COMMENTS%0D%0A" + metodo);
            isr = new InputStreamReader(urlpagina.openStream());
            br = new BufferedReader(isr);
            while ((linea = br.readLine()) != null) {
                cont++;
                if (cont >= 7) {
                    segmento = linea.replaceAll(" +", "#").replace("<BR>", "").replace("</BODY>", "").replace("</HTML>", "");
                    separar = segmento.split("#");
                    if (separar.length > 1 && Float.parseFloat(separar[2]) >= confiabilidad) {

                        factor = separar[1].substring(separar[1].indexOf("$") + 1, separar[1].indexOf("_"));
                        if (!control_factores.contains(factor)) {
                            Lecturatfbind aux = new Lecturatfbind();
                            control_factores.add(factor);
                            aux.id = separar[0];
                            aux.factor = factor;
                            aux.porcentaje = Float.parseFloat(separar[2]);
                            aux.numero = Integer.parseInt(separar[3]);
                            aux.signo = separar[4];
                            aux.cadena = separar[5] + "  " + separar[6];
                            lecturas.add(aux);

                        }

                        System.out.print("#");
                        if (in % 100 == 0 && in != 0) {
                            System.out.println("");
                        }
                        in++;

                    }

                }

            }
            br.close();
            isr.close();

        } catch (MalformedURLException e) {
            JOptionPane.showMessageDialog(null, "Error en la url, ejemplo http://www.java-elrincondetucasa.blogspot.com");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al leer el archivo");
        }
    }

    public void imprimir(Lecturatfbind lectura) {
        System.out.println(lectura.id + "\t" + lectura.factor + "\t" + lectura.porcentaje + "\t" + lectura.numero + " " + lectura.signo + "\t" + lectura.cadena);
    }

    private void leer_de_archivo(String ruta, float confiabilidad) {

        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
            archivo = new File(ruta);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String metodo;
            System.out.println("");
            System.out.println("Leyendo bloques consenso desde "+ruta);
            ArrayList<String> control_factores = new ArrayList<>();
            while ((metodo = br.readLine()) != null) {
               
                System.out.println("buscando registros FT desde TFBIND para: " + metodo);
                obtener_lecturas(metodo, confiabilidad, control_factores);

            }
            System.out.println("registros tfbind completados para: " + ruta);

        } catch (Exception e) {
            e.printStackTrace();
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

    public ArrayList<Lecturatfbind> getLecturas() {
        return lecturas;
    }

    public String getFactor() {
        return factor;
    }

    public String getPlantillaMotivo(){
        
        return cadena;
    }
    
     public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(float porcentaje) {
        this.porcentaje = porcentaje;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getSigno() {
        return signo;
    }

    public void setSigno(String signo) {
        this.signo = signo;
    }

    public String getCadena() {
        return cadena;
    }

    public void setCadena(String cadena) {
        this.cadena = cadena;
    }

    public int getIn() {
        return in;
    }

    public void setIn(int in) {
        this.in = in;
    }
    
     public void setFactor(String factor) {
        this.factor = factor;
    }

    public void setLecturas(ArrayList<Lecturatfbind> lecturas) {
        this.lecturas = lecturas;
    }
    
}
