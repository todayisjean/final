@web
Feature: Checkout Process
  As a customer
  I want to complete the checkout process
  So that I can purchase my selected items

  Background:
    Given I have logged in with default credentials
    And I am on the products page
    When I add the following items to my cart:
      | Item                    | Quantity |
      | Sauce Labs Backpack     | 1        |
      | Sauce Labs Bolt T-Shirt | 1        |
    And I go to the carts page
    Then I should see the products on the cart

  Scenario: Successful checkout with valid payment details
    Given I am on the checkout page
    When I enter my shipping information:
      | First Name  | John             |
      | Last Name   | Doe              |
      | ZIP Code    | 10001            |
    And I press the Continue button
    And I press Finish
    Then I should see the order confirmation page