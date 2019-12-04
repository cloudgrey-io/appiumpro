import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.imagecomparison.SimilarityMatchingOptions;
import io.appium.java_client.imagecomparison.SimilarityMatchingResult;
import java.io.File;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Edition098_Visual_Testing_1 {
    private final static String APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.10.0/TheApp-v1.10.0.apk";

    // need somewhere to store match files; change for a path suitable for your system
    private final static String VALIDATION_PATH = "/Users/jlipps/Desktop/validations";

    private final static String CHECK_HOME = "home_screen";
    private final static String CHECK_LOGIN = "login_screen";
    private final static String BASELINE = "BASELINE_";

    private final static double MATCH_THRESHOLD = 0.99;

    private final static By LOGIN_SCREEN = MobileBy.AccessibilityId("Login Screen");
    private final static By USERNAME_FIELD = MobileBy.AccessibilityId("username");

    private AndroidDriver driver;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Android Emulator");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("app", APP);
        // make sure we uninstall the app before each test regardless of version
        capabilities.setCapability("uninstallOtherPackages", "io.cloudgrey.the_app");
        URL server = new URL("http://localhost:4723/wd/hub");
        driver = new AndroidDriver(server, capabilities);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private WebElement waitForElement(WebDriverWait wait, By selector) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(selector));
        try { Thread.sleep(750); } catch (InterruptedException ign) {}
        return el;
    }

    @Test
    public void testAppDesign() throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, 5);

        // wait for an element that's on the home screen
        WebElement loginScreen = waitForElement(wait, LOGIN_SCREEN);

        // now we know the home screen is loaded, so do a visual check
        doVisualCheck(CHECK_HOME);

        // nav to the login screen, and wait for an element that's on the login screen
        loginScreen.click();
        waitForElement(wait, USERNAME_FIELD);

        // perform our second visual check, this time of the login screen
        doVisualCheck(CHECK_LOGIN);
    }

    private void doVisualCheck(String checkName) throws Exception {
        String baselineFilename = VALIDATION_PATH + "/" + BASELINE + checkName + ".png";
        File baselineImg = new File(baselineFilename);

        // If no baseline image exists for this check, we should create a baseline image
        if (!baselineImg.exists()) {
            System.out.println(String.format("No baseline found for '%s' check; capturing baseline instead of checking", checkName));
            File newBaseline = driver.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(newBaseline, new File(baselineFilename));
            return;
        }

        // Otherwise, if we found a baseline, get the image similarity from Appium. In getting the similarity,
        // we also turn on visualization so we can see what went wrong if something did.
        SimilarityMatchingOptions opts = new SimilarityMatchingOptions();
        opts.withEnabledVisualization();
        SimilarityMatchingResult res = driver.getImagesSimilarity(baselineImg, driver.getScreenshotAs(OutputType.FILE), opts);

        // If the similarity is not high enough, consider the check to have failed
        if (res.getScore() < MATCH_THRESHOLD) {
            File failViz = new File(VALIDATION_PATH + "/FAIL_" + checkName + ".png");
            res.storeVisualization(failViz);
            throw new Exception(
                String.format("Visual check of '%s' failed; similarity match was only %f, and below the threshold of %f. Visualization written to %s.",
                    checkName, res.getScore(), MATCH_THRESHOLD, failViz.getAbsolutePath()));
        }

        // Otherwise, it passed!
        System.out.println(String.format("Visual check of '%s' passed; similarity match was %f",
            checkName, res.getScore()));
    }
}
