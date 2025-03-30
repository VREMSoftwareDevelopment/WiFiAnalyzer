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
package com.vrem.wifianalyzer.wifi.channelavailable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.databinding.ChannelAvailableContentBinding
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.band.WiFiChannelCountry
import com.vrem.wifianalyzer.wifi.model.WiFiWidth

class ChannelAvailableFragment : Fragment() {
    private lateinit var binding: ChannelAvailableContentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ChannelAvailableContentBinding.inflate(inflater, container, false)
        update()
        return binding.root
    }

    private fun update() {
        val settings = MainContext.INSTANCE.settings
        val countryCode = settings.countryCode()
        val languageLocale = settings.languageLocale()
        binding.apply {
            channelsAvailableCountry.text = WiFiChannelCountry.find(countryCode).countryName(languageLocale)
            channelsAvailable2GHz20MHz.text = channels(WiFiBand.GHZ2, WiFiWidth.MHZ_20, countryCode)
            channelsAvailable2GHz40MHz.text = channels(WiFiBand.GHZ2, WiFiWidth.MHZ_40, countryCode)
            channelsAvailable5GHz20MHz.text = channels(WiFiBand.GHZ5, WiFiWidth.MHZ_20, countryCode)
            channelsAvailable5GHz40MHz.text = channels(WiFiBand.GHZ5, WiFiWidth.MHZ_40, countryCode)
            channelsAvailable5GHz80MHz.text = channels(WiFiBand.GHZ5, WiFiWidth.MHZ_80, countryCode)
            channelsAvailable5GHz160MHz.text = channels(WiFiBand.GHZ5, WiFiWidth.MHZ_160, countryCode)
            channelsAvailable6GHz20MHz.text = channels(WiFiBand.GHZ6, WiFiWidth.MHZ_20, countryCode)
            channelsAvailable6GHz40MHz.text = channels(WiFiBand.GHZ6, WiFiWidth.MHZ_40, countryCode)
            channelsAvailable6GHz80MHz.text = channels(WiFiBand.GHZ6, WiFiWidth.MHZ_80, countryCode)
            channelsAvailable6GHz160MHz.text = channels(WiFiBand.GHZ6, WiFiWidth.MHZ_160, countryCode)
            channelsAvailable6GHz320MHz.text = channels(WiFiBand.GHZ6, WiFiWidth.MHZ_320, countryCode)
        }
    }

    override fun onResume() {
        super.onResume()
        update()
    }

    private fun channels(wiFiBand: WiFiBand, wiFiWidth: WiFiWidth, countryCode: String): String {
        val wiFiChannels = wiFiBand.wiFiChannels
        return wiFiChannels.activeChannels[wiFiWidth]
            .orEmpty()
            .subtract(wiFiChannels.excludeChannels.flatMap { it[countryCode] ?: emptyList() })
            .toList()
            .joinToString(", ")
    }

}