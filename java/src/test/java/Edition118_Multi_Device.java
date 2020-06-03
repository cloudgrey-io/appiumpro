import java.net.MalformedURLException;
import java.net.URL;

import com.google.common.collect.ImmutableList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class Edition118_Multi_Device {
    private IOSDriver<WebElement> safari;
    private AndroidDriver<WebElement> chrome;
    private static final String CHAT_URL = "https://chat.appiumpro.com";
    private static final String CHANNEL = "Harlem";
    private static final String USER1_NAME = "Langston";
    private static final String USER2_NAME = "Hughes";
    private static final ImmutableList<String> USER1_CHATS = ImmutableList.of(
            "What happens to a dream deferred?",
            "Or fester like a sore---and then run?",
            "Or crust and sugar over---like a syrupy sweet?",
            "Or does it explode?");
    private static final ImmutableList<String> USER2_CHATS = ImmutableList.of(
            "Does it dry up like a raisin in the sun?",
            "Does it stink like rotten meat?",
            "Maybe it just sags like a heavy load.",
            "........yes");

    private IOSDriver<WebElement> getSafari() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "13.3");
        capabilities.setCapability("deviceName", "iPhone 11");
        capabilities.setCapability("browserName", "Safari");
        capabilities.setCapability("automationName", "XCUITest");
        return new IOSDriver<>(new URL("http://localhost:4723/wd/hub"), capabilities);
    }

    private AndroidDriver<WebElement> getChrome() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Android Emulator");
        capabilities.setCapability("browserName", "Chrome");
        capabilities.setCapability("automationName", "UiAutomator2");
        return new AndroidDriver<>(new URL("http://localhost:4723/wd/hub"), capabilities);
    }

    @Before
    public void setUp() throws MalformedURLException {
        safari = getSafari();
        chrome = getChrome();
    }

    @After
    public void tearDown() {
        if (safari != null) {
            safari.quit();
        }
        if (chrome != null) {
            chrome.quit();
        }
    }

    private void joinChannel(RemoteWebDriver driver, String username, String channel) throws MalformedURLException {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        driver.navigate().to(new URL(CHAT_URL));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#channel"))).sendKeys(channel);
        driver.findElement(By.cssSelector("#username")).sendKeys(username);
        driver.findElement(By.cssSelector("#joinChannel")).click();
    }

    private void sendChat(RemoteWebDriver driver, String message) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#sendMessageInput"))).sendKeys(message);
        driver.findElement(By.cssSelector("#sendMessageBtn")).click();
    }

    private String getChatLog(RemoteWebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#messages"))).getText();
    }

    @Test
    public void testChatApp() throws MalformedURLException {
        joinChannel(safari, USER1_NAME, CHANNEL);
        joinChannel(chrome, USER2_NAME, CHANNEL);
        for (int i = 0; i < USER1_CHATS.size(); i++) {
            sendChat(safari, USER1_CHATS.get(i));
            sendChat(chrome, USER2_CHATS.get(i));
        }
        System.out.println(getChatLog(chrome));
        try { Thread.sleep(4000); } catch (Exception ign) {}
    }
}
