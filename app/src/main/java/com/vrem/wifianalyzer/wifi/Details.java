/*
 *    Copyright (C) 2010 - 2015 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.wifi;

import android.net.wifi.ScanResult;
import android.support.annotation.NonNull;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Details implements DetailsInfo {
    private final ScanResult scanResult;
    private final String vendorName;

    public Details(@NonNull ScanResult scanResult, @NonNull String vendorName) {
        this.scanResult = scanResult;
        this.vendorName = vendorName;
    }

    @Override
    public int frequency() {
        return scanResult.frequency;
    }

    @Override
    public int channel() {
        return Frequency.findChannel(frequency());
    }

    @Override
    public Security security() {
        return Security.findOne(scanResult.capabilities);
    }

    @Override
    public Strength strength() {
        return Strength.calculate(scanResult.level);
    }

    @Override
    public String SSID() {
        return scanResult.SSID;
    }

    @Override
    public String BSSID() {
        return scanResult.BSSID;
    }

    @Override
    public int level() {
        return Math.abs(scanResult.level);
    }

    @Override
    public String capabilities() {
        return scanResult.capabilities;
    }

    @Override
    public double distance() {
        return Distance.calculate(frequency(), level());
    }

    @Override
    public String vendorName() {
        return vendorName;
    }

    @Override
    public int compareTo(@NonNull DetailsInfo other) {
        return new CompareToBuilder()
                .append(level(), other.level())
                .append(SSID().toUpperCase(), other.SSID().toUpperCase())
                .append(BSSID().toUpperCase(), other.BSSID().toUpperCase())
                .toComparison();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;

        if (other == null || getClass() != other.getClass()) return false;

        return new EqualsBuilder()
                .append(SSID().toUpperCase(), ((Details) other).SSID().toUpperCase())
                .append(BSSID().toUpperCase(), ((Details) other).BSSID().toUpperCase())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(SSID().toUpperCase())
                .append(BSSID().toUpperCase())
                .toHashCode();
    }
}
