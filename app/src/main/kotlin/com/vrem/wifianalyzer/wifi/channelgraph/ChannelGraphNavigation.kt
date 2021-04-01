/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2021 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.wifi.channelgraph

import android.content.Context
import android.view.View
import android.widget.Button
import androidx.core.text.parseAsHtml
import com.vrem.annotation.OpenClass
import com.vrem.util.compatColor
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.wifi.band.WiFiChannelPair
import com.vrem.wifianalyzer.wifi.band.WiFiChannelsGHZ5

internal val navigationSet: Map<WiFiChannelPair, Int> = mapOf(
        WiFiChannelsGHZ5.SET1 to R.id.graphNavigationSet1,
        WiFiChannelsGHZ5.SET2 to R.id.graphNavigationSet2,
        WiFiChannelsGHZ5.SET3 to R.id.graphNavigationSet3)

@OpenClass
class ChannelGraphNavigation(private val view: View, private val mainContext: Context) {

    internal fun update() {
        if (MainContext.INSTANCE.settings.wiFiBand().ghz5()) {
            val selectedWiFiChannelPair = MainContext.INSTANCE.configuration.wiFiChannelPair
            navigationSet.entries.forEach { button(it, selectedWiFiChannelPair) }
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }

    internal fun initialize() {
        navigationSet.entries.forEach { entry ->
            with(view.findViewById<Button>(entry.value)) {
                setOnClickListener { onClickListener(entry.key) }
                text = """<strong>${entry.key.first.channel} &#8722 ${entry.key.second.channel}</strong>""".parseAsHtml()
            }
        }
    }

    internal fun onClickListener(wiFiChannelPair: WiFiChannelPair) {
        val mainContext = MainContext.INSTANCE
        mainContext.configuration.wiFiChannelPair = wiFiChannelPair
        mainContext.scannerService.update()
    }

    private fun button(entry: Map.Entry<WiFiChannelPair, Int>, selectedWiFiChannelPair: WiFiChannelPair) {
        with(view.findViewById<Button>(entry.value)) {
            val selected = entry.key == selectedWiFiChannelPair
            val color = if (selected) R.color.selected else R.color.background
            setBackgroundColor(mainContext.compatColor(color))
            isSelected = selected
        }
    }

}