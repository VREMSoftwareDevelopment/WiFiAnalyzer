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

class WiFiChannelCountry(val locale: Locale) {
    private val unknown = "-Unknown"

    val countryCode: String get() = locale.country.toCapitalize(locale)

    fun countryName(currentLocale: Locale): String {
        val countryName: String = locale.getDisplayCountry(currentLocale)
        return if (locale.country == countryName) countryName + unknown else countryName
    }

    fun channels(wiFiBand: WiFiBand): List<Int> = wiFiBand.wiFiChannels.ratingChannels(wiFiBand, countryCode)

    companion object {
        fun find(countryCode: String): WiFiChannelCountry = WiFiChannelCountry(findByCountryCode(countryCode))

        fun findAll(): List<WiFiChannelCountry> = allCountries().map { WiFiChannelCountry(it) }
    }

}
