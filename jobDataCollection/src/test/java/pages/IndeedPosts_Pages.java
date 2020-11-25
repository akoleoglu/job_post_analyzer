package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import utilities.Driver;

import java.util.List;

public class IndeedPosts_Pages {

    public IndeedPosts_Pages() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy(xpath = "//span[.='Abc']")
    public WebElement firstLocate;

    @FindBy(xpath = "//input[contains (@id,'what')]")
    public WebElement JobTitleInput;

    @FindBy(xpath = "//input[contains (@id,'where')]")
    public WebElement JobLocationInput;

    @FindBy(xpath = "//button[contains(@class, 'WhatWhere-button')]")
    public WebElement JobSearchButton;

    @FindBy(xpath = "//div[contains(@class,'clickcard')]/h2/a")
    //@FindBy(xpath = "//h2/a")
    public List<WebElement> jobLinksList;

    @FindBy(id = "vjs-container-iframe")
    public WebElement jobDescriptionIFrame;

    @FindBy(id = "vjs-desc")
    public WebElement jobDescriptionTextEurope;

     @FindBy(xpath = "//*[@id='jobDescriptionText']")
    public WebElement jobDescriptionTextUSA;

    @FindBy(xpath = "//button[@id='onetrust-accept-btn-handler']")
    public WebElement cookiesAcceptButton;


}
