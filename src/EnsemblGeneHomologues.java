///**
// * Copyright (C) 2010-13 The Roslin Institute <contact andy.law@roslin.ed.ac.uk>
// *
// * This file is part of JEnsembl: a Java API to Ensembl data sources developed by the
// * Bioinformatics Group at The Roslin Institute, The Royal (Dick) School of
// * Veterinary Studies, University of Edinburgh.
// *
// * Project hosted at: http://jensembl.sourceforge.net
// *
// * This is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License (version 3) as published by
// * the Free Software Foundation.
// *
// * This software is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// *
// * You should have received a copy of the GNU General Public License
// * in this software distribution. If not, see: http://www.gnu.org/licenses/gpl-3.0.html
// */
//
//import java.util.List;
//import uk.ac.roslin.ensembl.config.DBConnection.DataSource;
//import uk.ac.roslin.ensembl.dao.database.DBRegistry;
//import uk.ac.roslin.ensembl.dao.database.DBSpecies;
//import uk.ac.roslin.ensembl.datasourceaware.compara.DAHomologyPairRelationship;
//import uk.ac.roslin.ensembl.datasourceaware.core.DADNASequence;
//import uk.ac.roslin.ensembl.datasourceaware.core.DAGene;
//import uk.ac.roslin.ensembl.model.Mapping;
//import uk.ac.roslin.ensembl.model.MappingSet;
//
///**
// *
// * @author tpaterso
// */
//public class EnsemblGeneHomologues {
//    
//    //retrieving all the homologue for a gene
//    //and comparing the mapping data for this gene in Compara - with
//    //that pulled out of correct core database        
//
//    public static void main(String[] args) throws Exception {
//
//        DBRegistry eReg = DBRegistry.createRegistryForDataSourceAtReleaseVersion(DataSource.ENSEMBLDB, 72);
//        DBSpecies sp = eReg.getSpeciesByAlias("human");
//        
//        DAGene g = sp.getGeneByStableID("ENSG00000153551");
//
//        List<DAHomologyPairRelationship> out = g.getHomologies();
//        System.out.println(g.getSpecies().getCommonName() + "v"+g.getDBVersion()+" gene " + g.getStableID() 
//                + " has " + out.size() + " homologies.");
//        System.out.println("_____________________________________________\r\n");
//        
//        
//        g = sp.getGeneByStableID("ENSG00000153551", "68");
//
//        out = g.getHomologies();
//        System.out.println(g.getSpecies().getCommonName() + "v"+g.getDBVersion()+" gene " + g.getStableID() 
//                + " has " + out.size() + " homologies.");
//        System.out.println("_____________________________________________\r\n");
//
//
//        for ( DAHomologyPairRelationship h : out  ) {
//
//            System.out.print(h.getTargetProperties().getSpeciesName());
//            System.out.print(" gene: " + h.getTarget().getStableID());
//            System.out.println(" [" + h.getType().toString() + "] (last common ancestor: " + h.getLastCommonAncestor() + ")");
//
//            System.out.println("MAPPING DATA IN COMPARA");
//            System.out.println("'chromosome' name: " + h.getTargetProperties().getSequenceName()
//                    + " [" + h.getTargetProperties().getCoords().toString() + "]");
//
//            System.out.println("MAPPING DATA LAZY LOADED FROM CORE");
//
//            MappingSet m;
//
//            m = h.getTarget().getAnnotationLevelMappings();
//
//            System.out.print("ANNOTATION LEVEL: ");
//            if (m != null && !m.isEmpty()) {
//                for (Mapping mp : m) {
//                    System.out.println(mp.getTarget().getClass().getSimpleName()
//                            + " name: "
//                            + ((DADNASequence) mp.getTarget()).getName()
//                            + " id: " + mp.getTarget().getId() + " ["
//                            + mp.getTargetCoordinates().toString() + "]");
//
//                    if (!h.getTargetProperties().getSequenceName().contentEquals(
//                            ((DADNASequence) mp.getTarget()).getName())) {
//                        System.out.println("\n\n\n*********ERROR in name");
//                    }
//                    if (h.getTargetProperties().getCoords().getStart() - mp.getTargetCoordinates().getStart() != 0) {
//                        System.out.println("\n\n\n*********ERROR in start coord");
//                    }
//                    if (h.getTargetProperties().getCoords().getEnd() - mp.getTargetCoordinates().getEnd() != 0) {
//                        System.out.println("\n\n\n*********ERROR in end coord");
//                    }
//                    if (!h.getTargetProperties().getCoords().getStrand().equals(mp.getTargetCoordinates().getStrand())) {
//                        System.out.println("\n\n\n*********ERROR in strande");
//                    }
//                }
//            } else {
//                System.out.println("");
//            }
//
//            m = h.getTarget().getBuildLevelMappings();
//            System.out.print("BUILD LEVEL: ");
//            if (m != null && !m.isEmpty()) {
//
//                for (Mapping mp : m) {
//                    System.out.println(mp.getTarget().getClass().getSimpleName()
//                            + " name: "
//                            + ((DADNASequence) mp.getTarget()).getName()
//                            + " id: " + mp.getTarget().getId() + " ["
//                            + mp.getTargetCoordinates().toString() + "]");
//
//                    if (!h.getTargetProperties().getSequenceName().contentEquals(((DADNASequence) mp.getTarget()).getName())) {
//                        System.out.println("\n\n\n*********ERROR in name");
//                    }
//                    if (h.getTargetProperties().getCoords().getStart() - mp.getTargetCoordinates().getStart() != 0) {
//                        System.out.println("\n\n\n*********ERROR in start coord");
//                    }
//                    if (h.getTargetProperties().getCoords().getEnd() - mp.getTargetCoordinates().getEnd() != 0) {
//                        System.out.println("\n\n\n*********ERROR in end coord");
//                    }
//                    if (!h.getTargetProperties().getCoords().getStrand().equals(mp.getTargetCoordinates().getStrand())) {
//                        System.out.println("\n\n\n*********ERROR in strande");
//                    }
//
//                }
//            } else {
//                System.out.println("");
//            }
//
//            m = h.getTarget().getTopLevelMappings();
//            System.out.print("TOP LEVEL: ");
//            if (m != null && !m.isEmpty()) {
//                for (Mapping mp : m) {
//                    System.out.println(mp.getTarget().getClass().getSimpleName()
//                            + " name: "
//                            + ((DADNASequence) mp.getTarget()).getName()
//                            + " id: " + mp.getTarget().getId() + " ["
//                            + mp.getTargetCoordinates().toString() + "]");
//
//                    if (!h.getTargetProperties().getSequenceName().contentEquals(((DADNASequence) mp.getTarget()).getName())) {
//                        System.out.println("\n\n\n*********ERROR in name");
//                    }
//                    if (h.getTargetProperties().getCoords().getStart() - mp.getTargetCoordinates().getStart() != 0) {
//                        System.out.println("\n\n\n*********ERROR in start coord");
//                    }
//                    if (h.getTargetProperties().getCoords().getEnd() - mp.getTargetCoordinates().getEnd() != 0) {
//                        System.out.println("\n\n\n*********ERROR in end coord");
//                    }
//                    if (!h.getTargetProperties().getCoords().getStrand().equals(mp.getTargetCoordinates().getStrand())) {
//                        System.out.println("\n\n\n*********ERROR in strande");
//                    }
//
//                }
//            } else {
//                System.out.println("");
//            }
//            
//            System.out.println("___________________________________________________");
//            System.out.println("");
// //           }
//        
//        }
//
//
//        System.out.println("\n\n*************************\nCOMPLETED FUNCTIONAL TEST\n*************************\n");
//
//    }
//}