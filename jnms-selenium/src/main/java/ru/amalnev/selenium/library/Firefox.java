package ru.amalnev.selenium.library;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.firefox.FirefoxDriver;
import ru.amalnev.selenium.interpreter.ExecutionContext;

@FunctionName("firefox")
//@FunctionArguments({"driverPath"})
public class Firefox extends AbstractNativeFunction
{
    @Override
    public void call(final ExecutionContext context)
    {
        /*final String driverPath = (String) context.getLocalVariable("driverPath").getValue();
        if(driverPath == null)
        {*/
            WebDriverManager.firefoxdriver().setup();
        /*}
        else
        {
            System.setProperty("webdriver.firefox.driver", driverPath);
        }*/

        setReturnValue(context, new FirefoxDriver());
    }
}
