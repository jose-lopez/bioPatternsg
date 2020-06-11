:-style_check(-discontiguous).

inicio(A,E,B):-base(C),buscar_en_lista(event(A,E,B),C),buscar_en_lista(E,[activate,bind,interact,recognize]),(ligand(A);receptor(A)),receptor(B),not(A = B).

final(A,E,B):-base(C),buscar_en_lista(event(A,E,B),C),transcription_factor(A),buscar_en_lista(E,[regulate,enhance,induce,lead,trigger,translate,transcribe,reactivate,promote,synthesize,stimulate,target,express,provoke,modulate,mediate,respond,infect,detect,raise,develop,incubate,support,betamediate,emerge,stabilise,stabilize,inhibit,down-regulate,repress,prevent,suppress,retain,decrease,inactivate,prevent,limit,remove,affect,antagonize,agonize,fall,destabilise,destabilize,reduce]).

intermedios(A,E,B):-base(C),buscar_en_lista(event(A,E,B),C),protein(B),buscar_en_lista(E,[require,interact,associate,phosphorylate,recruit,recognize,participate,involve,coordinate,trimerize,heterodimerize,dimerize,relate,release,collect,combine,envelop,bring]).

eventoEspecial(A,E,B):-base(C),buscar_en_lista(event(A,E,B),C),transcription_factor(A),buscar_en_lista(E,[regulate,enhance,induce,lead,trigger,translate,transcribe,reactivate,promote,synthesize,stimulate,target,express,provoke,modulate,mediate,respond,infect,detect,raise,develop,incubate,support,betamediate,emerge,stabilise,stabilize,inhibit,down-regulate,repress,prevent,suppress,retain,decrease,inactivate,prevent,limit,remove,affect,antagonize,agonize,fall,destabilise,destabilize,reduce]).


finalEspecial(A,E,B):-base(C),buscar_en_lista(event(A,E,B),C),buscar_en_lista(E,[regulate,enhance,induce,lead,trigger,translate,transcribe,reactivate,promote,synthesize,stimulate,target,express,provoke,modulate,mediate,respond,infect,detect,raise,develop,incubate,support,betamediate,emerge,stabilise,stabilize,inhibit,down-regulate,repress,prevent,suppress,retain,decrease,inactivate,prevent,limit,remove,affect,antagonize,agonize,fall,destabilise,destabilize,reduce]).

%[bind,activate,inactivate,repress,inhibit,regulate,stimulate]

%Patrones con restricciones de objetos

inicio_rest(A,E,B,L):-buscar_en_lista(A,L),buscar_en_lista(B,L),base(C),buscar_en_lista(event(A,E,B),C),buscar_en_lista(E,[activate,bind,interact,recognize]),(ligand(A);receptor(A)),receptor(B),not(A = B).

final_rest(A,E,B,L):-buscar_en_lista(A,L),buscar_en_lista(B,L),base(C),buscar_en_lista(event(A,E,B),C),transcription_factor(A),buscar_en_lista(E,[regulate,enhance,induce,lead,trigger,translate,transcribe,reactivate,promote,synthesize,stimulate,target,express,provoke,modulate,mediate,respond,infect,detect,raise,develop,incubate,support,betamediate,emerge,stabilise,stabilize,inhibit,down-regulate,repress,prevent,suppress,retain,decrease,inactivate,prevent,limit,remove,affect,antagonize,agonize,fall,destabilise,destabilize,reduce]).


intermedios_rest(A,E,B,L):-buscar_en_lista(B,L),base(C),buscar_en_lista(event(A,E,B),C),protein(B),buscar_en_lista(E,[require,interact,associate,phosphorylate,recruit,recognize,participate,involve,coordinate,trimerize,heterodimerize,dimerize,relate,release,collect,combine,envelop,bring]).


eventoEspecial_rest(A,E,B,L):-transcription_factor(A),buscar_en_lista(A,L),buscar_en_lista(B,L),base(C),buscar_en_lista(event(A,E,B),C),buscar_en_lista(E,[regulate,enhance,induce,lead,trigger,translate,transcribe,reactivate,promote,synthesize,stimulate,target,express,provoke,modulate,mediate,respond,infect,detect,raise,develop,incubate,support,betamediate,emerge,stabilise,stabilize,inhibit,down-regulate,repress,prevent,suppress,retain,decrease,inactivate,prevent,limit,remove,affect,antagonize,agonize,fall,destabilise,destabilize,reduce]).



buscar_en_lista(L,[L|_]).
buscar_en_lista(L,[_|Ys]):-buscar_en_lista(L,Ys).

objeto(A,E,B):-base(C),buscar_en_lista(event(A,E,B),C),transcription_factor(A).

%[trimerize,require,heterodimerize,interact,associate,phosphorylate,recruit,dimerize,recognize,participate,activate,inhibit]
