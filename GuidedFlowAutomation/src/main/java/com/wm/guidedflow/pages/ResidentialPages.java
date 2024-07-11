package com.wm.guidedflow.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ResidentialPages {
    WebDriver driver;
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(30000));
    //wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@id='onetrust-accept-btn-handler']")));
    public ResidentialPages(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    //@FindBy(css = "button#onetrust-accept-btn-handler")
    @FindBy(xpath = "//button[@id='onetrust-accept-btn-handler']")
    private WebElement acceptCookiesButton;
    public void clickOnAcceptCookiesButton(){
        wait.until(ExpectedConditions.visibilityOf(acceptCookiesButton));
        if(acceptCookiesButton.isDisplayed()) {
            acceptCookiesButton.click();
        }
    }
    @FindBy(xpath = "//button[@type='button' and @analytics='Residential']")
    private WebElement residentialLink;
    public void clickOnResidentialLink(){
        residentialLink.click();
    }
    @FindBy(xpath = "//div[@class='d-flex align-items-center justify-content-between '][normalize-space()='Residential Trash & Recycling Pickup']")
    private WebElement residentialTrashLink;
    public void clickOnResidentialTrashLink(){
        residentialTrashLink.click();
    }
    @FindBy(xpath = "//input[@class='geosuggest__input']")
    private WebElement addressBox;
    public void enterAddressInTheAddressBox(String address){
        addressBox.sendKeys(address);
    }
    public String getAddressBoxValue(){
        return addressBox.getAttribute("value");
    }

    @FindBy(xpath = "//button[text()='Check Availability']")
    private WebElement checkAvailabilityButton;
    public void clickOnCheckAvailabilityButton(){
        checkAvailabilityButton.click();;
    }

    @FindBy(xpath = "//button[@type='submit'][@data-testid='AddressConfirmation - confirm button']")
    private WebElement addressConfirmButton;
    public void clickOnAddressConfirmButton(){
        addressConfirmButton.click();;
    }

    @FindBy(xpath = "//button[contains(text(), 'Close')]")
    private WebElement closeButton;
    public void clickOnCloseButton(){
        closeButton.click();
    }

    @FindBy(xpath = "//div[@data-testid='AddressSearchBar']/div/span/p")
    private WebElement errorElement;
    public String getErrorElementText(){
        return errorElement.getText();
    }

    @FindBy(xpath = "//div/h4[contains(text(), 'Contact Us')]")
    private WebElement contactUsHeader;
    public String getContactUsHeaderText(){
        return contactUsHeader.getText();
    }

    @FindBy(xpath = "//div/button[@type='button' and @aria-label='close']/span")
    private WebElement closeXButton;

    public WebElement getCloseXButton(){
        return closeXButton;
    }
    public void clickOnCloseXButtonByJavaScript(){
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript("arguments[0].click();", getCloseXButton());
    }

}
