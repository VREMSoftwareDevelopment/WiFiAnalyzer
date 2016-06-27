# WiFi Analyzer (open-source) for Android 5.1+

Optimize your WiFi network using WiFi Analyzer (open-source) by examining surrounding WiFi networks, measuring their signal strength as well as identifying crowded channels.

#### Note for Android OS 6 Users:
Some users running Android OS 6 (Marshmallow) have reported that the location service must be enabled for the application to see WiFi networks. Marshmallow users may need to turn on the location service, even though WiFi Analyzer does not explicitly require it. [This appears to be attributed to a potential issue in Android OS 6 that requires location service in order to see WiFi networks](https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/issues/23).

[![Download from Google Play](http://www.android.com/images/brand/android_app_on_play_large.png "Download from Google Play")](https://play.google.com/store/apps/details?id=com.vrem.wifianalyzer)
[![WiFi Analyzer on fdroid.org](https://camo.githubusercontent.com/7df0eafa4433fa4919a56f87c3d99cf81b68d01c/68747470733a2f2f662d64726f69642e6f72672f77696b692f696d616765732f632f63342f462d44726f69642d627574746f6e5f617661696c61626c652d6f6e2e706e67 "Download from fdroid.org")](https://f-droid.org/repository/browse/?fdid=com.vrem.wifianalyzer)

This is the official repository of WiFi Analyzer (open-source).

![Main Menu Screenshot](https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/raw/master/screenshots/screenshot1.png "Main Menu Screenshot") ![Access Points Screenshot](https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/raw/master/screenshots/screenshot2.png "Access Points Screenshot") ![Channel Graph Screenshot](https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/raw/master/screenshots/screenshot4.png "Channel Graph Screenshot")

## Features:
* Identify nearby Access Points
* Graph channels signal strength
* Graph Access Point signal strength over time
* Analyze WiFi networks to rate channels
 
## Usage Tips:
* Tap the title bar to switch between 2.4 and 5 GHz WiFi band.
* Swipe to refresh manually screen content. 

## How-to:
* [How to guide in Czech](http://www.svetandroida.cz/aplikace-wifi-analyzer-open-source-201605)

## Feedback:
Bug reports and feature requests can be submitted [here](https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/issues).

## License:
WiFi Analyzer is licensed under the Apache License, Version 2.0.

You can find the license text [here](http://www.apache.org/licenses/LICENSE-2.0).

## Build WiFi Analyzer:

Travis CI status [![Build Status](https://travis-ci.org/VREMSoftwareDevelopment/WiFiAnalyzer.svg?branch=master)](https://travis-ci.org/VREMSoftwareDevelopment/WiFiAnalyzer)

Codacy analysis/coverage [![Codacy Badge](https://api.codacy.com/project/badge/grade/203eaa0583694bcca6554190513179ba)](https://www.codacy.com/app/vremsoftwaredevelopment/WiFiAnalyzer) [![Codacy Badge](https://api.codacy.com/project/badge/coverage/203eaa0583694bcca6554190513179ba)](https://www.codacy.com/app/vremsoftwaredevelopment/WiFiAnalyzer)

Codecov [![codecov](https://codecov.io/gh/VREMSoftwareDevelopment/WiFiAnalyzer/branch/master/graph/badge.svg)](https://codecov.io/gh/VREMSoftwareDevelopment/WiFiAnalyzer)

### Fork project
[How to fork project](https://help.github.com/articles/fork-a-repo)
### Build project
  1. Install Android Studio
  2. Import project into Android Studio
      * In Welcome to Android Studio Screen select Import project ...
      * Select the root directory of the WiFi Analyzer repository and click "OK".
      * WiFi Analyzer will build automatically
