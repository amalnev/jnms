package ru.amalnev.selenium.library;

import ru.amalnev.selenium.interpreter.ExecutionContext;

@FunctionName("print")
@FunctionArguments("what")
public class Print extends AbstractNativeFunction
{
    @Override
    public void call(final ExecutionContext context)
    {
        final String what = context.getLocalVariableValue("what", String.class);
        System.out.println(what);
    }
}
