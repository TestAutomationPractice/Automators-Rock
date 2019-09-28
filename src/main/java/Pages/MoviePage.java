package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import Conferencing.Test.ComponentBase;
import Miscellaneous.Log4JLogger;
import Miscellaneous.TestDataHelper;

public class MoviePage extends ComponentBase{

	@FindBy(how = How.NAME, using = "search")
	WebElement movie_search;
	
	public MoviePage(WebDriver driver) {
		super(driver);
	}
	
	public void setDependencies(Log4JLogger logger, TestDataHelper testDataHelper) {
		super.setDependencies(logger, testDataHelper);
	}
	
	public void searchMovie(String movieName){
		log("Method called: searchMovie");
		movie_search.sendKeys(movieName);
	}
	
}
