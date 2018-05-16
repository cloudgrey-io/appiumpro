import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.annotation.Nullable;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Edition017_Hybrid_Apps {

    private String APP_IOS = "https://github.com/cloudgrey-io/the-app/releases/download/v1.5.0/TheApp-v1.5.0.app.zip";
    private String APP_ANDROID = "https://github.com/cloudgrey-io/the-app/releases/download/v1.5.0/TheApp-v1.5.0.apk";


    private static By hybridScreen = MobileBy.AccessibilityId("Webview Demo");
    private static By urlInput = MobileBy.AccessibilityId("urlInput");
    private static By navigateBtn = MobileBy.AccessibilityId("navigateBtn");
    private static By clearBtn = MobileBy.AccessibilityId("clearBtn");

    @Test
    public void testAppiumProSite_iOS() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "11.3");
        capabilities.setCapability("deviceName", "iPhone 6");
        capabilities.setCapability("app", APP_IOS);

        IOSDriver driver = new IOSDriver<>(new URL("http://localhost:4723/wd/hub"), capabilities);
        actualTest(driver);
    }

    @Test
    public void testAppiumProSite_Android() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Android Emulator");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("app", APP_ANDROID);

        AndroidDriver driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
        actualTest(driver);
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

    public void actualTest(AppiumDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        final String title = "Appium Pro: The Awesome Appium Tips Newsletter";

        try {
            wait
                .until(ExpectedConditions.presenceOfElementLocated(hybridScreen))
                .click();

            MobileElement input = (MobileElement) wait
                .until(ExpectedConditions.presenceOfElementLocated(urlInput));

            // Get into the webview and assert that we're not yet at the correct page
            String webContext = getWebContext(driver);
            driver.context(webContext);
            Assert.assertNotEquals(driver.getTitle(), title);

            // Go back into the native context and automate the URL button
            driver.context("NATIVE_APP");
            input.sendKeys("https://google.com");
            WebElement navigate = driver.findElement(navigateBtn);
            navigate.click();

            // Assert that going to Google is not allowed
            Thread.sleep(1000); // cheap way to ensure alert has time to show
            driver.switchTo().alert().accept();

            // Now try to go to Appium Pro
            driver.findElement(clearBtn).click();
            input.sendKeys("https://appiumpro.com");
            navigate.click();

            // Go back into the webview and assert that the title is correct
            driver.context(webContext);
            wait.until(ExpectedConditions.titleIs(title));
        } catch (InterruptedException ign) {
        } finally {
            driver.quit();
        }

    }
}
