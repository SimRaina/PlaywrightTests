package uiTests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class AssertJTests {

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
