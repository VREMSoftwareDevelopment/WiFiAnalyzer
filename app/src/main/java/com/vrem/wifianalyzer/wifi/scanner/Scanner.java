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
import android.os.Handler;
import android.support.annotation.NonNull;

import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.model.WiFiData;

import java.util.Map;
import java.util.TreeMap;

public class Scanner {
    private final Map<String, UpdateNotifier> updateNotifiers;
    private final WifiManager wifiManager;
    private final Transformer transformer;
    private Cache cache;
    private PeriodicScan periodicScan;

    public Scanner(@NonNull WifiManager wifiManager, @NonNull Handler handler, @NonNull Settings settings, @NonNull Transformer transformer) {
        this.wifiManager = wifiManager;
        this.updateNotifiers = new TreeMap<>();
        this.transformer = transformer;
        this.setCache(new Cache());
        this.periodicScan = new PeriodicScan(this, handler, settings);
    }

    public void update() {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        if (wifiManager.startScan()) {
            cache.add(wifiManager.getScanResults());
            WiFiData wiFiData = transformer.transformToWiFiData(cache.getScanResults(), wifiManager.getConnectionInfo(), wifiManager.getConfiguredNetworks());
            for (String key : updateNotifiers.keySet()) {
                UpdateNotifier updateNotifier = updateNotifiers.get(key);
                updateNotifier.update(wiFiData);
            }
        }
    }

    public void addUpdateNotifier(@NonNull UpdateNotifier updateNotifier) {
        String key = updateNotifier.getClass().getName();
        updateNotifiers.put(key, updateNotifier);
    }

    public void pause() {
        periodicScan.stop();
    }

    public boolean isRunning() {
        return periodicScan.isRunning();
    }

    public void resume() {
        periodicScan.start();
    }

    protected PeriodicScan getPeriodicScan() {
        return periodicScan;
    }

    protected void setPeriodicScan(@NonNull PeriodicScan periodicScan) {
        this.periodicScan = periodicScan;
    }

    protected void setCache(@NonNull Cache cache) {
        this.cache = cache;
    }

    protected Map<String, UpdateNotifier> getUpdateNotifiers() {
        return updateNotifiers;
    }

}
