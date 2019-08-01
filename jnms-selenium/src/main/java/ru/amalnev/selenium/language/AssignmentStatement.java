package ru.amalnev.selenium.language;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.amalnev.selenium.interpreter.ExecutionContext;
import ru.amalnev.selenium.interpreter.LocalVariable;

@Getter
@Setter
@AllArgsConstructor
public class AssignmentStatement extends AbstractStatement
{
    private Variable variable;

    private IExpression expression;

    public String toString()
    {
        return variable.toString() + " = " + expression.toString() + ";";
    }

    @Override
    public void execute(final ExecutionContext context)
    {
        if (!context.isVariableDefined(variable.getName()))
            context.defineLocalVariable(variable.getName());

        final LocalVariable localVariable = context.getLocalVariable(variable.getName());
        localVariable.setValue(expression.evaluate(context));
    }
}
