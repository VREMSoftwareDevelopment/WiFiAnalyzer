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
package com.vrem.wifianalyzer.wifi.band

import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.wifi.band.WiFiBand.Companion.find
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

class WiFiBandTest {
    private val wiFiManagerWrapper = MainContextHelper.INSTANCE.wiFiManagerWrapper

    @After
    fun tearDown() {
        MainContextHelper.INSTANCE.restore()
        verifyNoMoreInteractions(wiFiManagerWrapper)
    }


    @Test
    fun wiFiBand() {
        assertThat(WiFiBand.entries).hasSize(3)
    }

    @Test
    fun available() {
        assertThat(WiFiBand.GHZ2.available.javaClass.isInstance(availableGHZ2)).isTrue()
        assertThat(WiFiBand.GHZ5.available.javaClass.isInstance(availableGHZ5)).isTrue()
        assertThat(WiFiBand.GHZ6.available.javaClass.isInstance(availableGHZ6)).isTrue()
    }

    @Test
    fun textResource() {
        assertThat(WiFiBand.GHZ2.textResource).isEqualTo(R.string.wifi_band_2ghz)
        assertThat(WiFiBand.GHZ5.textResource).isEqualTo(R.string.wifi_band_5ghz)
        assertThat(WiFiBand.GHZ6.textResource).isEqualTo(R.string.wifi_band_6ghz)
    }

    @Test
    fun ghz5() {
        assertThat(WiFiBand.GHZ2.ghz5).isFalse()
        assertThat(WiFiBand.GHZ5.ghz5).isTrue()
        assertThat(WiFiBand.GHZ6.ghz5).isFalse()
    }

    @Test
    fun ghz2() {
        assertThat(WiFiBand.GHZ2.ghz2).isTrue()
        assertThat(WiFiBand.GHZ5.ghz2).isFalse()
        assertThat(WiFiBand.GHZ6.ghz2).isFalse()
    }

    @Test
    fun ghz6() {
        assertThat(WiFiBand.GHZ2.ghz6).isFalse()
        assertThat(WiFiBand.GHZ5.ghz6).isFalse()
        assertThat(WiFiBand.GHZ6.ghz6).isTrue()
    }

    @Test
    fun wiFiBandFind() {
        assertThat(find(2399)).isEqualTo(WiFiBand.GHZ2)
        assertThat(find(2400)).isEqualTo(WiFiBand.GHZ2)
        assertThat(find(2499)).isEqualTo(WiFiBand.GHZ2)
        assertThat(find(2500)).isEqualTo(WiFiBand.GHZ2)

        assertThat(find(4899)).isEqualTo(WiFiBand.GHZ2)
        assertThat(find(4900)).isEqualTo(WiFiBand.GHZ5)
        assertThat(find(5899)).isEqualTo(WiFiBand.GHZ5)
        assertThat(find(5900)).isEqualTo(WiFiBand.GHZ2)

        assertThat(find(5924)).isEqualTo(WiFiBand.GHZ2)
        assertThat(find(5925)).isEqualTo(WiFiBand.GHZ6)
        assertThat(find(7125)).isEqualTo(WiFiBand.GHZ6)
        assertThat(find(7126)).isEqualTo(WiFiBand.GHZ2)
    }

    @Test
    fun availableGHZ2() {
        // execute
        val actual = WiFiBand.GHZ2.available()
        // validate
        assertThat(actual).isTrue()
    }

    @Test
    fun availableGHZ5() {
        // setup
        whenever(wiFiManagerWrapper.is5GHzBandSupported()).thenReturn(true)
        // execute
        val actual = WiFiBand.GHZ5.available()
        // validate
        assertThat(actual).isTrue()
        verify(wiFiManagerWrapper).is5GHzBandSupported()
    }

    @Test
    fun availableGHZ6() {
        // setup
        whenever(wiFiManagerWrapper.is6GHzBandSupported()).thenReturn(true)
        // execute
        val actual = WiFiBand.GHZ6.available()
        // validate
        assertThat(actual).isTrue()
        verify(wiFiManagerWrapper).is6GHzBandSupported()
    }

}