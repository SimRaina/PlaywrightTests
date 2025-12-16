package uiTests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.junit.jupiter.api.*;

import java.util.List;

public class LocateCSSTest {

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

    @DisplayName("Locating an element by CSS")
    @Test
    void byCSS() {
        /* page.locator("#login-form input[name='username']").fill("value");
        page.locator(".btn-primary").click();
        page.locator("a:has-text('Home')");
        page.locator("input[type='email]"); */

        page.locator("#first_name").fill("Joe");
        PlaywrightAssertions.assertThat(page.locator("#first_Name")).hasValue("Joe");

        page.locator("#first_name").fill("QA");
        page.locator(".btnSubmit").click();

        List<String> alertMessages = page.locator(".alert").allTextContents();
        Assertions.assertFalse(alertMessages.isEmpty());

        page.locator("input[placeholder='Your last name *']").fill("Doe");
        PlaywrightAssertions.assertThat(page.locator("#last_name")).hasValue("Doe");


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
