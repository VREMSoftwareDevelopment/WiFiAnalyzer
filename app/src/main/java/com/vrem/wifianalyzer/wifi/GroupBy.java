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

import android.support.annotation.NonNull;

import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.Comparator;

public enum GroupBy {
    SSID(new SSIDSortOrder(), new SSIDGroupBy()),
    CHANNEL(new ChannelSortOrder(), new ChannelGroupBy());

    private final Comparator<DetailsInfo> sortOrder;
    private final Comparator<DetailsInfo> groupBy;

    GroupBy(@NonNull Comparator<DetailsInfo> sortOrder, @NonNull Comparator<DetailsInfo> groupBy) {
        this.sortOrder = sortOrder;
        this.groupBy = groupBy;
    }

    public static GroupBy find(String value) {
        try {
            return valueOf(value.toUpperCase());
        } catch (Exception e) {
            return GroupBy.SSID;
        }
    }

    Comparator<DetailsInfo> getSortOrder() {
        return sortOrder;
    }

    Comparator<DetailsInfo> getGroupBy() {
        return groupBy;
    }

    private static class SSIDSortOrder implements Comparator<DetailsInfo> {
        @Override
        public int compare(DetailsInfo lhs, DetailsInfo rhs) {
            return new CompareToBuilder()
                    .append(lhs.SSID().toUpperCase(), rhs.SSID().toUpperCase())
                    .append(lhs.level(), rhs.level())
                    .append(lhs.BSSID().toUpperCase(), rhs.BSSID().toUpperCase())
                    .toComparison();
        }
    }

    private static class SSIDGroupBy implements Comparator<DetailsInfo> {
        @Override
        public int compare(DetailsInfo lhs, DetailsInfo rhs) {
            return new CompareToBuilder()
                    .append(lhs.SSID().toUpperCase(), rhs.SSID().toUpperCase())
                    .toComparison();
        }
    }

    private static class ChannelSortOrder implements Comparator<DetailsInfo> {
        @Override
        public int compare(DetailsInfo lhs, DetailsInfo rhs) {
            return new CompareToBuilder()
                    .append(lhs.channel(), rhs.channel())
                    .append(lhs.level(), rhs.level())
                    .append(lhs.SSID().toUpperCase(), rhs.SSID().toUpperCase())
                    .append(lhs.BSSID().toUpperCase(), rhs.BSSID().toUpperCase())
                    .toComparison();
        }
    }

    private static class ChannelGroupBy implements Comparator<DetailsInfo> {
        @Override
        public int compare(DetailsInfo lhs, DetailsInfo rhs) {
            return new CompareToBuilder()
                    .append(lhs.channel(), rhs.channel())
                    .toComparison();
        }
    }

}
