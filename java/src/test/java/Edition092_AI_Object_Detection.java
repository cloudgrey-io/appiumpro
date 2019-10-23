import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.Setting;
import io.appium.java_client.ios.IOSDriver;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Edition092_AI_Object_Detection {

    private String BUNDLE_ID = "com.apple.DocumentsApp";
    private IOSDriver driver;
    private WebDriverWait wait;

    private By recents = MobileBy.custom("ai:clock");
    private By browse = MobileBy.AccessibilityId("Browse");
    private By noRecents = MobileBy.AccessibilityId("No Recents");

    @Before
    public void setUp() throws IOException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "iOS");
        caps.setCapability("automationName", "XCUITest");
        caps.setCapability("platformVersion", "11.4");
        caps.setCapability("deviceName", "iPhone 8");
        caps.setCapability("bundleId", BUNDLE_ID);

        HashMap<String, String> customFindModules = new HashMap<>();
        customFindModules.put("ai", "test-ai-classifier");

        caps.setCapability("customFindModules", customFindModules);
        caps.setCapability("testaiFindMode", "object_detection");
        caps.setCapability("testaiObjectDetectionThreshold", "0.9");
        caps.setCapability("shouldUseCompactResponses", false);

        driver = new IOSDriver<MobileElement>(new URL("http://localhost:4723/wd/hub"), caps);
        wait  = new WebDriverWait(driver, 10);
    }

    @After
    public void tearDown() {
        try {
            driver.quit();
        } catch (Exception ign) {}
    }

    @Test
    public void testFindElementUsingAI() {
        driver.setSetting(Setting.CHECK_IMAGE_ELEMENT_STALENESS, false);

        // click the "Browse" button to navigate away from Recents
        wait.until(ExpectedConditions.presenceOfElementLocated(browse)).click();

        // now find and click the recents button by the fact that it looks like a clock
        driver.findElement(recents).click();

        // prove that the click was successful by locating the 'No Recents' text
        wait.until(ExpectedConditions.presenceOfElementLocated(noRecents));
    }
}
