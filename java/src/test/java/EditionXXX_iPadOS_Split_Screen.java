import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import com.google.common.collect.ImmutableMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.ios.IOSDriver;

public class EditionXXX_iPadOS_Split_Screen {
    private IOSDriver<WebElement> driver;
    private final static String PREFS = "com.apple.Preferences";
    private final static String CAL = "com.apple.mobilecal";

    @Before
    public void setUp() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "13.3");
        capabilities.setCapability("deviceName", "iPad Pro (12.9-inch) (3rd generation)");
        capabilities.setCapability("app", CAL);
        driver = new IOSDriver<WebElement>(new URL("http://localhost:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testSplitScreen() {
        driver.executeScript("mobile: activateApp", ImmutableMap.of("bundleId", PREFS));
        System.out.println(driver.executeScript("mobile: activeAppInfo", new HashMap<String, Object>()));
        driver.setSetting("defaultActiveApplication", CAL);
        System.out.println(driver.executeScript("mobile: activeAppInfo", new HashMap<String, Object>()));
    }

}
