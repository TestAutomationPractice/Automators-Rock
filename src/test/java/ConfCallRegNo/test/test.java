package ConfCallRegNo.test;

import java.util.Iterator;
import java.util.logging.Level;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class test  {

	@SuppressWarnings("deprecation")
	public static void main(String [] args) throws InterruptedException{
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\SETUPS\\chromedriver.exe");
		ChromeDriver driver = null;
		
		String url = "https://nrs.awstest.netroadshow.com/nrsadmin/service/api/conferencecalls/10655/pins";
		
			ChromeOptions options = new ChromeOptions();
            DesiredCapabilities cap = DesiredCapabilities.chrome();
            cap.setCapability(ChromeOptions.CAPABILITY, options);

             LoggingPreferences logPrefs = new LoggingPreferences();
            logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
            cap.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);

            driver = new ChromeDriver(cap);
	
            
            
            System.out.println("Navigate to " + url);
            driver.navigate().to(url);
            Thread.sleep(10000);
            String page = driver.getPageSource();
            

           
            System.out.println("################"+page);

         
            
	}
}