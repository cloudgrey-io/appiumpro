import io.appium.java_client.MobileBy;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.serverevents.CommandEvent;
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
    private static final String APP_PKG = "io.cloudgrey.the_app";
    private static final String APP_ACT = ".MainActivity";
    private static final String APP_WAIT = "com.reactnativenavigation.controllers.NavigationActivity";


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
        capabilities.setCapability("appPackage", APP_PKG);
        capabilities.setCapability("appActivity", APP_ACT);
        capabilities.setCapability("appWaitActivity", APP_WAIT);
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
        WebDriverWait wait = new WebDriverWait(driver, 10);

        // launch the app and wait for an element to be interactable
        Activity act = new Activity(APP_PKG, APP_ACT);
        act.setAppWaitActivity(APP_WAIT);
        driver.startActivity(act);
        wait.until(ExpectedConditions.presenceOfElementLocated(loginScreen));


        // pull out the events
        ServerEvents evts = driver.getEvents();
        List<CommandEvent> cmds = evts.getCommands();
        Optional<CommandEvent> startActCmd = cmds.stream()
            .filter((cmd) -> cmd.getName().equals("startActivity"))
            .findFirst();
        Optional<CommandEvent> findCmd = cmds.stream()
            .filter((cmd) -> cmd.getName().equals("findElement"))
            .findFirst();

        if (!startActCmd.isPresent() || !findCmd.isPresent()) {
            throw new Exception("Could not determine start or end time of app launch");
        }

        long launchMs = startActCmd.get().endTimestamp - startActCmd.get().startTimestamp;
        long interactMs = findCmd.get().endTimestamp - startActCmd.get().startTimestamp;

        System.out.println("The app took total <" + (launchMs / 1000.0) + "s to launch " +
                           "and total <" + (interactMs / 1000.0) + "s to become interactable");
    }
}
