import com.google.common.collect.ImmutableMap;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static io.appium.java_client.remote.MobileCapabilityType.APP;

public class Edition080_iOS_FaceId {
    // App provided by https://github.com/zaimramlan/iOSBiometricLogin
    File classpathRoot = new File(System.getProperty("user.dir"));
    File appDir = new File(classpathRoot, "../apps/");
    String app = new File(appDir.getCanonicalPath(), "BiometricLogin.app").getAbsolutePath();
    private IOSDriver driver;

    public Edition080_iOS_FaceId() throws IOException {
    }

    @Before
    public void setUp() throws IOException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "iOS");
        caps.setCapability("platformVersion", "12.4");
        caps.setCapability("deviceName", "iPhone Xs");
        caps.setCapability("automationName", "XCUITest");

        caps.setCapability("app", app);

        driver = new IOSDriver<MobileElement>(new URL("http://localhost:4723/wd/hub"), caps);
    }

    @After
    public void tearDown() {
        try {
            driver.quit();
        } catch (Exception ign) {}
    }

    @Test
    public void testIOSFaceId() {
        driver.executeScript("mobile:enrollBiometric", ImmutableMap.of("isEnabled", true));
        WebElement loginButton = driver.findElementByAccessibilityId("Log In");
        loginButton.click();

        driver.switchTo().alert().accept();

        driver.executeScript("mobile:sendBiometricMatch", ImmutableMap.of("type", "faceId", "match", true));

        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Log Out")));

        WebElement logoutButton = driver.findElementByAccessibilityId("Log Out");
        logoutButton.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Log In")));

        loginButton = driver.findElementByAccessibilityId("Log In");
        loginButton.click();

        driver.executeScript("mobile:sendBiometricMatch", ImmutableMap.of("type", "faceId", "match", false));

        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().dismiss();
    }
}