package Edition054_Selenium_Grid;

import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Edition054_Selenium_Grid_Desktop extends Edition054_Selenium_Grid {

    @Test
    public void testDesktop() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("browserName", "chrome");
        caps.setCapability("browserVersion", "71");
        caps.setCapability("platformName", "MAC");

        driver = new RemoteWebDriver(new URL(HUB_URL), caps);
        actualTest(driver);
    }
}
