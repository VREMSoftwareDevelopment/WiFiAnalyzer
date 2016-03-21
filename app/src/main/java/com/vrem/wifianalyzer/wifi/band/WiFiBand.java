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

import java.util.Collections;
import java.util.List;

public enum WiFiBand {
    GHZ_2("2.4 GHz", 2402, 2499, WiFiChannel.GHZ_2),
    GHZ_5("5 GHz", 4900, 5900, WiFiChannel.GHZ_5);

    private final String band;
    private final int frequencyStart;
    private final int frequencyEnd;
    private final List<WiFiChannel> channels;

    WiFiBand(@NonNull String band, int frequencyStart, int frequencyEnd, @NonNull List<WiFiChannel> channels) {
        this.band = band;
        this.frequencyStart = frequencyStart;
        this.frequencyEnd = frequencyEnd;
        this.channels = channels;
    }

    public static WiFiBand findByFrequency(int value) {
        for (WiFiBand wiFiBand : WiFiBand.values()) {
            if (wiFiBand.inRange(value)) {
                return wiFiBand;
            }
        }
        return WiFiBand.GHZ_2;
    }

    public static WiFiBand findByBand(String value) {
        for (WiFiBand wiFiBand : WiFiBand.values()) {
            if (wiFiBand.getBand().equals(value)) {
                return wiFiBand;
            }
        }
        return WiFiBand.GHZ_2;
    }

    public WiFiChannel findChannel(int frequency) {
        if (inRange(frequency)) {
            for (WiFiChannel current : channels) {
                if (frequency >= current.getFrequencyStart() && frequency <= current.getFrequencyEnd()) {
                    return current;
                }
            }
        }
        return WiFiChannel.UNKNOWN;
    }

    public boolean inRange(int value) {
        return value >= frequencyStart && value <= frequencyEnd;
    }

    public String getBand() {
        return band;
    }

    public WiFiBand toggle() {
        return WiFiBand.GHZ_2.equals(this) ? WiFiBand.GHZ_5 : WiFiBand.GHZ_2;
    }

    public int getFrequencyStart() {
        return frequencyStart;
    }

    public int getFrequencyEnd() {
        return frequencyEnd;
    }

    public List<WiFiChannel> getWiFiChannels() {
        return Collections.unmodifiableList(channels);
    }
}
