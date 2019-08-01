//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";



package ru.amalnev.selenium.language;



//#line 2 "/home/amalnev/dev/jnms/jnms-selenium/src/main/yacc/selenium.y"
import java.util.*;
import java.io.IOException;
//#line 20 "Parser.java"




public class Parser
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
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
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
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
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
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short NUMERIC_LITERAL=257;
public final static short STRING_LITERAL=258;
public final static short EQ=259;
public final static short WORD=260;
public final static short SEMICOLON=261;
public final static short OPEN_BRACKET=262;
public final static short CLOSE_BRACKET=263;
public final static short COMMA=264;
public final static short FUNCTION_KEYWORD=265;
public final static short OPEN_CURLY_BRACKET=266;
public final static short CLOSE_CURLY_BRACKET=267;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    1,    2,    3,    3,    3,    6,    7,
    7,    7,    4,    8,    5,    5,    5,    9,    9,   10,
   11,   12,   12,   12,
};
final static short yylen[] = {                            2,
    1,    0,    1,    2,    2,    1,    1,    1,    8,    0,
    3,    1,    3,    1,    1,    1,    1,    1,    1,    4,
    1,    0,    3,    1,
};
final static short yydefred[] = {                         0,
   18,   19,    0,    0,    0,    0,    3,    0,    6,    7,
    8,    0,   15,   16,    0,    0,    4,    5,    0,    0,
    0,   13,   17,   24,    0,   12,    0,   20,    0,    0,
    0,   23,    0,   11,    0,    9,
};
final static short yydgoto[] = {                          5,
    6,    7,    8,    9,   10,   11,   27,   12,   13,   14,
   15,   25,
};
final static short yysindex[] = {                      -244,
    0,    0,    0, -257,    0, -244,    0, -251,    0,    0,
    0, -247,    0,    0, -236, -224,    0,    0, -230, -230,
 -221,    0,    0,    0, -245,    0, -232,    0, -230, -233,
 -220,    0, -244,    0, -256,    0,
};
final static short yyrindex[] = {                        41,
    0,    0, -239,    0,    0,   42,    0,    0,    0,    0,
    0, -218,    0,    0,    0,    0,    0,    0,    0, -229,
 -227,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -223,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   12,   -6,    0,    0,  -14,    0,    0,  -12,    0,    0,
    0,    0,
};
final static int YYTABLESIZE=45;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         17,
    1,    2,   16,    3,   22,   24,   23,   23,    4,   18,
   36,   19,    1,    2,   32,    3,   23,   28,   29,   14,
    4,   14,   21,   14,   14,   20,    1,    2,   17,    3,
   30,   31,   33,   22,   22,   10,   10,   21,   26,   34,
    2,    1,   17,    2,   35,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          6,
  257,  258,  260,  260,   19,   20,   19,   20,  265,  261,
  267,  259,  257,  258,   29,  260,   29,  263,  264,  259,
  265,  261,  262,  263,  264,  262,  257,  258,   35,  260,
  263,  264,  266,  263,  264,  263,  264,  262,  260,  260,
    0,    0,  261,  267,   33,
};
}
final static short YYFINAL=5;
final static short YYMAXTOKEN=267;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
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
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"NUMERIC_LITERAL","STRING_LITERAL","EQ","WORD","SEMICOLON",
"OPEN_BRACKET","CLOSE_BRACKET","COMMA","FUNCTION_KEYWORD","OPEN_CURLY_BRACKET",
"CLOSE_CURLY_BRACKET",
};
final static String yyrule[] = {
"$accept : input",
"input : statement_list",
"statement_list :",
"statement_list : statement",
"statement_list : statement_list statement",
"statement : statement_body SEMICOLON",
"statement_body : assignment",
"statement_body : expression",
"statement_body : function_definition",
"function_definition : FUNCTION_KEYWORD WORD OPEN_BRACKET argument_name_list CLOSE_BRACKET OPEN_CURLY_BRACKET statement_list CLOSE_CURLY_BRACKET",
"argument_name_list :",
"argument_name_list : argument_name_list COMMA WORD",
"argument_name_list : WORD",
"assignment : varname EQ expression",
"varname : WORD",
"expression : literal",
"expression : function_call",
"expression : varname",
"literal : NUMERIC_LITERAL",
"literal : STRING_LITERAL",
"function_call : function_name OPEN_BRACKET function_call_argument_list CLOSE_BRACKET",
"function_name : WORD",
"function_call_argument_list :",
"function_call_argument_list : function_call_argument_list COMMA expression",
"function_call_argument_list : expression",
};

//#line 159 "/home/amalnev/dev/jnms/jnms-selenium/src/main/yacc/selenium.y"

private FunctionDefinition entryPointFunction;

private Lexer lexer;

private void yyerror(final String s) throws ParserException
{
    throw new ParserException(s);
}

private int yylex() throws IOException
{
    return lexer.yylex();
}

public FunctionDefinition parseFromString(final String inputString) throws ParserException, IOException
{
    lexer = new Lexer(this, inputString);
    yyparse();
    return entryPointFunction;
}

//#line 234 "Parser.java"
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
throws ParserException,IOException
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
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
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 19 "/home/amalnev/dev/jnms/jnms-selenium/src/main/yacc/selenium.y"
{
	FunctionDefinition entryPoint = new FunctionDefinition();
	entryPoint.setStatements((List<IStatement>)val_peek(0).obj);
	this.entryPointFunction = entryPoint;
 }
break;
case 3:
//#line 27 "/home/amalnev/dev/jnms/jnms-selenium/src/main/yacc/selenium.y"
{
 	/*System.out.println("first statement in the list");*/
 	List<IStatement> statements = new LinkedList<IStatement>();
 	statements.add((IStatement)val_peek(0).obj);
 	yyval = new ParserVal(statements);
 }
break;
case 4:
//#line 33 "/home/amalnev/dev/jnms/jnms-selenium/src/main/yacc/selenium.y"
{
 	/*System.out.println("more statements");*/
  	((List<IStatement>)val_peek(1).obj).add((IStatement)val_peek(0).obj);
 }
break;
case 5:
//#line 39 "/home/amalnev/dev/jnms/jnms-selenium/src/main/yacc/selenium.y"
{
	/*System.out.println("statement body");*/
	yyval = val_peek(1);
 }
break;
case 6:
//#line 45 "/home/amalnev/dev/jnms/jnms-selenium/src/main/yacc/selenium.y"
{
	/*System.out.println("statement body: assignment");*/
	yyval = val_peek(0);
 }
break;
case 7:
//#line 49 "/home/amalnev/dev/jnms/jnms-selenium/src/main/yacc/selenium.y"
{
 	/*System.out.println("statement body: expression");*/
	yyval = new ParserVal(new ExpressionStatement((IExpression)val_peek(0).obj));
 }
break;
case 8:
//#line 53 "/home/amalnev/dev/jnms/jnms-selenium/src/main/yacc/selenium.y"
{
  	/*System.out.println("statement body: function definition");*/
  	yyval = val_peek(0);
 }
break;
case 9:
//#line 59 "/home/amalnev/dev/jnms/jnms-selenium/src/main/yacc/selenium.y"
{
	/*System.out.println("function definition");*/
	FunctionDefinition functionDefinition = new FunctionDefinition();
	functionDefinition.setStatements((List<IStatement>)val_peek(1).obj);
	functionDefinition.setFunctionName(val_peek(6).sval);
	functionDefinition.setArgumentNames((List<String>)val_peek(4).obj);
	yyval = new ParserVal(functionDefinition);
 }
break;
case 10:
//#line 69 "/home/amalnev/dev/jnms/jnms-selenium/src/main/yacc/selenium.y"
{
	/*System.out.println("function definition argument list: empty list");*/
	yyval = new ParserVal(new LinkedList<String>());
 }
break;
case 11:
//#line 73 "/home/amalnev/dev/jnms/jnms-selenium/src/main/yacc/selenium.y"
{
 	/*System.out.println("function definition argument list: more arguments");*/
 	List<String> argumentList = (List<String>)val_peek(2).obj;
        argumentList.add(val_peek(0).sval);
        yyval = val_peek(2);
 }
break;
case 12:
//#line 79 "/home/amalnev/dev/jnms/jnms-selenium/src/main/yacc/selenium.y"
{
 	/*System.out.println("function definition argument list: first argument");*/
 	List<String> argumentList = new LinkedList<>();
 	argumentList.add(val_peek(0).sval);
 	yyval = new ParserVal(argumentList);
 }
break;
case 13:
//#line 87 "/home/amalnev/dev/jnms/jnms-selenium/src/main/yacc/selenium.y"
{
	/*System.out.println("assignment");*/
	yyval = new ParserVal(new AssignmentStatement((Variable)val_peek(2).obj, (IExpression)val_peek(0).obj));
 }
break;
case 14:
//#line 93 "/home/amalnev/dev/jnms/jnms-selenium/src/main/yacc/selenium.y"
{
	/*System.out.println("varname: " + $1.sval);*/
	yyval = new ParserVal(new Variable(val_peek(0).sval));
 }
break;
case 15:
//#line 99 "/home/amalnev/dev/jnms/jnms-selenium/src/main/yacc/selenium.y"
{
	/*System.out.println("expression (literal)");*/
	yyval = val_peek(0);
 }
break;
case 16:
//#line 103 "/home/amalnev/dev/jnms/jnms-selenium/src/main/yacc/selenium.y"
{
 	/*System.out.println("expression (function call)");*/
	yyval = val_peek(0);
 }
break;
case 17:
//#line 107 "/home/amalnev/dev/jnms/jnms-selenium/src/main/yacc/selenium.y"
{
	/*System.out.println("expression (variable expansion)");*/
	yyval = val_peek(0);
 }
break;
case 18:
//#line 113 "/home/amalnev/dev/jnms/jnms-selenium/src/main/yacc/selenium.y"
{
	/*System.out.println("numeric literal: " + $1.ival);*/
	yyval = new ParserVal(new IntegerLiteral(val_peek(0).ival));
 }
break;
case 19:
//#line 117 "/home/amalnev/dev/jnms/jnms-selenium/src/main/yacc/selenium.y"
{
 	/*System.out.println("string literal: " + $1.sval);*/
 	yyval = new ParserVal(new StringLiteral(val_peek(0).sval));
 }
break;
case 20:
//#line 123 "/home/amalnev/dev/jnms/jnms-selenium/src/main/yacc/selenium.y"
{
	/*System.out.println("Function call: " + $1.sval);*/
	FunctionCallExpression functionCall = new FunctionCallExpression();
	functionCall.setFunctionName(val_peek(3).sval);
	functionCall.setFunctionCallArguments((List<IExpression>)val_peek(1).obj);
	yyval = new ParserVal(functionCall);
 }
break;
case 21:
//#line 132 "/home/amalnev/dev/jnms/jnms-selenium/src/main/yacc/selenium.y"
{
	/*System.out.println("Function name: " + $1.sval);*/
	yyval = val_peek(0);
 }
break;
case 22:
//#line 138 "/home/amalnev/dev/jnms/jnms-selenium/src/main/yacc/selenium.y"
{
	/*System.out.println("Function call arglist: empty list");*/
	List<IExpression> argumentList = new LinkedList<>();
        yyval = new ParserVal(argumentList);
 }
break;
case 23:
//#line 143 "/home/amalnev/dev/jnms/jnms-selenium/src/main/yacc/selenium.y"
{
 	/*System.out.println("Function call arglist: more args");*/
 	List<IExpression> argumentList = (List<IExpression>)val_peek(2).obj;
 	IExpression expression = (IExpression)val_peek(0).obj;
 	argumentList.add(expression);
 	yyval = val_peek(2);
 }
break;
case 24:
//#line 150 "/home/amalnev/dev/jnms/jnms-selenium/src/main/yacc/selenium.y"
{
 	/*System.out.println("Function call arglist: first arg");*/
	List<IExpression> argumentList = new LinkedList<>();
	IExpression expression = (IExpression)val_peek(0).obj;
	argumentList.add(expression);
	yyval = new ParserVal(argumentList);
 }
break;
//#line 565 "Parser.java"
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
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
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
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
//## The -Jnorun option was used ##
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
