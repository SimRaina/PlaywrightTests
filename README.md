# PlaywrightTests

This project contains Java-based Playwright tests for UI and API automation using Maven.

## Overview

The suite demonstrates:
- Browser automation for UI tests
- API testing with Playwright request fixtures
- Locator strategies and assertions
- Waiting and synchronization patterns
- Test structure using JUnit 5

## Tech Stack

- Java 21
- Maven
- Playwright Java
- JUnit 5
- AssertJ
- Allure

## Project Structure

```text
PlaywrightTests/
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   └── test/
│       └── java/
│           ├── apiTests/
│           ├── uiTests/
│           └── ...
└── target/
```

## Prerequisites

Install the following before running tests:

- Java 21+
- Maven
- A supported browser environment for Playwright

To install the Playwright browsers and dependencies:

```bash
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
```

Or, if using the Playwright CLI directly:

```bash
playwright install
```

## Running Tests

Run all tests:

```bash
mvn test
```

Run a specific test class:

```bash
mvn test -Dtest=uiTests.LocatorTests
```

Run a specific test method:

```bash
mvn test -Dtest=uiTests.LocatorTests#locateByRole
```

Run with more detailed output:

```bash
mvn test -DtrimStackTrace=false
```

## UI Test Examples

This project includes UI tests for:
- CSS locators
- Role-based locators
- Test ID locators
- Text/alt/title locators
- Label/placeholder locators
- Nested locators and filtered locators
- Waiting strategies

Typical UI test flow:

```java
page.navigate("https://practicesoftwaretesting.com");
page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
assertThat(page.locator(".card")).hasCount(4);
```

## API Test Examples

API tests can use Playwright's request API to verify service responses directly:

```java
APIRequestContext request = playwright.request().newContext();
APIResponse response = request.get("https://jsonplaceholder.typicode.com/posts/1");
assertThat(response.status()).isEqualTo(200);
```

## Playwright Trace Viewer

Playwright can record traces for debugging and visual analysis.

Run tests with tracing enabled:

```bash
mvn test -Dplaywright.trace=on
```

Open the trace viewer after the run:

```bash
npx playwright show-trace test-results/**/*.zip
```

If you want to open a specific generated trace file:

```bash
npx playwright show-trace path/to/trace.zip
```

Trace files are useful for:
- debugging failed tests
- reviewing step-by-step browser actions
- inspecting DOM and network activity
- debugging flaky automation

## Playwright Code Generation

Generate Playwright code from browser interactions:

```bash
npx playwright codegen https://practicesoftwaretesting.com
```

This launches a browser and records your interactions, generating Playwright Java or JavaScript test code depending on your setup and configuration.

For Maven-based Java projects, the generated examples can be adapted into JUnit tests under `src/test/java`.

## Useful Maven Commands

Compile the project:

```bash
mvn test-compile
```

Clean build artifacts:

```bash
mvn clean test
```

Skip tests while compiling:

```bash
mvn -DskipTests test-compile
```

Generate Allure report:

```bash
mvn verify
```

## Playwright Browser Commands

Run Playwright CLI:

```bash
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI
```

Install browsers:

```bash
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
```

## Best Practices

- Prefer stable locators such as `getByRole`, `getByTestId`, `getByLabel`, and `getByPlaceholder`
- Use explicit waits only when necessary
- Keep tests independent and readable
- Use assertions for validation rather than just performing actions
- Add trace capture to investigate flaky tests
