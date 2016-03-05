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

import android.net.wifi.ScanResult;
import android.support.annotation.NonNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class WiFiDetail {
    private final List<WiFiDetail> children;
    private final String SSID;
    private final String BSSID;
    private final String capabilities;
    private final WiFiSignal wiFiSignal;
    private final WiFiAdditional wiFiAdditional;

    public WiFiDetail(@NonNull String SSID, @NonNull String BSSID, @NonNull String capabilities,
                      @NonNull WiFiSignal wiFiSignal, @NonNull WiFiAdditional wiFiAdditional) {
        this.SSID = SSID;
        this.BSSID = BSSID;
        this.capabilities = capabilities;
        this.wiFiSignal = wiFiSignal;
        this.wiFiAdditional = wiFiAdditional;
        this.children = new ArrayList<>();
    }

    public WiFiDetail(@NonNull ScanResult scanResult, @NonNull WiFiAdditional wiFiAdditional) {
        this(scanResult.SSID, scanResult.BSSID, scanResult.capabilities, new WiFiSignal(scanResult), wiFiAdditional);
    }

    public Security getSecurity() {
        return Security.findOne(capabilities);
    }

    public String getSSID() {
        return SSID;
    }

    public String getBSSID() {
        return BSSID;
    }

    public String getCapabilities() {
        return capabilities;
    }

    public WiFiSignal getWiFiSignal() {
        return wiFiSignal;
    }

    public WiFiAdditional getWiFiAdditional() {
        return wiFiAdditional;
    }

    public List<WiFiDetail> getChildren() {
        return children;
    }

    public String getTitle() {
        return String.format("%s (%s)", StringUtils.isBlank(getSSID()) ? "***" : getSSID(), getBSSID());
    }

    public void addChild(@NonNull WiFiDetail wiFiDetail) {
        children.add(wiFiDetail);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;

        if (other == null || getClass() != other.getClass()) return false;

        WiFiDetail otherDetail = (WiFiDetail) other;
        return new EqualsBuilder()
                .append(getSSID().toUpperCase(), (otherDetail).getSSID().toUpperCase())
                .append(getBSSID().toUpperCase(), (otherDetail).getBSSID().toUpperCase())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getSSID().toUpperCase())
                .append(getBSSID().toUpperCase())
                .toHashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}