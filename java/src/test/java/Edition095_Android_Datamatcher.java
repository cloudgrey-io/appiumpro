import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import java.net.URL;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.json.Json;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Edition095_Android_Datamatcher {
    private AndroidDriver driver;
    private String APP = "https://github.com/appium/android-apidemos/releases/download/v3.1.0/ApiDemos-debug.apk";
    WebDriverWait wait;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Android Emulator");
        capabilities.setCapability("automationName", "Espresso");
        capabilities.setCapability("app", APP);

        driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
        wait = new WebDriverWait(driver, 10);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testFindHiddenListItemNormally() {
        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Views"))).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.xpath("//*[@content-desc='Layouts']"))).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Baseline")));
    }

    @Test
    public void testFindHiddenListItemWithDatamatcher() {
        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Views"))).click();
        WebElement list = wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.className("android.widget.ListView")));
        Map<String, Object> dataMatcher = ImmutableMap.of(
            "name", "hasEntry",
            "args", ImmutableList.of("title", "Layouts")
        );
        list.findElement(MobileBy.androidDataMatcher(new Json().toJson(dataMatcher))).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Baseline")));
    }
}
