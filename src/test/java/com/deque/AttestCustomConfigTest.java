package com.deque;

import com.deque.attest.AttestConfiguration;
import com.deque.attest.AttestDriver;
import com.deque.attest.reporter.AttestReportingOptions;
import org.testng.annotations.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import static com.deque.attest.matchers.IsAuditedForAccessibility.isAuditedForAccessibility;
import static org.hamcrest.MatcherAssert.assertThat;
import java.io.File;
import java.io.IOException;

/*
* ~ AttestCustomConfigTest~
* ~~~~~ IN THIS TEST CASE ~~~~~~~~~~
* << Shows local testing of HTML page in project >> 
* << Uses specific identifier and rule set in single test case >>
* << Uses specific Axe script and rule set for all test cases (shown in configuration) >>
*/

public class AttestCustomConfigTest {
    //In this case we point to specific LOCAL page within our project
    String absolutePath = new File("src/main/webapp/index.html").getAbsolutePath();
    AttestDriver attestDriver;
    private WebDriver webDriver;
    private AttestReportingOptions _reportOptions = new AttestReportingOptions();
    static String macOSReporter = new File("src/test/resources/attest-reporter-macos").getAbsolutePath();

    //Setup: Specific configuration used below: 
    //forAuditSuite() allows users to set custom rules to be used during test run
    //withAxeScript() allows users to use custom axe script of their choosing
    @BeforeTest
    public void setUp() throws Exception {
        AttestConfiguration.configure()
            .forAuditSuite(getClass().getResource("resources/config/attest.json"))
            .withAxeScript(getClass().getResource("/axe.js"))
            .testSuiteName("Homepage") // << Default reporting suite name, results will be associated with this suite unless explicitly specified
            .outputDirectory("target/attest-reports/attest-customization");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");
        webDriver = new ChromeDriver(options);
        attestDriver = new AttestDriver(webDriver);
        webDriver.get("file:///"+absolutePath);
    }

    @AfterTest
    public void tearDown() throws Exception {
        webDriver.quit();
    }
      //Reporting function that calls the executable after all the tests have been run and creates HTML and XML report off of it.
    //Requires the attest reporter executable to be within the project.  
    @AfterClass
    public static void createHtmlXmlReports() throws IOException{
        Runtime rt = Runtime.getRuntime();
        String command = macOSReporter +" target/attest-reports/attest-customization --dest target/attest-reports/attest-customization/a11y-html-report --format html+junit";
        rt.exec(command);
    }
    //TestCase: Using local page in the project and running Attest against it
    @Test (groups = { "a11yCustom" })
    public void auditHomePageForAccessibility() throws Exception {
        assertThat(attestDriver, isAuditedForAccessibility().logResults(_reportOptions.uiState("Homepage All")));
    }

    //TestCase: Using local page, but running it on one single section of the page using wcag AA rules. 
    @Test(groups = { "a11yCustom" })
    public void auditHomePageWithOptions() throws Exception {
        assertThat(attestDriver, isAuditedForAccessibility().within(".jumbotron").accordingTo("wcag2a").logResults(_reportOptions.uiState("Homepage section")));
    }

    //TestCase: Using local page, but running it against content we want excluded from the page.  
    @Test(groups = { "a11yCustom" })
    public void auditHomePageWithExclusions() throws Exception {
        assertThat(attestDriver, isAuditedForAccessibility().excluding(".jumbotron").logResults(_reportOptions.uiState("Homepage excluding section")));
    }


     //TestCase: Using local page, but checking for 3 accessibility rules anyways.  
     @Test(groups = { "a11yCustom" })
     public void auditHomePageWithCertainChecks() throws Exception {
         assertThat(attestDriver, isAuditedForAccessibility().checkingOnly("label", "aria-roles", "html-has-lang").logResults(_reportOptions.uiState("Homepage with certain checks")));
     }

}
