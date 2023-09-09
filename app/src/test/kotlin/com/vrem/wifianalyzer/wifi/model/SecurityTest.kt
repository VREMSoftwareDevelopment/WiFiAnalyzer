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

import com.vrem.wifianalyzer.R
import org.junit.Assert.*
import org.junit.Test

class SecurityTest {
    @Test
    fun testSize() {
        assertEquals(6, Security.entries.size)
    }

    @Test
    fun testImageResource() {
        assertEquals(R.drawable.ic_lock_open, Security.NONE.imageResource)
        assertEquals(R.drawable.ic_lock_outline, Security.WPS.imageResource)
        assertEquals(R.drawable.ic_lock_outline, Security.WEP.imageResource)
        assertEquals(R.drawable.ic_lock, Security.WPA.imageResource)
        assertEquals(R.drawable.ic_lock, Security.WPA2.imageResource)
        assertEquals(R.drawable.ic_lock, Security.WPA3.imageResource)
    }

    @Test
    fun testOrder() {
        // setup
        val expected: Array<Security> = arrayOf(Security.NONE, Security.WPS, Security.WEP, Security.WPA, Security.WPA2, Security.WPA3)
        // execute
        val actual = Security.entries
        // validate
        assertArrayEquals(expected, actual.toTypedArray())
    }

    @Test
    fun testExtras() {
        assertTrue(Security.NONE.extras.isEmpty())
        assertTrue(Security.WPS.extras.isEmpty())
        assertTrue(Security.WEP.extras.isEmpty())
        assertTrue(Security.WPA.extras.isEmpty())
        assertTrue(Security.WPA2.extras.isEmpty())
        assertEquals(listOf("SAE", "EAP_SUITE_B_192", "OWE"), Security.WPA3.extras)
    }
}