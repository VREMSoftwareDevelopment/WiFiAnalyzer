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
package com.vrem.wifianalyzer.wifi.graphutils

import com.jjoe64.graphview.series.BaseSeries
import com.vrem.wifianalyzer.wifi.model.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verifyNoMoreInteractions

class SeriesCacheTest {
    private val series1: BaseSeries<GraphDataPoint> = mock()
    private val series2: BaseSeries<GraphDataPoint> = mock()
    private val series3: BaseSeries<GraphDataPoint> = mock()
    private val series = listOf(series1, series2, series3)
    private val fixture = SeriesCache()

    @After
    fun tearDown() {
        verifyNoMoreInteractions(series1)
        verifyNoMoreInteractions(series2)
        verifyNoMoreInteractions(series3)
    }

    @Test
    fun contains() {
        // setup
        val wiFiDetails = withData()
        // execute
        val actual = fixture.contains(wiFiDetails[0])
        // validate
        assertTrue(actual)
    }

    @Test
    fun get() {
        // setup
        val wiFiDetails = withData()
        // execute & validate
        for (i in series.indices) {
            val wiFiDetail = wiFiDetails[i]
            assertEquals(series[i], fixture[wiFiDetail])
        }
    }

    @Test
    fun addExistingSeries() {
        // setup
        val wiFiDetails = withData()
        // execute
        val actual = fixture.put(wiFiDetails[0], series2)
        // validate
        assertEquals(series1, actual)
        assertEquals(series2, fixture[wiFiDetails[0]])
    }

    @Test
    fun tesDifferenceExpectOneLess() {
        // setup
        val expected = withData()
        // execute
        val actual = fixture.difference(expected.subList(0, 1).toSet())
        // validate
        assertEquals(expected.size - 1, actual.size)
        for (i in 1 until expected.size) {
            assertEquals(expected[i], actual[i - 1])
        }
    }

    @Test
    fun tesDifferenceExpectEverything() {
        // setup
        val expected = withData()
        // execute
        val actual = fixture.difference(setOf())
        // validate
        assertEquals(expected.size, actual.size)
        for (i in expected.indices) {
            assertEquals(expected[i], actual[i])
        }
    }

    @Test
    fun tesDifferenceExpectNone() {
        // setup
        val expected = withData()
        // execute
        val actual = fixture.difference(expected.toSet())
        // validate
        assertTrue(actual.isEmpty())
    }

    @Test
    fun removeExpectedAllLeft() {
        // setup
        val expected = withData()
        // execute
        val actual = fixture.remove(listOf())
        // validate
        assertTrue(actual.isEmpty())
        expected.forEach { assertTrue(fixture.contains(it)) }
    }

    @Test
    fun removeExpectNoneLeft() {
        // setup
        val expected = withData()
        // execute
        val actual = fixture.remove(expected)
        // validate
        assertEquals(expected.size, actual.size)
        expected.forEach { assertFalse(fixture.contains(it)) }
    }

    @Test
    fun removeExpectOneLeft() {
        // setup
        val expected = withData()
        // execute
        val actual = fixture.remove(expected.subList(1, expected.size))
        // validate
        assertEquals(2, actual.size)
        for (i in 1 until expected.size) {
            assertTrue(series.contains(actual[i - 1]))
            assertFalse(fixture.contains(expected[i]))
        }
        assertTrue(fixture.contains(expected[0]))
    }

    @Test
    fun removeNonExistingOne() {
        // setup
        val expected = withData()
        val toRemove = listOf(makeWiFiDetail("SSID-999"))
        // execute
        val actual = fixture.remove(toRemove)
        // validate
        assertTrue(actual.isEmpty())
        expected.forEach { assertTrue(fixture.contains(it)) }
    }

    @Test
    fun removeExpectMoreThanOneLeft() {
        // setup
        val expected = withData()
        // execute
        val actual = fixture.remove(expected.subList(0, 1))
        // validate
        assertEquals(1, actual.size)
        assertTrue(series.contains(actual[0]))
        for (i in 1 until expected.size) {
            assertTrue(fixture.contains(expected[i]))
        }
        assertFalse(fixture.contains(expected[0]))
    }

    @Test
    fun find() {
        // setup
        val wiFiDetails = withData()
        // execute
        val actual = fixture.find(series2)
        // validate
        assertEquals(wiFiDetails[1], actual)
    }

    private fun makeWiFiDetail(ssid: String): WiFiDetail {
        return WiFiDetail(
            WiFiIdentifier(ssid, "BSSID"),
            WiFiSecurity.EMPTY,
            WiFiSignal(100, 100, WiFiWidth.MHZ_20, 5)
        )
    }

    private fun withData(): List<WiFiDetail> {
        val results: MutableList<WiFiDetail> = mutableListOf()
        for (i in series.indices) {
            val wiFiDetail = makeWiFiDetail("SSID$i")
            results.add(wiFiDetail)
            fixture.put(wiFiDetail, series[i])
        }
        return results
    }

}