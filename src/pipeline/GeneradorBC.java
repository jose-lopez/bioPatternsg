 /*
 GeneradorBC.java


 Copyright (C) 2016.
 Jose Lopez (jlopez@unet.edu.ve). Yacson Ramirez (yacson.ramirez).

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

/**
 *
 * @author Jose Lopez.
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

public class GeneradorBC {

    String verbo = "";

    public static void main(String[] args) throws FileNotFoundException, IOException, Exception {
        GeneradorBC generador = new GeneradorBC();
        //baseC.generador("entradas.txt");
        //baseC.generador("salida-abstracts-098-20-2-SRIF.txt");
        //baseC.generador("salida-abstracts-099-10-3-08112015.txt");
        //**** generadorBC.generador("abstracts-experimento-SRIF-26112015-Part-I-II-salida.txt")****;
        //baseC.generador("ENSG00000157005SST1-gen-comparable-1-salida.txt");
        //baseC.generador("abstracts-experimento-SRIF-26112015-Part-I-II-salida.txt");
        configuracion config = new configuracion();
        generador.generadorBC("baseC.pl",config);


    }

    public String generadorBC(String baseC, configuracion config) throws FileNotFoundException, IOException, StringIndexOutOfBoundsException, Exception {

        String oracionesSVC;

        Vector eventos = new Vector(100, 100);

        int n = 1;

        String baseCtemp = "baseCTemp";

        FileWriter fichero = new FileWriter(baseCtemp);
        FileWriter fichero1 = new FileWriter("baseCdoc");

        try (PrintWriter archivoBC = new PrintWriter(fichero)) {

            PrintWriter archivoBCdoc = new PrintWriter(fichero1);
            archivoBC.println("base([");

            while (oracionesSVC(n)) {

                oracionesSVC = "abstracts/resumen_" + n + ".txt";

                generador(oracionesSVC, archivoBCdoc, eventos);

                n++;

            }
           
            int cont_eventos;
            cont_eventos = printBC(archivoBC, eventos);
            System.out.println("cantidad de eventos: " + cont_eventos);

            archivoBC.println("]).");

            archivoBC.close();
            
            archivoBCdoc.println("Total de eventos: " + cont_eventos);
            archivoBCdoc.close();

            try (BufferedReader baseKB = new BufferedReader(new FileReader(new File(baseCtemp)))) {
                PrintWriter kb = new PrintWriter(new FileWriter(baseC));
                String lineaActual, lineaAnt;

                lineaAnt = baseKB.readLine();

                while (baseKB.ready()) {

                    lineaActual = baseKB.readLine();

                    if (!lineaActual.equals("]).")) {
                        kb.println(lineaAnt);
                        lineaAnt = lineaActual;

                    } else {
                        kb.println(lineaAnt.replace("),", ")"));
                        kb.println(lineaActual);
                        kb.close();
                    }
                }
                baseKB.close();
            }
        }

        config.setGenerarBC(true);
        config.guardar();

        return baseC;

    }

    private boolean oracionesSVC(int n) {

        boolean existe = false;

        File archivo_fuente = new File("abstracts/resumen_" + n + ".txt");

        existe = archivo_fuente.exists();

        return existe;

    }

    public String generador(String oracionesSVC, PrintWriter baseC, Vector eventos) throws FileNotFoundException, IOException, StringIndexOutOfBoundsException, Exception {

        /* descomenta aqui para correr ejemplo sencillo
         File f = new File("salida_resumidor.txt");
         File f1 = new File("diccionario.txt");
         File f2 = new File("objetos_CREB.txt");
         //*/

        //* descomenta aqui para correr ejemplo full.
        //proceso oracionesSVC para recorte.
        //File f = new File(generar_txt(oracionesSVC));
        File f = new File(oracionesSVC);
        File f1 = new File("aceptados2.txt");
        //File f2 = new File("objetos_CREB.txt");
        File f2 = new File("objetosMinados.txt");
        //File f2 = new File("objetosBAXSMinadosBC.txt");

        //System.out.println("Esto");



        //FileWriter html = new FileWriter(salidaprueba);

        BufferedReader resumidor, diccionario, objetos, resumidor1, diccionario1, objetos1;
        String[] vec;

        int i = 0, j = 0, l = 0, ii = 0, ll = 0, jj = 0, contEventosArchivoActual = 0;
        resumidor = new BufferedReader(new FileReader(f));
        diccionario = new BufferedReader(new FileReader(f1));
        objetos = new BufferedReader(new FileReader(f2));
        resumidor1 = new BufferedReader(new FileReader(f));
        diccionario1 = new BufferedReader(new FileReader(f1));
        objetos1 = new BufferedReader(new FileReader(f2));

        String linea = null, aceptados = null;

        //-----------------Guardando objetos moleculares en un vector-----------------------------------------
        while (objetos.ready()) {
            aceptados = objetos.readLine();
            ii++;
        }

        String[] vec_objetos = new String[ii];

        while (objetos1.ready()) {
            aceptados = objetos1.readLine();
            vec_objetos[jj] = aceptados;
            //System.out.println(vec1[jj]);
            jj++;
        }

        Vector objetos_detallados = new Vector(jj);

        for (int obj = 0; obj < jj; obj++) {
            String[] sinonimos = vec_objetos[obj].split(";");
            int len_sinos = sinonimos.length;
            Vector obj_mol_sinos = new Vector(len_sinos);
            for (int sin = 0; sin < len_sinos; sin++) {
                obj_mol_sinos.add(sinonimos[sin]);
            }

            objetos_detallados.add(obj_mol_sinos);

        }
        //--------------------------------------
        //-----Determinando numero de Columnas y Filas de la Matriz de verbos Aceptados ---------------------------------
        while (diccionario.ready()) {
            linea = diccionario.readLine();
            if (i == 0) {
                vec = linea.split(",");
                l = vec.length;
            }
            i++;
        }
        //System.out.println(l+"Esto");
        //-----------------------------------------------------------
        //-----------------Guardando Todos Los Verbos aceptados con sus conjugados------------------------------------------
        //------------------------------------------

        String[][] conjugados = new String[i][l];

        while (diccionario1.ready()) {
            linea = diccionario1.readLine();
            conjugados[j] = linea.split(",");
            j++;
            //System.out.println(linea);
        }




//------------------------------------------------------------------------
//------------------------Se procesan las oraciones del Resumidor---------
        int cant_objetos_minados = vec_objetos.length;
        Vector sujetos = new Vector(100, 100);
        Vector verbos = new Vector(100, 100);
        Vector objetos_complemento = new Vector(1000, 1000);
        Vector relaciones = new Vector(100, 100);
        //Vector eventos = new Vector(100, 100);


        //System.out.print("base([");
        int cont_lineas = 1;
        int lineas = 0;

        while (resumidor1.readLine() != null) {
            lineas++;
        }
        resumidor1.close();

        try {

            //salidaresumidor="[html]\n";//inicio de cabecera html para 

            baseC.println("Archivo " + oracionesSVC);

            while (resumidor.ready()) {

                //System.out.println(cont_lineas);
                linea = resumidor.readLine();
                //System.out.println(linea);
                int pos_sujeto = linea.indexOf("sujeto(");
                int pos_verbo = linea.indexOf("verbo(");
                String contenido_sujeto = linea.substring(pos_sujeto, pos_verbo);
                // Se comparan todos los objetos moleculares minados con los tokens presentes en 
                // el contenido del sujeto de la oracion en proceso.
                for (int cant_objetos = 0; cant_objetos < cant_objetos_minados; cant_objetos++) {
                    Vector sinoms_suj = (Vector) objetos_detallados.elementAt(cant_objetos);
                    int cant_alias = sinoms_suj.size();
                    for (int alias = 0; alias < cant_alias; alias++) {
                        String obj_comparador = (String) sinoms_suj.elementAt(alias);
                        //String obj_comparador = "'"+(String)sinoms_suj.elementAt(alias);

                        if (contenido_sujeto.indexOf(obj_comparador) != -1) {
                            sujetos.add(sinoms_suj.elementAt(0));
                            break;
                        }

                    }


                }

                // Se determinan los verbos presentes en la oracion y se guardan en el Vector verbos.
                int pos_parent_abierto, pos_parent_cerrado;


                while (pos_verbo != -1) {
                    pos_parent_abierto = linea.indexOf("[", pos_verbo);
                    pos_parent_cerrado = linea.indexOf("]", pos_verbo);
                    verbo = linea.substring(pos_parent_abierto + 1, pos_parent_cerrado);
                    verbos.add(verbo);
                    pos_verbo = linea.indexOf("verbo(", pos_parent_cerrado);
                }

                // Se comparan los verbos conjugados guardados en el Vector verbos con el diccionario
                // para determinar la relacion que corresponde. La variable i refiere numero de verbos en
                // en el diccionario y la variable l el numero de alternativas para cada verbo.
                int cant_verbos_almacenados = verbos.size();

                for (int cant_verbos = 0; cant_verbos < i; cant_verbos++) {
                    for (int cant_conj = 0; cant_conj < l; cant_conj++) {

                        if (verbos.contains(conjugados[cant_verbos][cant_conj])) {
                            relaciones.add(conjugados[cant_verbos][0]);
                            cant_verbos_almacenados--;
                        }

                        if (cant_verbos_almacenados == 0) {
                            break;
                        }

                    }
                }


                // Se determinan los objetos moleculares presentes en el complemento de la oracion en proceso.

                int pos_complemento = linea.indexOf("complemento(");
                int pos_cierre_complemento = linea.indexOf("]", pos_complemento);
                String contenido_complemento = linea.substring(pos_complemento, pos_cierre_complemento);
                // Se comparan todos los objetos moleculares minados con los tokens presentes en 
                // el contenido del complemento de la oracion en proceso.

                for (int cant_objetos = 0; cant_objetos < cant_objetos_minados; cant_objetos++) {
                    Vector sinoms_compl = (Vector) objetos_detallados.elementAt(cant_objetos);
                    int cant_alias = sinoms_compl.size();
                    for (int alias = 0; alias < cant_alias; alias++) {
                        //String comparador = "'"+(String)sinoms_compl.elementAt(alias);
                        String comparador = (String) sinoms_compl.elementAt(alias);
                        if (contenido_complemento.indexOf(comparador) != -1) {
                            objetos_complemento.add(sinoms_compl.elementAt(0));
                        }

                    }


                }

                //--------------------- Armando eventos para la oracion en proceso--------------



                int cant_suj = sujetos.size();
                int cant_rels = relaciones.size();
                int cant_suj_comp = objetos_complemento.size();
                String suj, rel, comple;

                if ((cant_suj != 0) && (cant_rels != 0) && (cant_suj_comp != 0)) {

                    for (int s = 0; s < cant_suj; s++) {
                        suj = (String) sujetos.elementAt(s);
                        for (int r = 0; r < cant_rels; r++) {
                            rel = (String) relaciones.elementAt(r);
                            for (int c = 0; c < cant_suj_comp; c++) {
                                comple = (String) objetos_complemento.elementAt(c);
                                if (!suj.equals(comple)) {
                                    String event = "event(" + "'" + suj + "'" + "," + rel + "," + "'" + comple + "'" + ")";
                                    if (!eventos.contains(event)) {
                                        eventos.add(event);
                                        System.out.println("evento: " + event + "; Linea: " + cont_lineas);
                                        baseC.println("evento: " + event + "; Linea: " + cont_lineas);
                                        contEventosArchivoActual++;
                                    }
                                }

                            }

                        }
                    }
                }

                // Se limpian vectores para procesar siguiente oracion.

                sujetos.clear();
                verbos.clear();
                objetos_complemento.clear();
                relaciones.clear();

                cont_lineas++;

            }



        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("Error en:!!!!!!!!" + cont_lineas);
            //e.printStackTrace();
        }

        //*System.out.print("]).");

     
        System.out.println("eventos provenientes de " + oracionesSVC + " :" + contEventosArchivoActual);
        baseC.println("eventos provenientes de " + oracionesSVC + " :" + contEventosArchivoActual);
        
        return "Base.pl";


    }

    private int printBC(PrintWriter baseC, Vector eventos) throws IOException {
        int cant_eventos;

        cant_eventos = eventos.size();
        String evento;

        for (int i = 0; i < cant_eventos; i++) {
            if (i != (cant_eventos - 1)) {
                evento = (String) eventos.elementAt(i) + ",";

            } else {
                evento = (String) eventos.elementAt(i) + ",";
            }

            baseC.println(evento);
            // System.out.println(evento);
        }

        //eventos.clear();

        return cant_eventos;
    }

    private int printBC1(String archivo, Vector eventos) throws IOException {
        int cant_eventos;
        try (FileWriter fichero = new FileWriter(archivo)) {
            PrintWriter pw = new PrintWriter(fichero);
            cant_eventos = eventos.size();
            String evento;
            pw.println("base([");
            for (int i = 0; i < cant_eventos; i++) {
                if (i != (cant_eventos - 1)) {
                    evento = (String) eventos.elementAt(i) + ",";

                } else {
                    evento = (String) eventos.elementAt(i);
                }

                pw.println(evento);
                // System.out.println(evento);
            }
            pw.println("]).");
            pw.close();
        }

        return cant_eventos;
    }
}
