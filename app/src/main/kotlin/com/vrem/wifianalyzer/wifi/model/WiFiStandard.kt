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

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.vrem.wifianalyzer.R

typealias WiFiStandardId = Int

enum class WiFiStandard(val wiFiStandardId: WiFiStandardId, @StringRes val textResource: Int, @DrawableRes val imageResource: Int) {
    UNKNOWN(0, R.string.wifi_standard_unknown, R.drawable.ic_wifi_unknown),
    LEGACY(1, R.string.wifi_standard_legacy, R.drawable.ic_wifi_legacy),
    N(4, R.string.wifi_standard_n, R.drawable.ic_wifi_4),
    AC(5, R.string.wifi_standard_ac, R.drawable.ic_wifi_5),
    AX(6, R.string.wifi_standard_ax, R.drawable.ic_wifi_6),
    AD(7, R.string.wifi_standard_ad, R.drawable.ic_wifi_unknown),
    BE(8, R.string.wifi_standard_be, R.drawable.ic_wifi_unknown);

    companion object {
        fun findOne(wiFiStandardId: WiFiStandardId): WiFiStandard = values().firstOrNull { it.wiFiStandardId == wiFiStandardId } ?: UNKNOWN
    }
}