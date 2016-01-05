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
package com.vrem.wifianalyzer.wifi;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.support.annotation.NonNull;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.vendor.VendorService;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WiFiData {

    private MainContext mainContext = MainContext.INSTANCE;

    private List<ScanResult> scanResults;
    private WifiInfo wifiInfo;

    public WiFiData(List<ScanResult> scanResults, WifiInfo wifiInfo) {
        this.scanResults = scanResults;
        this.wifiInfo = wifiInfo;
    }

    public List<DetailsInfo> getWiFiList() {
        if (hasData()) {
            Settings settings = mainContext.getSettings();
            List<DetailsInfo> wifiList = buildWiFiList(settings.hideWeakSignal());
            return groupWiFiList(settings.getGroupBy(), wifiList);
        }
        return new ArrayList<>();
    }

    private boolean hasData() {
        return scanResults != null && !scanResults.isEmpty();
    }

    public DetailsInfo getConnection() {
        if (hasData()) {
            VendorService vendorService = mainContext.getVendorService();
            Connection connection = new Connection(wifiInfo);
            for (ScanResult scanResult : scanResults) {
                String ipAddress = connection.getIPAddress(scanResult);
                if (StringUtils.isNotBlank(ipAddress)) {
                    String vendorName = vendorService.getVendorName(scanResult.BSSID);
                    return new Details(scanResult, vendorName, ipAddress);
                }
            }
        }
        return null;
    }

    @NonNull
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

    @NonNull
    private List<DetailsInfo> buildWiFiList(boolean hideWeakSignal) {
        List<DetailsInfo> results = new ArrayList<>();
        VendorService vendorService = mainContext.getVendorService();
        DetailsInfo connection = getConnection();
        for (ScanResult scanResult : scanResults) {
            String vendorName = vendorService.getVendorName(scanResult.BSSID);
            Details details = new Details(scanResult, vendorName);
            if (details.equals(connection)) {
                results.add(connection);
            } else if (!hideWeakSignal(hideWeakSignal, details)) {
                results.add(details);
            }
        }
        return results;
    }


    private boolean hideWeakSignal(boolean hideWeakSignal, DetailsInfo detailsInfo) {
        return hideWeakSignal && detailsInfo.getStrength().weak();
    }
}
