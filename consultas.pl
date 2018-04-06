:-style_check(-discontiguous).
:-[mineria/objetos_patrones].
:-[baseC].
:-[mineria/objetosMinados].

%************************************************************************
% Dado un ligando, ¿Cuál (les) receptores lo reconocen y se le enlazan?.
buscar_receptores(L):-ligand(L),base(C),buscar_en_lista(event(L,E,R),C),receptor(R),write(L),write(" "),write(E),write(" "),write(R),nl,fail.

%---------------------------------
% Dado un complejo (o un grupo de ellos) ¿cuáles tienen rol inhibitorio o estimulatorio?.

%**************************************************************************
% ¿Dado un receptor (en su forma monomérica o dimérica), a cuáles proteínas adicionales puede éste enlazarse? (e.g. SHP se enlaza al complejo dimérico FXR-RXR).

buscar_prot_adi(R,L,A):-receptor(R),base(C),buscar_en_lista(event(L,E,R),C),ligand(L),buscar_en_lista(event(R,E,A),C),protein(A).
%---------------------------------

%Buscar ligandos a un receptor
buscar_ligando_rec(R,L):-receptor(R),ligandos(R,LL),buscar_en_lista(L,LL).



%******************************************
%Buscar objeto en una lista dada 

buscar_en_lista(L,[L|_]).
buscar_en_lista(L,[_|Ys]):-buscar_en_lista(L,Ys).

%*************************************************************************


