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

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
//import com.sun.javafx.geom.Vec2d;
import configuracion.configuracion;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import servicios.lecturas_TFBIND;
import configuracion.*;
import estructura.HGNC;
import estructura.factorTranscripcion;
import estructura.objetos_Experto;
import estructura.ontologiaMESH;
import estructura.ontologiaObjMin;
import servicios.lecturas_HGNC;
import servicios.lecturas_MESH;
import servicios.lecturas_pathwaycommons;

public class minado_FT {

    public void minado(String regProm, float confiabilidad, int Iteraciones, int numeroObjetos, boolean GO, boolean MESH, configuracion config,String ruta, String rutaD) {

        //lleva un control de los objetos minados y los nuevos objetos encontrados en el proceso
        objetosMineria objetosMineria = new objetosMineria();
        //crea el archivo minedObjects.txt con la informacion de cada objeto minado en el proceso     
        new objetosMinados().crear_archivo(ruta);

        //buca informacion en los diferentes servicios sobre cada homologo en la lista de homologos
        buscarHomologos(listaObjetos_homologosExperto(rutaD+"/homologous"), objetosMineria, config, GO, MESH,ruta);

        //buca informacion en los diferentes servicios sobre cada objetos agregado por el experto
        buscarObjetosExperto(listaObjetos_homologosExperto(rutaD+"/expert_objects.txt"), objetosMineria, config, GO, MESH,ruta);

        //primera Iteracion partiendo de TFBind
        primeraIteracion(rutaD+"/"+regProm, confiabilidad, numeroObjetos, objetosMineria, config, new ArrayList<lecturas_TFBIND>(), GO, MESH,ruta);

        //Segunda Iteracion en adelante partiendo de nuevos objetos encontrados en PDB
        Iteraciones(false, new ArrayList<String>(), numeroObjetos, Iteraciones, objetosMineria, config, 1, GO, MESH,ruta);
    }

    public void primeraIteracion(String regProm, float confiabilidad, int numeroObjetos, objetosMineria objetosMineria, configuracion config, ArrayList<lecturas_TFBIND> lecturas, boolean GO, boolean MESH,String ruta) {
        //Primera Iteracion
        System.out.println("\n\n"+utilidades.idioma.get(70)+"\n");
        objetosMineria.setIteracion(0);
        ArrayList<lecturas_TFBIND> lecturasTFB;
        utilidades.texto_etapa=utilidades.idioma.get(70);
        utilidades.momento="";
        utilidades.texto_carga="";
        // si la lista 'lecturas' esta vacia indica que aun no se a consultado a tfbinb y se procede a hacerlo
        if (lecturas.size() == 0) {
            // se envia la ruta del archivo con la region promotora
            // y el procentaje de confiabilidad para obtener las lecturas de tfbind
            lecturasTFB = lecturasTFBID(regProm, confiabilidad);
            utilidades.momento="* " + lecturasTFB.size() +" "+utilidades.idioma.get(71);
            //System.out.println("* " + lecturasTFB.size() + " "+utilidades.idioma.get(71));
            //se guardan el lista de lecturas tfbind en caso de que se reinicie el proceso
            config.setTfbind(lecturasTFB);
            config.guardar(ruta);
        } else {
            //si el proceso se recupera desde este punto toma la lista de lecturas de tfbind y continua desde aqui
            lecturasTFB = lecturas;
        }

        for (lecturas_TFBIND lectura : lecturasTFB) {
            utilidades.momento+="\n"+utilidades.idioma.get(142)+"" + lectura.getFactor();
            new utilidades().carga();
            //se busca toda la informacion correspondiente al factor de transcripcion
            factorTranscripcion FT = new factorTranscripcion(lectura, numeroObjetos, objetosMineria, GO, MESH,ruta);

            //se agregar el factor de transcripcion a los objetos minados
            objetosMineria.getObjetos_minados().add(FT.getID());

            //se agregan los nuevos objetos encontrados a la lista de nuevos objetos
            objetosMineria.agregar_objeto(FT.getComplejoProteinico());

            //se agrega la informacion al archivo mineria/minedObjects.txt
            //objeto minado, ligandos, nuevos objetos
            new objetosMinados().agregar_objetos(FT);

            //se guarda la informacion del objeto minado en 'mineria/TF.db'
            guardar_Factor_transcripcion(FT,ruta);
            //System.out.println("...ok");
            FT = null;
        }
        //se guarda los objetos minados y los nuevos objetos de la iteracion en 'mineria/mineryObjects.db'
        guardar_objetosIteracion(objetosMineria,ruta);
        //se guarda el cheklist que indica que ya se culmino con el proceso de la primera iteracion
        config.setLecturas_tfbind(true);
        config.guardar(ruta);
        //objetosMineria.imprimir();

    }

    public void Iteraciones(boolean Reanudar, ArrayList<String> Lista, int numeroObjetos, int Iteraciones, objetosMineria objetosMineria, configuracion config, int iter, boolean GO, boolean MESH,String ruta) {
        //Iteracion 2 en adelante
        
        //la variable 'iter' indica la iteracion en la que esta el proceso altualmente si este es reanudado
        //La variable Iteraciones indica el numero total de iteraciones que tendra el proceso
        utilidades.momento="";
        utilidades.texto_carga="";
        for (int i = iter; i < Iteraciones; i++) {
            utilidades.texto_etapa="\n\n===="+utilidades.idioma.get(72) + " " + (i) + " ====\n";
            new utilidades().carga();
            //si Reanudar = true se toman los objetos que llegan por los parametros
            //Lista = en este llegan los objetos que faltan por minar en la iteracion
            //objetosMineria = tiene la informacion de los objetos minados y nuevos objetos que van hasta el momento
            //si Reanudar = false el proceso indica que el proceso comienza de 0
            if (!Reanudar) {
                Lista = objetosMineria.getNuevos_objetos(); // se cargan los nuevos objetos encontrados en la iteracion anterior
                objetosMineria.setNuevos_objetos(new ArrayList<String>());// se borran los objetos nuevos de la iteracion anterior para agregar los nuevos objetos de la iteracion actual
            }
            Reanudar = false;

            for (String objeto : Lista) {
                // se busca toda la informacion correspondiente al objeto
                // nombre, simbolo, sinonimos, complejos, nuevos objetos, ligandos
               
                utilidades.momento+="\n"+utilidades.idioma.get(142)+"" + objeto;
                new utilidades().carga();
                factorTranscripcion FT = new factorTranscripcion(objeto, i, numeroObjetos, GO, MESH,ruta);

                //se agrega el objeto a la lista de objetos minados
                objetosMineria.getObjetos_minados().add(FT.getID());

                //se agrega la informacion de nuevos objetos encontrados a la lista de nuevos objetos ausar en la siguente iteracion
                objetosMineria.agregar_objeto(FT.getComplejoProteinico());

                // se guarda la informacion obtenida sobre el objeto en el archivo minedObjects.txt
                //nombres, ligandos,
                new objetosMinados().agregar_objetos(FT);

                //se guarda la informacion del objeto minado en mineria/TF.db
                guardar_Factor_transcripcion(FT,ruta);
                //System.out.println("Listo....");
            }

            //se guarda la lista de objetos minados y nuevos objetos en la iteracion
            objetosMineria.setIteracion(i);
            guardar_objetosIteracion(objetosMineria,ruta);
            //objetosMineria.imprimir();
        }
        //se guarda el checklist que indica que el proceso de iteraciones culmino
        config.setProcesoIteraciones(true);
        config.guardar(ruta);
    }

//  se obtinen lecturas de TFBIND recibe la ruta del archivo bloquesconsenso 
//  y el porsentaje de confiabnilidad, debuelve un listado con los factores de transcripcion 
//  encontrados y algunas caracteristicas que ofrece TFBIND
    private ArrayList<lecturas_TFBIND> lecturasTFBID(String ruta, float confiabilidad) {
        lecturas_TFBIND lecturasTFBIND = new lecturas_TFBIND();

        return lecturasTFBIND.leer_de_archivo(ruta, confiabilidad);

    }

    private boolean buscarObjeto(String objeto, factorTranscripcion FT,String ruta) {
        objetosMineria obj = new objetosMineria();
        ObjectContainer db = Db4o.openFile(ruta+"/TF.db");
        factorTranscripcion ft = new factorTranscripcion();
        ft.setID(objeto);
        try {
            ObjectSet result = db.queryByExample(ft);
            while (result.hasNext()) {
                FT = (factorTranscripcion) result.next();
                return true;
            }
        } catch (Exception e) {

        } finally {
            db.close();
        }

        return false;
    }

    public objetosMineria recuperarObjetosMin(String ruta) {
        objetosMineria obj = new objetosMineria();
        ObjectContainer db = Db4o.openFile(ruta+"/mineryObjects.db");

        try {
            ObjectSet result = db.queryByExample(obj);
            while (result.hasNext()) {
                obj = (objetosMineria) result.next();
            }
        } catch (Exception e) {
        } finally {
            db.close();
        }

        return obj;
    }

    public void crearCarpeta(String nombre) {
        File f = new File(nombre);
        try {
            borrarDirectorio(f);
        } catch (Exception e) {

        }
        if (f.delete()) {
            // System.out.println("El directorio   ha sido borrado correctamente");
        } else {
            //System.out.println("El directorio  no se ha podido borrar");
        }

        File file = new File(nombre);
        file.mkdir();

    }

    // elimina todos los archivos contenidos dentro del directorio
    private void borrarDirectorio(File directorio) {
        File[] ficheros = directorio.listFiles();
        for (int i = 0; i < ficheros.length; i++) {
            if (ficheros[i].isDirectory()) {
                borrarDirectorio(ficheros[i]);
            }
            ficheros[i].delete();
        }
    }

    public void crear_archivo(String nombre) {

        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter(nombre);
        } catch (IOException ex) {
            Logger.getLogger(minado_FT.class.getName()).log(Level.SEVERE, null, ex);
        }
        pw = new PrintWriter(fichero);

    }

    public void borrar_archivo(String nombre) {
        try {
            File ficherod = new File(nombre);
            ficherod.delete();
        } catch (Exception e) {

        }
    }

    private void guardar_Factor_transcripcion(factorTranscripcion FT,String ruta) {

        ObjectContainer db = Db4o.openFile(ruta+"/TF.db");
        try {
            db.store(FT);
        } catch (Exception e) {
            System.out.println(utilidades.idioma.get(73));
        } finally {
            db.close();
        }
    }

    public void guardar_objetosIteracion(objetosMineria objetosMin,String ruta) {
        ObjectContainer db = Db4o.openFile(ruta+"/mineryObjects.db");
        try {
            db.store(objetosMin);
        } catch (Exception e) {
            System.out.println(utilidades.idioma.get(74));
        } finally {
            db.close();
        }
    }

    public void obtenerFT() {

        ObjectContainer db = Db4o.openFile("mineria/TF.db");
        factorTranscripcion FT = new factorTranscripcion();

        try {

            ObjectSet result = db.queryByExample(FT);
            while (result.hasNext()) {
                try {
                    factorTranscripcion ft = (factorTranscripcion) result.next();
                    ft.imprimir();
                    System.out.println("====================================================");
                } catch (Exception e) {

                }
            }
        } catch (Exception e) {
            System.out.println(utilidades.idioma.get(75));
        } finally {
            db.close();
        }

    }

    public void buscarHomologos(ArrayList<String> homologos, objetosMineria objetosMineria, configuracion config, boolean GO, boolean MESH,String ruta) {
        new utilidades().limpiarPantalla();
        utilidades.texto_etapa=utilidades.idioma.get(147);
        System.out.println("\n\n**"+utilidades.idioma.get(76));

        for (String homologo : homologos) {
            utilidades.texto_carga="";
            utilidades.momento+="\n"+utilidades.idioma.get(78)+" " + homologo;
            //System.out.println(utilidades.idioma.get(78)+" " + homologo);
            objetos_Experto objExp = new objetos_Experto();// la clase objeto_Experto tiene los aributos necesarios para guardar la informacion de cada objeto
            objExp.setID(homologo);

            //busca la informacion del objeto haciendo uso de HGNC
            ArrayList<HGNC> infgen = new ArrayList<>();
            infgen = new lecturas_HGNC().busquedaInfGen(homologo, GO, MESH,ruta);

            //si no existe informacion en HGNC se hace uso de los servicios de pathwaycommons 
            //y con el nombre que se obtenga de intenta buscar de nuevo en HGNC
            if (infgen.size() == 0) {
                lecturas_pathwaycommons pc = new lecturas_pathwaycommons();
                String simbolo = pc.obtenercodigoUP(homologo);
                infgen = new lecturas_HGNC().busquedaInfGen(simbolo, GO, MESH,ruta);
            }

            objExp.setHGNC(infgen);
            //se agrega la informacion del objeto a los objetos minados
            new objetosMinados().agregar_objetos(objExp);

            //En ocaciones HGNC no da como respuesta un objeto unico si no que un listado de ellos
            //en estos casos se agrega cada objeto al listado de objetos minados
            objExp.getHGNC().forEach(objhgnc -> objetosMineria.agregar_objeto(objhgnc));

            guardarObjetos_Homologos_Experto(objExp,ruta);

            buscar_coincidencia(homologo, objExp.getHGNC(),ruta);

        }
        //guarda el checklist que indica que culmino la busqueda de informacion sobre los homologos
        config.setHomologos(true);
        config.guardar(ruta);
    }

    public void buscarObjetosExperto(ArrayList<String> lista, objetosMineria objetosMineria, configuracion config, boolean GO, boolean MESH,String ruta) {
        
        new utilidades().limpiarPantalla();
        System.out.println("\n\n**"+utilidades.idioma.get(77));
        utilidades.texto_etapa=utilidades.idioma.get(148);
        utilidades.momento="";
       
        
        for (String objeto : lista) {
            utilidades.texto_carga="";
            utilidades.momento+="\n"+utilidades.idioma.get(78)+" " + objeto;
           // System.out.println(utilidades.idioma.get(78)+" " + objeto);
            objetos_Experto objExp = new objetos_Experto();// la clase objeto_Experto tiene los aributos necesarios para guardar la informacion de cada objeto
            objExp.setID(objeto);

            //busca la informacion del objeto haciendo uso de HGNC
            ArrayList<HGNC> infgen = new ArrayList<>();
            infgen = new lecturas_HGNC().busquedaInfGen(objeto, GO, MESH,ruta);

            //En ocaciones HGNC no da como respuesta un objeto unico si no que un listado de ellos
            //en estos casos se agrega cada objeto al listado de objetos minados
            if (infgen.size() == 0) {
                lecturas_pathwaycommons pc = new lecturas_pathwaycommons();
                String simbolo = pc.obtenercodigoUP(objeto);
                infgen = new lecturas_HGNC().busquedaInfGen(simbolo, GO, MESH,ruta);
            }

            objExp.setHGNC(infgen);
            //se agrega la informacion del objeto a los objetos minados
            new objetosMinados().agregar_objetos(objExp);

            //En ocaciones HGNC no da como respuesta un objeto unico si no que un listado de ellos
            //en estos casos se agrega cada objeto al listado de objetos minados
            objExp.getHGNC().forEach(objhgnc -> objetosMineria.agregar_objeto(objhgnc));

            guardarObjetos_Homologos_Experto(objExp,ruta);
            buscar_coincidencia(objeto, objExp.getHGNC(),ruta);
        }
        //guarda el checklist que indica que culmino la busqueda de informacion sobre los objetos del experto
        config.setObjetosExperto(true);
        config.guardar(ruta);
    }

    private void buscar_coincidencia(String obj, ArrayList<HGNC> hgnc,String ruta) {
        boolean encontrado = false;

        for (HGNC h : hgnc) {
            ArrayList<String> lista = h.ListaNombres();
            if (lista.contains(obj)) {
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            ontologiaObjMin ontologia = new ontologiaObjMin();
            ontologia.setNombre(obj);
            try {
                lecturas_MESH letMesh = new lecturas_MESH();
                //System.out.println(hgnc.getSimbolo() + "  " + hgnc.getNombre());
                String idmesh = letMesh.busquedaTerm(obj.replace(" ", "+"), 1);
                if (idmesh == null) {
                    idmesh = letMesh.busquedaTerm(obj.replace(" ", "+"), 2);
                }
                ontologia.getParent().add(idmesh);
            } catch (Exception e) {

            }

            ontologia.guardarObjeto(ontologia, false, true,ruta);

        }

    }

    private ArrayList<String> listaObjetos_homologosExperto(String ruta) {
        ArrayList<String> lista = new ArrayList<>();
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            archivo = new File(ruta);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String lectura;
            while ((lectura = br.readLine()) != null) {
                lista.add(lectura);
            }
        } catch (Exception e) {
        }
        return lista;
    }

    private void guardarObjetos_Homologos_Experto(objetos_Experto objExp, String ruta) {

        ObjectContainer db = Db4o.openFile(ruta+"/homologousObjects.db");
        try {
            db.store(objExp);
        } catch (Exception e) {
            System.out.println(utilidades.idioma.get(79));
        } finally {

            db.close();
        }

    }

    public void vaciar_bc_pl(boolean GO, boolean MESH, configuracion config,String ruta) {
        new utilidades().limpiarPantalla();
        utilidades.texto_etapa=utilidades.idioma.get(80);
        utilidades.momento="";
        utilidades.texto_carga="";
        //System.out.print(utilidades.idioma.get(80));
        new escribirBC("ligando(\'\').", ruta+"/minedObjects.pl");
        new escribirBC("transcription_factors(\'\').", ruta+"/minedObjects.pl");
        //-------------------------------------------------------
        ObjectContainer dbHE = Db4o.openFile(ruta+"/homologousObjects.db");
        objetos_Experto objEH = new objetos_Experto();
        try {
            ObjectSet result = dbHE.queryByExample(objEH);
            while (result.hasNext()) {
                try {
                    objetos_Experto obj = (objetos_Experto) result.next();
                    obj.vaciar_pl(ruta);
                    
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {

        } finally {
            dbHE.close();
        }

        
        //----------------------------------
        ObjectContainer db = Db4o.openFile(ruta+"/TF.db");
        factorTranscripcion FT = new factorTranscripcion();
        try {

            ObjectSet result = db.queryByExample(FT);
            result.forEach((f) -> {
                factorTranscripcion ft = (factorTranscripcion) f;
                ft.vaciar_pl(ruta);
            });

        } catch (Exception e) {

        } finally {
            db.close();
        }

        ontologiaObjMin ontologias = new ontologiaObjMin();
        ontologias.vaciarOntologia_pl(GO, MESH,ruta);

        config.setVaciado_pl(true);
        config.guardar(ruta);
    }

        

}

