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

package com.vrem.wifianalyzer.wifi.band;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.List;

public abstract class WiFiChannels {
    public static final Pair<WiFiChannel, WiFiChannel> UNKNOWN = new Pair<>(WiFiChannel.UNKNOWN, WiFiChannel.UNKNOWN);

    private final Pair<Integer, Integer> wiFiRange;
    private final List<Pair<WiFiChannel, WiFiChannel>> wiFiChannelPairs;
    private final int frequencyOffset;
    private final int frequencySpread;

    protected WiFiChannels(@NonNull Pair<Integer, Integer> wiFiRange, @NonNull List<Pair<WiFiChannel, WiFiChannel>> wiFiChannelPairs, int frequencyOffset, int frequencySpread) {
        this.wiFiRange = wiFiRange;
        this.wiFiChannelPairs = wiFiChannelPairs;
        this.frequencyOffset = frequencyOffset;
        this.frequencySpread = frequencySpread;
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

    public WiFiChannel getWiFiChannelByChannel(int channel) {
        for (Pair<WiFiChannel, WiFiChannel> wiFiChannelPair : wiFiChannelPairs) {
            WiFiChannel first = wiFiChannelPair.first;
            WiFiChannel last = wiFiChannelPair.second;
            if (channel >= first.getChannel() && channel <= last.getChannel()) {
                int frequency = first.getFrequency() + ((channel - first.getChannel()) * WiFiChannel.FREQUENCY_SPREAD);
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

    public int getFrequencyOffset() {
        return frequencyOffset;
    }

    public int getChannelOffset() {
        return frequencyOffset / WiFiChannel.FREQUENCY_SPREAD;
    }

    public int getFrequencySpread() {
        return frequencySpread;
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

    protected WiFiChannel getWiFiChannel(int frequency, @NonNull Pair<WiFiChannel, WiFiChannel> wiFiChannelPair) {
        WiFiChannel first = wiFiChannelPair.first;
        WiFiChannel last = wiFiChannelPair.second;
        int channel = (int) (((double) (frequency - first.getFrequency()) / WiFiChannel.FREQUENCY_SPREAD) + first.getChannel() + 0.5);
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