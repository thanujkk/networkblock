package com.wm.guidedflow.gflownetworktests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class SeleniumTest {
        public static final String USERNAME = "thanujkumarkatik_Pv1L9I";
        public static final String AUTOMATE_KEY = "M2qDsmCosBdoreywDyCy";
        public static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";

        public static void main(String[] args) throws MalformedURLException {
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("platformName", "windows");
            caps.setCapability("platformVersion", "11");
            caps.setCapability("browserName", "Chrome");
            caps.setCapability("browserVersion", "latest");
            caps.setCapability("name", "Developer Tools Extension Test");

            // Optional: Load your extension
            ChromeOptions options = new ChromeOptions();
            options.addArguments("load-extension=path/to/your/extension");
            caps.setCapability(ChromeOptions.CAPABILITY, options);

            WebDriver driver = new RemoteWebDriver(new URL(URL), caps);

            driver.get("http://www.google.com");
            System.out.println("Title of the page is: " + driver.getTitle());

            driver.quit();
        }
}


