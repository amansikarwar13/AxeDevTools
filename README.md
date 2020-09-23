
<p align="center">
        <img src="https://i2.wp.com/4.bp.blogspot.com/-USb0koz6POI/WvwIYzCSrtI/AAAAAAACfl4/dHLmkfwepo43y1jSPoleB-X0oEGHvZcSQCLcBGAs/s1600/TestNG%2BLogo.png?resize=634%2C176&ssl=1" alt="java" height="70px" width="100px"  />   
<img src="https://dequeuniversity.com/assets/images/logos/attest_hero_blue.png" height="70px" width="100px" alt="attest" />
</p>

# Attest Java testNG Integration
A sample set of test cases that show the use and integration of Attest in the TestNG testing framework.
## Prerequisites
  * Maven 
    * If you dont have maven here is how to install https://maven.apache.org/install.html
  * Java (7+)
  * Java JDK
  * Selenium webdriver
  
## Integration information
In order to use attest-java within your project, you must include the dependency in your pom file: 
## POM file setup
### Add in the attest-java dependency
``` xml
<dependency>
  <groupId>com.deque</groupId>
  <artifactId>attest-java</artifactId>
  <version>3.6.0</version>
</dependency>
```
### Add the repository for deque to your POM file:
``` xml
<repositories>
<repository>
    <id>deque</id>
    <url>http://agora.dequecloud.com/artifactory/attest-java/</url>
  </repository>
</repositories>
```
### Update settings.xml to include authentication for agora:

``` xml
        <server>
            <id>deque</id>
            <username>yourname@yourcompany.com</username>
            <password>YOUR_API_KEY</password>
      </server>
```
<p>
    Once all the above has been completed, the attest-java artifacts have been added into your project. 
</p>

## In this project:

### Example 1:
<p>
    This example includes two seperate test cases one called <b>ApplicationFullTest.java</b> that includes functional testing with selenium webdriver with the basic usage and setup for J-unit test cases. </p>
<p> Notice, in this example there are two seperate functions used in the test cases: <b> isAuditedForAccessibility()</b> and <b>isAccessible()</b>. The integrations for both are as follows: </p>

```java

assertThat(page, isAuditedForAccessibility().logResults(_reportOptions.uiState("Mars Commuter")));

```


```java

assertThat(page, isAccessible().logResults(_reportOptions.uiState("Deque University")));

```


  * <b>isAccessible()</b>: Scans your page, and when an accessibility violation is found will fail the test case and break the build
  * <b>isAuditedForAccessibility()</b>: Scans your page, and when an accessibility violation is found it will log the results but will not fail the test case or the build. 
         
### Example 2:
<p>
The other example is <b>AttestCustomConfigTest.java</b> that includes more customization of the attest suite, including specific axe-core.js file to use, custom rule sets, and local HTML page configuration. 
</p>
<p>Notice, before each test case, we are configuring the attest suite to use a certain set of rules: </p>

```java

 AttestConfiguration.configure().forAuditSuite(getClass().getResource("resources/config/attest.json"))

```
<p>This allows for users to customize the rules they want on and off, in this case, HTML has lang as been shut off for demonstration purposes. </p>

<p>The other customization we have done, is get a specific version of axe-core you like to run: </p>

```java

 AttestConfiguration.configure().withAxeScript(axeScript);

```



## Running the example
To run the test case, simply use:  
```sh
mvn clean install
```
<p>Note: all of the examples are running headless chrome, so selenium will not show the web browser for these examples</p>

## Output
<p>
    When the tests run completely you will see no errors in the console, this is fine. The test cases are setup to audit for accessibility and not break the build. If you look in the <b>target</b> folder a new folder called <b>attest-results</b>
</p>
<p>
    By default the attest-java implementation gives you a log file and JSON object as the output for the test data. 
</p>

