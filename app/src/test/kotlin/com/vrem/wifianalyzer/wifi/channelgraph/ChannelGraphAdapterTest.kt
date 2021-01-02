/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2021 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.model.WiFiConnection
import com.vrem.wifianalyzer.wifi.model.WiFiData
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.Q])
class ChannelGraphAdapterTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val channelGraphNavigation: ChannelGraphNavigation = mock()
    private val fixture = ChannelGraphAdapter(channelGraphNavigation)

    @After
    fun tearDown() {
        verifyNoMoreInteractions(channelGraphNavigation)
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun testGetGraphViewNotifiers() {
        // setup
        val expected = WiFiBand.values().map { it.wiFiChannels.wiFiChannelPairs().size }.sum()
        // execute
        val graphViewNotifiers = fixture.graphViewNotifiers()
        // validate
        assertEquals(expected, graphViewNotifiers.size)
    }

    @Test
    fun testGetGraphViews() {
        // setup
        val expected = WiFiBand.values().map { it.wiFiChannels.wiFiChannelPairs().size }.sum()
        // execute
        val graphViews = fixture.graphViews()
        // validate
        assertEquals(expected, graphViews.size)
    }

    @Test
    fun testUpdate() {
        // setup
        val wiFiData = WiFiData(listOf(), WiFiConnection.EMPTY)
        // execute
        fixture.update(wiFiData)
        // validate
        verify(channelGraphNavigation).update()
    }
}