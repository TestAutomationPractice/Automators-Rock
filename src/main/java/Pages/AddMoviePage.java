package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import AutomatorsRock.Test.ComponentBase;
import Miscellaneous.Log4JLogger;
import Miscellaneous.TestDataHelper;
import Miscellaneous.Enums.WaitInterval;

public class AddMoviePage extends ComponentBase{

	@FindBy(how = How.NAME, using = "title")
	WebElement movie_title;
	
	@FindBy(how = How.NAME, using = "director")
	WebElement movie_director;
	
	@FindBy(how = How.XPATH, using = "//textarea[@placeholder='Please enter a short description of the movie']")
	WebElement movie_description;
	
	@FindBy(how = How.NAME, using = "categories")
	WebElement movie_categories;
	
	@FindBy(how = How.NAME, using = "file")
	WebElement movie_url;
	
	@FindBy(how = How.XPATH, using = "//button[contains(text(),'Save Movie')]")
	WebElement btn_saveMovie;
	
	@FindBy(how = How.XPATH, using = "//button[contains(text(),'Discard Movie')]")
	WebElement btn_discardMovie;
	
	public AddMoviePage(WebDriver driver) {
		super(driver);
	}
	
	public void setDependencies(Log4JLogger logger, TestDataHelper testDataHelper) {
		super.setDependencies(logger, testDataHelper);
	}
	
	public void enterMovieTitle(String movieTitle){
		log("Method called: enterMovieTitle");
		movie_title.sendKeys(movieTitle);
	}
	
	public void enterMovieDescription(String moviedescription){
		log("Method called: enterMovieDescription");
		new WebDriverWait(driver, WaitInterval.OneMinute.getSeconds()).until(ExpectedConditions.visibilityOf(movie_description));
		/*JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("arguments[0].value='"+ moviedescription +"';", movie_description);*/
		movie_description.sendKeys(moviedescription);
	}
	
	public void enterMovieDirector(String moviedirector){
		log("Method called: enterMovieDirector");
		movie_director.sendKeys(moviedirector);
	}	
	
	public void enterMovieCategories(String moviecategories){
		log("Method called: enterMovieCategories");
		movie_categories = driver.findElement(By.xpath("//option[contains(text(),'"+moviecategories +"')]"));
		movie_categories.click();
	}
	
	public void enterMovieURL(String movieurl){
		log("Method called: enterMovieURL");
		movie_url.clear();
		movie_url.sendKeys(movieurl);
	}
	
	public void enterMovieRatings(int rating){
		log("Method called: enterMovieRatings");

		WebElement element = driver.findElement(By.xpath("(//*[@fill='black'])[1]"));
		Actions act = new Actions(driver);
		act.moveToElement(element).build().perform();
	}
	
	public void clickSaveMovie() throws InterruptedException{
		log("Method called: clickSaveMovie");
		Thread.sleep(2000);
		btn_saveMovie.click();
	}
	
	public void clickDiscardMovie(){
		log("Method called: clickDiscardMovie");
		btn_discardMovie.click();
	}
}
