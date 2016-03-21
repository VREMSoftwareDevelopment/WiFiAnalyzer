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

package com.vrem.wifianalyzer.wifi.band;

import android.support.annotation.NonNull;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Arrays;
import java.util.List;

public class WiFiChannel implements Comparable<WiFiChannel> {
    public static final int FREQUENCY_SPREAD = 5;
    public static final int FREQUENCY_SPREAD_HALF = FREQUENCY_SPREAD / 2;

    public static final WiFiChannel UNKNOWN = new WiFiChannel(0, 0);

    final static List<WiFiChannel> GHZ_2 = Arrays.asList(
            new WiFiChannel(1, 2412),
            new WiFiChannel(2, 2417),
            new WiFiChannel(3, 2422),
            new WiFiChannel(4, 2427),
            new WiFiChannel(5, 2432),
            new WiFiChannel(6, 2437),
            new WiFiChannel(7, 2442),
            new WiFiChannel(8, 2447),
            new WiFiChannel(9, 2452),
            new WiFiChannel(10, 2457),
            new WiFiChannel(11, 2462),
            new WiFiChannel(12, 2467),
            new WiFiChannel(13, 2472),
            new WiFiChannel(14, 2484)
    );

    final static List<WiFiChannel> GHZ_5 = Arrays.asList(
            new WiFiChannel(183, 4915),
            new WiFiChannel(184, 4920),
            new WiFiChannel(185, 4925),
            new WiFiChannel(187, 4935),
            new WiFiChannel(188, 4940),
            new WiFiChannel(189, 4945),
            new WiFiChannel(192, 4960),
            new WiFiChannel(196, 4980),
            new WiFiChannel(7, 5035),
            new WiFiChannel(8, 5040),
            new WiFiChannel(9, 5045),
            new WiFiChannel(11, 5055),
            new WiFiChannel(12, 5060),
            new WiFiChannel(16, 5080),
            new WiFiChannel(34, 5170),
            new WiFiChannel(36, 5180),
            new WiFiChannel(38, 5190),
            new WiFiChannel(40, 5200),
            new WiFiChannel(42, 5210),
            new WiFiChannel(44, 5220),
            new WiFiChannel(46, 5230),
            new WiFiChannel(48, 5240),
            new WiFiChannel(52, 5260),
            new WiFiChannel(56, 5280),
            new WiFiChannel(60, 5300),
            new WiFiChannel(64, 5320),
            new WiFiChannel(100, 5500),
            new WiFiChannel(104, 5520),
            new WiFiChannel(108, 5540),
            new WiFiChannel(112, 5560),
            new WiFiChannel(116, 5580),
            new WiFiChannel(120, 5600),
            new WiFiChannel(124, 5620),
            new WiFiChannel(128, 5640),
            new WiFiChannel(132, 5660),
            new WiFiChannel(136, 5680),
            new WiFiChannel(140, 5700),
            new WiFiChannel(149, 5745),
            new WiFiChannel(153, 5765),
            new WiFiChannel(157, 5785),
            new WiFiChannel(161, 5805),
            new WiFiChannel(165, 5825)
    );

    private final int channel;
    private final int frequency;

    WiFiChannel(int channel, int frequency) {
        this.channel = channel;
        this.frequency = frequency;
    }

    public int getChannel() {
        return channel;
    }

    public int getFrequency() {
        return frequency;
    }

    public int getFrequencyStart() {
        return getFrequency() - FREQUENCY_SPREAD_HALF;
    }

    public int getFrequencyEnd() {
        return getFrequency() + FREQUENCY_SPREAD_HALF;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;

        if (other == null || getClass() != other.getClass()) return false;

        WiFiChannel otherDetail = (WiFiChannel) other;
        return new EqualsBuilder()
                .append(getChannel(), (otherDetail).getChannel())
                .append(getFrequency(), (otherDetail).getFrequency())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getChannel())
                .append(getFrequency())
                .toHashCode();
    }

    @Override
    public int compareTo(@NonNull WiFiChannel another) {
        return new CompareToBuilder()
                .append(getChannel(), another.getChannel())
                .append(getFrequency(), another.getFrequency())
                .toComparison();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
