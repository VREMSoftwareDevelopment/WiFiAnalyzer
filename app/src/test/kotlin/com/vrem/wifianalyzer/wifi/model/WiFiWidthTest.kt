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
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class WiFiWidthTest {

    @Test
    fun width() {
        assertThat(WiFiWidth.entries.size).isEqualTo(6)
    }

    @Test
    fun groupByGroup() {
        assertThat(WiFiWidth.MHZ_20.calculateCenter).isInstanceOf(calculateCenter20::class.java)
        assertThat(WiFiWidth.MHZ_40.calculateCenter).isInstanceOf(calculateCenter40::class.java)
        assertThat(WiFiWidth.MHZ_80.calculateCenter).isInstanceOf(calculateCenter80::class.java)
        assertThat(WiFiWidth.MHZ_160.calculateCenter).isInstanceOf(calculateCenter160::class.java)
        assertThat(WiFiWidth.MHZ_80_PLUS.calculateCenter).isInstanceOf(calculateCenter80::class.java)
        assertThat(WiFiWidth.MHZ_320.calculateCenter).isInstanceOf(calculateCenter320::class.java)
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
    fun calculateCenter20() {
        // setup
        val expected = 35
        // execute & validate
        assertThat(calculateCenter20(expected, Int.MIN_VALUE, Int.MIN_VALUE)).isEqualTo(expected)
        assertThat(calculateCenter20(expected, 0, Int.MIN_VALUE)).isEqualTo(expected)
        assertThat(calculateCenter20(expected, Int.MAX_VALUE, Int.MIN_VALUE)).isEqualTo(expected)
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
    fun calculateCenter80() {
        // setup
        val expected = 35
        // execute & validate
        assertThat(calculateCenter80(Int.MIN_VALUE, expected, Int.MIN_VALUE)).isEqualTo(expected)
        assertThat(calculateCenter80(0, expected, Int.MIN_VALUE)).isEqualTo(expected)
        assertThat(calculateCenter80(Int.MAX_VALUE, expected, Int.MIN_VALUE)).isEqualTo(expected)
    }

    @Test
    fun calculateCenter160() {
        // setup
        val frequencyRange = listOf(
            50 to (5250 to (5170 to 5329)),
            82 to (5410 to (5330 to 5489)),
            114 to (5650 to (5490 to 5730)),
            163 to (5815 to (5735 to 5895)),
            15 to (6025 to (5945 to 6104)),
            47 to (6185 to (6105 to 6264)),
            79 to (6345 to (6265 to 6424)),
            111 to (6505 to (6425 to 6584)),
            143 to (6665 to (6585 to 6744)),
            175 to (6825 to (6745 to 6904)),
            207 to (6985 to (6905 to 7065))
        )
        val frequencyCenter = frequencyRange.map { it.first to it.second.first }
        val frequencyOutOfRange = listOf(5169, 5731, 5732, 5733, 5734, 5896, 5944, 7066)
        // execute & validate
        calculateCenterUsingCenter1(frequencyCenter, calculateCenter160)
        calculateCenterUsingCenter0(frequencyCenter, calculateCenter160)
        calculateCenterUsingPrimary(frequencyCenter, calculateCenter160)
        calculateCenterUsingRange(frequencyRange, calculateCenter160, frequencyCenter.map { it.second })
        calculateCenterUsingOutOfRange(frequencyOutOfRange, calculateCenter160)
    }

    @Test
    fun calculateCenter320() {
        // setup
        val frequencyRange = listOf(
            31 to (6100 to (5945 to 6264)),
            95 to (6430 to (6265 to 6584)),
            159 to (6750 to (6585 to 6904)),
            191 to (6910 to (6905 to 7065))
        )
        val frequencyCenter = listOf(31 to 6100, 63 to 6270, 95 to 6430, 127 to 6590, 159 to 6750, 191 to 6910)
        val frequencyOutOfRange = listOf(5944, 7066)
        // execute & validate
        calculateCenterUsingCenter1(frequencyCenter, calculateCenter320)
        calculateCenterUsingCenter0(frequencyCenter, calculateCenter320)
        calculateCenterUsingPrimary(frequencyCenter, calculateCenter320)
        calculateCenterUsingRange(frequencyRange, calculateCenter320, frequencyCenter.map { it.second })
        calculateCenterUsingOutOfRange(frequencyOutOfRange, calculateCenter320)
    }

    /**
     * parameters:
     * frequencyCenter: list of channel and frequency center
     * calculateCenter: function to calculate center
     */
    private fun calculateCenterUsingCenter1(frequencyCenter: List<Pair<Int, Int>>, calculateCenter: CalculateCenter) {
        frequencyCenter.forEach { (channel, expected) ->
            // execute
            val actualCenter1 = calculateCenter(0, 0, expected)
            // validate
            assertThat(actualCenter1).describedAs("channel: $channel | frequency: $expected").isEqualTo(expected)
        }
    }

    /**
     * parameters:
     * frequencyCenter: list of channel and frequency center
     * calculateCenter: function to calculate center
     */
    private fun calculateCenterUsingCenter0(frequencyCenter: List<Pair<Int, Int>>, calculateCenter: CalculateCenter) {
        frequencyCenter.forEach { (channel, expected) ->
            // execute
            val actualCenter0 = calculateCenter(0, expected, 0)
            // validate
            assertThat(actualCenter0).describedAs("channel: $channel | frequency: $expected").isEqualTo(expected)
        }
    }

    /**
     * parameters:
     * frequencyCenter: list of channel and frequency center
     * calculateCenter: function to calculate center
     */
    private fun calculateCenterUsingPrimary(frequencyCenter: List<Pair<Int, Int>>, calculateCenter: CalculateCenter) {
        frequencyCenter.forEach { (channel, expected) ->
            // execute
            val actualPrimary = calculateCenter(expected, 0, 0)
            // validate
            assertThat(actualPrimary).describedAs("channel: $channel | frequency: $expected").isEqualTo(expected)
        }
    }

    /**
     * parameters:
     * frequencyRange: list of channel, frequency center, frequency range
     * calculateCenter: function to calculate center
     * frequencyCenter: list of frequency center
     */
    private fun calculateCenterUsingRange(
        frequencyRange: List<Pair<Int, Pair<Int, Pair<Int, Int>>>>,
        calculateCenter: CalculateCenter,
        frequencyCenter: List<Int>
    ) {
        frequencyRange.forEach { (channel, frequencyAndRange) ->
            val (expected, range) = frequencyAndRange
            (range.first..range.second)
                .filter { it !in frequencyCenter }
                .forEach {
                    // execute
                    val actualPrimary = calculateCenter(it, 0, 0)
                    // validate
                    assertThat(actualPrimary).describedAs("channel: $channel | frequency: $it").isEqualTo(expected)
                }
        }
    }

    /**
     * parameters:
     * outOfRangeFrequencies: list of out of range frequencies
     * calculateCenter: function to calculate center
     */
    private fun calculateCenterUsingOutOfRange(outOfRangeFrequencies: List<Int>, calculateCenter: CalculateCenter) {
        outOfRangeFrequencies.forEach { frequency ->
            // setup
            val expected = 111
            // execute
            val actualPrimary = calculateCenter(frequency, 0, expected)
            // validate
            assertThat(actualPrimary).describedAs("frequency: $frequency").isEqualTo(expected)
        }
    }
}