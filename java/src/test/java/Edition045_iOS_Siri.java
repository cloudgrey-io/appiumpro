import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
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

public class Edition045_iOS_Siri {

    private String APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.8.0/TheApp-v1.8.0.app.zip";

    private IOSDriver driver;
    private WebDriverWait wait;

    private By siriCalcQ = MobileBy.AccessibilityId("2 + 2 =");
    private By siriCalcA = MobileBy.AccessibilityId("4");

    @Before
    public void setUp() throws IOException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "iOS");
        caps.setCapability("platformVersion", "12.1");
        caps.setCapability("deviceName", "iPhone 8");
        caps.setCapability("noReset", true);
        caps.setCapability("app", APP);

        driver = new IOSDriver<MobileElement>(new URL("http://localhost:4723/wd/hub"), caps);
        wait  = new WebDriverWait(driver, 20);
    }

    @After
    public void tearDown() {
        try {
            driver.quit();
        } catch (Exception ign) {}
    }

    @Test
    public void testSiri() {
        HashMap<String, String> args = new HashMap<>();
        args.put("text", "What's two plus two?");
        driver.executeScript("mobile: siriCommand", args);
        wait.until(ExpectedConditions.presenceOfElementLocated(siriCalcQ));
        wait.until(ExpectedConditions.presenceOfElementLocated(siriCalcA));
    }
}
