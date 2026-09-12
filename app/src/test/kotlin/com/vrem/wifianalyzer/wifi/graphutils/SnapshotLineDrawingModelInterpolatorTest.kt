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

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.patrykandpatrick.vico.views.cartesian.data.LineCartesianLayerDrawingModel
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.CINNAMON_BUN])
class SnapshotLineDrawingModelInterpolatorTest {
    private val fixture = SnapshotLineDrawingModelInterpolator()

    @Test
    fun transformWithoutModelsReturnsNull() {
        // Act
        val actual = runBlocking { fixture.transform(1f) }
        // Assert
        assertThat(actual).isNull()
    }

    @Test
    fun transformAtFractionOneYieldsNewModelValues() {
        // Arrange
        val new = drawingModel("a" to mapOf(0.0 to 0.2f, 1.0 to 0.4f))
        fixture.setModels(old = null, new = new)
        // Act
        val actual = runBlocking { fixture.transform(1f) }!!
        // Assert
        assertThat(actual.seriesKeys).isEqualTo(listOf("a"))
        assertThat(actual[0][0.0]?.y).isEqualTo(0.2f)
        assertThat(actual[0][1.0]?.y).isEqualTo(0.4f)
    }

    @Test
    fun transformInterpolatesBetweenOldAndNewForMatchingKeys() {
        // Arrange
        val old = drawingModel("a" to mapOf(0.0 to 0.0f))
        val new = drawingModel("a" to mapOf(0.0 to 1.0f))
        fixture.setModels(old = old, new = new)
        // Act
        val actual = runBlocking { fixture.transform(0.5f) }!!
        // Assert
        assertThat(actual[0][0.0]?.y).isEqualTo(0.5f)
    }

    @Test
    fun transformAfterSeriesCountChangeMatchesLatestModel() {
        // Arrange
        fixture.setModels(old = null, new = drawingModel("a" to mapOf(0.0 to 0.1f), "b" to mapOf(0.0 to 0.2f)))
        val latest =
            drawingModel(
                "a" to mapOf(0.0 to 0.1f),
                "b" to mapOf(0.0 to 0.2f),
                "c" to mapOf(0.0 to 0.3f),
            )
        fixture.setModels(old = null, new = latest)
        // Act
        val actual = runBlocking { fixture.transform(1f) }!!
        // Assert
        assertThat(actual.seriesKeys).isEqualTo(listOf("a", "b", "c"))
        assertThat(actual).hasSize(3)
    }

    @Test
    fun transformDropsPointAbsentFromNewModel() {
        // Arrange
        val old = drawingModel("a" to mapOf(0.0 to 0.2f, 1.0 to 0.4f))
        val new = drawingModel("a" to mapOf(0.0 to 0.6f))
        fixture.setModels(old = old, new = new)
        // Act
        val actual = runBlocking { fixture.transform(1f) }!!
        // Assert
        assertThat(actual[0]).containsOnlyKeys(0.0)
        assertThat(actual[0][0.0]?.y).isEqualTo(0.6f)
    }

    private fun drawingModel(vararg series: Pair<Any, Map<Double, Float>>): LineCartesianLayerDrawingModel =
        LineCartesianLayerDrawingModel(
            series.map { (_, points) -> points.mapValues { (_, y) -> LineCartesianLayerDrawingModel.Entry(y) } },
            series.map { it.first },
        )
}
