/**
 * Copyright (C) 2010-13 The Roslin Institute <contact andy.law@roslin.ed.ac.uk>
 *
 * This file is part of JEnsembl: a Java API to Ensembl data sources developed by the
 * Bioinformatics Group at The Roslin Institute, The Royal (Dick) School of
 * Veterinary Studies, University of Edinburgh.
 *
 * Project hosted at: http://jensembl.sourceforge.net
 *
 * This is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License (version 3) as published by
 * the Free Software Foundation.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * in this software distribution. If not, see: http://www.gnu.org/licenses/gpl-3.0.html
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.biojava3.core.sequence.transcription.TranscriptionEngine;
import uk.ac.roslin.ensembl.config.DBConnection.DataSource;
import uk.ac.roslin.ensembl.config.EnsemblCoordSystemType;
import uk.ac.roslin.ensembl.dao.database.DBCollectionSpecies;
import uk.ac.roslin.ensembl.dao.database.DBRegistry;
import uk.ac.roslin.ensembl.dao.database.DBSpecies;
import uk.ac.roslin.ensembl.datasourceaware.core.*;
import uk.ac.roslin.ensembl.model.Mapping;
import uk.ac.roslin.ensembl.model.core.Exon;

public class ExonsTranscriptionAndTranslation {

    
    //demonstrates integration of BioJava3 transcription and translation functions
    //and JEnsembls retrieval and stitching together of exon sequences
    //uses BioJava transcription engines for translation
    //the datasource is queried to use the correct codon table if specified
    //show works with chordate, plant and bacterial genes

    
    public static void main(String[] args) throws Exception {

        DBRegistry eReg = DBRegistry.createRegistryForDataSourceAtReleaseVersion(DataSource.ENSEMBLDB, 72);
        
        //note by default translation by this default engine is configured to convert 
        //non-Methionine initiations to Methionine
       /* valid codes are
         * 1 - UNIVERSAL
         * 2 - VERTEBRATE_MITOCHONDRIAL
         * 3 - YEAST_MITOCHONDRIAL
         * 4 - MOLD_MITOCHONDRIAL
         * 5 - INVERTEBRATE_MITOCHONDRIAL
         * 6 - CILIATE_NUCLEAR
         * 9 - ECHINODERM_MITOCHONDRIAL
         * 10 - EUPLOTID_NUCLEAR
         * 11 - BACTERIAL
         * 12 - ALTERNATIVE_YEAST_NUCLEAR
         * 13 - ASCIDIAN_MITOCHONDRIAL
         * 14 - FLATWORM_MITOCHONDRIAL
         * 15 - BLEPHARISMA_MACRONUCLEAR
         * 16 - 2CHLOROPHYCEAN_MITOCHONDRIAL
         * 21 - TREMATODE_MITOCHONDRIAL
         * 23 - SCENEDESMUS_MITOCHONDRIAL 
         */

        System.out.println("*******************VACA***********************");
        DBSpecies specie = eReg.getSpeciesByAlias("cow");
        DAGene migen = specie.getGeneByStableID("ENSBTAG00000021527","68");
        Mapping mapping = migen.getChromosomeMapping();
        
        //if the genes are not annotated on the chromosome level
       // Mapping mapping = gene.getAnnotationLevelMappings().first();


        System.out.println("Gene: "+ migen.getStableID());
        System.out.println("\tchr start: "+mapping.getTargetCoordinates().getStart());
        System.out.println("\tchr stop: "+mapping.getTargetCoordinates().getEnd());
        System.out.println("\tassembly: "+migen.getAssembly());
        System.out.println("\tdescription: "+ migen.getDescription());
        System.out.println("\tsymbol: "+migen.getDisplayName());
        System.out.println("\tstrand: "+mapping.getTargetCoordinates().getStrand());
        System.out.println("\ttaxonID: "+migen.getSpecies().getTaxonomyID());
        System.out.println("\tstatus: "+migen.getStatus());
        System.out.println("\ttype: "+ migen.getBiotype());

        
        //look at all the transcripts
        
        System.out.println("\tTranscript Count: "+ migen.getTranscripts().size());
        for (DATranscript t : migen.getTranscripts()) {
            System.out.println("\t\tTranscript: "+ t.getStableID());
            System.out.println("\t\t "+ t.getDisplayName());
            System.out.println("\t\t "+ t.getStatus());
            System.out.println("\t\t "+ t.getBiotype());
            System.out.println("\t\t "+ t.getDescription());
            System.out.println("\t\t "+ t.getGene().getStableID());
            System.out.println("\t\tCanonical ?  "+ t.isCanonical());
            System.out.println("\t\tXREF: "+ t.getDisplayXRef().getDBDisplayName());
            System.out.println("\t\tXREF: "+ t.getDisplayXRef().getDisplayID());
            System.out.println("\t\tXREF: "+ t.getDisplayXRef().getInfoType());
            System.out.println("\t\tXREF: "+ t.getDisplayXRef().getInfo());
            for (Mapping m:t.getLoadedMappings(EnsemblCoordSystemType.chromosome)) {
                System.out.println("\t\tMapping: "+m.getTargetHashID());
                System.out.println("\t\t\tCoords: "+m.getTargetCoordinates().toString());
            }

            // look at all the exons of the transcript 
            
            System.out.println("EXONS");
            for (DAExon e : t.getExons()) {
            System.out.println("\t\tRank: "+ e.getRank());
            System.out.println("\t\tStableID: "+ e.getStableID());
            System.out.println("\t\tID: "+ e.getId());
            System.out.println("\t\tstart phase: "+ e.getPhase());
            System.out.println("\t\tend phase: "+ e.getEndPhase());
            System.out.println("\t\tcurrent: "+ e.isCurrent());
            System.out.println("\t\tconstitutive: "+ e.isConstitutive());
            
            //get locations of exon
            for (Mapping m:e.getLoadedMappings(EnsemblCoordSystemType.chromosome)) {
                System.out.println("\t\tMapping: "+m.getTargetHashID());
                System.out.println("\t\t\tCoords: "+m.getTargetCoordinates().toString());
            }
            }




        }
/*
 
        
        List<DAGene> genes = new ArrayList<DAGene>();

        //DBSpecies sp = eReg.getSpeciesByAlias("human");
        System.out.println("*******************HUMANO***********************");
        specie = eReg.getSpeciesByAlias("human");
        //chr1
        //DAGene ge = sp.getGeneByStableID("ENSG00000197049","68");
        

        
         if (migen.getCanonicalTranscript().isTranslated()) {
             System.out.println(migen.getCanonicalTranslation().getTranslatedSequenceAsString());
             System.out.println("");
             //checks whether the codon table is specified by the datasource
             System.out.println(migen.getCanonicalTranslation().getProteinSequence().getSequenceAsString());
             System.out.println("");
         }

        DATranscript tt = (DATranscript) specie.getTranscriptByStableID("ENST00000400701");
        
        System.out.println("A Transcript on chr X with out of phase start sequence: ENST00000400701");
        
         Collection<DAExon> exons = (Collection<DAExon>) tt.getExons();
            
            System.out.println("EXONS:...");

            for (DAExon ex : exons) {
                System.out.print(ex.getRank());
                System.out.print("\t" + ex.getStableID());
                System.out.print("\t" + ex.getId());
                System.out.print("\t" + ex.getPhase());
                System.out.print("\t" + ex.getEndPhase());
                
                
                //not necessarily on a  chromosome :)
                //for (Mapping m : ex.getLoadedMappings(EnsemblCoordSystemType.chromosome)) {
                for (Mapping m : ex.getLoadedMappings()) {
                    System.out.println("\t" +((DADNASequence) m.getTarget()).getName() +": "+ m.getTargetCoordinates().toString());
                }
            }


            if (tt.isTranslated()) {

                DATranslation trl = (DATranslation) tt.getCanonicalTranslation();

                Exon e = trl.getFirstExon();
                Exon e2 = trl.getLastExon();
                Integer i = trl.getFirstExonStart();
                Integer ii = trl.getLastExonEnd();

                System.out.println("\nTranslateable Sequence:");
                System.out.println(trl.getTranslatedSequenceAsString());
                System.out.println("");

                System.out.println("PROTEIN: ");
                System.out.println(trl.getProteinSequence().getSequenceAsString());

            } else {
                System.out.println("\n"+tt.getStableID() + ": NOT TRANSLATED");
            }
        
        //MT
        DAGene g8 = specie.getGeneByStableID("ENSG00000198899", "68");    
            
        //all chr1
        
        DAGene g5 = specie.getGeneByStableID("ENSG00000197049","56");
        DAGene g1 = specie.getGeneByStableID("ENSG00000237330", "68");
        DAGene g6 = specie.getGeneByStableID("ENSG00000237330","56");
        
        //retired after 67
        DAGene g2 = specie.getGeneByStableID("ENSG00000238916", "67");

        System.out.println("*******************VACA***********************");
        DBSpecies sp2 = eReg.getSpeciesByAlias("cow");
        DAGene g3 = sp2.getGeneByStableID("ENSBTAG00000021527", "68");
        DAGene g4 = sp2.getGeneByStableID("ENSBTAG00000021531","68");
        
        System.out.println("*******************POLLO***********************");
        DBSpecies sp3 = eReg.getSpeciesByAlias("chicken");
        DAGene g7 = sp3.getGeneByStableID("ENSGALG00000021950","68");
        
        

        genes.add(g8);
        genes.add(g7);
        genes.add(migen);
        genes.add(g5);
        genes.add(g1);
        genes.add(g6);
        genes.add(g2);
        genes.add(g3);
        genes.add(g4);
        
        DBRegistry eRegG = new DBRegistry(DataSource.ENSEMBLGENOMES);
       
        System.out.println("*******************OTRAS***********************");
        DBSpecies spG = eRegG.getSpeciesByAlias("Arabidopsis lyrata");
        DBSpecies spG2 = eRegG.getSpeciesByAlias("Arabidopsis thaliana");
        DBSpecies spG3 = eRegG.getSpeciesByAlias("Mycobacterium tuberculosis H37Rv");
        
        DAGene geneG1 = spG.getGeneByStableID("fgenesh2_kg.1__2__AT1G02190.2", "15");
        DAGene geneG2 = spG2.getGeneByStableID("AT1G78060", "15");
        DAGene geneG3 = spG3.getGeneByStableID("EBMYCG00000000423", "15");
        
        genes.add(geneG1);
        genes.add(geneG2);
        genes.add(geneG3);



        for (DAGene gene : genes) {
            
            System.out.println("\n"+gene.getSpecies().getShortName()+" GENE: version"+gene.getDBVersion()+"\n");

            DATranscript trp = (DATranscript) gene.getCanonicalTranscript();
            
            System.out.println("CANON Transcript: "+trp.getStableID()+"\n");
            
            
            exons = (Collection<DAExon>) trp.getExons();
            
            System.out.println("EXONS\n");

            for (DAExon ex : exons) {
                System.out.print(ex.getRank());
                System.out.print("\t" + ex.getStableID());
                System.out.print("\t" + ex.getId());
                System.out.print("\t" + ex.getPhase());
                System.out.print("\t" + ex.getEndPhase());
                
                
                //not necessarily on a  chromosome :)
                //for (Mapping m : ex.getLoadedMappings(EnsemblCoordSystemType.chromosome)) {
                for (Mapping m : ex.getLoadedMappings()) {
                    System.out.println("\t" +((DADNASequence) m.getTarget()).getName() +": "+  m.getTargetCoordinates().toString());
                }
            }


            if (trp.isTranslated()) {

                DATranslation trl = (DATranslation) gene.getCanonicalTranslation();

                Exon e = trl.getFirstExon();
                Exon e2 = trl.getLastExon();
                Integer i = trl.getFirstExonStart();
                Integer ii = trl.getLastExonEnd();

                System.out.println("\nTranslateable Sequence:");
                System.out.println(trl.getTranslatedSequenceAsString());
                System.out.println("");

                System.out.println("PROTEIN: ");
                System.out.println(trl.getProteinSequence().getSequenceAsString());

            } else {
                System.out.println("\n"+trp.getStableID() + ": NOT TRANSLATED");
            }
        }
        
        
        
        //showing we can get genes, transcripts and translations by stableID 
        //from a species

        System.out.println("*******************HUMANO***********************");
        DBSpecies hs = eReg.getSpeciesByAlias("human");
        System.out.println("*** HUMAN ***");
        DAGene gene = hs.getGeneByStableID("ENSG00000139618");
       
        DATranscript t = specie.getTranscriptByStableID("ENST00000380152" );
        DATranslation tr = specie.getTranslationByStableID("ENSP00000369497" );
        
        //note the queries used pre v67 need to link to the relevant  stableid table
        DAGene gene60 = hs.getGeneByStableID("ENSG00000139618", "60");
        DATranscript t60 = specie.getTranscriptByStableID("ENST00000380152","60" );        
        DATranslation tr60 = specie.getTranslationByStableID("ENSP00000369497","60" );
        
        
        // can lazy load a gene to a transcript
        DAGene gene2 = t.getGene();
        // can lazy load a transcript to a translation 
        DATranscript t2 = tr.getTranscript();
        
        System.out.println("Gene VegaID: "+gene.getVegaID());
        System.out.println("Gene retrieved from transcript: VegaID: "+gene2.getVegaID());
        System.out.println("Transcript VegaID: "+t.getVegaID());
        System.out.println("Transcript retrieved from translation: VegaID: "+t2.getVegaID());
        System.out.println("Translation VegaID: "+tr.getVegaID());

        
        if (gene.getStableID().equals(gene2.getStableID())) {
            System.out.println("The two genes ARE the same (according to their stableIDS)");
        } else {
            System.out.println("The two genes are NOT the same (according to their stableIDs)");
        }
        if (gene.getStableID().equals(gene60.getStableID())) {
            System.out.println("The  genes pre and post v67 ARE the same (according to their stableIDS)");
        } else {
            System.out.println("The genes pre and post v67 are NOT the same (according to their stableIDs)");
        }
        if (t.getStableID().equals(t2.getStableID())) {
            System.out.println("The two transcripts ARE the same (according to their stableIDS)");
        } else {
            System.out.println("The two transcripts are NOT the same (according to their stableIDs)");
        }
        if (t.getStableID().equals(t60.getStableID())) {
            System.out.println("The  transcripts pre and post v67 ARE the same (according to their stableIDS)");
        } else {
            System.out.println("The  transcripts pre and post v67 are NOT the same (according to their stableIDs)");
        }
        if (tr.getStableID().equals(tr60.getStableID())) {
            System.out.println("The  translations pre and post v67 ARE the same (according to their stableIDS)");
        } else {
            System.out.println("The  translations pre and post v67 are NOT the same (according to their stableIDs)");
        }
        
        System.out.println("*** CERDO ***");
        specie = eReg.getSpeciesByAlias("pig");
         migen = specie.getGeneByStableID("ENSSSCG00000007520");
         //t = sp.getTranscriptByStableID("ENSSSCT00000034146" );
         tr = specie.getTranslationByStableID("ENSSSCP00000028626" );
         t = tr.getTranscript();
         
        System.out.println("Gene VegaID: "+migen.getVegaID());
        System.out.println("Transcript VegaID: "+t.getVegaID());
        System.out.println("Translation VegaID: "+tr.getVegaID());

        //System.out.println("*** Pyrococcus ***");//there won't be any Vega annotations
 
        
        System.out.println("*******************PYROCOCCUS***********************");
        DBCollectionSpecies sp1 = (DBCollectionSpecies) eRegG.getSpeciesByAlias("Pyrococcus abyssi");
        
        DAGene gene1 = sp1.getGeneByStableID("EBPYRG00000002639", "15");
        DATranscript t1 = sp1.getTranscriptByStableID("EBPYRT00000002639" );
        DATranslation tr1 = sp1.getTranslationByStableID("EBPYRP00000002570" );
        DATranscript t12 = tr1.getTranscript();
        DAGene gene12 = t1.getGene();
        
        System.out.println("Gene VegaID: "+gene1.getVegaID());
        System.out.println("Gene retrieved from Transcript VegaID: "+gene12.getVegaID());
        System.out.println("Transcript VegaID: "+t1.getVegaID());
        System.out.println("Transcript retrieved from translation: VegaID: "+t12.getVegaID());
        System.out.println("Translation VegaID: "+tr1.getVegaID());


        if (gene1.getStableID().equals(gene12.getStableID())) {
            System.out.println("The two genes ARE the same (according to their stableIDS)");
        } else {
            System.out.println("The two genes are NOT the same (according to their stableIDs)");
        }        
        if (t1.getStableID().equals(t12.getStableID())) {
            System.out.println("The two transcripts ARE the same (according to their stableIDS)");
        } else {
            System.out.println("The two transcripts are NOT the same (according to their stableIDs)");
        }
        
        System.out.println("\n\n*************************\nCOMPLETED FUNCTIONAL TEST\n*************************\n");
        
 */
   }
}