import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.annotation.Nullable;
import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Edition038_Android_Web_Console {

    private AndroidDriver driver;
    private String APP_ANDROID = "https://github.com/cloudgrey-io/the-app/releases/download/v1.7.1/TheApp-v1.7.1.apk";
    private static By hybridScreen = MobileBy.AccessibilityId("Webview Demo");
    private static By urlInput = MobileBy.AccessibilityId("urlInput");

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Nullable
    private String getWebContext(AppiumDriver driver) {
        ArrayList<String> contexts = new ArrayList(driver.getContextHandles());
        for (String context : contexts) {
            if (!context.equals("NATIVE_APP")) {
                return context;
            }
        }
        return null;
    }

    @Test
    public void testLogging_Chrome() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("deviceName", "Android Emulator");
        capabilities.setCapability("browserName", "Chrome");
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.ALL);
        capabilities.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);

        driver = new AndroidDriver<>(new URL("http://localhost:4723/wd/hub"), capabilities);
        loggingRoutine(driver);
    }

    @Test
    public void testLogging_Hybrid() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("deviceName", "Android Emulator");
        capabilities.setCapability("app", APP_ANDROID);
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.ALL);
        capabilities.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);

        driver = new AndroidDriver<>(new URL("http://localhost:4723/wd/hub"), capabilities);
        WebDriverWait wait = new WebDriverWait(driver, 10);

        // get to webview screen and enter webview mode
        wait.until(ExpectedConditions.presenceOfElementLocated(hybridScreen)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(urlInput));
        driver.context(getWebContext(driver));

        // now we can run the same routine as for the browser
        loggingRoutine(driver);
    }

    public void loggingRoutine(AndroidDriver driver) {
        driver.get("https://appiumpro.com/test");
        driver.executeScript("window.onerror=console.error.bind(console)");
        driver.executeScript("console.log('foo.');");
        driver.executeScript("console.warn('bar?');");
        driver.findElementById("jsErrLink").click();

        for (LogEntry entry : driver.manage().logs().get("browser")) {
            System.out.println(entry.getMessage());
        }
    }

}
