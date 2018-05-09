import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.clipboard.HasClipboard;
import io.appium.java_client.ios.IOSDriver;
import java.io.IOException;
import java.net.URL;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@RunWith(JUnit4.class)
public class Edition016_Clipboard {

    private String ANDROID_APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.4.0/TheApp-v1.4.0.apk";
    private String IOS_APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.4.0/TheApp-v1.4.0.app.zip";
    private String AVD_NAME = "emu27";
    private String APPIUM_SERVER = "http://localhost:4723/wd/hub";

    private By clipboardNav = MobileBy.AccessibilityId("Clipboard Demo");
    private By refreshClipboardBtn = MobileBy.AccessibilityId("refreshClipboardText");
    private By clipboardInput = MobileBy.AccessibilityId("messageInput");
    private By setTextBtn = MobileBy.AccessibilityId("setClipboardText");

    @Test
    public void testClipboard_Android() throws IOException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Android Emulator");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("avd", AVD_NAME);
        capabilities.setCapability("app", ANDROID_APP);

        AndroidDriver driver = new AndroidDriver(new URL(APPIUM_SERVER), capabilities);
        automateClipboard(driver);
    }

    @Test
    public void testClipboard_iOS() throws IOException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "11.3");
        capabilities.setCapability("deviceName", "iPhone 7");
        capabilities.setCapability("app", IOS_APP);

        IOSDriver driver = new IOSDriver(new URL(APPIUM_SERVER), capabilities);
        automateClipboard(driver);
    }

    private void automateClipboard(AppiumDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 5);

        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(clipboardNav)).click();

            String text = "Hello World";
            ((HasClipboard) driver).setClipboardText(text);
            wait.until(ExpectedConditions.presenceOfElementLocated(refreshClipboardBtn)).click();
            By clipboardText = MobileBy.AccessibilityId(text);
            Assert.assertEquals(driver.findElement(clipboardText).getText(), text);

            text = "Hello World Again";
            driver.findElement(clipboardInput).sendKeys(text);
            try {
                driver.hideKeyboard();
            } catch (Exception ign) {}
            driver.findElement(setTextBtn).click();
            Assert.assertEquals(((HasClipboard) driver).getClipboardText(), text);

        } finally {
            driver.quit();
        }
    }
}
