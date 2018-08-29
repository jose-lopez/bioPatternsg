///*
//    ADN.java
//
//
//    Copyright (C) 2016.
//    Jose Lopez (jlopez@unet.edu.ve).
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU Affero General Public License as
//    published by the Free Software Foundation, either version 3 of the
//    License, or (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU Affero General Public License for more details.
//
//    You should have received a copy of the GNU Affero General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>
//
//*/
//
//
//package findmotifs;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Set;
//import java.util.TreeMap;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import uk.ac.roslin.ensembl.config.DBConnection;
//import uk.ac.roslin.ensembl.config.DBConnection.DataSource;
//import uk.ac.roslin.ensembl.config.EnsemblCoordSystemType;
//import uk.ac.roslin.ensembl.config.FeatureType;
//import uk.ac.roslin.ensembl.config.SchemaVersion;
//import uk.ac.roslin.ensembl.dao.database.DBCollectionSpecies;
//import uk.ac.roslin.ensembl.dao.database.DBRegistry;
//import uk.ac.roslin.ensembl.dao.database.DBSpecies;
//import uk.ac.roslin.ensembl.datasourceaware.DAXRef;
//import uk.ac.roslin.ensembl.datasourceaware.compara.DAHomologyPairRelationship;
//import uk.ac.roslin.ensembl.datasourceaware.core.DAChromosome;
//import uk.ac.roslin.ensembl.datasourceaware.core.DACoordinateSystem;
//import uk.ac.roslin.ensembl.datasourceaware.core.DAExon;
//import uk.ac.roslin.ensembl.datasourceaware.core.DAGene;
//import uk.ac.roslin.ensembl.datasourceaware.core.DATranscript;
//import uk.ac.roslin.ensembl.exception.ConfigurationException;
//import uk.ac.roslin.ensembl.exception.DAOException;
//import uk.ac.roslin.ensembl.exception.NonUniqueException;
//import uk.ac.roslin.ensembl.model.Mapping;
//import uk.ac.roslin.ensembl.datasourceaware.core.DADNASequence;
//import uk.ac.roslin.ensembl.model.core.Chromosome;
//
//public class ADN {
//
//    public String nombre_especie;
//    public String DBversion;
//    public String gen_id;
//    DBRegistry eReg;
//    public DBSpecies especie;
//    public DAGene gen;
//    public List<DAHomologyPairRelationship> homologos;
//    public List<Homologo> region_promotora_homolgs;
//    Mapping mapping;
//    public ArrayList secuencias;
//    public ArrayList secufasta;
//    public String nombrefasta;
//
//    public ADN(String nombre_especie, String gen_id, String dBversion, DBRegistry eReg) throws ConfigurationException, DAOException, NonUniqueException, IOException {
//        this.nombre_especie = nombre_especie;
//        this.DBversion = dBversion;
//        
//        //eReg = DBRegistry.createRegistryForDataSourceAtReleaseVersion(DataSource.ENSEMBLDB, Integer.parseInt(DBversion));
//        //eReg = DBRegistry.createRegistryForDataSourceCurrentRelease(DataSource.ENSEMBLDB);
//        
//
//        especie = eReg.getSpeciesByAlias(this.nombre_especie);
//        gen = especie.getGeneByStableID(gen_id);
//        mapping = gen.getChromosomeMapping();
//        /*homologos = gen.getHomologies();
//        region_promotora_homolgs = new ArrayList<Homologo>();
//        secuencias = new ArrayList();
//        secufasta = new ArrayList();*/
//    }
//    
//    public ADN(String nombre_especie, String gen_id, String dBversion) throws ConfigurationException, DAOException, NonUniqueException, IOException {
//        this.nombre_especie = nombre_especie;
//        this.DBversion = dBversion;
//        
//        //eReg = DBRegistry.createRegistryForDataSourceAtReleaseVersion(DataSource.ENSEMBLDB, Integer.parseInt(DBversion));
//        eReg = DBRegistry.createRegistryForDataSourceCurrentRelease(DataSource.ENSEMBLDB);
//        
//
//        especie = eReg.getSpeciesByAlias(this.nombre_especie);
//        gen = especie.getGeneByStableID(gen_id, dBversion);
//        mapping = gen.getChromosomeMapping();
//        homologos = gen.getHomologies();
//        region_promotora_homolgs = new ArrayList<Homologo>();
//        secuencias = new ArrayList();
//        secufasta = new ArrayList();
//    }
//
//    public void getInformacionEspecie() {
//        System.out.println("-----------------Información de la especie -------------------------");
//        System.out.println("Nombre: " + gen.getSpecies().getCommonName());
//        System.out.println("Taxionomia: " + especie.getTaxonomyID());
//    }
//
//    public void getInformacionGen() throws DAOException, NonUniqueException {
//        System.out.println("-----------------Información del gen " + gen + "-------------------------");
//        System.out.println("Gene: " + gen.getStableID());
//        System.out.println(homologos.size() + " homólogos encontrados");
//        System.out.println("\tchr start: " + mapping.getTargetCoordinates().getStart());
//        System.out.println("\tchr stop: " + mapping.getTargetCoordinates().getEnd());
//        System.out.println("\tassembly: " + gen.getAssembly());
//        System.out.println("\tdescription: " + gen.getDescription());
//        System.out.println("\tsymbol: " + gen.getDisplayName());
//        System.out.println("\tstrand: " + mapping.getTargetCoordinates().getStrand());
//        System.out.println("\ttaxonID: " + gen.getSpecies().getTaxonomyID());
//        System.out.println("\tstatus: " + gen.getStatus());
//        System.out.println("\ttype: " + gen.getBiotype());
//
//        //System.out.println("Secuencia: " + gen.getSequenceAsString().length());
//    }
//
//    public void getInformacionHomologo() throws DAOException, NonUniqueException, IOException {
//        System.out.println("-----------------------Homólogos------------------------------");
//        System.out.println(homologos.size() + " homólogos encontrados");
//
//        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
//        String ans;
//
//        int i = 1;
//        boolean exit = false;
//        for (DAHomologyPairRelationship h : homologos) {
//            System.out.print(i + " ");
//            System.out.print(h.getTargetProperties().getSpeciesName());
//            System.out.print(" Gen: " + h.getTarget().getStableID());
//            System.out.print(" [" + h.getType().toString() + "] (Ancestro común: " + h.getLastCommonAncestor() + ")");
//            System.out.println("[" + h.getTargetProperties().getCoords().toString() + "]");
//            nombrefasta = ">" + h.getTargetProperties().getSequenceName() + " Gen: "
//                    + " [" + h.getTarget().getStableID() + " [" + h.getType().toString() + "] (Ancestro común: "
//                    + h.getLastCommonAncestor() + ")" + "[" + h.getTargetProperties().getCoords().toShortString() + "]";
//            do {
//                System.out.print("¿Desea buscar region promotora para este homologo? (y/n/f)");
//                ans = bufferRead.readLine();
//                if (ans.equalsIgnoreCase("y")) {
//                    System.out.print("Ingrese el valor inferior: ");
//                    String va = bufferRead.readLine();
//                    System.out.print("Ingrese el valor superior: ");
//                    String vb = bufferRead.readLine();
//
//                    int valorA = Integer.parseInt(va);
//                    int valorB = Integer.parseInt(vb);
//                    regionPromotoraHomolg(valorA, valorB, i - 1);
//                } else if (ans.equalsIgnoreCase("n")) {
//                    //no se ingresa el homologo a las regiones promotoras escogidas 
//                } else if (ans.equalsIgnoreCase("f")) {
//                    exit = true;
//                    break;
//                }
//            } while (ans.equalsIgnoreCase("y") && ans.equalsIgnoreCase("n") && ans.equalsIgnoreCase("f"));
//            i++;
//            if (exit) {
//                generarArchivo(secuencias, "secuencias", secufasta, "secufasta");
//                break;
//            }
//        }
//    }
//
//    public String getInformacionRegPromotor(String limInf, String limSup) throws DAOException, NonUniqueException, IOException {
//
//        System.out.println("Definiendo region promotora para :" + this.gen.getStableID());
//
//        String regionPromotora = regionPromotoraGen(Integer.parseInt(limInf), Integer.parseInt(limSup));
//
//        return regionPromotora;
//    }
//
//    public void regionPromotoraHomolg(int valorA, int valorB, int index) {
//
//        String[] splitted = homologos.get(index).getTargetProperties().getCoords().toString().split(" ");
//        int inicio = Integer.parseInt(splitted[0]);
//        int inicio_promotor = inicio - valorA;
//        if (inicio_promotor < 1) {
//            System.out.println("Region Promotora: ");
//            return;
//        }
//        int fin_promotor = inicio - valorB;
//
//        DAGene gen_cr = null;
//        Chromosome cromosoma_homo = null;
//        try {
//            gen_cr = especie.getGeneByStableID(homologos.get(index).getTarget().getStableID(), DBversion);
//            try {
//                cromosoma_homo = (Chromosome) gen_cr.getChromosomeMapping().getTarget();
//            } catch (NonUniqueException ex) {
//                //Logger.getLogger(ADN.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (NullPointerException nex) {
//            }
//        } catch (DAOException ex) {
//            //Logger.getLogger(ADN.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        if (cromosoma_homo != null) {
//            Homologo h = new Homologo(homologos.get(index), cromosoma_homo.getSequenceAsString(inicio_promotor, fin_promotor));
//            region_promotora_homolgs.add(h);
//            secuencias.add(region_promotora_homolgs.get(region_promotora_homolgs.size() - 1).region_promotora);
//            secufasta.add(index, nombrefasta);
//            System.out.println("Region Promotora: " + region_promotora_homolgs.get(region_promotora_homolgs.size() - 1).region_promotora); //ultima posicion
//        } else {
//            Homologo h = new Homologo(homologos.get(index), "null");
//            region_promotora_homolgs.add(h);
//            System.out.println("Region Promotora: ");
//        }
//        System.out.println("");
//    }
//
//    public String regionPromotoraGen(int valorA, int valorB) throws NonUniqueException {
//
//        String regionPromoGen = "";
//        Integer inicio = gen.getChromosomeMapping().getTargetCoordinates().getStart();
//
//        int inicio_promotor = inicio - valorA;
//        if (inicio_promotor < 1) {
//            System.out.println("Region Promotora fallida para" + gen.getStableID());
//            return "";
//        }
//        int fin_promotor = inicio + valorB;
//
//        //DAGene gen_cr = null;
//        Chromosome cromosoma = null;
//        try {
//            cromosoma = (Chromosome) gen.getChromosomeMapping().getTarget();
//        } catch (NonUniqueException ex) {
//            //Logger.getLogger(ADN.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (NullPointerException nex) {
//        }
//
//        if (cromosoma != null) {
//            regionPromoGen = cromosoma.getSequenceAsString(inicio_promotor, fin_promotor);
//        } else {
//
//            System.out.println("No disponible Region Promotora para: " + gen.getStableID());
//        }
//
//        System.out.println("");
//        return regionPromoGen;
//    }
//
//    public void getInformacionExones() {
//        System.out.println("-----------------Información de los Exones -------------------------");
//        System.out.println("\tTranscript Count: " + gen.getTranscripts().size());
//        for (DATranscript t : gen.getTranscripts()) {
//            System.out.println("\t\tTranscript: " + t.getStableID());
//            System.out.println("\t\t " + t.getDisplayName());
//            System.out.println("\t\t " + t.getStatus());
//            System.out.println("\t\t " + t.getBiotype());
//            System.out.println("\t\t " + t.getDescription());
//            System.out.println("\t\t " + t.getGene().getStableID());
//            System.out.println("\t\tCanonical ?  " + t.isCanonical());
//            System.out.println("\t\tXREF: " + t.getDisplayXRef().getDBDisplayName());
//            System.out.println("\t\tXREF: " + t.getDisplayXRef().getDisplayID());
//            System.out.println("\t\tXREF: " + t.getDisplayXRef().getInfoType());
//            System.out.println("\t\tXREF: " + t.getDisplayXRef().getInfo());
//            for (Mapping m : t.getLoadedMappings(EnsemblCoordSystemType.chromosome)) {
//                System.out.println("\t\tMapping: " + m.getTargetHashID());
//                System.out.println("\t\t\tCoords: " + m.getTargetCoordinates().toString());
//            }
//
//            // look at all the exons of the transcript 
//            System.out.println("EXONS");
//            for (DAExon e : t.getExons()) {
//                System.out.println("\t\tRank: " + e.getRank());
//                System.out.println("\t\tStableID: " + e.getStableID());
//                System.out.println("\t\tID: " + e.getId());
//                System.out.println("\t\tstart phase: " + e.getPhase());
//                System.out.println("\t\tend phase: " + e.getEndPhase());
//                System.out.println("\t\tcurrent: " + e.isCurrent());
//                System.out.println("\t\tconstitutive: " + e.isConstitutive());
//
//                //get locations of exon
//                for (Mapping m : e.getLoadedMappings(EnsemblCoordSystemType.chromosome)) {
//                    System.out.println("\t\tMapping: " + m.getTargetHashID());
//                    System.out.println("\t\t\tCoords: " + m.getTargetCoordinates().toString());
//                }
//            }
//        }
//    }
//
//    public void generarArchivo(ArrayList cadenas, String nomarch, ArrayList cadefasta, String nomfasta) {
//        FileWriter ficherofasta = null;
//        PrintWriter pwfasta = null;
//        FileWriter fichero = null;
//        PrintWriter pw = null;
//        try {
//            ficherofasta = new FileWriter(nomfasta + ".stam");
//            pwfasta = new PrintWriter(ficherofasta);
//
//            fichero = new FileWriter(nomarch + ".stam");
//            pw = new PrintWriter(fichero);
//            for (int i = 0; i < cadenas.size(); i++) {
//                pw.println(cadenas.get(i).toString());
//                pwfasta.println(cadefasta.get(i));
//                pwfasta.println(cadenas.get(i).toString());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (null != fichero) {
//                    fichero.close();
//                }
//                if (null != ficherofasta) {
//                    ficherofasta.close();
//                }
//            } catch (Exception e2) {
//                e2.printStackTrace();
//            }
//        }
//        System.out.println("Se ha generado el archivo " + nomarch + ".stam con las secuencias de ADN solicitadas");
//        System.out.println("Se ha generado el archivo " + nomfasta + ".stam para buscar los motifs");
//    }
//}
