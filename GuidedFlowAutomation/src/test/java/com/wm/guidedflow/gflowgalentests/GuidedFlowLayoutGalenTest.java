package com.wm.guidedflow.gflowgalentests;

import com.galenframework.api.Galen;
import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.HtmlReportBuilder;
import com.galenframework.reports.model.LayoutReport;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
//import org.testng.annotations.Test;

public class GuidedFlowLayoutGalenTest
{
    private final WebDriver driver;


    public GuidedFlowLayoutGalenTest(WebDriver driver){
        this.driver = driver;
    }
    @Test
    public void residentialHomPageTest() throws IOException
    {
        //driver.findElement(By.xpath("//button[@type='button' and @analytics='Residential']")).click();
        //driver.findElement(By.xpath("//div[@class='d-flex align-items-center justify-content-between '][normalize-space()='Residential Trash & Recycling Pickup']")).click();

        //Create a layoutReport object
        //checkLayout function checks the layout and returns a LayoutReport object
        //LayoutReport layoutReport = Galen.checkLayout(driver, "specs/wmResiPage.gspec", Arrays.asList("desktop"));
        LayoutReport layoutReport = Galen.checkLayout(driver, "specs/wmResiPageFullPass.gspec", Arrays.asList("desktop"));

        //Create a tests list
        List<GalenTestInfo> tests = new LinkedList<GalenTestInfo>();

        //Create a GalenTestInfo object
        GalenTestInfo test = GalenTestInfo.fromString("Residential Page Layout");

        //Get layoutReport and assign to test object
        test.getReport().layout(layoutReport, "Check Residential Homepage Layout");

        //Add test object to the tests list
        tests.add(test);

        //Create a htmlReportBuilder object
        HtmlReportBuilder htmlReportBuilder = new HtmlReportBuilder();

        //Create a report under /target folder based on tests list
        htmlReportBuilder.build(tests, "target/galen-html-report/residential-page-"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

        //If layoutReport has errors Assert Fail
        if (layoutReport.errors() > 0)
        {
            Assert.fail("Layout test failed");
        }
    }

    @Test
    public void serviceUnavailableComponentTest() throws IOException
    {
        //driver.findElement(By.xpath("//button[@type='button' and @analytics='Residential']")).click();
        //driver.findElement(By.xpath("//div[@class='d-flex align-items-center justify-content-between '][normalize-space()='Residential Trash & Recycling Pickup']")).click();

        //Create a layoutReport object
        //checkLayout function checks the layout and returns a LayoutReport object
      LayoutReport layoutReport = Galen.checkLayout(driver, "specs/wmResiPage.gspec", Arrays.asList("desktop"));
//        LayoutReport layoutReport = Galen.checkLayout(driver, "specs/serviceUnavailablePopup.gspec", Arrays.asList("desktop"));

        //Create a tests list
        List<GalenTestInfo> tests = new LinkedList<GalenTestInfo>();

        //Create a GalenTestInfo object
        GalenTestInfo test = GalenTestInfo.fromString("Residential Page Layout");

        //Get layoutReport and assign to test object
        test.getReport().layout(layoutReport, "Check Residential Homepage Layout");

        //Add test object to the tests list
        tests.add(test);

        //Create a htmlReportBuilder object
        HtmlReportBuilder htmlReportBuilder = new HtmlReportBuilder();

        //Create a report under /target folder based on tests list
        htmlReportBuilder.build(tests, "target/galen-html-report/serviceUnavailable-"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

        //If layoutReport has errors Assert Fail
        if (layoutReport.errors() > 0)
        {
            Assert.fail("Layout test failed");
        }
    }
}
