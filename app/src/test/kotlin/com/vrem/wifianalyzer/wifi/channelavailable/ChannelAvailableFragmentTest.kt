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
package com.vrem.wifianalyzer.wifi.channelavailable

import android.os.Build
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.MainContextHelper.INSTANCE
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.model.WiFiWidth
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config
import java.util.Locale

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.VANILLA_ICE_CREAM])
class ChannelAvailableFragmentTest {
    @Suppress("unused")
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val countryCode = Locale.JAPAN.country
    private val settings = INSTANCE.settings
    private val fixture = ChannelAvailableFragment()

    @Before
    fun setUp() {
        whenever(settings.countryCode()).thenReturn(countryCode)
    }

    @After
    fun tearDown() {
        verify(settings, atLeastOnce()).countryCode()
        INSTANCE.restore()
    }

    @Test
    fun onCreateView() {
        // setup
        RobolectricUtil.INSTANCE.startFragment(fixture)
        // validate
        assertThat(fixture).isNotNull()
        val view = fixture.view!!
        assertThat(view.findViewById<TextView>(R.id.channels_available_2GHz_20MHz).text)
            .isEqualTo(convert(WiFiBand.GHZ2, WiFiWidth.MHZ_20))
        assertThat(view.findViewById<TextView>(R.id.channels_available_2GHz_40MHz).text)
            .isEqualTo(convert(WiFiBand.GHZ2, WiFiWidth.MHZ_40))
        assertThat(view.findViewById<TextView>(R.id.channels_available_5GHz_20MHz).text)
            .isEqualTo(convert(WiFiBand.GHZ5, WiFiWidth.MHZ_20))
        assertThat(view.findViewById<TextView>(R.id.channels_available_5GHz_40MHz).text)
            .isEqualTo(convert(WiFiBand.GHZ5, WiFiWidth.MHZ_40))
        assertThat(view.findViewById<TextView>(R.id.channels_available_5GHz_80MHz).text)
            .isEqualTo(convert(WiFiBand.GHZ5, WiFiWidth.MHZ_80))
        assertThat(view.findViewById<TextView>(R.id.channels_available_5GHz_160MHz).text)
            .isEqualTo(convert(WiFiBand.GHZ5, WiFiWidth.MHZ_160))
        assertThat(view.findViewById<TextView>(R.id.channels_available_6GHz_20MHz).text)
            .isEqualTo(convert(WiFiBand.GHZ6, WiFiWidth.MHZ_20))
        assertThat(view.findViewById<TextView>(R.id.channels_available_6GHz_40MHz).text)
            .isEqualTo(convert(WiFiBand.GHZ6, WiFiWidth.MHZ_40))
        assertThat(view.findViewById<TextView>(R.id.channels_available_6GHz_80MHz).text)
            .isEqualTo(convert(WiFiBand.GHZ6, WiFiWidth.MHZ_80))
        assertThat(view.findViewById<TextView>(R.id.channels_available_6GHz_160MHz).text)
            .isEqualTo(convert(WiFiBand.GHZ6, WiFiWidth.MHZ_160))
        assertThat(view.findViewById<TextView>(R.id.channels_available_6GHz_320MHz).text)
            .isEqualTo(convert(WiFiBand.GHZ6, WiFiWidth.MHZ_320))
    }

    private fun convert(wiFiBand: WiFiBand, wiFiWidth: WiFiWidth): String {
        val wiFiChannels = wiFiBand.wiFiChannels
        return wiFiChannels.activeChannels[wiFiWidth]
            .orEmpty()
            .subtract(wiFiChannels.excludeChannels.flatMap { it[countryCode] ?: emptyList() })
            .toList()
            .joinToString(", ")
    }

}

