import java.net.MalformedURLException;
import java.net.URL;

import com.google.common.collect.ImmutableMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.MobileBy;
import io.appium.java_client.ios.IOSDriver;

public class Edition108_iOS_Home_Screen_Actions {
    private IOSDriver<WebElement> driver;
    private final static String REMINDERS = "com.apple.reminders";
    private final static By REMINDER_ICON = By.xpath("//*[@name='Home screen icons']//*[@name='Reminders']");
    private final static By ADD_REMINDER = MobileBy.AccessibilityId("New in Reminders");
    private final static By LISTS = MobileBy.AccessibilityId("Lists");


    @Before
    public void setUp() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "13.3");
        capabilities.setCapability("deviceName", "iPhone 11");
        capabilities.setCapability("app", REMINDERS);
        driver = new IOSDriver<WebElement>(new URL("http://localhost:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testHomeActions() {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        // press home button twice to make sure we are on the page with reminders
        ImmutableMap<String, String> pressHome = ImmutableMap.of("name", "home");
        driver.executeScript("mobile: pressButton", pressHome);
        driver.executeScript("mobile: pressButton", pressHome);

        // find the reminders icon and long-press it
        WebElement homeIcon = wait.until(ExpectedConditions.presenceOfElementLocated(REMINDER_ICON));
        driver.executeScript("mobile: touchAndHold", ImmutableMap.of(
            "element", ((RemoteWebElement)homeIcon).getId(),
            "duration", 2.0
        ));

        // now find the home action using the appropriate accessibility id
        wait.until(ExpectedConditions.presenceOfElementLocated(ADD_REMINDER)).click();

        // prove we opened up the reminders app to the view where we can add a reminder
        wait.until(ExpectedConditions.presenceOfElementLocated(LISTS));
    }

}
