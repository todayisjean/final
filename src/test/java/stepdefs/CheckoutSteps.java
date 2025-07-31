package stepdefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.*;
import util.DriverManager;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class CheckoutSteps {
    private final WebDriver driver;
    private final LoginPage loginPage;
    private final ProductsPage productsPage;
    private final CartPage cartPage;
    private final CheckoutPage checkoutPage;
    private final ConfirmationPage confirmationPage;

    public CheckoutSteps() {
        driver = DriverManager.getDriver();
        loginPage = new LoginPage(driver);
        productsPage = new ProductsPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
        confirmationPage = new ConfirmationPage(driver);
    }

    @Given("I have logged in with default credentials")
    public void iHaveLoggedInWithDefaultCredentials() {
        loginPage.navigateTo();
        loginPage.login("standard_user", "secret_sauce");
    }

    @Given("I am on the products page")
    public void iAmOnTheProductsPage() {
        productsPage.verifyOnProductsPage();
    }

    @When("I add the following items to my cart:")
    public void iAddTheFollowingItemsToMyCart(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> items = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> item : items) {
            String productName = item.get("Item");
            int quantity = Integer.parseInt(item.get("Quantity"));
            productsPage.addProductToCart(productName, quantity);
        }
    }

    @And("I go to the carts page")
    public void iGoToTheCartsPage() {
        productsPage.goToCart();
    }

    @Then("I should see the products on the cart")
    public void iShouldSeeTheProductsOnTheCart() {
        cartPage.verifyCartHasItems();
    }

    @Given("I am on the checkout page")
    public void iAmOnTheCheckoutPage() {
        cartPage.proceedToCheckout();
    }

    @When("I enter my shipping information:")
    public void iEnterMyShippingInformation(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> shippingInfo = dataTable.asMap();
        checkoutPage.enterShippingDetails(
                shippingInfo.get("First Name"),
                shippingInfo.get("Last Name"),
                shippingInfo.get("ZIP Code")
        );
    }

    @And("I press the Continue button")
    public void iPressTheContinueButton() {
        checkoutPage.continueToOverview();
    }

    @And("I press Finish")
    public void iPressFinish() {
        checkoutPage.finishCheckout();
    }

    @Then("I should see the order confirmation page")
    public void iShouldSeeTheOrderConfirmationPage() {
        confirmationPage.verifyConfirmationDisplayed();
    }
}
