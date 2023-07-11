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
package com.vrem.wifianalyzer.wifi.channelavailable

import android.view.View
import android.widget.TextView
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.databinding.ChannelAvailableDetailsBinding

internal class ChannelAvailableAdapterBinding {
    val root: View
    val channelAvailableCountry: TextView
    val channelAvailableTitleGhz2: TextView
    val channelAvailableGhz2: TextView
    val channelAvailableTitle5GHz: TextView
    val channelAvailable5GHz: TextView
    val channelAvailableTitle6GHz: TextView
    val channelAvailable6GHz: TextView

    internal constructor(binding: ChannelAvailableDetailsBinding) {
        root = binding.root
        channelAvailableCountry = binding.channelAvailableCountry
        channelAvailableTitleGhz2 = binding.channelAvailableTitleGhz2
        channelAvailableGhz2 = binding.channelAvailableGhz2
        channelAvailableTitle5GHz = binding.channelAvailableTitle5GHz
        channelAvailable5GHz = binding.channelAvailable5GHz
        channelAvailableTitle6GHz = binding.channelAvailableTitle6GHz
        channelAvailable6GHz = binding.channelAvailable6GHz
    }

    internal constructor(view: View) {
        root = view
        channelAvailableCountry = view.findViewById(R.id.channel_available_country)
        channelAvailableTitleGhz2 = view.findViewById(R.id.channel_available_title2_g_hz)
        channelAvailableGhz2 = view.findViewById(R.id.channel_available2_g_hz)
        channelAvailableTitle5GHz = view.findViewById(R.id.channel_available_title5_g_hz)
        channelAvailable5GHz = view.findViewById(R.id.channel_available5_g_hz)
        channelAvailableTitle6GHz = view.findViewById(R.id.channel_available_title6_g_hz)
        channelAvailable6GHz = view.findViewById(R.id.channel_available6_g_hz)
    }

}
