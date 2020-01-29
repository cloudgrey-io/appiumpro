import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.zeroturnaround.exec.ProcessExecutor;

import io.appium.java_client.ios.IOSDriver;

public class Edition104_Network_Conditioning_iOS {
    private IOSDriver<WebElement> driver;
    private String NET_SCRIPT = Paths.get(System.getProperty("user.dir"), "..", "scripts", "network.sh").toString();

    @Before
    public void setUp() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "13.3");
        capabilities.setCapability("deviceName", "iPhone 11");
        capabilities.setCapability("browserName", "Safari");
        driver = new IOSDriver<WebElement>(new URL("http://localhost:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void loadPage() {
        long startTime = System.nanoTime();
        driver.get("https://appiumpro.com");
        long endTime = System.nanoTime();
        long msElapsed = (endTime - startTime) / 1000000;
        System.out.println("Time elapsed: " + msElapsed);
    }

    private void setNetworkMode(String mode) throws Exception {
        new ProcessExecutor().command(NET_SCRIPT, "start", mode).exitValueNormal().execute();
    }

    private void stopNetworkConditioning() throws Exception {
        new ProcessExecutor().command(NET_SCRIPT, "stop").exitValueNormal().execute();
    }

    @Test
    public void testPageLoadWithNormalNetwork() {
        loadPage();
    }

    @Test
    public void testPageLoadWith3GNetwork() throws Exception {
        setNetworkMode("3G");
        try {
            loadPage();
        } finally {
            stopNetworkConditioning();
        }
    }
}
