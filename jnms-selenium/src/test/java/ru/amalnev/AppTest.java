package ru.amalnev;

import static org.junit.Assert.assertTrue;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class AppTest
{
    /**
     * Пытаемся сделать login/logout
     */
    @Test
    public void loginLogoutTest()
    {
        //Запускаем FireFox
        WebDriverManager.firefoxdriver().setup();
        WebDriver webDriver = new FirefoxDriver();

        try
        {
            //Переходим на страницу ввода учетных данных
            webDriver.get("http://localhost:8189/jnms/login");

            //Проверяем заголовок страницы
            Assert.assertEquals(webDriver.getTitle(), "Please sign in");

            //Заполняем поля и нажимаем кнопку "Войти"
            webDriver.findElement(By.id("username")).sendKeys("root");
            webDriver.findElement(By.id("password")).sendKeys("root");
            webDriver.findElement(By.cssSelector(".btn")).click();

            //Если успешно зашли - попадаем на начальную страницу приложения, ее заголовок будет "jNMS"
            Assert.assertEquals(webDriver.getTitle(), "jNMS");

            //Нажимаем кнопку logout
            webDriver.findElement(By.cssSelector("li:nth-child(14) .icon")).click();

            //Проверяем заголовок страницы
            Assert.assertEquals(webDriver.getTitle(), "Please sign in");
        }
        finally
        {
            //К сожалению WebDriver не AutoCloseable
            webDriver.close();
        }
    }
}
