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
package com.vrem.wifianalyzer.vendor.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class VendorUtilsTest {

    private val macAddressClean = "0023AB"
    private val macAddressShort = "00:23:AB"
    private val macAddressFull = "00:23:AB:8C:DF:10"

    @Test
    fun testClean() {
        assertTrue("".clean().isEmpty())
        assertEquals(macAddressClean, macAddressFull.clean())
        assertEquals("34AF", "34aF".clean())
        assertEquals("34AF0B", "34aF0B".clean())
        assertEquals("34AA0B", "34:aa:0b".clean())
        assertEquals("34AC0B", "34:ac:0B:A0".clean())
    }

    @Test
    fun testToMacAddress() {
        assertTrue("".toMacAddress().isEmpty())
        assertEquals(macAddressShort, macAddressClean.toMacAddress())
        assertEquals("*34AF*", "34AF".toMacAddress())
        assertEquals("34:AF:0B", "34AF0BAC".toMacAddress())
    }

}