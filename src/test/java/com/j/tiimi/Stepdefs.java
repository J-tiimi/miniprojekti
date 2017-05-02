package com.j.tiimi;
// tänne tulee cucumberin testimetodeja

import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.io.IOException;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


public class Stepdefs {
	WebDriver driver = new HtmlUnitDriver(true);
	String baseUrl = "http://localhost:8080";

    @Given("^user visits input form for references and chooses \"([^\"]*)\" for type$")
    public void user_visits_input_form_for_references_and_chooses_for_type(String type) throws Throwable {
		driver.get(baseUrl + "/" + type);
    }

	@Given("^there are no references in the system$")
	public void there_are_no_references_in_the_system() throws Throwable {
		driver.get(baseUrl + "/list");
		assertFalse(driver.getPageSource().contains("book"));
		assertFalse(driver.getPageSource().contains("article"));
		assertFalse(driver.getPageSource().contains("inproceedings"));
	}

	@Given("^book reference has been added to the system$")
	public void book_reference_has_been_added_to_the_system() throws Throwable {
		driver.get(baseUrl + "/book");
		bookInput("asd", "asd", "asd", "asd", "1999");
		driver.get(baseUrl);
	}

	@Given("^article reference has been added to the system$")
	public void article_reference_has_been_added_to_the_system() throws Throwable {
		driver.get(baseUrl + "/article");
		articleInput("asd", "asd", "asd", "asd", "1999", "asd");
		driver.get(baseUrl);
	}

	@Given("^inproceedings reference has been added to the system$")
	public void inproceedings_reference_has_been_added_to_the_system() throws Throwable {
		driver.get(baseUrl + "/inproceedings");
		inproceedingsInput("asd", "asd", "asd", "asd", "1999");
		driver.get(baseUrl);
	}

    @When("^user inputs id \"([^\"]*)\", author \"([^\"]*)\", title \"([^\"]*)\", journal \"([^\"]*)\", year \"([^\"]*)\" and volume \"([^\"]*)\"$")
    public void user_inputs_id_author_title_journal_and_year(String id, String author, String title, String journal, String year, String volume) throws Throwable {
		articleInput(id, author, title, journal, year, volume);
	}

	@When("^user inputs id \"([^\"]*)\", author \"([^\"]*)\", title \"([^\"]*)\", publisher \"([^\"]*)\" and year \"([^\"]*)\"$")
	public void user_intputs_id_author_title_publisher_and_year(String id, String author, String title, String publisher, String year) throws Throwable {
		bookInput(id, author, title, publisher, year);
	}

	@When("^user inputs id \"([^\"]*)\", author \"([^\"]*)\", title \"([^\"]*)\", booktitle \"([^\"]*)\" and year \"([^\"]*)\"$")
	public void user_inputs_id_author_title_publisher_and_year(String id, String author, String title, String booktitle, String year) {
		inproceedingsInput(id, author, title, booktitle, year);
	}

	@When("^user visits the listings page$")
	public void user_visits_the_listings_page() throws Throwable {
		driver.get(baseUrl + "/list");
	}

    @Then("^reference is not saved and error message is shown$")
    public void reference_is_not_saved_and_error_message_is_shown() throws Throwable {
		assertFalse(driver.getPageSource().contains("Book"));
		assertFalse(driver.getPageSource().contains("Article"));
		assertFalse(driver.getPageSource().contains("Inproceedings"));
    }

	@Then("^new reference is added to the system$")
	public void new_reference_is_added_to_the_system() throws Throwable {
		driver.get(baseUrl + "/list");
		assertTrue(driver.getPageSource().contains("@"));
	}

	@Then("^that book reference is shown on the page$")
	public void that_book_reference_is_shown_on_the_page() throws Throwable {
		driver.get(baseUrl + "/list");
		assertTrue(driver.getPageSource().contains("Book"));
	}

	@Then("^that article reference is shown on the page$")
	public void that_article_reference_is_shown_on_the_page() throws Throwable {
		driver.get(baseUrl + "/list");
		assertTrue(driver.getPageSource().contains("Article"));
	}

	@Then("^that inproceedings reference is shown on the page$")
	public void that_inproceedings_reference_is_shown_the_page() throws Throwable {
		driver.get(baseUrl + "/list");
		assertTrue(driver.getPageSource().contains("Inproceedings"));
	}

	private void createReferences() {
		driver.get(baseUrl + "/test");
		driver.get(baseUrl);
	}

	private void bookInput(String id, String author, String title, String publisher, String year) {
		WebElement element = driver.findElement(By.name("identifier"));
		element.sendKeys(id);
		element = driver.findElement(By.name("author"));
		element.sendKeys(author);
		element = driver.findElement(By.name("title"));
		element.sendKeys(title);
		element = driver.findElement(By.name("publisher"));
		element.sendKeys(publisher);
		element = driver.findElement(By.name("year"));
		element.sendKeys(year);
		element.submit();
	}

	private void articleInput(String id, String author, String title, String journal, String year, String volume) {
		WebElement element = driver.findElement(By.name("identifier"));
		element.sendKeys(id);
		element = driver.findElement(By.name("author"));
        element.sendKeys(author);
        element = driver.findElement(By.name("title"));
        element.sendKeys(title);
        element = driver.findElement(By.name("journal"));
        element.sendKeys(journal);
        element = driver.findElement(By.name("year"));
        element.sendKeys(year);
		element = driver.findElement(By.name("volume"));
		element.sendKeys(volume);
		element.submit();
	}

	private void inproceedingsInput(String id, String author, String title, String booktitle, String year) {
		WebElement element = driver.findElement(By.name("identifier"));
		element.sendKeys(id);
		element = driver.findElement(By.name("author"));
        element.sendKeys(author);
        element = driver.findElement(By.name("title"));
        element.sendKeys(title);
        element = driver.findElement(By.name("booktitle"));
        element.sendKeys(booktitle);
        element = driver.findElement(By.name("year"));
        element.sendKeys(year);
		element.submit();
	}
}
