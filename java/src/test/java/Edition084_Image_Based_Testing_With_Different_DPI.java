import ImageFinder.TestUtil;
import io.appium.java_client.Setting;
import io.appium.java_client.android.AndroidDriver;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.PointerInput.Kind;
import org.openqa.selenium.interactions.PointerInput.MouseButton;
import org.openqa.selenium.interactions.PointerInput.Origin;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.DesiredCapabilities;

import static java.util.Optional.ofNullable;

public class Edition084_Image_Based_Testing_With_Different_DPI {

    private AndroidDriver driver;

    private Integer getDPI(AndroidDriver driver) {
        Object dpiObject = driver.getSessionDetail("deviceScreenDensity");
        int dpi = ofNullable(dpiObject).map(String::valueOf).map(Integer::valueOf).orElse(null);

        return dpi;
    }

    private void shootBird(AndroidDriver driver, int[] birdLocation, int xOffset, int yOffset) {
        Point start = new Point(birdLocation[0], birdLocation[1]);
        Point end = start.moveBy(xOffset, yOffset);
        Duration dragDuration = Duration.ofMillis(500);

        PointerInput finger = new PointerInput(Kind.TOUCH, "finger");
        Sequence shoot = new Sequence(finger, 0);
        shoot.addAction(finger.createPointerMove(Duration.ofMillis(0), Origin.viewport(), start.x, start.y));
        shoot.addAction(finger.createPointerDown(MouseButton.LEFT.asArg()));
        shoot.addAction(finger.createPointerMove(dragDuration, Origin.viewport(), end.x, end.y));
        shoot.addAction(finger.createPointerUp(MouseButton.LEFT.asArg()));
        driver.perform(Arrays.asList(shoot));
    }

    @Before
    public void setUp() throws URISyntaxException, IOException {
//        File classpathRoot = new File(System.getProperty("user.dir"));
//        File appDir = new File(classpathRoot, "../apps/");
//        String app = new File(appDir.getCanonicalPath(), "Angry_Birds_Classic_v8.0.3_apkpure.com.apk").getAbsolutePath();

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Android Emulator");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("appPackage", "com.rovio.angrybirds");
        capabilities.setCapability("appActivity", "com.rovio.fusion.App");
        capabilities.setCapability("appWaitActivity", "com.rovio.fusion.App");
        capabilities.setCapability("orientation", "LANDSCAPE");
     //   capabilities.setCapability("fixImageTemplateScale", true);
     //   capabilities.setCapability("mjpegScreenshotUrl", "http://localhost:8080/stream.mjpeg");

        driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

        int dpi = getDPI(driver);

    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testPigDestruction() throws URISyntaxException, IOException, InterruptedException {
        Thread.sleep(11000);
        TestUtil.TapImage("queryimages/checkmark.png", driver);

        Thread.sleep(7000);
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Thread.sleep(2000);
        int[] birdLocation = TestUtil.FindImage("queryimages/red-bird-in-slingshot.png", driver);
        shootBird(driver, birdLocation, -280, 140);

        // takes a while to sum up the scores and win
        driver.manage().timeouts().implicitlyWait(25, TimeUnit.SECONDS);

        Thread.sleep(25000);
        Assert.assertTrue(TestUtil.FindImage("queryimages/level-cleared-three-stars.png", driver)[0] > 0);
    }

}
