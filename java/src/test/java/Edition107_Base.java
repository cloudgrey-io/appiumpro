import java.time.Duration;

import com.google.common.collect.ImmutableList;

import org.junit.After;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.PointerInput.Kind;
import org.openqa.selenium.interactions.PointerInput.MouseButton;
import org.openqa.selenium.interactions.PointerInput.Origin;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

abstract public class Edition107_Base {
    protected AppiumDriver<MobileElement> driver;

    private By listView = MobileBy.AccessibilityId("List Demo");
    private By firstCloud = MobileBy.AccessibilityId("Altocumulus");
    private WebDriverWait wait;
    private Dimension windowSize;

    private static Duration SCROLL_DUR = Duration.ofMillis(1000);
    private static double SCROLL_RATIO = 0.8;
    private static int ANDROID_SCROLL_DIVISOR = 3;

    protected AppiumDriver<MobileElement> getDriver() {
        return driver;
    }

    @After
    public void tearDown() {
        try {
            getDriver().quit();
        } catch (Exception ign) {}
    }

    protected void navToList() {
        wait  = new WebDriverWait(getDriver(), 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(listView)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(firstCloud));
    }

    private Dimension getWindowSize() {
        if (windowSize == null) {
            windowSize = getDriver().manage().window().getSize();
        }
        return windowSize;
    }

    protected void swipe(Point start, Point end, Duration duration) {
        AppiumDriver<MobileElement> d = getDriver();
        boolean isAndroid = d instanceof AndroidDriver<?>;

        PointerInput input = new PointerInput(Kind.TOUCH, "finger1");
        Sequence swipe = new Sequence(input, 0);
        swipe.addAction(input.createPointerMove(Duration.ZERO, Origin.viewport(), start.x, start.y));
        swipe.addAction(input.createPointerDown(MouseButton.LEFT.asArg()));
        if (isAndroid) {
            duration = duration.dividedBy(ANDROID_SCROLL_DIVISOR);
        } else {
            swipe.addAction(new Pause(input, duration));
            duration = Duration.ZERO;
        }
        swipe.addAction(input.createPointerMove(duration, Origin.viewport(), end.x, end.y));
        d.perform(ImmutableList.of(swipe));
    }

    protected void swipe(double startXPct, double startYPct, double endXPct, double endYPct, Duration duration) {
        Dimension size = getWindowSize();
        Point start = new Point((int)(size.width * startXPct), (int)(size.height * startYPct));
        Point end = new Point((int)(size.width * endXPct), (int)(size.height * endYPct));
        swipe(start, end, duration);
    }

    protected void scroll(ScrollDirection dir, double distance) {
        if (distance < 0 || distance > 1) {
            throw new Error("Scroll distance must be between 0 and 1");
        }
        Dimension size = getWindowSize();
        Point midPoint = new Point((int)(size.width * 0.5), (int)(size.height * 0.5));
        int top = midPoint.y - (int)((size.height * distance) * 0.5);
        int bottom = midPoint.y + (int)((size.height * distance) * 0.5);
        int left = midPoint.x - (int)((size.width * distance) * 0.5);
        int right = midPoint.x + (int)((size.width * distance) * 0.5);
        if (dir == ScrollDirection.UP) {
            swipe(new Point(midPoint.x, top), new Point(midPoint.x, bottom), SCROLL_DUR);
        } else if (dir == ScrollDirection.DOWN) {
            swipe(new Point(midPoint.x, bottom), new Point(midPoint.x, top), SCROLL_DUR);
        } else if (dir == ScrollDirection.LEFT) {
            swipe(new Point(left, midPoint.y), new Point(right, midPoint.y), SCROLL_DUR);
        } else {
            swipe(new Point(right, midPoint.y), new Point(left, midPoint.y), SCROLL_DUR);
        }
    }

    protected void scroll(ScrollDirection dir) {
        scroll(dir, SCROLL_RATIO);
    }

    protected void scroll() {
        scroll(ScrollDirection.DOWN, SCROLL_RATIO);
    }

    public enum ScrollDirection {
        UP, DOWN, LEFT, RIGHT
    }
}
