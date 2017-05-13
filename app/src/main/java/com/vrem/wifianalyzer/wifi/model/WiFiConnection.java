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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class WiFiConnection {
    public static final int LINK_SPEED_INVALID = -1;
    public static final WiFiConnection EMPTY = new WiFiConnection(StringUtils.EMPTY, StringUtils.EMPTY);

    private final String SSID;
    private final String BSSID;
    private final String ipAddress;
    private final int linkSpeed;

    public WiFiConnection(@NonNull String SSID, @NonNull String BSSID, @NonNull String ipAddress, int linkSpeed) {
        this.SSID = SSID;
        this.BSSID = BSSID;
        this.ipAddress = ipAddress;
        this.linkSpeed = linkSpeed;
    }

    public WiFiConnection(@NonNull String SSID, @NonNull String BSSID) {
        this(SSID, BSSID, StringUtils.EMPTY, LINK_SPEED_INVALID);
    }

    public String getSSID() {
        return SSID;
    }

    public String getBSSID() {
        return BSSID;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getLinkSpeed() {
        return linkSpeed;
    }

    public boolean isConnected() {
        return StringUtils.isNotBlank(getIpAddress());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        WiFiConnection that = (WiFiConnection) o;

        return new EqualsBuilder()
            .append(getSSID(), that.getSSID())
            .append(getBSSID(), that.getBSSID())
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(getSSID())
            .append(getBSSID())
            .toHashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
