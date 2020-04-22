/*
 * WiFiAnalyzer
 * Copyright (C) 2020  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import android.net.wifi.WifiManager;
import android.os.Build;

import com.vrem.util.BuildUtils;
import com.vrem.wifianalyzer.ActivityUtils;

import androidx.annotation.NonNull;

class WiFiSwitch {
    private final WifiManager wifiManager;

    WiFiSwitch(@NonNull WifiManager wifiManager) {
        this.wifiManager = wifiManager;
    }

    boolean setEnabled(boolean enabled) {
        return BuildUtils.isMinVersionQ() ? enableWiFiAndroidQ() : enableWiFiLegacy(enabled);
    }

    @TargetApi(Build.VERSION_CODES.Q)
    private boolean enableWiFiAndroidQ() {
        startWiFiSettings();
        return true;
    }

    @SuppressWarnings("deprecation")
    private boolean enableWiFiLegacy(boolean enabled) {
        return wifiManager.setWifiEnabled(enabled);
    }

    void startWiFiSettings() {
        new ActivityUtils().startWiFiSettings();
    }
}
