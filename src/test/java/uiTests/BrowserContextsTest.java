package uiTests;

import com.microsoft.playwright.*;
import org.testng.annotations.*;

import static org.testng.Assert.assertTrue;

public class BrowserContextsTest {

    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext browserContext;
    Page page;

    @BeforeTest
    public static void setupBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
        );
        browserContext = browser.newContext(); // common browser context for two tests
    }

    @BeforeMethod
    public void setUp() {
        page = browserContext.newPage(); // opens tabs for each test
    }

    @AfterTest
    public static void tearDown() {
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

        int matchingSearchResults = page.locator(".card").count();

        assertTrue(matchingSearchResults > 0);
    }
}
