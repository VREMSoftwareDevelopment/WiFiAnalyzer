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

package com.vrem.wifianalyzer;

import com.vrem.wifianalyzer.wifi.band.WiFiChannel;
import com.vrem.wifianalyzer.wifi.band.WiFiChannels;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

public class Configuration {
    public static final int SIZE_MIN = 1024;
    public static final int SIZE_MAX = 4096;

    private final boolean largeScreen;
    private int size;
    private Pair<WiFiChannel, WiFiChannel> wiFiChannelPair;

    public Configuration(boolean largeScreen) {
        this.largeScreen = largeScreen;
        setSize(SIZE_MAX);
        setWiFiChannelPair(WiFiChannels.UNKNOWN);
    }

    public boolean isLargeScreen() {
        return largeScreen;
    }

    @NonNull
    public Pair<WiFiChannel, WiFiChannel> getWiFiChannelPair() {
        return wiFiChannelPair;
    }

    public void setWiFiChannelPair(@NonNull Pair<WiFiChannel, WiFiChannel> wiFiChannelPair) {
        this.wiFiChannelPair = wiFiChannelPair;
    }

    public boolean isSizeAvailable() {
        return size == SIZE_MAX;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
