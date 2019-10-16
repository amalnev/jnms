package ru.amalnev.selenium.library;

import org.openqa.selenium.WebElement;
import ru.amalnev.selenium.interpreter.ExecutionContext;

@FunctionName("send_keys")
@FunctionArguments({"element", "keys"})
public class SendKeys extends AbstractNativeFunction
{
    @Override
    public void call(ExecutionContext context)
    {
        final WebElement element = context.getLocalVariableValue("element", WebElement.class);
        final String keys = context.getLocalVariableValue("keys", String.class);
        element.sendKeys(keys);
    }
}
