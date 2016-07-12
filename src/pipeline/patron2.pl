%    patron.pl
%    Prolog script to infer biological regulation pathways from a knowledge base of regulation events. 
%
%    Copyright (C) 2016 
%    Jacinto Dávila.
%
%    This program is free software: you can redistribute it and/or modify
%    it under the terms of the GNU Affero General Public License as
%    published by the Free Software Foundation, either version 3 of the
%    License, or (at your option) any later version.
%
%    This program is distributed in the hope that it will be useful,
%    but WITHOUT ANY WARRANTY; without even the implied warranty of
%    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
%    GNU Affero General Public License for more details.
%
%    You should have received a copy of the GNU Affero General Public License
%    along with this program.  If not, see <http://www.gnu.org/licenses/>
%


% The dimerization is the result of the interaction of two proteins. It is normal to find verbs like
% to interact and to associate instead of to dimerize. If three proteins interact then we have a 
% trimerization process and we talk about a trimerized complex. If more than three proteins interact
% we talk about a hetedimerized complex.
% synonymous(trimerize, [trimerizes, trimerized, trimerizing]).
% synonymous(heterodimerize, [heterodimerizes, heterodimerized, heterodimerizing]).
% synonymous(interact, [interacts, interacted, interacting]).
% synonymous(associate, [associates, associated, associating]).
% synonymous(bind,[binds, bound, binding]).
% synonymous(activate,[activates, activated, activating]).
% synonymous(phosphorylate, [phosphorylates, phosphorylated, phosphorylating]).
% synonymous(recruit, [recruits, recruited, recruiting]).
% synonymous(dimerize, [dimerizes, dimerized, dimerizing]).
% synonymous(regulate, [bind, 'triggers transcriptional activation of', 'up regulates', 
%                        'triggers transcription of', 'regulates', 'promotes regulation of', 
%                        'enhances regulation of', regulated, regulating]).
% synonymous('down regulate', ['inhibits transcriptional activation of', 'down regulates', 
% synonymous('enhance', ['enhances', 'enhanced', 'enhancing']).

patron(P) :-
	comienzo(Inicio), Inicio = [event(_,_,N)], 
	eventos_intermedios(N, Intermedios, NN),
	final(NN, Final),
	une(Inicio, Intermedios, H),
	une(H, Final, P).

% The KB regulatory events that we need from the Summarizer.
% 
base([event('EGF',bind,'EGFR'), 
event('EGF',activate,'EGFR'), 
event('EGF',phosphorylate,'EGFR'), 
event('EGFR',recruit,'GRB2'),
event('EGFR',dimerize,'GRB2'),  
event('EGFR',activate,'GRB2'),
event('SOS',interact,'GRB2'),
event('GRB2',activate,'SOS'),
event('GRB2',associate,'SOS'),
event('SOS',associate,'Ras'),
event('SOS',activate,'Ras'),
event('Ras',associate,'Raf'),
event('Ras',dimerize,'Raf'),
event('Ras',activate,'Raf'),
event('Raf',interact,'MEK'),
event('Raf',phosphorylate,'MEK'),
event('Raf',activate,'MEK'),
event('MEK',activate,'MAPK'),
event('MEK',phosphorylate,'MAPK'),
event('MEK',activate,'MAPK'),
event('MAPK',interact,'MNK'),
event('MAPK',phosphorylate,'MNK'),
event('MAPK',activate,'MNK'), 
event('MNK',activate,'CREB'),
event('MNK',phosphorylate,'CREB'),
event('MNK',activate,'CREB'),
event('MNK',translocate,'CREB'), 
event('CREB',bind,'CRE'),
event('CREB',regulate,'SRIF')]).

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

% The facts for the example above.
% -------------------------------
presence('EGF').
ligand('EGF').
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
transcription_factor('CREB').
synonymous('CREB',['cAMP response element-binding protein']).
response_element('CRE').
dna_response_element('CREB', '5'-TGACGTCA-3'').
synonymous('CRE',['cAMP response element']).
hormone(SRIF).
synonymous('somatostatin',['SST','prepro-somatostatin', 'SMST', 'somatostatin-14', 'somatostatin-28','SRIF','GHIH','somatotropin release-inhibiting factor','growth hormone-inhibiting hormone']).
gen_family('somatostatin',''Endogenous ligands').
locus_type('somatostatin', 'gene with protein product').
ensembl_gen_id('somatostatin', 'ENSG00000157005').
pdb_id('somatostatin', '2mi1').
kinase('Ras').
kinase('Raf').
kinase('MEK').
kinase('ERK').
kinase('MAPK').
kinase('MNK').
nuclear_receptor('').
 