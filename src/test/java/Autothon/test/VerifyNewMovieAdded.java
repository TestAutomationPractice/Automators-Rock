package Autothon.test;

import org.openqa.selenium.chrome.ChromeDriver;
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

public class VerifyNewMovieAdded extends Base {
	private static String movieTitle = "Movie" + generateNumInStringFormat(4);
	private static String movieDirector = "Director" + generateNumInStringFormat(4);
	private static String movieDescription = "movieDescription" + generateNumInStringFormat(10);
	private static String movieURL = "http://autothon.net/addMovie";
	private static String type= "Comedy";
	
	public VerifyNewMovieAdded() {
		testRunType = TestRunType.Regression;
		userName = Configuration.getInstance().getSiteAdminUserName();
		password = Configuration.getInstance().getSiteAdminPassword();
		userRole = UserRole.SiteAdmin;
	}

	@Test(description = "Adding movie and Verify New Moview Through UI in two drivers", enabled = true)
	@CustomTestAnnotations(testCaseNumber = "TC-003")
	public void newMovie() throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
		//Login and Create a new Conference Call on NRS
		
		RemoteWebDriver driver2 =  new ChromeDriver();
		driver2.get(testDataHelper.getValue("URL"));
		MoviePage moviesPage = PageFactory.initElements(driver2, MoviePage.class);
		moviesPage.setDependencies(logger, testDataHelper);
		moviesPage.clickExpand();
		moviesPage.clickLogin();
		moviesPage.enterUsername("user");
		moviesPage.enterPassword("password");
		
		navigateToURL(testDataDto, testDataHelper.getValue("URL"));
		MoviePage moviesPageAdmin = PageFactory.initElements(driver, MoviePage.class);
		moviesPageAdmin.setDependencies(logger, testDataHelper);
		moviesPageAdmin.clickExpand();
		moviesPageAdmin.clickLogin();
		moviesPageAdmin.enterUsername(userName);
		moviesPageAdmin.enterPassword(password);
		moviesPageAdmin.clickLoginButton();
		moviesPageAdmin.clickAddMovie();
		
		AddMoviePage addMoviePage = PageFactory.initElements(driver, AddMoviePage.class);
		addMoviePage.setDependencies(logger, testDataHelper);
		
		addMoviePage.enterMovieTitle(movieTitle);
		addMoviePage.enterMovieDirector(movieDirector);
		addMoviePage.enterMovieDescription(movieDescription);
		addMoviePage.enterMovieCategories(type);
		addMoviePage.enterMovieRatings(1);
		addMoviePage.enterMovieURL(movieURL);
		Thread.sleep(5000);
		addMoviePage.clickSaveMovie();
		
		moviesPage.searchMovie(movieTitle);
		Thread.sleep(2000);
   	}

	
}