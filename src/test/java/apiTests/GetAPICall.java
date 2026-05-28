package apiTests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

public class GetAPICall {

    Playwright playwright;
    APIRequest request;
    APIRequestContext requestContext;

    @BeforeTest
    public void setup(){
        playwright = Playwright.create();
        request = playwright.request();
        requestContext = request.newContext();
    }

    @Test
    public void getSpecificUserTest(){
        APIResponse apiResponse = requestContext.get("url", RequestOptions.create()
                .setQueryParam("status", "active"));

        Assert.assertTrue(apiResponse.ok());
    }

    @Test
    public void getUsersAPITest(){
        APIResponse apiResponse = requestContext.get("url");

        int statusCode = apiResponse.status();
        String statusText = apiResponse.statusText();
        // System.out.println(apiResponse.body().toString());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(apiResponse.body());
        System.out.println(jsonResponse.toPrettyString());
        System.out.println(apiResponse.url());
        Map<String, String> headers = apiResponse.headers();
        System.out.println(headers);
        Assert.assertEquals(headers.get("content-type"), "application/json");
    }

    @Test
    public void getUsersAPIDisposeTest(){
        APIResponse apiResponse = requestContext.get("url");

        int statusCode = apiResponse.status();
        String statusText = apiResponse.statusText();
        System.out.println(apiResponse.text()); // plain text

        apiResponse.dispose(); // body is removed from the response
        System.out.println("Status Code after dispose: " + statusCode);
        System.out.println("Response URL after dispose: " + apiResponse.url());
        try {
            System.out.println(apiResponse.text()); // Exception
        }catch(PlaywrightException e) {
            System.out.println("API response body is disposed");
        }

        // dispose on requestContext
        APIResponse apiResponse1 = requestContext.get("url1");

        requestContext.dispose();
        // System.out.println(apiResponse.text()); // Exception
        System.out.println(apiResponse1.text()); // Exception


    }
}
