package apiTests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class PostAPICallTest {

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
    public void createUserTest() {
        Map<String, Object> body = new HashMap<>();
        body.put("id", "1");
        body.put("name", "Sim");
        body.put("status", "active");

        APIResponse apiResponse = requestContext.post("url", RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", "token")
                .setData(body));

        Assert.assertEquals(apiResponse.status(), 201);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(apiResponse.body());
        System.out.println(jsonResponse.toPrettyString());
        String userId = jsonResponse.get("id").asString();

        APIResponse apiGetResponse = requestContext.get("url/" + userId, RequestOptions.create()
                .setHeader("Authorization", "token"));
        Assert.assertEquals(apiGetResponse.status(), 200);
        Assert.assertTrue(apiGetResponse.text().contains(userId));
    }
}
