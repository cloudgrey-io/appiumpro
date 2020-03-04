import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;

abstract public class Edition107_Base {
    protected AppiumDriver<MobileElement> driver;

    private By listView = MobileBy.AccessibilityId("List Demo");
    private By firstCloud = MobileBy.AccessibilityId("Altocumulus");
    private WebDriverWait wait;

    protected AppiumDriver<MobileElement> getDriver() {
        return driver;
    }

    protected void navToList() {
        wait  = new WebDriverWait(getDriver(), 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(listView)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(firstCloud));
    }

    protected void swipe(Point start, Point end, TimeUnit duration) {
        AppiumDriver<MobileElement> d = getDriver();
    }
}
