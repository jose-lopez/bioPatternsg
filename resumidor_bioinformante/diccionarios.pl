% diccionarios.pl
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
   


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% conjugacion de verbos en ingles
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


verb(verbo([recruit])) --> [recruit].
verb(verbo([recruits])) --> [recruits].
verb(verbo([recruited])) --> [recruited].
verb(verbo([recruiting])) --> [recruiting].
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
verb(verbo([repress])) --> [repress].
verb(verbo([represses])) --> [represses].
verb(verbo([repressed])) --> [repressed].
verb(verbo([repressing])) --> [repressing].
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
verb(verbo([activating])) --> [activating].
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
verb(verbo([inhibit])) --> [inhibit].
verb(verbo([inhibits])) --> [inhibits].
verb(verbo([inhibited])) --> [inhibited].
verb(verbo([inhibiting])) --> [inhibiting].
verb(verbo([increase])) --> [increase].
verb(verbo([increases])) --> [increases].
verb(verbo([increased])) --> [increased].
verb(verbo([increasing])) --> [increasing].
verb(verbo([decrease])) --> [decrease].
verb(verbo([decreases])) --> [decreases].
verb(verbo([decreased])) --> [decreased].
verb(verbo([decreasing])) --> [decreasing].
verb(verbo([regulate])) --> [regulate].
verb(verbo([regulates])) --> [regulates].
verb(verbo([regulated])) --> [regulated].
verb(verbo([regulating])) --> [regulating].
verb(verbo([modulate])) --> [modulate].
verb(verbo([modulates])) --> [modulates].
verb(verbo([modulated])) --> [modulated].
verb(verbo([modulating])) --> [modulating].
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
verb(verbo([synthesize])) --> [synthesize].
verb(verbo([synthesized])) --> [synthesized].
verb(verbo([synthesizes])) --> [synthesizes].
verb(verbo([synthesizing])) --> [synthesizing].


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% verbos participios regulares 
% llamados participios pasivos terminados en 
% los sufijos ido y ado
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% verbos auxiliares 
% conjugacion de los verbos haber, ser, estar
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%




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



