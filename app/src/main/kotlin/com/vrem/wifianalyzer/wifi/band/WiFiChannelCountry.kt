/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2023 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import java.util.Locale
import java.util.SortedSet

class WiFiChannelCountry(private val country: Locale) {
    private val unknown = "-Unknown"
    private val wiFiChannel2GHz = WiFiChannelCountry2GHz()
    private val wiFiChannel5GHz = WiFiChannelCountry5GHz()
    private val wiFiChannel6GHz = WiFiChannelCountry6GHz()

    fun countryCode(): String = country.country

    fun countryName(currentLocale: Locale): String {
        val countryName: String = country.getDisplayCountry(currentLocale)
        return if (country.country == countryName) countryName + unknown else countryName
    }

    fun channels2GHz(): SortedSet<Int> = wiFiChannel2GHz.findChannels(country.country)

    fun channels5GHz(): SortedSet<Int> = wiFiChannel5GHz.findChannels(country.country)

    fun channels6GHz(): SortedSet<Int> = wiFiChannel6GHz.findChannels()

    fun channelAvailable2GHz(channel: Int): Boolean = channels2GHz().contains(channel)

    fun channelAvailable5GHz(channel: Int): Boolean = channels5GHz().contains(channel)

    fun channelAvailable6GHz(channel: Int): Boolean = channels6GHz().contains(channel)

    companion object {
        fun find(countryCode: String): WiFiChannelCountry = WiFiChannelCountry(findByCountryCode(countryCode))

        fun findAll(): List<WiFiChannelCountry> = allCountries().map { WiFiChannelCountry(it) }
    }

}
