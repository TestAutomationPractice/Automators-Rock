package SM.test;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;

import org.testng.annotations.Test;

import AutomatorsRock.Test.Base;
import DTO.CommonTestDataDto;
import Miscellaneous.*;
import Miscellaneous.Enums.TestRunType;
import Miscellaneous.Enums.UserRole;
import Pages.AddMoviePage;
import Pages.MoviePage;

public class AddingNewMovie extends Base {
	private static String movieTitle = "Movie" + generateNumInStringFormat(4);
	private static String movieDirector = "Director" + generateNumInStringFormat(4);
	private static String movieDescription = "movieDescription" + generateNumInStringFormat(4);
	private static String movieURL = "http://autothon-nagarro-frontend-b04.azurewebsites.net/addMovie";
	private static String type= "Comedy";
	
	public AddingNewMovie() {
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
		

		navigateToURL(testDataDto, testDataHelper.getValue("URL"));
		Thread.sleep(5000);
		MoviePage moviesPage = PageFactory.initElements(driver, MoviePage.class);
		moviesPage.setDependencies(logger, testDataHelper);
		moviesPage.clickExpand();
		Thread.sleep(2000);
		moviesPage.clickLogin();
		Thread.sleep(2000);
		moviesPage.enterUsername(userName);
		Thread.sleep(2000);
		moviesPage.enterPassword(password);
		Thread.sleep(2000);
		moviesPage.clickLoginButton();
		Thread.sleep(5000);
		moviesPage.clickAddMovie();
		
		AddMoviePage addMoviePage = PageFactory.initElements(driver, AddMoviePage.class);
		addMoviePage.setDependencies(logger, testDataHelper);
		
		addMoviePage.enterMovieTitle(movieTitle);
		addMoviePage.enterMovieDirector(movieDirector);
		addMoviePage.enterMovieDescription(movieDescription);
		addMoviePage.enterMovieCategories(type);
		addMoviePage.enterMovieURL(movieURL);
		addMoviePage.clickSaveMovie();
    
		
		
	}

	
}