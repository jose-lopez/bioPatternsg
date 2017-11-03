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

protein(X):-transcription_factor(X),!.
protein(X):- sinonimos(O,Ls),buscar_en_lista(X,Ls),wkr_proteins(O),!.
transcription_factor(X):- sinonimos(O,Ls),buscar_en_lista(X,Ls),wkr_transcription_factors(O),!.
adaptor_proteins(X):-sinonimos(O,Ls),buscar_en_lista(X,Ls),wkr_adaptor_proteins(O),!.
receptor(X):-sinonimos(O,Ls),buscar_en_lista(X,Ls),wkr_receptors(O),!.
enzyme(X):-sinonimos(O,Ls),buscar_en_lista(X,Ls),wkr_enzymes(O),!.
ligand(X):-sinonimos(O,Ls),buscar_en_lista(X,Ls),(wkr_ligand(O);ligando(O)),!.


%******************************************
%Buscar objeto en una lista dada 

buscar_en_lista(L,[L|_]).
buscar_en_lista(L,[_|Ys]):-buscar_en_lista(L,Ys).

%*************************************************************************

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

protein(X):-transcription_factor(X),!.
protein(X):- sinonimos(O,Ls),buscar_en_lista(X,Ls),wkr_proteins(O),!.
transcription_factor(X):- sinonimos(O,Ls),buscar_en_lista(X,Ls),wkr_transcription_factors(O),!.
adaptor_proteins(X):-sinonimos(O,Ls),buscar_en_lista(X,Ls),wkr_adaptor_proteins(O),!.
receptor(X):-sinonimos(O,Ls),buscar_en_lista(X,Ls),wkr_receptors(O),!.
enzyme(X):-sinonimos(O,Ls),buscar_en_lista(X,Ls),wkr_enzymes(O),!.
ligand(X):-sinonimos(O,Ls),buscar_en_lista(X,Ls),(wkr_ligand(O);ligando(O)),!.

enzyme(X):-wkr_enzymes(O),!.
%******************************************
%Buscar objeto en una lista dada 

buscar_en_lista(L,[L|_]).
buscar_en_lista(L,[_|Ys]):-buscar_en_lista(L,Ys).

%*************************************************************************