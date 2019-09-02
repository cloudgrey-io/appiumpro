import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

@RunWith(JUnit4.class)
public class Edition085_Execute_Driver_Script {

    private AndroidDriver driver;
    private String APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.9.0/TheApp-v1.9.0.apk";

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Android Emulator");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("app", APP);

        driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testLoginNormally() {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.findElement(MobileBy.AccessibilityId("Login Screen")).click();
        driver.findElement(MobileBy.AccessibilityId("username")).sendKeys("alice");
        driver.findElement(MobileBy.AccessibilityId("password")).sendKeys("mypassword");
        driver.findElement(MobileBy.AccessibilityId("loginBtn")).click();
        driver.findElement(By.xpath("//*[@text='Logout']")).click();
    }

    @Test
    public void testLoginWithExecute() {
        driver.executeDriverScript(
            "await driver.setImplicitTimeout(10000);\n" +
            "await (await driver.$('~Login Screen')).click();\n" +
            "await (await driver.$('~username')).setValue('alice');\n" +
            "await (await driver.$('~password')).setValue('mypassword');\n" +
            "await (await driver.$('~loginBtn')).click();\n" +
            "await (await driver.$('//*[@text=\"Logout\"]')).click();\n"
        );
    }
}
