package com.wm.guidedflow.devtoolspoc;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;

public class SetGeoLocation {

	public static void main(String[] args) {

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--incognito");

		ChromeDriver driver = new ChromeDriver();
		//40 3
		
		DevTools devTools = driver.getDevTools();
		devTools.createSession();
		
		Map<String,Object>coordinates = new HashMap<String,Object>();
		//40 3
		coordinates.put("latitude", 53);
		coordinates.put("longitude", 37);
		coordinates.put("accuracy", 1);

		driver.manage().window().maximize();
		driver.executeCdpCommand("Emulation.setGeolocationOverride", coordinates);
		driver.get("http://google.com");
		driver.findElement(By.name("q")).sendKeys("netflix",Keys.ENTER);
//		driver.findElements(By.cssSelector(".LC20lb")).get(0).click();
		driver.findElement(By.xpath("//a[@jsname='UWckNb']/h3")).click();
		String title =driver.findElement(By.xpath("//h1[@data-uia='nmhp-card-hero-text-title']")).getText();
		System.out.println(title);
		driver.quit();
	}

}
