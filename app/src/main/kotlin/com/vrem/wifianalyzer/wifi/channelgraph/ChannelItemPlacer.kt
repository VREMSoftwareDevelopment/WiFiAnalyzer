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

import com.patrykandpatrick.vico.views.cartesian.CartesianDrawingContext
import com.patrykandpatrick.vico.views.cartesian.CartesianMeasuringContext
import com.patrykandpatrick.vico.views.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.views.cartesian.layer.CartesianLayerDimensions
import com.vrem.wifianalyzer.wifi.band.FREQUENCY_SPREAD
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import kotlin.math.max

private const val LINE_FREQUENCY_GHZ2 = FREQUENCY_SPREAD
private const val LINE_FREQUENCY_OTHER = FREQUENCY_SPREAD * 4

internal fun channelItemPlacer(wiFiBand: WiFiBand): HorizontalAxis.ItemPlacer {
    val wiFiChannels = wiFiBand.wiFiChannels
    val labelFrequencies =
        wiFiChannels.graphChannels.keys
            .map { wiFiChannels.wiFiChannelByChannel(it).frequency.toDouble() }
            .sorted()
    val measurementLabels = labelFrequencies.take(3)
    return object : HorizontalAxis.ItemPlacer {
        override fun getLabelValues(
            context: CartesianDrawingContext,
            visibleXRange: ClosedFloatingPointRange<Double>,
            fullXRange: ClosedFloatingPointRange<Double>,
            maxLabelWidth: Float,
        ): List<Double> = labelFrequencies.filter { it in fullXRange.start..fullXRange.endInclusive }

        override fun getWidthMeasurementLabelValues(
            context: CartesianMeasuringContext,
            layerDimensions: CartesianLayerDimensions,
            fullXRange: ClosedFloatingPointRange<Double>,
        ): List<Double> = measurementLabels

        override fun getHeightMeasurementLabelValues(
            context: CartesianMeasuringContext,
            layerDimensions: CartesianLayerDimensions,
            fullXRange: ClosedFloatingPointRange<Double>,
            maxLabelWidth: Float,
        ): List<Double> = measurementLabels

        override fun getStartLayerMargin(
            context: CartesianMeasuringContext,
            layerDimensions: CartesianLayerDimensions,
            tickThickness: Float,
            maxLabelWidth: Float,
        ): Float = 0f

        override fun getEndLayerMargin(
            context: CartesianMeasuringContext,
            layerDimensions: CartesianLayerDimensions,
            tickThickness: Float,
            maxLabelWidth: Float,
        ): Float = 0f

        override fun getLineValues(
            context: CartesianDrawingContext,
            visibleXRange: ClosedFloatingPointRange<Double>,
            fullXRange: ClosedFloatingPointRange<Double>,
            maxLabelWidth: Float,
        ): List<Double> {
            val ranges = context.ranges
            val lineFrequency = if (wiFiBand.ghz2) LINE_FREQUENCY_GHZ2 else LINE_FREQUENCY_OTHER
            val lineSpacing = max(1, (lineFrequency / ranges.xStep).toInt())
            val totalSteps = ((ranges.maxX - ranges.minX) / ranges.xStep).toInt()
            return (0..totalSteps step lineSpacing)
                .map { ranges.minX + it * ranges.xStep }
                .filter { it in fullXRange.start..fullXRange.endInclusive }
        }

        override fun getShiftExtremeLines(context: CartesianDrawingContext): Boolean = false
    }
}
