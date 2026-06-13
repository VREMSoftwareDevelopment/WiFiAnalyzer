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

import android.view.View
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
    private val graphNotifier: GraphNotifier = mock()
    private val view: View = mock()
    private val wiFiData: WiFiData = mock()
    private val fixture = GraphAdapter(listOf(graphNotifier))

    @After
    fun tearDown() {
        verifyNoMoreInteractions(graphNotifier)
        verifyNoMoreInteractions(view)
        verifyNoMoreInteractions(wiFiData)
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun update() {
        // execute
        fixture.update(wiFiData)
        // validate
        verify(graphNotifier).update(wiFiData)
    }

    @Test
    fun destroy() {
        // Act
        fixture.destroy()
        // Assert
        verify(graphNotifier).destroy()
    }

    @Test
    fun graphs() {
        // setup
        whenever(graphNotifier.graph()).thenReturn(view)
        // execute
        val actual = fixture.graphs()
        // validate
        assertThat(actual).hasSize(1)
        assertThat(actual[0]).isEqualTo(view)
        verify(graphNotifier).graph()
    }
}
