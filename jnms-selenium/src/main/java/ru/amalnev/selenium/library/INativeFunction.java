package ru.amalnev.selenium.library;

import ru.amalnev.selenium.interpreter.ExecutionContext;

public interface INativeFunction
{
    void call(ExecutionContext context);
}
