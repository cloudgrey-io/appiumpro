import java.net.URL;

import com.applitools.eyes.StdoutLogHandler;
import com.applitools.eyes.appium.Eyes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;

public class Edition099_Visual_Testing_2 {
    private final static String APP_V1 = "https://github.com/cloudgrey-io/the-app/releases/download/v1.10.0/TheApp-VR-v1.apk";
    private final static String APP_V2 = "https://github.com/cloudgrey-io/the-app/releases/download/v1.10.0/TheApp-VR-v2.apk";

    private final static String CHECK_HOME = "home_screen";
    private final static String CHECK_LOGIN = "login_screen";

    private final static By LOGIN_SCREEN = MobileBy.AccessibilityId("Login Screen");
    private final static By USERNAME_FIELD = MobileBy.AccessibilityId("username");

    private AndroidDriver<WebElement> driver;
    private Eyes eyes;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Android Emulator");
        capabilities.setCapability("automationName", "UiAutomator2");
        //capabilities.setCapability("app", APP_V1);
        capabilities.setCapability("app", APP_V2);
        capabilities.setCapability("uninstallOtherPackages", "io.cloudgrey.the_app");
        URL server = new URL("http://localhost:4723/wd/hub");
        driver = new AndroidDriver<>(server, capabilities);

        // set up Eyes SDK
        eyes = new Eyes();
        eyes.setLogHandler(new StdoutLogHandler());
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
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
    public void testAppDesign() {
        eyes.open(driver, "TheApp", "appium pro basic design test");

        WebDriverWait wait = new WebDriverWait(driver, 5);

        // wait for an element that's on the home screen
        WebElement loginScreen = waitForElement(wait, LOGIN_SCREEN);

        // now we know the home screen is loaded, so do a visual check
        eyes.checkWindow(CHECK_HOME);

        // nav to the login screen, and wait for an element that's on the login screen
        loginScreen.click();
        waitForElement(wait, USERNAME_FIELD);

        // perform our second visual check, this time of the login screen
        eyes.checkWindow(CHECK_LOGIN);
        eyes.close();
    }
}
