import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static junit.framework.TestCase.assertTrue;

public class Edition061_FullContextList {

    private AppiumDriver driver;
    private String IOS_APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.9.0/TheApp-v1.9.0.app.zip";
    private static By hybridScreen = MobileBy.AccessibilityId("Webview Demo");
    private static By urlInput = MobileBy.AccessibilityId("urlInput");
    private static By goButton = MobileBy.AccessibilityId("navigateBtn");

    private class Context {
        public String id;
        public String url;
        public String title;
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void useFullContextList() throws MalformedURLException, InterruptedException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "iOS");
        caps.setCapability("platformVersion", "12.1");
        caps.setCapability("deviceName", "iPhone XS");
        caps.setCapability("automationName", "XCUITest");
        caps.setCapability("app", IOS_APP);
        caps.setCapability("fullContextList", true);


        driver = new AppiumDriver(new URL("http://localhost:4723/wd/hub"), caps);
        WebDriverWait wait = new WebDriverWait(driver, 10);

        // get to webview screen
        wait.until(ExpectedConditions.presenceOfElementLocated(hybridScreen)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(urlInput)).sendKeys("https://appiumpro.com");
        driver.findElement(goButton).click();

        Optional<Map<String, Object>> appiumProWebview;
        do {
            Thread.sleep(100);
            // get full list of contexts
            Set<Map<String, Object>> contexts = driver.getContextHandles();
            appiumProWebview = contexts.stream()
                    .filter(c -> !c.get("id").equals("NATIVE_APP"))
                    .filter(c -> c.get("url").equals("https://appiumpro.com/"))
                    .findFirst();
        } while (!appiumProWebview.isPresent());


        driver.context(appiumProWebview.get().get("id").toString());

        // now we know we're in the webview context which was pointing to "appiumpro.com"
        assertTrue(driver.getTitle().equals("Appium Pro: The Awesome Appium Tips Newsletter"));
    }
}
