package uiTests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;

public class FirstTest {

    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeMethod
    void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        page = browser.newPage();
    }

    @AfterMethod
    void tearDown() {
        browser.close();
        playwright.close();
    }

    @Test
    void shouldShowPageTitle() {
        page.navigate("https://practicesoftwaretesting.com");
        String title = page.title();
        assertTrue(title.contains("Practice Software Testing"));
    }

    @Test
    void shouldSearchByKeyword() {
        page.navigate("https://practicesoftwaretesting.com");
        page.locator("[placeholder=Search]").fill("Pliers"); // css selector
        page.locator("button:has-text('Search')").click(); // css selector
        int matchingSearchResults = page.locator(".card").count(); // count list of elements
        // page.locator(".card").first().click(); // click first element
        // page.locator(".card").last().click(); // click last element
        // page.locator(".card").nth(2).click(); // click on third element (0th, 1st, 2nd, ...)
        assertTrue(matchingSearchResults > 0);
    }
}
