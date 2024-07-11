package com.wm.guidedflow.devtoolspoc;

import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
//import org.openqa.selenium.devtools.
import org.openqa.selenium.devtools.v124.network.Network;

import com.google.common.collect.ImmutableList;

public class BlockNetworkRequests {

	public static void main(String[] args) throws InterruptedException {
				//css , images

		ChromeDriver driver = new ChromeDriver();
		//log file ->
		
		DevTools devTools = driver.getDevTools();
		devTools.createSession();
		devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
		devTools.send(Network.setBlockedURLs(ImmutableList.of("*.jpg", "*.jpeg")));
		long startTime = System.currentTimeMillis();
		driver.manage().window().maximize();;
		driver.get("https://wmqa3.wm.com/");
		Thread.sleep(5000);
		driver.findElement(By.xpath("//button[@type='button' and @analytics='Residential']")).click();
		driver.findElement(By.xpath("//div[@class='d-flex align-items-center justify-content-between '][normalize-space()='Residential Trash & Recycling Pickup']")).click();
		Thread.sleep(5000);
		WebElement textBox = driver.findElement(By.xpath("//input[@class='geosuggest__input']"));
		textBox.click();
		textBox.sendKeys("Enter text into the address field");
		System.out.println(driver.findElement(By.xpath("//input[@class='geosuggest__input']")).getAttribute("value"));
		long endTime = System.currentTimeMillis();
		System.out.println(endTime - startTime);
		//1793 2033
		Thread.sleep(5000);
		driver.quit();
	}

}
