package uiTests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LocatorTests {

    private Playwright playwright;
    private Browser browser;
    private Page page;

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
    @DisplayName("Locating an element by CSS")
    void locateByCss() {
        page.locator("input[placeholder='Search']").fill("Pliers");
        page.locator("button[type='submit']").click();

        assertThat(page.locator(".card")).hasCount(4);

        List<String> productNames = page.getByTestId("product-name").allTextContents();
        Assertions.assertThat(productNames).allMatch(name -> name.contains("Pliers"));
    }

    @Test
    @DisplayName("Locating an element by role")
    void locateByRole() {
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
        page.getByTestId("search-query").fill("Pliers");
        assertThat(page.getByTestId("search-query")).hasValue("Pliers");
    }

    @Test
    @DisplayName("Locating an element by text")
    void locateByText() {
        page.getByText("Combination Pliers").click();
        assertThat(page.getByText("ForgeFlex Tools")).isVisible();
    }

    @Test
    @DisplayName("Locating by alt text and title")
    void locateByAltTextAndTitle() {
        page.getByAltText("Combination Pliers").click();
        page.getByTitle("Practice Software Testing - ToolShop").click();
    }

    @Test
    @DisplayName("Locating an element by label and placeholder")
    void locateByLabelAndPlaceholder() {
        page.getByPlaceholder("Search").fill("Pliers");
        assertThat(page.getByPlaceholder("Search")).hasValue("Pliers");

        page.getByLabel("Search").fill("Hammer");
        assertThat(page.getByLabel("Search")).hasValue("Hammer");
    }

    @Test
    @DisplayName("Locating nested elements")
    void locateNestedElements() {
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
        page.navigate("https://practicesoftwaretesting.com");
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

    void openPage() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
        );
        page = browser.newPage();
    }
}
