let test = require('ava')
let { remote } = require('webdriverio')
let B = require('bluebird')

let driver

test.before(async t => {
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
})

test.after(async t => {
  await driver.deleteSession()
})

test('capture logcat', async t => {
  // inspect available log types
  let logtypes = await driver.getLogTypes()
  console.log('supported log types: ', logtypes) // [ 'logcat', 'bugreport', 'server' ]

  // print first and last 10 lines of logs
  let logs = await driver.getLogs('logcat')
  console.log('First and last ten lines of logs:')
  console.log(logs.slice(0, 10).map(entry => entry.message).join('\n'))
  console.log('...')
  console.log(logs.slice(-10).map(entry => entry.message).join('\n'))

  // wait for more logs
  await B.delay(5000)

  // demonstrate that each time get logs, we only get new logs
  // which were generated since the last time we got logs
  let secondCallToLogs = await driver.getLogs('logcat')
  console.log('First ten lines of next log call: ')
  console.log(secondCallToLogs.slice(0, 10).map(entry => entry.message).join('\n'))

  t.true(logs[0].message !== secondCallToLogs[0].message)
})
