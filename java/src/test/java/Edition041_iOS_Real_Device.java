import io.appium.java_client.ios.IOSDriver;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Edition041_iOS_Real_Device {
    private IOSDriver driver;

    @Before
    public void setUp() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "12.0.1");
        capabilities.setCapability("deviceName", "iPhone 8");
        capabilities.setCapability("udid", "auto");
        capabilities.setCapability("bundleId", "<your bundle id>");
        capabilities.setCapability("xcodeOrgId", "<your org id>");
        capabilities.setCapability("xcodeSigningId", "iPhone Developer");
        capabilities.setCapability("updatedWDABundleId", "<bundle id in scope of provisioning profile>");

        driver = new IOSDriver<>(new URL("http://localhost:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testFindingAnElement() {
        driver.findElementByAccessibilityId("Login Screen");
    }
}
