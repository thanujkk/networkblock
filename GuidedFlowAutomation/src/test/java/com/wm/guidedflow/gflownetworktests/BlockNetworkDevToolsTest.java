package com.wm.guidedflow.gflownetworktests;

import com.google.common.collect.ImmutableList;
import com.wm.guidedflow.gflowbase.BlockNetworkBaseTest;
import com.wm.guidedflow.pages.ResidentialPages;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v125.network.Network;
import org.openqa.selenium.devtools.v125.network.model.RequestId;
import org.openqa.selenium.devtools.v125.network.model.Response;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

public class BlockNetworkDevToolsTest extends BlockNetworkBaseTest {

	String requestUrlToBlock;

	@Test
    public void blockNetworkRequestsValidation() throws InterruptedException, IOException {

		//Test Data:
		String appURL = "https://wmqa3.wm.com/";
		String addressToEnter = "1235 Lorraine Street, Houston, TX, USA";

		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(Duration.ofMillis(20000));
		long startTime = System.currentTimeMillis();

		driver.get(appURL);
		ResidentialPages resiPages = new ResidentialPages(driver);
		//resiPages.clickOnAcceptCookiesButton();
		Thread.sleep(5000);

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

		DevTools devTools =driver.getDevTools();
		devTools.createSession();
		devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
		devTools.send(Network.setBlockedURLs(ImmutableList.of( requestUrlToBlock )));
		Thread.sleep(5000);

		resiPages.clickOnCloseButton();
		Thread.sleep(5000);

		resiPages.clickOnCheckAvailabilityButton();
		Thread.sleep(5000);

		String actualErrorMessage = resiPages.getErrorElementText();
		String expectedErrorMessage = "Sorry, we can't find that address. Please try again.";
		System.out.println("actualErrorMessage: "+actualErrorMessage);
		System.out.println("expectedErrorMessage: "+expectedErrorMessage);

		resiPages.clickOnCheckAvailabilityButton();
		Thread.sleep(5000);

		resiPages.clickOnCheckAvailabilityButton();
		Thread.sleep(5000);

		String contactUsHeaderText = resiPages.getContactUsHeaderText();
		System.out.println("Popup with \""+contactUsHeaderText+"\" header text has been displayed");

		//UnBlocking the network url
		devTools.send(Network.disable());
		Thread.sleep(5000);

		resiPages.clickOnCloseXButtonByJavaScript();
		Thread.sleep(5000);

		resiPages.clickOnCheckAvailabilityButton();
		Thread.sleep(5000);

		//Printing total execution time
		long endTime = System.currentTimeMillis();
		System.out.println(endTime - startTime);

		driver.quit();
	}
	public void getResponseUrlFromDevTools(ChromeDriver driver) {
		DevTools devTools = driver.getDevTools();
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
			if(url.contains("products")) {
				if(response.getStatus()==200) {
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
	}

}
