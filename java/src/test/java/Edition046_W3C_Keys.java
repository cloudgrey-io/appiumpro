import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.KeyInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Edition046_W3C_Keys {

    private String APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.8.0/TheApp-v1.8.0.apk";
    private By loginScreen = MobileBy.AccessibilityId("Login Screen");
    private By username = MobileBy.AccessibilityId("username");

    private AppiumDriver driver;
    private WebDriverWait wait;

    @Before
    public void setUp() throws IOException {
        DesiredCapabilities caps = new DesiredCapabilities();

        caps.setCapability("platformName", "Android");
        caps.setCapability("deviceName", "Android Emulator");
        caps.setCapability("automationName", "UiAutomator2");

        caps.setCapability("app", APP);
        driver = new AppiumDriver(new URL("http://localhost:4723/wd/hub"), caps);
        wait = new WebDriverWait(driver, 10);
    }

    @After
    public void tearDown() {
        try {
            driver.quit();
        } catch (Exception ign) {}
    }

    @Test
    public void testSendKeysAction() {
        wait.until(ExpectedConditions.presenceOfElementLocated(loginScreen)).click();
        WebElement usernameField = driver.findElement(username);
        usernameField.click();
        Actions a = new Actions(driver);
        a.sendKeys("foo");
        a.perform();
        Assert.assertEquals("foo", usernameField.getText());
    }

    @Test
    public void testLowLevelKeys() {
        wait.until(ExpectedConditions.presenceOfElementLocated(loginScreen)).click();
        WebElement usernameField = driver.findElement(username);
        usernameField.click();

        KeyInput keyboard = new KeyInput("keyboard");
        Sequence sendKeys = new Sequence(keyboard, 0);

        sendKeys.addAction(keyboard.createKeyDown(Keys.SHIFT.getCodePoint()));
        sendKeys.addAction(keyboard.createKeyDown("f".codePointAt(0)));
        sendKeys.addAction(keyboard.createKeyUp("f".codePointAt(0)));
        sendKeys.addAction(keyboard.createKeyUp(Keys.SHIFT.getCodePoint()));

        sendKeys.addAction(keyboard.createKeyDown("o".codePointAt(0)));
        sendKeys.addAction(keyboard.createKeyUp("o".codePointAt(0)));

        sendKeys.addAction(keyboard.createKeyDown("o".codePointAt(0)));
        sendKeys.addAction(keyboard.createKeyUp("o".codePointAt(0)));

        driver.perform(Arrays.asList(sendKeys));

        Assert.assertEquals("Foo", usernameField.getText());
    }

}
