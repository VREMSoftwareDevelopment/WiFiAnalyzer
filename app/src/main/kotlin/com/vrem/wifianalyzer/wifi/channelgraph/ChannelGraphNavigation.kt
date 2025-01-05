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
package com.vrem.wifianalyzer.wifi.channelgraph

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.text.parseAsHtml
import com.vrem.annotation.OpenClass
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.band.WiFiChannelPair
import com.vrem.wifianalyzer.wifi.band.WiFiChannelsGHZ5
import com.vrem.wifianalyzer.wifi.band.WiFiChannelsGHZ6

typealias NavigationSets = Map<Int, WiFiChannelPair>
typealias NavigationLines = Map<Int, NavigationSets>

internal val navigationGHZ2Lines = mapOf<Int, NavigationSets>()

internal val navigationGHZ5Lines = mapOf(
    R.id.graphNavigationLine1 to mapOf(
        R.id.graphNavigationSet1 to WiFiChannelsGHZ5.SET1,
        R.id.graphNavigationSet2 to WiFiChannelsGHZ5.SET2,
        R.id.graphNavigationSet3 to WiFiChannelsGHZ5.SET3
    ),
    R.id.graphNavigationLine2 to emptyMap()
)

internal val navigationGHZ6Lines = mapOf(
    R.id.graphNavigationLine1 to mapOf(
        R.id.graphNavigationSet1 to WiFiChannelsGHZ6.SET1,
        R.id.graphNavigationSet2 to WiFiChannelsGHZ6.SET2,
        R.id.graphNavigationSet3 to WiFiChannelsGHZ6.SET3
    ),
    R.id.graphNavigationLine2 to mapOf(
        R.id.graphNavigationSet4 to WiFiChannelsGHZ6.SET4,
        R.id.graphNavigationSet5 to WiFiChannelsGHZ6.SET5,
        R.id.graphNavigationSet6 to WiFiChannelsGHZ6.SET6,
        R.id.graphNavigationSet7 to WiFiChannelsGHZ6.SET7
    )
)

@OpenClass
class ChannelGraphNavigation(private val view: View, private val mainContext: Context) {

    internal fun update() {
        val wiFiBand = MainContext.INSTANCE.settings.wiFiBand()
        val selectedWiFiChannelPair = MainContext.INSTANCE.configuration.wiFiChannelPair(wiFiBand)
        val navigationLines = navigationLines(wiFiBand)
        view.visibility = visibility(navigationLines)
        navigationLines.entries.forEach { entry ->
            view.findViewById<LinearLayout>(entry.key).visibility = visibility(entry.value)
            buttons(entry.value, wiFiBand, selectedWiFiChannelPair)
        }
    }

    private fun buttons(navigationSets: NavigationSets, wiFiBand: WiFiBand, selectedWiFiChannelPair: WiFiChannelPair) {
        navigationSets.forEach { entry ->
            with(view.findViewById<Button>(entry.key)) {
                val value = entry.value
                val selected = value == selectedWiFiChannelPair
                val color = ContextCompat.getColor(mainContext, if (selected) R.color.selected else R.color.background)
                val textValue =
                    """<strong>${value.first.channel} &#8722 ${value.second.channel}</strong>""".parseAsHtml()
                        .toString()
                setBackgroundColor(color)
                text = textValue
                isSelected = selected
                setOnClickListener { onClickListener(wiFiBand, value) }
            }
        }
    }

    private fun visibility(map: Map<Int, Any>) =
        if (map.isEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }

    internal fun onClickListener(wiFiBand: WiFiBand, wiFiChannelPair: WiFiChannelPair) {
        val mainContext = MainContext.INSTANCE
        mainContext.configuration.wiFiChannelPair(wiFiBand, wiFiChannelPair)
        mainContext.scannerService.update()
    }

    private fun navigationLines(wiFiBand: WiFiBand): NavigationLines =
        when (wiFiBand) {
            WiFiBand.GHZ2 -> navigationGHZ2Lines
            WiFiBand.GHZ5 -> navigationGHZ5Lines
            WiFiBand.GHZ6 -> navigationGHZ6Lines
        }

}