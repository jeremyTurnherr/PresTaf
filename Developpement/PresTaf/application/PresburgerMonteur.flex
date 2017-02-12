//
//  PresburgerMonteur.flex
//  PresTafProject
//
//  Created by couvreur on Tue Dec 02 2003.
//

package application;

%%

%byaccj
%public
%class PresburgerMonteurFlex

%{
  private PresburgerMonteurYacc yyparser;
  
  private int comment_count = 0;

  public PresburgerMonteurFlex(java.io.Reader r, PresburgerMonteurYacc yyparser) {
    this(r);
    this.yyparser = yyparser;
  }
%}

/* comments */
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
Comment = "%" {InputCharacter}* {LineTerminator}? 

/* main character classes */
WHITE_SPACE_CHAR=[\n\r\ \t\b\012]

ALPHA=[A-Za-z]
DIGIT=[0-9]
Ident = {ALPHA}({ALPHA}|{DIGIT}|_)*
Int = {DIGIT}{DIGIT}*

%%


<YYINITIAL> {
    "<->"		{ return(yyparser.EQUIV);}
    "->"		{ return(yyparser.IMPLY);}
    "&&"		{ return(yyparser.AND);}
    "||"		{ return(yyparser.OR);}
    "<"			{ return('<');}
    ">"			{ return('>');}

    "-"			{ return('-'); }
    "+"			{ return('+'); }
    "*"			{ return('*'); }

    "="			{ return('=');}
    "!"			{ return('!');}

    "E."		{ return(yyparser.EXISTS);}
    "A."		{ return(yyparser.FORALL);}

    "("			{ return('('); }
    ")"			{ return(')'); }
    "{"			{ return('('); }
    "}"			{ return(')'); }
    ":"			{ return(':'); }
    ";"			{ return(';'); }

    {Int}		{ 
                            yyparser.yylval = new PresburgerMonteurYaccVal(Integer.parseInt(yytext()));
                            return yyparser.INTEGER; 
                        }
    {Ident}		{ 
                            yyparser.yylval = new PresburgerMonteurYaccVal(yytext());
                            return yyparser.IDENT; 
                        }
    
    {WHITE_SPACE_CHAR}+ { }
    {Comment} 		{}

}
