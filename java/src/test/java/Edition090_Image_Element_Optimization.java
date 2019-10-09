import io.appium.java_client.MobileBy;
import io.appium.java_client.Setting;
import io.appium.java_client.android.AndroidDriver;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Arrays;
import java.util.Base64;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.PointerInput.Kind;
import org.openqa.selenium.interactions.PointerInput.MouseButton;
import org.openqa.selenium.interactions.PointerInput.Origin;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Edition090_Image_Element_Optimization {

    private AndroidDriver driver;

    @Before
    public void setUp() throws URISyntaxException, IOException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Android Emulator");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("appPackage", "com.rovio.angrybirds");
        capabilities.setCapability("appActivity", "com.rovio.fusion.App");
        capabilities.setCapability("orientation", "LANDSCAPE");

        driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    private File getImageFile(String imageName) {
        return new File("queryimages/" + imageName + ".png");
    }

    private String getReferenceImageB64(String imageName) throws IOException {
        Path refImgPath = getImageFile(imageName).toPath();
        return Base64.getEncoder().encodeToString(Files.readAllBytes(refImgPath));
    }

    private void shootBird(AndroidDriver driver, WebElement birdEl, int xOffset, int yOffset) {
        Rectangle rect = birdEl.getRect();
        Point start = new Point(rect.x + rect.width / 2, rect.y + rect.height / 2);
        Point end = start.moveBy(xOffset, yOffset);
        Duration dragDuration = Duration.ofMillis(750);

        PointerInput finger = new PointerInput(Kind.TOUCH, "finger");
        Sequence shoot = new Sequence(finger, 0);
        shoot.addAction(finger.createPointerMove(Duration.ofMillis(0), Origin.viewport(), start.x, start.y));
        shoot.addAction(finger.createPointerDown(MouseButton.LEFT.asArg()));
        shoot.addAction(finger.createPointerMove(dragDuration, Origin.viewport(), end.x, end.y));
        shoot.addAction(finger.createPointerUp(MouseButton.LEFT.asArg()));
        driver.perform(Arrays.asList(shoot));
    }

    private WebElement findImageWithOptimizationNotes(String imageName) throws Exception {
        String imageData = getReferenceImageB64(imageName);
        WebElement el = null;
        double max = 1.0;
        double min = 0.0;
        double haltSearchSpread = 0.05;
        double check = 0;
        NotFoundException notFound = null;

        while (Math.abs(max - min) > haltSearchSpread) {
            check = (max + min) / 2;
            driver.setSetting(Setting.IMAGE_MATCH_THRESHOLD, check);
            try {
                el = driver.findElement(MobileBy.image(imageData));
                min = check;
            } catch (NotFoundException err) {
                max = check;
                notFound = err;
            }
        }

        if (el != null) {
            System.out.println("Image '" + imageName + "' was found at the highest threshold of: " + check);
            return el;
        }

        System.out.println("Image '" + imageName + "' could not be found even at a threshold as low as: " + check);
        throw notFound;
    }

    @Test
    public void testPigDestruction() throws Exception {
        Thread.sleep(12000);
        findImageWithOptimizationNotes("checkmark").click();

        Thread.sleep(3000);
        WebElement birdEl = findImageWithOptimizationNotes("red-bird-in-slingshot");
        shootBird(driver, birdEl, -280, 140);

        Thread.sleep(14000);
        findImageWithOptimizationNotes("level-cleared-three-stars");
    }

}
