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
import java.nio.ByteBuffer

enum class FastRoaming(val protocol: String) {
    REQUIRE_ANDROID_R(""),
    K("802.11k"),
    V("802.11v"),
    R("802.11r");

    companion object {
        @RequiresApi(Build.VERSION_CODES.R)
        fun transformOrNull(it: ScanResult.InformationElement): FastRoaming? =
            if (it.id == WiFiSignal.RM_ENABLED_CAPABILITIES_IE &&
                validRoamingBit(
                    it.bytes,
                    WiFiSignal.NEIGHBOR_REPORT_IDX,
                    WiFiSignal.NEIGHBOR_REPORT_BIT
                )
            ) K
            else if (it.id == WiFiSignal.EXTENDED_CAPABILITIES_IE &&
                validRoamingBit(
                    it.bytes,
                    WiFiSignal.BSS_TRANSITION_IDX,
                    WiFiSignal.BSS_TRANSITION_BIT
                )
            ) V
            else if (it.id == WiFiSignal.MOBILE_DOMAIN_IE
            ) R
            else null

        // some evil access point broadcasts illegal package, which may trigger oob exception
        // make sure limit > index first
        private fun validRoamingBit(data: ByteBuffer, idx: Int, bit: Int) =
            data.limit() > idx && data[idx].toInt().and(1 shl bit) == (1 shl bit)
    }
}
