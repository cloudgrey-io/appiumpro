
package ImageFinder;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

public class TestUtil 
{
    static public void Wait(long time) 
    {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

    static public File takeScreenshot(final String name, AppiumDriver driver) {

        System.out.println("ScreenShot: " + name);

        String screenshotDirectory = System.getProperty("appium.screenshots.dir", System.getProperty("java.io.tmpdir", ""));
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        //move to aws device farm's screenshot folder
        screenshot.renameTo(new File(screenshotDirectory, String.format("%s.png", name)));

        return screenshot;
    }

    static public int[] FindImage(String fileName, AppiumDriver driver)
    {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                
        if(!screenshot.exists() || FileUtils.sizeOfAsBigInteger(screenshot) == BigInteger.ZERO)
            return null;
        
        int[] region = AkazeImageFinder.QueryImage(fileName, screenshot.getAbsolutePath());
        return region;
    }
    
    static public boolean TapImage(String fileName, AppiumDriver driver)
    {
        int[] region = FindImage(fileName, driver);
        if(region == null)
               return false;
        
        int x = region[0];
        int y = region[1];

        io.appium.java_client.TouchAction touchAction = new io.appium.java_client.TouchAction(driver);
        touchAction.tap(PointOption.point(x, y));
        driver.performTouchAction(touchAction);

        return true;
    }
    
    static public boolean WaitingForIamge(String fileName,int seconds, AppiumDriver driver)
    {
        int retry = 0;
        while(retry < seconds)
        {
            if(TestUtil.FindImage(fileName, driver) != null)
                return true;
            
            TestUtil.Wait(1000);
            retry++;
        }
        
        return false;
    }
}
