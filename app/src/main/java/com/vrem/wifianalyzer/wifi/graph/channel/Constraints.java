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
package com.vrem.wifianalyzer.wifi.graph.channel;

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

    int channelFirst() {
        return channels.first();
    }

    int channelLast() {
        return channels.last();
    }

    int boundsMin() {
        return channelFirst() - Frequency.CHANNEL_SPREAD;
    }

    int boundsMax() {
        return boundsMin() + WiFiConstants.CNT_X - 1;
    }

    int boundsSize() {
        return WiFiConstants.CNT_X;
    }

    boolean contains(int channel) {
        return channels.contains(channel);
    }

    WiFiBand getWiFiBand() {
        return wifiBand;
    }
}
