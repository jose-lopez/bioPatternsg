% diccionarios.pl (only english) 2020-03-26
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

:- encoding(utf8).
:- discontiguous es_conjuncion/1.

es_articulo(el,singular,masculino).

% espanol
% espanol
es_pronombre(lo).
es_pronombre(le).
es_pronombre(les).
es_pronombre(esa).
es_pronombre(ese).
es_pronombre(se).
es_pronombre(X) :- pronombre(_,_,[X],[]).

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



%% adjetivo_posesivo_antsustantivo



%% adjetivo_posesivo_despsustantivo




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

%% Expresiones de tiempo y espacio



%% Expresiones para evaluar






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



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% verbos binario - auxiliares
% conjugacion de los verbos tener, deber, poder
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


verb_aux(verbo([haber])) --> [haber].
verb_aux(verbo([habiendo])) --> [habiendo].
verb_aux(verbo([has])) --> [has].


verb(verbo([love])) --> [love].
verb(verbo([have])) --> [have].

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% conjugacion de verbos en ingles
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

verb(verbo([is])) --> [is].
verb(verbo([coordinates])) --> [coordinates].
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
verb(verbo([bring])) --> [bring].
verb(verbo([brings])) --> [brings].
verb(verbo([brought])) --> [brought].
verb(verbo([bringing])) --> [bringing].

% agregados en 2020-05-23
verb(verbo([limit])) --> [limit].
verb(verbo([limits])) --> [limits].
verb(verbo([limiting])) --> [limiting].
verb(verbo([limited])) --> [limited].
verb(verbo([infect])) --> [infect].
verb(verbo([infects])) --> [infects].
verb(verbo([infected])) --> [infected].
verb(verbo([infecting])) --> [infecting].
verb(verbo([relate])) --> [relate].
verb(verbo([relates])) --> [relates].
verb(verbo([relating])) --> [relating].
verb(verbo([related])) --> [related].
verb(verbo([release])) --> [release].
verb(verbo([releases])) --> [releases].
verb(verbo([releasing])) --> [releasing].
verb(verbo([released])) --> [released].
verb(verbo([collect])) --> [collect].
verb(verbo([collects])) --> [collects].
verb(verbo([collecting])) --> [collecting].
verb(verbo([collected])) --> [collected].
verb(verbo([detect])) --> [detect].
verb(verbo([detects])) --> [detects].
verb(verbo([detecting])) --> [detecting].
verb(verbo([detected])) --> [detected].
verb(verbo([raise])) --> [raise].
verb(verbo([raises])) --> [raises].
verb(verbo([raising])) --> [raising].
verb(verbo([raised])) --> [raised].
verb(verbo([develop])) --> [develop].
verb(verbo([develops])) --> [develops].
verb(verbo([developing])) --> [developing].
verb(verbo([developed])) --> [developed].
verb(verbo([combine])) --> [combine].
verb(verbo([combines])) --> [combines].
verb(verbo([combining])) --> [combining].
verb(verbo([combined])) --> [combined].
verb(verbo([remove])) --> [remove].
verb(verbo([removes])) --> [removes].
verb(verbo([removing])) --> [removing].
verb(verbo([removed])) --> [removed].
verb(verbo([incubate])) --> [incubate].
verb(verbo([incubates])) --> [incubates].
verb(verbo([incubating])) --> [incubating].
verb(verbo([incubated])) --> [incubated].
verb(verbo([convert])) --> [convert].
verb(verbo([converts])) --> [converts].
verb(verbo([converting])) --> [converting].
verb(verbo([converted])) --> [converted].
verb(verbo([change])) --> [change].
verb(verbo([changes])) --> [changes].
verb(verbo([changing])) --> [changing].
verb(verbo([changed])) --> [changed].
verb(verbo([support])) --> [support].
verb(verbo([supports])) --> [supports].
verb(verbo([supporting])) --> [supporting].
verb(verbo([supported])) --> [supported].
verb(verbo([betamediate])) --> [betamediate].
verb(verbo([betamediates])) --> [betamediates].
verb(verbo([betamediating])) --> [betamediating].
verb(verbo([betamediated])) --> [betamediated].
verb(verbo([affect])) --> [affect].
verb(verbo([affects])) --> [affects].
verb(verbo([affecting])) --> [affecting].
verb(verbo([affected])) --> [affected].
verb(verbo([control])) --> [control].
verb(verbo([controls])) --> [controls].
verb(verbo([controlling])) --> [controlling].
verb(verbo([controlled])) --> [controlled].
verb(verbo([envelop])) --> [envelop].
verb(verbo([envelops])) --> [envelops].
verb(verbo([enveloping])) --> [enveloping].
verb(verbo([enveloped])) --> [enveloped].
verb(verbo([antagonize])) --> [antagonize].
verb(verbo([antagonizes])) --> [antagonizes].
verb(verbo([antagonizing])) --> [antagonizing].
verb(verbo([antagonized])) --> [antagonized].
verb(verbo([agonize])) --> [agonize].
verb(verbo([agonizes])) --> [agonizes].
verb(verbo([agonizing])) --> [agonizing].
verb(verbo([agonized])) --> [agonized].
verb(verbo([emerge])) --> [emerge].
verb(verbo([emerges])) --> [emerges].
verb(verbo([emerging])) --> [emerging].
verb(verbo([emerged])) --> [emerged].
verb(verbo([fit])) --> [fit].
verb(verbo([fits])) --> [fits].
verb(verbo([fitting])) --> [fitting].
verb(verbo([fitted])) --> [fitted].
verb(verbo([fall])) --> [fall].
verb(verbo([falls])) --> [falls].
verb(verbo([falling])) --> [falling].
verb(verbo([fell])) --> [fell].
verb(verbo([destabilise])) --> [destabilise].
verb(verbo([destabilises])) --> [destabilises].
verb(verbo([destabilising])) --> [destabilising].
verb(verbo([destabilised])) --> [destabilised].
verb(verbo([stabilise])) --> [stabilise].
verb(verbo([stabilises])) --> [stabilises].
verb(verbo([stabilising])) --> [stabilising].
verb(verbo([stabilised])) --> [stabilised].
verb(verbo([destabilize])) --> [destabilize].
verb(verbo([destabilizes])) --> [destabilizes].
verb(verbo([destabilizing])) --> [destabilizing].
verb(verbo([destabilized])) --> [destabilized].
verb(verbo([stabilize])) --> [stabilize].
verb(verbo([stabilizes])) --> [stabilizes].
verb(verbo([stabilizing])) --> [stabilizing].
verb(verbo([stabilized])) --> [stabilized].
verb(verbo([reduce])) --> [reduce].
verb(verbo([reduces])) --> [reduces].
verb(verbo([reducing])) --> [reducing].
verb(verbo([reduced])) --> [reduced].
verb(verbo([sequester])) --> [sequester].
verb(verbo([sequesters])) --> [sequesters].
verb(verbo([sequestering])) --> [sequestering].
verb(verbo([sequestered])) --> [sequestered].
verb(verbo([reveal])) --> [reveal].
verb(verbo([reveals])) --> [reveals].
verb(verbo([revealing])) --> [revealing].
verb(verbo([revealed])) --> [revealed].

% fin de agregados en 2020-05-23

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% verbos participios regulares
% llamados participios pasivos terminados en
% los sufijos ido y ado
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


verb_part(verbo([acometido])) --> [acometido].
verb_part(verbo([acompañado])) --> [acompañado].
verb_part(verbo([adecuado])) --> [adecuado].

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
es_conjuncion(therefore).
es_conjuncion(nevertheless).
es_conjuncion(furthermore).

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
