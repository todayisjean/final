package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import util.DriverManager;

public class BasePage {
    private WebDriver driver;
    public BasePage() {
        driver = DriverManager.getDriver();
        PageFactory.initElements(driver, this);
    }
}
