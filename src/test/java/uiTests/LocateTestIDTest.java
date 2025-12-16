package uiTests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LocateTestIDTest {

    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeEach
    void setup() {
        openPage();
        page.navigate("https://practicesoftwaretesting.com");
    }

    @AfterEach
    void tearDown() {
        browser.close();
        playwright.close();
    }

    // ARIA - Accessible Rich Internet Applications
    @DisplayName("Locating an element by Test ID")
    @Test
    void byTestID() {
        // Define a special attribute (e.g. "data-testid")

        page.getByTestId("search-query").fill("Pliers");
        // List<String> itemNames = page.getByTestId("product-names).allTextContents();
    }

    void openPage() {
        playwright =
                Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
        );
    }
}
