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

import com.jjoe64.graphview.GraphView
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.wifi.model.WiFiData
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test

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
    fun testUpdate() {
        // execute
        fixture.update(wiFiData)
        // validate
        verify(graphViewNotifier).update(wiFiData)
    }

    @Test
    fun testGraphViews() {
        // setup
        whenever(graphViewNotifier.graphView()).thenReturn(graphView)
        // execute
        val actual = fixture.graphViews()
        // validate
        assertEquals(1, actual.size)
        assertEquals(graphView, actual[0])
        verify(graphViewNotifier).graphView()
    }
}