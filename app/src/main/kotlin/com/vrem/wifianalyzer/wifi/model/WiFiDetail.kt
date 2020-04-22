/*
 * WiFiAnalyzer
 * Copyright (C) 2020  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import com.vrem.util.StringUtils

data class WiFiDetail @JvmOverloads constructor(val rawSSID: String = StringUtils.EMPTY,
                                                val BSSID: String = StringUtils.EMPTY,
                                                val capabilities: String = StringUtils.EMPTY,
                                                val wiFiSignal: WiFiSignal = WiFiSignal.EMPTY,
                                                val wiFiAdditional: WiFiAdditional = WiFiAdditional.EMPTY,
                                                val children: List<WiFiDetail> = emptyList()) :
        Comparable<WiFiDetail> {

    constructor(wiFiDetail: WiFiDetail, wiFiAdditional: WiFiAdditional) :
            this(wiFiDetail.rawSSID, wiFiDetail.BSSID, wiFiDetail.capabilities, wiFiDetail.wiFiSignal, wiFiAdditional)

    constructor(wiFiDetail: WiFiDetail, children: List<WiFiDetail>) :
            this(wiFiDetail.rawSSID, wiFiDetail.BSSID, wiFiDetail.capabilities, wiFiDetail.wiFiSignal, wiFiDetail.wiFiAdditional, children)

    val SSID = when {
        rawSSID.isEmpty() -> SSID_EMPTY
        else -> rawSSID
    }

    fun security(): Security = Security.findOne(capabilities)

    fun securities(): Set<Security> = Security.findAll(capabilities)

    fun noChildren(): Boolean = children.isNotEmpty()

    fun title(): String = String.format("%s (%s)", SSID, BSSID)

    override fun compareTo(other: WiFiDetail): Int =
            compareBy<WiFiDetail> { it.SSID }.thenBy { it.BSSID }.compare(this, other)

    override fun equals(other: Any?): Boolean {

        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WiFiDetail

        if (SSID != other.SSID) return false
        if (BSSID != other.BSSID) return false

        return true
    }

    override fun hashCode(): Int = 31 * rawSSID.hashCode() + BSSID.hashCode()

    companion object {
        const val SSID_EMPTY = "*hidden*"

        @JvmField
        val EMPTY = WiFiDetail()

        @JvmStatic
        fun sortBySSID(): Comparator<WiFiDetail> =
                compareBy<WiFiDetail> { it.SSID }
                        .thenByDescending { it.wiFiSignal.level }
                        .thenBy { it.BSSID }

        @JvmStatic
        fun sortByStrength(): Comparator<WiFiDetail> =
                compareByDescending<WiFiDetail> { it.wiFiSignal.level }
                        .thenBy { it.SSID }
                        .thenBy { it.BSSID }

        @JvmStatic
        fun sortByChannel(): Comparator<WiFiDetail> =
                compareBy<WiFiDetail> { it.wiFiSignal.primaryWiFiChannel().channel }
                        .thenByDescending { it.wiFiSignal.level }
                        .thenBy { it.SSID }
                        .thenBy { it.BSSID }

        @JvmStatic
        fun sortByDefault(): Comparator<WiFiDetail> =
                compareBy<WiFiDetail> { it.SSID }.thenBy { it.BSSID }

    }
}