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


import java.io.*;
import java.util.regex.Matcher;
/**
 *
 * @author usuario
 */
import java.util.regex.Pattern;
//import jpl.Query;

import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.Sequence;
import org.biojava.bio.seq.SequenceIterator;

public class MultAlignTest {

    public MultAlignTest(String[] V, String cad) {
        System.out.println("Tamaño: " + V.length);
        String[] secuencias = new String[(V.length)+1];
        for (int i = 0; i < V.length; i++) {
            secuencias[i] = null;            
        }
        String cadena = null;
        String consenso = "";
        char caracter;
        int i = 0;
        int band;
        int j, k;
        try {
            ClustalWAlign alSequences = new ClustalWAlign("archivodesecuencias");
            alSequences.addSequence(DNATools.createDNASequence(cad, "SeqProble"));

            for (int l = 0; l < V.length; l++) {
                System.out.println("V??? "+V[l]);
                alSequences.addSequence(DNATools.createDNASequence(V[l], "Sequence" + (l + 1)));
            }
            //System.in.read();
            
            alSequences.doMultAlign();
            
            SequenceIterator it = alSequences.getIterator();

            System.out.println("Antes...");
            while (it.hasNext()) {
                Sequence seq = it.nextSequence();
                System.out.println(seq.getName() + ": " + seq.seqString());
                secuencias[i] = seq.seqString();
                i++;
            }
        } catch (Throwable t) {
            t.printStackTrace();
            System.out.println(t.getMessage());
        }
    }
}
