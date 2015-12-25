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

import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;

import com.vrem.wifianalyzer.vendor.VendorService;

import java.util.List;

public class WiFi {
    private final WifiManager wifiManager;
    private final VendorService vendorService;
    private GroupBy groupBy;
    private WiFiData wifiData;

    public WiFi(@NonNull WifiManager wifiManager, @NonNull VendorService vendorService, @NonNull GroupBy groupBy) {
        this.wifiManager = wifiManager;
        this.vendorService = vendorService;
        setGroupBy(groupBy);
    }

    public boolean enable() {
        return wifiManager.isWifiEnabled() || wifiManager.setWifiEnabled(true);
    }

    public WiFiData scan() {
        wifiData = new WiFiData();
        if (wifiManager.startScan()) {
            List<ScanResult> scanResults = wifiManager.getScanResults();
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (scanResults != null) {
                wifiData = new WiFiData(scanResults, wifiInfo, vendorService, groupBy);
            }
        }
        return wifiData;
    }

    WiFiData getWifiData() {
        return wifiData;
    }

    public void setGroupBy(@NonNull GroupBy groupBy) {
        this.groupBy = groupBy;
    }
}
