package apiTests;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import tools.jackson.databind.ObjectMapper;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class DeleteAPICallTest {

    Playwright playwright;
    APIRequest request;
    APIRequestContext requestContext;

    @BeforeTest
    public void setup(){
        playwright = Playwright.create();
        request = playwright.request();
        requestContext = request.newContext();
    }

    @AfterTest
    public void tearDown(){
        playwright.close();
    }

    @Test
    public void createUserFromPojoTest(){

        // Create Java Object for new User
        UserPojo requestPojo = new UserPojo();
        requestPojo.setName("Sim");
        requestPojo.setJob("Senior QA Engineer");

        // Serialization: Java Object -> JSON
        ObjectMapper mapper = new ObjectMapper();

        String requestBody = mapper.writeValueAsString(requestPojo);

        System.out.println("Serialized Request:"+ requestBody);

        // Create New User
        APIResponse response = requestContext.post(
                "https://reqres.in/api/users",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(requestBody)
        );

        String createResponseBody = response.text();
        System.out.println("Create User Response:" + createResponseBody);

        // Deserialization
        UserPojo createdUser = mapper.readValue(createResponseBody, UserPojo.class);
        String userId = createdUser.getId();
        assertNotNull(userId);

        // Delete User
        APIResponse deleteResponse = requestContext.delete(
                "https://reqres.in/api/users/" + userId
        );

        System.out.println("Delete Status Code: " + deleteResponse.status());

        // Reqres returns 204
        assertEquals(deleteResponse.status(), 204);

        // Try to GET deleted user to validate
        APIResponse getResponse = requestContext.get(
                "https://reqres.in/api/users/" + userId
        );

        System.out.println("GET After Delete Status: " + getResponse.status());

        String getResponseBody = getResponse.text();
        System.out.println("GET After Delete Response:" + getResponseBody);
        assertEquals(getResponse.status(), 404);
    }
}
