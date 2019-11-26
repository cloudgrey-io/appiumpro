import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.serverevents.CommandEvent;
import io.appium.java_client.serverevents.CustomEvent;
import io.appium.java_client.serverevents.ServerEvents;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Edition097_Tracking_App_Launch_DeviceFarm {

    private By loginScreen = MobileBy.AccessibilityId("Login Screen");

    private AndroidDriver driver;

    @Before
    public void setUp() throws MalformedURLException {
        String app = System.getenv("DEVICEFARM_APP_PATH");
        if (app == null) {
            app = "https://github.com/cloudgrey-io/the-app/releases/download/v1.10.0/TheApp-v1.10.0.apk";
        }
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Android");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("app", app);
        capabilities.setCapability("appPackage", "io.cloudgrey.the_app");
        capabilities.setCapability("appActivity", ".MainActivity");
        capabilities.setCapability("appWaitActivity", "com.reactnativenavigation.controllers.NavigationActivity");
        capabilities.setCapability("autoGrantPermissions", true);
        capabilities.setCapability("skipUnlock", true);
        capabilities.setCapability("ignoreUnimportantViews", true);
        capabilities.setCapability("eventTimings", true);
        capabilities.setCapability("autoLaunch", false);

        driver = new AndroidDriver<>(new URL("http://localhost:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testAppLaunch() throws Exception {
        driver.launchApp();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(loginScreen));

        ServerEvents evts = driver.getEvents();
        List<CommandEvent> cmds = evts.getCommands();
        Optional<CommandEvent> launchAppCmd = cmds.stream()
            .filter((cmd) -> cmd.getName().equals("launchApp"))
            .findFirst();
        Optional<CommandEvent> findCmd = cmds.stream()
            .filter((cmd) -> cmd.getName().equals("findElement"))
            .findFirst();

        if (!launchAppCmd.isPresent() || !findCmd.isPresent()) {
            throw new Exception("Could not determine start or end time of app launch");
        }

        long elapsedMs = findCmd.get().endTimestamp - launchAppCmd.get().startTimestamp;

        System.out.println("The app took <" + (elapsedMs / 1000.0) + "s to launch");

        CustomEvent evt = new CustomEvent();
        evt.setVendor("theApp");
        evt.setEventName("mainScreenLoaded");
        driver.logEvent(evt);
    }
}
