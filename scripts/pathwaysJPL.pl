:-style_check(-discontiguous).

inicio(A,E,B):-base(C),buscar_en_lista(event(A,E,B),C),buscar_en_lista(E,[activate,bind]),(ligand(A);receptor(A)),receptor(B),not(A = B).

final(A,E,B):-base(C),buscar_en_lista(event(A,E,B),C),transcription_factor(A),buscar_en_lista(E,[activate,inactivate,repress,inhibit,regulate,stimulate,bind]).

intermedios(A,E,B):-base(C),buscar_en_lista(event(A,E,B),C),protein(B),buscar_en_lista(E,[require,interact,associate,phosphorylate,recruit,recognize,participate]).

eventoEspecial(A,E,B):-base(C),buscar_en_lista(event(A,E,B),C),transcription_factor(A),buscar_en_lista(E,[bind,activate,inactivate,repress,inhibit,regulate,stimulate]).

finalEspecial(A,E,B):-base(C),buscar_en_lista(event(A,E,B),C),buscar_en_lista(E,[bind,activate,inactivate,repress,inhibit,regulate,stimulate]).
%[bind,activate,inactivate,repress,inhibit,regulate,stimulate]

%Patrones con restricciones de objetos

inicio_rest(A,E,B,L):-buscar_en_lista(A,L),buscar_en_lista(B,L),base(C),buscar_en_lista(event(A,E,B),C),buscar_en_lista(E,[activate,bind]),(ligand(A);receptor(A)),receptor(B),not(A = B).

final_rest(A,E,B,L):-buscar_en_lista(A,L),buscar_en_lista(B,L),base(C),buscar_en_lista(event(A,E,B),C),transcription_factor(A),buscar_en_lista(E,[activate,inactivate,repress,inhibit,regulate,stimulate,bind]).

intermedios_rest(A,E,B,L):-buscar_en_lista(B,L),base(C),buscar_en_lista(event(A,E,B),C),protein(B),buscar_en_lista(E,[require,interact,associate,phosphorylate,recruit,recognize,participate]).

eventoEspecial_rest(A,E,B,L):-transcription_factor(A),buscar_en_lista(A,L),buscar_en_lista(B,L),base(C),buscar_en_lista(event(A,E,B),C),buscar_en_lista(E,[bind,activate,inactivate,repress,inhibit,regulate,stimulate]).


buscar_en_lista(L,[L|_]).
buscar_en_lista(L,[_|Ys]):-buscar_en_lista(L,Ys).

objeto(A,E,B):-base(C),buscar_en_lista(event(A,E,B),C),transcription_factor(A).

%[trimerize,require,heterodimerize,interact,associate,phosphorylate,recruit,dimerize,recognize,participate,activate,inhibit]