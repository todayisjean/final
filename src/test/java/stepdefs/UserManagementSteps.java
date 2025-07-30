package stepdefs;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.example.UserData;
import org.junit.jupiter.api.Tag;
import util.ContextManager;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.junit.jupiter.api.Assertions.*;
@Tag("api")
public class UserManagementSteps {
    private final Type jsonToMapType = new TypeToken<Map<String, String>>() {
    }.getType();
    private final Type jsonToUserDataType = new TypeToken<UserData>() {
    }.getType();
    String baseUrl;
    Gson gson = new Gson();

    Map<String, String> userData;
    Map<String, String> invalidData;
    Map<String, String> updateData;

    String userId = "";
    Response response;
    String responseBody;
    RequestSpecification request;


    /// ////////////////////////////////////////////////
    /// Given - Data disimpan di client
    /// ////////////////////////////////////////////////
    @Given("the API base URL is {string}")
    public void api_baseurl(String url) {
        baseUrl = url;
        RestAssured.baseURI = url;
        request = given();
    }

    @Given("I set the {string} header to {string}")
    public void setHeader(String header, String type) {
        request.header(header, type);
    }

    @Given("a user exists with ID {string}")
    public void userExist(String userID) {
        userId = userID;
    }

    @Given("I have the following user data:")
    public void setUserData(DataTable userData) {
        this.userData = userData.asMap();
    }

    @Given("I have the following update data:")
    public void setUpdateData(Map<String, String> userData) {
        this.updateData = userData;
    }

    @Given("I have the following invalid user data:")
    public void setInvalidUserData(Map<String, String> userData) {
        this.invalidData = userData;
    }


    /// ////////////////////////////////////////////////
    /// When - Data yang dikirim ke server
    /// ////////////////////////////////////////////////
    @When("I send a GET request to {string}")
    public void sendGetRequest(String endpoint) throws IOException {
        response = request.when()
                .get(endpoint)
                .then()
                .statusCode(200)
                .extract()
                .response();
        responseBody = response.getBody().asString();
    }

    @Then("a GET request to {string} with the last created ID should return {int}")
    public void sendGetRequestLastCreatedID(String endpoint, int expectedStatusCode) throws IOException {
        String createdUserId = ContextManager.get("createdUserId").toString();
        response = request.when()
                .get(endpoint + createdUserId)
                .then()
                .extract()
                .response();
        responseBody = response.getBody().asString();
        assertNotEquals(createdUserId, emptyOrNullString());
        assertEquals(expectedStatusCode, response.statusCode());
    }

    @Then("a GET request to {string} should return {int}")
    public void a_get_request_to_should_return(String endpoint, Integer expectedCode) throws IOException {

        response = request
                .when()
                .get(endpoint)
                .then()
                .statusCode(expectedCode)
                .extract()
                .response();

        assertEquals(expectedCode, response.statusCode());
    }
    @When("I send a DELETE request to {string}")
    public void sendDeleteRequest(String endpoint) throws IOException {
        response = request
                .when()
                .delete(endpoint)
                .then()
                .extract()
                .response();
        responseBody = response.getBody().asString();
    }

    @When("I send a DELETE request to {string} with the latest created ID")
    public void sendDeleteRequestWithID(String endpoint) throws IOException {
        String createdUserId = ContextManager.get("createdUserId").toString();
        var url = endpoint + createdUserId;
        response = request
                .when()
                .delete(url)
                .then()
                .extract()
                .response();
        responseBody = response.getBody().asString();
    }

    @When("I send a POST request to {string} with the user data")
    public void sendPostRequest(String endpoint) throws IOException {
        String jsonData = gson.toJson((userData));
        response = request
                .and()
                .body(jsonData)
                .when()
                .post(endpoint)
                .then()
                .body("error", emptyOrNullString())
                .extract()
                .response();
        responseBody = response.getBody().asString();

    }

    @When("I send a PUT request to {string} with the update data")
    public void i_send_a_patch_request_to_with_the_update_data(String endpoint) throws IOException {
        response = request
                .when()
                .body(gson.toJson(updateData))
                .put(endpoint)
                .then()
                .statusCode(200)
                .extract()
                .response();
        responseBody = response.getBody().asString();
    }

    @When("I send a POST request to {string} with the invalid data")
    public void i_send_a_post_request_to_with_the_invalid_data(String endpoint) throws IOException {
        response = request
                .when()
                .body(gson.toJson(invalidData))
                .post(endpoint)
                .then()
                .extract()
                .response();
        responseBody = response.getBody().asString();
    }


    /// ////////////////////////////////////////////////
    /// Then - Validasi data yang diperoleh
    /// ////////////////////////////////////////////////
    @Then("the response should contain a valid user ID")
    public void verifyValidUser() throws IOException {
        var json = gson.fromJson(responseBody, JsonObject.class);
        assertEquals(true, json.keySet().contains("id"));
        var createdUserId = json.get("id").getAsString();
        ContextManager.set("createdUserId", createdUserId);
    }

    @Then("the response should match the JSON schema:")
    public void the_response_should_match_the_json_schema(String schema) throws IOException {
        JsonElement jsonElement = JsonParser.parseString(responseBody);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        Set<String> keys = jsonObject.keySet();
        assertTrue(keys.contains("id"));
        assertTrue(keys.contains("firstName"));
        assertTrue(keys.contains("lastName"));
    }

    @Then("the response status code should be {int}")
    public void verifyStatusCode(int expectedStatusCode) {
        assertEquals(expectedStatusCode, response.statusCode());
    }

    @Then("the response body should contain:")
    public void verifyBody(String expectedFieldsJson) throws IOException {
        // Konversi string menjadi map
        JsonObject json = gson.fromJson(responseBody, JsonObject.class);
        JsonObject expectedJson = gson.fromJson(responseBody, JsonObject.class);
        var result = json.entrySet().containsAll(expectedJson.entrySet());
        assertEquals(true, result);
    }

    @Then("the response should contain validation errors:")
    public void the_response_should_contain_validation_errors(io.cucumber.datatable.DataTable dataTable) {
        // Konversi string menjadi map
        JsonObject json = gson.fromJson(responseBody, JsonObject.class);
        assertEquals(true, json.keySet().contains("error"));
    }


}
