/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2023 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
        assertTrue(WiFiWidth.bandwidth20MHz.calculateCenter.javaClass.isInstance(calculateCenter20))
        assertTrue(WiFiWidth.bandwidth40MHz.calculateCenter.javaClass.isInstance(calculateCenter40))
        assertTrue(WiFiWidth.bandwidth80MHz.calculateCenter.javaClass.isInstance(calculateCenter80))
        assertTrue(WiFiWidth.bandwidth160MHz.calculateCenter.javaClass.isInstance(calculateCenter160))
        assertTrue(WiFiWidth.bandwidth80Plus80MHz.calculateCenter.javaClass.isInstance(calculateCenter80))
    }

    @Test
    fun testFrequencyWidth() {
        assertEquals(20, WiFiWidth.bandwidth20MHz.frequencyWidth)
        assertEquals(40, WiFiWidth.bandwidth40MHz.frequencyWidth)
        assertEquals(80, WiFiWidth.bandwidth80MHz.frequencyWidth)
        assertEquals(160, WiFiWidth.bandwidth160MHz.frequencyWidth)
        assertEquals(80, WiFiWidth.bandwidth80Plus80MHz.frequencyWidth)
    }

    @Test
    fun testFrequencyHalfWidth() {
        assertEquals(10, WiFiWidth.bandwidth20MHz.frequencyWidthHalf)
        assertEquals(20, WiFiWidth.bandwidth40MHz.frequencyWidthHalf)
        assertEquals(40, WiFiWidth.bandwidth80MHz.frequencyWidthHalf)
        assertEquals(80, WiFiWidth.bandwidth160MHz.frequencyWidthHalf)
        assertEquals(40, WiFiWidth.bandwidth80Plus80MHz.frequencyWidthHalf)
    }

    @Test
    fun testGuardBand() {
        assertEquals(2, WiFiWidth.bandwidth20MHz.guardBand)
        assertEquals(3, WiFiWidth.bandwidth40MHz.guardBand)
        assertEquals(3, WiFiWidth.bandwidth80MHz.guardBand)
        assertEquals(3, WiFiWidth.bandwidth160MHz.guardBand)
        assertEquals(3, WiFiWidth.bandwidth80Plus80MHz.guardBand)
    }

    @Test
    fun testFindOne() {
        assertEquals(WiFiWidth.bandwidth20MHz, WiFiWidth.findOne(ScanResult.CHANNEL_WIDTH_20MHZ))
        assertEquals(WiFiWidth.bandwidth40MHz, WiFiWidth.findOne(ScanResult.CHANNEL_WIDTH_40MHZ))
        assertEquals(WiFiWidth.bandwidth80MHz, WiFiWidth.findOne(ScanResult.CHANNEL_WIDTH_80MHZ))
        assertEquals(WiFiWidth.bandwidth160MHz, WiFiWidth.findOne(ScanResult.CHANNEL_WIDTH_160MHZ))
        assertEquals(WiFiWidth.bandwidth80Plus80MHz, WiFiWidth.findOne(ScanResult.CHANNEL_WIDTH_80MHZ_PLUS_MHZ))
        assertEquals(WiFiWidth.bandwidth20MHz, WiFiWidth.findOne(ScanResult.CHANNEL_WIDTH_20MHZ - 1))
        assertEquals(WiFiWidth.bandwidth20MHz, WiFiWidth.findOne(ScanResult.CHANNEL_WIDTH_80MHZ_PLUS_MHZ + 1))
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
        val center = primary + WiFiWidth.bandwidth40MHz.frequencyWidthHalf - 1
        // execute & validate
        assertEquals(center, calculateCenter40(primary, center))
        assertEquals(primary, calculateCenter40(center, primary))
    }

    @Test
    fun testCalculateCenter40WithCenter() {
        // setup
        val primary = 10
        val center = primary + WiFiWidth.bandwidth40MHz.frequencyWidthHalf
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
    fun testCalculateCenter160_UNII_6_1() {
        // execute & validate
        for (value in 6430..6580) {
            assertEquals(6505, calculateCenter160(value, Int.MIN_VALUE))
            assertEquals(6505, calculateCenter160(value, 0))
            assertEquals(6505, calculateCenter160(value, Int.MAX_VALUE))
        }
    }

    @Test
    fun testCalculateCenter160_UNII_7_1() {
        // execute & validate
        for (value in 6590..6740) {
            assertEquals(6665, calculateCenter160(value, Int.MIN_VALUE))
            assertEquals(6665, calculateCenter160(value, 0))
            assertEquals(6665, calculateCenter160(value, Int.MAX_VALUE))
        }
    }

    @Test
    fun testCalculateCenter160_UNII_7_2() {
        // execute & validate
        for (value in 6750..6900) {
            assertEquals(6825, calculateCenter160(value, Int.MIN_VALUE))
            assertEquals(6825, calculateCenter160(value, 0))
            assertEquals(6825, calculateCenter160(value, Int.MAX_VALUE))
        }
    }

    @Test
    fun testCalculateCenter160_UNII_8_1() {
        // execute & validate
        for (value in 6910..7120) {
            assertEquals(6985, calculateCenter160(value, Int.MIN_VALUE))
            assertEquals(6985, calculateCenter160(value, 0))
            assertEquals(6985, calculateCenter160(value, Int.MAX_VALUE))
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
