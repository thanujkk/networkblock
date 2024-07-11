package com.wm.guidedflow.gflownetworktests;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.bidi.module.Network;
import org.openqa.selenium.bidi.network.AddInterceptParameters;
import org.openqa.selenium.bidi.network.ContinueResponseParameters;
import org.openqa.selenium.bidi.network.InterceptPhase;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
public class InterceptNetworkResponse {
    public static String USERNAME = "thanujkumarkatik_Pv1L9I";
    public static String AUTOMATE_KEY =  "M2qDsmCosBdoreywDyCy";
    public static String REMOTE_URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";

    static Boolean success = false;
    public static void main(String[] args) throws MalformedURLException {
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("browserName", "chrome");
        HashMap<String, Object> browserstackOptions = new HashMap<>();
        browserstackOptions.put("os", "OS X");
        browserstackOptions.put("osVersion", "Big Sur");
        browserstackOptions.put("sessionName", "Console Logs");
        browserstackOptions.put("seleniumVersion", "4.20.0");
        // This can be replaced with the appropriate flag
        browserstackOptions.put("seleniumBidi", true);
        capabilities.setCapability("bstack:options", browserstackOptions);
        WebDriver driver = new RemoteWebDriver(new URL(REMOTE_URL), capabilities);
        try {
            Augmenter augmenter = new Augmenter();
            driver = augmenter.augment(driver);
            try (Network network = new Network(driver)) {
                network.addIntercept(new AddInterceptParameters(InterceptPhase.RESPONSE_STARTED));
                CountDownLatch latch = new CountDownLatch(1);
                network.onResponseStarted(
                        responseDetails -> {
                            network.continueResponse(
                                    new ContinueResponseParameters(responseDetails.getRequest().getRequestId()));
                            latch.countDown();
                        });
                network.onResponseStarted(
                        responseDetails -> {
                            System.out.println("Request response received for url ->" + responseDetails.getRequest().getUrl());
                            // More parameters are available to modify the incoming response.
                            // Ensure it is supported by the browser under test.
                            network.continueResponse(
                                    new ContinueResponseParameters(responseDetails.getRequest().getRequestId()));
                            latch.countDown();
                        });
                driver.get("https://www.selenium.dev/selenium/web/bidi/logEntryAdded.html");
                boolean countdown = latch.await(5, TimeUnit.SECONDS);
                if (countdown) {
                    markTestStatus("passed", "Network response intercepted", driver);
                } else {
                    markTestStatus("failed", "Network response not intercepted", driver);
                }
                driver.quit();
            }
        } catch (Exception e) {
            markTestStatus("failed", "Exception!", driver);
            e.printStackTrace();
            driver.quit();
        }
    }
    public static void markTestStatus(String status, String reason, WebDriver driver) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"" + status + "\", \"reason\": \"" + reason + "\"}}");
    }
}