package pipeline;

import java.io.*;
import org.biojava.bio.BioException;
import org.biojava.bio.seq.Sequence;
import org.biojava.bio.seq.SequenceIterator;
import org.biojava.bio.seq.db.HashSequenceDB;
import org.biojava.bio.seq.db.SequenceDB;
import org.biojava.bio.seq.io.SeqIOTools;
import org.biojava.bio.symbol.AlphabetManager;
import org.biojava.utils.ChangeVetoException;

public class ClustalWAlign {
// This are Constants, but I'll change...

    private static final String fileFormat = "fasta";
    public static String clustalwPath = null;
    private SequenceDB dbSequences;
    private String strAlfa;
    private String fileName;
    private String guideTree;
    
    static{
        ClassLoader classLoader;
        classLoader = ClustalWAlign.class.getClassLoader();
        clustalwPath = classLoader.getResource("").getPath();
    }

    public ClustalWAlign() {

        this.dbSequences = new HashSequenceDB();
        this.strAlfa = "DNA";

    }

    public ClustalWAlign(String fileName) {

        this.dbSequences = new HashSequenceDB();
        this.strAlfa = "DNA";
        this.fileName = fileName;

    }

    public ClustalWAlign(SequenceIterator itSequences, String strAlfa) throws BioException, ChangeVetoException {

        this.dbSequences = new HashSequenceDB();

        this.strAlfa = strAlfa;

        while (itSequences.hasNext()) {
            this.dbSequences.addSequence(itSequences.nextSequence());
        }

    }

    public ClustalWAlign(BufferedReader bufSequences, String strAlfa) throws BioException, ChangeVetoException {

        this.dbSequences = new HashSequenceDB();
        this.strAlfa = strAlfa;

        SequenceIterator itSequences = (SequenceIterator) SeqIOTools.fileToBiojava(fileFormat, strAlfa, bufSequences);

        while (itSequences.hasNext()) {
            this.dbSequences.addSequence(itSequences.nextSequence());
        }
    }

    public void addSequence(Sequence seqSequence) throws BioException, ChangeVetoException {
        this.dbSequences.addSequence(seqSequence);
    }

    public void removeSequence(String idSequence) throws BioException, ChangeVetoException {
        this.dbSequences.removeSequence(idSequence);
    }

    public int doMultAlign() {
        int exitVal = 999;

        try {

            System.out.println("Current Path "+clustalwPath);
            
            System.out.println("FileName "+fileName);
            
            FileOutputStream newFile = new FileOutputStream(clustalwPath + fileName + ".fasta");

            SeqIOTools.writeFasta(newFile, this.dbSequences);

            Runtime rt = Runtime.getRuntime();
            //"-output=" + "fasta",

            String[] strComando = {"clustalw",
                "-infile=" + clustalwPath + fileName + ".fasta", 
                "-outfile=" + clustalwPath + fileName + ".aln",
                "-output=" + "fasta",
                "-align"};

            for(int i = 0; i< strComando.length; i++) {
                System.out.println(strComando[i]);
            }
            
            Process proc = rt.exec(strComando);

            InputStream stdin = proc.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(stdin));

            while ((br.readLine()) != null) {
                // do nothing only read "stdout" from ClustalW
                // you can put a System.out.print here to prints
                // the output from ClustalW to console.
                //System.out.print(br);
            }

            exitVal = proc.waitFor();
            
            if (exitVal == 0) {
                this.dbSequences = SeqIOTools.readFasta(
                        new BufferedInputStream(
                        new FileInputStream(
                        clustalwPath
                        + fileName
                        + ".aln")),
                        AlphabetManager.alphabetForName(strAlfa));

                this.guideTree = fileToString(
                        clustalwPath
                        + fileName
                        + ".dnd");
            }
            
            //System.out.println(""+dbSequences);

        } catch (Throwable t) {
            t.printStackTrace();
        }
        return (exitVal);
    }

    public void setAlphabet(String strAlfa) {
        this.strAlfa = strAlfa;
    }

    public SequenceDB getDBSequences() {
        return this.dbSequences;
    }

    public SequenceIterator getIterator() {
        return this.dbSequences.sequenceIterator();
    }

    public String getGuideTree() {
        return guideTree;
    }

    public void setGuideTree(String guideTree) {
        this.guideTree = guideTree;
    }

    private String fileToString(String fileName) {

        String fileBody = "";
        boolean endOfFile = false;

        try {

            FileReader fileClustalW = new FileReader(fileName);
            BufferedReader fileBuffer = new BufferedReader(fileClustalW);

            while (!endOfFile) {
                String fileLine = fileBuffer.readLine();

                if (fileLine == null) {
                    endOfFile = true;
                } else {
                    fileBody = fileBody.concat(fileLine);
                }
            }
            fileBuffer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileBody;
    }
}
