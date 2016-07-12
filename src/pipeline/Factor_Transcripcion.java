 /*
    Factor_Transcripcion.java


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
public class Factor_Transcripcion {

    private String ID;
    private Lecturatfbind lectura;
    private String nombre;
    private String simbolo;
    private ArrayList<Complejo_Proteinico> com_prot;
    private ArrayList<String> motivos; // Contiene todos los motivos que este FT puede reconocer.

    
    public Factor_Transcripcion() {
        com_prot = new ArrayList<>();
    }

    public ArrayList<ligando> obtener_ligandos() {
        ArrayList<ligando> listalig = new ArrayList<>();

        for (int i = 0; i < com_prot.size(); i++) {
            listalig.addAll(com_prot.get(i).getLigandos());
        }

        return listalig;

    }
    
    public ArrayList<String> obtener_complejos_description(){
        ArrayList<String> Lcom_des = new ArrayList<>();
        
        for (int i = 0; i < com_prot.size(); i++) {
            ArrayList<Description> Ldesc = com_prot.get(i).getDescripcion();
            for (int j = 0; j < Ldesc.size(); j++) {
                
                Lcom_des.add(Ldesc.get(j).getNombre());
                Lcom_des.add(Ldesc.get(j).getSimbolo());
                
                for (int k = 0; k < Ldesc.get(j).getSinonimos().size(); k++) {
                    Lcom_des.add(Ldesc.get(j).getSinonimos().get(k));
                }
                
            }
        }
        
        
        return Lcom_des;
    }
    
    public ArrayList<String> obtener_ADN(){
        ArrayList<String> listADN= new ArrayList<>();
        
        for (int i = 0; i < com_prot.size(); i++) {
            
            listADN.addAll(com_prot.get(i).getDNA());
        }
        
        return listADN;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
    
    public Lecturatfbind getLectura() {
        return this.lectura;
    }

    public void setLectura(Lecturatfbind lectura) {
        this.lectura = lectura;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

  

    public ArrayList<Complejo_Proteinico> getCom_prot() {
        return com_prot;
    }

    public void setCom_prot(ArrayList<Complejo_Proteinico> com_prot) {
        this.com_prot = com_prot;
    }
    
    public void setMotivos(ArrayList<String> motivos) {
        this.motivos = motivos;
    }

    public ArrayList<String> getMotivos() {
        return motivos;
    }


    public void imprimir() {
        System.out.println("");
        System.out.println("Factor de Transcripcion: " + ID);
        System.out.println("Simbolo: " + getSimbolo());
        System.out.println("Nombre: " + getNombre());
        System.out.println();
        System.out.println("    Complejos Proteinicos: ");
        System.out.println();
        for (int i = 0; i < com_prot.size(); i++) {
            com_prot.get(i).imprimir();
        }

    }
    public void generarMotivos(){
        
        // Se agrega a motivos del FT el motivo reportado por TFBIND.
        String[] plantilla = lectura.getPlantillaMotivo().split(" ");
        String motivo = plantilla[1];
        motivos.add(motivo);
        // Se determinan otros posibles motivos presentes en los complejos asociados al FT
        // obtenidos desde PDB.
        for(Complejo_Proteinico comp: com_prot){
            for(String motif : comp.getDNA())
             motivos.add(motif);                
            }
            
        }
    }

