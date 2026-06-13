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

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import com.patrykandpatrick.vico.views.cartesian.CartesianChart
import com.patrykandpatrick.vico.views.cartesian.CartesianChartView
import com.patrykandpatrick.vico.views.cartesian.ScrollHandler
import com.patrykandpatrick.vico.views.cartesian.ZoomHandler
import com.patrykandpatrick.vico.views.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.views.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.views.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.views.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.views.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.views.common.Fill
import com.patrykandpatrick.vico.views.common.component.LineComponent
import com.patrykandpatrick.vico.views.common.component.TextComponent
import com.vrem.util.EMPTY
import com.vrem.wifianalyzer.settings.ThemeStyle

class GraphBuilder(
    private val maximumY: Int,
    private val themeStyle: ThemeStyle,
    private val xStep: Double? = null,
) {
    private var xAxisFormatter: CartesianValueFormatter = CartesianValueFormatter.decimal()
    private var yAxisFormatter: CartesianValueFormatter = signalYAxisFormatter
    private var verticalTitle: String = String.EMPTY
    private var horizontalTitle: String = String.EMPTY
    private var itemPlacer: HorizontalAxis.ItemPlacer = HorizontalAxis.ItemPlacer.aligned(shiftExtremeLines = false)

    fun setXAxisFormatter(formatter: CartesianValueFormatter): GraphBuilder {
        this.xAxisFormatter = formatter
        return this
    }

    fun setVerticalTitle(verticalTitle: String): GraphBuilder {
        this.verticalTitle = verticalTitle
        return this
    }

    fun setHorizontalTitle(horizontalTitle: String): GraphBuilder {
        this.horizontalTitle = horizontalTitle
        return this
    }

    fun setItemPlacer(itemPlacer: HorizontalAxis.ItemPlacer): GraphBuilder {
        this.itemPlacer = itemPlacer
        return this
    }

    fun build(
        context: Context,
        scalable: Boolean,
    ): CartesianChartView {
        val chartView = CartesianChartView(context)
        chartView.layoutParams = layoutParams
        chartView.visibility = View.GONE
        chartView.animateIn = false
        chartView.setAnimationDuration(0)

        val textColor = themeStyle.colorGraphText
        val labelTextSizeSp = 12f * TEXT_SIZE_ADJUSTMENT
        val titleTextSizeSp = 12f * AXIS_TEXT_SIZE_ADJUSTMENT

        val labelComponent = TextComponent(color = textColor, textSizeSp = labelTextSizeSp)
        val titleComponent = TextComponent(color = textColor, textSizeSp = titleTextSizeSp)
        val guideline = LineComponent(Fill(Color.GRAY), thicknessDp = 0.5f)

        val axisLine = LineComponent(Fill(Color.GRAY), thicknessDp = 1f)

        val startAxis =
            VerticalAxis.start(
                line = axisLine,
                label = labelComponent,
                valueFormatter = yAxisFormatter,
                guideline = guideline,
                itemPlacer = VerticalAxis.ItemPlacer.step({ 10.0 }),
                titleComponent = titleComponent,
                title = { verticalTitle },
            )

        val endAxis =
            VerticalAxis.end(
                line = axisLine,
            )

        val bottomAxis =
            HorizontalAxis.bottom(
                line = axisLine,
                label = labelComponent,
                valueFormatter = xAxisFormatter,
                guideline = guideline,
                itemPlacer = itemPlacer,
                titleComponent = titleComponent,
                title = { horizontalTitle },
            )

        val defaultLine =
            LineCartesianLayer.Line(
                fill = LineCartesianLayer.LineFill.single(Fill(Color.TRANSPARENT)),
            )
        val lineLayer =
            LineCartesianLayer(
                lineProvider = LineCartesianLayer.LineProvider.series(defaultLine),
                rangeProvider =
                    CartesianLayerRangeProvider.fixed(
                        minY = MIN_Y.toDouble(),
                        maxY = maximumPortY.toDouble(),
                    ),
            )

        val chart =
            CartesianChart(
                lineLayer,
                startAxis = startAxis,
                endAxis = endAxis,
                bottomAxis = bottomAxis,
            )
        chartView.chart = xStep?.let { value -> chart.copy(getXStep = { _, _, _ -> value }) } ?: chart

        chartView.scrollHandler = ScrollHandler(scrollEnabled = true)
        chartView.zoomHandler = ZoomHandler(zoomEnabled = scalable)

        return chartView
    }

    val numVerticalLabels: Int get() = (maximumPortY - MIN_Y) / 10 + 1

    val maximumPortY: Int get() = if (maximumY in MIN_Y_HALF..MAX_Y) maximumY else MAX_Y_DEFAULT

    val layoutParams: ViewGroup.LayoutParams =
        ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
}
