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
package com.vrem.wifianalyzer.wifi.timegraph

import com.vrem.util.EMPTY
import com.vrem.wifianalyzer.wifi.graphutils.MAX_NOT_SEEN_COUNT
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import com.vrem.wifianalyzer.wifi.model.WiFiIdentifier
import com.vrem.wifianalyzer.wifi.model.WiFiSignal
import com.vrem.wifianalyzer.wifi.model.WiFiWidth
import org.junit.Assert.*
import org.junit.Test

class TimeGraphCacheTest {
    private val fixture = TimeGraphCache()

    @Test
    fun testAll() {
        // setup
        val expected = withWiFiDetails()
        // execute
        val actual = fixture.wiFiDetails
        // validate
        assertEquals(expected.size, actual.size)
    }

    @Test
    fun testActive() {
        // setup
        val expected = withWiFiDetails()
        // execute
        val actual = fixture.active()
        // validate
        assertEquals(expected.size - 1, actual.size)
        assertFalse(actual.contains(expected[0]))
    }

    @Test
    fun testClear() {
        // setup
        val expected = withWiFiDetails()
        // execute
        fixture.clear()
        // validate
        val actual = fixture.wiFiDetails
        assertEquals(expected.size - 1, actual.size)
        assertFalse(actual.contains(expected[0]))
    }

    @Test
    fun testReset() {
        // setup
        val expected = withWiFiDetails()
        // execute
        fixture.reset(expected[0])
        // validate
        val actual = fixture.wiFiDetails
        assertEquals(expected.size, actual.size)
        assertTrue(actual.contains(expected[0]))
    }

    private fun withWiFiDetail(SSID: String): WiFiDetail {
        return WiFiDetail(
                WiFiIdentifier(SSID, "BSSID"),
                String.EMPTY,
                WiFiSignal(100, 100, WiFiWidth.MHZ_20, 5, true))
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