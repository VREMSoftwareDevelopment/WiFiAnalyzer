/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2021 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.R
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

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
    fun testGetColorStartsOverWhenEndIsReached() {
        // setup
        val graphColors = withGraphColors()
        // validate & execute
        assertEquals(graphColors[2], fixture.graphColor())
        assertEquals(graphColors[1], fixture.graphColor())
        assertEquals(graphColors[0], fixture.graphColor())
        assertEquals(graphColors[2], fixture.graphColor())
    }

    @Test
    fun testAddColorAddsColorToAvailablePool() {
        // setup
        val graphColors = withGraphColors()
        val expected = graphColors[2]
        // validate & execute
        assertEquals(expected, fixture.graphColor())
        fixture.addColor(expected.primary)
        assertEquals(expected, fixture.graphColor())
    }

    @Test
    fun testAddColorDoesNotAddNonExistingColor() {
        // setup
        val graphColors = withGraphColors()
        val expected = graphColors[1]
        val graphColor = graphColors[2]
        val original = fixture.graphColor()
        // execute
        val actual = fixture.graphColor()
        // validate
        assertEquals(expected, actual)
        assertEquals(graphColor, original)
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