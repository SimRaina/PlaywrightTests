package apiTests;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class AuthTests {

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
    public void differentAuthTests(){

        // basic
        APIResponse response = requestContext.get("url",
                RequestOptions.create()
                        .setHeader("Authorization", "Basic dXNlcjpwYXNzd2Q=") // base64(user:passwd)
        );
        System.out.println(response.status());
        System.out.println(response.text());

        // digest
        APIResponse response1 = requestContext.get("url",
                RequestOptions.create()
                        .setHeader("Authorization", "Digest username=user")
        );

        // OAuth 2.0
        String token = "your_oauth2_token_here";

        APIResponse response2 = requestContext.get("url",
                RequestOptions.create()
                        .setHeader("Authorization", "Bearer " + token)
        );

        // API Key
        APIResponse response3 = requestContext.get("url",
                RequestOptions.create()
                        .setHeader("x-api-key", "your_api_key_here")
        );
    }
}
