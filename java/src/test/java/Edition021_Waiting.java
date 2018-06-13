import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@RunWith(JUnit4.class)
public class Edition021_Waiting {

    private String APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.2.1/TheApp-v1.2.1.apk";

    private String AUTH_USER = "alice";
    private String AUTH_PASS = "mypassword";

    private By loginScreen = MobileBy.AccessibilityId("Login Screen");
    private By loginBtn = MobileBy.AccessibilityId("loginBtn");
    private By username = MobileBy.AccessibilityId("username");
    private By password = MobileBy.AccessibilityId("password");

    private AndroidDriver driver;

    private By getLoggedInBy(String username) {
        return By.xpath("//android.widget.TextView[@text=\"You are logged in as " + username + "\"]");
    }

    @Before
    public void setUp() throws IOException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("deviceName", "Android Emulator");
        caps.setCapability("app", APP);
        caps.setCapability("automationName", "UiAutomator2");
        driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), caps);
    }

    @After
    public void tearDown() {
        try {
            driver.quit();
        } catch (Exception ign) {}
    }

    @Test
    public void testLogin_StaticWait() throws InterruptedException {
        Thread.sleep(3000);
        driver.findElement(loginScreen).click();

        Thread.sleep(3000);
        driver.findElement(username).sendKeys(AUTH_USER);
        driver.findElement(password).sendKeys(AUTH_PASS);
        driver.findElement(loginBtn).click();

        Thread.sleep(3000);
        driver.findElement(getLoggedInBy(AUTH_USER));
    }

    @Test
    public void testLogin_ImplicitWait() {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        driver.findElement(loginScreen).click();
        driver.findElement(username).sendKeys(AUTH_USER);
        driver.findElement(password).sendKeys(AUTH_PASS);
        driver.findElement(loginBtn).click();
        driver.findElement(getLoggedInBy(AUTH_USER));
    }

    @Test
    public void testLogin_ExplicitWait() {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        wait.until(ExpectedConditions.presenceOfElementLocated(loginScreen)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(username)).sendKeys(AUTH_USER);
        wait.until(ExpectedConditions.presenceOfElementLocated(password)).sendKeys(AUTH_PASS);
        wait.until(ExpectedConditions.presenceOfElementLocated(loginBtn)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(getLoggedInBy(AUTH_USER)));
    }

    @Test
    public void testLogin_CustomWait() {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        wait.until(elementFoundAndClicked(loginScreen));
        wait.until(ExpectedConditions.presenceOfElementLocated(username)).sendKeys(AUTH_USER);
        wait.until(ExpectedConditions.presenceOfElementLocated(password)).sendKeys(AUTH_PASS);
        wait.until(elementFoundAndClicked(loginBtn));
        wait.until(ExpectedConditions.presenceOfElementLocated(getLoggedInBy(AUTH_USER)));
    }

    private ExpectedCondition<Boolean> elementFoundAndClicked(By locator) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                WebElement el = driver.findElement(locator);
                el.click();
                return true;
            }
        };
    }
}
