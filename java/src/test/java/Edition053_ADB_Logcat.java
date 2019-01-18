import io.appium.java_client.AppiumDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.net.URL;
import java.util.Set;
import java.util.stream.StreamSupport;

public class Edition053_ADB_Logcat {

    private String APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.8.1/TheApp-v1.8.1.apk";

    private AppiumDriver driver;

    @Before
    public void setUp() throws IOException {
        DesiredCapabilities caps = new DesiredCapabilities();

        caps.setCapability("platformName", "Android");
        caps.setCapability("deviceName", "Android Emulator");
        caps.setCapability("automationName", "UiAutomator2");
        caps.setCapability("app", APP);

        driver = new AppiumDriver(new URL("http://localhost:4723/wd/hub"), caps);
    }

    @After
    public void tearDown() {
        try {
            driver.quit();
        } catch (Exception ign) {}
    }

    @Test
    public void captureLogcat() {
        // inspect available log types
        Set<String> logtypes = driver.manage().logs().getAvailableLogTypes();
        System.out.println("suported log types: " + logtypes.toString()); // [logcat, bugreport, server, client]

        // print first and last 10 lines of logs
        LogEntries logs = driver.manage().logs().get("logcat");
        System.out.println("First and last ten lines of log: ");
        StreamSupport.stream(logs.spliterator(), false).limit(10).forEach(System.out::println);
        System.out.println("...");
        StreamSupport.stream(logs.spliterator(), false).skip(logs.getAll().size() - 10).forEach(System.out::println);

        // wait for more logs
        try { Thread.sleep(5000); } catch (Exception ign) {} // pause to allow visual verification

        // demonstrate that each time get logs, we only get new logs
        // which were generated since the last time we got logs
        LogEntries secondCallToLogs = driver.manage().logs().get("logcat");
        System.out.println("\nFirst ten lines of next log call: ");
        StreamSupport.stream(secondCallToLogs.spliterator(), false).limit(10).forEach(System.out::println);

        Assert.assertNotEquals(logs.iterator().next(), secondCallToLogs.iterator().next());
    }
}
