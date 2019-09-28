package Autothon.test;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.DataProvider;
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
	private static String movieDescription = "movieDescription" + generateNumInStringFormat(10);
	private static String movieURL = "http://autothon.net/addMovie";
	private static String type= "Comedy";
	
	public AddingNewMovie() {
		testRunType = TestRunType.Regression;
		userName = Configuration.getInstance().getSiteAdminUserName();
		password = Configuration.getInstance().getSiteAdminPassword();
		userRole = UserRole.SiteAdmin;
	}

	@Test(description = "Adding New Moview Through UI", enabled = true)
	@CustomTestAnnotations(testCaseNumber = "TC-001")
	public void loginAndAddMovie() throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
		//Login and Create a new Conference Call on NRS
		

		navigateToURL(testDataDto, testDataHelper.getValue("URL"));
		MoviePage moviesPage = PageFactory.initElements(driver, MoviePage.class);
		moviesPage.setDependencies(logger, testDataHelper);
		moviesPage.clickExpand();
		moviesPage.clickLogin();
		moviesPage.enterUsername(userName);
		moviesPage.enterPassword(password);
		moviesPage.clickLoginButton();
		moviesPage.clickAddMovie();
		
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
   	}
	
	@Test(description = "VeriFy Login", enabled = true, dataProvider="user details")
	@CustomTestAnnotations(testCaseNumber = "TC-003")
	public void VerifyLogin(String user, String pass, String expectation) throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
		//Login and Create a new Conference Call on NRS
		
		navigateToURL(testDataDto, testDataHelper.getValue("URL"));
		MoviePage moviesPage = PageFactory.initElements(driver, MoviePage.class);
		moviesPage.setDependencies(logger, testDataHelper);
		moviesPage.clickExpand();
		moviesPage.clickLogin();
		moviesPage.enterUsername(user);
		moviesPage.enterPassword(pass);
		moviesPage.clickLoginButton();
		Thread.sleep(5000);
		
		if(expectation.equals("Admin")) {
			moviesPage.isAddMoviePresent(true);
			moviesPage.isLogoutPresent(true);
			
		}
		if(expectation.equals("Regular")) {
			moviesPage.isAddMoviePresent(false);
			moviesPage.isLogoutPresent(true);
			
		}
		if(expectation.equals("wrong user")) {
			moviesPage.isAddMoviePresent(false);
			moviesPage.isLogoutPresent(false);
			
		}
		
   	}

	@DataProvider(name="user details")
    public Object[][] getDataFromDataprovider(){
    return new Object[][] 
    	{
            { "admin", "password" , "Admin" },
            { "user", "password" , "Regular" },
            { "test", "test" , "wrong user"}
        };

    }
	
}