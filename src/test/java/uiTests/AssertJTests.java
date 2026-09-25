package uiTests;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import uiTests.BaseClass.BaseClass;

import java.util.List;

public class AssertJTests extends BaseClass {

    @Test
    void allProductPricesShouldBeCorrectValues() {
        navigateTo("/");
        List<Double> prices = page.getByTestId("product-price")
               .allInnerTexts()
               .stream()
               .map(price -> Double.parseDouble(price.replace("$", "")))
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
}
