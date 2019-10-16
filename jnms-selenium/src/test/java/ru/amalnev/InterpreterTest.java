package ru.amalnev;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.amalnev.selenium.interpreter.Program;
import ru.amalnev.selenium.language.ParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class InterpreterTest
{
    @Parameterized.Parameter(0)
    public String fileName;

    @Parameterized.Parameters
    public static List<Object[]> parameters()
    {
        return Arrays.asList(new Object[][] {
                /*{"/interpreter-test-1.txt"},
                {"/start-stop-firefox.txt"},*/
                {"/yandex-search.txt"}
        });
    }

    @Test
    public void interpreterTest() throws IOException, ParserException
    {
        final InputStream inputStream = getClass().getResourceAsStream(fileName);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        final StringBuilder testCode = new StringBuilder();
        while(reader.ready())
        {
            testCode.append(reader.readLine());
        }

        Program.runFromString(testCode.toString());
    }
}
