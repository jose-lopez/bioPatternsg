 /*
    Complejo_Proteinico.java


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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import java.util.ArrayList;


/**
 *
 * @author yacson
 */
public class Complejo_Proteinico {

    private String Id; // PDB Id
    private ArrayList<Description> descripcion; // Los objetos (proteinas) del complejo descritos segun HUGO.
    private ArrayList<String> DNA; // Si el complejo incluye algun motivo DNA esta lista los guarda.
    private ArrayList<ligando> ligandos; // Ligandos asociados al complejo.
    private ArrayList<String> pdbx_keywords;

    public Complejo_Proteinico() {

        ligandos = new ArrayList<>();
        DNA = new ArrayList<>();
        descripcion = new ArrayList<>();
        pdbx_keywords = new ArrayList<>();
    }

    public void imprimir() {

        System.out.println("    Complejo: " + Id);
        System.out.println("      pdbx_keywords:");
        for (int i = 0; i < pdbx_keywords.size(); i++) {
            System.out.println("        - "+pdbx_keywords.get(i));
        }
        
        for (int i = 0; i < DNA.size(); i++) {
            System.out.println("      -DNA: " + DNA.get(i));
        }
        
        for (int i = 0; i < descripcion.size(); i++) {
            descripcion.get(i).imprimir();
        }
        
        for (int i = 0; i < ligandos.size(); i++) {
            ligandos.get(i).imprimir();
        }
        System.out.println();

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

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public ArrayList<ligando> getLigandos() {
        return ligandos;
    }

    public void setLigandos(ArrayList<ligando> ligandos) {
        this.ligandos = ligandos;
    }

    public ArrayList<Description> getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String tipo, String simbolo) {

       // this.descripcion.add(new Description(tipo, simbolo));
    }

    public ArrayList<String> getPdbx_keywords() {
        return pdbx_keywords;
    }

    public void setPdbx_keywords(ArrayList<String> pdbx_keywords) {
        this.pdbx_keywords = pdbx_keywords;
    }

    
    
    

}
class Description {

    private String etiqueta; 
    private String criterio; 
    //-------------------------------------------------------------------------------------//
    private String simbolo;
    private String nombre;
    private String locus_type;
    private String ensembl_gene_id;
    private ArrayList<String> sinonimos;
    private ArrayList<String> gene_family;

    public Description() {
        sinonimos = new ArrayList<>();
        gene_family = new ArrayList<>();
    }
    
    public boolean verifica_ADN (String etiqueta){
        boolean adn  =false;
         
        for (int i = 0; i < etiqueta.length()-3; i++) {
            String letra1 = etiqueta.substring(i, i + 1);
            String letra2 = etiqueta.substring(i+1, i + 2);
            String letra3 = etiqueta.substring(i+2, i + 3);
            String letra4 = etiqueta.substring(i+3, i + 4);
            
            if(letra1.equals("5") && letra2.equals("'") && letra3.equals("-") && letra4.equals("D") ){
                
                //System.out.println("cadena ADN");
                adn=true;
                break;
                
            }
            
        
        }
         return adn;
    }
    
    public void imprimir(){
        
        System.out.println("        Etiqueta PDB: "+etiqueta);
        System.out.println("        Criterio Busqueda: "+criterio);
        System.out.println("        Simbolo: "+simbolo);
        System.out.println("        Nombre: "+nombre);
        System.out.println("        Locus Type: "+locus_type);
        System.out.println("        Ensembl gene id: "+ensembl_gene_id);
        System.out.print("        Sinonimos: ");
        for (int i = 0; i < sinonimos.size(); i++) {
            System.out.print(sinonimos.get(i)+", ");
        }
        System.out.println();
        System.out.println("        Gene Family: ");
        for (int i = 0; i < gene_family.size(); i++) {
            System.out.println("         "+gene_family.get(i)+", ");
        }
        
        System.out.println();
       
        
    }
    
   
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

  

    public String getLocus_type() {
        return locus_type;
    }

    public void setLocus_type(String locus_type) {
        this.locus_type = locus_type;
    }

    public ArrayList<String> getSinonimos() {
        return sinonimos;
    }

    public void setSinonimos(ArrayList<String> sinonimos) {
        this.sinonimos = sinonimos;
    }

    public ArrayList<String> getGene_family() {
        return gene_family;
    }

    public void setGene_family(ArrayList<String> gene_family) {
        this.gene_family = gene_family;
    }
    
    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getEnsembl_gene_id() {
        return ensembl_gene_id;
    }

    public void setEnsembl_gene_id(String ensembl_gene_id) {
        this.ensembl_gene_id = ensembl_gene_id;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }
    
    
    
    
    

}
