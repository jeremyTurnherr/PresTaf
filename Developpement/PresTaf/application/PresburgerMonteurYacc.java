//### This file created by BYACC 1.8(/Java extension  1.1)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//### Please send bug reports to rjamison@lincom-asg.com
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 9 "PresburgerMonteur.y"
package application;

import prestaf.*;
import java.io.*;
import java.util.*;
//#line 21 "PresburgerMonteurYacc.java"




/**
 * Encapsulates yacc() parser functionality in a Java
 *        class for quick code development
 */
public class PresburgerMonteurYacc
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[],stateptr;           //state stack
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
void state_push(int state)
{
  if (stateptr>=YYSTACKSIZE)         //overflowed?
    return;
  statestk[++stateptr]=state;
  if (stateptr>statemax)
    {
    statemax=state;
    stateptrmax=stateptr;
    }
}
int state_pop()
{
  if (stateptr<0)                    //underflowed?
    return -1;
  return statestk[stateptr--];
}
void state_drop(int cnt)
{
int ptr;
  ptr=stateptr-cnt;
  if (ptr<0)
    return;
  stateptr = ptr;
}
int state_peek(int relative)
{
int ptr;
  ptr=stateptr-relative;
  if (ptr<0)
    return -1;
  return statestk[ptr];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
boolean init_stacks()
{
  statestk = new int[YYSTACKSIZE];
  stateptr = -1;
  statemax = -1;
  stateptrmax = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class PresburgerMonteurYaccVal is defined in PresburgerMonteurYaccVal.java


String   yytext;//user variable to return contextual strings
PresburgerMonteurYaccVal yyval; //used to return semantic vals from action routines
PresburgerMonteurYaccVal yylval;//the 'lval' (result) I got from yylex()
PresburgerMonteurYaccVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new PresburgerMonteurYaccVal[YYSTACKSIZE];
  yyval=new PresburgerMonteurYaccVal(0);
  yylval=new PresburgerMonteurYaccVal(0);
  valptr=-1;
}
void val_push(PresburgerMonteurYaccVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
PresburgerMonteurYaccVal val_pop()
{
  if (valptr<0)
    return new PresburgerMonteurYaccVal(-1);
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
PresburgerMonteurYaccVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new PresburgerMonteurYaccVal(-1);
  return valstk[ptr];
}
//#### end semantic value section ####
public final static short EQUIV=257;
public final static short IMPLY=258;
public final static short AND=259;
public final static short OR=260;
public final static short EXISTS=261;
public final static short FORALL=262;
public final static short INTEGER=263;
public final static short IDENT=264;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    6,    6,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    2,    2,    2,    2,    3,    3,    3,    3,
    3,    3,    3,    3,    4,    4,    4,    4,    5,    5,
    5,
};
final static short yylen[] = {                            2,
    2,    7,    0,    3,    2,    3,    3,    3,    3,    1,
    1,    1,    5,    5,    3,    3,    3,    4,    3,    4,
    4,    3,    4,    4,    3,    3,    1,    2,    3,    1,
    1,
};
final static short yydefred[] = {                         3,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   10,   11,    0,   27,    0,    0,    0,    0,    0,    5,
   30,   28,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   15,    0,   16,   29,    0,    4,
    0,    7,    0,    0,    0,    0,    0,    0,    0,    0,
   26,   25,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   13,   14,    0,    2,
};
final static short yydgoto[] = {                          1,
   10,   11,   12,   13,   14,    2,
};
final static short yysindex[] = {                         0,
    0,  -33, -255, -246,   -5,  -28,  -29, -250,  -29, -121,
    0,    0,   63,    0,  -38,  -35, -223,   -8,    0,    0,
    0,    0,  -21,  -29,  -29,  -29,  -29,   -2,   43,  -42,
 -250, -250,   -7,  -29,    0,  -29,    0,    0,   14,    0,
 -180,    0, -236, -232,  -14,  -14,  -14,  -10,  -14,  -10,
    0,    0,  -14,  -10,  -17,  -12,  -29,  -10,  -10,  -10,
  -10,  -10,    0,    0,  -51,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    1,   10,    0,    0,    0,   83,
    0,    0,    0,    0,    0,    0,    0,    0,    6,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   57,    0,   17,   48,    0,    0,    0,   15,    0,   23,
    0,    0,    0,   28,    0,    0,    0,   32,   36,   40,
   45,   52,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   93,   64,    0,  102,   82,    0,
};
final static int YYTABLESIZE=317;
final static short yytable[] = {                          7,
   31,   34,    8,    7,   36,   12,    9,   66,   15,   12,
    9,    8,    5,   21,   17,    8,    6,   16,   49,   40,
   24,   25,   19,   63,   24,   25,   26,   22,   64,   18,
    8,   18,   32,   31,   31,   21,   17,    8,   30,   24,
   38,   31,   30,   31,   20,   31,   12,    9,   30,   39,
   30,   23,   30,   53,   30,   17,    8,    6,   45,   31,
   31,   31,   31,   19,   12,   30,   30,   30,   22,   30,
   30,   30,   18,   17,   57,    6,   21,   25,   35,   37,
   24,   19,    1,    0,    0,   20,   22,    8,    9,   22,
   18,    0,   23,    0,   21,   28,    0,    8,   24,   20,
    0,   23,   46,   20,   47,   32,    9,   31,    0,    0,
   23,    0,   51,   52,    0,    8,   41,   42,   43,   44,
    0,    0,   30,   29,   33,    0,   55,    0,   56,    0,
   48,   50,    0,    0,   54,   24,   25,   26,   27,    0,
    0,    0,    0,    0,    0,    0,   58,   59,   60,   65,
   61,    0,    0,    0,   62,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   24,   25,   26,   27,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    5,   21,    3,    4,    0,    3,    4,    3,    4,    5,
    6,    3,    4,    5,   19,   24,   25,   26,   27,   24,
   25,   26,   27,    0,   24,   25,   26,   27,    5,   21,
    0,    0,    0,    0,    0,    5,   21,   31,   31,   31,
   31,    0,   12,   12,   12,   12,   12,   12,   12,   12,
    0,   17,   17,   17,   17,    6,    6,    0,    0,   19,
   19,   19,   19,    0,   22,   22,   22,   22,   18,   18,
   18,   18,   21,   21,   21,   21,   24,   24,   24,   24,
    0,   20,   20,   20,   20,    5,   21,    9,   23,   23,
   23,   23,    0,    8,    0,    8,    8,
};
final static short yycheck[] = {                         33,
    0,   40,   45,   33,   40,    0,   40,   59,  264,    0,
   40,   45,  263,  264,    0,   45,    0,  264,   61,   41,
  257,  258,    0,   41,  257,  258,  259,    0,   41,   58,
   45,    0,   43,   33,   45,    0,   42,   45,   33,    0,
  264,   41,   33,   43,    0,   45,   41,    0,   43,   58,
   45,    0,   43,   61,   45,   41,    0,   41,   61,   59,
   60,   61,   62,   41,   59,   60,   61,   62,   41,   60,
   61,   62,   41,   59,   61,   59,   41,  258,   15,   16,
   41,   59,    0,   -1,   -1,   41,   59,   45,   41,    8,
   59,   -1,   41,   -1,   59,   33,   -1,   41,   59,    7,
   -1,    9,   60,   59,   62,   43,   59,   45,   -1,   -1,
   59,   -1,   31,   32,   -1,   59,   24,   25,   26,   27,
   -1,   -1,   60,   61,   62,   -1,   34,   -1,   36,   -1,
   29,   30,   -1,   -1,   33,  257,  258,  259,  260,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   45,   46,   47,   57,
   49,   -1,   -1,   -1,   53,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  257,  258,  259,  260,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  263,  264,  261,  262,   -1,  261,  262,  261,  262,  263,
  264,  261,  262,  263,  264,  257,  258,  259,  260,  257,
  258,  259,  260,   -1,  257,  258,  259,  260,  263,  264,
   -1,   -1,   -1,   -1,   -1,  263,  264,  257,  258,  259,
  260,   -1,  257,  258,  259,  260,  257,  258,  259,  260,
   -1,  257,  258,  259,  260,  259,  260,   -1,   -1,  257,
  258,  259,  260,   -1,  257,  258,  259,  260,  257,  258,
  259,  260,  257,  258,  259,  260,  257,  258,  259,  260,
   -1,  257,  258,  259,  260,  263,  264,  260,  257,  258,
  259,  260,   -1,  257,   -1,  259,  260,
};
final static short YYFINAL=1;
final static short YYMAXTOKEN=264;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"'!'",null,null,null,null,null,null,"'('","')'","'*'","'+'",null,
"'-'",null,null,null,null,null,null,null,null,null,null,null,null,"':'","';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,"EQUIV","IMPLY","AND","OR","EXISTS","FORALL",
"INTEGER","IDENT",
};
final static String yyrule[] = {
"$accept : formula",
"formula : definition presburger",
"definition : definition IDENT ':' ':' '=' presburger ';'",
"definition :",
"presburger : '(' presburger ')'",
"presburger : '!' presburger",
"presburger : presburger AND presburger",
"presburger : presburger IMPLY presburger",
"presburger : presburger EQUIV presburger",
"presburger : presburger OR presburger",
"presburger : quantifier",
"presburger : prop",
"presburger : IDENT",
"quantifier : EXISTS IDENT '(' presburger ')'",
"quantifier : FORALL IDENT '(' presburger ')'",
"quantifier : EXISTS IDENT quantifier",
"quantifier : FORALL IDENT quantifier",
"prop : term '=' term",
"prop : term '!' '=' term",
"prop : term '<' term",
"prop : term '<' '=' term",
"prop : term '=' '<' term",
"prop : term '>' term",
"prop : term '>' '=' term",
"prop : term '=' '>' term",
"term : term '+' factor",
"term : term '-' factor",
"term : factor",
"term : '-' factor",
"factor : INTEGER '*' IDENT",
"factor : IDENT",
"factor : INTEGER",
};

//#line 308 "PresburgerMonteur.y"
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



//#line 338 "PresburgerMonteurYacc.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 48 "PresburgerMonteur.y"
{
                    formula = (Presburger) val_peek(0).obj;
                }
break;
case 2:
//#line 54 "PresburgerMonteur.y"
{
                    Presburger f = (Presburger) val_peek(1).obj;
                    f.expr = val_peek(5).sval;
                    table.put(val_peek(5).sval,f);
                }
break;
case 4:
//#line 63 "PresburgerMonteur.y"
{
                    yyval.obj = ((Presburger) val_peek(1).obj).bracket();
                }
break;
case 5:
//#line 68 "PresburgerMonteur.y"
{
                    Presburger p1 = (Presburger) val_peek(0).obj;
                    if (verbose)
                        System.out.println("!" + p1);
                    Presburger res = p1.not();
                    if (verbose)
                        System.out.println("\t" + res.statistics());
                    yyval.obj = res;
                }
break;
case 6:
//#line 79 "PresburgerMonteur.y"
{
                    Presburger p1 = (Presburger) val_peek(2).obj;
                    Presburger p2 = (Presburger) val_peek(0).obj;
                    if (verbose)
                        System.out.println(p1 + " && " + p2);
                    Presburger res = p1.and(p2);
                    if (verbose)
                        System.out.println("\t" + res.statistics());
                    yyval.obj = res;
                }
break;
case 7:
//#line 90 "PresburgerMonteur.y"
{
                    Presburger p1 = (Presburger) val_peek(2).obj;
                    Presburger p2 = (Presburger) val_peek(0).obj;
                    if (verbose)
                        System.out.println(p1 + " -> " + p2);
                    Presburger res = p1.imply(p2);
                    if (verbose)
                        System.out.println("\t" + res.statistics());
                    yyval.obj = res;
                }
break;
case 8:
//#line 101 "PresburgerMonteur.y"
{
                    Presburger p1 = (Presburger) val_peek(2).obj;
                    Presburger p2 = (Presburger) val_peek(0).obj;
                    if (verbose)
                        System.out.println(p1 + " <-> " + p2);
                    Presburger res = p1.equiv(p2);
                    if (verbose)                    
                        System.out.println("\t" + res.statistics());
                    yyval.obj = res;
                }
break;
case 9:
//#line 112 "PresburgerMonteur.y"
{
                    Presburger p1 = (Presburger) val_peek(2).obj;
                    Presburger p2 = (Presburger) val_peek(0).obj;
                    if (verbose)
                        System.out.println(p1 + " || " + p2);
                    Presburger res = p1.or(p2);
                    if (verbose)
                        System.out.println("\t" + res.statistics());
                    yyval.obj = res;
                }
break;
case 10:
//#line 123 "PresburgerMonteur.y"
{
                    yyval.obj = val_peek(0).obj;
                }
break;
case 11:
//#line 127 "PresburgerMonteur.y"
{
                    yyval.obj = val_peek(0).obj;
                }
break;
case 12:
//#line 131 "PresburgerMonteur.y"
{
                    yyval.obj = table.get(val_peek(0).sval);
                }
break;
case 13:
//#line 138 "PresburgerMonteur.y"
{
                    Presburger p1 = ((Presburger) val_peek(1).obj).bracket();
                    if (verbose)
                        System.out.println("E." + val_peek(3).sval + p1);
                    Presburger res = p1.exists(Term.getIndex(val_peek(3).sval),val_peek(3).sval);
                    if (verbose)
                        System.out.println("\t" + res.statistics());
                    yyval.obj = res;
                }
break;
case 14:
//#line 148 "PresburgerMonteur.y"
{
                    Presburger p1 = ((Presburger) val_peek(1).obj).bracket();
                    if (verbose)
                        System.out.println("A." + val_peek(3).sval + p1 );
                    Presburger res = p1.forall(Term.getIndex(val_peek(3).sval),val_peek(3).sval);
                    if (verbose)
                        System.out.println("\t" + res.statistics());
                    yyval.obj = res;
                }
break;
case 15:
//#line 158 "PresburgerMonteur.y"
{
                    Presburger p1 = (Presburger) val_peek(0).obj;
                    if (verbose)
                        System.out.println("E." + val_peek(1).sval + " " + p1);
                    Presburger res = p1.exists(Term.getIndex(val_peek(1).sval),val_peek(1).sval);
                    if (verbose)
                        System.out.println("\t" + res.statistics());
                    yyval.obj = res;
                }
break;
case 16:
//#line 168 "PresburgerMonteur.y"
{
                    Presburger p1 = (Presburger) val_peek(0).obj;
                    if (verbose)
                        System.out.println("A." + val_peek(1).sval + " " + p1);
                    Presburger res = p1.forall(Term.getIndex(val_peek(1).sval),val_peek(1).sval);
                    if (verbose)
                        System.out.println("\t" + res.statistics());
                    yyval.obj = res;
                }
break;
case 17:
//#line 182 "PresburgerMonteur.y"
{
                    Term t1 = (Term) val_peek(2).obj;
                    Term t2 = (Term) val_peek(0).obj;
                    if (verbose)
                        System.out.println(t1 + " = " + t2);
                    Presburger res = Presburger.equals(t1,t2);
                    if (verbose)
                        System.out.println("\t" + res.statistics());
                    yyval.obj = res;
                }
break;
case 18:
//#line 194 "PresburgerMonteur.y"
{
                    Term t1 = (Term) val_peek(3).obj;
                    Term t2 = (Term) val_peek(0).obj;
                    if (verbose)
                        System.out.println(t1 + " != " + t2);
                    Presburger res = Presburger.notEquals(t1,t2);
                    if (verbose)
                        System.out.println("\t" + res.statistics());
                    yyval.obj = res;
                }
break;
case 19:
//#line 205 "PresburgerMonteur.y"
{
                    Term t1 = (Term) val_peek(2).obj;
                    Term t2 = (Term) val_peek(0).obj;
                    if (verbose)
                        System.out.println(t1 + " < " + t2);
                    Presburger res = Presburger.less(t1,t2);
                    if (verbose)
                        System.out.println("\t" + res.statistics());
                    yyval.obj = res;
                }
break;
case 20:
//#line 216 "PresburgerMonteur.y"
{
                    Term t1 = (Term) val_peek(3).obj;
                    Term t2 = (Term) val_peek(0).obj;
                    if (verbose)
                        System.out.println(t1 + " <= " + t2);
                    Presburger res = Presburger.lessEquals(t1,t2);
                    if (verbose)
                        System.out.println("\t" + res.statistics());
                    yyval.obj = res;
                }
break;
case 21:
//#line 228 "PresburgerMonteur.y"
{
                    Term t1 = (Term) val_peek(3).obj;
                    Term t2 = (Term) val_peek(0).obj;
                    if (verbose)
                        System.out.println(t1 + " <= " + t2);
                    Presburger res = Presburger.lessEquals(t1,t2);
                    if (verbose)
                        System.out.println("\t" + res.statistics());
                    yyval.obj = res;
                }
break;
case 22:
//#line 240 "PresburgerMonteur.y"
{
                    Term t1 = (Term) val_peek(2).obj;
                    Term t2 = (Term) val_peek(0).obj;
                    if (verbose)
                        System.out.println(t1 + " > " + t2);
                    Presburger res = Presburger.greater(t1,t2);
                    if (verbose)
                        System.out.println("\t" + res.statistics());
                    yyval.obj = res;
                }
break;
case 23:
//#line 252 "PresburgerMonteur.y"
{
                    Term t1 = (Term) val_peek(3).obj;
                    Term t2 = (Term) val_peek(0).obj;
                    if (verbose)
                        System.out.println(t1 + " >= " + t2);
                    Presburger res = Presburger.greaterEquals(t1,t2);
                    if (verbose)
                        System.out.println("\t" + res.statistics());
                    yyval.obj = res;
                }
break;
case 24:
//#line 264 "PresburgerMonteur.y"
{
                    Term t1 = (Term) val_peek(3).obj;
                    Term t2 = (Term) val_peek(0).obj;
                    if (verbose)
                        System.out.println(t1 + " >= " + t2);
                    Presburger res = Presburger.greaterEquals(t1,t2);
                    if (verbose)
                        System.out.println("\t" + res.statistics());
                    yyval.obj = res;
                }
break;
case 25:
//#line 277 "PresburgerMonteur.y"
{
                    yyval.obj = ((Term) val_peek(2).obj).plus((Term) val_peek(0).obj);
                }
break;
case 26:
//#line 281 "PresburgerMonteur.y"
{
                    yyval.obj = ((Term) val_peek(2).obj).minus((Term) val_peek(0).obj);
                }
break;
case 27:
//#line 285 "PresburgerMonteur.y"
{
                    yyval.obj = val_peek(0).obj;
                }
break;
case 28:
//#line 289 "PresburgerMonteur.y"
{
                    yyval.obj = ((Term) val_peek(0).obj).neg();
                }
break;
case 29:
//#line 295 "PresburgerMonteur.y"
{
                    yyval.obj = Term.factor(val_peek(2).ival,val_peek(0).sval);
                }
break;
case 30:
//#line 299 "PresburgerMonteur.y"
{
                    yyval.obj = Term.variable(val_peek(0).sval);
                }
break;
case 31:
//#line 303 "PresburgerMonteur.y"
{
                    yyval.obj = Term.integer(val_peek(0).ival);
                }
break;
//#line 781 "PresburgerMonteurYacc.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public PresburgerMonteurYacc()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public PresburgerMonteurYacc(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
