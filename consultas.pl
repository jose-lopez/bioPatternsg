:-[mineria/ontologiaMESH].
:-[mineria/objetosMinados].
:-[mineria/well_know_rules].

%************************************************************************
% Dado un ligando, ¿Cuál (les) receptores lo reconocen y se le enlazan?.

buscar_receptores(L):-ligandos(O,Xs),buscar_en_lista(L,Xs),receptor_ontologia(O),write(L),write('->'),write(O),nl,fail.

%buscar en ontologia MESH que el objeto sea un receptor

receptor_ontologia(X):-is_a(X,'Receptors'),!.
receptor_ontologia(X):-is_a(X,Y),receptor_ontologia(Y),!.

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
