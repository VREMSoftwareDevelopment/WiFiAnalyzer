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

package com.vrem.wifianalyzer.wifi.band;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

class WiFiChannelsGHZ2 extends WiFiChannels {
    private static final Pair<Integer, Integer> RANGE = new Pair<>(2400, 2499);
    private static final List<Pair<WiFiChannel, WiFiChannel>> SETS = Arrays.asList(
        new Pair<>(new WiFiChannel(1, 2412), new WiFiChannel(13, 2472)),
        new Pair<>(new WiFiChannel(14, 2484), new WiFiChannel(14, 2484)));
    private static final Pair<WiFiChannel, WiFiChannel> SET = new Pair<>(SETS.get(0).first, SETS.get(SETS.size() - 1).second);

    WiFiChannelsGHZ2() {
        super(RANGE, SETS);
    }

    @Override
    @NonNull
    public List<Pair<WiFiChannel, WiFiChannel>> getWiFiChannelPairs() {
        return Collections.singletonList(SET);
    }

    @Override
    @NonNull
    public Pair<WiFiChannel, WiFiChannel> getWiFiChannelPairFirst(String countryCode) {
        return SET;
    }

    @Override
    @NonNull
    public List<WiFiChannel> getAvailableChannels(String countryCode) {
        return getAvailableChannels(WiFiChannelCountry.get(countryCode).getChannelsGHZ2());
    }

    @Override
    public boolean isChannelAvailable(String countryCode, int channel) {
        return WiFiChannelCountry.get(countryCode).isChannelAvailableGHZ2(channel);
    }

    @Override
    @NonNull
    public WiFiChannel getWiFiChannelByFrequency(int frequency, @NonNull Pair<WiFiChannel, WiFiChannel> wiFiChannelPair) {
        return isInRange(frequency) ? getWiFiChannel(frequency, SET) : WiFiChannel.UNKNOWN;
    }

}

