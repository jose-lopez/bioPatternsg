% The dimerization is the result of the interaction of two proteins. It is normal to find verbs like
% to interact and to associate instead of to dimerize. If three proteins interact then we have a 
% trimerization process and we talk about a trimerized complex. If more than three proteins interact
% we talk about a hetedimerized complex.
% synonymous(trimerize, [trimerizes, trimerized, trimerizing]).
% synonymous(heterodimerize, [heterodimerizes, heterodimerized, heterodimerizing]).
% synonymous(interact, [interacts, interacted, interacting]).
% synonymous(associate, [associates, associated, associating]).
%% Esta puede estar tanto en eventos iniciales como finales. Se debe deben cheqeuar sus sujetos, objetos.
% synonymous(bind,[binds, bound, binding]).
% synonymous(activate,[activates, activated, activating]).
% synonymous(phosphorylate, [phosphorylates, phosphorylated, phosphorylating]).
% synonymous(recruit, [recruits, recruited, recruiting]).
% synonymous(dimerize, [dimerizes, dimerized, dimerizing]).
%% Esta puede estar tanto en eventos iniciales como finales. Se debe deben cheqeuar sus sujetos, objetos.
% synonymous('enhance', ['enhances', 'enhanced', 'enhancing']).
% %% 
% !!! Estas solo aceptables en eventos finales !!!.
% synonymous(regulate, [regulates, regulated, regulating]).
% synonymous('transcriptional activate', ['transcriptional activates', 'transcriptional activated', 'transcriptional activating']).
% synonymous('up regulate', ['up regulates', 'up regulated', 'up regulating']).
% synonymous('down regulate', ['down regulates', 'down regulated', 'down regulating']).
% synonymous(inhibit, [inhibits, inhibited, inhibiting]).
% synonymous(stimulate, [stimulates, stimulated, stimulating]).




patron(P) :-
	comienzo(Inicio), Inicio = [event(_,_,N)], 
	eventos_intermedios(N, Intermedios, NN),
	final(NN, Final),
	une(Inicio, Intermedios, H),
	une(H, Final, P).

% The KB regulatory events that we need from the Summarizer.
[baseC].
% 


une([], L, L).
une([P|R], L, [P|NR]) :- not(member(P, L)), une(R, L, NR).
une([P|R], L, NR) :- member(P, L), une(R, L, NR). 

comienzo([event(N, Rel, NN)]) :-
	ligand(N), base(B), member(event(N, Rel, NN), B).
	member(Rel, [bind, recognize, interact, associate]).

eventos_intermedios(N1, [event(N1, Rel1, N), event(N, Rel2, N2)], N2) :- 
	base(B), 
	member(event(N1, Rel1, N), B),
	member(Rel1, [interact, associate, activate, phosphorylate, recruit, dimerize, trimerize, heterodimerize, translocate]),
	member(event(N, Rel2, N2), B). 
	member(Rel1, [interact, associate, activate, phosphorylate, recruit, dimerize, trimerize, heterodimerize, translocate]),


eventos_intermedios(N1, [event(N1, Rel1, N), event(N, Rel2, N2)|R], NL) :-
	base(B), 
	member(event(N1, Rel1, N), B),
	member(Rel1, [interact, associate, activate, phosphorylate, recruit, dimerize, trimerize, heterodimerize, translocate]),
	member(event(N, Rel2, N2), B). 
	member(Rel1, [interact, associate, activate, phosphorylate, recruit, dimerize, trimerize, heterodimerize, translocate]),

final(N, [event(N, Rel, NN)]) :- base(B), 
	protein(N), 
	member(event(N, Rel, NN), B),
	member(Rel, [bind, regulate, stimulate, induce, enhance, inhibit, transcriptional-activate]),
	protein(NN).


% Restrictions.




% Well known rules in cellular signalling.
% ---------------------------------------
activates(X,Y) :- phosphorylates(X,Y).
phosphorylated(X) :- phosphorylates(Y,X).
associates(X,Y) :- activates(X,Y).
interacts(X,Y) :- activates(X,Y).
protein(X):-enzime(X).
protein(X):-growth_factor(X).
protein(X):-receptor(X).
protein(X):-adaptor_protein(X).
protein(X):-transcription_factor(X).
protein(X):-hormone(X).
dna_sequence(X):-response_element(X). % synonymous ('response element', 'motif').
dna_sequence(X):-motif(X).
adaptor_protein(X) :-nucleotide_exchange_factor(X).
enzime(X):-kinase(X).
mitogen(X):-growth_factor(X). % mitogen: A mitogen is usually some form of a protein but generally, a chemical substance that
                              % encourages a cell to commence cell division, triggering mitosis. 
receptor(X):-cell_receptor(X).
receptor(X):-nuclear_receptor(X).

% The Facts.
% -------------------------------
presence('EGF').
ligand('EGF').
ligand('MG').
synonymous('MG',['MAGNESIUM ION']).
synonymous('EGF',[]).
nucleotide_exchange_factor('SOS').
synonymous('SOS',['GEF']).
synonymous('MEK',['LAMTOR3','late endosomal/lysosomal adaptor', 'MAPK and MTOR activator 3', 'MAP2K1IP1', 'MAPK scaffold protein 1', 'MAPKSP1', 'mitogen-activated protein kinase kinase 1 interacting protein 1','MAPBP', 'MEK partner 1', 'MP1', 'Ragulator3','MAP2K','MAPKK']).
synonymous('ATP7A',['MNK','ATPase', 'Cu++ transporting', 'alpha polypeptide', 'Menkes syndrome', 'copper pump 1', 'copper-transporting ATPase 1']).
synonymous('MAPK',['ERK']).
adaptor_protein('GRB2').
cell_receptor('EGFR').
growth_factor('EGF').
small_molecule('GTP').
small_molecule('DTP').
ligans('CREBBP', ['MG', ]
transcription_factor('CREBBP').
synonymous('CREBBP',['CREB','cAMP response element-binding protein','CREB-binding protein','RSTS', 'Rubinstein-Taybi syndrome','CBP', 'KAT3A', 'RTS']).
response_element('CRE').
dna_response_element('CREB', '5'-TGACGTCA-3'').
synonymous('CRE',['cAMP response element']).
hormone(SRIF).
synonymous('somatostatin',['SST','prepro-somatostatin', 'SMST', 'somatostatin-14', 'somatostatin-28','SRIF','GHIH','somatotropin release-inhibiting factor','growth hormone-inhibiting hormone']).
gen_family('somatostatin',''Endogenous ligands').
locus_type('somatostatin', 'gene with protein product').
locus_type('CREBBP', 'gene with protein product').
ensembl_gen_id('somatostatin', 'ENSG00000157005').
ensembl_gen_id('CREBBP', 'ENSG00000005339').
pdb_id('somatostatin', '2mi1').
kinase('Ras').
kinase('Raf').
kinase('MEK').
kinase('ERK').
kinase('MAPK').
kinase('MNK').
nuclear_receptor('').
 
