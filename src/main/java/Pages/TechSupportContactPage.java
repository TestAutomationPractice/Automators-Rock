package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;

import AutomatorsRock.Test.ComponentBase;
import Miscellaneous.TestDataHelper;
import Miscellaneous.*;

public class TechSupportContactPage extends ComponentBase {

	@FindBy(how = How.ID, using = "name")
	WebElement name;
	
	@FindBy(how = How.ID, using = "email")
	WebElement email;
	
	@FindBy(how = How.ID, using = "phoneNumber")
	WebElement phoneNumber;
	
	
	public TechSupportContactPage(WebDriver driver) {
		super(driver);
	}
	
	public void setDependencies(Log4JLogger logger, TestDataHelper testDataHelper) {
		super.setDependencies(logger, testDataHelper);
	}

	public void verifyElement(){
		log("Method called: verifyElement");
		Assert.assertTrue(name.isDisplayed());
		Assert.assertTrue(email.isDisplayed());
		Assert.assertTrue(phoneNumber.isDisplayed());
	}

}
