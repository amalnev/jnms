package ru.amalnev.selenium.library;

import org.openqa.selenium.WebElement;
import ru.amalnev.selenium.interpreter.ExecutionContext;

@FunctionName("get_attribute_value")
@FunctionArguments({"element", "attribute_name"})
public class GetAttribute extends AbstractNativeFunction
{
    @Override
    public void call(ExecutionContext context)
    {
        final WebElement element = context.getLocalVariableValue("element", WebElement.class);
        final String attributeName = context.getLocalVariableValue("attribute_name", String.class);
        setReturnValue(context, element.getAttribute(attributeName));
    }
}
