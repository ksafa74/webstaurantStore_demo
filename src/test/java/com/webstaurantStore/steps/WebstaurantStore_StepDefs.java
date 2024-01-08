package com.webstaurantStore.steps;

import com.webstaurantStore.pages.WebstaurantStore_Search_Page;
import com.webstaurantStore.utility.Driver;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.junit.rules.ErrorCollector;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.hamcrest.Matchers.is;

public class WebstaurantStore_StepDefs {

    public ErrorCollector errorCollector = new ErrorCollector();

    WebstaurantStore_Search_Page searchPage = new WebstaurantStore_Search_Page();


    @Given("user is on the search page")
    public void user_is_on_the_search_page() {

       Assert.assertEquals("https://www.webstaurantstore.com/",Driver.getDriver().getCurrentUrl());
    }

    @When("user enters {string} on the search box")
    public void userEntersOnTheSearchBox(String searchItem) {

        searchPage.clickSearchBox.click();
        searchPage.searchBox.sendKeys(searchItem);

    }

    @And("user click on search button")
    public void userClickOnSearchButton() {

        searchPage.searchButton.click();
    }

    @Then("every product should have the word {string} in it's title")
    public void everyProductShouldHaveTheWordInItSTitle(String expectedWord) {

        JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();

        for (int i = 0; i < 8; i++) {
            js.executeScript("scrollBy(0,750)");
        }


        try {
            while (searchPage.rightPageArrow.isDisplayed()) {

                for (int i = 0; i < 8; i++) {

                    js.executeScript("scrollBy(0,750)");
                }

                for (WebElement eachItemInTablesList : searchPage.tablesList) {

                    errorCollector.checkThat(eachItemInTablesList.getText().toLowerCase().contains("table"), is(true));

                    if(!eachItemInTablesList.getText().toLowerCase().contains("table")){
                        System.out.println("Element does not contain \"table\" = " + eachItemInTablesList.getText());
                    }

                }
                searchPage.rightPageArrow.click();
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    //============================================Second Scenario================================================

    @When("user add the last item on the search list to Cart")
    public void userAddTheLastItemToCart() {

        searchPage.lastPages.click();

        List<WebElement> lastPageList = Driver.getDriver().findElements(By.xpath("//span[@data-testid ='itemDescription']"));

        WebElement lastItemOnLIST = lastPageList.get(lastPageList.size()-1);

        //GUIUtils.waitForClickability(lastItemOnLIST,2000);

        lastItemOnLIST.click();

        searchPage.addToCartButton.click();

    }


    @And("user click view Cart")
    public void userClickViewCart() {

        searchPage.viewCartElement.click();

    }

    @Then("user should see item on the cart")
    public void userSeeItemOnTheCart() {

        Assert.assertTrue("Item added to cart verification failed", searchPage.lastAddedItemToCart.isDisplayed());
    }


    @When("user click empty cart")
    public void userClickEmptyCart() {

        searchPage.emptyCartElement1.click();
        searchPage.emptyCartElement2.click();
    }

    @Then("user should see {string} message")
    public void userShouldSeeMessage(String expectedEmptyCartMessage) {

        String actualEmptyCartMessage = searchPage.emptyCartMessage.getText();

        Assert.assertEquals("Empty cart message verification failed", expectedEmptyCartMessage, actualEmptyCartMessage);
    }


}
