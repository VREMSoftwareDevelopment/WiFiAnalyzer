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
package com.vrem.wifianalyzer.wifi.scanner;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.wifi.model.WiFiData;

import java.util.ArrayList;
import java.util.List;

public class Scanner {
    private final PeriodicScan periodicScan;
    private final MainContext mainContext = MainContext.INSTANCE;
    private WiFiData wiFiData;
    private List<UpdateNotifier> updateNotifiers = new ArrayList<>();

    public Scanner() {
        this.periodicScan = new PeriodicScan(this);
    }

    public void update() {
        WifiManager wifiManager = mainContext.getWifiManager();
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        if (wifiManager.startScan()) {
            List<ScanResult> scanResults = wifiManager.getScanResults();
            WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
            wiFiData = new WiFiData(scanResults, connectionInfo, configuredNetworks);
            for (UpdateNotifier updateNotifier : updateNotifiers) {
                updateNotifier.update(wiFiData);
            }
        }
    }

    WiFiData getWifiData() {
        return wiFiData;
    }

    public boolean addUpdateNotifier(@NonNull UpdateNotifier updateNotifier) {
        return updateNotifiers.add(updateNotifier);
    }

    PeriodicScan getPeriodicScan() {
        return periodicScan;
    }
}
