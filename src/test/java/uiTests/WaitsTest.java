package uiTests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class WaitsTest {

    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeEach
    void setup() {
        openPage();
        page.navigate("https://practicesoftwaretesting.com");
        // page.waitForSelector("[data-test=product-name]");
        page.waitForSelector(".card-img-top"); // explicit wait for looking up an element
    }

    @AfterEach
    void tearDown() {
        browser.close();
        playwright.close();
    }

    @Test
    void validateProductNames() {
        List<String> productNames = page.getByTestId("product-name").allInnerTexts();
        Assertions.assertThat(productNames).contains("Pliers", "Bolt Cutters");
    }

    @Test
    void validateProductImages() {
        List<String> productImageTitles = page.locator(".card-img-top").all()
                .stream()
                .map(img -> img.getAttribute("alt"))
                .toList();
        Assertions.assertThat(productImageTitles).contains("Pliers", "Bolt Cutters");
    }

    @Test
    void validateFilterCheckboxes() {  // automatic/implicit waits for clicking/input values
        var screwdriverFilter = page.getByLabel("Screwdriver");
        screwdriverFilter.click();
        assertThat(screwdriverFilter).isChecked();
    }

    @Test
    void validateFilterProductsByCategory() {
        page.getByRole(AriaRole.MENUBAR).getByText("Categories").click();
        page.getByRole(AriaRole.MENUBAR).getByText("Power Tools").click();

        // page.waitForSelector(".card");
        page.waitForSelector(".card",
                new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(2000)
                );
        var filterProducts = page.getByTestId("product-name").allInnerTexts();
        Assertions.assertThat(filterProducts).contains("Sheet Sender", "Belt Sender");

    }

    @Test
    void validateToastMessage() {
        page.getByText("Bolt Cutters").click();
        page.getByText("Add to cart").click();

        // wait for toaster message to appear
        assertThat(page.getByRole(AriaRole.ALERT)).isVisible();
        assertThat(page.getByRole(AriaRole.ALERT)).hasText("Product added to shopping cart.");

        page.waitForCondition(() -> page.getByRole(AriaRole.ALERT).isHidden());
    }

    @Test
    void validateUpdateCartItemCount() {
        page.getByText("Bolt Cutters").click();
        page.getByText("Add to cart").click();

        page.waitForCondition(() -> page.getByTestId("cart-quantity").textContent().equals("1"));

        page.waitForSelector("[data-test=cart-quantity]:has-text('1')");
    }

    @Test
    void validateSortByDescPrice() {       // wait for API response
        page.waitForResponse("**/products?sort**",
                () -> {
                    page.getByTestId("sort").selectOption("Price (High - Low)");
                    // page.getByTestId("product-price").first().waitFor();
                });

        var productPrices = page.getByTestId("product-price")
                .allInnerTexts()
                .stream()
                .map(WaitsTest::extractPrice)
                .toList();

        // Are the prices in the correct order
        System.out.println("Product Prices " + productPrices);
        Assertions.assertThat(productPrices)
                .isNotEmpty()
                .isSortedAccordingTo(Comparator.reverseOrder());
    }

    private static double extractPrice(String price) {
        return Double.parseDouble(price.replace("$", ""));
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
