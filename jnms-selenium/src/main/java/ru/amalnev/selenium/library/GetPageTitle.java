package ru.amalnev.selenium.library;

import org.openqa.selenium.WebDriver;
import ru.amalnev.selenium.interpreter.ExecutionContext;

@FunctionName("get_current_page_title")
@FunctionArguments({"browser"})
public class GetPageTitle extends AbstractNativeFunction
{
    @Override
    public void call(ExecutionContext context)
    {
        final WebDriver browser = context.getLocalVariableValue("browser", WebDriver.class);
        setReturnValue(context, browser.getTitle());
    }
}
