package ru.amalnev.selenium.interpreter;

import lombok.Getter;
import lombok.Setter;
import ru.amalnev.selenium.language.FunctionDefinition;

import java.util.HashMap;
import java.util.Map;

public class StackFrame
{
    @Getter
    @Setter
    private String functionName;

    private Map<String, LocalVariable> localVariables = new HashMap<>();

    public boolean isVariableDefined(final String name)
    {
        return localVariables.get(name) != null;
    }

    public LocalVariable getLocalVariable(final String name)
    {
        final LocalVariable localVariable = localVariables.get(name);
        if (localVariable == null)
            throw new InterpreterException("Undefined variable: " + name);

        return localVariable;
    }

    public LocalVariable defineLocalVariable(final String name)
    {
        if (localVariables.get(name) != null) throw new InterpreterException("Redefinition: " + name);
        final LocalVariable localVariable = new LocalVariable(name);
        localVariables.put(name, localVariable);
        return localVariable;
    }

    public LocalVariable defineLocalVariable(final String name, final Object value)
    {
        final LocalVariable localVariable = defineLocalVariable(name);
        localVariable.setValue(value);
        return localVariable;
    }

    public static StackFrame makeFrameForFunctionCall(final FunctionDefinition functionDefinition,
                                                      Object... args)
    {
        final StackFrame stackFrame = new StackFrame();
        stackFrame.setFunctionName(functionDefinition.getFunctionName());
        if (args.length != functionDefinition.getArgumentNames().size())
            throw new InterpreterException(functionDefinition.getFunctionName() + ": wrong number of args");

        for (int i = 0; i < args.length; i++)
        {
            stackFrame.defineLocalVariable(functionDefinition.getArgumentNames().get(i), args[i]);
        }

        stackFrame.defineLocalVariable(functionDefinition.getFunctionName());
        return stackFrame;
    }
}
