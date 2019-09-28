package Miscellaneous;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class test {
	
	public static void main(String[] args) throws InterruptedException
	{
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\SETUPS\\chromedriver.exe");
		
		WebDriver driver = new ChromeDriver();
		
		
		driver.get("https://www.flipkart.com");
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id='container']/div/div")).click();
		
		Thread.sleep(1000);
		driver.findElement(By.xpath("//h2[contains(text(),'Deals')]/parent::div//a[text()='VIEW ALL']"));
		
		
	}

	
	

}
