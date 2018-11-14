import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import java.io.IOException;
import java.net.URL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.html5.Location;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Edition043_iOS_Permissions {

    private String APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.8.0/TheApp-v1.8.0.app.zip";

    private IOSDriver driver;
    private WebDriverWait wait;

    private By geolocation = MobileBy.AccessibilityId("Geolocation Demo");

    @Before
    public void setUp() throws IOException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "iOS");
        caps.setCapability("platformVersion", "12.1");
        caps.setCapability("deviceName", "iPhone 8");
        caps.setCapability("app", APP);
        caps.setCapability("permissions", "{\"io.cloudgrey.the-app\": {\"location\": \"inuse\"}}");

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
    public void testLocationPermissions() {
        // first, set the geolocation to something arbitrary
        double newLat = 49.2827, newLong = 123.1207;
        driver.setLocation(new Location(newLat, newLong, 0));

        // now navigate to the location demo
        wait.until(ExpectedConditions.presenceOfElementLocated(geolocation)).click();

        // if permissions were set correctly, we should get no popup and instead be
        // able to read the latitude and longitude that were previously set
        By newLatEl = MobileBy.AccessibilityId("Latitude: " + newLat);
        By newLongEl = MobileBy.AccessibilityId("Longitude: " + newLong);
        wait.until(ExpectedConditions.presenceOfElementLocated(newLatEl));
        wait.until(ExpectedConditions.presenceOfElementLocated(newLongEl));
    }
}
