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
@Config(sdk = [Build.VERSION_CODES.BAKLAVA])
class ChannelAvailableFragmentTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val locale = Locale.JAPAN
    private val settings = INSTANCE.settings
    private val fixture = ChannelAvailableFragment()

    private val textViewsIds =
        listOf(
            Triple(R.id.channels_available_2GHz_20MHz, WiFiBand.GHZ2, WiFiWidth.MHZ_20),
            Triple(R.id.channels_available_2GHz_40MHz, WiFiBand.GHZ2, WiFiWidth.MHZ_40),
            Triple(R.id.channels_available_5GHz_20MHz, WiFiBand.GHZ5, WiFiWidth.MHZ_20),
            Triple(R.id.channels_available_5GHz_40MHz, WiFiBand.GHZ5, WiFiWidth.MHZ_40),
            Triple(R.id.channels_available_5GHz_80MHz, WiFiBand.GHZ5, WiFiWidth.MHZ_80),
            Triple(R.id.channels_available_5GHz_160MHz, WiFiBand.GHZ5, WiFiWidth.MHZ_160),
            Triple(R.id.channels_available_6GHz_20MHz, WiFiBand.GHZ6, WiFiWidth.MHZ_20),
            Triple(R.id.channels_available_6GHz_40MHz, WiFiBand.GHZ6, WiFiWidth.MHZ_40),
            Triple(R.id.channels_available_6GHz_80MHz, WiFiBand.GHZ6, WiFiWidth.MHZ_80),
            Triple(R.id.channels_available_6GHz_160MHz, WiFiBand.GHZ6, WiFiWidth.MHZ_160),
            Triple(R.id.channels_available_6GHz_320MHz, WiFiBand.GHZ6, WiFiWidth.MHZ_320),
        )

    @Before
    fun setUp() {
        whenever(settings.countryCode()).thenReturn(locale.country)
        whenever(settings.languageLocale()).thenReturn(Locale.US)
    }

    @After
    fun tearDown() {
        verify(settings, atLeastOnce()).countryCode()
        verify(settings, atLeastOnce()).languageLocale()
        INSTANCE.restore()
    }

    @Test
    fun onCreateView() {
        // setup
        RobolectricUtil.INSTANCE.startFragment(fixture)
        // validate
        assertThat(fixture).isNotNull()
        val view = fixture.view!!
        assertThat(view.findViewById<TextView>(R.id.channels_available_country_code).text).isEqualTo(locale.country)
        assertThat(
            view.findViewById<TextView>(R.id.channels_available_country_name).text,
        ).isEqualTo(locale.displayCountry)
        textViewsIds.forEach { (id, wiFiBand, wiFiWidth) ->
            assertThat(view.findViewById<TextView>(id).text)
                .isEqualTo(
                    wiFiBand.wiFiChannels.availableChannels(wiFiWidth, wiFiBand, locale.country).joinToString(", "),
                )
        }
    }
}
