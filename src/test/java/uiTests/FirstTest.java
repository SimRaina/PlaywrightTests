package uiTests;

import org.testng.annotations.Test;
import uiTests.BaseClass.BaseClass;

import static org.testng.Assert.assertTrue;

public class FirstTest extends BaseClass {

    @Test
    void shouldShowPageTitle() {
        navigateTo("https://practicesoftwaretesting.com");
        String title = page.title();
        assertTrue(title.contains("Practice Software Testing"));
    }
}
