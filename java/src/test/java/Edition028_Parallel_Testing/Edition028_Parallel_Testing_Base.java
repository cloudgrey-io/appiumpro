package Edition028_Parallel_Testing;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Edition028_Parallel_Testing_Base {

    protected String AUTH_USER = "alice";
    protected String AUTH_PASS = "mypassword";

    protected By loginScreen = MobileBy.AccessibilityId("Login Screen");
    protected By loginBtn = MobileBy.AccessibilityId("loginBtn");
    protected By username = MobileBy.AccessibilityId("username");
    protected By password = MobileBy.AccessibilityId("password");


    public void actualTest(AppiumDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(loginScreen)).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(username)).sendKeys(AUTH_USER);
            wait.until(ExpectedConditions.presenceOfElementLocated(password)).sendKeys(AUTH_PASS);
            wait.until(ExpectedConditions.presenceOfElementLocated(loginBtn)).click();
        } finally {
            driver.quit();
        }
    }
}
