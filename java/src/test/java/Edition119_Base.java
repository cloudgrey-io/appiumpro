import java.time.Duration;

import com.google.common.collect.ImmutableList;

import org.junit.After;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.PointerInput.Kind;
import org.openqa.selenium.interactions.PointerInput.MouseButton;
import org.openqa.selenium.interactions.PointerInput.Origin;
import org.openqa.selenium.interactions.Sequence;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

abstract public class Edition119_Base {
    protected AppiumDriver<MobileElement> driver;

    protected AppiumDriver<MobileElement> getDriver() {
        return driver;
    }

    @After
    public void tearDown() {
        try {
            getDriver().quit();
        } catch (Exception ign) {}
    }

    protected void tapAtPoint(Point point) {
        AppiumDriver<MobileElement> d = getDriver();
        PointerInput input = new PointerInput(Kind.TOUCH, "finger1");
        Sequence tap = new Sequence(input, 0);
        tap.addAction(input.createPointerMove(Duration.ZERO, Origin.viewport(), point.x, point.y));
        tap.addAction(input.createPointerDown(MouseButton.LEFT.asArg()));
        tap.addAction(new Pause(input, Duration.ofMillis(200)));
        tap.addAction(input.createPointerUp(MouseButton.LEFT.asArg()));
        d.perform(ImmutableList.of(tap));
    }

    protected void tapElement(WebElement el) {
        tapElementAt(el, 0.5, 0.5);
    }

    protected void tapElementAt(WebElement el, double xPct, double yPct) {
        Rectangle elRect = el.getRect();
        Point point = new Point(
            elRect.x + (int)(elRect.getWidth() * xPct),
            elRect.y + (int)(elRect.getHeight() * yPct)
        );
        tapAtPoint(point);
    }
}
