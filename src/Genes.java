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
//import java.util.Set;
//import uk.ac.roslin.ensembl.config.DBConnection.DataSource;
//import uk.ac.roslin.ensembl.config.EnsemblDBType;
//import uk.ac.roslin.ensembl.config.FeatureType;
//import uk.ac.roslin.ensembl.dao.database.*;
//import uk.ac.roslin.ensembl.dao.factory.DAOCoreFactory;
//import uk.ac.roslin.ensembl.datasourceaware.core.DAChromosome;
//import uk.ac.roslin.ensembl.datasourceaware.core.DACoordinateSystem;
//import uk.ac.roslin.ensembl.datasourceaware.core.DADNASequence;
//import uk.ac.roslin.ensembl.datasourceaware.core.DAGene;
//import uk.ac.roslin.ensembl.model.Coordinate;
//import uk.ac.roslin.ensembl.model.Mapping;
//import uk.ac.roslin.ensembl.model.core.Gene;
//
//public class Genes {
//
//    //retreiving genes
//    //genes are 'feature' annotations - 
//    //when genes are retrieved - they are mapped on to a chromosome
//    
//    public  static void main(String[] args) throws Exception {
//        
//        /*System.out.println("Comienzo....");
//        System.out.println("Especie: Pyrococcus abyssi");
//        System.out.println("Especie: Chromosome 15");
//
//        DBRegistry eReg = DBRegistry.createRegistryForDataSourceAtReleaseVersion(DataSource.ENSEMBLDB, 72);
//        System.out.println("1");
//        
//        DBCollectionSpecies sp1 = (DBCollectionSpecies) DBRegistry.
//                ensemblgenomesRegistry.getSpeciesByAlias("Pyrococcus abyssi");
//        System.out.println("2");
//        //DBDAOCollectionCoreFactory f =(DBDAOCollectionCoreFactory) ((DBCollectionCoreDatabase) sp1.getMostRecentCoreDatabase()).getCoreFactory(sp1);
//
//        DAChromosome chr = sp1.getChromosomeByName("Chromosome", "15");
//        //DAChromosome chr = f.getChromosomeDAO().getChromosomeByName("Chromosome");
//
//        System.out.println(chr.getSpecies().getSpeciesBinomial());
//        System.out.println(chr.getChromosomeName());
//        System.out.println(chr.getId());
//
//        System.out.println(chr.getBioBegin());
//        System.out.println(chr.getLength());
//        System.out.println(chr.getDBSeqLength());
//        System.out.println(chr.getBioEnd());
//        System.out.println(chr.getDBSeqLength());
//        System.out.println(chr.getCoordSystem().getId());
//        System.out.println(chr.getCoordSystem().getType().toString());
//
//        System.out.println(chr.getSequenceAsString(1, 100));
//        System.out.println(chr.getReverseComplementSequenceAsString(1, 100));
//
//        //lets look at all the features that might be mapped on a chromosome ( very few implemented so far:)
//       System.out.println("\n------------\nFeatures\n-------------");
//
//        for (FeatureType t : FeatureType.getAllTypes()) {
//            System.out.println(t.toString());
//
//            Set<DACoordinateSystem> s = (sp1.getMostRecentCoreDatabase()).getCSForFeature(sp1, t);
//
//            if (s != null && !s.isEmpty()) {
//
//                for (DACoordinateSystem cs : s) {
//                    System.out.println("\tcs-id: " + cs.getId() + " - " + "cs-type: " + cs.getType().toString());
//                }
//            } else {
//                System.out.println("NO INFORMATION");
//            }
//            System.out.println("");
//        }
//
//        DAGene gene;
//        DAOCoreFactory f = ((DBCollectionCoreDatabase)sp1.getDatabaseByTypeAndVersion(
//                    EnsemblDBType.collection_core, "15")).getCoreFactory(sp1);
//        gene = new DAGene(f);
//        gene.setStableID("EBPYRG00000002612");
//        //a gene with a factory and stableID can lazyload everything else....
//        
//         System.out.println("getAnalysisId "+gene.getAnalysisID());
//                  System.out.println("getAnalysis "+gene.getAnalysis().getDisplayLabel());  
//         System.out.println("getAnalysisDescription "+gene.getAnalysis().getDescription());        
//         System.out.println("gene: "+gene.getId()+" ("+gene.getStableID()+") "+gene.getBiotype());
//         System.out.println("\t"+gene.getDescription());
//         System.out.println("Species: "+gene.getSpecies().getCommonName());
//         System.out.println("Created "+gene.getCreationDate().toString());
//         System.out.println("Modified "+gene.getModificationDate().toString());
//         System.out.println("getAssembly "+gene.getAssembly());
//         System.out.println("getBiotype "+gene.getBiotype());
//         System.out.println("getDescription "+gene.getDescription());
//         System.out.println("getDBVersion "+gene.getDBVersion());
//         System.out.println("getDisplayName "+gene.getDisplayName());
//         System.out.println("getId "+gene.getId());
//
//         System.out.println("getSchemaVersion "+gene.getSchemaVersion());
//         System.out.println("getStatus "+gene.getStatus());
//        
//        
//        //v1.15 removed the ability to get a gene by internal db id from a species 
//        // DAGene gene =   sp1.getGeneByID(3555, "15");
//          gene = (DAGene) ((DBCollectionCoreDatabase)sp1.getDatabaseByTypeAndVersion(
//                    EnsemblDBType.collection_core, "15")).getCoreFactory(sp1).getGeneDAO().getGeneByID(3555);
//
//         System.out.println("gene: "+gene.getId()+" ("+gene.getStableID()+") "+gene.getBiotype());
//         System.out.println("\t"+gene.getDescription());
//         System.out.println("Species: "+gene.getSpecies().getCommonName());
//         System.out.println("Created "+gene.getCreationDate().toString());
//         System.out.println("Modified "+gene.getModificationDate().toString());
//         System.out.println("getAssembly "+gene.getAssembly());
//         System.out.println("getBiotype "+gene.getBiotype());
//         System.out.println("getDescription "+gene.getDescription());
//         System.out.println("getDBVersion "+gene.getDBVersion());
//         System.out.println("getDisplayName "+gene.getDisplayName());
//         System.out.println("getId "+gene.getId());
//         System.out.println("getAnalysisId "+gene.getAnalysisID());
//                  System.out.println("getAnalysis "+gene.getAnalysis().getDisplayLabel());  
//         System.out.println("getAnalysisDescription "+gene.getAnalysis().getDescription());
//         System.out.println("getSchemaVersion "+gene.getSchemaVersion());
//         System.out.println("getStatus "+gene.getStatus());
//
//
//         System.out.println("getCanonicalTranscriptID "+gene.getCanonicalTranscriptID());
//         System.out.println("getCanonicalTranscript().getBiotype() "+gene.getCanonicalTranscript().getBiotype());
//         System.out.println("getCanonicalTranscript().getDisplayName() "+gene.getCanonicalTranscript().getDisplayName());
//         System.out.println("getCanonicalTranscript().getStableID() "+gene.getCanonicalTranscript().getStableID());
//         System.out.println("getCanonicalTranscript().getCanonicalTranslationID() "+gene.getCanonicalTranscript().getCanonicalTranslationID());
//                  System.out.println("gene.getCanonicalTranslation().getId() "+gene.getCanonicalTranslation().getId());
//                  System.out.println("getCanonicalTranslation().getStableID() "+gene.getCanonicalTranslation().getStableID());
//         System.out.println("getCanonicalTranscript().getCanonicalTranslation().getProteinSequenceAsString() "+gene.getCanonicalTranscript().getCanonicalTranslation().getProteinSequenceAsString());
//         System.out.println("getCanonicalTranscript().getCanonicalTranslation().getRNASequenceAsString() "+gene.getCanonicalTranscript().getCanonicalTranslation().getRNASequenceAsString());
//
//
//         System.out.println("Gene's loaded mappings...");
//         for (Mapping m : gene.getLoadedMappings()) {
//             System.out.println("HASHES "+m.getSourceHashID()+" "+m.getTargetHashID());
//             System.out.println("Mapping: "+m.getSource().getClass().getSimpleName() + " id: "+ m.getSource().getId() );
//             System.out.println("\tTarget: "+m.getTarget().getClass().getSimpleName() +" id: "+m.getTarget().getId() 
//                     + "type: "+m.getTarget().getType() );
//             System.out.println("\tTargetCoords: "+m.getTargetCoordinates().getStart()
//                     +" - "+m.getTargetCoordinates().getEnd() + "["+m.getTargetCoordinates().getStrand() +"]");
//
//         }
//         System.out.println("");
//         System.out.println("Gene's anotation level mappings...");
//          for (Mapping m : gene.getAnnotationLevelMappings() ) {
//                           System.out.println("HASHES "+m.getSourceHashID()+" "+m.getTargetHashID());
//             System.out.println("Mapping: "+m.getSource().getClass().getSimpleName() + " id: "+ m.getSource().getId() );
//             System.out.println("\tTarget: "+m.getTarget().getClass().getSimpleName() +" id: "+m.getTarget().getId() 
//                     + "type: "+m.getTarget().getType() );
//             System.out.println("\tTargetCoords: "+m.getTargetCoordinates().getStart()
//                     +" - "+m.getTargetCoordinates().getEnd() + "["+m.getTargetCoordinates().getStrand() +"]");
//
//          }
//         System.out.println("");
//        //this single gene will have been added to the chromosome
//        System.out.println("\nGenes mapped on the chromosome:...");
//         for (Mapping m : chr.getLoadedMappings(FeatureType.gene)) {
//            System.out.println(m.getTarget().getClass().getSimpleName()
//                    + " gene stableID: "
//                    + ((DAGene) m.getTarget()).getStableID()
//                    + " id: " + m.getTarget().getId());
//            System.out.println("Gene on chromosome: HashID: " + m.getTarget().getHashID()
//                    + " VMID:" + m.getTarget().hashCode());
//        }
//        
//         gene =   sp1.getGeneByStableID("EBPYRG00000003245", "15");
//         //gene = f.getGeneDAO().getGeneByStableID("EBPYRG00000003245");
//         
//         System.out.println("gene: "+gene.getId()+" ("+gene.getStableID()+") "+gene.getBiotype());
//         System.out.println("\t"+gene.getDescription());
//         System.out.println("Species: "+gene.getSpecies().getCommonName());
//         System.out.println("Created "+gene.getCreationDate().toString());
//         System.out.println("Modified "+gene.getModificationDate().toString());
//         
//         System.out.println("Gene's loaded mappings...");
//         for (Mapping m : gene.getLoadedMappings()) {
//             System.out.println("HASHES "+m.getSourceHashID()+" "+m.getTargetHashID());
//            System.out.println("Mapping source: "+m.getSource().getId());
//             System.out.println("\tTarget: "+m.getTarget().getClass().getSimpleName() +" id: "+m.getTarget().getId()
//                     + "type: "+m.getTarget().getType() );
//            System.out.println("target coords:"+m.getTargetCoordinates().getStart()+"-"
//                     +m.getTargetCoordinates().getEnd() +"("
//                     +m.getTargetCoordinates().getStrand().toString() +")");
//             System.out.println("CS: "+((DADNASequence) m.getTarget()).getCoordSystem().getType().toString());
//         }
//         
//         System.out.println("");
//         System.out.println("Gene's anotation level mappings...");
//          for (Mapping m : gene.getAnnotationLevelMappings() ) {
//                           System.out.println("HASHES "+m.getSourceHashID()+" "+m.getTargetHashID());
//             System.out.println("Mapping: "+m.getSource().getClass().getSimpleName() + " id: "+ m.getSource().getId() );
//             System.out.println("\tTarget: "+m.getTarget().getClass().getSimpleName() +" id: "+m.getTarget().getId() 
//                     + "type: "+m.getTarget().getType() );
//             System.out.println("\tTargetCoords: "+m.getTargetCoordinates().getStart()
//                     +" - "+m.getTargetCoordinates().getEnd() + "["+m.getTargetCoordinates().getStrand() +"]");
//
//          }
//        System.out.println("");
//        //two genes will have been added to the chromosome
//         System.out.println("\nGenes now  mapped on the chromosome:...");         
//         for (Mapping m : chr.getLoadedMappings(FeatureType.gene)) {
//            System.out.println(m.getTarget().getClass().getSimpleName()
//                    + " gene stableID: "
//                    + ((DAGene) m.getTarget()).getStableID()
//                    + " id: " + m.getTarget().getId());
//            System.out.println("Gene on chromosome: HashID: " + m.getTarget().getHashID()
//                    + " VMID:" + m.getTarget().hashCode());
//        }
//         
//         System.out.println("");
//         Coordinate coord = new Coordinate();
//
//         coord.setStart(1);
//         coord.setEnd(1000000);
//
//         List<? extends Gene> genes = chr.getGenesOnRegion(1, 500000, uk.ac.roslin.ensembl.model.Coordinate.Strand.REVERSE_STRAND);
//         System.out.println("");
//         System.out.println(genes.size()+ " returned  genes");
//         System.out.println("total gene mappings: "+chr.getLoadedMappings(FeatureType.gene).size());
//
//         genes = chr.getGenesOnRegion(1,1000000, uk.ac.roslin.ensembl.model.Coordinate.Strand.REVERSE_STRAND);
//         System.out.println(genes.size()+ " returned genes");
//         System.out.println("total gene mappings: "+chr.getLoadedMappings(FeatureType.gene).size());
//
//         genes = chr.getGenesOnRegion(500000,1000000, uk.ac.roslin.ensembl.model.Coordinate.Strand.REVERSE_STRAND);
//         System.out.println(genes.size()+ " returned genes");
//         System.out.println("total gene mappings: "+chr.getLoadedMappings(FeatureType.gene).size());
//
//         genes = chr.getGenesOnRegion(1010000, 1020000, uk.ac.roslin.ensembl.model.Coordinate.Strand.REVERSE_STRAND);
//         System.out.println(genes.size()+ " returned genes");
//         System.out.println("total gene mappings: "+chr.getLoadedMappings(FeatureType.gene).size());
//
//         genes = chr.getGenesOnRegion(1030000, 1040000, uk.ac.roslin.ensembl.model.Coordinate.Strand.REVERSE_STRAND);
//         System.out.println(genes.size()+ " returned genes");
//         System.out.println("total gene mappings: "+chr.getLoadedMappings(FeatureType.gene).size());
//
//         genes = chr.getGenesOnRegion(1050000, 1060000, uk.ac.roslin.ensembl.model.Coordinate.Strand.REVERSE_STRAND);
//         System.out.println(genes.size()+ " returned genes");
//         System.out.println("total gene mappings: "+chr.getLoadedMappings(FeatureType.gene).size());
//
//
//         for (Mapping m : chr.getLoadedMappings(FeatureType.gene)) {
//
//             System.out.println( ((DAGene) m.getTarget()).getStableID()+" "+m.getSourceHashID()+" "+m.getTargetHashID());
//
//         }
//
//
//         System.out.println("gaps in the gene mappings: ");
////         for (Coordinate cd : Coordinate.getCoordinateGaps(chr.getMappedRegions().get(FeatureType.gene))) {
//         for (Coordinate cd : chr.getMappedRegions().get(FeatureType.gene).getGaps()) {
//             System.out.println("gap "+cd.getStart()+" - "+cd.getEnd());
//         }
//         System.out.println("gaps in the gene mappings for the whole chromosome: ");
//         Coordinate cx = new Coordinate(chr.getBioBegin(), chr.getBioEnd(),1);
////         for (Coordinate cd : cx.getGaps(chr.getMappedRegions().get(FeatureType.gene))) {
//         for (Coordinate cd : chr.getMappedRegions().get(FeatureType.gene).getUncoveredRegions(cx)) {
//             System.out.println("gap "+cd.getStart()+" - "+cd.getEnd());
//         }
//
//         Coordinate test = new Coordinate(900000,1200000,1);
//
//         System.out.println("testing 900000-1200000 query");
////         System.out.println("test lies within known region: "+test.liesWithinCoordinateSetWithoutGaps(chr.getMappedRegions().get(FeatureType.gene)));
//         System.out.println("test lies within known region: "+chr.getMappedRegions().get(FeatureType.gene).containsCoordinateWithoutGaps(test));
//
////         if (test.getGaps(chr.getMappedRegions().get(FeatureType.gene)).isEmpty()) {
//         if (chr.getMappedRegions().get(FeatureType.gene).getUncoveredRegions(test).isEmpty()) {
//
//             System.out.println("test region is fully covered");
//
//         } else {
//             System.out.println("test region is not fully covered");
// //            for (Coordinate cd : test.getGaps(chr.getMappedRegions().get(FeatureType.gene))) {
//             for (Coordinate cd : chr.getMappedRegions().get(FeatureType.gene).getUncoveredRegions(test)) {
//                System.out.println("gap "+cd.getStart()+" - "+cd.getEnd());
//             }
//         }
//
////         System.out.println("test has regions not covered: "+test.getGaps(chr.getMappedRegions().get(FeatureType.gene)).size());
//         System.out.println("test has regions not covered: "+chr.getMappedRegions().get(FeatureType.gene).getUncoveredRegions(test).size());
//
//         test = new Coordinate(1,900000,1);
//
//         System.out.println("testing 1-900000 query");
//
////         System.out.println("test lies within known region: "+test.liesWithinCoordinateSetWithoutGaps(chr.getMappedRegions().get(FeatureType.gene)));
//         System.out.println("test lies within known region: "+chr.getMappedRegions().get(FeatureType.gene).containsCoordinateWithoutGaps(test));
//
////         if (test.getGaps(chr.getMappedRegions().get(FeatureType.gene)).isEmpty()) {
//         if (chr.getMappedRegions().get(FeatureType.gene).getUncoveredRegions(test).isEmpty()) {
//             System.out.println("test region is fully covered");
//         } else {
//             System.out.println("test region is not fully covered");
//             for (Coordinate cd : chr.getMappedRegions().get(FeatureType.gene).getUncoveredRegions(test)) {
//                System.out.println("gap "+cd.getStart()+" - "+cd.getEnd());
//             }
//         }
//
// //        System.out.println("test has regions not covered: "+test.getGaps(chr.getMappedRegions().get(FeatureType.gene)).size());
//         System.out.println("test has regions not covered: "+chr.getMappedRegions().get(FeatureType.gene).getUncoveredRegions(test).size());
//
//
//         genes = chr.getGenesOnRegion(1, 10000, uk.ac.roslin.ensembl.model.Coordinate.Strand.REVERSE_STRAND);
//
//         System.out.println(genes.size()+ " genes");
//         
//         DBRegistry ensemblRegistry = new DBRegistry(DataSource.ENSEMBLDB);
//         DBSpecies hs = ensemblRegistry.getSpeciesByAlias("human");
//         
//
//         f = ((DBSingleSpeciesCoreDatabase)hs.getDatabaseByTypeAndVersion(
//                    EnsemblDBType.core, "69")).getCoreFactory();
//        gene = new DAGene(f);
//        gene.setStableID("ENSG00000139618");
//        //a gene with a factory and stableID can lazyload everything else....
//        
//         System.out.println("getAnalysisId "+gene.getAnalysisID());
//         System.out.println("getAnalysisDescription "+gene.getAnalysis().getDescription());   
//         System.out.println("getAnalysis "+gene.getAnalysis().getDisplayLabel());  
//         System.out.println("gene: "+gene.getId()+" ("+gene.getStableID()+") "+gene.getBiotype());
//         System.out.println("\t"+gene.getDescription());
//         System.out.println("Species: "+gene.getSpecies().getCommonName());
//         System.out.println("Created "+gene.getCreationDate().toString());
//         System.out.println("Modified "+gene.getModificationDate().toString());
//         System.out.println("getAssembly "+gene.getAssembly());
//         System.out.println("getBiotype "+gene.getBiotype());
//         System.out.println("getDescription "+gene.getDescription());
//         System.out.println("getDBVersion "+gene.getDBVersion());
//         System.out.println("getDisplayName "+gene.getDisplayName());
//         System.out.println("getId "+gene.getId());
//
//         System.out.println("getSchemaVersion "+gene.getSchemaVersion());
//         System.out.println("getStatus "+gene.getStatus());
//        
//
//       System.out.println("\n\n*****************************\n* COMPLETED FUNCTIONAL TEST *\n*****************************\n");
//*/
//
//    }
//}