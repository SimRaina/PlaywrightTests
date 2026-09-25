package uiTests;

import com.microsoft.playwright.BrowserContext;
import org.testng.annotations.*;
import uiTests.BaseClass.BaseClass;

import static org.testng.Assert.assertTrue;

public class BrowserContextsTest extends BaseClass {

    private BrowserContext browserContext;

    @Override
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        playwright = com.microsoft.playwright.Playwright.create();
        browser = playwright.chromium().launch(
                new com.microsoft.playwright.BrowserType.LaunchOptions()
                        .setHeadless(false)
        );
        browserContext = browser.newContext();
        page = browserContext.newPage();
    }

    @Override
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (page != null) {
            page.close();
        }
        if (browserContext != null) {
            browserContext.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    @Test
    void shouldShowPageTitle() {
        navigateTo("/");
        String title = page.title();
        assertTrue(title.contains("Practice Software Testing"));
    }

    @Test
    void shouldSearchByKeyword() {
        navigateTo("/");
        page.locator("[placeholder=Search]").fill("Pliers");
        page.locator("button:has-text('Search')").click();
        int matchingSearchResults = page.locator(".card").count();
        assertTrue(matchingSearchResults > 0);
    }
}
