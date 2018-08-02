import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Edition030_iOS_Gestures {

    private String APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.6.0/TheApp-v1.6.0.app.zip";

    private IOSDriver driver;
    private WebDriverWait wait;

    private By listView = MobileBy.AccessibilityId("List Demo");

    @Before
    public void setUp() throws IOException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "iOS");
        caps.setCapability("platformVersion", "11.4");
        caps.setCapability("deviceName", "iPhone X");
        caps.setCapability("app", APP);
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
    public void testGestures() {
        wait.until(ExpectedConditions.presenceOfElementLocated(listView)).click();

        // swipe up then down
        Map<String, Object> args = new HashMap<>();
        args.put("direction", "up");
        driver.executeScript("mobile: swipe", args);
        args.put("direction", "down");
        driver.executeScript("mobile: swipe", args);

        // scroll down then up
        args.put("direction", "down");
        driver.executeScript("mobile: scroll", args);
        args.put("direction", "up");
        driver.executeScript("mobile: scroll", args);

        // scroll to the last item in the list by accessibility id
        args.put("direction", "down");
        args.put("name", "Stratus");
        driver.executeScript("mobile: scroll", args);

        // scroll back to the first item in the list
        MobileElement list = (MobileElement) driver.findElement(By.className("XCUIElementTypeScrollView"));
        args.put("direction", "up");
        args.put("name", null);
        args.put("element", list.getId());
        driver.executeScript("mobile: scroll", args);
    }

}
