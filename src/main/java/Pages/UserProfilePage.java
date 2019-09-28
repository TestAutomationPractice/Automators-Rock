package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;

import AutomatorsRock.*;
import AutomatorsRock.Test.ComponentBase;
import Miscellaneous.Log4JLogger;
import Miscellaneous.TestDataHelper;

public class UserProfilePage extends ComponentBase {

	@FindBy(how = How.XPATH, using = "//div[@class='mycard']")
	WebElement movieCards;

	@FindBy(how = How.XPATH, using = "//div[@class='profile']/h2")
	WebElement yourProfileHeader;

	@FindBy(how = How.XPATH, using = "//input[@name='search']")
	WebElement searchBox;

	String txtYourProfile = "Your Profile: ";

	public UserProfilePage(WebDriver driver) {
		super(driver);
	}

	public void setDependencies(Log4JLogger logger, TestDataHelper testDataHelper) {
		super.setDependencies(logger, testDataHelper);
	}

	public void getProfileHeader() {
		log("Method called: getProfileHeader");
		yourProfileHeader.getText();

	}

	public void verifySearchBox() {
		log("Method called: verifySearchBox");
		Assert.assertTrue(searchBox.isDisplayed());
	}

}
