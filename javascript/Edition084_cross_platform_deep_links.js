/**
 * Create a cross platform solution for opening a deep link
 *
 * @param {string} url
 */
export function openDeepLinkUrl(url) {
   const prefix = 'theapp://';

   // WebdriverIO provides the `isIOS` property to determine if the running instance
   // is iOS or Android
   if (driver.isIOS) {
       // This one was optional
       driver.execute('mobile: terminateApp', { bundleId: 'io.cloudgrey.the-app' });

       // Launch Safari to open the deep link
       driver.execute('mobile: launchApp', { bundleId: 'com.apple.mobilesafari' });

       // Add the deep link url in Safari in the `URL`-field
       // This can be 2 different elements, or the button, or the text field
       // Use the predicate string because  the accessibility label will return 2 different types
       // of elements making it flaky to use. With predicate string we can be more precise
       const urlButtonSelector = 'type == \'XCUIElementTypeButton\' && name CONTAINS \'URL\'';
       const urlFieldSelector = 'type == \'XCUIElementTypeTextField\' && name CONTAINS \'URL\'';
       const urlButton = $(`-ios predicate string:${ urlButtonSelector }`);
       const urlField = $(`-ios predicate string:${ urlFieldSelector }`);

       // Wait for the url button to appear and click on it so the text field will appear
       urlButton.waitForDisplayed(15000);
       urlButton.click();

       // Submit the url and add a break
       urlField.setValue(`${ prefix }${ url }\uE007`);

       // Wait for the notification and accept it
       const openSelector = 'type == \'XCUIElementTypeButton\' && name CONTAINS \'Open\'';
       const openButton = $(`-ios predicate string:${ openSelector }`);
       openButton.waitForDisplayed(15000);

       return openButton.click();
   }
   // Life is so much easier =)
   return driver.execute('mobile:deepLink', {
       url: `${ prefix }${ url }`,
       package: "io.cloudgrey.the_app",
   });
}
