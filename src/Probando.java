///*
//    Probando.java
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
//import java.util.List;
//import java.util.Set;
//import java.util.TreeMap;
//import uk.ac.roslin.ensembl.config.DBConnection;
//import uk.ac.roslin.ensembl.config.EnsemblCoordSystemType;
//import uk.ac.roslin.ensembl.config.FeatureType;
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
//
///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
///**
// *
// * @author LuisM
// */
//public class Probando {
//
//    String especie;
//    String DBversion;
//
//    public Probando(String especie,String dbversion) throws ConfigurationException, DAOException {
//        //String especie = "escherichia coli k12";
//        //String especie = "Pyrococcus abyssi";
//        //DBversion = "15";
//        DBversion = dbversion;
//        this.especie = especie;
//        //System.out.println("Especie: " + especie);
//
//        /*DBRegistry ensemblgenomesRegistry = new DBRegistry(DBConnection.DataSource.ENSEMBLGENOMES);
//        System.out.println("Conexion Establecida");
//
//        DBCollectionSpecies sp1 = (DBCollectionSpecies) ensemblgenomesRegistry.getSpeciesByAlias(especie);
//        System.out.println("Obtenida la especie");
//
//        System.out.println("Taxionomia: " + sp1.getTaxonomyID());
//        System.out.println("Nombre: " + sp1.getDisplayName());
//
//        DAChromosome chr = sp1.getChromosomeByName("Chromosome", DBversion);
//        System.out.println(chr.getSequenceAsString(1, 100));*/
//
//        /*System.out.println("\n------------\nCaracterísticas\n-------------");
//
//         for (FeatureType t : FeatureType.getAllTypes()) {
//         System.out.println(t.toString());
//
//         Set<DACoordinateSystem> s = (sp1.getMostRecentCoreDatabase()).getCSForFeature(sp1, t);
//
//         if (s != null && !s.isEmpty()) {
//
//         for (DACoordinateSystem cs : s) {
//         System.out.println("\tcs-id: " + cs.getId() + " - " + "cs-type: " + cs.getType().toString());
//         }
//         } else {
//         System.out.println("Sin Información");
//         }
//         System.out.println("");
//         }*/
//
//    }
//
//    public static void main(String[] args) throws Exception {
//        Probando prueba = new Probando("cow","15");
//        prueba.homologos("ENSBTAG00000021527");
//        prueba.infoGen("ENSBTAG00000021527");
//
//    }
//
//    public void homologos(String gen) throws ConfigurationException, DAOException, NonUniqueException{
//        System.out.println("Búsqueda de homólogos para el gen " + gen);
//             DBRegistry   eReg = DBRegistry.createRegistryForDataSourceAtReleaseVersion(DBConnection.DataSource.ENSEMBLDB, Integer.parseInt(DBversion));
//
//
//        DBSpecies sp = eReg.getSpeciesByAlias(especie);
//        System.out.println("Taxionomia: " + sp.getTaxonomyID());
//        DAGene g = sp.getGeneByStableID(gen);
//
//        System.out.println("-----------------------Homólogos------------------------------");
//        List<DAHomologyPairRelationship> out = g.getHomologies();
//        System.out.println("Especie: " + g.getSpecies().getCommonName());
//        System.out.println(out.size() + " homólogos encontrados");
//        System.out.println("_____________________________________________\r\n");
//
//        out = g.getHomologies();
//        int i = 1;
//        for (DAHomologyPairRelationship h : out) {
//            System.out.print(i + " ");
//            System.out.print(h.getTargetProperties().getSpeciesName());
//            System.out.print(" Gen: " + h.getTarget().getStableID());
//            System.out.println(" [" + h.getType().toString() + "] (Ancestro común: " + h.getLastCommonAncestor() + ")");
//            i++;
//        }
//    }
//    
//    public void infoGen(String gen) throws ConfigurationException, DAOException, NonUniqueException {
//        System.out.println("-----------------Información del gen "+gen+"-------------------------");
//        DBRegistry eReg = DBRegistry.createRegistryForDataSourceAtReleaseVersion(DBConnection.DataSource.ENSEMBLDB, Integer.parseInt(DBversion));
//
//        DBSpecies specie = eReg.getSpeciesByAlias(especie);
//        DAGene migen = specie.getGeneByStableID(gen, "68");
//        Mapping mapping = migen.getChromosomeMapping();
//
//        //if the genes are not annotated on the chromosome level
//        // Mapping mapping = gene.getAnnotationLevelMappings().first();
//
//
//        System.out.println("Gene: " + migen.getStableID());
//        System.out.println("\tchr start: " + mapping.getTargetCoordinates().getStart());
//        System.out.println("\tchr stop: " + mapping.getTargetCoordinates().getEnd());
//        System.out.println("\tassembly: " + migen.getAssembly());
//        System.out.println("\tdescription: " + migen.getDescription());
//        System.out.println("\tsymbol: " + migen.getDisplayName());
//        System.out.println("\tstrand: " + mapping.getTargetCoordinates().getStrand());
//        System.out.println("\ttaxonID: " + migen.getSpecies().getTaxonomyID());
//        System.out.println("\tstatus: " + migen.getStatus());
//        System.out.println("\ttype: " + migen.getBiotype());
//
//
//        //look at all the transcripts
//
//        System.out.println("\tTranscript Count: " + migen.getTranscripts().size());
//        for (DATranscript t : migen.getTranscripts()) {
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
//
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
//
//
//
//
//        }
//    }
//}
