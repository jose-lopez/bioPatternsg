/*
    Busqueda_PubMed.java


    Copyright (C) 2016.
    Yackson Ramirez (yacson.ramirez), Jose Lopez (jlopez@unet.edu.ve). 

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
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yacson
 */
public class Busqueda_PubMed {

    ;
    private ArrayList<String> listaIDs;

    public Busqueda_PubMed() {

        //   palabras = new ArrayList<>();
        listaIDs = new ArrayList<>();

    }

    public void limpiar_men() {
        listaIDs = null;
    }

    public void busqueda_IDs(ArrayList<ArrayList> listaFT, ArrayList<Description> homologos, boolean todas) {
        System.out.println();
        System.out.println("busqueda de ID pudmed");

        combinacionHomologos(homologos);

        for (int i = 0; i < listaFT.size(); i++) {
            for (int j = 0; j < listaFT.get(i).size(); j++) {
                busqueda_factor_ligando((Factor_Transcripcion) listaFT.get(i).get(j));
                busqueda_factor_complejo((Factor_Transcripcion) listaFT.get(i).get(j));
                if (i == 0 || todas) {
                    busqueda_factor_homologo((Factor_Transcripcion) listaFT.get(i).get(j), homologos);
                }
            }

        }
        System.out.println("Listo..");
        System.out.println("PubMed ID encontrados: " + listaIDs.size());

    }

    public void generador_lista_Pub_Med(ArrayList<combinacion> palabras) {

        //System.out.println("combinacion");
        for (int i = 0; i < palabras.size(); i++) {
            try {
                String palabraClave = "";
                String[] pal = palabras.get(i).palabra1.split(" ");

                for (int j = 0; j < pal.length; j++) {
                    palabraClave = palabraClave + pal[j] + "+";
                }

                pal = palabras.get(i).palabra2.split(" ");
                for (int j = 0; j < pal.length; j++) {
                    palabraClave = palabraClave + pal[j] + "+";
                }

                ArrayList<String> listaid = new lecturas_PM().busquedaPM_ID(palabraClave);
                //System.out.println(palabraClave+"           [id encontrados: "+listaid.size()+"]");

                for (int j = 0; j < listaid.size(); j++) {
                    if (!listaIDs.contains(listaid.get(j))) {
                        listaIDs.add(listaid.get(j));
                    }
                }
            } catch (Exception e) {
            }
        }

    }

    private ArrayList<combinacion> busqueda_factor_ligando(Factor_Transcripcion factor) {

        ArrayList<combinacion> palabras = new ArrayList<>();

        ArrayList<ligando> listlig = factor.obtener_ligandos();
        for (int j = 0; j < listlig.size(); j++) {

            if (factor.getNombre() != null) {
                combinacion pal = new combinacion();
                pal.setPalabra1(factor.getNombre());
                pal.setPalabra2(listlig.get(j).getNombre());
                filtro_palabras(pal, palabras);

            }
            if (factor.getSimbolo() != null) {
                combinacion pal = new combinacion();
                pal.setPalabra1(factor.getSimbolo());
                pal.setPalabra2(listlig.get(j).getNombre());
                filtro_palabras(pal, palabras);

            }
            if (factor.getID() != null) {
                combinacion pal = new combinacion();
                pal.setPalabra1(factor.getID());
                pal.setPalabra2(listlig.get(j).getNombre());
                filtro_palabras(pal, palabras);

            }

        }

        generador_lista_Pub_Med(palabras);
        return palabras;

    }

    private void busqueda_factor_complejo(Factor_Transcripcion factor) {

        //-------------------------------------------------------
        //polimeros
        ArrayList<combinacion> palabras = new ArrayList<>();
        ArrayList<String> Lis_prot = factor.obtener_complejos_description();
        for (int j = 0; j < Lis_prot.size(); j++) {

            if (factor.getNombre() != null) {
                combinacion pal = new combinacion();
                pal.setPalabra1(factor.getNombre());
                pal.setPalabra2(Lis_prot.get(j));
                filtro_palabras(pal, palabras);
            }

            if (factor.getSimbolo() != null) {
                combinacion pal = new combinacion();
                pal.setPalabra1(factor.getSimbolo());
                pal.setPalabra2(Lis_prot.get(j));
                filtro_palabras(pal, palabras);
            }
            if (factor.getID() != null) {
                combinacion pal = new combinacion();
                pal.setPalabra1(factor.getID());
                pal.setPalabra2(Lis_prot.get(j));
                filtro_palabras(pal, palabras);
            }
        }
        generador_lista_Pub_Med(palabras);
    }

    private void busqueda_factor_homologo(Factor_Transcripcion factor, ArrayList<Description> homologo) {

        ArrayList<combinacion> palabras = new ArrayList<>();
        ArrayList<String> homo = new ArrayList<>();
        for (int i = 0; i < homologo.size(); i++) {
            homo.addAll(homologo.get(i).getSinonimos());
            homo.add(homologo.get(i).getNombre());
            homo.add(homologo.get(i).getSimbolo());
            homo.add(homologo.get(i).getEtiqueta());
        }

        for (int i = 0; i < homo.size(); i++) {

            if (factor.getNombre() != null) {
                combinacion pal = new combinacion();
                pal.setPalabra1(factor.getNombre());
                pal.setPalabra2(homo.get(i));
                filtro_palabras(pal, palabras);
            }

            if (factor.getSimbolo() != null) {
                combinacion pal = new combinacion();
                pal.setPalabra1(factor.getSimbolo());
                pal.setPalabra2(homo.get(i));
                filtro_palabras(pal, palabras);
            }

            if (factor.getID() != null) {
                combinacion pal = new combinacion();
                pal.setPalabra1(factor.getID());
                pal.setPalabra2(homo.get(i));
                filtro_palabras(pal, palabras);
            }

        }

        generador_lista_Pub_Med(palabras);

    }

    private void combinacionHomologos(ArrayList<Description> listaHomologos) {

        ArrayList<String> nombreHomologos = new ArrayList<>();
        for (int i = 0; i < listaHomologos.size(); i++) {
            nombreHomologos.add(listaHomologos.get(i).getEtiqueta());
        }

        ArrayList<combinacion> palabras = new ArrayList<>();

        IteradorCombinacion it = new IteradorCombinacion(nombreHomologos, 2);
        Iterator s = it.iterator();

        while (s.hasNext()) {

            List<String> listares = (List<String>) s.next();

            for (int i = 0; i < listaHomologos.size(); i++) {

                if (listaHomologos.get(i).getEtiqueta().equals(listares.get(0))) {
                    ArrayList<String> nom1 = listaHomologos.get(i).nombres();

                    for (int j = 0; j < listaHomologos.size(); j++) {

                        if (listaHomologos.get(i).getEtiqueta().equals(listares.get(0))) {
                            ArrayList<String> nom2 = listaHomologos.get(j).nombres();

                            for (int k = 0; k < nom1.size(); k++) {
                                for (int l = 0; l < nom2.size(); l++) {
                                    combinacion con = new combinacion();
                                    con.setPalabra1(nom1.get(k));
                                    con.setPalabra1(nom2.get(l));

                                }
                            }

                        }

                    }

                }

            }

        }

        generador_lista_Pub_Med(palabras);

    }

    private void filtro_palabras(combinacion pal, ArrayList<combinacion> palabras) {
        boolean repite = false;
        try {
            if (!pal.palabra1.equalsIgnoreCase(pal.palabra2)) {
                for (int i = 0; i < palabras.size(); i++) {

                    if (palabras.get(i).palabra1.equalsIgnoreCase(pal.getPalabra1()) && palabras.get(i).palabra2.equalsIgnoreCase(pal.getPalabra2())) {
                        repite = true;
                        break;
                    }

                }
                if (!repite) {
                    palabras.add(pal);
                }
            }
        } catch (Exception e) {
            //palabras.add(pal);
        }

    }

    public ArrayList<String> getListaIDs() {
        return listaIDs;
    }

    public void setListaIDs(ArrayList<String> listaIDs) {
        this.listaIDs = listaIDs;
    }

    class combinacion {

        private String palabra1;
        private String palabra2;

        public String getPalabra1() {
            return palabra1;
        }

        public void setPalabra1(String palabra1) {
            this.palabra1 = palabra1;
        }

        public String getPalabra2() {
            return palabra2;
        }

        public void setPalabra2(String palabra2) {
            this.palabra2 = palabra2;
        }

        public void imprimir() {
            System.out.println(palabra1 + "  " + palabra2);
        }
    }

}
