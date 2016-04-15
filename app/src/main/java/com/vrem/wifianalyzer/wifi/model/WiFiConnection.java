/*
 *    Copyright (C) 2015 - 2016 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
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

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        return new EqualsBuilder()
                .append(getSSID(), ((WiFiConnection) other).getSSID())
                .append(getBSSID(), ((WiFiConnection) other).getBSSID())
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
