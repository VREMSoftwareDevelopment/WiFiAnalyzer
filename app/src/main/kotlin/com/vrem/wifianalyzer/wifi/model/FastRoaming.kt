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
import com.vrem.util.buildMinVersionR
import com.vrem.wifianalyzer.R
import java.nio.ByteBuffer

typealias Available = (id: Int, bytes: ByteBuffer) -> Boolean

private const val ID_MOBILE_DOMAIN = 54
private const val ID_RM_ENABLED_CAPABILITIES = 70
private const val ID_EXTENDED_CAPABILITIES = 127
private const val ID_VENDOR_SPECIFIC = 221

/**
 * Checks for 802.11k (Radio Resource Management) support.
 * Returns true if the Information Element is RM Enabled Capabilities (IE 70)
 * and the Neighbor Report bit (bit 1 of the first byte) is set.
 */
private val available802_11k: Available = { id: Int, bytes: ByteBuffer ->
    id == ID_RM_ENABLED_CAPABILITIES &&
        bytes.remaining() > 0 &&
        (bytes[0].toInt() and (1 shl 1)) != 0
}

/**
 * Checks for 802.11r (Fast BSS Transition) support.
 * Returns true if the Information Element is Mobile Domain IE (IE 54).
 */
private val available802_11r: Available = { id: Int, _: ByteBuffer -> id == ID_MOBILE_DOMAIN }

/**
 * Checks for 802.11v (BSS Transition Management) support.
 * Returns true if the Information Element is Extended Capabilities (IE 127)
 * and the BSS Transition bit (bit 3 of the third byte) is set.
 */
private val available802_11v: Available = { id: Int, bytes: ByteBuffer ->
    id == ID_EXTENDED_CAPABILITIES &&
        bytes.remaining() > 2 &&
        (bytes[2].toInt() and (1 shl 3)) != 0
}

/**
 * Checks for Opportunistic Key Caching (OKC) support.
 * Returns true if the Information Element is Vendor Specific (IE 221)
 * and the first 4 bytes match the OKC OUI and type.
 */
private val availableOKC: Available = { id: Int, bytes: ByteBuffer ->
    id == ID_VENDOR_SPECIFIC &&
        bytes.remaining() >= 4 &&
        bytes[0] == 0x00.toByte() &&
        bytes[1] == 0x0F.toByte() &&
        bytes[2] == 0xAC.toByte() &&
        bytes[3] == 0x12.toByte()
}

enum class FastRoaming(
    val textResource: Int,
    val available: Available,
) {
    FR_802_11K(R.string.fast_roaming_k, available802_11k),
    FR_802_11R(R.string.fast_roaming_r, available802_11r),
    FR_802_11V(R.string.fast_roaming_v, available802_11v),
    FR_OKC(R.string.fast_roaming_okc, availableOKC),
    ;

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
                        runCatching { fastRoaming.available(it.id, it.bytes) }
                            .getOrDefault(false)
                    }
                }.toList()
    }
}
