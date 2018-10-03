import io.appium.java_client.ios.IOSDriver;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.json.Json;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Edition037_iOS_Web_Console {
    private IOSDriver driver;

    @Before
    public void setUp() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "11.4");
        capabilities.setCapability("deviceName", "iPhone 8");
        capabilities.setCapability("browserName", "Safari");

        driver = new IOSDriver<>(new URL("http://localhost:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            for (LogEntry entry : driver.manage().logs().get("safariConsole")) {
                HashMap<String, Object> consoleEntry = new Json().toType(entry.getMessage(), Json.MAP_TYPE);
                System.out.println(String.format(
                    "%s [%s] %s",
                    entry.getTimestamp(),
                    entry.getLevel(),
                    consoleEntry.get("text")));
            }
            driver.quit();
        }
    }

    @Test
    public void testLogging() {
        driver.get("https://appiumpro.com/test");
        driver.executeScript("window.onerror=console.error.bind(console)");
        driver.executeScript("console.log('foo.');");
        driver.executeScript("console.warn('bar?');");
        driver.findElementById("jsErrLink").click();
    }
}
