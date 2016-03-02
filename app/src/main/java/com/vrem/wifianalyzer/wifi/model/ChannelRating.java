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

public class ChannelRating {
    private List<WiFiDetails> wiFiList = new ArrayList<>();

    public ChannelRating() {
    }

    public int getCount(int channel) {
        return collectOverlappingChannels(channel).size();
    }

    public Strength getStrength(int channel) {
        Strength strength = Strength.ZERO;
        for (WiFiDetails wiFiDetails : collectOverlappingChannels(channel)) {
            if (!wiFiDetails.isConnected()) {
                strength = Strength.values()[Math.max(strength.ordinal(), wiFiDetails.getStrength().ordinal())];
            }
        }
        return strength;
    }

    public void setWiFiChannels(@NonNull List<WiFiDetails> wiFiList) {
        this.wiFiList = wiFiList;
    }

    private List<WiFiDetails> collectOverlappingChannels(int channel) {
        List<WiFiDetails> result = new ArrayList<>();
        for (WiFiDetails wiFiDetails : wiFiList) {
            if (channel >= wiFiDetails.getChannelStart() && channel <= wiFiDetails.getChannelEnd()) {
                result.add(wiFiDetails);
            }
        }
        return result;
    }
}
