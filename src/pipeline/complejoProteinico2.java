/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yacson-ramirez
 */
public class complejoProteinico2 {

    private String ID;
    private ArrayList<lecturas_HGNC> HGNC;
    private ArrayList<String> DNA;
    private ArrayList<String> pdbx_keywords;
    private ArrayList<ligando> ligandos;

    public complejoProteinico2() {
        this.DNA = new ArrayList<>();
        this.pdbx_keywords = new ArrayList<>();;
        this.ligandos = new ArrayList<>();
        this.HGNC = new ArrayList<>();
    }

    public complejoProteinico2(String ID, ArrayList<lecturas_HGNC> HGNC, ArrayList<String> DNA, ArrayList<String> pdbx_keywords) {
        this.ID = ID;
        this.HGNC = HGNC;
        this.DNA = DNA;
        this.pdbx_keywords = pdbx_keywords;
    }

    public void buscar_ligandos() {
        ligandos = new Lecturas_PDB_Ligandos().Buscar_ligandos(ID);
    }

    public void imprimir() {

        System.out.println("ID: " + ID);
        if (getDNA().size() > 0) {
            System.out.println("Cadenas DNA:");
            for (int i = 0; i < getDNA().size(); i++) {
                System.out.println(getDNA().get(i));
            }
        }
        if (getPdbx_keywords().size() > 0) {
            System.out.println("PDBX KEYWORDS:");
            for (int i = 0; i < getPdbx_keywords().size(); i++) {
                System.out.println("   -" + getPdbx_keywords().get(i));
            }
        }
        if (getLigandos().size() > 0) {
            System.out.println("LIGANDOS:");
            for (int i = 0; i < getLigandos().size(); i++) {
                ligandos.get(i).imprimir();
            }
        }
        if (getHGNC().size() > 0) {
            System.out.println("OBJETOS ENCONTRADOS:");
            for (int i = 0; i < getHGNC().size(); i++) {
                System.out.println("   -"+getHGNC().get(i).getID());
            }
        }
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public ArrayList<lecturas_HGNC> getHGNC() {
        return HGNC;
    }

    public void setHGNC(ArrayList<lecturas_HGNC> HGNC) {
        this.HGNC = HGNC;
    }

    public ArrayList<String> getDNA() {
        return DNA;
    }

    public void setDNA(String DNA) {
        String cadena = "";
        for (int i = 0; i < DNA.length(); i++) {
            String letra = DNA.substring(i, i + 1);

            switch (letra) {
                case "A":
                    cadena = cadena + "A";
                    break;
                case "T":
                    cadena = cadena + "T";
                    break;
                case "C":
                    cadena = cadena + "C";
                    break;
                case "G":
                    cadena = cadena + "G";

            }
        }
        //System.out.println(cadena);
        this.DNA.add(cadena);
    }

    public ArrayList<String> getPdbx_keywords() {
        return pdbx_keywords;
    }

    public void setPdbx_keywords(ArrayList<String> pdbx_keywords) {
        this.pdbx_keywords = pdbx_keywords;
    }

    public ArrayList<ligando> getLigandos() {
        return ligandos;
    }

    public void setLigandos(ArrayList<ligando> ligandos) {
        this.ligandos = ligandos;
    }

}
