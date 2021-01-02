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

package com.vrem.wifianalyzer.wifi.model

import android.net.wifi.ScanResult
import org.junit.Assert.assertEquals
import org.junit.Test

class WiFiWidthTest {

    @Test
    fun testWidth() {
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

    @Test
    fun testGuardBand() {
        assertEquals(2, WiFiWidth.MHZ_20.guardBand)
        assertEquals(3, WiFiWidth.MHZ_40.guardBand)
        assertEquals(3, WiFiWidth.MHZ_80.guardBand)
        assertEquals(3, WiFiWidth.MHZ_160.guardBand)
        assertEquals(3, WiFiWidth.MHZ_80_PLUS.guardBand)
    }

    @Test
    fun testFindOne() {
        assertEquals(WiFiWidth.MHZ_20, WiFiWidth.findOne(ScanResult.CHANNEL_WIDTH_20MHZ))
        assertEquals(WiFiWidth.MHZ_40, WiFiWidth.findOne(ScanResult.CHANNEL_WIDTH_40MHZ))
        assertEquals(WiFiWidth.MHZ_80, WiFiWidth.findOne(ScanResult.CHANNEL_WIDTH_80MHZ))
        assertEquals(WiFiWidth.MHZ_160, WiFiWidth.findOne(ScanResult.CHANNEL_WIDTH_160MHZ))
        assertEquals(WiFiWidth.MHZ_80_PLUS, WiFiWidth.findOne(ScanResult.CHANNEL_WIDTH_80MHZ_PLUS_MHZ))
        assertEquals(WiFiWidth.MHZ_20, WiFiWidth.findOne(ScanResult.CHANNEL_WIDTH_20MHZ - 1))
        assertEquals(WiFiWidth.MHZ_20, WiFiWidth.findOne(ScanResult.CHANNEL_WIDTH_80MHZ_PLUS_MHZ + 1))
    }

}