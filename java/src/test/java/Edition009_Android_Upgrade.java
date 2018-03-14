import io.appium.java_client.MobileBy;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
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
public class Edition009_Android_Upgrade {

    private String APP_PKG = "io.cloudgrey.the_app";
    private String APP_ACT = "com.theapp.MainActivity";

    private String APP_V1_0_0 = "https://github.com/cloudgrey-io/the-app/releases/download/v1.0.0/TheApp-v1.0.0.apk";
    private String APP_V1_0_1 = "https://github.com/cloudgrey-io/the-app/releases/download/v1.0.1/TheApp-v1.0.1.apk";
    private String APP_V1_0_2 = "https://github.com/cloudgrey-io/the-app/releases/download/v1.0.2/TheApp-v1.0.2.apk";


    private String TEST_MESSAGE = "Hello World";

    private By msgInput = MobileBy.AccessibilityId("messageInput");
    private By savedMsg = MobileBy.AccessibilityId(TEST_MESSAGE);
    private By saveMsgBtn = MobileBy.AccessibilityId("messageSaveBtn");
    private By echoBox = MobileBy.AccessibilityId("Echo Box");

    @Test
    public void testSavedTextAfterUpgrade () throws IOException {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Android Emulator");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("app", APP_V1_0_0);

        // change this to APP_V1_0_1 to experience a failing scenario
        String appUpgradeVersion = APP_V1_0_2;

        // Open the app.
        AndroidDriver driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), capabilities);

        WebDriverWait wait = new WebDriverWait(driver, 10);

        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(echoBox)).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(msgInput)).sendKeys(TEST_MESSAGE);
            wait.until(ExpectedConditions.presenceOfElementLocated(saveMsgBtn)).click();
            String savedText = wait.until(ExpectedConditions.presenceOfElementLocated(savedMsg)).getText();
            Assert.assertEquals(savedText, TEST_MESSAGE);

            driver.installApp(appUpgradeVersion);
            Activity activity = new Activity(APP_PKG, APP_ACT);
            driver.startActivity(activity);

            wait.until(ExpectedConditions.presenceOfElementLocated(echoBox)).click();
            savedText = wait.until(ExpectedConditions.presenceOfElementLocated(savedMsg)).getText();
            Assert.assertEquals(savedText, TEST_MESSAGE);
        } finally {
            driver.quit();
        }
    }
}
