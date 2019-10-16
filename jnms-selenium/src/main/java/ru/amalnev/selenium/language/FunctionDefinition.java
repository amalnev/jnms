package ru.amalnev.selenium.language;

import lombok.Getter;
import lombok.Setter;
import ru.amalnev.selenium.interpreter.ExecutionContext;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class FunctionDefinition extends AbstractStatement
{
    private String functionName;

    private List<String> argumentNames = new LinkedList<>();

    private List<IStatement> statements = new LinkedList<>();

    public String toString()
    {
        final StringBuilder builder = new StringBuilder();
        if (functionName != null && !functionName.isEmpty())
        {
            builder.append("function ");
            builder.append(functionName);
            builder.append("(");
            for (int i = 0; i < argumentNames.size(); i++)
            {
                builder.append(argumentNames.get(i));
                if (i != argumentNames.size() - 1)
                {
                    builder.append(", ");
                }
            }

            builder.append(")\n{\n");
        }

        statements.forEach(statement -> {
            builder.append(statement);
            builder.append("\n");
        });

        if (functionName != null && !functionName.isEmpty())
        {
            builder.append("};");
        }

        return builder.toString();
    }

    @Override
    public void execute(final ExecutionContext context)
    {
        context.defineFunction(this);
    }
}
