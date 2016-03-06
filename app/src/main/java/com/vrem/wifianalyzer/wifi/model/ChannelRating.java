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

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;

public class ChannelRating {
    private List<WiFiDetail> wiFiDetails = new ArrayList<>();

    public ChannelRating() {
    }

    public int getCount(int channel) {
        return collectOverlappingChannels(channel).size();
    }

    public Strength getStrength(int channel) {
        Strength strength = Strength.ZERO;
        for (WiFiDetail wiFiDetail : collectOverlappingChannels(channel)) {
            if (!wiFiDetail.getWiFiAdditional().isConnected()) {
                strength = Strength.values()[Math.max(strength.ordinal(), wiFiDetail.getWiFiSignal().getStrength().ordinal())];
            }
        }
        return strength;
    }

    public void setWiFiChannels(@NonNull List<WiFiDetail> wiFiDetails) {
        this.wiFiDetails = wiFiDetails;
    }

    private List<WiFiDetail> collectOverlappingChannels(int channel) {
        List<WiFiDetail> result = new ArrayList<>();
        for (WiFiDetail wiFiDetail : wiFiDetails) {
            if (wiFiDetail.getWiFiAdditional().isConnected()) {
                continue;
            }
            WiFiSignal wiFiSignal = wiFiDetail.getWiFiSignal();
            if (channel >= wiFiSignal.getChannelStart() && channel <= wiFiSignal.getChannelEnd()) {
                result.add(wiFiDetail);
            }
        }
        return result;
    }

    public List<ChannelAPCount> getBestChannels(@NonNull final SortedSet<Integer> channels) {
        List<ChannelAPCount> results = new ArrayList<>();
        for (Integer channel : channels) {
            Strength strength = getStrength(channel);
            if (Strength.ZERO.equals(strength) || Strength.ONE.equals(strength)) {
                results.add(new ChannelAPCount(channel, getCount(channel)));
            }
        }
        Collections.sort(results);
        return results;
    }

    public class ChannelAPCount implements Comparable<ChannelAPCount> {
        private final int channel;
        private final int apCount;

        public ChannelAPCount(int channel, int apCount) {
            this.channel = channel;
            this.apCount = apCount;
        }

        public int getChannel() {
            return channel;
        }

        public int getApCount() {
            return apCount;
        }

        @Override
        public int compareTo(@NonNull ChannelAPCount another) {
            return new CompareToBuilder()
                    .append(getApCount(), another.getApCount())
                    .append(getChannel(), another.getChannel())
                    .toComparison();
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }

}
