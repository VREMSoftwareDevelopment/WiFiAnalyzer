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

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.databinding.ChannelAvailableDetailsBinding
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.band.WiFiChannelCountry

internal class ChannelAvailableAdapter(context: Context, wiFiChannelCountries: List<WiFiChannelCountry>) :
    ArrayAdapter<WiFiChannelCountry>(context, R.layout.channel_available_details, wiFiChannelCountries) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val binding = view?.let { ChannelAvailableAdapterBinding(it) } ?: ChannelAvailableAdapterBinding(create(parent))
        val rootView = binding.root
        getItem(position)?.let {
            val resources = rootView.resources
            val currentLocale = MainContext.INSTANCE.settings.languageLocale()
            binding.channelAvailableCountry.text = "${it.countryCode()} - ${it.countryName(currentLocale)}"
            binding.channelAvailableTitleGhz2.text = "${resources.getString(WiFiBand.GHZ2.textResource)} : "
            binding.channelAvailableGhz2.text = it.channelsGHZ2().joinToString(",")
            binding.channelAvailableTitleGhz5.text = "${resources.getString(WiFiBand.GHZ5.textResource)} : "
            binding.channelAvailableGhz5.text = it.channelsGHZ5().joinToString(",")
            binding.channelAvailableTitleGhz6.text = "${resources.getString(WiFiBand.GHZ6.textResource)} : "
            binding.channelAvailableGhz6.text = it.channelsGHZ6().joinToString(",")
        }
        return rootView
    }

    private fun create(parent: ViewGroup): ChannelAvailableDetailsBinding =
        ChannelAvailableDetailsBinding.inflate(MainContext.INSTANCE.layoutInflater, parent, false)

}