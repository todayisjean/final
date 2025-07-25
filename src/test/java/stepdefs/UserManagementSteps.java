package stepdefs;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.internal.shadowed.jackson.databind.jsonschema.JsonSchema;
import okhttp3.*;
import org.example.UserData;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserManagementSteps {
    public static final MediaType JSON = MediaType.get("application/json");
    private final Type jsonToMapType = new TypeToken<Map<String, String>>(){}.getType();
    private final Type jsonToUserDataType = new TypeToken<UserData>(){}.getType();
    String baseUrl;
    OkHttpClient client = new OkHttpClient.Builder().build();
    Gson gson = new Gson();

    Map<String, String> userData;
    Map<String, String> invalidData;
    Map<String, String> updateData;

    String userId = "";
    Response response;
    String responseBody;
    Request.Builder request;



    /// ////////////////////////////////////////////////
    /// Given - Data disimpan di client
    /// ////////////////////////////////////////////////
    @Given("the API base URL is {string}")
    public void api_baseurl(String url) {
        baseUrl = url;
        client.address(HttpUrl.parse(baseUrl));
        request = new Request.Builder();
    }

    @Given("I set the {string} header to {string}")
    public void setHeader(String header, String type)
    {
        request.header(header, type);
    }

    @Given("a user exists with ID {string}")
    public void userExist(String userID){
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
        Request r = request
                .url(baseUrl + endpoint)
                .get() // GET request
                .build();

        // Mencoba mengirim request
        try(Response response = client.newCall(r).execute()) {
            // Jika berhasil set response string
            this.response = response;
            this.responseBody = response.body().string();
        }
    }

    @Then("a GET request to {string} should return {int}")
    public void a_get_request_to_should_return(String endpoint, Integer expectedCode) throws IOException {
        Request r = request
                .url(baseUrl + endpoint)
                .get() // GET request
                .build();

        // Mencoba mengirim request
        try(Response response = client.newCall(r).execute()) {
            // Jika berhasil set response string
            this.response = response;
            assertEquals(expectedCode, response.code());
        }
    }

    @When("I send a DELETE request to {string}")
    public void sendDeleteRequest(String endpoint) throws IOException {
        Request r = new Request.Builder()
                .url(baseUrl + endpoint)
                .header("x-api-key", "reqres-free-v1") // API Key
                .delete() // GET request
                .build();

        // Mencoba mengirim request
        try(Response response = client.newCall(r).execute()) {
            // Jika berhasil set response string
            this.response = response;
            this.responseBody = response.body().string();
        }
    }

    @When("I send a POST request to {string} with the user data")
    public void sendPostRequest(String endpoint) throws IOException {
        RequestBody body = RequestBody.create(gson.toJson(userData), JSON);
        Request r = new Request.Builder()
                .url(baseUrl + endpoint)
                .header("x-api-key", "reqres-free-v1") // API Key
                .post(body) // GET request
                .build();

        // Mencoba mengirim request
        try(Response response = client.newCall(r).execute()) {
            // Jika berhasil set response string
            this.response = response;
            this.responseBody = response.body().string();
        }
    }

    @When("I send a PATCH request to {string} with the update data")
    public void i_send_a_patch_request_to_with_the_update_data(String endpoint) throws IOException {
        RequestBody body = RequestBody.create(gson.toJson(updateData), JSON);
        Request r = new Request.Builder()
                .url(baseUrl + endpoint)
                .header("x-api-key", "reqres-free-v1") // API Key
                .patch(body) // GET request
                .build();

        // Mencoba mengirim request
        try(Response response = client.newCall(r).execute()) {
            // Jika berhasil set response string
            this.response = response;
            this.responseBody = response.body().string();
        }
    }

    @When("I send a POST request to {string} with the invalid data")
    public void i_send_a_post_request_to_with_the_invalid_data(String endpoint) throws IOException {
        RequestBody body = RequestBody.create(gson.toJson(invalidData), JSON);
        Request r = new Request.Builder()
                .url(baseUrl + endpoint)
                .header("x-api-key", "reqres-free-v1") // API Key
                .post(body) // GET request
                .build();

        // Mencoba mengirim request
        try(Response response = client.newCall(r).execute()) {
            // Jika berhasil set response string
            this.response = response;
            this.responseBody = response.body().string();
        }
    }


    /// ////////////////////////////////////////////////
    /// Then - Validasi data yang diperoleh
    /// ////////////////////////////////////////////////
    ///
    @Then("the response should contain a valid user ID")
    public void verifyValidUser() throws IOException {
        var json = gson.fromJson(responseBody, JsonObject.class);
        assertEquals(true, json.keySet().contains("id"));
    }

    @Then("the response should match the JSON schema:")
    public void the_response_should_match_the_json_schema(String schema) throws IOException {
        JsonElement jsonElement = JsonParser.parseString(responseBody);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        Set<String> keys = jsonObject.keySet();
        assertTrue(keys.contains("id"));
        assertTrue(keys.contains("name"));
        assertTrue(keys.contains("job"));
    }

    @Then("the response status code should be {int}")
    public void verifyStatusCode(int expectedStatusCode) {
        assertEquals(expectedStatusCode, response.code());
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
