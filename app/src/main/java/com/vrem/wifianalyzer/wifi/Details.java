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

import org.apache.commons.lang3.StringUtils;
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
    public int getFrequency() {
        return scanResult.frequency;
    }

    @Override
    public int getChannel() {
        return Frequency.findChannel(getFrequency());
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
        return Math.abs(scanResult.level);
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
    public int compareTo(@NonNull DetailsInfo other) {
        int result = getLevel() - other.getLevel();
        if (result == 0) {
            result = getSSID().compareTo(other.getSSID());
            if (result == 0) {
                result = getBSSID().compareTo(other.getBSSID());
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;

        if (other == null || getClass() != other.getClass()) return false;

        Details details = (Details) other;

        return new EqualsBuilder()
                .append(getSSID(), details.getSSID())
                .append(getBSSID(), details.getBSSID())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getSSID())
                .append(getBSSID())
                .toHashCode();
    }
}
