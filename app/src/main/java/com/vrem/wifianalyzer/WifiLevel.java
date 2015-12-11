/*
 *    Copyright (C) 2010 - 2015 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.vrem.wifianalyzer;

import android.net.wifi.WifiManager;

enum WifiLevel {
    ZERO(R.drawable.wifi0, R.color.wifi0),
    ONE(R.drawable.wifi1, R.color.wifi1),
    TWO(R.drawable.wifi2, R.color.wifi2),
    THREE(R.drawable.wifi3, R.color.wifi3),
    FOUR(R.drawable.wifi4, R.color.wifi4);

    private final int imageResource;
    private final int colorResource;

    WifiLevel(int imageResource, int colorResource) {
        this.imageResource = imageResource;
        this.colorResource = colorResource;
    }

    int getColorResource() {
        return colorResource;
    }

    int getImageResource() {
        return imageResource;
    }

    static WifiLevel find(int level) {
        int maxLevel = WifiLevel.values().length;
        int signalLevel = WifiManager.calculateSignalLevel(level, maxLevel);
        if (signalLevel < 0) {
            return WifiLevel.ZERO;
        }
        if (signalLevel >= maxLevel) {
            return WifiLevel.FOUR;
        }
        return WifiLevel.values()[signalLevel];
    }
}
