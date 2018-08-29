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
//
//import org.biojava3.core.sequence.DNASequence;
//import org.biojava3.core.sequence.Strand;
//import org.biojava3.core.sequence.compound.NucleotideCompound;
//import org.biojava3.core.sequence.template.Sequence;
//import org.biojava3.core.sequence.template.SequenceView;
//import org.biojava3.core.sequence.views.ComplementSequenceView;
//import org.biojava3.core.sequence.views.ReversedSequenceView;
//import uk.ac.roslin.ensembl.datasourceaware.core.DADNASequence;
//
///**
// *
// * @author paterson
// */
//public class DNASequences {
//    
//    //JEnsembl Datasource Aware DNA Sequences extends org.biojava3.core.sequence.DNASequence 
//    // but with modified behavious specified by implementing uk.ac.roslin.ensembl.model.core.DNASequence
//    
//    //here we look at basic sequence functions of DADNASequences whish are not actually  datasource aware
//    //use of BioJava STRAND is deprecated, Jensembl works in a Positive context unles methods specify 'ReverseComplement'
//    
//
//    public static void main(String[] args) throws Exception {
//
//        System.out.println("Comienzo....");
//        System.out.println("Secuencia: AAAAACCCCGGGTT");
//        
//        
//        DADNASequence seq = new DADNASequence("AAAAACCCCGGGTT");
//
//        System.out.println("seq.toString(): " + seq.toString());
//        System.out.println("seq.getSequenceAsString(): " + seq.getSequenceAsString());
//        System.out.println("seq.getReverseComplementSequenceAsString(): " + seq.getReverseComplementSequenceAsString());
//
//        System.out.println("seq.getSequenceAsString(1, 14, Strand.POSITIVE): " + seq.getSequenceAsString(1, 14, Strand.POSITIVE));
//        System.out.println("seq.getSequenceAsString(1, 14): " + seq.getSequenceAsString(1, 14));
//        System.out.println("seq.getSequenceAsString(11, 14, Strand.POSITIVE): " + seq.getSequenceAsString(11, 14, Strand.POSITIVE));
//        System.out.println("seq.getSequenceAsString(11, 14): " + seq.getSequenceAsString(11, 14));
//        System.out.println("seq.getSequenceAsString(1, 14, Strand.NEGATIVE): " + seq.getSequenceAsString(1, 14, Strand.NEGATIVE));
//        System.out.println("seq.getReverseComplementSequenceAsString(1, 14): " + seq.getReverseComplementSequenceAsString(1, 14));
//        System.out.println("seq.getSequenceAsString(11, 14, Strand.NEGATIVE): " + seq.getSequenceAsString(11, 14, Strand.NEGATIVE));
//        System.out.println("seq.getReverseComplementSequenceAsString(11, 14): " + seq.getReverseComplementSequenceAsString(11, 14));
//        System.out.println("seq.getReverseComplement().getSequenceAsString(): " + seq.getReverseComplement().getSequenceAsString());
//
//
//
//        System.out.println("seq.getReverseComplementSequenceAsString(4, 13): "+seq.getReverseComplementSequenceAsString(4, 13));
//        System.out.println("(new DNASequence(seq.getSubSequence(4, 13).getSequenceAsString())).getReverseComplement().getSequenceAsString(): "+
//                (new DNASequence(seq.getSubSequence(4, 13).getSequenceAsString())).getReverseComplement().getSequenceAsString());
//        System.out.println("seq.getReverseComplement().getSequenceAsString().substring(seq.getBioEnd() - 13, seq.getBioEnd() - 4 + 1): "+
//                seq.getReverseComplement().getSequenceAsString().substring(seq.getBioEnd() - 13, seq.getBioEnd() - 4 + 1));
//
//        //Andy Yates suggestion - I could wrap this in a method
//
//        Sequence<NucleotideCompound> subseq4_13 = seq.getSubSequence(4, 13);
//        Sequence<NucleotideCompound> revComp = new ReversedSequenceView<NucleotideCompound>(new ComplementSequenceView<NucleotideCompound>(subseq4_13));
//        System.out.println("revComp.getSequenceAsString() " + revComp.getSequenceAsString());
//
//        SequenceView rev = seq.getReverseComplement();
//        String revStr = rev.getSequenceAsString();
//        System.out.println("revStr: " + revStr);
//
//        DNASequence subseq = new DNASequence(seq.getSequenceAsString(11, 14));
//        System.out.println("" + subseq.getReverseComplement().getSequenceAsString());
//
//        try {
//            System.out.println("seq.getReverseComplement().getSubSequence(1,4).getSequenceAsString(): "
//                    + seq.getReverseComplement().getSubSequence(1, 4).getSequenceAsString());
//        } catch (Exception e) {
//        }
//
//
//
//        String nuc = "aacccggggtttttMKYRNWSVBDHACTGmkyrnwsvbdh";
//
//        System.out.println("sequence initialised with: " + nuc);
//
//        DADNASequence myseq = new DADNASequence(nuc);
//
//        System.out.println("myseq.getSequenceAsString(): " + myseq.getSequenceAsString());
//        System.out.println("myseq.getReverseComplementSequenceAsString(): " + myseq.getReverseComplementSequenceAsString());
//        System.out.println("myseq.getSequenceAsString(2,5): " + myseq.getSequenceAsString(2, 5, Strand.POSITIVE));
//        System.out.println("myseq.getReverseComplementSequenceAsString(2,5): " + myseq.getReverseComplementSequenceAsString(2, 5));
//
//        DADNASequence aseq = new DADNASequence();
//
//
//        System.out.println("seqlength: " + aseq.getDBSeqLength());
//        System.out.println("biobegin: " + aseq.getBioBegin());
//        System.out.println("bioend: " + aseq.getBioEnd());
//        System.out.println("length: " + aseq.getLength());
//
//
//         System.out.println("\n\n*****************************\n* COMPLETED FUNCTIONAL TEST *\n*****************************\n");
//
//    }
//}
