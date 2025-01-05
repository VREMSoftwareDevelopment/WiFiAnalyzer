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
import androidx.fragment.app.ListFragment
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.databinding.ChannelAvailableContentBinding
import com.vrem.wifianalyzer.wifi.band.WiFiChannelCountry
import com.vrem.wifianalyzer.wifi.band.WiFiChannelCountry.Companion.find

class ChannelAvailableFragment : ListFragment() {
    private lateinit var channelAvailableAdapter: ChannelAvailableAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = ChannelAvailableContentBinding.inflate(inflater, container, false)
        channelAvailableAdapter = ChannelAvailableAdapter(requireActivity(), channelAvailable())
        listAdapter = channelAvailableAdapter
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        channelAvailableAdapter.clear()
        channelAvailableAdapter.addAll(channelAvailable())
    }

    private fun channelAvailable(): MutableList<WiFiChannelCountry> =
        mutableListOf(find(MainContext.INSTANCE.settings.countryCode()))

}