import java.util.NoSuchElementException;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class Edition114_Custom_ExpectedConditions extends Edition113_Automating_Zoom {

    protected By CONTENT = By.id("content");

    @Override
    @Test
    public void testJoinMeeting() throws Exception {
        // navigate through the UI to join a meeting with correct meeting id and password
        waitFor(JOIN_MEETING_BUTTON).click();
        waitFor(MEETING_ID_FIELD).sendKeys(MEETING_ID);
        driver.findElement(ACTUALLY_JOIN_MEETING_BUTTON).click();
        waitFor(PASSWORD_FIELD).sendKeys(MEETING_PW);
        driver.findElement(PASSWORD_OK_BUTTON).click();

        Thread.sleep(30000);
        waitFor(CONTENT).click();

        wait.until(ZoomUIPresent());
        driver.findElement(LEAVE_BTN).click();
        Thread.sleep(2000);
    }

    private ExpectedCondition<Boolean> ZoomUIPresent() {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    driver.findElement(LEAVE_BTN);
                    return true;
                } catch (NoSuchElementException ign) {
                    driver.findElement(CONTENT).click();
                }
                return false;
            }
        };
    }
}
