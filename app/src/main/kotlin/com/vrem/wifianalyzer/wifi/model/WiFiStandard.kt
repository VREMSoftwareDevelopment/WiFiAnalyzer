/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2023 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.wifi.model

import android.net.wifi.ScanResult
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.vrem.util.buildMinVersionR
import com.vrem.util.buildMinVersionS
import com.vrem.util.buildMinVersionT
import com.vrem.wifianalyzer.R

typealias WiFiStandardId = Int

private val unknown: WiFiStandardId = if (buildMinVersionR()) ScanResult.WIFI_STANDARD_UNKNOWN else 0
private val legacy: WiFiStandardId = if (buildMinVersionR()) ScanResult.WIFI_STANDARD_LEGACY else 1
private val n: WiFiStandardId = if (buildMinVersionR()) ScanResult.WIFI_STANDARD_11N else 4
private val ac: WiFiStandardId = if (buildMinVersionR()) ScanResult.WIFI_STANDARD_11AC else 5
private val ax: WiFiStandardId = if (buildMinVersionR()) ScanResult.WIFI_STANDARD_11AX else 6
private val ad: WiFiStandardId = if (buildMinVersionS()) ScanResult.WIFI_STANDARD_11AD else 7
private val be: WiFiStandardId = if (buildMinVersionT()) ScanResult.WIFI_STANDARD_11BE else 8


enum class WiFiStandard(
    val wiFiStandardId: WiFiStandardId,
    @StringRes val textResource: Int,
    @DrawableRes val imageResource: Int
) {
    UNKNOWN(unknown, R.string.wifi_standard_unknown, R.drawable.ic_wifi_unknown),
    LEGACY(legacy, R.string.wifi_standard_legacy, R.drawable.ic_wifi_legacy),
    N(n, R.string.wifi_standard_n, R.drawable.ic_wifi_4),
    AC(ac, R.string.wifi_standard_ac, R.drawable.ic_wifi_5),
    AX(ax, R.string.wifi_standard_ax, R.drawable.ic_wifi_6),
    AD(ad, R.string.wifi_standard_ad, R.drawable.ic_wifi_unknown),
    BE(be, R.string.wifi_standard_be, R.drawable.ic_wifi_unknown);

    companion object {
        fun findOne(wiFiStandardId: WiFiStandardId): WiFiStandard =
            entries.find { it.wiFiStandardId == wiFiStandardId } ?: UNKNOWN
    }
}