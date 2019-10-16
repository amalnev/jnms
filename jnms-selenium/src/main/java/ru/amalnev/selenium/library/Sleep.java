package ru.amalnev.selenium.library;

import ru.amalnev.selenium.interpreter.ExecutionContext;

@FunctionName("sleep")
@FunctionArguments({"duration"})
public class Sleep extends AbstractNativeFunction
{
    @Override
    public void call(ExecutionContext context)
    {
        final Integer duration = context.getLocalVariableValue("duration", Integer.class);
        try
        {
            Thread.sleep(duration);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
