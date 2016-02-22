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
import java.util.Map;
import java.util.TreeMap;

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

    @NonNull
    public Map<Integer, List<WiFiDetails>> getWiFiChannels(@NonNull WiFiBand wiFiBand) {
        Map<Integer, List<WiFiDetails>> results = new TreeMap<>();
        List<WiFiDetails> wifiList = getWiFiList(wiFiBand, GroupBy.CHANNEL, SortBy.STRENGTH);
        for (WiFiDetails wiFiDetails : wifiList) {
            List<WiFiDetails> details = new ArrayList<>();
            details.add(wiFiDetails);
            details.addAll(wiFiDetails.getChildren());
            results.put(wiFiDetails.getChannel(), details);
        }
        return results;
    }

    public WiFiDetails getConnection() {
        if (hasData()) {
            Connection connection = new Connection(connectionInfo);
            for (ScanResult scanResult : scanResults) {
                if (connection.matches(scanResult)) {
                    String ipAddress = connection.getIPAddress();
                    String vendorName = vendorService.findVendorName(scanResult.BSSID);
                    return Details.makeConnection(scanResult, vendorName, ipAddress);
                }
            }
        }
        return null;
    }

    @NonNull
    public List<WiFiDetails> getWiFiList(@NonNull WiFiBand wiFiBand, @NonNull SortBy sortBy) {
        return getWiFiList(wiFiBand, GroupBy.NONE, sortBy);
    }

    @NonNull
    public List<WiFiDetails> getWiFiList(@NonNull WiFiBand wiFiBand, @NonNull GroupBy groupBy, @NonNull SortBy sortBy) {
        return hasData() ? groupWiFiList(groupBy, sortBy, buildWiFiList(wiFiBand)) : new ArrayList<WiFiDetails>();
    }

    private boolean hasData() {
        return scanResults != null && !scanResults.isEmpty();
    }

    private List<WiFiDetails> groupWiFiList(@NonNull GroupBy groupBy, @NonNull SortBy sortBy, @NonNull List<WiFiDetails> wifiList) {
        List<WiFiDetails> results = new ArrayList<>();
        Collections.sort(wifiList, groupBy.sortOrder());
        WiFiDetails parent = null;
        for (WiFiDetails wiFiDetails : wifiList) {
            if (parent == null || groupBy.groupBy().compare(parent, wiFiDetails) != 0) {
                if (parent != null) {
                    Collections.sort(parent.getChildren(), sortBy.comparator());
                }
                parent = wiFiDetails;
                results.add(parent);
            } else {
                parent.addChild(wiFiDetails);
            }
        }
        if (parent != null) {
            Collections.sort(parent.getChildren(), sortBy.comparator());
        }
        Collections.sort(results, sortBy.comparator());
        return results;
    }

    private List<WiFiDetails> buildWiFiList(@NonNull WiFiBand wiFiBand) {
        List<WiFiDetails> results = new ArrayList<>();
        WiFiDetails connection = getConnection();
        for (ScanResult scanResult : scanResults) {
            String vendorName = vendorService.findVendorName(scanResult.BSSID);
            Details details = Details.makeScanResult(scanResult, vendorName, isConfiguredNetwork(scanResult));
            if (details.getWiFiBand().equals(wiFiBand)) {
                if (details.equals(connection)) {
                    results.add(connection);
                } else {
                    results.add(details);
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
