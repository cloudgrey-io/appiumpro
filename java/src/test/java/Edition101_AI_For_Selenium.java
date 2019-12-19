import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import ai.test.classifier_client.ClassifierClient;

public class Edition101_AI_For_Selenium {
    private RemoteWebDriver driver;
    private ClassifierClient classifier;

    @Before
    public void setUp() throws MalformedURLException {
        driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),
                                     new ChromeOptions());
        classifier = new ClassifierClient("127.0.0.1", 50051);
    }

    @After
    public void tearDown() throws InterruptedException {
        if (driver != null) {
            driver.quit();
        }
        if (classifier != null) {
            classifier.shutdown();
        }
    }


    @Test
    public void testClassifierClient() throws Exception {
        driver.get("https://test.ai");
        List<WebElement> els = classifier.findElementsMatchingLabel(driver, "twitter");
        Assert.assertThat(els, IsCollectionWithSize.hasSize(1));
        els.get(0).click();
        Assert.assertEquals(driver.getCurrentUrl(), "https://twitter.com/testdotai");
    }
}
