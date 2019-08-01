package ru.amalnev.selenium.language;

import ru.amalnev.selenium.interpreter.ExecutionContext;

public interface IExpression
{
    Object evaluate(ExecutionContext context);
}
