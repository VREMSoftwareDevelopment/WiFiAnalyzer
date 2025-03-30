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

import com.vrem.util.EMPTY
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

private fun excludeGHZ6(): List<Map<String, List<Int>>> {
    val exclude = (97..223).toList()
    val additional = listOf("JP", "RU", "NZ", "AU", "GL", "AE", "GB", "MX", "SG", "HK", "MO", "PH")
    return (countriesETSI + additional).map { mapOf(it to exclude) }
}

private fun excludeGHZ5(): List<Map<String, List<Int>>> {
    val exclude1 = listOf(177)
    val exclude2 = (120..128).toList()
    val exclude3 = (169..177).toList()
    val exclude4 = (96..128).toList()
    val exclude5 = (173..177).toList()
    val exclude6 = (149..177).toList()
    val exclude7 = (96..144).toList()
    val exclude8 = listOf(144)
    val exclude9 = (165..177).toList()
    val additional = listOf(
        "AU" to exclude2 + exclude1,
        "CA" to exclude2 + exclude3,
        "UK" to exclude2 + exclude1,
        "RU" to exclude4 + exclude5,
        "JP" to exclude4 + exclude6,
        "IN" to exclude1,
        "SG" to exclude3,
        "CH" to exclude7 + exclude3,
        "IL" to exclude1,
        "KR" to exclude3,
        "TR" to exclude8 + exclude6,
        "ZA" to exclude8 + exclude6,
        "BR" to exclude3,
        "TW" to exclude3,
        "NZ" to exclude3,
        "BH" to exclude7 + exclude3,
        "VN" to exclude3,
        "ID" to exclude7 + exclude9,
        "PH" to exclude5
    )
    return countriesETSI.map { mapOf(it to exclude1) } + additional.map { mapOf(it.first to it.second) }
}


val wiFiChannelsGHZ2: WiFiChannels = WiFiChannels(
    WiFiChannelPair(WiFiChannel(-1, 2402), WiFiChannel(15, 2482)),
    1,
    mapOf(
        (WiFiWidth.MHZ_20 to listOf(1, 2, 3, 6, 7, 8, 11, 12, 13)),
        (WiFiWidth.MHZ_40 to listOf(3, 7, 11))
    ),
    (1..13).associate { it to "$it" },
    listOf(1, 2, 3, 6, 7, 8, 11, 12, 13)
)

val wiFiChannelsGHZ5: WiFiChannels = WiFiChannels(
    WiFiChannelPair(WiFiChannel(30, 5150), WiFiChannel(184, 5920)),
    2,
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
    ((42..138 step WiFiWidth.MHZ_80.step) + (155..171 step WiFiWidth.MHZ_80.step) +
            (50..114 step WiFiWidth.MHZ_160.step) + 163)
        .toSortedSet()
        .toList(),
    excludeGHZ5()
)

val wiFiChannelsGHZ6: WiFiChannels = WiFiChannels(
    WiFiChannelPair(WiFiChannel(-5, 5925), WiFiChannel(235, 7125)),
    2,
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
    ((15..207 step WiFiWidth.MHZ_160.step) + (31..191 step WiFiWidth.MHZ_320.step))
        .toSortedSet()
        .toList(),
    excludeGHZ6()
)

class WiFiChannels(
    val channelRange: WiFiChannelPair,
    val offset: Int,
    val activeChannels: Map<WiFiWidth, List<Int>>,
    val graphChannels: Map<Int, String>,
    val availableChannels: List<Int>,
    val excludeChannels: List<Map<String, List<Int>>> = listOf()
) {

    fun availableChannels(wiFiBand: WiFiBand, countryCode: String): List<WiFiChannel> =
        WiFiChannelCountry.find(countryCode).channels(wiFiBand).map { wiFiChannelByChannel(it) }

    fun inRange(frequency: Int): Boolean = frequency in channelRange.first.frequency..channelRange.second.frequency

    fun wiFiChannelByFrequency(frequency: Int): WiFiChannel =
        if (inRange(frequency)) wiFiChannel(frequency) else WiFiChannel.UNKNOWN

    fun wiFiChannelByChannel(channel: Int): WiFiChannel =
        if (channel in channelRange.first.channel..channelRange.second.channel) {
            WiFiChannel(channel, channelRange.first.frequency + (channel - channelRange.first.channel) * FREQUENCY_SPREAD)
        } else {
            WiFiChannel.UNKNOWN
        }

    fun graphChannelCount(): Int = (channelRange.second.channel - channelRange.first.channel + 1) / offset

    fun graphChannelByFrequency(frequency: Int): String =
        graphChannels[wiFiChannelByFrequency(frequency).channel] ?: String.EMPTY

    fun wiFiChannels(): List<WiFiChannel> =
        (channelRange.first.channel..channelRange.second.channel).map { wiFiChannelByChannel(it) }

    private fun wiFiChannel(frequency: Int): WiFiChannel {
        val firstChannel = (channelRange.first.channel + if (channelRange.first.channel < 0) -0.5 else 0.5).toInt()
        val channel = ((frequency - channelRange.first.frequency) / FREQUENCY_SPREAD + firstChannel).toInt()
        return WiFiChannel(channel, frequency)
    }
}