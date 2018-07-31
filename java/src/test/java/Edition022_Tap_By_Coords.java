import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Edition022_Tap_By_Coords {

    private String APP_IOS = "https://github.com/cloudgrey-io/the-app/releases/download/v1.5.0/TheApp-v1.5.0.app.zip";
    private String APP_ANDROID = "https://github.com/cloudgrey-io/the-app/releases/download/v1.5.0/TheApp-v1.5.0.apk";


    private static By referenceElement = MobileBy.AccessibilityId("Clipboard Demo");
    private static By username = MobileBy.AccessibilityId("username");

    @Test
    public void testTapByCoord_iOS() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "11.4");
        capabilities.setCapability("deviceName", "iPhone 8");
        capabilities.setCapability("app", APP_IOS);

        IOSDriver driver = new IOSDriver<>(new URL("http://localhost:4723/wd/hub"), capabilities);
        actualTest(driver);
    }

    @Test
    public void testTapByCoord_Android() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Android Emulator");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("app", APP_ANDROID);

        AndroidDriver driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
        actualTest(driver);
    }

    public void actualTest(AppiumDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        try {
            // find our reference element
            WebElement ref = wait
                .until(ExpectedConditions.presenceOfElementLocated(referenceElement));

            // get the location and dimensions of the reference element, and find its center point
            Rectangle rect = ref.getRect();
            int refElMidX = rect.getX() + rect.getWidth() / 2;
            int refElMidY = rect.getY() + rect.getHeight() / 2;

            // set the center point of our desired element; we know it is one row above the
            // reference element so we simply have to subtract the height of the reference element
            int desiredElMidX = refElMidX;
            int desiredElMidY = refElMidY - rect.getHeight();

            // perform the TouchAction that will tap the desired point
            TouchAction action = new TouchAction<>(driver);
            action.press(PointOption.point(desiredElMidX, desiredElMidY));
            action.waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)));
            action.release();
            action.perform();

            // finally, verify we made it to the login screen (which means we did indeed tap on
            // the desired element)
            wait.until(ExpectedConditions.presenceOfElementLocated(username));
        } finally {
            driver.quit();
        }

    }
}
