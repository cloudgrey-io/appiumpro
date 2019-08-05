import io.appium.java_client.Setting;
import io.appium.java_client.android.AndroidDriver;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.PointerInput.Kind;
import org.openqa.selenium.interactions.PointerInput.MouseButton;
import org.openqa.selenium.interactions.PointerInput.Origin;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.DesiredCapabilities;

import static java.util.Optional.ofNullable;

public class Edition081_Image_Based_Testing_With_Different_DPI {

    private AndroidDriver driver;

    private Path getResource(String fileName) throws URISyntaxException {
        URL refImgUrl = getClass().getClassLoader().getResource(fileName);
        return Paths.get(refImgUrl.toURI()).toFile().toPath();
    }

    private String getReferenceImageB64(String fileName) throws URISyntaxException, IOException {
        Path refImgPath = getResource("Edition081_" + fileName);
        return Base64.getEncoder().encodeToString(Files.readAllBytes(refImgPath));
    }

    private Integer getDPI(AndroidDriver driver) {
        Object dpiObject = driver.getSessionDetail("deviceScreenDensity");
        int dpi = ofNullable(dpiObject).map(String::valueOf).map(Integer::valueOf).orElse(null);

        return dpi;
    }

    private void shootBird(AndroidDriver driver, WebElement birdEl, int xOffset, int yOffset) {
        Rectangle rect = birdEl.getRect();
        Point start = new Point(rect.x + rect.width / 2, rect.y + rect.height / 2);
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
        File classpathRoot = new File(System.getProperty("user.dir"));
        File appDir = new File(classpathRoot, "../apps/");
        String app = new File(appDir.getCanonicalPath(), "Angry_Birds_Classic_v8.0.3_apkpure.com.apk").getAbsolutePath();

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Android Emulator");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("app", app);
        capabilities.setCapability("appWaitActivity", "com.rovio.fusion.App");
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
    public void testPigDestruction() throws URISyntaxException, IOException {
        String checkmark = getReferenceImageB64("checkmark.png");
        String bird = getReferenceImageB64("red-bird-in-slingshot.png");
        String victory = getReferenceImageB64("level-cleared-three-stars.png");

        driver.setSetting(Setting.IMAGE_MATCH_THRESHOLD, 0.5);

        driver.findElementByImage(checkmark).click();

        WebElement birdEl = driver.findElementByImage(bird);
        shootBird(driver, birdEl, -280, 140);

        // takes a while to sum up the scores and win
        driver.manage().timeouts().implicitlyWait(25, TimeUnit.SECONDS);

        driver.findElementByImage(victory);
    }

}
