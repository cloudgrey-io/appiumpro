import io.appium.java_client.MobileBy;
import io.appium.java_client.ios.IOSDriver;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@RunWith(JUnit4.class)
public class Edition001_iOS_Photos {

    @Test
    public void testSeedPhotoPicker () throws IOException, InterruptedException {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        File classpathRoot = new File(System.getProperty("user.dir"));
        File appDir = new File(classpathRoot, "../apps/");
        File app = new File(appDir.getCanonicalPath(), "SamplePhotosApp.app.zip");

        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("deviceName", "iPhone 8 Plus");
        capabilities.setCapability("platformVersion", "11.2");
        capabilities.setCapability("app", app);

        // Open the app.
        IOSDriver driver = new IOSDriver<>(new URL("http://localhost:4723/wd/hub"), capabilities);

        try {
            // first allow the app to access photos on the phone
            driver.switchTo().alert().accept();

            // navigate to the photo view and count how many there are
            WebDriverWait wait = new WebDriverWait(driver, 10);
            WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("All Photos")));
            el.click();

            List<WebElement> photos = driver.findElements(MobileBy.className("XCUIElementTypeImage"));
            int numPhotos = photos.size();

            // set up the file we want to push to the phone's library
            File assetDir = new File(classpathRoot, "../assets");
            File img = new File(assetDir.getCanonicalPath(), "cloudgrey.png");

            // push the file -- note that it's important it's just the bare basename of the file
            driver.pushFile("pano.jpg", img);

            // in lieu of a formal verification, simply print out the new number of photos, which
            // should have increased by one
            photos = driver.findElements(MobileBy.className("XCUIElementTypeImage"));
            System.out.println("There were " + numPhotos + " photos before, and now there are " +
                photos.size() + "!");

        } finally {
            driver.quit();
        }
    }
}
