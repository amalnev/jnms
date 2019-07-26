package ru.amalnev;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Hello world!
 */
public class App
{
    public static void main(String[] args)
    {
        WebDriverManager.firefoxdriver().setup();
        WebDriver webDriver = new FirefoxDriver();

        try
        {
            webDriver.get("http://localhost:8189/jnms/login");

            System.out.println(webDriver.getTitle());

            webDriver.findElement(By.id("username")).sendKeys("root");
            webDriver.findElement(By.id("password")).sendKeys("root");
            webDriver.findElement(By.cssSelector(".btn")).click();

            System.out.println(webDriver.getTitle());

            webDriver.findElement(By.cssSelector("li:nth-child(14) .icon")).click();
            System.out.println(webDriver.getTitle());
        }
        finally
        {
            webDriver.close();
        }
    }
}
