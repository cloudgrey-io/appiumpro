package Edition054_Selenium_Grid;

import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Edition054_Selenium_Grid_IOS extends Edition054_Selenium_Grid {

    @Test
    public void testIOS() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "iOS");
        caps.setCapability("platformVersion", "11.4");
        caps.setCapability("deviceName", "iPhone 8");
        caps.setCapability("browserName", "Safari");

        driver = new RemoteWebDriver(new URL(HUB_URL), caps);
        actualTest(driver);
    }
}