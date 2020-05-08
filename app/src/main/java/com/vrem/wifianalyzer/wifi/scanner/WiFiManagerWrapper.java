/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2020 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;

class WiFiManagerWrapper {
    private final WifiManager wifiManager;
    private WiFiSwitch wiFiSwitch;

    WiFiManagerWrapper(@NonNull WifiManager wifiManager) {
        this.wifiManager = wifiManager;
        this.wiFiSwitch = new WiFiSwitch(wifiManager);
    }

    boolean isWifiEnabled() {
        try {
            return wifiManager.isWifiEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    boolean enableWiFi() {
        try {
            return isWifiEnabled() || wiFiSwitch.setEnabled(true);
        } catch (Exception e) {
            return false;
        }
    }

    boolean disableWiFi() {
        try {
            return !isWifiEnabled() || wiFiSwitch.setEnabled(false);
        } catch (Exception e) {
            return false;
        }
    }

    @SuppressWarnings("deprecation")
    boolean startScan() {
        try {
            return wifiManager.startScan();
        } catch (Exception e) {
            return false;
        }
    }

    @NonNull
    List<ScanResult> scanResults() {
        try {
            List<ScanResult> results = wifiManager.getScanResults();
            return results == null ? Collections.emptyList() : results;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    WifiInfo wiFiInfo() {
        try {
            return wifiManager.getConnectionInfo();
        } catch (Exception e) {
            return null;
        }
    }

    void setWiFiSwitch(@NonNull WiFiSwitch wiFiSwitch) {
        this.wiFiSwitch = wiFiSwitch;
    }
}
