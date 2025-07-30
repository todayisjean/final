@web
Feature: User Login
  As a registered user
  I want to log in to the system
  So that I can access my account and use the application's features

  Scenario: Successful login with valid credentials
    Given I am on the login page
    When I enter "standard_user" in the email field
    And I enter "secret_sauce" in the password field
    And I click the login button
    Then I should be redirected to the dashboard
    And I should see a title text "Products"

  Scenario: Failed login with invalid password
    Given I am on the login page
    When I enter "testuser@example.com" in the email field
    And I enter "wrongPassword" in the password field
    And I click the login button
    Then I should see an error message "Epic sadface: Username and password do not match any user in this service"
    And I should remain on the login page

  Scenario: Failed login with empty credentials
    Given I am on the login page
    When I leave the email field empty
    And I leave the password field empty
    And I click the login button
    Then I should see an error message "Epic sadface: Username is required"
    And I should remain on the login page

