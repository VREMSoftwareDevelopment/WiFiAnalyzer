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

import com.vrem.util.allCountries
import com.vrem.util.findByCountryCode
import com.vrem.util.toCapitalize
import java.util.Locale
import java.util.SortedSet

typealias Rules = Pair<Set<String>, Set<Int>>

private val countriesETSI: Set<String> = setOf(
    "AT",      // ETSI Austria
    "BE",      // ETSI Belgium
    "CH",      // ETSI Switzerland
    "CY",      // ETSI Cyprus
    "CZ",      // ETSI Czechia
    "DE",      // ETSI Germany
    "DK",      // ETSI Denmark
    "EE",      // ETSI Estonia
    "ES",      // ETSI Spain
    "FI",      // ETSI Finland
    "FR",      // ETSI France
    "GR",      // ETSI Greece
    "HU",      // ETSI Hungary
    "IE",      // ETSI Ireland
    "IS",      // ETSI Iceland
    "IT",      // ETSI Italy
    "LI",      // ETSI Liechtenstein
    "LT",      // ETSI Lithuania
    "LU",      // ETSI Luxembourg
    "LV",      // ETSI Latvia
    "MT",      // ETSI Malta
    "NL",      // ETSI Netherlands
    "NO",      // ETSI Norway
    "PL",      // ETSI Poland
    "PT",      // ETSI Portugal
    "RO",      // ETSI Romania
    "SE",      // ETSI Sweden
    "SI",      // ETSI Slovenia
    "SK",      // ETSI Slovakia
    "IL"       // ETSI Israel
)

// 2.4 GHz
private val channelsGHZ2 = (1..13).toSortedSet()

// 5 GHz
// 80/160 MHz center channels
private val channelsGHZ5 = sortedSetOf(42, 50, 58, 74, 82, 90, 106, 114, 122, 138, 155, 163, 171)

// exclusion rules
private val excludeGHZ5: List<Rules> = listOf(
    Rules(setOf("JP", "TR", "ZA"), sortedSetOf(155, 163, 171)),
    Rules(setOf("CN", "BH", "ID"), sortedSetOf(106, 114, 122, 138, 171)),
    Rules(setOf("RU"), sortedSetOf(106, 114, 122))
)

// 6 GHz
// 160/320 MHz center channels
private val channelsGHZ6 = sortedSetOf(15, 31, 47, 63, 79, 95, 111, 127, 143, 159, 175, 191, 207)

// exclusion rules
private val excludeGHZ6: List<Rules> = listOf(
    Rules(
        countriesETSI.union(setOf("JP", "RU", "NZ", "AU", "GL", "AE", "GB", "MX", "SG", "HK", "MO", "PH")),
        sortedSetOf(95, 111, 127, 143, 159, 175, 191, 207)
    )
)

private class WiFiChannelsCountry(val channels: SortedSet<Int>, val exclude: List<Rules> = listOf()) {
    fun findChannels(locale: Locale): SortedSet<Int> {
        val countryCode = locale.country.toCapitalize(Locale.getDefault())
        val excludedChannels = exclude.filter { countryCode in it.first }.flatMap { it.second }
        return channels.subtract(excludedChannels).toSortedSet()
    }

    fun available(channel: Int): Boolean = channels.contains(channel)
}

class WiFiChannelCountry(private val locale: Locale) {
    private val unknown = "-Unknown"
    private val wiFiBandChannelsCountry = mapOf(
        WiFiBand.GHZ2 to WiFiChannelsCountry(channelsGHZ2, excludeGHZ6),
        WiFiBand.GHZ5 to WiFiChannelsCountry(channelsGHZ5, excludeGHZ5),
        WiFiBand.GHZ6 to WiFiChannelsCountry(channelsGHZ6, excludeGHZ6),
    )

    fun countryCode(): String = locale.country

    fun countryName(currentLocale: Locale): String {
        val countryName: String = locale.getDisplayCountry(currentLocale)
        return if (locale.country == countryName) countryName + unknown else countryName
    }

    fun channels(wiFiBand: WiFiBand): SortedSet<Int> =
        wiFiBandChannelsCountry[wiFiBand]!!.findChannels(locale)

    fun available(wiFiBand: WiFiBand, channel: Int): Boolean =
        wiFiBandChannelsCountry[wiFiBand]!!.available(channel)

    companion object {
        fun find(countryCode: String): WiFiChannelCountry = WiFiChannelCountry(findByCountryCode(countryCode))

        fun findAll(): List<WiFiChannelCountry> = allCountries().map { WiFiChannelCountry(it) }
    }

}
