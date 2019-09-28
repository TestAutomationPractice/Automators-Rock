package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import Conferencing.Test.ComponentBase;
import Miscellaneous.Log4JLogger;
import Miscellaneous.TestDataHelper;

public class UserMoviesPage extends ComponentBase {

	@FindBy(how = How.XPATH, using = "//div[@class='mycard']")
	WebElement movieCards;

	@FindBy(how = How.XPATH, using = "//div[@class='profile']/h2")
	WebElement yourProfileHeader;

	@FindBy(how = How.XPATH, using = "//input[@name='search']")
	WebElement searchBox;

	// driver.get("http://autothon-nagarro-frontend-x04.azurewebsites.net");

	String txtYourProfile = "Your Profile: ";
	int countOfMovieCards;

	public UserMoviesPage(WebDriver driver) {
		super(driver);
	}

	public void setDependencies(Log4JLogger logger, TestDataHelper testDataHelper) {
		super.setDependencies(logger, testDataHelper);
	}

	public void getProfileHeader()
	{
		log("Method called: getProfileHeader");
		yourProfileHeader.getText();
		
	}
	// Verifies if the user has successfully logged in to the application by
	// verifying the presence of Search Box on the landing screen
	public boolean verifyUserHasLoggedInSuccessfully() {
		log("Method called: verifyUserHasLoggedInSuccessfully");
		if (driver.findElement(By.xpath(xpathSearchBox)).isDisplayed())
			return true;
		else
			return false;
	}

	// Verifies if the user has successfully navigated to the Profile Page
	public boolean verifyUserIsNavigatedtoProfilePage() {
		log("Method called: verifyUserIsNavigatedtoProfilePage");
		if (driver.getCurrentUrl().equals("http://autothon-nagarro-frontend-x04.azurewebsites.net/profile"))
			if (driver.findElement(By.tagName("h2")).getText().equals(txtYourProfile))
				return true;
			else
				return false;
		return false;
	}

	// Verifies the UI of User Profile Page
	public boolean verifyUIofUserProfilePage() {

		// Verify the title of User Profile Page

		return false;

	}

}
