package uiTests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;

public class LocateRoleTest {

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

    // ARIA - Accessible Rich Internet Applications
    @DisplayName("Locating an element by role")
    @Test
    void byRole() {
        page.getByRole(AriaRole.BUTTON,         // Find "Search" button
                new Page.GetByRoleOptions().setName("Search")).click();

        page.getByRole(AriaRole.CHECKBOX,       // Find all checked checkboxes
                new Page.GetByRoleOptions().setChecked(true));

        page.getByRole(AriaRole.HEADING,        // Find "Price Range" heading
                new Page.GetByRoleOptions().setName("Price Range"));

        page.getByRole(AriaRole.HEADING,        // Find all level 5 headings
                new Page.GetByRoleOptions()
                        .setName("Pliers")
                        .setLevel(5));
    }

    /*
    Other Roles:
           ALERT, ALERTDIALOG, BANNER, CAPTION, CELL, COLUMNHEADER,
           DIALOG, FORM, GRID, IMG, LINK, LIST, LISTBOX, LISTITEM,
           MENU, MENUBAR, MENUITEM, MENUITEMCHECKED, MENUITEMRADIO,
           NAVIGATION, NOTE, OPTION, PARAGRAPH, RADIO, RGION, ROW,
           ROWHEADER, SCROLLBAR, SEARCH, SEARCHBOX, SEPARATOR, SLIDER,
           SPINBUTTON, STATUS, SWITCH, TAB, TABLE, TABPANEL, TEXTBOX,
           TIME, TIMER, TOOLBER, TOOLTIP, TREEGRID, TREEITEM
     */

    void openPage() {
        playwright =
                Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
        );
    }
}
