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

data class WiFiDetail(val rawSSID: String = StringUtils.EMPTY,
                      val BSSID: String = StringUtils.EMPTY,
                      val capabilities: String = StringUtils.EMPTY,
                      val wiFiSignal: WiFiSignal = WiFiSignal.EMPTY,
                      val wiFiAdditional: WiFiAdditional = WiFiAdditional.EMPTY) :
        Comparable<WiFiDetail> {

    constructor(rawSSID: String, BSSID: String, capabilities: String, wiFiSignal: WiFiSignal) :
            this(rawSSID, BSSID, capabilities, wiFiSignal, WiFiAdditional.EMPTY)

    constructor(wiFiDetail: WiFiDetail, wiFiAdditional: WiFiAdditional) :
            this(wiFiDetail.rawSSID, wiFiDetail.BSSID, wiFiDetail.capabilities, wiFiDetail.wiFiSignal, wiFiAdditional)

    val SSID = when {
        rawSSID.isEmpty() -> SSID_EMPTY
        else -> rawSSID
    }

    private val children: MutableList<WiFiDetail> = ArrayList()

    fun getSecurity(): Security = Security.findOne(capabilities)

    fun getSecurities(): Set<Security> = Security.findAll(capabilities)

    fun getChildren(): List<WiFiDetail> = children

    fun noChildren(): Boolean = !getChildren().isEmpty()

    fun getTitle(): String = String.format("%s (%s)", SSID, BSSID)

    fun addChild(wiFiDetail: WiFiDetail) {
        children.add(wiFiDetail)
    }

    override fun compareTo(other: WiFiDetail): Int = when {
        SSID != other.SSID -> SSID.compareTo(other.SSID)
        else -> BSSID.compareTo(other.BSSID)
    }

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
    }

}