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
package com.vrem.wifianalyzer.wifi.channelgraph;

import android.support.annotation.NonNull;

import com.vrem.wifianalyzer.wifi.WiFiConstants;
import com.vrem.wifianalyzer.wifi.model.Frequency;
import com.vrem.wifianalyzer.wifi.model.WiFiBand;

import java.util.SortedSet;

class Constraints {
    private final WiFiBand wifiBand;
    private final SortedSet<Integer> channels;

    Constraints(@NonNull WiFiBand wifiBand) {
        this.wifiBand = wifiBand;
        this.channels = Frequency.findChannels(wifiBand);
    }

    int minX() {
        return channels.first();
    }

    int maxX() {
        return channels.last();
    }

    int cntX() {
        return WiFiConstants.CNT_X;
    }

    int minXBounds() {
        return minX() - Frequency.CHANNEL_SPREAD;
    }

    int maxXBounds() {
        return minXBounds() + WiFiConstants.CNT_X;
    }

    boolean isScrollable() {
        return !is24GHz();
    }

    boolean is24GHz() {
        return WiFiBand.TWO.equals(wifiBand);
    }

    boolean contains(int channel) {
        return channels.contains(channel);
    }

    WiFiBand getWiFiBand() {
        return wifiBand;
    }
}
