Feature: User Management API
  As an API consumer
  I want to interact with the user management endpoints
  So that I can verify the system's user management capabilities

  Background:
    Given the API base URL is "https://reqres.in/api"
    And I set the "Content-Type" header to "application/json"
    And I set the "x-api-key" header to "reqres-free-v1"

  Scenario: Create a new user
    Given I have the following user data:
      | name      | Michael Lawson       |
      | job       | IT                   |
    When I send a POST request to "/users" with the user data
    Then the response status code should be 201
    And the response should contain a valid user ID
    And the response should match the JSON schema:
      """
      {
        "type": "object",
        "required": ["job", "name"],
        "properties": {
          "job": {"type": "string"},
          "name": {"type": "string"},
        }
      }
      """

  Scenario Outline: Retrieve user information
    Given a user exists with ID "7"
    When I send a GET request to "/users/7"
    Then the response status code should be 200
    And the response body should contain:
      """
      {
        "id": "7",
        "firstName": "Michael",
        "lastName": "Lawson"
      }
      """

    Examples:
      | user_id   |
      | 7         |
      | 8         |

  Scenario: Update user information
    Given a user exists with ID "2"
    And I have the following update data:
      | name | Jonathan       |
      | job  | Office Boy     |
    When I send a PATCH request to "/users/2" with the update data
    Then the response status code should be 200
    And the response body should contain:
      """
      {
        "name": "Jonathan",
        "job": "Office Boy"
      }
      """

  Scenario: Delete a user
    Given a user exists with ID "23"
    When I send a DELETE request to "/users/23"
    Then the response status code should be 204
    And a GET request to "/users/23" should return 404

  Scenario: Attempt to create user with invalid data
    Given I have the following invalid user data:
      | fullName  | Michael Lawson       |
      | jeb       | IT                   |
    When I send a POST request to "/register" with the invalid data
    Then the response status code should be 400
    And the response should contain validation errors:
      | error | Missing password |