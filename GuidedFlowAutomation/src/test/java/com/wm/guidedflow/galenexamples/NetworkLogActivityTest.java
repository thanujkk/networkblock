package com.wm.guidedflow.galenexamples;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v124.network.Network;
import org.openqa.selenium.devtools.v124.network.model.Request;
import org.openqa.selenium.devtools.v124.network.model.Response;

import java.util.Optional;

public class NetworkLogActivityTest {

	public static void main(String[] args) {

		ChromeDriver driver = new ChromeDriver();
		//log file ->
		
		DevTools devTools = driver.getDevTools();
		devTools.createSession();
		devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
		devTools.addListener(Network.requestWillBeSent(), request ->
				{
					Request req = request.getRequest();
					System.out.println(req.getUrl());
					//req.getHeaders()
				});
	
		//Event will get fired-
		devTools.addListener(Network.responseReceived(), response ->
		{
			Response res = response.getResponse();
			System.out.println(res.getUrl());
			System.out.println(res.getStatus());
			if(res.getStatus().toString().startsWith("4"))
			{
				System.out.println(res.getUrl()+"is failing with status code"+res.getStatus());
			}
		});
	
		driver.get("https://wmqa3.wm.com");
		driver.findElement(By.xpath("//a[@type='submit'][contains(text(), 'Get Inspired')]")).click();
		driver.quit();
	}
}
