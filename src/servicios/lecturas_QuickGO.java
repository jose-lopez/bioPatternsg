/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios;


import com.db4o.collections.ActivatableArrayList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import estructura.ontologiaGO;

/**
 *
 * @author yacson-ramirez
 */
public class lecturas_QuickGO {

    public ontologiaGO obtenerOntologia(String codigo) {
        ontologiaGO ontologia = new ontologiaGO();
        try {
            buscarNombre(codigo, ontologia);
            busqueda_de_padres(codigo, ontologia);
        } catch (IOException ex) {
            //Logger.getLogger(lecturas_QuickGO.class.getName()).log(Level.SEVERE, null, ex);
        }

//        String url = "https://www.ebi.ac.uk/QuickGO-Old/GTerm?id="+codigo+"&format=oboxml";
//       
//        try {
//            Document doc = new conexionServ().conecta(url);
//            ontologia = revisa_xml(doc);
//        } catch (Exception e) {
//
//        }
        return ontologia;
    }

    public void buscarNombre(String GO, ontologiaGO ontologia) throws MalformedURLException, IOException {

        String requestURL = "https://www.ebi.ac.uk/QuickGO/services/ontology/go/search?query=" + GO.replace(":", "%3A");
        System.out.println("Url: " + requestURL);
        String output = conectar2(requestURL);

        JsonParser parser = new JsonParser();

        JsonArray gsonArr = parser.parse("[" + output + "]").getAsJsonArray();

        for (JsonElement obj : gsonArr) {
            JsonObject gsonObj = obj.getAsJsonObject();

            JsonArray result = gsonObj.get("results").getAsJsonArray();

            JsonObject gsonObj2 = (JsonObject) result.get(0);
            String nombre = gsonObj2.get("name").getAsString();
            ontologia.setNombre(nombre);
            ontologia.setGO(GO);
            //System.out.println(nombre + "  " + GO);

        }

    }

    public void busqueda_de_padres(String GO, ontologiaGO ontologia) throws MalformedURLException, IOException {

        String requestURL = "https://www.ebi.ac.uk/QuickGO/services/ontology/go/terms/" + GO.replace(":", "%3A") + "/paths/GO%3A0008150%2CGO%3A0003674%2CGO%3A0005575";

        String output = conectar(requestURL);

        JsonParser parser = new JsonParser();

        JsonArray gsonArr = parser.parse("[" + output + "]").getAsJsonArray();

        for (JsonElement obj : gsonArr) {
            JsonObject gsonObj = obj.getAsJsonObject();

            JsonArray result = gsonObj.get("results").getAsJsonArray();

            result.forEach((t) -> {

                JsonArray gsonArr2 = parser.parse(t.toString()).getAsJsonArray();

                gsonArr2.forEach((t2) -> {
                    JsonObject gsonObj2 = t2.getAsJsonObject();
                    String child = gsonObj2.get("child").getAsString();

                    String parent = gsonObj2.get("parent").getAsString();

                    String relationship = gsonObj2.get("relationship").getAsString();
                    //System.out.println(child + " " + parent + " " + relationship);
                    if (child.equals(GO)) {
                        if (relationship.equals("is_a") && !ontologia.getIs_a().contains(parent)) {
                            ontologia.getIs_a().add(parent);
                        } else if (relationship.equals("part_of") && !ontologia.getPart_of().contains(parent)) {
                            ontologia.getPart_of().add(parent);
                        } else if (relationship.equals("regulates") && !ontologia.getRegulates().contains(parent)) {
                            ontologia.getRegulates().add(parent);
                        } else if (relationship.equals("positively_regulates") && !ontologia.getPositively_regulates().contains(parent)) {
                            ontologia.getPositively_regulates().add(parent);
                        } else if (relationship.equals("negatively_regulates") && !ontologia.getNegatively_regulates().contains(parent)) {
                            ontologia.getNegatively_regulates().add(parent);
                        } else if (relationship.equals("occurs_in") && !ontologia.getOccurs_in().contains(parent)) {
                            ontologia.getOccurs_in().add(parent);
                        } else if (relationship.equals("capable_of") && !ontologia.getCapable_of().contains(parent)) {
                            ontologia.getCapable_of().add(parent);
                        } else if (relationship.equals("capable_of_part_of") && !ontologia.getCapable_of_part_of().contains(parent)) {
                            ontologia.getCapable_of_part_of().add(parent);
                        }
                    }

                });

            });

        }

    }

    public String conectar2(String requestURL) {
        String output = "";
        try{
        //String requestURL = "https://www.ebi.ac.uk/QuickGO/services/ontology/go/search?query=GO%3A0005179";
        URL url = new URL(requestURL);

        URLConnection connection = url.openConnection();
        HttpURLConnection httpConnection = (HttpURLConnection) connection;

        httpConnection.setRequestProperty("Accept", "application/json");

        InputStream response = connection.getInputStream();
        int responseCode = httpConnection.getResponseCode();

        if (responseCode != 200) {
            throw new RuntimeException("Response code was not 200. Detected response was " + responseCode);
        }

        Reader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(response, "UTF-8"));
            StringBuilder builder = new StringBuilder();
            char[] buffer = new char[8192];
            int read;
            while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
                builder.append(buffer, 0, read);
            }
            output = builder.toString();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException logOrIgnore) {
                    logOrIgnore.printStackTrace();
                }
            }
        }

       // System.out.println(output);
        }catch(Exception e ){
            
        }
        return output;
    }

    private String conectar(String requestURL) {
        String output = "";

        try {

            URL url = new URL(requestURL);

            URLConnection connection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;

            httpConnection.setRequestProperty("Accept", "application/json");

            InputStream response = connection.getInputStream();
            int responseCode = httpConnection.getResponseCode();

            if (responseCode != 200) {
                throw new RuntimeException("Response code was not 200. Detected response was " + responseCode);
            }

            Reader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(response, "UTF-8"));
                StringBuilder builder = new StringBuilder();
                char[] buffer = new char[8192];
                int read;
                while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
                    builder.append(buffer, 0, read);
                }
                output = builder.toString();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException logOrIgnore) {
                        logOrIgnore.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
        }

        return output;
    }

}
