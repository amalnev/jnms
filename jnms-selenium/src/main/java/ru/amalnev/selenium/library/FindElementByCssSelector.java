package ru.amalnev.selenium.library;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import ru.amalnev.selenium.interpreter.ExecutionContext;

@FunctionName("find_element_by_css_selector")
@FunctionArguments({"browser", "selector"})
public class FindElementByCssSelector extends AbstractNativeFunction
{
    @Override
    public void call(ExecutionContext context)
    {
        final WebDriver browser = context.getLocalVariableValue("browser", WebDriver.class);
        final String selector = context.getLocalVariableValue("selector", String.class);
        setReturnValue(context, browser.findElement(By.cssSelector(selector)));
    }
}
