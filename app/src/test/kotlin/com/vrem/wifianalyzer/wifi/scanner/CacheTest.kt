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
package com.vrem.wifianalyzer.wifi.scanner

import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import com.nhaarman.mockitokotlin2.*
import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.wifi.band.WiFiRange
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CacheTest {
    private val wifiInfo: WifiInfo = mock()
    private val scanResult1: ScanResult = mock()
    private val scanResult2: ScanResult = mock()
    private val scanResult3: ScanResult = mock()
    private val scanResult4: ScanResult = mock()
    private val scanResult5: ScanResult = mock()
    private val scanResult6: ScanResult = mock()
    private val settings = MainContextHelper.INSTANCE.settings
    private val configuration = MainContextHelper.INSTANCE.configuration
    private val fixture = Cache()

    @Before
    fun setUp() {
        whenever(settings.scanSpeed()).thenReturn(5)
        whenever(configuration.sizeAvailable).thenReturn(true)
    }

    @After
    fun tearDown() {
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun testAddWithSizeAvailable() {
        // setup
        val scanResults = listOf<ScanResult>()
        // execute
        fixture.add(scanResults, wifiInfo)
        // validate
        assertEquals(scanResults, fixture.first())
    }

    @Test
    fun testAddCompliesToMaxCacheSizeWithSizeAvailable() {
        // setup
        val cacheSize = 2
        val expected: MutableList<List<ScanResult>> = mutableListOf()
        // execute
        for (i in 0 until cacheSize) {
            val scanResults = listOf<ScanResult>()
            expected.add(scanResults)
            fixture.add(scanResults, wifiInfo)
        }
        // validate
        assertEquals(cacheSize, expected.size)
        assertEquals(expected[cacheSize - 1], fixture.first())
        assertEquals(expected[cacheSize - 2], fixture.last())
    }

    @Test
    fun testScanResultsWithSizeAvailable() {
        // setup
        withScanResults()
        // execute
        val actual = fixture.scanResults()
        // validate
        assertEquals(3, actual.size)
        validate(scanResult2, -25, actual[0])
        validate(scanResult5, -40, actual[1])
        validate(scanResult6, -10, actual[2])
    }

    @Test
    fun testSizeWithSizeAvailable() {
        // setup
        val values: List<WiFiRange> = listOf(
                WiFiRange(1, 4),
                WiFiRange(2, 3),
                WiFiRange(4, 3),
                WiFiRange(5, 2),
                WiFiRange(9, 2),
                WiFiRange(10, 1),
                WiFiRange(20, 1)
        )
        // execute & validate
        values.forEach {
            whenever(settings.scanSpeed()).thenReturn(it.first)
            assertEquals("Scan Speed:" + it.first, it.second, fixture.size())
        }
        verify(settings, times(values.size)).scanSpeed()
    }

    @Test
    fun testAdd() {
        // setup
        whenever(configuration.sizeAvailable).thenReturn(false)
        val scanResults = listOf<ScanResult>()
        // execute
        fixture.add(scanResults, wifiInfo)
        // validate
        assertEquals(scanResults, fixture.first())
        assertEquals(wifiInfo, fixture.wifiInfo())
    }

    @Test
    fun testAddCompliesToMaxCacheSize() {
        // setup
        val cacheSize = 2
        whenever(configuration.sizeAvailable).thenReturn(false)
        val expected: MutableList<List<ScanResult>> = mutableListOf()
        // execute
        for (i in 0 until cacheSize) {
            val scanResults = listOf<ScanResult>()
            expected.add(scanResults)
            fixture.add(scanResults, wifiInfo)
        }
        // validate
        assertEquals(cacheSize, expected.size)
        assertEquals(expected[cacheSize - 1], fixture.first())
        assertEquals(expected[cacheSize - 2], fixture.last())
    }

    @Test
    fun testScanResultsWhenSingle() {
        // setup
        whenever(configuration.sizeAvailable).thenReturn(false)
        withScanResults()
        // execute
        val actual = fixture.scanResults()
        // validate
        assertEquals(2, actual.size)
        validate(scanResult3, -47, actual[0])
        validate(scanResult6, -27, actual[1])
    }

    @Test
    fun testScanResultsWhenMultiple() {
        // setup
        whenever(configuration.sizeAvailable).thenReturn(false)
        withScanResults()
        withScanResults()
        // execute
        val actual = fixture.scanResults()
        // validate
        assertEquals(2, actual.size)
        validate(scanResult3, -55, actual[0])
        validate(scanResult6, -35, actual[1])
    }

    @Test
    fun testSize() {
        // setup
        val expected = 1
        whenever(configuration.sizeAvailable).thenReturn(false)
        // execute
        val actual = fixture.size()
        // validate
        assertEquals(expected, actual)
        verify(settings, never()).scanSpeed()
    }

    private fun validate(expectedScanResult: ScanResult, expectedLevel: Int, actual: CacheResult) {
        assertEquals(expectedScanResult, actual.scanResult)
        assertEquals(expectedLevel, actual.average)
    }

    private fun withScanResults() {
        scanResult1.SSID = "SSID1"
        scanResult1.BSSID = "BSSID1"
        scanResult1.level = -10

        scanResult2.SSID = "SSID1"
        scanResult2.BSSID = "BSSID1"
        scanResult2.level = -20

        scanResult3.SSID = "SSID1"
        scanResult3.BSSID = "BSSID1"
        scanResult3.level = -30

        scanResult4.SSID = "SSID2"
        scanResult4.BSSID = "BSSID2"
        scanResult4.level = -60

        scanResult5.SSID = "SSID2"
        scanResult5.BSSID = "BSSID2"
        scanResult5.level = -40

        scanResult6.SSID = "SSID3"
        scanResult6.BSSID = "BSSID3"
        scanResult6.level = -10

        fixture.add(listOf(scanResult1, scanResult4), wifiInfo)
        fixture.add(listOf(scanResult2, scanResult5), wifiInfo)
        fixture.add(listOf(scanResult3, scanResult6), wifiInfo)
    }
}