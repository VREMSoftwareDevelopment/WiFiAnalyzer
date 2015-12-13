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
package com.vrem.wifianalyzer.wifi;

import com.vrem.wifianalyzer.R;

public enum Strength {
    ZERO(R.drawable.ic_signal_wifi_1_bar_red_900_48dp, R.color.wifi0),
    ONE(R.drawable.ic_signal_wifi_1_bar_yellow_900_48dp, R.color.wifi1),
    TWO(R.drawable.ic_signal_wifi_2_bar_yellow_900_48dp, R.color.wifi2),
    THREE(R.drawable.ic_signal_wifi_3_bar_green_900_48dp, R.color.wifi3),
    FOUR(R.drawable.ic_signal_wifi_4_bar_green_900_48dp, R.color.wifi4);

    private final int imageResource;
    private final int colorResource;

    Strength(int imageResource, int colorResource) {
        this.imageResource = imageResource;
        this.colorResource = colorResource;
    }

    public int getColorResource() {
        return colorResource;
    }

    public int getImageResource() {
        return imageResource;
    }

    public static Strength calculate(int level) {
        return Strength.values()[calculate(level, values().length)];
    }

    private static int calculate(int rssi, int numLevels) {
        int rssiMin = -100;
        int rssiMax = -55;

        if (rssi <= rssiMin) {
            return 0;
        } else if (rssi >= rssiMax) {
            return numLevels - 1;
        } else {
            float inputRange = (rssiMax - rssiMin);
            float outputRange = (numLevels - 1);
            return (int)((float)(rssi - rssiMin) * outputRange / inputRange);
        }
    }

}
