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
package com.vrem.wifianalyzer.wifi.graphutils

import android.view.View
import com.patrykandpatrick.vico.views.cartesian.CartesianChartView
import com.patrykandpatrick.vico.views.cartesian.ScrollHandler
import com.patrykandpatrick.vico.views.cartesian.Zoom
import com.patrykandpatrick.vico.views.cartesian.ZoomHandler
import com.patrykandpatrick.vico.views.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.views.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.views.cartesian.data.lineModel
import com.vrem.annotation.OpenClass
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.SIZE_MAX
import com.vrem.wifianalyzer.SIZE_MIN
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.security.MessageDigest

data class GraphViewport(
    val rangeProvider: CartesianLayerRangeProvider,
    val scrollHandler: ScrollHandler,
    val placeholderDataPoints: List<DataPoint>,
    val initialZoom: Zoom = Zoom.Content,
    val zoomEnabled: Boolean = false,
    val minZoom: Zoom = Zoom.Content,
    val maxZoom: Zoom = Zoom.max(Zoom.fixed(ZOOM_MAX), Zoom.Content),
    val scalable: Boolean = false,
)

private const val ZOOM_MAX: Float = 10f

@OpenClass
class GraphWrapper(
    val graphViewport: GraphViewport,
    val chartView: CartesianChartView,
    val seriesLabel: SeriesLabel,
    private val seriesCache: SeriesCache = SeriesCache(graphViewport.placeholderDataPoints),
    private val graphColors: GraphColors = GraphColors(),
    private val chartUpdater: ChartUpdater = ChartUpdater(chartView, seriesLabel, seriesCache),
) {
    internal val modelProducer: CartesianChartModelProducer = CartesianChartModelProducer()
    internal val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    init {
        chartView.modelProducer = modelProducer
        chartView.scrollHandler = graphViewport.scrollHandler
        chartView.zoomHandler = buildZoomHandler()
    }

    fun removeSeries(newSeries: Set<WiFiDetail>) {
        val removed = seriesCache.remove(differenceSeries(newSeries))
        if (removed.isNotEmpty()) {
            chartUpdater.resetStyles()
            chartUpdater.syncPointMap(seriesCache.populatedEntries())
        }
        removed.filterNot { it.connected }.forEach {
            graphColors.addColor(it.graphColor.primary)
        }
    }

    fun reset() {
        removeSeries(emptySet())
        flushData()
        if (graphViewport.scalable) {
            chartView.scrollHandler = graphViewport.scrollHandler
            chartView.zoomHandler = buildZoomHandler()
        }
    }

    private fun buildZoomHandler(): ZoomHandler =
        ZoomHandler(
            zoomEnabled = graphViewport.zoomEnabled,
            initialZoom = graphViewport.initialZoom,
            minZoom = graphViewport.minZoom,
            maxZoom = graphViewport.maxZoom,
        )

    fun differenceSeries(newSeries: Set<WiFiDetail>): List<WiFiDetail> = seriesCache.difference(newSeries)

    fun addSeries(
        wiFiDetail: WiFiDetail,
        dataPoints: List<DataPoint>,
        drawBackground: Boolean,
    ): Boolean =
        if (seriesExists(wiFiDetail)) {
            false
        } else {
            val connected = wiFiDetail.wiFiAdditional.wiFiConnection.connected
            val graphColor = if (connected) graphColors.connectedColor else graphColors.graphColor()
            val seriesData = SeriesData(dataPoints, graphColor, seriesTitle(wiFiDetail), connected, drawBackground)
            seriesCache.put(wiFiDetail, seriesData)
            true
        }

    fun updateSeries(
        wiFiDetail: WiFiDetail,
        data: List<DataPoint>,
        drawBackground: Boolean,
    ): Boolean {
        val seriesData = seriesCache[wiFiDetail] ?: return false
        seriesData.replaceAll(data)
        seriesData.title = seriesTitle(wiFiDetail)
        updateConnectionColor(seriesData, wiFiDetail.wiFiAdditional.wiFiConnection.connected)
        seriesData.drawBackground = drawBackground
        return true
    }

    fun appendToSeries(
        wiFiDetail: WiFiDetail,
        data: DataPoint,
        count: Int,
        drawBackground: Boolean,
    ): Boolean {
        val seriesData = seriesCache[wiFiDetail] ?: return false
        seriesData.append(data, count + 1)
        updateConnectionColor(seriesData, wiFiDetail.wiFiAdditional.wiFiConnection.connected)
        seriesData.drawBackground = drawBackground
        return true
    }

    fun destroy() {
        coroutineScope.cancel()
    }

    fun flushData() {
        val populatedEntries = seriesCache.populatedEntries()
        if (populatedEntries.isEmpty()) return
        val existingChart = chartView.chart ?: return
        val populatedData = chartUpdater.sync(populatedEntries, existingChart, graphViewport.rangeProvider)
        val snapshot = populatedData.toCoordinates()
        coroutineScope.launch {
            modelProducer.runTransaction {
                lineModel {
                    snapshot.forEach { (x, y) ->
                        series(x = x, y = y)
                    }
                }
            }
        }
    }

    fun newSeries(wiFiDetail: WiFiDetail): Boolean = !seriesExists(wiFiDetail)

    fun calculateGraphType(): Int =
        runCatching {
            with(MessageDigest.getInstance("MD5")) {
                update(
                    MainContext.INSTANCE.mainActivity.packageName
                        .toByteArray(),
                )
                val digest: ByteArray = digest()
                digest.contentHashCode()
            }
        }.getOrDefault(TYPE1)

    fun show() {
        chartView.visibility = View.VISIBLE
    }

    fun gone() {
        chartView.visibility = View.GONE
    }

    fun size(value: Int): Int = if (value == TYPE1 || value == TYPE2 || value == TYPE3) SIZE_MAX else SIZE_MIN

    private fun updateConnectionColor(
        seriesData: SeriesData,
        connected: Boolean,
    ) {
        if (seriesData.connected == connected) return
        seriesData.graphColor =
            if (connected) {
                graphColors.addColor(seriesData.graphColor.primary)
                graphColors.connectedColor
            } else {
                graphColors.graphColor()
            }
        seriesData.connected = connected
    }

    private fun seriesExists(wiFiDetail: WiFiDetail): Boolean = seriesCache.contains(wiFiDetail)

    private fun seriesTitle(wiFiDetail: WiFiDetail): String =
        "${wiFiDetail.wiFiIdentifier.ssid} ${wiFiDetail.wiFiSignal.channelDisplay()}"
}
