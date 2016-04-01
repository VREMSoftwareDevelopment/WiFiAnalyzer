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

import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;

import com.vrem.wifianalyzer.MainConfiguration;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.wifi.model.WiFiData;

import java.util.Map;
import java.util.TreeMap;

public class Scanner {
    private final Map<String, UpdateNotifier> updateNotifiers;
    private PeriodicScan periodicScan;
    private Cache cache;
    private Transformer transformer;

    public Scanner() {
        if (!MainContext.INSTANCE.isInitialized() || !MainConfiguration.INSTANCE.isInitialized()) {
            throw new RuntimeException("Main Context/Configuration is NOT set! Can not start WiFi scans...");
        }
        this.periodicScan = new PeriodicScan(this);
        this.updateNotifiers = new TreeMap<>();
        setTransformer(new Transformer());
        setCache(new Cache());
    }

    public void update() {
        MainContext instance = MainContext.INSTANCE;
        instance.getLogger().info(this, "running update...");
        WifiManager wifiManager = instance.getWifiManager();
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        if (wifiManager.startScan()) {
            cache.add(wifiManager.getScanResults());
            WiFiData wiFiData = transformer.transformToWiFiData(cache.getScanResults(), wifiManager.getConnectionInfo(), wifiManager.getConfiguredNetworks());
            for (String key : updateNotifiers.keySet()) {
                UpdateNotifier updateNotifier = updateNotifiers.get(key);
                instance.getLogger().info(this, "running notifier: " + key);
                updateNotifier.update(wiFiData);
            }
        }
    }

    public void addUpdateNotifier(@NonNull UpdateNotifier updateNotifier) {
        String key = updateNotifier.getClass().getName();
        MainContext.INSTANCE.getLogger().info(this, "register notifier: " + key);
        updateNotifiers.put(key, updateNotifier);
    }

    public void pause() {
        periodicScan.stop();
    }

    public void resume() {
        periodicScan.start();
    }

    PeriodicScan getPeriodicScan() {
        return periodicScan;
    }

    void setPeriodicScan(@NonNull PeriodicScan periodicScan) {
        this.periodicScan = periodicScan;
    }

    void setCache(@NonNull Cache cache) {
        this.cache = cache;
    }

    void setTransformer(@NonNull Transformer transformer) {
        this.transformer = transformer;
    }

    Map<String, UpdateNotifier> getUpdateNotifiers() {
        return updateNotifiers;
    }

}
