package ru.amalnev.selenium.interpreter;

import ru.amalnev.selenium.language.FunctionCallExpression;
import ru.amalnev.selenium.language.FunctionDefinition;
import ru.amalnev.selenium.language.Parser;
import ru.amalnev.selenium.language.ParserException;
import ru.amalnev.selenium.library.Library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Program
{
    public static void runFromString(final String sourceCode) throws IOException, ParserException
    {
        final Parser parser = new Parser();
        final FunctionDefinition entryPoint = parser.parseFromString(sourceCode);
        final ExecutionContext executionContext = new ExecutionContext();

        final Library library = new Library();
        library.getNativeFunctions().forEach(executionContext::defineFunction);

        executionContext.defineFunction(entryPoint);

        final FunctionCallExpression entryPointCall = new FunctionCallExpression();
        entryPointCall.setFunctionName(entryPoint.getFunctionName());
        entryPointCall.evaluate(executionContext);
    }

    public static void runFromFile(final String sourceFilePath) throws IOException, ParserException
    {
        final InputStream inputStream = Program.class.getResourceAsStream(sourceFilePath);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        final StringBuilder sourceCode = new StringBuilder();
        while (reader.ready())
        {
            sourceCode.append(reader.readLine());
        }

        runFromString(sourceCode.toString());
    }
}
