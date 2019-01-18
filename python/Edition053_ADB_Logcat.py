import unittest
import time
from appium import webdriver

class Edition053_ADB_Logcat(unittest.TestCase):

    def setUp(self):
        desired_caps = {}
        desired_caps['platformName'] = 'Android'
        desired_caps['automationName'] = 'UiAutomator2'
        desired_caps['deviceName'] = 'Android Emulator'
        desired_caps['app'] = 'https://github.com/cloudgrey-io/the-app/releases/download/v1.8.1/TheApp-v1.8.1.apk'

        self.driver = webdriver.Remote('http://localhost:4723/wd/hub', desired_caps)

    def tearDown(self):
        # end the session
        self.driver.quit()

    def test_capture_logcat(self):
        # inspect available log types
        logtypes = self.driver.log_types
        print(' ,'.join(logtypes)) #

        # print first and last 10 lines of logs
        logs = self.driver.get_log('logcat')
        log_messages = list(map(lambda log: log['message'], logs))
        print('First and last ten lines of log: ')
        print('\n'.join(log_messages[:10]))
        print('...')
        print('\n'.join(log_messages[-9:]))

        # wait for more logs
        time.sleep(5)

        # demonstrate that each time get logs, we only get new logs
        # which were generated since the last time we got logs
        logs = self.driver.get_log('logcat')
        second_set_of_log_messages = list(map(lambda log: log['message'], logs))
        print('\nFirst ten lines of second log call: ')
        print('\n'.join(second_set_of_log_messages[:10]))

        assert log_messages[0] != second_set_of_log_messages[0]

if __name__ == '__main__':
    suite = unittest.TestLoader().loadTestsFromTestCase(Edition053_ADB_Logcat)
    unittest.TextTestRunner(verbosity=2).run(suite)
