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
package com.vrem.wifianalyzer.wifi.timegraph

import com.patrykandpatrick.vico.views.common.data.ExtraStore
import com.vrem.wifianalyzer.wifi.graphutils.MAX_SCAN_COUNT
import com.vrem.wifianalyzer.wifi.graphutils.MAX_Y_DEFAULT
import com.vrem.wifianalyzer.wifi.graphutils.MIN_Y
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.mock

private const val NUM_X_TIME = 21

class TimeLayerRangeProviderTest {
    private val extraStore: ExtraStore = mock()
    private val fixture = TimeLayerRangeProvider()

    @Test
    fun getMinXBelowThresholdWithEvenMinXIsUnchanged() {
        // Act
        val actual = fixture.getMinX(minX = 0.0, maxX = NUM_X_TIME.toDouble(), extraStore = extraStore)
        // Assert
        assertThat(actual).isEqualTo(0.0)
    }

    @Test
    fun getMinXBelowThresholdWithOddMinXSnapsDownToEven() {
        // Act
        val actual = fixture.getMinX(minX = 7.0, maxX = NUM_X_TIME.toDouble(), extraStore = extraStore)
        // Assert
        assertThat(actual).isEqualTo(6.0)
    }

    @Test
    fun getMinXAboveThresholdClampsToMaxXMinusMaxScanCount() {
        // Act
        val actual = fixture.getMinX(minX = 0.0, maxX = (MAX_SCAN_COUNT + 50).toDouble(), extraStore = extraStore)
        // Assert
        assertThat(actual).isEqualTo(50.0)
    }

    @Test
    fun getMinXAboveThresholdWithOddClampedValueSnapsDownToEven() {
        // Act
        val actual = fixture.getMinX(minX = 0.0, maxX = (MAX_SCAN_COUNT + 51).toDouble(), extraStore = extraStore)
        // Assert
        assertThat(actual).isEqualTo(50.0)
    }

    @Test
    fun getMinXUsesClampedValueWhenMinXLessThanMaxXMinusMaxScanCount() {
        // Act
        val actual = fixture.getMinX(minX = 10.0, maxX = (MAX_SCAN_COUNT + 50).toDouble(), extraStore = extraStore)
        // Assert
        assertThat(actual).isEqualTo(50.0)
    }

    @Test
    fun getMinXUsesMinXWhenGreaterThanMaxXMinusMaxScanCount() {
        // Act
        val actual = fixture.getMinX(minX = 60.0, maxX = (MAX_SCAN_COUNT + 50).toDouble(), extraStore = extraStore)
        // Assert
        assertThat(actual).isEqualTo(60.0)
    }

    @Test
    fun getMaxXReturnsMaxXWhenAboveNumXTime() {
        // Act
        val actual = fixture.getMaxX(minX = 0.0, maxX = (NUM_X_TIME + 10).toDouble(), extraStore = extraStore)
        // Assert
        assertThat(actual).isEqualTo((NUM_X_TIME + 10).toDouble())
    }

    @Test
    fun getMaxXReturnsMaxXWhenEqualToNumXTime() {
        // Act
        val actual = fixture.getMaxX(minX = 0.0, maxX = NUM_X_TIME.toDouble(), extraStore = extraStore)
        // Assert
        assertThat(actual).isEqualTo(NUM_X_TIME.toDouble())
    }

    @Test
    fun getMaxXCoercesUpToNumXTimeWhenBelow() {
        // Act
        val actual = fixture.getMaxX(minX = 0.0, maxX = (NUM_X_TIME - 5).toDouble(), extraStore = extraStore)
        // Assert
        assertThat(actual).isEqualTo(NUM_X_TIME.toDouble())
    }

    @Test
    fun getMinYReturnsMinYIgnoringInputs() {
        // Act
        val actual = fixture.getMinY(minY = -123.0, maxY = 45.0, extraStore = extraStore)
        // Assert
        assertThat(actual).isEqualTo(MIN_Y.toDouble())
    }

    @Test
    fun getMaxYReturnsMaxYDefaultIgnoringInputs() {
        // Act
        val actual = fixture.getMaxY(minY = -123.0, maxY = 45.0, extraStore = extraStore)
        // Assert
        assertThat(actual).isEqualTo(MAX_Y_DEFAULT.toDouble())
    }
}
