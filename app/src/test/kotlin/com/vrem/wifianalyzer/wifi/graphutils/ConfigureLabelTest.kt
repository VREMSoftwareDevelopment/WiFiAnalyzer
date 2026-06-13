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
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.patrykandpatrick.vico.views.cartesian.CartesianDrawingContext
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.BAKLAVA])
class ConfigureLabelTest {
    private val position = LabelPosition(10f, 20f, Paint.Align.LEFT)
    private val seriesData = SeriesData(listOf(DataPoint(1, -50)), GraphColor(0xFF0000, 0x00FF00), "SSID")
    private val context: CartesianDrawingContext = mock()
    private val paint = Paint()

    @Before
    fun setUp() {
        doReturn(20f).whenever(context).spToPx(10f)
    }

    @After
    fun tearDown() {
        verify(context).spToPx(10f)
        verifyNoMoreInteractions(context)
    }

    @Test
    fun configureLabelSetsTextAlign() {
        // Act
        configureLabel(context, position, seriesData, paint)
        // Assert
        assertThat(paint.textAlign).isEqualTo(Paint.Align.LEFT)
    }

    @Test
    fun configureLabelSetsColor() {
        // Act
        configureLabel(context, position, seriesData, paint)
        // Assert
        assertThat(paint.color).isEqualTo(0xFF0000)
    }

    @Test
    fun configureLabelSetsTextSize() {
        // Act
        configureLabel(context, position, seriesData, paint)
        // Assert
        assertThat(paint.textSize).isEqualTo(20f)
    }

    @Test
    fun configureLabelSetsDefaultTypeface() {
        // Act
        configureLabel(context, position, seriesData, paint)
        // Assert
        assertThat(paint.typeface).isEqualTo(Typeface.DEFAULT)
    }

    @Test
    fun configureLabelSetsBoldTypeface() {
        // Arrange
        val seriesData = SeriesData(listOf(DataPoint(1, -50)), GraphColor(0xFF0000, 0x00FF00), "SSID", true)
        // Act
        configureLabel(context, position, seriesData, paint)
        // Assert
        assertThat(paint.typeface).isEqualTo(Typeface.DEFAULT_BOLD)
    }
}
