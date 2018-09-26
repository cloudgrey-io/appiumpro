import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Edition036_NativeWebTap {

    private static By link = By.id("noClickLink");

    // This test will fail without nativeWebTap
    @Test
    public void testClickLink_iOS_withoutNativeWebTap() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "11.4");
        capabilities.setCapability("deviceName", "iPhone 8");
        capabilities.setCapability("browserName", "Safari");

        IOSDriver driver = new IOSDriver<>(new URL("http://localhost:4723/wd/hub"), capabilities);
        actualTest(driver);
    }

    // This test will pass with nativeWebTap
    @Test
    public void testClickLink_iOS_withNativeWebTap() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "11.4");
        capabilities.setCapability("deviceName", "iPhone 8");
        capabilities.setCapability("browserName", "Safari");
        capabilities.setCapability("nativeWebTap", true);

        IOSDriver driver = new IOSDriver<>(new URL("http://localhost:4723/wd/hub"), capabilities);
        actualTest(driver);
    }

    public void actualTest(AppiumDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        try {
            driver.get("https://appiumpro.com/test");
            // click the link
            wait.until(ExpectedConditions.presenceOfElementLocated(link)).click();

            // assert we navigated as expected
            wait.until(ExpectedConditions.urlToBe("https://appiumpro.com/"));
        } finally {
            driver.quit();
        }
    }
}
