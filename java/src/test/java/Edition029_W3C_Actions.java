import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.PointerInput.Kind;
import org.openqa.selenium.interactions.PointerInput.MouseButton;
import org.openqa.selenium.interactions.PointerInput.Origin;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Edition029_W3C_Actions {

    private String APP = "http://appium.s3.amazonaws.com/ApiDemos-debug-2015-03-19.apk";
    private Duration STEP_DURATION = Duration.ofMillis(20);
    private Duration NO_TIME = Duration.ofMillis(0);
    private Origin VIEW = Origin.viewport();

    private AndroidDriver driver;

    @Before
    public void setUp() throws IOException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("deviceName", "Android Emulator");
        caps.setCapability("app", APP);
        caps.setCapability("appActivity", ".graphics.FingerPaint");
        caps.setCapability("automationName", "UiAutomator2");
        driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), caps);
    }

    @After
    public void tearDown() {
        try {
            driver.quit();
        } catch (Exception ign) {}
    }

    @Test
    public void drawFace() {
        Point head = new Point(220, 450);
        Point leftEye = head.moveBy(-50, -50);
        Point rightEye = head.moveBy(50, -50);
        Point mouth = head.moveBy(0, 50);

        drawCircle(driver, head, 150, 30);
        drawCircle(driver, leftEye, 20, 20);
        drawCircle(driver, rightEye, 20, 20);
        drawCircle(driver, mouth, 40, 20);

        try { Thread.sleep(5000); } catch (InterruptedException ign) {}
    }

    private Point getPointOnCircle (int step, int totalSteps, Point origin, double radius) {
        double theta = 2 * Math.PI * ((double)step / totalSteps);
        int x = (int)Math.floor(Math.cos(theta) * radius);
        int y = (int)Math.floor(Math.sin(theta) * radius);
        return new Point(origin.x + x, origin.y + y);
    }

    private void drawCircle (AppiumDriver driver, Point origin, double radius, int steps) {
        Point firstPoint = getPointOnCircle(0, steps, origin, radius);

        PointerInput finger = new PointerInput(Kind.TOUCH, "finger");
        Sequence circle = new Sequence(finger, 0);
        circle.addAction(finger.createPointerMove(NO_TIME, VIEW, firstPoint.x, firstPoint.y));
        circle.addAction(finger.createPointerDown(MouseButton.LEFT.asArg()));

        for (int i = 1; i < steps + 1; i++) {
            Point point = getPointOnCircle(i, steps, origin, radius);
            circle.addAction(finger.createPointerMove(STEP_DURATION, VIEW, point.x, point.y));
        }

        circle.addAction(finger.createPointerUp(MouseButton.LEFT.asArg()));
        driver.perform(Arrays.asList(circle));
    }

}
