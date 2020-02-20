
import java.io.IOException;
import java.net.URL;

import com.google.common.collect.ImmutableMap;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.AndroidDriver;


public class Edition105_Video_Streaming_Android {

    private String ANDROID_APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.10.0/TheApp-v1.10.0.apk";

    private AndroidDriver<WebElement> driver;

    @After
    public void tearDown() {
        driver.quit();
    }

    public long timeScreenshots (boolean useMjpeg) throws IOException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("deviceName", "Android Emulator");
        caps.setCapability("automationName", "UiAutomator2");
        caps.setCapability("app", ANDROID_APP);
        if (useMjpeg) {
            caps.setCapability("autoScreenStream", true);
            caps.setCapability("mjpegScreenshotUrl", "http://localhost:8093");
        }

        driver = new AndroidDriver<WebElement>(new URL("http://0.0.0.0:4723/wd/hub"), caps);

        long startTime = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            driver.getScreenshotAs(OutputType.FILE);
        }
        long endTime = System.nanoTime();

        long msElapsed = (endTime - startTime) / 1000000;

        return msElapsed;
    }

    //@Test
    //public void timeScreenshotsWithDefaultBehavior() throws IOException {
        //long msElapsed = timeScreenshots(false);
        //System.out.println("100 screenshots normally: " + msElapsed + "ms. On average " + msElapsed/100 + "ms per screenshot");
        //// about 305ms per screenshot on my machine
    //}

    @Test
    public void timeScreenshotsWithMjpegScreenshotBehavior() throws IOException {
        long msElapsed = timeScreenshots(true);
        System.out.println("100 screenshots using mjpeg: " + msElapsed + "ms. On average " + msElapsed/100 + "ms per screenshot");
        // about 142ms per screenshot on my machine
    }
}
