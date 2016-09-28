 /*
    Region.java


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
package pipeline;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author jose
 */
public class Region {

    private String regionPromotora;
    private int[] coordenadasPromotor;
    private ArrayList<Motivo> promotor;

    public void setRegionPromotora(String regionPromotora) {
        this.regionPromotora = regionPromotora;
    }

    public void setCoordenadasPromotor(int[] coordenadasPromotor) {
        this.coordenadasPromotor = coordenadasPromotor;
    }

    public void setPromotor(ArrayList<Motivo> promotor) {
        this.promotor = promotor;
    }

    public Region(String rP) {
        this.regionPromotora = rP;
        this.promotor = new ArrayList<>();
        this.coordenadasPromotor = new int[2];
        this.coordenadasPromotor[0] = regionPromotora.length() - 1;
        this.coordenadasPromotor[1] = 0;
    }

    public void constructPromotor(ArrayList<ArrayList> listas_FTs) {

        ArrayList<Factor_Transcripcion> listaFT = listas_FTs.get(0);
        //for (ArrayList<Factor_Transcripcion> listaFT : listas_FTs) {// Para cada arreglo de FTs:

        for (Factor_Transcripcion ft : listaFT) { // Tomo uno y para ese defino motivos posibles a los que se ancle.

            Lecturatfbind lectura = ft.getLectura(); // Tomamos la lectura que describe la estructura del FT, segun TFBIND.
            if (!(lectura == null)) {
                // Desde lectura se obtiene la plantilla que describe al motivo,
                // dos elementos hay en esa plantilla: (1) consenso y (2) el motif hallado en regionPromotora
                // que se corresponde con ese consenso, segun reporta TFBIND.
                String[] plantilla = lectura.getPlantillaMotivo().split(" ");
                String motif = plantilla[2];

                if (!promotorContieneFactor(motif, ft)) {// Si el promotor aun no contiene el motif asociado a ft, entonces:
                    // se asocia un nuevo motivo a promotor y se asocia ft a ese motivo.

                    if (regionPromotora.contains(motif)) {//Tener en cuenta que un motivo puede estar presente mas de una vez.
                        int primera = regionPromotora.indexOf(motif);
                        int ultima = regionPromotora.lastIndexOf(motif);
                        int siguiente;
                        boolean iguales = false;

                        do {
                            int[] coordsMotivo = new int[2];
                            Motivo motivo = new Motivo();
                            motivo.setMotivo(motif);
                            coordsMotivo[0] = primera;
                            coordsMotivo[1] = coordsMotivo[0] + motif.length() - 1;
                            motivo.setCoordenadas(coordsMotivo);
                            motivo.getFactores().add(ft);
                            if (this.coordenadasPromotor[0] > coordsMotivo[0]) {
                                this.coordenadasPromotor[0] = coordsMotivo[0];
                            }
                            if (this.coordenadasPromotor[1] < coordsMotivo[1]) {
                                this.coordenadasPromotor[1] = coordsMotivo[1];
                            }
                            this.promotor.add(motivo);
                            if (primera == ultima) {
                                iguales = true;
                            }
                            siguiente = regionPromotora.indexOf(motif, coordsMotivo[1]);
                            if (siguiente != -1) {
                                primera = siguiente;
                            }


                        } while (!iguales);

                    }
                }
            }

        }

        //}


    }

    public void constructPromotorConsensos(String region, boolean uTR5p) {

        String[] consensosUTR3p = {"TTTTT[T]+", "A[TA]TAAA", "[CT]GTGTT[CT][CT]"};
        //TFBIND:
        //String[] consensosUTR5p = {"[GTA][GA]GG[CTA][GA][GT][GAT][GT][CT]", "[AG][ACGT][AG]T[GT][ACGT][ACGT]G[AC]AA[GT][ACGT][ACGT]", "[GC][GC][GA]CGCC", "TATA[AT]AA[GA]", "[TC][TC]A[ACGT][TA][TC][TC]", "[AG]G[AT][CT][GAC]"};
        //EPD:
        String[] consensosUTR5p = {"[GT][GA]GGCG[GT][GA][GA][CT]", "[CT][GA][GA][GT]GCGG[GA][GT]", "[TCA][CT][TC][AG][GA]CCA[AT][TA][CG][AG]", "[GC][GC][GA]CGCC", "TATA[AT]A[AGT][AG]", "AATGGGGGGGAA", "[TG]C[AT][GTC][TCA][CT][TCG][TC]", "[AG]G[AT][CT][GAC]"};


        String[] consensosUTR = {};

        if (uTR5p) {
            consensosUTR = consensosUTR5p;
        }

        if (!uTR5p) {
            consensosUTR = consensosUTR3p;
        }



        String id = "";
        String core="";

        for (String consenso : consensosUTR) {

            Pattern p = Pattern.compile(consenso);
            Matcher m = p.matcher(region);


            while (m.find()) {
                int[] coordsMotivo = new int[2];

                coordsMotivo[0] = m.start();
                coordsMotivo[1] = m.end() - 1;

                //String motif = region.substring(coordsMotivo[0], coordsMotivo[1]);
                String motif = m.group();

                if (!uTR5p) {

                    if (consenso.equalsIgnoreCase("TTTTT[T]+")) {
                        id = "CPEB";
                    }
                    if (consenso.equalsIgnoreCase("A[TA]TAAA")) {
                        id = "CPSF";
                    }
                    if (consenso.equalsIgnoreCase("[CT]GTGTT[CT][CT]")) {
                        id = "CSTF";
                    }
                } else {// Dado que mas de una proteina reconoce un consenso,
                    // entonces entrar y verificar cual motivo es y a cual proteina corresponde.
                                                // "[GT][GA]GGCG[GT][GA][GA][CT]"
                    if (consenso.equalsIgnoreCase("[GT][GA]GGCG[GT][GA][GA][CT]")) {
                        id = "SP1"; core = "GC";
                    }
                    if (consenso.equalsIgnoreCase("[CT][GA][GA][GT]GCGG[GA][GT]")) {
                        id = "SP1"; core = "GC";
                    }
                    if (consenso.equalsIgnoreCase("[AGT][GA][AG][TC][CT]GGT[TA][AT][GC][TC]")) {
                        id = "C/EBP"; core = "CAAT";
                    }
                    if (consenso.equalsIgnoreCase("[GC][GC][GA]CGCC")) {
                        id = "TFIIB"; core = "BRE";
                    }
                    if (consenso.equalsIgnoreCase("TATA[AT]A[AGT][AG]")) {
                        id = "TAF1"; core = "TATA";
                    }
                    if (consenso.equalsIgnoreCase("AATGGGGGGGAA")) {
                        id = "EIF4E"; core = "EIF4E";
                    }
                    if (consenso.equalsIgnoreCase("[TG]C[AT][GTC][TCA][CT][TCG][TC]")) {
                        id = "TFIID"; core = "Inr";
                    }
                    if (consenso.equalsIgnoreCase("[AG]G[AT][CT][GAC]")) {
                        id = "TFIID"; core = "DPE";
                    }
                }


                String cadena = consenso + " " + motif;
                Lecturatfbind lectura = new Lecturatfbind(id, id, cadena);
                Factor_Transcripcion ft = new Factor_Transcripcion();
                ft.setID(id);
                ft.setSimbolo(id);
                ft.setLectura(lectura);


                Motivo motivo = new Motivo();
                motivo.setMotivo(motif);
                motivo.setCoordenadas(coordsMotivo);
                motivo.setCore(core);
                motivo.getFactores().add(ft);

                promotor.add(motivo);

            }

            /*
             hasConsense = m.matches();

             int primerCA = region.
             int ultimoCA = regionCAs.lastIndexOf("CA");
             int indexCA = primerCA;
             ArrayList<Integer> posicionesCAs = new ArrayList<>();

             do {
             posicionesCAs.add(indexCA);
             indexCA = regionCAs.indexOf("CA", indexCA);
             } while (indexCA != ultimoCA);

             posicionesCAs.add(ultimoCA);*/
        }

    }

    boolean promotorContieneFactor(String motif, Factor_Transcripcion ft) {

        Factor_Transcripcion factorMotivo;
        
        for (Motivo m : this.promotor) {

            factorMotivo = m.getFactores().get(0);
                        
            if (m.getMotivo().equals(motif)) {

                if (factorMotivo.getID().equals(ft.getID())) {

                    return true;
                
                }
            }

        }

        return false;

    }

    public String getRegionPromotora() {
        return regionPromotora;
    }

    public int[] getCoordenadasPromotor() {
        return coordenadasPromotor;
    }

    public ArrayList<Motivo> getPromotor() {
        return promotor;
    }
    
    public void imprimirRegRegulacion(String archivo) throws UnsupportedEncodingException, FileNotFoundException, IOException{
        
        File salidaPromotor = new File(archivo);
        salidaPromotor.delete();
        
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(salidaPromotor, true), "UTF8"))) {
            
            out.write("cadena" + "\t" + "Consenso" + "\t" + "Coordenada Ini." + "\t" + "Coord Fin." + "\t" + "Factor Simbololo" +  "Factor Nombre" + "\n");
            
            for (Motivo m : this.promotor) {
                out.write("cadena" + "\t" + "Consenso" + "\t" + "Coordenada Ini." + "\t" + "Coord Fin." + "\t" + "Factor Simbololo" +  "Factor Nombre" + "\n");
                
                String motifFirma = m.getFactores().get(0).getLectura().getCadena();
                String consenso = m.getFactores().get(0).getLectura().getPlantillaMotivo();                
                out.write(motifFirma + "\t" + consenso + "\t" + m.getCoordenadas()[0] + "\t" + m.getCoordenadas()[1] + "\t" + m.getFactores().get(0).getSimbolo() +  m.getFactores().get(0).getNombre() + "\n");
                
            }
            
            out.close();
            
        }
        
    }
}
