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

import android.support.annotation.NonNull;

import org.apache.commons.lang3.StringUtils;

public enum Frequency {
    UNKNOWN(0, 0, 0, StringUtils.EMPTY),
    TWO_POINT_FOUR(2412, 2472, 1, "2.4GHz"),
    TWO_POINT_FOUR_CH_14(2484, 2484, 14, "2.4GHz"),
    FIVE(5170, 5825, 34, "5GHz");

    private final int CHANNEL_FREQUENCY_SPREAD = 5;

    private final int start;
    private final int end;
    private final int offset;
    private final String band;

    Frequency(int start, int end, int offset, @NonNull String band) {
        this.start = start;
        this.end = end;
        this.offset = offset;
        this.band = band;
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

    public boolean inRange(int value) {
        return value >= start && value <= end;
    }

    public int channel(int value) {
        if (inRange(value)) {
            return (value - start) / CHANNEL_FREQUENCY_SPREAD + offset;
        }
        return 0;
    }

    public String getBand() {
        return band;
    }
}
