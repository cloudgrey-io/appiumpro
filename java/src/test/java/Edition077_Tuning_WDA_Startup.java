import com.google.common.collect.ImmutableMap;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.net.URL;

import static junit.framework.TestCase.assertEquals;

public class Edition077_Tuning_WDA_Startup {
    // private String APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.9.0/TheApp-v1.9.0.app.zip";
    // specifying local app instead of download saves 3.9 seconds per test
    private String APP = "/Users/jonahss/Workspace/TheApp-v1.9.0.app.zip";

    private IOSDriver driver;
    private static Boolean firstTest = true;

    @Before
    public void setUp() throws IOException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "iOS");
        caps.setCapability("platformVersion", "12.2");
        caps.setCapability("deviceName", "iPhone Xs");
        caps.setCapability("automationName", "XCUITest");

        caps.setCapability("app", APP);

        /* saves 2.9 seconds */ caps.setCapability("noReset", true);
        /* saves 0.4 seconds */ caps.setCapability("udid", "009D8025-28AB-4A1B-A7C8-85A9F6FDBE95");
        /* saves 0.1 seconds */ caps.setCapability("bundleId", "io.cloudgrey.the-app");
        /* saves 1.4 seconds */ caps.setCapability("derivedDataPath", "/Users/jonahss/Library/Developer/Xcode/DerivedData/WebDriverAgent-apridxpigtzdjdecthgzpygcmdkp");
        if (!firstTest) {
            /* saves 0.2 seconds */
            caps.setCapability("webDriverAgentUrl", "http://localhost:8100");
        }

        // total speedup per test: 4.7 seconds, which is %52.5

        driver = new IOSDriver<MobileElement>(new URL("http://localhost:4723/wd/hub"), caps);
    }

    @After
    public void tearDown() {
        firstTest = false;
        try {
            driver.quit();
        } catch (Exception ign) {}
    }

    @Test
    public void testA() {
        assertEquals(1,1);
    }

    @Test
    public void testB() {
        assertEquals(1,1);
    }

    @Test
    public void testC() {
        assertEquals(1,1);
    }

    @Test
    public void testD() {
        assertEquals(1,1);
    }

    @Test
    public void testE() {
        assertEquals(1,1);
    }

    @Test
    public void testF() {
        assertEquals(1,1);
    }
}