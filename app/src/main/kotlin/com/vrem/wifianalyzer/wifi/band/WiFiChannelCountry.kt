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

import com.vrem.util.allCountries
import com.vrem.util.findByCountryCode
import java.util.*

class WiFiChannelCountry(private val country: Locale) {
    private val unknown = "-Unknown"
    private val wiFiChannelGHZ2 = WiFiChannelCountryGHZ2()
    private val wiFiChannelGHZ5 = WiFiChannelCountryGHZ5()

    fun countryCode(): String = country.country

    fun countryName(currentLocale: Locale): String {
        val countryName: String = country.getDisplayCountry(currentLocale)
        return if (country.country == countryName) countryName + unknown else countryName
    }

    fun channelsGHZ2(): SortedSet<Int> = wiFiChannelGHZ2.findChannels(country.country)

    fun channelsGHZ5(): SortedSet<Int> = wiFiChannelGHZ5.findChannels(country.country)

    fun channelAvailableGHZ2(channel: Int): Boolean = channelsGHZ2().contains(channel)

    fun channelAvailableGHZ5(channel: Int): Boolean = channelsGHZ5().contains(channel)

    companion object {
        @JvmStatic
        fun find(countryCode: String): WiFiChannelCountry = WiFiChannelCountry(findByCountryCode(countryCode))

        @JvmStatic
        fun findAll(): List<WiFiChannelCountry> = allCountries().map { WiFiChannelCountry(it) }.toList()
    }

}