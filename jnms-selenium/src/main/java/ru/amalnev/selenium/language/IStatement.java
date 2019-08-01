package ru.amalnev.selenium.language;

import ru.amalnev.selenium.interpreter.ExecutionContext;

public interface IStatement
{
    void execute(ExecutionContext context);
}
