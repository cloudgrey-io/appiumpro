import java.net.URL;

import com.applitools.eyes.StdoutLogHandler;
import com.applitools.eyes.appium.Eyes;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;

public class Edition100_Visual_Testing_3 {
    private final static String APP_V1 = "https://github.com/cloudgrey-io/the-app/releases/download/v1.10.0/TheApp-VR-v1.apk";
    private final static String APP_V2 = "https://github.com/cloudgrey-io/the-app/releases/download/v1.10.0/TheApp-VR-v2.apk";

    private final static String CHECK_HOME = "home_screen_full";
    private final static String CHECK_ECHO = "echo_screen";

    private final static By LOGIN_SCREEN = MobileBy.AccessibilityId("Login Screen");
    private final static By ECHO_SCREEN = MobileBy.AccessibilityId("Echo Box");
    private final static By MSG_BOX = MobileBy.AccessibilityId("messageInput");
    private final static By SAVE_BTN = MobileBy.AccessibilityId("messageSaveBtn");

    private AndroidDriver<WebElement> driver;
    private WebDriverWait wait;
    private Eyes eyes;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Android Emulator");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("app", APP_V1);
        //capabilities.setCapability("app", APP_V2);
        capabilities.setCapability("uninstallOtherPackages", "io.cloudgrey.the_app");
        URL server = new URL("http://localhost:4723/wd/hub");
        driver = new AndroidDriver<>(server, capabilities);

        // set up Eyes SDK
        eyes = new Eyes();
        eyes.setLogHandler(new StdoutLogHandler());
        eyes.setForceFullPageScreenshot(true);
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));

        wait = new WebDriverWait(driver, 5);
    }

    @After
    public void tearDown() {
        eyes.abortIfNotClosed();
        if (driver != null) {
            driver.quit();
        }
    }

    private WebElement waitForElement(WebDriverWait wait, By selector) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(selector));
        try { Thread.sleep(750); } catch (InterruptedException ign) {}
        return el;
    }

    @Test
    public void testLoginScreen() {
        eyes.open(driver, "TheApp", "appium pro login screen scroll test");

        // wait for an element that's on the home screen
        waitForElement(wait, LOGIN_SCREEN);

        // now we know the home screen is loaded, so do a visual check
        eyes.checkWindow(CHECK_HOME);
        eyes.close();
    }

    @Test
    public void testEchoScreen() {
        String msg = "hello";
        eyes.open(driver, "TheApp", "appium pro echo screen test");

        wait.until(ExpectedConditions.presenceOfElementLocated(ECHO_SCREEN)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(MSG_BOX)).sendKeys(msg);
        driver.findElement(SAVE_BTN).click();
        WebElement savedMsg = wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId(msg)));
        Assert.assertEquals(msg, savedMsg.getText());

        eyes.checkWindow(CHECK_ECHO);
        eyes.close();
    }
}
