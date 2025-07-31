package pages;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage extends BasePage {
    private WebDriver driver;
    @FindBy(id = "user-name")
    public WebElement emailField;

    @FindBy(id = "password")
    public WebElement passwordField;

    @FindBy(css = "input[type='submit']")
    public WebElement loginButton;

    public void pressLoginButton() {
        loginButton.click();
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void setEmail(String value) {
        emailField.clear();;
        emailField.sendKeys(value);
    }

    public void setPassword(String value) {
        passwordField.clear();
        passwordField.sendKeys(value);
    }

    public void navigateTo() {
        driver.navigate().to("https://www.saucedemo.com/v1/index.html");
    }


    public void login(String username, String password) {
        emailField.sendKeys(username);
        passwordField.sendKeys(password);
        loginButton.click();

        // Wait for products page to load
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("inventory"));
    }
}
