:-[mineria/ontologiaMESH].
:-[mineria/objetosMinados].

%************************************************************************
% Dado un ligando, ¿Cuál (les) receptores lo reconocen y se le enlazan?.

buscar_receptores(L):-ligandos(O,Xs),buscar_objeto(L,Xs),receptor_ontologia(O),write(L),write('->'),write(O),nl,fail.

%buscar los objetos donde el ligando lo reconoce

buscar_objeto(L,[L|_]).
buscar_objeto(L,[_|Ys]):-buscar_objeto(L,Ys).

%buscar en ontologia MESH que el objeto sea un receptor

receptor_ontologia(X):-is_a(X,'Receptors'),!.
receptor_ontologia(X):-is_a(X,Y),receptor_ontologia(Y),!.

%**************************************************************************