package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import AutomatorsRock.Test.ComponentBase;
import Miscellaneous.Log4JLogger;
import Miscellaneous.TestDataHelper;

public class AddMoviePage extends ComponentBase{

	@FindBy(how = How.NAME, using = "title")
	WebElement movie_title;
	
	@FindBy(how = How.NAME, using = "director")
	WebElement movie_director;
	
	@FindBy(how = How.NAME, using = "description")
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
	
	public void enterMovieDescription(String moviedescription) throws InterruptedException{
		log("Method called: enterMovieDescription");
		Thread.sleep(6000);
		Actions act = new Actions(driver);
		act.moveToElement(movie_description).build().perform();
		movie_description.clear();
		movie_description.sendKeys(moviedescription);
	}
	
	public void enterMovieDirector(String moviedirector){
		log("Method called: enterMovieDirector");
		movie_director.sendKeys(moviedirector);
	}	
	
	public void enterMovieCategories(String moviecategories){
		log("Method called: enterMovieCategories");
		movie_categories = driver.findElement(By.xpath("//option[contains(text(),'"+moviecategories +"']"));
		movie_categories.click();
	}
	
	public void enterMovieURL(String movieurl){
		log("Method called: enterMovieURL");
		movie_url.sendKeys(movieurl);
	}
	
	/*public void enterMovieRatings(int rating){
		log("Method called: enterMovieRatings");

		WebElement element = driver.findElement(By.xpath("//label[contains(text(),'Rating')]/"));
	}*/
	
	public void clickSaveMovie(){
		log("Method called: clickSaveMovie");
		btn_saveMovie.click();
	}
	
	public void clickDiscardMovie(){
		log("Method called: clickDiscardMovie");
		btn_discardMovie.click();
	}
}
