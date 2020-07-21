import java.net.MalformedURLException;
import java.net.URL;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;

public class Edition120_Android_Accelerometer {
    protected AndroidDriver<WebElement> driver;
    protected WebDriverWait wait;

    @Before
    public void setUp() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("deviceName", "Android");
        caps.setCapability("app", "/Users/jlipps/Code/testapps/vibration.apk");
        caps.setCapability("automationName", "UiAutomator2");
        driver = new AndroidDriver<>(new URL("http://localhost:4723/wd/hub"), caps);
        wait = new WebDriverWait(driver, 10);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected WebElement waitFor(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    @Test
    public void testAccelerometer() throws Exception {
        waitFor(By.xpath("//android.widget.Button[@text='CONTI']")).click();
        int numIterations = 10;
        int multiplier = 5;
        for (int i = 0; i <= numIterations; i++) {
            Double val = Math.sin((Math.PI / numIterations) * i) * multiplier;
            String command = "sensor set acceleration " + val + ":" + val + ":" + val;
            driver.executeScript("mobile: execEmuConsoleCommand", ImmutableMap.of("command", command));
            Thread.sleep(400);
        }
        Thread.sleep(5000);
    }
}
