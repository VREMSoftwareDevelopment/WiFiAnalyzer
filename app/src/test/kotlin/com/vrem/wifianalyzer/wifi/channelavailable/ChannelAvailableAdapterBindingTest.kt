/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2023 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.vrem.wifianalyzer.R
import org.junit.Assert.assertEquals
import org.junit.Test

class ChannelAvailableAdapterBindingTest {

    @Test
    fun testChannelAvailableAdapterBinding() {
        // setup
        val view: View = mock()
        val channelAvailableCountry: TextView = mock()
        val channelAvailableTitleGhz2: TextView = mock()
        val channelAvailableGhz2: TextView = mock()
        val channelAvailableTitle5GHz: TextView = mock()
        val channelAvailable5GHz: TextView = mock()
        val channelAvailableTitle6GHz: TextView = mock()
        val channelAvailable6GHz: TextView = mock()
        whenever(view.findViewById<TextView>(R.id.channel_available_country)).thenReturn(channelAvailableCountry)
        whenever(view.findViewById<TextView>(R.id.channel_available_title_ghz_2)).thenReturn(channelAvailableTitleGhz2)
        whenever(view.findViewById<TextView>(R.id.channel_available_ghz_2)).thenReturn(channelAvailableGhz2)
        whenever(view.findViewById<TextView>(R.id.channel_available_title5_g_hz)).thenReturn(channelAvailableTitle5GHz)
        whenever(view.findViewById<TextView>(R.id.channel_available5_g_hz)).thenReturn(channelAvailable5GHz)
        whenever(view.findViewById<TextView>(R.id.channel_available_title6_g_hz)).thenReturn(channelAvailableTitle6GHz)
        whenever(view.findViewById<TextView>(R.id.channel_available6_g_hz)).thenReturn(channelAvailable6GHz)
        // execute
        val fixture = ChannelAvailableAdapterBinding(view)
        // validate
        assertEquals(view, fixture.root)
        assertEquals(channelAvailableCountry, fixture.channelAvailableCountry)
        assertEquals(channelAvailableTitleGhz2, fixture.channelAvailableTitleGhz2)
        assertEquals(channelAvailableGhz2, fixture.channelAvailableGhz2)
        assertEquals(channelAvailableTitle5GHz, fixture.channelAvailableTitle5GHz)
        assertEquals(channelAvailable5GHz, fixture.channelAvailable5GHz)
        assertEquals(channelAvailableTitle6GHz, fixture.channelAvailableTitle6GHz)
        assertEquals(channelAvailable6GHz, fixture.channelAvailable6GHz)
        verify(view).findViewById<TextView>(R.id.channel_available_country)
        verify(view).findViewById<TextView>(R.id.channel_available_title_ghz_2)
        verify(view).findViewById<TextView>(R.id.channel_available_ghz_2)
        verify(view).findViewById<TextView>(R.id.channel_available_title5_g_hz)
        verify(view).findViewById<TextView>(R.id.channel_available5_g_hz)
        verify(view).findViewById<TextView>(R.id.channel_available_title6_g_hz)
        verify(view).findViewById<TextView>(R.id.channel_available6_g_hz)
    }

}
