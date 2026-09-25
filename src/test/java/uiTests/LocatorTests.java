package uiTests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import uiTests.BaseClass.BaseClass;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LocatorTests extends BaseClass {

    @Test
    @DisplayName("Locating an element by CSS")
    void locateByCss() {
        navigateTo("/");
        page.locator("input[placeholder='Search']").fill("Pliers");
        page.locator("button[type='submit']").click();

        assertThat(page.locator(".card")).hasCount(4);

        List<String> productNames = page.getByTestId("product-name").allTextContents();
        Assertions.assertThat(productNames).allMatch(name -> name.contains("Pliers"));
    }

    @Test
    @DisplayName("Locating an element by role")
    void locateByRole() {
        navigateTo("/");
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Search")).click();

        page.getByRole(AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("Price Range")).isVisible();

        assertThat(page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Search"))).isVisible();
    }

    @Test
    @DisplayName("Locating an element by Test ID")
    void locateByTestId() {
        navigateTo("/");
        page.getByTestId("search-query").fill("Pliers");
        assertThat(page.getByTestId("search-query")).hasValue("Pliers");
    }

    @Test
    @DisplayName("Locating an element by text")
    void locateByText() {
        navigateTo("/");
        page.getByText("Combination Pliers").click();
        assertThat(page.getByText("ForgeFlex Tools")).isVisible();
    }

    @Test
    @DisplayName("Locating by alt text and title")
    void locateByAltTextAndTitle() {
        navigateTo("/");
        page.getByAltText("Combination Pliers").click();
        page.getByTitle("Practice Software Testing - ToolShop").click();
    }

    @Test
    @DisplayName("Locating an element by label and placeholder")
    void locateByLabelAndPlaceholder() {
        navigateTo("/");
        page.getByPlaceholder("Search").fill("Pliers");
        assertThat(page.getByPlaceholder("Search")).hasValue("Pliers");

        page.getByLabel("Search").fill("Hammer");
        assertThat(page.getByLabel("Search")).hasValue("Hammer");
    }

    @Test
    @DisplayName("Locating nested elements")
    void locateNestedElements() {
        navigateTo("/");
        page.getByRole(AriaRole.MENUBAR,
                        new Page.GetByRoleOptions().setName("Main Menu"))
                .getByRole(AriaRole.MENUITEM,
                        new Locator.GetByRoleOptions().setName("Home")).click();

        page.getByRole(AriaRole.MENUBAR,
                        new Page.GetByRoleOptions().setName("Main Menu"))
                .getByText("Home")
                .click();
    }

    @Test
    @DisplayName("Filtering elements")
    void filterElements() {
        navigateTo("/");
        List<String> allProductsWithSander = page.getByTestId("product-name")
                .filter(new Locator.FilterOptions().setHasText("Sander"))
                .allTextContents();

        List<String> allProductsWithoutDrill = page.getByTestId("product-name")
                .filter(new Locator.FilterOptions().setHasNotText("Drill"))
                .allTextContents();

        List<String> allProducts = page.locator(".card")
                .filter(new Locator.FilterOptions()
                        .setHas(page.getByText("Out of stock")))
                .getByTestId("product-name")
                .allTextContents();

        Assertions.assertThat(allProducts).isNotEmpty();
        Assertions.assertThat(allProductsWithoutDrill).isNotEmpty();
        Assertions.assertThat(allProductsWithSander).isNotEmpty();
    }

    @Test
    void searchForPliers() {
        navigateTo("/");
        page.getByPlaceholder("Search").fill("Pliers");
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Search")).click();

        assertThat(page.locator(".card")).hasCount(4);

        List<String> productNames = page.getByTestId("product-name").allTextContents();
        Assertions.assertThat(productNames).allMatch(name -> name.contains("Pliers"));

        Locator outOfStockItem = page.locator(".card")
                .filter(new Locator.FilterOptions().setHasText("Out of stock"))
                .getByTestId("product-name");

        assertThat(outOfStockItem).hasCount(1);
        assertThat(outOfStockItem).hasText("Long Nose Pliers");
    }

}
