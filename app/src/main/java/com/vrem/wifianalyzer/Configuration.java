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

package com.vrem.wifianalyzer;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.vrem.wifianalyzer.wifi.band.WiFiChannel;
import com.vrem.wifianalyzer.wifi.band.WiFiChannels;

public class Configuration {
    private static final int FOUR_KB = 4096;

    private final boolean largeScreen;
    private final int size;
    private Pair<WiFiChannel, WiFiChannel> wiFiChannelPair;

    public Configuration(boolean largeScreen, int size) {
        this.largeScreen = largeScreen;
        this.size = size;
        setWiFiChannelPair(WiFiChannels.UNKNOWN);
    }

    public boolean isLargeScreen() {
        return largeScreen;
    }

    public Pair<WiFiChannel, WiFiChannel> getWiFiChannelPair() {
        return wiFiChannelPair;
    }

    public void setWiFiChannelPair(@NonNull Pair<WiFiChannel, WiFiChannel> wiFiChannelPair) {
        this.wiFiChannelPair = wiFiChannelPair;
    }

    public boolean isSizeAvailable() {
        return size == FOUR_KB;
    }
}
