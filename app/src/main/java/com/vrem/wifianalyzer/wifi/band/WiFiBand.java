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

import com.vrem.wifianalyzer.R;

import androidx.annotation.NonNull;

public enum WiFiBand {
    GHZ2(R.string.wifi_band_2ghz, new WiFiChannelsGHZ2()),
    GHZ5(R.string.wifi_band_5ghz, new WiFiChannelsGHZ5());

    private final int textResource;
    private final WiFiChannels wiFiChannels;

    WiFiBand(int textResource, @NonNull WiFiChannels wiFiChannels) {
        this.textResource = textResource;
        this.wiFiChannels = wiFiChannels;
    }

    public int getTextResource() {
        return textResource;
    }

    @NonNull
    public WiFiBand toggle() {
        return isGHZ5() ? WiFiBand.GHZ2 : WiFiBand.GHZ5;
    }

    public boolean isGHZ5() {
        return WiFiBand.GHZ5.equals(this);
    }

    @NonNull
    public WiFiChannels getWiFiChannels() {
        return wiFiChannels;
    }
}
