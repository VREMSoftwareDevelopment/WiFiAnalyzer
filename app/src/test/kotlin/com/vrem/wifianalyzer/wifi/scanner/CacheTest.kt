/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2026 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import com.vrem.wifianalyzer.Configuration
import com.vrem.wifianalyzer.settings.Settings
import com.vrem.wifianalyzer.wifi.band.WiFiRange
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class CacheTest {
    private val scanResult1: ScanResult = mock()
    private val scanResult2: ScanResult = mock()
    private val scanResult3: ScanResult = mock()
    private val scanResult4: ScanResult = mock()
    private val scanResult5: ScanResult = mock()
    private val scanResult6: ScanResult = mock()
    private val settings: Settings = mock()
    private val configuration: Configuration = mock()
    private val fixture = Cache(settings, configuration)

    @Before
    fun setUp() {
        whenever(settings.scanSpeed()).thenReturn(5)
        whenever(settings.cacheOff()).thenReturn(false)
        whenever(configuration.sizeAvailable).thenReturn(true)
    }

    @Test
    fun addWithSizeAvailable() {
        // Arrange
        val scanResults = listOf<ScanResult>()
        // Act
        fixture.add(scanResults)
        // Assert
        assertThat(fixture.first()).isEqualTo(scanResults)
    }

    @Test
    fun addCompliesToMaxCacheSizeWithSizeAvailable() {
        // Arrange
        val cacheSize = 2
        val expected: MutableList<List<ScanResult>> = mutableListOf()
        // Act
        repeat(cacheSize) {
            val scanResults = listOf<ScanResult>()
            expected.add(scanResults)
            fixture.add(scanResults)
        }
        // Assert
        assertThat(expected).hasSize(cacheSize)
        assertThat(fixture.first()).isEqualTo(expected[1])
        assertThat(fixture.last()).isEqualTo(expected[0])
    }

    @Test
    fun scanResultsWithSizeAvailable() {
        // Arrange
        withScanResults()
        // Act
        val actual = fixture.scanResults()
        // Assert
        assertThat(actual).hasSize(3)
        validate(scanResult2, -25, actual[0])
        validate(scanResult5, -40, actual[1])
        validate(scanResult6, -10, actual[2])
    }

    @Test
    fun sizeWithSizeAvailable() {
        // Arrange
        val values: List<WiFiRange> =
            listOf(
                WiFiRange(1, 4),
                WiFiRange(2, 3),
                WiFiRange(4, 3),
                WiFiRange(5, 2),
                WiFiRange(9, 2),
                WiFiRange(10, 1),
                WiFiRange(20, 1),
            )
        // Act & Assert
        values.forEach {
            whenever(settings.scanSpeed()).thenReturn(it.first)
            assertThat(fixture.size()).isEqualTo(it.second)
        }
        verify(settings, times(values.size)).scanSpeed()
    }

    @Test
    fun addWithCacheOff() {
        // Arrange
        whenever(settings.cacheOff()).thenReturn(true)
        val scanResults = listOf<ScanResult>()
        // Act
        fixture.add(scanResults)
        // Assert
        assertThat(fixture.first()).isEqualTo(scanResults)
        verify(settings).cacheOff()
    }

    @Test
    fun addCompliesToMaxCacheSizeWhenCacheOff() {
        // Arrange
        val count = 2
        whenever(settings.cacheOff()).thenReturn(true)
        val expected: MutableList<List<ScanResult>> = mutableListOf()
        // Act
        repeat(count) {
            val scanResults = listOf<ScanResult>()
            expected.add(scanResults)
            fixture.add(scanResults)
        }
        // Assert
        assertThat(expected).hasSize(count)
        assertThat(fixture.first()).isEqualTo(expected[1])
        assertThat(fixture.last()).isEqualTo(expected[0])
        verify(settings, times(count)).cacheOff()
    }

    @Test
    fun scanResultsWhenSingleAndCacheOff() {
        // Arrange
        whenever(settings.cacheOff()).thenReturn(true)
        val count = withScanResults()
        // Act
        val actual = fixture.scanResults()
        // Assert
        assertThat(actual).hasSize(2)
        validate(scanResult3, -30, actual[0])
        validate(scanResult6, -10, actual[1])
        verify(settings, times(count)).cacheOff()
    }

    @Test
    fun scanResultsWhenMultipleAndCacheOff() {
        // Arrange
        whenever(settings.cacheOff()).thenReturn(true)
        val count = withScanResults() + withScanResults()
        // Act
        val actual = fixture.scanResults()
        // Assert
        assertThat(actual).hasSize(2)
        validate(scanResult3, -30, actual[0])
        validate(scanResult6, -10, actual[1])
        verify(settings, times(count)).cacheOff()
    }

    @Test
    fun sizeWhenCacheOff() {
        // Arrange
        val expected = 1
        whenever(settings.cacheOff()).thenReturn(true)
        // Act
        val actual = fixture.size()
        // Assert
        assertThat(actual).isEqualTo(expected)
        verify(settings).cacheOff()
    }

    @Test
    fun add() {
        // Arrange
        whenever(configuration.sizeAvailable).thenReturn(false)
        val scanResults = listOf<ScanResult>()
        // Act
        fixture.add(scanResults)
        // Assert
        assertThat(fixture.first()).isEqualTo(scanResults)
    }

    @Test
    fun addCompliesToMaxCacheSize() {
        // Arrange
        val cacheSize = 2
        whenever(configuration.sizeAvailable).thenReturn(false)
        val expected: MutableList<List<ScanResult>> = mutableListOf()
        // Act
        repeat(cacheSize) {
            val scanResults = listOf<ScanResult>()
            expected.add(scanResults)
            fixture.add(scanResults)
        }
        // Assert
        assertThat(expected).hasSize(cacheSize)
        assertThat(fixture.first()).isEqualTo(expected[1])
        assertThat(fixture.last()).isEqualTo(expected[0])
    }

    @Test
    fun scanResultsWhenSingle() {
        // Arrange
        whenever(configuration.sizeAvailable).thenReturn(false)
        withScanResults()
        // Act
        val actual = fixture.scanResults()
        // Assert
        assertThat(actual).hasSize(2)
        validate(scanResult3, -47, actual[0])
        validate(scanResult6, -27, actual[1])
    }

    @Test
    fun scanResultsWhenMultiple() {
        // Arrange
        whenever(configuration.sizeAvailable).thenReturn(false)
        withScanResults()
        withScanResults()
        // Act
        val actual = fixture.scanResults()
        // Assert
        assertThat(actual).hasSize(2)
        validate(scanResult3, -55, actual[0])
        validate(scanResult6, -35, actual[1])
    }

    @Test
    fun size() {
        // Arrange
        val expected = 1
        whenever(configuration.sizeAvailable).thenReturn(false)
        // Act
        val actual = fixture.size()
        // Assert
        assertThat(actual).isEqualTo(expected)
        verify(settings, never()).scanSpeed()
    }

    @Test
    fun cacheKey() {
        // Arrange
        val bssid = "BSSID"
        val ssid = "SSID"
        // Act
        val fixture = CacheKey(bssid, ssid)
        // Assert
        assertThat(fixture.bssid).isEqualTo(bssid)
        assertThat(fixture.ssid).isEqualTo(ssid)
    }

    @Test
    fun addResetsCountWhenMaximumIsReached() {
        // Arrange
        whenever(configuration.sizeAvailable).thenReturn(false)
        val scanResults = listOf(scanResult1)
        whenSsid(scanResult1, "SSID1")
        scanResult1.BSSID = "BSSID1"
        scanResult1.level = -50
        // Act & Assert
        repeat(10) {
            fixture.add(scanResults)
        }
        var actual = fixture.scanResults()
        assertThat(actual).hasSize(1)
        validate(scanResult1, -80, actual[0])
        // Act & Assert
        // call add again, count is reset to 2
        fixture.add(scanResults)
        actual = fixture.scanResults()
        assertThat(actual).hasSize(1)
        validate(scanResult1, -60, actual[0])
    }

    private fun validate(
        expectedScanResult: ScanResult,
        expectedLevel: Int,
        actual: CacheResult,
    ) {
        assertThat(actual.scanResult).isEqualTo(expectedScanResult)
        assertThat(actual.average).isEqualTo(expectedLevel)
    }

    private fun withScanResults(): Int {
        whenSsid(scanResult1, "SSID1")
        scanResult1.BSSID = "BSSID1"
        scanResult1.level = -10

        whenSsid(scanResult2, "SSID1")
        scanResult2.BSSID = "BSSID1"
        scanResult2.level = -20

        whenSsid(scanResult3, "SSID1")
        scanResult3.BSSID = "BSSID1"
        scanResult3.level = -30

        whenSsid(scanResult4, "SSID2")
        scanResult4.BSSID = "BSSID2"
        scanResult4.level = -60

        whenSsid(scanResult5, "SSID2")
        scanResult5.BSSID = "BSSID2"
        scanResult5.level = -40

        whenSsid(scanResult6, "SSID3")
        scanResult6.BSSID = "BSSID3"
        scanResult6.level = -10

        var result = 0
        fixture.add(listOf(scanResult1, scanResult4))
        result++
        fixture.add(listOf(scanResult2, scanResult5))
        result++
        fixture.add(listOf(scanResult3, scanResult6))
        result++

        return result
    }
}
