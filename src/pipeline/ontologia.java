/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipeline;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author yacson-ramirez
 */
public class ontologia {

    private String GO;
    private String nombre;
    private ArrayList<String> sinonimos;
    private ArrayList<String> is_a;
    private ArrayList<String> part_of;
    private ArrayList<String> regulates;
    private ArrayList<String> positively_regulates;
    private ArrayList<String> negatively_regulates;
    private ArrayList<String> occurs_in;
    private ArrayList<String> capable_of;
    private ArrayList<String> capable_of_part_of;

    public ontologia() {
        sinonimos = new ArrayList<>();
        is_a = new ArrayList<>();
        part_of = new ArrayList<>();
        regulates = new ArrayList<>();
        positively_regulates = new ArrayList<>();
        negatively_regulates = new ArrayList<>();
        occurs_in = new ArrayList<>();
        capable_of = new ArrayList<>();
        capable_of_part_of = new ArrayList<>();
    }

    public void imprimirTodo() {
        ontologia objeto = new ontologia();
        ObjectContainer db = Db4o.openFile("mineria/Ontologia.db");
        try {
            ObjectSet result = db.queryByExample(objeto);
            while (result.hasNext()) {
                ontologia obj = (ontologia) result.next();
                imprimir(obj);
            }
        } catch (Exception e) {
            System.out.println("Error al acceder a Ontologia.db");
        } finally {
            db.close();
        }

    }
    //GO:0008123
    private int max = 0;

    public void buscar(String GO, String restriccion) {

        buscarObjeto(GO, 0, "", restriccion);

    }

    private void buscarObjeto(String GO, int nivel, String relacion, String restriccion) {

        ontologia objeto = new ontologia();
        objeto.setGO(GO);
        objeto = consultarBD(objeto);

        for (int i = 0; i < nivel; i++) {
            System.out.print("      ");
        }
        System.out.println(relacion + objeto.getNombre() + " " + objeto.getGO());

        nivel++;
        if (restriccion == null || restriccion.equals("is a")) {

            for (int i = 0; i < objeto.is_a.size(); i++) {
                buscarObjeto(objeto.is_a.get(i), nivel, "is a--> ", restriccion);
            }
        } else if (restriccion == null || restriccion.equals("part of")) {
            for (int i = 0; i < objeto.part_of.size(); i++) {
                buscarObjeto(objeto.part_of.get(i), nivel, "part of--> ", restriccion);
            }
        } else if (restriccion == null || restriccion.equals("regulate")) {
            for (int i = 0; i < objeto.regulates.size(); i++) {
                buscarObjeto(objeto.regulates.get(i), nivel, "regulate--> ", restriccion);
            }
        } else if (restriccion == null || restriccion.equals("negatively regulate")) {
            for (int i = 0; i < objeto.negatively_regulates.size(); i++) {
                buscarObjeto(objeto.negatively_regulates.get(i), nivel, "negatively regulate--> ", restriccion);
            }
        } else if (restriccion == null || restriccion.equals("positively regulate")) {
            for (int i = 0; i < objeto.positively_regulates.size(); i++) {
                buscarObjeto(objeto.positively_regulates.get(i), nivel, "positively regulate--> ", restriccion);
            }
        } else if (restriccion == null || restriccion.equals("occurs in")) {
            for (int i = 0; i < objeto.occurs_in.size(); i++) {
                buscarObjeto(objeto.occurs_in.get(i), nivel, "occurs in--> ", restriccion);
            }
        } else if (restriccion == null || restriccion.equals("capable of")) {
            for (int i = 0; i < objeto.capable_of.size(); i++) {
                buscarObjeto(objeto.capable_of.get(i), nivel, "capable of--> ", restriccion);
            }
        } else if (restriccion == null || restriccion.equals("capable of part of")) {
            for (int i = 0; i < objeto.capable_of_part_of.size(); i++) {
                buscarObjeto(objeto.capable_of_part_of.get(i), nivel, "capable of part of--> ", restriccion);
            }
        }

    }

      
    public void vaciar_pl(String GO, String obj, String relacion, ArrayList<String> listObj) {

        ontologia objeto = new ontologia();
        objeto.setGO(GO);
        objeto = consultarBD(objeto);
        
        if (obj != null) {
            String cadena = relacion + "(\'" + obj + "\',\'" + objeto.getNombre() + "\').";
            if (!revisar_en_archivo(cadena)) {
                escribirArchivo(cadena);
            }
        }
        if (!listObj.contains(GO)) {
            listObj.add(GO);

            for (int i = 0; i < objeto.is_a.size(); i++) {
                vaciar_pl(objeto.is_a.get(i), objeto.getNombre(), "is_a", listObj);
            }

            for (int i = 0; i < objeto.capable_of.size(); i++) {
                vaciar_pl(objeto.capable_of.get(i), objeto.getNombre(), "capable_of", listObj);
            }

            for (int i = 0; i < objeto.capable_of_part_of.size(); i++) {
                vaciar_pl(objeto.capable_of_part_of.get(i), objeto.getNombre(), "capable_of_part_of", listObj);
            }

            for (int i = 0; i < objeto.negatively_regulates.size(); i++) {
                vaciar_pl(objeto.negatively_regulates.get(i), objeto.getNombre(), "negatively_regulates", listObj);
            }

            for (int i = 0; i < objeto.positively_regulates.size(); i++) {
                vaciar_pl(objeto.positively_regulates.get(i), objeto.getNombre(), "positively_regulates", listObj);
            }

            for (int i = 0; i < objeto.part_of.size(); i++) {
                vaciar_pl(objeto.part_of.get(i), objeto.getNombre(), "part_of", listObj);
            }

            for (int i = 0; i < objeto.regulates.size(); i++) {
                vaciar_pl(objeto.regulates.get(i), objeto.getNombre(), "regulates", listObj);
            }

            for (int i = 0; i < objeto.occurs_in.size(); i++) {
                vaciar_pl(objeto.occurs_in.get(i), objeto.getNombre(), "occurs_in", listObj);
            }
        }

    }

    private ontologia consultarBD(ontologia obj) {
        ontologia objeto = new ontologia();
        ObjectContainer db = Db4o.openFile("mineria/Ontologia.db");
        try {

            ObjectSet result = db.queryByExample(obj);
            while (result.hasNext()) {
                objeto = (ontologia) result.next();

            }
        } catch (Exception e) {
            System.out.println("Error al acceder a Ontologia.db");
        } finally {
            db.close();
        }

        return objeto;
    }

    private void escribirArchivo(String cadena) {

        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter("ontologia.pl", true);
            pw = new PrintWriter(fichero);
            System.out.println(cadena);
            pw.println(cadena);
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

    private boolean revisar_en_archivo(String objeto) {

        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
            archivo = new File("ontologia.pl");
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String linea;

            while ((linea = br.readLine()) != null) {

                if (linea.equals(objeto)) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    private void imprimir(ontologia objeto) {
        System.out.println("\n\n++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println(objeto.getGO());
        System.out.println("Nombre: " + objeto.getNombre());
        System.out.println("Sinonimos:");

        try {
            for (int i = 0; i < objeto.getSinonimos().size(); i++) {
                System.out.println("    " + objeto.getSinonimos().get(i));
            }
        } catch (Exception e) {
        }

        try {
            if (objeto.getIs_a().size() > 0) {
                System.out.println("________________________________");
                for (int i = 0; i < objeto.getIs_a().size(); i++) {
                    System.out.println("is a--> " + objeto.getIs_a().get(i));
                }
            }
        } catch (Exception e) {
        }

        try {
            if (objeto.getRegulates().size() > 0) {
                System.out.println("________________________________");
                for (int i = 0; i < objeto.getIs_a().size(); i++) {
                    System.out.println("regulate--> " + objeto.getRegulates().get(i));
                }
            }
        } catch (Exception e) {
        }

        try {
            if (objeto.getCapable_of().size() > 0) {
                System.out.println("________________________________");
                for (int i = 0; i < objeto.getIs_a().size(); i++) {
                    System.out.println("capable of--> " + objeto.getCapable_of().get(i));
                }
            }
        } catch (Exception e) {
        }

        try {
            if (objeto.getCapable_of_part_of().size() > 0) {
                System.out.println("________________________________");
                for (int i = 0; i < objeto.getCapable_of_part_of().size(); i++) {
                    System.out.println("capable of part--> " + objeto.getCapable_of_part_of().get(i));
                }
            }
        } catch (Exception e) {
        }

        try {
            if (objeto.getNegatively_regulates().size() > 0) {
                System.out.println("________________________________");
                for (int i = 0; i < objeto.getNegatively_regulates().size(); i++) {
                    System.out.println("negatively regulates--> " + objeto.getNegatively_regulates().get(i));
                }
            }
        } catch (Exception e) {
        }

        try {
            if (objeto.getPositively_regulates().size() > 0) {
                System.out.println("________________________________");
                for (int i = 0; i < objeto.getPositively_regulates().size(); i++) {
                    System.out.println("positively regulates--> " + objeto.getPositively_regulates().get(i));
                }
            }
        } catch (Exception e) {
        }

        try {
            if (objeto.getOccurs_in().size() > 0) {
                System.out.println("________________________________");
                for (int i = 0; i < objeto.getOccurs_in().size(); i++) {
                    System.out.println("is a--> " + objeto.getOccurs_in().get(i));
                }
            }
        } catch (Exception e) {
        }

    }

    public String getGO() {
        return GO;
    }

    public void setGO(String GO) {
        this.GO = GO;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String FuncionMolecular) {
        this.nombre = FuncionMolecular;
    }

    public ArrayList<String> getIs_a() {
        return is_a;
    }

    public void setIs_a(ArrayList<String> is_a) {
        this.is_a = is_a;
    }

    public ArrayList<String> getSinonimos() {
        return sinonimos;
    }

    public void setSinonimos(ArrayList<String> sinonimos) {
        this.sinonimos = sinonimos;
    }

    public ArrayList<String> getPart_of() {
        return part_of;
    }

    public void setPart_of(ArrayList<String> part_of) {
        this.part_of = part_of;
    }

    public ArrayList<String> getRegulates() {
        return regulates;
    }

    public void setRegulates(ArrayList<String> regulates) {
        this.regulates = regulates;
    }

    public ArrayList<String> getPositively_regulates() {
        return positively_regulates;
    }

    public void setPositively_regulates(ArrayList<String> positively_regulates) {
        this.positively_regulates = positively_regulates;
    }

    public ArrayList<String> getNegatively_regulates() {
        return negatively_regulates;
    }

    public void setNegatively_regulates(ArrayList<String> negatively_regulates) {
        this.negatively_regulates = negatively_regulates;
    }

    public ArrayList<String> getOccurs_in() {
        return occurs_in;
    }

    public void setOccurs_in(ArrayList<String> occurs_in) {
        this.occurs_in = occurs_in;
    }

    public ArrayList<String> getCapable_of() {
        return capable_of;
    }

    public void setCapable_of(ArrayList<String> capable_of) {
        this.capable_of = capable_of;
    }

    public ArrayList<String> getCapable_of_part_of() {
        return capable_of_part_of;
    }

    public void setCapable_of_part_of(ArrayList<String> capable_of_part_of) {
        this.capable_of_part_of = capable_of_part_of;
    }

}
