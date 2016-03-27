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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

class WiFiChannelsGHZ_5 extends WiFiChannels {
    private final static Pair<Integer, Integer> RANGE = new Pair<>(4900, 5899);
    private final static Pair<WiFiChannel, WiFiChannel> SET0 = new Pair<>(new WiFiChannel(7, 5035), new WiFiChannel(16, 5080));
    private final static Pair<WiFiChannel, WiFiChannel> SET1 = new Pair<>(new WiFiChannel(36, 5180), new WiFiChannel(64, 5320));
    private final static Pair<WiFiChannel, WiFiChannel> SET2 = new Pair<>(new WiFiChannel(100, 5500), new WiFiChannel(140, 5700));
    private final static Pair<WiFiChannel, WiFiChannel> SET3 = new Pair<>(new WiFiChannel(149, 5745), new WiFiChannel(165, 5825));
    private final static Pair<WiFiChannel, WiFiChannel> SET4 = new Pair<>(new WiFiChannel(183, 4915), new WiFiChannel(196, 4980));
    private final static List<Pair<WiFiChannel, WiFiChannel>> SETS = Arrays.asList(SET0, SET1, SET2, SET3, SET4);

    private final static int FREQUENCY_OFFSET = WiFiChannel.FREQUENCY_SPREAD * 4;
    private final static int FREQUENCY_SPREAD = WiFiChannel.FREQUENCY_SPREAD;

    WiFiChannelsGHZ_5() {
        super(RANGE, SETS, FREQUENCY_OFFSET, FREQUENCY_SPREAD);
    }

    @Override
    public List<Pair<WiFiChannel, WiFiChannel>> getWiFiChannelPairs() {
        return Collections.unmodifiableList(SETS);
    }

    public Pair<WiFiChannel, WiFiChannel> getWiFiChannelFirstPair() {
        return SET1;
    }

    @Override
    public List<WiFiChannel> getAvailableChannels(@NonNull Locale locale) {
        List<WiFiChannel> wiFiChannels = new ArrayList<>();
        for (int channel : WiFiChannelCountry.find(locale).getChannelsGHZ_5()) {
            wiFiChannels.add(getWiFiChannelByChannel(channel));
        }
        return wiFiChannels;
    }

    @Override
    public boolean isChannelAvailable(Locale locale, int channel) {
        return WiFiChannelCountry.find(locale).isChannelAvailableGHZ_5(channel);
    }

    @Override
    public WiFiChannel getWiFiChannelByFrequency(int frequency, @NonNull Pair<WiFiChannel, WiFiChannel> wiFiChannelPair) {
        return isInRange(frequency) ? getWiFiChannel(frequency, wiFiChannelPair) : WiFiChannel.UNKNOWN;
    }
}
