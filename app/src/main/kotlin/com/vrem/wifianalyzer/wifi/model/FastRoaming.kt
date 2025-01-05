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

import android.net.wifi.ScanResult
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import com.vrem.util.buildMinVersionR
import com.vrem.wifianalyzer.R
import java.nio.ByteBuffer

internal const val RM_ENABLED_CAPABILITIES_IE = 70
internal const val NEIGHBOR_REPORT_IDX = 0
internal const val NEIGHBOR_REPORT_BIT = 1
internal const val EXTENDED_CAPABILITIES_IE = 127
internal const val BSS_TRANSITION_IDX = 2
internal const val BSS_TRANSITION_BIT = 3
internal const val MOBILE_DOMAIN_IE = 54

private fun ByteBuffer.contains(idx: Int, bit: Int): Boolean =
    this[idx].toInt().and(1 shl bit) == (1 shl bit)

typealias Available = (id: Int, bytes: ByteBuffer) -> Boolean

private val available802_11k: Available = { id: Int, bytes: ByteBuffer ->
    RM_ENABLED_CAPABILITIES_IE == id &&
        bytes.contains(NEIGHBOR_REPORT_IDX, NEIGHBOR_REPORT_BIT)
}
private val available802_11r: Available = { id: Int, _: ByteBuffer ->
    MOBILE_DOMAIN_IE == id
}
private val available802_11v: Available = { id: Int, bytes: ByteBuffer ->
    EXTENDED_CAPABILITIES_IE == id &&
        bytes.contains(BSS_TRANSITION_IDX, BSS_TRANSITION_BIT)
}

enum class FastRoaming(@StringRes val textResource: Int, val available: Available) {
    FR_802_11K(R.string.fast_roaming_k, available802_11k),
    FR_802_11R(R.string.fast_roaming_r, available802_11r),
    FR_802_11V(R.string.fast_roaming_v, available802_11v);

    companion object {
        fun find(scanResult: ScanResult): List<FastRoaming> =
            if (buildMinVersionR()) {
                find(scanResult.informationElements)
            } else {
                listOf()
            }

        @RequiresApi(Build.VERSION_CODES.R)
        private fun find(informationElements: List<ScanResult.InformationElement>): List<FastRoaming> =
            entries
                .filter { fastRoaming ->
                    informationElements.any {
                        try {
                            fastRoaming.available(it.id, it.bytes)
                        } catch (e: Exception) {
                            false
                        }
                    }
                }
                .toList()
    }
}
