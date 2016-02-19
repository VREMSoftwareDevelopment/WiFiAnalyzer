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

public enum Frequency {
    UNKNOWN(),
    TWO(2412, 2462, 2401, 2499, 1, 11, WiFiBand.TWO),
    FIVE(5180, 5825, 5001, 5999, 36, 165, WiFiBand.FIVE);

    public static final int CHANNEL_FREQUENCY_SPREAD = 5;
    public static final int CHANNEL_SPREAD = 2;

    private final int channelFrequencyStart;
    private final int channelFrequencyEnd;
    private final int start;
    private final int end;
    private final int channelFirst;
    private final int channelLast;
    private final WiFiBand wiFiBand;

    Frequency() {
        channelFrequencyStart = channelFrequencyEnd = channelFirst = channelLast = start = end = 0;
        wiFiBand = null;
    }

    Frequency(int channelFrequencyStart, int channelFrequencyEnd, int start, int end, int channelFirst, int channelLast, @NonNull WiFiBand wiFiBand) {
        this.channelFrequencyStart = channelFrequencyStart;
        this.channelFrequencyEnd = channelFrequencyEnd;
        this.start = start;
        this.end = end;
        this.channelFirst = channelFirst;
        this.channelLast = channelLast;
        this.wiFiBand = wiFiBand;
    }

    public static Frequency find(int value) {
        for (Frequency frequency : Frequency.values()) {
            if (frequency.inRange(value)) {
                return frequency;
            }
        }
        return Frequency.UNKNOWN;
    }

    public static int findChannel(int value) {
        return Frequency.find(value).channel(value);
    }

    public static SortedSet<Integer> findChannels(@NonNull WiFiBand wiFiBand) {
        SortedSet<Integer> results = new TreeSet<>();
        for (Frequency frequency : values()) {
            if (wiFiBand.equals(frequency.wiFiBand())) {
                results.addAll(frequency.channels());
            }
        }
        return results;
    }

    public boolean inRange(int value) {
        return value >= start && value <= end;
    }

    public int channel(int value) {
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

    public WiFiBand wiFiBand() {
        return wiFiBand;
    }

    public SortedSet<Integer> channels() {
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

    public WiFiBand getWiFiBand() {
        return wiFiBand;
    }
}
