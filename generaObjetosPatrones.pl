:-style_check(-discontiguous).
%:-[mineria/ontologiaMESH].
%:-[mineria/objetosMinados].
%:-[mineria/well_know_rules].
%:-[baseC].

p_protein(X):-p_transcription_factor(X).
p_protein(X):-wkr_proteins(X).
p_transcription_factor(X):-wkr_transcription_factors(X);transcription_factors(X).
p_adaptor_proteins(X):-wkr_adaptor_proteins(X).
p_receptor(X):-wkr_receptors(X).
p_enzyme(X):-wkr_enzymes(X).
p_ligand(X):-(wkr_ligand(X);ligando(X)).

listar_eventos(Obj1,Obj2):-base(C),buscar_en_lista(event(Obj1,E,Obj2),C).

buscar_en_lista(L,[L|_]).
buscar_en_lista(L,[_|Ys]):-buscar_en_lista(L,Ys).


