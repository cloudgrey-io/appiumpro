import com.google.common.collect.ImmutableMap;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import java.io.IOException;
import java.net.URL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Edition068_iOS_Buttons {
    private String APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.9.0/TheApp-v1.9.0.app.zip";

    private IOSDriver driver;

    @Before
    public void setUp() throws IOException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "iOS");
        caps.setCapability("platformVersion", "11.4");
        caps.setCapability("deviceName", "iPhone 8");

        caps.setCapability("app", APP);

        driver = new IOSDriver<MobileElement>(new URL("http://localhost:4723/wd/hub"), caps);
    }

    @After
    public void tearDown() {
        try {
            driver.quit();
        } catch (Exception ign) {}
    }

    @Test
    public void testHomeButton() {
        driver.executeScript("mobile: pressButton", ImmutableMap.of("name", "home"));
        try { Thread.sleep(1000); } catch (Exception ign) {}
        driver.executeScript("mobile: pressButton", ImmutableMap.of("name", "home"));

        // pause for effect
        try { Thread.sleep(2000); } catch (Exception ign) {}
    }
}
