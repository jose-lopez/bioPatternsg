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
import configuracion.configuracion;
import configuracion.utilidades;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jpl7.Query;
import org.jpl7.Term;

/**
 *
 * @author yacson
 */
public class patrones {

    ArrayList<pathway> patrones = new ArrayList<>();
    boolean detener = false;

    public void inferir_patrones(configuracion config, String ruta) {

        try {
            new utilidades().limpiarPantalla();
            utilidades.momento = "";
            utilidades.texto_carga = "";
            utilidades.texto_etapa = utilidades.idioma.get(153);
            System.out.println(utilidades.colorTexto1 + utilidades.titulo);
            System.out.println(utilidades.colorTexto1 + utilidades.proceso);
            System.out.println("\n" + utilidades.colorTexto2 + utilidades.texto_etapa);

            ArrayList<String> objRestricion = menuRestricionObjetos();

            ArrayList<String> objCierre = menuMotivos();

            try {
                resumirBaseC(ruta);
            } catch (Exception e) {
            }
            File pathways = new File(ruta + "/pathways.txt");
            File eventsDoc = new File(ruta + "/pathways.txt");

            /*
            // En este punto se asume que hay un archivo de eventos documentados que han sido etiquetados como P, F o U.
            // Este metodo lee ese archivo y actualiza la kBase.pl para agregar nuevos eventos del ususario y elimiar los eventos falsos
            // que el usuario haya identificado.
            if (eventsDoc.exists()) {
                BufferedReader e = new BufferedReader(new FileReader(eventsDoc));
                if (!e.readLine().contains("///**")) {
                    kbase_update(config, ruta);
                }
            }
             */
            // System.out.println(objRestricion);
            borrar_archivo(ruta + "/pathways.txt");
            borrar_archivo(ruta + "/pathways.db");

            String v = "style_check(-discontiguous).";
            Query q0 = new Query(v);
            q0.hasSolution();

            String objPatr = "['" + ruta + "/pathwaysObjects'].";
            Query q1 = new Query(objPatr);
            q1.hasSolution();

            cargarBaseC(ruta);

            String archivo = "[scripts/pathwaysJPL].";
            Query q = new Query(archivo);
            q.hasSolution();

            ArrayList<String> listaInicio = inicio(objRestricion);

            ArrayList<String> objEnlace = new ArrayList<>();

            listaInicio.parallelStream().forEach((obj) -> {
                String sep1[] = obj.split(",");
                if (!objEnlace.contains(sep1[2])) {
                    objEnlace.add(sep1[2]);
                }
                //System.out.println("evento inicio:  " + obj);
            });

            ArrayList<String> listaFin = new ArrayList<>();

            objCierre.forEach(obj -> listaFin.addAll(fin(obj, objRestricion)));

            if (objCierre.isEmpty()) {
                listaFin.addAll(fin("", objRestricion));
            }

            ArrayList<String> FT = new ArrayList<>();

            listaFin.forEach((String fin) -> {
                String sep[] = fin.split(",");
                if (!FT.contains(sep[0])) {
                    FT.add(sep[0]);
                }

                if (!objCierre.contains(sep[2])) {
                    objCierre.add(sep[2]);
                }

                //System.out.println("evento fin:  " + fin);
            });
            //patrones de 2 eventos
            patron_2_eventos(objCierre, listaInicio, objEnlace, ruta);

            objEnlace.forEach(enlace -> intermedios(new ArrayList<String>(), enlace, FT, "", 0, listaInicio, listaFin, objCierre, objRestricion, ruta));

            guardar_Patron(ruta);

            config.setInferirPatrones(true);
            config.guardar(ruta);

            // Una vez que se han inferido los patrones, el siguiente metodo extrae desde pathways.txt todos los eventos que participan en ellos.
            // Para cada evento se indaga en la BC docimentada kBaseDoc y se extraen todas las oraciones posibles asociadas a los mismos.
            // El resultado de este metodo es el archivo eventsDocs.txt. Ek usuario puede proceder a etiquetas cada evento como Positivo, Falso o 
            // agregado por si mismo (tipo U). Esta version modificada luego es utilizada por el metodo kbase_update para modificar kBase.pl.
            if (pathways.exists()) {
                events_documentation(config, ruta);
            }

        } catch (IOException ex) {
            Logger.getLogger(patrones.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void events_documentation(configuracion config, String ruta) throws IOException {

        BufferedReader pathways = null, kBaseDoc = null, eventsDocHis = null, eventsDocReader;

        //File evsDoc = new File(ruta + "/" + "eventsDoc.txt");
        //File evsNotD = new File(ruta + "/" + "eventsNotDoc.txt");
        try {
            if (new File(ruta + "/pathways.txt").exists()) {
                pathways = new BufferedReader(new FileReader(new File(ruta + "/pathways.txt")));
            } else {
                System.out.println("The file pathways.txt does not exist");
            }
            if (new File(ruta + "/kBaseDoc").exists()) {
                kBaseDoc = new BufferedReader(new FileReader(new File(ruta + "/kBaseDoc")));
            } else {
                System.out.println("The file kBaseDoc does not exist");
            }

            BufferedReader relations = new BufferedReader(new FileReader(new File("scripts/relations.txt")));
            BufferedReader relationsFunctions = new BufferedReader(new FileReader(new File("scripts/relations-functions.txt")));

            String line = "", lineaEvent = "";
            String[] events = null;
            boolean event_printed = false;
            Vector events_pathways = new Vector(100);
            Vector eventsNoDoc = new Vector(10, 50);

            if (pathways == null || kBaseDoc == null) {
                System.out.println("The file eventsDoc.txt can not be produced because either the file pathways.txt or kBaseDoc does not exist");

            } else {

                pathways.mark(1000000);

                try (PrintWriter eventsDocu = new PrintWriter(new FileWriter(ruta + "/" + "eventsDoc.txt")); PrintWriter eventsNotDocu = new PrintWriter(new FileWriter(ruta + "/" + "eventsNotDoc.txt"))) {

                    try {

                        while (pathways.ready()) {

                            line = pathways.readLine();

                            if (line.startsWith("'")) {

                                events = line.split(";");

                                for (String e : events) {

                                    if (!events_pathways.contains(e)) {
                                        events_pathways.add(e);
                                        //System.out.println("event(" + (String) e + ")" + "\n");
                                    }
                                }

                            }
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(patrones.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    kBaseDoc.mark(10000000);
                    relations.mark(1000);

                    //--------------------------------------
                    //-----Determinando numero de Columnas y Filas de la Matriz de verbos  ---------------------------------
                    int i = 0, l = 0;
                    String[] vec;
                    while (relations.ready()) {
                        line = relations.readLine();
                        if (i == 0) {
                            vec = line.split(",");
                            l = vec.length;
                        }
                        i++;
                    }
                    relations.reset();

                    String[][] verbs = new String[i][l];
                    int j = 0;
                    while (relations.ready()) {
                        line = relations.readLine();
                        verbs[j] = line.split(",");
                        j++;
                        //System.out.println(linea);
                    }

                    Vector regulate = new Vector(50);
                    Vector inhibit = new Vector(50);
                    Vector associate = new Vector(50);
                    Vector bind = new Vector(50);

                    line = relationsFunctions.readLine();

                    if (line.startsWith("//------------Regulate")) {
                        line = relationsFunctions.readLine();
                        do {
                            regulate.add(line);
                            line = relationsFunctions.readLine();

                        } while (!line.startsWith("//------------Inhibit"));
                    }

                    if (line.startsWith("//------------Inhibit")) {
                        line = relationsFunctions.readLine();
                        do {
                            inhibit.add(line);
                            line = relationsFunctions.readLine();

                        } while (!line.startsWith("//------------Associate"));
                    }

                    if (line.startsWith("//------------Associate")) {
                        line = relationsFunctions.readLine();
                        do {
                            associate.add(line);
                            line = relationsFunctions.readLine();

                        } while (!line.startsWith("//------------Bind"));
                    }

                    if (line.startsWith("//------------Bind")) {

                        while (relationsFunctions.ready()) {
                            line = relationsFunctions.readLine();
                            bind.add(line);

                        }
                    }

                    String eventKB, verb, possibleEvent;
                    String[] eventSplitted;
                    Vector relats;
                    boolean eventDocsEmpty = true;

                    for (Object e : events_pathways) {

                        eventKB = e.toString();
                        eventSplitted = eventKB.split(",");
                        verb = eventSplitted[1];

                        //lineaEvent = kBaseDoc.readLine();
                        if (regulate.contains(verb)) {
                            relats = regulate;
                        } else if (inhibit.contains(verb)) {
                            relats = inhibit;
                        } else if (associate.contains(verb)) {
                            relats = associate;
                        } else {
                            relats = bind;
                        }

                        String lineaEventHistory;

                        for (Object r : relats) {

                            do {

                                lineaEvent = kBaseDoc.readLine();

                                if (lineaEvent.contains("evento: event('")) {

                                    possibleEvent = "event(" + eventSplitted[0] + "," + r + "," + eventSplitted[2] + ")";
                                    if (lineaEvent.contains(possibleEvent)) {

                                        lineaEvent = kBaseDoc.readLine();

                                        if (!eventsDocHistoryContains(ruta, possibleEvent, lineaEvent)) {

                                            if (!eventsNoDoc.contains(possibleEvent)) {
                                                eventsNotDocu.print(possibleEvent + "\n");
                                                eventsNoDoc.add(possibleEvent);
                                            }
                                            eventsDocu.print(possibleEvent + "\n");
                                            eventsDocu.print(lineaEvent + "\n" + "\n");
                                            eventDocsEmpty = false;

                                        }

                                    }

                                }

                            } while (kBaseDoc.ready());

                            kBaseDoc.reset();

                        }
                        kBaseDoc.reset();

                    }
                    //
                    if (eventDocsEmpty) {
                        eventsDocu.print("///** The regulatory events in pathways.txt are already labelled in eventsDoc-History.txt **///");

                    }

                    eventsDocu.close();
                }

            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(patrones.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pathways.close();
            } catch (IOException ex) {
                Logger.getLogger(patrones.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void pathways_documentation(configuracion config, String ruta) {

        try {

            if (!new File(ruta + "/" + "pathways.txt").exists()) {

                System.out.println("The file pathways.txt does not exist.");

            } else {

                BufferedReader eventsDocHistory, pathways;
                FileWriter pathwaysDocu = new FileWriter(ruta + "/" + "pathwaysDoc.txt");
                PrintWriter pathwaysDoc;
                pathwaysDoc = new PrintWriter(pathwaysDocu);

                eventsDocHistory = new BufferedReader(new FileReader(new File(ruta + "/" + "eventsDoc-History.txt")));
                pathways = new BufferedReader(new FileReader(new File(ruta + "/" + "pathways.txt")));

                eventsDocHistory.mark(10000000);

                String[] events, eventSplitted, lineSplitted;
                String line, verb;
                Vector relats, linesAlreadyPrinted = null;

                BufferedReader relations = new BufferedReader(new FileReader(new File("scripts/relations.txt")));
                BufferedReader relationsFunctions = new BufferedReader(new FileReader(new File("scripts/relations-functions.txt")));

                relations.mark(1000);

                //--------------------------------------
                //-----Determinando numero de Columnas y Filas de la Matriz de verbos  ---------------------------------
                int i = 0, l = 0;
                String[] vec;
                while (relations.ready()) {
                    line = relations.readLine();
                    if (i == 0) {
                        vec = line.split(",");
                        l = vec.length;
                    }
                    i++;
                }
                relations.reset();

                String[][] verbs = new String[i][l];
                int j = 0;
                while (relations.ready()) {
                    line = relations.readLine();
                    verbs[j] = line.split(",");
                    j++;
                    //System.out.println(linea);
                }

                Vector regulate = new Vector(50);
                Vector inhibit = new Vector(50);
                Vector associate = new Vector(50);
                Vector bind = new Vector(50);
                linesAlreadyPrinted = new Vector(100);

                line = relationsFunctions.readLine();

                if (line.startsWith("//------------Regulate")) {
                    line = relationsFunctions.readLine();
                    do {
                        regulate.add(line);
                        line = relationsFunctions.readLine();

                    } while (!line.startsWith("//------------Inhibit"));
                }

                if (line.startsWith("//------------Inhibit")) {
                    line = relationsFunctions.readLine();
                    do {
                        inhibit.add(line);
                        line = relationsFunctions.readLine();

                    } while (!line.startsWith("//------------Associate"));
                }

                if (line.startsWith("//------------Associate")) {
                    line = relationsFunctions.readLine();
                    do {
                        associate.add(line);
                        line = relationsFunctions.readLine();

                    } while (!line.startsWith("//------------Bind"));
                }

                if (line.startsWith("//------------Bind")) {

                    while (relationsFunctions.ready()) {
                        line = relationsFunctions.readLine();
                        bind.add(line);

                    }
                }

                while (pathways.ready()) {

                    line = pathways.readLine();

                    if (line.startsWith("'")) {

                        pathwaysDoc.print("---------------------------------------------------: " + "\n" + "\n");

                        pathwaysDoc.print("PATHWAY: " + line + "\n" + "\n");

                        events = line.split(";");

                        for (String e : events) {

                            pathwaysDoc.print("----> event: " + e + "\n" + "\n");

                            eventSplitted = e.split(",");
                            verb = eventSplitted[1];
                            String event, lineFromEventsDocHistory;

                            //lineaEvent = kBaseDoc.readLine();
                            if (regulate.contains(verb)) {
                                relats = regulate;
                            } else if (inhibit.contains(verb)) {
                                relats = inhibit;
                            } else if (associate.contains(verb)) {
                                relats = associate;
                            } else {
                                relats = bind;
                            }

                            boolean contains;
                            for (Object v : relats) {

                                event = "event(" + eventSplitted[0] + "," + v + "," + eventSplitted[2] + ")";
                                int linesReadFromEventHistory = 0;

                                while (eventsDocHistory.ready()) {

                                    lineFromEventsDocHistory = eventsDocHistory.readLine();
                                    linesReadFromEventHistory++;

                                    if (lineFromEventsDocHistory.contains(event)) {

                                        lineSplitted = lineFromEventsDocHistory.split(":");

                                        if (lineSplitted[1].equals("P") || lineSplitted[1].equals("U")) {

                                            lineFromEventsDocHistory = eventsDocHistory.readLine();
                                            linesReadFromEventHistory++;

                                            contains = false;

                                            for (Object linePrinted : linesAlreadyPrinted) {

                                                if (((String) linePrinted).contentEquals(lineFromEventsDocHistory)) {
                                                    contains = true;
                                                }

                                            }

                                            if (!contains) {

                                                pathwaysDoc.print(lineFromEventsDocHistory + "\n" + "\n");
                                                linesAlreadyPrinted.add(lineFromEventsDocHistory);

                                            }

                                        } else if (!lineSplitted[1].equals("F")) {
                                            System.out.println("The event " + e + " on line #: " + linesReadFromEventHistory + lineFromEventsDocHistory + " is not well labelled" + "\n");
                                            //System.out.println(lineFromEventsDocHistory);

                                            System.exit(0);

                                        }

                                    }

                                }
                                eventsDocHistory.reset();

                            }
                            linesAlreadyPrinted.clear();
                        }

                    }
                }
                eventsDocHistory.close();
                pathwaysDoc.close();
            }

        } catch (IOException ex) {
            Logger.getLogger(patrones.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private boolean eventsDocHistoryContains(String ruta, String possibleEvent, String possibleLine) {

        Boolean contains = false;

        File file = new File(ruta + "/eventsDoc-History.txt");

        String lineEventHistory;

        BufferedReader eventsDocHistory = null;

        if (file.exists()) {
            try {
                try {
                    eventsDocHistory = new BufferedReader(new FileReader(ruta + "/eventsDoc-History.txt"));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(patrones.class.getName()).log(Level.SEVERE, null, ex);
                }

                // Aqui el codigo que verifica si el evento y la linea ya han sido evaluados por el usuario.
                while (eventsDocHistory.ready()) {

                    lineEventHistory = eventsDocHistory.readLine();

                    if (lineEventHistory.contains(possibleEvent)) {
                        lineEventHistory = eventsDocHistory.readLine();
                        if (lineEventHistory.equals(possibleLine)) {
                            contains = true;
                            eventsDocHistory.close();
                            break;
                        }

                    }

                }
                eventsDocHistory.close();

            } catch (IOException ex) {
                Logger.getLogger(patrones.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(patrones.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return contains;
    }

    private void eventsDocHistoryADD(String ruta, String possibleEvent, String lineaEvent) {

        File file = new File(ruta + "/eventsDoc-History.txt");
        FileWriter eventsDocHis = null;

        if (file.exists()) {
            try {
                try {
                    eventsDocHis = new FileWriter(file, true);
                } catch (IOException ex) {
                    Logger.getLogger(patrones.class.getName()).log(Level.SEVERE, null, ex);
                }
                PrintWriter eventsDocHistory = new PrintWriter(eventsDocHis);
                eventsDocHistory.print(possibleEvent + "\n");
                eventsDocHistory.print(lineaEvent + "\n" + "\n");
                eventsDocHistory.close();
                eventsDocHis.close();
            } catch (IOException ex) {
                Logger.getLogger(patrones.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void kbase_update(configuracion config, String ruta) {

        BufferedReader eventsDoc = null;
        BufferedReader kBase = null;
        File kb = new File(ruta + "/kBase.pl");

        try {
            if (kb.exists()) {
                kBase = new BufferedReader(new FileReader(kb));
            } else {
                System.out.println("The file kbase.pl does not exist therefore the KB cannnot be updated");
            }
            try {
                if (new File(ruta + "/eventsDoc.txt").exists()) {
                    eventsDoc = new BufferedReader(new FileReader(new File(ruta + "/eventsDoc.txt")));
                } else {
                    System.out.println("The file eventsDoc.txt does not exist therefore the KB cannnot be updated");
                }
            } catch (IOException ex) {
                Logger.getLogger(patrones.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(patrones.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (kBase != null && eventsDoc != null) {

            try {
                int linesNumber = 0;
                String lineEvent = null, lineEventDoc, ev;
                boolean eventlabelled;
                String[] splittedEvent;
                Vector positiveEvents = new Vector(100);
                Vector falseEvents = new Vector(100);
                Vector userEvents = new Vector(100);
                if (eventsDoc.ready()) {

                    lineEvent = eventsDoc.readLine();

                    if (lineEvent.contains("///**")) {
                        System.out.println(" The events in the pathways.txt file are already labelled and the KBase.pl is updated ");
                        System.exit(0);
                    } else {

                        while (eventsDoc.ready()) {

                            eventlabelled = false;
                            linesNumber++;

                            if (lineEvent.startsWith("event('")) {

                                if (lineEvent.contains(":")) {

                                    splittedEvent = lineEvent.split(":");

                                    if (splittedEvent[1].equals("P")) {
                                        positiveEvents.add(splittedEvent[0]);
                                        eventlabelled = true;
                                    }

                                    if (splittedEvent[1].equals("F")) {
                                        falseEvents.add(splittedEvent[0]);
                                        eventlabelled = true;
                                    }

                                    if (splittedEvent[1].equals("U")) {
                                        userEvents.add(splittedEvent[0]);
                                        eventlabelled = true;
                                    }

                                    if (!eventlabelled) {
                                        System.out.println("The event at the line " + linesNumber + " in evenDoc.txt is not well labelled. The labels P, F or U, must be used");
                                        System.exit(0);
                                    }

                                    lineEventDoc = eventsDoc.readLine();

                                    if (!eventsDocHistoryContains(ruta, lineEvent, lineEventDoc)) {
                                        eventsDocHistoryADD(ruta, lineEvent, lineEventDoc);

                                    }

                                } else {
                                    System.out.println("The event at the line " + linesNumber + " in evenDoc.txt is not well labelled. The labels P, F or U, must be used");
                                    System.exit(0);
                                }

                            }

                            lineEvent = eventsDoc.readLine();

                        }

                        String lineKBase, split;
                        String[] events;
                        Vector kBaseEvents = new Vector(100);

                        while (kBase.ready()) {

                            lineKBase = kBase.readLine();

                            if (lineKBase.startsWith("event('")) {

                                split = lineKBase.split("\\)")[0] + ")";

                                kBaseEvents.add(split);
                            }

                        }

                        String eventKBase, eventC;
                        String[] event, eventsCompare;
                        Vector falses = new Vector(100);
                        Vector fromUser = new Vector(100);
                        Vector positives = new Vector(100);

                        boolean contains = false;

                        for (Object p : positiveEvents) {
                            eventC = (String) p;
                            eventsCompare = eventC.split("\\)");

                            for (Object e : kBaseEvents) {

                                eventKBase = (String) e;
                                event = eventKBase.split("\\)");

                                if (event[0].equals(eventsCompare[0])) {
                                    contains = true;
                                    break;
                                }
                            }

                            if (!contains) {
                                System.out.println("The following event was added for you but labelled as :P");
                                System.out.println("The system assumes that it is a positive one");
                                System.out.println(eventC);
                                fromUser.add(p);
                            }
                            contains = false;
                        }

                        for (Object f : falseEvents) {
                            eventC = (String) f;
                            eventsCompare = eventC.split("\\)");

                            for (Object e : kBaseEvents) {

                                eventKBase = (String) e;
                                event = eventKBase.split("\\)");

                                if (event[0].equals(eventsCompare[0])) {
                                    contains = true;
                                    f = e;
                                    break;
                                }
                            }

                            if (contains) {
                                falses.add(f);
                            } else {
                                System.out.println("The following event was added for you but labelled as :F");
                                System.out.println("The system assumes that it is a positive one");
                                fromUser.add(f);
                            }

                            contains = false;
                        }

                        contains = false;

                        for (Object u : userEvents) {
                            eventC = (String) u;
                            eventsCompare = eventC.split("\\)");

                            for (Object e : kBaseEvents) {

                                eventKBase = (String) e;
                                event = eventKBase.split("\\)");

                                if (event[0].equals(eventsCompare[0])) {
                                    contains = true;
                                    break;
                                }
                            }

                            if (!contains) {
                                fromUser.add(u);
                            }
                            contains = false;
                        }

                        for (Object k : falses) {
                            //System.out.println("Removing " + (String) k + " from the KB");
                            kBaseEvents.removeElement(k);
                        }

                        for (Object u : fromUser) {
                            //System.out.println("Adding " + (String) u + " to the KB");
                            kBaseEvents.add(u);
                        }

                        kBase.close();

                        int eventsLength = kBaseEvents.size(), eventCounter = 0;

                        if (!falses.isEmpty() || !fromUser.isEmpty()) {

                            PrintWriter kbase = new PrintWriter(new FileWriter(kb));

                            kbase.print("base([" + "\n");
                            for (Object e : kBaseEvents) {

                                if ((eventCounter < (eventsLength - 1))) {
                                    kbase.print(e + "," + "\n");
                                    eventCounter++;
                                } else {
                                    kbase.print(e + "\n");
                                }
                            }
                            kbase.print("]).");
                            kbase.close();
                        }
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(patrones.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void cargarBaseC(String ruta) {
        Scanner lectura = new Scanner(System.in);

        while (true) {
            System.out.print(utilidades.idioma.get(85));
            String resp = lectura.nextLine();
            if (resp.equalsIgnoreCase("s") || resp.equalsIgnoreCase("y")) {
                String bc = "['" + ruta + "/kBaseR'].";
                Query q2 = new Query(bc);
                q2.hasSolution();

                break;
            } else if (resp.equalsIgnoreCase("n")) {
                String bc = "['" + ruta + "/kBase'].";
                System.out.println(bc);
                Query q2 = new Query(bc);
                q2.hasSolution();

                break;
            } else {
                System.out.println(utilidades.idioma.get(53));
            }
        }
    }

    //Se ingresan los objetos que aparecen en los patrones
    private ArrayList<String> menuRestricionObjetos() {

        ArrayList<String> objetos = new ArrayList<>();
        Scanner lectura = new Scanner(System.in);
        boolean r = false;

        while (true) {

            System.out.print(utilidades.idioma.get(86));
            String resp = lectura.nextLine();
            if (resp.equalsIgnoreCase("s") || resp.equalsIgnoreCase("y")) {
                r = true;
                break;
            } else if (resp.equalsIgnoreCase("n")) {
                r = false;
                break;
            } else {
                System.out.println(utilidades.idioma.get(53));
            }
        }

        if (r) {
            while (true) {
                System.out.print(utilidades.idioma.get(87) + "\n");
                String objeto = lectura.nextLine();
                if (!objeto.equals("")) {
                    String sep[] = objeto.split(",");
                    for (int i = 0; i < sep.length; i++) {
                        String obj = sep[i].replace("'", "");
                        obj = "'" + obj + "'";
                        objetos.add(obj);
                    }
                    break;

                } else {
                    System.out.println(utilidades.idioma.get(88));
                }
            }
        }

        return objetos;
    }

    private ArrayList<String> menuMotivos() {
        ArrayList<String> motivos = new ArrayList<>();
        Scanner lectura = new Scanner(System.in);
        boolean r = false;

        while (true) {
            System.out.print(utilidades.idioma.get(89));
            String resp = lectura.nextLine();
            if (resp.equalsIgnoreCase("s") || resp.equalsIgnoreCase("y")) {
                r = true;
                break;
            } else if (resp.equalsIgnoreCase("n")) {
                r = false;
                break;
            } else {
                System.out.println(utilidades.idioma.get(53));
            }
        }

        if (r) {
            while (true) {
                System.out.print(utilidades.idioma.get(90));
                String motivo = lectura.nextLine();
                if (!motivo.equals("")) {
                    motivo = motivo.replace("'", "");
                    motivo = "'" + motivo + "'";
                    motivos.add(motivo);

                    boolean r2 = false;
                    while (true) {
                        System.out.print(utilidades.idioma.get(91));
                        String resp = lectura.nextLine();
                        if (resp.equalsIgnoreCase("s") || resp.equalsIgnoreCase("y")) {
                            r2 = true;
                            break;
                        } else if (resp.equalsIgnoreCase("n")) {
                            r2 = false;
                            break;
                        } else {
                            System.out.println(utilidades.idioma.get(53));
                        }
                    }

                    if (!r2) {
                        break;
                    }

                } else {
                    System.out.println(utilidades.idioma.get(92));
                }
            }
        }

        return motivos;
    }

    private ArrayList<String> inicio(ArrayList<String> objRest) {
        ArrayList<String> lista = new ArrayList<>();
        String consulta;
        if (objRest.size() > 0) {
            consulta = "inicio_rest(A,E,B," + objRest.toString() + ").";
        } else {
            consulta = "inicio(A,E,B).";
        }

        Query q2 = new Query(consulta);
        Map<String, Term>[] solutions = q2.allSolutions();

        for (int i = 0; i < solutions.length; i++) {

            try {
                // System.out.println(solutions[i].toString());
                String even = separa_cadena(solutions[i].toString());
                if (!lista.contains(even)) {
                    lista.add(even);
                }
            } catch (Exception e) {

            }

        }

        return lista;
    }

    private ArrayList<String> fin(String Objf, ArrayList<String> objRest) {
        ArrayList<String> lista = new ArrayList<>();
        String consulta;

        if (objRest.size() > 0) {
            if (!Objf.equals("")) {
                consulta = "final_rest(A,E," + Objf + "," + objRest + ").";
            } else {
                consulta = "final_rest(A,E,B," + objRest + ").";

            }
        } else if (!Objf.equals("")) {
            consulta = "final(A,E," + Objf + ").";
        } else {
            consulta = "final(A,E,B).";

        }

        Query q2 = new Query(consulta);
        Map<String, Term>[] solutions = q2.allSolutions();
        for (int i = 0; i < solutions.length; i++) {

            String even = separa_cadena(solutions[i].toString());

            if (!Objf.equals("")) {
                even = even + Objf;
            }
            if (!lista.contains(even)) {
                lista.add(even);
            }

        }

        return lista;

    }

    private void intermedios(ArrayList<String> enlista, String objini, ArrayList<String> FT, String patron, int max, ArrayList<String> listain, ArrayList<String> listafin, ArrayList<String> objCierre, ArrayList<String> objRest, String ruta) {

        ArrayList<String> lista = new ArrayList<>();
        ArrayList<String> cierre = new ArrayList<>();
        lista.addAll(enlista);
        lista.add(objini);
        String consulta;
        cierre.addAll(objCierre);
        if (objRest.size() > 0) {
            consulta = "intermedios_rest(" + objini + ",E,B," + objRest + ").";
        } else {
            consulta = "intermedios(" + objini + ",E,B).";
        }

        //System.out.println(consulta);
        Query q2 = new Query(consulta);
        ArrayList<String> resp = new ArrayList<>();
        Map<String, Term>[] solutions = q2.allSolutions();
        for (int i = 0; i < solutions.length; i++) {
            ///  System.out.println(solutions[i].toString());
            resp.add(solutions[i].toString());
        }

        for (String sol : resp) {
            busqueda_nodo(max, sol, objini, cierre, FT, lista, listain, listafin, patron, objRest, ruta);
        }

    }

    private void busqueda_nodo(int max, String sol, String objini, ArrayList<String> cierre, ArrayList<String> FT, ArrayList<String> lista, ArrayList<String> listain, ArrayList<String> listafin, String patron, ArrayList<String> objRest, String ruta) {

        int cont = 0;
        cont += max;
        String even = separa_cadena(sol);
        even = objini + even;
        String separa[] = even.split(",");

        try {
            cierre.removeIf(x -> x.equals(separa[2]));
        } catch (Exception e) {

        }

        boolean pat = false;

        for (String factorT : FT) {
            //System.out.println(separa[2]+"  "+factorT);
            //
            if (separa[2].equals(factorT) && !lista.contains(separa[2]) && cierre.size() > 0) {

                ArrayList<String> listaAux = new ArrayList<>();
                listaAux.addAll(lista);
                listaAux.add(separa[2]);
                //listaAux.remove(0);
                try {

                    encadenarPatron(patron + ";" + even, listain, listafin, cierre, listaAux, ruta);
                } catch (Exception e) {
                }
                //pat = true;
            }

        }

        if (!lista.contains(separa[2]) && !pat && cierre.size() > 0) {
            // System.out.println(lista);
            // System.out.println(even);
            String patronaux = patron + ";" + even;
            if (cont < 10) {
                cont++;
                intermedios(lista, separa[2], FT, patronaux, cont, listain, listafin, cierre, objRest, ruta);

            }
        }

    }

    private void patron_2_eventos(ArrayList<String> finales, ArrayList<String> inicio, ArrayList<String> Objenlace, String ruta) {
        ArrayList<String> lista = new ArrayList<>();

        Objenlace.forEach((E) -> {
            finales.forEach((F) -> {
                String consulta = "eventoEspecial(" + E + ",E," + F + ").";
                // System.out.println(consulta);

                Query q2 = new Query(consulta);
                Map<String, Term>[] solutions = q2.allSolutions();
                for (int i = 0; i < solutions.length; i++) {
                    //System.out.println(solutions[i].toString());
                    String evento = solutions[i].toString().replace("{", "").replace("}", "").replace("E", "").replace("=", "");
                    String fin = E + "," + evento + "," + F;
                    //System.out.println(fin);
                    encadenarPatron2eventos(inicio, fin, E, F, finales, ruta);
                }

            });

        });

    }

    private void encadenarPatron2eventos(ArrayList<String> inicio, String fin, String enlace, String objF, ArrayList<String> finales, String ruta) {

        ArrayList<String> patrones = new ArrayList<>();
        String sep[] = fin.split(",");
//&& (clasificarevento(fin)).equals("regulate") || clasificarevento(fin).endsWith("inhibit")

        if (sep[1].equals("interact") || sep[1].equals("bind") || clasificarevento(sep[1]).equals("regulate") || clasificarevento(sep[1]).equals("inhibit")) {
            //System.out.println("---" + fin);
            inicio.forEach((i) -> {
                String sep1[] = i.split(",");

                if (enlace.equals(sep1[2]) && !objF.equals(sep1[0])) {

                    String patron = i + ";" + fin;
                    ArrayList<String> list = listarObetosPatron(patron);
                    //System.out.println("\n\n" + patron);
                    //System.out.println(list);
                    agregar_a_lista(patron, list);
                    //guardar_Patron(patron, list);
                    escribirArchivo(patron, list.toString(), "pathways.txt", ruta);
                    new utilidades().carga();
                }
            });
        }

        if (!clasificarevento(fin).equals("bind") || !clasificarevento(fin).equals("associate")) {

            inicio.forEach((in) -> {
                String sep1[] = in.split(",");

                finales.forEach((f) -> {

                    String consulta = "finalEspecial(" + sep1[0] + ",E," + f + ").";
                    //System.out.println(consulta);
                    Query q2 = new Query(consulta);
                    Map<String, Term>[] solutions = q2.allSolutions();
                    for (int i = 0; i < solutions.length; i++) {
                        //System.out.println(solutions[i].toString());
                        String evento = solutions[i].toString().replace("{", "").replace("}", "").replace("E", "").replace("=", "");
                        String Efin = sep1[0] + "," + evento + "," + f;
                        // System.out.println("Efin=  "+Efin);
                        if (enlace.equals(sep1[2]) && sep[2].equals(f) && (clasificarevento(evento).equals("regulate") || clasificarevento(evento).equals("inhibit"))) {

                            String patron = in + ";" + fin;
                            // System.out.println(patron);
                            ArrayList<String> list = listarObetosPatron(patron);
                            patron += ";" + Efin;
                            list.addAll(listarObetosPatron(Efin));
                            //System.out.println("\n\n" + patron);
                            //System.out.println(list);
                            agregar_a_lista(patron, list);
                            //guardar_Patron(patron, list);
                            escribirArchivo(patron, list.toString(), "pathways.txt", ruta);
                            new utilidades().carga();
                        }
                    }

                });

            });
        }

    }

    //;'EGFR',regulate,'MAPK';'MAPK',regulate,'MEK';'MEK',phosphorylate,'Raf';'Raf',activate,'Ras';'Ras',activate,'CREB'
    private void encadenarPatron(String patron, ArrayList<String> listain, ArrayList<String> listafin, ArrayList<String> Objcierre, ArrayList<String> enlista, String ruta) {

        String sep[] = patron.split(",");
        String primero = sep[0].replace(";", "");
        String ultimo = sep[sep.length - 1];

        ArrayList<String> patrones = new ArrayList<>();

        listain.forEach((objin) -> {
            String sep1[] = objin.split(",");
            if (primero.equals(sep1[2])) {
                if (!enlista.contains(sep1[0])) {
                    String pataux = objin + patron;
                    if (!patrones.contains(pataux)) {
                        patrones.add(pataux);

                    }
                }
            }
        });

        ArrayList<String> ObjC = new ArrayList<>();
        listafin.forEach((finales) -> {
            String sep2[] = finales.split(",");
            if (!ObjC.contains(sep2[2])) {
                ObjC.add(sep2[2]);
            }
        });

        patrones.forEach((Patron) -> {

            listafin.forEach((fin) -> {

                String sep2[] = fin.split(",");

                if (ultimo.equals(sep2[0]) && Patron != null) {

                    String patronF = Patron + ";" + fin;
                    ArrayList<String> list = listarObetosPatron(patronF);
                    ObjC.forEach((cierre) -> {

                        if (cierre.equals(list.get(list.size() - 1))) {
                            //System.out.println("\n\n" + patronF);
                            //System.out.println(list);
                            agregar_a_lista(patronF, list);
                            escribirArchivo(patronF, list.toString(), "pathways.txt", ruta);
                            new utilidades().carga();
                            //guardar_Patron(patronF, list);
                        }

                    });
                }
            });

        });

    }

    private void agregar_a_lista(String patron, ArrayList<String> objetos) {
        // System.out.println(patron);
        try {
            pathway pathway = new pathway();
            pathway.setPatron(patron);
            pathway.setObjetos(objetos);
            patrones.add(pathway);
        } catch (Exception e) {
            System.out.println("error");
        }

    }

    private void escribirArchivo(String cadena, String Lista, String archivo, String ruta) {

        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter(ruta + "/" + archivo, true);
            pw = new PrintWriter(fichero);
            //System.out.println(cadena);
            pw.println("\n\n" + cadena);
            pw.println(Lista);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }

    private void guardar_Patron(String ruta) {

        ObjectContainer db = Db4o.openFile(ruta + "/pathways.db");
        // pathway pathway = new pathway();
        //pathway.setPatron(patron);
        //pathway.setObjetos(objetos);

        try {
            db.store(patrones);
        } catch (Exception e) {
            System.out.println(utilidades.idioma.get(93));
        } finally {
            db.close();
        }

    }

    private void borrar_archivo(String nombre) {
        try {
            File ficherod = new File(nombre);
            ficherod.delete();
        } catch (Exception e) {

        }
    }

    private ArrayList<String> listarObetosPatron(String patron) {
        ArrayList<String> lista = new ArrayList<>();

        String sep1[] = patron.split(";");
        // System.out.println("patron: "+patron);             
        for (int i = 0; i < sep1.length; i++) {
            String sep2[] = sep1[i].split(",");
            //System.out.println(sep1[i]);
            if (!lista.contains(sep2[0])) {
                lista.add(sep2[0]);
            }
            if (!lista.contains(sep2[2])) {
                lista.add(sep2[2]);
            }

        }

        return lista;
    }

    private String separa_cadena(String cadena) {

        String even = "";

        cadena = cadena.replace("{", "").replace("}", "").replace(", ", ",");

        String separa[] = cadena.split(",");

        String obj1, eve, obj2;
        obj1 = eve = obj2 = "";

        for (int i = 0; i < separa.length; i++) {

            String sep[] = separa[i].split("=");

            if (sep[0].equals("A")) {
                obj1 = "'" + sep[1].replace("'", "") + "'";
            }
            if (sep[0].equals("B")) {
                obj2 = "'" + sep[1].replace("'", "") + "'";
            }
            if (sep[0].equals("E")) {
                eve = sep[1];
            }
        }

        even = obj1 + "," + eve + "," + obj2;

        return even;
    }

    public void resumirBaseC(String ruta) {
        borrar_archivo(ruta + "/kBaseR.pl");
        ArrayList<String> eventos = leerBaseC(ruta);
        ArrayList<String> resumenEven = new ArrayList<>();
        eventos.stream().forEach((e) -> {
            String sep[] = e.split(",");
            String obj1 = sep[0];
            String obj2 = sep[2];
            String eve = sep[1];
            eve = clasificarevento(eve);

            String eventoR = "event(" + obj1 + "," + eve + "," + obj2 + ")";

            if (!resumenEven.contains(eventoR)) {
                resumenEven.add(eventoR);
            }

        });

        new escribirBC("base([", ruta + "/kBaseR.pl");

        for (int i = 0; i < resumenEven.size() - 1; i++) {
            new escribirBC(resumenEven.get(i) + ",", ruta + "/kBaseR.pl");
        }
        new escribirBC(resumenEven.get(resumenEven.size() - 1), ruta + "/kBaseR.pl");

        new escribirBC("]).", ruta + "/kBaseR.pl");

    }

    public ArrayList<String> leerBaseC(String ruta) {
        ArrayList<String> eventos = new ArrayList<>();

        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {

            archivo = new File(ruta + "/kBase.pl");

            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String linea;
            while ((linea = br.readLine()) != null) {
                String texto = linea.replace("base([", "").replace("]).", "").replace("event(", "").replace(")", "");
                if (!texto.equals("")) {
                    eventos.add(texto);
                }
            }

        } catch (Exception e) {

            // e.printStackTrace();
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        return eventos;
    }

    public String clasificarevento(String rel) {
        String sinonimo = rel;

        ArrayList<String> eventosUP = new ArrayList<>();

        eventosUP.add("activate");
        eventosUP.add("increase");
        eventosUP.add("regulate");
        eventosUP.add("transcriptional-activate");
        eventosUP.add("up-regulate");
        eventosUP.add("enhance");
        eventosUP.add("induce");
        eventosUP.add("lead");
        eventosUP.add("trigger");
        eventosUP.add("translate");
        eventosUP.add("transcribe");
        eventosUP.add("reactivate");
        eventosUP.add("promote");
        eventosUP.add("synthesize");
        eventosUP.add("stimulate");
        eventosUP.add("target");
        eventosUP.add("express");
        eventosUP.add("provoke");
        eventosUP.add("modulate");
        eventosUP.add("mediate");
        eventosUP.add("act");
        eventosUP.add("respond");
        eventosUP.add("infect");
        eventosUP.add("detect");
        eventosUP.add("raise");
        eventosUP.add("develop");
        eventosUP.add("incubate");
        eventosUP.add("convert");
        eventosUP.add("change");
        eventosUP.add("fit");
        eventosUP.add("support");
        eventosUP.add("betamediate");
        eventosUP.add("control");
        eventosUP.add("emerge");
        eventosUP.add("stabilise");
        eventosUP.add("stabilize");
        eventosUP.add("reveal");

        ArrayList<String> eventosDOWN = new ArrayList<>();
        eventosDOWN.add("inhibit");
        eventosDOWN.add("down-regulate");
        eventosDOWN.add("repress");
        eventosDOWN.add("prevent");
        eventosDOWN.add("suppress");
        eventosDOWN.add("retain");
        eventosDOWN.add("decrease");
        eventosDOWN.add("prevent");
        eventosDOWN.add("limit");
        eventosDOWN.add("remove");
        eventosDOWN.add("affect");
        eventosDOWN.add("antagonize");
        eventosDOWN.add("agonize");
        eventosDOWN.add("fall");
        eventosDOWN.add("destabilise");
        eventosDOWN.add("destabilize");
        eventosDOWN.add("reduce");

        ArrayList<String> eventosMiddle = new ArrayList<>();
        eventosMiddle.add("require");
        eventosMiddle.add("interact");
        eventosMiddle.add("associate");
        eventosMiddle.add("phosphorylate");
        eventosMiddle.add("recruit");
        eventosMiddle.add("recognize");
        eventosMiddle.add("involve");
        eventosMiddle.add("trimerize");
        eventosMiddle.add("heterodimerize");
        eventosMiddle.add("dimerize");
        eventosMiddle.add("relate");
        eventosMiddle.add("release");
        eventosMiddle.add("collect");
        eventosMiddle.add("combine");
        eventosMiddle.add("envelop");
        eventosMiddle.add("bring");
        eventosMiddle.add("participate");

        if (eventosUP.contains(sinonimo)) {
            sinonimo = "regulate";
        } else if (eventosDOWN.contains(sinonimo)) {
            sinonimo = "inhibit";
        } else if (eventosMiddle.contains(sinonimo)) {
            sinonimo = "associate";
        }

        return sinonimo;
    }
}
