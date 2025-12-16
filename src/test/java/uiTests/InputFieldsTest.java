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

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class InputFieldsTest {

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

    @DisplayName("Interacting with input fields")
    @Test
    void completeForm() throws URISyntaxException {
        var firstName = page.getByLabel("First name");
        var lastName = page.getByLabel("Last name");
        var email = page.getByLabel("Email");
        var message = page.getByLabel("Message");
        // dropdown
        var subject = page.getByLabel("Subject");
        // upload file
        var upload = page.getByLabel("Attachment");
        firstName.fill("Sim");
        lastName.fill("Raina");
        email.fill("sim@gmail.com");
        message.fill("Hello!");
        // subject.selectOption("Payments");
        subject.selectOption(new SelectOption().setIndex(4));
        Path fileToUpload = Paths.get(ClassLoader.getSystemResource("data/uploadFile.txt").toURI());
        page.setInputFiles("#attachment", fileToUpload);
        // Assertions
        assertThat(firstName).hasValue("Sim");
        assertThat(lastName).hasValue("Raina");
        assertThat(email).hasValue("sim@gmail.com");
        assertThat(message).hasValue("Hello!");
        assertThat(subject).hasValue("payments");
        String uploaded = upload.inputValue();
        Assertions.assertThat(uploaded).endsWith("uploadFile.txt");
        assertThat(firstName).isDisabled();
        assertThat(lastName).not().isDisabled();
        assertThat(email).isVisible();
        assertThat(message).isEditable();
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
