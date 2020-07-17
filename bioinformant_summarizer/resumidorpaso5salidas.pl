% resumidorpaso5salidas.pl version 1.0 
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


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% salida_html(+Texto,+TextoUpper,+Topico).
% Dado el texto de un parrafo, resalta sus tópicos en negrita del texto
% original (TextoUpper) en una salida HTML (salida estandar).

salida_html(Texto,TextoUpper,Topico):-
    etiquetar_topicos(Topico,Texto,TextoUpper,TextoEtiquetado),
    imprimir_etiquetas_encabezado,%%tell('/home/dahyana/Escritorio/prueba.txt'),write('['),told,
    imprimir_texto(TextoEtiquetado),%%append('/home/dahyana/Escritorio/prueba.txt'),write(']'),told,
    imprimir_etiquetas_cierre.

etiquetar_topicos([],_,TextoUpper,TextoUpper).

etiquetar_topicos([Topico|Resto],Texto,TextoUpper,TextoEtiquetado):-
    topico(Topico,TopicoLista),
    etiquetar_topico_en_texto(TopicoLista,Texto,TextoUpper,TextoTopico),
    etiquetar_topicos(Resto,Texto,TextoTopico,TextoEtiquetado).

etiquetar_topico_en_texto(_,[],[],[]):- fail,!.

etiquetar_topico_en_texto(TopicoLista,[Oracion|_],[OracionUpper|TextoUpper],[OracionEtiqueta|TextoUpper]):-
    subset(TopicoLista,Oracion),
    insertar_etiqueta(TopicoLista,Oracion,OracionUpper,OracionEtiqueta).    

etiquetar_topico_en_texto(TopicoLista,[_|Texto],[OracionUpper|TextoUpper],[OracionUpper|TextoTopico]):-
    etiquetar_topico_en_texto(TopicoLista,Texto,TextoUpper,TextoTopico).

insertar_etiqueta(TopicoLista,Oracion,OracionUpper,OracionEtiqueta):-
    topico_en_oracion(OracionDividida,TopicoLista,Oracion,[]),
    insertar_bold(OracionDividida,OracionUpper,OracionEtiqueta).

insertar_bold(OracionDivida,OracionUpper,OracionEtiqueta):-
    dividir_oracion_upper(OracionDivida,OracionUpper,[Antes,Topico,Despues]),
    append(Antes,['<b>'],AntesEtiqueta),
    append(AntesEtiqueta,Topico,AntesEtiquetaTopico),
    append(AntesEtiquetaTopico,['</b>'],AntesTopicoEtiqueta),
    append(AntesTopicoEtiqueta,Despues,OracionEtiqueta).    

dividir_oracion_upper([Antes,Topico,Despues],OracionUpper,[AntesUpper,TopicoUpper,DespuesUpper]) :-
    lista_upper(Antes,OracionUpper,RestoAntes,AntesUpper),
    lista_upper(Topico,RestoAntes,RestoTopico,TopicoUpper),
    lista_upper(Despues,RestoTopico,[],DespuesUpper).

% lista_upper(SubListaLower,ListaUpper,RestoUpper,SubListaUpper)

lista_upper([],L,L,[]).

lista_upper(SubListaLower,[P1|ListaUpper],RestoUpper,[P1|SubListaUpper]) :-
    es_especial(P1),
    lista_upper(SubListaLower,ListaUpper,RestoUpper,SubListaUpper).

lista_upper([_|SubListaLower],[P1|ListaUpper],RestoUpper,[P1|SubListaUpper]) :-
    lista_upper(SubListaLower,ListaUpper,RestoUpper,SubListaUpper).

topico_en_oracion([T1,TopicoLista,T2],TopicoLista) --> 
    antes(T1), igual(TopicoLista), despues(T2).

antes([X|Resto]) --> [X|Resto].
antes([]) --> [].
despues([]) --> [].
despues([X|Resto]) --> [X|Resto].
igual(Topico,TopicoResto,Resto):- append(Topico,Resto,TopicoResto).

imprimir_etiquetas_encabezado :-
    write('<!DOCTYPE html> '), 
    write('<html>'), 
    write('<head>'), 
    write('<meta charset="utf-8">'), 
    write('<title>RESUMEN AUTOMATICO</title>'), 
    write('</head>'), 
    write('<body>'),
    write('<h1><B>Resumen autom&aacute;tico </B></h1>'), 
    write('<hr>'),nl.

imprimir_texto([]).

imprimir_texto([Oracion|Resto]):-
    member('<b>',Oracion),
    imprimir_oracion(Oracion),
    write('.   '),%%append('/home/dahyana/Escritorio/prueba.txt'),write(Oracion),write(', '),told,
    imprimir_texto(Resto).

imprimir_texto([_|Resto]):-
    imprimir_texto(Resto).

imprimir_oracion([]).

imprimir_oracion(['-', P|RestoO]) :-
  write('-'), imprimir_palabra(P), !, 
  imprimir_oracion(RestoO).

imprimir_oracion([')',','|RestoO]) :-
  write('),'), !, 
  imprimir_oracion(RestoO).

imprimir_oracion(['('|RestoO]) :-
  write(' ('), !, 
  imprimir_oracion(RestoO).

imprimir_oracion([')'|RestoO]) :-
  write(')'), !, 
  imprimir_oracion(RestoO).

imprimir_oracion(['[', Palabra|RestoO]) :-
  write('['), imprimir_palabra(Palabra), !, 
  imprimir_oracion(RestoO).

imprimir_oracion([']'|RestoO]) :-
  write(']'), !, write(' '), 
  imprimir_oracion(RestoO).

imprimir_oracion([Palabra,','|RestoO]) :-
  write(' '), imprimir_palabra(Palabra), write(','), !, 
  imprimir_oracion(RestoO).

imprimir_oracion([Palabra,';'|RestoO]) :-
  write(' '), 
  imprimir_palabra(Palabra), write(';'), !, 
  imprimir_oracion(RestoO).

imprimir_oracion([Palabra,':'|RestoO]) :-
  write(' '), 
  imprimir_palabra(Palabra), write(':'), !, 
  imprimir_oracion(RestoO).

imprimir_oracion([Palabra|Resto]):-
    imprimir_especial(Palabra), !,
    imprimir_oracion(Resto).

imprimir_oracion([Palabra|RestoO]) :-
  write(' '), imprimir_palabra(Palabra), !, 
  imprimir_oracion(RestoO).

% retirada esta clausula..por una anterior
%imprimir_oracion([Palabra|Resto]):-
%   write(' '),
%   write(Palabra),
%   imprimir_oracion(Resto).

imprimir_palabra(P) :- imprimir_especial(P), !.
imprimir_palabra(P) :- print(P).

%
% imprimir_especial(Palabra):-
%   es_coma(Palabra).

imprimir_especial(Palabra):-
    es_especial(Palabra). 

imprimir_etiquetas_cierre :-
    write('<hr>'), 
    listing(relacion), 
    write(' </p> <hr> </body> </html>').

es_especial('<b>') :- write('<b>'). 
es_especial('</b>') :- write('</b>'). 

%%%%%%%%%%%%%%%%%%%%%%%%%%%%% fin de resumidorpaso5salidas.pl
