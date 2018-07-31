package Edition028_Parallel_Testing;

import io.appium.java_client.ios.IOSDriver;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Edition028_Parallel_Testing_iOS extends Edition028_Parallel_Testing_Base {

    protected String APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.5.0/TheApp-v1.5.0.app.zip";

    @Test
    public void testLogin_iOS() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "11.4");
        capabilities.setCapability("deviceName", "iPhone 8");
        capabilities.setCapability("wdaLocalPort", 8100);
        capabilities.setCapability("app", APP);

        IOSDriver driver = new IOSDriver<>(new URL("http://localhost:4723/wd/hub"), capabilities);
        actualTest(driver);
    }
}
