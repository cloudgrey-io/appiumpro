import io.appium.java_client.android.AndroidDriver;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Edition005_Android_Memory {

    private static int MEMORY_USAGE_WAIT = 30000;
    private static int MEMORY_CAPTURE_WAIT = 10;
    private static String PKG = "io.appium.android.apis";
    private static String PERF_TYPE = "memoryinfo";
    private static String PSS_TYPE = "totalPss";

    @Test
    public void testMemoryUsage() throws Exception {
        File classpathRoot = new File(System.getProperty("user.dir"));
        File appDir = new File(classpathRoot, "../apps/");
        File app = new File(appDir.getCanonicalPath(), "ApiDemos.apk");

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Android Emulator");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("app", app);

        AndroidDriver driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
        try {
            // get the usage at one point in time
            int totalPss1 = getMemoryInfo(driver).get(PSS_TYPE);

            // then get it again after waiting a while
            try { Thread.sleep(MEMORY_USAGE_WAIT); } catch (InterruptedException ign) {}
            int totalPss2 = getMemoryInfo(driver).get(PSS_TYPE);

            // finally, verify that we haven't increased usage more than 5%
            Assert.assertThat((double) totalPss2, Matchers.lessThan(totalPss1 * 1.05));
        } finally {
            driver.quit();
        }
    }

    private HashMap<String, Integer> getMemoryInfo(AndroidDriver driver) throws Exception {
        List<List<Object>> data = driver.getPerformanceData(PKG, PERF_TYPE, MEMORY_CAPTURE_WAIT);
        HashMap<String, Integer> readableData = new HashMap<>();
        for (int i = 0; i < data.get(0).size(); i++) {
            int val;
            if (data.get(1).get(i) == null) {
                val = 0;
            } else {
                val = Integer.parseInt((String) data.get(1).get(i));
            }
            readableData.put((String) data.get(0).get(i), val);
        }
        return readableData;
    }
}
