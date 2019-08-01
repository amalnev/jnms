package ru.amalnev;

import ru.amalnev.selenium.interpreter.Program;
import ru.amalnev.selenium.language.ParserException;

import java.io.IOException;

public class App
{
    public static void main(String[] args) throws IOException, ParserException
    {
        Program.runFromFile(args[0]);
    }
}
