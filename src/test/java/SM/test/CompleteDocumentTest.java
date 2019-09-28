package SM.test;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

import Conferencing.Test.Base;
import Conferencing.Test.ComponentBase;
import DTO.CommonTestDataDto;
import Miscellaneous.Configuration;
import Miscellaneous.CustomTestAnnotations;
import Miscellaneous.Log4JLogger;
import Miscellaneous.TestDataHelper;
import Miscellaneous.Enums.ShowStatus;
import Miscellaneous.Enums.ShowStatusColor;
import Miscellaneous.Enums.ShowType;
import Miscellaneous.Enums.TestRunType;
import Miscellaneous.Enums.UserRole;

import Pages.ShowManagement.Documentation;

import Pages.ShowManagement.SMHome;
import Pages.ShowManagement.ShowDetails;

public class CompleteDocumentTest extends Base{
	private static String automationShowName = "AutomationShow" + ComponentBase.dateTime().replaceAll("/", "").replaceAll(":", "");

	public CompleteDocumentTest() {
		testRunType = TestRunType.Regression;
		userName = Configuration.getInstance().getSiteAdminUserName();
		password = Configuration.getInstance().getSiteAdminPassword();
		userRole = UserRole.SiteAdmin;
	}

	@Test(description = "Create an Document only Show", enabled = true)
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
		showdetails.selectMediaType(ShowType.Document_Only.getShowType());
		showdetails.enterShowName(automationShowName);
		showdetails.enterShowTitle(automationShowName);
		showdetails.selectBusinessGroup(testDataHelper.getValue("BusinessGroup"));
		showdetails.enterStartDate(ComponentBase.modDate(-3));
		showdetails.enterEndDate(ComponentBase.modDate(5));
		showdetails.selectTimezone(testDataHelper.getValue("Timezone"));
		showdetails.clickSaveAndContinue();

		Documentation documentaion = PageFactory.initElements(driver, Documentation.class);
		documentaion.setDependencies(logger, testDataHelper);

		// Uploading PDF files
		documentaion.selectDocumentType("Addendum");
		documentaion.enterDocumentName("Addendum PDF");
		documentaion.enterDocumentFilename("Addendum PDF.pdf");
		documentaion.clickUpload();
		documentaion.waitForFileUpload();

		documentaion.selectDocumentType("Preliminary Prospectus");
		documentaion.enterDocumentName("Preliminary Prospectus PDF");
		documentaion.enterDocumentFilename("Preliminary Prospectus PDF.pdf");
		documentaion.clickUpload();
		documentaion.waitForFileUpload();

		documentaion.selectDocumentType("Final Prospectus");
		documentaion.enterDocumentName("Final Prospectus PDF");
		documentaion.enterDocumentFilename("Final Prospectus PDF.pdf");
		documentaion.clickUpload();
		documentaion.waitForFileUpload();

		documentaion.selectDocumentType("Other");
		documentaion.enterDocumentName("Other PDF");
		documentaion.enterDocumentFilename("Other PDF.pdf");
		documentaion.clickUpload();
		documentaion.waitForFileUpload();


		// uploading Doc file
		documentaion.selectDocumentType("Addendum");
		documentaion.enterDocumentName("Addendum DOC");
		documentaion.enterDocumentFilename("Addendum DOC.doc");
		documentaion.clickUpload();
		documentaion.waitForFileUpload();

		documentaion.selectDocumentType("Preliminary Prospectus");
		documentaion.enterDocumentName("Preliminary Prospectus DOC");
		documentaion.enterDocumentFilename("Preliminary Prospectus DOC.doc");
		documentaion.clickUpload();
		documentaion.waitForFileUpload();

		documentaion.selectDocumentType("Final Prospectus");
		documentaion.enterDocumentName("Final Prospectus DOC");
		documentaion.enterDocumentFilename("Final Prospectus DOC.doc");
		documentaion.clickUpload();
		documentaion.waitForFileUpload();

		documentaion.selectDocumentType("Other");
		documentaion.enterDocumentName("Other DOC");
		documentaion.enterDocumentFilename("Other DOC.doc");
		documentaion.clickUpload();
		documentaion.waitForFileUpload();

		// uploading Docx file
		documentaion.selectDocumentType("Addendum");
		documentaion.enterDocumentName("Addendum DOCX");
		documentaion.enterDocumentFilename("Addendum DOCX.docx");
		documentaion.clickUpload();
		documentaion.waitForFileUpload();

		documentaion.selectDocumentType("Preliminary Prospectus");
		documentaion.enterDocumentName("Preliminary Prospectus DOCX");
		documentaion.enterDocumentFilename("Preliminary Prospectus DOCX.docx");
		documentaion.clickUpload();
		documentaion.waitForFileUpload();

		documentaion.selectDocumentType("Final Prospectus");
		documentaion.enterDocumentName("Final Prospectus DOCX");
		documentaion.enterDocumentFilename("Final Prospectus DOCX.docx");
		documentaion.clickUpload();
		documentaion.waitForFileUpload();

		documentaion.selectDocumentType("Other");
		documentaion.enterDocumentName("Other DOCX");
		documentaion.enterDocumentFilename("Other DOCX.docx");
		documentaion.clickUpload();
		documentaion.waitForFileUpload();

		// uploading ppt file
		documentaion.selectDocumentType("Addendum");
		documentaion.enterDocumentName("Addendum PPT");
		documentaion.enterDocumentFilename("Addendum PPT.ppt");
		documentaion.clickUpload();
		documentaion.waitForFileUpload();

		documentaion.selectDocumentType("Preliminary Prospectus");
		documentaion.enterDocumentName("Preliminary Prospectus PPT");
		documentaion.enterDocumentFilename("Preliminary Prospectus PPT.ppt");
		documentaion.clickUpload();
		documentaion.waitForFileUpload();

		documentaion.selectDocumentType("Final Prospectus");
		documentaion.enterDocumentName("Final Prospectus PPT");
		documentaion.enterDocumentFilename("Final Prospectus PPT.ppt");
		documentaion.clickUpload();
		documentaion.waitForFileUpload();

		documentaion.selectDocumentType("Other");
		documentaion.enterDocumentName("Other PPT");
		documentaion.enterDocumentFilename("Other PPT.ppt");
		documentaion.clickUpload();
		documentaion.waitForFileUpload();


		// uploading pptx file
		documentaion.selectDocumentType("Addendum");
		documentaion.enterDocumentName("Addendum PPTX");
		documentaion.enterDocumentFilename("Addendum PPTX.pptx");
		documentaion.clickUpload();
		documentaion.waitForFileUpload();

		documentaion.selectDocumentType("Preliminary Prospectus");
		documentaion.enterDocumentName("Preliminary Prospectus PPTX");
		documentaion.enterDocumentFilename("Preliminary Prospectus PPTX.pptx");
		documentaion.clickUpload();
		documentaion.waitForFileUpload();

		documentaion.selectDocumentType("Final Prospectus");
		documentaion.enterDocumentName("Final Prospectus PPTX");
		documentaion.enterDocumentFilename("Final Prospectus PPTX.pptx");
		documentaion.clickUpload();
		documentaion.waitForFileUpload();

		documentaion.selectDocumentType("Other");
		documentaion.enterDocumentName("Other PPTX");
		documentaion.enterDocumentFilename("Other PPTX.pptx");
		documentaion.clickUpload();
		documentaion.waitForFileUpload();

		// uploading xls file

		documentaion.selectDocumentType("Addendum");
		documentaion.enterDocumentName("Addendum XLS");
		documentaion.enterDocumentFilename("Addendum XLS.xls");
		documentaion.clickUpload();
		documentaion.waitForFileUpload();

		documentaion.selectDocumentType("Preliminary Prospectus");
		documentaion.enterDocumentName("Preliminary Prospectus XLS");
		documentaion.enterDocumentFilename("Preliminary Prospectus XLS.xls");
		documentaion.clickUpload();
		documentaion.waitForFileUpload();

		documentaion.selectDocumentType("Final Prospectus");
		documentaion.enterDocumentName("Final Prospectus XLS");
		documentaion.enterDocumentFilename("Final Prospectus XLS.xls");
		documentaion.clickUpload();
		documentaion.waitForFileUpload();

		documentaion.selectDocumentType("Other");
		documentaion.enterDocumentName("Other XLS");
		documentaion.enterDocumentFilename("Other XLS.xls");
		documentaion.clickUpload();
		documentaion.waitForFileUpload();

		// uploading xlsx file

		documentaion.selectDocumentType("Addendum");
		documentaion.enterDocumentName("Addendum XLSX");
		documentaion.enterDocumentFilename("Addendum XLSX.xlsx");
		documentaion.clickUpload();
		documentaion.waitForFileUpload();

		documentaion.selectDocumentType("Preliminary Prospectus");
		documentaion.enterDocumentName("Preliminary Prospectus XLSX");
		documentaion.enterDocumentFilename("Preliminary Prospectus XLSX.xlsx");
		documentaion.clickUpload();
		documentaion.waitForFileUpload();

		documentaion.selectDocumentType("Final Prospectus");
		documentaion.enterDocumentName("Final Prospectus XLSX");
		documentaion.enterDocumentFilename("Final Prospectus XLSX.xlsx");
		documentaion.clickUpload();
		documentaion.waitForFileUpload();

		documentaion.selectDocumentType("Other");
		documentaion.enterDocumentName("Other XLSX");
		documentaion.enterDocumentFilename("Other XLSX.xlsx");
		documentaion.clickUpload();
		documentaion.waitForFileUpload();
		Thread.sleep(5000);
		documentaion.clickSaveAndContinue();
		documentaion.clickBackButton();
		
		documentaion.VerifyNumberofDocuments("Final Prospectus",7);
		documentaion.VerifyNumberofDocuments("Other",7);
		documentaion.VerifyNumberofDocuments("Preliminary Prospectus",7);
		documentaion.VerifyNumberofDocuments("Addendum",7);


	}
}
