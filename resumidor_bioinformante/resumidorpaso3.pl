%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Paso 3 : Tópicos %%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% obtener tópicos de cada oración %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Primitivas para obtener de la Oración Williams el sujeto, verbo y 
%% complemento.

%% sujeto(+OracionW, -Sujeto) 
%% obtener el sujeto de la oracion Williams

sujeto([sujeto(S)|_],S).

%% verbo(+OracionW, -Verbo)
%% obtener el verbo de la oracion Williams

% cuando el verbo es simple
verbo([sujeto(_),verbo(V)|_],V).
% cuando el verbo es una lista de verbos, devuelve el primer verbo 
% del verbo compuesto
verbo_compuesto([sujeto(_),[verbo(V)|_]|_],V).
% cuando el verbo es binario, se devuelve los dos verbos
verbo_binario([sujeto(_),[verbo(V1),verbo(V2)]|_],V1,V2).
% cuando la oración es impersonal, pronombre "se"
verbo_impersonal([sujeto(_),[sujeto_verbo(V)|_]|_],V).

%% complemento(+OracionW, -Complemento)
%% obtener el complemento de la oracion Williams

complemento([sujeto(_),_,complemento(C)],C).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% extraer_topico(+ParrafoWilliams,-TopicosParrafo)
%% Dado un párrafo Williams (con el criterio de claridad local), retorna
%% sus tópicos (de que se habla en cada oración del párrafo).

extraer_topico([],[]).

%% Caso Tópico 1
%% Se obtiene el verbo de la oración, si es copulativo se obtiene el sujeto
%% de la oración, al sujeto se le aplica el filtro de expresiones y el 
%% resultado es el tópico de la oración.
%% Los verbos copulativos o sustantivo, expresan la idea de esencia o 
%% sustancia. El tópico de una oración con verbo copulativo es el sujeto de 
%% la oración. Para saber si un verbo es copulativo se revisa la lista de 
%% verbos verb_aux. No interesa cuando el verbo es compuesto y el verbo 
%% copulativo es usado como auxiliar.

extraer_topico([OracionWilliams|ParrafoWilliams],[Topico|TopicosParrafo]) :-
    verbo(OracionWilliams,Verbo),
%    buscar_verbos([activacion,abastecimiento,utilizacion],ListaVerbos),
%    agregar_verbos(ListaVerbos),
    %% si el verbo es auxiliar o copulativo
    verb_aux(_,Verbo,[]), 
    sujeto(OracionWilliams,Sujeto), 
    %% Obtener Tópico a partir del Sujeto
    filtrar_expresion(TopicoSujeto,Sujeto,[]),
    filtrar_coma(TopicoSujeto,Topico,_,(,)),
    extraer_topico(ParrafoWilliams,TopicosParrafo).

%% Caso Tópico 3
%% Caso particular de ciertos verbos ("revelar" y "puede contraer")
%% para el ejemplo de las vacas locas.
%% Tópico compuesto en el complemento. 

extraer_topico([OracionWilliams|ParrafoWilliams],[Topico|TopicosParrafo]) :-
    %% si el verbo de la oración es uno de este grupo
    verbo(OracionWilliams,[reveló]), 
    complemento(OracionWilliams,Complemento),
    %% Obtener Tópico a partir del Complemento
    filtrar_expresion(TopicoComplemento,Complemento,[]),
    claridad([TopicoComplemento],[ComplementoWilliams]),
    sujeto(ComplementoWilliams,Sujeto),
    filtrar_expresion(Topico1,Sujeto,[]),
    verbo_binario(ComplementoWilliams,[pueden],[contraer]),
    complemento(ComplementoWilliams,Complemento2),
    filtrar_expresion(Topico2,Complemento2,[]),
    construir_topico([Topico2, [en], Topico1],Topico,[]),
    extraer_topico(ParrafoWilliams,TopicosParrafo).

%% Caso Tópico con anáforas.
%% el sujeto se refiere a un topico mencionado anteriormente. Se agrega el topico del
%% del complemento de la oracion que contiene esta referencia anaforica

extraer_topico([OracionWilliams|ParrafoWilliams],[[[esta_antes],[Numero,Genero]],TopicoComplemento|TopicosParrafo]) :-
    sujeto(OracionWilliams,Sujeto), 
    filtrar_expresion(Topico_posible,Sujeto,[]),
    contiene_anafora(Numero,Genero,Topico_posible,[]),
    complemento(OracionWilliams,Complemento),
    claridad([Complemento],[ComplementoWilliams]),
    extraer_topico_complemento(ComplementoWilliams,TopicoComplemento),
    extraer_topico(ParrafoWilliams,TopicosParrafo).

extraer_topico([OracionWilliams|ParrafoWilliams],[[[esta_antes],[Numero,Genero]],Topico|TopicosParrafo]) :-
    sujeto(OracionWilliams,Sujeto), 
    filtrar_expresion(Topico_posible,Sujeto,[]),
    contiene_anafora(Numero,Genero,Topico_posible,[]),
    complemento(OracionWilliams,Complemento),
    filtrar_coma(Complemento,TopicoComplemento,_,(,)),
    filtrar_expresion(Topico,TopicoComplemento,[]),
    extraer_topico(ParrafoWilliams,TopicosParrafo).


%% Caso Tópico 2
%% verbo con pronombre impersonal "se", la oración es impersonal. 
%% Generalmente estas oraciones tienen su tópico en el complemento.

extraer_topico([OracionWilliams|ParrafoWilliams],[Topico|TopicosParrafo]) :-
    %% si el verbo contiene el pronombre impersonal "se"
    verbo_impersonal(OracionWilliams,[se]),
    complemento(OracionWilliams,Complemento),
    %% Obtener Tópicos a partir del Complemento de la oracion
    claridad([Complemento],[ComplementoWilliams]),
    extraer_topico_complemento(ComplementoWilliams,Topico),
    extraer_topico(ParrafoWilliams,TopicosParrafo).

extraer_topico([OracionWilliams|ParrafoWilliams],[Topico|TopicosParrafo]) :-
    %% si el verbo contiene el pronombre impersonal "se"
    verbo_impersonal(OracionWilliams,[se]),
    complemento(OracionWilliams,Complemento),
    %% Obtener Tópicos a partir del Complemento de la oracion
    filtrar_coma(Complemento,TopicoComplemento,_,(,)),  
    filtrar_expresion(Topico,TopicoComplemento,[]),
    extraer_topico(ParrafoWilliams,TopicosParrafo).

%% Caso Tópico por defecto
%% los demás verbos (predicativos) expresan estado o acción

extraer_topico([OracionWilliams|ParrafoWilliams],[Topico|TopicosParrafo]) :-
    complemento(OracionWilliams,[]),
    sujeto(OracionWilliams,Sujeto), 
    filtrar_expresion(Topico,Sujeto,[]),
    extraer_topico(ParrafoWilliams,TopicosParrafo).

extraer_topico([OracionWilliams|ParrafoWilliams],[TopicoSujeto,TopicoComplementoFiltrado|TopicosParrafo]) :-
    sujeto(OracionWilliams,Sujeto), 
    filtrar_expresion(TopicoS,Sujeto,[]),
    filtrar_coma(TopicoS,TopicoSujeto,_,(,)),
    %% Obtener Tópicos a partir del Complemento de la oracion
    complemento(OracionWilliams,Complemento),
    claridad([Complemento],[]),
    filtrar_coma(Complemento,TopicoComplemento,_,(,)),  
    filtrar_expresion(TopicoComplementoFiltrado,TopicoComplemento,[]),  
    extraer_topico(ParrafoWilliams,TopicosParrafo).

extraer_topico([OracionWilliams|ParrafoWilliams],[TopicoSujeto,TopicoComplemento|TopicosParrafo]) :-
    sujeto(OracionWilliams,Sujeto), 
    filtrar_expresion(TopicoS,Sujeto,[]),
    filtrar_coma(TopicoS,TopicoSujeto,_,(,)),
    %% Obtener Tópicos a partir del Complemento, cuando éste es una oración con verbo
    complemento(OracionWilliams,Complemento),
    claridad([Complemento],[ComplementoWilliams]),
    extraer_topico_complemento(ComplementoWilliams,TopicoComplemento),
    extraer_topico(ParrafoWilliams,TopicosParrafo).

extraer_topico_complemento([],[]).

extraer_topico_complemento(ComplementoWilliams,Topico):-
    verbo(ComplementoWilliams,Verbo),
    verb_aux(_,Verbo,[]), 
    sujeto(ComplementoWilliams,Sujeto),
    filtrar_coma(Sujeto,SujetoSinComa,_,(,)),
    filtrar_expresion(Topico,SujetoSinComa,[]).

extraer_topico_complemento(ComplementoWilliams,Topico):-
    verbo_impersonal(ComplementoWilliams,[se]),
    complemento(ComplementoWilliams,Complemento),
    filtrar_coma(Complemento,TopicoComplemento,_,(,)),
    filtrar_expresion(Topico,TopicoComplemento,[]).

extraer_topico_complemento(ComplementoWilliams,Topico):-
    sujeto(ComplementoWilliams,Sujeto),
    filtrar_expresion([],Sujeto,[]),
    complemento(ComplementoWilliams,Complemento),
    filtrar_coma(Complemento,ComplementoSinComa,_,(,)),
    filtrar_expresion(Topico,ComplementoSinComa,[]).

extraer_topico_complemento(ComplementoWilliams,Topico):-
    sujeto(ComplementoWilliams,Sujeto),
    filtrar_expresion(TopicoPosible,Sujeto,[]),
    es_conjuncion(TopicoPosible),
    complemento(ComplementoWilliams,Complemento),
    filtrar_coma(Complemento,ComplementoSinComa,_,(,)),
    filtrar_expresion(Topico,ComplementoSinComa,[]).

extraer_topico_complemento(ComplementoWilliams,Topico):-
    sujeto(ComplementoWilliams,Sujeto),
    filtrar_coma(Sujeto,SujetoSinComa,_,(,)),
    filtrar_expresion(Topico,SujetoSinComa,[]).
    
filtrar_coma([],[],[],_).

filtrar_coma([Palabra|RestoOracion],[],RestoOracion,Coma):-
    igual(Coma,Palabra).

filtrar_coma([Palabra|RestoOracion],[Palabra|Antes],Despues,Coma):-
    filtrar_coma(RestoOracion,Antes,Despues,Coma).

igual(X,X).

%% construir tópicos compuestos (con DCG)

construir_topico([T1,C,T2]) --> topico(T1), conector(C), topico(T2).

topico([X|Resto]) --> [X|Resto].
conector([X|Resto]) --> [X|Resto].

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% filtrar_expresion(-TextoSinExpresion) *DCG
%% filtrar_expresion(-TextoSinExpresion, +Texto, +Listavacia) *Prolog
%%
%% Filtra una frase y elimina las expresiones de evaluación, conectores 
%% lógicos y de tiempo y espacio de la oración. Estas expresiones no son 
%% parte del tópico. Williams define el uso de estas expresiones como 
%% cohesion entre las oraciones de un párrafo. pág 49 del libro de Style

filtrar_expresion([]) --> []. 

filtrar_expresion(Topico) --> expresion(_), filtrar_expresion(Topico).

filtrar_expresion(Topico) --> filtrar_expresion_resto(Topico), expresion(_).

filtrar_expresion(Resto) --> filtrar_expresion_resto(Resto).

filtrar_expresion_resto([]) --> []. 

filtrar_expresion_resto([X|Resto]) --> [X|Resto].

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% chequea si la frase contiene anaforas
% por ahora sólo identifica pronombres.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

contiene_anafora(Numero,Genero) --> pronombre(Numero,Genero).
contiene_anafora(Numero,Genero) --> palabra(_), pronombre(Numero,Genero), palabra(_).
palabra([X|Resto]) --> [X|Resto].
palabra([]) --> [].

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% fin de resumidorpaso3.pl

