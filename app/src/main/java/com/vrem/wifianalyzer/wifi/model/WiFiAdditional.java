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