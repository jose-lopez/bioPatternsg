/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package estructura;

import java.util.ArrayList;
import pipeline.escribirBC;
import servicios.lecturas_PDB_Ligandos;

/**
 *
 * @author yacson
 */
public class complejoProteinico {
    private String ID;
    private ArrayList<HGNC> HGNC;
    private ArrayList<String> DNA;
    private ArrayList<String> pdbx_keywords;
    private ArrayList<ligando> ligandos;

    public complejoProteinico() {
        this.DNA = new ArrayList<>();
        this.pdbx_keywords = new ArrayList<>();;
        this.ligandos = new ArrayList<>();
        this.HGNC = new ArrayList<>();
    }
    
    public complejoProteinico(String ID, ArrayList<HGNC> HGNC, ArrayList<String> DNA, ArrayList<String> pdbx_keywords) {
        this.ID = ID;
        this.HGNC = HGNC;
        this.DNA = DNA;
        this.pdbx_keywords = pdbx_keywords;
    }

    public void buscar_ligandos() {
        ligandos = new lecturas_PDB_Ligandos().Buscar_ligandos(ID);
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
                System.out.println("   -" + getHGNC().get(i).getSimbolo());
            }
        }
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public ArrayList<HGNC> getHGNC() {
        return HGNC;
    }

    public void setHGNC(ArrayList<HGNC> HGNC) {
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

    public void vaciar_pl(String ruta) {
        System.out.print(".");
        new escribirBC("complejo(\'" + ID.replace("\'", "") + "\').",ruta+"/minedObjects.pl");
        for (int i = 0; i < ligandos.size(); i++) {
            ligandos.get(i).vaciar_pl(ruta);
        }

        String cadena = "[";
        for (HGNC hgnc : HGNC) {
            if (cadena.equals("[")) {
                cadena += "\'" + hgnc.getSimbolo().replace("\'", "") + "\'";
            } else {
                cadena += ",\'" + hgnc.getSimbolo().replace("\'", "") + "\'";
            }
        }
        cadena += "]";
        if (!cadena.equals("[]")) {
            new escribirBC("componentes(\'" + ID.replace("\'", "") + "\'," + cadena + ").",ruta+"/minedObjects.pl");
        }
        
        
        for (HGNC hgnc : HGNC) {
            cadena = "[\'"+hgnc.getSimbolo().replace("\'", "")+"\',";
            cadena += "\'"+hgnc.getNombre().replace("\'", "")+"\'";
            for (String sinonimo : hgnc.getSinonimos()) {
                cadena+=",\'"+sinonimo.replace("\'", "")+"\'";
            }
            cadena+="]";
            new escribirBC("sinonimos(\'"+hgnc.getSimbolo().replace("\'", "")+"\',"+cadena+").", ruta+"/minedObjects.pl");
        }

    }

    public ArrayList<String> getPdbx_keywords() {
        return pdbx_keywords;
    }

    public void setPdbx_keywords(ArrayList<String> pdbx_keywords) {
        this.pdbx_keywords = pdbx_keywords;
    }

    public ArrayList< ligando> getLigandos() {
        return ligandos;
    }

    public void setLigandos(ArrayList<ligando> ligandos) {
        this.ligandos = ligandos;
    }

    public ArrayList<String> listaNombres() {
        ArrayList lista = new ArrayList();
        
        HGNC.forEach(hgnc -> lista.addAll(hgnc.ListaNombres()));
        
        return lista;
    }
    
    public void NuevosObjetos(ArrayList<String> Lista){
        HGNC.forEach((hgnc) -> {
            if (!Lista.contains(hgnc.getSimbolo())) {
                Lista.add(hgnc.getSimbolo());
            }
        });
    }   
}
