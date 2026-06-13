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

import android.graphics.Paint
import android.graphics.Typeface
import androidx.core.graphics.withClip
import com.patrykandpatrick.vico.views.cartesian.CartesianDrawingContext
import com.patrykandpatrick.vico.views.cartesian.decoration.Decoration

typealias CalculateLabelPosition = (CartesianDrawingContext, SeriesData) -> LabelPosition?
typealias ConfigureLabel = (CartesianDrawingContext, LabelPosition, SeriesData, Paint) -> Unit

data class LabelPosition(
    val x: Float,
    val y: Float,
    val textAlign: Paint.Align = Paint.Align.CENTER,
)

internal fun CartesianDrawingContext.canvasY(point: DataPoint): Float {
    val yRange = ranges.getYRange(null)
    return layerBounds.bottom - ((point.y - yRange.minY) / yRange.length).toFloat() * layerBounds.height()
}

internal val configureLabel: ConfigureLabel = { context, position, seriesData, paint ->
    paint.textAlign = position.textAlign
    paint.color = seriesData.graphColor.primary
    paint.textSize = context.spToPx(10f)
    paint.typeface = if (seriesData.connected) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
}

class SeriesLabel(
    val calculateLabelPosition: CalculateLabelPosition,
    val configure: ConfigureLabel = configureLabel,
    var seriesSnapshot: List<SeriesData> = emptyList(),
) : Decoration {
    internal val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun drawOverLayers(context: CartesianDrawingContext) {
        if (seriesSnapshot.isEmpty()) return
        with(context) {
            canvas.withClip(layerBounds) {
                val verticalOffset = spToPx(4f)
                seriesSnapshot.forEach { seriesData ->
                    calculateLabelPosition(context, seriesData)?.let { position ->
                        configure(context, position, seriesData, paint)
                        drawText(seriesData.title, position.x, position.y - verticalOffset, paint)
                    }
                }
            }
        }
    }
}
