:- discontiguous enzyme/1.
:- discontiguous transporter/1.
:- discontiguous nuclear_receptor/1.
:- discontiguous generic_transcription_factor/1.
:- discontiguous response_element/1.
:- discontiguous generic_protein/1.

ligand('oxysterols').
nuclear_receptor('RXR').
nuclear_receptor('LXRb').
nuclear_receptor('LXRa').
nuclear_receptor('SHP').
enzyme('CYP7A1').

% The Facts.
% -------------------------------
presence('EGF').
synonymous('MG',['MAGNESIUM ION']).
synonymous('EGF',[]).
nucleotide_exchange_factor('SOS').
synonymous('SOS',['GEF']).
synonymous('MEK',['LAMTOR3','late endosomal/lysosomal adaptor', 'MAPK and MTOR activator 3', 'MAP2K1IP1', 'MAPK scaffold protein 1', 'MAPKSP1', 'mitogen-activated protein kinase kinase 1 interacting protein 1','MAPBP', 'MEK partner 1', 'MP1', 'Ragulator3','MAP2K','MAPKK']).
synonymous('ATP7A',['MNK','ATPase', 'Cu++ transporting', 'alpha polypeptide', 'Menkes syndrome', 'copper pump 1', 'copper-transporting ATPase 1']).
synonymous('MAPK',['ERK']).
cell_receptor('EGFR').
small_molecule('GTP').
small_molecule('DTP').
ligands('CREB', ['MG']). 
synonymous('CREB',['CREB','cAMP response element-binding protein','CREB-binding protein','RSTS', 'Rubinstein-Taybi syndrome','CBP', 'KAT3A', 'RTS']).
response_element('CRE').
dna_response_element('CREB', '5\'-TGACGTCA-3\'').
synonymous('CRE',['cAMP response element']).
synonymous('somatostatin',['SST','prepro-somatostatin', 'SMST', 'somatostatin-14', 'somatostatin-28','SRIF','GHIH','somatotropin release-inhibiting factor','growth hormone-inhibiting hormone', 'SSTR5']).
gen_family('somatostatin','Endogenous ligands').
locus_type('somatostatin', 'gene with protein product').
locus_type('CREB', 'gene with protein product').
ensembl_gen_id('somatostatin', 'ENSG00000157005').
ensembl_gen_id('CREB', 'ENSG00000005339').
pdb_id('somatostatin', '2mi1').
