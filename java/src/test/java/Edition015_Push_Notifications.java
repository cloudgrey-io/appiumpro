import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import java.net.URL;
import java.time.Duration;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Edition015_Push_Notifications {

    // these values all need to be replaced with your own app which is signed for a real device,
    // and a WDA_BUNDLE_ID which works with your wildcard app id
    private String BUNDLE_ID = "com.company.yourapp";
    private String WDA_BUNDLE_ID = "com.company.webdriveragent";
    private String APP = "/path/to/yourapp.ipa";
    private String XCODE_ORG_ID = "ABCDEFG123";

    private AppiumDriver driver;
    private Dimension screenSize;

    @Test
    public void testPushNotifications() throws Exception {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "11.2");
        capabilities.setCapability("deviceName", "iPhone 6s");
        capabilities.setCapability("app", APP);
        capabilities.setCapability("udid", "auto");
        capabilities.setCapability("xcodeOrgId", XCODE_ORG_ID);
        capabilities.setCapability("xcodeSigningId", "iPhone Developer");
        capabilities.setCapability("updatedWDABundleId", WDA_BUNDLE_ID);

        driver = new IOSDriver(new URL("http://localhost:4723/wd/hub"), capabilities);

        try {
            // get screen size so we can swipe correctly
            screenSize = driver.manage().window().getSize();

            // close app and wait for push notification to arrive
            driver.terminateApp(BUNDLE_ID);

            // here is where your code should trigger the push notification and wait a bit

            // now pull down the notification shade, check for the message we're expecting, and
            // then close the shade again ('TWITTER' is just an example of an expected message)
            showNotifications();
            driver.findElement(By.xpath("//XCUIElementTypeCell[contains(@label, 'TWITTER')]"));
            hideNotifications();

            // finally, we can reactivate our app in order to verify that the message is present,
            // or whatever we need to do
            driver.activateApp(BUNDLE_ID);
        } finally {
            driver.quit();
        }
    }

    private void showNotifications() {
        manageNotifications(true);
    }

    private void hideNotifications() {
        manageNotifications(false);
    }

    private void manageNotifications(Boolean show) {
        int yMargin = 5;
        int xMid = screenSize.width / 2;
        PointOption top = PointOption.point(xMid, yMargin);
        PointOption bottom = PointOption.point(xMid, screenSize.height - yMargin);

        TouchAction action = new TouchAction(driver);
        if (show) {
            action.press(top);
        } else {
            action.press(bottom);
        }
        action.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)));
        if (show) {
            action.moveTo(bottom);
        } else {
            action.moveTo(top);
        }
        action.perform();
    }
}
