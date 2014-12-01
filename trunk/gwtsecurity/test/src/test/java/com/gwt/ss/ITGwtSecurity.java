/**
 * $Id$
 *
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 * Copyright (c) 2014 MJN Services, Inc., All Rights Reserved.
 */
package com.gwt.ss;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;
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
public class ITGwtSecurity extends TestCase {

    private static final String ACCESS_DENIED = "com.gwt.ss.client.exceptions.GwtAccessDeniedException: Access is denied";

    private static WebDriver driver;

    private static Properties props;

    private static TestServer server;

    public static void destroyWebDriver() {
        driver.quit();
    }

    public static void initWebDriver() throws IOException {
        props = TestServer.loadProperties();
        assertNotNull(props);
        String cdl = props.getProperty("chromedriver.location", null);
        if (cdl == null) {
            // Try and find the chromedriver on the path. UNIX only!
            cdl = IOUtils.toString(Runtime.getRuntime().exec("which chromedriver").getInputStream());
            if (cdl != null) {
                cdl = cdl.trim();
                if (cdl.equals("")) {
                    cdl = null;
                }
            }
        }
        assertNotNull("ChromeDriver not found!", cdl);
        assertTrue(new File(cdl).isFile());
        System.setProperty("webdriver.chrome.driver", cdl);
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

    public String doRPCClick(final String id) {
        WebElement element = driver.findElement(By.id("gwt-debug-" + id));
        element.click();
        new WebDriverWait(driver, 10000).until(ExpectedConditions.presenceOfElementLocated(By.id("gwt-debug-"
                + MainPanel.RETURN_VALUE_ID)));
        element = driver.findElement(By.id("gwt-debug-" + MainPanel.RETURN_VALUE_ID));
        return element != null ? element.getText() : null;
    }

    @Test
    public void testElement() throws Exception {
        String pathToGwtApp = "http://127.0.0.1:17000/index.html";
        driver.get(pathToGwtApp);

        new WebDriverWait(driver, 20000).until(ExpectedConditions.presenceOfElementLocated(By.id("gwt-debug-"
                + MainPanel.DEBUG_ID)));

        // Not logged in.
        String result = doRPCClick(MainPanel.UNRESTRICTED_BTN_ID);
        assertTrue(result != null);
        assertTrue(result.equals("unrestricted"));

        result = doRPCClick(MainPanel.USER_SECURED_BTN_ID);
        assertTrue(result != null);
        assertTrue(result.equals(ACCESS_DENIED));

        result = doRPCClick(MainPanel.ADMIN_SECURED_BTN_ID);
        assertTrue(result != null);
        assertTrue(result.equals(ACCESS_DENIED));

        // Logged in as user.
        result = doRPCClick(MainPanel.USER_LOGIN_BTN_ID);
        assertTrue(result != null);
        assertTrue(result.equals("UserLoginSuccess"));

        result = doRPCClick(MainPanel.UNRESTRICTED_BTN_ID);
        assertTrue(result != null);
        assertTrue(result.equals("unrestricted"));

        result = doRPCClick(MainPanel.USER_SECURED_BTN_ID);
        assertTrue(result != null);
        assertTrue(result.equals("userSecured"));

        result = doRPCClick(MainPanel.ADMIN_SECURED_BTN_ID);
        assertTrue(result != null);
        assertTrue(result.equals(ACCESS_DENIED));

        // Logout user.
        result = doRPCClick(MainPanel.LOGOUT_BTN_ID);
        assertTrue(result != null);
        assertTrue(result.equals("LogoutSuccess"));

        // Logged in as admin.
        result = doRPCClick(MainPanel.ADMIN_LOGIN_BTN_ID);
        assertTrue(result != null);
        assertTrue(result.equals("AdminLoginSuccess"));

        result = doRPCClick(MainPanel.UNRESTRICTED_BTN_ID);
        assertTrue(result != null);
        assertTrue(result.equals("unrestricted"));

        result = doRPCClick(MainPanel.USER_SECURED_BTN_ID);
        assertTrue(result != null);
        assertTrue(result.equals("userSecured"));

        result = doRPCClick(MainPanel.ADMIN_SECURED_BTN_ID);
        assertTrue(result != null);
        assertTrue(result.equals("adminSecured"));

        // Logout user.
        result = doRPCClick(MainPanel.LOGOUT_BTN_ID);
        assertTrue(result != null);
        assertTrue(result.equals("LogoutSuccess"));

        // Not logged in.
        result = doRPCClick(MainPanel.UNRESTRICTED_BTN_ID);
        assertTrue(result != null);
        assertTrue(result.equals("unrestricted"));

        result = doRPCClick(MainPanel.USER_SECURED_BTN_ID);
        assertTrue(result != null);
        assertTrue(result.equals(ACCESS_DENIED));

        result = doRPCClick(MainPanel.ADMIN_SECURED_BTN_ID);
        assertTrue(result != null);
        assertTrue(result.equals(ACCESS_DENIED));
    }

}
