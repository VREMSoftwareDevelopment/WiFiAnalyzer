/*
 * WiFiAnalyzer
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

package com.vrem.wifianalyzer.wifi.model;

import android.support.annotation.NonNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class WiFiAdditional {
    public static final WiFiAdditional EMPTY = new WiFiAdditional(StringUtils.EMPTY, false);

    private final String vendorName;
    private final boolean configuredNetwork;
    private final WiFiConnection wiFiConnection;

    private WiFiAdditional(@NonNull String vendorName, @NonNull WiFiConnection wiFiConnection, boolean configuredNetwork) {
        this.vendorName = vendorName;
        this.wiFiConnection = wiFiConnection;
        this.configuredNetwork = configuredNetwork;
    }

    public WiFiAdditional(@NonNull String vendorName, boolean configuredNetwork) {
        this(vendorName, WiFiConnection.EMPTY, configuredNetwork);
    }

    public WiFiAdditional(@NonNull String vendorName, @NonNull WiFiConnection wiFiConnection) {
        this(vendorName, wiFiConnection, true);
    }

    public String getVendorName() {
        return vendorName;
    }

    public WiFiConnection getWiFiConnection() {
        return wiFiConnection;
    }

    public boolean isConfiguredNetwork() {
        return configuredNetwork;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}