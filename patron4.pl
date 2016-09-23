% patron4.pl
%
% ?  forall(member(I, [10, 20]), (patron(P, [bind, activate, regulate], ['SRIF', 'CREB'], I), print_pattern(P))).
%

:- discontiguous synonymous/2.
:- discontiguous adaptor_protein/1.

% The KB of regulatory events that we need from the Summarizer.
% 

member(E, [E|_]).
member(E, [_|R]) :- member(E,R). 

patron(P) :-
	comienzo(Inicio), Inicio = [event(_,_,N)], 
	eventos_intermedios(N, Intermedios, NN),
	final(NN, Final),
	une(Inicio, Intermedios, H),
	une(H, Final, P).

patron(P, R, O, S) :-
        R \= [], O \= [], 
	comienzo(Inicio), Inicio = [event(_,_,N)], 
	final(NN, R, O, Final), 
        % base(C), length(C, L), 
	eventos_intermedios(S, N, Intermedios, NN),
	une(Inicio, Intermedios, H),
	une(H, Final, P).


une([], L, L).
une([P|R], L, [P|NR]) :- not(member(P, L)), une(R, L, NR).
une([P|R], L, NR) :- member(P, L), une(R, L, NR). 

comienzo([event(N, Rel, NN)]) :-
	(ligand(N);receptor(N)), % 3. El primer evento de un pathway debe contener un ligando como sujeto y el objeto del ultimo evento en un pathway deber una proteina.
        base(B), % 1. Solo pueden haber eventos de la BC en los pathways.
        member(event(N, Rel, NN), B),
	member(Rel, [bind, recognize, interact, associate]).

%2. El programa debe conectar eventos tipo event(S,V,O) y siempre debe cumplirse que event(Si,_,Oj) sea seguido por event(Oj,_,_); es decir, que el sujeto del evento siguiente sea el objeto del evento predecesor. 
eventos_intermedios(_, N1, [event(N1, Rel1, N)], N) :- 
	base(B), 
	member(event(N1, Rel1, N), B), % 1. Solo pueden haber eventos de la BC en los pathways. 
	member(Rel1, [interact, associate, activate, phosphorylate, recruit, dimerize, trimerize, heterodimerize, translocate]).
%	member(event(N, Rel2, N2), B), % 1. Solo pueden haber eventos de la BC en los pathways. 
%	member(Rel2, [interact, associate, activate, phosphorylate, recruit, dimerize, trimerize, heterodimerize, translocate]).


eventos_intermedios(L, N1, [event(N1, Rel1, N)|R], NL) :- L > 0, 
	base(B), 
	member(event(N1, Rel1, N), B), % 1. Solo pueden haber eventos de la BC en los pathways. 
	member(Rel1, [interact, associate, activate, phosphorylate, recruit, dimerize, trimerize, heterodimerize, translocate]),
%	member(event(N, Rel2, N2), B), % 1. Solo pueden haber eventos de la BC en los pathways. 
%	member(Rel2, [interact, associate, activate, phosphorylate, recruit, dimerize, trimerize, heterodimerize, translocate]), 
        LL is L - 1, 
        eventos_intermedios(LL, N, R, NL). 
	% not(member(event(N, _, N1), R)). % sin ciclos?

final(N, [event(N, Rel, NN)]) :- base(B), 
	protein(N), 
	member(event(N, Rel, NN), B), % 1. Solo pueden haber eventos de la BC en los pathways. 
	member(Rel, [bind, regulate, stimulate, induce, enhance, inhibit, transcriptional-activate]),
        % 3. El primer evento de un pathway debe contener un ligando como sujeto y el objeto del ultimo evento en un pathway deber una proteina.
	protein(NN).

final(N, R, O, [event(N, Rel, NN)]) :-
        base(B), 
        R\=[], O\=[], 
        % 4. Si el evento de cierre es event(Sn,Vz,Or) entonces Vz debe ser del tipo que se indique en la lista de relaciones de cierre (indicados en la entrada al programa).
        member(Rel, R), 
        % 5. La lista de posibles objetos de cierre Or (indicada en la entrada al programa), permite que el experto señale los objetos de cierre en un pathway que el esperaría ver.
        member(NN, O),   
        % 3. El primer evento de un pathway debe contener un ligando como sujeto y el objeto del ultimo evento en un pathway deber una proteina.
        protein(NN), 
        % 1. Solo pueden haber eventos de la BC en los pathways. 
        member(event(N, Rel, NN), B).  

final(N, R, O, [event(N, Rel, NN)]) :-
	% 6. Si alguna de las listas (relaciones u objetos) esta' vacia, entonces cualquier relacion u objeto es permisible en el cierre de un pathway. Si ambas listas estan vacias, eso debe permitir ver todos los pathways contenidos en la BC.
        ( R=[] ; O=[]), 
	final(N, [event(N, Rel, NN)]). 

print_pattern([event(PN, FRelation, N)]) :- 
	write(PN), write('\t--- '), write(FRelation), write(' -->'), write(N), write('@'), nl. 
print_pattern([event(PN, FRelation, _N)|R]) :- nl, 
	write(PN), write('\t--- '), write(FRelation), write(' -- '), print_pattern(R). 


% Restrictions.

% Well known rules in cellular signalling.
% ---------------------------------------
activates(X,Y) :- phosphorylates(X,Y).
phosphorylated(X) :- phosphorylates(_Y,X).
associates(X,Y) :- activates(X,Y).
interacts(X,Y) :- activates(X,Y).
protein(X):-receptor(X).
dna_sequence(X):-response_element(X). % synonymous ('response element', 'motif').
dna_sequence(X):-motif(X).
adaptor_protein(X) :-nucleotide_exchange_factor(X).
enzime(X):-kinase(X).
mitogen(X):-growth_factor(X). % mitogen: A mitogen is usually some form of a protein but generally, a chemical substance that
                              % encourages a cell to commence cell division, triggering mitosis. 

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
 
