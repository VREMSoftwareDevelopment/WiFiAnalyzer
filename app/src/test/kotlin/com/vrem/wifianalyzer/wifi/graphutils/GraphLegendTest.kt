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

import com.jjoe64.graphview.LegendRenderer
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions

class GraphLegendTest {
    private val legendRenderer: LegendRenderer = mock()

    @After
    fun tearDown() {
        verifyNoMoreInteractions(legendRenderer)
    }

    @Test
    fun graphLegend() {
        assertThat(GraphLegend.entries)
            .hasSize(3)
            .containsExactly(GraphLegend.LEFT, GraphLegend.RIGHT, GraphLegend.HIDE)
    }

    @Test
    fun graphLegendOrdinal() {
        assertThat(GraphLegend.LEFT.ordinal).isEqualTo(0)
        assertThat(GraphLegend.RIGHT.ordinal).isEqualTo(1)
        assertThat(GraphLegend.HIDE.ordinal).isEqualTo(2)
    }

    @Test
    fun graphLegendDisplay() {
        assertThat(legendDisplayNone).isEqualTo(GraphLegend.HIDE.legendDisplay)
        assertThat(legendDisplayLeft).isEqualTo(GraphLegend.LEFT.legendDisplay)
        assertThat(legendDisplayRight).isEqualTo(GraphLegend.RIGHT.legendDisplay)
    }

    @Test
    fun graphLegendHideDisplay() {
        GraphLegend.HIDE.display(legendRenderer)
        verify(legendRenderer).isVisible = false
    }

    @Test
    fun graphLegendLeftDisplay() {
        GraphLegend.LEFT.display(legendRenderer)
        verify(legendRenderer).isVisible = true
        verify(legendRenderer).setFixedPosition(0, 0)
    }

    @Test
    fun graphLegendRightDisplay() {
        GraphLegend.RIGHT.display(legendRenderer)
        verify(legendRenderer).isVisible = true
        verify(legendRenderer).align = LegendRenderer.LegendAlign.TOP
    }
}