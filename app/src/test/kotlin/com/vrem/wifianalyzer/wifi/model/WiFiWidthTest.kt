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
import org.junit.Assert.assertTrue
import org.junit.Test

class WiFiWidthTest {

    @Test
    fun testWidth() {
        assertEquals(5, WiFiWidth.values().size)
    }

    @Test
    fun testGroupByGroup() {
        assertTrue(WiFiWidth.MHZ_20.calculateCenter.javaClass.isInstance(calculateCenter20))
        assertTrue(WiFiWidth.MHZ_40.calculateCenter.javaClass.isInstance(calculateCenter40))
        assertTrue(WiFiWidth.MHZ_80.calculateCenter.javaClass.isInstance(calculateCenter80))
        assertTrue(WiFiWidth.MHZ_160.calculateCenter.javaClass.isInstance(calculateCenter160))
        assertTrue(WiFiWidth.MHZ_80_PLUS.calculateCenter.javaClass.isInstance(calculateCenter80))
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

    @Test
    fun testCalculateCenter20() {
        // setup
        val expected = 35
        // execute & validate
        assertEquals(expected, calculateCenter20(expected, Int.MIN_VALUE))
        assertEquals(expected, calculateCenter20(expected, 0))
        assertEquals(expected, calculateCenter20(expected, Int.MAX_VALUE))
    }

    @Test
    fun testCalculateCenter40() {
        // setup
        val primary = 10
        val center = primary + WiFiWidth.MHZ_40.frequencyWidthHalf - 1
        // execute & validate
        assertEquals(center, calculateCenter40(primary, center))
        assertEquals(primary, calculateCenter40(center, primary))
    }

    @Test
    fun testCalculateCenter40WithCenterBroken() {
        // setup
        val primary = 10
        val center = primary + WiFiWidth.MHZ_40.frequencyWidthHalf
        val expected = (primary + center) / 2
        // execute & validate
        assertEquals(expected, calculateCenter40(primary, center))
        assertEquals(expected, calculateCenter40(center, primary))
    }

    @Test
    fun testCalculateCenter80() {
        // setup
        val expected = 35
        // execute & validate
        assertEquals(expected, calculateCenter80(Int.MIN_VALUE, expected))
        assertEquals(expected, calculateCenter80(0, expected))
        assertEquals(expected, calculateCenter80(Int.MAX_VALUE, expected))
    }

    @Test
    fun testCalculateCenter160_UNII_1_2A() {
        // execute & validate
        for (value in 5170..5330) {
            assertEquals(5250, calculateCenter160(value, Int.MIN_VALUE))
            assertEquals(5250, calculateCenter160(value, 0))
            assertEquals(5250, calculateCenter160(value, Int.MAX_VALUE))
        }
    }

    @Test
    fun testCalculateCenter160_UNII_2C_3() {
        // execute & validate
        for (value in 5490..5730) {
            assertEquals(5570, calculateCenter160(value, Int.MIN_VALUE))
            assertEquals(5570, calculateCenter160(value, 0))
            assertEquals(5570, calculateCenter160(value, Int.MAX_VALUE))
        }
    }

    @Test
    fun testCalculateCenter160_UNII_3_4() {
        // execute & validate
        for (value in 5735..5895) {
            assertEquals(5815, calculateCenter160(value, Int.MIN_VALUE))
            assertEquals(5815, calculateCenter160(value, 0))
            assertEquals(5815, calculateCenter160(value, Int.MAX_VALUE))
        }
    }

    @Test
    fun testCalculateCenter160_UNII_5_1() {
        // execute & validate
        for (value in 5950..6100) {
            assertEquals(6025, calculateCenter160(value, Int.MIN_VALUE))
            assertEquals(6025, calculateCenter160(value, 0))
            assertEquals(6025, calculateCenter160(value, Int.MAX_VALUE))
        }
    }

    @Test
    fun testCalculateCenter160_UNII_5_2() {
        // execute & validate
        for (value in 6110..6260) {
            assertEquals(6185, calculateCenter160(value, Int.MIN_VALUE))
            assertEquals(6185, calculateCenter160(value, 0))
            assertEquals(6185, calculateCenter160(value, Int.MAX_VALUE))
        }
    }

    @Test
    fun testCalculateCenter160_UNII_5_3() {
        // execute & validate
        for (value in 6270..6420) {
            assertEquals(6345, calculateCenter160(value, Int.MIN_VALUE))
            assertEquals(6345, calculateCenter160(value, 0))
            assertEquals(6345, calculateCenter160(value, Int.MAX_VALUE))
        }
    }

    @Test
    fun testCalculateCenter160Invalid() {
        // execute & validate
        assertEquals(Int.MIN_VALUE, calculateCenter160(5169, Int.MIN_VALUE))
        assertEquals(0, calculateCenter160(5169, 0))
        assertEquals(Int.MAX_VALUE, calculateCenter160(5169, Int.MAX_VALUE))
        assertEquals(Int.MIN_VALUE, calculateCenter160(5896, Int.MIN_VALUE))
        assertEquals(0, calculateCenter160(5896, 0))
        assertEquals(Int.MAX_VALUE, calculateCenter160(5896, Int.MAX_VALUE))
    }

}