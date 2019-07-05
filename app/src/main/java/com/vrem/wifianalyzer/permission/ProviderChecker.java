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

package com.vrem.wifianalyzer.permission;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.support.annotation.NonNull;

import com.vrem.util.BuildUtils;

public class ProviderChecker {
    private final Activity activity;

    public ProviderChecker(@NonNull Activity activity) {
        this.activity = activity;
    }

    public boolean isEnabled() {
        if (BuildUtils.isMinVersionM()) {
            return isProviderEnabled();
        } else {
            return true;
        }
    }

    private boolean isProviderEnabled() {
        try {
            LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            return isNetworkEnabled || isGPSEnabled;
        } catch (Exception e) {
            return false;
        }
    }
}
