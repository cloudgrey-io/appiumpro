import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;


public class Edition066_Automating_iOS_System_Apps {

    private String IOS_APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.9.0/TheApp-v1.9.0.app.zip";

    private AppiumDriver driver;

    @After
    public void Quit() {
        driver.quit();
    }

    @Test
    public void launchSystemApp() throws MalformedURLException, InterruptedException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "iOS");
        caps.setCapability("platformVersion", "12.2");
        caps.setCapability("deviceName", "iPhone Xs");
        caps.setCapability("automationName", "XCUITest");
        caps.setCapability("app", IOS_APP);

        driver = new IOSDriver(new URL("http://0.0.0.0:4723/wd/hub"), caps);

        WebElement picker = driver.findElementByAccessibilityId("Picker Demo");
        picker.click();

        // open iMessage
        driver.activateApp("com.apple.MobileSMS");

        // do things with SMS here

        Thread.sleep(3000);

        // open Settings app
        driver.activateApp("com.apple.Preferences");

        // regular automation commands to change device settings here

        // go back to our app
        driver.activateApp("io.cloudgrey.the-app");

        Thread.sleep(3000);
    }
}
