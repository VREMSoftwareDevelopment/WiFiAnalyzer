/*
 * WiFi Analyzer
 * Copyright (C) 2017  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.wifi.band;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.List;

public abstract class WiFiChannels {
    public static final Pair<WiFiChannel, WiFiChannel> UNKNOWN = new Pair<>(WiFiChannel.UNKNOWN, WiFiChannel.UNKNOWN);
    public static final int FREQUENCY_SPREAD = 5;
    public static final int CHANNEL_OFFSET = 2;
    public static final int FREQUENCY_OFFSET = FREQUENCY_SPREAD * CHANNEL_OFFSET;

    private final Pair<Integer, Integer> wiFiRange;
    private final List<Pair<WiFiChannel, WiFiChannel>> wiFiChannelPairs;

    WiFiChannels(@NonNull Pair<Integer, Integer> wiFiRange, @NonNull List<Pair<WiFiChannel, WiFiChannel>> wiFiChannelPairs) {
        this.wiFiRange = wiFiRange;
        this.wiFiChannelPairs = wiFiChannelPairs;
    }

    public boolean isInRange(int frequency) {
        return frequency >= wiFiRange.first && frequency <= wiFiRange.second;
    }

    public WiFiChannel getWiFiChannelByFrequency(int frequency) {
        if (isInRange(frequency)) {
            for (Pair<WiFiChannel, WiFiChannel> wiFiChannelPair : wiFiChannelPairs) {
                WiFiChannel wiFiChannel = getWiFiChannel(frequency, wiFiChannelPair);
                if (!WiFiChannel.UNKNOWN.equals(wiFiChannel)) {
                    return wiFiChannel;
                }
            }
        }
        return WiFiChannel.UNKNOWN;
    }

    WiFiChannel getWiFiChannelByChannel(int channel) {
        for (Pair<WiFiChannel, WiFiChannel> wiFiChannelPair : wiFiChannelPairs) {
            WiFiChannel first = wiFiChannelPair.first;
            WiFiChannel last = wiFiChannelPair.second;
            if (channel >= first.getChannel() && channel <= last.getChannel()) {
                int frequency = first.getFrequency() + ((channel - first.getChannel()) * FREQUENCY_SPREAD);
                return new WiFiChannel(channel, frequency);
            }
        }
        return WiFiChannel.UNKNOWN;
    }

    public WiFiChannel getWiFiChannelFirst() {
        return wiFiChannelPairs.get(0).first;
    }

    public WiFiChannel getWiFiChannelLast() {
        return wiFiChannelPairs.get(wiFiChannelPairs.size() - 1).second;
    }

    public List<WiFiChannel> getWiFiChannels() {
        List<WiFiChannel> results = new ArrayList<>();
        for (Pair<WiFiChannel, WiFiChannel> wiFiChannelPair : wiFiChannelPairs) {
            for (int channel = wiFiChannelPair.first.getChannel(); channel <= wiFiChannelPair.second.getChannel(); channel++) {
                results.add(getWiFiChannelByChannel(channel));
            }
        }
        return results;
    }

    WiFiChannel getWiFiChannel(int frequency, @NonNull Pair<WiFiChannel, WiFiChannel> wiFiChannelPair) {
        WiFiChannel first = wiFiChannelPair.first;
        WiFiChannel last = wiFiChannelPair.second;
        int channel = (int) (((double) (frequency - first.getFrequency()) / FREQUENCY_SPREAD) + first.getChannel() + 0.5);
        if (channel >= first.getChannel() && channel <= last.getChannel()) {
            return new WiFiChannel(channel, frequency);
        }
        return WiFiChannel.UNKNOWN;
    }

    public abstract List<WiFiChannel> getAvailableChannels(String countryCode);

    public abstract boolean isChannelAvailable(String countryCode, int channel);

    public abstract List<Pair<WiFiChannel, WiFiChannel>> getWiFiChannelPairs();

    public abstract Pair<WiFiChannel, WiFiChannel> getWiFiChannelPairFirst(String countryCode);

    public abstract WiFiChannel getWiFiChannelByFrequency(int frequency, @NonNull Pair<WiFiChannel, WiFiChannel> wiFiChannelPair);
}