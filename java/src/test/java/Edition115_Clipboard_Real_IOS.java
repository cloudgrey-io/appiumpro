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

import io.appium.java_client.MobileBy;
import io.appium.java_client.ios.IOSDriver;

public class Edition115_Clipboard_Real_IOS {

    private IOSDriver<WebElement> driver;
    private WebDriverWait wait;
    private final static String NOTES_ID = "com.apple.mobilenotes";
    private final static String PHOTOS = "com.apple.mobileslideshow";

    private final static By NEW_NOTE = MobileBy.AccessibilityId("New note");
    private final static By NOTE_CONTENT = MobileBy.AccessibilityId("New note");
    private final static By PASTE = MobileBy.AccessibilityId("Paste");
    //private final static By LINK_IN_NOTE = By.xpath("//XCUIElementTypeTextView[@name='note']/XCUIElementTypeLink");


    @Before
    public void setUp() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "13.3");
        capabilities.setCapability("deviceName", "iPhone 11");
        capabilities.setCapability("app", PHOTOS);
        capabilities.setCapability("automationName", "XCUITest");
        driver = new IOSDriver<WebElement>(new URL("http://localhost:4723/wd/hub"), capabilities);
        wait = new WebDriverWait(driver, 10);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testGetClipboardContents() {
        driver.activateApp(NOTES_ID);
        wait.until(ExpectedConditions.presenceOfElementLocated(NEW_NOTE)).click();
        WebElement note = wait.until(ExpectedConditions.presenceOfElementLocated(NOTE_CONTENT));
        note.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(PASTE)).click();
        System.out.println("Clipboard text was: " + note.getText());
    }


}
