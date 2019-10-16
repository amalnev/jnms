package ru.amalnev.selenium.library;

import ru.amalnev.selenium.interpreter.ExecutionContext;
import ru.amalnev.selenium.language.FunctionDefinition;

public abstract class AbstractNativeFunction extends FunctionDefinition implements INativeFunction
{
    protected void setReturnValue(final ExecutionContext context, final Object returnValue)
    {
        final String functionName = getClass().getAnnotation(FunctionName.class).value();
        context.getLocalVariable(functionName).setValue(returnValue);
    }
}
