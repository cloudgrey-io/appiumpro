import io.appium.java_client.MobileBy;
import io.appium.java_client.ios.IOSDriver;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Edition012_iOS_Performance {


    private String APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.3.0/TheApp-v1.3.0.app.zip";
    private File traceZip;

    private By msgInput = By.xpath("//XCUIElementTypeTextField[@name=\"messageInput\"]");
    private By savedMsg = MobileBy.AccessibilityId("savedMessage");
    private By saveMsgBtn = MobileBy.AccessibilityId("messageSaveBtn");
    private By echoBox = MobileBy.AccessibilityId("Echo Box");

    private String TEST_MESSAGE = "Hello World";

    @Test
    public void testAppActivity() throws Exception {

        // Note: Appium server must have been started with --relaxed-security

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "11.3");
        capabilities.setCapability("deviceName", "iPhone X");
        capabilities.setCapability("app", APP);

        IOSDriver driver = new IOSDriver<>(new URL("http://localhost:4723/wd/hub"), capabilities);

        // replace the path here with something that makes sense for your system
        traceZip = new File("/path/to/trace.zip");

        try {
            HashMap<String, Object> args = new HashMap<>();
            args.put("timeout", 60000);
            args.put("pid", "current");
            args.put("profileName", "Time Profiler");
            driver.executeScript("mobile: startPerfRecord", args);

            performActions(driver);
            performActions(driver);
            performActions(driver);

            args = new HashMap<>();
            args.put("profileName", "Time Profiler");
            String b64Zip = (String)driver.executeScript("mobile: stopPerfRecord", args);
            byte[] bytesZip = Base64.getMimeDecoder().decode(b64Zip);
            FileOutputStream stream = new FileOutputStream(traceZip);
            stream.write(bytesZip);
        } finally {
            driver.quit();
        }
    }

    public void performActions(IOSDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(echoBox)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(msgInput)).sendKeys(TEST_MESSAGE);
        wait.until(ExpectedConditions.presenceOfElementLocated(saveMsgBtn)).click();
        String savedText = wait.until(ExpectedConditions.presenceOfElementLocated(savedMsg)).getText();
        Assert.assertEquals(savedText, TEST_MESSAGE);
        driver.navigate().back();
    }
}
