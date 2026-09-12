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
package com.vrem.wifianalyzer.wifi.band

import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.wifi.band.WiFiBand.Companion.find
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

class WiFiBandTest {
    private val wiFiManagerWrapper = MainContextHelper.INSTANCE.wiFiManagerWrapper

    @After
    fun tearDown() {
        verifyNoMoreInteractions(wiFiManagerWrapper)
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun wiFiBand() {
        assertThat(WiFiBand.entries)
            .hasSize(3)
            .containsExactly(WiFiBand.GHZ2, WiFiBand.GHZ5, WiFiBand.GHZ6)
    }

    @Test
    fun available() {
        assertThat(WiFiBand.GHZ2.available as Any).isSameAs(availableGHZ2)
        assertThat(WiFiBand.GHZ5.available as Any).isSameAs(availableGHZ5)
        assertThat(WiFiBand.GHZ6.available as Any).isSameAs(availableGHZ6)
    }

    @Test
    fun textResource() {
        assertThat(WiFiBand.GHZ2.textResource).isEqualTo(R.string.wifi_band_2ghz)
        assertThat(WiFiBand.GHZ5.textResource).isEqualTo(R.string.wifi_band_5ghz)
        assertThat(WiFiBand.GHZ6.textResource).isEqualTo(R.string.wifi_band_6ghz)
    }

    @Test
    fun wiFiChannels() {
        assertThat(WiFiBand.GHZ2.wiFiChannels).isEqualTo(wiFiChannelsGHZ2)
        assertThat(WiFiBand.GHZ5.wiFiChannels).isEqualTo(wiFiChannelsGHZ5)
        assertThat(WiFiBand.GHZ6.wiFiChannels).isEqualTo(wiFiChannelsGHZ6)
    }

    @Test
    fun wiFiBandOrdinal() {
        assertThat(WiFiBand.GHZ2.ordinal).isEqualTo(0)
        assertThat(WiFiBand.GHZ5.ordinal).isEqualTo(1)
        assertThat(WiFiBand.GHZ6.ordinal).isEqualTo(2)
    }

    @Test
    fun ghz5() {
        assertThat(WiFiBand.GHZ2.ghz5).isFalse
        assertThat(WiFiBand.GHZ5.ghz5).isTrue
        assertThat(WiFiBand.GHZ6.ghz5).isFalse
    }

    @Test
    fun ghz2() {
        assertThat(WiFiBand.GHZ2.ghz2).isTrue
        assertThat(WiFiBand.GHZ5.ghz2).isFalse
        assertThat(WiFiBand.GHZ6.ghz2).isFalse
    }

    @Test
    fun ghz6() {
        assertThat(WiFiBand.GHZ2.ghz6).isFalse
        assertThat(WiFiBand.GHZ5.ghz6).isFalse
        assertThat(WiFiBand.GHZ6.ghz6).isTrue
    }

    @Test
    fun findGHZ2() {
        assertThat(find(WiFiBand.GHZ2.wiFiChannels.channelRange.first.frequency - 1)).isEqualTo(WiFiBand.GHZ2)
        assertThat(find(WiFiBand.GHZ2.wiFiChannels.channelRange.first.frequency)).isEqualTo(WiFiBand.GHZ2)
        assertThat(find(WiFiBand.GHZ2.wiFiChannels.channelRange.second.frequency)).isEqualTo(WiFiBand.GHZ2)
        assertThat(find(WiFiBand.GHZ2.wiFiChannels.channelRange.second.frequency + 1)).isEqualTo(WiFiBand.GHZ2)
    }

    @Test
    fun findGHZ5() {
        assertThat(find(WiFiBand.GHZ5.wiFiChannels.channelRange.first.frequency - 1)).isEqualTo(WiFiBand.GHZ2)
        assertThat(find(WiFiBand.GHZ5.wiFiChannels.channelRange.first.frequency)).isEqualTo(WiFiBand.GHZ5)
        assertThat(find(WiFiBand.GHZ5.wiFiChannels.channelRange.second.frequency)).isEqualTo(WiFiBand.GHZ5)
        assertThat(find(WiFiBand.GHZ5.wiFiChannels.channelRange.second.frequency + 1)).isEqualTo(WiFiBand.GHZ2)
    }

    @Test
    fun findGHZ6() {
        assertThat(find(WiFiBand.GHZ6.wiFiChannels.channelRange.first.frequency - 1)).isEqualTo(WiFiBand.GHZ2)
        assertThat(find(WiFiBand.GHZ6.wiFiChannels.channelRange.first.frequency)).isEqualTo(WiFiBand.GHZ6)
        assertThat(find(WiFiBand.GHZ6.wiFiChannels.channelRange.second.frequency)).isEqualTo(WiFiBand.GHZ6)
        assertThat(find(WiFiBand.GHZ6.wiFiChannels.channelRange.second.frequency + 1)).isEqualTo(WiFiBand.GHZ2)
    }

    @Test
    fun availableGHZ2() {
        // Act
        val actual = WiFiBand.GHZ2.available()
        // Assert
        assertThat(actual).isTrue
    }

    @Test
    fun bandSupportGHZ5() {
        // Arrange
        whenever(wiFiManagerWrapper.is5GHzBandSupported()).thenReturn(true)
        // Act
        val actual = bandSupportGHZ5 { wiFiManagerWrapper }()
        // Assert
        assertThat(actual).isTrue
        verify(wiFiManagerWrapper).is5GHzBandSupported()
    }

    @Test
    fun bandSupportGHZ6() {
        // Arrange
        whenever(wiFiManagerWrapper.is6GHzBandSupported()).thenReturn(true)
        // Act
        val actual = bandSupportGHZ6 { wiFiManagerWrapper }()
        // Assert
        assertThat(actual).isTrue
        verify(wiFiManagerWrapper).is6GHzBandSupported()
    }

    @Test
    fun availableGHZ5() {
        // Arrange
        doReturn(true).whenever(wiFiManagerWrapper).is5GHzBandSupported()
        // Act
        val actual = WiFiBand.GHZ5.available()
        // Assert
        assertThat(actual).isTrue
        verify(wiFiManagerWrapper).is5GHzBandSupported()
    }

    @Test
    fun availableGHZ6() {
        // Arrange
        doReturn(true).whenever(wiFiManagerWrapper).is6GHzBandSupported()
        // Act
        val actual = WiFiBand.GHZ6.available()
        // Assert
        assertThat(actual).isTrue
        verify(wiFiManagerWrapper).is6GHzBandSupported()
    }
}
