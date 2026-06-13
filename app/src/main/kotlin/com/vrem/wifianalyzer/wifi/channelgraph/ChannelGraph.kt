/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2026 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import com.patrykandpatrick.vico.views.cartesian.CartesianChartView
import com.patrykandpatrick.vico.views.cartesian.CartesianDrawingContext
import com.patrykandpatrick.vico.views.cartesian.ScrollHandler
import com.patrykandpatrick.vico.views.cartesian.Zoom
import com.patrykandpatrick.vico.views.cartesian.data.CartesianLayerRangeProvider
import com.vrem.annotation.OpenClass
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.settings.Settings
import com.vrem.wifianalyzer.settings.ThemeStyle
import com.vrem.wifianalyzer.wifi.band.FREQUENCY_SPREAD
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.graphutils.DataPoint
import com.vrem.wifianalyzer.wifi.graphutils.GraphBuilder
import com.vrem.wifianalyzer.wifi.graphutils.GraphNotifier
import com.vrem.wifianalyzer.wifi.graphutils.GraphViewport
import com.vrem.wifianalyzer.wifi.graphutils.GraphWrapper
import com.vrem.wifianalyzer.wifi.graphutils.LabelPosition
import com.vrem.wifianalyzer.wifi.graphutils.MAX_Y_DEFAULT
import com.vrem.wifianalyzer.wifi.graphutils.MIN_VISIBLE_FREQUENCY_RANGE
import com.vrem.wifianalyzer.wifi.graphutils.MIN_Y
import com.vrem.wifianalyzer.wifi.graphutils.SeriesData
import com.vrem.wifianalyzer.wifi.graphutils.SeriesLabel
import com.vrem.wifianalyzer.wifi.graphutils.canvasY
import com.vrem.wifianalyzer.wifi.model.WiFiData
import com.vrem.wifianalyzer.wifi.predicate.Predicate
import com.vrem.wifianalyzer.wifi.predicate.makeOtherPredicate

internal fun calculateLabelPosition(
    context: CartesianDrawingContext,
    seriesData: SeriesData,
): LabelPosition? {
    if (seriesData.dataPoints.isEmpty() || seriesData.title.isEmpty()) return null
    val point = seriesData.dataPoints[seriesData.dataPoints.size / 2]
    if (point.y <= MIN_Y || context.ranges.xStep == 0.0) return null
    return with(context) {
        val boundsStart = if (isLtr) layerBounds.left else layerBounds.right
        val drawingStart = boundsStart + layoutDirectionMultiplier * layerDimensions.startPadding - scroll
        val canvasX =
            drawingStart +
                layoutDirectionMultiplier * layerDimensions.xSpacing *
                ((point.x - ranges.minX) / ranges.xStep).toFloat()
        LabelPosition(canvasX, canvasY(point))
    }
}

internal fun makeGraph(
    mainContext: MainContext,
    graphMaximumY: Int,
    themeStyle: ThemeStyle,
    wiFiBand: WiFiBand,
    scalable: Boolean,
): CartesianChartView {
    val resources = mainContext.resources
    return GraphBuilder(graphMaximumY, themeStyle, FREQUENCY_SPREAD.toDouble())
        .setXAxisFormatter(channelXAxisFormatter(wiFiBand))
        .setItemPlacer(channelItemPlacer(wiFiBand))
        .setVerticalTitle(resources.getString(R.string.graph_axis_y))
        .setHorizontalTitle(resources.getString(R.string.graph_channel_axis_x))
        .build(mainContext.context, scalable)
}

internal fun makeGraphWrapper(wiFiBand: WiFiBand): GraphWrapper {
    val mainContext = MainContext.INSTANCE
    val configuration = mainContext.configuration
    val settings = mainContext.settings
    val graphMaximumY = settings.graphMaximumY()
    val themeStyle = settings.themeStyle()
    val scalable = !wiFiBand.ghz2
    val chartView = makeGraph(mainContext, graphMaximumY, themeStyle, wiFiBand, scalable)
    val seriesLabel = SeriesLabel(::calculateLabelPosition)
    val wiFiChannels = wiFiBand.wiFiChannels.wiFiChannels()
    val minX = wiFiChannels.first().frequency
    val maxX = wiFiChannels.last().frequency
    val rangeProvider =
        CartesianLayerRangeProvider.fixed(
            minX = minX.toDouble(),
            maxX = maxX.toDouble(),
            minY = MIN_Y.toDouble(),
            maxY = MAX_Y_DEFAULT.toDouble(),
        )
    val graphViewport =
        GraphViewport(
            rangeProvider = rangeProvider,
            scrollHandler = ScrollHandler(scalable),
            placeholderDataPoints = listOf(DataPoint(minX, MIN_Y), DataPoint(maxX, MIN_Y)),
            zoomEnabled = scalable,
            maxZoom = Zoom.x(MIN_VISIBLE_FREQUENCY_RANGE),
            scalable = scalable,
        )
    val graphWrapper = GraphWrapper(graphViewport, chartView, seriesLabel)
    configuration.size = graphWrapper.size(graphWrapper.calculateGraphType())
    return graphWrapper
}

@OpenClass
internal class ChannelGraph(
    private val wiFiBand: WiFiBand,
    private var dataManager: DataManager = DataManager(),
    private var graphWrapper: GraphWrapper = makeGraphWrapper(wiFiBand),
) : GraphNotifier {
    private var wasSelected: Boolean = false

    override fun update(wiFiData: WiFiData) {
        if (!selected()) {
            wasSelected = false
            graphWrapper.gone()
            return
        }
        graphWrapper.show()
        if (!wasSelected) {
            graphWrapper.reset()
        }
        wasSelected = true
        val mainContext = MainContext.INSTANCE
        val settings = mainContext.settings
        val levelMax = settings.graphMaximumY()
        val predicate = predicate(settings)
        val wiFiDetails = wiFiData.wiFiDetails(predicate, settings.sortBy())
        val newSeries = dataManager.newSeries(wiFiDetails)
        dataManager.addSeriesData(graphWrapper, newSeries, levelMax)
        graphWrapper.removeSeries(newSeries)
    }

    fun selected(): Boolean = wiFiBand == MainContext.INSTANCE.settings.wiFiBand()

    fun predicate(settings: Settings): Predicate = makeOtherPredicate(settings)

    override fun graph(): View = graphWrapper.chartView

    override fun destroy() = graphWrapper.destroy()
}
