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

import android.net.wifi.ScanResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class WiFiWidthTest {

    @Test
    fun width() {
        assertThat(WiFiWidth.entries.size).isEqualTo(5)
    }

    @Test
    fun groupByGroup() {
        assertThat(WiFiWidth.MHZ_20.calculateCenter.javaClass.isInstance(calculateCenter20)).isTrue()
        assertThat(WiFiWidth.MHZ_40.calculateCenter.javaClass.isInstance(calculateCenter40)).isTrue()
        assertThat(WiFiWidth.MHZ_80.calculateCenter.javaClass.isInstance(calculateCenter80)).isTrue()
        assertThat(WiFiWidth.MHZ_160.calculateCenter.javaClass.isInstance(calculateCenter160)).isTrue()
        assertThat(WiFiWidth.MHZ_80_PLUS.calculateCenter.javaClass.isInstance(calculateCenter80)).isTrue()
    }

    @Test
    fun frequencyWidth() {
        assertThat(WiFiWidth.MHZ_20.frequencyWidth).isEqualTo(20)
        assertThat(WiFiWidth.MHZ_40.frequencyWidth).isEqualTo(40)
        assertThat(WiFiWidth.MHZ_80.frequencyWidth).isEqualTo(80)
        assertThat(WiFiWidth.MHZ_160.frequencyWidth).isEqualTo(160)
        assertThat(WiFiWidth.MHZ_80_PLUS.frequencyWidth).isEqualTo(80)
    }

    @Test
    fun frequencyHalfWidth() {
        assertThat(WiFiWidth.MHZ_20.frequencyWidthHalf).isEqualTo(10)
        assertThat(WiFiWidth.MHZ_40.frequencyWidthHalf).isEqualTo(20)
        assertThat(WiFiWidth.MHZ_80.frequencyWidthHalf).isEqualTo(40)
        assertThat(WiFiWidth.MHZ_160.frequencyWidthHalf).isEqualTo(80)
        assertThat(WiFiWidth.MHZ_80_PLUS.frequencyWidthHalf).isEqualTo(40)
    }

    @Test
    fun guardBand() {
        assertThat(WiFiWidth.MHZ_20.guardBand).isEqualTo(2)
        assertThat(WiFiWidth.MHZ_40.guardBand).isEqualTo(3)
        assertThat(WiFiWidth.MHZ_80.guardBand).isEqualTo(3)
        assertThat(WiFiWidth.MHZ_160.guardBand).isEqualTo(3)
        assertThat(WiFiWidth.MHZ_80_PLUS.guardBand).isEqualTo(3)
    }

    @Test
    fun findOne() {
        assertThat(WiFiWidth.findOne(ScanResult.CHANNEL_WIDTH_20MHZ)).isEqualTo(WiFiWidth.MHZ_20)
        assertThat(WiFiWidth.findOne(ScanResult.CHANNEL_WIDTH_40MHZ)).isEqualTo(WiFiWidth.MHZ_40)
        assertThat(WiFiWidth.findOne(ScanResult.CHANNEL_WIDTH_80MHZ)).isEqualTo(WiFiWidth.MHZ_80)
        assertThat(WiFiWidth.findOne(ScanResult.CHANNEL_WIDTH_160MHZ)).isEqualTo(WiFiWidth.MHZ_160)
        assertThat(WiFiWidth.findOne(ScanResult.CHANNEL_WIDTH_80MHZ_PLUS_MHZ)).isEqualTo(WiFiWidth.MHZ_80_PLUS)
        assertThat(WiFiWidth.findOne(ScanResult.CHANNEL_WIDTH_20MHZ - 1)).isEqualTo(WiFiWidth.MHZ_20)
        assertThat(WiFiWidth.findOne(ScanResult.CHANNEL_WIDTH_80MHZ_PLUS_MHZ + 1)).isEqualTo(WiFiWidth.MHZ_20)
    }

    @Test
    fun calculateCenter20() {
        // setup
        val expected = 35
        // execute & validate
        assertThat(calculateCenter20(expected, Int.MIN_VALUE)).isEqualTo(expected)
        assertThat(calculateCenter20(expected, 0)).isEqualTo(expected)
        assertThat(calculateCenter20(expected, Int.MAX_VALUE)).isEqualTo(expected)
    }

    @Test
    fun calculateCenter40() {
        // setup
        val primary = 10
        val center = primary + WiFiWidth.MHZ_40.frequencyWidthHalf - 1
        // execute & validate
        assertThat(calculateCenter40(primary, center)).isEqualTo(center)
        assertThat(calculateCenter40(center, primary)).isEqualTo(primary)
    }

    @Test
    fun calculateCenter40WithCenter() {
        // setup
        val primary = 10
        val center = primary + WiFiWidth.MHZ_40.frequencyWidthHalf
        val expected = (primary + center) / 2
        // execute & validate
        assertThat(calculateCenter40(primary, center)).isEqualTo(expected)
        assertThat(calculateCenter40(center, primary)).isEqualTo(expected)
    }

    @Test
    fun calculateCenter80() {
        // setup
        val expected = 35
        // execute & validate
        assertThat(calculateCenter80(Int.MIN_VALUE, expected)).isEqualTo(expected)
        assertThat(calculateCenter80(0, expected)).isEqualTo(expected)
        assertThat(calculateCenter80(Int.MAX_VALUE, expected)).isEqualTo(expected)
    }

    @Test
    fun calculateCenter160Invalid() {
        // execute & validate
        assertThat(calculateCenter160(5169, Int.MIN_VALUE)).isEqualTo(Int.MIN_VALUE)
        assertThat(calculateCenter160(5169, 0)).isEqualTo(0)
        assertThat(calculateCenter160(5169, Int.MAX_VALUE)).isEqualTo(Int.MAX_VALUE)
        assertThat(calculateCenter160(5896, Int.MIN_VALUE)).isEqualTo(Int.MIN_VALUE)
        assertThat(calculateCenter160(5896, 0)).isEqualTo(0)
        assertThat(calculateCenter160(5896, Int.MAX_VALUE)).isEqualTo(Int.MAX_VALUE)
    }

    @Test
    fun calculateCenter160() {
        withTestDatas().forEach {
            println(it)
            validate(it.expected, it.start, it.end)
        }
    }

    private fun validate(expected: Int, start: Int, end: Int) {
        // execute & validate
        for (value in start..end) {
            assertThat(calculateCenter160(value, Int.MIN_VALUE)).isEqualTo(expected)
            assertThat(calculateCenter160(value, 0)).isEqualTo(expected)
            assertThat(calculateCenter160(value, Int.MAX_VALUE)).isEqualTo(expected)
        }
    }

    private data class TestData(val tag: String, val expected: Int, val start: Int, val end: Int)

    private fun withTestDatas(): List<TestData> =
        listOf(
            TestData("UNII_1_2A", 5250, 5170, 5330),
            TestData("UNII_2C_3", 5570, 5490, 5730),
            TestData("UNII_3_4", 5815, 5735, 5895),
            TestData("UNII_5_1", 6025, 5950, 6100),
            TestData("UNII_5_2", 6185, 6110, 6260),
            TestData("UNII_5_3", 6345, 6270, 6420),
            TestData("UNII_6_1", 6505, 6430, 6580),
            TestData("UNII_7_1", 6665, 6590, 6740),
            TestData("UNII_7_2", 6825, 6750, 6900),
            TestData("UNII_8_1", 6985, 6910, 7120)
        )

}