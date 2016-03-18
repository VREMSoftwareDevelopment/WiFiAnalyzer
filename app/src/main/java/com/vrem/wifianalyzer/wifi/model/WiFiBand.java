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
    GHZ_2("2.4 GHz", 2401, 2499, new Range(2412, 2472, 1), new Range(2484, 2484, 14)),
    GHZ_5("5 GHz", 5001, 5999, new Range(5180, 5825, 36));

    static final int CHANNEL_FREQUENCY_SPREAD = 5;
    private static final int CHANNEL_SPREAD = 2;

    private final String band;
    private final int frequencyStart;
    private final int frequencyEnd;
    private final Range[] ranges;

    WiFiBand(@NonNull String band, int frequencyStart, int frequencyEnd, Range... ranges) {
        this.band = band;
        this.frequencyStart = frequencyStart;
        this.frequencyEnd = frequencyEnd;
        this.ranges = ranges;
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
        for (Range range : ranges) {
            if (frequency <= range.getFrequencyStart()) {
                return range.getChannelFirst();
            }
            int channel = range.getChannelByFrequency(frequency);
            if (channel != 0) {
                return channel;
            }
        }
        return getChannelLast();
    }

    public SortedSet<Integer> getChannels() {
        SortedSet<Integer> results = new TreeSet<>();
        for (int i = getChannelFirst(); i <= getChannelLast(); i++) {
            results.add(i);
        }
        return results;
    }

    public int getFrequencyStart() {
        return ranges[0].getFrequencyStart();
    }

    public int getFrequencyEnd() {
        return ranges[ranges.length - 1].getFrequencyEnd();
    }

    public int getChannelFirst() {
        return ranges[0].getChannelFirst();
    }

    public int getChannelFirstHidden() {
        return getChannelFirst() - CHANNEL_SPREAD;
    }

    public int getChannelLast() {
        return ranges[ranges.length - 1].getChannelLast();
    }

    public int getChannelLastHidden() {
        return getChannelLast() + CHANNEL_SPREAD + 1;
    }

    public String getBand() {
        return band;
    }

    public WiFiBand toggle() {
        return WiFiBand.GHZ_2.equals(this) ? WiFiBand.GHZ_5 : WiFiBand.GHZ_2;
    }

    private static class Range {
        private final int frequencyStart;
        private final int frequencyEnd;
        private final int channelFirst;

        private Range(int frequencyStart, int frequencyEnd, int channelFirst) {
            this.frequencyStart = frequencyStart;
            this.frequencyEnd = frequencyEnd;
            this.channelFirst = channelFirst;
        }

        private int getChannelByFrequency(int value) {
            if (value >= frequencyStart && value <= frequencyEnd) {
                return (value - frequencyStart) / CHANNEL_FREQUENCY_SPREAD + channelFirst;
            }
            return 0;
        }

        private int getFrequencyStart() {
            return frequencyStart;
        }

        private int getFrequencyEnd() {
            return frequencyEnd;
        }

        private int getChannelFirst() {
            return channelFirst;
        }

        private int getChannelLast() {
            return getChannelByFrequency(getFrequencyEnd());
        }
    }
}
