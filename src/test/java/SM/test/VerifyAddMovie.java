package SM.test;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

import AutomatorsRock.Test.Base;
import DTO.CommonTestDataDto;
import Miscellaneous.Configuration;
import Miscellaneous.Log4JLogger;
import Miscellaneous.TestDataHelper;
import Miscellaneous.Enums.TestRunType;
import Miscellaneous.Enums.UserRole;

public class VerifyAddMovie extends Base{

	public VerifyAddMovie() {
		testRunType = TestRunType.Regression;
		userName = Configuration.getInstance().getSiteAdminUserName();
		password = Configuration.getInstance().getSiteAdminPassword();
		userRole = UserRole.SiteAdmin;
	}
	
	@Test(description = "Add a Moview", enabled = true)
	public void AddAMoview() throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
		
	}
	
}
