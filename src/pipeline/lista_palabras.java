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
package pipeline;

import java.util.ArrayList;


/**
 *
 * @author yacson.ramirez
 */
public class lista_palabras {
    
    private String proteina;
    private ArrayList<String>ligandos= new ArrayList<>();

    public lista_palabras(){}
    public lista_palabras(String proteina, ArrayList<String> ligandos) {
        this.proteina = proteina;
        this.ligandos = ligandos;
    }

    public String getProteina() {
        return proteina;
    }

    public void setProteina(String proteina) {
        this.proteina = proteina;
    }

    public ArrayList<String> getlistaLigandos() {
        return ligandos;
    }

    public void setlistaLigandos(ArrayList<String> ligandos) {
        this.ligandos = ligandos;
    }
    
    
    
    
    
    
    
    
}


