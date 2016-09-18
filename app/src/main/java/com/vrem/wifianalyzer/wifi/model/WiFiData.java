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

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.vendor.model.VendorService;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WiFiData {
    private final List<WiFiDetail> wiFiDetails;
    private final WiFiConnection wiFiConnection;
    private final List<String> wiFiConfigurations;

    public WiFiData(@NonNull List<WiFiDetail> wiFiDetails, @NonNull WiFiConnection wiFiConnection, @NonNull List<String> wiFiConfigurations) {
        this.wiFiDetails = wiFiDetails;
        this.wiFiConnection = wiFiConnection;
        this.wiFiConfigurations = wiFiConfigurations;
    }

    @NonNull
    public WiFiDetail getConnection() {
        VendorService vendorService = MainContext.INSTANCE.getVendorService();
        for (WiFiDetail wiFiDetail : wiFiDetails) {
            WiFiConnection connection = new WiFiConnection(wiFiDetail.getSSID(), wiFiDetail.getBSSID());
            if (wiFiConnection.equals(connection)) {
                String vendorName = vendorService.findVendorName(wiFiDetail.getBSSID());
                WiFiAdditional wiFiAdditional = new WiFiAdditional(vendorName, wiFiConnection.getIpAddress(), wiFiConnection.getLinkSpeed());
                return new WiFiDetail(wiFiDetail, wiFiAdditional);
            }
        }
        return WiFiDetail.EMPTY;
    }

    @NonNull
    public List<WiFiDetail> getWiFiDetails(@NonNull WiFiBand wiFiBand, @NonNull SortBy sortBy) {
        return getWiFiDetails(wiFiBand, sortBy, GroupBy.NONE);
    }

    @NonNull
    public List<WiFiDetail> getWiFiDetails(@NonNull WiFiBand wiFiBand, @NonNull SortBy sortBy, @NonNull GroupBy groupBy) {
        List<WiFiDetail> results = getWiFiDetails(wiFiBand);
        if (!results.isEmpty() && !GroupBy.NONE.equals(groupBy)) {
            results = getWiFiDetails(results, sortBy, groupBy);
        }
        Collections.sort(results, sortBy.comparator());
        return Collections.unmodifiableList(results);
    }

    @NonNull
    List<WiFiDetail> getWiFiDetails(@NonNull List<WiFiDetail> wiFiDetails, @NonNull SortBy sortBy, @NonNull GroupBy groupBy) {
        List<WiFiDetail> results = new ArrayList<>();
        Collections.sort(wiFiDetails, groupBy.sortOrder());
        WiFiDetail parent = null;
        for (WiFiDetail wiFiDetail : wiFiDetails) {
            if (parent == null || groupBy.groupBy().compare(parent, wiFiDetail) != 0) {
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
    private List<WiFiDetail> getWiFiDetails(@NonNull WiFiBand wiFiBand) {
        List<WiFiDetail> results = new ArrayList<>();
        WiFiDetail connection = getConnection();
        VendorService vendorService = MainContext.INSTANCE.getVendorService();
        for (WiFiDetail wiFiDetail : wiFiDetails) {
            if (wiFiDetail.getWiFiSignal().getWiFiBand().equals(wiFiBand)) {
                if (wiFiDetail.equals(connection)) {
                    results.add(connection);
                } else {
                    String vendorName = vendorService.findVendorName(wiFiDetail.getBSSID());
                    boolean contains = wiFiConfigurations.contains(wiFiDetail.getSSID());
                    WiFiAdditional wiFiAdditional = new WiFiAdditional(vendorName, contains);
                    results.add(new WiFiDetail(wiFiDetail, wiFiAdditional));
                }
            }
        }
        return results;
    }

    @NonNull
    public List<WiFiDetail> getWiFiDetails() {
        return Collections.unmodifiableList(wiFiDetails);
    }

    @NonNull
    public List<String> getWiFiConfigurations() {
        return Collections.unmodifiableList(wiFiConfigurations);
    }

    @NonNull
    public WiFiConnection getWiFiConnection() {
        return wiFiConnection;
    }

}
