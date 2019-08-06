import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.URL;

public class Edition081_Windows {

    private AppiumDriver<WebElement> driver;

    @Before
    public void setUp() throws IOException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Windows");
        caps.setCapability("platformVersion", "10");
        caps.setCapability("deviceName", "WindowsPC");
        caps.setCapability("app", "Microsoft.BingWeather_8wekyb3d8bbwe!App");

        driver = new AppiumDriver<>(new URL("http://localhost:4723/wd/hub"), caps);
    }

    @After
    public void tearDown() {
        try {
            driver.quit();
        } catch (Exception ign) {}
    }

    @Test
    public void testWeatherApp() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("NameAndConditions")));
        for (WebElement el : driver.findElements(By.xpath("//ListItem[contains(@Name, 'day ')]"))) {
            el.click();
            WebElement sunrise = driver.findElement(MobileBy.AccessibilityId("Almanac_Sunrise"));
            WebElement sunset = driver.findElement(MobileBy.AccessibilityId("Almanac_Sunset"));
            System.out.println(el.getAttribute("Name"));
            System.out.println("Sunrise: " + sunrise.getAttribute("Name"));
            System.out.println("Sunset: " + sunset.getAttribute("Name"));
            System.out.println("----------------------------");
        }
    }
}
