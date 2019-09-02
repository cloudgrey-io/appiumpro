import io.appium.java_client.android.AndroidDriver;
import java.io.IOException;
import java.net.URL;
import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.DesiredCapabilities;


public class Edition083_Mjpeg_Screenshots_Android {

    private String ANDROID_APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.9.0/TheApp-v1.9.0.apk"; // in order to download, you may need to install the mitmproxy certificate on your operating system first. Or download the app and replace this capability with the path to your app.

    private AndroidDriver driver;

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
            caps.setCapability("mjpegScreenshotUrl", "http://localhost:8080/stream.mjpeg");
        }

        driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"), caps);

        long startTime = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            driver.getScreenshotAs(OutputType.FILE);
        }
        long endTime = System.nanoTime();

        long msElapsed = (endTime - startTime) / 1000000;
        return msElapsed;
    }

    @Test
    public void timeScreenshotsWithDefaultBehavior() throws IOException {
        long msElapsed = timeScreenshots(false);
        System.out.println("100 screenshots normally: " + msElapsed + "ms. On average " + msElapsed/100 + "ms per screenshot");
        // about 305ms per screenshot on my machine
    }

    @Test
    public void timeScreenshotsWithMjpegScreenshotBehavior() throws IOException {
        long msElapsed = timeScreenshots(true);
        System.out.println("100 screenshots using mjpeg: " + msElapsed + "ms. On average " + msElapsed/100 + "ms per screenshot");
        // about 142ms per screenshot on my machine
    }
}