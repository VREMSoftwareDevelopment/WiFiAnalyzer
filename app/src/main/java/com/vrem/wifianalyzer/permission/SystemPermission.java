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

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Build;

import com.vrem.util.BuildUtils;

import androidx.annotation.NonNull;

class SystemPermission {
    private final Activity activity;

    SystemPermission(@NonNull Activity activity) {
        this.activity = activity;
    }

    boolean isEnabled() {
        return !BuildUtils.isMinVersionM() || isProviderEnabledAndroidM();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean isProviderEnabledAndroidM() {
        try {
            LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            return isLocationEnabled(locationManager) || isNetworkProviderEnabled(locationManager) || isGPSProviderEnabled(locationManager);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isGPSProviderEnabled(@NonNull LocationManager locationManager) {
        try {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isNetworkProviderEnabled(@NonNull LocationManager locationManager) {
        try {
            return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isLocationEnabled(@NonNull LocationManager locationManager) {
        return BuildUtils.isMinVersionP() && isLocationEnabledAndroidP(locationManager);
    }

    @TargetApi(Build.VERSION_CODES.P)
    private boolean isLocationEnabledAndroidP(@NonNull LocationManager locationManager) {
        try {
            return locationManager.isLocationEnabled();
        } catch (Exception e) {
            return false;
        }
    }

}
