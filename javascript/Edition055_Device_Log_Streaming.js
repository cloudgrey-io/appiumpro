let test = require('ava')
let { remote } = require('webdriverio')
let B = require('bluebird')
let websocket = require('websocket-stream')

let driver

test('stream Android logcat logs', async t => {
  driver = await remote({
    hostname: 'localhost',
    port: 4723,
    path: '/wd/hub',
    capabilities: {
      platformName: 'Android',
      deviceName: 'Android Emulator',
      automationName: 'UiAutomator2',
      app: 'https://github.com/cloudgrey-io/the-app/releases/download/v1.8.1/TheApp-v1.8.1.apk'
    },
    logLevel: 'error'
  })

  let logStream = websocket(`ws://localhost:4723/ws/session/${driver.sessionId}/appium/device/logcat`)
  logStream.pipe(process.stdout)

  logStream.on('finish', () => {
    console.log('Connection closed, log streaming has stopped')
  })

  driver.executeScript('mobile:startLogsBroadcast', [])

  await B.delay(5000)

  driver.executeScript('mobile:stopLogsBroadcast', [])

  await driver.deleteSession()
  t.pass()
})

test('stream iOS system logs', async t => {
  driver = await remote({
    hostname: 'localhost',
    port: 4723,
    path: '/wd/hub',
    capabilities: {
      platformName: 'iOS',
      platformVersion: '12.1',
      deviceName: 'iPhone XS',
      automationName: 'XCUITest',
      app: 'https://github.com/cloudgrey-io/the-app/releases/download/v1.6.1/TheApp-v1.6.1.app.zip'
    },
    logLevel: 'error'
  })

  let logStream = websocket(`ws://localhost:4723/ws/session/${driver.sessionId}/appium/device/syslog`)
  logStream.pipe(process.stdout)

  logStream.on('finish', () => {
    console.log('Connection closed, log streaming has stopped')
  })

  driver.executeScript('mobile:startLogsBroadcast', [])

  await B.delay(5000)

  driver.executeScript('mobile:stopLogsBroadcast', [])

  await driver.deleteSession()
  t.pass()
})
