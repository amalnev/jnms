package ru.amalnev.selenium.library;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import ru.amalnev.selenium.interpreter.ExecutionContext;

@FunctionName("find_element_by_id")
@FunctionArguments({"browser", "id"})
public class FindElementById extends AbstractNativeFunction
{
    @Override
    public void call(ExecutionContext context)
    {
        final WebDriver browser = context.getLocalVariableValue("browser", WebDriver.class);
        final String elementId = context.getLocalVariableValue("id", String.class);
        setReturnValue(context, browser.findElement(By.id(elementId)));
    }
}
