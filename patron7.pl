% patron6.pl
%
% ?  forall(member(I, [10, 20]), (patron(P, [bind, activate, regulate], ['SRIF', 'CREB'], I), print_pattern(P))).
%
% ?  findnsols(50, P, (patron(P, [bind, activate, regulate, stimulate, decrease, enhance, induce, repress, prevent, lead, trigger, target, express, transcribe, suppress, retain, modulate, promote, synthesize], ['NR0B2', 'Nuclear receptor subfamily 0 group B member 2','Cholesterol 7-alpha-monooxygenase','CYP7A1'], 20), print_pattern(P)), ListP). 
%
% ?  findnsols(50, P, (patron(estricto, P, <aqui va la lista de los objetos deseados>, [bind, activate, regulate], ['SRIF', 'CREB'], 10), ListP), print_some(5, ListP). 
%
% ?  findnsols(50, P, (patron(noestricto, P, <aqui va la lista de los objetos deseados>, [bind, activate, regulate], ['SRIF', 'CREB'], 10), ListP), print_some(5, ListP).
%

:- discontiguous synonymous/2.
:- discontiguous adaptor_protein/1.
:- discontiguous protein/1.



% The KB regulatory events that we need from the Summarizer.
% 

%member(E, [E|_]).
%member(E, [_|R]) :- member(E,R). 

patron(P, R, O, S, Lf) :-
        R \= [], O \= [], 
	comienzo(Inicio), Inicio = [event(N1,_,N)], 
	final(NN, R, O, Final), Final = [event(SF,RF,OF)],
        % base(C), length(C, L), 
	eventos_intermedios(O, S, N, Intermedios, NN,LOi),
	une(Inicio, Intermedios, H),
	une(H, Final, P),
	une(LOi,[OF],T),
	une([N1],T,Lf).
	

% El código debe poder recibir un listado de objetos que pueden participar en los pathways y devolver aquellos pathways que los contengan. Es decir, el listado pasado por el usuario es un subconjunto de objetos que indica aquellos objetos, que de alguna manera se espera participen en los pathways inferidos. Ese listado de entrada no limita el lugar del pathway en que pueden aparecer; puede tratarse de eventos iniciales, intermedios o finales. Debe ser opcional si el listado se trata como listado estricto (todos presentes) en cada pathway o no estricto (uno o mas).

patron(estricto, P, Lista_de_Objetos_dados, R, O, S) :- 
   patron(P, R, O, S), 
   contiene_todos(Lista_de_Objetos_dados, P). 

patron(noestricto,  P, Lista_de_Objetos_dados, R, O, S) :- 
   patron(P, R, O, S), 
   contiene_alguno(Lista_de_Objetos_dados, P). 

contiene_todos(L, P) :- not(falta_alguno(L,P)). 

falta_alguno(L, P) :- 
  member(X, L), 
  not(member(event(X,_,_), P)), 
  not(member(event(_,_,X), P)). 

contiene_alguno(L, P) :- 
  member(X, L), 
  (member(event(X,_,_), P) ; member(event(_,_,X), P)). 

une([], L, L).
une([P|R], L, [P|NR]) :- not(member(P, L)), une(R, L, NR).
une([P|R], L, NR) :- member(P, L), une(R, L, NR). 

comienzo([event(N, Rel, NN)]) :-
	% 3. El primer evento de un pathway debe contener un ligando.
	ligand(N),
        base(B), % 1. Solo pueden haber eventos de la BC en los pathways.
        member(event(N, Rel, NN), B),
	member(Rel,[bind]).

%2. El programa debe conectar eventos tipo event(S,V,O) y siempre debe cumplirse que event(Si,_,Oj) sea seguido por event(Oj,_,_); es decir, que el sujeto del evento siguiente sea el objeto del evento predecesor.

eventos_intermedios(O, _, N1, [event(N1, Rel1, N)], N, LOf) :- 
	base(B), 
	member(event(N1, Rel1, N), B), % 1. Solo pueden haber eventos de la BC en los pathways. 
	member(Rel1, [trimerize,require,heterodimerize,interact,associate,phosphorylate,recruit,dimerize,recognize,participate,activate]),
	% Se asegura que el objeto de cierre no esté presente en eventos intermedios pues es el objeto regulado.
	% Además se asegura que no haya ligandos en el camino pues eso indica más de un pathway en el recorrido.
	not(member(N,O)), not(ligand(N)),
	une([N1],[N],LOf). 
%	member(event(N, Rel2, N2), B), % 1. Solo pueden haber eventos de la BC en los pathways. 
%	member(Rel2,[interact,associate,recruit,activate]).


eventos_intermedios(O, L, N1, [event(N1, Rel1, N)|R], NL, LOf) :- L > 0, 
	base(B), 
	member(event(N1, Rel1, N), B), % 1. Solo pueden haber eventos de la BC en los pathways. 
	member(Rel1, [trimerize,require,heterodimerize,interact,associate,phosphorylate,recruit,dimerize,recognize,participate,activate]),
%	member(event(N, Rel2, N2), B), % 1. Solo pueden haber eventos de la BC en los pathways. 
%	member(Rel2,[interact,associate,recruit,activate]),
	% Aqui la restricción que se asegura de que no hayan objetos finales o ligandos en el camino.
	not(member(N,O)), not(ligand(N)),	
        LL is L - 1,
        eventos_intermedios(O, LL, N, R, NL,NLOf),
	% Restricción que se asegura de que no hayan eventos repetidos en el pathway en proceso.
	not(member(event(N, R1, N1), R)), not(member(event(N1, R2, N), R)), % Se eliminan los rebotes.
	not(member(N1, NLOf)),	
	une([N1],NLOf,LOf).
	

final(N, [event(N, Rel, NN)]) :- %base(B),	
	%member(event(N, Rel, NN), B), % 1. Solo pueden haber eventos de la BC en los pathways. 
	member(Rel, ['bind','activate','regulate','inhibit']),
	transcription_factor(N), 
        % 3. El primer evento de un pathway debe contener un ligando como sujeto y el objeto del ultimo evento en un pathway deber una proteina.
	protein(NN).

final(N, R, O, [event(N, Rel, NN)]) :-
        base(B), 
        R\=[], O\=[], 
        % 4. Si el evento de cierre es event(Sn,Vz,Or) entonces Vz debe ser del tipo que se indique en la lista de relaciones de cierre (indicados en la entrada al programa).
        member(Rel, R), 
        % 5. La lista de posibles objetos de cierre Or (indicada en la entrada al programa), permite que el experto señale los objetos de cierre en un pathway que el esperaría ver.
        member(NN, O),       
        % 1. Solo pueden haber eventos de la BC en los pathways. 
        member(event(N, Rel, NN), B),
	% Aqui la restricción sobre el sujeto del evento final, que debe ser un FT.  
	final(N, [event(N, Rel, NN)]).

final(N, R, O, [event(N, Rel, NN)]) :-
	% 6. Si alguna de las listas (relaciones u objetos) esta' vacia, entonces cualquier relacion u objeto es permisible en el cierre de un pathway. Si ambas listas estan vacias, eso debe permitir ver todos los pathways contenidos en la BC.
        ( R=[] ; O=[]), 
	final(N, [event(N, Rel, NN)]). 

print_pattern([event(PN, FRelation, N)]) :- 
	write(PN), write('\t--- '), write(FRelation), write(' -->'), write(N), write('@'), nl. 
print_pattern([event(PN, FRelation, _N)|R]) :- nl, 
	write(PN), write('\t--- '), write(FRelation), write(' -- '), print_pattern(R). 

print_some(0, _Rest). 
print_some(_N, []).
print_some(N, [P|Rest] ) :- print_pattern(P), NN is N - 1, print_some(NN, Rest). 

% Restrictions.

% ---------------------------------------
% Well known rules in cellular signalling.
% ---------------------------------------
activates(X,Y) :- phosphorylates(X,Y).
phosphorylated(X) :- phosphorylates(_Y,X).
associates(X,Y) :- activates(X,Y).
interacts(X,Y) :- activates(X,Y).
transcription_factor(X):-nuclear_receptor(X).
transcription_factor(X):-generic_transcription_factor(X).
protein(X):-nuclear_receptor(X).
protein(X):-transcription_factor(X).
protein(X):-enzyme(X).
protein(X):-transporter(X).
protein(X):-generic_protein(X).
dna_sequence(X):-response_element(X). % synonymous ('response element', 'motif').
dna_sequence(X):-motif(X).
adaptor_protein(X) :-nucleotide_exchange_factor(X).
%growth_factor(X):-mitogen(X).
protein(X):-adaptor_protein(X).
enzyme(X):-gtp_ase(X).
enzyme(X):-kinase(X).
protein(X):-hormone(X).
protein(X):-cell_receptor(X).

:- [baseC].
%:- [baseCp].
%:- [baseGPR].
%:- [baseL].
:- [objetosCREB].
%:- [objetos5].
