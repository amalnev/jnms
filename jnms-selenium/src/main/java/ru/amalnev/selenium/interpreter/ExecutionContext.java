package ru.amalnev.selenium.interpreter;

import ru.amalnev.selenium.language.FunctionDefinition;
import ru.amalnev.selenium.language.IStatement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExecutionContext
{
    private CallStack callStack = new CallStack();

    private Map<String, FunctionDefinition> functionDefinitions = new HashMap<>();

    public LocalVariable defineLocalVariable(final String name)
    {
        return callStack.defineLocalVariable(name);
    }

    public LocalVariable defineLocalVariable(final String name, final Object value)
    {
        return callStack.defineLocalVariable(name, value);
    }

    public LocalVariable getLocalVariable(final String name)
    {
        return callStack.getLocalVariable(name);
    }

    public <T> T getLocalVariableValue(final String name, final Class<T> valueType)
    {
        return (T) callStack.getLocalVariable(name).getValue();
    }

    public void defineFunction(final FunctionDefinition functionDefinition)
    {
        final String functionName = functionDefinition.getFunctionName();
        if (functionDefinitions.get(functionName) != null)
            throw new InterpreterException("Redefinition: " + functionName);

        functionDefinitions.put(functionName, functionDefinition);
    }

    public List<IStatement> enterFunction(final String functionName, Object... args)
    {
        final FunctionDefinition functionDefinition = functionDefinitions.get(functionName);
        if (functionDefinition == null)
            throw new InterpreterException(functionName + " is not defined");

        callStack.enterFunction(functionDefinition, args);
        return functionDefinition.getStatements();
    }

    public Object exitFunction()
    {
        return callStack.exitFunction();
    }

    public boolean isVariableDefined(final String name)
    {
        return callStack.isVariableDefined(name);
    }
}
