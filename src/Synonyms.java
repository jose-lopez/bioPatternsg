/**
 * Copyright (C) 2010-13 The Roslin Institute <contact andy.law@roslin.ed.ac.uk>
 *
 * This file is part of JEnsembl: a Java API to Ensembl data sources developed
 * by the Bioinformatics Group at The Roslin Institute, The Royal (Dick) School
 * of Veterinary Studies, University of Edinburgh.
 *
 * Project hosted at: http://jensembl.sourceforge.net
 *
 * This is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License (version 3) as published by the Free
 * Software Foundation.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License in this
 * software distribution. If not, see: http://www.gnu.org/licenses/gpl-3.0.html
 */

import java.util.List;
import uk.ac.roslin.ensembl.config.DBConnection;
import uk.ac.roslin.ensembl.dao.database.DBRegistry;
import uk.ac.roslin.ensembl.dao.database.DBSpecies;
import uk.ac.roslin.ensembl.datasourceaware.DAXRef;
import uk.ac.roslin.ensembl.datasourceaware.core.DAGene;
import uk.ac.roslin.ensembl.datasourceaware.core.DATranscript;
import uk.ac.roslin.ensembl.datasourceaware.core.DATranslation;

public class Synonyms {

    public static void main(String[] args) throws Exception {

        System.out.println("Comienzo....");
        System.out.println("Especie: escherichia coli k12");

        DBRegistry  eReg = DBRegistry.createRegistryForDataSourceAtReleaseVersion(DBConnection.DataSource.ENSEMBLDB, 72);

        DBSpecies ek = eReg.getSpeciesByAlias("escherichia coli k12");

        System.out.println("ek.getGenesForExactName(\"yjbE\")");

        List<DAGene> genesForExactName = ek.getGenesForExactName("yjbE");

        System.out.println("count is: " + genesForExactName.size());

        for (DAGene g : genesForExactName) {

            System.out.println("GENE: " + g.getStableID());

            System.out.println("DisplayXref displayID: " + g.getDisplayXRef().getDisplayID());
            System.out.println("DisplayXref primary accession: " + g.getDisplayXRef().getPrimaryAccession());
            System.out.println("DisplayXref version: " + g.getDisplayXRef().getVersion());
            System.out.println("DisplayXref database: " + g.getDisplayXRef().getDBDisplayName());
            System.out.println("DisplayXref db version: " + g.getDisplayXRef().getDBVersion());
            System.out.println("DisplayXref description: " + g.getDisplayXRef().getDescription());
            System.out.print("DisplayXref synonyms (method1): ");
            for (String s : g.getDisplayXRef().getSynonyms()) {
                System.out.print(s + " | ");
            }
            System.out.println("");
            System.out.print("DisplayXref synonyms (method2): ");
            for (String s : g.getSynonyms(g.getDisplayXRef())) {
                System.out.print(s + " | ");
            }
            System.out.println("");
            System.out.println("VEGA XREFS");

            for (DAXRef x : g.getVegaXRefs()) {
                System.out.println(" displayID: " + x.getDisplayID());
                System.out.println(" primary accession: " + x.getPrimaryAccession());
                System.out.println(" version: " + x.getVersion());
                System.out.println(" database: " + x.getDBDisplayName());
                System.out.println(" db version: " + x.getDBVersion());
                System.out.println(" description: " + x.getDescription());
                System.out.print("Synonyms: ");
                for (String s : x.getSynonyms()) {
                    System.out.print(s + " | ");
                }
                System.out.println("");
            }

            System.out.print("ALL SYNONYMS: ");

            for (String s : g.getAllSynonyms()) {
                System.out.print(s + " | ");
            }
            System.out.println("");

        }
        System.out.println("");
        System.out.println("----------------------------------");
        System.out.println("");

        System.out.println("ek.getGenesForExactName(\"yjbE\", \"9\")");

        genesForExactName = ek.getGenesForExactName("yjbE", "9");

        System.out.println("count is: " + genesForExactName.size());

        for (DAGene g : genesForExactName) {

            System.out.println("GENE: " + g.getStableID());

            System.out.println("DisplayXref displayID: " + g.getDisplayXRef().getDisplayID());
            System.out.println("DisplayXref primary accession: " + g.getDisplayXRef().getPrimaryAccession());
            System.out.println("DisplayXref version: " + g.getDisplayXRef().getVersion());
            System.out.println("DisplayXref database: " + g.getDisplayXRef().getDBDisplayName());
            System.out.println("DisplayXref db version: " + g.getDisplayXRef().getDBVersion());
            System.out.println("DisplayXref description: " + g.getDisplayXRef().getDescription());
            System.out.print("DisplayXref synonyms (method1): ");
            for (String s : g.getDisplayXRef().getSynonyms()) {
                System.out.print(s + " | ");
            }
            System.out.println("");
            System.out.print("DisplayXref synonyms (method2): ");
            for (String s : g.getSynonyms(g.getDisplayXRef())) {
                System.out.print(s + " | ");
            }
            System.out.println("");
            System.out.println("VEGA XREFS");

            for (DAXRef x : g.getVegaXRefs()) {
                System.out.println(" displayID: " + x.getDisplayID());
                System.out.println(" primary accession: " + x.getPrimaryAccession());
                System.out.println(" version: " + x.getVersion());
                System.out.println(" database: " + x.getDBDisplayName());
                System.out.println(" db version: " + x.getDBVersion());
                System.out.println(" description: " + x.getDescription());
                System.out.print("Synonyms: ");
                for (String s : x.getSynonyms()) {
                    System.out.print(s + " | ");
                }
                System.out.println("");
            }

            System.out.print("ALL SYNONYMS: ");

            for (String s : g.getAllSynonyms()) {
                System.out.print(s + " | ");
            }
            System.out.println("");

        }
        System.out.println("");
        System.out.println("----------------------------------");
        System.out.println("");


        //DBRegistry eReg = new DBRegistry(DBConnection.DataSource.ENSEMBLDB);
        DBSpecies hs = eReg.getSpeciesByAlias("human");

        System.out.println("hs.getGenesForExactName(\"MAPK13\")");

        genesForExactName = hs.getGenesForExactName("MAPK13");

        System.out.println("count is: " + genesForExactName.size());

        for (DAGene g : genesForExactName) {

            System.out.println("GENE: " + g.getStableID());

            System.out.println("DisplayXref displayID: " + g.getDisplayXRef().getDisplayID());
            System.out.println("DisplayXref primary accession: " + g.getDisplayXRef().getPrimaryAccession());
            System.out.println("DisplayXref version: " + g.getDisplayXRef().getVersion());
            System.out.println("DisplayXref database: " + g.getDisplayXRef().getDBDisplayName());
            System.out.println("DisplayXref db version: " + g.getDisplayXRef().getDBVersion());
            System.out.println("DisplayXref description: " + g.getDisplayXRef().getDescription());
            System.out.print("DisplayXref synonyms (method1): ");
            for (String s : g.getDisplayXRef().getSynonyms()) {
                System.out.print(s + " | ");
            }
            System.out.println("");
            System.out.print("DisplayXref synonyms (method2): ");
            for (String s : g.getSynonyms(g.getDisplayXRef())) {
                System.out.print(s + " | ");
            }
            System.out.println("");
            System.out.println("VEGA XREFS");

            for (DAXRef x : g.getVegaXRefs()) {
                System.out.println(" displayID: " + x.getDisplayID());
                System.out.println(" primary accession: " + x.getPrimaryAccession());
                System.out.println(" version: " + x.getVersion());
                System.out.println(" database: " + x.getDBDisplayName());
                System.out.println(" db version: " + x.getDBVersion());
                System.out.println(" description: " + x.getDescription());
                System.out.print("Synonyms: ");
                for (String s : x.getSynonyms()) {
                    System.out.print(s + " | ");
                }
                System.out.println("");
            }

            System.out.print("ALL SYNONYMS: ");

            for (String s : g.getAllSynonyms()) {
                System.out.print(s + " | ");
            }
            System.out.println("");

        }
        System.out.println("");
        System.out.println("----------------------------------");
        System.out.println("");


        System.out.println("hs.getGenesForExactName(\"SAPK4\");");
        //SAPK4 is infact an a synonym
        genesForExactName = hs.getGenesForExactName("SAPK4");
        System.out.println("count is: " + genesForExactName.size());
        for (DAGene g : genesForExactName) {

            System.out.println("GENE: " + g.getStableID());

            System.out.println("DisplayXref displayID: " + g.getDisplayXRef().getDisplayID());
            System.out.println("DisplayXref primary accession: " + g.getDisplayXRef().getPrimaryAccession());
            System.out.println("DisplayXref version: " + g.getDisplayXRef().getVersion());
            System.out.println("DisplayXref database: " + g.getDisplayXRef().getDBDisplayName());
            System.out.println("DisplayXref db version: " + g.getDisplayXRef().getDBVersion());
            System.out.println("DisplayXref description: " + g.getDisplayXRef().getDescription());
            System.out.print("DisplayXref synonyms (method1): ");
            for (String s : g.getDisplayXRef().getSynonyms()) {
                System.out.print(s + " | ");
            }
            System.out.println("");
            System.out.print("DisplayXref synonyms (method2): ");
            for (String s : g.getSynonyms(g.getDisplayXRef())) {
                System.out.print(s + " | ");
            }
            System.out.println("");
            System.out.println("VEGA XREFS");

            for (DAXRef x : g.getVegaXRefs()) {
                System.out.println(" displayID: " + x.getDisplayID());
                System.out.println(" primary accession: " + x.getPrimaryAccession());
                System.out.println(" version: " + x.getVersion());
                System.out.println(" database: " + x.getDBDisplayName());
                System.out.println(" db version: " + x.getDBVersion());
                System.out.println(" description: " + x.getDescription());
                System.out.print("Synonyms: ");
                for (String s : x.getSynonyms()) {
                    System.out.print(s + " | ");
                }
                System.out.println("");
            }

            System.out.print("ALL SYNONYMS: ");

            for (String s : g.getAllSynonyms()) {
                System.out.print(s + " | ");
            }
            System.out.println("");
        }

        System.out.println("");
        System.out.println("----------------------------------");
        System.out.println("");

        System.out.println("hs.getGenesForNameBeginning(\"MAPK13\");");
        //SAPK4 is infact an a synonym
        genesForExactName = hs.getGenesForNameBeginning("MAPK13");
        System.out.println("count is: " + genesForExactName.size());
        for (DAGene g : genesForExactName) {

            System.out.println("GENE: " + g.getStableID());

            System.out.println("DisplayXref displayID: " + g.getDisplayXRef().getDisplayID());
            System.out.println("DisplayXref primary accession: " + g.getDisplayXRef().getPrimaryAccession());
            System.out.println("DisplayXref version: " + g.getDisplayXRef().getVersion());
            System.out.println("DisplayXref database: " + g.getDisplayXRef().getDBDisplayName());
            System.out.println("DisplayXref db version: " + g.getDisplayXRef().getDBVersion());
            System.out.println("DisplayXref description: " + g.getDisplayXRef().getDescription());
            System.out.print("DisplayXref synonyms (method1): ");
            for (String s : g.getDisplayXRef().getSynonyms()) {
                System.out.print(s + " | ");
            }
            System.out.println("");
            System.out.print("DisplayXref synonyms (method2): ");
            for (String s : g.getSynonyms(g.getDisplayXRef())) {
                System.out.print(s + " | ");
            }
            System.out.println("");
            System.out.println("VEGA XREFS");

            for (DAXRef x : g.getVegaXRefs()) {
                System.out.println(" displayID: " + x.getDisplayID());
                System.out.println(" primary accession: " + x.getPrimaryAccession());
                System.out.println(" version: " + x.getVersion());
                System.out.println(" database: " + x.getDBDisplayName());
                System.out.println(" db version: " + x.getDBVersion());
                System.out.println(" description: " + x.getDescription());
                System.out.print("Synonyms: ");
                for (String s : x.getSynonyms()) {
                    System.out.print(s + " | ");
                }
                System.out.println("");
            }

            System.out.print("ALL SYNONYMS: ");

            for (String s : g.getAllSynonyms()) {
                System.out.print(s + " | ");
            }
            System.out.println("");

        }

        System.out.println("");
        System.out.println("----------------------------------");
        System.out.println("");

        System.out.println("hs.getGenesForNameBeginning(\"MAPK1\");");
        //SAPK4 is infact an a synonym
        genesForExactName = hs.getGenesForNameBeginning("MAPK1");
        System.out.println("count is: " + genesForExactName.size());
        for (DAGene g : genesForExactName) {

            System.out.println("GENE: " + g.getStableID());

            System.out.println("DisplayXref displayID: " + g.getDisplayXRef().getDisplayID());
            System.out.println("DisplayXref primary accession: " + g.getDisplayXRef().getPrimaryAccession());
            System.out.println("DisplayXref version: " + g.getDisplayXRef().getVersion());
            System.out.println("DisplayXref database: " + g.getDisplayXRef().getDBDisplayName());
            System.out.println("DisplayXref db version: " + g.getDisplayXRef().getDBVersion());
            System.out.println("DisplayXref description: " + g.getDisplayXRef().getDescription());
            System.out.print("DisplayXref synonyms (method1): ");
            for (String s : g.getDisplayXRef().getSynonyms()) {
                System.out.print(s + " | ");
            }
            System.out.println("");
            System.out.print("DisplayXref synonyms (method2): ");
            for (String s : g.getSynonyms(g.getDisplayXRef())) {
                System.out.print(s + " | ");
            }
            System.out.println("");
            System.out.println("VEGA XREFS");

            for (DAXRef x : g.getVegaXRefs()) {
                System.out.println(" displayID: " + x.getDisplayID());
                System.out.println(" primary accession: " + x.getPrimaryAccession());
                System.out.println(" version: " + x.getVersion());
                System.out.println(" database: " + x.getDBDisplayName());
                System.out.println(" db version: " + x.getDBVersion());
                System.out.println(" description: " + x.getDescription());
                System.out.print("Synonyms: ");
                for (String s : x.getSynonyms()) {
                    System.out.print(s + " | ");
                }
                System.out.println("");
            }

            System.out.print("ALL SYNONYMS: ");

            for (String s : g.getAllSynonyms()) {
                System.out.print(s + " | ");
            }
            System.out.println("");

        }

        System.out.println("");
        System.out.println("----------------------------------");
        System.out.println("");


        System.out.println("hs.getGenesForNameBeginning(\"SAPK\", \"57\");");
        //SAPK4 is infact an a synonym
        genesForExactName = hs.getGenesForNameBeginning("SAPK", "57");
        System.out.println("count is: " + genesForExactName.size());

        int count = 1;
        for (DAGene g : genesForExactName) {

            System.out.println("GENE: " + count++ + "  -  " + g.getStableID());

            System.out.println("DisplayXref displayID: " + g.getDisplayXRef().getDisplayID());
            System.out.println("DisplayXref primary accession: " + g.getDisplayXRef().getPrimaryAccession());
            System.out.println("DisplayXref version: " + g.getDisplayXRef().getVersion());
            System.out.println("DisplayXref database: " + g.getDisplayXRef().getDBDisplayName());
            System.out.println("DisplayXref db version: " + g.getDisplayXRef().getDBVersion());
            System.out.println("DisplayXref description: " + g.getDisplayXRef().getDescription());
            System.out.print("DisplayXref synonyms (method1): ");
            for (String s : g.getDisplayXRef().getSynonyms()) {
                System.out.print(s + " | ");
            }
            System.out.println("");
            System.out.print("DisplayXref synonyms (method2): ");
            for (String s : g.getSynonyms(g.getDisplayXRef())) {
                System.out.print(s + " | ");
            }
            System.out.println("");
            System.out.println("VEGA XREFS");

            for (DAXRef x : g.getVegaXRefs()) {
                System.out.println(" displayID: " + x.getDisplayID());
                System.out.println(" primary accession: " + x.getPrimaryAccession());
                System.out.println(" version: " + x.getVersion());
                System.out.println(" database: " + x.getDBDisplayName());
                System.out.println(" db version: " + x.getDBVersion());
                System.out.println(" description: " + x.getDescription());
                System.out.print("Synonyms: ");
                for (String s : x.getSynonyms()) {
                    System.out.print(s + " | ");
                }
                System.out.println("");
            }

            System.out.print("ALL SYNONYMS: ");

            for (String s : g.getAllSynonyms()) {
                System.out.print(s + " | ");
            }
            System.out.println("");
        }

        System.out.println("");
        System.out.println("----------------------------------");
        System.out.println("");


        DAGene gene = hs.getGeneByStableID("ENSG00000156711");

        System.out.println("GENE: " + gene.getStableID());

        System.out.println("DisplayXref displayID: " + gene.getDisplayXRef().getDisplayID());
        System.out.println("DisplayXref primary accession: " + gene.getDisplayXRef().getPrimaryAccession());
        System.out.println("DisplayXref version: " + gene.getDisplayXRef().getVersion());
        System.out.println("DisplayXref database: " + gene.getDisplayXRef().getDBDisplayName());
        System.out.println("DisplayXref db version: " + gene.getDisplayXRef().getDBVersion());
        System.out.println("DisplayXref description: " + gene.getDisplayXRef().getDescription());
        System.out.print("DisplayXref synonyms (method1): ");
        for (String s : gene.getDisplayXRef().getSynonyms()) {
            System.out.print(s + " | ");
        }
        System.out.println("");
        System.out.print("DisplayXref synonyms (method2): ");
        for (String s : gene.getSynonyms(gene.getDisplayXRef())) {
            System.out.print(s + " | ");
        }
        System.out.println("");
        System.out.println("VEGA XREFS");

        for (DAXRef x : gene.getVegaXRefs()) {
            System.out.println(" displayID: " + x.getDisplayID());
            System.out.println(" primary accession: " + x.getPrimaryAccession());
            System.out.println(" version: " + x.getVersion());
            System.out.println(" database: " + x.getDBDisplayName());
            System.out.println(" db version: " + x.getDBVersion());
            System.out.println(" description: " + x.getDescription());
            System.out.print("Synonyms: ");
            for (String s : x.getSynonyms()) {
                System.out.print(s + " | ");
            }
            System.out.println("");
        }

        System.out.print("ALL SYNONYMS: ");

        for (String s : gene.getAllSynonyms()) {
            System.out.print(s + " | ");
        }
        System.out.println("");
        System.out.println("");
        System.out.println("");

        DATranscript transcript = hs.getTranscriptByStableID("ENST00000368759");
        System.out.println("TRANSCRIPT: " + transcript.getStableID());

        System.out.println("DisplayXref displayID: " + transcript.getDisplayXRef().getDisplayID());
        System.out.println("DisplayXref primary accession: " + transcript.getDisplayXRef().getPrimaryAccession());
        System.out.println("DisplayXref version: " + transcript.getDisplayXRef().getVersion());
        System.out.println("DisplayXref database: " + transcript.getDisplayXRef().getDBDisplayName());
        System.out.println("DisplayXref db version: " + transcript.getDisplayXRef().getDBVersion());
        System.out.println("DisplayXref description: " + transcript.getDisplayXRef().getDescription());
        System.out.print("DisplayXref synonyms (method1): ");
        for (String s : transcript.getDisplayXRef().getSynonyms()) {
            System.out.print(s + " | ");
        }
        System.out.println("");
        System.out.print("DisplayXref synonyms (method2): ");
        for (String s : transcript.getSynonyms(transcript.getDisplayXRef())) {
            System.out.print(s + " | ");
        }
        System.out.println("");
        System.out.println("VEGA XREFS");

        for (DAXRef x : transcript.getVegaXRefs()) {
            System.out.println(" displayID: " + x.getDisplayID());
            System.out.println(" primary accession: " + x.getPrimaryAccession());
            System.out.println(" version: " + x.getVersion());
            System.out.println(" database: " + x.getDBDisplayName());
            System.out.println(" db version: " + x.getDBVersion());
            System.out.println(" description: " + x.getDescription());
            System.out.print("Synonyms: ");
            for (String s : x.getSynonyms()) {
                System.out.print(s + " | ");
            }
            System.out.println("");
        }

        System.out.print("ALL SYNONYMS: ");

        for (String s : transcript.getAllSynonyms()) {
            System.out.print(s + " | ");
        }
        System.out.println("");
        System.out.println("");
        System.out.println("");

        DATranslation translation = hs.getTranslationByStableID("ENSP00000261584");
        System.out.println("TRANSLATION: " + translation.getStableID());


        System.out.println("");
        System.out.println("VEGA XREFS");

        for (DAXRef x : translation.getVegaXRefs()) {
            System.out.println(" displayID: " + x.getDisplayID());
            System.out.println(" primary accession: " + x.getPrimaryAccession());
            System.out.println(" version: " + x.getVersion());
            System.out.println(" database: " + x.getDBDisplayName());
            System.out.println(" db version: " + x.getDBVersion());
            System.out.println(" description: " + x.getDescription());
            System.out.print("Synonyms: ");
            for (String s : x.getSynonyms()) {
                System.out.print(s + " | ");
            }
            System.out.println("");
        }

        System.out.print("ALL SYNONYMS: ");

        for (String s : translation.getAllSynonyms()) {
            System.out.print(s + " | ");
        }
        System.out.println("\n\n*************************\nCOMPLETED FUNCTIONAL TEST\n*************************\n");

    }
}
