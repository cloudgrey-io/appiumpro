import io.appium.java_client.MobileBy;
import io.appium.java_client.ios.IOSDriver;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Edition088_Debugging_Aids {

    private String APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.10.0/TheApp-v1.10.0.app.zip";

    private By loginScreen = MobileBy.AccessibilityId("Login Screen");
    private By loginBtn = MobileBy.AccessibilityId("loginBtn");
    private By username = MobileBy.AccessibilityId("username");
    private By password = MobileBy.AccessibilityId("password");
    private By loggedIn = MobileBy.xpath("//*[@label='You are logged in as alice']");

    private IOSDriver driver;

    @Rule
    public DebugWatcher debugWatcher = new DebugWatcher();

    @Before
    public void setUp() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "12.4");
        capabilities.setCapability("deviceName", "iPhone X");
        capabilities.setCapability("app", APP);

        driver = new IOSDriver<>(new URL("http://localhost:4723/wd/hub"), capabilities);
    }

    @Test
    public void testLogin() {
        WebDriverWait wait = new WebDriverWait(driver, 5);

        wait.until(ExpectedConditions.presenceOfElementLocated(loginScreen)).click();
        String AUTH_USER = "alice";
        wait.until(ExpectedConditions.presenceOfElementLocated(username)).sendKeys(AUTH_USER);
        String AUTH_PASS = "wrongpassword";
        wait.until(ExpectedConditions.presenceOfElementLocated(password)).sendKeys(AUTH_PASS);
        wait.until(ExpectedConditions.presenceOfElementLocated(loginBtn)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(loggedIn));
    }

    public class DebugWatcher extends TestWatcher {
        private static final String SCREEN_DIR = "/tmp";

        @Override
        protected void failed(Throwable e, Description desc) {
            // print appium logs
            LogEntries entries = driver.manage().logs().get("server");
            System.out.println("======== APPIUM SERVER LOGS ========");
            for (LogEntry entry : entries) {
                System.out.println(new Date(entry.getTimestamp()) + " " + entry.getMessage());
            }
            System.out.println("================");

            // print source
            System.out.println("======== APP SOURCE ========");
            System.out.println(driver.getPageSource());
            System.out.println("================");

            // save screenshot
            String testName = desc.getMethodName().replaceAll("[^a-zA-Z0-9-_\\.]", "_");
            File screenData = driver.getScreenshotAs(OutputType.FILE);
            try {
                File screenFile = new File(SCREEN_DIR + "/" + testName + ".png");
                FileUtils.copyFile(screenData, screenFile);
                System.out.println("======== SCREENSHOT ========");
                System.out.println(screenFile.getAbsolutePath());
                System.out.println("================");
            } catch (IOException ign) {}
        }

        @Override
        protected void finished(Description desc) {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
