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

import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiChannel;
import com.vrem.wifianalyzer.wifi.band.WiFiWidth;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class WiFiSignal {
    public static final WiFiSignal EMPTY = new WiFiSignal(0, WiFiWidth.MHZ_20, 0);

    private final int frequency;
    private final WiFiWidth wiFiWidth;
    private final WiFiBand wiFiBand;
    private final int level;

    public WiFiSignal(int frequency, @NonNull WiFiWidth wiFiWidth, int level) {
        this.frequency = frequency;
        this.wiFiWidth = wiFiWidth;
        this.level = level;
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

    public WiFiBand getWiFiBand() {
        return wiFiBand;
    }

    public WiFiWidth getWiFiWidth() {
        return wiFiWidth;
    }

    public WiFiChannel getWiFiChannel() {
        return getWiFiBand().getWiFiChannels().getWiFiChannelByFrequency(getFrequency());
    }

    public int getLevel() {
        return level;
    }

    public Strength getStrength() {
        return Strength.calculate(level);
    }

    public double getDistance() {
        return WiFiUtils.calculateDistance(getFrequency(), getLevel());
    }

    public boolean isInRange(int frequency) {
        return frequency >= getFrequencyStart() && frequency <= getFrequencyEnd();
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
            .append(getFrequency(), ((WiFiSignal) other).getFrequency())
            .append(getWiFiWidth(), ((WiFiSignal) other).getWiFiWidth())
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
