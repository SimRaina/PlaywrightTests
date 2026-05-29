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

public class PostAPICallPojoTest {

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

        // Create Java Object
        UserPojo requestPojo = new UserPojo();
        requestPojo.setName("Sim");
        requestPojo.setJob("QA Engineer");

        // Serialization: Java Object -> JSON
        ObjectMapper mapper = new ObjectMapper();

        String requestBody = mapper.writeValueAsString(requestPojo);

        /* Alternatively, Builder Pattern
        UserPojo users = UserPojo.builder()
                    .name("Sim")
                    .job("QA Engineer").build();
         */

        System.out.println("Serialized Request:"+ requestBody);

        // POST API Call
        APIResponse response = requestContext.post(
                "https://reqres.in/api/users",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(requestBody)
        );

        // Response body
        String responseBody = response.text();

        System.out.println("Response JSON:" + responseBody);

        // Deserialization: JSON -> Java Object
        UserPojo responsePojo =
                mapper.readValue(responseBody, UserPojo.class);

        // Assertions
        assertEquals(response.status(), 201); // Resource Created

        assertEquals(responsePojo.getName(), "Sim");
        assertEquals(responsePojo.getJob(), "QA Engineer");

        assertNotNull(responsePojo.getId());
        assertNotNull(responsePojo.getCreatedAt());

        System.out.println("ID: " + responsePojo.getId());
        System.out.println("Created At: " + responsePojo.getCreatedAt());
    }
}
