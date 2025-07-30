@api
Feature: User Management API
  As an API consumer
  I want to interact with the user management endpoints
  So that I can verify the system's user management capabilities

  Background:
    Given the API base URL is "https://dummyapi.io/data/v1"
    And I set the "Content-Type" header to "application/json"
    And I set the "app-id" header to "63a804408eb0cb069b57e43a"

  Scenario: Create a new user
    Given I have the following user data:
      | firstName| Michael                  |
      | lastName | Lawson                   |
      | email    | mic6jkjjj4sa2g2ahsjsd@example.com|
    When I send a POST request to "/user/create" with the user data
    Then the response status code should be 200
    And the response should contain a valid user ID
    And the response should match the JSON schema:
      """
      {
        "type": "object",
        "required": ["id", "firstName", "lastName"],
        "properties": {
          "id": {"type": "string"},
          "firstName": {"type": "string"},
          "lastName": {"type": "string"},
        }
      }
      """

  Scenario Outline: Retrieve user information
    Given a user exists with ID "60d0fe4f5311236168a10a0e"
    When I send a GET request to "/user/60d0fe4f5311236168a10a0e"
    Then the response status code should be 200
    And the response body should contain:
      """
      {
        "id": "60d0fe4f5311236168a10a0e",
        "firstName": "Mads",
        "lastName": "Andersen"
      }
      """

    Examples:
      | user_id                          |
      | 60d0fe4f5311236168a10a0e         |
      | 68888c71e181cd27f1466302         |

  Scenario: Update user information
    Given a user exists with ID "60d0fe4f5311236168a10a0c"
    And I have the following update data:
      | firstName | Jonathan       |
      | lastName  | Han            |
    When I send a PUT request to "/user/60d0fe4f5311236168a10a0c" with the update data
    Then the response status code should be 200
    And the response body should contain:
      """
      {
        "firstName": "Jonathan",
        "lastName": "Han",
      }
      """

  Scenario: Delete a user
    When I send a DELETE request to "/user/" with the latest created ID
    Then the response status code should be 200

  Scenario: Attempt to create user with invalid data
    Given I have the following invalid user data:
      | fullName  | Michael Lawson            |
      | email      | michaelLawson12@gmail.com |
    When I send a POST request to "/user/create" with the invalid data
    Then the response status code should be 400
    And the response should contain validation errors:
      | error | Missing password |