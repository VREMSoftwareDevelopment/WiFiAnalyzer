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
package com.vrem.wifianalyzer.wifi.timegraph

import com.vrem.wifianalyzer.wifi.graphutils.MAX_NOT_SEEN_COUNT
import com.vrem.wifianalyzer.wifi.model.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class TimeGraphCacheTest {
    private val fixture = TimeGraphCache()

    @Test
    fun all() {
        // setup
        val expected = withWiFiDetails()
        // execute
        val actual = fixture.wiFiDetails
        // validate
        assertThat(actual).hasSize(expected.size)
    }

    @Test
    fun active() {
        // setup
        val expected = withWiFiDetails()
        // execute
        val actual = fixture.active()
        // validate
        assertThat(actual).hasSize(expected.size - 1)
        assertThat(actual).doesNotContain(expected[0])
    }

    @Test
    fun clear() {
        // setup
        val expected = withWiFiDetails()
        // execute
        fixture.clear()
        // validate
        val actual = fixture.wiFiDetails
        assertThat(actual).hasSize(expected.size - 1)
        assertThat(actual).doesNotContain(expected[0])
    }

    @Test
    fun reset() {
        // setup
        val expected = withWiFiDetails()
        // execute
        fixture.reset(expected[0])
        // validate
        val actual = fixture.wiFiDetails
        assertThat(actual).hasSize(expected.size)
        assertThat(actual).contains(expected[0])
    }

    private fun withWiFiDetail(ssid: String): WiFiDetail {
        return WiFiDetail(
            WiFiIdentifier(ssid, "BSSID"),
            WiFiSecurity.EMPTY,
            WiFiSignal(100, 100, WiFiWidth.MHZ_20, 5)
        )
    }

    private fun withWiFiDetails(): List<WiFiDetail> {
        val results: MutableList<WiFiDetail> = mutableListOf()
        for (i in 0..3) {
            val wiFiDetail = withWiFiDetail("SSID$i")
            results.add(wiFiDetail)
        }
        results.forEach { fixture.add(it) }
        for (i in 0 until MAX_NOT_SEEN_COUNT) {
            fixture.add(results[0])
        }
        return results
    }

}