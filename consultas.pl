:-style_check(-discontiguous).
:-[mineria/objetos_patrones].
:-[baseC].
:-[mineria/objetosMinados].
:-[mineria/ontologiaGO].

%************************************************************************
% Dado un ligando, ¿Cuál (les) receptores lo reconocen y se le enlazan?.
buscar_receptores(L):-ligand(L),base(C),buscar_en_lista(event(L,E,R),C),receptor(R),write(L),write(" "),write(E),write(" "),write(R),nl,fail.

%---------------------------------
% Dado un complejo (o un grupo de ellos) ¿cuáles tienen rol inhibitorio o estimulatorio?.

%**************************************************************************
% ¿Dado un receptor (en su forma monomérica o dimérica), a cuáles proteínas adicionales puede éste enlazarse? (e.g. SHP se enlaza al complejo dimérico FXR-RXR).

buscar_prot_adi(R,L,A):-receptor(R),base(C),buscar_en_lista(event(L,E,R),C),ligand(L),buscar_en_lista(event(R,E,A),C),protein(A).
%---------------------------------
%**************************************************************************
%Buscar ligandos a un receptor
buscar_ligando_rec(R,L):-receptor(R),ligandos(R,LL),buscar_en_lista(L,LL).

%******************************************
%buscar tejidos a un receptor y coponentes asociados

%buscar_tejido(R,X,T):-componente_celular(R,T),buscar_en_lista(O,X),componente_celular(O,T).

%buscar_tejidos(T):-componente_celular('SST',T),componente_celular('EGF',T).

componente_celular(O,T):- (cc(O,LCC),buscar_en_lista(T,LCC));
                          (cc(O,LCC),buscar_en_lista(TT,LCC),camino_is_a(TT,T)).

camino_is_a(X,Y):-is_a(X,Y).
camino_is_a(X,Y):-is_a(X,Z),camino_is_a(Z,Y).

%******************************************
%Buscar objeto en una lista dada 

buscar_en_lista(L,[L|_]).
buscar_en_lista(L,[_|Ys]):-buscar_en_lista(L,Ys).

%*************************************************************************


