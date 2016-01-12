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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ChannelRating {
    private Map<Integer, List<WiFiDetails>> wifiChannels = new TreeMap<>();

    public ChannelRating() {
    }

    public int getCount(int channel) {
        return collectOverlappingChannels(channel).size();
    }

    public Strength getStrength(int channel) {
        Strength strength = Strength.ZERO;
        for (WiFiDetails wifiDetails : collectOverlappingChannels(channel)) {
            if (!wifiDetails.isConnected()) {
                strength = Strength.values()[Math.max(strength.ordinal(), wifiDetails.getStrength().ordinal())];
            }
        }
        return strength;
    }

    public void setWiFiChannels(@NonNull Map<Integer, List<WiFiDetails>> wifiChannels) {
        this.wifiChannels = wifiChannels;
    }

    private List<WiFiDetails> collectOverlappingChannels(int channel) {
        List<WiFiDetails> details = new ArrayList<>();
        for (int i = channel - WiFiData.CHANNEL_OFFSET; i <= channel + WiFiData.CHANNEL_OFFSET; i++) {
            List<WiFiDetails> wifiDetails = wifiChannels.get(i);
            if (wifiDetails != null) {
                details.addAll(wifiDetails);
            }
        }
        return details;
    }
}
