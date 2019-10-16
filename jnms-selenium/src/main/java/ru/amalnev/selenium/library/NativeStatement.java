package ru.amalnev.selenium.library;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.amalnev.selenium.interpreter.ExecutionContext;
import ru.amalnev.selenium.language.IStatement;

public class NativeStatement implements IStatement
{
    @Getter
    private String functionName;

    private Library library;

    public NativeStatement(final String functionName, final Library library)
    {
        this.functionName = functionName;
        this.library = library;
    }

    @Override
    public void execute(final ExecutionContext context)
    {
        final INativeFunction nativeFunction = library.getFunctionByName(functionName);
        nativeFunction.call(context);
    }
}
