import java.io.IOException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class Edition107_Generic_Swipe_Actions_Android extends Edition107_Base {

    private String APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.10.0/TheApp-v1.10.0.apk";
    private AndroidDriver<MobileElement> driver;

    @Before
    public void setUp() throws IOException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("deviceName", "Android Emulator");
        caps.setCapability("app", APP);
        driver = new AndroidDriver<MobileElement>(new URL("http://localhost:4723/wd/hub"), caps);
    }

    @Override
    protected AppiumDriver<MobileElement> getDriver() {
        return driver;
    }

    @Test
    public void testGestures() {
        navToList();
        scroll();
        scroll(ScrollDirection.UP);
    }

}
