/* 
 * bioPatternsg
 * BioPatternsg is a system that allows the integration and analysis of information related to the modeling of Gene Regulatory Networks (GRN).
 * Copyright (C) 2020
 * Jose Lopez (josesmooth@gmail.com), Jacinto Dávila (jacinto.davila@gmail.com), Yacson Ramirez (yacson.ramirez@gmail.com).
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package findmotifs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author jose
 */
public class PreprocesadorUTR5p {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        PreprocesadorUTR5p prePro = new PreprocesadorUTR5p();
        //prePro.generadorPromots("promotores_Inr_small.txt", "promtsMuestrados.txt", "Inr", 5);
        //prePro.generadorPromots("promotores_Inr.txt", "promtsMuestrados.txt", "Inr", 10);
        prePro.crear_archivos_muestras(100);
    }

    public void crear_archivos_muestras(int tam) throws IOException {

        crear_archivo("datos_UTR5p/Inr_positives_D.pl");
        crear_archivo("datos_UTR5p/Inr_positives_I.pl");
        crear_archivo("datos_UTR5p/TATA_negatives_Inr_D.pl");
        crear_archivo("datos_UTR5p/TATA_negatives_Inr_I.pl");
        crear_archivo("datos_UTR5p/CAAT_negatives_Inr_D.pl");
        crear_archivo("datos_UTR5p/CAAT_negatives_Inr_I.pl");
        crear_archivo("datos_UTR5p/GC_negatives_Inr_D.pl");
        crear_archivo("datos_UTR5p/GC_negatives_Inr_I.pl");
        System.out.println("muestras Inr");
        generar_muestras(10, "Inr", tam, "TATA", "CAAT", "GC");

        crear_archivo("datos_UTR5p/TATA_positives_D.pl");
        crear_archivo("datos_UTR5p/TATA_positives_I.pl");
        crear_archivo("datos_UTR5p/Inr_negatives_TATA_D.pl");
        crear_archivo("datos_UTR5p/Inr_negatives_TATA_I.pl");
        crear_archivo("datos_UTR5p/CAAT_negatives_TATA_D.pl");
        crear_archivo("datos_UTR5p/CAAT_negatives_TATA_I.pl");
        crear_archivo("datos_UTR5p/GC_negatives_TATA_D.pl");
        crear_archivo("datos_UTR5p/GC_negatives_TATA_I.pl");
        System.out.println("muestras TATA");
        generar_muestras(0, "TATA", tam, "Inr", "CAAT", "GC");

        crear_archivo("datos_UTR5p/CAAT_positives_D.pl");
        crear_archivo("datos_UTR5p/CAAT_positives_I.pl");
        crear_archivo("datos_UTR5p/Inr_negatives_CAAT_D.pl");
        crear_archivo("datos_UTR5p/Inr_negatives_CAAT_I.pl");
        crear_archivo("datos_UTR5p/TATA_negatives_CAAT_D.pl");
        crear_archivo("datos_UTR5p/TATA_negatives_CAAT_I.pl");
        crear_archivo("datos_UTR5p/GC_negatives_CAAT_D.pl");
        crear_archivo("datos_UTR5p/GC_negatives_CAAT_I.pl");
        System.out.println("muestras CAAT");
        generar_muestras(0, "CAAT", tam, "Inr", "TATA", "GC");

        crear_archivo("datos_UTR5p/GC_positives_D.pl");
        crear_archivo("datos_UTR5p/GC_positives_I.pl");
        crear_archivo("datos_UTR5p/Inr_negatives_GC_D.pl");
        crear_archivo("datos_UTR5p/Inr_negatives_GC_I.pl");
        crear_archivo("datos_UTR5p/TATA_negatives_GC_D.pl");
        crear_archivo("datos_UTR5p/TATA_negatives_GC_I.pl");
        crear_archivo("datos_UTR5p/CAAT_negatives_GC_D.pl");
        crear_archivo("datos_UTR5p/CAAT_negatives_GC_I.pl");
        System.out.println("muestras GC");
        generar_muestras(0, "GC", tam, "Inr", "TATA", "CAAT");

        //-----------------------------------------------------//
        System.out.println("Generar archivos test");
        archivos_test(tam, "Inr", "TATA", "CAAT", "GC");
        archivos_test(tam, "TATA", "Inr", "CAAT", "GC");
        archivos_test(tam, "CAAT", "TATA", "Inr", "GC");
        archivos_test(tam, "GC", "TATA", "CAAT", "Inr");
        System.out.println("listo...");

    }

    public void archivos_test(int tam, String caja, String N1, String N2, String N3) throws IOException {

        crear_archivo("datos_UTR5p/" + caja + "_test_" + N1 + "_D.pl");
        crear_archivos_test(tam, "datos_UTR5p/" + caja + "_positives_D.pl", "datos_UTR5p/" + caja + "_test_" + N1 + "_D.pl");
        crear_archivos_test(tam, "datos_UTR5p/" + caja + "_negatives_" + N1 + "_D.pl", "datos_UTR5p/" + caja + "_test_" + N1 + "_D.pl");
        crear_archivo("datos_UTR5p/" + caja + "_test_" + N1 + "_I.pl");
        crear_archivos_test(tam, "datos_UTR5p/" + caja + "_positives_I.pl", "datos_UTR5p/" + caja + "_test_" + N1 + "_I.pl");
        crear_archivos_test(tam, "datos_UTR5p/" + caja + "_negatives_" + N1 + "_I.pl", "datos_UTR5p/" + caja + "_test_" + N1 + "_I.pl");

        crear_archivo("datos_UTR5p/" + caja + "_test_" + N2 + "_D.pl");
        crear_archivos_test(tam, "datos_UTR5p/" + caja + "_positives_D.pl", "datos_UTR5p/" + caja + "_test_" + N2 + "_D.pl");
        crear_archivos_test(tam, "datos_UTR5p/" + caja + "_negatives_" + N2 + "_D.pl", "datos_UTR5p/" + caja + "_test_" + N2 + "_D.pl");
        crear_archivo("datos_UTR5p/" + caja + "_test_" + N2 + "_I.pl");
        crear_archivos_test(tam, "datos_UTR5p/" + caja + "_positives_I.pl", "datos_UTR5p/" + caja + "_test_" + N2 + "_I.pl");
        crear_archivos_test(tam, "datos_UTR5p/" + caja + "_negatives_" + N2 + "_I.pl", "datos_UTR5p/" + caja + "_test_" + N2 + "_I.pl");

        crear_archivo("datos_UTR5p/" + caja + "_test_" + N3 + "_D.pl");
        crear_archivos_test(tam, "datos_UTR5p/" + caja + "_positives_D.pl", "datos_UTR5p/" + caja + "_test_" + N3 + "_D.pl");
        crear_archivos_test(tam, "datos_UTR5p/" + caja + "_negatives_" + N3 + "_D.pl", "datos_UTR5p/" + caja + "_test_" + N3 + "_D.pl");
        crear_archivo("datos_UTR5p/" + caja + "_test_" + N3 + "_I.pl");
        crear_archivos_test(tam, "datos_UTR5p/" + caja + "_positives_I.pl", "datos_UTR5p/" + caja + "_test_" + N3 + "_I.pl");
        crear_archivos_test(tam, "datos_UTR5p/" + caja + "_negatives_" + N3 + "_I.pl", "datos_UTR5p/" + caja + "_test_" + N3 + "_I.pl");

    }

    public void generar_muestras(int TSS, String caja, int tam_muestra, String N1, String N2, String N3) {
        Random rdn = new Random();
        ArrayList Lnum = new ArrayList();
        for (int i = 0; i < tam_muestra; i++) {
            while (true) {
                int num = rdn.nextInt(2010);
                if (!Lnum.contains(num)) {
                    Lnum.add(num);
                    String muestra = leer_de_archivo_EPD(num, caja, TSS);
                    if (muestra != null && muestra != "") {
                        //System.out.println(num);
                        String muestrapD = caja + "(" + muestra + ",p,'der')";
                        String muestrapI = caja + "(" + muestra + ",p,'izq')";
                        Escribir_archivo("datos_UTR5p/" + caja + "_positives_D.pl", muestrapD);
                        Escribir_archivo("datos_UTR5p/" + caja + "_positives_I.pl", muestrapI);
                        //-----------------------------------------------------------//
                        String muestranD = ":-" + N1 + "(" + muestra + ",p,'der')";
                        String muestranI = ":-" + N1 + "(" + muestra + ",p,'izq')";
                        Escribir_archivo("datos_UTR5p/" + N1 + "_negatives_" + caja + "_D.pl", muestranD);
                        Escribir_archivo("datos_UTR5p/" + N1 + "_negatives_" + caja + "_I.pl", muestranI);

                        muestranD = ":-" + N2 + "(" + muestra + ",p,'der')";
                        muestranI = ":-" + N2 + "(" + muestra + ",p,'izq')";
                        Escribir_archivo("datos_UTR5p/" + N2 + "_negatives_" + caja + "_D.pl", muestranD);
                        Escribir_archivo("datos_UTR5p/" + N2 + "_negatives_" + caja + "_I.pl", muestranI);

                        muestranD = ":-" + N3 + "(" + muestra + ",p,'der')";
                        muestranI = ":-" + N3 + "(" + muestra + ",p,'izq')";
                        Escribir_archivo("datos_UTR5p/" + N3 + "_negatives_" + caja + "_D.pl", muestranD);
                        Escribir_archivo("datos_UTR5p/" + N3 + "_negatives_" + caja + "_I.pl", muestranI);

                        System.out.println(muestra);
                        break;
                    }

                }

            }
        }

    }

    public String obtener_promotores(String cadena, int C_FM, int C_EPD, int TSS, String tipo) {

        String cad = "";
        int coor_ref = cadena.length() - (C_EPD - C_FM + 1) - TSS;

        if (coor_ref >= 0 && coor_ref < cadena.length()) {
//            System.out.println("long cadena " + cadena.length());
//            System.out.println("coordenada ref " + coor_ref);
            switch (tipo) {
                case "TATA":
                    //System.out.println(cadena.substring(coor_TATA, coor_TATA+1));
                    int coor_TATA = coor_ref - 3;
                    try {
                        for (int i = 0; i < 11; i++) {
                            if (i == 0) {
                                cad += "[";
                            }
                            if (i == 3) {
                                //System.out.print("p " + cadena.substring(coor_TATA + i, coor_TATA + i + 1) + "-");
                                cad += "p," + cadena.substring(coor_TATA + i, coor_TATA + i + 1) + ",";
                            } else {
                                //System.out.print(cadena.substring(coor_TATA + i, coor_TATA + i + 1) + "-");
                                cad += cadena.substring(coor_TATA + i, coor_TATA + i + 1);
                                if (i < 10) {
                                    cad += ",";
                                } else {
                                    cad += "]";
                                }
                            }
                        }
                        System.out.print(coor_TATA);
                    } catch (Exception e) {
                        cad = "";
                    }
                    break;

                case "Inr":
                    int coor_Inr = coor_ref - 2;
                    try {
                        for (int i = 0; i < 8; i++) {
                            if (i == 0) {
                                cad += "[";
                            }
                            if (i == 2) {
                                //System.out.print("p" + cadena.substring(coor_Inr + i, coor_Inr + i + 1) + "-");
                                cad += "p," + cadena.substring(coor_Inr + i, coor_Inr + i + 1) + ",";
                            } else {
                                //System.out.print(cadena.substring(coor_Inr + i, coor_Inr + i + 1) + "-");
                                cad += cadena.substring(coor_Inr + i, coor_Inr + i + 1);

                                if (i < 7) {
                                    cad += ",";
                                } else {
                                    cad += "]";
                                }
                            }

                        }
                    } catch (Exception e) {
                        cad = "";
                    }
                    break;

                case "CAAT":
                    int coor_CAAT = coor_ref - 7;
                    try {
                        for (int i = 0; i < 12; i++) {
                            if (i == 0) {
                                cad += "[";
                            }
                            if (i == 7) {
                                //System.out.print("p" + cadena.substring(coor_CAAT + i, coor_CAAT + i + 1) + "-");
                                cad += "p," + cadena.substring(coor_CAAT + i, coor_CAAT + i + 1) + ",";
                            } else {
                                //System.out.print(cadena.substring(coor_CAAT + i, coor_CAAT + i + 1) + "-");
                                cad += cadena.substring(coor_CAAT + i, coor_CAAT + i + 1);
                                if (i < 11) {
                                    cad += ",";
                                } else {
                                    cad += "]";
                                }
                            }
                        }
                    } catch (Exception e) {
                        cad = "";
                    }
                    break;

                case "GC":
                    int coor_GC = coor_ref - 6;
                    try {
                        for (int i = 0; i < 14; i++) {
                            if (i == 0) {
                                cad += "[";
                            }
                            if (i == 6) {
                                //System.out.print("p" + cadena.substring(coor_GC + i, coor_GC + i + 1) + "-");
                                cad += "p," + cadena.substring(coor_GC + i, coor_GC + i + 1) + ",";
                            } else {
                                //System.out.print(cadena.substring(coor_GC + i, coor_GC + i + 1) + "-");
                                cad += cadena.substring(coor_GC + i, coor_GC + i + 1);
                                if (i < 13) {
                                    cad += ",";
                                } else {
                                    cad += "]";
                                }
                            }

                        }
                    } catch (Exception e) {
                        cad = "";
                    }
                    break;

                default:
                    System.out.println("error " + tipo + " no existe");
            }
        }
        return cad;

    }

    public String leer_de_archivo_EPD(int num, String caja, int TSS) {
        String muestra = null;
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        String Cadena = null;
        try {
            archivo = new File("datos_UTR5p/promotores_EPD_" + caja + ".fps");
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String[] cad = new String[10];

            for (int i = 0; i < num; i++) {

                int c = 0;
                String[] lectura = (br.readLine().replace(" ", "@").split(""));
                if (i == num - 1) {

                    for (int j = 0; j < lectura.length; j++) {

                        if (!lectura[j].equals("@")) {
                            String texto = "";
                            for (int k = j; k < lectura.length; k++) {
                                if (!lectura[k].equals("@")) {
                                    texto += lectura[k];
                                } else {
                                    cad[c] = texto;
                                    j = k;
                                    break;
                                }
                            }

                            c++;
                        }

                    }

                    //System.out.println(cad[0]+" "+cad[1]+" "+cad[4]+" "+cad[5]+" ");
                    String[] separa = cad[4].split("");

                    if (separa[1].equals("+")) {

                        String ID = cad[1];

                        separa = cad[5].split(";");
                        int coorEPD = Integer.parseInt(separa[0]);

                        int coorFM = leer_archio_FM(ID, caja);
                        if (coorFM >= 0) {

                            Cadena = leer_archivo_fasta(ID, caja);
                            if (!Cadena.equals(null) && coorEPD >= coorFM) {
                                if (caja.equals("Inr") && coorEPD == coorFM || !caja.equals("Inr")) {
//                                    System.out.println("------------");
//                                    System.out.println("caja: " + caja);
//                                    System.out.println("ID " + ID);
//                                    System.out.println("Coordenada EPD " + coorEPD);
//                                    System.out.println("Coordenada FM " + coorFM);
//                                    System.out.println("Coordenada ref "+(Cadena.length()- Math.abs(coorEPD-coorFM -TSS-1)));
//                                    //System.out.println(Cadena);
//                                    System.out.println("-----------------");
                                    muestra = obtener_promotores(Cadena, coorFM, coorEPD, TSS, caja);

                                    break;
                                }
                            }
                        }
                    }
                }
            }

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

        return muestra;
    }

    public int leer_archio_FM(String ID, String caja) {
        int coordenada = -1;

        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            archivo = new File("datos_UTR5p/promotores_FM_" + caja + ".fps");
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String[] cad = new String[10];
            String lectura;

            while ((lectura = br.readLine()) != null) {

                int c = 0;
                String[] lectura2 = lectura.replace(" ", "@").split("");

                for (int j = 0; j < lectura2.length; j++) {

                    if (!lectura2[j].equals("@")) {
                        String texto = "";
                        for (int k = j; k < lectura2.length; k++) {
                            if (!lectura2[k].equals("@")) {
                                texto += lectura2[k];
                            } else {
                                cad[c] = texto;
                                j = k;
                                break;
                            }
                        }
                        c++;
                    }

                }
                if (ID.equals(cad[1])) {
                    coordenada = Integer.parseInt(cad[5]);
                }

            }

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

        return coordenada;
    }

    public String leer_archivo_fasta(String ID, String caja) {
        String cadena = "";

        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        int n = 17;
        if (caja.equals("Inr")) {
            n = 1;
        }
        try {
            archivo = new File("datos_UTR5p/promotores_EPD_" + caja + ".fasta");
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String[] cad = new String[10];
            String lectura;

            while ((lectura = br.readLine()) != null) {

                int c = 0;
                String[] lectura2 = lectura.split(" ");

                for (int i = 0; i < n; i++) {
                    lectura = br.readLine();
                    cadena += lectura;
                }

                if (ID.equals(lectura2[1])) {
                    //System.out.println(cadena);
                    return cadena;

                } else {
                    cadena = "";
                }

            }

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

        return null;
    }

    public void crear_archivos_test(int tam, String archivol, String archivot) {
        int tam_reg = (int) (tam * 0.3f);
        Random rdn = new Random();
        ArrayList Lnum = new ArrayList();
        for (int i = 0; i < tam_reg; i++) {
            while (true) {
                int num = rdn.nextInt(tam - 1) + 1;
                if (!Lnum.contains(num)) {
                    Lnum.add(num);
                    String muest = leer_archivo(num, archivol);
                    //System.out.println(muest);
                    Escribir_archivo(archivot, muest);
                    break;
                }
            }
        }

    }

    public String leer_archivo(int num, String arch) {
        String muestra = null;
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        String Cadena = null;
        try {
            archivo = new File(arch);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            for (int i = 0; i < num; i++) {

                muestra = br.readLine();

            }
        } catch (Exception e) {

        }

        return muestra;

    }

    public void crear_archivo(String nombre) throws IOException {

        FileWriter fichero = null;
        PrintWriter pw = null;
        fichero = new FileWriter(nombre);
        pw = new PrintWriter(fichero);

    }

    public void Escribir_archivo(String archivo, String texto) {

        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter(archivo, true);
            pw = new PrintWriter(fichero);

            pw.println(texto);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Nuevamente aprovechamos el finally para 
                // asegurarnos que se cierra el fichero.
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }

    public void generadorPromots(String promtsFasta, String promtsMuestrados, String consSearch, int cantPromotores) throws FileNotFoundException, IOException, StringIndexOutOfBoundsException {

        String consensus = "";

        if (consSearch.equals("CAAT")) {// t/c/a	c/t	t/c	A/g	g/a	C	C	A	a/t	T/a	c/g	A/g
            consensus = "[TCA][CT][TC][AG][GA]CCA[AT][TA][CG][AG]";
        }

        if (consSearch.equals("GC")) { //a/t	a/g g/t/a G/a G	G C/t/a G/a G/t	g/a/t g/t c/t t/c t/g
            consensus = "[AT][AG][GTA][GA]GG[CTA][GA][GT][GAT][GT][CT][TC][TG]";
        }

        if (consSearch.equals("TATA")) { // g/c	t	A/t	T	A/t	A/T	A	a/t	g/a	g/c	c/g	g/c	g/c	g/c	g/c
            consensus = "[GC]T[AT]T[AT][AT]A[AT][GA][GC][CG][GC][GC][GC][GC]";
        }

        if (consSearch.equals("Inr")) {// t/g	C	A/t	g/t/c	T/c/a	c/t	t/c/g	t/c
            consensus = "[TG]C[AT][GTC][TCA][CT][TCG][TC]";
        }

        if (consSearch.equals("DPE")) {
            consensus = "[AG]G[AT][CT][GAC]";
        }

        // hasConsense = Pattern.matches("[TC][TC]A[ACGT][TA][TC][TC]", motif);// Consenso Inr
        String promotorParcial, promotor = "";
        String esc = ">";

        File promotsFasta = new File(promtsFasta); // Los promotores como vienen desde EPD.
        File promotsFormat = new File("ejemplosPromts.txt"); // Todos los promotores ya formateados para muestrear
        File promotsConsensus = new File("ejemplosPromtsConsensus.txt"); // Todos los promotores con consenso consSearch.
        File promotsMuestreo = new File(promtsMuestrados); // Los promotores muestrados desde los cuales extraer ejemplos para 
        // experimento ILP.

        BufferedReader promotoresFasta = new BufferedReader(new FileReader(promotsFasta));

        FileWriter ejemplosPromtsFormat = new FileWriter(promotsFormat);
        FileWriter ejemplosPromtsConsensus = new FileWriter(promotsConsensus); // Aqui se guardan  los promotores
        // que si' tienen el consenso consSearch.

        String[] fastaHeader;
        promotorParcial = promotoresFasta.readLine();
        fastaHeader = promotorParcial.split(" ");
        String promoterID = fastaHeader[1];
        int contPromGlob = 0;

        boolean escape = false;

        while (promotoresFasta.ready()) {

            promotorParcial = promotoresFasta.readLine();

            do {

                promotor = promotor + promotorParcial;
                if (promotoresFasta.ready()) {
                    promotorParcial = promotoresFasta.readLine();
                    escape = promotorParcial.startsWith(esc);
                } else {
                    escape = true;
                }

            } while (!escape);

            if (promotoresFasta.ready()) {
                ejemplosPromtsFormat.write(promoterID + " " + promotor + "\n");
            } else {
                ejemplosPromtsFormat.write(promoterID + " " + promotor);
            }
            if (promotoresFasta.ready()) {
                fastaHeader = promotorParcial.split(" ");
                promoterID = fastaHeader[1];
            }

            promotor = "";
            contPromGlob++;

        }
        ejemplosPromtsFormat.close();
        BufferedReader promotores = new BufferedReader(new FileReader(promotsFormat));
        int cantPromots = 0;
        String promConConsensus, temporal;
        boolean hasConsense = false;
        List<Integer> posConsensus = new ArrayList<>();

        temporal = promotores.readLine();
        int posMatched, posTestCons = 48;

        while (promotores.ready()) { // Se derminan los promotores que tienen el consenso en proceso.

            promConConsensus = temporal.split(" ")[1];
            Pattern p = Pattern.compile(consensus);
            Matcher m = p.matcher(promConConsensus);

            if (consSearch.equals("Inr")) {
                //
                while (m.find()) {
                    posMatched = m.start();
                    posConsensus.add(posMatched);
                }
                if (!posConsensus.isEmpty()) {
                    hasConsense = posConsensus.contains(posTestCons);
                    posConsensus.clear();
                }
            } else {
                hasConsense = m.find();
            }

            if (hasConsense && promotores.ready()) {
                ejemplosPromtsConsensus.write(temporal + "\n");
                cantPromots++;
            } else {
                if (hasConsense && !promotores.ready()) {
                    ejemplosPromtsConsensus.write(temporal);
                    cantPromots++;
                }
            }

            temporal = promotores.readLine();

            if (!promotores.ready()) {
                promConConsensus = temporal.split(" ")[1];
                p = Pattern.compile(consensus);
                m = p.matcher(promConConsensus);

                if (consSearch.equals("Inr")) {
                    //
                    while (m.find()) {
                        posMatched = m.start();
                        posConsensus.add(posMatched);
                    }
                    if (!posConsensus.isEmpty()) {
                        hasConsense = posConsensus.contains(posTestCons);
                        posConsensus.clear();
                    }
                } else {
                    hasConsense = m.find();
                }

                if (hasConsense) {
                    ejemplosPromtsConsensus.write(temporal);
                    cantPromots++;
                }
            }
            hasConsense = false;

        }
        ejemplosPromtsConsensus.close();

        promotores.close();
        System.out.println(" Existen " + cantPromots + "/" + contPromGlob + " promotores tipo " + consSearch);

        promotores = new BufferedReader(new FileReader(promotsConsensus));

        FileWriter promtsM = new FileWriter(promotsMuestreo);
        ArrayList<Integer> promotsMuestrear = new ArrayList<>();
        int muestrados = 0;

        if (cantPromotores <= cantPromots) {

            float reference = (float) Math.random() * cantPromots;
            int aMuestrear = Math.round(reference);
            do {
                if ((aMuestrear != 0) && !promotsMuestrear.contains(aMuestrear)) {
                    promotsMuestrear.add(aMuestrear);
                    muestrados++;
                }
                reference = (float) Math.random() * cantPromots;
                aMuestrear = Math.round(reference);

            } while (muestrados < cantPromotores);

            Collections.sort(promotsMuestrear);

            int contMuestreo = 1, muestreados = 0;
            String promot;

            if (promotores.ready()) {

                for (Integer muestra : promotsMuestrear) {

                    do {
                        try {
                            if (promotores.ready()) {
                                promot = promotores.readLine();
                                if (contMuestreo == muestra) {
                                    promtsM.write(promot);
                                    muestreados++;
                                    if (muestreados != promotsMuestrear.size()) {
                                        promtsM.write("\n");
                                    }
                                }
                            } else {
                                System.out.println("Error: No hay la cantidad de promotores que se requieren en el muestreo. Revisar codigo!");
                                System.exit(0);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        contMuestreo++;
                    } while (contMuestreo <= muestra);
                }
            }
        } else {

            System.out.println("No hay esa cantidad de promotores disponibles");

        }
        promtsM.close();
        promotores.close();

    }

}
