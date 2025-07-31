package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProductsPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void verifyOnProductsPage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".inventory_list")));
    }

    public void addProductToCart(String productName, int quantity) {
        // Find product container by name
        String xpath = String.format(
                "//div[@class='inventory_item_name' and text()='%s']/ancestor::div[@class='inventory_item']",
                productName
        );
        WebElement productContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(xpath)
        ));

        // Find add to cart button within the container
        WebElement addButton = productContainer.findElement(By.cssSelector("button"));
        String initialButtonText = addButton.getText();

        // Add item multiple times
        for (int i = 0; i < quantity; i++) {
            addButton.click();

            // Wait for button state to change
            wait.until(ExpectedConditions.not(
                    ExpectedConditions.textToBePresentInElement(addButton, initialButtonText)
            ));

            // For subsequent clicks, refresh the button reference
            if (i < quantity - 1) {
                addButton = productContainer.findElement(By.cssSelector("button"));
            }
        }
    }

    public void goToCart() {
        driver.findElement(By.cssSelector(".shopping_cart_link")).click();
    }
}
