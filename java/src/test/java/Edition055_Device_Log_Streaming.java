import io.appium.java_client.AppiumDriver;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;



public class Edition055_Device_Log_Streaming {

    private String ANDROID_APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.8.1/TheApp-v1.8.1.apk";
    private String IOS_APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.6.1/TheApp-v1.6.1.app.zip";

    private AppiumDriver driver;

    public class LogClient extends WebSocketClient {

        public LogClient( URI serverURI ) {
            super(serverURI);
        }

        @Override
        public void onOpen(ServerHandshake handshakedata) {
            System.out.println("WEBSOCKET OPENED");
        }

        @Override
        public void onMessage(String message) {
            System.out.println(message);
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            System.out.println("Connection closed, log streaming has stopped");
        }

        @Override
        public void onError(Exception ex) {
            ex.printStackTrace();
            // if the error is fatal then onClose will be called additionally
        }
    }

    @Test
    public void streamAndroidLogs() throws URISyntaxException, MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("deviceName", "Android Emulator");
        caps.setCapability("automationName", "UiAutomator2");
        caps.setCapability("app", ANDROID_APP);

        driver = new AppiumDriver(new URL("http://localhost:4723/wd/hub"), caps);

        LogClient logClient = new LogClient(new URI( "ws://localhost:4723/ws/session/" + driver.getSessionId() + "/appium/device/logcat"));

        driver.executeScript("mobile:startLogsBroadcast");

        logClient.connect();

        try { Thread.sleep(5000); } catch (Exception ign) {} // logs printed to stdout while we're sleeping.

        driver.executeScript("mobile:stopLogsBroadcast");
        driver.quit();
    }

    @Test
    public void streamIOSLogs() throws URISyntaxException, MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "iOS");
        caps.setCapability("platformVersion", "12.1");
        caps.setCapability("deviceName", "iPhone XS");
        caps.setCapability("automationName", "XCUITest");
        caps.setCapability("app", IOS_APP);

        driver = new AppiumDriver(new URL("http://localhost:4723/wd/hub"), caps);

        LogClient logClient = new LogClient( new URI("ws://localhost:4723/ws/session/" + driver.getSessionId() + "/appium/device/syslog"));

        driver.executeScript("mobile:startLogsBroadcast");

        logClient.connect();

        try { Thread.sleep(5000); } catch (Exception ign) {} // logs printed to stdout while we're sleeping.

        driver.executeScript("mobile:stopLogsBroadcast");
        driver.quit();
    }
}
