package apiTests;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
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
        System.out.println(apiResponse.body().toString());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(apiResponse.body());
        System.out.println(jsonResponse.toPrettyString());
        System.out.println(apiResponse.url());
        Map<String, String> headers = apiResponse.headers();
        System.out.println(headers);
        Assert.assertEquals(headers.get("content-type"), "application/json");
    }
}
