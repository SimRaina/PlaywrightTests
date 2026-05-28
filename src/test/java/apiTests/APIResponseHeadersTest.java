package apiTests;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.HttpHeader;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.http.HttpHeaders;
import java.util.List;
import java.util.Map;

public class APIResponseHeadersTest {

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
    public void getHeadersTest(){
        APIResponse apiResponse = requestContext.get("url");
        int statusCode = apiResponse.status();
        Assert.assertEquals(statusCode, 200);

        Map<String, String> headers = apiResponse.headers();
        headers.forEach((k, v) -> System.out.println(k + " " + v));
        System.out.println(headers.size());

        // using List (headersArray)
        List<HttpHeader> headers1 = apiResponse.headersArray();
        for(HttpHeader h : headers1){
            System.out.println(h.name + " " + h.value);
        }
    }
}
