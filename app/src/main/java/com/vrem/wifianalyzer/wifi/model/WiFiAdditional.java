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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import androidx.annotation.NonNull;

public class WiFiAdditional {
    public static final WiFiAdditional EMPTY = new WiFiAdditional(StringUtils.EMPTY);

    private final String vendorName;
    private final WiFiConnection wiFiConnection;

    public WiFiAdditional(@NonNull String vendorName, @NonNull WiFiConnection wiFiConnection) {
        this.vendorName = vendorName;
        this.wiFiConnection = wiFiConnection;
    }

    public WiFiAdditional(@NonNull String vendorName) {
        this(vendorName, WiFiConnection.EMPTY);
    }

    @NonNull
    public String getVendorName() {
        return vendorName;
    }

    @NonNull
    public WiFiConnection getWiFiConnection() {
        return wiFiConnection;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}