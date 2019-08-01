%{
import java.util.*;
import java.io.IOException;
%}

%token NUMERIC_LITERAL
%token STRING_LITERAL
%token EQ
%token WORD
%token SEMICOLON
%token OPEN_BRACKET
%token CLOSE_BRACKET
%token COMMA
%token FUNCTION_KEYWORD
%token OPEN_CURLY_BRACKET
%token CLOSE_CURLY_BRACKET

%%
input: statement_list  {
	FunctionDefinition entryPoint = new FunctionDefinition();
	entryPoint.setStatements((List<IStatement>)$1.obj);
	this.entryPointFunction = entryPoint;
 }
 ;

statement_list:
 | statement {
 	//System.out.println("first statement in the list");
 	List<IStatement> statements = new LinkedList<IStatement>();
 	statements.add((IStatement)$1.obj);
 	$$ = new ParserVal(statements);
 }
 | statement_list statement {
 	//System.out.println("more statements");
  	((List<IStatement>)$1.obj).add((IStatement)$2.obj);
 }
 ;

statement: statement_body SEMICOLON {
	//System.out.println("statement body");
	$$ = $1;
 }
 ;

statement_body: assignment {
	//System.out.println("statement body: assignment");
	$$ = $1;
 }
 | expression {
 	//System.out.println("statement body: expression");
	$$ = new ParserVal(new ExpressionStatement((IExpression)$1.obj));
 }
 | function_definition {
  	//System.out.println("statement body: function definition");
  	$$ = $1;
 }
 ;

function_definition: FUNCTION_KEYWORD WORD OPEN_BRACKET argument_name_list CLOSE_BRACKET OPEN_CURLY_BRACKET statement_list CLOSE_CURLY_BRACKET {
	//System.out.println("function definition");
	FunctionDefinition functionDefinition = new FunctionDefinition();
	functionDefinition.setStatements((List<IStatement>)$7.obj);
	functionDefinition.setFunctionName($2.sval);
	functionDefinition.setArgumentNames((List<String>)$4.obj);
	$$ = new ParserVal(functionDefinition);
 }
 ;

argument_name_list: {
	//System.out.println("function definition argument list: empty list");
	$$ = new ParserVal(new LinkedList<String>());
 }
 | argument_name_list COMMA WORD {
 	//System.out.println("function definition argument list: more arguments");
 	List<String> argumentList = (List<String>)$1.obj;
        argumentList.add($3.sval);
        $$ = $1;
 }
 | WORD {
 	//System.out.println("function definition argument list: first argument");
 	List<String> argumentList = new LinkedList<>();
 	argumentList.add($1.sval);
 	$$ = new ParserVal(argumentList);
 }
 ;

assignment: varname EQ expression {
	//System.out.println("assignment");
	$$ = new ParserVal(new AssignmentStatement((Variable)$1.obj, (IExpression)$3.obj));
 }
 ;

varname: WORD {
	//System.out.println("varname: " + $1.sval);
	$$ = new ParserVal(new Variable($1.sval));
 }
 ;

expression: literal {
	//System.out.println("expression (literal)");
	$$ = $1;
 }
 | function_call {
 	//System.out.println("expression (function call)");
	$$ = $1;
 }
 | varname {
	//System.out.println("expression (variable expansion)");
	$$ = $1;
 }
 ;

literal: NUMERIC_LITERAL {
	//System.out.println("numeric literal: " + $1.ival);
	$$ = new ParserVal(new IntegerLiteral($1.ival));
 }
 | STRING_LITERAL {
 	//System.out.println("string literal: " + $1.sval);
 	$$ = new ParserVal(new StringLiteral($1.sval));
 }
 ;

function_call: function_name OPEN_BRACKET function_call_argument_list CLOSE_BRACKET {
	//System.out.println("Function call: " + $1.sval);
	FunctionCallExpression functionCall = new FunctionCallExpression();
	functionCall.setFunctionName($1.sval);
	functionCall.setFunctionCallArguments((List<IExpression>)$3.obj);
	$$ = new ParserVal(functionCall);
 }
 ;

function_name: WORD {
	//System.out.println("Function name: " + $1.sval);
	$$ = $1;
 }
 ;

function_call_argument_list: {
	//System.out.println("Function call arglist: empty list");
	List<IExpression> argumentList = new LinkedList<>();
        $$ = new ParserVal(argumentList);
 }
 | function_call_argument_list COMMA expression {
 	//System.out.println("Function call arglist: more args");
 	List<IExpression> argumentList = (List<IExpression>)$1.obj;
 	IExpression expression = (IExpression)$3.obj;
 	argumentList.add(expression);
 	$$ = $1;
 }
 | expression {
 	//System.out.println("Function call arglist: first arg");
	List<IExpression> argumentList = new LinkedList<>();
	IExpression expression = (IExpression)$1.obj;
	argumentList.add(expression);
	$$ = new ParserVal(argumentList);
 }
 ;
%%

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

