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
package com.vrem.wifianalyzer.wifi.model;

import android.support.annotation.NonNull;

import java.util.SortedSet;
import java.util.TreeSet;

public enum WiFiBand {
    TWO(2412, 2462, 2401, 2499, 1, 11, "2.4 GHz"),
    FIVE(5180, 5825, 5001, 5999, 36, 165, "5 GHz");

    public static final int CHANNEL_FREQUENCY_SPREAD = 5;
    public static final int CHANNEL_SPREAD = 2;

    private final int channelFrequencyStart;
    private final int channelFrequencyEnd;
    private final int start;
    private final int end;
    private final int channelFirst;
    private final int channelLast;
    private final String band;

    WiFiBand(int channelFrequencyStart, int channelFrequencyEnd, int start, int end, int channelFirst, int channelLast, @NonNull String band) {
        this.channelFrequencyStart = channelFrequencyStart;
        this.channelFrequencyEnd = channelFrequencyEnd;
        this.start = start;
        this.end = end;
        this.channelFirst = channelFirst;
        this.channelLast = channelLast;
        this.band = band;
    }

    public static WiFiBand findByFrequency(int value) {
        for (WiFiBand wiFiBand : WiFiBand.values()) {
            if (wiFiBand.inRange(value)) {
                return wiFiBand;
            }
        }
        return WiFiBand.TWO;
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
        return WiFiBand.TWO;
    }

    public boolean inRange(int value) {
        return value >= start && value <= end;
    }

    public int getChannelByFrequency(int value) {
        if (inRange(value)) {
            if (value <= channelFrequencyStart) {
                return channelFirst;
            }
            if (value >= channelFrequencyEnd) {
                return channelLast;
            }
            return (value - channelFrequencyStart) / CHANNEL_FREQUENCY_SPREAD + channelFirst;
        }
        return 0;
    }

    public SortedSet<Integer> getChannels() {
        SortedSet<Integer> results = new TreeSet<>();
        if (channelFirst != channelLast) {
            for (int i = channelFirst; i <= channelLast; i++) {
                results.add(i);
            }
        }
        return results;
    }

    public int getChannelFirst() {
        return channelFirst;
    }

    public int getChannelLast() {
        return channelLast;
    }

    public String getBand() {
        return band;
    }
}
