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

data class WiFiDetail(
    val wiFiIdentifier: WiFiIdentifier = WiFiIdentifier.EMPTY,
    val wiFiSecurity: WiFiSecurity = WiFiSecurity.EMPTY,
    val wiFiSignal: WiFiSignal = WiFiSignal.EMPTY,
    val wiFiAdditional: WiFiAdditional = WiFiAdditional.EMPTY,
    val children: List<WiFiDetail> = listOf()
) : Comparable<WiFiDetail> {

    constructor(wiFiDetail: WiFiDetail, wiFiAdditional: WiFiAdditional) :
        this(wiFiDetail.wiFiIdentifier, wiFiDetail.wiFiSecurity, wiFiDetail.wiFiSignal, wiFiAdditional)

    constructor(wiFiDetail: WiFiDetail, children: List<WiFiDetail>) :
        this(wiFiDetail.wiFiIdentifier, wiFiDetail.wiFiSecurity, wiFiDetail.wiFiSignal, wiFiDetail.wiFiAdditional, children)

    val noChildren: Boolean
        get() = children.isNotEmpty()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WiFiDetail

        return wiFiIdentifier == other.wiFiIdentifier
    }

    override fun hashCode(): Int = wiFiIdentifier.hashCode()

    override fun compareTo(other: WiFiDetail): Int = wiFiIdentifier.compareTo(other.wiFiIdentifier)

    companion object {
        val EMPTY = WiFiDetail()
    }
}

