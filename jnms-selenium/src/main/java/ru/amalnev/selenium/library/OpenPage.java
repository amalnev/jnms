package ru.amalnev.selenium.library;

import org.openqa.selenium.WebDriver;
import ru.amalnev.selenium.interpreter.ExecutionContext;

@FunctionName("open_page")
@FunctionArguments({"browser", "url"})
public class OpenPage extends AbstractNativeFunction
{
    @Override
    public void call(ExecutionContext context)
    {
        final WebDriver webDriver = context.getLocalVariableValue("browser", WebDriver.class);
        final String url = context.getLocalVariableValue("url", String.class);
        webDriver.get(url);
    }
}
