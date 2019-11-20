import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Edition096_Cookies {

    private String APPIUM = "http://localhost:4723/wd/hub";
    private String COOKIE_NAME = "PHPSESSID";
    private String USERNAME = System.getenv("EXPENSEUS_USERNAME");
    private String PASSWORD = System.getenv("EXPENSEUS_PASSWORD");

    private AppiumDriver driver;
    private static Cookie loginCookie = null;

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testLogin_IOS() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "11.4");
        capabilities.setCapability("deviceName", "iPhone 8");
        capabilities.setCapability("automationName", "XCUITest");
        capabilities.setCapability("browserName", "Safari");

        driver = new IOSDriver<>(new URL(APPIUM), capabilities);
        loginTest();
    }

    @Test
    public void testLogin_Android() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Android Emulator");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("browserName", "Chrome");

        driver = new AndroidDriver(new URL(APPIUM), capabilities);
        loginTest();
    }

    private void loginTest() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        driver.get("http://expenseus.com/user/login");
        WebElement username = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//input[@name='user[email]']")));
        if (loginCookie != null) {
            System.out.println("Using cookie");
            driver.manage().deleteCookieNamed(COOKIE_NAME);
            driver.manage().addCookie(loginCookie);
            driver.get("http://expenseus.com");
        } else {
            System.out.println("No cookie, logging in via form");
            username.sendKeys(USERNAME);
            driver.findElement(By.xpath("//input[@name='user[password]']")).sendKeys(PASSWORD);
            driver.findElement(By.xpath("//input[@value='Log in']")).click();
            loginCookie = driver.manage().getCookieNamed(COOKIE_NAME);
            System.out.println(loginCookie.getName());
            System.out.println(loginCookie.getValue());
        }
        wait.until(ExpectedConditions.urlContains("dashboard"));
    }
}
