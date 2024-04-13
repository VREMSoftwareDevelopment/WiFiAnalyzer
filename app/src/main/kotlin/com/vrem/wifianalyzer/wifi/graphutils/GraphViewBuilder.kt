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
package com.vrem.wifianalyzer.wifi.graphutils

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.LabelFormatter
import com.jjoe64.graphview.Viewport
import com.vrem.util.EMPTY
import com.vrem.wifianalyzer.settings.ThemeStyle

internal fun GraphView.layout(layoutParams: ViewGroup.LayoutParams): GraphView {
    this.layoutParams = layoutParams
    this.visibility = View.GONE
    return this
}

internal fun Viewport.initialize(maximumY: Int): Viewport {
    this.isScrollable = true
    this.isYAxisBoundsManual = true
    this.setMinY(MIN_Y.toDouble())
    this.setMaxY(maximumY.toDouble())
    this.isXAxisBoundsManual = true
    return this
}

internal fun GridLabelRenderer.colors(themeStyle: ThemeStyle): GridLabelRenderer {
    val color = if (ThemeStyle.DARK == themeStyle) Color.WHITE else Color.BLACK
    this.gridColor = Color.GRAY
    this.verticalLabelsColor = color
    this.verticalAxisTitleColor = color
    this.horizontalLabelsColor = color
    this.horizontalAxisTitleColor = color
    return this
}

internal fun GridLabelRenderer.horizontalTitle(title: String): GridLabelRenderer {
    if (title.isNotEmpty()) {
        this.horizontalAxisTitle = title
        this.horizontalAxisTitleTextSize *= AXIS_TEXT_SIZE_ADJUSTMENT
    }
    return this
}

internal fun GridLabelRenderer.verticalTitle(title: String): GridLabelRenderer {
    if (title.isNotEmpty()) {
        this.verticalAxisTitle = title
        this.verticalAxisTitleTextSize *= AXIS_TEXT_SIZE_ADJUSTMENT
    }
    return this
}

internal fun GridLabelRenderer.labelFormat(labelFormatter: LabelFormatter?): GridLabelRenderer {
    labelFormatter?.let {
        this.labelFormatter = labelFormatter
    }
    return this
}

internal fun GridLabelRenderer.labels(
    numHorizontalLabels: Int,
    numVerticalLabels: Int,
    horizontalLabelsVisible: Boolean
): GridLabelRenderer {
    this.setHumanRounding(false)
    this.isHighlightZeroLines = false
    this.numVerticalLabels = numVerticalLabels
    this.numHorizontalLabels = numHorizontalLabels
    this.isVerticalLabelsVisible = true
    this.isHorizontalLabelsVisible = horizontalLabelsVisible
    this.textSize *= TEXT_SIZE_ADJUSTMENT
    this.reloadStyles()
    return this
}

class GraphViewBuilder(
    private val numHorizontalLabels: Int,
    private val maximumY: Int,
    private val themeStyle: ThemeStyle,
    private val horizontalLabelsVisible: Boolean = true
) {
    private var labelFormatter: LabelFormatter? = null
    private var verticalTitle: String = String.EMPTY
    private var horizontalTitle: String = String.EMPTY

    fun setLabelFormatter(labelFormatter: LabelFormatter): GraphViewBuilder {
        this.labelFormatter = labelFormatter
        return this
    }

    fun setVerticalTitle(verticalTitle: String): GraphViewBuilder {
        this.verticalTitle = verticalTitle
        return this
    }

    fun setHorizontalTitle(horizontalTitle: String): GraphViewBuilder {
        this.horizontalTitle = horizontalTitle
        return this
    }

    fun build(context: Context): GraphView =
        GraphView(context)
            .layout(layoutParams)
            .gridLabelInitialize()
            .viewportInitialize()

    private fun GraphView.viewportInitialize(): GraphView {
        this.viewport.initialize(maximumY)
        return this
    }

    private fun GraphView.gridLabelInitialize(): GraphView {
        this.gridLabelRenderer
            .labels(numHorizontalLabels, numVerticalLabels, horizontalLabelsVisible)
            .labelFormat(labelFormatter)
            .horizontalTitle(horizontalTitle)
            .verticalTitle(verticalTitle)
            .colors(themeStyle)
        return this
    }

    val numVerticalLabels: Int
        get() = (maximumPortY - MIN_Y) / 10 + 1

    val maximumPortY: Int
        get() = if (maximumY > MAX_Y || maximumY < MIN_Y_HALF) MAX_Y_DEFAULT else maximumY

    val layoutParams: ViewGroup.LayoutParams =
        ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

}