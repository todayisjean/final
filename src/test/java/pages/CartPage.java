package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CartPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;

    @FindBy(className = "checkout_button")
    private WebElement checkoutButton;

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void verifyCartHasItems() {
        // Wait for cart items to appear
        wait.until(ExpectedConditions.visibilityOfAllElements(cartItems));

        if (cartItems.isEmpty()) {
            throw new AssertionError("Cart is empty");
        }
    }

    public void proceedToCheckout() {
        checkoutButton.click();
    }
}