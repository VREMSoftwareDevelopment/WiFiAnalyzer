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

import android.graphics.Color
import com.patrykandpatrick.vico.views.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.views.cartesian.marker.CartesianMarkerController
import com.patrykandpatrick.vico.views.cartesian.marker.CartesianMarkerVisibilityListener
import com.patrykandpatrick.vico.views.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.views.cartesian.marker.Interaction
import com.patrykandpatrick.vico.views.cartesian.marker.LineCartesianLayerMarkerTarget
import com.patrykandpatrick.vico.views.common.Fill
import com.patrykandpatrick.vico.views.common.Point
import com.patrykandpatrick.vico.views.common.component.ShapeComponent
import com.patrykandpatrick.vico.views.common.component.TextComponent
import com.patrykandpatrick.vico.views.common.shape.CorneredShape

internal fun createMarker(): DefaultCartesianMarker =
    DefaultCartesianMarker(
        label = TextComponent(color = Color.TRANSPARENT, textSizeSp = 0f),
        indicator = { color -> ShapeComponent(fill = Fill(color), shape = CorneredShape.Pill) },
    )

class MarkerVisibilityListenerWrapper(
    private val onMarkerShown: (List<CartesianMarker.Target>) -> Unit,
) : CartesianMarkerVisibilityListener {
    override fun onShown(
        marker: CartesianMarker,
        targets: List<CartesianMarker.Target>,
    ) {
        onMarkerShown(targets)
    }
}

class MarkerControllerWrapper(
    private val thresholdPx: Float,
    private val onAccepted: (Point) -> Unit,
) : CartesianMarkerController {
    override val acceptsLongPress: Boolean get() = false

    override fun shouldAcceptInteraction(
        interaction: Interaction,
        targets: List<CartesianMarker.Target>,
    ): Boolean {
        if (interaction is Interaction.Release) return true
        if (interaction !is Interaction.Press) return false
        val accepted =
            targets
                .filterIsInstance<LineCartesianLayerMarkerTarget>()
                .any { target ->
                    target.points.any { point ->
                        interaction.point.withinProximity(target.canvasX, point.canvasY, thresholdPx)
                    }
                }
        if (accepted) {
            onAccepted(interaction.point)
        }
        return accepted
    }

    override fun shouldShowMarker(
        interaction: Interaction,
        targets: List<CartesianMarker.Target>,
    ): Boolean = interaction !is Interaction.Release
}
