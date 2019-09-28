package AutomatorsRock.Test;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import Miscellaneous.Log4JLogger;
import Miscellaneous.Enums.*;
import junit.framework.Assert;
import Miscellaneous.*;

/**
 * Provides common functionality across all pages/components across application.
 *
 */
public abstract class ComponentBase {
	private Log4JLogger _logger;
	private Miscellaneous.TestDataHelper _testDataHelper;
	
	public static final String TIMEZONE_IST= "IST";

	protected final WebDriver driver;
	public static String projectName;

	@FindBy(how = How.LINK_TEXT, using = "Profile")
	protected WebElement link_profile;

	@FindBy(how = How.LINK_TEXT, using = "Movies")
	protected WebElement link_movies;

	@FindBy(how = How.LINK_TEXT, using = "Login")
	protected WebElement link_login;
	
	@FindBy(how = How.LINK_TEXT, using = "Logout")
	protected WebElement link_logout;
	
	@FindAll(@FindBy(how = How.LINK_TEXT, using = "Logout"))
	List<WebElement> link_logoutList;
	
	@FindBy(how = How.LINK_TEXT, using = "add movie")
	protected WebElement link_addMovie;
	
	@FindAll(@FindBy(how = How.LINK_TEXT, using = "add movie"))
	List<WebElement> link_addMovieList;
	
	@FindBy(how = How.LINK_TEXT, using = "Cancel")
	protected WebElement link_cancel;
	
	@FindBy(how = How.XPATH, using = "//input[@name='password']")
	protected WebElement input_password;
	
	@FindBy(how = How.XPATH, using = "//input[@name='username']")
	protected WebElement input_username;
	
	@FindBy(how = How.XPATH, using = "//button[text()='#']")
	protected WebElement button_collapse;
	
	@FindBy(how = How.XPATH, using = "//button[@class='btn btn-success']")
	protected WebElement button_login;
	
	protected ComponentBase(WebDriver driver) {
		this.driver = driver;
	}

	protected void setDependencies(Log4JLogger logger, TestDataHelper testDataHelper) {
		_logger = logger;
		_testDataHelper = testDataHelper;
	}

	protected Log4JLogger getLogger() {
		return _logger;
	}

	protected Miscellaneous.TestDataHelper getTestDataHelper() {
		return _testDataHelper;
	}

	protected void log(String message) {
		StackTraceElement[] currentStacktrace = Thread.currentThread().getStackTrace();
		System.out.println("Method Executed: " + currentStacktrace[2].getMethodName());
		getLogger().log(message);
	}

	
	protected String GetValue(WebElement webElement) {
		return webElement.getAttribute("value");
	}

	public void clickProfile(){
		log("Method Called: clickProfile");
		new WebDriverWait(driver, WaitInterval.OneMinute.getSeconds()).until(ExpectedConditions.elementToBeClickable(link_profile));
		link_profile.click();
	}
	
	public void clickMovies(){
		log("Method Called: clickMovies");
		new WebDriverWait(driver, WaitInterval.OneMinute.getSeconds()).until(ExpectedConditions.elementToBeClickable(link_movies));
		link_movies.click();
	}
	
	public void clickLogin(){
		log("Method Called: clickLogin");
		new WebDriverWait(driver, WaitInterval.OneMinute.getSeconds()).until(ExpectedConditions.elementToBeClickable(link_login));
		link_login.click();
	}
	
	public void clickCancel(){
		log("Method Called: clickCancel");
		new WebDriverWait(driver, WaitInterval.OneMinute.getSeconds()).until(ExpectedConditions.elementToBeClickable(link_cancel));
		link_cancel.click();
	}
	
	public void enterUsername(String name){
		log("Method Called: enterUsername");
		new WebDriverWait(driver, WaitInterval.OneMinute.getSeconds()).until(ExpectedConditions.visibilityOf(input_username));
		input_username.clear();
		input_username.sendKeys(name);
	}
	
	public void enterPassword(String password){
		log("Method Called: enterPassword");
		new WebDriverWait(driver, WaitInterval.OneMinute.getSeconds()).until(ExpectedConditions.visibilityOf(input_password));
		input_password.clear();
		input_password.sendKeys(password);
	}
	
	public void clickLoginButton(){
		log("Method Called: clickLoginButton");
		new WebDriverWait(driver, WaitInterval.OneMinute.getSeconds()).until(ExpectedConditions.elementToBeClickable(button_login));
		button_login.click();
	}
	
	public void clickExpand(){
		log("Method Called: clickExpand");
		new WebDriverWait(driver, WaitInterval.OneMinute.getSeconds()).until(ExpectedConditions.elementToBeClickable(button_collapse));
		button_collapse.click();
	}
	
	public void isAddMoviePresent(boolean expected){
		log("Method Called: isAddMoviePresent");
	 boolean actual = false; 
		if(link_addMovieList.size()>0){
			actual = true; 
		}
		Assert.assertEquals(actual, expected);	
	}
	
	public void isLogoutPresent(boolean expected){
		log("Method Called: isLogoutPresent");
	 boolean actual = false; 
		if(link_logoutList.size()>0){
			actual = true; 
		}
		Assert.assertEquals(actual, expected);	
	}
	
	public void clickAddMovie(){
		log("Method Called: clickAddMovie");
		new WebDriverWait(driver, WaitInterval.OneMinute.getSeconds()).until(ExpectedConditions.elementToBeClickable(link_addMovie));
		link_addMovie.click();
	}
}