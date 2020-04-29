import java.net.MalformedURLException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;

public class Edition113_Automating_Zoom {
    protected AndroidDriver<WebElement> driver;
    protected WebDriverWait wait;
    protected static final String MEETING_ID = System.getenv("ZOOM_MEETING_ID");
    protected static final String MEETING_PW = System.getenv("ZOOM_MEETING_PW");

    protected By JOIN_MEETING_BUTTON = By.id("btnJoinConf");
    protected By MEETING_ID_FIELD = By.id("edtConfNumber");
    protected By ACTUALLY_JOIN_MEETING_BUTTON = By.id("btnJoin");
    protected By PASSWORD_FIELD = By.id("edtPassword");
    protected By PASSWORD_OK_BUTTON = By.id("button1");
    protected By LEAVE_BTN = By.id("btnLeave");

    @Before
    public void setUp() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("deviceName", "Android");
        caps.setCapability("appPackage", "us.zoom.videomeetings");
        caps.setCapability("appActivity", "com.zipow.videobox.LauncherActivity");
        caps.setCapability("automationName", "UiAutomator2");
        driver = new AndroidDriver<>(new URL("http://localhost:4723/wd/hub"), caps);
        wait = new WebDriverWait(driver, 10);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected WebElement waitFor(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    @Test
    public void testJoinMeeting() throws Exception {
        // navigate through the UI to join a meeting with correct meeting id and password
        waitFor(JOIN_MEETING_BUTTON).click();
        waitFor(MEETING_ID_FIELD).sendKeys(MEETING_ID);
        driver.findElement(ACTUALLY_JOIN_MEETING_BUTTON).click();
        waitFor(PASSWORD_FIELD).sendKeys(MEETING_PW);
        driver.findElement(PASSWORD_OK_BUTTON).click();

        // prove that we made it into the meeting by finding the 'leave' button
        waitFor(LEAVE_BTN);
    }
}
