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

package com.vrem.wifianalyzer.wifi.model;

import com.vrem.wifianalyzer.wifi.band.WiFiChannel;

import org.apache.commons.lang3.builder.ToStringBuilder;

import androidx.annotation.NonNull;

public class ChannelAPCount {
    private final WiFiChannel wiFiChannel;
    private final int count;

    public ChannelAPCount(@NonNull WiFiChannel wiFiChannel, int count) {
        this.wiFiChannel = wiFiChannel;
        this.count = count;
    }

    @NonNull
    public WiFiChannel getWiFiChannel() {
        return wiFiChannel;
    }

    int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
