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

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class WiFiChannelsGHZ5 extends WiFiChannels {
    private static final Pair<Integer, Integer> RANGE = new Pair<>(4900, 5899);
    private static final List<Pair<WiFiChannel, WiFiChannel>> SETS = Arrays.asList(
        new Pair<>(new WiFiChannel(36, 5180), new WiFiChannel(64, 5320)),
        new Pair<>(new WiFiChannel(100, 5500), new WiFiChannel(144, 5720)),
        new Pair<>(new WiFiChannel(149, 5745), new WiFiChannel(165, 5825)));

    private static final int DEFAULT_PAIR = 0;

    WiFiChannelsGHZ5() {
        super(RANGE, SETS);
    }

    @Override
    public List<Pair<WiFiChannel, WiFiChannel>> getWiFiChannelPairs() {
        return new ArrayList<>(SETS);
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
        return getAvailableChannels(WiFiChannelCountry.get(countryCode).getChannelsGHZ5());
    }

    @Override
    public boolean isChannelAvailable(String countryCode, int channel) {
        return WiFiChannelCountry.get(countryCode).isChannelAvailableGHZ5(channel);
    }

    @Override
    public WiFiChannel getWiFiChannelByFrequency(int frequency, @NonNull Pair<WiFiChannel, WiFiChannel> wiFiChannelPair) {
        return isInRange(frequency) ? getWiFiChannel(frequency, wiFiChannelPair) : WiFiChannel.UNKNOWN;
    }
}
