package org.example;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Playwright UI Tests")
@Feature("Basic UI Interactions")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestExample {

  static Playwright playwright;
  static Browser browser;

  BrowserContext context;
  Page page;

  @BeforeAll
  static void launchBrowser() {
    playwright = Playwright.create();
    browser = playwright.chromium().launch();
  }

  @AfterAll
  static void closeBrowser() {
    playwright.close();
  }

  @BeforeEach
  void createContextAndPage() {
    context = browser.newContext();
    page = context.newPage();
  }

  @AfterEach
  void closeContext() {
    context.close();
  }

  @Test
  @Order(1)
  @Story("Click Button")
  @Severity(SeverityLevel.CRITICAL)
  @Description("Verifies that clicking the button sets the result variable.")
  void shouldClickButton() {
    page.navigate("data:text/html,<script>var result;</script><button onclick='result=\"Clicked\"'>Go</button>");
    page.locator("button").click();
    assertEquals("Clicked", page.evaluate("result"));
  }

  @Test
  @Order(2)
  @Story("Checkbox Interaction")
  @Severity(SeverityLevel.NORMAL)
  @Description("Verifies that the checkbox can be checked.")
  void shouldCheckTheBox() {
    page.setContent("<input id='checkbox' type='checkbox'></input>");
    page.locator("input").check();
    assertTrue((Boolean) page.evaluate("() => window['checkbox'].checked"));
  }

  @Test
  @Order(3)
  @Story("Wikipedia Search")
  @Severity(SeverityLevel.BLOCKER)
  @Description("Searches for 'playwright' on Wikipedia and verifies the URL.")
  void shouldSearchWiki() {
    page.navigate("https://www.wikipedia.org/");
    page.locator("input[name=\"search\"]").click();
    page.locator("input[name=\"search\"]").fill("playwright");
    page.locator("input[name=\"search\"]").press("Enter");
    page.waitForURL("**/Playwright");
    assertEquals("https://en.wikipedia.org/wiki/Playwright", page.url());
  }
}
