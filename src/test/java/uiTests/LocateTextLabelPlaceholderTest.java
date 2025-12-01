package uiTests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class LocateTextLabelPlaceholderTest {

    Playwright playwright;
    Browser browser;
    Page page;

    @DisplayName("Locating Elements by text")
    @Nested
    class LocatingElementsByText {
        @BeforeEach
        void setup() {
            openPage();
        }

        @AfterEach
        void tearDown() {
            browser.close();
            playwright.close();
        }

        @DisplayName("Locating an element by text contents")
        @Test
        void byText() {
            page.getByText("Bolt-Cutters").click();
            PlaywrightAssertions.assertThat(page.getByText("MightyCraft Hardware"))
                    .isVisible();
        }

        @DisplayName("Locating an element by Image Alt text")
        @Test
        void byAltText() {
            page.getByAltText("Combination Pliers").click();
            PlaywrightAssertions.assertThat(page.getByText("ForgeFlex Tools"))
                    .isVisible();
        }

        @DisplayName("Locating an element by Title text")
        @Test
        void byTitleText() {
            page.getByAltText("Combination Pliers").click();
            page.getByTitle("Practice Software Testing - ToolShop").click();
        }
    }

    @DisplayName("Locating Elements by Labels and Placeholders")
    @Nested
    class LocatingElementsByLabelsPlaceholders {
        @BeforeEach
        void setup() {
            openPage();
        }

        @AfterEach
        void tearDown() {
            browser.close();
            playwright.close();
        }

        @DisplayName("Locating an element by Label")
        @Test
        void byLabel() {
            page.getByLabel("Bolt-Cutters").click();
        }

        @DisplayName("Locating an element by Placeholders")
        @Test
        void byPlaceholder() {
            page.getByPlaceholder("Combination Pliers").click();
        }
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
