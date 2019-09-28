package SM.test;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;

import org.testng.annotations.Test;
import Conferencing.Test.Base;
import Conferencing.Test.ComponentBase;
import DTO.CommonTestDataDto;
import Miscellaneous.*;
import Miscellaneous.Enums.ShowStatus;
import Miscellaneous.Enums.ShowStatusColor;
import Miscellaneous.Enums.ShowType;
import Miscellaneous.Enums.TestRunType;
import Miscellaneous.Enums.UserRole;
import Miscellaneous.Enums.MediaType;
import Pages.ShowManagement.Billing;
import Pages.ShowManagement.Disclaimer;
import Pages.ShowManagement.Documentation;
import Pages.ShowManagement.EntryCodes;
import Pages.ShowManagement.Review;
import Pages.ShowManagement.SMHome;
import Pages.ShowManagement.ShowDetails;
import Pages.ShowManagement.Media;

public class VideoOnlyShow extends Base {

	private static String automationShowName = "AutoSM" + ComponentBase.dateTime();
	private String entryCode = "EC" + generateNumInStringFormat(10);
	String count ;
	
	public VideoOnlyShow() {
		testRunType = TestRunType.Regression;
		userName = Configuration.getInstance().getSiteAdminUserName();
		password = Configuration.getInstance().getSiteAdminPassword();
		userRole = UserRole.SiteAdmin;
	}

	@Test(description = "Create an Video only Show", enabled = true)
	@CustomTestAnnotations(testCaseNumber = "TC-119")
	public void createshow() throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
		//Login and Create a new Conference Call on NRS
		
		navigateToShowManagement(testDataDto, userName, password);
		SMHome smHome = PageFactory.initElements(driver, SMHome.class);
		smHome.setDependencies(logger, testDataHelper);
		smHome.clickCreateNew();
		
		ShowDetails showdetails = PageFactory.initElements(driver, ShowDetails.class);
		showdetails.setDependencies(logger, testDataHelper);
		showdetails.enterUnderwriter(testDataHelper.getValue("Underwriter"));
		showdetails.enterunderwriterRole(testDataHelper.getValue("UnderwriterRole"));
		showdetails.clickAdd();
		showdetails.selectMediaType(ShowType.Video_Only.getShowType());
		showdetails.enterShowName(automationShowName);
		showdetails.enterShowTitle(automationShowName);
		showdetails.selectDealType(testDataHelper.getValue("DealType"));
		showdetails.selectSector(testDataHelper.getValue("Sector"));
		showdetails.selectBusinessGroup(testDataHelper.getValue("BusinessGroup"));
		showdetails.enterStartDate(ComponentBase.modDate(-3));
		showdetails.enterEndDate(ComponentBase.modDate(5));
		showdetails.selectTimezone(testDataHelper.getValue("Timezone"));
		showdetails.clickSaveAndContinue();
		
		Media media = PageFactory.initElements(driver, Media.class);
		media.setDependencies(logger, testDataHelper);
		media.clickUploadMedia(testDataDto,MediaType.Video,"p.mp4");
		media.enterSpeakerName("Speaker");
		media.enterSpeakerCompany("Nagarro");
		media.enterSpeakerTitle("Tester");
		media.enterSequence("1");
		media.clickAdd();
		media.clickContinue();
		
		Documentation documentaion = PageFactory.initElements(driver, Documentation.class);
		documentaion.setDependencies(logger, testDataHelper);
		documentaion.selectDocumentType("Addendum");
		documentaion.enterDocumentName("Document 1");
		documentaion.enterDocumentFilename("sample.pdf");
		documentaion.clickUpload();
		documentaion.waitForFileUpload();
		Thread.sleep(5000);
		documentaion.clickSaveAndContinue();
		
		Disclaimer disclaimer = PageFactory.initElements(driver, Disclaimer.class);
		disclaimer.setDependencies(logger, testDataHelper);
		disclaimer.clickSaveAndContinue();
		
		EntryCodes entryCodes = PageFactory.initElements(driver, EntryCodes.class);
		entryCodes.setDependencies(logger, testDataHelper);
		entryCodes.enterEntryCode(entryCode);
		Thread.sleep(1000);
		entryCodes.clickAdd();
		Thread.sleep(2000);
		entryCodes.clickContinue();
				
		Billing billing = PageFactory.initElements(driver, Billing.class);
		billing.setDependencies(logger, testDataHelper);
		billing.clickSaveAndContinue();
		Thread.sleep(5000);
		
		Review review = PageFactory.initElements(driver, Review.class);
		review.setDependencies(logger, testDataHelper);
		review.verifyShowStatus(ShowStatus.Disabled.getString());
		review.verifyShowColor(ShowStatusColor.Red.getString());
		review.clickMakeShowLive();
		review.clickYes();
		review.verifyShowStatus(ShowStatus.Live.getString());
		review.verifyShowColor(ShowStatusColor.Green.getString());
		Thread.sleep(5000);
	}

	
}