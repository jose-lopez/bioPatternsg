/**
 * PeakMotifsRequest.java
 *
 * Desarrollado basado en la WSDL
 * 
 */

package RSATWS;

public class PeakMotifsRequest  implements java.io.Serializable {
    /* Return type. Accepted values: 'server' (result is stored on
     * a file on the server), 'client' (result is directly transferred to
     * the client), or  'both'. 
     * Default is 'both'. */
    private java.lang.String output;

    /* Verbosity. */
    private java.lang.Integer verbosity;

	/* tests Input test peak sequence in fasta format. You need to supply either this parameter or the next one (tmp_test_infile).*/
	private java.lang.String test;
	
	/* temp_test_infile Name of the file with input test peak sequence on the server. You need to supply either this parameter or the previous one (test).*/
	private java.lang.String tmp_test_infile;
	
	/* control Input control peak sequence in fasta format. You can supply either this parameter or the next one (tmp_control_infile) or none.*/
	private java.lang.String control;
	
	/* tmp_control_infile Name of the file with input control peak sequence on the server. You can supply either this parameter or the previous one (control) or none.*/
	private java.lang.String tmp_control_infile;
	
	/* max_seq_length 
	Maximal sequence length.
	Larger sequences  are truncated  at the specified length around the sequence center (from -value/2 to value/2)*/
	private java.lang.Integer max_seq_length;

	/* max_motif_number 
	Maximal number of motifs (matrices) to return for pattern discovery algorithms.
	Note the distinction between the maximal number of motifs (matrices) and the maximum number of patterns (words, dyads): a motif generally corresponds to mutually overlapping several patterns (dyads, words).*/
	private java.lang.Integer max_motif_number;
	
	/* ref_motif
	Reference motif.
	In some cases, we already dispose of a reference motif, for example the motif annotated in some transcription factor database (e.g. RegulonDB, Jaspar, TRANSFAC) for the transcription factor of interest. These annotations may come from low-throughput experiments, and rely on a poor number of sites, but the reference motif may nevertheless be informative, because it is based on several independent studies.
	Each discovered motif can be compared to the reference motif, in order to evaluate its correspondence with the binding motif of the factor of interest.*/
	private java.lang.String ref_motif;
	
	/* top_peaks
	Restrict the analysis to the N peaks at the top of the input sequence file. Some peak calling programs return the peaks sorted by
	score. In such case, the -top_peaks option allows to restrict the analysis to the highest scoring peaks. In some cases, the top-scoring peaks might contain a higher density of binding sites,
	allowing to detect motifs with a higher significance.
	This option can also be convenient for performing quick tests, parameter selection and debugging before running the full analysis of large sequence sets.*/
	private java.lang.Integer top_peaks;
	
	/* min_length 
	Minimal oligonucleotide length. Use in combination with the next option (max_length).
	If those options are used, the program iterates over the specified range of oligonucleotide lengths.*/
	private java.lang.Integer min_length;
	
	/* max_length
	Maximal oligonucleotide length. Use in combination with the previous option (min_length).
	If those options are used, the program iterates over the specified range of oligonucleotide lengths.*/
	private java.lang.Integer max_length;
	
	/* markov 
	Order of the Markov model used to estimatd expected oligonucleotide frequencies for oligo-analysis and local-word-analysis.
	Higher order Markov models are more stringent, lower order are more sensitive, but tend to return a large number of false positives.
	Markov models can be specified with either a positive or a negative value. Positive value indicate the length of the prefix in the transition matrix. Negative value indicate the order of the Markov model relative to the oligonucleotide length. For example, the option -markov -2 gives a model of order m=k-2 (thus, an order 5 for heptanucleotides, an order 4 for hexanucleotides).
	The optimal Markov order depends on the number of sequences in the test set. Since ChIP-seq data typically contain hundreds to thoursands of peaks, high Markov orders are generally good, because they are stringent and still sensitive enough. In our experience, motifs are well detected with the most stringent Markov order (-markov -2).*/
	private java.lang.Integer markov;
	
	/* min_markov Minimal value for markov order. Use in combination with the next option (max_markov).
	If those options are used, the program iterates over the specified range of markov orders.*/
	private java.lang.Integer min_markov;
	
	/* max_markov Maximal value for markov order. Use in combination with the previous option (min_markov).
	If those options are used, the program iterates over the specified range of markov orders.*/
	private java.lang.Integer max_markov;
	
	/* noov No overlapping of oligos allowed if value = 1.
	Disable the detection of overlapping matches for self-overlapping patterns (ex TATATA, GATAGA).*/
	private java.lang.Integer noov;
	
	/* class_int Class interval for position-analysis. The width of the position classes, in number of bases (default: 20)*/
	private java.lang.Integer class_int;
	
	/* str Oligonucleotide occurrences found on both stands are summed (2) or not (1). Default is 2.*/
	private java.lang.Integer str;
	
	/* graph_title Title displayed on top of the graphs.*/
	private java.lang.String graph_title;
	
	/* image_format Image format.
	All the formats supported by XYgraph can be used.*/
	private java.lang.String image_format;
	
	/* disco oligos|dyads|positions|local_words|merged_words|meme|chipmunk
	Specify the software tool(s) that will be used for motif discovery.
	Several algorithms can be specified either by using the option iteratively:
		-disco oligos -disco dyads
	or by entering a comma-separated list of algorithms:
		-disco oligos,dyads

	Default motif discovery algorithms
	oligos: Run oligo-analysis to detect over-represented oligonucleotides of a given length (k, specified with option -l) in the test set (van Helden et al., 1998). Prior frequencies of oligonucleotides are taken from Markov model of order m (see option -markov) estimated from the test set sequences themselves.
	dyads: Run dyad-analysis to detect over-represented dyads, i.e. pairs of short oligonucleotides (monads) spaced by a region of fixed width but variable content (van Helden et al., 2000). Spaced motifs are typical of certain classes of transcription factors forming homo- or heterodimers. By default, chip-seq-analysis analyzes pairs of trinucleotides with any spacing between 0 and 20. The expected frequency of each dyad is estimated as the product of its monad frequencies in the input sequences (option -bg monads of dyad-analysis).
	positions: Run position-analysis to detect oligonucleotides showing a positional bias, i.e. have a non-homogeneous distribution in the peak sequence set.
		This method was initially developed to analyze termination and poly-adenylation signals in downstream sequences (van Helden et al., 2001), and it turns out to be very efficient for detecting motifs centred on the ChIP-seq peaks. For ChIP-seq analysis, the reference position is the center of each sequence.
		Note that chip-seq-analysis also uses position-analysis for the task profiles, in order to detect compositional biases (residues, dinucleotides) in the test sequence set.
		local_words: Run local-word-analysis to detect locally over-represented oligonucleotides and dyads.
	The program local-word-analysis (Matthieu Defrance,unpublished) tests the over-representation of each possible word (oligo, dyad) in positional windows in the input sequence set.
	Two types of background models are supported: (i) Markov model of order m estimated locally (within the window under consideration; (ii) the frequency observed for a word in the whole sequence set is used as estimator of the prior probability of this word in the window.
	After our first trials, this program gives excellent results in ChIP-seq datasets, because its senstivitity increases with large number of sequences (several hundreds/thousands), and its background model is more stringent than for programs computing the global over-representation (oligo-analysis, dyad-analysis).
	merged_words: Extract a position-specific scoring matrix (using matrix-from-patterns) from all the words discovered by the selected string-based motif disovery algorithms (oigos,dyads,positions and/or local_words). */
	private java.lang.String disco;
	
	/* source Enter the source of the fasta sequence file.
Supported source: galaxy
When the sequence file comes from Galaxy, peak coordinates embedded in the fasta headers are extracted and used to convert predicted site coordinates (relative to peak center) to genomic coordinates (in the form of a bed file), which can then be uploaded to the UCSC genome browser as an annotation track.
This option is incompatible with -coord.*/
	private java.lang.String source;
	
	/* task Specify a subset of tasks to be executed.
	By default, the program runs all necessary tasks. However, in some cases, it can be useful to select one or several tasks to be executed separately.
	Beware: task selection requires expertise, because most tasks depends on the prior execution of some other tasks in the workflow. Selecting tasks before their prerequisite tasks have been completed will provoke fatal errors.
	Available Tasks:
		all (default): Run all supported tasks.
		purge: Purge input sequences (test set and, if specified, control set) to mask redundant fragments before applying pattern discovery algorithms. Sequence purging is necessary because redundant fragments would violate the hypothesis of independence underlying the binomial significance test, resulting in a large number of false positive patterns.
		seqlen: Compute sequence lengths and their distribution. Sequence lengths are useful for the negative control (selection of random genome fragments). Sequence length distribution is informative to get an idea about the variability of peak lengths.
		composition: Compute compositional profiles, i.e. distributions of residues and dinucleotide frequencies per position (using position-analysis).
		Residue profiles may reveal composition biases in the neighborhood of the peak sequences. Dinucleotide profiles can reveal (for example) an enrichment in CpG island.
		Note that peak-motifs also runs position-analysis with larger oligonucleotide length (see option -l) to detect motifs on the basis of positionally biased oligonucleotides (see task positions).
		ref_motifs: This task combines various operations.
	Formating of the reference motif
          Perform various format conversion for the reference motif (compute parameters, consensus, logo).
	Motif enrichment
          Generate an enriched motif by scanning the peak sequence set with the reference motif.
	Motif comparison
	  Compare all discovered motifs with the reference motif.
	disco: Run the motif discovery algorithms. See option -disco for the selection of motif discovery algorithm(s).
	merge_words: Merge the words (oligos or dyads) discovered by the different string-based motif discovery algorithms.
		The table of merged words has one row per word (oligo or dyad) and one column per motif discovery program. This table is convenient to analyze the consistency between the words detected by different approaches, e.g. show that a word is both over-represented (oligo-analysis, dyad-analysis) and positionally biased (position-analysis, local-words). A heatmap is also exported to provide a graphical representation of the significance of each word (row) for each algorthm (column).
		The merged words can optionally be used as seeds for extracting position-specific scoring matrices from the sequences, using the program matrix-from-patterns (see option -disco merged_words).
	motif_compa: Motifs are compared in three ways.
		Discovered versus discovered (task cluster_motifs)
        Perform pairwise comparisons between all motifs (matrices) discovered by the different algorithms, to assess their consistency.
		motifs_vs_ref:
	Compare each discovered motif to the reference motif.
		motifs_vs_db:
	Compare each discovered motif to a database of known motifs (e.g. Jaspar, TRANSFAC, RegulonDB, UniProbe, ...)
	timelog: Generate a log file summarizing the time spent in the different tasks.
	synthesis: Generate the HTML file providing a synthesis of the results and pointing towards the individual result files.
	clean_seq: Delete the purged sequence files after the analysis, in order to save space. This task is executed only when it is called explicitly. It is not part of the tasks running with the option "-task all".*/
	private java.lang.String task;
	
	/* */
	
	public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public Integer getVerbosity() {
        return verbosity;
    }

    public void setVerbosity(Integer verbosity) {
        this.verbosity = verbosity;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getTmp_test_infile() {
        return tmp_test_infile;
    }

    public void setTmp_test_infile(String tmp_test_infile) {
        this.tmp_test_infile = tmp_test_infile;
    }

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }

    public String getTmp_control_infile() {
        return tmp_control_infile;
    }

    public void setTmp_control_infile(String tmp_control_infile) {
        this.tmp_control_infile = tmp_control_infile;
    }

    public Integer getMax_seq_length() {
        return max_seq_length;
    }

    public void setMax_seq_length(Integer max_seq_length) {
        this.max_seq_length = max_seq_length;
    }

    public Integer getMax_motif_number() {
        return max_motif_number;
    }

    public void setMax_motif_number(Integer max_motif_number) {
        this.max_motif_number = max_motif_number;
    }

    public String getRef_motif() {
        return ref_motif;
    }

    public void setRef_motif(String ref_motif) {
        this.ref_motif = ref_motif;
    }

    public Integer getTop_peaks() {
        return top_peaks;
    }

    public void setTop_peaks(Integer top_peaks) {
        this.top_peaks = top_peaks;
    }

    public Integer getMin_length() {
        return min_length;
    }

    public void setMin_length(Integer min_length) {
        this.min_length = min_length;
    }

    public Integer getMax_length() {
        return max_length;
    }

    public void setMax_length(Integer max_length) {
        this.max_length = max_length;
    }

    public Integer getMarkov() {
        return markov;
    }

    public void setMarkov(Integer markov) {
        this.markov = markov;
    }

    public Integer getMin_markov() {
        return min_markov;
    }

    public void setMin_markov(Integer min_markov) {
        this.min_markov = min_markov;
    }

    public Integer getMax_markov() {
        return max_markov;
    }

    public void setMax_markov(Integer max_markov) {
        this.max_markov = max_markov;
    }

    public Integer getNoov() {
        return noov;
    }

    public void setNoov(Integer noov) {
        this.noov = noov;
    }

    public Integer getClass_int() {
        return class_int;
    }

    public void setClass_int(Integer class_int) {
        this.class_int = class_int;
    }

    public Integer getStr() {
        return str;
    }

    public void setStr(Integer str) {
        this.str = str;
    }

    public String getGraph_title() {
        return graph_title;
    }

    public void setGraph_title(String graph_title) {
        this.graph_title = graph_title;
    }

    public String getImage_format() {
        return image_format;
    }

    public void setImage_format(String image_format) {
        this.image_format = image_format;
    }

    public String getDisco() {
        return disco;
    }

    public void setDisco(String disco) {
        this.disco = disco;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Object getEqualsCalc() {
        return __equalsCalc;
    }

    public void setEqualsCalc(Object __equalsCalc) {
        this.__equalsCalc = __equalsCalc;
    }

    public boolean isHashCodeCalc() {
        return __hashCodeCalc;
    }

    public void setHashCodeCalc(boolean __hashCodeCalc) {
        this.__hashCodeCalc = __hashCodeCalc;
    }

	
        /* */
	
    public PeakMotifsRequest() {
    }

    public PeakMotifsRequest(			
           java.lang.String output,
           java.lang.Integer verbosity,
           java.lang.String test,
            java.lang.String tmp_test_infile,
            java.lang.String control,
            java.lang.String tmp_control_infile,
            java.lang.Integer max_seq_length,
            java.lang.Integer max_motif_number,
            java.lang.String ref_motif,
            java.lang.Integer top_peaks,
            java.lang.Integer min_length,
            java.lang.Integer max_length,
            java.lang.Integer markov,
            java.lang.Integer min_markov,
            java.lang.Integer max_markov,
            java.lang.Integer noov,
            java.lang.Integer class_int,
            java.lang.Integer str,
            java.lang.String graph_title,
            java.lang.String image_format,
            java.lang.String disco,
            java.lang.String source,
            java.lang.String task)
                   {
           this.output = output;
           this.verbosity = verbosity;
           this.test = test;
           this.tmp_test_infile = tmp_test_infile;
           this.control = control;
           this.tmp_control_infile = tmp_control_infile;
           this.max_seq_length = max_seq_length;
           this.max_motif_number = max_motif_number;
           this.ref_motif = ref_motif;
           this.top_peaks = top_peaks;
           this.min_length = min_length;
           this.max_length = max_length;
           this.markov = markov;
           this.min_markov = min_markov;
           this.max_length = max_markov;
           this.noov = noov;
           this.class_int = class_int;
           this.str = str;
           this.graph_title = graph_title;
           this.image_format = image_format;
           this.disco = disco;
           this.source = source;
           this.task = task;
    }

	/* AQUI VOY */
	
    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PeakMotifsRequest)) return false;
        PeakMotifsRequest other = (PeakMotifsRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.output==null && other.getOutput()==null) || 
             (this.output!=null &&
              this.output.equals(other.getOutput()))) &&
            ((this.verbosity==null && other.getVerbosity()==null) || 
             (this.verbosity!=null &&
              this.verbosity.equals(other.getVerbosity()))) &&
            ((this.test==null && other.getTest()==null) || 
             (this.test!=null &&
              this.test.equals(other.getTest()))) &&
            ((this.tmp_test_infile==null && other.getTmp_test_infile()==null) || 
             (this.tmp_test_infile!=null &&
              this.tmp_test_infile.equals(other.getTmp_test_infile()))) &&
            ((this.control==null && other.getControl()==null) || 
             (this.control!=null &&
              this.control.equals(other.getControl()))) &&
            ((this.tmp_control_infile==null && other.getTmp_control_infile()==null) || 
             (this.tmp_control_infile!=null &&
              this.tmp_control_infile.equals(other.getTmp_control_infile()))) &&
            ((this.max_seq_length==null && other.getMax_seq_length()==null) || 
             (this.max_seq_length!=null &&
              this.max_seq_length.equals(other.getMax_seq_length()))) &&
            ((this.max_motif_number==null && other.getMax_motif_number()==null) || 
             (this.max_motif_number!=null &&
              this.max_motif_number.equals(other.getMax_motif_number()))) &&
            ((this.ref_motif==null && other.getRef_motif()==null) || 
             (this.ref_motif!=null &&
              this.ref_motif.equals(other.getRef_motif()))) &&                       
            ((this.top_peaks==null && other.getTop_peaks()==null) || 
             (this.top_peaks!=null &&
              this.top_peaks.equals(other.getTop_peaks()))) &&            
            ((this.max_length==null && other.getMax_length()==null) || 
             (this.max_length!=null &&
              this.max_length.equals(other.getMax_length()))) &&
            ((this.markov==null && other.getMarkov()==null) || 
             (this.markov!=null &&
              this.markov.equals(other.getMarkov()))) &&    
            ((this.min_markov==null && other.getMin_markov()==null) || 
             (this.min_markov!=null &&
              this.min_markov.equals(other.getMin_markov()))) &&
            ((this.max_markov==null && other.getMax_markov()==null) || 
             (this.max_markov!=null &&
              this.max_markov.equals(other.getMax_markov()))) &&                    
            ((this.noov==null && other.getNoov()==null) || 
             (this.noov!=null &&
              this.noov.equals(other.getNoov()))) &&
            ((this.class_int==null && other.getClass_int()==null) || 
             (this.class_int!=null &&
              this.class_int.equals(other.getClass_int()))) &&                    
            ((this.str==null && other.getStr()==null) || 
             (this.str!=null &&
              this.str.equals(other.getStr()))) &&
            ((this.graph_title==null && other.getGraph_title()==null) || 
             (this.graph_title!=null &&
              this.graph_title.equals(other.getGraph_title()))) &&
            ((this.image_format==null && other.getImage_format()==null) || 
             (this.image_format!=null &&
              this.image_format.equals(other.getImage_format()))) &&
            ((this.disco==null && other.getDisco()==null) || 
             (this.disco!=null &&
              this.disco.equals(other.getDisco()))) &&
            ((this.source==null && other.getSource()==null) || 
             (this.source!=null &&
              this.source.equals(other.getSource()))) &&
            ((this.task==null && other.getTask()==null) || 
             (this.task!=null &&
              this.task.equals(other.getTask())));    
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getOutput() != null) {
            _hashCode += getOutput().hashCode();
        }
        if (getVerbosity() != null) {
            _hashCode += getVerbosity().hashCode();
        }
        if (getTest() != null) {
            _hashCode += getTest().hashCode();
        }
        if (getTmp_test_infile() != null) {
            _hashCode += getTmp_test_infile().hashCode();
        }
        if (getControl() != null) {
            _hashCode += getControl().hashCode();
        }
        
        /* _hashCode += getLength(); */
        
        if (getTmp_control_infile() != null) {
            _hashCode += getTmp_control_infile().hashCode();
        }
        if (getMax_seq_length() != null) {
            _hashCode += getMax_seq_length().hashCode();
        }
        if (getMax_motif_number() != null) {
            _hashCode += getMax_motif_number().hashCode();
        }
        if (getRef_motif() != null) {
            _hashCode += getRef_motif().hashCode();
        }
        if (getTop_peaks() != null) {
            _hashCode += getTop_peaks().hashCode();
        }
        if (getMin_length() != null) {
            _hashCode += getMin_length().hashCode();
        }
        if (getMax_length() != null) {
            _hashCode += getMax_length().hashCode();
        }
        if (getMarkov() != null) {
            _hashCode += getMarkov().hashCode();
        }
        if (getMin_markov() != null) {
            _hashCode += getMin_markov().hashCode();
        }
        if (getMax_markov() != null) {
            _hashCode += getMax_markov().hashCode();
        }
        if (getNoov() != null) {
            _hashCode += getNoov().hashCode();
        }
        if (getClass_int() != null) {
            _hashCode += getClass_int().hashCode();
        }     
        if (getStr() != null) {
            _hashCode += getStr().hashCode();
        }
        if (getGraph_title() != null) {
            _hashCode += getGraph_title().hashCode();
        }
        if (getImage_format() != null) {
            _hashCode += getImage_format().hashCode();
        }
        if (getDisco() != null) {
            _hashCode += getDisco().hashCode();
        }
        if (getSource() != null) {
            _hashCode += getSource().hashCode();
        }
        if (getTask() != null) {
            _hashCode += getTask().hashCode();
        }
        
        
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PeakMotifsRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:RSATWS", "PeakMotifsRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("output");
        elemField.setXmlName(new javax.xml.namespace.QName("", "output"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("verbosity");
        elemField.setXmlName(new javax.xml.namespace.QName("", "verbosity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("test");
        elemField.setXmlName(new javax.xml.namespace.QName("", "test"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tmp_test_infile");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tmp_test_infile"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("control");
        elemField.setXmlName(new javax.xml.namespace.QName("", "control"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tmp_control_infile");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tmp_control_infile"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("max_seq_length");
        elemField.setXmlName(new javax.xml.namespace.QName("", "max_seq_length"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("max_motif_number");
        elemField.setXmlName(new javax.xml.namespace.QName("", "max_motif_number"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ref_motif");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ref_motif"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("top_peaks");
        elemField.setXmlName(new javax.xml.namespace.QName("", "top_peaks"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("min_length");
        elemField.setXmlName(new javax.xml.namespace.QName("", "min_length"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("max_length");
        elemField.setXmlName(new javax.xml.namespace.QName("", "max_length"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("markov");
        elemField.setXmlName(new javax.xml.namespace.QName("", "markov"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("min_markov");
        elemField.setXmlName(new javax.xml.namespace.QName("", "min_markov"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("max_markov");
        elemField.setXmlName(new javax.xml.namespace.QName("", "max_markov"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("noov");
        elemField.setXmlName(new javax.xml.namespace.QName("", "noov"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("class_int");
        elemField.setXmlName(new javax.xml.namespace.QName("", "class_int"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("str");
        elemField.setXmlName(new javax.xml.namespace.QName("", "str"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("graph_title");
        elemField.setXmlName(new javax.xml.namespace.QName("", "graph_title"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("image_format");
        elemField.setXmlName(new javax.xml.namespace.QName("", "image_format"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("disco");
        elemField.setXmlName(new javax.xml.namespace.QName("", "disco"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("source");
        elemField.setXmlName(new javax.xml.namespace.QName("", "source"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("task");
        elemField.setXmlName(new javax.xml.namespace.QName("", "task"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}

