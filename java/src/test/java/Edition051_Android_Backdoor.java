import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import com.google.common.collect.ImmutableMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.AndroidDriver;

public class Edition051_Android_Backdoor {

    private String APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.8.1/TheApp-v1.8.1.apk";

    private AndroidDriver<WebElement> driver;

    @Before
    public void setUp() throws IOException {
        DesiredCapabilities caps = new DesiredCapabilities();

        caps.setCapability("platformName", "Android");
        caps.setCapability("deviceName", "Android Emulator");
        caps.setCapability("automationName", "Espresso");
        caps.setCapability("app", APP);
        driver = new AndroidDriver<>(new URL("http://localhost:4723/wd/hub"), caps);
    }

    @After
    public void tearDown() {
        try {
            driver.quit();
        } catch (Exception ign) {}
    }

    @Test
    public void testBackdoor() {
        ImmutableMap<String, Object> scriptArgs = ImmutableMap.of(
            "target", "application",
            "methods", Arrays.asList(ImmutableMap.of(
                "name", "raiseToast",
                "args", Arrays.asList(ImmutableMap.of(
                    "value", "Hello from the test script!",
                    "type", "String"
                ))
            ))
        );

        driver.executeScript("mobile: backdoor", scriptArgs);
        try { Thread.sleep(2000); } catch (Exception ign) {} // pause to allow visual verification
    }

}
