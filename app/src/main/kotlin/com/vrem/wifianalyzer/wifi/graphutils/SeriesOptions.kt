/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2020 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import com.jjoe64.graphview.series.BaseSeries
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.TitleLineGraphSeries
import com.vrem.annotation.OpenClass

private fun BaseSeries<DataPoint>.removeSeriesColor(graphColors: GraphColors) = graphColors.addColor(this.color.toLong())

private fun BaseSeries<DataPoint>.highlightConnected(connected: Boolean) {
    val thickness = if (connected) THICKNESS_CONNECTED else THICKNESS_REGULAR
    when (this) {
        is LineGraphSeries<DataPoint> -> this.setThickness(thickness)
        is TitleLineGraphSeries<DataPoint> -> {
            this.thickness = thickness
            this.setTextBold(connected)
        }
    }
}

private fun BaseSeries<DataPoint>.seriesColor(graphColors: GraphColors) {
    val graphColor = graphColors.graphColor()
    this.color = graphColor.primary.toInt()
    when (this) {
        is LineGraphSeries<DataPoint> -> this.backgroundColor = graphColor.background.toInt()
        is TitleLineGraphSeries<DataPoint> -> this.backgroundColor = graphColor.background.toInt()
    }
}

private fun BaseSeries<DataPoint>.drawBackground(drawBackground: Boolean) {
    when (this) {
        is LineGraphSeries<DataPoint> -> this.isDrawBackground = drawBackground
        is TitleLineGraphSeries<DataPoint> -> this.isDrawBackground = drawBackground
    }
}

@OpenClass
class SeriesOptions(private val graphColors: GraphColors = GraphColors()) {

    fun highlightConnected(series: BaseSeries<DataPoint>, connected: Boolean) = series.highlightConnected(connected)

    fun setSeriesColor(series: BaseSeries<DataPoint>) = series.seriesColor(graphColors)

    fun drawBackground(series: BaseSeries<DataPoint>, drawBackground: Boolean) = series.drawBackground(drawBackground)

    fun removeSeriesColor(series: BaseSeries<DataPoint>) = series.removeSeriesColor(graphColors)

}