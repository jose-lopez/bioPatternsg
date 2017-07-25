% diccionarios.pl
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
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Diccionarios %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  Español

%% es_articulo(Palabra,Numero,Genero).

es_articulo(el,singular,masculino).
es_articulo(los,plural,masculino). 
es_articulo(la,singular,femenino).
es_articulo(las,plural,femenino). 
es_articulo(un,singular,masculino). 
es_articulo(unos,plural,masculino). 
es_articulo(una,singular,femenino).
es_articulo(unas,plural,femenino).
es_articulo(the,plural,masculino).
es_articulo(a,singular,masculino).
es_articulo(an,plural,masculino).

%% articulos
articulo(el).
articulo(los). 
articulo(la).
articulo(las). 
articulo(un). 
articulo(unos). 
articulo(una).
articulo(unas).
articulo(the).
articulo(a).
articulo(an).

%% concatenar_listas([TopicosRelacionado,Despues],ListaNueva,[]).
%%
%% ListaNueva = TopicosRelacionado + Despues

concatenar_listas([TopicosRelacionado,Despues]) -->
    lista(TopicosRelacionado),
    lista(Despues).

lista([X|Resto]) --> [X|Resto].
lista([])--> [].

% espanol
es_pronombre(lo).
es_pronombre(le).
es_pronombre(les).
es_pronombre(esa).
es_pronombre(ese).
es_pronombre(se).
es_pronombre(X) :- pronombre(_,_,[X],[]).

pronom(lo).
pronom(le).
pronom(les).
pronom(esa).
pronom(ese).
pronom(se).

es_conjuncion(a).
es_conjuncion(e).
es_conjuncion(y).
es_conjuncion(de).
es_conjuncion(del).
es_conjuncion(en).
es_conjuncion(con).
es_conjuncion(como).
es_conjuncion(para).
es_conjuncion(que).
es_conjuncion(qué). 

%% adjetivo_posesivo_antsustantivo

adjetivo_posesivo_antsustantivo(mi).
adjetivo_posesivo_antsustantivo(mis).
adjetivo_posesivo_antsustantivo(tu).
adjetivo_posesivo_antsustantivo(tus).
adjetivo_posesivo_antsustantivo(su).
adjetivo_posesivo_antsustantivo(sus).
adjetivo_posesivo_antsustantivo(nuestro).
adjetivo_posesivo_antsustantivo(nuestros).
adjetivo_posesivo_antsustantivo(nuestra).
adjetivo_posesivo_antsustantivo(nuestras).
adjetivo_posesivo_antsustantivo(vuestro).
adjetivo_posesivo_antsustantivo(vuestros).
adjetivo_posesivo_antsustantivo(vuestra).
adjetivo_posesivo_antsustantivo(vuestras).
adjetivo_posesivo_antsustantivo(su).
adjetivo_posesivo_antsustantivo(sus).

%% adjetivo_posesivo_despsustantivo

adjetivo_posesivo_despsustantivo(mio).
adjetivo_posesivo_despsustantivo(mia).
adjetivo_posesivo_despsustantivo(mios).
adjetivo_posesivo_despsustantivo(mias).
adjetivo_posesivo_despsustantivo(tuyo).
adjetivo_posesivo_despsustantivo(tuyos).
adjetivo_posesivo_despsustantivo(tuyas).
adjetivo_posesivo_despsustantivo(tuya).
adjetivo_posesivo_despsustantivo(suyo).
adjetivo_posesivo_despsustantivo(suya).
adjetivo_posesivo_despsustantivo(suyas).
adjetivo_posesivo_despsustantivo(suyos).
adjetivo_posesivo_despsustantivo(nuestra).
adjetivo_posesivo_despsustantivo(nuestro).
adjetivo_posesivo_despsustantivo(nuestras).
adjetivo_posesivo_despsustantivo(nuestros).
adjetivo_posesivo_despsustantivo(vuestro).
adjetivo_posesivo_despsustantivo(vuestra).
adjetivo_posesivo_despsustantivo(vuestros).
adjetivo_posesivo_despsustantivo(vuestras).
adjetivo_posesivo_despsustantivo(suyo).
adjetivo_posesivo_despsustantivo(suya).
adjetivo_posesivo_despsustantivo(suyos).
adjetivo_posesivo_despsustantivo(suyas).

pronombre(singular,_) --> [esto].
pronombre(plural,masculino) --> [estos].
pronombre(singular,femenino) --> [esta].
pronombre(plural,femenino) --> [estas].
pronombre(singular,masculino) --> [este].
pronombre(singular,_) --> [ello].
pronombre(singular,masculino) --> [aquel].
pronombre(singular,femenino) --> [aquella].
pronombre(plural,femenino) --> [aquellas].
pronombre(plural,masculino) --> [aquellos].

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Tokenizer
%% modificado por Jacinto Dávila - 2012 Junio 21

%% tomado de 
% et.pl - M. Covington      2003 February 12

% etu.pl - Modified for Unicode - Donald Rogers     2006 July 17
%          email: dero9753@ihug.co.nz
%          Modified to cope with comma in numbers   2006 July 20

% ET the Efficient Tokenizer

%%
%% Character classification
%%

% char_type_char(+Char,-Type,-TranslatedChar)
%   Classifies all characters as letter, digit, special, etc.,
%   and also translates each character into the character that
%   will represent it, converting upper to lower case.

char_type_char(Char,Type,Tr) :-
  char_table(Char,Type,Tr),
   !.

char_type_char(C,alfa,C).

% End of line marks
% eol -> fin
char_table(end_of_file, fin, end_of_file).

char_table('\n',        especial, '\n'       ).
% Whitespace characters
% whitespace -> blanco
char_table(' ',     blanco,  ' ').     % blank
char_table('\t',    blanco,  ' ').     % tab
char_table('\r',    blanco,  ' ').     % return
char_table('''',    blanco, '''').     % apostrophe does not translate to blank

% Donald removed the letter characters and replaced them by special characters.
% There are too many Unicode letters to put them all in a table.
% The third parameter may be useless, but maybe someone will want to convert
% some of the special characters.
% There may be other Unicode characters that need to be added.
% special -> especial
char_table('~',     especial,    '~' ).
char_table('`',     especial,    '`' ).
char_table('!',     especial,    '!' ).
char_table('@',     especial,    '@' ).
char_table('#',     especial,    '#' ).
char_table('$',     especial,    '$' ).
char_table('\u0025',especial,    '\u0025' ). %
char_table('^',     especial,    '^' ).
char_table('&',     especial,    '&' ).
char_table('*',     especial,    '*' ).
char_table('(',     especial,    '(' ).
char_table(')',     especial,    ')' ).
char_table('_',     especial,    '_' ).
char_table('-',     especial,    '-' ).
char_table('+',     especial,    '+' ).
char_table('=',     especial,    '=' ).
char_table('{',     especial,    '{' ).
char_table('[',     especial,    '[' ).
char_table('}',     especial,    '}' ).
char_table(']',     especial,    ']' ).
char_table('|',     especial,    '|' ).
char_table('\\',    especial,    '\\' ).
char_table(':',     especial,    ':' ).
char_table(';',     especial,    ';' ).
char_table('"',     especial,    '"' ).
char_table('<',     especial,    '<' ).
char_table(',',     especial,    ',' ).
char_table('>',     especial,    '>' ).
char_table('.',     especial,    '.' ).
char_table('?',     especial,    '?' ).
char_table('/',     especial,    '/' ).

% Digits
% digit -> alfa ; Ojo, corregir
char_table('0',   alfa,     '0' ).
char_table('1',   alfa,     '1' ).
char_table('2',   alfa,     '2' ).
char_table('3',   alfa,     '3' ).
char_table('4',   alfa,     '4' ).
char_table('5',   alfa,     '5' ).
char_table('6',   alfa,     '6' ).
char_table('7',   alfa,     '7' ).
char_table('8',   alfa,     '8' ).
char_table('9',   alfa,     '9' ).

% Everything else is a letter character.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% diccionario de expresiones (Williams y texto)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% Conectores lógicos

expresion([(,)]) --> [(,)].
expresion([además]) --> [además].
expresion([como, resultado]) --> [como, resultado].
expresion([pero]) --> [pero].
expresion([no, obstante]) --> [no, obstante].
expresion([de, esta, forma]) --> [de, esta, forma].
expresion([sin, embargo]) --> [sin, embargo].
expresion([en, vista, de]) --> [en, vista, de].
expresion([también]) --> [también].
expresion([que]) --> [que].
expresion([y]) --> [y].

%% Expresiones de tiempo y espacio

expresion([más, tarde]) --> [más, tarde].
expresion([en, américa]) --> [en, américa].
expresion([en, europa]) --> [en, europa].
expresion([en, los, últimos, años]) --> [en, los, últimos, años].
expresion([en, el, siglo, X]) --> [en, el, siglo, X].
expresion([del, siglo, X]) --> [del, siglo, X].
expresion([siglo, X]) --> [siglo, X].
expresion([a, principios, de]) --> [a, principios, de].
expresion([a, principios, del]) --> [a, principios, del].
expresion([de, esa, época]) --> [de, esa, época].
expresion([en, X]) --> [en, X].
expresion([actualmente]) --> [actualmente].
expresion([desde, X]) --> [desde, X].
expresion([hasta, X]) --> [hasta, X].
expresion([luego]) --> [luego].
expresion([eventualmente]) --> [eventualmente].
expresion([anteriormente]) --> [anteriormente].
expresion([cuando]) --> [cuando].

%% Expresiones para evaluar

expresion([no]) --> [no].
expresion([entonces]) --> [entonces].
expresion([quizás]) --> [quizás].
expresion([afirmativamente]) --> [afirmativamente].
expresion([bajo, estas, circunstancias]) --> [bajo, estas, circunstancias].
expresion([a, partir]) --> [a, partir].
expresion([en, consecuencia]) --> [en, consecuencia].
expresion([como, consecuencia]) --> [como, consecuencia].
expresion([se, concluye]) --> [se, concluye].
expresion([con, base, en, el, marco, antes, descrito]) --> [con, base, en, el, marco, antes, descrito].
expresion([es, importante, señalar]) --> [es, importante, señalar].
expresion([adicionalmente]) --> [adicionalmente].
expresion([debido, a]) --> [debido, a].
expresion([por, tanto]) --> [por, tanto].
expresion([por, lo, tanto]) --> [por, lo, tanto].
expresion([por, ejemplo]) --> [por, ejemplo].
expresion([se, sabe]) --> [se, sabe].
expresion([el, presente]) --> [el, presente].
expresion([a, través, de]) --> [a, través, de].
expresion([a, través, del]) --> [a, través, del].
expresion([de, acuerdo, con]) --> [de, acuerdo, con].
expresion([por, otra, parte]) --> [por, otra, parte].
expresion([por, otro, lado]) --> [por, otro, lado].
expresion([por, su, parte]) --> [por, su, parte].
expresion([por, esta, razón]) --> [por, esta, razón].
expresion([por, último]) --> [por, último].
expresion([por]) --> [por].
expresion([más, específicamente]) --> [más, específicamente].
expresion([según]) --> [según].
expresion([aún]) --> [aún].
expresion([a, su, vez]) --> [a, su, vez].
expresion([teóricamente]) --> [teóricamente].
expresion([solo]) --> [solo].
expresion([cómo]) --> [cómo].
expresion([a]) --> [a].
expresion([de]) --> [de].
expresion([de, ellas]) --> [de, ellas].
expresion([como, objetivo]) --> [como, objetivo].
expresion([dentro, del, mencionado, sector]) --> [dentro, del, mencionado, sector].
expresion([para]) --> [para].
expresion([paradójicamente]) --> [paradójicamente].
expresion([de, otra, manera]) --> [de, otra, manera].
expresion([con, el, propósito, de]) --> [con, el, propósito, de].
expresion([con, ese, propósito]) --> [con, ese, propósito].
expresion([cabe]) --> [cabe].




%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% diccionario de verbos %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% verbos obtenidos con el tact
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% Verbos que se cargan a partir de las palabras indicads por la ontologi
verb_ont(verbo([pintar])) --> [pintar].

buscar_verbos([], []) :- !.

buscar_verbos([Cabeza|Resto],LResult) :-
  (write('proceso la palabra'), write(Cabeza), nl, 
  es_verbo_de(Cabeza,Lista1),!,buscar_verbos(Resto,Lista2), append(Lista1,Lista2,LResult); buscar_verbos(Resto,LResult)).

agregar_verbos([]) :- !.

agregar_verbos([VerboCabecera|VerboCuerpo]) :-
%    write('Esta agregando el verbo: '), write(VerboCabecera), nl, nl,
     assert(( verb(verbo([VerboCabecera])) --> [VerboCabecera] )),
     agregar_verbos(VerboCuerpo).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Las funciones buscar_verbos y agregar_verbos corresponden al trabajo
%% realizado por Jose Lopez y Dahyana Nimo, para hacer que la carga del
%% diccionario de verbos (que a continuacion se realiza de manera estatica)
%% a traves de una ontologia del area que permita determinar los verbos que 
%% tienen pertinencia para el resumidor en un contexto determinado. 
%% dnimo@unet.edu.ve, jlopez@unet.edu.ve
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% verbos regulares: ar,er,ir,ando,endo
   
verb(verbo([acarreando])) --> [acarreando].
verb(verbo([acceder])) --> [acceder].
verb(verbo([acondicionar])) --> [acondicionar].
verb(verbo([acopiar])) --> [acopiar].
verb(verbo([acrecentar])) --> [acrecentar].
verb(verbo([activando])) --> [activando].
verb(verbo([actuar])) --> [actuar].
verb(verbo([acudir])) --> [acudir].
verb(verbo([adaptando])) --> [adaptando].
verb(verbo([adaptar])) --> [adaptar].
verb(verbo([adecuar])) --> [adecuar].
verb(verbo([adelgazar])) --> [adelgazar].
verb(verbo([administrar])) --> [administrar].
verb(verbo([adoptando])) --> [adoptando].
verb(verbo([adoptar])) --> [adoptar].
verb(verbo([adquiriendo])) --> [adquiriendo].
verb(verbo([adquirir])) --> [adquirir].
verb(verbo([afectando])) --> [afectando].
verb(verbo([afirmar])) --> [afirmar].
verb(verbo([afrontar])) --> [afrontar].
verb(verbo([agrupando])) --> [agrupando].
verb(verbo([agrupar])) --> [agrupar].
verb(verbo([ahorrar])) --> [ahorrar].
verb(verbo([alarmar])) --> [alarmar].
verb(verbo([alcanzando])) --> [alcanzando].
verb(verbo([alcanzar])) --> [alcanzar].
verb(verbo([alimentar])) --> [alimentar].
verb(verbo([aminorar])) --> [aminorar].
verb(verbo([ampliar])) --> [ampliar].
verb(verbo([analizando])) --> [analizando].
verb(verbo([analizar])) --> [analizar].

verb(verbo([aparecer])) --> [aparecer].
verb(verbo([apareciendo])) --> [apareciendo].

verb(verbo([aplicando])) --> [aplicando].
verb(verbo([aplicar])) --> [aplicar].
verb(verbo([aportar])) --> [aportar].
verb(verbo([apoyando])) --> [apoyando].
verb(verbo([apoyar])) --> [apoyar].
verb(verbo([aprender])) --> [aprender].
verb(verbo([aprovechar])) --> [aprovechar].

%%Verbos agregados por DN, aproximar
verb(verbo([aproximar])) --> [aproximar].
verb(verbo([aproximando])) --> [aproximando].

verb(verbo([apuntar])) --> [apuntar].
verb(verbo([apuntando])) --> [apuntando].

verb(verbo([armonizar])) --> [armonizar].
verb(verbo([arreglar])) --> [arreglar].
verb(verbo([asegurando])) --> [asegurando].
verb(verbo([asegurar])) --> [asegurar].
verb(verbo([asesorar])) --> [asesorar].
verb(verbo([asimilar])) --> [asimilar].

verb(verbo([asociar])) --> [asociar].
verb(verbo([asociando])) --> [asociando].

verb(verbo([asumiendo])) --> [asumiendo].
verb(verbo([asumir])) --> [asumir].
verb(verbo([atacar])) --> [atacar].
verb(verbo([atajar])) --> [atajar].
verb(verbo([atender])) --> [atender].
verb(verbo([atraer])) --> [atraer].
verb(verbo([aumentar])) --> [aumentar].
verb(verbo([autorizar])) --> [autorizar].
verb(verbo([ayudar])) --> [ayudar].
verb(verbo([ayudando])) --> [ayudando].
verb(verbo([beneficiar])) --> [beneficiar].

verb(verbo([bloquear])) --> [bloquear].
verb(verbo([bloqueando])) --> [bloqueando].

verb(verbo([brindando])) --> [brindando].
verb(verbo([buscando])) --> [buscando].
verb(verbo([buscar])) --> [buscar].
verb(verbo([calificar])) --> [calificar].
verb(verbo([canalizar])) --> [canalizar].
verb(verbo([capacitar])) --> [capacitar].
verb(verbo([capacitando])) --> [capacitando].
verb(verbo([caracterizar])) --> [caracterizar].
verb(verbo([caracterizando])) --> [caracterizando].

verb(verbo([carecer])) --> [carecer].
verb(verbo([careciendo])) --> [careciendo].

verb(verbo([causar])) --> [causar].
verb(verbo([cerrando])) --> [cerrando].
verb(verbo([clasificar])) --> [clasificar].

verb(verbo([coactivar])) --> [coactivar].
verb(verbo([coactivando])) --> [coactivando].

%%Verbo agregado por DN, codificar
verb(verbo([codificar])) --> [codificar].
verb(verbo([codificando])) --> [codificando].

verb(verbo([colocar])) --> [colocar].
verb(verbo([combinando])) --> [combinando].
verb(verbo([comenzar])) --> [comenzar].
verb(verbo([comer])) --> [comer].
verb(verbo([comparar])) --> [comparar].

verb(verbo([compartir])) --> [compartir].
verb(verbo([compartiendo])) --> [compartiendo].

verb(verbo([competir])) --> [competir].
verb(verbo([complementar])) --> [complementar].
verb(verbo([comprar])) --> [comprar].
verb(verbo([comprender])) --> [comprender].
verb(verbo([comprobar])) --> [comprobar].

verb(verbo([componer])) --> [componer].
verb(verbo([componiendo])) --> [componiendo].
verb(verbo([comprender])) --> [comprender].
verb(verbo([comprendiendo])) --> [comprendiendo].


verb(verbo([comprometiendo])) --> [comprometiendo].
verb(verbo([concertar])) --> [concertar].
verb(verbo([concluir])) --> [concluir].
verb(verbo([concretar])) --> [concretar].

verb(verbo([conducir])) --> [conducir].
verb(verbo([conduciendo])) --> [conduciendo].

verb(verbo([confiscar])) --> [confiscar].
verb(verbo([conformar])) --> [conformar].
verb(verbo([congelar])) --> [congelar].
verb(verbo([conocer])) --> [conocer].
verb(verbo([conseguir])) --> [conseguir].
verb(verbo([conservar])) --> [conservar].
verb(verbo([considerando])) --> [considerando].
verb(verbo([consolidar])) --> [consolidar].
verb(verbo([construir])) --> [construir].
verb(verbo([consumir])) --> [consumir].

verb(verbo([contener])) --> [contener].
verb(verbo([conteniendo])) --> [conteniendo].

verb(verbo([continuar])) --> [continuar].
verb(verbo([contraer])) --> [contraer].
verb(verbo([contribuir])) --> [contribuir].
verb(verbo([contribuyendo])) --> [contribuyendo].
verb(verbo([controlando])) --> [controlando].
verb(verbo([controlar])) --> [controlar].
verb(verbo([convencer])) --> [convencer].
verb(verbo([convertir])) --> [convertir].
verb(verbo([convirtiendo])) --> [convirtiendo].
verb(verbo([coordinar])) --> [coordinar].
verb(verbo([corroer])) --> [corroer].
verb(verbo([cosechando])) --> [cosechando].
verb(verbo([cosechar])) --> [cosechar].
verb(verbo([costar])) --> [costar].
verb(verbo([crear])) --> [crear].
verb(verbo([crecer])) --> [crecer].
verb(verbo([creciendo])) --> [creciendo].
verb(verbo([creer])) --> [creer].
verb(verbo([cubrir])) --> [cubrir].
verb(verbo([cultivar])) --> [cultivar].
verb(verbo([cumplir])) --> [cumplir].
verb(verbo([dando])) --> [dando].
verb(verbo([dar])) --> [dar].
verb(verbo([decidir])) --> [decidir].
verb(verbo([decir])) --> [decir].
verb(verbo([declinando])) --> [declinando].

verb(verbo([decodificar])) --> [decodificar].
verb(verbo([decodificando])) --> [decodificando].

verb(verbo([defender])) --> [defender].
verb(verbo([definir])) --> [definir].
verb(verbo([dejando])) --> [dejando].
verb(verbo([demostrar])) --> [demostrar].
verb(verbo([depender])) --> [depender].
verb(verbo([dependiendo])) --> [dependiendo].

verb(verbo([derivar])) --> [derivar].
verb(verbo([derivando])) --> [derivando].

verb(verbo([desarrollando])) --> [desarrollando].
verb(verbo([desarrollar])) --> [desarrollar].
verb(verbo([desasistir])) --> [desasistir].
verb(verbo([descender])) --> [descender].
verb(verbo([describir])) --> [describir].

verb(verbo([describiendo])) --> [describiendo].

verb(verbo([descuidar])) --> [descuidar].
verb(verbo([desgastar])) --> [desgastar].
verb(verbo([desmontar])) --> [desmontar].
verb(verbo([despegar])) --> [despegar].
verb(verbo([desplazar])) --> [desplazar].
verb(verbo([destacando])) --> [destacando].
verb(verbo([destacar])) --> [destacar].

verb(verbo([detectar])) --> [detectar].
verb(verbo([detectando])) --> [detectando].

verb(verbo([destruir])) --> [destruir].
verb(verbo([detectar])) --> [detectar].
verb(verbo([deteriorando])) --> [deteriorando].
verb(verbo([determinando])) --> [determinando].
verb(verbo([diagnosticar])) --> [diagnosticar].
verb(verbo([dictar])) --> [dictar].
verb(verbo([dictando])) --> [dictando].
verb(verbo([difundir])) --> [difundir].

verb(verbo([dirigir])) --> [dirigir].
verb(verbo([dirigiendo])) --> [dirigiendo].

verb(verbo([diseñar])) --> [diseñar].
verb(verbo([disminuir])) --> [disminuir].
verb(verbo([disminuyendo])) --> [disminuyendo].
verb(verbo([distinguir])) --> [distinguir].
verb(verbo([distribuir])) --> [distribuir].
verb(verbo([diversificar])) --> [diversificar].
verb(verbo([dividir])) --> [dividir].
verb(verbo([ejecutar])) --> [ejecutar].
verb(verbo([ejercer])) --> [ejercer].
verb(verbo([elaborar])) --> [elaborar].
verb(verbo([elevar])) --> [elevar].
verb(verbo([eliminando])) --> [eliminando].
verb(verbo([eliminar])) --> [eliminar].
verb(verbo([emigrando])) --> [emigrando].
verb(verbo([empezar])) --> [empezar].
verb(verbo([empleando])) --> [empleando].
verb(verbo([encontrar])) --> [encontrar].
verb(verbo([enfrentar])) --> [enfrentar].

verb(verbo([enlazar])) --> [enlazar].
verb(verbo([enlazando])) --> [enlazando].

verb(verbo([enriquecer])) --> [enriquecer].

verb(verbo([ensamblar])) --> [ensamblar].
verb(verbo([ensamblando])) --> [ensamblando].
verb(verbo([ensayar])) --> [ensayar].
verb(verbo([ensayando])) --> [ensayando].

verb(verbo([entender])) --> [entender].

verb(verbo([entorpecer])) --> [entorpecer].
verb(verbo([entorpeciendo])) --> [entorpeciendo].

verb(verbo([entrando])) --> [entrando].
verb(verbo([entrar])) --> [entrar].
verb(verbo([entregar])) --> [entregar].
verb(verbo([enviando])) --> [enviando].

verb(verbo([envolver])) --> [envolver].
verb(verbo([envolviendo])) --> [envolviendo].


verb(verbo([equilibrar])) --> [equilibrar].

verb(verbo([erigir])) --> [erigir].
verb(verbo([erigiendo])) --> [erigiendo].

verb(verbo([escribir])) --> [escribir].
verb(verbo([esperar])) --> [esperar].

verb(verbo([especificar])) --> [especificar].
verb(verbo([especificando])) --> [especificando].

verb(verbo([estabilizar])) --> [estabilizar].
verb(verbo([establecer])) --> [establecer].
verb(verbo([estableciendo])) --> [estableciendo].
verb(verbo([estar])) --> [estar].
verb(verbo([estimar])) --> [estimar].
verb(verbo([estimular])) --> [estimular].
verb(verbo([estrechar])) --> [estrechar].
verb(verbo([estudiar])) --> [estudiar].
verb(verbo([evaluar])) --> [evaluar].

verb(verbo([evidenciar])) --> [evidenciar].
verb(verbo([evidenciando])) --> [evidenciando].

verb(verbo([evitando])) --> [evitando].
verb(verbo([evitar])) --> [evitar].
verb(verbo([examinando])) --> [examinando].
verb(verbo([examinar])) --> [examinar].
verb(verbo([excluyendo])) --> [excluyendo].
verb(verbo([existiendo])) --> [existiendo].

verb(verbo([expandir])) --> [expandir].
verb(verbo([expandiendo])) --> [expandiendo].

verb(verbo([experimentar])) --> [experimentar].
verb(verbo([explotar])) --> [explotar].
verb(verbo([exportar])) --> [exportar].
verb(verbo([extender])) --> [extender].
verb(verbo([facilitar])) --> [facilitar].
verb(verbo([fermentar])) --> [fermentar].
verb(verbo([fertilizar])) --> [fertilizar].
verb(verbo([fijar])) --> [fijar].
verb(verbo([financiando])) --> [financiando].
verb(verbo([financiar])) --> [financiar].
verb(verbo([fomentar])) --> [fomentar].
verb(verbo([formando])) --> [formando].
verb(verbo([formar])) --> [formar].
verb(verbo([formular])) --> [formular].
verb(verbo([fortalecer])) --> [fortalecer].
verb(verbo([funcionar])) --> [funcionar].
verb(verbo([ganar])) --> [ganar].
verb(verbo([garantizando])) --> [garantizando].
verb(verbo([garantizar])) --> [garantizar].
verb(verbo([generando])) --> [generando].
verb(verbo([generar])) --> [generar].
verb(verbo([gestionando])) --> [gestionando].
verb(verbo([gozando])) --> [gozando].

verb(verbo([guiar])) --> [guiar].
verb(verbo([guiando])) --> [guiando].


verb(verbo([haber])) --> [haber].
verb(verbo([hablar])) --> [hablar].
verb(verbo([hacer])) --> [hacer].
verb(verbo([haciendo])) --> [haciendo].
verb(verbo([identificar])) --> [identificar].
verb(verbo([ilustrar])) --> [ilustrar].
verb(verbo([implementar])) --> [implementar].

verb(verbo([implicar])) --> [implicar].
verb(verbo([implicando])) --> [implicando].

verb(verbo([incentivando])) --> [incentivando].
verb(verbo([incentivar])) --> [incentivar].

verb(verbo([incluir])) --> [incluir].
verb(verbo([incluyendo])) --> [incluyendo].
verb(verbo([incorporando])) --> [incorporando].
verb(verbo([incorporar])) --> [incorporar].
verb(verbo([incrementar])) --> [incrementar].
verb(verbo([incurrir])) --> [incurrir].
verb(verbo([incursionando])) --> [incursionando].
verb(verbo([incursionar])) --> [incursionar].
verb(verbo([indicar])) --> [indicar].

verb(verbo([inducir])) --> [inducir].
verb(verbo([induciendo])) --> [induciendo].

verb(verbo([informar])) --> [informar].

verb(verbo([inhibir])) --> [inhibir].
verb(verbo([inhibiendo])) --> [inhibiendo].
verb(verbo([iniciar])) --> [iniciar].
verb(verbo([iniciando])) --> [iniciando].

verb(verbo([innovar])) --> [innovar].
verb(verbo([intentando])) --> [intentando].

verb(verbo([interactuar])) --> [interactuar].
verb(verbo([interactuando])) --> [interactuando].

verb(verbo([intercalar])) --> [intercalar].
verb(verbo([interpretar])) --> [interpretar].
verb(verbo([interviniendo])) --> [interviniendo].
verb(verbo([inundando])) --> [inundando].
verb(verbo([inventando])) --> [inventando].
verb(verbo([invertir])) --> [invertir].
verb(verbo([investigar])) --> [investigar].

verb(verbo([involucrar])) --> [involucrar].
verb(verbo([involucrando])) --> [involucrando].
verb(verbo([jerarquizar])) --> [jerarquizar].
verb(verbo([jerarquizando])) --> [jerarquizando].

verb(verbo([leer])) --> [leer].
verb(verbo([liderando])) --> [liderando].
verb(verbo([limitar])) --> [limitar].

verb(verbo([llamar])) --> [llamar].
verb(verbo([llamando])) --> [llamando].

verb(verbo([llegando])) --> [llegando].
verb(verbo([llegar])) --> [llegar].
verb(verbo([llevar])) --> [llevar].
verb(verbo([logrando])) --> [logrando].
verb(verbo([lograr])) --> [lograr].
verb(verbo([lugar])) --> [lugar].
verb(verbo([manejar])) --> [manejar].
verb(verbo([manifestando])) --> [manifestando].
verb(verbo([mantener])) --> [mantener].
verb(verbo([manteniendo])) --> [manteniendo].
verb(verbo([maximizar])) --> [maximizar].

verb(verbo([mediar])) --> [mediar].
verb(verbo([mediando])) --> [mediando].

verb(verbo([medir])) --> [medir].
verb(verbo([mejorando])) --> [mejorando].
verb(verbo([mejorar])) --> [mejorar].
verb(verbo([mencionar])) --> [mencionar].
verb(verbo([minando])) --> [minando].
verb(verbo([minimizar])) --> [minimizar].
verb(verbo([modificar])) --> [modificar].

verb(verbo([modular])) --> [modular].
verb(verbo([modulando])) --> [modulando].

verb(verbo([morir])) --> [morir].
verb(verbo([movilizar])) --> [movilizar].
verb(verbo([multiplicar])) --> [multiplicar].
verb(verbo([navegar])) --> [navegar].
verb(verbo([negociar])) --> [negociar].

verb(verbo([negar])) --> [negar].
verb(verbo([negando])) --> [negando].


verb(verbo([notar])) --> [notar].

verb(verbo([nuclear])) --> [nuclear].
verb(verbo([nucleando])) --> [nucleando].

verb(verbo([obligando])) --> [obligando].

verb(verbo([observar])) --> [observar].
verb(verbo([observando])) --> [observando].
verb(verbo([obstaculizar])) --> [obstaculizar].
verb(verbo([obstaculizando])) --> [obstaculizando].
verb(verbo([obstruyendo])) --> [obstruyendo].
verb(verbo([obstruir])) --> [obstruir].


verb(verbo([obtener])) --> [obtener].
verb(verbo([ocurriendo])) --> [ocurriendo].
verb(verbo([ocurrir])) --> [ocurrir].
verb(verbo([ofrecer])) --> [ofrecer].
verb(verbo([olvidando])) --> [olvidando].
verb(verbo([olvidar])) --> [olvidar].
verb(verbo([optar])) --> [optar].
verb(verbo([optimizando])) --> [optimizando].
verb(verbo([optimizar])) --> [optimizar].
verb(verbo([organizar])) --> [organizar].
verb(verbo([orientando])) --> [orientando].
verb(verbo([orientar])) --> [orientar].
verb(verbo([pagar])) --> [pagar].
verb(verbo([parecer])) --> [parecer].
verb(verbo([participar])) --> [participar].
verb(verbo([partir])) --> [partir].
verb(verbo([pasando])) --> [pasando].
verb(verbo([pasar])) --> [pasar].
verb(verbo([peligrar])) --> [peligrar].
verb(verbo([pensar])) --> [pensar].
verb(verbo([perder])) --> [perder].
verb(verbo([perdiendo])) --> [perdiendo].
verb(verbo([perjudicando])) --> [perjudicando].
verb(verbo([permitiendo])) --> [permitiendo].
verb(verbo([permitir])) --> [permitir].
verb(verbo([perseguir])) --> [perseguir].
verb(verbo([pesar])) --> [pesar].
verb(verbo([planificar])) --> [planificar].
verb(verbo([plantear])) --> [plantear].
verb(verbo([poder])) --> [poder].
verb(verbo([popularizar])) --> [popularizar].

verb(verbo([poseer])) --> [poseer].
verb(verbo([poseyendo])) --> [poseyendo].

verb(verbo([posicionar])) --> [posicionar].

verb(verbo([potenciar])) --> [potenciar].
verb(verbo([potenciando])) --> [potenciando].
verb(verbo([preceder])) --> [preceder].
verb(verbo([precediendo])) --> [precediendo].
verb(verbo([precisar])) --> [precisar].
verb(verbo([precisando])) --> [precisando].

verb(verbo([predecir])) --> [predecir].
verb(verbo([predicando])) --> [predicando].
verb(verbo([preparar])) --> [preparar].
verb(verbo([presentar])) --> [presentar].
verb(verbo([preservar])) --> [preservar].
verb(verbo([presionar])) --> [presionar].
verb(verbo([pretender])) --> [pretender].
verb(verbo([prever])) --> [prever].
verb(verbo([primer])) --> [primer].
verb(verbo([proceder])) --> [proceder].
verb(verbo([producir])) --> [producir].
verb(verbo([profundizar])) --> [profundizar].
verb(verbo([prohibiendo])) --> [prohibiendo].
verb(verbo([prohibir])) --> [prohibir].
verb(verbo([prolongar])) --> [prolongar].
verb(verbo([promover])) --> [promover].
verb(verbo([propiciando])) --> [propiciando].

verb(verbo([proponer])) --> [proponer].
verb(verbo([proponiendo])) --> [proponiendo].

verb(verbo([proteger])) --> [proteger].
verb(verbo([proveer])) --> [proveer].

verb(verbo([proveniendo])) --> [proveniendo].
verb(verbo([provenir])) --> [provenir].

verb(verbo([provocar])) --> [provocar].
verb(verbo([quedando])) --> [quedando].
verb(verbo([quedar])) --> [quedar].
verb(verbo([reactivar])) --> [reactivar].
verb(verbo([reafirmar])) --> [reafirmar].
verb(verbo([reajustando])) --> [reajustando].
verb(verbo([realizando])) --> [realizando].
verb(verbo([realizar])) --> [realizar].
verb(verbo([recabar])) --> [recabar].
verb(verbo([recibir])) --> [recibir].

verb(verbo([reclutar])) --> [reclutar].
verb(verbo([reclutando])) --> [reclutando].

verb(verbo([recobrar])) --> [recobrar].
verb(verbo([recogiendo])) --> [recogiendo].
verb(verbo([reconocer])) --> [reconocer].
verb(verbo([recuperar])) --> [recuperar].
verb(verbo([recurrir])) --> [recurrir].
verb(verbo([redefinir])) --> [redefinir].
verb(verbo([redituar])) --> [redituar].
verb(verbo([reduciendo])) --> [reduciendo].
verb(verbo([reducir])) --> [reducir].
verb(verbo([redundando])) --> [redundando].
verb(verbo([reemplazar])) --> [reemplazar].
verb(verbo([reencontrar])) --> [reencontrar].

verb(verbo([reestaurar])) --> [reestaurar].
verb(verbo([reestaurando])) --> [reestaurando].

verb(verbo([referir])) --> [referir].
verb(verbo([reforzar])) --> [reforzar].
verb(verbo([regar])) --> [regar].
verb(verbo([registrando])) --> [registrando].
verb(verbo([registrar])) --> [registrar].

%%Verbo agregado por DN, regular
verb(verbo([regular])) --> [regular].
verb(verbo([regulando])) --> [regulando].

verb(verbo([relativizar])) --> [relativizar].
verb(verbo([remontar])) --> [remontar].
verb(verbo([remunerar])) --> [remunerar].
verb(verbo([renovar])) --> [renovar].
verb(verbo([repercutiendo])) --> [repercutiendo].

verb(verbo([repetir])) --> [repetir].
verb(verbo([repitiendo])) --> [repitiendo].

verb(verbo([reportar])) --> [reportar].
verb(verbo([represar])) --> [represar].

verb(verbo([representar])) --> [representar].
verb(verbo([representando])) --> [representando].
verb(verbo([reprimir])) --> [reprimir].
verb(verbo([reprimiendo])) --> [reprimiendo].

verb(verbo([requiriendo])) --> [requiriendo].
verb(verbo([requerir])) --> [requerir].
verb(verbo([resaltar])) --> [resaltar].
verb(verbo([rescatar])) --> [rescatar].
verb(verbo([resolver])) --> [resolver].
verb(verbo([respetar])) --> [respetar].
verb(verbo([responder])) --> [responder].
verb(verbo([restablecer])) --> [restablecer].
verb(verbo([resultar])) --> [resultar].
verb(verbo([retirar])) --> [retirar].

verb(verbo([revelar])) --> [revelar].
verb(verbo([revelando])) --> [revelando].

verb(verbo([revertir])) --> [revertir].
verb(verbo([saber])) --> [saber].
verb(verbo([saliendo])) --> [saliendo].
verb(verbo([sancionar])) --> [sancionar].
verb(verbo([satisfacer])) --> [satisfacer].

%% Verbo agregado por DN, secuenciar
verb(verbo([secuenciar])) --> [secuenciar].
verb(verbo([secuenciando])) --> [secuenciando].

verb(verbo([segmentar])) --> [segmentar].
verb(verbo([seguir])) --> [seguir].

verb(verbo([seleccionando])) --> [seleccionando].
verb(verbo([seleccionar])) --> [seleccionar].
verb(verbo([sembrar])) --> [sembrar].
verb(verbo([señalar])) --> [señalar].
verb(verbo([ser])) --> [ser].
verb(verbo([siendo])) --> [siendo].
verb(verbo([siguiendo])) --> [siguiendo].
verb(verbo([simplificando])) --> [simplificando].

verb(verbo([sintetizar])) --> [sintetizar].
verb(verbo([sintetizando])) --> [sintetizando].
verb(verbo([situar])) --> [situar].
verb(verbo([situando])) --> [situando].


verb(verbo([sobrevivir])) --> [sobrevivir].
verb(verbo([solicitar])) --> [solicitar].
verb(verbo([soslayar])) --> [soslayar].
verb(verbo([sostener])) --> [sostener].
verb(verbo([subsidiar])) --> [subsidiar].

verb(verbo([substituir])) --> [substituir].
verb(verbo([subtituyendo])) --> [substituyendo].
verb(verbo([sugerir])) --> [sugerir].
verb(verbo([sugiriendo])) --> [sugiriendo].

verb(verbo([sumar])) --> [sumar].
verb(verbo([suministrar])) --> [suministrar].
verb(verbo([superar])) --> [superar].
verb(verbo([surgiendo])) --> [surgiendo].
verb(verbo([surgir])) --> [surgir].
verb(verbo([sustituyendo])) --> [sustituyendo].
verb(verbo([tener])) --> [tener].
verb(verbo([tercer])) --> [tercer].
verb(verbo([tomando])) --> [tomando].
verb(verbo([tomar])) --> [tomar].
verb(verbo([trabajar])) --> [trabajar].
verb(verbo([traducir])) --> [traducir].
verb(verbo([traer])) --> [traer].
verb(verbo([tranquilizar])) --> [tranquilizar].

verb(verbo([transcribir])) --> [transcribir].
verb(verbo([transcribiendose])) --> [transcribiendose].

verb(verbo([transmitir])) --> [transmitir].
verb(verbo([trasladar])) --> [trasladar].
verb(verbo([tratar])) --> [tratar].
verb(verbo([ubicar])) --> [ubicar].
verb(verbo([unir])) --> [unir].
verb(verbo([usando])) --> [usando].
verb(verbo([utilizando])) --> [utilizando].
verb(verbo([utilizar])) --> [utilizar].
verb(verbo([valer])) --> [valer].
verb(verbo([velar])) --> [velar].
verb(verbo([vender])) --> [vender].
verb(verbo([vendiendo])) --> [vendiendo].
verb(verbo([ver])) --> [ver].
verb(verbo([viendo])) --> [viendo].
verb(verbo([vigilar])) --> [vigilar].
verb(verbo([vinculando])) --> [vinculando].
verb(verbo([vincular])) --> [vincular].
verb(verbo([viniendo])) --> [viniendo].
verb(verbo([vivir])) --> [vivir].
verb(verbo([volver])) --> [volver].

% verbos pasivos: arse,irse,erse,dose

verb(verbo([agruparse])) --> [agruparse].
verb(verbo([aparecerse])) --> [aparecerse].

verb(verbo([aplicarse])) --> [aplicarse].
verb(verbo([apoyarse])) --> [apoyarse].
verb(verbo([apreciarse])) --> [apreciarse].

verb(verbo([aproximarse])) --> [aproximarse].
verb(verbo([apuntarse])) --> [apuntarse].
verb(verbo([asociarse])) --> [asociarse].

verb(verbo([aventurarse])) --> [aventurarse].
verb(verbo([basándose])) --> [basándose].
verb(verbo([bloquearse])) --> [bloquearse].
verb(verbo([carecerse])) --> [carecerse].
verb(verbo([coactivarse])) --> [coactivarse].
verb(verbo([codificarse])) --> [codificarse].
verb(verbo([compartirse])) --> [compartirse].
verb(verbo([componerse])) --> [componerse].
verb(verbo([comprenderse])) --> [comprenderse].


verb(verbo([concluirse])) --> [concluirse].
verb(verbo([conducirse])) --> [conducirse].



verb(verbo([considerarse])) --> [considerarse].

verb(verbo([contenerse])) --> [contenerse].

verb(verbo([convertirse])) --> [convertirse].

verb(verbo([decirse])) --> [decirse].
verb(verbo([decodificarse])) --> [decodificarse].

verb(verbo([dedicarse])) --> [dedicarse].
verb(verbo([definirse])) --> [definirse].
verb(verbo([denominarse])) --> [denominarse].
verb(verbo([deprimirse])) --> [deprimirse].
verb(verbo([derivarse])) --> [derivarse].
verb(verbo([desarrollarse])) --> [desarrollarse].
verb(verbo([describirse])) --> [describirse].
verb(verbo([desintegrarse])) --> [desintegrarse].
verb(verbo([destinándose])) --> [destinándose].
verb(verbo([detectarse])) --> [detectarse].
verb(verbo([dirigirse])) --> [dirigirse].
verb(verbo([diferenciarse])) --> [diferenciarse].
verb(verbo([diversificándose])) --> [diversificándose].
verb(verbo([dividiéndose])) --> [dividiéndose].
verb(verbo([duplicarse])) --> [duplicarse].
verb(verbo([encargarse])) --> [encargarse].
verb(verbo([enfrentarse])) --> [enfrentarse].
verb(verbo([enlazarse])) --> [enlazarse].
verb(verbo([ensamblarse])) --> [ensamblarse].

verb(verbo([entregarse])) --> [entregarse].
verb(verbo([envolverse])) --> [envolverse].

verb(verbo([esforzarse])) --> [esforzarse].
verb(verbo([especializándose])) --> [especializándose].
verb(verbo([especializarse])) --> [especializarse].

verb(verbo([erigirse])) --> [erigirse].

verb(verbo([establecerse])) --> [establecerse].

verb(verbo([evidenciarse])) --> [evidenciarse].
verb(verbo([expandirse])) --> [expandirse].
verb(verbo([exponerse])) --> [exponerse].
verb(verbo([formándose])) --> [formándose].
verb(verbo([fortalecerse])) --> [fortalecerse].
verb(verbo([fumarse])) --> [fumarse].
verb(verbo([hacerse])) --> [hacerse].
verb(verbo([incluirse])) --> [incluirse].
verb(verbo([incorporándose])) --> [incorporándose].
verb(verbo([inhibirse])) --> [inhibirse].
verb(verbo([iniciarse])) --> [iniciarse].
verb(verbo([inquietarse])) --> [inquietarse].
verb(verbo([instalarse])) --> [instalarse].
verb(verbo([introducirse])) --> [introducirse].
verb(verbo([involucrarse])) --> [involucrarse].
verb(verbo([mantenerse])) --> [mantenerse].
verb(verbo([manteniéndose])) --> [manteniéndose].
verb(verbo([obstruirse])) --> [obstruirse].
verb(verbo([optarse])) --> [optarse].
verb(verbo([organizarse])) --> [organizarse].
verb(verbo([ponerse])) --> [ponerse].
verb(verbo([preguntarse])) --> [preguntarse].
verb(verbo([preocuparse])) --> [preocuparse].
verb(verbo([prohibirse])) --> [prohibirse].
verb(verbo([prolongarse])) --> [prolongarse].
verb(verbo([protegerse])) --> [protegerse].
verb(verbo([proveerse])) --> [proveerse].
verb(verbo([reconvertirse])) --> [reconvertirse].
verb(verbo([recuperarse])) --> [recuperarse].
verb(verbo([reducirse])) --> [reducirse].
verb(verbo([reestaurarse])) --> [reestaurarse].
verb(verbo([refiriéndose])) --> [refiriéndose].

verb(verbo([regularse])) --> [regularse].
verb(verbo([repetirse])) --> [repetirse].
verb(verbo([replantearse])) --> [replantearse].
verb(verbo([reprimirse])) --> [reprimirse].
verb(verbo([restringirse])) --> [restringirse].
verb(verbo([retirarse])) --> [retirarse].
verb(verbo([revelarse])) --> [revelarse].
verb(verbo([secuenciarse])) --> [secuenciarse].

verb(verbo([señalarse])) --> [señalarse].
verb(verbo([situarse])) --> [situarse].
verb(verbo([substituirse])) --> [substituirse].
verb(verbo([superarse])) --> [superarse].
verb(verbo([tomarse])) --> [tomarse].

% verbos irregulares obtenidos manualmente

verb(verbo([comenzó])) --> [comenzó].
verb(verbo([identifica])) --> [identifica].
verb(verbo([origina])) --> [origina].
verb(verbo([aparece])) --> [aparece].
verb(verbo([crea])) --> [crea].
verb(verbo([formó])) --> [formó].
verb(verbo([provocó])) --> [provocó].
verb(verbo([realizó])) --> [realizó].
verb(verbo([concedió])) --> [concedió].
verb(verbo([permite])) --> [permite].
verb(verbo([consolidó])) --> [consolidó].
verb(verbo([transforma])) --> [transforma].
verb(verbo([ejecutó])) --> [ejecutó].
verb(verbo([extendió])) --> [extendió].
verb(verbo([consistía])) --> [consistía].
verb(verbo([desarrollaron])) --> [desarrollaron].
verb(verbo([diseñó])) --> [diseñó].
verb(verbo([promovía])) --> [promovía].
verb(verbo([orientó])) --> [orientó].
verb(verbo([realizaba])) --> [realizaba].
verb(verbo([realizaban])) --> [realizaban].
verb(verbo([efectuó])) --> [efectuó].
verb(verbo([transportaba])) --> [transportaba].
verb(verbo([establecía])) --> [establecía].
verb(verbo([efectuaban])) --> [efectuaban].
verb(verbo([ofrecía])) --> [ofrecía].
verb(verbo([destinaba])) --> [destinaba].
verb(verbo([controlaba])) --> [controlaba].
verb(verbo([mantuvo])) --> [mantuvo].
verb(verbo([logró])) --> [logró].
verb(verbo([localizaron])) --> [localizaron].
verb(verbo([consiste])) --> [consiste].
verb(verbo([rompió])) --> [rompió].
verb(verbo([fijaba])) --> [fijaba].
verb(verbo([lograban])) --> [lograban].
verb(verbo([obtenía])) --> [obtenía].
verb(verbo([superó])) --> [superó].
verb(verbo([trasladaron])) --> [trasladaron].
verb(verbo([ocasionó])) --> [ocasionó].
verb(verbo([manifestó])) --> [manifestó].
verb(verbo([sostuvo])) --> [sostuvo].
verb(verbo([dependían])) --> [dependían].
verb(verbo([correspondía])) --> [correspondía].
verb(verbo([correspondían])) --> [correspondían].
verb(verbo([dependía])) --> [dependía].
verb(verbo([existía])) --> [existía].
verb(verbo([existían])) --> [existían].
verb(verbo([desconocían])) --> [desconocían].
verb(verbo([dinamizaría])) --> [dinamizaría].
verb(verbo([varían])) --> [varían].
verb(verbo([varía])) --> [varía].
verb(verbo([manejaba])) --> [manejaba].
verb(verbo([permitía])) --> [permitía].
verb(verbo([comienza])) --> [comienza].
verb(verbo([reveló])) --> [reveló].
verb(verbo([contraer])) --> [contraer].
verb(verbo([ocurrido])) --> [ocurrido].
verb(verbo([funciona])) --> [funciona].
verb(verbo([mantiene])) --> [mantiene].
verb(verbo([ganandose])) --> [ganandose].
verb(verbo([gana])) --> [gana].
verb(verbo([gano])) --> [gano].
verb(verbo([pierde])) --> [pierde].
% verb(verbo([sugerido])) --> [sugerido].
% verb(verbo([ejecutado])) --> [ejecutado].
verb(verbo([ejecutando])) --> [ejecutando].
verb(verbo([encontrando])) --> [encontrando].
verb(verbo([encontro])) --> [encontro].
verb(verbo([proporcionar])) --> [proporcionar].
% verb(verbo([proporcionado])) --> [proporcionado].
verb(verbo([proporcionando])) --> [proporcionando].
verb(verbo([proporciono])) --> [proporciono].
verb(verbo([proporciona])) --> [proporciona].
% verb(verbo([revelado])) --> [revelado].
verb(verbo([revelo])) --> [revelo].
verb(verbo([revela])) --> [revela].
verb(verbo([reporta])) --> [reporta].
verb(verbo([reportando])) --> [reportando].
% verb(verbo([asegurado])) --> [asegurado].
verb(verbo([asegura])) --> [asegura].
% verb(verbo([inhibido])) --> [inhibido].
verb(verbo([inhibe])) --> [inhibe].
verb(verbo([inhibio])) --> [inhibio].
verb(verbo([actua])) --> [actua].
verb(verbo([actuando])) --> [actuando].
% verb(verbo([actuado])) --> [actuado].
verb(verbo([actuo])) --> [actuo].
verb(verbo([regula])) --> [regula].
% verb(verbo([regulado])) --> [regulado].
verb(verbo([estimula])) --> [estimula].
% verb(verbo([estimulado])) --> [estimulado].
verb(verbo([estimulando])) --> [estimulando].
verb(verbo([liberar])) --> [liberar].
verb(verbo([liberando])) --> [liberando].
% verb(verbo([liberado])) --> [liberado].
verb(verbo([libera])) --> [libera].
verb(verbo([activar])) --> [activar].
verb(verbo([activa])) --> [activa].
verb(verbo([activando])) --> [activando].
% verb(verbo([activado])) --> [activado].
verb(verbo([interactua])) --> [interactua].
verb(verbo([responde])) --> [responde].
verb(verbo([respondiendo])) --> [respondiendo].
verb(verbo([promueve])) --> [promueve].
verb(verbo([promoviendo])) --> [promoviendo].
verb(verbo([provoca])) --> [provoca].
verb(verbo([provocando])) --> [provocando].


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% verbos binario - auxiliares 
% conjugacion de los verbos tener, deber, poder
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

verb(verbo([deber])) --> [deber].
verb(verbo([debiendo])) --> [debiendo].
verb(verbo([debo])) --> [debo].
verb(verbo([debes])) --> [debes].
verb(verbo([debe])) --> [debe].
verb(verbo([debemos])) --> [debemos].
verb(verbo([debéis])) --> [debéis].
verb(verbo([deben])) --> [deben].
verb(verbo([debía])) --> [debía].
verb(verbo([debías])) --> [debías].
verb(verbo([debía])) --> [debía].
verb(verbo([debíamos])) --> [debíamos].
verb(verbo([debíais])) --> [debíais].
verb(verbo([debían])) --> [debían].
verb(verbo([debí])) --> [debí].
verb(verbo([debiste])) --> [debiste].
verb(verbo([debió])) --> [debió].
verb(verbo([debimos])) --> [debimos].
verb(verbo([debisteis])) --> [debisteis].
verb(verbo([debieron])) --> [debieron].
verb(verbo([deberé])) --> [deberé].
verb(verbo([deberás])) --> [deberás].
verb(verbo([deberá])) --> [deberá].
verb(verbo([deberemos])) --> [deberemos].
verb(verbo([deberéis])) --> [deberéis].
verb(verbo([deberán])) --> [deberán].
verb(verbo([debería])) --> [debería].
verb(verbo([deberías])) --> [deberías].
verb(verbo([debería])) --> [debería].
verb(verbo([deberíamos])) --> [deberíamos].
verb(verbo([deberíais])) --> [deberíais].
verb(verbo([deberían])) --> [deberían].
verb(verbo([deba])) --> [deba].
verb(verbo([debas])) --> [debas].
verb(verbo([debamos])) --> [debamos].
verb(verbo([debáis])) --> [debáis].
verb(verbo([deban])) --> [deban].
verb(verbo([debiera])) --> [debiera].
verb(verbo([debiese])) --> [debiese].
verb(verbo([debieras])) --> [debieras].
verb(verbo([debieses])) --> [debieses].
verb(verbo([debiera])) --> [debiera].
verb(verbo([debiese])) --> [debiese].
verb(verbo([debiéramos])) --> [debiéramos].
verb(verbo([debiésemos])) --> [debiésemos].
verb(verbo([debierais])) --> [debierais].
verb(verbo([debieseis])) --> [debieseis].
verb(verbo([debieran])) --> [debieran].
verb(verbo([debiesen])) --> [debiesen].
verb(verbo([debiere])) --> [debiere].
verb(verbo([debieres])) --> [debieres].
verb(verbo([debiere])) --> [debiere].
verb(verbo([debiéremos])) --> [debiéremos].
verb(verbo([debiereis])) --> [debiereis].
verb(verbo([debieren])) --> [debieren].
verb(verbo([tener])) --> [tener].
verb(verbo([teniendo])) --> [teniendo].
verb(verbo([tengo])) --> [tengo].
verb(verbo([tienes])) --> [tienes].
verb(verbo([tiene])) --> [tiene].
verb(verbo([tenemos])) --> [tenemos].
verb(verbo([tenéis])) --> [tenéis].
verb(verbo([tienen])) --> [tienen].
verb(verbo([tenía])) --> [tenía].
verb(verbo([tenías])) --> [tenías].
verb(verbo([tenía])) --> [tenía].
verb(verbo([teníamos])) --> [teníamos].
verb(verbo([teníais])) --> [teníais].
verb(verbo([tenían])) --> [tenían].
verb(verbo([tuve])) --> [tuve].
verb(verbo([tuviste])) --> [tuviste].
verb(verbo([tuvo])) --> [tuvo].
verb(verbo([tuvimos])) --> [tuvimos].
verb(verbo([tuvisteis])) --> [tuvisteis].
verb(verbo([tuvieron])) --> [tuvieron].
verb(verbo([tendré])) --> [tendré].
verb(verbo([tendrás])) --> [tendrás].
verb(verbo([tendrá])) --> [tendrá].
verb(verbo([tendremos])) --> [tendremos].
verb(verbo([tendréis])) --> [tendréis].
verb(verbo([tendrán])) --> [tendrán].
verb(verbo([tendría])) --> [tendría].
verb(verbo([tendrías])) --> [tendrías].
verb(verbo([tendría])) --> [tendría].
verb(verbo([tendríamos])) --> [tendríamos].
verb(verbo([tendríais])) --> [tendríais].
verb(verbo([tendrían])) --> [tendrían].
verb(verbo([tenga])) --> [tenga].
verb(verbo([tengas])) --> [tengas].
verb(verbo([tenga])) --> [tenga].
verb(verbo([tengamos])) --> [tengamos].
verb(verbo([tengáis])) --> [tengáis].
verb(verbo([tengan])) --> [tengan].
verb(verbo([tuviera])) --> [tuviera].
verb(verbo([tuviese])) --> [tuviese].
verb(verbo([tuvieras])) --> [tuvieras].
verb(verbo([tuvieses])) --> [tuvieses].
verb(verbo([tuviera])) --> [tuviera].
verb(verbo([tuviese])) --> [tuviese].
verb(verbo([tuviéramos])) --> [tuviéramos].
verb(verbo([tuviésemos])) --> [tuviésemos].
verb(verbo([tuvierais])) --> [tuvierais].
verb(verbo([tuvieseis])) --> [tuvieseis].
verb(verbo([tuvieran])) --> [tuvieran].
verb(verbo([tuviesen])) --> [tuviesen].
verb(verbo([tuviere])) --> [tuviere].
verb(verbo([tuvieres])) --> [tuvieres].
verb(verbo([tuviere])) --> [tuviere].
verb(verbo([tuviéremos])) --> [tuviéremos].
verb(verbo([tuviereis])) --> [tuviereis].
verb(verbo([tuvieren])) --> [tuvieren].
verb(verbo([poder])) --> [poder].
verb(verbo([pudiendo])) --> [pudiendo].
verb(verbo([puedo])) --> [puedo].
verb(verbo([puedes])) --> [puedes].
verb(verbo([puede])) --> [puede].
verb(verbo([podemos])) --> [podemos].
verb(verbo([podéis])) --> [podéis].
verb(verbo([pueden])) --> [pueden].
verb(verbo([podía])) --> [podía].
verb(verbo([podías])) --> [podías].
verb(verbo([podía])) --> [podía].
verb(verbo([podíamos])) --> [podíamos].
verb(verbo([podíais])) --> [podíais].
verb(verbo([podían])) --> [podían].
verb(verbo([pude])) --> [pude].
verb(verbo([pudiste])) --> [pudiste].
verb(verbo([pudo])) --> [pudo].
verb(verbo([pudimos])) --> [pudimos].
verb(verbo([pudisteis])) --> [pudisteis].
verb(verbo([pudieron])) --> [pudieron].
verb(verbo([podré])) --> [podré].
verb(verbo([podrás])) --> [podrás].
verb(verbo([podrá])) --> [podrá].
verb(verbo([podremos])) --> [podremos].
verb(verbo([podréis])) --> [podréis].
verb(verbo([podrán])) --> [podrán].
verb(verbo([podría])) --> [podría].
verb(verbo([podrías])) --> [podrías].
verb(verbo([podría])) --> [podría].
verb(verbo([podríamos])) --> [podríamos].
verb(verbo([podríais])) --> [podríais].
verb(verbo([podrían])) --> [podrían].
verb(verbo([pueda])) --> [pueda].
verb(verbo([puedas])) --> [puedas].
verb(verbo([pueda])) --> [pueda].
verb(verbo([podamos])) --> [podamos].
verb(verbo([podáis])) --> [podáis].
verb(verbo([puedan])) --> [puedan].
verb(verbo([pudiera])) --> [pudiera].
verb(verbo([pudiese])) --> [pudiese].
verb(verbo([pudieras])) --> [pudieras].
verb(verbo([pudieses])) --> [pudieses].
verb(verbo([pudiera])) --> [pudiera].
verb(verbo([pudiese])) --> [pudiese].
verb(verbo([pudiéramos])) --> [pudiéramos].
verb(verbo([pudiésemos])) --> [pudiésemos].
verb(verbo([pudierais])) --> [pudierais].
verb(verbo([pudieseis])) --> [pudieseis].
verb(verbo([pudieran])) --> [pudieran].
verb(verbo([pudiesen])) --> [pudiesen].
verb(verbo([pudiere])) --> [pudiere].
verb(verbo([pudieres])) --> [pudieres].
verb(verbo([pudiere])) --> [pudiere].
verb(verbo([pudiéremos])) --> [pudiéremos].
verb(verbo([pudiereis])) --> [pudiereis].
verb(verbo([pudieren])) --> [pudieren].
verb(verbo([love])) --> [love].
verb(verbo([have])) --> [have].

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% conjugacion de verbos en ingles
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

verb(verbo([maintain])) --> [maintain].
verb(verbo([maintains])) --> [maintains].
verb(verbo([maintained])) --> [maintained].
verb(verbo([maintaining])) --> [maintaining].
verb(verbo([gain])) --> [gain].
verb(verbo([gains])) --> [gains].
verb(verbo([gained])) --> [gaining].
verb(verbo([loss])) --> [loss].
verb(verbo([losses])) --> [losses].
verb(verbo([lossed])) --> [lossed].
verb(verbo([lossing])) --> [lossing].
verb(verbo([suggest])) --> [suggest].
verb(verbo([suggests])) --> [suggests].
verb(verbo([suggested])) --> [suggested].
verb(verbo([suggesting])) --> [suggesting].
verb(verbo([execute])) --> [execute].
verb(verbo([executes])) --> [executes].
verb(verbo([executed])) --> [executed].
verb(verbo([executing])) --> [executing].
verb(verbo([find])) --> [find].
verb(verbo([finds])) --> [finds].
verb(verbo([found])) --> [found].
verb(verbo([finding])) --> [finding].
verb(verbo([provide])) --> [provide].
verb(verbo([provides])) --> [provides].
verb(verbo([provided])) --> [provided].
verb(verbo([providing])) --> [providing].
verb(verbo([recruit])) --> [recruit].
verb(verbo([recruits])) --> [recruits].
verb(verbo([recruited])) --> [recruited].
verb(verbo([recruiting])) --> [recruiting].
verb(verbo([reveal])) --> [reveal].
verb(verbo([reveals])) --> [reveals].
verb(verbo([revealed])) --> [revealing].
verb(verbo([report])) --> [report].
verb(verbo([reports])) --> [reports].
verb(verbo([reported])) --> [reported].
verb(verbo([reporting])) --> [reporting].
verb(verbo([ensure])) --> [ensure].
verb(verbo([ensures])) --> [ensures].
verb(verbo([ensured])) --> [ensured].
verb(verbo([ensuring])) --> [ensuring].
verb(verbo([release])) --> [release].
verb(verbo([releases])) --> [releases].
verb(verbo([released])) --> [released].
verb(verbo([releasing])) --> [releasing].
verb(verbo([arrest])) --> [arrest].
verb(verbo([arrests])) --> [arrests].
verb(verbo([arrested])) --> [arrested].
verb(verbo([arresting])) --> [arresting].
verb(verbo([bind])) --> [bind].
verb(verbo([binds])) --> [binds].
verb(verbo([bound])) --> [bound].
verb(verbo([binding])) --> [binding].
verb(verbo([recognize])) --> [recognize].
verb(verbo([recognizes])) --> [recognizes].
verb(verbo([recognized])) --> [recognized].
verb(verbo([recognizing])) --> [recognizing].
verb(verbo([prevent])) --> [prevent].
verb(verbo([prevents])) --> [prevents].
verb(verbo([prevented])) --> [prevented].
verb(verbo([preventing])) --> [preventing].
verb(verbo([lead])) --> [lead].
verb(verbo([leads])) --> [leads].
verb(verbo([led])) --> [led].
verb(verbo([leading])) --> [leading].
verb(verbo([participate])) --> [participate].
verb(verbo([participates])) --> [participates].
verb(verbo([participated])) --> [participated].
verb(verbo([participating])) --> [participating].
verb(verbo([involve])) --> [involve].
verb(verbo([involves])) --> [involves].
verb(verbo([involved])) --> [involved].
verb(verbo([involving])) --> [involving].
verb(verbo([trigger])) --> [trigger].
verb(verbo([triggers])) --> [triggers].
verb(verbo([triggered])) --> [triggered].
verb(verbo([triggering])) --> [triggering].
verb(verbo([target])) --> [target].
verb(verbo([targets])) --> [targets].
verb(verbo([targeted])) --> [targeted].
verb(verbo([targeting])) --> [targeting].
verb(verbo([express])) --> [express].
verb(verbo([expresses])) --> [expresses].
verb(verbo([expressed])) --> [expressed].
verb(verbo([expressing])) --> [expressing].
verb(verbo([increase])) --> [increase].
verb(verbo([increases])) --> [increases].
verb(verbo([increased])) --> [increased].
verb(verbo([increasing])) --> [increasing].
verb(verbo([translate])) --> [translate].
verb(verbo([translates])) --> [translates].
verb(verbo([translated])) --> [translated].
verb(verbo([translating])) --> [translating].
verb(verbo([transcribe])) --> [transcribe].
verb(verbo([transcribes])) --> [transcribes].
verb(verbo([transcribed])) --> [transcribed].
verb(verbo([transcribing])) --> [transcribing].
verb(verbo([induce])) --> [induce].
verb(verbo([induces])) --> [induces].
verb(verbo([induced])) --> [induced].
verb(verbo([inducing])) --> [inducing].
verb(verbo([enhance])) --> [enhance].
verb(verbo([enhances])) --> [enhances].
verb(verbo([enhanced])) --> [enhanced].
verb(verbo([enhancing])) --> [enhancing].
verb(verbo([suppress])) --> [suppress].
verb(verbo([suppresses])) --> [suppresses].
verb(verbo([suppressed])) --> [suppressed].
verb(verbo([suppressing])) --> [suppressing].
verb(verbo([retain])) --> [retain].
verb(verbo([retains])) --> [retains].
verb(verbo([retained])) --> [retained].
verb(verbo([retaining])) --> [retaining].
verb(verbo([reactivate])) --> [reactivate].
verb(verbo([reactivates])) --> [reactivates].
verb(verbo([reactivated])) --> [reactivated].
verb(verbo([reactivating])) --> [reactivating].
verb(verbo([activate])) --> [activate].
verb(verbo([activates])) --> [activates].
verb(verbo([activated])) --> [activated].
% agregado 2015-08-25
verb(verbo([phosphorylate])) --> [phosphorylate].
verb(verbo([phosphorylates])) --> [phosphorylates].
verb(verbo([phosphorylated])) --> [phosphorylated].
verb(verbo([phosphorylating])) --> [phosphorylating].
% agregado 2015-08-28
verb(verbo([trimerize])) --> [trimerize].
verb(verbo([trimerizes])) --> [trimerizes].
verb(verbo([trimerized])) --> [trimerized].
verb(verbo([trimerizing])) --> [trimerizing].
verb(verbo([heterodimerize])) --> [heterodimerize].
verb(verbo([heterodimerizes])) --> [heterodimerizes].
verb(verbo([heterodimerized])) --> [heterodimerized].
verb(verbo([heterodimerizing])) --> [heterodimerizing].
verb(verbo([associate])) --> [associate].
verb(verbo([associates])) --> [associates].
verb(verbo([associated])) --> [associated].
verb(verbo([associating])) --> [associating].
verb(verbo([activating])) --> [activating].
verb(verbo([dimerize])) --> [dimerize].
verb(verbo([dimerizes])) --> [dimerizes].
verb(verbo([dimerized])) --> [dimerized].
verb(verbo([dimerizing])) --> [dimerizing].
verb(verbo([transcriptional-activate])) --> [transcriptional,'-',activate].
verb(verbo([transcriptional-activates])) --> [transcriptional,'-',activates].
verb(verbo([transcriptional-activated])) --> [transcriptional,'-', activated].
verb(verbo([transcriptional-activating])) --> [transcriptional, '-', activating].
verb(verbo([up-regulate])) --> [up, '-', regulate].
verb(verbo([up-regulates])) --> [up, '-', regulates].
verb(verbo([up-regulated])) --> [up,'-', regulated].
verb(verbo([up-regulating])) --> [up,'-',regulating].
verb(verbo([down-regulate])) --> [down, '-', regulate].
verb(verbo([down-regulates])) --> [down, '-', regulates].
verb(verbo([down-regulated])) --> [down, '-', regulated].
verb(verbo([down-regulating])) --> [down, '-', regulating].
% fin de agregados en 2015-08-28
verb(verbo([activating])) --> [activating].
verb(verbo([conserve])) --> [conserve].
verb(verbo([conserves])) --> [conserves].
verb(verbo([conserved])) --> [conserved].
verb(verbo([conserving])) --> [conserving].
verb(verbo([inhibit])) --> [inhibit].
verb(verbo([inhibits])) --> [inhibits].
verb(verbo([inhibited])) --> [inhibited].
verb(verbo([inhibiting])) --> [inhibiting].
verb(verbo([act])) --> [act].
verb(verbo([acts])) --> [acts].
verb(verbo([acted])) --> [acted].
verb(verbo([acting])) --> [acting].
verb(verbo([increase])) --> [increase].
verb(verbo([increases])) --> [increases].
verb(verbo([increased])) --> [increased].
verb(verbo([increasing])) --> [increasing].
verb(verbo([confer])) --> [confer].
verb(verbo([confers])) --> [confers].
verb(verbo([conferred])) --> [conferred].
verb(verbo([conferring])) --> [conferring].
verb(verbo([decrease])) --> [decrease].
verb(verbo([decreases])) --> [decreases].
verb(verbo([decreased])) --> [decreased].
verb(verbo([decreasing])) --> [decreasing].
verb(verbo([regulate])) --> [regulate].
verb(verbo([regulates])) --> [regulates].
verb(verbo([regulated])) --> [regulated].
verb(verbo([regulating])) --> [regulating].
verb(verbo([provoke])) --> [provoke].
verb(verbo([provokes])) --> [provokes].
verb(verbo([provoked])) --> [provoked].
verb(verbo([provoking])) --> [provoking].
verb(verbo([modulate])) --> [modulate].
verb(verbo([modulates])) --> [modulates].
verb(verbo([modulated])) --> [modulated].
verb(verbo([modulating])) --> [modulating].
verb(verbo([cause])) --> [cause].
verb(verbo([causes])) --> [causes].
verb(verbo([caused])) --> [caused].
verb(verbo([causing])) --> [causing].
verb(verbo([require])) --> [require].
verb(verbo([requires])) --> [requires].
verb(verbo([required])) --> [required].
verb(verbo([requiring])) --> [requiring].
verb(verbo([promote])) --> [promote].
verb(verbo([promotes])) --> [promotes].
verb(verbo([promoted])) --> [promoted].
verb(verbo([promoting])) --> [promoting].
verb(verbo([interact])) --> [interact].
verb(verbo([interacts])) --> [interacts].
verb(verbo([interacted])) --> [interacted].
verb(verbo([interacting])) --> [interacting].
verb(verbo([mediate])) --> [mediate].
verb(verbo([mediates])) --> [mediates].
verb(verbo([mediated])) --> [mediated].
verb(verbo([mediating])) --> [mediating].
verb(verbo([respond])) --> [respond].
verb(verbo([responds])) --> [responds].
verb(verbo([responded])) --> [responded].
verb(verbo([responding])) --> [responding].
verb(verbo([stimulate])) --> [stimulate].
verb(verbo([stimulates])) --> [stimulates].
verb(verbo([stimulated])) --> [stimulated].
verb(verbo([stimulating])) --> [stimulating].

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% verbos participios regulares 
% llamados participios pasivos terminados en 
% los sufijos ido y ado
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

verb_part(verbo([acometido])) --> [acometido].
verb_part(verbo([acompañado])) --> [acompañado].
verb_part(verbo([adecuado])) --> [adecuado].
verb_part(verbo([adelantado])) --> [adelantado].
verb_part(verbo([adherido])) --> [adherido].
verb_part(verbo([administrado])) --> [administrado].
verb_part(verbo([adquirido])) --> [adquirido].
verb_part(verbo([afectado])) --> [afectado].
verb_part(verbo([agregado])) --> [agregado].
verb_part(verbo([agrupado])) --> [agrupado].
verb_part(verbo([alcanzado])) --> [alcanzado].
verb_part(verbo([alejado])) --> [alejado].
verb_part(verbo([aparecido])) --> [aparecido].
verb_part(verbo([aplicado])) --> [aplicado].
verb_part(verbo([apropiado])) --> [apropiado].

verb_part(verbo([aproximado])) --> [aproximado].

verb_part(verbo([apuntalado])) --> [apuntalado].
verb_part(verbo([arraigado])) --> [arraigado].
verb_part(verbo([asalariado])) --> [asalariado].
verb_part(verbo([asistido])) --> [asistido].
verb_part(verbo([atravesado])) --> [atravesado].
verb_part(verbo([aumentado])) --> [aumentado].
verb_part(verbo([aunado])) --> [aunado].
verb_part(verbo([beneficiado])) --> [beneficiado].
verb_part(verbo([brindado])) --> [brindado].
verb_part(verbo([cancelado])) --> [cancelado].
verb_part(verbo([caracterizado])) --> [caracterizado].
verb_part(verbo([causado])) --> [causado].
verb_part(verbo([cedido])) --> [cedido].
verb_part(verbo([citado])) --> [citado].
verb_part(verbo([clasificado])) --> [clasificado].

verb_part(verbo([codificado])) --> [codificado].

verb_part(verbo([comercializado])) --> [comercializado].
verb_part(verbo([comisionado])) --> [comisionado].
verb_part(verbo([complicado])) --> [complicado].
verb_part(verbo([comprendido])) --> [comprendido].
verb_part(verbo([comprometido])) --> [comprometido].
verb_part(verbo([concretado])) --> [concretado].
verb_part(verbo([conformado])) --> [conformado].
verb_part(verbo([conocido])) --> [conocido].
verb_part(verbo([considerado])) --> [considerado].
verb_part(verbo([consolidado])) --> [consolidado].
verb_part(verbo([consolidó])) --> [consolidó].
verb_part(verbo([consumido])) --> [consumido].
verb_part(verbo([contado])) --> [contado].
verb_part(verbo([contagiado])) --> [contagiado].
verb_part(verbo([contaminado])) --> [contaminado].
verb_part(verbo([contratado])) --> [contratado].
verb_part(verbo([contribuido])) --> [contribuido].
verb_part(verbo([convertido])) --> [convertido].
verb_part(verbo([cosechado])) --> [cosechado].
verb_part(verbo([creado])) --> [creado].
verb_part(verbo([crecido])) --> [crecido].
verb_part(verbo([criticado])) --> [criticado].
verb_part(verbo([cumplido])) --> [cumplido].
verb_part(verbo([dado])) --> [dado].
verb_part(verbo([debido])) --> [debido].
verb_part(verbo([debilitado])) --> [debilitado].
verb_part(verbo([declinado])) --> [declinado].
verb_part(verbo([definido])) --> [definido].
verb_part(verbo([dejado])) --> [dejado].
verb_part(verbo([demostrado])) --> [demostrado].
verb_part(verbo([dependido])) --> [dependido].
verb_part(verbo([desaparecido])) --> [desaparecido].
verb_part(verbo([desarrollado])) --> [desarrollado].
verb_part(verbo([desconcertado])) --> [desconcertado].
verb_part(verbo([desconocido])) --> [desconocido].
verb_part(verbo([designado])) --> [designado].
verb_part(verbo([desmesurado])) --> [desmesurado].
verb_part(verbo([desmonopolizado])) --> [desmonopolizado].
verb_part(verbo([desplazado])) --> [desplazado].
verb_part(verbo([destinado])) --> [destinado].
verb_part(verbo([detectado])) --> [detectado].
verb_part(verbo([determinado])) --> [determinado].
verb_part(verbo([dictado])) --> [dictado].
verb_part(verbo([diferenciado])) --> [diferenciado].
verb_part(verbo([dirigido])) --> [dirigido].
verb_part(verbo([diseñado])) --> [diseñado].
verb_part(verbo([disminuido])) --> [disminuido].
verb_part(verbo([dominado])) --> [dominado].
verb_part(verbo([ejercido])) --> [ejercido].
verb_part(verbo([elevado])) --> [elevado].
verb_part(verbo([eliminado])) --> [eliminado].
verb_part(verbo([empleado])) --> [empleado].
verb_part(verbo([encontrado])) --> [encontrado].
verb_part(verbo([enfrentado])) --> [enfrentado].
verb_part(verbo([engañado])) --> [engañado].
verb_part(verbo([engarzado])) --> [engarzado].
verb_part(verbo([envasado])) --> [envasado].
verb_part(verbo([esforzado])) --> [esforzado].
verb_part(verbo([establecido])) --> [establecido].
verb_part(verbo([etiquetado])) --> [etiquetado].
verb_part(verbo([exigido])) --> [exigido].
verb_part(verbo([explicado])) --> [explicado].
verb_part(verbo([exportado])) --> [exportado].
verb_part(verbo([extendido])) --> [extendido].
verb_part(verbo([fermentado])) --> [fermentado].
verb_part(verbo([fijado])) --> [fijado].
verb_part(verbo([fundamentado])) --> [fundamentado].
verb_part(verbo([garantizado])) --> [garantizado].
verb_part(verbo([generado])) --> [generado].
verb_part(verbo([generalizado])) --> [generalizado].
verb_part(verbo([gestado])) --> [gestado].
verb_part(verbo([hablado])) --> [hablado].
verb_part(verbo([identificado])) --> [identificado].
verb_part(verbo([ido])) --> [ido].
verb_part(verbo([imaginado])) --> [imaginado].
verb_part(verbo([implementado])) --> [implementado].
verb_part(verbo([importado])) --> [importado].
verb_part(verbo([inadecuado])) --> [inadecuado].
verb_part(verbo([incidido])) --> [incidido].
verb_part(verbo([incluido])) --> [incluido].
verb_part(verbo([incorporado])) --> [incorporado].
verb_part(verbo([incrementado])) --> [incrementado].
verb_part(verbo([infectado])) --> [infectado].
verb_part(verbo([influenciado])) --> [influenciado].
verb_part(verbo([influido])) --> [influido].
verb_part(verbo([iniciado])) --> [iniciado].
verb_part(verbo([integrado])) --> [integrado].
verb_part(verbo([interesado])) --> [interesado].
verb_part(verbo([invertido])) --> [invertido].
verb_part(verbo([jugado])) --> [jugado].
verb_part(verbo([liberalizado])) --> [liberalizado].
verb_part(verbo([ligado])) --> [ligado].
verb_part(verbo([limitado])) --> [limitado].
verb_part(verbo([llamado])) --> [llamado].
verb_part(verbo([llevado])) --> [llevado].
verb_part(verbo([logrado])) --> [logrado].
verb_part(verbo([mantenido])) --> [mantenido].
verb_part(verbo([manufacturado])) --> [manufacturado].
verb_part(verbo([mencionado])) --> [mencionado].
verb_part(verbo([moderado])) --> [moderado].
verb_part(verbo([molido])) --> [molido].
verb_part(verbo([monopolizado])) --> [monopolizado].
verb_part(verbo([negociado])) --> [negociado].
verb_part(verbo([nutrido])) --> [nutrido].
verb_part(verbo([obligado])) --> [obligado].
verb_part(verbo([obtenido])) --> [obtenido].
verb_part(verbo([ocasionado])) --> [ocasionado].
verb_part(verbo([ocurrido])) --> [ocurrido].
verb_part(verbo([ofrecido])) --> [ofrecido].
verb_part(verbo([optado])) --> [optado].
verb_part(verbo([organizado])) --> [organizado].
verb_part(verbo([orientado])) --> [orientado].
verb_part(verbo([oscilado])) --> [oscilado].
verb_part(verbo([otorgado])) --> [otorgado].
verb_part(verbo([pagado])) --> [pagado].
verb_part(verbo([participado])) --> [participado].
verb_part(verbo([pasado])) --> [pasado].
verb_part(verbo([penetrado])) --> [penetrado].
verb_part(verbo([perdido])) --> [perdido].
verb_part(verbo([permitido])) --> [permitido].
verb_part(verbo([posesionado])) --> [posesionado].
verb_part(verbo([preferido])) --> [preferido].
verb_part(verbo([presentado])) --> [presentado].
verb_part(verbo([prestado])) --> [prestado].
verb_part(verbo([privado])) --> [privado].
verb_part(verbo([privilegiado])) --> [privilegiado].
verb_part(verbo([probado])) --> [probado].
verb_part(verbo([producido])) --> [producido].
verb_part(verbo([prometido])) --> [prometido].
verb_part(verbo([promovido])) --> [promovido].
verb_part(verbo([publicado])) --> [publicado].
verb_part(verbo([quedado])) --> [quedado].
verb_part(verbo([realizado])) --> [realizado].
verb_part(verbo([reconocido])) --> [reconocido].
verb_part(verbo([recorrido])) --> [recorrido].
verb_part(verbo([reducido])) --> [reducido].

verb_part(verbo([regulado])) --> [regulado].

verb_part(verbo([reportado])) --> [reportado].
verb_part(verbo([represado])) --> [represado].
verb_part(verbo([representado])) --> [representado].
verb_part(verbo([requerido])) --> [requerido].
verb_part(verbo([resultado])) --> [resultado].
verb_part(verbo([revisado])) --> [revisado].
verb_part(verbo([rodeado])) --> [rodeado].
verb_part(verbo([salido])) --> [salido].
verb_part(verbo([secado])) --> [secado].

verb_part(verbo([secuenciado])) --> [secuenciado].

verb_part(verbo([sembrado])) --> [sembrado].
verb_part(verbo([señalado])) --> [señalado].
verb_part(verbo([sentido])) --> [sentido].
verb_part(verbo([servido])) --> [servido].
verb_part(verbo([sido])) --> [sido].
verb_part(verbo([significado])) --> [significado].
verb_part(verbo([situado])) --> [situado].
verb_part(verbo([sometido])) --> [sometido].
verb_part(verbo([sostenido])) --> [sostenido].
verb_part(verbo([subsidiado])) --> [subsidiado].
verb_part(verbo([sucedido])) --> [sucedido].
verb_part(verbo([sufrido])) --> [sufrido].
verb_part(verbo([superado])) --> [superado].
verb_part(verbo([surgido])) --> [surgido].
verb_part(verbo([surtido])) --> [surtido].
verb_part(verbo([sustentado])) --> [sustentado].
verb_part(verbo([sustituido])) --> [sustituido].
verb_part(verbo([tejido])) --> [tejido].
verb_part(verbo([tenido])) --> [tenido].
verb_part(verbo([terminado])) --> [terminado].
verb_part(verbo([traído])) --> [traído].
verb_part(verbo([transcurrido])) --> [transcurrido].
verb_part(verbo([transmitido])) --> [transmitido].
verb_part(verbo([tratado])) --> [tratado].
verb_part(verbo([ubicado])) --> [ubicado].
verb_part(verbo([unido])) --> [unido].
verb_part(verbo([usado])) --> [usado].
verb_part(verbo([utilizado])) --> [utilizado].
verb_part(verbo([variado])) --> [variado].
verb_part(verbo([vendido])) --> [vendido].
verb_part(verbo([venido])) --> [venido].
verb_part(verbo([vinculado])) --> [vinculado].
verb_part(verbo([vivido])) --> [vivido].

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% verbos auxiliares 
% conjugacion de los verbos haber, ser, estar
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

verb_aux(verbo([haber])) --> [haber].
verb_aux(verbo([habiendo])) --> [habiendo].
verb_aux(verbo([he])) --> [he].
verb_aux(verbo([has])) --> [has].
verb_aux(verbo([ha])) --> [ha].
verb_aux(verbo([hay])) --> [hay].
verb_aux(verbo([hemos])) --> [hemos].
verb_aux(verbo([habemos])) --> [habemos].
verb_aux(verbo([habéis])) --> [habéis].
verb_aux(verbo([han])) --> [han].
verb_aux(verbo([había])) --> [había].
verb_aux(verbo([habías])) --> [habías].
verb_aux(verbo([había])) --> [había].
verb_aux(verbo([habíamos])) --> [habíamos].
verb_aux(verbo([habíais])) --> [habíais].
verb_aux(verbo([habían])) --> [habían].
verb_aux(verbo([hube])) --> [hube].
verb_aux(verbo([hubiste])) --> [hubiste].
verb_aux(verbo([hubo])) --> [hubo].
verb_aux(verbo([hubimos])) --> [hubimos].
verb_aux(verbo([hubisteis])) --> [hubisteis].
verb_aux(verbo([hubieron])) --> [hubieron].
verb_aux(verbo([habré])) --> [habré].
verb_aux(verbo([habrás])) --> [habrás].
verb_aux(verbo([habrá])) --> [habrá].
verb_aux(verbo([habremos])) --> [habremos].
verb_aux(verbo([habréis])) --> [habréis].
verb_aux(verbo([habrán])) --> [habrán].
verb_aux(verbo([habría])) --> [habría].
verb_aux(verbo([habrías])) --> [habrías].
verb_aux(verbo([habría])) --> [habría].
verb_aux(verbo([habríamos])) --> [habríamos].
verb_aux(verbo([habríais])) --> [habríais].
verb_aux(verbo([habrían])) --> [habrían].
verb_aux(verbo([haya])) --> [haya].
verb_aux(verbo([hayas])) --> [hayas].
verb_aux(verbo([hayamos])) --> [hayamos].
verb_aux(verbo([hayáis])) --> [hayáis].
verb_aux(verbo([hayan])) --> [hayan].
verb_aux(verbo([hubiera])) --> [hubiera].
verb_aux(verbo([hubiese])) --> [hubiese].
verb_aux(verbo([hubieras])) --> [hubieras].
verb_aux(verbo([hubieses])) --> [hubieses].
verb_aux(verbo([hubiera])) --> [hubiera].
verb_aux(verbo([hubiese])) --> [hubiese].
verb_aux(verbo([hubiéramos])) --> [hubiéramos].
verb_aux(verbo([hubiésemos])) --> [hubiésemos].
verb_aux(verbo([hubierais])) --> [hubierais].
verb_aux(verbo([hubieseis])) --> [hubieseis].
verb_aux(verbo([hubieran])) --> [hubieran].
verb_aux(verbo([hubiesen])) --> [hubiesen].
verb_aux(verbo([hubiere])) --> [hubiere].
verb_aux(verbo([hubieres])) --> [hubieres].
verb_aux(verbo([hubiere])) --> [hubiere].
verb_aux(verbo([hubiéremos])) --> [hubiéremos].
verb_aux(verbo([hubiereis])) --> [hubiereis].
verb_aux(verbo([hubieren])) --> [hubieren].
verb_aux(verbo([ser])) --> [ser].
verb_aux(verbo([siendo])) --> [siendo].
verb_aux(verbo([soy])) --> [soy].
verb_aux(verbo([eres])) --> [eres].
verb_aux(verbo([es])) --> [es].
verb_aux(verbo([somos])) --> [somos].
verb_aux(verbo([sois])) --> [sois].
verb_aux(verbo([son])) --> [son].
verb_aux(verbo([era])) --> [era].
verb_aux(verbo([eras])) --> [eras].
verb_aux(verbo([era])) --> [era].
verb_aux(verbo([éramos])) --> [éramos].
verb_aux(verbo([erais])) --> [erais].
verb_aux(verbo([eran])) --> [eran].
verb_aux(verbo([fui])) --> [fui].
verb_aux(verbo([fuiste])) --> [fuiste].
verb_aux(verbo([fue])) --> [fue].
verb_aux(verbo([fuimos])) --> [fuimos].
verb_aux(verbo([fuisteis])) --> [fuisteis].
verb_aux(verbo([fueron])) --> [fueron].
verb_aux(verbo([seré])) --> [seré].
verb_aux(verbo([serás])) --> [serás].
verb_aux(verbo([será])) --> [será].
verb_aux(verbo([seremos])) --> [seremos].
verb_aux(verbo([seréis])) --> [seréis].
verb_aux(verbo([serán])) --> [serán].
verb_aux(verbo([sería])) --> [sería].
verb_aux(verbo([serías])) --> [serías].
verb_aux(verbo([sería])) --> [sería].
verb_aux(verbo([seríamos])) --> [seríamos].
verb_aux(verbo([seríais])) --> [seríais].
verb_aux(verbo([serían])) --> [serían].
verb_aux(verbo([sea])) --> [sea].
verb_aux(verbo([seas])) --> [seas].
verb_aux(verbo([sea])) --> [sea].
verb_aux(verbo([seamos])) --> [seamos].
verb_aux(verbo([seáis])) --> [seáis].
verb_aux(verbo([sean])) --> [sean].
verb_aux(verbo([fuera])) --> [fuera].
verb_aux(verbo([fuese])) --> [fuese].
verb_aux(verbo([fueras])) --> [fueras].
verb_aux(verbo([fueses])) --> [fueses].
verb_aux(verbo([fuera])) --> [fuera].
verb_aux(verbo([fuese])) --> [fuese].
verb_aux(verbo([fuéramos])) --> [fuéramos].
verb_aux(verbo([fuésemos])) --> [fuésemos].
verb_aux(verbo([fuerais])) --> [fuerais].
verb_aux(verbo([fueseis])) --> [fueseis].
verb_aux(verbo([fueran])) --> [fueran].
verb_aux(verbo([fuesen])) --> [fuesen].
verb_aux(verbo([fuere])) --> [fuere].
verb_aux(verbo([fueres])) --> [fueres].
verb_aux(verbo([fuere])) --> [fuere].
verb_aux(verbo([fuéremos])) --> [fuéremos].
verb_aux(verbo([fuereis])) --> [fuereis].
verb_aux(verbo([fueren])) --> [fueren].
verb_aux(verbo([estar])) --> [estar].
verb_aux(verbo([estando])) --> [estando].
verb_aux(verbo([estoy])) --> [estoy].
verb_aux(verbo([estás])) --> [estás].
verb_aux(verbo([está])) --> [está].
verb_aux(verbo([estamos])) --> [estamos].
verb_aux(verbo([estáis])) --> [estáis].
verb_aux(verbo([están])) --> [están].
verb_aux(verbo([estaba])) --> [estaba].
verb_aux(verbo([estabas])) --> [estabas].
verb_aux(verbo([estaba])) --> [estaba].
verb_aux(verbo([estábamos])) --> [estábamos].
verb_aux(verbo([estabais])) --> [estabais].
verb_aux(verbo([estaban])) --> [estaban].
verb_aux(verbo([estuve])) --> [estuve].
verb_aux(verbo([estuviste])) --> [estuviste].
verb_aux(verbo([estuvo])) --> [estuvo].
verb_aux(verbo([estuvimos])) --> [estuvimos].
verb_aux(verbo([estuvisteis])) --> [estuvisteis].
verb_aux(verbo([estuvieron])) --> [estuvieron].
verb_aux(verbo([estaré])) --> [estaré].
verb_aux(verbo([estarás])) --> [estarás].
verb_aux(verbo([estará])) --> [estará].
verb_aux(verbo([estaremos])) --> [estaremos].
verb_aux(verbo([estaréis])) --> [estaréis].
verb_aux(verbo([estarán])) --> [estarán].
verb_aux(verbo([estaría])) --> [estaría].
verb_aux(verbo([estarías])) --> [estarías].
verb_aux(verbo([estaría])) --> [estaría].
verb_aux(verbo([estaríamos])) --> [estaríamos].
verb_aux(verbo([estaríais])) --> [estaríais].
verb_aux(verbo([estarían])) --> [estarían].
verb_aux(verbo([esté])) --> [esté].
verb_aux(verbo([estés])) --> [estés].
verb_aux(verbo([esté])) --> [esté].
verb_aux(verbo([estemos])) --> [estemos].
verb_aux(verbo([estéis])) --> [estéis].
verb_aux(verbo([estén])) --> [estén].
verb_aux(verbo([estuviera])) --> [estuviera].
verb_aux(verbo([estuviese])) --> [estuviese].
verb_aux(verbo([estuvieras])) --> [estuvieras].
verb_aux(verbo([estuvieses])) --> [estuvieses].
verb_aux(verbo([estuviera])) --> [estuviera].
verb_aux(verbo([estuviese])) --> [estuviese].
verb_aux(verbo([estuviéramos])) --> [estuviéramos].
verb_aux(verbo([estuviésemos])) --> [estuviésemos].
verb_aux(verbo([estuvierais])) --> [estuvierais].
verb_aux(verbo([estuvieseis])) --> [estuvieseis].
verb_aux(verbo([estuvieran])) --> [estuvieran].
verb_aux(verbo([estuviesen])) --> [estuviesen].
verb_aux(verbo([estuviere])) --> [estuviere].
verb_aux(verbo([estuvieres])) --> [estuvieres].
verb_aux(verbo([estuviere])) --> [estuviere].
verb_aux(verbo([estuviéremos])) --> [estuviéremos].
verb_aux(verbo([estuviereis])) --> [estuviereis].
verb_aux(verbo([estuvieren])) --> [estuvieren].


% predicado

predicado(aaa).



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  Inglés

%% en ingles
%% coordinativas
es_conjuncion(and). 
es_conjuncion(now).
es_conjuncion(but).
es_conjuncion(still).
es_conjuncion(yet).
es_conjuncion(only).
es_conjuncion(while).
es_conjuncion(then).
es_conjuncion(so).
es_conjuncion(for).
es_conjuncion(either).
es_conjuncion(neither).
es_conjuncion(however).
es_conjuncion(therefor).
es_conjuncion(nevertheless).

%% subordinativas
es_conjuncion(that).
es_conjuncion(because).
es_conjuncion(since).
es_conjuncion(as).
es_conjuncion(so).
es_conjuncion(lest).
es_conjuncion(if).
es_conjuncion(unless).
es_conjuncion(although).
es_conjuncion(though).
es_conjuncion(while).
es_conjuncion(until).
es_conjuncion(when).
es_conjuncion(why).
es_conjuncion(whether).

%% ingles
adjetivo_posesivo_antsustantivo(my).
adjetivo_posesivo_antsustantivo(your).
adjetivo_posesivo_antsustantivo(his).
adjetivo_posesivo_antsustantivo(her).
adjetivo_posesivo_antsustantivo(its).
adjetivo_posesivo_antsustantivo(our).
adjetivo_posesivo_antsustantivo(their).



