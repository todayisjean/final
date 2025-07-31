package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ConfirmationPage {
    private WebDriver driver;

    @FindBy(className = "complete-header")
    private WebElement confirmationHeader;

    public ConfirmationPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void verifyConfirmationDisplayed() {
        if (!confirmationHeader.getText().contains("THANK YOU")) {
            throw new AssertionError("Order confirmation not displayed");
        }
    }
}