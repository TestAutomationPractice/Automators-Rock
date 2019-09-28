package Miscellaneous;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

public class WebDriverHelper {
	private final Configuration _configuration;

	/**
	 * 
	 */
	public WebDriverHelper() {
		_configuration = Configuration.getInstance();
	}

	public WebDriver getWebDriver(String platform, String browser, String version, String screenResolution,
			String testCaseNumber, Log4JLogger logger) throws Exception {
		logger.log(platform);
		WebDriver driver = null;
		String testCaseName = testCaseNumber + "_" + platform + "_" + browser + "_" + version + "_" + screenResolution;
		String downloadFilepath = _configuration.get_rootPath() + "\\DOWNLOADS\\";
		if (platform.equals("Local PC")) {
			driver = createLocalDriver(browser, driver, downloadFilepath);
		} else if (platform.equals("Local Android")) {
			driver = createAndroidDriver();
		} else if (_configuration.get_needsGridInfrastructure().equals("1")) {
			String URL = _configuration.getSeleniumGridEndPoint();
			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			logger.log("Url: " + URL);

			driver = new RemoteWebDriver(new URL(URL), capabilities);
			((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
		} else {
			driver = createSauceLabDriver(platform, browser, version, screenResolution, logger, testCaseName);
		}
		if(!browser.equalsIgnoreCase("chrome")){
			driver.manage().window().maximize();
		}
		logger.log("PASS: launch run successfully." + testCaseName);
		return driver;
	}

	public void closeWebDriver(WebDriver driver) {
		if (driver != null) {
			driver.close();
			driver.quit();
		}
	}

	/**
	 * @return
	 * @throws MalformedURLException
	 */
	private WebDriver createAndroidDriver() throws MalformedURLException {
		WebDriver driver;
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName", "Auto6");
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("browserName", "Chrome");

		// Set the auto accept alert.
		capabilities.setCapability("autoAcceptAlerts", true);
		driver = new RemoteWebDriver(new URL("http://0.0.0.0:4723/wd/hub"), capabilities);
		return driver;
	}

	/**
	 * @param platform
	 * @param browser
	 * @param version
	 * @param screenResolution
	 * @param logger
	 * @param testCaseName
	 * @return
	 * @throws MalformedURLException
	 */
	private WebDriver createSauceLabDriver(String platform, String browser, String version, String screenResolution,
			Log4JLogger logger, String testCaseName) throws MalformedURLException {
		WebDriver driver;
		String USERNAME = _configuration.getSauceLabUserName();
		String ACCESS_KEY = _configuration.getSauceLabAccessKey();
		String URL = "http://" + USERNAME + ":" + ACCESS_KEY + "@ondemand.saucelabs.com:80/wd/hub/";
		// String URL = "http://build.dev.netroadshow.com:4444/wd/hub";
		DesiredCapabilities capabilities = null;
		if (platform.equals("Android")) {
			capabilities = DesiredCapabilities.android();
			capabilities.setCapability("appiumVersion", "1.6.4");
			capabilities.setCapability("deviceName", screenResolution);
			capabilities.setCapability("deviceOrientation", "portrait");
			capabilities.setCapability("platformVersion", version);
			capabilities.setCapability("platformName", platform);
			capabilities.setCapability("browserName", browser);
		} else {

			switch (browser) {
			// all chrome
			case "Chrome":
				capabilities = DesiredCapabilities.chrome();
				break;
			// all IE
			case "IE":
				capabilities = DesiredCapabilities.internetExplorer();
				break;
			// all Edge
			case "Edge":
				capabilities = DesiredCapabilities.firefox();
				break;
			// all ff
			case "FireFox":
				capabilities = DesiredCapabilities.firefox();
				break;
			case "Safari":
				capabilities = DesiredCapabilities.safari();
				break;
			}
			capabilities.setCapability(CapabilityType.PLATFORM, platform);
			capabilities.setCapability(CapabilityType.VERSION, version);
			capabilities.setCapability("screenResolution", screenResolution);
		}

		capabilities.setCapability("name", testCaseName);
		logger.log("build identifier: " + _configuration.getBuildIdentifier());
		capabilities.setCapability("build", _configuration.getBuildIdentifier());
		logger.log("Url: " + URL);

		driver = new RemoteWebDriver(new URL(URL), capabilities);
		((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
		return driver;
	}

	/**
	 * @param browser
	 * @param driver
	 * @param downloadFilepath
	 * @return
	 */
	private WebDriver createLocalDriver(String browser, WebDriver driver, String downloadFilepath) {
		switch (browser) {
		case "Chrome":
			System.setProperty("webdriver.chrome.driver", _configuration.get_rootPath() + "\\SETUPS\\chromedriver.exe");
			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("profile.default_content_settings.popups", 0);
		//	chromePrefs.put("download.prompt_for_download", false);
			chromePrefs.put("download.default_directory", downloadFilepath);
			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", chromePrefs);
			options.addArguments("disable-infobars");
			options.addArguments("--start-maximized");
			options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			driver = new ChromeDriver(options);
			break;
		case "IE":
			System.setProperty("webdriver.ie.driver", _configuration.get_rootPath() + "\\SETUPS\\IEDriverServer.exe");
			driver = new InternetExplorerDriver();
			break;
		case "FireFox":
			System.setProperty("webdriver.gecko.driver", _configuration.get_rootPath() + "\\SETUPS\\geckodriver.exe");
			FirefoxOptions ffOption = new FirefoxOptions();
			ffOption.addPreference("browser.download.dir", downloadFilepath);
			ffOption.addPreference("browser.link.open_newwindow", 3);
			ffOption.addPreference("browser.download.folderList", 2);
			ffOption.addPreference("browser.download.manager.alertOnEXEOpen", false);
			ffOption.addPreference("browser.helperApps.neverAsk.saveToDisk",
					"application/x-msexcel, application/excel, application/x-excel, "
							+ "application/vnd.ms-excel, application/octet-stream, application/x-compress, "
							+ "application/msword, application/csv, text/csv, image/png, image/jpeg, "
							+ "application/pdf, text/html, text/plain");
			driver = new FirefoxDriver(ffOption);
			break;
		case "Edge":
			System.setProperty("webdriver.edge.driver",
					"C:\\Program Files (x86)\\Microsoft Web Driver\\MicrosoftWebDriver.exe");
			driver = new EdgeDriver();
			break;
		default:
			System.out.println("No driver for Local");
			break;
		}
		return driver;
	}

}
