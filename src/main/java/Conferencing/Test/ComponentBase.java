package Conferencing.Test;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import Miscellaneous.Log4JLogger;
import Miscellaneous.Enums.*;
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

	@FindBy(how = How.XPATH, using = "//div[@class='spinner' and @style='display: none;']")
	protected WebElement element_Loader;

	@FindBy(how = How.XPATH, using = "//div[@class='loader-container ng-hide']/div[@class='loader ng-hide']")
	protected WebElement loader;
	
	@FindBy(how = How.XPATH, using = "//div[@class='loading-grid-container vertical-center ng-hide']")
	protected WebElement loaderMYNRS;
	
	@FindBy(how = How.XPATH, using = "//div[@class='loader blue']")
	protected WebElement loaderLD;

	@FindBy(how = How.XPATH, using = "//div[@class='vue-simple-spinner']")
	protected WebElement myProjects_Loader;

	@FindBy(how = How.XPATH, using = "//div[@style='display: none;']")
	protected WebElement investerFlow_Loader;
	
	@FindBy(how = How.XPATH, using = "//span[contains(text(),'Share')]/parent::button")
	protected WebElement share_btn;
	
	@FindBy(how = How.XPATH, using = "//button[contains(text(),'Back')]")
	protected WebElement back_btn;
	
	@FindBy(how = How.XPATH, using = "//div[contains(text(),'Cancel')]")
	protected WebElement cancel_btn;
	
	@FindBy(how = How.XPATH, using = "//b[contains(text(),'You have unsaved changes')]")
	protected WebElement unsavedChange_popup;
	
	@FindBy(how = How.XPATH, using = "//div[contains(text(),'Would you like to save them before navigating away from this page')]")
	protected WebElement unsavedChange1_popup;
	
	@FindBy(how = How.XPATH, using = "//h4[contains(text(),'Share')]")
	protected WebElement share_modalTitle;

	@FindBy(how = How.NAME, using = "recipientMail")
	protected WebElement share_userInput;
	
	@FindBy(how = How.XPATH, using = "//multiselect[@ng-model='show.BusinessGroup']//span[text()='Select']/parent::button")
	WebElement dropdown_BG;
	
	@FindBy(how = How.XPATH, using = "//input[@placeholder='Filter ..']")
	WebElement input_BG;
	
	@FindBy(how = How.XPATH, using = "//button[contains(text(),'Add')]")
	protected WebElement share_userAddBtn;
	
	@FindBy(how = How.XPATH, using = "//button[contains(text(),'Apply')]")
	protected WebElement share_Modalbtn;
	
	@FindBy(how = How.XPATH, using = "//span[text()='Preview']/parent::*")
	protected WebElement button_preview;
	
	@FindAll(@FindBy(how = How.ID, using = "btnDocumentList"))
	protected List<WebElement> documentList;
	
	@FindAll(@FindBy(how = How.XPATH, using = "//div[@class='page-header-dark']"))
	protected List<WebElement> pageHeaderDocumentPreview;
		
	@FindBy(how = How.XPATH, using = "//div[@id='btnDocumentList']/span")
	protected WebElement numberOfDocuments;
	
	@FindBy(how = How.XPATH, using = "(//div[@id='leftPanel']/following-sibling::div//span)[1]/following-sibling::span")
	protected WebElement showStatus;
	
	@FindBy(how = How.PARTIAL_LINK_TEXT, using = "Home")
	protected WebElement homeLink;
	
	static String documentCount; 
		 	
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

	public void waitForLoading() {
		WebDriverWait wait = new WebDriverWait(driver, WaitInterval.TwoMinute.getSeconds());
		wait.until(ExpectedConditions.attributeToBe(element_Loader, "style", "display: none;"));
	}

	public void waitForLoadingSM() {
		WebDriverWait wait = new WebDriverWait(driver, WaitInterval.FiveMinutes.getSeconds());
		wait.until(ExpectedConditions.invisibilityOf(loader));
	}
	
	public void waitForLoadingMYNRS() {
		WebDriverWait wait = new WebDriverWait(driver, WaitInterval.FiveMinutes.getSeconds());
		wait.until(ExpectedConditions.invisibilityOf(loaderMYNRS));
	}

	public void waitForLoadingLD() {
		WebDriverWait wait = new WebDriverWait(driver, WaitInterval.FiveMinutes.getSeconds());
		wait.until(ExpectedConditions.invisibilityOf(loaderLD));
	}

	public void waitForLoadingInvesterFlow() {
		WebDriverWait wait = new WebDriverWait(driver, WaitInterval.TwoMinute.getSeconds());
		wait.until(ExpectedConditions.attributeToBe(investerFlow_Loader, "style", "display: none;"));
	}
	
	protected String GetValue(WebElement webElement) {
		return webElement.getAttribute("value");
	}

	// generate dateTime
	public static String dateTime() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/YY_HH:mm:ss"); 
		Date date = new Date();
		String d = dateFormat.format(date);
		return (d);
	}
	
	public static String Newdatetime() {
		DateFormat dateFormat = new SimpleDateFormat("ddMMYY_hhmmss"); 
		Date date = new Date();
		String d = dateFormat.format(date);
		return (d);
	}
	
	// generate only date
	public static String date() {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy"); 
		dateFormat.setTimeZone(TimeZone.getTimeZone(TIMEZONE_IST));
		Date date = new Date();
		String d = dateFormat.format(date);
		return (d);
	}
	
	public static String modDate(int days) {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		dateFormat.setTimeZone(TimeZone.getTimeZone(TIMEZONE_IST));
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, days);
		return (dateFormat.format(cal.getTime()));
	}
	
	public static String modTime(int mins) {
		DateFormat dateFormat = new SimpleDateFormat("hh:mm a");				
		dateFormat.setTimeZone(TimeZone.getTimeZone(TIMEZONE_IST));
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, mins);
		return dateFormat.format(cal.getTime());
	}
	
	public static String modDate(int days, Calendar cal, DateFormat dateFormat, String timeZone ) {				
		dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
		cal.add(Calendar.DATE, days);
		return (dateFormat.format(cal.getTime()));
	}
	
	public static String modTime(int mins, Calendar cal, DateFormat dateFormat, String timeZone) {				
		dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
		cal.add(Calendar.MINUTE, mins);
		System.out.println(cal.getTime());
		return dateFormat.format(cal.getTime());
	}
	
	public boolean checkWhetherMailExistsInMailTrap(String subject, String RegisteredUserEmail) throws Exception {
		boolean b = false;
		int loop = 0;
		do {
			String arr[] = mailTrapGetResponseArray();
			for (int i = 0; i < arr.length; i++) {
				if (arr[i].contains(subject) && arr[i].contains(RegisteredUserEmail)) {
					System.out.println("found");
					System.out.println(arr[i]);
					b = true;
				}
			} 
			loop++;
			Thread.sleep(5000);
		} while (!b && loop < 10);
		return b;
	}
	
	public static String verifyMyEmailLinkFromMailTrap(String subject, String RegisteredUserEmail) throws Exception {
		String urlFinal = null;
		int loop = 0;
		do {
			String responseArr[] = mailTrapGetResponseArray();
			String USER_AGENT = "Mozilla/5.0";
			for (int i = 0; i < responseArr.length; i++) {
				if (responseArr[i].contains(subject) && responseArr[i].contains(RegisteredUserEmail)) {
					String arr[] = responseArr[i].split("/api/v1/inboxes/37330/messages/");
					String url = "https://mailtrap.io/api/v1/inboxes/37330/messages/" + arr[1].substring(0, 10)
							+ "/body.html?api_token=e262f6bb4b6d11c791c79a15f8d341cf";
					URL obj = new URL(url);
					HttpURLConnection con = (HttpURLConnection) obj.openConnection();
					// optional default is GET
					con.setRequestMethod("GET");
					//add request header
					con.setRequestProperty("User-Agent", USER_AGENT);
					int responseCode = con.getResponseCode();
					System.out.println("\nSending 'GET' request to URL : " + url);
					System.out.println("Response Code : " + responseCode);
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer response = new StringBuffer();
					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}
					in.close();
					String str2 = response.toString();
					String arr7[] = str2.split("href=");
					String mailurl[] = arr7[1].split(">");
					urlFinal = mailurl[0].substring(1, (mailurl[0].length() - 1));
					System.out.println(urlFinal);
				}
			} loop++;
			Thread.sleep(5000);
		} while (urlFinal == null && loop < 10);
		
		if(urlFinal == null) {
			Assert.assertNull(urlFinal, "The mail was not received in Mailtrap");
		}
		return urlFinal;
	}
	
	public static String[] mailTrapGetResponseArray() throws IOException{
		String url = "https://mailtrap.io/api/v1/inboxes/37330/messages?api_token=e262f6bb4b6d11c791c79a15f8d341cf";
		String USER_AGENT = "Mozilla/5.0";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		// optional default is GET
		con.setRequestMethod("GET");
		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
		int responseCode = con.getResponseCode();
		System.out.println("Response Code : " + responseCode);
		BufferedReader in = new BufferedReader(
		new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		String str = response.toString(); 
		String responseArr[]= str.split(":37330");
		return responseArr;
	}
	
	public void clickShare()throws InterruptedException{
		log("Method Called: clickShare");
		waitForLoadingSM();
		Thread.sleep(2000);
		share_btn.click();
		Thread.sleep(2000);
		waitForLoadingSM();
	}
	
	
	public void enterSharedUserEmailAddress(String sharedUser) throws InterruptedException {
		log("Method Called: enterSharedUserEmailAddress");
		share_userInput.sendKeys(sharedUser);
		Thread.sleep(2000);
	}
	
	public void selectBusinessGroup(String businessGroup){
		log("Method called: selectBusinessGroup");
		dropdown_BG.click();
		input_BG.sendKeys(businessGroup);
		driver.findElement(By.xpath("//span[contains(text(),'"+businessGroup+"')]")).click();
		share_modalTitle.click();
	}
	
	public void clickAddButtonShareModal(){
		log("Method Called: clickAddButtonShareModal");
		new WebDriverWait(driver, WaitInterval.OneMinute.getSeconds()).until(ExpectedConditions.elementToBeClickable(share_userAddBtn));
		share_userAddBtn.click();
	}
	
	public void clickShareButtonModal() throws InterruptedException{
		log("Method Called: clickShareButtonModal");
		new WebDriverWait(driver, WaitInterval.OneMinute.getSeconds()).until(ExpectedConditions.elementToBeClickable(share_Modalbtn));
		share_Modalbtn.click();
		waitForLoadingSM();
	}
		
	public String verifySharedShowEmailWithRegisteredUser(String automationShowName,String sharedUserEmail,String expectedLink) throws Exception{
		log("Method Called: verifySharedShowEmailWithRegisteredUser");
		Thread.sleep(5000);
		String subject = "Access Granted: "+automationShowName;
		String link = verifyMyEmailLinkFromMailTrap(subject, sharedUserEmail.toLowerCase());
		Assert.assertEquals(link, expectedLink);
		log("Method Ended: shareShow");
		return link;
		
	}
	
	public String verifySharedShowEmailWithUnRegisteredUser(String automationShowName,String sharedUserEmail,String expectedLink) throws Exception{
		log("Method Called: verifySharedShowEmailWithUnRegisteredUser");
		Thread.sleep(5000);
		String subject = "Access Granted: "+automationShowName;
		String link = verifyMyEmailLinkFromMailTrap(subject, sharedUserEmail.toLowerCase());
		Assert.assertEquals(link, expectedLink);
		log("Method Ended: shareShow");
		return link;
		
	}
	
	public static String getLiveDashboardLinkMailtrap(String automationShowName, String registeredUser) throws Exception{
		//log("Method called: clickLaunchDashboardMailtrap");
		String url=verifyMyEmailLinkFromMailTrap(automationShowName,registeredUser);
		String arr[] = url.split(" ");
		String urlLD =  arr[0].substring(0, arr[0].length()-1);
		System.out.println("URL: " + urlLD);
		return urlLD;
	}
	
	public void verifyFilePresent(String filePath){
		 File f = new File(filePath);

		  if(f.exists()){
			  System.out.println("File existed");
		  }else{
			  System.out.println("File not found!");
			  Assert.fail("File not present : " + filePath);
		  }
		
	} 
	
	public void verifyPreviewButton(){
		log("Method Called: verifyPreviewButton");
		new WebDriverWait(driver, WaitInterval.OneMinute.getSeconds()).until(ExpectedConditions.elementToBeClickable(button_preview));
		button_preview.isDisplayed();
	}
	
	public void clickAndVerifyDocumentPreview() throws InterruptedException{
		log("Method Called: clickAndVerifyDocumentPreview");
		waitForLoadingSM();
		new WebDriverWait(driver, WaitInterval.OneMinute.getSeconds()).until(ExpectedConditions.elementToBeClickable(button_preview));
		button_preview.click();
		System.out.println("Preview opened.............");
		String winHandleBefore = driver.getWindowHandle();
		boolean value = true;
		do{
			for(String winHandle : driver.getWindowHandles()){
			    driver.switchTo().window(winHandle);
			    if(pageHeaderDocumentPreview.size()!=0){
			    	value = false;
			    	Thread.sleep(5000);
			    	break;
			    }
			}
		}while(value);
		driver.close();
		// Switch back to original browser (first window)
		driver.switchTo().window(winHandleBefore);
		waitForLoadingSM();
	}
	
	public String clickPreviewButtonAndGetDocumentCount() throws InterruptedException{
		log("Method Called: clickPreviewButton");
		new WebDriverWait(driver, WaitInterval.OneMinute.getSeconds()).until(ExpectedConditions.elementToBeClickable(button_preview));
		button_preview.click();
		System.out.println("Preview opened.............");
		String winHandleBefore = driver.getWindowHandle();
		boolean value = true;
		do{
			for(String winHandle : driver.getWindowHandles()){
			    driver.switchTo().window(winHandle);
			    if(documentList.size()!=0){
			    	value = false;
			    	Thread.sleep(5000);
			    	break;
			    }
			}
		}while(value);
		documentCount = numberOfDocuments.getText();
		System.out.println("Number of Documents............."+ documentCount);
		driver.close();
		// Switch back to original browser (first window)
		driver.switchTo().window(winHandleBefore);
		waitForLoadingSM();
		return documentCount;
	}
	
	public void verifyDocumentCount(String count){
		log("Method Called: verifyDocumentCount");
		Assert.assertTrue(documentCount.equals(count));
	}
	
	public void verifyShowStatus(String expectedShowStatus){
		log("Method Called: verifyShowStatus");
		Assert.assertEquals(showStatus.getText(), expectedShowStatus);
		log("Method Ended: verifyShowStatus");
	}
	
	public void verifyShowColor(String expectedShowStatusColor){
		log("Method Called: expectedShowStatusColor");
		Assert.assertEquals(showStatus.getCssValue("background-color"), expectedShowStatusColor);
		log("Method Ended: expectedShowStatusColor");
	}
	
	public void goToSMHomePage(){
		log("Method Called: goToSMHomePage");
		homeLink.click();
		waitForLoadingSM();
		log("Method Ended: goToSMHomePage");
	}
	
	public void clickBackButton(){
		log("Method Called: clickBackButton");
		back_btn.click();
		waitForLoadingSM();
		log("Method Ended: clickBackButton");
	}
	
	public void verifyMessage(String message) throws InterruptedException{
		log("Method Called: fetchMessage");
		Thread.sleep(2000);
		String page = driver.getPageSource();
		Assert.assertTrue(page.contains(message));
	}
	
	public void verifyUnsavedPopup(){
		log("Method Called: verifyUnsavedPopup");
		Assert.assertTrue(unsavedChange_popup.isDisplayed());
		Assert.assertTrue(unsavedChange1_popup.isDisplayed());
		log("Method Ended: verifyUnsavedPopup");
	}
	
	public void clickCancelButton() throws InterruptedException{
		log("Method Called: clickCancelButton");
		cancel_btn.click();
		Thread.sleep(2000);
		log("Method Ended: clickCancelButton");
	}
	
	
}