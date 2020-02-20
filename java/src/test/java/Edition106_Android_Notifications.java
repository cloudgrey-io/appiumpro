import java.net.URL;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.AndroidDriver;

public class Edition106_Android_Notifications {
    private String APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.10.0/TheApp-v1.10.0.apk";
    private AndroidDriver driver;

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
    @SuppressWarnings("unchecked")
    public void testNotifications() {
        Map<String, Object> res = (Map<String, Object>)driver.executeScript("mobile: getNotifications");
        List<Map<String, Object>> notifications = (List<Map<String, Object>>)res.get("statusBarNotifications");
        for (Map<String, Object> notification : notifications) {
            Map<String, String> innerNotification = (Map<String, String>)notification.get("notification");
            if (innerNotification.get("bigTitle") != null) {
                System.out.println(innerNotification.get("bigTitle"));
            } else {
                System.out.println(innerNotification.get("title"));
            }
            if (innerNotification.get("bigText") != null) {
                System.out.println(innerNotification.get("bigText"));
            } else {
                System.out.println(innerNotification.get("text"));
            }
        }
    }
}
