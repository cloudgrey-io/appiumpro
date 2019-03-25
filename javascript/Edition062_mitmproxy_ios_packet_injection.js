let test = require('ava')
let { remote } = require('webdriverio')
let Mitmproxy = require('mitmproxy').default

let proxy, driver
let interceptedMessages = []

// this is the response we will return from our proxy, instead of what the site usually returns
let injectedResponse = {
  date: "January 1",
  url: "https://wikipedia.org/wiki/January_1",
  data: {
    Events: [
      {
        year: "2019",
        text: "Tests Passed",
      }
    ]
  }
}

let requestHandler = (message) => {
  message.setResponseBody(Buffer.from(JSON.stringify(injectedResponse), 'utf8'))
}

test.before(async t => {
  proxy = await Mitmproxy.Create(requestHandler, [], true, true)

  driver = await remote({
    hostname: 'localhost',
    port: 4723,
    path: '/wd/hub',
    capabilities: {
      platformName: 'iOS',
      platformVersion: '12.1',
      deviceName: 'iPhone XS',
      automationName: 'XCUITest',
      app: 'https://github.com/cloudgrey-io/the-app/releases/download/v1.9.0/TheApp-v1.9.0.app.zip' // in order to download, you may need to install the mitmproxy certificate on your operating system first. Or download the app and replace this capability with the path to your app.
    },
    logLevel: 'silent'
  })
})

test(`insert our own event for a day, assert that it's displayed`, async t => {
  let pickerDemo = await driver.$('~Picker Demo')
  await pickerDemo.click()
  let button = await driver.$('~learnMore')
  await button.click()

  // wait for alert
  let alertIsPresent = async () => {
    try { return await driver.getAlertText(); } catch { return false; }
  }
  await driver.waitUntil(alertIsPresent, 4000)

  let alertText = await driver.getAlertText()
  await driver.dismissAlert()

  // assert that the alertText is the same as the packet we injected
  t.true(/Tests Passed/.test(alertText))
})

test.after.always(async t => {
  t.log('shutting down')
  await proxy.shutdown()
  await driver.deleteSession()
})
