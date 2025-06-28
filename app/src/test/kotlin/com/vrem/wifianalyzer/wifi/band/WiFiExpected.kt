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

private val expectedETSICountries = listOf(
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

private val expectedNACountries = listOf("AS", "CA", "CO", "DO", "FM", "GT", "GU", "MP", "MX", "PA", "PR", "UM", "US", "UZ", "VI")

private val expectedGHZ6Countries = listOf("JP", "RU", "NZ", "AU", "GL", "AE", "GB", "MX", "SG", "HK", "MO", "PH")

private val expectedChannelExcludeGHZ2: List<Map<String, List<Int>>> =
    expectedNACountries.map { mapOf(it to listOf(12, 13)) }

private val expectedChannelExcludeGHZ5: List<Map<String, List<Int>>> =
    expectedETSICountries.map { mapOf(it to listOf(177)) } +
        listOf(
            "AU" to (120..128).toList() + listOf(177),
            "CA" to (120..128).toList() + (169..177).toList(),
            "UK" to (120..128).toList() + 177,
            "RU" to (96..128).toList() + (173..177).toList(),
            "JP" to (96..128).toList() + (149..177).toList(),
            "IN" to listOf(177),
            "SG" to (169..177).toList(),
            "CN" to (96..144).toList() + (169..177).toList(),
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

private val expectedChannelExcludeGHZ6: List<Map<String, List<Int>>> =
    (expectedETSICountries + expectedGHZ6Countries)
        .map { mapOf(it to (97..223).toList()) }

private val expectedRatingChannelsGHZ2: RatingChannels = { wiFiBand, countryCode ->
    val excludedChannels = expectedChannelExcludeGHZ2.flatMap { it[countryCode] ?: emptyList() }
    wiFiBand.wiFiChannels.availableChannels.filterNot { it in excludedChannels }
}

private val expectedRatingChannelsGHZ5: RatingChannels = { wiFiBand, countryCode ->
    val excludedChannels = expectedChannelExcludeGHZ5.flatMap { it[countryCode] ?: emptyList() }
    wiFiBand.wiFiChannels.availableChannels.filterNot { it in excludedChannels }
}

private val expectedRatingChannelsGHZ6: RatingChannels = { wiFiBand, countryCode ->
    val excludedChannels = expectedChannelExcludeGHZ6.flatMap { it[countryCode] ?: emptyList() }
    wiFiBand.wiFiChannels.availableChannels.filterNot { it in excludedChannels }
}

data class ExpectedWiFiInfo(
    val expectedChannels: List<WiFiChannel>,
    val expectedChannelsCount: Int,
    val expectedActiveChannels: Map<WiFiWidth, List<Int>>,
    val expectedGraphChannels: Map<Int, String>,
    val expectedAvailableChannels: List<Int>,
    val expectedRatingChannels: RatingChannels
) {
    fun availableChannels(wiFiWidth: WiFiWidth, wiFiBand: WiFiBand, countryCode: String): List<Int> =
        expectedActiveChannels[wiFiWidth].orEmpty().filter { it in expectedRatingChannels(wiFiBand, countryCode) }
}

val expectedWiFiInfoGHZ2 = ExpectedWiFiInfo(
    (-1..15).map { WiFiChannel(it, 2407 + it * FREQUENCY_SPREAD) },
    17,
    mapOf(
        WiFiWidth.MHZ_20 to (1..13).toList(),
        WiFiWidth.MHZ_40 to listOf(3, 7, 11),
    ),
    (1..13).associateWith { "$it" },
    (1..13).toList(),
    expectedRatingChannelsGHZ2
)

val expectedWiFiInfoGHZ5 = ExpectedWiFiInfo(
    (30..184).map { WiFiChannel(it, 5150 + (it - 30) * FREQUENCY_SPREAD) },
    77,
    mapOf(
        (WiFiWidth.MHZ_20 to ((32..144 step WiFiWidth.MHZ_20.step) + (149..177 step WiFiWidth.MHZ_20.step))),
        (WiFiWidth.MHZ_40 to ((38..142 step WiFiWidth.MHZ_40.step) + (151..175 step WiFiWidth.MHZ_40.step))),
        (WiFiWidth.MHZ_80 to ((42..138 step WiFiWidth.MHZ_80.step) + (155..171 step WiFiWidth.MHZ_80.step))),
        (WiFiWidth.MHZ_160 to ((50..114 step WiFiWidth.MHZ_160.step) + 163))
    ),
    listOf(34, 50, 66, 82, 98, 113, 129, 147, 163, 178).associateWith {
        when (it) {
            113 -> "114"
            129 -> "130"
            178 -> "179"
            else -> "$it"
        }
    },
    ((32..144 step WiFiWidth.MHZ_20.step) + (149..177 step WiFiWidth.MHZ_20.step) +
        (38..142 step WiFiWidth.MHZ_40.step) + (151..175 step WiFiWidth.MHZ_40.step) +
        (42..138 step WiFiWidth.MHZ_80.step) + (155..171 step WiFiWidth.MHZ_80.step) +
        (50..114 step WiFiWidth.MHZ_160.step) + 163)
        .toSortedSet()
        .toList(),
    expectedRatingChannelsGHZ5
)

val expectedWiFiInfoGHZ6 = ExpectedWiFiInfo(
    (-5..235).map { WiFiChannel(it, 5950 + it * FREQUENCY_SPREAD) },
    120,
    mapOf(
        (WiFiWidth.MHZ_20 to (1..233 step WiFiWidth.MHZ_20.step).toList()),
        (WiFiWidth.MHZ_40 to (3..227 step WiFiWidth.MHZ_40.step).toList()),
        (WiFiWidth.MHZ_80 to (7..215 step WiFiWidth.MHZ_80.step).toList()),
        (WiFiWidth.MHZ_160 to (15..207 step WiFiWidth.MHZ_160.step).toList()),
        (WiFiWidth.MHZ_320 to (31..191 step WiFiWidth.MHZ_320.step).toList())
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
    ((1..233 step WiFiWidth.MHZ_20.step) +
        (3..227 step WiFiWidth.MHZ_40.step) +
        (7..215 step WiFiWidth.MHZ_80.step) +
        (15..207 step WiFiWidth.MHZ_160.step) +
        (31..191 step WiFiWidth.MHZ_320.step))
        .toSortedSet()
        .toList(),
    expectedRatingChannelsGHZ6
)

