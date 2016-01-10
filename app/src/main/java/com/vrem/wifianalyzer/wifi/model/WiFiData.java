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
import android.net.wifi.WifiInfo;
import android.support.annotation.NonNull;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.vendor.model.VendorService;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class WiFiData {

    private final MainContext mainContext = MainContext.INSTANCE;

    private List<ScanResult> scanResults;
    private WifiInfo wifiInfo;

    public WiFiData(List<ScanResult> scanResults, WifiInfo wifiInfo) {
        this.scanResults = scanResults;
        this.wifiInfo = wifiInfo;
    }

    @NonNull
    public List<DetailsInfo> getWiFiList() {
        Settings settings = mainContext.getSettings();
        return getWiFiList(settings.getGroupBy(), settings.hideWeakSignal());
    }

    @NonNull
    public Map<Integer, List<DetailsInfo>> getWiFiChannels() {
        Map<Integer, List<DetailsInfo>> results = new TreeMap<>();
        List<DetailsInfo> wiFiList = getWiFiList(GroupBy.CHANNEL, false);
        for (DetailsInfo detailsInfo : wiFiList) {
            List<DetailsInfo> details = new ArrayList<>();
            details.add(detailsInfo);
            details.addAll(detailsInfo.getChildren());
            results.put(detailsInfo.getChannel(), details);
        }
        return results;
    }

    public DetailsInfo getConnection() {
        if (hasData()) {
            VendorService vendorService = mainContext.getVendorService();
            Connection connection = new Connection(wifiInfo);
            for (ScanResult scanResult : scanResults) {
                String ipAddress = connection.getIPAddress(scanResult);
                if (StringUtils.isNotBlank(ipAddress)) {
                    String vendorName = vendorService.findVendorName(scanResult.BSSID);
                    return new Details(scanResult, vendorName, ipAddress);
                }
            }
        }
        return null;
    }

    private List<DetailsInfo> getWiFiList(@NonNull GroupBy groupBy, boolean hideWeakSignal) {
        return hasData() ? groupWiFiList(groupBy, buildWiFiList(hideWeakSignal)) : new ArrayList<DetailsInfo>();
    }

    private boolean hasData() {
        return scanResults != null && !scanResults.isEmpty();
    }

    private List<DetailsInfo> groupWiFiList(@NonNull GroupBy groupBy, List<DetailsInfo> wifiList) {
        List<DetailsInfo> results = new ArrayList<>();
        Collections.sort(wifiList, groupBy.sortOrder());
        DetailsInfo parent = null;
        for (DetailsInfo detailsInfo : wifiList) {
            if (parent == null || groupBy.groupBy().compare(parent, detailsInfo) != 0) {
                if (parent != null) {
                    Collections.sort(parent.getChildren());
                }
                parent = detailsInfo;
                results.add(parent);
            } else {
                parent.addChild(detailsInfo);
            }
        }
        if (parent != null) {
            Collections.sort(parent.getChildren());
        }
        Collections.sort(results);
        return results;
    }

    private List<DetailsInfo> buildWiFiList(boolean hideWeakSignal) {
        List<DetailsInfo> results = new ArrayList<>();
        WiFiBand wiFiBand = mainContext.getSettings().getWiFiBand();
        VendorService vendorService = mainContext.getVendorService();
        DetailsInfo connection = getConnection();
        for (ScanResult scanResult : scanResults) {
            String vendorName = vendorService.findVendorName(scanResult.BSSID);
            Details details = new Details(scanResult, vendorName);
            if (wiFiBand.equals(details.getWiFiBand())) {
                if (details.equals(connection)) {
                    results.add(connection);
                } else if (!hideWeakSignal(hideWeakSignal, details)) {
                    results.add(details);
                }
            }
        }
        return results;
    }


    private boolean hideWeakSignal(boolean hideWeakSignal, DetailsInfo detailsInfo) {
        return hideWeakSignal && detailsInfo.getStrength().weak();
    }
}
