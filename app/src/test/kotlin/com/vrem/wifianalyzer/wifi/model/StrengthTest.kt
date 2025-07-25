/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2025 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class StrengthTest {
    @Test
    fun strength() {
        assertThat(Strength.entries)
            .hasSize(5)
            .containsExactly(Strength.ZERO, Strength.ONE, Strength.TWO, Strength.THREE, Strength.FOUR)
    }

    @Test
    fun strengthOrdinal() {
        assertThat(Strength.ZERO.ordinal).isEqualTo(0)
        assertThat(Strength.ONE.ordinal).isEqualTo(1)
        assertThat(Strength.TWO.ordinal).isEqualTo(2)
        assertThat(Strength.THREE.ordinal).isEqualTo(3)
        assertThat(Strength.FOUR.ordinal).isEqualTo(4)
    }

    @Test
    fun imageResource() {
        val expectedImages = listOf(
            R.drawable.ic_signal_wifi_0_bar,
            R.drawable.ic_signal_wifi_1_bar,
            R.drawable.ic_signal_wifi_2_bar,
            R.drawable.ic_signal_wifi_3_bar,
            R.drawable.ic_signal_wifi_4_bar
        )
        Strength.entries.forEachIndexed { i, strength ->
            assertThat(strength.imageResource).isEqualTo(expectedImages[i])
        }
    }

    @Test
    fun colorResource() {
        val expectedColors = listOf(
            R.color.error,
            R.color.warning,
            R.color.warning,
            R.color.success,
            R.color.success
        )
        Strength.entries.forEachIndexed { i, strength ->
            assertThat(strength.colorResource).isEqualTo(expectedColors[i])
        }
    }

    @Test
    fun weak() {
        assertThat(Strength.ZERO.weak()).isTrue
        assertThat(Strength.ONE.weak()).isFalse
        assertThat(Strength.TWO.weak()).isFalse
        assertThat(Strength.THREE.weak()).isFalse
        assertThat(Strength.FOUR.weak()).isFalse
    }

    @Test
    fun calculate() {
        val testCases = listOf(
            -89 to Strength.ZERO,
            -88 to Strength.ONE,
            -78 to Strength.ONE,
            -77 to Strength.TWO,
            -67 to Strength.TWO,
            -66 to Strength.THREE,
            -56 to Strength.THREE,
            -55 to Strength.FOUR,
            0 to Strength.FOUR
        )
        testCases.forEach { (input, expected) ->
            assertThat(Strength.calculate(input)).isEqualTo(expected)
        }
    }

    @Test
    fun reverse() {
        val expected = listOf(Strength.FOUR, Strength.THREE, Strength.TWO, Strength.ONE, Strength.ZERO)
        Strength.entries.forEachIndexed { i, strength ->
            assertThat(Strength.reverse(strength)).isEqualTo(expected[i])
        }
    }
}