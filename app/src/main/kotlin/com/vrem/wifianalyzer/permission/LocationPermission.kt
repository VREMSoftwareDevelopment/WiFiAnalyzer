/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2025 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.permission

import android.annotation.TargetApi
import android.app.Activity
import android.location.LocationManager
import android.os.Build
import com.vrem.annotation.OpenClass
import com.vrem.util.buildMinVersionP

@OpenClass
class LocationPermission(private val activity: Activity) {
    fun enabled(): Boolean =
        if (buildMinVersionP()) {
            try {
                val locationManager = activity.getSystemService(LocationManager::class.java)
                locationEnabled(locationManager) ||
                    networkProviderEnabled(locationManager) ||
                    gpsProviderEnabled(locationManager)
            } catch (e: Exception) {
                false
            }
        } else {
            true
        }

    private fun gpsProviderEnabled(locationManager: LocationManager): Boolean =
        try {
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            false
        }

    private fun networkProviderEnabled(locationManager: LocationManager): Boolean =
        try {
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (e: Exception) {
            false
        }

    @TargetApi(Build.VERSION_CODES.P)
    private fun locationEnabled(locationManager: LocationManager): Boolean =
        try {
            locationManager.isLocationEnabled
        } catch (e: Exception) {
            false
        }

}