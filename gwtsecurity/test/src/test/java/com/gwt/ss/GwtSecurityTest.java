/**
 * 
 */
package com.gwt.ss;

import java.io.IOException;

import junit.framework.TestCase;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.gwt.ss.test.client.MainPanel;

/**
 * @author steve
 * 
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class GwtSecurityTest extends TestCase {

	private static WebDriver driver;

	private static TestServer server;

	public static void destroyWebDriver() {
		driver.quit();
	}

	public static void initWebDriver() throws IOException {
		System.setProperty("webdriver.chrome.driver",
				"/home/steve/bin/chromedriver");
		driver = new ChromeDriver();
	}

	@BeforeClass
	public static void startServer() throws Exception {
		server = new TestServer();
		server.start();
		initWebDriver();
	}

	@AfterClass
	public static void stopServer() throws Exception {
		destroyWebDriver();
		server.stop();
	}

	public String doRPCClick(String id) {
		WebElement element = driver.findElement(By.id("gwt-debug-" + id));
		element.click();
		new WebDriverWait(driver, 10000).until(ExpectedConditions
				.presenceOfElementLocated(By.id("gwt-debug-"
						+ MainPanel.RETURN_VALUE_ID)));
		element = driver.findElement(By.id("gwt-debug-"
				+ MainPanel.RETURN_VALUE_ID));
		return element != null ? element.getText() : null;
	}

	@Test
	public void testElement() throws Exception {
		String pathToGwtApp = "http://127.0.0.1:17000/index.html";
		driver.get(pathToGwtApp);

		new WebDriverWait(driver, 20000).until(ExpectedConditions
				.presenceOfElementLocated(By.id("gwt-debug-"
						+ MainPanel.DEBUG_ID)));

		String result = doRPCClick(MainPanel.UNRESTRICTED_BTN_ID);
		assertTrue(result != null);
		assertTrue(result.equals("unrestricted"));

		result = doRPCClick(MainPanel.USER_SECURED_BTN_ID);
		assertTrue(result != null);
		assertFalse(result.equals("userSecured"));

		result = doRPCClick(MainPanel.ADMIN_SECURED_BTN_ID);
		assertTrue(result != null);
		assertFalse(result.equals("adminSecured"));
	}

}
