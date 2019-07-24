import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.net.URL;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

public class Edition078_Attaching_To_Existing_Session {
    private String APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.9.0/TheApp-v1.9.0.app.zip";
    private IOSDriver driver;
    private static Boolean firstTest = true;

    private String sessionId;

    @Before
    public void setUp() throws IOException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "iOS");
        caps.setCapability("platformVersion", "12.2");
        caps.setCapability("deviceName", "iPhone Xs");
        caps.setCapability("automationName", "XCUITest");

        caps.setCapability("app", APP);

        driver = new IOSDriver<MobileElement>(new URL("http://localhost:4723/wd/hub"), caps);
        sessionId = driver.getSessionId().toString();
    }

    @After
    public void tearDown() {
        firstTest = false;
        try {
            driver.quit();
        } catch (Exception ign) {}
    }

    @Test
    public void testAttachingToSession() {
        // This constructor has not yet been published, ongoing work is here: https://github.com/appium/java-client/pull/1181
//        IOSDriver newDriver = new AppiumDriver("http://localhost:4723/wd/hub" ,sessionId);
//
//        // new driver is equivalent to the original driver
//        String time = newDriver.getDeviceTime();
//        assertFalse(time.isEmpty());
//
//        // the original driver object still works fine
//        String laterTime = driver.getDeviceTime();
//        assertNotEquals(time, laterTime);
    }
}