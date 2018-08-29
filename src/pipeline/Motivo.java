 /*
    Yackson Ramirez (yackson.ramirez), Jose Lopez (jlopez@unet.edu.ve).


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

import estructura.factorTranscripcion;
import java.util.ArrayList;

/**
 *
 * @author jose
 */
public class Motivo {

    private String motivo;
    private String type; // Core or General
    private String core; // TATA, CAAT, BRE, Inr, GpC
    
    private int[] coordenadas;
    
    private ArrayList<factorTranscripcion> factores;

    public Motivo() {
        this.coordenadas = new int[2];
        this.factores = new ArrayList<>();
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCore() {
        return core;
    }

    public void setCore(String core) {
        this.core = core;
    }
    
    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String m) {
        this.motivo = m;
    }

    public int[] getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(int[] coords) {
        this.coordenadas = coords;
    }

    public ArrayList<factorTranscripcion> getFactores() {
        return factores;
    }

    public void setFactores(ArrayList<factorTranscripcion> fts) {
        this.factores = fts;
    }
}
