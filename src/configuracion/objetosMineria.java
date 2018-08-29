/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuracion;

import java.util.ArrayList;
import estructura.HGNC;
import estructura.complejoProteinico;

/**
 *
 * @author yacson
 */
public class objetosMineria {
    private int Iteracion;
    private ArrayList<String> objetos_minados;
    private ArrayList<String> nuevos_objetos;

    public objetosMineria() {

        this.objetos_minados = new ArrayList<>();
        this.nuevos_objetos = new ArrayList<>();
    }

    public void imprimir() {

        //----------------------
        System.out.println();
        System.out.println("Objetos Minados: ");
        for (int i = 0; i < getObjetos_minados().size(); i++) {
            System.out.println(getObjetos_minados().get(i));
        }
        System.out.println();
        System.out.println("Nuevos Objetos: ");
        for (int i = 0; i < getNuevos_objetos().size(); i++) {
            System.out.println(getNuevos_objetos().get(i));
        }

    }

    public int getIteracion() {
        return Iteracion;
    }

    public void setIteracion(int Iteracion) {
        this.Iteracion = Iteracion;
    }

    public ArrayList<String> getObjetos_minados() {
        return objetos_minados;
    }

    public void setObjetos_minados(ArrayList<String> objetos_minados) {
        this.objetos_minados = objetos_minados;
    }

    public void agregarObjetosMinado(String objeto) {
        if (objeto != null) {
            this.objetos_minados.add(objeto);

        }
    }

    public ArrayList<String> getNuevos_objetos() {
        return nuevos_objetos;
    }

    public void setNuevos_objetos(ArrayList<String> nuevos_objetos) {
        this.nuevos_objetos = nuevos_objetos;
    }

    public void agregar_objeto(HGNC HGNC) {

        if (!objetos_minados.contains(HGNC.getSimbolo())) {

            if (!nuevos_objetos.contains(HGNC.getSimbolo()) && HGNC.getSimbolo() != null) {
                nuevos_objetos.add(HGNC.getSimbolo());
            }

        }

    }

    public void agregar_objeto(String objeto) {
        if (!objetos_minados.contains(objeto)) {
            if (!nuevos_objetos.contains(objeto) && objeto != null) {
                nuevos_objetos.add(objeto);
                //System.out.println("nuevo "+objeto);
            }
        }
    }

    public void agregar_objeto(ArrayList<complejoProteinico> objetos) {

        objetos.forEach((objeto) -> {
            objeto.getHGNC().forEach((objHGNC) -> {
                if (!objetos_minados.contains(objHGNC.getSimbolo()) && objHGNC.getSimbolo() != null) {
                    if (!nuevos_objetos.contains(objHGNC.getSimbolo())) {
                        nuevos_objetos.add(objHGNC.getSimbolo());
                    }
                }
            });
        });

    }
}
