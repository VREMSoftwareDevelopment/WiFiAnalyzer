# WiFi Analyzer (open-source)

<img src="https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/raw/master/app/src/main/ic_launcher-web.png" alt="Laucher Icon" height="50"> Optimize your WiFi network using WiFi Analyzer (open-source) by examining surrounding WiFi networks, measuring their signal strength as well as identifying crowded channels.

### Android OS Requirements
* Minimum: Jelly Bean (Version 4.1 - API 16)
* Recommended: Marshmallow (Version 6.0 - API 23)

### Note for Android OS 6 Users
Some users running Android OS 6 (Marshmallow) have reported that the location service must be enabled for the application to see WiFi networks. Marshmallow users may need to turn on the location service, even though WiFi Analyzer does not explicitly require it. [This appears to be attributed to Android OS 6 that requires location service in order to see WiFi networks](https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/issues/23).

[<img src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" alt="Get it on Google Play" height="100">](https://play.google.com/store/apps/details?id=com.vrem.wifianalyzer)
[<img src="https://f-droid.org/badge/get-it-on.png" alt="Get it on F-Droid" height="100">](https://f-droid.org/repository/browse/?fdid=com.vrem.wifianalyzer)

This is the official repository of WiFi Analyzer (open-source).

<img src="https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/raw/master/screenshots/feature-graphic.png" alt="Feature Graphic" height="300">

### The open-source app was created using Agile methodologies, [click here](http://i-proving.com/2016/10/13/using-agile-methodologies-develop-open-source-android-app) to learn more about it.

[<img src="https://i-proving.com/wp-content/uploads/2016/07/cropped-i-proving-logo.png" alt="i-proving" height="40">](http://i-proving.com)

## Features
* Identify nearby Access Points
* Graph channels signal strength
* Graph Access Point signal strength over time
* Analyze WiFi networks to rate channels
* HT/VHT Detection - 40/80/160MHz (Requires Android OS 6+)
* Access Point view complete or compact
* Export access points details
* Dark or Light theme available

**Please note WiFi Analyzer is not a WiFi password cracking or phishing tool.**

 
## Usage Tips
* Tap the title bar to switch between 2.4 and 5 GHz WiFi band.
* Swipe to manually refresh screen content.
* SSID with (***) means it is hidden.
* Distance to the Access Point is estimated.
* Access Point security: <img src="https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/raw/master/app/src/main/res/drawable-hdpi/ic_lock_black_18dp.png" alt="WPA2/WPA" height="20"> WPA2/WPA; <img src="https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/raw/master/app/src/main/res/drawable-hdpi/ic_lock_outline_black_18dp.png" alt="WEP/WPS" height="20"> WEP/WPS; <img src="https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/raw/master/app/src/main/res/drawable-hdpi/ic_lock_open_black_18dp.png" alt="Disabled" height="20"> Disabled
* Saved networks <img src="https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/raw/master/app/src/main/res/drawable-hdpi/ic_tag_faces_black_18dp.png" alt="Saved Networks" height="20">

## How-to
* [How to guide](http://translate.google.com/translate?js=n&sl=auto&tl=en&u=http://www.svetandroida.cz/aplikace-wifi-analyzer-open-source-201605)
* [Why Wi-Fi Stinks and How to Fix It](http://spectrum.ieee.org/telecom/wireless/why-wifi-stinksand-how-to-fix-it)
* [Wikipedia WiFi channels list](https://en.wikipedia.org/wiki/List_of_WLAN_channels)

## Feedback
Bug reports and feature requests can be submitted [here](https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/issues) or use [google groups](https://groups.google.com/forum/#!forum/wifi-analyzer-open-source) to discuss application features if you do not have GitHub account.

## License
WiFi Analyzer is licensed under the GNU General Public License v3.0 (GPLv3).

### GPLv3 License key requirements:
* Disclose Source
* License and Copyright Notice
* Same License
* State Changes

[GNU General Public License v3.0 (GPLv3) Explained in Plain English](https://tldrlegal.com/license/gnu-general-public-license-v3-(gpl-3))

[GNU General Public License v3.0 (GPLv3)](http://www.gnu.org/licenses/gpl-3.0.html).

## WiFi Analyzer Build

[![Build Status](https://travis-ci.org/VREMSoftwareDevelopment/WiFiAnalyzer.svg?branch=master)](https://travis-ci.org/VREMSoftwareDevelopment/WiFiAnalyzer)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/203eaa0583694bcca6554190513179ba)](https://www.codacy.com/app/vremsoftwaredevelopment/WiFiAnalyzer)
[![codecov](https://codecov.io/gh/VREMSoftwareDevelopment/WiFiAnalyzer/branch/master/graph/badge.svg)](https://codecov.io/gh/VREMSoftwareDevelopment/WiFiAnalyzer)
[![Dependency Status](https://www.versioneye.com/user/projects/5774046699ed290049b8bb59/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/5774046699ed290049b8bb59)

[![Issues](https://img.shields.io/github/issues/VREMSoftwareDevelopment/WiFiAnalyzer.svg)](https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/issues)
[![Star](https://img.shields.io/github/stars/VREMSoftwareDevelopment/WiFiAnalyzer.svg)](https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/stargazers)
[![Fork](https://img.shields.io/github/forks/VREMSoftwareDevelopment/WiFiAnalyzer.svg)](https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/network)

## Contribute
Feel free to fork the project and submit your changes back.

### Fork project
[How to fork project](https://help.github.com/articles/fork-a-repo)

### Build project
  1. Install Android Studio
  2. Import project into Android Studio
      * In Welcome to Android Studio Screen select Import project ...
      * Select the root directory of the WiFi Analyzer repository and click "OK".
      * WiFi Analyzer will build automatically

### Running unit tests and jacoco coverage report
```
./gradlew testDebugUnitTest jacocoTestDebugUnitTestReport --info
```

## Special Thanks

We want to express our gratitude to all people for contributed with patches, bug solving and updates to WiFi Analyzer.

## Donations

If you would like to contribute financially to the project please feel free to send any amount through paypal.

[![](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=62PA6HJ3BZL3E)

Thanks to every one who already donated so far!


