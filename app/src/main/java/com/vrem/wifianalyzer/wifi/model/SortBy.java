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

import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.Comparator;

public enum SortBy {
    STRENGTH(new StrengthComparator()),
    SSID(new SSIDComparator()),
    CHANNEL(new ChannelComparator());

    private final Comparator<WiFiDetails> comparator;

    SortBy(@NonNull Comparator<WiFiDetails> comparator) {
        this.comparator = comparator;
    }

    public static SortBy find(String value) {
        try {
            return valueOf(value.toUpperCase());
        } catch (Exception e) {
            return SortBy.STRENGTH;
        }
    }

    Comparator<WiFiDetails> comparator() {
        return comparator;
    }


    static class StrengthComparator implements Comparator<WiFiDetails> {
        @Override
        public int compare(WiFiDetails lhs, WiFiDetails rhs) {
            return new CompareToBuilder()
                    .append(rhs.getLevel(), lhs.getLevel())
                    .append(lhs.getSSID().toUpperCase(), rhs.getSSID().toUpperCase())
                    .append(lhs.getBSSID().toUpperCase(), rhs.getBSSID().toUpperCase())
                    .toComparison();
        }
    }


    static class SSIDComparator implements Comparator<WiFiDetails> {
        @Override
        public int compare(WiFiDetails lhs, WiFiDetails rhs) {
            return new CompareToBuilder()
                    .append(lhs.getSSID().toUpperCase(), rhs.getSSID().toUpperCase())
                    .append(rhs.getLevel(), lhs.getLevel())
                    .append(lhs.getBSSID().toUpperCase(), rhs.getBSSID().toUpperCase())
                    .toComparison();
        }
    }

    static class ChannelComparator implements Comparator<WiFiDetails> {
        @Override
        public int compare(WiFiDetails lhs, WiFiDetails rhs) {
            return new CompareToBuilder()
                    .append(lhs.getChannel(), rhs.getChannel())
                    .append(rhs.getLevel(), lhs.getLevel())
                    .append(lhs.getSSID().toUpperCase(), rhs.getSSID().toUpperCase())
                    .append(lhs.getBSSID().toUpperCase(), rhs.getBSSID().toUpperCase())
                    .toComparison();
        }
    }


}
