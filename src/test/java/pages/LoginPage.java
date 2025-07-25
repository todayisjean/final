package pages;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

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

    public void setEmail(String value) {
        emailField.clear();;
        emailField.sendKeys(value);
    }

    public void setPassword(String value) {
        passwordField.clear();
        passwordField.sendKeys(value);
    }
}
