/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2024 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import com.vrem.wifianalyzer.wifi.model.Strength.Companion.calculate
import com.vrem.wifianalyzer.wifi.model.Strength.Companion.reverse
import org.junit.Assert.*
import org.junit.Test

class StrengthTest {
    @Test
    fun testStrength() {
        assertEquals(5, Strength.entries.size)
    }

    @Test
    fun testImageResource() {
        assertEquals(R.drawable.ic_signal_wifi_0_bar, Strength.ZERO.imageResource)
        assertEquals(R.drawable.ic_signal_wifi_1_bar, Strength.ONE.imageResource)
        assertEquals(R.drawable.ic_signal_wifi_2_bar, Strength.TWO.imageResource)
        assertEquals(R.drawable.ic_signal_wifi_3_bar, Strength.THREE.imageResource)
        assertEquals(R.drawable.ic_signal_wifi_4_bar, Strength.FOUR.imageResource)
    }

    @Test
    fun testColorResource() {
        assertEquals(R.color.error, Strength.ZERO.colorResource)
        assertEquals(R.color.warning, Strength.ONE.colorResource)
        assertEquals(R.color.warning, Strength.TWO.colorResource)
        assertEquals(R.color.success, Strength.THREE.colorResource)
        assertEquals(R.color.success, Strength.FOUR.colorResource)
    }

    @Test
    fun testWeak() {
        assertTrue(Strength.ZERO.weak())
        assertFalse(Strength.ONE.weak())
        assertFalse(Strength.TWO.weak())
        assertFalse(Strength.THREE.weak())
        assertFalse(Strength.FOUR.weak())
    }

    @Test
    fun testCalculate() {
        assertEquals(Strength.ZERO, calculate(-89))
        assertEquals(Strength.ONE, calculate(-88))
        assertEquals(Strength.ONE, calculate(-78))
        assertEquals(Strength.TWO, calculate(-77))
        assertEquals(Strength.TWO, calculate(-67))
        assertEquals(Strength.THREE, calculate(-66))
        assertEquals(Strength.THREE, calculate(-56))
        assertEquals(Strength.FOUR, calculate(-55))
        assertEquals(Strength.FOUR, calculate(0))
    }

    @Test
    fun testReverse() {
        assertEquals(Strength.FOUR, reverse(Strength.ZERO))
        assertEquals(Strength.THREE, reverse(Strength.ONE))
        assertEquals(Strength.TWO, reverse(Strength.TWO))
        assertEquals(Strength.ONE, reverse(Strength.THREE))
        assertEquals(Strength.ZERO, reverse(Strength.FOUR))
    }
}