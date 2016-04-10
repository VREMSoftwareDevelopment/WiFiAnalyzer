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

import com.vrem.wifianalyzer.wifi.band.WiFiChannel;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChannelRating {
    private List<WiFiDetail> wiFiDetails = new ArrayList<>();

    public int getCount(WiFiChannel wiFiChannel) {
        return collectOverlapping(wiFiChannel).size();
    }

    public Strength getStrength(WiFiChannel wiFiChannel) {
        Strength strength = Strength.ZERO;
        for (WiFiDetail wiFiDetail : collectOverlapping(wiFiChannel)) {
            if (!wiFiDetail.getWiFiAdditional().isConnected()) {
                strength = Strength.values()[Math.max(strength.ordinal(), wiFiDetail.getWiFiSignal().getStrength().ordinal())];
            }
        }
        return strength;
    }

    public void setWiFiChannels(@NonNull List<WiFiDetail> wiFiDetails) {
        this.wiFiDetails = wiFiDetails;
    }

    private List<WiFiDetail> collectOverlapping(WiFiChannel wiFiChannel) {
        List<WiFiDetail> result = new ArrayList<>();
        for (WiFiDetail wiFiDetail : wiFiDetails) {
            if (wiFiDetail.getWiFiAdditional().isConnected()) {
                continue;
            }
            WiFiSignal wiFiSignal = wiFiDetail.getWiFiSignal();
            if (wiFiChannel.getFrequency() >= wiFiSignal.getFrequencyStart() && wiFiChannel.getFrequency() <= wiFiSignal.getFrequencyEnd()) {
                result.add(wiFiDetail);
            }
        }
        return result;
    }

    public List<ChannelAPCount> getBestChannels(@NonNull final List<WiFiChannel> wiFiChannels) {
        List<ChannelAPCount> results = new ArrayList<>();
        for (WiFiChannel wiFiChannel : wiFiChannels) {
            Strength strength = getStrength(wiFiChannel);
            if (Strength.ZERO.equals(strength) || Strength.ONE.equals(strength)) {
                results.add(new ChannelAPCount(wiFiChannel, getCount(wiFiChannel)));
            }
        }
        Collections.sort(results);
        return results;
    }

    public class ChannelAPCount implements Comparable<ChannelAPCount> {
        private final WiFiChannel wiFiChannel;
        private final int count;

        public ChannelAPCount(WiFiChannel wiFiChannel, int count) {
            this.wiFiChannel = wiFiChannel;
            this.count = count;
        }

        public WiFiChannel getWiFiChannel() {
            return wiFiChannel;
        }

        public int getCount() {
            return count;
        }

        @Override
        public int compareTo(@NonNull ChannelAPCount another) {
            return new CompareToBuilder()
                    .append(getCount(), another.getCount())
                    .append(getWiFiChannel(), another.getWiFiChannel())
                    .toComparison();
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }

}
