import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Edition044_Shadow_DOM {

    private static By SWITCH_COMPONENT = By.xpath("//mwc-switch[1]");

    private static String SHADOWED_INPUT = "return arguments[0].shadowRoot.querySelector('input')";
    private static String INPUT_CHECKED = SHADOWED_INPUT + ".checked";
    private static String CLICK_INPUT = SHADOWED_INPUT + ".click()";
    private static String URL = "https://material-components.github.io/material-components-web-components/demos/switch.html";

    @Test
    public void testShadowDom_iOS() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "12.1");
        capabilities.setCapability("deviceName", "iPhone 8");
        capabilities.setCapability("browserName", "Safari");

        // Open up Safari
        IOSDriver driver = new IOSDriver<>(new URL("http://localhost:4723/wd/hub"), capabilities);
        try {
            testShadowElementsWithJS(driver);
        } finally {
            driver.quit();
        }
    }

    @Test
    public void testAppiumProSite_Android() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Android Emulator");
        capabilities.setCapability("browserName", "Chrome");
        capabilities.setCapability("automationName", "UiAutomator2");

        // Open up Chrome
        AndroidDriver driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), capabilities);

        try {
            testShadowElementsAsNative(driver);
            testShadowElementsWithJS(driver);
        } finally {
            driver.quit();
        }
    }

    public void testShadowElementsAsNative (AppiumDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        driver.get(URL);

        // find the web component
        WebElement switchComponent = wait.until(ExpectedConditions.presenceOfElementLocated(
            SWITCH_COMPONENT));

        // use it to find the inner control
        WebElement nativeCheckbox = (WebElement) driver.executeScript(SHADOWED_INPUT, switchComponent);

        // use the standard API to determine whether the control is checked
        Assert.assertEquals(nativeCheckbox.isSelected(), false);

        // use the standard API to change the checked status
        switchComponent.click();

        // and finally verify the new checked state after the click
        Assert.assertEquals(nativeCheckbox.isSelected(), true);
    }

    public void testShadowElementsWithJS(AppiumDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        driver.get(URL);

        // find the web component
        WebElement switchComponent = wait.until(ExpectedConditions.presenceOfElementLocated(
            SWITCH_COMPONENT));

        // pierce shadow dom to get checked status of inner control, and assert on it
        boolean checked = (boolean) driver.executeScript(INPUT_CHECKED, switchComponent);
        Assert.assertEquals(false, checked);

        // change the state from off to on by clicking inner input
        // (clicking the parent component will not work)
        driver.executeScript(CLICK_INPUT, switchComponent);

        // check that state of inner control has changed appropriately
        checked = (boolean) driver.executeScript(INPUT_CHECKED, switchComponent);
        Assert.assertEquals(true, checked);
    }
}
