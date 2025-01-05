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
import android.view.ViewGroup
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.MainContextHelper.INSTANCE
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.band.WiFiChannelCountry.Companion.find
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config
import java.util.*

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class ChannelAvailableAdapterTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val settings = INSTANCE.settings
    private val currentLocale = Locale.getDefault()
    private val wiFiChannelCountry = find(currentLocale.country)
    private val fixture = ChannelAvailableAdapter(mainActivity, listOf(wiFiChannelCountry))

    @Before
    fun setUp() {
        whenever(settings.languageLocale()).thenReturn(currentLocale)
    }

    @After
    fun tearDown() {
        verify(settings, atLeastOnce()).languageLocale()
        INSTANCE.restore()
    }

    @Test
    fun getView() {
        // setup
        val resources = mainActivity.resources
        val wiFiBand2 = resources.getString(WiFiBand.GHZ2.textResource)
        val wiFiBand5 = resources.getString(WiFiBand.GHZ5.textResource)
        val wiFiBand6 = resources.getString(WiFiBand.GHZ6.textResource)
        val expected = "${wiFiChannelCountry.countryCode()} - ${wiFiChannelCountry.countryName(currentLocale)}"
        val expectedGHZ2 = wiFiChannelCountry.channelsGHZ2().joinToString(",")
        val expectedGHZ5 = wiFiChannelCountry.channelsGHZ5().joinToString(",")
        val expectedGHZ6 = wiFiChannelCountry.channelsGHZ6().joinToString(",")
        val viewGroup = mainActivity.findViewById<ViewGroup>(android.R.id.content)
        // execute
        val actual = fixture.getView(0, null, viewGroup)
        // validate
        assertThat(actual).isNotNull()
        assertThat(actual.findViewById<TextView>(R.id.channel_available_country).text).isEqualTo(expected)
        assertThat(actual.findViewById<TextView>(R.id.channel_available_title_ghz_2).text).isEqualTo("$wiFiBand2 : ")
        assertThat(actual.findViewById<TextView>(R.id.channel_available_ghz_2).text).isEqualTo(expectedGHZ2)
        assertThat(actual.findViewById<TextView>(R.id.channel_available_title_ghz_5).text).isEqualTo("$wiFiBand5 : ")
        assertThat(actual.findViewById<TextView>(R.id.channel_available_ghz_5).text).isEqualTo(expectedGHZ5)
        assertThat(actual.findViewById<TextView>(R.id.channel_available_title_ghz_6).text).isEqualTo("$wiFiBand6 : ")
        assertThat(actual.findViewById<TextView>(R.id.channel_available_ghz_6).text).isEqualTo(expectedGHZ6)
    }

}