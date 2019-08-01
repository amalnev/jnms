package ru.amalnev;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.amalnev.selenium.language.FunctionDefinition;
import ru.amalnev.selenium.language.Parser;
import ru.amalnev.selenium.language.ParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class ParserTest
{
    @Parameterized.Parameter(0)
    public String fileName;

    @Parameterized.Parameters
    public static List<Object[]> parameters()
    {
        return Arrays.asList(new Object[][] {
                {"/parser-test-1.txt"},
                {"/parser-test-2.txt"}
        });
    }

    @Test
    public void parserTest() throws IOException, ParserException
    {
        final InputStream inputStream = getClass().getResourceAsStream(fileName);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        final StringBuilder testCode = new StringBuilder();
        while(reader.ready())
        {
            testCode.append(reader.readLine());
        }

        final Parser parser = new Parser();
        final FunctionDefinition entryPoint = parser.parseFromString(testCode.toString());
        System.out.println(entryPoint);
    }
}
