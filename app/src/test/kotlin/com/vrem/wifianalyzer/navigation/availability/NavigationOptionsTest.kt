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
package com.vrem.wifianalyzer.navigation.availability

import org.junit.Assert.*
import org.junit.Test

class NavigationOptionsTest {
    @Test
    fun testRating() {
        val options: List<NavigationOption> = navigationOptionRating
        assertEquals(4, options.size)
        assertTrue(options.contains(navigationOptionWifiSwitchOn))
        assertTrue(options.contains(navigationOptionScannerSwitchOn))
        assertTrue(options.contains(navigationOptionFilterOff))
        assertTrue(options.contains(navigationOptionBottomNavOn))
        assertFalse(options.contains(navigationOptionWifiSwitchOff))
        assertFalse(options.contains(navigationOptionScannerSwitchOff))
        assertFalse(options.contains(navigationOptionFilterOn))
        assertFalse(options.contains(navigationOptionBottomNavOff))
    }

    @Test
    fun testOther() {
        val options: List<NavigationOption> = navigationOptionOther
        assertEquals(4, options.size)
        assertTrue(options.contains(navigationOptionWifiSwitchOn))
        assertTrue(options.contains(navigationOptionScannerSwitchOn))
        assertTrue(options.contains(navigationOptionFilterOn))
        assertTrue(options.contains(navigationOptionBottomNavOn))
        assertFalse(options.contains(navigationOptionWifiSwitchOff))
        assertFalse(options.contains(navigationOptionScannerSwitchOff))
        assertFalse(options.contains(navigationOptionFilterOff))
        assertFalse(options.contains(navigationOptionBottomNavOff))
    }

    @Test
    fun testOff() {
        val options: List<NavigationOption> = navigationOptionOff
        assertEquals(4, options.size)
        assertTrue(options.contains(navigationOptionWifiSwitchOff))
        assertTrue(options.contains(navigationOptionScannerSwitchOff))
        assertTrue(options.contains(navigationOptionFilterOff))
        assertTrue(options.contains(navigationOptionBottomNavOff))
        assertFalse(options.contains(navigationOptionWifiSwitchOn))
        assertFalse(options.contains(navigationOptionScannerSwitchOn))
        assertFalse(options.contains(navigationOptionFilterOn))
        assertFalse(options.contains(navigationOptionBottomNavOn))
    }

    @Test
    fun testAccessPoints() {
        val options: List<NavigationOption> = navigationOptionAp
        assertEquals(4, options.size)
        assertTrue(options.contains(navigationOptionWifiSwitchOff))
        assertTrue(options.contains(navigationOptionScannerSwitchOn))
        assertTrue(options.contains(navigationOptionFilterOn))
        assertTrue(options.contains(navigationOptionBottomNavOn))
        assertFalse(options.contains(navigationOptionWifiSwitchOn))
        assertFalse(options.contains(navigationOptionScannerSwitchOff))
        assertFalse(options.contains(navigationOptionFilterOff))
        assertFalse(options.contains(navigationOptionBottomNavOff))
    }
}