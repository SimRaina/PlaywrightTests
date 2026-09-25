package uiTests;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import uiTests.BaseClass.BaseClass;

import java.util.Comparator;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class WaitsTest extends BaseClass {

    @Test
    void validateProductNames() {
        navigateTo("/");
        page.waitForSelector(".card-img-top");
        List<String> productNames = page.getByTestId("product-name").allInnerTexts();
        Assertions.assertThat(productNames).contains("Pliers", "Bolt Cutters");
    }

    @Test
    void validateProductImages() {
        navigateTo("/");
        page.waitForSelector(".card-img-top");
        List<String> productImageTitles = page.locator(".card-img-top").all()
                .stream()
                .map(img -> img.getAttribute("alt"))
                .toList();
        Assertions.assertThat(productImageTitles).contains("Pliers", "Bolt Cutters");
    }

    @Test
    void validateFilterCheckboxes() {  // automatic/implicit waits for clicking/input values
        navigateTo("/");
        var screwdriverFilter = page.getByLabel("Screwdriver");
        screwdriverFilter.click();
        assertThat(screwdriverFilter).isChecked();
    }

    @Test
    void validateFilterProductsByCategory() {
        navigateTo("/");
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
        navigateTo("/");
        page.getByText("Bolt Cutters").click();
        page.getByText("Add to cart").click();

        // wait for toaster message to appear
        assertThat(page.getByRole(AriaRole.ALERT)).isVisible();
        assertThat(page.getByRole(AriaRole.ALERT)).hasText("Product added to shopping cart.");

        page.waitForCondition(() -> page.getByRole(AriaRole.ALERT).isHidden());
    }

    @Test
    void validateUpdateCartItemCount() {
        navigateTo("/");
        page.getByText("Bolt Cutters").click();
        page.getByText("Add to cart").click();

        page.waitForCondition(() -> page.getByTestId("cart-quantity").textContent().equals("1"));

        page.waitForSelector("[data-test=cart-quantity]:has-text('1')");
    }

    @Test
    void validateSortByDescPrice() {       // wait for API response
        navigateTo("/");
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

}
