/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2020 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.wifi.band

import org.junit.Assert.assertEquals
import org.junit.Test

class WiFiWidthTest {
    @Test
    fun testWiFiWidth() {
        assertEquals(5, WiFiWidth.values().size)
    }

    @Test
    fun testFrequencyWidth() {
        assertEquals(20, WiFiWidth.MHZ_20.frequencyWidth)
        assertEquals(40, WiFiWidth.MHZ_40.frequencyWidth)
        assertEquals(80, WiFiWidth.MHZ_80.frequencyWidth)
        assertEquals(160, WiFiWidth.MHZ_160.frequencyWidth)
        assertEquals(80, WiFiWidth.MHZ_80_PLUS.frequencyWidth)
    }

    @Test
    fun testFrequencyHalfWidth() {
        assertEquals(10, WiFiWidth.MHZ_20.frequencyWidthHalf)
        assertEquals(20, WiFiWidth.MHZ_40.frequencyWidthHalf)
        assertEquals(40, WiFiWidth.MHZ_80.frequencyWidthHalf)
        assertEquals(80, WiFiWidth.MHZ_160.frequencyWidthHalf)
        assertEquals(40, WiFiWidth.MHZ_80_PLUS.frequencyWidthHalf)
    }
}