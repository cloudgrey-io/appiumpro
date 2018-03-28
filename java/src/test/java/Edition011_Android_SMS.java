import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import java.io.IOException;
import java.net.URL;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@RunWith(JUnit4.class)
public class Edition011_Android_SMS {

    private String APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.3.0/TheApp-v1.3.0.apk";
    private String AVD_NAME = "emu27";

    private By verifyScreen = MobileBy.AccessibilityId("Verify Phone Number");
    private By waiting = MobileBy.xpath("//android.widget.TextView[contains(@text, 'Waiting to receive')]");
    private By verified = MobileBy.xpath("//android.widget.TextView[contains(@text, 'verified')]");


    @Test
    public void testSMS () throws IOException {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Android Emulator");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("avd", AVD_NAME);
        capabilities.setCapability("app", APP);

         // Open the app.
        AndroidDriver driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
        WebDriverWait wait = new WebDriverWait(driver, 5);

        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(verifyScreen)).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(waiting));

            // first test an incorrect code and ensure that the waiting message is still present
            driver.sendSMS("555-555-5555", "Your code is 654321");
            driver.findElement(waiting);

            // now test the correct code and assert that the verification message is present
            driver.sendSMS("555-555-5555", "Your code is 123456");
            wait.until(ExpectedConditions.presenceOfElementLocated(verified));
        } finally {
            driver.quit();
        }
    }
}
