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
    GHZ2("2.4 GHz", new WiFiChannelsGHZ2()),
    GHZ5("5 GHz", new WiFiChannelsGHZ5());

    private final String band;
    private final WiFiChannels wiFiChannels;

    WiFiBand(@NonNull String band, @NonNull WiFiChannels wiFiChannels) {
        this.band = band;
        this.wiFiChannels = wiFiChannels;
    }

    public static WiFiBand findByFrequency(int frequency) {
        for (WiFiBand wiFiBand : WiFiBand.values()) {
            if (wiFiBand.getWiFiChannels().isInRange(frequency)) {
                return wiFiBand;
            }
        }
        return WiFiBand.GHZ2;
    }

    public static WiFiBand find(int index) {
        try {
            return values()[index];
        } catch (Exception e) {
            return WiFiBand.GHZ2;
        }
    }

    public String getBand() {
        return band;
    }

    public WiFiBand toggle() {
        return isGHZ5() ? WiFiBand.GHZ2 : WiFiBand.GHZ5;
    }

    public boolean isGHZ5() {
        return WiFiBand.GHZ5.equals(this);
    }

    public WiFiChannels getWiFiChannels() {
        return wiFiChannels;
    }
}
