import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Edition018_Espresso_Beta {

    private String APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.5.0/TheApp-v1.5.0.apk";
    private By loginScreen = MobileBy.AccessibilityId("Login Screen");
    private By username = MobileBy.AccessibilityId("username");
    private By password = MobileBy.AccessibilityId("password");
    private By loginBtn = MobileBy.AccessibilityId("loginBtn");
    private By verificationTextEspresso = By.xpath(
        "//com.facebook.react.views.text.ReactTextView[@text='You are logged in as alice']");
    private By verificationTextUiAuto2 = By.xpath(
        "//android.widget.TextView[contains(@text, 'alice')]");

    private AndroidDriver getDriver(String automationName) throws MalformedURLException {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Android Emulator");
        capabilities.setCapability("automationName", automationName);
        capabilities.setCapability("app", APP);

        return new AndroidDriver<>(new URL("http://localhost:4723/wd/hub"), capabilities);
    }

    @Test
    public void testLogin_Espresso() throws MalformedURLException {
        AndroidDriver driver = getDriver("Espresso");
        WebDriverWait wait = new WebDriverWait(driver, 10);
        ExpectedCondition<WebElement> loginScreenReady =
            ExpectedConditions.presenceOfElementLocated(loginScreen);

        try {
            wait.until(loginScreenReady).click();
            driver.findElement(username).sendKeys("alice");
            driver.findElement(password).sendKeys("mypassword");
            driver.findElement(loginBtn).click();
            driver.findElement(verificationTextEspresso);
        } finally {
            driver.quit();
        }
    }

    @Test
    public void testLogin_UiAutomator2() throws MalformedURLException {
        AndroidDriver driver = getDriver("UiAutomator2");
        WebDriverWait wait = new WebDriverWait(driver, 10);
        ExpectedCondition<WebElement> loginScreenReady =
            ExpectedConditions.presenceOfElementLocated(loginScreen);
        ExpectedCondition<WebElement> usernameReady =
            ExpectedConditions.presenceOfElementLocated(username);
        ExpectedCondition<WebElement> verificationReady =
            ExpectedConditions.presenceOfElementLocated(verificationTextUiAuto2);

        try {
            wait.until(loginScreenReady).click();
            wait.until(usernameReady).sendKeys("alice");
            driver.findElement(password).sendKeys("mypassword");
            driver.findElement(loginBtn).click();
            wait.until(verificationReady);
        } finally {
            driver.quit();
        }

    }
}
