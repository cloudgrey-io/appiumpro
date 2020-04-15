import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.ios.IOSDriver;

public class Edition112_iOS_Screenshot_Orientation {
    private IOSDriver<WebElement> driver;
    private final static String PHOTOS = "com.apple.mobileslideshow";

    @Before
    public void setUp() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "13.3");
        capabilities.setCapability("deviceName", "iPad Pro (12.9-inch) (3rd generation)");
        capabilities.setCapability("app", PHOTOS);
        capabilities.setCapability("simulatorTracePointer", true);
        driver = new IOSDriver<WebElement>(new URL("http://localhost:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testScreenshots() throws Exception {
        Thread.sleep(2000);
        String desktop = System.getenv("HOME") + "/Desktop";
        driver.rotate(ScreenOrientation.LANDSCAPE);
        File regularScreenshot = driver.getScreenshotAs(OutputType.FILE);
        driver.setSetting("screenshotOrientation", "landscapeRight");
        File adjustedScreenshot = driver.getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(regularScreenshot, new File(desktop + "/screen1.png"));
        FileUtils.copyFile(adjustedScreenshot, new File(desktop + "/screen2.png"));
    }
}
