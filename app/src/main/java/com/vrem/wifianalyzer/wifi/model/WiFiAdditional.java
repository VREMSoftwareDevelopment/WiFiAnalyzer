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

package com.vrem.wifianalyzer.wifi.model;

import android.support.annotation.NonNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class WiFiAdditional {
    public static final WiFiAdditional EMPTY = new WiFiAdditional(StringUtils.EMPTY, false);

    private final String vendorName;
    private final String ipAddress;
    private final int linkSpeed;
    private final boolean configuredNetwork;

    private WiFiAdditional(@NonNull String vendorName, @NonNull String ipAddress, int linkSpeed, boolean configuredNetwork) {
        this.vendorName = vendorName;
        this.ipAddress = ipAddress;
        this.configuredNetwork = configuredNetwork;
        this.linkSpeed = linkSpeed;
    }

    public WiFiAdditional(@NonNull String vendorName, boolean configuredNetwork) {
        this(vendorName, StringUtils.EMPTY, WiFiConnection.LINK_SPEED_INVALID, configuredNetwork);
    }

    public WiFiAdditional(@NonNull String vendorName, @NonNull String ipAddress, int linkSpeed) {
        this(vendorName, ipAddress, linkSpeed, true);
    }

    public String getVendorName() {
        return vendorName;
    }

    public String getIPAddress() {
        return ipAddress;
    }

    public int getLinkSpeed() {
        return linkSpeed;
    }

    public boolean isConnected() {
        return StringUtils.isNotBlank(getIPAddress());
    }

    public boolean isConfiguredNetwork() {
        return configuredNetwork;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}