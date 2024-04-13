/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2024 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import com.vrem.util.buildMinVersionR
import com.vrem.wifianalyzer.R

typealias WiFiStandardId = Int

enum class WiFiStandard(val wiFiStandardId: WiFiStandardId, @StringRes val fullResource: Int, @StringRes val valueResource: Int) {
    UNKNOWN(0, R.string.wifi_standard_unknown, R.string.wifi_standard_unknown),
    LEGACY(1, R.string.wifi_standard_legacy, R.string.wifi_standard_unknown),
    N(4, R.string.wifi_standard_n, R.string.wifi_standard_value_n),
    AC(5, R.string.wifi_standard_ac, R.string.wifi_standard_value_ac),
    AX(6, R.string.wifi_standard_ax, R.string.wifi_standard_value_ax),
    AD(7, R.string.wifi_standard_ad, R.string.wifi_standard_unknown),
    BE(8, R.string.wifi_standard_be, R.string.wifi_standard_value_be);

    companion object {
        fun findOne(scanResult: ScanResult): WiFiStandard =
            if (buildMinVersionR()) {
                find(scanResult.wifiStandard)
            } else {
                UNKNOWN
            }

        @RequiresApi(Build.VERSION_CODES.R)
        private fun find(wifiStandard: Int): WiFiStandard =
            entries.firstOrNull { it.wiFiStandardId == wifiStandard } ?: UNKNOWN
    }
}