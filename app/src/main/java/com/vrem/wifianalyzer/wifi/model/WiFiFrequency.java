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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class WiFiFrequency {
    private final int frequency;
    private final WiFiWidth wiFiWidth;
    private final WiFiBand wiFiBand;

    public WiFiFrequency(@NonNull ScanResult scanResult) {
        this(scanResult.frequency);
    }

    public WiFiFrequency(int frequency) {
        this(frequency, WiFiWidth.MHZ_20);
    }

    public WiFiFrequency(int frequency, @NonNull WiFiWidth wiFiWidth) {
        this.frequency = frequency;
        this.wiFiWidth = wiFiWidth;
        this.wiFiBand = WiFiBand.findByFrequency(frequency);
    }

    public int getFrequency() {
        return frequency;
    }

    public int getFrequencyStart() {
        return getFrequency() - getWiFiWidth().getFrequencyWidthHalf();
    }

    public int getFrequencyEnd() {
        return getFrequency() + getWiFiWidth().getFrequencyWidthHalf();
    }

    public int getChannel() {
        return WiFiBand.findChannelByFrequency(getFrequency());
    }

    public int getChannelStart() {
        return getChannel() - getWiFiWidth().getChannelWidthHalf();
    }

    public int getChannelEnd() {
        return getChannel() + getWiFiWidth().getChannelWidthHalf();
    }

    public WiFiBand getWiFiBand() {
        return wiFiBand;
    }

    public WiFiWidth getWiFiWidth() {
        return wiFiWidth;
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
                .append(getFrequency(), ((WiFiFrequency) other).getFrequency())
                .append(getWiFiWidth(), ((WiFiFrequency) other).getWiFiWidth())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getFrequency())
                .append(getWiFiWidth())
                .toHashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
