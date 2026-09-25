package uiTests.BaseClass;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.util.Arrays;

public abstract class BaseClass {
    protected static final String BASE_URL = System.getProperty("baseUrl", "https://practicesoftwaretesting.com");

    protected Playwright playwright;
    protected Browser browser;
    protected Page page;

    @BeforeEach
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setArgs(Arrays.asList(
                                "--disable-notifications",
                                "--disable-extensions"
                        ))
        );
        page = browser.newPage();
    }

    @AfterEach
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (page != null) {
            page.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    protected void navigateTo(String path) {
        String url = path.startsWith("http")
                ? path
                : BASE_URL + (path.startsWith("/") ? path : "/" + path);
        page.navigate(url);
    }
}
