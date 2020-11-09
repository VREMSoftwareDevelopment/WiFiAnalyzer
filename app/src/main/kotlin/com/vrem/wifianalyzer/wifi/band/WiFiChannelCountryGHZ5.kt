/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2020 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.wifi.band

import java.util.*

internal class WiFiChannelCountryGHZ5 {
    private val channelsSet1: SortedSet<Int> = sortedSetOf(36, 40, 44, 48, 52, 56, 60, 64)
    private val channelsSet2: SortedSet<Int> = sortedSetOf(100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)
    private val channelsSet3: SortedSet<Int> = sortedSetOf(149, 153, 157, 161, 165)
    private val channelsToExcludeCanada: SortedSet<Int> = sortedSetOf(120, 124, 128)
    private val channelsToExcludeIsrael: SortedSet<Int> = channelsSet2.union(channelsSet3).toSortedSet()
    private val channelsToExcludeETSI: SortedSet<Int> = sortedSetOf(144)
    private val channelsToIncludeETSI: SortedSet<Int> = sortedSetOf(169, 173)
    private val channels: SortedSet<Int> = channelsSet1.union(channelsSet2).union(channelsSet3).toSortedSet()

    private val channelsToExclude: Map<String, SortedSet<Int>> = mapOf(
            "AU" to channelsToExcludeCanada,    // Australia
            "CA" to channelsToExcludeCanada,    // Canada
            "CN" to channelsSet2,               // China
            "IL" to channelsToExcludeIsrael,    // Israel
            "JP" to channelsSet3,               // Japan
            "KR" to channelsSet2,               // South Korea
            "TR" to channelsSet3,               // Turkey
            "ZA" to channelsSet3,               // South Africa
            "AT" to channelsToExcludeETSI,      // ETSI Austria
            "BE" to channelsToExcludeETSI,      // ETSI Belgium
            "CH" to channelsToExcludeETSI,      // ETSI Switzerland
            "CY" to channelsToExcludeETSI,      // ETSI Cyprus
            "CZ" to channelsToExcludeETSI,      // ETSI Czechia
            "DE" to channelsToExcludeETSI,      // ETSI Germany
            "DK" to channelsToExcludeETSI,      // ETSI Denmark
            "EE" to channelsToExcludeETSI,      // ETSI Estonia
            "ES" to channelsToExcludeETSI,      // ETSI Spain
            "FI" to channelsToExcludeETSI,      // ETSI Finland
            "FR" to channelsToExcludeETSI,      // ETSI France
            "GR" to channelsToExcludeETSI,      // ETSI Greece
            "HU" to channelsToExcludeETSI,      // ETSI Hungary
            "IE" to channelsToExcludeETSI,      // ETSI Ireland
            "IS" to channelsToExcludeETSI,      // ETSI Iceland
            "IT" to channelsToExcludeETSI,      // ETSI Italy
            "LI" to channelsToExcludeETSI,      // ETSI Liechtenstein
            "LT" to channelsToExcludeETSI,      // ETSI Lithuania
            "LU" to channelsToExcludeETSI,      // ETSI Luxembourg
            "LV" to channelsToExcludeETSI,      // ETSI Latvia
            "MT" to channelsToExcludeETSI,      // ETSI Malta
            "NL" to channelsToExcludeETSI,      // ETSI Netherlands
            "NO" to channelsToExcludeETSI,      // ETSI Norway
            "PL" to channelsToExcludeETSI,      // ETSI Poland
            "PT" to channelsToExcludeETSI,      // ETSI Portugal
            "RO" to channelsToExcludeETSI,      // ETSI Romania
            "SE" to channelsToExcludeETSI,      // ETSI Sweden
            "SI" to channelsToExcludeETSI,      // ETSI Slovenia
            "SK" to channelsToExcludeETSI       // ETSI Slovakia
    )

    private val channelsToInclude: Map<String, SortedSet<Int>> = mapOf(
            "AT" to channelsToIncludeETSI,      // ETSI Austria
            "BE" to channelsToIncludeETSI,      // ETSI Belgium
            "CH" to channelsToIncludeETSI,      // ETSI Switzerland
            "CY" to channelsToIncludeETSI,      // ETSI Cyprus
            "CZ" to channelsToIncludeETSI,      // ETSI Czechia
            "DE" to channelsToIncludeETSI,      // ETSI Germany
            "DK" to channelsToIncludeETSI,      // ETSI Denmark
            "EE" to channelsToIncludeETSI,      // ETSI Estonia
            "ES" to channelsToIncludeETSI,      // ETSI Spain
            "FI" to channelsToIncludeETSI,      // ETSI Finland
            "FR" to channelsToIncludeETSI,      // ETSI France
            "GR" to channelsToIncludeETSI,      // ETSI Greece
            "HU" to channelsToIncludeETSI,      // ETSI Hungary
            "IE" to channelsToIncludeETSI,      // ETSI Ireland
            "IS" to channelsToIncludeETSI,      // ETSI Iceland
            "IT" to channelsToIncludeETSI,      // ETSI Italy
            "LI" to channelsToIncludeETSI,      // ETSI Liechtenstein
            "LT" to channelsToIncludeETSI,      // ETSI Lithuania
            "LU" to channelsToIncludeETSI,      // ETSI Luxembourg
            "LV" to channelsToIncludeETSI,      // ETSI Latvia
            "MT" to channelsToIncludeETSI,      // ETSI Malta
            "NL" to channelsToIncludeETSI,      // ETSI Netherlands
            "NO" to channelsToIncludeETSI,      // ETSI Norway
            "PL" to channelsToIncludeETSI,      // ETSI Poland
            "PT" to channelsToIncludeETSI,      // ETSI Portugal
            "RO" to channelsToIncludeETSI,      // ETSI Romania
            "SE" to channelsToIncludeETSI,      // ETSI Sweden
            "SI" to channelsToIncludeETSI,      // ETSI Slovenia
            "SK" to channelsToIncludeETSI       // ETSI Slovakia
    )

    fun findChannels(countryCode: String): SortedSet<Int> =
            channels.subtract(channelsToExclude[countryCode.capitalize()]?.toSortedSet()
                    ?: setOf()).union(channelsToInclude[countryCode.capitalize()]?.toSortedSet()
                    ?: setOf())
                    .toSortedSet()

}
