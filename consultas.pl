:-style_check(-discontiguous).
:-[mineria/ontologiaMESH].
:-[mineria/objetosMinados].
:-[mineria/well_know_rules].
:-[baseC].

%************************************************************************
% Dado un ligando, ¿Cuál (les) receptores lo reconocen y se le enlazan?.
buscar_receptores(L):-ligand(L),base(C),buscar_en_lista(event(L,E,R),C),receptor(R),write(L),write(" "),write(E),write(" "),write(R),nl,fail.

%---------------------------------
% Dado un complejo (o un grupo de ellos) ¿cuáles tienen rol inhibitorio o estimulatorio?.

%**************************************************************************

protein(X):-transcription_factor(X).
protein(X):-wkr_proteins(X).
transcription_factor(X):-wkr_transcription_factors(X);transcription_factors(X).
adaptor_proteins(X):-wkr_adaptor_proteins(X).
receptor(X):-wkr_receptors(X).
enzyme(X):-wkr_enzymes(X).
ligand(X):-(wkr_ligand(X);ligando(X)).

%******************************************
%Buscar objeto en una lista dada 

buscar_en_lista(L,[L|_]).
buscar_en_lista(L,[_|Ys]):-buscar_en_lista(L,Ys).

%*************************************************************************

listar_eventos(Obj1,Obj2):-base(C),buscar_en_lista(event(Obj1,E,Obj2),C).

%ayuda con patrones

inicio(A,E,B):-base(C),buscar_en_lista(event(A,E,B),C),buscar_en_lista(E,['bind','activate']),ligand(A),receptor(B).

final(A,E,B):-base(C),buscar_en_lista(event(A,E,B),C),transcription_factor(A).

intermedios(A,E,B):-base(C),buscar_en_lista(event(A,E,B),C),protein(B).

%buscar_en_lista(E,['bind','activate','regulate','phosphorylate'])