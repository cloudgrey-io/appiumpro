import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import java.net.MalformedURLException;
import java.net.URL;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Edition004_Web_Testing {

    private static By EMAIL = By.id("contactEmail");
    private static By MESSAGE = By.id("contactText");
    private static By SEND = By.cssSelector("input[type=submit]");
    private static By ERROR = By.cssSelector(".contactResponse");

    @Test
    public void testAppiumProSite_iOS() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "11.2");
        capabilities.setCapability("deviceName", "iPhone 7");
        capabilities.setCapability("browserName", "Safari");

        // Open up Safari
        IOSDriver driver = new IOSDriver<>(new URL("http://localhost:4723/wd/hub"), capabilities);
        actualTest(driver);
    }

    @Test
    public void testAppiumProSite_Android() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Android Emulator");
        capabilities.setCapability("browserName", "Chrome");

        // Open up Safari
        AndroidDriver driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
        actualTest(driver);
    }

    public void actualTest(AppiumDriver driver) {
        // Set up default wait
        WebDriverWait wait = new WebDriverWait(driver, 10);

        try {
            driver.get("http://appiumpro.com/contact");
            wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL))
                .sendKeys("foo@foo.com");
            driver.findElement(MESSAGE).sendKeys("Hello!");
            driver.findElement(SEND).click();
            String response = wait.until(ExpectedConditions.visibilityOfElementLocated(ERROR)).getText();

            // validate that we get an error message involving a captcha, which we didn't fill out
            Assert.assertThat(response, CoreMatchers.containsString("Captcha"));
        } finally {
            driver.quit();
        }

    }
}
