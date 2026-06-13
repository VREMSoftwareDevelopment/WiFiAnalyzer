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
package com.vrem.wifianalyzer.wifi.graphutils

import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import com.vrem.wifianalyzer.wifi.model.WiFiIdentifier
import com.vrem.wifianalyzer.wifi.model.WiFiSecurity
import com.vrem.wifianalyzer.wifi.model.WiFiSignal
import com.vrem.wifianalyzer.wifi.model.WiFiWidth
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SeriesCacheTest {
    private val seriesData1 = SeriesData()
    private val seriesData2 = SeriesData()
    private val seriesData3 = SeriesData()
    private val seriesList = listOf(seriesData1, seriesData2, seriesData3)
    private val fixture = SeriesCache(emptyList())

    @Test
    fun contains() {
        // Arrange
        val wiFiDetails = withData()
        // Act
        val actual = fixture.contains(wiFiDetails[0])
        // Assert
        assertThat(actual).isTrue
    }

    @Test
    fun get() {
        // Arrange
        val wiFiDetails = withData()
        // Act
        val actual = wiFiDetails.map { fixture[it] }
        // Assert
        assertThat(actual).containsExactlyElementsOf(seriesList)
    }

    @Test
    fun getReturnsNullWhenAbsent() {
        // Act
        val actual = fixture[makeWiFiDetail("unknown")]
        // Assert
        assertThat(actual).isNull()
    }

    @Test
    fun addExistingSeries() {
        // Arrange
        val wiFiDetails = withData()
        // Act
        val actual = fixture.put(wiFiDetails[0], seriesData2)
        // Assert
        assertThat(actual).isEqualTo(seriesData1)
        assertThat(fixture[wiFiDetails[0]]).isEqualTo(seriesData2)
    }

    @Test
    fun differenceExpectOneLess() {
        // Arrange
        val expected = withData()
        // Act
        val actual = fixture.difference(expected.subList(0, 1).toSet())
        // Assert
        assertThat(actual).hasSize(expected.size - 1)
        for (i in 1 until expected.size) {
            assertThat(actual[i - 1]).isEqualTo(expected[i])
        }
    }

    @Test
    fun differenceExpectEverything() {
        // Arrange
        val expected = withData()
        // Act
        val actual = fixture.difference(setOf())
        // Assert
        assertThat(actual).hasSize(expected.size)
        for (i in expected.indices) {
            assertThat(actual[i]).isEqualTo(expected[i])
        }
    }

    @Test
    fun differenceExpectNone() {
        // Arrange
        val expected = withData()
        // Act
        val actual = fixture.difference(expected.toSet())
        // Assert
        assertThat(actual).isEmpty()
    }

    @Test
    fun removeExpectedAllLeft() {
        // Arrange
        val expected = withData()
        // Act
        val actual = fixture.remove(listOf())
        // Assert
        assertThat(actual).isEmpty()
        expected.forEach { assertThat(fixture.contains(it)).isTrue }
    }

    @Test
    fun removeExpectNoneLeft() {
        // Arrange
        val expected = withData()
        // Act
        val actual = fixture.remove(expected)
        // Assert
        assertThat(actual).hasSize(expected.size)
        expected.forEach { assertThat(fixture.contains(it)).isFalse }
    }

    @Test
    fun removeExpectOneLeft() {
        // Arrange
        val expected = withData()
        // Act
        val actual = fixture.remove(expected.subList(1, expected.size))
        // Assert
        assertThat(actual).hasSize(2)
        for (i in 1 until expected.size) {
            assertThat(seriesList).contains(actual[i - 1])
            assertThat(fixture.contains(expected[i])).isFalse
        }
        assertThat(fixture.contains(expected[0])).isTrue
    }

    @Test
    fun removeNonExistingOne() {
        // Arrange
        val expected = withData()
        val toRemove = listOf(makeWiFiDetail("SSID-999"))
        // Act
        val actual = fixture.remove(toRemove)
        // Assert
        assertThat(actual).isEmpty()
        expected.forEach { assertThat(fixture.contains(it)).isTrue }
    }

    @Test
    fun removeExpectMoreThanOneLeft() {
        // Arrange
        val expected = withData()
        // Act
        val actual = fixture.remove(expected.subList(0, 1))
        // Assert
        assertThat(actual).hasSize(1)
        assertThat(seriesList).contains(actual[0])
        for (i in 1 until expected.size) {
            assertThat(fixture.contains(expected[i])).isTrue
        }
        assertThat(fixture.contains(expected[0])).isFalse
    }

    @Test
    fun data() {
        // Arrange
        withData()
        // Act
        val actual = fixture.data()
        // Assert
        assertThat(actual).hasSize(seriesList.size + 1)
        assertThat(actual).containsAll(seriesList)
    }

    @Test
    fun dataPreservesInsertionOrder() {
        // Arrange
        withData()
        // Act
        val actual = fixture.data()
        // Assert
        assertThat(actual.drop(1)).containsExactlyElementsOf(seriesList)
    }

    @Test
    fun populatedEntriesExcludesEntriesWithEmptyDataPoints() {
        // Arrange
        val withPoints = SeriesData(listOf(DataPoint(1, -50)))
        val withoutPoints = SeriesData()
        val detailWithPoints = makeWiFiDetail("WITH")
        val detailWithoutPoints = makeWiFiDetail("WITHOUT")
        fixture.put(detailWithPoints, withPoints)
        fixture.put(detailWithoutPoints, withoutPoints)
        // Act
        val actual = fixture.populatedEntries()
        // Assert
        assertThat(actual).hasSize(1)
        assertThat(actual[0].key).isEqualTo(detailWithPoints)
        assertThat(actual[0].value).isEqualTo(withPoints)
    }

    @Test
    fun populatedEntriesReturnsEmptyWhenCacheEmpty() {
        // Act
        val actual = fixture.populatedEntries()
        // Assert
        assertThat(actual).isEmpty()
    }

    @Test
    fun populatedDataReturnsOnlySeriesWithDataPoints() {
        // Arrange
        val withPoints = SeriesData(listOf(DataPoint(1, -50)))
        val withoutPoints = SeriesData()
        fixture.put(makeWiFiDetail("WITH"), withPoints)
        fixture.put(makeWiFiDetail("WITHOUT"), withoutPoints)
        // Act
        val actual = fixture.populatedData()
        // Assert
        assertThat(actual).containsExactly(withPoints)
    }

    @Test
    fun entriesPreservesInsertionOrder() {
        // Arrange
        val wiFiDetails = withData()
        // Act
        val actual = fixture.entries().drop(1)
        // Assert
        assertThat(actual).hasSize(wiFiDetails.size)
        for (i in wiFiDetails.indices) {
            assertThat(actual[i].key).isEqualTo(wiFiDetails[i])
            assertThat(actual[i].value).isEqualTo(seriesList[i])
        }
    }

    private fun makeWiFiDetail(ssid: String): WiFiDetail =
        WiFiDetail(
            WiFiIdentifier(ssid, "BSSID"),
            WiFiSecurity.EMPTY,
            WiFiSignal(100, 100, WiFiWidth.MHZ_20, 5),
        )

    private fun withData(): List<WiFiDetail> {
        val results: MutableList<WiFiDetail> = mutableListOf()
        for (i in seriesList.indices) {
            val wiFiDetail = makeWiFiDetail("SSID$i")
            results.add(wiFiDetail)
            fixture.put(wiFiDetail, seriesList[i])
        }
        return results
    }
}
