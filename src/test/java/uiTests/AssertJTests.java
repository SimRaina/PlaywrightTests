package uiTests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.SelectOption;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AssertJTests {

    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeEach
    void setup() {
            openPage();
        }

    @AfterEach
    void tearDown() {
        browser.close();
        playwright.close();
    }

    @Test
    void allProductPricesShouldBeCorrectValues() {
       List<Double> prices = page.getByTestId("product-price")
               .allInnerTexts()
               .stream()
               .map(price ->
                       Double.parseDouble(price.replace("$", "")))
               .toList();

        Assertions.assertThat(prices)
                .isNotEmpty()
                .allMatch(price -> price > 0.0)
                .doesNotContain(0.0)
                .allMatch(price -> price < 1000.0)
                .allSatisfy(price ->
                        Assertions.assertThat(price)
                                .isGreaterThan(0.0)
                                .isLessThan(1000.0));
    }

    @Test
    void shouldSortInAlphabeticalOrder() {

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
