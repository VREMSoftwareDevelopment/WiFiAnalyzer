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

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.text.DecimalFormat

class WiFiUtilsTest {
    private val decimalFormat = DecimalFormat("#.##")

    @Test
    fun testCalculateDistance() {
        validate(2437, -36, "0.62")
        validate(2437, -42, "1.23")
        validate(2432, -88, "246.34")
        validate(2412, -91, "350.85")
    }

    private fun validate(frequency: Int, level: Int, expected: String) {
        assertEquals(expected, decimalFormat.format(calculateDistance(frequency, level)))
    }

    @Test
    fun testCalculateSignalLevel() {
        assertEquals(0, calculateSignalLevel(-110, 5))
        assertEquals(0, calculateSignalLevel(-89, 5))
        assertEquals(1, calculateSignalLevel(-88, 5))
        assertEquals(1, calculateSignalLevel(-78, 5))
        assertEquals(2, calculateSignalLevel(-77, 5))
        assertEquals(2, calculateSignalLevel(-67, 5))
        assertEquals(3, calculateSignalLevel(-66, 5))
        assertEquals(3, calculateSignalLevel(-56, 5))
        assertEquals(4, calculateSignalLevel(-55, 5))
        assertEquals(4, calculateSignalLevel(0, 5))
    }

    @Test
    fun testConvertIpAddress() {
        assertEquals("21.205.91.7", convertIpAddress(123456789))
        assertEquals("1.0.0.0", convertIpAddress(1))
        assertTrue(convertIpAddress(0).isEmpty())
        assertTrue(convertIpAddress(-1).isEmpty())
    }

    @Test
    fun testConvertSSID() {
        assertEquals("SSID", convertSSID("\"SSID\""))
        assertEquals("SSID", convertSSID("SSID"))
    }
}