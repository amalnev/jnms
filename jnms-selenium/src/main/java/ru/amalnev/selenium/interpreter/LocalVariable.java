package ru.amalnev.selenium.interpreter;

import lombok.Getter;
import lombok.Setter;
import ru.amalnev.selenium.language.Variable;

@Getter
@Setter
public class LocalVariable
{
    private Variable variable;

    private Object value;

    public LocalVariable(final String name)
    {
        variable = new Variable(name);
    }
}
