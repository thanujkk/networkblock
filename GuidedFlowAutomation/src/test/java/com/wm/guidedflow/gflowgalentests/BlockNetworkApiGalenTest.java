package com.wm.guidedflow.gflowgalentests;

import com.google.common.collect.ImmutableList;
import com.wm.guidedflow.gflowgalentests.GuidedFlowLayoutGalenTest;
import com.wm.guidedflow.pages.ResidentialPages;
import io.percy.selenium.Percy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v125.network.Network;
import org.openqa.selenium.devtools.v125.network.model.RequestId;
import org.openqa.selenium.devtools.v125.network.model.Response;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

public class BlockNetworkApiGalenTest {

	String requestUrlToBlock;

	@Test
    public void blockNetworkRequestsValidation() throws InterruptedException, IOException {

		//Test Data:
		String appURL = "https://wmqa3.wm.com/";
		//String urlToBlock = "https://webapiqat1.wm.com/v1/catalogs/products?latitude=29.7785555&longitude=-95.35493&city=Houston&state=TX&street=1235%20Lorraine%20St&zipcode=77009&country=US&locale=en_US&product_type=curb_side&service_type=&number_of_days=0";
		String addressToEnter = "1235 Lorraine Street, Houston, TX, USA";

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--incognito");
		ChromeDriver driver = new ChromeDriver(options);
		long startTime = System.currentTimeMillis();
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(Duration.ofMillis(20000));

		driver.get(appURL);
		Percy percy = new Percy(driver);
		ResidentialPages resiPages = new ResidentialPages(driver);
		resiPages.clickOnAcceptCookiesButton();
		Thread.sleep(5000);

		resiPages.clickOnResidentialLink();
		Thread.sleep(10000);

		resiPages.clickOnResidentialTrashLink();
		Thread.sleep(10000);

		//Validating alignments for Residential Home Page layout
		GuidedFlowLayoutGalenTest guidedFlowLayoutGalenTest = new GuidedFlowLayoutGalenTest(driver);
		guidedFlowLayoutGalenTest.residentialHomPageTest();

		//using percy snapshot
		percy.snapshot("ResidentialHomPage");
		Thread.sleep(5000);

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

		//WebElement serviceUnavailableHeader = driver.findElement(By.xpath("//button[contains(text(), 'Close')]/ancestor::div/div/div/h4]"));
		//String serviceUnavailableHeaderText = serviceUnavailableHeader.getText();
		//System.out.println("Popup with \""+serviceUnavailableHeaderText+"\" header text has been displayed");

		//Validating image to image for Service Unavailable Popup
		//guidedFlowLayoutTest.serviceUnavailableComponentTest();

		DevTools devTools = driver.getDevTools();;
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

		//System.out.println("Popup with \""+serviceUnavailableHeaderText+"\" header text has been displayed");

		//Printing total execution time
		long endTime = System.currentTimeMillis();
		System.out.println(endTime - startTime);

		//driver.quit();
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
