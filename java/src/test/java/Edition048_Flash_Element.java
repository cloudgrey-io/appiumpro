import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Edition048_Flash_Element {

    private String APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.8.0/TheApp-v1.8.0.apk";

    private AppiumDriver driver;
    private WebDriverWait wait;

    private By loginScreen = MobileBy.AccessibilityId("Login Screen");

    @Before
    public void setUp() throws IOException {
        DesiredCapabilities caps = new DesiredCapabilities();

        caps.setCapability("platformName", "Android");
        caps.setCapability("deviceName", "Android Emulator");
        caps.setCapability("automationName", "Espresso");
        caps.setCapability("forceEspressoRebuild", true);
        caps.setCapability("useKeystore", true);
        caps.setCapability("keystorePath", "/Users/jlipps/.android/debug.keystore");
        caps.setCapability("keystorePassword", "android");
        caps.setCapability("keyAlias", "androiddebugkey");
        caps.setCapability("keyPassword", "android");


        caps.setCapability("app", APP);
        driver = new AppiumDriver(new URL("http://localhost:4723/wd/hub"), caps);
        wait = new WebDriverWait(driver, 10);
    }

    @After
    public void tearDown() {
        try {
            driver.quit();
        } catch (Exception ign) {}
    }

    @Test
    public void testFlashElement() {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(loginScreen));

        HashMap<String, Object> scriptArgs = new HashMap<>();
        scriptArgs.put("element", ((RemoteWebElement)el).getId());
        scriptArgs.put("durationMillis", 50); // how long should each flash take?
        scriptArgs.put("repeatCount", 20);     // how many times should we flash?

        driver.executeScript("mobile: flashElement", scriptArgs);
    }

}
