package com.deque;

import com.deque.attest.AttestConfiguration;
import com.deque.attest.AttestDriver;
import com.deque.attest.reporter.AttestReportingOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

import static com.deque.attest.matchers.IsAuditedForAccessibility.isAuditedForAccessibility;
import static com.deque.attest.matchers.IsAccessible.isAccessible;
import org.testng.annotations.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.hamcrest.MatcherAssert.assertThat;

/*
* ~ ApplicationFullTest ~
* ~~~~~ IN THIS TEST CASE ~~~~~~~~~~
* << Shows selenium webdriver use with Attest, and basic setup >> 
* << Uses isAuditedForAccessibility() function >>
* << Uses isAccessible() function >>
* << Uses selenium webdriver page interaction, and run Attest at different page states >>
*/
public class ApplicationFullTest {

    AttestDriver attestDriver;
    private WebDriver webDriver;
    private AttestReportingOptions _reportOptions = new AttestReportingOptions();

    @BeforeTest
    public void setUp() throws Exception {
        AttestConfiguration.configure()
            .testSuiteName("Deque") // << Default reporting suite name, results will be associated with this suite unless explicitly specified
            .outputDirectory("target/attest-reports");
        //System.setProperty("webdriver.chrome.driver",	System.getProperty("user.dir") + "/driver/chrome-windows.exe");
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("headless"); //If not headless, comment out this line.
        options.addArguments("disable-gpu");
        webDriver = new ChromeDriver(options);
        attestDriver = new AttestDriver(webDriver);
    }

    @AfterTest
    public void tearDown() throws Exception {
        webDriver.quit();
    }

     // TestCase: uses isAuditedForAccessibility() >> will audit, and track all accessibility issues
    // if a violation exists it will NOT fail the test or break the build
    @Test (groups = { "a11yTest" })
    public void auditTestPageForAccessibility() throws Exception {
        webDriver.get("https://dequeuniversity.com/demo/mars/");
        assertThat(attestDriver, isAuditedForAccessibility().logResults(_reportOptions.uiState("Mars Commuter")));
    }

    //@Test (groups = { "a11yTest" })
    // TestCase:  uses isAccessible() >> enforces the scanned page to have 0 violations,
    // if a violation exists the test will fail.
    public void DQUShouldBeAccessible() throws Exception {
        webDriver.get("https://www.dequeuniversity.com");
        assertThat(attestDriver, isAccessible().logResults(_reportOptions.uiState("Deque University")));
    }
    
    //TestCase: uses seleniums webelement functionality to allow page interaction and run Attest before and after state change. 
   // @Test (groups = { "a11yTest" })
    public void auditMarsCommuterAccessibility() throws Exception {
        //Scan the initial homepage of Mars Commuter before state change
        webDriver.get("https://dequeuniversity.com/demo/mars/");
        _reportOptions.testSuiteName("Mars Commuter State Change");
        assertThat(attestDriver, isAuditedForAccessibility().logResults(_reportOptions.uiState("Mars Commuter No Change")));
        
        // Click a radio button to change form on page. 
        WebElement planet = webDriver.findElement(By.id("route-type-multi-city"));
        planet.sendKeys("ChromeDriver");
        planet.click();
        assertThat(attestDriver, isAuditedForAccessibility().logResults(_reportOptions.uiState("Mars Commuter Changed State")));
    }

}
