package tests;

import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import pages.IndeedPosts_Pages;
import utilities.ConfigurationReader;
import utilities.Driver;
import utilities.ReusableMethods;

import org.bson.Document;

import javax.swing.*;
import java.util.*;

import static com.mongodb.client.model.Filters.*;

public class IndeedPosts {

    IndeedPosts_Pages pages;
    JavascriptExecutor jse = (JavascriptExecutor) Driver.getDriver();
    List<Map<String, String>> jobDescriptionsList = new ArrayList<>();
    String jobSearchTitle;
    String jobSearchLocation;
    String jobSite = "Indeed";
    String next;

//    @Before
    public void setUp() throws Exception {
        pages = new IndeedPosts_Pages();
        String url = "https://indeed.com/";
        Driver.getDriver().get(url);
//        pages.cookiesAcceptButton.click();

    }


//    @Test
    public void getJobPostText() {

        jobSearchTitle = "NLP";
        WebElement jobDescription;

        String pageUrl = Driver.getDriver().getCurrentUrl();
        String countryUrl = pageUrl.substring(8, 10);
        if (countryUrl.contains("fr")) {
            jobSearchLocation = "Paris";
            jobDescription = pages.jobDescriptionTextEurope;
            next = "Suivant";
        } else if (countryUrl.contains("nl")) {
            jobSearchLocation = "Amsterdam";
            jobDescription = pages.jobDescriptionTextEurope;
            next = "Volgende";
        } else if (countryUrl.contains("ww")) {
            jobSearchLocation = "Denver";
            jobDescription = pages.jobDescriptionTextUSA;
            next = "Next";
        } else if (countryUrl.contains("tr")) {
            jobSearchLocation = "Ankara";
            jobDescription = pages.jobDescriptionTextEurope;
            next = "Sonraki";
        } else {
            jobSearchLocation = "Paris";
            next = "Suivant";
            jobDescription = pages.jobDescriptionTextEurope;
        }

        pages.JobTitleInput.sendKeys(jobSearchTitle);
        pages.JobLocationInput.sendKeys(Keys.CONTROL + "a");
        pages.JobLocationInput.sendKeys(Keys.DELETE);
//        ReusableMethods.waitFor(3);

        pages.JobLocationInput.sendKeys(jobSearchLocation);
        pages.JobSearchButton.click();

        boolean hasNext = true;

        while (hasNext) {
            int jobCount = 1;
            for (WebElement jobLink : pages.jobLinksList) {
                ReusableMethods.waitForVisibility(jobLink, 20);
                Map<String, String> jobDetails = new HashMap<>();
                WebElement jobTitle = Driver.getDriver().findElement(By.xpath("(//div[contains(@class,'clickcard')]/h2)[" + jobCount + "]"));
                WebElement jobCompany = Driver.getDriver().findElement(By.xpath("(//div[contains(@class,'clickcard')])[" + jobCount + "]/div[1]/div[1]"));
                WebElement jobLocation = Driver.getDriver().findElement(By.xpath("(//div[contains(@class,'clickcard')])[" + jobCount + "]/div[1]/div[2]"));

                jobDetails.put("jobDetailsTitle", jobTitle.getText());
                jobDetails.put("jobDetailsCompany", jobCompany.getText());
                jobDetails.put("jobDetailsLocation", jobLocation.getText());
                jobDetails.put("jobDetailsSalary", "");
                jobCount++;


                //use executeScript
                jse.executeScript("arguments[0].click();", jobLink);

                //jobLink.click();
                //System.out.println(jobCount++ + " .is icin iframe gecis");
                try {

                    Driver.getDriver().switchTo().frame(pages.jobDescriptionIFrame);

//                    System.out.println(pages.JobDescriptionText.getText());
                    ReusableMethods.waitForVisibility(pages.jobDescriptionTextUSA, 10);
                    jobDetails.put("Job_Description", pages.jobDescriptionTextUSA.getText());
                    //jobDescriptionsList.add(pages.jobDescriptionTextUSA.getText());
                    jobDescriptionsList.add(jobDetails);

                    Driver.getDriver().switchTo().parentFrame();

                } catch (Exception e) {
//                    System.out.println(pages.JobDescriptionText.getText());
                    ReusableMethods.waitForVisibility(jobDescription, 10);
                    jobDetails.put("Job_Description", jobDescription.getText());
                    jobDescriptionsList.add(jobDetails);
                }


            }
            try {
//                Driver.getDriver().findElement(By.xpath("//a[@aria-label='" + next + "']")).click();
                System.out.println("//a[@aria-label='" + next + "']");
                WebElement nextButton = Driver.getDriver().findElement(By.xpath("/a[@aria-label='Volgende']"));
                jse.executeScript("arguments[0].click();", nextButton);

            } catch (Exception e) {
                hasNext = false;
            }
            try {
                Driver.getDriver().findElement(By.xpath("//div[@id='popover-foreground']//button[@aria-label='Close']")).click();
            } catch (Exception e) {

            }

        }

        //   System.out.println(jobDescriptionsList);
    }


//    @After
    public void MongoDBDataSaving() {
        String connectionString = ConfigurationReader.getPropertySecrets("MongoDBUrl");

        try (MongoClient mongoClient = MongoClients.create(connectionString)) {

            String database = ConfigurationReader.getPropertySecrets("MongoDBDatabase");
            String collection = ConfigurationReader.getPropertySecrets("MongoDBIndeedCollection");

            MongoDatabase sampleTrainingDB = mongoClient.getDatabase(database);
            MongoCollection<Document> gradesCollection = sampleTrainingDB.getCollection(collection);

            List<Document> documentList = new ArrayList<>();
            int numDocuments = (int) gradesCollection.countDocuments();
            System.out.println(numDocuments);
            int documentID = numDocuments + 1;
            for (int i = 0; i < jobDescriptionsList.size(); i++) {
                Document jobDocument = new Document("_id", new ObjectId());
                jobDocument.append("Job_id", documentID++);

                for (String w : jobDescriptionsList.get(i).keySet()) {
                    jobDocument.append(w, jobDescriptionsList.get(i).get(w));
                }
                documentList.add(jobDocument);
            }
            gradesCollection.insertMany(documentList);

            System.out.println("Total Added Data: " + (documentID - numDocuments));
            System.out.println("Total Data: " + documentID);
        }

    }

    @Test
    public void deleteSelectedDocuments() {

        boolean doYouWantDelete = false;
        int totalDeletedDocument = 0;

        if (doYouWantDelete) {
            String connectionString = ConfigurationReader.getPropertySecrets("MongoDBUrl");
            try (MongoClient mongoClient = MongoClients.create(connectionString)) {

                String database = ConfigurationReader.getPropertySecrets("MongoDBDatabase");
                String collection = ConfigurationReader.getPropertySecrets("MongoDBIndeedCollection");

                MongoDatabase sampleTrainingDB = mongoClient.getDatabase(database);
                MongoCollection<Document> gradesCollection = sampleTrainingDB.getCollection(collection);

                Bson filter = null;

                Iterator<Document> it = gradesCollection.find().iterator();
                while(it.hasNext()) {
                    Document siradaki = it.next();
                    DeleteResult result = gradesCollection.deleteMany(siradaki);
                    totalDeletedDocument += (int) result.getDeletedCount();
                }
            }

            System.out.println("Total deleted document: " + totalDeletedDocument);
        }else{
            System.out.println("If you want to delete all data in the indeed colection you should set true ");
        }

    }

    @AfterClass
    public static void tearDown() {
        Driver.closeDriver();
    }

}
