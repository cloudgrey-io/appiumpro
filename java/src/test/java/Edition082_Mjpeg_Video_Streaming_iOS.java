import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.AndroidStartScreenRecordingOptions;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.screenrecording.CanRecordScreen;
import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static junit.framework.TestCase.assertTrue;


public class Edition082_Mjpeg_Video_Streaming_iOS {

    private String IOS_APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.9.0/TheApp-v1.9.0.app.zip"; // in order to download, you may need to install the mitmproxy certificate on your operating system first. Or download the app and replace this capability with the path to your app.

    private IOSDriver driver;

    @After
    public void Quit() {
        driver.quit();
    }


    @Test
    public void timeScreenshotsWithDefaultBehavior() throws IOException, URISyntaxException, InterruptedException, ExecutionException, TimeoutException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "iOS");
        caps.setCapability("platformVersion", "12.4");
        caps.setCapability("deviceName", "iPhone Xs");
        caps.setCapability("automationName", "XCUITest");
        caps.setCapability("app", IOS_APP);

        driver = new IOSDriver(new URL("http://0.0.0.0:4723/wd/hub"), caps);

        driver.startRecordingScreen(); // this is unnecessary for this test run, but included here to make this test identical to the next test

        long startTime = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            driver.getScreenshotAs(OutputType.FILE);
        }
        long endTime = System.nanoTime();

        long msElapsed = (endTime - startTime) / 1000000;
        System.out.println("100 screenshots normally: " + msElapsed + "ms. On average " + msElapsed/100 + "ms per screenshot");
        // about 172ms per screenshot on my machine
    }

    @Test
    public void timeScreenshotsWithMjpegScreenshotBehavior() throws IOException, URISyntaxException, InterruptedException, ExecutionException, TimeoutException {

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "iOS");
        caps.setCapability("platformVersion", "12.4");
        caps.setCapability("deviceName", "iPhone Xs");
        caps.setCapability("automationName", "XCUITest");
        caps.setCapability("app", IOS_APP);

        driver = new IOSDriver(new URL("http://0.0.0.0:4723/wd/hub"), caps);

        driver.startRecordingScreen(); // this is unnecessary for this test run, but included here to make this test identical to the next test

        long startTime = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            driver.getScreenshotAs(OutputType.FILE);
        }
        long endTime = System.nanoTime();

        long msElapsed = (endTime - startTime) / 1000000;
        System.out.println("100 screenshots using mjpeg: " + msElapsed + "ms. On average " + msElapsed/100 + "ms per screenshot");
        // about 436ms per screenshot on my machine
    }
}
