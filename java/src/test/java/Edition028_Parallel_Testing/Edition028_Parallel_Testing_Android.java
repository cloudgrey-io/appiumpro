package Edition028_Parallel_Testing;

import io.appium.java_client.android.AndroidDriver;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Edition028_Parallel_Testing_Android extends Edition028_Parallel_Testing_Base {

    protected String APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.5.0/TheApp-v1.5.0.apk";

    @Test
    public void testLogin_Android() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Android Emulator");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("app", APP);

        AndroidDriver driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
        actualTest(driver);
    }
}
