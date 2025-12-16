package uiTests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MockingAPITest {

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

    public static final String RESPONSE_WITH_A_SINGLE_ENTRY = """
            {
                "current_page": 1,
                "data": [
                    {
                        "id": "01KCK671YYZ8305M4VW3W91RE1",
                        "name": "Combination Pliers",
                        "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris viverra felis nec pellentesque feugiat. Donec faucibus arcu maximus, convallis nisl eu, placerat dolor. Morbi finibus neque nec tincidunt pharetra. Sed eget tortor malesuada, mollis enim id, condimentum nisi. In viverra quam at bibendum ultricies. Aliquam quis eros ex. Etiam at pretium massa, ut pharetra tortor. Sed vel metus sem. Suspendisse ac molestie turpis. Duis luctus justo massa, faucibus ornare eros elementum et. Vestibulum quis nisl vitae ante dapibus tempor auctor ut leo. Mauris consectetur et magna at ultricies. Proin a aliquet turpis.",
                        "price": 14.15,
                        "is_location_offer": false,
                        "is_rental": false,
                        "co2_rating": "D",
                        "in_stock": true,
                        "is_eco_friendly": false,
                        "product_image": {
                            "id": "01KCK671XND3JMS3YJBJ1X0073",
                            "by_name": "Helinton Fantin",
                            "by_url": "https:\\/\\/unsplash.com\\/@fantin",
                            "source_name": "Unsplash",
                            "source_url": "https:\\/\\/unsplash.com\\/photos\\/W8BNwvOvW4M",
                            "file_name": "pliers01.avif",
                            "title": "Combination pliers"
                        },
                        "category": {
                            "id": "01KCK671X5VBV2CDFYVB0D8N67",
                            "name": "Pliers"
                        },
                        "brand": {
                            "id": "01KCK671GJ8SP3Y52KGF5J355P",
                            "name": "ForgeFlex Tools"
                        }
                    }
                ],
                "from": 1,
                "last_page": 1,
                "per_page": 9,
                "to": 1,
                "total": 1
            }
            """;

    public static final String RESPONSE_WITH_NO_ENTRIES = """
            {
              "current_page: 1,
              "data": [],
              "from": 1,
              "last_page": 1,
              "per_page": 9,
              "to": 1,
              "total": 0
            }
            """;

    @Test
    void validateSingleItemFound() {
        page.route("**/products/search?q=Pliers", route -> {
            route.fulfill(
                    new Route.FulfillOptions()
                            .setBody(MockingAPITest.RESPONSE_WITH_A_SINGLE_ENTRY)
                            .setStatus(200)
            );
        });
        page.getByPlaceholder("Search").fill("Pliers");
        page.getByPlaceholder("Search").press("Enter");

        assertThat(page.getByTestId("product-name")).hasCount(1);
        assertThat(page.getByTestId("product-name")).hasText("Combination Pliers");
    }

    @Test
    void validateNoItemsAreFound() {
        page.route("**/products/search?q=Pliers", route -> {
            route.fulfill(
                    new Route.FulfillOptions()
                            .setBody(MockingAPITest.RESPONSE_WITH_NO_ENTRIES)
                            .setStatus(200)
            );
        });
        page.getByPlaceholder("Search").fill("Pliers");
        page.getByPlaceholder("Search").press("Enter");

        assertThat(page.getByTestId("product-name")).hasCount(0);
        assertThat(page.getByTestId("search_completed"))
                .hasText("There are no products found");
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
