/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2024 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import com.jjoe64.graphview.GraphView
import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.wifi.model.WiFiData
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

class GraphAdapterTest {
    private val graphViewNotifier: GraphViewNotifier = mock()
    private val graphView: GraphView = mock()
    private val wiFiData: WiFiData = mock()
    private val fixture = GraphAdapter(listOf(graphViewNotifier))

    @After
    fun tearDown() {
        verifyNoMoreInteractions(graphViewNotifier)
        verifyNoMoreInteractions(graphView)
        verifyNoMoreInteractions(wiFiData)
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun update() {
        // execute
        fixture.update(wiFiData)
        // validate
        verify(graphViewNotifier).update(wiFiData)
    }

    @Test
    fun graphViews() {
        // setup
        whenever(graphViewNotifier.graphView()).thenReturn(graphView)
        // execute
        val actual = fixture.graphViews()
        // validate
        assertThat(actual).hasSize(1)
        assertThat(actual[0]).isEqualTo(graphView)
        verify(graphViewNotifier).graphView()
    }
}