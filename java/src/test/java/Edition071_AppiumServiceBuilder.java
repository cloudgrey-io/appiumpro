import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.junit.*;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.HashMap;

import static junit.framework.TestCase.assertTrue;

public class Edition071_AppiumServiceBuilder {
    private String APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.9.0/TheApp-v1.9.0.app.zip";

    private AppiumDriver driver;
    private static AppiumDriverLocalService server;

    @BeforeClass
    public static void startAppiumServer() {
        AppiumServiceBuilder serviceBuilder = new AppiumServiceBuilder();
        // Use any port, in case the default 4723 is already taken (maybe by another Appium server)
        serviceBuilder.usingAnyFreePort();
        // Tell serviceBuilder where node is installed. Or set this path in an environment variable named NODE_PATH
        serviceBuilder.usingDriverExecutable(new File("/Users/jonahss/.nvm/versions/node/v12.1.0/bin/node"));
        // Tell serviceBuilder where Appium is installed. Or set this path in an environment variable named APPIUM_PATH
        serviceBuilder.withAppiumJS(new File("/Users/jonahss/.nvm/versions/node/v12.1.0/bin/appium"));
        // The XCUITest driver requires that a path to the Carthage binary is in the PATH variable. I have this set for my shell, but the Java process does not see it. It can be inserted here.
        HashMap<String, String> environment = new HashMap();
        environment.put("PATH", "/usr/local/bin:" + System.getenv("PATH"));
        serviceBuilder.withEnvironment(environment);

        server = AppiumDriverLocalService.buildService(serviceBuilder);
        server.start();
    }

    @Before
    public void startSession() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "iOS");
        caps.setCapability("platformVersion", "12.2");
        caps.setCapability("deviceName", "iPhone Xs");
        caps.setCapability("automationName", "XCUITest");

        caps.setCapability("app", APP);

        driver = new IOSDriver<MobileElement>(server.getUrl(), caps);
    }

    @After
    public void endSession() {
        try {
            driver.quit();
        } catch (Exception ign) {}
    }

    @AfterClass
    public static void stopAppiumServer() {
        server.stop();
    }

    @Test
    public void test() {
        // test code goes here
        assertTrue(true);
    }
}
