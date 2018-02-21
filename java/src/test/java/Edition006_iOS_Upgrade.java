import io.appium.java_client.MobileBy;
import io.appium.java_client.ios.IOSDriver;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@RunWith(JUnit4.class)
public class Edition006_iOS_Upgrade {

    private String BUNDLE_ID = "io.cloudgrey.the-app";

    private String APP_V1_0_0 = "https://github.com/cloudgrey-io/the-app/releases/download/v1.0.0/TheApp-v1.0.0.app.zip";
    private String APP_V1_0_1 = "https://github.com/cloudgrey-io/the-app/releases/download/v1.0.1/TheApp-v1.0.1.app.zip";
    private String APP_V1_0_2 = "https://github.com/cloudgrey-io/the-app/releases/download/v1.0.2/TheApp-v1.0.2.app.zip";

    private By msgInput = By.xpath("//XCUIElementTypeTextField[@name=\"messageInput\"]");
    private By savedMsg = MobileBy.AccessibilityId("savedMessage");
    private By saveMsgBtn = MobileBy.AccessibilityId("messageSaveBtn");
    private By echoBox = MobileBy.AccessibilityId("Echo Box");

    private String TEST_MESSAGE = "Hello World";

    @Test
    public void testSavedTextAfterUpgrade () throws IOException {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("deviceName", "iPhone 7");
        capabilities.setCapability("platformVersion", "11.2");
        capabilities.setCapability("app", APP_V1_0_0);

        // change this to APP_V1_0_1 to experience a failing scenario
        String appUpgradeVersion = APP_V1_0_2;

        // Open the app.
        IOSDriver driver = new IOSDriver<>(new URL("http://localhost:4723/wd/hub"), capabilities);

        WebDriverWait wait = new WebDriverWait(driver, 10);

        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(echoBox)).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(msgInput)).sendKeys(TEST_MESSAGE);
            wait.until(ExpectedConditions.presenceOfElementLocated(saveMsgBtn)).click();
            String savedText = wait.until(ExpectedConditions.presenceOfElementLocated(savedMsg)).getText();
            Assert.assertEquals(savedText, TEST_MESSAGE);

            HashMap<String, String> bundleArgs = new HashMap<>();
            bundleArgs.put("bundleId", BUNDLE_ID);
            driver.executeScript("mobile: terminateApp", bundleArgs);

            HashMap<String, String> installArgs = new HashMap<>();
            installArgs.put("app", appUpgradeVersion);
            driver.executeScript("mobile: installApp", installArgs);

            driver.executeScript("mobile: launchApp", bundleArgs);

            wait.until(ExpectedConditions.presenceOfElementLocated(echoBox)).click();
            savedText = wait.until(ExpectedConditions.presenceOfElementLocated(savedMsg)).getText();
            Assert.assertEquals(savedText, TEST_MESSAGE);
        } finally {
            driver.quit();
        }
    }
}
