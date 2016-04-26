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

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class WiFiChannelsGHZ5 extends WiFiChannels {
    private static final Pair<Integer, Integer> RANGE = new Pair<>(4900, 5899);
    private static final List<Pair<WiFiChannel, WiFiChannel>> SETS = Arrays.asList(
            new Pair<>(new WiFiChannel(8, 5040), new WiFiChannel(16, 5080)),
            new Pair<>(new WiFiChannel(36, 5180), new WiFiChannel(64, 5320)),
            new Pair<>(new WiFiChannel(100, 5500), new WiFiChannel(140, 5700)),
            new Pair<>(new WiFiChannel(149, 5745), new WiFiChannel(165, 5825)),
            new Pair<>(new WiFiChannel(184, 4910), new WiFiChannel(196, 4980)));

    private static final int FREQUENCY_OFFSET = WiFiChannel.FREQUENCY_SPREAD * 4;
    private static final int FREQUENCY_SPREAD = WiFiChannel.FREQUENCY_SPREAD;
    private static final int DEFAULT_PAIR = 1;

    WiFiChannelsGHZ5() {
        super(RANGE, SETS, FREQUENCY_OFFSET, FREQUENCY_SPREAD);
    }

    @Override
    public List<Pair<WiFiChannel, WiFiChannel>> getWiFiChannelPairs() {
        return Collections.unmodifiableList(SETS);
    }

    @Override
    public Pair<WiFiChannel, WiFiChannel> getWiFiChannelPairFirst(String countryCode) {
        List<Pair<WiFiChannel, WiFiChannel>> wiFiChannelPairs = getWiFiChannelPairs();
        if (!StringUtils.isBlank(countryCode)) {
            for (Pair<WiFiChannel, WiFiChannel> wiFiChannelPair : wiFiChannelPairs) {
                if (isChannelAvailable(countryCode, wiFiChannelPair.first.getChannel())) {
                    return wiFiChannelPair;
                }
            }
        }
        return wiFiChannelPairs.get(DEFAULT_PAIR);
    }

    @Override
    public List<WiFiChannel> getAvailableChannels(String countryCode) {
        List<WiFiChannel> wiFiChannels = new ArrayList<>();
        for (int channel : WiFiChannelCountry.find(countryCode).getChannelsGHZ5()) {
            wiFiChannels.add(getWiFiChannelByChannel(channel));
        }
        return wiFiChannels;
    }

    @Override
    public boolean isChannelAvailable(String countryCode, int channel) {
        return WiFiChannelCountry.find(countryCode).isChannelAvailableGHZ5(channel);
    }

    @Override
    public WiFiChannel getWiFiChannelByFrequency(int frequency, @NonNull Pair<WiFiChannel, WiFiChannel> wiFiChannelPair) {
        return isInRange(frequency) ? getWiFiChannel(frequency, wiFiChannelPair) : WiFiChannel.UNKNOWN;
    }
}
