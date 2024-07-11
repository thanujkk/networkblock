package com.wm.guidedflow.gflowbase;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.net.URL;
import java.time.Duration;
import java.util.HashMap;

public class BrowserStackBaseTest {
    public WebDriver driver;

    public String USERNAME = "thanujkumarkatik_Pv1L9I";
    public String AUTOMATE_KEY =  "M2qDsmCosBdoreywDyCy";
    public String Remote_URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";

    @BeforeMethod(alwaysRun = true)
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {
        DesiredCapabilities caps = getDesiredCapabilities();
//        System.out.println(URL);
        driver = new RemoteWebDriver(new URL(Remote_URL), caps);

//        options.setCapability("browserName", "Chrome");
//        options.setCapability("projectName","My Project");
//        options.setCapability("buildName","test percy_screenshot");
//        options.setCapability("sessionName","Percy second_test");
//        options.setCapability("local","false");
//        options.setCapability("seleniumVersion","4.21.0");
//        options.setCapability("browserVersion", "latest");
//        options.setCapability("os", "Windows");
//        options.setCapability("os_version", "11");
//        driver = new RemoteWebDriver(new URL(URL), options);
    }

    private static DesiredCapabilities getDesiredCapabilities() {
        ChromeOptions chromeOptions = new ChromeOptions();
        DesiredCapabilities caps = new DesiredCapabilities();

        caps.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        caps.setCapability("browserName", "Chrome");
        caps.setCapability("browserVersion", "latest");
        //caps.setCapability("seleniumCdp", true);

        HashMap<String, Object> browserstackOptions = new HashMap<>();
        browserstackOptions.put("os", "Windows");
        browserstackOptions.put("osVersion", "11");
//        browserstackOptions.put("w3c", true);
//        browserstackOptions.put("browserstack.debug", "true");
//        browserstackOptions.put("browserstack.networkLogs", "true");
//        browserstackOptions.put("seleniumVersion","4.22.0");

        caps.setCapability("bstack:options", browserstackOptions);
        return caps;
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() throws Exception {
        driver.quit();
    }

    public void waitForSomeTime(int waitTime){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitTime));
    }
}
