import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.zeroturnaround.exec.ProcessExecutor;

public class Edition070_Audio_Verification extends Edition069_Audio_Capture {

    private File getReferenceAudio() throws URISyntaxException {
        URL refImgUrl = getClass().getClassLoader().getResource("Edition070_Reference.wav");
        return Paths.get(refImgUrl.toURI()).toFile();
    }

    @Test
    @Override
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
            By.xpath("//div[@data-track-number='1']//div[@data-action='dm-playable']")
        ));

        driver.executeScript("window.scrollBy(0, 150);");

        // start the song sample
        play.click();

        // start an ffmpeg audio capture of system audio. Replace with a path and device id
        // appropriate for your system (list devices with `ffmpeg -f avfoundation -list_devices true -i ""`
        File audioCapture = new File("/Users/jlipps/Desktop/capture.wav");
        captureForDuration(audioCapture, 10000);

        // now we calculate the fingerprint of the freshly-captured audio...
        AudioFingerprint fp1 = AudioFingerprint.calcFP(audioCapture);

        // as well as the fingerprint of our baseline audio...
        AudioFingerprint fp2 = AudioFingerprint.calcFP(getReferenceAudio());

        // and compare the two
        double comparison = fp1.compare(fp2);

        // finally, we assert that the comparison is sufficiently strong
        Assert.assertThat(comparison, Matchers.greaterThanOrEqualTo(70.0));
    }
}

class AudioFingerprint {

    private static String FPCALC = "/Users/jlipps/Desktop/chromaprint/fpcalc";

    private String fingerprint;

    AudioFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getFingerprint() { return fingerprint; }

    public double compare(AudioFingerprint other) {
        return FuzzySearch.partialRatio(this.getFingerprint(), other.getFingerprint());
    }

    public static AudioFingerprint calcFP(File wavFile) throws Exception {
        String output = new ProcessExecutor()
            .command(FPCALC, "-raw", wavFile.getAbsolutePath())
            .readOutput(true).execute()
            .outputUTF8();

        Pattern fpPattern = Pattern.compile("^FINGERPRINT=(.+)$", Pattern.MULTILINE);
        Matcher fpMatcher = fpPattern.matcher(output);

        String fingerprint = null;

        if (fpMatcher.find()) {
            fingerprint = fpMatcher.group(1);
        }

        if (fingerprint == null) {
            throw new Exception("Could not get fingerprint via Chromaprint fpcalc");
        }

        return new AudioFingerprint(fingerprint);
    }
}
