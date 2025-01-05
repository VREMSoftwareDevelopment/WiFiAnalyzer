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
package com.vrem.util

import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager.PackageInfoFlags
import android.content.res.Configuration
import android.content.res.Resources
import android.net.wifi.ScanResult
import android.os.Build
import java.util.*

fun Context.createContext(newLocale: Locale): Context {
    val resources: Resources = resources
    val configuration: Configuration = resources.configuration
    configuration.setLocale(newLocale)
    return createConfigurationContext(configuration)
}

fun Context.packageInfo(): PackageInfo =
    if (buildMinVersionT()) {
        packageInfoAndroidT()
    } else {
        packageInfoLegacy()
    }

@TargetApi(Build.VERSION_CODES.TIRAMISU)
private fun Context.packageInfoAndroidT(): PackageInfo =
    packageManager.getPackageInfo(packageName, PackageInfoFlags.of(0))

private fun Context.packageInfoLegacy(): PackageInfo =
    packageManager.getPackageInfo(packageName, 0)

fun ScanResult.ssid(): String =
    if (buildMinVersionT()) {
        ssidAndroidT()
    } else {
        ssidLegacy()
    }.removeSurrounding("\"")

@TargetApi(Build.VERSION_CODES.TIRAMISU)
private fun ScanResult.ssidAndroidT(): String = String.nullToEmpty(wifiSsid?.toString())

@Suppress("DEPRECATION")
private fun ScanResult.ssidLegacy(): String = String.nullToEmpty(SSID)
