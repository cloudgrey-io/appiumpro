import com.google.common.collect.ImmutableMap;
import io.appium.java_client.android.AndroidDriver;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Edition003_Arbitrary_ADB {

    private static String ANDROID_PHOTO_PATH = "/mnt/sdcard/Pictures";

    @Test
    public void testArbitraryADBCommands() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Android Emulator");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("appPackage", "com.google.android.apps.photos");
        capabilities.setCapability("appActivity", ".home.HomeActivity");

        // Open the app.
        AndroidDriver driver = new AndroidDriver<>(new URL("http://localhost:4723/wd/hub"), capabilities);

        try {
            List<String> removePicsArgs = Arrays.asList("-rf", ANDROID_PHOTO_PATH + "/*.*");
            Map<String, Object> removePicsCmd = ImmutableMap
                .of("command", "rm", "args", removePicsArgs);
            driver.executeScript("mobile: shell", removePicsCmd);

            List<String> lsArgs = Arrays.asList("/mnt/sdcard");
            Map<String, Object> lsCmd = ImmutableMap.of("command", "ls", "args", lsArgs);
            String lsOutput = (String) driver.executeScript("mobile: shell", lsCmd);
            Assert.assertEquals("", lsOutput);
        } finally {
            driver.quit();
        }
    }

}
