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
package com.vrem.wifianalyzer.wifi.timegraph

import android.graphics.Paint
import android.view.View
import com.patrykandpatrick.vico.views.cartesian.AutoScrollCondition
import com.patrykandpatrick.vico.views.cartesian.CartesianChartView
import com.patrykandpatrick.vico.views.cartesian.CartesianDrawingContext
import com.patrykandpatrick.vico.views.cartesian.Scroll
import com.patrykandpatrick.vico.views.cartesian.ScrollHandler
import com.patrykandpatrick.vico.views.cartesian.Zoom
import com.patrykandpatrick.vico.views.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.views.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.views.common.data.ExtraStore
import com.vrem.annotation.OpenClass
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.settings.Settings
import com.vrem.wifianalyzer.settings.ThemeStyle
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.graphutils.DataPoint
import com.vrem.wifianalyzer.wifi.graphutils.GraphBuilder
import com.vrem.wifianalyzer.wifi.graphutils.GraphNotifier
import com.vrem.wifianalyzer.wifi.graphutils.GraphViewport
import com.vrem.wifianalyzer.wifi.graphutils.GraphWrapper
import com.vrem.wifianalyzer.wifi.graphutils.LabelPosition
import com.vrem.wifianalyzer.wifi.graphutils.MAX_SCAN_COUNT
import com.vrem.wifianalyzer.wifi.graphutils.MAX_Y_DEFAULT
import com.vrem.wifianalyzer.wifi.graphutils.MIN_Y
import com.vrem.wifianalyzer.wifi.graphutils.SeriesData
import com.vrem.wifianalyzer.wifi.graphutils.SeriesLabel
import com.vrem.wifianalyzer.wifi.graphutils.canvasY
import com.vrem.wifianalyzer.wifi.model.WiFiData
import com.vrem.wifianalyzer.wifi.predicate.Predicate
import com.vrem.wifianalyzer.wifi.predicate.makeOtherPredicate

private const val NUM_X_TIME = 21

internal class TimeLayerRangeProvider : CartesianLayerRangeProvider {
    override fun getMinX(
        minX: Double,
        maxX: Double,
        extraStore: ExtraStore,
    ): Double {
        val clamped = minX.coerceAtLeast(maxX - MAX_SCAN_COUNT)
        return clamped - (clamped % 2)
    }

    override fun getMaxX(
        minX: Double,
        maxX: Double,
        extraStore: ExtraStore,
    ) = maxX.coerceAtLeast(NUM_X_TIME.toDouble())

    override fun getMinY(
        minY: Double,
        maxY: Double,
        extraStore: ExtraStore,
    ) = MIN_Y.toDouble()

    override fun getMaxY(
        minY: Double,
        maxY: Double,
        extraStore: ExtraStore,
    ) = MAX_Y_DEFAULT.toDouble()
}

internal fun calculateLabelPosition(
    context: CartesianDrawingContext,
    seriesData: SeriesData,
): LabelPosition? {
    if (seriesData.dataPoints.isEmpty() || seriesData.title.isEmpty()) return null
    val point = seriesData.dataPoints.last()
    if (point.y <= MIN_Y) return null
    return with(context) {
        val canvasX = layerBounds.right - spToPx(2f)
        LabelPosition(canvasX, canvasY(point), Paint.Align.RIGHT)
    }
}

internal fun makeGraph(
    mainContext: MainContext,
    graphMaximumY: Int,
    themeStyle: ThemeStyle,
): CartesianChartView =
    GraphBuilder(graphMaximumY, themeStyle)
        .setItemPlacer(HorizontalAxis.ItemPlacer.aligned(spacing = { 2 }, shiftExtremeLines = false))
        .setVerticalTitle(mainContext.resources.getString(R.string.graph_axis_y))
        .setHorizontalTitle(mainContext.resources.getString(R.string.graph_time_axis_x))
        .build(mainContext.context, false)

internal fun makeGraphWrapper(): GraphWrapper {
    val mainContext = MainContext.INSTANCE
    val settings = mainContext.settings
    val configuration = mainContext.configuration
    val themeStyle = settings.themeStyle()
    val graphMaximumY = settings.graphMaximumY()
    val chartView = makeGraph(mainContext, graphMaximumY, themeStyle)
    val seriesLabel = SeriesLabel(::calculateLabelPosition)
    val scrollHandler = ScrollHandler(true, Scroll.Absolute.End, Scroll.Absolute.End, AutoScrollCondition.OnModelGrowth)
    val graphViewport =
        GraphViewport(
            rangeProvider = TimeLayerRangeProvider(),
            scrollHandler = scrollHandler,
            placeholderDataPoints = (0..NUM_X_TIME).map { DataPoint(it, MIN_Y) },
            initialZoom = Zoom.x(NUM_X_TIME.toDouble()),
        )
    val graphWrapper = GraphWrapper(graphViewport, chartView, seriesLabel)
    configuration.size = graphWrapper.size(graphWrapper.calculateGraphType())
    return graphWrapper
}

@OpenClass
internal class TimeGraph(
    private val wiFiBand: WiFiBand,
    private val dataManager: DataManager = DataManager(),
    private val graphWrapper: GraphWrapper = makeGraphWrapper(),
) : GraphNotifier {
    private var wasSelected: Boolean = false

    override fun update(wiFiData: WiFiData) {
        if (!selected()) {
            wasSelected = false
            graphWrapper.gone()
            return
        }
        if (!wasSelected) {
            dataManager.reset(graphWrapper)
        }
        wasSelected = true
        val mainContext = MainContext.INSTANCE
        val settings = mainContext.settings
        val sortBy = settings.sortBy()
        val levelMax = settings.graphMaximumY()
        val predicate = predicate(settings)
        val wiFiDetails = wiFiData.wiFiDetails(predicate, sortBy)
        val newSeries = dataManager.addSeriesData(graphWrapper, wiFiDetails, levelMax, predicate)
        graphWrapper.removeSeries(newSeries)
        graphWrapper.show()
    }

    fun predicate(settings: Settings): Predicate = makeOtherPredicate(settings)

    private fun selected(): Boolean = wiFiBand == MainContext.INSTANCE.settings.wiFiBand()

    override fun graph(): View = graphWrapper.chartView

    override fun destroy() = graphWrapper.destroy()
}
