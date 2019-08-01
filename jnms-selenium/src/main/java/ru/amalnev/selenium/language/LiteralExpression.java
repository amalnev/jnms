package ru.amalnev.selenium.language;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.amalnev.selenium.interpreter.ExecutionContext;

@AllArgsConstructor
public abstract class LiteralExpression<T> implements IExpression
{
    @Setter
    @Getter
    private T value;

    public String toString()
    {
        return value.toString();
    }

    @Override
    public Object evaluate(final ExecutionContext context)
    {
        return value;
    }
}
