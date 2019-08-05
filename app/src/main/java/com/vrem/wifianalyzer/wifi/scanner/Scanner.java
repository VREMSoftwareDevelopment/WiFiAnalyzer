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

package com.vrem.wifianalyzer.wifi.scanner;

import android.annotation.TargetApi;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;

import com.vrem.util.BuildUtils;
import com.vrem.wifianalyzer.ActivityUtils;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.model.WiFiData;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.IterableUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

class Scanner implements ScannerService {
    private final List<UpdateNotifier> updateNotifiers;
    private final WifiManager wifiManager;
    private final Settings settings;
    private Transformer transformer;
    private WiFiData wiFiData;
    private Cache cache;
    private PeriodicScan periodicScan;

    Scanner(@NonNull WifiManager wifiManager, @NonNull Handler handler, @NonNull Settings settings) {
        this.updateNotifiers = new ArrayList<>();
        this.wifiManager = wifiManager;
        this.settings = settings;
        this.wiFiData = WiFiData.EMPTY;
        this.setTransformer(new Transformer());
        this.setCache(new Cache());
        this.periodicScan = new PeriodicScan(this, handler, settings);
    }

    @Override
    public void update() {
        enableWiFi();
        scanResults();
        wiFiData = transformer.transformToWiFiData(cache.getScanResults(), cache.getWifiInfo());
        IterableUtils.forEach(updateNotifiers, new UpdateClosure());
    }

    @Override
    @NonNull
    public WiFiData getWiFiData() {
        return wiFiData;
    }

    @Override
    public void register(@NonNull UpdateNotifier updateNotifier) {
        updateNotifiers.add(updateNotifier);
    }

    @Override
    public void unregister(@NonNull UpdateNotifier updateNotifier) {
        updateNotifiers.remove(updateNotifier);
    }

    @Override
    public void pause() {
        periodicScan.stop();
    }

    @Override
    public boolean isRunning() {
        return periodicScan.isRunning();
    }

    @Override
    public void resume() {
        periodicScan.start();
    }

    @Override
    public void stop() {
        if (!BuildUtils.isMinVersionQ()) {
            try {
                if (settings.isWiFiOffOnExit()) {
                    disableWiFiLegacy();
                }
            } catch (Exception e) {
                // critical error: do not die
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void disableWiFiLegacy() {
        wifiManager.setWifiEnabled(false);
    }

    @NonNull
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

    @NonNull
    List<UpdateNotifier> getUpdateNotifiers() {
        return updateNotifiers;
    }

    private void enableWiFi() {
        try {
            if (!wifiManager.isWifiEnabled()) {
                if (BuildUtils.isMinVersionQ()) {
                    enableWiFiAndroidQ();
                } else {
                    enableWiFiLegacy();
                }
            }
        } catch (Exception e) {
            // critical error: do not die
        }
    }

    @TargetApi(Build.VERSION_CODES.Q)
    private void enableWiFiAndroidQ() {
        ActivityUtils.startWiFiSettings();
    }

    @SuppressWarnings("deprecation")
    private void enableWiFiLegacy() {
        wifiManager.setWifiEnabled(true);
    }

    private void scanResults() {
        try {
            if (wifiManager.startScan()) {
                List<ScanResult> scanResults = wifiManager.getScanResults();
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                if (scanResults != null) {
                    cache.add(scanResults, wifiInfo);
                }
            }
        } catch (Exception e) {
            // critical error: do not die
        }
    }

    private class UpdateClosure implements Closure<UpdateNotifier> {
        @Override
        public void execute(UpdateNotifier updateNotifier) {
            updateNotifier.update(wiFiData);
        }
    }
}
