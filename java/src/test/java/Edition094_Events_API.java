import io.appium.java_client.MobileBy;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.serverevents.ServerEvents;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Edition094_Events_API {

    private String APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.10.0/TheApp-v1.10.0.app.zip";

    private By loginScreen = MobileBy.AccessibilityId("Login Screen");
    private By loginBtn = MobileBy.AccessibilityId("loginBtn");
    private By username = MobileBy.AccessibilityId("username");
    private By password = MobileBy.AccessibilityId("password");
    private By loggedIn = MobileBy.xpath("//*[@label='You are logged in as alice']");

    private IOSDriver driver;

    @Before
    public void setUp() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "11.4");
        capabilities.setCapability("deviceName", "iPhone X");
        capabilities.setCapability("app", APP);

        driver = new IOSDriver<>(new URL("http://localhost:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown() throws IOException {
        if (driver != null) {
            driver.logEvent("theapp", "testEnd");
            ServerEvents events = driver.getEvents();
            events.save(new File("/Users/jlipps/Desktop/java.json").toPath());
            driver.quit();
        }
    }

    @Test
    public void testLogin() {
        WebDriverWait wait = new WebDriverWait(driver, 5);

        wait.until(ExpectedConditions.presenceOfElementLocated(loginScreen)).click();
        driver.logEvent("theapp", "onLoginScreen");
        String AUTH_USER = "alice";
        wait.until(ExpectedConditions.presenceOfElementLocated(username)).sendKeys(AUTH_USER);
        String AUTH_PASS = "wrongpassword";
        wait.until(ExpectedConditions.presenceOfElementLocated(password)).sendKeys(AUTH_PASS);
        wait.until(ExpectedConditions.presenceOfElementLocated(loginBtn)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(loggedIn));
        driver.logEvent("theapp", "loggedIn");
    }
}
