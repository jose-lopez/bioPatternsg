% resumidorcompleto.pl
% text summarizer for spanish and english texts. 
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
:- encoding(utf8).
:- set_prolog_stack(global, limit(2 000 000 000)).
:- set_prolog_stack(trail,  limit(2 000 000 000)).
:- set_prolog_stack(local,  limit(2 000 000 000)).

:- [resumidorpaso1entradas].
:- [resumidorpaso2gramatica].
:- [resumidorpaso3].
:- [resumidorpaso4].
:- [diccionarios].
:- [resumidorpaso5salidas].
