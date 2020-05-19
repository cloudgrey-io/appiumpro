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
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.PointerInput.Kind;
import org.openqa.selenium.interactions.PointerInput.MouseButton;
import org.openqa.selenium.interactions.PointerInput.Origin;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.functions.ExpectedCondition;
import io.appium.java_client.ios.IOSDriver;

public class Edition116_iOS_Springboard {
    private IOSDriver<WebElement> driver;
    private WebDriverWait wait;
    private final static String SPRINGBOARD = "com.apple.springboard";
    private int curPage;


    @Before
    public void setUp() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "13.3");
        capabilities.setCapability("deviceName", "iPhone 11");
        capabilities.setCapability("app", SPRINGBOARD);
        capabilities.setCapability("autoLaunch", false);
        capabilities.setCapability("automationName", "XCUITest");
        driver = new IOSDriver<WebElement>(new URL("http://localhost:4723/wd/hub"), capabilities);
        wait = new WebDriverWait(driver, 10);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testSpringboard() {
        wait.until(AppIconPresent("Reminders")).click();
        pressHome();
        wait.until(AppIconPresent("Contacts")).click();
        pressHome();
    }

    protected void swipe(Point start, Point end, Duration duration) {
        PointerInput input = new PointerInput(Kind.TOUCH, "finger1");
        Sequence swipe = new Sequence(input, 0);
        swipe.addAction(input.createPointerMove(Duration.ZERO, Origin.viewport(), start.x, start.y));
        swipe.addAction(input.createPointerDown(MouseButton.LEFT.asArg()));
        swipe.addAction(new Pause(input, duration));
        duration = Duration.ZERO;
        swipe.addAction(input.createPointerMove(duration, Origin.viewport(), end.x, end.y));
        swipe.addAction(input.createPointerUp(MouseButton.LEFT.asArg()));
        driver.perform(ImmutableList.of(swipe));
    }

    protected void swipe(double startXPct, double startYPct, double endXPct, double endYPct, Duration duration) {
        Dimension size = driver.manage().window().getSize();
        Point start = new Point((int)(size.width * startXPct), (int)(size.height * startYPct));
        Point end = new Point((int)(size.width * endXPct), (int)(size.height * endYPct));
        swipe(start, end, duration);
    }

    protected void pressHome() {
        driver.executeScript("mobile: pressButton", ImmutableMap.of("name", "home"));
    }

    protected void swipeToPrevScreen() {
        swipe(0.1, 0.5, 0.9, 0.5, Duration.ofMillis(750));
    }

    protected void swipeToNextScreen() {
        swipe(0.9, 0.5, 0.1, 0.5, Duration.ofMillis(750));
    }

    protected ExpectedCondition<WebElement> AppIconPresent(final String appName) {
        pressHome();
        curPage = 1;
        return new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver driver) {
                try {
                    return driver.findElement(By.xpath(
                        "//*[@name='Home screen icons']" +
                        "//XCUIElementTypeIcon[" + curPage + "]" +
                        "/XCUIElementTypeIcon[@name='" + appName + "']"
                    ));
                } catch (NoSuchElementException err) {
                    swipeToNextScreen();
                    curPage += 1;
                    throw err;
                }
            }
        };
    }
}
