package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.Driver;

import java.util.List;

public class LinkedinPosts_Pages {

    public LinkedinPosts_Pages(){ PageFactory.initElements(Driver.getDriver(),this); }

   @FindBy(css = ".artdeco-global-alert__dismiss")
    public WebElement cookiesPopUpClose;

    @FindBy (css ="#session_key")
    public WebElement emailInputBox;

    @FindBy (css= "#session_password")
    public WebElement passwordInputBox;

    @FindBy (css = ".sign-in-form__submit-button")
    public WebElement signInButton;

    @FindBy (xpath= "(//a[@href='/jobs/'])[2]")
    public WebElement linkedinSearchJobsLink;

    @FindBy (xpath= "//input[contains(@id,'obs-search-box-keyword-id-ember')]")
    public WebElement jobTitleSearchBox;

    @FindBy (xpath= "//input[contains(@id,'obs-search-box-location-id-ember')]")
    public WebElement jobLocationSearchBox;

    @FindBy (xpath = "//button[@type='submit']")
    public WebElement jobSearchButton;

    //@FindBy (xpath= "//a[contains(@class, 'job-card-container__link job-card-list__title')]")
    //public WebElement linkedinJobTitleLink;

    @FindBy (xpath = "//section[@class='jobs-search__right-rail']")
    public WebElement jobDescription;

    //change the page number dynamically
//    @FindBy (xpath ="//button[@aria-label='Page 2']")
//    public WebElement pageNumber;
}
