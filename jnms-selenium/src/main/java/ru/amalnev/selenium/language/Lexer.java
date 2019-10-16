package ru.amalnev.selenium.language;

import java.io.*;

public class Lexer
{
    private final StreamTokenizer tokenizer;

    private final Parser parser;

    public Lexer(final Parser parser, final String inputString)
    {
        final Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(inputString.getBytes())));
        tokenizer = new StreamTokenizer(reader);
        this.parser = parser;

        tokenizer.eolIsSignificant(false);
        tokenizer.parseNumbers();
        tokenizer.quoteChar(34); //"
        tokenizer.slashSlashComments(true);
        tokenizer.slashStarComments(true);
        tokenizer.wordChars(97, 122); //a-z
        tokenizer.wordChars(65, 90);  //A-Z
        tokenizer.wordChars(48, 57); //0-9
        tokenizer.wordChars(95, 95); //_
        tokenizer.ordinaryChar(61); //=
        tokenizer.ordinaryChar(59); //;
        tokenizer.ordinaryChar(40); //(
        tokenizer.ordinaryChar(41); //)
        tokenizer.ordinaryChar(44); //,
        tokenizer.ordinaryChar(123); //{
        tokenizer.ordinaryChar(125); //}
    }

    int yylex() throws IOException
    {
        int token = tokenizer.nextToken();
        if (token == StreamTokenizer.TT_WORD)
        {
            if ("function".equals(tokenizer.sval))
            {
                return Parser.FUNCTION_KEYWORD;
            }

            parser.yylval = new ParserVal(tokenizer.sval);
            return Parser.WORD;
        }
        else if (token == StreamTokenizer.TT_NUMBER)
        {
            parser.yylval = new ParserVal((int) tokenizer.nval);
            return Parser.NUMERIC_LITERAL;
        }
        else if (token == 61)
        {
            return Parser.EQ;
        }
        else if (token == 59)
        {
            return Parser.SEMICOLON;
        }
        else if (token == 40)
        {
            return Parser.OPEN_BRACKET;
        }
        else if (token == 41)
        {
            return Parser.CLOSE_BRACKET;
        }
        else if (token == 44)
        {
            return Parser.COMMA;
        }
        else if (token == 123)
        {
            return Parser.OPEN_CURLY_BRACKET;
        }
        else if (token == 125)
        {
            return Parser.CLOSE_CURLY_BRACKET;
        }
        else if (token == 34)
        {
            parser.yylval = new ParserVal(tokenizer.sval);
            return Parser.STRING_LITERAL;
        }
        else if (token == StreamTokenizer.TT_EOF)
        {
            return 0;
        }

        return 0;
    }
}
