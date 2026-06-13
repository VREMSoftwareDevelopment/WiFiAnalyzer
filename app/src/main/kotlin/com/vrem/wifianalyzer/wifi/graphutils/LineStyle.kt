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

import com.patrykandpatrick.vico.views.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.views.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.views.common.Fill
import com.vrem.annotation.OpenClass

private data class LineStyle(
    val primaryColor: Int,
    val thicknessDp: Float,
    val backgroundColor: Int?,
)

private fun SeriesData.toLineStyle(): LineStyle =
    LineStyle(
        primaryColor = graphColor.primary,
        thicknessDp = if (connected) THICKNESS_CONNECTED_DP else THICKNESS_REGULAR_DP,
        backgroundColor = if (drawBackground) graphColor.background else null,
    )

private fun SeriesData.styleDigest(prior: Long): Long {
    var digest = prior * 31 + graphColor.primary
    digest = digest * 31 + (if (drawBackground) graphColor.background else -1).toLong()
    return digest * 31 + (if (connected) 1 else 0)
}

@OpenClass
class LineStyleTracker {
    private var lastDigest: Long = 0

    fun changed(seriesData: List<SeriesData>): Boolean {
        val digest = seriesData.fold(seriesData.size.toLong()) { prior, series -> series.styleDigest(prior) }
        return if (digest == lastDigest) {
            false
        } else {
            lastDigest = digest
            true
        }
    }

    fun reset() {
        lastDigest = 0
    }
}

@OpenClass
class LineLayerFactory {
    fun create(
        seriesData: List<SeriesData>,
        rangeProvider: CartesianLayerRangeProvider,
    ): LineCartesianLayer {
        val lineProvider = LineCartesianLayer.LineProvider.series(lines(seriesData))
        return LineCartesianLayer(lineProvider = lineProvider, rangeProvider = rangeProvider)
    }

    internal fun lines(seriesData: List<SeriesData>): List<LineCartesianLayer.Line> =
        seriesData.map { line(it.toLineStyle()) }

    private fun line(lineStyle: LineStyle): LineCartesianLayer.Line {
        val fill = LineCartesianLayer.LineFill.single(Fill(lineStyle.primaryColor))
        val stroke = LineCartesianLayer.LineStroke.Continuous(thicknessDp = lineStyle.thicknessDp)
        val areaFill =
            lineStyle.backgroundColor?.let {
                LineCartesianLayer.AreaFill.single(Fill(it), splitY = { MIN_Y })
            }
        return LineCartesianLayer.Line(fill = fill, stroke = stroke, areaFill = areaFill)
    }
}
