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
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.support.annotation.NonNull;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.vendor.model.VendorService;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WiFiData {
    private static final String QUOTE = "\"";

    private final List<ScanResult> scanResults;
    private final WifiInfo connectionInfo;
    private final List<WifiConfiguration> configuredNetworks;
    private final VendorService vendorService;

    public WiFiData(List<ScanResult> scanResults, WifiInfo connectionInfo, List<WifiConfiguration> configuredNetworks) {
        this.scanResults = scanResults;
        this.connectionInfo = connectionInfo;
        this.configuredNetworks = configuredNetworks;
        this.vendorService = MainContext.INSTANCE.getVendorService();
    }

    public WiFiDetail getConnection() {
        if (hasData()) {
            Connection connection = new Connection(connectionInfo);
            for (ScanResult scanResult : scanResults) {
                if (connection.matches(scanResult)) {
                    String ipAddress = connection.getIPAddress();
                    String vendorName = vendorService.findVendorName(scanResult.BSSID);
                    return new WiFiDetail(scanResult, new WiFiAdditional(vendorName, ipAddress));
                }
            }
        }
        return null;
    }

    @NonNull
    public List<WiFiDetail> getWiFiDetails(@NonNull WiFiBand wiFiBand, @NonNull SortBy sortBy) {
        return getWiFiDetails(wiFiBand, sortBy, GroupBy.NONE);
    }

    @NonNull
    public List<WiFiDetail> getWiFiDetails(@NonNull WiFiBand wiFiBand, @NonNull SortBy sortBy, @NonNull GroupBy groupBy) {
        if (hasData()) {
            List<WiFiDetail> wiFiDetails = buildWiFiDetails(wiFiBand);
            return groupWiFiDetails(wiFiDetails, sortBy, groupBy);
        }
        return new ArrayList<>();
    }

    private boolean hasData() {
        return scanResults != null && !scanResults.isEmpty();
    }

    private List<WiFiDetail> groupWiFiDetails(@NonNull List<WiFiDetail> wiFiDetails, @NonNull SortBy sortBy, @NonNull GroupBy groupBy) {
        List<WiFiDetail> results = new ArrayList<>();
        if (wiFiDetails.isEmpty()) {
            return results;
        }
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

    private List<WiFiDetail> buildWiFiDetails(@NonNull WiFiBand wiFiBand) {
        List<WiFiDetail> results = new ArrayList<>();
        WiFiDetail connection = getConnection();
        for (ScanResult scanResult : scanResults) {
            String vendorName = vendorService.findVendorName(scanResult.BSSID);
            WiFiDetail wiFiDetail = new WiFiDetail(scanResult, new WiFiAdditional(vendorName, isConfiguredNetwork(scanResult)));
            if (wiFiDetail.getWiFiSignal().getWiFiBand().equals(wiFiBand)) {
                if (wiFiDetail.equals(connection)) {
                    results.add(connection);
                } else {
                    results.add(wiFiDetail);
                }
            }
        }
        return results;
    }

    private boolean isConfiguredNetwork(@NonNull ScanResult scanResult) {
        if (configuredNetworks != null) {
            for (WifiConfiguration wifiConfiguration : configuredNetworks) {
                String ssid = StringUtils.removeEnd(StringUtils.removeStart(wifiConfiguration.SSID, QUOTE), QUOTE);
                if (scanResult.SSID.equalsIgnoreCase(ssid)) {
                    return true;
                }
            }
        }
        return false;
    }

    public WifiInfo getWiFiInfo() {
        return connectionInfo;
    }

    public List<WifiConfiguration> getConfiguredNetworks() {
        return configuredNetworks;
    }

    public List<ScanResult> getScanResults() {
        return scanResults;
    }
}
