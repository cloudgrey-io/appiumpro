import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.PointerInput.Kind;
import org.openqa.selenium.interactions.PointerInput.MouseButton;
import org.openqa.selenium.interactions.PointerInput.Origin;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.MobileBy;
import io.appium.java_client.ios.IOSDriver;

public class Edition109_iPadOS_Split_Screen {
    private IOSDriver<WebElement> driver;
    private WebDriverWait wait;
    private Dimension size;
    private final static String PHOTOS = "com.apple.mobileslideshow";
    private final static String REMINDERS = "com.apple.reminders";

    @Before
    public void setUp() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "13.3");
        capabilities.setCapability("deviceName", "iPad Pro (12.9-inch) (3rd generation)");
        capabilities.setCapability("app", PHOTOS);
        capabilities.setCapability("simulatorTracePointer", true);
        driver = new IOSDriver<WebElement>(new URL("http://localhost:4723/wd/hub"), capabilities);
        wait  = new WebDriverWait(driver, 10);
        size = driver.manage().window().getSize();
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected By getTaskIcon(String name) {
        return By.xpath("//*[@name='Multitasking Dock']//*[@name='" + name + "']");
    }

    protected void swipe(Point start, Point end, Duration swipeDur, Duration holdDur) {
        PointerInput input = new PointerInput(Kind.TOUCH, "finger1");
        Sequence swipe = new Sequence(input, 0);
        swipe.addAction(input.createPointerMove(Duration.ZERO, Origin.viewport(), start.x, start.y));
        swipe.addAction(input.createPointerDown(MouseButton.LEFT.asArg()));
        swipe.addAction(new Pause(input, swipeDur));
        swipe.addAction(input.createPointerMove(holdDur, Origin.viewport(), end.x, end.y));
        swipe.addAction(input.createPointerUp(MouseButton.LEFT.asArg()));
        driver.perform(ImmutableList.of(swipe));
    }

    protected void swipe(double startXPct, double startYPct, double endXPct, double endYPct,
            Duration duration, Duration holdDuration) {
        Point start = new Point((int)(size.width * startXPct), (int)(size.height * startYPct));
        Point end = new Point((int)(size.width * endXPct), (int)(size.height * endYPct));
        swipe(start, end, duration, holdDuration);
    }

    protected void swipe(double startXPct, double startYPct, double endXPct, double endYPct,
            Duration duration) {
        swipe(startXPct, startYPct, endXPct, endYPct, duration, Duration.ZERO);
    }

    protected void showDock() {
        swipe(0.5, 1.0, 0.5, 0.92, Duration.ofMillis(1000));
    }

    protected void dragElement(Rectangle elRect, double endXPct, double endYPct, Duration duration) {
        Point start = new Point((int)(elRect.x + elRect.width / 2), (int)(elRect.y + elRect.height / 2));
        Point end = new Point((int)(size.width * endXPct), (int)(size.height * endYPct));
        driver.executeScript("mobile: dragFromToForDuration", ImmutableMap.of(
            "fromX", start.x, "fromY", start.y,
            "toX", end.x, "toY", end.y,
            "duration", duration.toMillis() / 1000.0
        ));
    }

    protected Rectangle getDockIconRect(String appName) {
        WebElement icon = wait.until(
            ExpectedConditions.presenceOfElementLocated(getTaskIcon(appName)));
        return icon.getRect();
    }

    @Test
    public void testSplitScreen() throws InterruptedException {
        // terminate photos and launch reminders to make sure they're both the most recently-
        // launched apps
        driver.executeScript("mobile: terminateApp", ImmutableMap.of("bundleId", PHOTOS));
        driver.executeScript("mobile: launchApp", ImmutableMap.of("bundleId", REMINDERS));

        // go to the home screen so we have access to the dock icons
        ImmutableMap<String, String> pressHome = ImmutableMap.of("name", "home");
        driver.executeScript("mobile: pressButton", pressHome);

        // save the location of the icons in the dock so we know where they are when we need
        // to drag them later, but no longer have access to them as elements
        Rectangle photosIconRect = getDockIconRect("Photos");

        // relaunch reminders
        driver.executeScript("mobile: launchApp", ImmutableMap.of("bundleId", REMINDERS));

        // pull the dock up so we can see the recent icons, and give it time to settle
        showDock();
        Thread.sleep(800);

        // now we can drag the photos app icon over to the right edge to enter split view
        // also give it a bit of time to settle
        dragElement(photosIconRect, 1.0, 0.5, Duration.ofMillis(1500));
        Thread.sleep(800);

        driver.setSetting("defaultActiveApplication", PHOTOS);
        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("All Photos")));
        driver.setSetting("defaultActiveApplication", REMINDERS);
        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("New Reminder")));

        // clean up by terminating both apps
        driver.executeScript("mobile: terminateApp", ImmutableMap.of("bundleId", PHOTOS));
        driver.executeScript("mobile: terminateApp", ImmutableMap.of("bundleId", REMINDERS));
    }

}
