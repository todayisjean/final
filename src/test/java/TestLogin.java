import io.cucumber.java.After;
import io.cucumber.java.en.*;
import org.junit.platform.suite.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import static io.cucumber.core.options.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

class TestLogin {
    private WebDriver driver;
    private WebDriverWait wait;

    @Given("I am on the login page")
    public void i_am_on_the_login_page() {
        // Initialize WebDriver (configure your own WebDriver setup)
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("https://your-app-url.com/login");
        assertEquals("Login Page", driver.getTitle());
    }

    @When("I enter {string} in the email field")
    public void i_enter_in_the_email_field(String email) {
        WebElement emailField = driver.findElement(By.id("email"));
        emailField.clear();
        emailField.sendKeys(email);
    }

    @When("I enter {string} in the password field")
    public void i_enter_in_the_password_field(String password) {
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    @When("I click the login button")
    public void i_click_the_login_button() {
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    @Then("I should be redirected to the dashboard")
    public void i_should_be_redirected_to_the_dashboard() {
        wait.until(ExpectedConditions.urlContains("/dashboard"));
        assertEquals("Dashboard", driver.getTitle());
    }

    @Then("I should see a welcome message containing {string}")
    public void i_should_see_a_welcome_message_containing(String welcomeText) {
        WebElement welcomeMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".welcome-message"))
        );
        assertTrue(welcomeMessage.getText().contains(welcomeText));
    }

    @Then("I should see an error message {string}")
    public void i_should_see_an_error_message(String errorMessage) {
        WebElement errorElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert-error"))
        );
        assertEquals(errorMessage, errorElement.getText());
    }

    @Then("I should remain on the login page")
    public void i_should_remain_on_the_login_page() {
        assertTrue(driver.getCurrentUrl().endsWith("/login"));
    }

    @Then("I should see a {string} link")
    public void i_should_see_a_link(String linkText) {
        WebElement link = driver.findElement(By.linkText(linkText));
        assertTrue(link.isDisplayed());
    }

    @When("I click the {string} link")
    public void i_click_the_link(String linkText) {
        driver.findElement(By.linkText(linkText)).click();
    }

    @Then("I should be redirected to the password reset page")
    public void i_should_be_redirected_to_the_password_reset_page() {
        wait.until(ExpectedConditions.urlContains("/reset-password"));
        assertEquals("Reset Password", driver.getTitle());
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}