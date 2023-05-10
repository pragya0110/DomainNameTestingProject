package testcases;


import base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static Utility.ExcelUtil.readData;
import static org.testng.Assert.assertTrue;

public class SearchResultTest extends TestBase {
    List<WebElement> searchResult;
    String validDomainName,invalidDomainName;
    @DataProvider(name = "searchQueries")
    public static Object[][] getData(){
        return readData();
    }
    //Verify relevant results for valid search queries
    @Test(dataProvider = "searchQueries")
    public void testValidDomainSearch(String validDomainName) throws InterruptedException {
        //validDomainName= getDomainName("validDomainName");
        type("searchbar_XPATH",validDomainName);
        click("searchbutton_XPATH");
        wait.until(ExpectedConditions.visibilityOfAllElements(listOfElement("searchbutton_XPATH")));
        searchResult=listOfElement("searchresult_XPATH");
        Assert.assertTrue(searchResult.size() > 0,"No search results found");

        boolean keyFound = false;
        for(WebElement result: searchResult){
            if(result.getText().contains(validDomainName))
                keyFound = true;
                break;
        }
        Assert.assertTrue( keyFound,"Search key not found in any search result");
    }
    //Verify no response for invalid search queries
    @Test
    public void testInValidDomainSearch() throws InterruptedException {
        invalidDomainName= getDomainName("invalidDomainName");
        type("searchbar_XPATH",invalidDomainName);
        click("searchbutton_XPATH");
        Assert.assertTrue(isElementPresent(By.tagName("h1")));
    }
    //Verify search functionality for different domain extensions
    @Test
    public void testDomainExtensions()
    {

        searchResult= listOfElement("domainExtensions_XPATH");
        for(WebElement result:searchResult){
            result.click();
            System.out.println(result.getAttribute("class")+result.getText());
            //Verify whether the selected domain extension is currently active by checking its status.
            Assert.assertTrue(result.getAttribute("class").contains("active"));
            //Verify that the selected domain extension is accurately displayed in the search bar.
            Assert.assertEquals(getWebElement("searchbar_XPATH").getAttribute("value"),result.getText());

        }
    }

}
