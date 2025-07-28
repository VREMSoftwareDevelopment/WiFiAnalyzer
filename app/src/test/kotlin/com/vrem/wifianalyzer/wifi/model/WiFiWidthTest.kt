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

import android.net.wifi.ScanResult
import com.vrem.wifianalyzer.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class WiFiWidthTest {
    @Test
    fun wiFiWidth() {
        assertThat(WiFiWidth.entries)
            .hasSize(6)
            .containsExactly(
                WiFiWidth.MHZ_20,
                WiFiWidth.MHZ_40,
                WiFiWidth.MHZ_80,
                WiFiWidth.MHZ_160,
                WiFiWidth.MHZ_80_PLUS,
                WiFiWidth.MHZ_320,
            )
    }

    @Test
    fun calculateCenter() {
        assertThat(WiFiWidth.MHZ_20.calculateCenter).isInstanceOf(calculateCenterUsingPrimary::class.java)
        assertThat(WiFiWidth.MHZ_40.calculateCenter).isInstanceOf(calculateCenter40::class.java)
        assertThat(WiFiWidth.MHZ_80.calculateCenter).isInstanceOf(calculateCenterUsingCenter0::class.java)
        assertThat(WiFiWidth.MHZ_160.calculateCenter).isInstanceOf(calculateCenterUsingCenter1::class.java)
        assertThat(WiFiWidth.MHZ_80_PLUS.calculateCenter).isInstanceOf(calculateCenterUsingCenter1::class.java)
        assertThat(WiFiWidth.MHZ_320.calculateCenter).isInstanceOf(calculateCenterUsingCenter1::class.java)
    }

    @Test
    fun textResource() {
        assertThat(WiFiWidth.MHZ_20.textResource).isEqualTo(R.string.wifi_width_20mhz)
        assertThat(WiFiWidth.MHZ_40.textResource).isEqualTo(R.string.wifi_width_40mhz)
        assertThat(WiFiWidth.MHZ_80.textResource).isEqualTo(R.string.wifi_width_80mhz)
        assertThat(WiFiWidth.MHZ_160.textResource).isEqualTo(R.string.wifi_width_160mhz)
        assertThat(WiFiWidth.MHZ_80_PLUS.textResource).isEqualTo(R.string.wifi_width_80mhz)
        assertThat(WiFiWidth.MHZ_320.textResource).isEqualTo(R.string.wifi_width_320mhz)
    }

    @Test
    fun frequencyWidth() {
        assertThat(WiFiWidth.MHZ_20.frequencyWidth).isEqualTo(20)
        assertThat(WiFiWidth.MHZ_40.frequencyWidth).isEqualTo(40)
        assertThat(WiFiWidth.MHZ_80.frequencyWidth).isEqualTo(80)
        assertThat(WiFiWidth.MHZ_160.frequencyWidth).isEqualTo(160)
        assertThat(WiFiWidth.MHZ_80_PLUS.frequencyWidth).isEqualTo(80)
        assertThat(WiFiWidth.MHZ_320.frequencyWidth).isEqualTo(320)
    }

    @Test
    fun frequencyHalfWidth() {
        assertThat(WiFiWidth.MHZ_20.frequencyWidthHalf).isEqualTo(10)
        assertThat(WiFiWidth.MHZ_40.frequencyWidthHalf).isEqualTo(20)
        assertThat(WiFiWidth.MHZ_80.frequencyWidthHalf).isEqualTo(40)
        assertThat(WiFiWidth.MHZ_160.frequencyWidthHalf).isEqualTo(80)
        assertThat(WiFiWidth.MHZ_80_PLUS.frequencyWidthHalf).isEqualTo(40)
        assertThat(WiFiWidth.MHZ_320.frequencyWidthHalf).isEqualTo(160)
    }

    @Test
    fun guardBand() {
        assertThat(WiFiWidth.MHZ_20.guardBand).isEqualTo(2)
        assertThat(WiFiWidth.MHZ_40.guardBand).isEqualTo(3)
        assertThat(WiFiWidth.MHZ_80.guardBand).isEqualTo(3)
        assertThat(WiFiWidth.MHZ_160.guardBand).isEqualTo(3)
        assertThat(WiFiWidth.MHZ_80_PLUS.guardBand).isEqualTo(3)
        assertThat(WiFiWidth.MHZ_320.guardBand).isEqualTo(3)
    }

    @Test
    fun step() {
        assertThat(WiFiWidth.MHZ_20.step).isEqualTo(4)
        assertThat(WiFiWidth.MHZ_40.step).isEqualTo(8)
        assertThat(WiFiWidth.MHZ_80.step).isEqualTo(16)
        assertThat(WiFiWidth.MHZ_160.step).isEqualTo(32)
        assertThat(WiFiWidth.MHZ_80_PLUS.step).isEqualTo(16)
        assertThat(WiFiWidth.MHZ_320.step).isEqualTo(32)
    }

    @Test
    fun findOne() {
        assertThat(WiFiWidth.findOne(ScanResult.CHANNEL_WIDTH_20MHZ)).isEqualTo(WiFiWidth.MHZ_20)
        assertThat(WiFiWidth.findOne(ScanResult.CHANNEL_WIDTH_40MHZ)).isEqualTo(WiFiWidth.MHZ_40)
        assertThat(WiFiWidth.findOne(ScanResult.CHANNEL_WIDTH_80MHZ)).isEqualTo(WiFiWidth.MHZ_80)
        assertThat(WiFiWidth.findOne(ScanResult.CHANNEL_WIDTH_160MHZ)).isEqualTo(WiFiWidth.MHZ_160)
        assertThat(WiFiWidth.findOne(ScanResult.CHANNEL_WIDTH_320MHZ)).isEqualTo(WiFiWidth.MHZ_320)
        assertThat(WiFiWidth.findOne(ScanResult.CHANNEL_WIDTH_80MHZ_PLUS_MHZ)).isEqualTo(WiFiWidth.MHZ_80_PLUS)
        assertThat(WiFiWidth.findOne(ScanResult.CHANNEL_WIDTH_20MHZ - 1)).isEqualTo(WiFiWidth.MHZ_20)
        assertThat(WiFiWidth.findOne(ScanResult.CHANNEL_WIDTH_320MHZ + 1)).isEqualTo(WiFiWidth.MHZ_20)
    }

    @Test
    fun calculateCenterUsingPrimary() {
        // setup
        val expected = 35
        // execute & validate
        assertThat(calculateCenterUsingPrimary(expected, Int.MIN_VALUE, Int.MIN_VALUE)).isEqualTo(expected)
        assertThat(calculateCenterUsingPrimary(expected, 0, Int.MIN_VALUE)).isEqualTo(expected)
        assertThat(calculateCenterUsingPrimary(expected, Int.MAX_VALUE, Int.MIN_VALUE)).isEqualTo(expected)
    }

    @Test
    fun calculateCenter40() {
        // setup
        val primary = 10
        val center = primary + WiFiWidth.MHZ_40.frequencyWidthHalf - 1
        // execute & validate
        assertThat(calculateCenter40(primary, center, Int.MIN_VALUE)).isEqualTo(center)
        assertThat(calculateCenter40(center, primary, Int.MIN_VALUE)).isEqualTo(primary)
    }

    @Test
    fun calculateCenter40WithCenter() {
        // setup
        val primary = 10
        val center = primary + WiFiWidth.MHZ_40.frequencyWidthHalf
        val expected = (primary + center) / 2
        // execute & validate
        assertThat(calculateCenter40(primary, center, Int.MIN_VALUE)).isEqualTo(expected)
        assertThat(calculateCenter40(center, primary, Int.MIN_VALUE)).isEqualTo(expected)
    }

    @Test
    fun calculateCenterUsingCenter0() {
        // setup
        val expected = 35
        // execute & validate
        assertThat(calculateCenterUsingCenter0(0, expected, 0)).isEqualTo(expected)
        assertThat(calculateCenterUsingCenter0(0, expected, Int.MIN_VALUE)).isEqualTo(expected)
        assertThat(calculateCenterUsingCenter0(0, expected, Int.MAX_VALUE)).isEqualTo(expected)
        assertThat(calculateCenterUsingCenter0(Int.MIN_VALUE, expected, 0)).isEqualTo(expected)
        assertThat(calculateCenterUsingCenter0(Int.MAX_VALUE, expected, 0)).isEqualTo(expected)
        assertThat(calculateCenterUsingCenter0(Int.MIN_VALUE, expected, Int.MIN_VALUE)).isEqualTo(expected)
        assertThat(calculateCenterUsingCenter0(Int.MAX_VALUE, expected, Int.MAX_VALUE)).isEqualTo(expected)
    }

    @Test
    fun calculateCenterUsingCenter1() {
        // setup
        val expected = 35
        // execute & validate
        assertThat(calculateCenterUsingCenter1(0, 0, expected)).isEqualTo(expected)
        assertThat(calculateCenterUsingCenter1(0, Int.MIN_VALUE, expected)).isEqualTo(expected)
        assertThat(calculateCenterUsingCenter1(0, Int.MAX_VALUE, expected)).isEqualTo(expected)
        assertThat(calculateCenterUsingCenter1(Int.MIN_VALUE, 0, expected)).isEqualTo(expected)
        assertThat(calculateCenterUsingCenter1(Int.MAX_VALUE, 0, expected)).isEqualTo(expected)
        assertThat(calculateCenterUsingCenter1(Int.MIN_VALUE, Int.MIN_VALUE, expected)).isEqualTo(expected)
        assertThat(calculateCenterUsingCenter1(Int.MAX_VALUE, Int.MAX_VALUE, expected)).isEqualTo(expected)
    }
}
