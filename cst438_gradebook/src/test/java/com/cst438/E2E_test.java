package com.cst438;


import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.*;

public class E2E_test {
	public static final String CHROME_DRIVER_FILE_LOCATION 
                          = "C:/Users/bnhoy/chromedriver_win32/chromedriver.exe";
	public static final String URL = "http://localhost:3000";
	public static final String ALIAS_NAME = "test";
	public static final String ASSIGNMENT_NAME = "New Test";
	public static final String ASSIGNMENT_COURSE_ID = "123456";
	public static final String ASSIGNMENT_COURSE_ID_FAKE = "135790";
	public static final String ASSIGNMENT_DUE_DATE = "2012-12-12";
	public static final int SLEEP_DURATION = 1000; // 1 second.

	public static ChromeDriver getChromeDriver() {
	    ChromeOptions chromeOptions = new ChromeOptions();
	    chromeOptions.addArguments("--start-maximized");
	    chromeOptions.addArguments("--remote-allow-origins=*");
	    return new ChromeDriver(chromeOptions);
	}
	
	@Test
	public void addAssignment() throws Exception {


		//TODO update the property name for your browser 
		System.setProperty("webdriver.chrome.driver",
                     CHROME_DRIVER_FILE_LOCATION);
		//TODO update the class ChromeDriver()  for your browser
		WebDriver driver = getChromeDriver();

		try {
			WebElement we;
			
			driver.get(URL);
			// must have a short wait to allow time for the page to download 
			Thread.sleep(SLEEP_DURATION);

			//Clicking the button to go the New Assignment Page
			we = driver.findElement(By.id("addAssignment"));
			we.click();
			Thread.sleep(SLEEP_DURATION);
			
			//Filling in the inputs
			we = driver.findElement(By.name("aName"));
			we.sendKeys(ASSIGNMENT_NAME);
			we = driver.findElement(By.name("cName"));
			we.sendKeys(ASSIGNMENT_COURSE_ID);
			we = driver.findElement(By.name("dueDate"));
			we.sendKeys(ASSIGNMENT_DUE_DATE);
			
			we = driver.findElement(By.id("Add"));
			we.click();
			Thread.sleep(SLEEP_DURATION);
		
			we = driver.findElement(By.className("Toastify__toast-body"));
			assertEquals(we.getText(), "Assignment successfully posted");
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
			
		} finally {
			driver.quit();
		}
	}
		
	@Test
	public void addAssignmentError() throws Exception {


		//TODO update the property name for your browser 
		System.setProperty("webdriver.chrome.driver",
                     CHROME_DRIVER_FILE_LOCATION);
		//TODO update the class ChromeDriver()  for your browser
		WebDriver driver = getChromeDriver();

		try {
			WebElement we;
			
			driver.get(URL);
			// must have a short wait to allow time for the page to download 
			Thread.sleep(SLEEP_DURATION);

			//Clicking the button to go the New Assignment Page
			we = driver.findElement(By.id("addAssignment"));
			we.click();
			Thread.sleep(SLEEP_DURATION);
			
			//Filling in the inputs
			we = driver.findElement(By.name("aName"));
			we.sendKeys(ASSIGNMENT_NAME);
			we = driver.findElement(By.name("cName"));
			we.sendKeys(ASSIGNMENT_COURSE_ID_FAKE);
			we = driver.findElement(By.name("dueDate"));
			we.sendKeys(ASSIGNMENT_DUE_DATE);
			
			we = driver.findElement(By.id("Add"));
			we.click();
			Thread.sleep(SLEEP_DURATION);
		
			we = driver.findElement(By.className("Toastify__toast-body"));
			assertEquals(we.getText(), "Assignment failed to post");
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
			
		} finally {
			driver.quit();
		}
	}
}

