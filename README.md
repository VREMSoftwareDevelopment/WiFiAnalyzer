<h1>WiFiAnalyzer <img src="images/icon.png" alt="Application Icon" width="45" height="45"></h1>

[<img src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" alt="Get it on Google Play" height="80">](https://play.google.com/store/apps/details?id=com.vrem.wifianalyzer)
[<img src="https://f-droid.org/badge/get-it-on.png" alt="Get it on F-Droid" height="80">](https://f-droid.org/repository/browse/?fdid=com.vrem.wifianalyzer)
[<img src="https://raw.githubusercontent.com/andOTP/andOTP/master/assets/badges/get-it-on-github.png" alt="Get it on GitHub" height="80">](https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/releases/latest)

This is the official repository of WiFiAnalyzer.

<img src="images/feature-graphic.png" alt="Feature Graphic - Dark Theme" height="200">
<img src="images/feature-graphic-light.png" alt="Feature Graphic - Light Theme" height="200">

## Table of Contents

- [Features](#features)
- [Usage Tips](#usage-tips)
- [How-to](#how-to)
- [FAQ](#faq)
- [Feedback](#feedback)
- [License](#license)
- [Privacy policy](#privacy-policy)
- [WiFiAnalyzer Build](#wifi-analyzer-build)
- [Contribute](#contribute)

## Features

* Identify nearby Access Points
* Graph channels signal strength
* Graph Access Point signal strength over time
* Analyze Wi-Fi networks to rate channels
* HT/VHT Detection - 40/80/160/320 MHz (Requires hardware/software support)
* 2.4 GHz, 5 GHz and 6 GHz Wi-Fi bands (Requires hardware/software support)
* Access Point view complete or compact
* Estimated Distance to the Access Points
* Export access points details
* Dark, Light and System theme available
* Pause/Resume scanning
* Available filters: Wi-Fi band, Signal strength, Security and SSID
* Vendor/OUI Database Lookup
* The application has too many features to mention them all

**Please note WiFiAnalyzer is not a Wi-Fi password cracking or phishing tool.**

## Usage Tips

* Tap the title bar to switch between 2.4, 5 and 6 GHz Wi-Fi band.
* SSID with `***` or `*hidden*` means it is hidden.
* Access Point security:
    * <img src="images/ic_lock_black_18dp.png" alt="WPA2/WPA" height="20">WPA3(SAE)/WPA2/WPA
    * <img src="images/ic_lock_outline_black_18dp.png" alt="WEP/WPS" height="20">WEP/WPS
    * <img src="images/ic_lock_open_black_18dp.png" alt="Disabled" height="20">Disabled
* Wi-Fi Standard (Requires Android OS 11+):
    * 4 - <img src="images/ic_wifi_4.png" alt="4" height="20"> - 802.11n
    * 5 - <img src="images/ic_wifi_5.png" alt="5" height="20"> - 802.11ac
    * 6 - <img src="images/ic_wifi_6.png" alt="6" height="20"> - 802.11ax
    * 7 - <img src="images/ic_wifi_7.png" alt="7" height="20"> - 802.11be

## How-to

* [How to video](https://youtu.be/JJVKja0VDR0)
* [How to find the best 5Ghz Wi-Fi Channel](https://www.maketecheasier.com/best-wifi-channel-for-5ghz-frequency/)
* [Understand Wi-Fi 4/5/6/6E/7 (802.11 n/ac/ax/be) - Make educated wireless router/AP upgrade decisions](https://www.duckware.com/tech/wifi-in-the-us.html)
* [Why Wi-Fi stinks and how to fix it](http://spectrum.ieee.org/telecom/wireless/why-wifi-stinksand-how-to-fix-it)
* [Wi-Fi channels list](https://en.wikipedia.org/wiki/List_of_WLAN_channels)
* [How is distance calculated to Access Point](https://en.wikipedia.org/wiki/Free-space_path_loss)

## FAQ

* [Android Wi-Fi scanning throttling](https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/wiki/Android-WiFi-scanning-throttling)
* [Location Service Permission Requirements](https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/wiki/Location-Service-Permission-Requirements)
* [WiFiAnalyzer on secondary user accounts](https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/wiki/WiFi-Analyzer-on-secondary-user-accounts)
* [How channel graph displays 20/40/80/160/320 MHz channel width?](https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/issues/64)
* [How to use SSID filter?](https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/issues/125)
* [WiFiAnalyzer equivalent on iOS](https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/issues/69)

## Feedback

* Bug reports can be submitted [here](https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/issues).
* To learn how to submit a bug [click here](https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/wiki/Feedback).
* [Discussions](https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/discussions)
* If you do not have GitHub account, please use [google groups](https://groups.google.com/forum/#!forum/wifi-analyzer-open-source) to discuss application features.

## License

[<img src="https://www.gnu.org/graphics/gplv3-127x51.png" alt="GPLv3" >](http://www.gnu.org/licenses/gpl-3.0.html)

WiFiAnalyzer is licensed under the GNU General Public License v3.0 (GPLv3).

### GPLv3 License key requirements:

* Disclose Source
* License and Copyright Notice
* Same License
* State Changes

[GNU General Public License v3.0 (GPLv3) Explained in Plain English](https://tldrlegal.com/license/gnu-general-public-license-v3-(gpl-3))

[GNU General Public License v3.0 (GPLv3)](http://www.gnu.org/licenses/gpl-3.0.html).

## Privacy policy

* WiFiAnalyzer does not collect any personal/device information.
* WiFiAnalyzer is designed to use as few permissions as possible. It asks for just enough to perform the analysis. 
* Plus, it is all open source so nothing is hidden! 
* Most notably, this application does not require access to internet, so you can be sure it does not send any personal/device information to any other source and it does not receive any information from other sources. 

## WiFiAnalyzer Build

[![Translation status](https://hosted.weblate.org/widget/wifianalyzer/language-badge.svg)](https://hosted.weblate.org/engage/wifianalyzer/)
[![Translation status](https://hosted.weblate.org/widget/wifianalyzer/svg-badge.svg)](https://hosted.weblate.org/engage/wifianalyzer/)

[![Workflow Status](https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/workflows/Android%20CI/badge.svg)](https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/actions?query=workflow%3A%22Android+CI%22)
[![Workflow Status](https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/workflows/Android%20Instrumentation%20Tests/badge.svg)](https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/actions?query=workflow%3A%22Android+Instrumentation+Tests%22)
[![Workflow Status](https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/workflows/CodeQL%20Analyze/badge.svg)](https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/actions?query=workflow%3A%22CodeQL+Analyze%22)

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/203eaa0583694bcca6554190513179ba)](https://app.codacy.com/gh/VREMSoftwareDevelopment/WiFiAnalyzer/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![codecov](https://codecov.io/gh/VREMSoftwareDevelopment/WiFiAnalyzer/branch/main/graph/badge.svg)](https://codecov.io/gh/VREMSoftwareDevelopment/WiFiAnalyzer)

[![Known Vulnerabilities](https://snyk.io/test/github/vremsoftwaredevelopment/wifianalyzer/badge.svg)](https://snyk.io/test/github/vremsoftwaredevelopment/wifianalyzer)
[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2FVREMSoftwareDevelopment%2FWiFiAnalyzer.svg?type=shield)](https://app.fossa.io/projects/git%2Bgithub.com%2FVREMSoftwareDevelopment%2FWiFiAnalyzer?ref=badge_shield)

[![Issues](https://img.shields.io/github/issues/VREMSoftwareDevelopment/WiFiAnalyzer.svg)](https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/issues)
[![Star](https://img.shields.io/github/stars/VREMSoftwareDevelopment/WiFiAnalyzer.svg)](https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/stargazers)
[![Fork](https://img.shields.io/github/forks/VREMSoftwareDevelopment/WiFiAnalyzer.svg)](https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/network)

## Translation

We support many languages, all done by people like you! We make use of free [Weblate's Libre hosting](https://weblate.org/en/donate/) to allow this.
This means if you don't like how a translation was done for your language, you can change it!
Or, you can add a whole new language! Visit [our Weblate project](https://hosted.weblate.org/engage/wifianalyzer/) to help.

[![Translation status](https://hosted.weblate.org/widget/wifianalyzer/multi-auto.svg?threshold=0)](https://hosted.weblate.org/engage/wifianalyzer/)

## Contribute

* Feel free to fork the project and submit your changes.
* To learn how to submit a pull request [click here](https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/wiki/Pull-Request).
* We want to express our gratitude to all [people](https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/graphs/contributors) for their contributions, such as: patches, bug solving, updates and etc.
* If you would like to contribute financially to the project please feel free to send any amount through paypal.
    * [![](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=62PA6HJ3BZL3E)
    * Thanks to every one who already donated so far!

### Fork project

[How to fork project](https://help.github.com/articles/fork-a-repo)

### Build project

1. Install Android Studio
2. Import project into Android Studio
    * In Welcome to Android Studio Screen select Import project ...
    * Select the root directory of the WiFiAnalyzer repository and click "OK".
    * WiFiAnalyzer will build automatically.

### Running lint, unit tests and jacoco coverage report

```
./gradlew lintDebug jacocoTestCoverageVerification
```

- lint report: `app/build/reports/lint-results.html`
- unit test report: `app/build/reports/tests/testDebugUnitTest/index.html`
- code coverage report: `app/build/reports/jacoco/jacocoTestReport/html/index.html`
