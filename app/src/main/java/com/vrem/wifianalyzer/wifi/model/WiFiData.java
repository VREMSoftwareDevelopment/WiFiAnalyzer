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

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.vendor.model.VendorService;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;

public class WiFiData {
    public static final WiFiData EMPTY = new WiFiData(Collections.emptyList(), WiFiConnection.EMPTY);

    private final List<WiFiDetail> wiFiDetails;
    private final WiFiConnection wiFiConnection;

    public WiFiData(@NonNull List<WiFiDetail> wiFiDetails, @NonNull WiFiConnection wiFiConnection) {
        this.wiFiDetails = wiFiDetails;
        this.wiFiConnection = wiFiConnection;
    }

    @NonNull
    public WiFiDetail getConnection() {
        WiFiDetail wiFiDetail = IterableUtils.find(wiFiDetails, new ConnectionPredicate());
        return wiFiDetail == null ? WiFiDetail.EMPTY : copyWiFiDetail(wiFiDetail);
    }

    @NonNull
    public List<WiFiDetail> getWiFiDetails(@NonNull Predicate<WiFiDetail> predicate, @NonNull SortBy sortBy) {
        return getWiFiDetails(predicate, sortBy, GroupBy.NONE);
    }

    @NonNull
    public List<WiFiDetail> getWiFiDetails(@NonNull Predicate<WiFiDetail> predicate, @NonNull SortBy sortBy, @NonNull GroupBy groupBy) {
        List<WiFiDetail> results = getWiFiDetails(predicate);
        if (!results.isEmpty() && !GroupBy.NONE.equals(groupBy)) {
            results = sortAndGroup(results, sortBy, groupBy);
        }
        Collections.sort(results, sortBy.comparator());
        return results;
    }

    @NonNull
    List<WiFiDetail> sortAndGroup(@NonNull List<WiFiDetail> wiFiDetails, @NonNull SortBy sortBy, @NonNull GroupBy groupBy) {
        List<WiFiDetail> results = new ArrayList<>();
        Collections.sort(wiFiDetails, groupBy.sortOrderComparator());
        WiFiDetail parent = null;
        for (WiFiDetail wiFiDetail : wiFiDetails) {
            if (parent == null || groupBy.groupByComparator().compare(parent, wiFiDetail) != 0) {
                if (parent != null) {
                    Collections.sort(parent.getChildren(), sortBy.comparator());
                }
                parent = wiFiDetail;
                results.add(parent);
            } else {
                parent.addChild(wiFiDetail);
            }
        }
        if (parent != null) {
            Collections.sort(parent.getChildren(), sortBy.comparator());
        }
        Collections.sort(results, sortBy.comparator());
        return results;
    }

    @NonNull
    private List<WiFiDetail> getWiFiDetails(@NonNull Predicate<WiFiDetail> predicate) {
        Collection<WiFiDetail> selected = CollectionUtils.select(wiFiDetails, predicate);
        Collection<WiFiDetail> collected = CollectionUtils.collect(selected, new Transform());
        return new ArrayList<>(collected);
    }

    @NonNull
    public List<WiFiDetail> getWiFiDetails() {
        return Collections.unmodifiableList(wiFiDetails);
    }

    @NonNull
    public WiFiConnection getWiFiConnection() {
        return wiFiConnection;
    }

    @NonNull
    private WiFiDetail copyWiFiDetail(WiFiDetail wiFiDetail) {
        VendorService vendorService = MainContext.INSTANCE.getVendorService();
        String vendorName = vendorService.findVendorName(wiFiDetail.getBSSID());
        WiFiAdditional wiFiAdditional = new WiFiAdditional(vendorName, wiFiConnection);
        return new WiFiDetail(wiFiDetail, wiFiAdditional);
    }

    private class ConnectionPredicate implements Predicate<WiFiDetail> {
        @Override
        public boolean evaluate(WiFiDetail wiFiDetail) {
            return new EqualsBuilder()
                .append(wiFiConnection.getSSID(), wiFiDetail.getSSID())
                .append(wiFiConnection.getBSSID(), wiFiDetail.getBSSID())
                .isEquals();
        }
    }

    private class Transform implements Transformer<WiFiDetail, WiFiDetail> {
        private final WiFiDetail connection;
        private final VendorService vendorService;

        private Transform() {
            this.connection = getConnection();
            this.vendorService = MainContext.INSTANCE.getVendorService();
        }

        @Override
        public WiFiDetail transform(WiFiDetail input) {
            if (input.equals(connection)) {
                return connection;
            }
            String vendorName = vendorService.findVendorName(input.getBSSID());
            WiFiAdditional wiFiAdditional = new WiFiAdditional(vendorName);
            return new WiFiDetail(input, wiFiAdditional);
        }
    }

}
