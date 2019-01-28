package Edition054_Selenium_Grid;

import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Edition054_Selenium_Grid_Android extends Edition054_Selenium_Grid {

    @Test
    public void testAndroid() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("deviceName", "Android Emulator");
        caps.setCapability("automationName", "UiAutomator2");
        caps.setCapability("browserName", "Chrome");

        driver = new RemoteWebDriver(new URL(HUB_URL), caps);
        actualTest(driver);
    }
}
