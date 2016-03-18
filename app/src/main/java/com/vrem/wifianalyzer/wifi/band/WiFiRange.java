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

import java.util.SortedSet;
import java.util.TreeSet;

public class WiFiRange {
    public static final int CHANNEL_FREQUENCY_SPREAD = 5;
    private static final int CHANNEL_SPREAD = 2;

    private final Range[] ranges;

    WiFiRange(@NonNull Range... ranges) {
        this.ranges = ranges;
    }

    static WiFiRange makeGHZ2() {
        return new WiFiRange(new Range(2412, 2472, 1), new Range(2484, 2484, 14));
    }

    static WiFiRange makeGHZ5() {
        return new WiFiRange(new Range(5180, 5825, 36));
    }


    int getChannelByFrequency(int frequency) {
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
}
