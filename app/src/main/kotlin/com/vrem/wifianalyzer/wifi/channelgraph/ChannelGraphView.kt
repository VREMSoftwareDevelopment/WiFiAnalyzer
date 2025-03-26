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

import android.view.View
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.TitleLineGraphSeries
import com.vrem.annotation.OpenClass
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.settings.Settings
import com.vrem.wifianalyzer.settings.ThemeStyle
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.graphutils.*
import com.vrem.wifianalyzer.wifi.model.WiFiData
import com.vrem.wifianalyzer.wifi.predicate.Predicate
import com.vrem.wifianalyzer.wifi.predicate.makeOtherPredicate

internal fun makeGraphView(
    mainContext: MainContext,
    graphMaximumY: Int,
    themeStyle: ThemeStyle,
    wiFiBand: WiFiBand
): GraphView {
    val resources = mainContext.resources

    return GraphViewBuilder(wiFiBand.wiFiChannels.graphChannelCount(), graphMaximumY, themeStyle, true)
        .setLabelFormatter(ChannelAxisLabel(wiFiBand))
        .setVerticalTitle(resources.getString(R.string.graph_axis_y))
        .setHorizontalTitle(resources.getString(R.string.graph_channel_axis_x))
        .build(mainContext.context)
}

internal fun makeDefaultSeries(frequencyStart: Int, frequencyEnd: Int): TitleLineGraphSeries<GraphDataPoint> {
    val dataPoints = arrayOf(
        GraphDataPoint(frequencyStart, MIN_Y),
        GraphDataPoint(frequencyEnd, MIN_Y)
    )
    val series = TitleLineGraphSeries(dataPoints)
    series.color = transparent.primary.toInt()
    series.thickness = THICKNESS_INVISIBLE
    return series
}

internal fun makeGraphViewWrapper(wiFiBand: WiFiBand): GraphViewWrapper {
    val settings = MainContext.INSTANCE.settings
    val configuration = MainContext.INSTANCE.configuration
    val themeStyle = settings.themeStyle()
    val graphMaximumY = settings.graphMaximumY()
    val graphView = makeGraphView(MainContext.INSTANCE, graphMaximumY, themeStyle, wiFiBand)
    val graphViewWrapper = GraphViewWrapper(graphView, settings.channelGraphLegend(), themeStyle)
    configuration.size = graphViewWrapper.size(graphViewWrapper.calculateGraphType())
    val wiFiChannels = wiFiBand.wiFiChannels.wiFiChannels()
    val minX = wiFiChannels.first().frequency
    val maxX = wiFiChannels.last().frequency
    graphViewWrapper.setViewport(minX, maxX)
    graphViewWrapper.addSeries(makeDefaultSeries(minX, maxX))
    return graphViewWrapper
}

@OpenClass
internal class ChannelGraphView(
    private val wiFiBand: WiFiBand,
    private var dataManager: DataManager = DataManager(),
    private var graphViewWrapper: GraphViewWrapper = makeGraphViewWrapper(wiFiBand)
) : GraphViewNotifier {

    override fun update(wiFiData: WiFiData) {
        val predicate = predicate(MainContext.INSTANCE.settings)
        val wiFiDetails = wiFiData.wiFiDetails(predicate, MainContext.INSTANCE.settings.sortBy())
        val newSeries = dataManager.newSeries(wiFiDetails)
        dataManager.addSeriesData(graphViewWrapper, newSeries, MainContext.INSTANCE.settings.graphMaximumY())
        graphViewWrapper.removeSeries(newSeries)
        graphViewWrapper.updateLegend(MainContext.INSTANCE.settings.channelGraphLegend())
        graphViewWrapper.visibility(if (selected()) View.VISIBLE else View.GONE)
    }

    fun selected(): Boolean = wiFiBand == MainContext.INSTANCE.settings.wiFiBand()

    fun predicate(settings: Settings): Predicate = makeOtherPredicate(settings)

    override fun graphView(): GraphView {
        return graphViewWrapper.graphView
    }

}