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
//import java.util.Collection;
//import uk.ac.roslin.ensembl.config.DBConnection;
//import uk.ac.roslin.ensembl.dao.database.DBRegistry;
//import uk.ac.roslin.ensembl.dao.database.DBSpecies;
//import uk.ac.roslin.ensembl.datasourceaware.core.*;
//import uk.ac.roslin.ensembl.model.Coordinate;
//import uk.ac.roslin.ensembl.model.Mapping;
//
//public class TranscriptionAndTranslation {
//
//    
//    //demonstrates integration of BioJava3 transcription and translation functions
//    //and JEnsembl's retrieval and stitching together of exon sequences
//    //uses BioJava transcription engines for translation
//    //the datasource is queried to use the correct codon table if specified
//    //moving between the various axes is demonstrated
//    //i.e. Chromosome, Primary Transcript, Processed Transcript (mRNA), Translation and Protein
//    //and retrieveal of actual sequence data from these various axes
//
//    
//
//    public static void main(String[] args) throws Exception {
//
//        DBRegistry  eReg = DBRegistry.createRegistryForDataSourceAtReleaseVersion(DBConnection.DataSource.ENSEMBLDB, 72);
//        DBSpecies sp = eReg.getSpeciesByAlias("human");
//
//
//        //demo human gene on forward strand, results in comments are from release 68
//        DAGene brca2 = sp.getGeneByStableID("ENSG00000139618", "68");
//        Mapping chromosomeMapping = brca2.getChromosomeMapping();
//        Coordinate.Strand  orientation = chromosomeMapping.getTargetCoordinates().getStrand();
//        DAChromosome chr = (DAChromosome) chromosomeMapping.getTarget();
//        Coordinate targetCoordinates = chromosomeMapping.getTargetCoordinates();
//        Coordinate sourceCoordinates = chromosomeMapping.getSourceCoordinates();
//
//        System.out.println(brca2.getType() + " " + brca2.getDisplayName() + " " + brca2.getStableID());
//        System.out.println("\t" + chr.getType() + " " + chr.getChromosomeName() + " " + targetCoordinates);
//        if (Coordinate.Strand.REVERSE_STRAND.equals(orientation)) {
//            System.out.println("\tChromosome sequence: " + targetCoordinates.getLength() + " "
//                + chr.getReverseComplementSequenceAsString(targetCoordinates.getEnd(), targetCoordinates.getEnd() - 29) + ".....");
//        } else {
//            System.out.println("\tChromosome sequence: " + targetCoordinates.getLength() + " "
//                + chr.getSequenceAsString(targetCoordinates.getStart(), targetCoordinates.getStart() + 29) + ".....");
//        }
//        System.out.println("\tDNASequence: " + brca2.getSequence().getLength() + " : "
//                + brca2.getSequenceAsString(1, 30) + ".....");
//        System.out.println("\tRNASequence: " + brca2.getRNASequence().getLength() + " : "
//                + brca2.getRNASequence().getSequenceAsString(1, 30, null) + ".....");
//        
//        /*
//        gene BRCA2 ENSG00000139618
//                chromosome 13 32889611 - 32973805 (FORWARD_STRAND)
//                Chromosome sequence: 84195 GGGCTTGTGGCGCGAGCTTCTGAAACTAGG.....
//                DNASequence: 84195 : GGGCTTGTGGCGCGAGCTTCTGAAACTAGG.....
//                RNASequence: 84195 : GGGCUUGUGGCGCGAGCUUCUGAAACUAGG.....
//        */        
//
//        System.out.println("");
//        System.out.println("Gene coordinate conversion: " + 1 + " --> " + brca2.convertToTargetPosition(1));
//        System.out.println("Gene coordinate conversion: " + -1 + " --> " + brca2.convertToTargetPosition(-1));
//        System.out.println("Gene coordinate conversion: " + 1 + " --> " + brca2.convertToTargetPosition(1));
//        System.out.println("Gene coordinate conversion: " + -10 + " --> " + brca2.convertToTargetPosition(-10));
//        System.out.println("Gene coordinate conversion: " + 100 + " --> " + brca2.convertToTargetPosition(100));
//
//        /*
//        Gene coordinate conversion: 1 --> 32889611
//        Gene coordinate conversion: -1 --> 32889610
//        Gene coordinate conversion: 1 --> 32889611
//        Gene coordinate conversion: -10 --> 32889601
//        Gene coordinate conversion: 100 --> 32889710        
//        */
//        
//        System.out.println("");
//        DATranscript canonicalTranscript = brca2.getCanonicalTranscript();
//        Mapping chromosomeMapping1 = canonicalTranscript.getChromosomeMapping();
//        DAChromosome chr1 = (DAChromosome) chromosomeMapping1.getTarget();
//        targetCoordinates = chromosomeMapping1.getTargetCoordinates();
//
//        
//
//        System.out.println("");
//        System.out.println(canonicalTranscript.getType() + " " + canonicalTranscript.getDisplayName()
//                + " " + canonicalTranscript.getStableID());
//        System.out.println("\t" + chr1.getType() + " " + chr1.getChromosomeName() + " " + targetCoordinates);
//        
//        
//         System.out.println("getAnalysisId "+canonicalTranscript.getAnalysisID());
//         System.out.println("getAnalysis "+canonicalTranscript.getAnalysis().getDisplayLabel());      
//         System.out.println("getAnalysisDescription "+canonicalTranscript.getAnalysis().getDescription());      
//         System.out.println("getCCDS "+canonicalTranscript.getCcdsID());      
//        
//        if (Coordinate.Strand.REVERSE_STRAND.equals(targetCoordinates.getStrand())) {
//            System.out.println("\tChromosome sequence: " + targetCoordinates.getLength() + " "
//                + chr.getReverseComplementSequenceAsString(targetCoordinates.getEnd(), targetCoordinates.getEnd() - 29) + ".....");
//        } else {
//            System.out.println("\tChromosome sequence: " + targetCoordinates.getLength() + " "
//                + chr.getSequenceAsString(targetCoordinates.getStart(), targetCoordinates.getStart() + 29) + ".....");
//        }    
//        System.out.println("\tDNASequence: " + canonicalTranscript.getSequence().getLength() + " : "
//                + canonicalTranscript.getSequenceAsString(1, 30) + ".....");
//        System.out.println("\tRNASequence: " + canonicalTranscript.getRNASequence().getLength() + " : "
//                + canonicalTranscript.getRNASequence().getSequenceAsString(1, 30, null) + ".....");
//        
//        
//        /*
//        transcript BRCA2-001 ENST00000380152
//                chromosome 13 32889611 - 32973347 (FORWARD_STRAND)
//                Chromosome sequence: 83737 GGGCTTGTGGCGCGAGCTTCTGAAACTAGG.....
//                DNASequence: 83737 : GGGCTTGTGGCGCGAGCTTCTGAAACTAGG.....
//                RNASequence: 83737 : GGGCUUGUGGCGCGAGCUUCUGAAACUAGG.....      
//        */
//
//        System.out.println("");
//        System.out.println("Transcript coordinate conversion: " + 1 + " --> " + canonicalTranscript.convertToTargetPosition(1));//32889611
//        System.out.println("Transcript coordinate conversion: " + -1 + " --> " + canonicalTranscript.convertToTargetPosition(-1));//32889610
//        System.out.println("Transcript coordinate conversion: " + 10 + " --> " + canonicalTranscript.convertToTargetPosition(10));//32889620
//        System.out.println("Transcript coordinate conversion: " + -10 + " --> " + canonicalTranscript.convertToTargetPosition(-10));//32889601
//        System.out.println("Transcript coordinate conversion: " + 50 + " --> " + canonicalTranscript.convertToTargetPosition(50));//32889660
//        System.out.println("Transcript coordinate conversion: " + 194 + " --> " + canonicalTranscript.convertToTargetPosition(194));//32889804
//        System.out.println("Transcript coordinate conversion: " + 948 + " --> " + canonicalTranscript.convertToTargetPosition(948));//32890558
//        System.out.println("Transcript coordinate conversion: " + 10627 + " --> " + canonicalTranscript.convertToTargetPosition(10627));//32900237
//        System.out.println("Transcript coordinate conversion: " + 39387 + " --> " + canonicalTranscript.convertToTargetPosition(39387));//32928997
//        System.out.println("Transcript coordinate conversion: " + 83737 + " --> " + canonicalTranscript.convertToTargetPosition(83737));//32973347
//
//        
//        System.out.println("");
//        Collection<DAExon> exons = canonicalTranscript.getExons();
//        System.out.println("Canonical Transcript Exons: " + exons.size());
//        //Canonical Transcript Exons: 27
//        for (DAExon exon : exons) {
//            System.out.println("\t" + exon.getRank() + ": " + exon.getChromosomeMapping().getTargetCoordinates()
//                    + " [" + exon.getChromosomeMapping().getTargetCoordinates().getLength() + "] "
//                    + exon.getSequenceAsString(1, 30) + ".... phase:" + exon.getPhase() + "/" + exon.getEndPhase());
//        }
//        
//        /*
//	1: 32889611 - 32889804 (FORWARD_STRAND) [194] GGGCTTGTGGCGCGAGCTTCTGAAACTAGG.... phase:-1/-1
//	2: 32890559 - 32890664 (FORWARD_STRAND) [106] ACTTATTTACCAAGCATTGGAGGAATATCG.... phase:-1/1
//	3: 32893214 - 32893462 (FORWARD_STRAND) [249] ATTTAGGACCAATAAGTCTTAATTGGTTTG.... phase:1/1
//	4: 32899213 - 32899321 (FORWARD_STRAND) [109] GAAGGAATGTTCCCAATAGTAGACATAAAA.... phase:1/2
//	5: 32900238 - 32900287 (FORWARD_STRAND) [50] TCCTGTTGTTCTACAATGTACACATGTAAC.... phase:2/1
//	6: 32900379 - 32900419 (FORWARD_STRAND) [41] TGGTATGTGGGAGTTTGTTTCATACACCAA.... phase:1/0
//	7: 32900636 - 32900750 (FORWARD_STRAND) [115] GGTCGTCAGACACCAAAACATATTTCTGAA.... phase:0/1
//	8: 32903580 - 32903629 (FORWARD_STRAND) [50] TCAGAAATGAAGAAGCATCTGAAACTGTAT.... phase:1/0
//	9: 32905056 - 32905167 (FORWARD_STRAND) [112] AATGTGAAAAGCTATTTTTCCAATCATGAT.... phase:0/1
//	10: 32906409 - 32907524 (FORWARD_STRAND) [1116] GATTTGGAAAAACATCAGGGAATTCATTTA.... phase:1/1
//	11: 32910402 - 32915333 (FORWARD_STRAND) [4932] GTTTATTGCATTCTTCTGTGAAAAGAAGCT.... phase:1/1
//	12: 32918695 - 32918790 (FORWARD_STRAND) [96] GAGAACCCTCAATCAAAAGAAACTTATTAA.... phase:1/1
//	13: 32920964 - 32921033 (FORWARD_STRAND) [70] GCACAATAAAAGATCGAAGATTGTTTATGC.... phase:1/2
//	14: 32928998 - 32929425 (FORWARD_STRAND) [428] CACAACTAAGGAACGTCAAGAGATACAGAA.... phase:2/1
//	15: 32930565 - 32930746 (FORWARD_STRAND) [182] ATTTAATTACAAGTCTTCAGAATGCCAGAG.... phase:1/0
//	16: 32931879 - 32932066 (FORWARD_STRAND) [188] CTGTATACGTATGGCGTTTCTAAACATTGC.... phase:0/2
//	17: 32936660 - 32936830 (FORWARD_STRAND) [171] GGCTCTGTGTGACACTCCAGGTGTGGATCC.... phase:2/2
//	18: 32937316 - 32937670 (FORWARD_STRAND) [355] ATATGATACGGAAATTGATAGAAGCAGAAG.... phase:2/0
//	19: 32944539 - 32944694 (FORWARD_STRAND) [156] ATTTCTGCTAACAGTACTCGGCCTGCTCGC.... phase:0/0
//	20: 32945093 - 32945237 (FORWARD_STRAND) [145] TGGATGGAGAAGACATCATCTGGATTATAC.... phase:0/1
//	21: 32950807 - 32950928 (FORWARD_STRAND) [122] AAAACACAACAAAACCATATTTACCATCAC.... phase:1/0
//	22: 32953454 - 32953652 (FORWARD_STRAND) [199] GGTTATTTCAGTGAAGAGCAGTTAAGAGCC.... phase:0/1
//	23: 32953887 - 32954050 (FORWARD_STRAND) [164] TTATACTGAGTATTTGGCGTCCATCATCAG.... phase:1/0
//	24: 32954144 - 32954282 (FORWARD_STRAND) [139] GTTTCAGATGAAATTTTATTTCAGATTTAC.... phase:0/1
//	25: 32968826 - 32969070 (FORWARD_STRAND) [245] GACTTGCCCCTTTCGTCTATTTGTCAGACG.... phase:1/0
//	26: 32971035 - 32971181 (FORWARD_STRAND) [147] AATATTGACATACTTTGCAATGAAGCAGAA.... phase:0/0
//	27: 32972299 - 32973347 (FORWARD_STRAND) [1049] ATGTCTTCTCCTAATTGTGAGATATATTAT.... phase:0/-1        
//        */
//
//        System.out.println("");
//        System.out.println("Primary Transcript coordinate : " + -100 + " --> Processed Transcript: "
//                + canonicalTranscript.convertPrimaryToProcessedTranscriptPosition(-100));//-100
//        System.out.println("Primary Transcript coordinate : " + -1 + " --> Processed Transcript: "
//                + canonicalTranscript.convertPrimaryToProcessedTranscriptPosition(-1));//-1
//        System.out.println("Primary Transcript coordinate : " + 1 + " --> Processed Transcript: "
//                + canonicalTranscript.convertPrimaryToProcessedTranscriptPosition(1));//1
//        System.out.println("Primary Transcript coordinate : " + 194 + " --> Processed Transcript: "
//                + canonicalTranscript.convertPrimaryToProcessedTranscriptPosition(194));//194
//        System.out.println("Primary Transcript coordinate : " + 195 + " --> Processed Transcript: "
//                + canonicalTranscript.convertPrimaryToProcessedTranscriptPosition(195));//null
//        System.out.println("Primary Transcript coordinate : " + 948 + " --> Processed Transcript: "
//                + canonicalTranscript.convertPrimaryToProcessedTranscriptPosition(948));//null
//        System.out.println("Primary Transcript coordinate : " + 949 + " --> Processed Transcript: "
//                + canonicalTranscript.convertPrimaryToProcessedTranscriptPosition(949));//195
//        System.out.println("Primary Transcript coordinate : " + 83737 + " --> Processed Transcript: "
//                + canonicalTranscript.convertPrimaryToProcessedTranscriptPosition(83737));//10930
//        System.out.println("Primary Transcript coordinate : " + 83738 + " --> Processed Transcript: "
//                + canonicalTranscript.convertPrimaryToProcessedTranscriptPosition(83738));//10931
//        System.out.println("Primary Transcript coordinate : " + 837380 + " --> Processed Transcript: "
//                + canonicalTranscript.convertPrimaryToProcessedTranscriptPosition(837380));//764573
//
//        System.out.println("");
//        System.out.println("Processed Transcript coordinate : " + -100 + " --> Primary Transcript: "
//                + canonicalTranscript.convertProcessedToPrimaryTranscriptPosition(-100));//-100
//        System.out.println("Processed Transcript coordinate : " + -1 + " --> Primary Transcript: "
//                + canonicalTranscript.convertProcessedToPrimaryTranscriptPosition(-1));//-1
//        System.out.println("Processed Transcript coordinate : " + 1 + " --> Primary Transcript: "
//                + canonicalTranscript.convertProcessedToPrimaryTranscriptPosition(1));//1
//        System.out.println("Processed Transcript coordinate : " + 194 + " --> Primary Transcript: "
//                + canonicalTranscript.convertProcessedToPrimaryTranscriptPosition(194));//194
//        System.out.println("Processed Transcript coordinate : " + 195 + " --> Primary Transcript: "
//                + canonicalTranscript.convertProcessedToPrimaryTranscriptPosition(195));//949
//        System.out.println("Processed Transcript coordinate : " + 10930 + " --> Primary Transcript: "
//                + canonicalTranscript.convertProcessedToPrimaryTranscriptPosition(10930));//83737
//        System.out.println("Processed Transcript coordinate : " + 10931 + " --> Primary Transcript: "
//                + canonicalTranscript.convertProcessedToPrimaryTranscriptPosition(10931));//83738
//        System.out.println("Processed Transcript coordinate : " + 764573 + " --> Primary Transcript: "
//                + canonicalTranscript.convertProcessedToPrimaryTranscriptPosition(764573));//837380
//
//        System.out.println("");
//        System.out.println("Processed Transcript coordinate : " + -100 + " --> Chromosome: "
//                + canonicalTranscript.convertProcessedTranscriptPositionToChromosome(-100));//32889511
//        System.out.println("Processed Transcript coordinate : " + -1 + " --> Chromosome: "
//                + canonicalTranscript.convertProcessedTranscriptPositionToChromosome(-1));//32889610
//        System.out.println("Processed Transcript coordinate : " + 1 + " --> Chromosome: "
//                + canonicalTranscript.convertProcessedTranscriptPositionToChromosome(1));//32889611
//        System.out.println("Processed Transcript coordinate : " + 194 + " --> Chromosome: "
//                + canonicalTranscript.convertProcessedTranscriptPositionToChromosome(194));//32889804
//        System.out.println("Processed Transcript coordinate : " + 195 + " --> Chromosome: "
//                + canonicalTranscript.convertProcessedTranscriptPositionToChromosome(195));//32890559
//        System.out.println("Processed Transcript coordinate : " + 10930 + " --> Chromosome: "
//                + canonicalTranscript.convertProcessedTranscriptPositionToChromosome(10930));//32973347
//        System.out.println("Processed Transcript coordinate : " + 10931 + " --> Chromosome: "
//                + canonicalTranscript.convertProcessedTranscriptPositionToChromosome(10931));//32973348
//        System.out.println("Processed Transcript coordinate : " + 764573 + " --> Chromosome: "
//                + canonicalTranscript.convertProcessedTranscriptPositionToChromosome(764573));//33726990
//
//        System.out.println("");
//        
//        for (DATranscript tr: brca2.getTranscripts()) {
//            System.out.println("Transcript "+tr.getStableID() +" CCDS ID: "+tr.getCcdsID());
//        }                
//        System.out.println("Canonical Transcript " + (canonicalTranscript.isTranslated() ? "is Translated" : "not Translated"));
//        //Canonical Transcript is Translated
//        DATranslation canonicalTranslation = brca2.getCanonicalTranslation();
//
//        System.out.println("\ttranslation:" + canonicalTranslation.getStableID());
//        //	translation:ENSP00000369497
//        DAExon exon = canonicalTranslation.getFirstExon();
//
//        System.out.println("");
//        System.out.println("first translated exon (exon-" + exon.getRank() + "): " + exon.getChromosomeMapping().getTargetCoordinates()
//                + " [" + exon.getChromosomeMapping().getTargetCoordinates().getLength() + "] "
//                + exon.getSequenceAsString(1, 30) + ".... phase:" + exon.getPhase() + "/" + exon.getEndPhase());
//        System.out.println("Exon 1 coordinate conversion: " + 1 + " --> " + exon.convertToTargetPosition(1));
//        System.out.println("Exon 1 coordinate conversion: " + -1 + " --> " + exon.convertToTargetPosition(-1));
//        System.out.println("Exon 1 coordinate conversion: " + 10 + " --> " + exon.convertToTargetPosition(10));
//        System.out.println("Exon 1 coordinate conversion: " + -10 + " --> " + exon.convertToTargetPosition(-10));
//
//        /*
//        first translated exon (exon-2): 32890559 - 32890664 (FORWARD_STRAND) [106] ACTTATTTACCAAGCATTGGAGGAATATCG.... phase:-1/1
//        Exon 1 coordinate conversion: 1 --> 32890559
//        Exon 1 coordinate conversion: -1 --> 32890558
//        Exon 1 coordinate conversion: 10 --> 32890568
//        Exon 1 coordinate conversion: -10 --> 32890549        
//        */
//        
//        System.out.println("");
//        exon = canonicalTranslation.getLastExon();
//
//        System.out.println("\tlast translated exon: (exon-" + exon.getRank() + "): " + exon.getChromosomeMapping().getTargetCoordinates()
//                + " [" + exon.getChromosomeMapping().getTargetCoordinates().getLength() + "] "
//                + exon.getSequenceAsString(1, 30) + ".... phase:" + exon.getPhase() + "/" + exon.getEndPhase());
//        System.out.println("last Exon coordinate conversion: " + 1 + " --> " + exon.convertToTargetPosition(1));
//        System.out.println("last Exon coordinate conversion: " + -1 + " --> " + exon.convertToTargetPosition(-1));
//        System.out.println("last Exon coordinate conversion: " + 10 + " --> " + exon.convertToTargetPosition(10));
//        System.out.println("last Exon coordinate conversion: " + -10 + " --> " + exon.convertToTargetPosition(-10));
//
//        /*
//                last translated exon: (exon-27): 32972299 - 32973347 (FORWARD_STRAND) [1049] ATGTCTTCTCCTAATTGTGAGATATATTAT.... phase:0/-1
//        last Exon coordinate conversion: 1 --> 32972299
//        last Exon coordinate conversion: -1 --> 32972298
//        last Exon coordinate conversion: 10 --> 32972308
//        last Exon coordinate conversion: -10 --> 32972289        
//        */
//        
//        System.out.println("");
//        System.out.println("First exon  start:" + canonicalTranslation.getFirstExonStart() + " - "
//                + "Last exon end:" + canonicalTranslation.getLastExonEnd());
//        //First exon  start:40 - Last exon end:609
//
//        System.out.println("Translateable Sequence:");
//        System.out.print(canonicalTranslation.getTranslatedSequenceAsString(1, 30) + ".....  ");
//        Integer len = canonicalTranslation.getTranslatedSequenceAsString().length();
//        System.out.println(canonicalTranslation.getTranslatedSequenceAsString(len-29, len));
//        //ATGCCTATTGGATCCAAAGAGAGGCCAACA.....  GACACAATTACAACTAAAAAATATATCTAA
//        System.out.println("");
//        
//        System.out.println("PROTEIN: ");
//        System.out.print(canonicalTranslation.getProteinSequence().getSequenceAsString().subSequence(0, 30) + ".....  ");
//        len = canonicalTranslation.getProteinSequence().getSequenceAsString().length();
//        System.out.println(canonicalTranslation.getProteinSequence().getSequenceAsString().subSequence(len-8,len) );
//        //MPIGSKERPTFFEIFKTRCNKADLGPISLN.....  TITTKKYI
//        System.out.print(canonicalTranslation.getProteinSequenceAsString(1, 30) + ".....  ");
//        len = canonicalTranslation.getProteinSequence().getSequenceAsString().length();
//        System.out.println(canonicalTranslation.getProteinSequence().getSequenceAsString().subSequence(len-8,len) );
//        //MPIGSKERPTFFEIFKTRCNKADLGPISLN.....  TITTKKYI
//        System.out.println("");
//        
//        System.out.println("Translation: 1 --> PrimaryTranscript: " + canonicalTranslation.getPrimaryTranscriptPositionFromBASE(1));//988
//        System.out.println("Translation: 10 --> PrimaryTranscript: " + canonicalTranslation.getPrimaryTranscriptPositionFromBASE(10));//997
//        System.out.println("Translation: 100 --> PrimaryTranscript: " + canonicalTranslation.getPrimaryTranscriptPositionFromBASE(100));//3636
//        System.out.println("Translation: 65 --> PrimaryTranscript: " + canonicalTranslation.getPrimaryTranscriptPositionFromBASE(65));//1052
//        System.out.println("Translation: 66 --> PrimaryTranscript: " + canonicalTranslation.getPrimaryTranscriptPositionFromBASE(66));//1053
//        System.out.println("Translation: 67 --> PrimaryTranscript: " + canonicalTranslation.getPrimaryTranscriptPositionFromBASE(67));//1054
//        System.out.println("Translation: 68 --> PrimaryTranscript: " + canonicalTranslation.getPrimaryTranscriptPositionFromBASE(68));//3604
//        
//        System.out.println("PrimaryTranscript: 988 --> Translation: " + canonicalTranslation.getBasePositionFromPrimaryTranscript(988));//1
//        System.out.println("PrimaryTranscript: 997 --> Translation: " + canonicalTranslation.getBasePositionFromPrimaryTranscript(997));//10
//        System.out.println("PrimaryTranscript: 3636 --> Translation: " + canonicalTranslation.getBasePositionFromPrimaryTranscript(3636));//100
//        System.out.println("PrimaryTranscript: 1052 --> Translation: " + canonicalTranslation.getBasePositionFromPrimaryTranscript(1052));//65
//        System.out.println("PrimaryTranscript: 1053 --> Translation: " + canonicalTranslation.getBasePositionFromPrimaryTranscript(1053));//66
//        System.out.println("PrimaryTranscript: 1054 --> Translation: " + canonicalTranslation.getBasePositionFromPrimaryTranscript(1054));//67
//        System.out.println("PrimaryTranscript: 1055 --> Translation: " + canonicalTranslation.getBasePositionFromPrimaryTranscript(1055));//null
//        System.out.println("PrimaryTranscript: 3604 --> Translation: " + canonicalTranslation.getBasePositionFromPrimaryTranscript(3604));//68
//
//        System.out.println("");
//        
//        System.out.println("Translation: 1 -->  ProcessedTranscript: " + canonicalTranslation.getProcessedTranscriptPositionFromBASE(1));//234
//        System.out.println("Translation: 10 --> ProcessedTranscript: " + canonicalTranslation.getProcessedTranscriptPositionFromBASE(10));//243
//        System.out.println("Translation: 100 --> ProcessedTranscript: " + canonicalTranslation.getProcessedTranscriptPositionFromBASE(100));//333
//        System.out.println("Translation: 65 --> ProcessedTranscript: " + canonicalTranslation.getProcessedTranscriptPositionFromBASE(65));//298
//        System.out.println("Translation: 66 --> ProcessedTranscript: " + canonicalTranslation.getProcessedTranscriptPositionFromBASE(66));//299
//        System.out.println("Translation: 67 --> ProcessedTranscript: " + canonicalTranslation.getProcessedTranscriptPositionFromBASE(67));//300
//        System.out.println("Translation: 68 --> ProcessedTranscript: " + canonicalTranslation.getProcessedTranscriptPositionFromBASE(68));//301
//        
//        System.out.println("ProcessedTranscript: 234 --> Translation: " + canonicalTranslation.getBasePositionFromProcessedTranscript(234));//1
//        System.out.println("ProcessedTranscript: 243 --> Translation: " + canonicalTranslation.getBasePositionFromProcessedTranscript(243));//10
//        System.out.println("ProcessedTranscript: 333 --> Translation: " + canonicalTranslation.getBasePositionFromProcessedTranscript(333));//100
//        System.out.println("ProcessedTranscript: 298 --> Translation: " + canonicalTranslation.getBasePositionFromProcessedTranscript(298));//65
//        System.out.println("ProcessedTranscript: 299 --> Translation: " + canonicalTranslation.getBasePositionFromProcessedTranscript(299));//66
//        System.out.println("ProcessedTranscript: 300 --> Translation: " + canonicalTranslation.getBasePositionFromProcessedTranscript(300));//67
//        System.out.println("ProcessedTranscript: 301 --> Translation: " + canonicalTranslation.getBasePositionFromProcessedTranscript(301));//68
//
//        System.out.println("");
//        
//        System.out.println("Translation: 1 --> Chromosome: " + canonicalTranslation.getChromosomePositionFromBASE(1));//32890598
//        System.out.println("Translation: 10 --> Chromosome: " + canonicalTranslation.getChromosomePositionFromBASE(10));//32890607
//        System.out.println("Translation: 100 --> Chromosome: " + canonicalTranslation.getChromosomePositionFromBASE(100));//32893246
//        System.out.println("Translation: 65 --> Chromosome: " + canonicalTranslation.getChromosomePositionFromBASE(65));//32890662
//        System.out.println("Translation: 66 --> Chromosome: " + canonicalTranslation.getChromosomePositionFromBASE(66));//32890663
//        System.out.println("Translation: 67 --> Chromosome: " + canonicalTranslation.getChromosomePositionFromBASE(67));//32890664
//        System.out.println("Translation: 68 --> Chromosome: " + canonicalTranslation.getChromosomePositionFromBASE(68));//32893214
//        
//        System.out.println("Chromosome: 32890598 --> Translation: " + canonicalTranslation.getBasePositionFromChromosome(32890598));//1
//        System.out.println("Chromosome: 32890607 --> Translation: " + canonicalTranslation.getBasePositionFromChromosome(32890607));//10
//        System.out.println("Chromosome: 32893246 --> Translation: " + canonicalTranslation.getBasePositionFromChromosome(32893246));//100
//        System.out.println("Chromosome: 32890662 --> Translation: " + canonicalTranslation.getBasePositionFromChromosome(32890662));//65
//        System.out.println("Chromosome: 32890663 --> Translation: " + canonicalTranslation.getBasePositionFromChromosome(32890663));//66
//        System.out.println("Chromosome: 32890664 --> Translation: " + canonicalTranslation.getBasePositionFromChromosome(32890664));//67
//        System.out.println("Chromosome: 32890665 --> Translation: " + canonicalTranslation.getBasePositionFromChromosome(32890665));//null
//        System.out.println("Chromosome: 32893214 --> Translation: " + canonicalTranslation.getBasePositionFromChromosome(32893214));//68
//        
//        System.out.println("");
//        
//        System.out.println("Amino Acid: 1 --> PrimaryTranscript: " + canonicalTranslation.getPrimaryTranscriptPositionFromAA(1));//988 - 990 (FORWARD_STRAND)
//        System.out.println("Amino Acid: 3 --> PrimaryTranscript: " + canonicalTranslation.getPrimaryTranscriptPositionFromAA(3));//994 - 996 (FORWARD_STRAND)
//        System.out.println("Amino Acid: 33 --> PrimaryTranscript: " + canonicalTranslation.getPrimaryTranscriptPositionFromAA(33));//3633 - 3635 (FORWARD_STRAND)
//        System.out.println("Amino Acid: 23 --> PrimaryTranscript: " + canonicalTranslation.getPrimaryTranscriptPositionFromAA(23));//1054 - 3605 (FORWARD_STRAND)
//        System.out.println("Amino Acid: 24 --> PrimaryTranscript: " + canonicalTranslation.getPrimaryTranscriptPositionFromAA(24));//3606 - 3608 (FORWARD_STRAND)
//        System.out.println("Amino Acid: 25 --> PrimaryTranscript: " + canonicalTranslation.getPrimaryTranscriptPositionFromAA(25));//3609 - 3611 (FORWARD_STRAND)
//        
//        System.out.println("PrimaryTranscript: 988 --> Amino Acid: " + canonicalTranslation.getAAPositionFromPrimaryTranscript(988));//1
//        System.out.println("PrimaryTranscript: 989 --> Amino Acid: " + canonicalTranslation.getAAPositionFromPrimaryTranscript(989));//1
//        System.out.println("PrimaryTranscript: 990 --> Amino Acid: " + canonicalTranslation.getAAPositionFromPrimaryTranscript(990));//1
//        System.out.println("PrimaryTranscript: 994 --> Amino Acid: " + canonicalTranslation.getAAPositionFromPrimaryTranscript(994));//3
//        System.out.println("PrimaryTranscript: 3633 --> Amino Acid: " + canonicalTranslation.getAAPositionFromPrimaryTranscript(3633));//33
//        System.out.println("PrimaryTranscript: 1054 --> Amino Acid: " + canonicalTranslation.getAAPositionFromPrimaryTranscript(1054));//23
//        System.out.println("PrimaryTranscript: 1055 --> Amino Acid: " + canonicalTranslation.getAAPositionFromPrimaryTranscript(1055));//null
//        System.out.println("PrimaryTranscript: 3606 --> Amino Acid: " + canonicalTranslation.getAAPositionFromPrimaryTranscript(3606));//24
//        System.out.println("PrimaryTranscript: 3609 --> Amino Acid: " + canonicalTranslation.getAAPositionFromPrimaryTranscript(3609));//25
//                
//        System.out.println("");
//        
//        System.out.println("Amino Acid: 1 --> ProcessedTranscript: " + canonicalTranslation.getProcessedTranscriptPositionFromAA(1));//234 - 236 (FORWARD_STRAND)
//        System.out.println("Amino Acid: 3 --> ProcessedTranscript: " + canonicalTranslation.getProcessedTranscriptPositionFromAA(3));//240 - 242 (FORWARD_STRAND)
//        System.out.println("Amino Acid: 33 --> ProcessedTranscript: " + canonicalTranslation.getProcessedTranscriptPositionFromAA(33));//330 - 332 (FORWARD_STRAND)
//        System.out.println("Amino Acid: 23 --> ProcessedTranscript: " + canonicalTranslation.getProcessedTranscriptPositionFromAA(23));//300 - 302 (FORWARD_STRAND)
//        System.out.println("Amino Acid: 24 --> ProcessedTranscript: " + canonicalTranslation.getProcessedTranscriptPositionFromAA(24));//303 - 305 (FORWARD_STRAND)
//        System.out.println("Amino Acid: 25 --> ProcessedTranscript: " + canonicalTranslation.getProcessedTranscriptPositionFromAA(25));//306 - 308 (FORWARD_STRAND)
//        
//        System.out.println("ProcessedTranscript: 234 --> Amino Acid: " + canonicalTranslation.getAAPositionFromProcessedTranscript(234));//1
//        System.out.println("ProcessedTranscript: 235 --> Amino Acid: " + canonicalTranslation.getAAPositionFromProcessedTranscript(235));//1
//        System.out.println("ProcessedTranscript: 236 --> Amino Acid: " + canonicalTranslation.getAAPositionFromProcessedTranscript(236));//1
//        System.out.println("ProcessedTranscript: 240 --> Amino Acid: " + canonicalTranslation.getAAPositionFromProcessedTranscript(240));//3
//        System.out.println("ProcessedTranscript: 330 --> Amino Acid: " + canonicalTranslation.getAAPositionFromProcessedTranscript(330));//33
//        System.out.println("ProcessedTranscript: 300 --> Amino Acid: " + canonicalTranslation.getAAPositionFromProcessedTranscript(300));//23
//        System.out.println("ProcessedTranscript: 303 --> Amino Acid: " + canonicalTranslation.getAAPositionFromProcessedTranscript(303));//24
//        System.out.println("ProcessedTranscript: 306 --> Amino Acid: " + canonicalTranslation.getAAPositionFromProcessedTranscript(306));//25
//        
//        System.out.println("");
//        
//        System.out.println("Amino Acid: 1 --> Chromosome: " + canonicalTranslation.getChromosomePositionFromAA(1));//32890598 - 32890600 (FORWARD_STRAND)
//        System.out.println("Amino Acid: 3 --> Chromosome: " + canonicalTranslation.getChromosomePositionFromAA(3));//32890604 - 32890606 (FORWARD_STRAND)
//        System.out.println("Amino Acid: 33 --> Chromosome: " + canonicalTranslation.getChromosomePositionFromAA(33));//32893243 - 32893245 (FORWARD_STRAND)
//        System.out.println("Amino Acid: 23 --> Chromosome: " + canonicalTranslation.getChromosomePositionFromAA(23));//32890664 - 32893215 (FORWARD_STRAND)
//        System.out.println("Amino Acid: 24 --> Chromosome: " + canonicalTranslation.getChromosomePositionFromAA(24));// 32893216 - 32893218 (FORWARD_STRAND)
//        System.out.println("Amino Acid: 25 --> Chromosome: " + canonicalTranslation.getChromosomePositionFromAA(25));//32893219 - 32893221 (FORWARD_STRAND)
//
//        System.out.println("Chromosome: 32890598 --> Amino Acid: " + canonicalTranslation.getAAPositionFromChromosome(32890598));//1
//        System.out.println("Chromosome: 32890599 --> Amino Acid: " + canonicalTranslation.getAAPositionFromChromosome(32890599));//1
//        System.out.println("Chromosome: 32890600 --> Amino Acid: " + canonicalTranslation.getAAPositionFromChromosome(32890600));//1
//        System.out.println("Chromosome: 32890604 --> Amino Acid: " + canonicalTranslation.getAAPositionFromChromosome(32890604));//3
//        System.out.println("Chromosome: 32893243 --> Amino Acid: " + canonicalTranslation.getAAPositionFromChromosome(32893243));//33
//        System.out.println("Chromosome: 32890664 --> Amino Acid: " + canonicalTranslation.getAAPositionFromChromosome(32890664));//23
//        System.out.println("Chromosome: 32890665 --> Amino Acid: " + canonicalTranslation.getAAPositionFromChromosome(32890665));//null
//        System.out.println("Chromosome: 32893216 --> Amino Acid: " + canonicalTranslation.getAAPositionFromChromosome(32893216));//24
//        System.out.println("Chromosome: 32893219 --> Amino Acid: " + canonicalTranslation.getAAPositionFromChromosome(32893219));//25        
//        
//        System.out.println("");
//        
//        System.out.println("Translation\tAA\tchromosome\tbase\tcodon\tAA\tPrimaryT\tProcessedT");
//        for (Mapping m: canonicalTranslation.getTranslationMappings()) {
//            
//            int i = m.getSourceCoordinates().getStart(); 
//            System.out.print(i + "\t\t" +(i+2)/3 +"\t" + canonicalTranslation.getChromosomePositionFromBASE(i) + "\t");
//            System.out.print(canonicalTranslation.getTranslatedSequenceAsString(i, i) +"\t");
//            System.out.print(canonicalTranslation.getTranslatedSequenceAsString(3*((i+2)/3)-2, 3*((i+2)/3) )+"\t");
//            System.out.print(canonicalTranslation.getProteinSequenceAsString((i+2)/3,(i+2)/3) +"\t");
//            System.out.print(canonicalTranslation.getPrimaryTranscriptPositionFromBASE(i) + "\t\t");
//            System.out.print(canonicalTranslation.getProcessedTranscriptPositionFromBASE(i) + "\n");    
//            
//            i = m.getSourceCoordinates().getEnd(); 
//            if (i==10257){
//                System.out.print("");
//            }            
//            System.out.print(i + "\t\t" +(i+2)/3 +"\t" + canonicalTranslation.getChromosomePositionFromBASE(i) + "\t");
//            System.out.print(canonicalTranslation.getTranslatedSequenceAsString(i, i) +"\t");
//            System.out.print(canonicalTranslation.getTranslatedSequenceAsString(3*((i+2)/3)-2, 3*((i+2)/3) )+"\t");
//            System.out.print(canonicalTranslation.getProteinSequenceAsString((i+2)/3,(i+2)/3) +"\t");
//            System.out.print(canonicalTranslation.getPrimaryTranscriptPositionFromBASE(i) + "\t\t");
//            System.out.print(canonicalTranslation.getProcessedTranscriptPositionFromBASE(i) + "\n");            
//
//        }
//
//        /*
//        Translation	AA	chromosome	base	codon	AA	PrimaryT	ProcessedT
//        1		1	32890598	A	ATG	M	988		234
//        67		23	32890664	G	GAT	D	1054		300
//        68		23	32893214	A	GAT	D	3604		301
//        316		106	32893462	G	GGA	G	3852		549
//        317		106	32899213	G	GGA	G	9603		550
//        425		142	32899321	G	AGT	S	9711		658
//        426		142	32900238	T	AGT	S	10628		659
//        475		159	32900287	G	GTG	V	10677		708
//        476		159	32900379	T	GTG	V	10769		709
//        516		172	32900419	G	AAG	K	10809		749
//        517		173	32900636	G	GGT	G	11026		750
//        631		211	32900750	G	GTC	V	11140		864
//        632		211	32903580	T	GTC	V	13970		865
//        681		227	32903629	T	GCT	A	14019		914
//        682		228	32905056	A	AAT	N	15446		915
//        793		265	32905167	G	GGA	G	15557		1026
//        794		265	32906409	G	GGA	G	16799		1027
//        1909		637	32907524	G	GGT	G	17914		2142
//        1910		637	32910402	G	GGT	G	20792		2143
//        6841		2281	32915333	G	GGA	G	25723		7074
//        6842		2281	32918695	G	GGA	G	29085		7075
//        6937		2313	32918790	G	GGC	G	29180		7170
//        6938		2313	32920964	G	GGC	G	31354		7171
//        7007		2336	32921033	G	CGC	R	31423		7240
//        7008		2336	32928998	C	CGC	R	39388		7241
//        7435		2479	32929425	G	GAT	D	39815		7668
//        7436		2479	32930565	A	GAT	D	40955		7669
//        7617		2539	32930746	G	CAG	Q	41136		7850
//        7618		2540	32931879	C	CTG	L	42269		7851
//        7805		2602	32932066	G	AGG	R	42456		8038
//        7806		2602	32936660	G	AGG	R	47050		8039
//        7976		2659	32936830	G	AGA	R	47220		8209
//        7977		2659	32937316	A	AGA	R	47706		8210
//        8331		2777	32937670	G	AAG	K	48060		8564
//        8332		2778	32944539	A	ATT	I	54929		8565
//        8487		2829	32944694	G	CAG	Q	55084		8720
//        8488		2830	32945093	T	TGG	W	55483		8721
//        8632		2878	32945237	G	GAA	E	55627		8865
//        8633		2878	32950807	A	GAA	E	61197		8866
//        8754		2918	32950928	G	GAG	E	61318		8987
//        8755		2919	32953454	G	GGT	G	63844		8988
//        8953		2985	32953652	G	GTT	V	64042		9186
//        8954		2985	32953887	T	GTT	V	64277		9187
//        9117		3039	32954050	G	CCG	P	64440		9350
//        9118		3040	32954144	G	GTT	V	64534		9351
//        9256		3086	32954282	G	GGA	G	64672		9489
//        9257		3086	32968826	G	GGA	G	79216		9490
//        9501		3167	32969070	G	GAG	E	79460		9734
//        9502		3168	32971035	A	AAT	N	81425		9735
//        9648		3216	32971181	G	CTG	L	81571		9881
//        9649		3217	32972299	A	ATG	M	82689		9882
//        10257		3419	32972907	A	TAA	*	83297		10490        
//        
//        */
//        System.out.println("\n\n*************************\nCOMPLETED FUNCTIONAL TEST\n*************************\n");
//    }
//}