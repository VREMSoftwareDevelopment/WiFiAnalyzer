/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2025 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import android.content.res.Resources
import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

class GraphColorsTest {
    private val resources: Resources = mock()
    private val context: Context = mock()
    private val fixture = GraphColors()

    @Before
    fun setUp() {
        val mainActivity = MainContextHelper.INSTANCE.mainActivity
        whenever(mainActivity.applicationContext).thenReturn(context)
        whenever(context.resources).thenReturn(resources)
        whenever(resources.getStringArray(R.array.graph_colors)).thenReturn(withColors())
    }

    @After
    fun tearDown() {
        verify(context).resources
        verify(resources).getStringArray(R.array.graph_colors)
        verifyNoMoreInteractions(context)
        verifyNoMoreInteractions(resources)
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun getColorStartsOverWhenEndIsReached() {
        // setup
        val graphColors = withGraphColors()
        // validate & execute
        assertThat(fixture.graphColor()).isEqualTo(graphColors[2])
        assertThat(fixture.graphColor()).isEqualTo(graphColors[1])
        assertThat(fixture.graphColor()).isEqualTo(graphColors[0])
        assertThat(fixture.graphColor()).isEqualTo(graphColors[2])
    }

    @Test
    fun addColorAddsColorToAvailablePool() {
        // setup
        val graphColors = withGraphColors()
        val expected = graphColors[2]
        // validate & execute
        assertThat(fixture.graphColor()).isEqualTo(expected)
        fixture.addColor(expected.primary)
        assertThat(fixture.graphColor()).isEqualTo(expected)
    }

    @Test
    fun addColorDoesNotAddNonExistingColor() {
        // setup
        val graphColors = withGraphColors()
        val expected = graphColors[1]
        val graphColor = graphColors[2]
        val original = fixture.graphColor()
        // execute
        val actual = fixture.graphColor()
        // validate
        assertThat(actual).isEqualTo(expected)
        assertThat(original).isEqualTo(graphColor)
    }

    private fun withColors(): Array<String> {
        return arrayOf("#FB1554", "#33FB1554", "#74FF89", "#3374FF89", "#8B1EFC", "#338B1EFC")
    }

    private fun withGraphColors(): Array<GraphColor> {
        return arrayOf(
            GraphColor(0xFB1554, 0x33FB1554),
            GraphColor(0x74FF89, 0x3374FF89),
            GraphColor(0x8B1EFC, 0x338B1EFC)
        )
    }
}