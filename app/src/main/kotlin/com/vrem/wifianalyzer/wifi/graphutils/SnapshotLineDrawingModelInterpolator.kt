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

import com.patrykandpatrick.vico.views.cartesian.data.LineCartesianLayerDrawingModel
import com.patrykandpatrick.vico.views.common.data.CartesianLayerDrawingModelInterpolator
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

/**
 * Thread-safe replacement for Vico's `DefaultCartesianLayerDrawingModelInterpolator`.
 */
class SnapshotLineDrawingModelInterpolator :
    CartesianLayerDrawingModelInterpolator<LineCartesianLayerDrawingModel.Entry, LineCartesianLayerDrawingModel> {
    @Volatile
    private var snapshot = Snapshot(null, null, emptyList())

    override fun setModels(
        old: LineCartesianLayerDrawingModel?,
        new: LineCartesianLayerDrawingModel?,
    ) {
        val oldEntriesByKey = old?.let { it.seriesKeys.zip(it).toMap() }.orEmpty()
        val transformationMaps =
            new
                ?.let { newModel ->
                    newModel.seriesKeys.zip(newModel).map { (key, newEntries) ->
                        val map = mutableMapOf<Double, Transformation>()
                        oldEntriesByKey[key]?.forEach { (x, entry) -> map[x] = Transformation(entry, null) }
                        newEntries.forEach { (x, entry) -> map[x] = Transformation(map[x]?.old, entry) }
                        map
                    }
                }.orEmpty()
        snapshot = Snapshot(old, new, transformationMaps)
    }

    override suspend fun transform(fraction: Float): LineCartesianLayerDrawingModel? {
        val current = snapshot
        val newModel = current.newModel ?: return null
        val entries =
            current.transformationMaps.map { map ->
                map
                    .mapNotNull { (x, transformation) ->
                        currentCoroutineContext().ensureActive()
                        transformation.transform(fraction)?.let { entry -> x to entry }
                    }.toMap()
            }
        return newModel.transform(entries, current.oldModel, fraction) as LineCartesianLayerDrawingModel
    }

    private class Snapshot(
        val oldModel: LineCartesianLayerDrawingModel?,
        val newModel: LineCartesianLayerDrawingModel?,
        val transformationMaps: List<Map<Double, Transformation>>,
    )

    private class Transformation(
        val old: LineCartesianLayerDrawingModel.Entry?,
        val new: LineCartesianLayerDrawingModel.Entry?,
    ) {
        fun transform(fraction: Float): LineCartesianLayerDrawingModel.Entry? =
            new?.transform(old, fraction) as LineCartesianLayerDrawingModel.Entry?
    }
}
