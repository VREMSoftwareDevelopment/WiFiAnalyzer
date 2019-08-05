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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class WiFiDetail implements Comparable<WiFiDetail> {
    public static final WiFiDetail EMPTY = new WiFiDetail(StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, WiFiSignal.EMPTY);
    private static final String SSID_EMPTY = "***";

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

    public WiFiDetail(@NonNull String SSID, @NonNull String BSSID, @NonNull String capabilities, @NonNull WiFiSignal wiFiSignal) {
        this(SSID, BSSID, capabilities, wiFiSignal, WiFiAdditional.EMPTY);
    }

    public WiFiDetail(@NonNull WiFiDetail wiFiDetail, @NonNull WiFiAdditional wiFiAdditional) {
        this(wiFiDetail.SSID, wiFiDetail.BSSID, wiFiDetail.getCapabilities(), wiFiDetail.getWiFiSignal(), wiFiAdditional);
    }

    @NonNull
    public Security getSecurity() {
        return Security.findOne(capabilities);
    }

    @NonNull
    public String getSSID() {
        return isHidden() ? SSID_EMPTY : SSID;
    }

    boolean isHidden() {
        return StringUtils.isBlank(SSID);
    }

    @NonNull
    public String getBSSID() {
        return BSSID;
    }

    @NonNull
    public String getCapabilities() {
        return capabilities;
    }

    @NonNull
    public WiFiSignal getWiFiSignal() {
        return wiFiSignal;
    }

    @NonNull
    public WiFiAdditional getWiFiAdditional() {
        return wiFiAdditional;
    }

    @NonNull
    public List<WiFiDetail> getChildren() {
        return children;
    }

    public boolean noChildren() {
        return !getChildren().isEmpty();
    }

    @NonNull
    public String getTitle() {
        return String.format("%s (%s)", getSSID(), BSSID);
    }

    public void addChild(@NonNull WiFiDetail wiFiDetail) {
        children.add(wiFiDetail);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        WiFiDetail that = (WiFiDetail) o;

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
    public int compareTo(@NonNull WiFiDetail another) {
        return new CompareToBuilder()
            .append(getSSID(), another.getSSID())
            .append(getBSSID(), another.getBSSID())
            .toComparison();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}