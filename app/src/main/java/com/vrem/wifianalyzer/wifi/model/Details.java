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

public class Details implements WiFiDetails {
    private final ScanResult scanResult;
    private final String vendorName;
    private final String ipAddress;
    private final boolean configuredNetwork;
    private final List<WiFiDetails> children;

    private Details(@NonNull ScanResult scanResult, @NonNull String vendorName, @NonNull String ipAddress, boolean configuredNetwork) {
        this.scanResult = scanResult;
        this.vendorName = vendorName;
        this.ipAddress = ipAddress;
        this.configuredNetwork = configuredNetwork;
        this.children = new ArrayList<>();
    }

    public static Details makeConnection(@NonNull ScanResult scanResult, @NonNull String vendorName, @NonNull String ipAddress) {
        return new Details(scanResult, vendorName, ipAddress, true);
    }

    public static Details makeScanResult(@NonNull ScanResult scanResult, @NonNull String vendorName, boolean configuredNetwork) {
        return new Details(scanResult, vendorName, StringUtils.EMPTY, configuredNetwork);
    }

    @Override
    public int getFrequency() {
        return scanResult.frequency;
    }

    @Override
    public int getChannel() {
        return Frequency.findChannel(getFrequency());
    }

    @Override
    public WiFiBand getWiFiBand() {
        return Frequency.find(getFrequency()).wiFiBand();
    }

    @Override
    public Security getSecurity() {
        return Security.findOne(scanResult.capabilities);
    }

    @Override
    public Strength getStrength() {
        return Strength.calculate(scanResult.level);
    }

    @Override
    public String getSSID() {
        return scanResult.SSID;
    }

    @Override
    public String getBSSID() {
        return scanResult.BSSID;
    }

    @Override
    public int getLevel() {
        return scanResult.level;
    }

    @Override
    public String getCapabilities() {
        return scanResult.capabilities;
    }

    @Override
    public double getDistance() {
        return Distance.calculate(getFrequency(), getLevel());
    }

    @Override
    public String getVendorName() {
        return vendorName;
    }

    @Override
    public String getIPAddress() {
        return ipAddress;
    }

    @Override
    public boolean isConnected() {
        return StringUtils.isNotBlank(getIPAddress());
    }

    @Override
    public boolean isConfiguredNetwork() {
        return configuredNetwork;
    }

    @Override
    public List<WiFiDetails> getChildren() {
        return children;
    }

    @Override
    public String getTitle() {
        return String.format("%s (%s)", StringUtils.isBlank(getSSID()) ? "***" : getSSID(), getBSSID());
    }

    public void addChild(@NonNull WiFiDetails wiFiDetails) {
        children.add(wiFiDetails);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;

        if (other == null || getClass() != other.getClass()) return false;

        Details otherDetail = (Details) other;
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
