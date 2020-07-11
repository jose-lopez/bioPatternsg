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
package estructura;

import java.util.ArrayList;

/**
 *
 * @author yacson
 */
public class HGNC {
    private String Simbolo;
    private String Nombre;
    private String locus_type;
    private String ensembl_gene_id;
    private ArrayList<String> sinonimos;
    private ArrayList<String> gene_family;
    private ArrayList<String> tejidos;

    public HGNC() {
        sinonimos = new ArrayList<>();
        gene_family = new ArrayList<>();
    }

    public void imprimir() {
        System.out.println("    Nombre: " + getNombre());
        System.out.println("    Simbolo: " + getSimbolo());
        System.out.println("    Ensembl gene id: " + getEnsembl_gene_id());
        System.out.println("    SINONIMOS:");
        for (int i = 0; i < getSinonimos().size(); i++) {
            System.out.println("       -" + getSinonimos().get(i));
        }
        System.out.println("_____________________________________");
    }

    public String getSimbolo() {
        return Simbolo;
    }

    public void setSimbolo(String Simbolo) {
        this.Simbolo = Simbolo;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getLocus_type() {
        return locus_type;
    }

    public void setLocus_type(String locus_type) {
        this.locus_type = locus_type;
    }

    public String getEnsembl_gene_id() {
        return ensembl_gene_id;
    }

    public void setEnsembl_gene_id(String ensembl_gene_id) {
        this.ensembl_gene_id = ensembl_gene_id;
    }

    public ArrayList<String> getTejidos() {
        return tejidos;
    }

    public void setTejidos(ArrayList<String> tejidos) {
        this.tejidos = tejidos;
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
    
    

    public ArrayList<String> ListaNombres() {
        ArrayList<String> Lista = new ArrayList<>();

        Lista.add(Simbolo);
        Lista.add(Nombre);
        Lista.addAll(sinonimos);

        return Lista;
    }
}
