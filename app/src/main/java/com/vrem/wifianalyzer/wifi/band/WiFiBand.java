/*
 *    Copyright (C) 2015 - 2016 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.wifi.band;

import android.support.annotation.NonNull;

public enum WiFiBand {
    GHZ_2("2.4 GHz", 2401, 2499, WiFiRange.makeGHZ2()),
    GHZ_5("5 GHz", 5001, 5999, WiFiRange.makeGHZ5());

    private final String band;
    private final int frequencyStart;
    private final int frequencyEnd;
    private final WiFiRange wiFiRange;

    WiFiBand(@NonNull String band, int frequencyStart, int frequencyEnd, @NonNull WiFiRange wiFiRange) {
        this.band = band;
        this.frequencyStart = frequencyStart;
        this.frequencyEnd = frequencyEnd;
        this.wiFiRange = wiFiRange;
    }

    public static WiFiBand findByFrequency(int value) {
        for (WiFiBand wiFiBand : WiFiBand.values()) {
            if (wiFiBand.inRange(value)) {
                return wiFiBand;
            }
        }
        return WiFiBand.GHZ_2;
    }

    public static int findChannelByFrequency(int value) {
        return WiFiBand.findByFrequency(value).getChannelByFrequency(value);
    }

    public static WiFiBand findByBand(String value) {
        for (WiFiBand wiFiBand : WiFiBand.values()) {
            if (wiFiBand.getBand().equals(value)) {
                return wiFiBand;
            }
        }
        return WiFiBand.GHZ_2;
    }

    public boolean inRange(int value) {
        return value >= frequencyStart && value <= frequencyEnd;
    }

    public int getChannelByFrequency(int frequency) {
        if (!inRange(frequency)) {
            return 0;
        }
        return wiFiRange.getChannelByFrequency(frequency);
    }


    public WiFiRange getWiFiRange() {
        return wiFiRange;
    }

    public String getBand() {
        return band;
    }

    public WiFiBand toggle() {
        return WiFiBand.GHZ_2.equals(this) ? WiFiBand.GHZ_5 : WiFiBand.GHZ_2;
    }
}
