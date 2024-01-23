/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2024 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import android.view.View
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.TitleLineGraphSeries
import com.vrem.annotation.OpenClass
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.settings.Settings
import com.vrem.wifianalyzer.settings.ThemeStyle
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.band.WiFiChannelPair
import com.vrem.wifianalyzer.wifi.band.WiFiChannels
import com.vrem.wifianalyzer.wifi.graphutils.*
import com.vrem.wifianalyzer.wifi.model.WiFiData
import com.vrem.wifianalyzer.wifi.predicate.Predicate
import com.vrem.wifianalyzer.wifi.predicate.makeOtherPredicate

internal fun WiFiChannelPair.numX(): Int {
    val channelFirst = this.first.channel - WiFiChannels.CHANNEL_OFFSET
    val channelLast = this.second.channel + WiFiChannels.CHANNEL_OFFSET
    return channelLast - channelFirst + 1
}

internal fun WiFiChannelPair.selected(wiFiBand: WiFiBand): Boolean {
    val currentWiFiBand = MainContext.INSTANCE.settings.wiFiBand()
    val currentWiFiChannelPair = MainContext.INSTANCE.configuration.wiFiChannelPair(currentWiFiBand)
    return wiFiBand == currentWiFiBand && (WiFiBand.GHZ2 == wiFiBand || this == currentWiFiChannelPair)
}

internal fun makeGraphView(
    mainContext: MainContext,
    graphMaximumY: Int,
    themeStyle: ThemeStyle,
    wiFiBand: WiFiBand,
    wiFiChannelPair: WiFiChannelPair
): GraphView {
    val resources = mainContext.resources
    return GraphViewBuilder(wiFiChannelPair.numX(), graphMaximumY, themeStyle, true)
        .setLabelFormatter(ChannelAxisLabel(wiFiBand, wiFiChannelPair))
        .setVerticalTitle(resources.getString(R.string.graph_axis_y))
        .setHorizontalTitle(resources.getString(R.string.graph_channel_axis_x))
        .build(mainContext.context)
}

internal fun makeDefaultSeries(frequencyEnd: Int, minX: Int): TitleLineGraphSeries<GraphDataPoint> {
    val dataPoints = arrayOf(
        GraphDataPoint(minX, MIN_Y),
        GraphDataPoint(frequencyEnd + WiFiChannels.FREQUENCY_OFFSET, MIN_Y)
    )
    val series = TitleLineGraphSeries(dataPoints)
    series.color = transparent.primary.toInt()
    series.thickness = THICKNESS_INVISIBLE
    return series
}

internal fun makeGraphViewWrapper(wiFiBand: WiFiBand, wiFiChannelPair: WiFiChannelPair): GraphViewWrapper {
    val settings = MainContext.INSTANCE.settings
    val configuration = MainContext.INSTANCE.configuration
    val themeStyle = settings.themeStyle()
    val graphMaximumY = settings.graphMaximumY()
    val graphView = makeGraphView(MainContext.INSTANCE, graphMaximumY, themeStyle, wiFiBand, wiFiChannelPair)
    val graphViewWrapper = GraphViewWrapper(graphView, settings.channelGraphLegend(), themeStyle)
    configuration.size = graphViewWrapper.size(graphViewWrapper.calculateGraphType())
    val minX = wiFiChannelPair.first.frequency - WiFiChannels.FREQUENCY_OFFSET
    val maxX = minX + graphViewWrapper.viewportCntX * WiFiChannels.FREQUENCY_SPREAD
    graphViewWrapper.setViewport(minX, maxX)
    graphViewWrapper.addSeries(makeDefaultSeries(wiFiChannelPair.second.frequency, minX))
    return graphViewWrapper
}

@OpenClass
internal class ChannelGraphView(
    private val wiFiBand: WiFiBand,
    private val wiFiChannelPair: WiFiChannelPair,
    private var dataManager: DataManager = DataManager(),
    private var graphViewWrapper: GraphViewWrapper = makeGraphViewWrapper(wiFiBand, wiFiChannelPair)
) : GraphViewNotifier {

    override fun update(wiFiData: WiFiData) {
        val predicate = predicate(MainContext.INSTANCE.settings)
        val wiFiDetails = wiFiData.wiFiDetails(predicate, MainContext.INSTANCE.settings.sortBy())
        val newSeries = dataManager.newSeries(wiFiDetails, wiFiChannelPair)
        dataManager.addSeriesData(graphViewWrapper, newSeries, MainContext.INSTANCE.settings.graphMaximumY())
        graphViewWrapper.removeSeries(newSeries)
        graphViewWrapper.updateLegend(MainContext.INSTANCE.settings.channelGraphLegend())
        graphViewWrapper.visibility(if (selected()) View.VISIBLE else View.GONE)
    }

    fun selected(): Boolean = wiFiChannelPair.selected(wiFiBand)

    fun predicate(settings: Settings): Predicate = makeOtherPredicate(settings)

    override fun graphView(): GraphView {
        return graphViewWrapper.graphView
    }

}