package tests;

//import org.junit.Before;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.interactions.Locatable;
import pages.LinkedinPosts_Pages;
import utilities.ConfigurationReader;
import utilities.ConfigurationSecretReader;
import utilities.Driver;
import utilities.ReusableMethods;

import javax.lang.model.SourceVersion;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinkedinPosts {

    LinkedinPosts_Pages LinkedinPages;
    JavascriptExecutor jse= (JavascriptExecutor) Driver.getDriver();
    List<Map<String, String>> jobDescriptionList= new ArrayList<>();
    Actions actions = new Actions(Driver.getDriver());

    @Before
    public void setup(){

        LinkedinPages=new LinkedinPosts_Pages();

        Driver.getDriver().get(ConfigurationReader.getProperty("linkedinURL"));

        try{
            LinkedinPages.cookiesPopUpClose.click();
        }
        catch (Exception e){

        }

        LinkedinPages.emailInputBox.sendKeys(ConfigurationSecretReader.getProperty("linkedinEmail"));
        LinkedinPages.passwordInputBox.sendKeys(ConfigurationSecretReader.getProperty("linkedinPassword"));
        LinkedinPages.signInButton.click();


    }

    //@Test
    public void test(){

        ReusableMethods.waitForVisibility(LinkedinPages.linkedinSearchJobsLink, 180);
        LinkedinPages.linkedinSearchJobsLink.click();
        String jobTitle = "tester";
        ReusableMethods.waitForVisibility(LinkedinPages.jobTitleSearchBox, 180);
        LinkedinPages.jobTitleSearchBox.sendKeys(jobTitle);
        ReusableMethods.waitForVisibility(LinkedinPages.jobLocationSearchBox, 180);
        String jobLocation = "Valence";
        LinkedinPages.jobLocationSearchBox.sendKeys(jobLocation);
        jse.executeScript("arguments[0].click();", LinkedinPages.jobSearchButton);

        List<WebElement> Pages=Driver.getDriver().findElements(By.xpath("//section[@aria-label='pagination']//li"));
        int lastPageNum=Pages.size()-1;
        System.out.println("lastPageNum = " + (lastPageNum+1));
        System.out.println("Last Page Text = " + Pages.get(lastPageNum).getText());
        System.out.println("Pages.get(lastPageNum).toString() = " + Pages.get(lastPageNum).toString());
        //actions.moveToElement(Driver.getDriver().findElement(By.xpath("("(//div[@data-job-id])[i+7]"))).perform();
        actions.moveToElement(Driver.getDriver().findElement(By.xpath("//section[@aria-label='pagination']"))).perform();
        //jse.executeScript("arguments[0].click();", Driver.getDriver().findElement(By.xpath("(//div[@data-job-id])[25]")));

        WebElement pagetwo= Driver.getDriver().findElement(By.xpath("//button[@aria-label='Page 2']"));
        // System.out.println("Driver.getDriver().findElement(By.xpath(\"//span[.='2']\")).getLocation().getY() = " + Driver.getDriver().findElement(By.xpath("//span[.='2']")).getLocation().getY());
        jse.executeScript("arguments[0].click();", pagetwo);
        actions.moveToElement(Driver.getDriver().findElement(By.xpath("//section[@aria-label='pagination']"))).perform();
        ReusableMethods.waitFor(5);
        System.out.println("Last Page Text = " + Pages.get(8).getText());

    }

        @Test
        public void linekdinJobPosts() throws InterruptedException {

        ReusableMethods.waitForVisibility(LinkedinPages.linkedinSearchJobsLink, 180);
        LinkedinPages.linkedinSearchJobsLink.click();
        String jobTitle = "tester";
        ReusableMethods.waitForVisibility(LinkedinPages.jobTitleSearchBox, 180);
        LinkedinPages.jobTitleSearchBox.sendKeys(Keys.CONTROL + "a");
        LinkedinPages.jobTitleSearchBox.sendKeys(Keys.DELETE);
        LinkedinPages.jobTitleSearchBox.sendKeys(jobTitle);
        ReusableMethods.waitForVisibility(LinkedinPages.jobLocationSearchBox, 180);
        String jobLocation = "Valence";
        LinkedinPages.jobLocationSearchBox.sendKeys(Keys.CONTROL + "a");
        LinkedinPages.jobLocationSearchBox.sendKeys(Keys.DELETE);
        LinkedinPages.jobLocationSearchBox.sendKeys(jobLocation);
        jse.executeScript("arguments[0].click();", LinkedinPages.jobSearchButton);


        ReusableMethods.waitFor(5);
        List<WebElement> Pages=Driver.getDriver().findElements(By.xpath("//section[@aria-label='pagination']//li"));
        System.out.println("Pages = " + Pages.get((Pages.size()-1)).getText());
        int lastPageNum= Integer.parseInt(Pages.get(Pages.size()-1).getText());
        String Pageexception="...";
        int PageNum=1;

        do{

        for (int i = 1; i < 26; i++) {
            Map<String, String> jobDetailsMap= new HashMap<>();
            Thread.sleep(500);
            jse.executeScript("window.scrollBy(0,3000);");
            try {
                actions.moveToElement(Driver.getDriver().findElement(By.xpath("(//div[@data-job-id])[" + i + "]"))).perform();

                ReusableMethods.waitForClickablility(Driver.getDriver().findElement(By.xpath("(//div[@data-job-id])[" + i + "]")),30);

                List<WebElement> jobDetails = Driver.getDriver().findElements(By.xpath("(//div[@data-job-id])[" + i + "]/div/div[2]/div"));
                String jobDetailsTitle = jobDetails.get(0).getText();
                String jobDetailsCompany = jobDetails.get(1).getText();
                String jobDetailsLocation = jobDetails.get(2).getText();

                String jobDetailsSalary ="";
                for (int j = 3; j <jobDetails.size() ; j++) {
                    jobDetailsSalary +=  jobDetails.get(j).getText() + " ";
                }

                jobDetailsMap.put("jobDetailsTitle",jobDetailsTitle);
                jobDetailsMap.put("jobDetailsCompany",jobDetailsCompany);
                jobDetailsMap.put("jobDetailsLocation",jobDetailsLocation);
                jobDetailsMap.put("jobDetailsSalary",jobDetailsSalary);

                jse.executeScript("arguments[0].click();", Driver.getDriver().findElement(By.xpath("(//div[@data-job-id])[" + i + "]")));
                String jobDescription = LinkedinPages.jobDescription.getText();
                jobDetailsMap.put("Job_Description", jobDescription);

                jobDescriptionList.add(jobDetailsMap);

                Pages=Driver.getDriver().findElements(By.xpath("//section[@aria-label='pagination']//li"));
                List<WebElement> jobTitleLinks=Driver.getDriver().findElements(By.xpath("//div[@data-job-id]"));
                if(i == jobTitleLinks.size()){
                    PageNum++;
                    System.out.println("Current Page Number: " + PageNum);
                    actions.moveToElement(Driver.getDriver().findElement(By.xpath("//section[@aria-label='pagination']"))).perform();
                    try {
                        WebElement pageButton= Driver.getDriver().findElement(By.xpath("//button[@aria-label='Page " + PageNum +"']"));
                        jse.executeScript("arguments[0].click();", pageButton);
                    }catch (Exception e2){
                        PageNum++;
                        break;
                    }
             }
            }catch(Exception e) {
                System.out.println("Second catch entered");

            }
        }
        }while(PageNum <= lastPageNum+1);

        for (int i = 0; i < jobDescriptionList.size(); i++) {
            System.out.println("================================================");
//            System.out.println(i+1+ ". JOB DESCRIPTION = " +  jobDescriptionList.get(i));
            for (String w : jobDescriptionList.get(i).keySet()) {
                System.out.println(w +  ": " + jobDescriptionList.get(i).get(w));
            }
        }

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("jobDescriptionList.size() = " + jobDescriptionList.size());

   }

    @After
    public void MongoDBDataSaving() {
        String connectionString = ConfigurationReader.getPropertySecrets("MongoDBUrl");

        try (MongoClient mongoClient = MongoClients.create(connectionString)) {

            String database = ConfigurationReader.getPropertySecrets("MongoDBDatabase");
            String collection = ConfigurationReader.getPropertySecrets("MongoDBLinkedinCollection");

            MongoDatabase sampleTrainingDB = mongoClient.getDatabase(database);
            MongoCollection<Document> gradesCollection = sampleTrainingDB.getCollection(collection);

            List<Document> documentList = new ArrayList<>();
            int numDocuments = (int) gradesCollection.countDocuments();
            System.out.println(numDocuments);
            int documentID = numDocuments + 1;

            for (int i = 0; i < jobDescriptionList.size(); i++) {
                Document jobDocument = new Document("_id", new ObjectId());
                jobDocument.append("Job_id", documentID++);

                for (String w : jobDescriptionList.get(i).keySet()) {
                    jobDocument.append(w, jobDescriptionList.get(i).get(w));
                }
                documentList.add(jobDocument);
            }

            gradesCollection.insertMany(documentList);

            System.out.println("Total Added Data: " + (documentID - numDocuments));
            System.out.println("Total Data: " + documentID);
        }
    }

    @AfterClass
    public static void tearDown() {
        Driver.closeDriver();
    }



}
