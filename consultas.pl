:-style_check(-discontiguous).
%:-[mineria/objetos_patrones].
%:-[baseC].
%:-[mineria/objetosMinados].
%:-[mineria/ontologiaGO].

%************************************************************************
% Dado un ligando, ¿Cuál (les) receptores lo reconocen y se le enlazan?.
buscar_receptores(L,R,E):-ligand(L),base(C),buscar_en_listas(event(L,E,R),C),receptor(R).

%---------------------------------
% Dado un complejo (o un grupo de ellos) ¿cuáles tienen rol inhibitorio o estimulatorio?.

%**************************************************************************
% ¿Dado un receptor (en su forma monomérica o dimérica), a cuáles proteínas adicionales puede éste enlazarse? (e.g. SHP se enlaza al complejo dimérico FXR-RXR).

buscar_prot_adi(R,L,A):-receptor(R),base(C),buscar_en_listas(event(L,E,R),C),ligand(L),buscar_en_listas(event(R,E,A),C),protein(A).
%---------------------------------
%**************************************************************************
%Buscar ligandos a un receptor
buscar_ligando_rec(R,L):-receptor(R),(ligandos(R,LL),buscar_en_listas(L,LL));(base(C),buscar_en_listas(event(L,_,R),C),ligand(L)).

%******************************************
%buscar tejidos a un receptor y coponentes asociados

%buscar_tejido(R,X,T):-componente_celular(R,T),buscar_en_listas(O,X),componente_celular(O,T).

%buscar_tejidos(T):-componente_celular('SST',T),componente_celular('EGF',T).

componente_celular(O,T):- (cc(O,LCC),buscar_en_listas(T,LCC));
                          (cc(O,LCC),buscar_en_listas(TT,LCC),camino_is_a(TT,T)).

camino_is_a(X,Y):-is_a(X,Y).
camino_is_a(X,Y):-is_a(X,Z),camino_is_a(Z,Y).

%************************
buscar_evento(A,E,B):-base(C),buscar_en_listas(event(A,E,B),C).

%************************************************
%buscar informacion de objeto

buscar_objeto(A,B,S):-sinonimos(B,L),buscar_en_listas(A,L),buscar_en_listas(S,L).




%******************************************
%Buscar objeto en una lista dada 

buscar_en_listas(L,[L|_]).
buscar_en_listas(L,[_|Ys]):-buscar_en_listas(L,Ys).

%*************************************************************************


