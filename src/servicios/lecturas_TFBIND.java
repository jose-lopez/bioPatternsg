/*
    Lecturas_TFBIND.java


    Copyright (C) 2016.
    Yacson Ramirez (yacson.ramirez@gmail.com), Jose Lopez (jlopez@unet.edu.ve).

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
package servicios;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class lecturas_TFBIND {

    private String id;
    private String factor;
    private float porcentaje;
    private int numero;
    private String signo;
    private String cadena;

    public lecturas_TFBIND() {

    }

    public lecturas_TFBIND(String id, String factor, String cadena) {
        this.id = id;
        this.factor = factor;
        this.cadena = cadena;
    }

    public lecturas_TFBIND(String ruta, float confiabilidad) throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        /*System.out.printf("\nIngrese Confiabilidad: ");
        confiabilidad = Float.parseFloat(in.readLine());*/
        leer_de_archivo(ruta, confiabilidad);

    }

    public ArrayList<lecturas_TFBIND> leer_de_archivo(String ruta, float confiabilidad){

        ArrayList<lecturas_TFBIND> lecturas = new ArrayList<>();
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
            
            archivo = new File(ruta);
            
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String metodo;
            System.out.println("");
            System.out.println("* Leyendo bloques consenso desde " + ruta);
            ArrayList<String> control_factores = new ArrayList<>();

            while ((metodo = br.readLine()) != null) {

                System.out.println("* buscando registros FT desde TFBIND para: " + metodo);
                lecturas = obtener_lecturas(metodo, confiabilidad, control_factores);

            }
            System.out.println("...ok");

        } catch (Exception e) {

           // e.printStackTrace();

        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        return lecturas;
    }

    public ArrayList<lecturas_TFBIND> obtener_lecturas(String metodo, float confiabilidad, ArrayList<String> control_factores) throws MalformedURLException, IOException {

        int cont = 0;
        URL urlpagina;
        InputStreamReader isr;
        BufferedReader br;
        String linea, segmento;
        StringBuffer buffer = new StringBuffer();
        String[] separar;
        String factor;
        ArrayList<lecturas_TFBIND> lecturas = new ArrayList<>();

        try {
            urlpagina = new URL("http://tfbind.hgc.jp/cgi-bin/calculate.cgi?seq=%3E+COMMENTS%0D%0A" + metodo);
            isr = new InputStreamReader(urlpagina.openStream());
            br = new BufferedReader(isr);

            while ((linea = br.readLine()) != null) {

                cont++;
                try {
                    if (cont >= 7) {
                        segmento = linea.replaceAll(" +", "#").replace("<BR>", "").replace("</BODY>", "").replace("</HTML>", "");
                        separar = segmento.split("#");
                        //System.out.println(separar[2]);
                        if (separar.length > 1 && Float.parseFloat(separar[2]) >= confiabilidad) {

                            factor = separar[1].substring(separar[1].indexOf("$") + 1, separar[1].indexOf("_"));

                            if (!control_factores.contains(factor)) {

                                lecturas_TFBIND aux = new lecturas_TFBIND();
                                control_factores.add(factor);
                                aux.setFactor(factor);
                                aux.setPorcentaje(Float.parseFloat(separar[2]));
                                aux.setNumero(Integer.parseInt(separar[3]));
                                aux.setSigno(separar[4]);
                                aux.setCadena(separar[5] + "  " + separar[6]);
                                lecturas.add(aux);

                            }

                        }

                    }
                } catch (Exception e) {
                }

            }
            br.close();
            isr.close();

        } catch (MalformedURLException e) {
            System.out.println("Error al leer el archivo");
        } catch (IOException e) {

        }

        return lecturas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFactor() {
        return factor;
    }

    public void setFactor(String factor) {
        this.factor = factor;
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

}
