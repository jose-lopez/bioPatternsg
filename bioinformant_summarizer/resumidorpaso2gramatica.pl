% resumidorpaso2entradas.pl version 1.1
% parte de resumidorcompleto.pl
% version 1.1: cambios para mejorar las gramaticas
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

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Paso 2 : Construir Tabla %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% sujeto, verbo, complemento
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% claridad(+Parrafo,-ParrafoWilliams)
% Implementa el concepto de Claridad de Williams. La claridad local de
% oración es la estructura fija del discurso escrito: sujeto, verbo y
% complemento.
% Procesa el párrafo de entrada (dividido en tokens) y retorna un párrafo
% donde cada oración es una lista de la siguiente forma:
% [sujeto(Sujeto),verbo(Verbo),complemento(Complemento)]

claridad([],[]).

% claridad([Oracion|RestoParrafo],[OracionWilliams|RestoParrafoWilliams]) :-
%   s(OracionWilliams,Oracion,[]),
%   claridad(RestoParrafo,RestoParrafoWilliams).

% Se utiliza un setof para la gramatica s con el fin de usar el primer termino
% de la lista L, pues es el termino donde el sujeto es de menor longitud, es decir
% se tiene el primer verbo de la oración.

claridad([Oracion|RestoParrafo],[OracionWilliams|RestoParrafoWilliams]) :-
      write(Oracion),
      setof(OracionW,s(OracionW,Oracion,[]),Lista),
      oracion(Lista,OracionWilliams), % OracionWilliams = [sujeto(L1),verbo(Vr),complemento(L2)],
      % prohibido write desde acá. Toda salida desde paso5.
      % write(L1), nl, nl, write(Vr), nl, nl, write(L2),
      % guardar todas las relaciones
      assert_list(Lista),
      % assert(relacion(OracionWilliams)),
      % verb_aux(Vr)-> mandar(L2);
      % mostrar(L2),
      % mostrar(L1),
      claridad(RestoParrafo,RestoParrafoWilliams).

claridad([_|RestoParrafo],RestoParrafoWilliams) :-
    claridad(RestoParrafo,RestoParrafoWilliams).

assert_list([]).
assert_list([Clausula|Resto]) :-
      assert(relacion(Clausula)),
      assert_list(Resto).

% oracion, retorne la oracion con menor sujeto que tenga el verbo mas complejo.

oracion([Oracion|[]],Oracion).

oracion([Oracion1,Oracion2|_],Oracion2) :-
    sujeto(Oracion1,Sujeto1),
    sujeto(Oracion2,Sujeto1).

oracion([Oracion1,_|_],Oracion1).

% s(?Estructura,+Oracion,+ListaVacia)
% Implementa una gramática basada en verbos, sólo reconoce el verbo principal
% de la oración (a través de un diccionario de verbos). Lo que esta antes del
% verbo es el sujeto y lo que esta después es el complemento. Usa DCG.
% Contienen la definición básica de las oraciones: frase nominal, frase verbal
% (verbo y complemento). Es decir s --> np,vp
% Retorna la estructura (Estructura) de la oración (Oracion), en términos
% de sujeto, verbo y complemento (Retorna una lista con estos elementos).

s([NP,V,C]) --> np(NP), vp(V,C).
s([NP,V,C]) --> np(NP), vp(V,C), ac(_).
s([NP,V,C]) --> np(_NP0), vp(_V0,_C0), ac([NP,V,C]). % una clausula incluida

ac([NP,V,C]) --> semicolon, np(NP), vp(V,C).

% frase nominal (np)
% Frase que contiene algún nombre (sustantivo), correspondiente al sujeto
% sintáctico de la oración. Considera algunas frases o colocaciones que
% tienen conflictos con los verbos.

% np(sujeto([C1,C2|Resto])) --> colocacion(C1,C2), !, np_resto(Resto).
% np(sujeto([C1,C2,C3|Resto])) --> colocacion(C1,C2,C3), !, np_resto(Resto).

np(sujeto([X|Resto])) --> [X|Resto], {not(member(';', Resto))}.

np_resto([X|Resto]) --> [X|Resto].

colocacion(a,partir) --> [a,partir].
colocacion(es,decir) --> [es,decir].
colocacion(es,importante,señalar) --> [es,importante,señalar].

% frase verbal (vp)
% constituida por el verbo (verb) y el complemento de la oración (comp).

vp(V,C) --> verb_compuesto(V), comp(C).
vp(V,C) --> verb_impersonal(V), comp(C).
vp(V,C) --> verb_aux(V), comp(C).
vp(V,C) --> verb(V), comp(C).
vp(V,C) --> verb_ont(V), comp(C).

%% oración impersonal: usa el pronombre "se", el sujeto permisible es la
%% generalidad de la gente.

verb_impersonal([V1,V2,V3,V4]) -->  impersonal(V1,V2), verb_compuesto([V3,V4]).
verb_impersonal([V1,V2,V3]) -->  impersonal(V1), verb_compuesto([V2,V3]).
verb_impersonal([V1,V2,V3]) -->  impersonal(V1,V2), resto_impersonal(V3).
verb_impersonal([V1,V2]) -->  impersonal(V1), resto_impersonal(V2).
impersonal(sujeto_verbo([se]), sujeto_verbo([le])) --> [se, le].
impersonal(sujeto_verbo([se]), sujeto_verbo([les])) --> [se, les].
impersonal(sujeto_verbo([se])) --> [se].
%% si el verbo no esta en el diccionario de verbos
resto_impersonal(verbo([X])) --> [X].

%% casos de verbos compuestos

% Los verbos en participio solo pueden estar despues de los verbos
% auxiliares. Si no es asi son sustantivos o adjetivos.

verb_compuesto([V1,V2]) --> verb_aux(V1), verb_part(V2).
verb_compuesto([V1,V2]) --> verb_aux(V1), verb(V2).
verb_compuesto([V1,V2]) --> verb_aux(V1), verb_aux(V2).
verb_compuesto([V1,V2]) --> verb(V1), verb(V2).

% complemento de la frase verbal (al menos una palabra o símnbolo)

% a complement must not have a ; inside
comp(complemento([Y|Resto])) --> [Y|Resto],
                                 {not(member(';', [Y|Resto]))}.

% complement must have al least one word or a semicolon
complement([], [], []).
complement([';'], [';'|R], R) :- !.  % stop at ; forget after it
complement([H|Rn], [H|R], F) :-      % jumps over one word
    complement(Rn, R, F).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% fin resumidorpaso2gramatica.pl
