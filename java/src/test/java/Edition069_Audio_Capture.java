import io.appium.java_client.android.AndroidDriver;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ProcessBuilder.Redirect;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringJoiner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Edition069_Audio_Capture {
    private AndroidDriver driver;

    @Before
    public void setUp() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("platformVersion", "9");
        capabilities.setCapability("deviceName", "Android Emulator");
        capabilities.setCapability("browserName", "Chrome");

        driver = new AndroidDriver<>(new URL("http://localhost:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testAudioCapture() throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        // navigate to band homepage
        driver.get("http://www.splendourhyaline.com");

        // click the amazon store icon for the first album
        WebElement store = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector("img[src='/img/store-amazon.png']")
        ));
        driver.executeScript("window.scrollBy(0, 100);");
        store.click();

        // start playing a sample of the first track
        WebElement play = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//div[@data-track-number='2']//div[@data-action='dm-playable']")
        ));

        driver.executeScript("window.scrollBy(0, 150);");

        // start the song sample
        play.click();

        // start an ffmpeg audio capture of system audio. Replace with a path and device id
        // appropriate for your system (list devices with `ffmpeg -f avfoundation -list_devices true -i ""`
        File audioCapture = new File("/Users/jlipps/Desktop/capture.wav");
        captureForDuration(audioCapture, 10000);

        assert(audioCapture.exists());
    }

    private void captureForDuration(File audioCapture, int durationMs) throws Exception {
        FFmpeg capture = new FFmpeg(audioCapture, 4);
        Thread t = new Thread(capture);
        t.start();

        // wait for sufficient amount of song to play
        Thread.sleep(durationMs);

        // tell ffmpeg to stop sampling
        capture.stopCollection();

        // wait for ffmpeg thread to end on its own
        t.join();
    }
}

class FFmpeg implements Runnable {
    private Process proc;
    private File captureFile;
    private int deviceId;

    FFmpeg(File captureFile, int deviceId) {
        this.proc = null;
        this.captureFile = captureFile;
        this.deviceId = deviceId;
    }

    public void run() {
        ArrayList<String> cmd = new ArrayList<>();
        cmd.add("ffmpeg");       // binary should be on path
        cmd.add("-y");           // always overwrite files
        cmd.add("-f");           // format
        cmd.add("avfoundation"); // apple's system audio---something else for windows
        cmd.add("-i");           // input
        cmd.add(":" + deviceId); // device id returned by ffmpeg list
        cmd.add(captureFile.getAbsolutePath());

        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);
        pb.redirectOutput(Redirect.PIPE);
        StringJoiner out = new StringJoiner("\n");
        try {
            proc = pb.start();
            try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(proc.getInputStream()))) {

                reader.lines().forEach(out::add);
            }
            proc.waitFor();
        } catch (IOException | InterruptedException ign) {}
        System.out.println("FFMpeg output was: " + out.toString());
    }

    public void stopCollection() throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(proc.getOutputStream()));
        writer.write("q");
        writer.flush();
        writer.close();
    }
}
