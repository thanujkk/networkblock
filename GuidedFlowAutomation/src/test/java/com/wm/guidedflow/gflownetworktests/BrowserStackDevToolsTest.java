package com.wm.guidedflow.gflownetworktests;

import com.google.common.collect.ImmutableList;
import com.wm.guidedflow.gflowbase.BrowserStackBaseTest;
import com.wm.guidedflow.pages.ResidentialPages;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v126.network.Network;
import org.openqa.selenium.devtools.v126.network.model.RequestId;
import org.openqa.selenium.devtools.v126.network.model.Response;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

public class BrowserStackDevToolsTest extends BrowserStackBaseTest {

	String requestUrlToBlock;
	public WebDriverWait wait;


	@Test
    public void blockNetworkRequestsValidation() throws InterruptedException, IOException {

		//Test Data:
		String appURL = "https://wmqa3.wm.com/";
		String addressToEnter = "1235 Lorraine Street, Houston, TX, USA";

//		ChromeOptions options = new ChromeOptions();
//		options.addArguments("--incognito");
//		ChromeDriver driver = new ChromeDriver(options);
//		long startTime = System.currentTimeMillis();
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(Duration.ofMillis(30000));

		driver.get(appURL);
		ResidentialPages resiPages = new ResidentialPages(driver);
		resiPages.clickOnAcceptCookiesButton();
//		wait = new WebDriverWait(driver, Duration.ofSeconds(30));
//		wait.until(ExpectedConditions.visibilityOfElementLocated());
		Thread.sleep(10000);

		resiPages.clickOnResidentialLink();
		Thread.sleep(10000);

		resiPages.clickOnResidentialTrashLink();
		Thread.sleep(10000);

		resiPages.enterAddressInTheAddressBox(addressToEnter);
		String enteredAddress = resiPages.getAddressBoxValue();
		System.out.println(enteredAddress);
		Thread.sleep(5000);

		resiPages.clickOnCheckAvailabilityButton();
		Thread.sleep(5000);

		resiPages.clickOnAddressConfirmButton();
		Thread.sleep(5000);

		//Get the response url
		getResponseUrlFromDevTools(driver);
		Thread.sleep(5000);

		resiPages.clickOnCheckAvailabilityButton();
		Thread.sleep(10000);

		blockAndUnblockNetworkUrl(driver, resiPages);

		resiPages.clickOnCloseXButtonByJavaScript();
		Thread.sleep(5000);

		resiPages.clickOnCheckAvailabilityButton();

		//System.out.println("Popup with \""+serviceUnavailableHeaderText+"\" header text has been displayed");

		//Printing total execution time
//		long endTime = System.currentTimeMillis();
//		System.out.println(endTime - startTime);

		driver.quit();
	}

	public void blockAndUnblockNetworkUrl(WebDriver driver, ResidentialPages residentialPages) throws InterruptedException {
		//enabling Network DevTools and blocking the network URL
		driver = new Augmenter().augment(driver);
		DevTools devTools = ((ChromeDriver) driver).getDevTools();
		devTools.createSession();
		devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
		devTools.send(Network.setBlockedURLs(ImmutableList.of( requestUrlToBlock )));
		Thread.sleep(5000);

		residentialPages.clickOnCloseButton();
		Thread.sleep(5000);

		residentialPages.clickOnCheckAvailabilityButton();
		Thread.sleep(5000);

		String actualErrorMessage = residentialPages.getErrorElementText();
		String expectedErrorMessage = "Sorry, we can't find that address. Please try again.";
		System.out.println("actualErrorMessage: "+actualErrorMessage);
		System.out.println("expectedErrorMessage: "+expectedErrorMessage);

		residentialPages.clickOnCheckAvailabilityButton();
		Thread.sleep(5000);

		residentialPages.clickOnCheckAvailabilityButton();
		Thread.sleep(5000);

		String contactUsHeaderText = residentialPages.getContactUsHeaderText();
		System.out.println("Popup with \""+contactUsHeaderText+"\" header text has been displayed");

		//Disabling Network DevTools and unBlocking the network url
		devTools.send(Network.disable());
		Thread.sleep(5000);

	}
	public void getResponseUrlFromDevTools(WebDriver driver) {
		//driver = new Augmenter().augment(driver);
		try {
            DevTools devTools = ((ChromeDriver) driver).getDevTools();
            devTools.createSession();

            //devTools.send(Network.clearBrowserCache());
            //devTools.send(Network.setCacheDisabled(true));
            final RequestId[] requestIds = new RequestId[1];
            devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
            devTools.addListener(Network.responseReceived(), responseReceived ->
            {
                Response response = responseReceived.getResponse();
                requestIds[0] = responseReceived.getRequestId();
                String url = response.getUrl();
                if (url.contains("products")) {
                    if (response.getStatus() == 200) {
                        requestUrlToBlock = url;
                        System.out.println("API URL: " + url);
                        //String headers = response.getHeaders().toString();
                        //String type = response.getMimeType();
                        //System.out.println("Headers1: "+headers);
                        //System.out.println("MIME Type: "+type);
                        //String responseBody = devTools.send(Network.getResponseBody(requestIds[0])).getBody();
                        //System.out.println("Response Body: " + responseBody);
                    }
                }
            });
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}
