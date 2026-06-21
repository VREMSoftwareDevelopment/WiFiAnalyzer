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
package com.vrem.wifianalyzer.wifi.channelgraph

import com.patrykandpatrick.vico.views.common.data.ExtraStore
import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.wifi.graphutils.MIN_Y
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

class ChannelLayerRangeProviderTest {
    private val mainActivity = MainContextHelper.INSTANCE.mainActivity
    private val settings = MainContextHelper.INSTANCE.settings
    private val extraStore: ExtraStore = mock()
    private val minX = 100.0
    private val maxX = 200.0
    private val fixture = ChannelLayerRangeProvider(minX, maxX)

    @After
    fun tearDown() {
        MainContextHelper.INSTANCE.restore()
        verifyNoMoreInteractions(mainActivity, settings, extraStore)
    }

    @Test
    fun getMinXReturnsProvidedMinX() {
        // Act
        val actual = fixture.getMinX(minX = -999.0, maxX = 0.0, extraStore = extraStore)
        // Assert
        assertThat(actual).isEqualTo(minX)
    }

    @Test
    fun getMaxXReturnsProvidedMaxX() {
        // Act
        val actual = fixture.getMaxX(minX = 0.0, maxX = 999.0, extraStore = extraStore)
        // Assert
        assertThat(actual).isEqualTo(maxX)
    }

    @Test
    fun getMinYReturnsMinYIgnoringInputs() {
        // Act
        val actual = fixture.getMinY(minY = -123.0, maxY = 45.0, extraStore = extraStore)
        // Assert
        assertThat(actual).isEqualTo(MIN_Y.toDouble())
    }

    @Test
    fun getMaxYReturnsDefaultWhenSettingsNotAvailable() {
        // Arrange
        val expected = 50
        doReturn(expected).whenever(settings).graphMaximumY()
        // Act
        val actual = fixture.getMaxY(minY = -123.0, maxY = 45.0, extraStore = extraStore)
        // Assert
        assertThat(actual).isEqualTo(expected.toDouble())
        verify(settings).graphMaximumY()
    }
}
