//
// PresburgerMonteur.y
//  PresTafProject
//
//  Created by couvreur on Tue Dec 02 2003.
//

%{
package application;

import prestaf.*;
import java.io.*;
import java.util.*;
%}

%token EQUIV, IMPLY, AND, OR, EXISTS, FORALL
%token <ival> INTEGER
%token <sval> IDENT

%left OR 
%left AND
%left EQUIV
%left IMPLY 
%left EXISTS FORALL '!'

%left '=' '<'
%left '-'
%left '+' 
%left '*'
%left ':'
%left ';'

%type <obj> presburger
%type <obj> quantifier
%type <obj> prop
%type <obj> term
%type <obj> factor

%start formula

%%
/*........... Regles de grammaire ...*/

	/*********************/
	/* lecture du formule */
	/*********************/
formula		: definition presburger
                {
                    formula = (Presburger) $2;
                }
                ;
                
definition	: definition IDENT ':' ':' '=' presburger ';'
                {
                    Presburger f = (Presburger) $6;
                    f.expr = $2;
                    table.put($2,f);
                }
                | 
                ;

presburger	: '(' presburger ')'
                {
                    $$ = ((Presburger) $2).bracket();
                }

                | '!' presburger
                {
                    Presburger p1 = (Presburger) $2;
                    if (verbose)
                        System.out.println("!" + p1);
                    Presburger res = p1.not();
                    if (verbose)
                        System.out.println("\t" + res.statistics());
                    $$ = res;
                }

                | presburger AND presburger
                {
                    Presburger p1 = (Presburger) $1;
                    Presburger p2 = (Presburger) $3;
                    if (verbose)
                        System.out.println(p1 + " && " + p2);
                    Presburger res = p1.and(p2);
                    if (verbose)
                        System.out.println("\t" + res.statistics());
                    $$ = res;
                }
                | presburger IMPLY presburger
                {
                    Presburger p1 = (Presburger) $1;
                    Presburger p2 = (Presburger) $3;
                    if (verbose)
                        System.out.println(p1 + " -> " + p2);
                    Presburger res = p1.imply(p2);
                    if (verbose)
                        System.out.println("\t" + res.statistics());
                    $$ = res;
                }
                | presburger EQUIV presburger
                {
                    Presburger p1 = (Presburger) $1;
                    Presburger p2 = (Presburger) $3;
                    if (verbose)
                        System.out.println(p1 + " <-> " + p2);
                    Presburger res = p1.equiv(p2);
                    if (verbose)                    
                        System.out.println("\t" + res.statistics());
                    $$ = res;
                }
                | presburger OR presburger
                {
                    Presburger p1 = (Presburger) $1;
                    Presburger p2 = (Presburger) $3;
                    if (verbose)
                        System.out.println(p1 + " || " + p2);
                    Presburger res = p1.or(p2);
                    if (verbose)
                        System.out.println("\t" + res.statistics());
                    $$ = res;
                }
                | quantifier
                {
                    $$ = $1;
                }
                | prop
                {
                    $$ = $1;
                }
                | IDENT
                {
                    $$ = table.get($1);
                }
		;


quantifier      : EXISTS IDENT "(" presburger ")"
                {
                    Presburger p1 = ((Presburger) $4).bracket();
                    if (verbose)
                        System.out.println("E." + $2 + p1);
                    Presburger res = p1.exists(Term.getIndex($2),$2);
                    if (verbose)
                        System.out.println("\t" + res.statistics());
                    $$ = res;
                }
                | FORALL IDENT "(" presburger ")"
                {
                    Presburger p1 = ((Presburger) $4).bracket();
                    if (verbose)
                        System.out.println("A." + $2 + p1 );
                    Presburger res = p1.forall(Term.getIndex($2),$2);
                    if (verbose)
                        System.out.println("\t" + res.statistics());
                    $$ = res;
                }
                | EXISTS IDENT quantifier
                {
                    Presburger p1 = (Presburger) $3;
                    if (verbose)
                        System.out.println("E." + $2 + " " + p1);
                    Presburger res = p1.exists(Term.getIndex($2),$2);
                    if (verbose)
                        System.out.println("\t" + res.statistics());
                    $$ = res;
                }
                | FORALL IDENT quantifier
                {
                    Presburger p1 = (Presburger) $3;
                    if (verbose)
                        System.out.println("A." + $2 + " " + p1);
                    Presburger res = p1.forall(Term.getIndex($2),$2);
                    if (verbose)
                        System.out.println("\t" + res.statistics());
                    $$ = res;
                }
                ;



prop		: term '=' term 
                {
                    Term t1 = (Term) $1;
                    Term t2 = (Term) $3;
                    if (verbose)
                        System.out.println(t1 + " = " + t2);
                    Presburger res = Presburger.equals(t1,t2);
                    if (verbose)
                        System.out.println("\t" + res.statistics());
                    $$ = res;
                }

                | term '!' '=' term
                {
                    Term t1 = (Term) $1;
                    Term t2 = (Term) $4;
                    if (verbose)
                        System.out.println(t1 + " != " + t2);
                    Presburger res = Presburger.notEquals(t1,t2);
                    if (verbose)
                        System.out.println("\t" + res.statistics());
                    $$ = res;
                }
                | term '<' term
                {
                    Term t1 = (Term) $1;
                    Term t2 = (Term) $3;
                    if (verbose)
                        System.out.println(t1 + " < " + t2);
                    Presburger res = Presburger.less(t1,t2);
                    if (verbose)
                        System.out.println("\t" + res.statistics());
                    $$ = res;
                }
                | term '<' '=' term
                {
                    Term t1 = (Term) $1;
                    Term t2 = (Term) $4;
                    if (verbose)
                        System.out.println(t1 + " <= " + t2);
                    Presburger res = Presburger.lessEquals(t1,t2);
                    if (verbose)
                        System.out.println("\t" + res.statistics());
                    $$ = res;
                }

                | term '=' '<' term
                {
                    Term t1 = (Term) $1;
                    Term t2 = (Term) $4;
                    if (verbose)
                        System.out.println(t1 + " <= " + t2);
                    Presburger res = Presburger.lessEquals(t1,t2);
                    if (verbose)
                        System.out.println("\t" + res.statistics());
                    $$ = res;
                }

                | term '>' term
                {
                    Term t1 = (Term) $1;
                    Term t2 = (Term) $3;
                    if (verbose)
                        System.out.println(t1 + " > " + t2);
                    Presburger res = Presburger.greater(t1,t2);
                    if (verbose)
                        System.out.println("\t" + res.statistics());
                    $$ = res;
                }
                
                | term '>' '=' term
                {
                    Term t1 = (Term) $1;
                    Term t2 = (Term) $4;
                    if (verbose)
                        System.out.println(t1 + " >= " + t2);
                    Presburger res = Presburger.greaterEquals(t1,t2);
                    if (verbose)
                        System.out.println("\t" + res.statistics());
                    $$ = res;
                }
                
                | term '=' '>' term
                {
                    Term t1 = (Term) $1;
                    Term t2 = (Term) $4;
                    if (verbose)
                        System.out.println(t1 + " >= " + t2);
                    Presburger res = Presburger.greaterEquals(t1,t2);
                    if (verbose)
                        System.out.println("\t" + res.statistics());
                    $$ = res;
                }
		;

term		: term '+' factor 
                {
                    $$ = ((Term) $1).plus((Term) $3);
                }
                | term '-' factor
                {
                    $$ = ((Term) $1).minus((Term) $3);
                }
                | factor
                {
                    $$ = $1;
                }
                | '-' factor
                {
                    $$ = ((Term) $2).neg();
                }
                ;
                
factor		: INTEGER '*' IDENT 
                {
                    $$ = Term.factor($1,$3);
                }
		| IDENT
                {
                    $$ = Term.variable($1);
                }
		| INTEGER 
                {
                    $$ = Term.integer($1);
                }
                ;
%%
    Presburger formula;
    HashMap table = new HashMap();
    boolean verbose;
    
    public Presburger parse()
    {
        int v = yyparse();
        if (v == -1) {                            
            System.out.println("error");
            table.clear();
            return null;
        }
        else {
            table.clear();
            return formula;
        }
    }

  private PresburgerMonteurFlex lexer;

  private int yylex () {
    int yyl_return = -1;
    try {
      yylval = new PresburgerMonteurYaccVal(0);
      yyl_return = lexer.yylex();
    }
    catch (IOException e) {
      System.err.println("IO error :" + e);
    }
    return yyl_return;
  }


  public void yyerror (String error) 
  {
    System.err.println ("Error: " + error);
  }


  public PresburgerMonteurYacc(Reader r, boolean ver)
  {
    verbose = ver;
    lexer = new PresburgerMonteurFlex(r, this);
  }



