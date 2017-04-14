grammar Demo;

program
	: (statement ';')+;

statement
	: println
	| varAssignment
	| assignment
	;

expression
	: left=expression operator=('*' | '/') right=expression #MULTDIV
	| left=expression operator=('+' | '-') right=expression #PLUSMINUS
	| num=NUM #Number
	| varName=NAME #Variable
	;
	
assignment: varName=NAME '=' expr=expression;

varAssignment
	: 'int' varName=NAME ;

println
	: 'println(' argument=expression ')' ;

NAME
	: [a-zA-Z][a-zA-Z0-9]*;

NUM
	: [0-9]+;

WHITESPACE
	: [ \t\n\r]+ -> skip;