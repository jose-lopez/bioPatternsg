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

/**
 *
 * @author yacson
 */
public class cadenas_pathway {
    
   private String pathway_inicial;
   private String pathway_final;
   private String Eventos;

    public String getPathway_inicial() {
        return pathway_inicial;
    }

    public void setPathway_inicial(String pathway_inicial) {
        this.pathway_inicial = pathway_inicial;
    }

    public String getPathway_final() {
        return pathway_final;
    }

    public void setPathway_final(String pathway_final) {
        this.pathway_final = pathway_final;
    }

    public String getEventos() {
        return Eventos;
    }

    public void setEventos(String Eventos) {
        this.Eventos = Eventos;
    }
   
   
    
}
