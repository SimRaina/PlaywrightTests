package uiTests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;

import java.util.List;

public class LocateNestedElementsTest {

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

    @DisplayName("Locating Nested elements")
    @Test
    void byNested() {
        page.getByRole(AriaRole.MENUBAR,
                new Page.GetByRoleOptions().setName("Main Menu"))
                .getByRole(AriaRole.MENUITEM,
                        new Locator.GetByRoleOptions().setName("Home")).click();

        page.getByRole(AriaRole.MENUBAR,
                new Page.GetByRoleOptions().setName("Main Menu"))
                .getByText("Home")
                .click();
    }

    @DisplayName("Filtering elements")
    @Test
    void byFiltered() {
        List<String> allProductsWithSander = page.getByTestId("product-name")
                .filter(new Locator.FilterOptions().setHasText("Sander"))
                .allTextContents();

        List<String> allProductsWithoutDrill = page.getByTestId("product-name")
                .filter(new Locator.FilterOptions().setHasNotText("Drill"))
                .allTextContents();

        List<String> allProducts = page.locator(".card")
                .filter(new Locator.FilterOptions()
                        .setHas(page.getByText("Out of stock"))) // another element
                .getByTestId("product-name")
                .allTextContents();
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
