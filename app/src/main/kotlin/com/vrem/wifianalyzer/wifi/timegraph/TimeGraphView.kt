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
package com.vrem.wifianalyzer.wifi.timegraph

import android.view.View
import com.jjoe64.graphview.GraphView
import com.vrem.annotation.OpenClass
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.settings.Settings
import com.vrem.wifianalyzer.settings.ThemeStyle
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.graphutils.GraphViewBuilder
import com.vrem.wifianalyzer.wifi.graphutils.GraphViewNotifier
import com.vrem.wifianalyzer.wifi.graphutils.GraphViewWrapper
import com.vrem.wifianalyzer.wifi.model.WiFiData
import com.vrem.wifianalyzer.wifi.predicate.Predicate
import com.vrem.wifianalyzer.wifi.predicate.makeOtherPredicate

private const val NUM_X_TIME = 21

internal fun makeGraphView(mainContext: MainContext, graphMaximumY: Int, themeStyle: ThemeStyle): GraphView =
    GraphViewBuilder(NUM_X_TIME, graphMaximumY, themeStyle, false)
        .setLabelFormatter(TimeAxisLabel())
        .setVerticalTitle(mainContext.resources.getString(R.string.graph_axis_y))
        .setHorizontalTitle(mainContext.resources.getString(R.string.graph_time_axis_x))
        .build(mainContext.context)

internal fun makeGraphViewWrapper(): GraphViewWrapper {
    val settings = MainContext.INSTANCE.settings
    val themeStyle = settings.themeStyle()
    val configuration = MainContext.INSTANCE.configuration
    val graphView = makeGraphView(MainContext.INSTANCE, settings.graphMaximumY(), themeStyle)
    val graphViewWrapper = GraphViewWrapper(graphView, settings.timeGraphLegend(), themeStyle)
    configuration.size = graphViewWrapper.size(graphViewWrapper.calculateGraphType())
    graphViewWrapper.setViewport()
    return graphViewWrapper
}

@OpenClass
internal class TimeGraphView(
    private val wiFiBand: WiFiBand,
    private val dataManager: DataManager = DataManager(),
    private val graphViewWrapper: GraphViewWrapper = makeGraphViewWrapper()
) : GraphViewNotifier {

    override fun update(wiFiData: WiFiData) {
        val predicate = predicate(MainContext.INSTANCE.settings)
        val wiFiDetails = wiFiData.wiFiDetails(predicate, MainContext.INSTANCE.settings.sortBy())
        val newSeries = dataManager.addSeriesData(graphViewWrapper, wiFiDetails, MainContext.INSTANCE.settings.graphMaximumY())
        graphViewWrapper.removeSeries(newSeries)
        graphViewWrapper.updateLegend(MainContext.INSTANCE.settings.timeGraphLegend())
        graphViewWrapper.visibility(if (selected()) View.VISIBLE else View.GONE)
    }

    fun predicate(settings: Settings): Predicate = makeOtherPredicate(settings)

    private fun selected(): Boolean {
        return wiFiBand == MainContext.INSTANCE.settings.wiFiBand()
    }

    override fun graphView(): GraphView {
        return graphViewWrapper.graphView
    }

}