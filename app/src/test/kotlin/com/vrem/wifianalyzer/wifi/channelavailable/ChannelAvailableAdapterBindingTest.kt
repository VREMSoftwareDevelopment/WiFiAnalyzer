/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2024 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import android.view.View
import android.widget.TextView
import com.vrem.wifianalyzer.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ChannelAvailableAdapterBindingTest {

    @Test
    fun channelAvailableAdapterBinding() {
        // setup
        val view: View = mock()
        val channelAvailableCountry: TextView = mock()
        val channelAvailableTitleGhz2: TextView = mock()
        val channelAvailableGhz2: TextView = mock()
        val channelAvailableTitleGhz5: TextView = mock()
        val channelAvailableGhz5: TextView = mock()
        val channelAvailableTitleGhz6: TextView = mock()
        val channelAvailableGhz6: TextView = mock()
        whenever(view.findViewById<TextView>(R.id.channel_available_country)).thenReturn(channelAvailableCountry)
        whenever(view.findViewById<TextView>(R.id.channel_available_title_ghz_2)).thenReturn(channelAvailableTitleGhz2)
        whenever(view.findViewById<TextView>(R.id.channel_available_ghz_2)).thenReturn(channelAvailableGhz2)
        whenever(view.findViewById<TextView>(R.id.channel_available_title_ghz_5)).thenReturn(channelAvailableTitleGhz5)
        whenever(view.findViewById<TextView>(R.id.channel_available_ghz_5)).thenReturn(channelAvailableGhz5)
        whenever(view.findViewById<TextView>(R.id.channel_available_title_ghz_6)).thenReturn(channelAvailableTitleGhz6)
        whenever(view.findViewById<TextView>(R.id.channel_available_ghz_6)).thenReturn(channelAvailableGhz6)
        // execute
        val fixture = ChannelAvailableAdapterBinding(view)
        // validate
        assertThat(fixture.root).isEqualTo(view)
        assertThat(fixture.channelAvailableCountry).isEqualTo(channelAvailableCountry)
        assertThat(fixture.channelAvailableTitleGhz2).isEqualTo(channelAvailableTitleGhz2)
        assertThat(fixture.channelAvailableGhz2).isEqualTo(channelAvailableGhz2)
        assertThat(fixture.channelAvailableTitleGhz5).isEqualTo(channelAvailableTitleGhz5)
        assertThat(fixture.channelAvailableGhz5).isEqualTo(channelAvailableGhz5)
        assertThat(fixture.channelAvailableTitleGhz6).isEqualTo(channelAvailableTitleGhz6)
        assertThat(fixture.channelAvailableGhz6).isEqualTo(channelAvailableGhz6)
        verify(view).findViewById<TextView>(R.id.channel_available_country)
        verify(view).findViewById<TextView>(R.id.channel_available_title_ghz_2)
        verify(view).findViewById<TextView>(R.id.channel_available_ghz_2)
        verify(view).findViewById<TextView>(R.id.channel_available_title_ghz_5)
        verify(view).findViewById<TextView>(R.id.channel_available_ghz_5)
        verify(view).findViewById<TextView>(R.id.channel_available_title_ghz_6)
        verify(view).findViewById<TextView>(R.id.channel_available_ghz_6)
    }

}