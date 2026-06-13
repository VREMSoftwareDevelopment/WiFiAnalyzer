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
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.patrykandpatrick.vico.views.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.views.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.views.cartesian.marker.Interaction
import com.patrykandpatrick.vico.views.common.Fill
import com.patrykandpatrick.vico.views.common.Point
import com.patrykandpatrick.vico.views.common.component.ShapeComponent
import com.patrykandpatrick.vico.views.common.component.TextComponent
import com.patrykandpatrick.vico.views.common.shape.CorneredShape
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.robolectric.annotation.Config

private const val THRESHOLD_PX = 50f

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.BAKLAVA])
class CreateMarkerTest {
    @Test
    fun createsDefaultCartesianMarker() {
        // Arrange
        val expected =
            DefaultCartesianMarker(
                label = TextComponent(color = Color.TRANSPARENT, textSizeSp = 0f),
                indicator = { color -> ShapeComponent(fill = Fill(color), shape = CorneredShape.Pill) },
            )
        // Act
        val actual = createMarker()
        // Assert
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected)
    }
}

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.BAKLAVA])
class MarkerVisibilityListenerWrapperTest {
    @Test
    fun onShownCallsCallback() {
        // Arrange
        var callbackTargets: List<CartesianMarker.Target>? = null
        val listener = MarkerVisibilityListenerWrapper { targets -> callbackTargets = targets }
        val marker: DefaultCartesianMarker = mock()
        val expectedTargets: List<CartesianMarker.Target> = emptyList()
        // Act
        listener.onShown(marker, expectedTargets)
        // Assert
        assertThat(callbackTargets).isSameAs(expectedTargets)
    }
}

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.BAKLAVA])
class MarkerControllerWrapperTest {
    private var capturedPoint: Point? = null
    private val controller = MarkerControllerWrapper(THRESHOLD_PX) { point -> capturedPoint = point }

    @Test
    fun acceptsLongPress() {
        // Act & Assert
        assertThat(controller.acceptsLongPress).isFalse()
    }

    @Test
    fun shouldAcceptInteractionRelease() {
        // Act
        val actual = controller.shouldAcceptInteraction(Interaction.Release(Point(0f, 0f)), emptyList())
        // Assert
        assertThat(actual).isTrue()
    }

    @Test
    fun shouldAcceptInteractionMove() {
        // Act
        val actual = controller.shouldAcceptInteraction(Interaction.Move(Point(0f, 0f)), emptyList())
        // Assert
        assertThat(actual).isFalse()
    }

    @Test
    fun shouldAcceptInteractionPressEmptyTargets() {
        // Act
        val actual = controller.shouldAcceptInteraction(Interaction.Press(Point(0f, 0f)), emptyList())
        // Assert
        assertThat(actual).isFalse()
    }

    @Test
    fun shouldAcceptInteractionPressNonLineTarget() {
        // Arrange
        val target: CartesianMarker.Target = mock()
        // Act
        val actual = controller.shouldAcceptInteraction(Interaction.Press(Point(0f, 0f)), listOf(target))
        // Assert
        assertThat(actual).isFalse()
    }

    @Test
    fun shouldShowMarkerPress() {
        // Act
        val actual = controller.shouldShowMarker(Interaction.Press(Point(0f, 0f)), emptyList())
        // Assert
        assertThat(actual).isTrue()
    }

    @Test
    fun shouldShowMarkerRelease() {
        // Act
        val actual = controller.shouldShowMarker(Interaction.Release(Point(0f, 0f)), emptyList())
        // Assert
        assertThat(actual).isFalse()
    }
}
