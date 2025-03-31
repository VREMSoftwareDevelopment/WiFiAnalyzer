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
package com.vrem.wifianalyzer.wifi.band

import com.vrem.wifianalyzer.wifi.model.WiFiWidth

internal const val FREQUENCY_SPREAD = 5

typealias WiFiRange = Pair<Int, Int>
typealias WiFiChannelPair = Pair<WiFiChannel, WiFiChannel>

private val countriesETSI = listOf(
    "AT",
    "BE",
    "CH",
    "CY",
    "CZ",
    "DE",
    "DK",
    "EE",
    "ES",
    "FI",
    "FR",
    "GR",
    "HU",
    "IE",
    "IS",
    "IT",
    "LI",
    "LT",
    "LU",
    "LV",
    "MT",
    "NL",
    "NO",
    "PL",
    "PT",
    "RO",
    "SE",
    "SI",
    "SK",
    "IL"
)

private val excludeGHZ5: List<Map<String, List<Int>>> =
    countriesETSI.map { mapOf(it to listOf(177)) } +
        listOf(
            "AU" to (120..128).toList() + listOf(177),
            "CA" to (120..128).toList() + (169..177).toList(),
            "UK" to (120..128).toList() + listOf(177),
            "RU" to (96..128).toList() + (173..177).toList(),
            "JP" to (96..128).toList() + (149..177).toList(),
            "IN" to listOf(177),
            "SG" to (169..177).toList(),
            "CH" to (96..144).toList() + (169..177).toList(),
            "IL" to listOf(177),
            "KR" to (169..177).toList(),
            "TR" to listOf(144) + (149..177).toList(),
            "ZA" to listOf(144) + (149..177).toList(),
            "BR" to (169..177).toList(),
            "TW" to (169..177).toList(),
            "NZ" to (169..177).toList(),
            "BH" to (96..144).toList() + (169..177).toList(),
            "VN" to (169..177).toList(),
            "ID" to (96..144).toList() + (165..177).toList(),
            "PH" to (173..177).toList()
        ).map { mapOf(it.first to it.second) }

private val excludeGHZ6: List<Map<String, List<Int>>> =
    (countriesETSI + listOf("JP", "RU", "NZ", "AU", "GL", "AE", "GB", "MX", "SG", "HK", "MO", "PH"))
        .map { mapOf(it to (97..223).toList()) }

internal val wiFiChannelsGHZ2 = WiFiChannels(
    WiFiChannelPair(WiFiChannel(-1, 2402), WiFiChannel(15, 2482)),
    1,
    mapOf(
        WiFiWidth.MHZ_20 to listOf(1, 2, 3, 6, 7, 8, 11, 12, 13),
        WiFiWidth.MHZ_40 to listOf(3, 7, 11)
    ),
    (1..13).associateWith { "$it" },
    listOf(1, 2, 3, 6, 7, 8, 11, 12, 13)
)

internal val wiFiChannelsGHZ5 = WiFiChannels(
    WiFiChannelPair(WiFiChannel(30, 5150), WiFiChannel(184, 5920)),
    2,
    mapOf(
        WiFiWidth.MHZ_20 to ((32..144 step WiFiWidth.MHZ_20.step) + (149..177 step WiFiWidth.MHZ_20.step)),
        WiFiWidth.MHZ_40 to ((38..142 step WiFiWidth.MHZ_40.step) + (151..175 step WiFiWidth.MHZ_40.step)),
        WiFiWidth.MHZ_80 to ((42..138 step WiFiWidth.MHZ_80.step) + (155..171 step WiFiWidth.MHZ_80.step)),
        WiFiWidth.MHZ_160 to ((50..114 step WiFiWidth.MHZ_160.step) + 163)
    ),
    listOf(34, 50, 66, 82, 98, 113, 129, 147, 163, 178).associateWith {
        when (it) {
            113 -> "114"
            129 -> "130"
            178 -> "179"
            else -> "$it"
        }
    },
    ((42..138 step WiFiWidth.MHZ_80.step) + (155..171 step WiFiWidth.MHZ_80.step) +
        (50..114 step WiFiWidth.MHZ_160.step) + 163).toSortedSet().toList(),
    excludeGHZ5
)

internal val wiFiChannelsGHZ6 = WiFiChannels(
    WiFiChannelPair(WiFiChannel(-5, 5925), WiFiChannel(235, 7125)),
    2,
    mapOf(
        WiFiWidth.MHZ_20 to (1..233 step WiFiWidth.MHZ_20.step).toList(),
        WiFiWidth.MHZ_40 to (3..227 step WiFiWidth.MHZ_40.step).toList(),
        WiFiWidth.MHZ_80 to (7..215 step WiFiWidth.MHZ_80.step).toList(),
        WiFiWidth.MHZ_160 to (15..207 step WiFiWidth.MHZ_160.step).toList(),
        WiFiWidth.MHZ_320 to (31..191 step WiFiWidth.MHZ_320.step).toList()
    ),
    listOf(1, 31, 63, 95, 128, 160, 192, 222).associateWith {
        when (it) {
            128 -> "127"
            160 -> "159"
            192 -> "191"
            222 -> "223"
            else -> "$it"
        }
    },
    ((15..207 step WiFiWidth.MHZ_160.step) + (31..191 step WiFiWidth.MHZ_320.step)).toSortedSet().toList(),
    excludeGHZ6
)