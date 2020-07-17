% resumidorpaso4.pl version 1.0 
% parte de resumidorcompleto.pl
%
% ensamblado por Jacinto Dávila 
% 
%    Copyright (C) 2015 
%    Jacinto Dávila, Hilda Yelitza Contreras, M. Marilú Parra, Jose Lopez
%
%    This program is free software: you can redistribute it and/or modify
%    it under the terms of the GNU Affero General Public License as
%    published by the Free Software Foundation, either version 3 of the
%    License, or (at your option) any later version.
%
%    This program is distributed in the hope that it will be useful,
%    but WITHOUT ANY WARRANTY; without even the implied warranty of
%    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
%    GNU Affero General Public License for more details.
%
%    You should have received a copy of the GNU Affero General Public License
%    along with this program.  If not, see <http://www.gnu.org/licenses/>
%


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Paso 4 : Tópico Común %%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% obtener tópicos del párrafo %
%%%% eliminar las referencias de anáforas por [esta_antes]
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% topico_comun(+ListaTopicosParrafo, -TopicoComun).
%% Dado una lista de tópicos de un párrafo retorna su tópico común
%% El tópico común identifica el arranque y el discurso del párrafo
%% resuelve las referencias anafóricas simples y la repetición de tópicos 
%% del texto, además fusiona la ponderación de relevancia de cada tópico. 
%% Al final se selecciona según la ponderación de la lista los tópicos más 
%% frecuentes y más específicos.

topico_comun(ListaTopicosParrafo, TopicoComun) :-
    identificar_topico_arranque(ListaTopicosParrafo, ListaPonderada),
    resolver_anafora(ListaPonderada,ListaSinAnaforas),
    simplificar_topicos_identicos(ListaSinAnaforas, ListaSinDuplicados),
    seleccionar_topico_comun(ListaSinDuplicados, TopicoComun).
    % write(TopicoComun).

%% identificar_topico_arranque(+ListaTopicosParrafo, -ListaPonderada).
%% Identifica el arranque y el discurso del párrafo, asocia un 0 al arranque
%% (primer tópico) y un 1 al discurso (resto de los tópicos).
%% Cada elemento de la lista ListaPonderada tiene dos elementos el numero
%% ponderado y la lista de palabras de la oracion.

identificar_topico_arranque([], []).
identificar_topico_arranque([Topico|RestoTopicos], [[0,Topico]|RestoLista]):-
    identificar_discurso(RestoTopicos, RestoLista).

identificar_discurso([], []).
identificar_discurso([[]|RestoTopicos], RestoLista):-
    identificar_discurso(RestoTopicos, RestoLista).
identificar_discurso([Topico|RestoTopicos], [[1,Topico]|RestoLista]):-
    identificar_discurso(RestoTopicos, RestoLista).

%% resolver_anafora(+ListaPonderada,-ListaSinAnafora).
%% Realiza las asociaciones anafóricas del párrafo, simplifica los tópicos
%% y reajusta la frecuencia de los tópicos.
%% Busca la ocurrencia de la(s) anáfora(s) del Párrafo, previamente 
%% identificadas como [esta_antes] con los datos de género y número del 
%% pronombre correspondiente. La anáfora divide la lista de tópicos en dos
%% listas: Antes y Después. Para resolver la anáfora debe buscarse en la lista 
%% Antes un determinante del tópico semejante al del [esta_antes], se 
%% recorre la lista en sentido contrario. Se continua buscando en la lista 
%% Después hasta el final del parrafo.

resolver_anafora([Topico|[]],Topico).

resolver_anafora(ListaPonderada,ListaSinAnafora):-
    buscar_anafora(ListaPonderada,Antes,Despues,DatosLinguisticosAnafora),
    invertir_lista(Antes,AntesInvertida),
    relacionar_topico_anterior(AntesInvertida,DatosLinguisticosAnafora,TopicosRelacionado),
    invertir_lista(TopicosRelacionado,TopicosRelacionadoInvertido),
    concatenar_listas([TopicosRelacionadoInvertido,Despues],ListaNueva,[]),
    resolver_anafora(ListaNueva,ListaSinAnafora).

resolver_anafora(ListaPonderada,ListaPonderada).

%% buscar_anafora(ListaPonderada,Antes,Despues,DatosLinguisticosAnafora).

buscar_anafora([],[],[],[]):- fail,!.

buscar_anafora([Topico|RestoTopico],_,RestoTopico,DatosLinguisticosAnafora):-
    topico_es_anafora(Topico),
    obtener_datos_linguisticos(Topico, DatosLinguisticosAnafora).

buscar_anafora([Topico|RestoTopico],[Topico|Antes],Despues,DatosLinguisticosAnafora):-
    buscar_anafora(RestoTopico,Antes,Despues,DatosLinguisticosAnafora).
        
topico_es_anafora([_,[[esta_antes],_]]).

obtener_datos_linguisticos([_,[[esta_antes],[Numero,Genero]]],[Numero,Genero]).

%% invertir_lista(Lista,ListaInvertida)
%%

invertir_lista(Lista,ListaInvertida):-
    reverse(Lista,ListaInvertida).

%%% frecuencia(Topico,Frecuencia).
%% dado un topico retorna su frecuencia o numero de ponderacion

frecuencia([Frecuencia,_],Frecuencia).

%% topico(Topico,Topico).
%% dado un topico retorna la lista que contiene la oracion del topico

topico([_,Topico],Topico).

topico_ponderado([Frecuencia,Topico],Frecuencia,Topico).

%% relacionar_topico_anterior(Antes,DatosLinguisticosAnafora,TopicosRelacionado).
%%
%% Busca en la lista Antes invertida un determinante con los mismos
%% datos lingüísticos de la anáfora, cuando lo encuentre aumenta la ponderación
%% del tópico.

relacionar_topico_anterior([],_,[]).

relacionar_topico_anterior([Topico|Resto],DatosLinguisticosAnafora,[TopicoRelacionado|Resto]):-
    topico(Topico,TopicoLista),
    contiene_articulo_relacionado(TopicoLista,DatosLinguisticosAnafora),
    relacionar_topico(Topico,TopicoRelacionado).

relacionar_topico_anterior([Topico|Antes],DatosLinguisticosAnafora,[Topico|TopicosRelacionado]):-
    relacionar_topico_anterior(Antes,DatosLinguisticosAnafora,TopicosRelacionado).

relacionar_topico([Frecuencia,Topico],[FrecuenciaNueva,Topico]):-
    FrecuenciaNueva is Frecuencia + 1.

%% contiene_articulo_relacionado(Topico,DatosLinguisticosAnafora).
%%

contiene_articulo_relacionado([],_):- fail,!.

contiene_articulo_relacionado([Palabra|_],[NumeroAnafora,GeneroAnafora]):-
    es_articulo(Palabra,NumeroAnafora,GeneroAnafora).

contiene_articulo_relacionado([Palabra|_],_):-
    es_articulo(Palabra,_,_),
    fail,!.

contiene_articulo_relacionado([_|Resto],DatosLinguisticosAnafora):-
    contiene_articulo_relacionado(Resto,DatosLinguisticosAnafora).

%% concatenar_listas([TopicosRelacionado,Despues],ListaNueva,[]).
%%
%% ListaNueva = TopicosRelacionado + Despues

concatenar_listas([TopicosRelacionado,Despues]) -->
    lista(TopicosRelacionado),
    lista(Despues).

lista([X|Resto]) --> [X|Resto].
lista([])--> [].

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% simplificar_topicos_identicos(ListaSinAnaforas, ListaSinDuplicados).
%% Busca los tópicos que contengan palabras identicas en orden secuencial en la 
%% lista anterior. 

simplificar_topicos_identicos([], []).

simplificar_topicos_identicos([Topico|[]], [Topico|[]]).

simplificar_topicos_identicos([Topico|RestoListaSinAnaforas], [TopicoFusionado|ListaSinDuplicados]):-
    fusionar_topicos(Topico,RestoListaSinAnaforas,[TopicoFusionado|ListaFusionada]),
    simplificar_topicos_identicos(ListaFusionada,ListaSinDuplicados).

%% fusionar_topicos(Topico,RestoListaSinAnaforas,ListaFusionada),
%% Aplica al primer tópico de la lista un filtro para obtener solo los
%% sustantivos y adjetivos de la frase nominal que representa al tópico.
%% Se obtiene una lista de palabras (en donde no hay ni determinantes, ni 
%% pronombres, ni conectores) y aplica fusionar palabra a cada uno de los 
%% elementos.

fusionar_topicos(TopicoPonderado,RestoListaSinAnaforas,ListaFusionada):-
    topico(TopicoPonderado,Topico),
    filtrar_sustantivo_adjetivo(Topico,ListaSustantivoAdjetivo),
    fusionar_palabra(ListaSustantivoAdjetivo,TopicoPonderado,RestoListaSinAnaforas,ListaFusionada).

%% filtrar_sustantivo_adjetivo(Topico, ListaSustantivoAdjetivo)

filtrar_sustantivo_adjetivo([], []).

filtrar_sustantivo_adjetivo([Topico|Resto], ListaSustantivoAdjetivo):-
    es_articulo(Topico,_,_),!,
    filtrar_sustantivo_adjetivo(Resto,ListaSustantivoAdjetivo).

filtrar_sustantivo_adjetivo([Topico|Resto], ListaSustantivoAdjetivo):-
    es_pronombre(Topico),!,
    filtrar_sustantivo_adjetivo(Resto,ListaSustantivoAdjetivo).

filtrar_sustantivo_adjetivo([Topico|Resto], ListaSustantivoAdjetivo):-
    es_conjuncion(Topico),!,
    filtrar_sustantivo_adjetivo(Resto,ListaSustantivoAdjetivo).

filtrar_sustantivo_adjetivo([Topico|Resto], [Topico|ListaSustantivoAdjetivo]):-
    filtrar_sustantivo_adjetivo(Resto,ListaSustantivoAdjetivo).
        
%% fusionar_palabra(Palabra,Topico, Lista,ListaFusionada).
%% Recorre la lista de palabras del tópico, previamente filtradas hasta el final

fusionar_palabra([],Topico,Lista,[Topico|Lista]).

fusionar_palabra([Palabra|Resto],Topico,ListaTopicos,ListaFusionada):- 
    buscar_palabra_en_lista(Palabra,Topico,TopicoResultante,ListaTopicos,ListaResultante),
    fusionar_palabra(Resto,TopicoResultante,ListaResultante,ListaFusionada).

fusionar_palabra([_|RestoListaPalabras],Topico,ListaTopicos,ListaFusionada):- 
    fusionar_palabra(RestoListaPalabras,Topico,ListaTopicos,ListaFusionada).

%% buscar_palabra_en_lista(Palabra,Topico,ListaTopicos,ListaResultante).
%% Recorre la lista del resto de los tópicos en donde hay que buscar una palabra
%% que pertenece a un tópico anterior. Si la busqueda tiene éxito entonces se construye 
%% una lista con los tópicos en donde aparezca dicha palabra. Se escoge de dicha lista 
%% mas el tópico original el más representativo.

buscar_palabra_en_lista(_,Topico,Topico,[],[]).

buscar_palabra_en_lista(Palabra,Topico,TopicoResultante,[TopicoPonderado|Resto],Resultado):-
    topico(TopicoPonderado,TopicoResto),
    member(Palabra,TopicoResto),
    escoger_topico_representativo(Topico,TopicoPonderado,TopicoRepresentativo),
    buscar_palabra_en_lista(Palabra,TopicoRepresentativo,TopicoResultante,Resto,Resultado).

buscar_palabra_en_lista(Palabra,Topico,TopicoResultante,[TopicoResto|Resto],[TopicoResto|ListaResultante]):-
    buscar_palabra_en_lista(Palabra,Topico,TopicoResultante,Resto,ListaResultante).

%% El mejor de los topicos que se estan fusionando es el primer topico que aparece en el 
%% texto. Se asume que el escritor del texto sabe ordenar sus ideas y coloco el mejor topico 
%% antes. Se escoge el segundo topico en el orden del texto si este tiene mejor ponderacion.

escoger_topico_representativo(Topico1,Topico2,TopicoResultado):-
    frecuencia(Topico1,Frecuencia1),
    frecuencia(Topico2,Frecuencia2),
    topico(Topico2,TopicoR),
    Frecuencia2 > Frecuencia1,
    FrecuenciaR is Frecuencia1 + Frecuencia2,
    topico_ponderado(TopicoResultado,FrecuenciaR,TopicoR).

escoger_topico_representativo(Topico1,Topico2,TopicoResultado):- 
    frecuencia(Topico1,Frecuencia1),
    frecuencia(Topico2,Frecuencia2),
    topico(Topico1,TopicoR),    
    FrecuenciaR is Frecuencia1 + Frecuencia2,
    topico_ponderado(TopicoResultado,FrecuenciaR,TopicoR).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% seleccionar_topico_comun(+ListaTopicosSinDuplicados, -TopicoComun).
% Selecciona de la lista resultante de tópicos aquellos mejor poderados.
% Toma en cuenta la cardinalidad de la lista de tópicos y un factor de 
% resumen, el cual esta entre [0,1,2]. Donde 0 son todos los Tópicos menos
% los que tiene 0 de ponderación. El 1 indica que descarta los 0 y los 1 
% de cardinalidad baja. El 2 escoje solo el mejor tópico por cada parrafo.

seleccionar_topico_comun(ListaSinDuplicados, TopicoComun):-
    ordenar_descendentemente(ListaSinDuplicados,ListaOrdenada),
    obtener_relevantes(ListaOrdenada,TopicoComun).

ordenar_descendentemente(ListaSinDuplicados,ListaOrdenada):-
    sort(ListaSinDuplicados,ListaOrdenada).

%% factor 1 por defecto

obtener_relevantes(ListaOrdenada,TopicoComun):-
    factor(0),
    filtrar_ceros(ListaOrdenada,TopicoComun).

obtener_relevantes(ListaOrdenada,TopicoComun):-
    factor(2),
    mejor_topico(ListaOrdenada,TopicoComun).

obtener_relevantes(ListaOrdenada,TopicoComun):-
    filtrar_ceros_unos(ListaOrdenada,TopicoComun).
    
filtrar_ceros([],[]).

filtrar_ceros([Topico|Resto],TopicoComun):-
    frecuencia(Topico,0),
    filtrar_ceros(Resto,TopicoComun).

filtrar_ceros(ListaOrdenada,ListaOrdenada).

filtrar_ceros_unos([],[]).

filtrar_ceros_unos([Topico|Resto],TopicoComun):-
    frecuencia(Topico,0),
    filtrar_ceros_unos(Resto,TopicoComun).

filtrar_ceros_unos([Topico|Resto],TopicoComun):-
    frecuencia(Topico,1),
    topico(Topico,TopicoLista),
    length(TopicoLista,Cardinalidad),
    Cardinalidad < 2,
    filtrar_ceros_unos(Resto,TopicoComun).

filtrar_ceros_unos([Topico|Resto],[Topico|TopicoComun]):-
    frecuencia(Topico,1),
    filtrar_ceros_unos(Resto,TopicoComun).

filtrar_ceros_unos(ListaOrdenada,ListaOrdenada).

mejor_topico([],[]).

mejor_topico(ListaOrdenada,[TopicoComun]):-
    last(TopicoComun,ListaOrdenada).

