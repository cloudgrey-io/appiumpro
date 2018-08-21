import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Edition031_iOS_Alerts {

    private String APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.6.1/TheApp-v1.6.1.app.zip";

    private IOSDriver driver;
    private WebDriverWait wait;

    private By listView = MobileBy.AccessibilityId("List Demo");
    private By cloud = MobileBy.AccessibilityId("Cirrostratus");

    @Before
    public void setUp() throws IOException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "iOS");
        caps.setCapability("platformVersion", "11.4");
        caps.setCapability("deviceName", "iPhone 6");
        caps.setCapability("app", APP);
        driver = new IOSDriver<MobileElement>(new URL("http://localhost:4723/wd/hub"), caps);
        wait  = new WebDriverWait(driver, 10);
    }

    @After
    public void tearDown() {
        try {
            driver.quit();
        } catch (Exception ign) {}
    }

    @Test
    public void testCustomAlertButtons() {
        wait.until(ExpectedConditions.presenceOfElementLocated(listView)).click();

        WebElement cloudItem = wait.until(ExpectedConditions.presenceOfElementLocated(cloud));
        cloudItem.click();

        wait.until(ExpectedConditions.alertIsPresent());
        HashMap<String, String> args = new HashMap<>();
        args.put("action", "getButtons");
        List<String> buttons = (List<String>)driver.executeScript("mobile: alert", args);

        // find the text of the button which isn't 'OK' or 'Cancel'
        String buttonLabel = null;
        for (String button : buttons) {
            if (button.equals("OK") || button.equals("Cancel")) {
                continue;
            }
            buttonLabel = button;
        }

        if (buttonLabel == null) {
            throw new Error("Did not get a third alert button as we were expecting");
        }

        args.put("action", "accept");
        args.put("buttonLabel", buttonLabel);
        driver.executeScript("mobile: alert", args);

        wait.until(ExpectedConditions.alertIsPresent());

        // here we could verify that the new button press worked, but for now just print it out
        String alertText = driver.switchTo().alert().getText();
        System.out.println(alertText);
    }
}
