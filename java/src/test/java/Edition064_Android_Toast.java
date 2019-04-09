import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumDriver;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Edition064_Android_Toast {

    private String APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.9.0/TheApp-v1.9.0.apk";

    private AppiumDriver driver;

    @Before
    public void setUp() throws IOException {
        DesiredCapabilities caps = new DesiredCapabilities();

        caps.setCapability("platformName", "Android");
        caps.setCapability("deviceName", "Android Emulator");
        caps.setCapability("automationName", "Espresso");
        caps.setCapability("app", APP);
        driver = new AppiumDriver(new URL("http://localhost:4723/wd/hub"), caps);
    }

    @After
    public void tearDown() {
        try {
            driver.quit();
        } catch (Exception ign) {}
    }

    private void raiseToast(String text) {
        ImmutableMap<String, Object> scriptArgs = ImmutableMap.of(
            "target", "application",
            "methods", Arrays.asList(ImmutableMap.of(
                "name", "raiseToast",
                "args", Arrays.asList(ImmutableMap.of(
                    "value", text,
                    "type", "String"
                ))
            ))
        );

        driver.executeScript("mobile: backdoor", scriptArgs);
    }

    public static ExpectedCondition<Boolean> toastMatches(String matchText, Boolean isRegexp) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                ImmutableMap<String, Object> args = ImmutableMap.of(
                    "text", matchText,
                    "isRegexp", isRegexp
                );
                return (Boolean) ((JavascriptExecutor)driver).executeScript("mobile: isToastVisible", args);
            }

            @Override
            public String toString() {
                return "toast to be present";
            }
        };
    }

    @Test
    public void testToast() {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        final String toastText = "Catch me if you can!";
        raiseToast(toastText);

        wait.until(toastMatches(toastText, false));

        raiseToast(toastText);
        wait.until(toastMatches("^Catch.+!", true));
    }

}
