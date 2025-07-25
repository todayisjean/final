package util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverManager {
    private static WebDriver _driver;

    public static WebDriver getDriver() {
        if(_driver == null) {
            var option = new ChromeOptions();
            option.addArguments("headless");
            _driver = new ChromeDriver(option);
        }
        return _driver;
    }

    public static void quitDriver() {
        _driver.quit();
        _driver = null;
    }
}
