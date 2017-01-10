# resumidor
Resumidor de artículos en html / summarizer of Spanish-English, html-text files
El resumidor es un programa Prolog. Corre con swi-prolog version 7 o superior. 
La entrada del resumidor es un archivo html.  
Coloque el archivo a procesar en el mismo directorio de los archivo del resumidor. Corra swipl para hacer:

?- [resumidorcompleto].

?- tell('salida.html'), resume('entrada.html'), told.
true .

La salida del resumidor también es un html. Podrá revisarla con el navegador. Codificación utf8. 
