import org.junit.Test

import io.appium.java_client.MobileElement
import io.appium.java_client.ios.IOSDriver
import org.junit.After
import org.junit.Before
import org.openqa.selenium.remote.DesiredCapabilities

import java.io.IOException
import java.net.URL

import junit.framework.TestCase.assertEquals

// Converted test "Edition077_Tuning_WDA_Startup" from Java to Kotlin just by copy-pasting.
// the `firstTest` variable was manually changed from type Boolean? to Boolean, in order to compile.
// A few comments have been cleaned up, just to make this example easier to use and read.
class Edition078_Converting_A_Test_To_Kotlin {
    private val APP : String = "https://github.com/cloudgrey-io/the-app/releases/download/v1.9.0/TheApp-v1.9.0.app.zip";

    private var driver: IOSDriver<*>? = null

    @Before
    @Throws(IOException::class)
    fun setUp() {
        val caps = DesiredCapabilities()
        caps.setCapability("platformName", "iOS")
        caps.setCapability("platformVersion", "12.2")
        caps.setCapability("deviceName", "iPhone Xs")
        caps.setCapability("automationName", "XCUITest")

        caps.setCapability("app", APP)

        caps.setCapability("noReset", true)
        caps.setCapability("udid", "009D8025-28AB-4A1B-A7C8-85A9F6FDBE95")
        caps.setCapability("bundleId", "io.cloudgrey.the-app")

        if ((!firstTest)) {
            caps.setCapability("webDriverAgentUrl", "http://localhost:8100")
        }

        driver = IOSDriver<MobileElement>(URL("http://localhost:4723/wd/hub"), caps)
    }

    @After
    fun tearDown() {
        firstTest = false
        try {
            driver!!.quit()
        } catch (ign: Exception) {
        }

    }

    @Test
    fun testA() {
        assertEquals(1, 1)
    }

    @Test
    fun testB() {
        assertEquals(1, 1)
    }

    @Test
    fun testC() {
        assertEquals(1, 1)
    }

    @Test
    fun testD() {
        assertEquals(1, 1)
    }

    @Test
    fun testE() {
        assertEquals(1, 1)
    }

    @Test
    fun testF() {
        assertEquals(1, 1)
    }

    companion object {
        private var firstTest: Boolean = true
    }
}