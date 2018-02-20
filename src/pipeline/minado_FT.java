/*
    Minado_FT.java


    Copyright (C) 2016.
    Yacson Ramirez (yacson.ramirez@gmail.com), Jose Lopez (jlopez@unet.edu.ve).

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
package pipeline;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class minado_FT {

    public void minado(String ruta, float confiabilidad, int Iteraciones, int numeroObjetos, boolean GO, boolean MESH, configuracion config) {

        //lleva un control de los objetos minados y los nuevos objetos encontrados en el proceso
        objetosMineria objetosMineria = new objetosMineria();
        //crea el archivo objetosMinados.txt con la informacion de cada objeto minado en el proceso     
        new objetosMinados().crear_archivo();

        //buca informacion en los diferentes servicios sobre cada homologo en la lista de homologos
        buscarHomologos(listaObjetos_homologosExperto("homologos"), objetosMineria, config, GO, MESH);

        //buca informacion en los diferentes servicios sobre cada objetos agregado por el experto
        buscarObjetosExperto(listaObjetos_homologosExperto("objetos_Experto.txt"), objetosMineria, config, GO, MESH);

        //primera Iteracion partiendo de TFBind
        primeraIteracion(ruta, confiabilidad, numeroObjetos, objetosMineria, config, new ArrayList<lecturas_TFBIND>(), GO, MESH);

        //Segunda Iteracion en adelante partiendo de nuevos objetos encontrados en PDB
        Iteraciones(false, new ArrayList<String>(), numeroObjetos, Iteraciones, objetosMineria, config, 1, GO, MESH);
    }

    public void primeraIteracion(String ruta, float confiabilidad, int numeroObjetos, objetosMineria objetosMineria, configuracion config, ArrayList<lecturas_TFBIND> lecturas, boolean GO, boolean MESH) {
        //Primera Iteracion
        System.out.println("\n\n==== Nivel 0 ====\n");
        objetosMineria.setIteracion(0);
        ArrayList<lecturas_TFBIND> lecturasTFB;
        // si la lista 'lecturas' esta vacia indica que aun no se a consultado a tfbinb y se procede a hacerlo
        if (lecturas.size() == 0) {
            // se envia la ruta del archivo con la region promotora
            // y el procentaje de confiabilidad para obtener las lecturas de tfbind
            lecturasTFB = lecturasTFBID(ruta, confiabilidad);
            System.out.println("* " + lecturasTFB.size() + " Factores de transcripcion encontrados");
            //se guardan el lista de lecturas tfbind en caso de que se reinicie el proceso
            config.setTfbind(lecturasTFB);
            config.guardar();
        } else {
            //si el proceso se recupera desde este punto toma la lista de lecturas de tfbind y continua desde aqui
            lecturasTFB = lecturas;
        }

        for (lecturas_TFBIND lectura : lecturasTFB) {
            //se busca toda la informacion correspondiente al factor de transcripcion
            factorTranscripcion FT = new factorTranscripcion(lectura, numeroObjetos, objetosMineria, GO, MESH);

            //se agregar el factor de transcripcion a los objetos minados
            objetosMineria.getObjetos_minados().add(FT.getID());

            //se agregan los nuevos objetos encontrados a la lista de nuevos objetos
            objetosMineria.agregar_objeto(FT.getComplejoProteinico());

            //se agrega la informacion al archivo mineria/objetosMinados.txt
            //objeto minado, ligandos, nuevos objetos
            new objetosMinados().agregar_objetos(FT);

            //se guarda la informacion del objeto minado en 'mineria/FT.db'
            guardar_Factor_transcripcion(FT);
            //System.out.println("...ok");
            FT = null;
        }
        //se guarda los objetos minados y los nuevos objetos de la iteracion en 'mineria/objetosMineria.db'
        guardar_objetosIteracion(objetosMineria);
        //se guarda el cheklist que indica que ya se culmino con el proceso de la primera iteracion
        config.setLecturas_tfbind(true);
        config.guardar();
        //objetosMineria.imprimir();

    }

    public void Iteraciones(boolean Reanudar, ArrayList<String> Lista, int numeroObjetos, int Iteraciones, objetosMineria objetosMineria, configuracion config, int iter, boolean GO, boolean MESH) {
        //Iteracion 2 en adelante

        //la variable 'iter' indica la iteracion en la que esta el proceso altualmente si este es reanudado
        //La variable Iteraciones indica el numero total de iteraciones que tendra el proceso
        for (int i = iter; i < Iteraciones; i++) {
            System.out.println("\n\n==== Nivel " + (i) + " ====\n");

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
                factorTranscripcion FT = new factorTranscripcion(objeto, i, numeroObjetos, GO, MESH);

                //se agrega el objeto a la lista de objetos minados
                objetosMineria.getObjetos_minados().add(FT.getID());

                //se agrega la informacion de nuevos objetos encontrados a la lista de nuevos objetos ausar en la siguente iteracion
                objetosMineria.agregar_objeto(FT.getComplejoProteinico());

                // se guarda la informacion obtenida sobre el objeto en el archivo objetosMinados.txt
                //nombres, ligandos,
                new objetosMinados().agregar_objetos(FT);

                //se guarda la informacion del objeto minado en mineria/FT.db
                guardar_Factor_transcripcion(FT);
                //System.out.println("Listo....");
            }

            //se guarda la lista de objetos minados y nuevos objetos en la iteracion
            objetosMineria.setIteracion(i);
            guardar_objetosIteracion(objetosMineria);
            //objetosMineria.imprimir();
        }
        //se guarda el checklist que indica que el proceso de iteraciones culmino
        config.setProcesoIteraciones(true);
        config.guardar();
    }

//  se obtinen lecturas de TFBIND recibe la ruta del archivo bloquesconsenso 
//  y el porsentaje de confiabnilidad, debuelve un listado con los factores de transcripcion 
//  encontrados y algunas caracteristicas que ofrece TFBIND
    private ArrayList<lecturas_TFBIND> lecturasTFBID(String ruta, float confiabilidad) {
        lecturas_TFBIND lecturasTFBIND = new lecturas_TFBIND();

        return lecturasTFBIND.leer_de_archivo(ruta, confiabilidad);

    }

    private boolean buscarObjeto(String objeto, factorTranscripcion FT) {
        objetosMineria obj = new objetosMineria();
        ObjectContainer db = Db4o.openFile("mineria/FT.db");
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

    public objetosMineria recuperarObjetosMin() {
        objetosMineria obj = new objetosMineria();
        ObjectContainer db = Db4o.openFile("mineria/objetosMineria.db");

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

    private void guardar_Factor_transcripcion(factorTranscripcion FT) {

        ObjectContainer db = Db4o.openFile("mineria/FT.db");
        try {
            db.store(FT);
        } catch (Exception e) {
            System.out.println("Error al guardar FT.db...");
        } finally {
            db.close();
        }
    }

    public void guardar_objetosIteracion(objetosMineria objetosMin) {
        ObjectContainer db = Db4o.openFile("mineria/objetosMineria.db");
        try {
            db.store(objetosMin);
        } catch (Exception e) {
            System.out.println("Error al guardar en objetosMineria.db...");
        } finally {
            db.close();
        }
    }

    public void obtenerFT() {

        ObjectContainer db = Db4o.openFile("mineria/FT.db");
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
            System.out.println("error");
        } finally {
            db.close();
        }

    }

    public void buscarHomologos(ArrayList<String> homologos, objetosMineria objetosMineria, configuracion config, boolean GO, boolean MESH) {
        System.out.println("\n\n**Leyendo archivo de Homologos...");

        for (String homologo : homologos) {

            System.out.println("busqueda.." + homologo);
            objetos_Experto objExp = new objetos_Experto();// la clase objeto_Experto tiene los aributos necesarios para guardar la informacion de cada objeto
            objExp.setID(homologo);

            //busca la informacion del objeto haciendo uso de HGNC
            ArrayList<HGNC> infgen = new ArrayList<>();
            infgen = new lecturas_HGNC().busquedaInfGen(homologo, GO, MESH);

            //si no existe informacion en HGNC se hace uso de los servicios de pathwaycommons 
            //y con el nombre que se obtenga de intenta buscar de nuevo en HGNC
            if (infgen.size() == 0) {
                lecturas_pathwaycommons pc = new lecturas_pathwaycommons();
                String simbolo = pc.obtenercodigoUP(homologo);
                infgen = new lecturas_HGNC().busquedaInfGen(simbolo, GO, MESH);
            }

            objExp.setHGNC(infgen);
            //se agrega la informacion del objeto a los objetos minados
            new objetosMinados().agregar_objetos(objExp);

            //En ocaciones HGNC no da como respuesta un objeto unico si no que un listado de ellos
            //en estos casos se agrega cada objeto al listado de objetos minados
            objExp.getHGNC().forEach(objhgnc -> objetosMineria.agregar_objeto(objhgnc));

            guardarObjetos_Homologos_Experto(objExp);

            buscar_coincidencia(homologo, objExp.getHGNC());

        }
        //guarda el checklist que indica que culmino la busqueda de informacion sobre los homologos
        config.setHomologos(true);
        config.guardar();
    }

    public void buscarObjetosExperto(ArrayList<String> lista, objetosMineria objetosMineria, configuracion config, boolean GO, boolean MESH) {
        System.out.println("\n\n**Leyendo archivo de Objetos Experto...");

        for (String objeto : lista) {

            System.out.println("busqueda.." + objeto);
            objetos_Experto objExp = new objetos_Experto();// la clase objeto_Experto tiene los aributos necesarios para guardar la informacion de cada objeto
            objExp.setID(objeto);

            //busca la informacion del objeto haciendo uso de HGNC
            ArrayList<HGNC> infgen = new ArrayList<>();
            infgen = new lecturas_HGNC().busquedaInfGen(objeto, GO, MESH);

            //En ocaciones HGNC no da como respuesta un objeto unico si no que un listado de ellos
            //en estos casos se agrega cada objeto al listado de objetos minados
            if (infgen.size() == 0) {
                lecturas_pathwaycommons pc = new lecturas_pathwaycommons();
                String simbolo = pc.obtenercodigoUP(objeto);
                infgen = new lecturas_HGNC().busquedaInfGen(simbolo, GO, MESH);
            }

            objExp.setHGNC(infgen);
            //se agrega la informacion del objeto a los objetos minados
            new objetosMinados().agregar_objetos(objExp);

            //En ocaciones HGNC no da como respuesta un objeto unico si no que un listado de ellos
            //en estos casos se agrega cada objeto al listado de objetos minados
            objExp.getHGNC().forEach(objhgnc -> objetosMineria.agregar_objeto(objhgnc));

            guardarObjetos_Homologos_Experto(objExp);
            buscar_coincidencia(objeto, objExp.getHGNC());
        }
        //guarda el checklist que indica que culmino la busqueda de informacion sobre los objetos del experto
        config.setObjetosExperto(true);
        config.guardar();
    }

    private void buscar_coincidencia(String obj, ArrayList<HGNC> hgnc) {
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
                    idmesh = letMesh.busquedaTerm(obj, 2);
                }
                ontologia.getParent().add(idmesh);
            } catch (Exception e) {

            }

            ontologia.guardarObjeto(ontologia, false, true);

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

    private void guardarObjetos_Homologos_Experto(objetos_Experto objExp) {

        ObjectContainer db = Db4o.openFile("mineria/ObjH_E.db");
        try {
            db.store(objExp);
        } catch (Exception e) {
            System.out.println("Error al guardar en la base de datos ObjH_E.db");
        } finally {

            db.close();
        }

    }

    public void vaciar_bc_pl(boolean GO, boolean MESH) {
        System.out.println("vaciando ontologias y objetos minados a formato .pl");
        new escribirBC("ligando(\'\').", "objetosMinados.pl");
        new escribirBC("transcription_factors(\'\').", "objetosMinados.pl");
        //-------------------------------------------------------
        ObjectContainer dbHE = Db4o.openFile("mineria/ObjH_E.db");
        objetos_Experto objEH = new objetos_Experto();
        try {
            ObjectSet result = dbHE.queryByExample(objEH);
            while (result.hasNext()) {
                try {
                    objetos_Experto obj = (objetos_Experto) result.next();
                    obj.vaciar_pl("objetosMinados.pl");
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {

        } finally {
            dbHE.close();
        }

        //----------------------------------
        ObjectContainer db = Db4o.openFile("mineria/FT.db");
        factorTranscripcion FT = new factorTranscripcion();
        try {

            ObjectSet result = db.queryByExample(FT);
            result.forEach((f) -> {
                factorTranscripcion ft = (factorTranscripcion) f;
                ft.vaciar_pl("objetosMinados.pl");
            });

        } catch (Exception e) {

        } finally {
            db.close();
        }

        ontologiaObjMin ontologias = new ontologiaObjMin();
        ontologias.vaciarOntologia_pl(GO, MESH);

    }
}

//-------------------------------------------------------------------------------------------------//
class objetosMineria {

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
