import io.appium.java_client.ios.IOSDriver;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Edition049_Holidays_2018 {
    private IOSDriver driver;
    private WebDriverWait wait;

    private static String CHARITY_URL = "https://www.gifttool.com/donations/Donate?ID=1676&AID=1607";
    private static String DONATION_AMT = "25";
    private static String EMAIL = "your@email.com";
    private static String FIRST_NAME = "Your";
    private static String LAST_NAME = "Name";
    private static String STREET = "123 Main Street";
    private static String CITY = "Vancouver";
    private static String COUNTRY_CODE = "CA"; // 2-digit country code
    private static String STATE_CODE = "BC";
    private static String POSTAL_CODE = "V6K 123";
    private static String PHONE = "555-555-5555";
    private static String CARD_NAME = FIRST_NAME + " " + LAST_NAME;
    private static String CARD_NUMBER = "1234 5678 9012 3456";
    private static String CVV2 = "123";
    private static String CARD_TYPE = "Visa"; // 'Visa' or 'Mastercard'
    private static String CARD_MONTH = "01";  // 2-digit month
    private static String CARD_YEAR = "21";  // 2-digit year

    private static By donationAmt = By.id("ContributionAmount");
    private static By donorEmail = By.name("Email");
    private static By donorFirstName = By.name("FirstName");
    private static By donorLastName = By.name("LastName");
    private static By donorStreet = By.name("Street");
    private static By donorCity = By.name("City");
    private static By donorCountry = By.id("Country");
    private static By donorState = By.id("State");
    private static By donorPostalCode = By.name("ZipCode");
    private static By donorPhone = By.name("PhoneDay");
    private static By donorCardName = By.name("CardName");
    private static By donorCardNumber = By.name("CardNumber");
    private static By donorCVV2 = By.name("CVV2");
    private static By donorCardType = By.name("CardType");
    private static By donorCardMonth = By.name("ExpiryMonth");
    private static By donorCardYear = By.name("ExpiryYear");
    private static By submitDonation = By.id("gtSubmitButton");

    @Before
    public void setUp() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "11.4");
        capabilities.setCapability("deviceName", "iPhone 6");
        capabilities.setCapability("browserName", "Safari");

        driver = new IOSDriver<>(new URL("http://localhost:4723/wd/hub"), capabilities);
        wait = new WebDriverWait(driver, 10);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testHolidayCheer() {
        driver.get(CHARITY_URL);
        wait.until(ExpectedConditions.presenceOfElementLocated(donationAmt)).sendKeys(DONATION_AMT);
        driver.findElement(donorEmail).sendKeys(EMAIL);
        driver.findElement(donorFirstName).sendKeys(FIRST_NAME);
        driver.findElement(donorLastName).sendKeys(LAST_NAME);
        driver.findElement(donorStreet).sendKeys(STREET);
        driver.findElement(donorCity).sendKeys(CITY);
        new Select(driver.findElement(donorCountry)).selectByValue(COUNTRY_CODE);
        new Select(driver.findElement(donorState)).selectByValue(STATE_CODE);
        driver.findElement(donorPostalCode).sendKeys(POSTAL_CODE);
        driver.findElement(donorPhone).sendKeys(PHONE);
        driver.findElement(donorCardName).sendKeys(CARD_NAME);
        driver.findElement(donorCardNumber).sendKeys(CARD_NUMBER);
        driver.findElement(donorCVV2).sendKeys(CVV2);
        new Select(driver.findElement(donorCardType)).selectByValue(CARD_TYPE);
        new Select(driver.findElement(donorCardMonth)).selectByValue(CARD_MONTH);
        new Select(driver.findElement(donorCardYear)).selectByValue(CARD_YEAR);
        driver.findElement(submitDonation).click();
    }
}
