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
package com.vrem.wifianalyzer.wifi.timegraph

import com.vrem.util.EMPTY
import com.vrem.wifianalyzer.wifi.graphutils.MAX_Y
import com.vrem.wifianalyzer.wifi.graphutils.MIN_Y
import org.junit.Assert.assertEquals
import org.junit.Test

class TimeAxisLabelTest {
    private val fixture = TimeAxisLabel()

    @Test
    fun testYAxis() {
        assertEquals(String.EMPTY, fixture.formatLabel(MIN_Y.toDouble(), false))
        assertEquals("-99", fixture.formatLabel(MIN_Y + 1.toDouble(), false))
        assertEquals("0", fixture.formatLabel(MAX_Y.toDouble(), false))
        assertEquals(String.EMPTY, fixture.formatLabel(MAX_Y + 1.toDouble(), false))
    }

    @Test
    fun testXAxis() {
        assertEquals(String.EMPTY, fixture.formatLabel(-2.0, true))
        assertEquals(String.EMPTY, fixture.formatLabel(-1.0, true))
        assertEquals(String.EMPTY, fixture.formatLabel(0.0, true))
        assertEquals(String.EMPTY, fixture.formatLabel(1.0, true))
        assertEquals("2", fixture.formatLabel(2.0, true))
        assertEquals("10", fixture.formatLabel(10.0, true))
    }
}