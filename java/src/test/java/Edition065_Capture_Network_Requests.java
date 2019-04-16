import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static junit.framework.TestCase.assertTrue;


public class Edition065_Capture_Network_Requests {

    private String ANDROID_APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.8.1/TheApp-v1.9.0.apk";
    private String IOS_APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.6.1/TheApp-v1.6.1.app.zip"; // in order to download, you may need to install the mitmproxy certificate on your operating system first. Or download the app and replace this capability with the path to your app.

    private AppiumDriver driver;
    private MitmproxyJava proxy;

    @After
    public void Quit() throws IOException, InterruptedException {
        proxy.stop();
        driver.quit();
    }

    @Test
    public void captureIosSimulatorTraffic() throws IOException, URISyntaxException, InterruptedException, ExecutionException, TimeoutException {
        List<InterceptedMessage> messages = new ArrayList<InterceptedMessage>();

        // remember to set local OS proxy settings in the Network Preferences
        proxy = new MitmproxyJava("/usr/local/bin/mitmdump", (InterceptedMessage m) -> {
            System.out.println("intercepted request for " + m.requestURL.toString());
            messages.add(m);
            return m;
        });

        proxy.start();

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "iOS");
        caps.setCapability("platformVersion", "12.0");
        caps.setCapability("deviceName", "iPhone Xs");
        caps.setCapability("automationName", "XCUITest");
        caps.setCapability("app", IOS_APP);

        driver = new IOSDriver(new URL("http://0.0.0.0:4723/wd/hub"), caps);

        // automatically install mitmproxy certificate. Can be skipped if done manually on the simulator already.
        Path certificatePath = Paths.get(System.getProperty("user.home"), ".mitmproxy", "mitmproxy-ca-cert.pem");
        Map<String, Object> args = new HashMap<>();
        byte[] byteContent = Files.readAllBytes(certificatePath);
        args.put("content", Base64.getEncoder().encodeToString(byteContent));
        driver.executeScript("mobile: installCertificate", args);

        WebElement picker = driver.findElementByAccessibilityId("Picker Demo");
        picker.click();
        WebElement button = driver.findElementByAccessibilityId("learnMore");
        button.click();
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();


        assertTrue(messages.size() > 0);

        InterceptedMessage appiumIORequest = messages.stream().filter((m) -> m.requestURL.getHost().equals("history.muffinlabs.com")).findFirst().get();

        assertTrue(appiumIORequest.responseCode == 200);
    }

    @Test
    public void captureAndroidEmulatorTraffic() throws IOException, URISyntaxException, InterruptedException, ExecutionException, TimeoutException {
        List<InterceptedMessage> messages = new ArrayList<InterceptedMessage>();

        // remember to set local OS proxy settings in the Network Preferences
        proxy = new MitmproxyJava("/usr/local/bin/mitmdump", (InterceptedMessage m) -> {
            System.out.println("intercepted request for " + m.requestURL.toString());
            messages.add(m);
            return m;
        });

        proxy.start();

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("platformVersion", "9");
        caps.setCapability("deviceName", "test-proxy");
        caps.setCapability("automationName", "UiAutomator2");
        caps.setCapability("app", ANDROID_APP);

        driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"), caps);

        WebElement picker = driver.findElementByAccessibilityId("Picker Demo");
        picker.click();
        WebElement button = driver.findElementByAccessibilityId("learnMore");
        button.click();
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();


        assertTrue(messages.size() > 0);

        InterceptedMessage appiumIORequest = messages.stream().filter((m) -> m.requestURL.getPath().equals("/date/1/1")).findFirst().get();

        assertTrue(appiumIORequest.responseCode == 200);
    }
}
