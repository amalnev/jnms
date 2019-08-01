package ru.amalnev.selenium.language;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.amalnev.selenium.interpreter.ExecutionContext;

@AllArgsConstructor
public class ExpressionStatement extends AbstractStatement
{
    @Getter
    @Setter
    private IExpression expression;

    public String toString()
    {
        return expression.toString() + ";";
    }

    @Override
    public void execute(final ExecutionContext context)
    {
        expression.evaluate(context);
    }
}
