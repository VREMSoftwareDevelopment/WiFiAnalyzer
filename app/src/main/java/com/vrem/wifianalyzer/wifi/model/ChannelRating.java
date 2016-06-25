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
import java.util.Comparator;
import java.util.List;

public class ChannelRating {
    protected static final int LEVEL_RANGE_MIN = -5;
    protected static final int LEVEL_RANGE_MAX = 5;
    protected static final int BSSID_LENGTH = 17;

    private List<WiFiDetail> wiFiDetails = new ArrayList<>();

    public int getCount(@NonNull WiFiChannel wiFiChannel) {
        return collectOverlapping(wiFiChannel).size();
    }

    public Strength getStrength(@NonNull WiFiChannel wiFiChannel) {
        Strength strength = Strength.ZERO;
        for (WiFiDetail wiFiDetail : collectOverlapping(wiFiChannel)) {
            if (!wiFiDetail.getWiFiAdditional().isConnected()) {
                strength = Strength.values()[Math.max(strength.ordinal(), wiFiDetail.getWiFiSignal().getStrength().ordinal())];
            }
        }
        return strength;
    }

    public void setWiFiDetails(@NonNull List<WiFiDetail> wiFiDetails) {
        this.wiFiDetails = removeGuest(new ArrayList<>(wiFiDetails));
    }

    private List<WiFiDetail> removeGuest(@NonNull List<WiFiDetail> wiFiDetails) {
        List<WiFiDetail> results = new ArrayList<>();
        WiFiDetail wiFiDetail = WiFiDetail.EMPTY;
        Collections.sort(wiFiDetails, new GuestSort());
        for (WiFiDetail current : wiFiDetails) {
            if (isGuest(current, wiFiDetail)) {
                continue;
            }
            results.add(current);
            wiFiDetail = current;
        }
        Collections.sort(results, SortBy.STRENGTH.comparator());
        return results;
    }

    protected List<WiFiDetail> getWiFiDetails() {
        return wiFiDetails;
    }

    private static class GuestSort implements Comparator<WiFiDetail> {
        @Override
        public int compare(@NonNull WiFiDetail lhs, @NonNull WiFiDetail rhs) {
            return new CompareToBuilder()
                .append(lhs.getBSSID().toUpperCase(), rhs.getBSSID().toUpperCase())
                .append(lhs.getWiFiSignal().getFrequency(), rhs.getWiFiSignal().getFrequency())
                .append(rhs.getWiFiSignal().getLevel(), lhs.getWiFiSignal().getLevel())
                .append(lhs.getSSID().toUpperCase(), rhs.getSSID().toUpperCase())
                .toComparison();
        }
    }

    private boolean isGuest(@NonNull WiFiDetail lhs, @NonNull WiFiDetail rhs) {
        if (!isGuestBSSID(lhs.getBSSID(), rhs.getBSSID())) {
            return false;
        }
        int result = lhs.getWiFiSignal().getFrequency() - rhs.getWiFiSignal().getFrequency();
        if (result == 0) {
            result = rhs.getWiFiSignal().getLevel() - lhs.getWiFiSignal().getLevel();
            if (result > LEVEL_RANGE_MIN || result < LEVEL_RANGE_MAX) {
                result = 0;
            }
        }
        return result == 0;
    }

    private boolean isGuestBSSID(@NonNull String lhs, @NonNull String rhs) {
        return lhs.length() == BSSID_LENGTH &&
            lhs.length() == rhs.length() &&
            lhs.substring(0, 0).equalsIgnoreCase(rhs.substring(0, 0)) &&
            lhs.substring(2, BSSID_LENGTH - 1).equalsIgnoreCase(rhs.substring(2, BSSID_LENGTH - 1));
    }

    private List<WiFiDetail> collectOverlapping(@NonNull WiFiChannel wiFiChannel) {
        List<WiFiDetail> result = new ArrayList<>();
        for (WiFiDetail wiFiDetail : wiFiDetails) {
            if (wiFiDetail.getWiFiSignal().isInRange(wiFiChannel.getFrequency())) {
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

        public ChannelAPCount(@NonNull WiFiChannel wiFiChannel, int count) {
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
