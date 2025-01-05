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
package com.vrem.wifianalyzer.wifi.channelrating

import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import com.vrem.wifianalyzer.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ChannelRatingAdapterBindingTest {

    @Test
    fun channelRatingAdapterBinding() {
        // setup
        val view: View = mock()
        val channelNumber: TextView = mock()
        val accessPointCount: TextView = mock()
        val channelRating: RatingBar = mock()
        whenever(view.findViewById<TextView>(R.id.channelNumber)).thenReturn(channelNumber)
        whenever(view.findViewById<TextView>(R.id.accessPointCount)).thenReturn(accessPointCount)
        whenever(view.findViewById<RatingBar>(R.id.channelRating)).thenReturn(channelRating)
        // execute
        val fixture = ChannelRatingAdapterBinding(view)
        // validate
        assertThat(fixture.root).isEqualTo(view)
        assertThat(fixture.channelNumber).isEqualTo(channelNumber)
        assertThat(fixture.accessPointCount).isEqualTo(accessPointCount)
        assertThat(fixture.channelRating).isEqualTo(channelRating)
        verify(view).findViewById<View>(R.id.channelNumber)
        verify(view).findViewById<View>(R.id.accessPointCount)
        verify(view).findViewById<View>(R.id.channelRating)
    }

}