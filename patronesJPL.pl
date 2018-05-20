:-style_check(-discontiguous).
:-[mineria/objetos_patrones].
:-[baseC].
%:-[baseGPR].

inicio(A,E,B):-base(C),buscar_en_lista(event(A,E,B),C),buscar_en_lista(E,[activate,bind]),ligand(A),receptor(B).

final(A,E,B):-base(C),buscar_en_lista(event(A,E,B),C),transcription_factor(A),buscar_en_lista(E,[bind,activate,inhibit,regulate]).

intermedios(A,E,B):-base(C),buscar_en_lista(event(A,E,B),C),protein(B),buscar_en_lista(E,[require,interact,associate,phosphorylate,recruit,recognize,participate,activate]).

buscar_en_lista(L,[L|_]).
buscar_en_lista(L,[_|Ys]):-buscar_en_lista(L,Ys).

objeto(A,E,B):-base(C),buscar_en_lista(event(A,E,B),C),transcription_factor(A).

%[trimerize,require,heterodimerize,interact,associate,phosphorylate,recruit,dimerize,recognize,participate,activate,inhibit]