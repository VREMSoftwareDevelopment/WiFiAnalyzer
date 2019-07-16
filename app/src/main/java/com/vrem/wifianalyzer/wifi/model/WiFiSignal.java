/*
 * WiFiAnalyzer
 * Copyright (C) 2019  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import com.vrem.util.EnumUtils;
import com.vrem.wifianalyzer.wifi.band.FrequencyPredicate;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiChannel;
import com.vrem.wifianalyzer.wifi.band.WiFiWidth;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Locale;

import androidx.annotation.NonNull;

public class WiFiSignal {
    public static final WiFiSignal EMPTY = new WiFiSignal(0, 0, WiFiWidth.MHZ_20, 0, false);
    public static final String FREQUENCY_UNITS = "MHz";

    private final int primaryFrequency;
    private final int centerFrequency;
    private final WiFiWidth wiFiWidth;
    private final WiFiBand wiFiBand;
    private final int level;
    private final boolean is80211mc;

    public WiFiSignal(int primaryFrequency, int centerFrequency, @NonNull WiFiWidth wiFiWidth, int level, boolean is80211mc) {
        this.primaryFrequency = primaryFrequency;
        this.centerFrequency = centerFrequency;
        this.wiFiWidth = wiFiWidth;
        this.level = level;
        this.is80211mc = is80211mc;
        this.wiFiBand = EnumUtils.find(WiFiBand.class, new FrequencyPredicate(primaryFrequency), WiFiBand.GHZ2);
    }

    public int getPrimaryFrequency() {
        return primaryFrequency;
    }

    public int getCenterFrequency() {
        return centerFrequency;
    }

    public int getFrequencyStart() {
        return getCenterFrequency() - getWiFiWidth().getFrequencyWidthHalf();
    }

    public int getFrequencyEnd() {
        return getCenterFrequency() + getWiFiWidth().getFrequencyWidthHalf();
    }

    @NonNull
    public WiFiBand getWiFiBand() {
        return wiFiBand;
    }

    @NonNull
    public WiFiWidth getWiFiWidth() {
        return wiFiWidth;
    }

    @NonNull
    public WiFiChannel getPrimaryWiFiChannel() {
        return getWiFiBand().getWiFiChannels().getWiFiChannelByFrequency(getPrimaryFrequency());
    }

    @NonNull
    public WiFiChannel getCenterWiFiChannel() {
        return getWiFiBand().getWiFiChannels().getWiFiChannelByFrequency(getCenterFrequency());
    }

    public int getLevel() {
        return level;
    }

    public boolean is80211mc() {
        return is80211mc;
    }

    @NonNull
    public Strength getStrength() {
        return Strength.calculate(level);
    }

    @NonNull
    public String getDistance() {
        double distance = WiFiUtils.calculateDistance(getPrimaryFrequency(), getLevel());
        return String.format(Locale.ENGLISH, "~%.1fm", distance);
    }

    public boolean isInRange(int frequency) {
        return frequency >= getFrequencyStart() && frequency <= getFrequencyEnd();
    }

    @NonNull
    public String getChannelDisplay() {
        int primaryChannel = getPrimaryWiFiChannel().getChannel();
        int centerChannel = getCenterWiFiChannel().getChannel();
        String channel = Integer.toString(primaryChannel);
        if (primaryChannel != centerChannel) {
            channel += "(" + centerChannel + ")";
        }
        return channel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        WiFiSignal that = (WiFiSignal) o;

        return new EqualsBuilder()
            .append(getPrimaryFrequency(), that.getPrimaryFrequency())
            .append(getWiFiWidth(), that.getWiFiWidth())
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(getPrimaryFrequency())
            .append(getWiFiWidth())
            .toHashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
