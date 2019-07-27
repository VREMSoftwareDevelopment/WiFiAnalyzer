/*
 * WiFiAnalyzer
 * Copyright (C) 2019  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.vrem.wifianalyzer.wifi.model;

import com.vrem.wifianalyzer.wifi.band.WiFiChannel;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;

public class ChannelRating {
    static final int LEVEL_RANGE_MIN = -5;
    private static final int LEVEL_RANGE_MAX = 5;
    private static final int BSSID_LENGTH = 17;

    private List<WiFiDetail> wiFiDetails;

    public ChannelRating() {
        wiFiDetails = Collections.emptyList();
    }

    public int getCount(@NonNull WiFiChannel wiFiChannel) {
        return collectOverlapping(wiFiChannel).size();
    }

    @NonNull
    public Strength getStrength(@NonNull WiFiChannel wiFiChannel) {
        Strength strength = Strength.ZERO;
        for (WiFiDetail wiFiDetail : collectOverlapping(wiFiChannel)) {
            if (!wiFiDetail.getWiFiAdditional().getWiFiConnection().isConnected()) {
                strength = Strength.values()[Math.max(strength.ordinal(), wiFiDetail.getWiFiSignal().getStrength().ordinal())];
            }
        }
        return strength;
    }

    @NonNull
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

    @NonNull
    List<WiFiDetail> getWiFiDetails() {
        return wiFiDetails;
    }

    public void setWiFiDetails(@NonNull List<WiFiDetail> wiFiDetails) {
        this.wiFiDetails = removeGuest(new ArrayList<>(wiFiDetails));
    }

    private boolean isGuest(@NonNull WiFiDetail lhs, @NonNull WiFiDetail rhs) {
        if (!isGuestBSSID(lhs.getBSSID(), rhs.getBSSID())) {
            return false;
        }
        int result = lhs.getWiFiSignal().getPrimaryFrequency() - rhs.getWiFiSignal().getPrimaryFrequency();
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

    @NonNull
    private List<WiFiDetail> collectOverlapping(@NonNull WiFiChannel wiFiChannel) {
        return new ArrayList<>(CollectionUtils.select(wiFiDetails, new InRangePredicate(wiFiChannel)));
    }

    @NonNull
    public List<ChannelAPCount> getBestChannels(@NonNull final List<WiFiChannel> wiFiChannels) {
        List<ChannelAPCount> results = new ArrayList<>(
            CollectionUtils.collect(
                CollectionUtils.select(wiFiChannels, new BestChannelPredicate())
                , new ToChannelAPCount()));
        Collections.sort(results, new ChannelAPCountSort());
        return results;
    }

    private class ChannelAPCountSort implements Comparator<ChannelAPCount> {
        @Override
        public int compare(ChannelAPCount lhs, ChannelAPCount rhs) {
            return new CompareToBuilder()
                .append(lhs.getCount(), rhs.getCount())
                .append(lhs.getWiFiChannel(), rhs.getWiFiChannel())
                .toComparison();
        }
    }


    private class GuestSort implements Comparator<WiFiDetail> {
        @Override
        public int compare(@NonNull WiFiDetail lhs, @NonNull WiFiDetail rhs) {
            Locale locale = Locale.getDefault();
            return new CompareToBuilder()
                .append(lhs.getBSSID().toUpperCase(locale), rhs.getBSSID().toUpperCase(locale))
                .append(lhs.getWiFiSignal().getPrimaryFrequency(), rhs.getWiFiSignal().getPrimaryFrequency())
                .append(rhs.getWiFiSignal().getLevel(), lhs.getWiFiSignal().getLevel())
                .append(lhs.getSSID().toUpperCase(locale), rhs.getSSID().toUpperCase(locale))
                .toComparison();
        }
    }

    private class BestChannelPredicate implements Predicate<WiFiChannel> {
        @Override
        public boolean evaluate(WiFiChannel object) {
            Strength strength = getStrength(object);
            return Strength.ZERO.equals(strength) || Strength.ONE.equals(strength);
        }
    }

    private class ToChannelAPCount implements Transformer<WiFiChannel, ChannelAPCount> {
        @Override
        public ChannelAPCount transform(WiFiChannel input) {
            return new ChannelAPCount(input, getCount(input));
        }
    }

    private class InRangePredicate implements Predicate<WiFiDetail> {
        private final WiFiChannel wiFiChannel;

        private InRangePredicate(WiFiChannel wiFiChannel) {
            this.wiFiChannel = wiFiChannel;
        }

        @Override
        public boolean evaluate(WiFiDetail wiFiDetail) {
            return wiFiDetail.getWiFiSignal().isInRange(wiFiChannel.getFrequency());
        }
    }
}
