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

package com.vrem.wifianalyzer.export;

import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.IterableUtils;

import java.util.List;
import java.util.Locale;

import android.location.Location;
import androidx.annotation.NonNull;

public class Export {
    private final List<WiFiDetail> wiFiDetails;
    private final String timestamp;
    private final double latitude;
    private final double longitude;

    public Export(@NonNull List<WiFiDetail> wiFiDetails, @NonNull String timestamp, Location location) {
        this.wiFiDetails = wiFiDetails;
        this.timestamp = timestamp;
        if(location != null ) {
            this.latitude = location.getLatitude();
            this.longitude = location.getLongitude();
        } else {
            this.latitude = 0;
            this.longitude = 0;
       }
    }

    @NonNull
    public String getData() {
        final StringBuilder result = new StringBuilder();
        result.append(
            String.format(Locale.ENGLISH,
                "Time Stamp|SSID|BSSID|Strength|Primary Channel|Primary Frequency|Center Channel|Center Frequency|Width (Range)|Distance|802.11mc|Security|Latitude|Longitude%n"));
        IterableUtils.forEach(wiFiDetails, new WiFiDetailClosure(timestamp, result, latitude, longitude));
        return result.toString();
    }

    private class WiFiDetailClosure implements Closure<WiFiDetail> {
        private final StringBuilder result;
        private final String timestamp;
        private final double latitude;
        private final double longitude;

        private WiFiDetailClosure(String timestamp, @NonNull StringBuilder result, double latitude, double longitude) {
            this.result = result;
            this.timestamp = timestamp;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Override
        public void execute(WiFiDetail wiFiDetail) {
            WiFiSignal wiFiSignal = wiFiDetail.getWiFiSignal();
            result.append(String.format(Locale.ENGLISH, "%s|%s|%s|%ddBm|%d|%d%s|%d|%d%s|%d%s (%d - %d)|%s|%s|%s|%.7f|%.7f%n",
                timestamp,
                wiFiDetail.getSSID(),
                wiFiDetail.getBSSID(),
                wiFiSignal.getLevel(),
                wiFiSignal.getPrimaryWiFiChannel().getChannel(),
                wiFiSignal.getPrimaryFrequency(),
                WiFiSignal.FREQUENCY_UNITS,
                wiFiSignal.getCenterWiFiChannel().getChannel(),
                wiFiSignal.getCenterFrequency(),
                WiFiSignal.FREQUENCY_UNITS,
                wiFiSignal.getWiFiWidth().getFrequencyWidth(),
                WiFiSignal.FREQUENCY_UNITS,
                wiFiSignal.getFrequencyStart(),
                wiFiSignal.getFrequencyEnd(),
                wiFiSignal.getDistance(),
                wiFiSignal.is80211mc(),
                wiFiDetail.getCapabilities(),
                latitude,
                longitude));
        }
    }

}
