grammar Calculator;

expression: multiplyingExpression((PLUS | MINUS) multiplyingExpression)*;
multiplyingExpression: powExpression ((MULT | DIV) powExpression | (POW) powExpression)*;
powExpression: integralExpression ((POW | SQRT) integralExpression)*;
integralExpression: MINUS INT | INT;

INT: [0-9]+ ;
PLUS: '+' ;
MINUS: '-' ;
MULT: '*' ;
DIV: '/' ;
POW: '^' ;
SQRT: 'sqrt' ;
WS : [ \t\r\n]+ -> skip ;
