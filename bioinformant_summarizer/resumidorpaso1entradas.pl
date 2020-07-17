% resumidorpaso1entradas.pl version 1.1
% parte de resumidorcompleto.pl
% version 1.1: correcciones al lexer para reconocer ; y ) semicolon
%
% ensamblado por Jacinto Dávila
%
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
% Authors: H. Yelitza Contreras <hyelitza@ula.ve>
%        and Jacinto Dávila <jacinto@ula.ve>
% Adaptación: Jacinto Dávila and Marilú Parra <mmarilu@ula.ve>
% Direccion: Universidad de Los Andes. Mérida, Venezuela.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Resumidor: obtener tópicos de un texto
% Dado un archivo de texto html obtiene los tópicos del texto
% y los retorna en la salida estándar.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% resumir/0
% resumir/1 resumir(+TipoSalida)
% Lee la entrada estandar y dado el texto de un documento retorna
% por cada párrafo una lista de tópicos.
% La salida por defecto (usando resumidor/0) es párrafo original,
% párrafo williams y tópicos del párrafo.
% Si se requiere otra salida se puede usar resumidor/1 con los
% siguientes valores:
% TipoSalida = 0, sólo imprime en la salida los tópicos resultantes
% de cada párrafo en salida html
% TipoSalida = 1, sólo imprime en la salida los tópicos sin procesar
% del texto

:- encoding(utf8).

% Esta linea es necesaria para poder modificar terminales de la gramatica en tiempo real.
:- dynamic verbo/1,verb/2,predicado/1.

% Sección agregada de la última versión del resumidor
:- use_module(library('http/http_open.pl')).
:- use_module(library(sgml)).
:- use_module(library(dcg/basics)).

factor(1).

resume(URL) :-
    descargar(URL, L),
    leer_parrafos(L, _, Parrafos,ParrafosUpper,_, TodosTopicos), !,
    salida_html( Parrafos, ParrafosUpper, TodosTopicos ).

% ----------------------------------------------------  descargar

descargar(URL, Tokens) :-
  is_absolute_url(URL),
  http_open(URL, In, []), !,
  set_stream(In, encoding(utf8)),
  read_stream_to_codes(In, String),
  lex_html(Tokens, String, []),
  close(In).

descargar(FILE, Tokens) :-
  open(FILE, read, In, []), !,
  set_stream(In, encoding(utf8)),
  read_stream_to_codes(In, String),
  lex_html(Tokens, String, []),
  close(In).


/* lex adapted from: from http://stackoverflow.com/questions/2529049/swi-prolog-tokenize-atom-2-replacement
 */

% lexer

lex_html(L) --> head_off, filling, body_in(L), !.
lex_html([]) --> [].

head_off --> head, skipped_head. %, end_head.

body_in(L) --> start_body, skipped_angular, lex(L). %, end_body.

filling --> fill, !, filling.
filling --> [].

fill --> white. % elimina blancos incluyendo
fill --> "\r".  % elimina fines de linea
fill --> "\n".  % elimina salto de linea
fill --> "\t".  % elimina tabuladores

% esto es muy débil. Faltan variantes o downcase
head --> "<html".
head --> "<HTML".
head --> "<!doctype".
head --> "<!DOCTYPE".

% It stops at </head> or similar
skipped_head(In, R) :- end_head(F, []), append(F, R, In).
skipped_head([_|RIn], O) :- skipped_head(RIn, O).

end_head --> "</head>".
end_head --> "</HEAD>".

start_body --> "<body".
start_body --> "<BODY".

% It stops at > or similar
skipped_angular(In, R) :- closing_angular(F, []), append(F, R, In).
skipped_angular([_|RIn], O) :- skipped_angular(RIn, O).

closing_angular --> ">".

lex([H | T]) -->
    lexem_t(H), !, % {write(H), write(' ')},
    lex(T).

lex([]) --> [].

lexem_t(L) --> trashes, lexem(L), trashes.

% not needed
% end_body --> "</body>", !, string.
% end_body --> "</BODY>", !, string.

trashes --> trash, !, trashes.
trashes --> [].

trash --> tag_marker(End), string(_), End, !.
% trash --> white.
trash --> [S], {code_type(S, space);code_type(S, white)}.
trash --> "\t".
trash --> "\r".  % elimina fines de linea
trash --> "\n".  % elimina salto de linea


tag_marker("-->") --> "<!--".  %html comments
tag_marker(">") --> "<". %any other html tag gets removed

% , lexem('/'), lexem(T), {to_skip(T)}, !.
% tag_marker(">") --> "<", lexem(T), {T\='/', to_skip(T)}.

to_skip(T) :- not(to_keep(T)).

to_keep(p).
to_keep(b).
to_keep('PRE').

%lexem('\r') --> "\r".
%lexem('\n') --> "\n".
lexem('–') --> "–".
lexem('=') --> "=".
lexem('(') --> "(".
lexem(')') --> ")".
lexem('[') --> "[".
lexem(']') --> "]".
lexem('{') --> "{".
lexem('}') --> "}".
lexem('.') --> ".".
lexem(+) --> "+".
lexem(-) --> "-".
lexem(*) --> "*".
lexem(/) --> "/".
lexem(^) --> "^".
lexem(,) --> ",".
lexem(!) --> "!".
lexem(?) --> "?".
lexem(¿) --> "¿".
lexem(':') --> ":".
lexem(&) --> "&".
lexem(';') --> ";".
lexem('|') --> "|".
lexem('\'') --> "'".
lexem('\\') --> "\\".
lexem('\"') --> "\"".
lexem('%') --> "%".
lexem('•') --> "•".
lexem('@') --> "@".
lexem('º') --> "º".
lexem('°') --> "°".
lexem('“') --> "“".
lexem('”') --> "”".
lexem('©') --> "©".
lexem('#') --> "#".
lexem('<b>') --> "<b>".
lexem('</b>') --> "</b>".
lexem('</p>') --> "</p>".
lexem('<p>') --> "<p", skipped_angular.

lexem(N) --> hex_start, !, xinteger(N). % this handles hex numbers
lexem(N) --> number(N). % this handles integers/floats
lexem(A) --> identifier_c(L), {string_to_atom(L, A)}.

hex_start --> "0X".
hex_start --> "0x".

identifier_c([H | T]) --> alpha(H), !, many_alnum(T).

% alpha(H) --> [H], {code_type(H, alpha);code_type(H, csym);code_type(H,  graph)}.
alpha(H) --> [H], {(code_type(H, alnum);code_type(H, csym);
                    code_type(H, prolog_symbol);
		                code_type(H, graph)),
	          not(atom_codes('<', [H])), % excluye el comienzo de un tag
	          not(atom_codes('.', [H])), !}. % excluye al fin de oracion
alnum(H) --> [H], {(code_type(H, alnum);code_type(H, csym);
                    code_type(H, prolog_symbol);
	           code_type(H, graph)),
	          not(atom_codes('<', [H])), % excluye el comienzo de un tag
            not(atom_codes(';', [H])), % excluye el punto y coma de fin clausula
            not(atom_codes(')', [H])), % excluye el paréntesis de cierre
            not(atom_codes('.', [H])), !}. % excluye al fin de oracion

many_alnum([H | T]) --> alnum(H), !, many_alnum(T).

many_alnum([]) --> [].

semicolon --> [H], {string_to_atom([H], ';')}.

% --------------------------------------------------------------- leer_parrafos
% Leer los parrafos en una lista con los caracteres obtenidos de un documento.
%
% leer_parrafos(Tokens-, RestoTokens+, ...)
% leer_parrafos(Ascci-, REstoAscci+, Parrafos+, ParrafosUpper+, Proximo+, Topicos+)
% dado que la lista L de entrada puede contener mas de un
% parrafo, este procedimiento extrae todos los parrafos de
% esa lista con invocaciones repetidas a leer_parrafo, tal
% como fue implementado por Y. Contreras.
%

leer_parrafos([], [], [], [], -1, []).

leer_parrafos(L, RestoL, TodosParrafos,TodosParrafosUpper, ProximoC, NTopicos) :-
  leer_parrafo( L, Resto1, PrimerParrafo, PrimParrafoUpper),
  claridad(PrimerParrafo, ParrafoW),
  !,
  extraer_topico(ParrafoW, Topicos),
  topico_comun(Topicos, TopicosPonderados),
  % selecciona_topico(TopicosPonderados, -1, [], Topico),
  % write('Topico comun: '), write(Topico), nl,
  % ( Topico \= [] -> NTopicos = [Topico|RestoTopicos]  ; NTopicos = RestoTopicos ), !,
  leer_parrafos( Resto1, RestoL, Siguientes, SiguientesUpper, ProximoC, RestoTopicos ),
  append( TopicosPonderados, RestoTopicos, NTopicos ),
  append( PrimerParrafo, Siguientes, TodosParrafos),
  append( PrimParrafoUpper, SiguientesUpper, TodosParrafosUpper ).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Paso 1 : Leer, Tokenizer
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% archivo entrada/salida, tokens
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% Esto corresponde al primer paso para escribir resumenes.
%% Consiste en dividir el texto en fases del pensamiento: párrafos y
%% oraciones. Las palabras corresponden a los átomos de entrada.

% leer_parrafo(-AtomosEntrada, +AtomosResto, +AtomosSalida,+AtomosUpper)
% Lee una línea del texto, separándola en una lista de átomos.
% Atomos = párrafo lower-case, delimitado por el caracter especial
% de fin de línea [10]. AtomosUpper = párrafo original del texto
% (incluye upper-case).

%% los ultimos cambios en la lectura de los archivos forzaron una modificación
%% y simplificaron este método

%% aspectos pendientes incluyen distinción mayúscula-minuscula

leer_parrafo([], [], [], []).

leer_parrafo(['</p>'|RestoA], RestoA, [], []).

leer_parrafo(['<p>'|RestoA], Resto, [Oracion|R1], [Oracion|R2]) :-
  leer_oracion(RestoA, NuevoRestoA, Oracion),
  leer_parrafo(NuevoRestoA, Resto, R1, R2).

leer_parrafo([Palabra|RestoA], Resto, [Oracion|R1], [Oracion|R2]) :-
  leer_oracion([Palabra|RestoA], NuevoRestoA, Oracion),
  leer_parrafo(NuevoRestoA, Resto, R1, R2).

leer_oracion([], [], []).

leer_oracion(['.'|Resto], Resto, []).

leer_oracion(['<p>'|Resto], ['<p>'|Resto], []).

leer_oracion(['</p>'|Resto], ['</p>'|Resto], []).

leer_oracion([Palabra|Restantes], Resto, [Palabra|RR] ) :-
  leer_oracion(Restantes, Resto, RR).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% finpaso1entradas.pl
