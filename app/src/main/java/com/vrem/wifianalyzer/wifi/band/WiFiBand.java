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
    GHZ_2("2.4 GHz", 2401, 2499, new WiFiChannels2()),
    GHZ_5("5 GHz", 4901, 5899, new WiFiChannels5());

    private final String band;
    private final int frequencyStart;
    private final int frequencyEnd;
    private final WiFiChannels wiFiChannels;

    WiFiBand(@NonNull String band, int frequencyStart, int frequencyEnd, @NonNull WiFiChannels wiFiChannels) {
        this.band = band;
        this.frequencyStart = frequencyStart;
        this.frequencyEnd = frequencyEnd;
        this.wiFiChannels = wiFiChannels;
    }

    public static WiFiBand findByFrequency(int frequency) {
        for (WiFiBand wiFiBand : WiFiBand.values()) {
            if (wiFiBand.inRange(frequency)) {
                return wiFiBand;
            }
        }
        return WiFiBand.GHZ_2;
    }

    public static WiFiBand findByBand(String band) {
        for (WiFiBand wiFiBand : WiFiBand.values()) {
            if (wiFiBand.getBand().equals(band)) {
                return wiFiBand;
            }
        }
        return WiFiBand.GHZ_2;
    }

    private boolean inRange(int value) {
        return value >= frequencyStart && value <= frequencyEnd;
    }

    public String getBand() {
        return band;
    }

    public WiFiBand toggle() {
        return WiFiBand.GHZ_2.equals(this) ? WiFiBand.GHZ_5 : WiFiBand.GHZ_2;
    }

    public WiFiChannels getWiFiChannels() {
        return wiFiChannels;
    }
}
