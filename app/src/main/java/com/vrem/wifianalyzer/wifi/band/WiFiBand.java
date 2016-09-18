/*
 * WiFi Analyzer
 * Copyright (C) 2016  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

public enum WiFiBand {
    GHZ2("2.4 GHz", new WiFiChannelsGHZ2()),
    GHZ5("5 GHz", new WiFiChannelsGHZ5());

    private final String band;
    private final WiFiChannels wiFiChannels;

    WiFiBand(@NonNull String band, @NonNull WiFiChannels wiFiChannels) {
        this.band = band;
        this.wiFiChannels = wiFiChannels;
    }

    public static WiFiBand findByFrequency(int frequency) {
        for (WiFiBand wiFiBand : WiFiBand.values()) {
            if (wiFiBand.getWiFiChannels().isInRange(frequency)) {
                return wiFiBand;
            }
        }
        return WiFiBand.GHZ2;
    }

    public static WiFiBand find(int index) {
        if (index < 0 || index >= values().length) {
            return GHZ2;
        }
        return values()[index];
    }

    public String getBand() {
        return band;
    }

    public WiFiBand toggle() {
        return isGHZ5() ? WiFiBand.GHZ2 : WiFiBand.GHZ5;
    }

    public boolean isGHZ5() {
        return WiFiBand.GHZ5.equals(this);
    }

    public WiFiChannels getWiFiChannels() {
        return wiFiChannels;
    }
}
