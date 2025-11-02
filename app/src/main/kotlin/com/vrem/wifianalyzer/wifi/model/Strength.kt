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
package com.vrem.wifianalyzer.wifi.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.vrem.wifianalyzer.R

enum class Strength(
    @DrawableRes val imageResource: Int,
    @ColorRes val colorResource: Int,
) {
    ZERO(R.drawable.ic_signal_wifi_0_bar, R.color.error),
    ONE(R.drawable.ic_signal_wifi_1_bar, R.color.warning),
    TWO(R.drawable.ic_signal_wifi_2_bar, R.color.warning),
    THREE(R.drawable.ic_signal_wifi_3_bar, R.color.success),
    FOUR(R.drawable.ic_signal_wifi_4_bar, R.color.success),
    ;

    fun weak(): Boolean = ZERO == this

    companion object {
        fun fromRssi(rssi: Int): Strength = entries[calculateSignalLevel(rssi, entries.size)]

        fun reverse(strength: Strength): Strength = entries[entries.size - strength.ordinal - 1]

        /**
         * RSSI thresholds are based on common Wi-Fi signal quality guidelines:
         * - -67 dBm or higher: Excellent signal (mapped to [R.color.success])
         * - -70 dBm to -68 dBm: Good signal (mapped to [R.color.warning])
         * - Below -70 dBm: Weak or poor signal (mapped to [R.color.error])
         */
        fun colorForRssi(rssi: Int): Int =
            when {
                rssi >= -67 -> R.color.success
                rssi >= -70 -> R.color.warning
                else -> R.color.error
            }
    }
}
