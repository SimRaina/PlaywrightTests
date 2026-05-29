package apiTests;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class PostAPICallJSONTest {

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
    public void createUserWithJsonStringTest() {
        String jsonBody = "{\n" +
                "  \"name\" : \"testingAPI\",\n" +
                "  \"email\" : \"testpwapi1@gmail.com\",\n" +
                "  \"gender\" : \"male\",\n" +
                "  \"status\" : \"active\"\n" +
                "}";

        //POST Call: create a user
        APIResponse apiPostResponse = requestContext.post("url",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization", "token")
                        .setData(jsonBody));

        System.out.println(apiPostResponse.status());
    }

    @Test
    public void createUserWithJsonFileTest() throws IOException {
        //get json file:
        byte[] data = null;
        File file = new File("./src/test/data/userdata.json");
        data = Files.readAllBytes(file.toPath());

        //POST Call: create a user
        APIResponse apiPostResponse = requestContext.post("url",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization", "token")
                        .setData(data));
    }
}
